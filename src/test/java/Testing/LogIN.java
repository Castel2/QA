package Testing;//import com.sun.org.apache.xpath.internal.operations.String;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;



public class LogIN {
    private final Browser browser;
    VerificacionLIMSP verificacionLIMSP;
    private Properties propiedades;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private static Logger logger = LogManager.getLogger(LogIN.class);

    public LogIN() {
        this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
        propiedades = new Properties();
        verificacionLIMSP = new VerificacionLIMSP();
    }

    @Step("Establecer host <host> port <port> dir <dir>")
    /**
     * Establece el HOST y el PORT que se utilizara en el escenario
     */
    public void establecerHostPort(String host, String port, String dir) throws Exception {
        String hostport="";
        dir = "contracts/";
        if ((host.equals("")) && (port.equals(""))) {

            /**
             * HOST y PORT pueden venir por parametro o bien utilizar esta lista de VMs. Es opcional escoger uno u otro modo.
             * Se priorizara el HOST y PORT pasado por parametro
             */
            FileInputStream in = null;
            try {
                in = LatiniaScenarioUtil.readPropertiesLogIN();
                propiedades.load(in);

                hostport = propiedades.getProperty("hostport");
                logger.info("EL HOSTPORT ES: " + hostport);

                // hostport = "192.168.1.10:7003"

                if (hostport.equals("")) {
                    logger.error("No se ha especificado 'host:port'.");
                    throw new Exception("");
                }

                //Separo HOST y PORT para usarlos por separado
                host = hostport.split(":")[0];
                port = hostport.split(":")[1];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Meto estos valores aqui para reutilizarlo en otros escenarios
        datosGlobales.put(Constantes.HOST, host);
        datosGlobales.put(Constantes.PORT, port);
        datosGlobales.put(Constantes.DIR, dir);
    }


    /**
     * Establece las credenciales que utilizara el escenario para entrar en el GUI
     */
    @Step("Establecer login <login> user <user> pass <pass>")
    public void establecerLoginUserPass(String login, String user, String pass) {
        FileInputStream in = null;
        if (login.equals("") & user.equals("") & pass.equals("")){
            logger.info("Tomando del archivo de propiedades LOGIN, USER y PASSWORD");
            try {
                in = LatiniaScenarioUtil.readPropertiesLogIN();
                propiedades.load(in);
                login = propiedades.getProperty("login");
                user = propiedades.getProperty("user");
                pass = propiedades.getProperty("pass");
                String customer = propiedades.getProperty("customer");

                datosGlobales.put(LatiniaUtil.LOGIN_EMPRESA, login); //Meto este valor aqui para reutilizarlo en otros escenarios
                datosGlobales.put(LatiniaUtil.LOGIN_USER, user); //Meto este valor aqui para reutilizarlo en otros escenarios
                datosGlobales.put(LatiniaUtil.LOGIN_PASSWORD, pass); //Meto este valor aqui para reutilizarlo en otros escenarios
                datosGlobales.put(LatiniaUtil.CUSTOMER, customer); //Meto este valor aqui para reutilizarlo en otros escenarios
                verificacionLIMSP.ReadProvision(customer);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

        }else {
            logger.info("Tomando LOGIN, USER y PASSWORD desde parámetros");
            datosGlobales.put(LatiniaUtil.LOGIN_EMPRESA, login); //Meto este valor aqui para reutilizarlo en otros escenarios
            datosGlobales.put(LatiniaUtil.LOGIN_USER, user); //Meto este valor aqui para reutilizarlo en otros escenarios
            datosGlobales.put(LatiniaUtil.LOGIN_PASSWORD, pass); //Meto este valor aqui para reutilizarlo en otros escenarios

            try {
                in = LatiniaScenarioUtil.readPropertiesLogIN();
                propiedades.load(in);
                String customer = propiedades.getProperty("customer");
                datosGlobales.put(LatiniaUtil.CUSTOMER, customer); //Meto este valor aqui para reutilizarlo en otros escenarios
                verificacionLIMSP.ReadProvision(customer);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Accede al GUI de la plataforma, utilizando la URL y las credenciales establecidas anteriormente
     *
     * @throws Exception
     */
    @Step("Cumplimentar Formulario y Login")
    public void cumplimentarFormularioYLogin() throws Exception {

        String host = (String) datosGlobales.get(Constantes.HOST);
        String port = (String) datosGlobales.get(Constantes.PORT);
        String dir = (String) datosGlobales.get(Constantes.DIR);

        /**Los siguientes 'try' sirven para cuando invocamos una URL sin necesidad de logearnos en
         * el GUI con login/user/pass, por ejemplo para el Hist&Summary.
         * Como los 3 campos son null, de este modo evitamos que pete la ejecucion
         */
        Integer nologar = 0;
        String empresa = "";
        String pass = "";
        String user = "";

        try {
            empresa = datosGlobales.get(LatiniaUtil.LOGIN_EMPRESA).toString();
        } catch (Exception e) {
            logger.info("'loginEmpresa' no tiene valor asignado");
            nologar = 1;
        }

        try {
            user = datosGlobales.get(LatiniaUtil.LOGIN_USER).toString();
        } catch (Exception e) {
            logger.info("'loginUser' no tiene valor asignado");
            nologar = nologar + 1;
        }

        try {
            pass = datosGlobales.get(LatiniaUtil.LOGIN_PASSWORD).toString();
        } catch (Exception e) {
            logger.info("'loginpassword' no tiene valor asignado");
            nologar = nologar + 1;
        }


        if ((nologar == 0) || (!dir.contains("contracts"))) {
            logger.info("Iniciando la sesion en " + host + ":" + port + " con Navegador '" + LatiniaUtil.NAVEGADOR);
            if (dir.contains("contracts")) {
                browser.navigateTo("http://" + host + ":" + port + "/" + dir);
                browser.textbox("enterprise").setValue(empresa);
                browser.textbox("username").setValue(user);
                browser.password("j_password").setValue(pass);
            } else {
                browser.navigateTo("http://" + host + "/" + dir);
            }



            logger.info("se ha cumplimentado el formulario");
            if (browser.button("btn-acceder").exists()) {
                logger.info("Click en el botón acceder");
                browser.button("btn-acceder").click();
            }


            if (browser.submit("btn-acceder").exists()) {
                logger.info("Click en el botón submit");
                browser.submit("btn-acceder").click();
            }
            // Para solventar el error conocido que se presenta al iniciar sesión en contracts, se valida la existencia del error y simplemente se da click en "Inicio"
            if(browser.cell("Ha habido algún problema en la operación que se pretendía realizar.").exists()){
                logger.info("Se presenta error conocido \"Ha habido algún problema en la operación que se pretendía realizar.\"");
                if (browser.button("Inicio").exists()) {
                    logger.info("Se procede a dar click en Inicio");
                    browser.button("Inicio").click();
                }
            }


        } else {
            browser.close();
        }

    }

}
