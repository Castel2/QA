package Testing; /**
 * Created by amartinez on 6/02/2017.
 */

import java.lang.Exception;
import java.util.Properties;

import LData_Testing.EjecucionDeProcesos;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import com.sahipro.lang.java.client.ElementStub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InsercionWSJMS {

    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private static Logger logger = LogManager.getLogger(InsercionWSJMS.class);
    VerificacionLIMSP verificacionLIMSP;
    private LatiniaScenarioUtil scenarioUtil;
    private final Browser browser;

    public InsercionWSJMS() {
        //Instanciacion de la clase 'LatiniaScenarioUtil' para utilizar sus metodos
        scenarioUtil = new LatiniaScenarioUtil();
        this.browser = LatiniaUtil.getBrowser();
        verificacionLIMSP = new VerificacionLIMSP();
    }

    /**
     * Ya que el uso de esta herramienta (RGTester) no se hace através del contracts, es decir, se invoca directamente la url, en este método invocamos a esta url
     */
    @Step("Login RGTester")
    public void loginRGTester() {
        String host = (String) datosGlobales.get(Constantes.HOST);
        String port = (String) datosGlobales.get(Constantes.PORT);
        String dir = "limsp-jms-rgtester/";

        browser.navigateTo("http://" + host + ":" + port + "/" + dir);
    }

    /**
     * Hacemos uso de los xml que se encuentra en /recursos, y seteamos los valores necesarios del xml, en este método solo se utilizan xml
     * para mensajes en formato EMAIL
     *
     * @param fname        nombre del xml que tenemos previamente en /recursos
     * @param empresa
     * @param ref_contract
     * @param destinatario
     * @param subject
     * @param content
     */
    @Step("Set XML EMAIL <> <> <> <> <> <>")
    public void setXMLEmail(String fname, String empresa, String ref_contract, String destinatario, String subject, String content) {
        //Lee el contenido del fichero de texto y lo entrega convertido en 'string'
        String data = LatiniaScenarioUtil.readResource("xml/" + fname);
        logger.info("Leido RAW: " + data);
        Properties props = new Properties();
        props.put("loginenterprise", empresa);
        props.put("ref_contract", ref_contract);
        props.put("destinatario", destinatario);
        props.put("subject", subject);
        props.put("content", content);
        //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido del mensaje, asi podre encontralo en Estadisticas
        int randomNum = scenarioUtil.randInt(1, 99999);
        //guardo el valor del 'randomNum' generado para utilizarlo despues en la busqueda de estadisticas
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum);

        props.put("subject", subject + randomNum);
        props.put("content", content + randomNum);
        //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
        data = LatiniaScenarioUtil.replaceDataParams(data, props);
        datosGlobales.put(Constantes.DATA, data);
    }

    /**
     * * Hacemos uso de los xml que se encuentra en /recursos, y seteamos los valores necesarios del xml, en este método solo se utilizan xml
     * para mensajes en formato INOT estableciendo la dirección de email
     *
     * @param fname
     * @param empresa
     * @param ref_contract
     * @param destinatario
     * @param subject
     * @param content
     */
    @Step("Set XML INOT to EMAIL <> <> <> <> <> <> <>")
    public void setXMLEmailInot(String fname, String empresa, String ref_contract, String destinatario, String remitente, String subject, String content) {
        //Lee el contenido del fichero de texto y lo entrega convertido en 'string'
        String data = LatiniaScenarioUtil.readResource("xml/" + fname);
        logger.info("Leido RAW: " + data);
        Properties props = new Properties();
        props.put("loginenterprise", empresa);
        props.put("ref_contract", ref_contract);
        props.put("destinatario", destinatario);
        props.put("remitente", remitente);
        props.put("subject", subject);
        props.put("content", content);
        //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido del mensaje, asi podre encontralo en Estadisticas
        int randomNum = scenarioUtil.randInt(1, 99999);
        //guardo el valor del 'randomNum' generado para utilizarlo despues en la busqueda de estadisticas
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum);

        props.put("subject", subject + randomNum);
        props.put("content", content + randomNum);
        //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
        data = LatiniaScenarioUtil.replaceDataParams(data, props);
        datosGlobales.put(Constantes.DATA, data);
    }


    /**
     * Hacemos uso de los xml que se encuentra en /recursos, y seteamos los valores necesarios del xml, en este método solo se utilizan xml
     * para mensajes en formato SMS
     *
     * @param fname
     * @param empresa
     * @param ref_contract
     * @param operador
     * @param content
     */
    @Step("Set XML SMS <> <> <> <> <> <>")
    public void setXMLSMS(String fname, String empresa, String ref_contract, String operador, String content, String refUser) throws Exception {

        String code = verificacionLIMSP.obtenerCodigoPais();
        String number = verificacionLIMSP.obtenerNumeroVirtual();
        String gsm = "";
        if (operador.equalsIgnoreCase("Virtual")) {
            if (code != null && number != null) {
                gsm = code + number;
            }
        }
        datosGlobales.put(Constantes.GSM, gsm);
        //Lee el contenido del fichero de texto y lo entrega convertido en 'string'
        String data = LatiniaScenarioUtil.readResource("xml/" + fname);
        logger.info("Leido RAW: " + data);
        Properties props = new Properties();
        props.put("loginenterprise", empresa);
        props.put("ref_contract", ref_contract);
        props.put("gsmdest", gsm);

        if (!refUser.equals("")) {
            props.put("refUser", refUser);
        }
        //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido del mensaje, asi podre encontralo en Estadisticas
        int randomNum = scenarioUtil.randInt(1, 99999);
        //guardo el valor del 'randomNum' generado para utilizarlo despues en la busqueda de estadisticas
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum);
        props.put("content", content + randomNum);
        //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
        data = LatiniaScenarioUtil.replaceDataParams(data, props);
        datosGlobales.put(Constantes.DATA, data);
    }

    @Step("Set XML INOT <> <> <> <> <>")
    public void setXMLINOT(String fname, String empresa, String ref_contract, String refUser, String content) {
        //Lee el contenido del fichero de texto y lo entrega convertido en 'string'
        String data = LatiniaScenarioUtil.readResource("xml/" + fname);
        logger.info("Leido RAW: " + data);
        Properties props = new Properties();
        props.put("loginenterprise", empresa);
        props.put("ref_contract", ref_contract);
        props.put("clavevalor", refUser);
        //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido del mensaje, asi podre encontralo en Estadisticas
        int randomNum = scenarioUtil.randInt(1, 99999);
        //guardo el valor del 'randomNum' generado para utilizarlo despues en la busqueda de estadisticas
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum);
        props.put("content", content + randomNum);
        props.put("subject", content + randomNum);
        //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
        data = LatiniaScenarioUtil.replaceDataParams(data, props);
        datosGlobales.put(Constantes.DATA, data);
    }

    /**
     * Hacemos uso de los xml que se encuentra en /recursos, y seteamos los valores necesarios del xml, en este método solo se utilizan xml
     * para mensajes en formato PNS
     *
     * @param fname
     * @param empresa
     * @param ref_contract
     * @param ref_app
     * @param clave
     * @param clavevalor
     * @param content
     */

    @Step("Set XML PNSunitario <> <> <> <> <> <> <>")
    public void setXMLPNSunitario(String fname, String empresa, String ref_contract, String ref_app, String clave, String clavevalor, String content) {
        //Lee el contenido del fichero de texto y lo entrega convertido en 'string'
        String data = LatiniaScenarioUtil.readResource("xml/" + fname);
        logger.info("Leido RAW: " + data);
        Properties props = new Properties();
        props.put("loginenterprise", empresa);
        props.put("ref_contract", ref_contract);
        props.put("pns_ref_app", ref_app);

        if (clave.equalsIgnoreCase("refUser")) {
            //Cuando envio utilizando el refUser, se requiere que el XML no tenga el campo  "KeyName='refUser'"
            props.put("clave", "");
        } else {
            props.put("clave", "KeyName='" + clave + "'");
        }
        props.put("clavevalor", clavevalor);
        //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido del mensaje, asi podre encontralo en Estadisticas
        int randomNum = scenarioUtil.randInt(1, 99999);
        //guardo el valor del 'randomNum' generado para utilizarlo despues en la busqueda de estadisticas
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum);
        props.put("content", content + randomNum);
        props.put("id", randomNum + "");
        //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
        data = LatiniaScenarioUtil.replaceDataParams(data, props);
        datosGlobales.put(Constantes.DATA, data);
    }


    @Step("Insercion JMS <> <> <>")
    public void insercionJMS(String type, String refProduct, String adaptor) {
        String data;
        data = datosGlobales.get(Constantes.DATA).toString();
        if (type.equalsIgnoreCase("email")) {
            if (//Comprueba que el XML leido sea de tipo EMAIL
                    data.contains("<type ref='email'>")) {
                //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
                logger.info("Procesado: " + data);

                sendMessageJMS(data, refProduct, adaptor);
            }

        } else if (type.equalsIgnoreCase("pns")) {
            if (//Comprueba que el XML leido sea de tipo PNS
                    data.contains("<type ref='pns'>")) {
                //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
                logger.info("Procesado: " + data);

                sendMessageJMS(data, refProduct, adaptor);
            }

        } else if (type.equalsIgnoreCase("sms")) {
            if (//Comprueba que el XML leido sea de tipo SMS
                    data.contains("<type ref='sms'>")) {
                //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
                logger.info("Procesado: " + data);

                sendMessageJMS(data, refProduct, adaptor);
            }
        } else if (type.equalsIgnoreCase("inot")) {
            if (//Comprueba que el XML leido sea de tipo INOT
                    data.contains("<type ref='inot'>")) {
                //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
                logger.info("Procesado: " + data);

                sendMessageJMS(data, refProduct, adaptor);
            }
        }
    }

    @Step("Insercion WS <> <> <>")
    public void insercionWS(String type, String refProduct, String adaptor) throws Exception {
        String data;
        data = datosGlobales.get(Constantes.DATA).toString();
        if (type.equalsIgnoreCase("email")) {
            if (//Comprueba que el XML leido sea de tipo EMAIL
                    data.contains("<type ref='email'>")) {
                //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
                logger.info("Procesado: " + data);
                logger.info("Insertando EMAIL por WS");
                LatiniaScenarioUtil.sendMessageWS(refProduct, Constantes.LATINIA, data, adaptor);

            } else {
                logger.error("El fichero leido no contiene un <type ref='email'>");
                throw new Exception("");
            }

        } else if (type.equalsIgnoreCase("pns")) {
            if (//Comprueba que el XML leido sea de tipo PNS
                    data.contains("<type ref='pns'>")) {
                logger.info("Procesado: " + data);
                logger.info("Insertando PNS UNITARIO por WS");
                LatiniaScenarioUtil.sendMessageWS(refProduct, "latinia", data, adaptor);
            } else {
                logger.error("El fichero leido no contiene un <type ref='pns'>");
                throw new Exception("");
            }

        } else if (type.equalsIgnoreCase("sms")) {
            if (//Comprueba que el XML leido sea de tipo SMS
                    data.contains("<type ref='sms'>")) {
                logger.info("Procesado: " + data);
                logger.info("Insertando SMS por WS");
                LatiniaScenarioUtil.sendMessageWS(refProduct, Constantes.LATINIA, data, adaptor);
            } else {
                logger.error("El fichero leido no contiene un <type ref='sms'>");
                throw new Exception("");
            }
        }
        EjecucionDeProcesos procesos = new EjecucionDeProcesos();
        procesos.establecerFechas();
        LatiniaScenarioUtil.sendMessageWS(refProduct, Constantes.LATINIA, data, adaptor);
    }

    /**
     * Enviamos el mensaje a través de la aplicación RGTester, especificacindo el adaptador JMS que se esté probando
     *
     * @param data
     * @param refProduct
     * @param adaptor
     */
    public void sendMessageJMS(String data, String refProduct, String adaptor) {
        ElementStub divText = browser.div("").in(browser.div("ace_line").in(browser.div("ace_layer ace_text-layer").in(browser.div("ace_content").in(browser.div("ace_scroller").in(browser.div("ta_tx_message"))))));
        ElementStub textA = browser.textarea("").in(browser.div("ta_tx_message"));

        if (textA.exists()) {

            textA.setValue(data);

        }
        logger.info("RESULTADO " + divText.getText().toString());

        browser.checkbox("chkbx_refproduct").click();
        browser.textbox("txtbx_refproduct").setValue(refProduct);

        browser.select("jmsQueue").choose("serviceToLimsp/" + adaptor);
        logger.info("Enviando mensaje por el adaptador JMS " + adaptor);


        browser.div("Put").click();
    }
}
