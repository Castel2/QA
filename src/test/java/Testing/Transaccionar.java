package Testing;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//estos imports son para las funciones de trabajo con fechas
import java.io.File;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class Transaccionar {

    private Browser browser;
    GestionContratos gestionContratos;
    VerificacionLIMSP verificacionLIMSP;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private static Logger logger = LogManager.getLogger(Transaccionar.class);

    public Transaccionar() {
        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
        gestionContratos = new GestionContratos();
        verificacionLIMSP = new VerificacionLIMSP();
    }

    @Step("Transaccionar MO DefaultSMS <Default_CualquierCosa>")
    public void transaccionarMODefaultSMS(String string1) throws Exception {
        //Recojo el valor del texto del defaultSMS para luego compararlo
        String canal = "+000001";

        if (browser.link(1).near(browser.cell("+340001")).exists()) {
            canal = "+340001";
        }
        browser.link(1).near(browser.cell(canal)).click();

        String msjxdefecto = browser.textbox("msg").text();


        if (browser.image("button_inicio.gif").exists()) { //Hasta R3.9.3
            browser.image("button_inicio.gif").click();
        }

        if (browser.link("header-btn-home").exists()) { //Desde las primeras releases de R3.9.4
            browser.link("header-btn-home").click();
        }

        if (browser.span("hbtn home").exists()) { //Ultima release de R3.9.4
            browser.span("hbtn home").click();
        }

        if (browser.link("Diagnosis").exists()) {
            browser.link("Diagnosis").click();
        }
        //Voy al simulador de movil para transaccionar una prueba
        browser.link("Simulador de móvil").click();
        String numero = datosGlobales.get(Constantes.COUNTRY_CODE).toString()+datosGlobales.get(Constantes.NUM_VCOLLECTOR).toString();
        if(existennumeros()){
            browser.textarea("contentIn").setValue(string1);
            browser.select("mobileOrg").choose(numero);
            browser.select("refChannelRecv").choose(canal);
            browser.submit("Enviar").click();

            //Recoge el resultado mostrado en pantalla del Simulador de Movil
            String resultado = browser.textarea("contentIn").text();
            if (resultado.equals(msjxdefecto)) {
                //Si el resultado es correcto, continuará sin hacer nada
            } else {
                //Si el resultado NO es correcto, devuelve error
                logger.error("Resultado incorrecto. No ha devuelto el mensaje esperado. Revisa provision");
                throw new Exception("");
            }
        }
    }


    //Este metodo verifica la existencia de los numeros obtenidos del provision-naming contra los disponibles en el simulador movil
    //Valida que tambien la misma cantidad de numeros provisionados en el XML con los disponibles en el simulador movil.
    public boolean existennumeros() throws Exception {
        boolean existe =false;
        LinkedList listcollector = (LinkedList) datosGlobales.get(Constantes.LISTCOLLECTOR);
        int valueoption = browser.option("/.*/").in(browser.select("mobileOrg")).countSimilar();
        if(valueoption == listcollector.size()){
            logger.info("Igual numeros provisionados en XML y encontrados en herramienta diagnosis");
            for (int i=0;i < valueoption;i++){
                if(browser.option(""+datosGlobales.get(Constantes.COUNTRY_CODE)+listcollector.get(i)).in(browser.select("mobileOrg")).exists()){
                    logger.info("Existe el numero de collector: " +  datosGlobales.get(Constantes.COUNTRY_CODE)+listcollector.get(i));
                }else{
                    logger.error("NO existe el numero de collector: " +  datosGlobales.get(Constantes.COUNTRY_CODE)+listcollector.get(i));
                    throw new Exception("");
                }
            existe = true;
            }
        }else{
            existe = false;
            logger.error("Existen mas numeros que los provisionados en el provision_naming.xml");
            throw new Exception("");
        }
        return existe;
    }

    @Step("Transaccionar EnviosSMS <> <>")
    public void transaccionarEnviosSMS(String operador, String texto) throws Exception {
        String code = verificacionLIMSP.obtenerCodigoPais();
        String number = verificacionLIMSP.obtenerNumeroVirtual();
        String gsm ="";
        if (operador.equalsIgnoreCase("Virtual")) {
            if (code != null && number != null) {
                gsm = code + number;
            }
        } else if (operador.equalsIgnoreCase("unknown")) {
            gsm = code + "123456789";
        }
        datosGlobales.put(Constantes.GSM,gsm);

        browser.textbox("Numero").setValue(gsm);
        browser.textbox("Texto").setValue(texto);
        browser.submit("Enviar").click();
        browser.button("<< Volver").click();
    }

    @Step("Transaccionar EnviosSMS Diferido <+34659000001> <Diferido Virtual>")
    public void transaccionarEnviosSMSDiferido(String operador, String texto) throws Exception {
        String code = verificacionLIMSP.obtenerCodigoPais();
        String number = verificacionLIMSP.obtenerNumeroVirtual();
        String gsm = "";
        if (operador.equalsIgnoreCase("virtual")) {
            if (code != null && number != null) {
                gsm = code + number;
                browser.textbox("Numero").setValue(gsm);
            }
        } else if (operador.equalsIgnoreCase("unknown")) {
            gsm = code + "123456789";
            browser.textbox("Numero").setValue(gsm);
        }

        browser.checkbox("show_scheduler").click();
        browser.select("delivery").choose("20 minutos");
        browser.select("deliveryEnd").choose("30 minutos");
        browser.select("expire").choose("1 hora");
        browser.textbox("Numero").setValue(gsm);
        browser.textbox("Texto").setValue(texto);
        browser.submit("Enviar").click();
        browser.button("<< Volver").click();
    }


    public void transaccionarEnviosSMSExpirado(String string1, String string2) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date hoy = new Date(); //obtengo la fecha de hoy y la meto en 'hoy'
        Calendar c = Calendar.getInstance(); //creo un objeto calendar llamado c
        c.setTime(hoy); //establezco en calendar la fecha de hoy
        c.add(Calendar.DATE, -1); //en el calendar me voy 1 dia atrás
        hoy.setTime(c.getTime().getTime()); //meto en la variable hoy la hora y minutos del calendario

        int dia = c.get(Calendar.DATE);
        int mes = c.get(Calendar.MONTH);
        mes = mes + 1; //sumo 1 porque enero es el mes 0
        int anyo = c.get(Calendar.YEAR);

        browser.checkbox("show_scheduler").check();


        logger.info("Estableciendo la fecha de ayer: " + dateFormat.format(hoy.getTime()));
        if (mes <= 9) {
            browser.textbox("ddi").setValue(dia + "/0" + mes + "/" + anyo);
            browser.textbox("dedi").setValue(dia + "/0" + mes + "/" + anyo);
        } else {
            browser.textbox("ddi").setValue(dia + "/" + mes + "/" + anyo);
            browser.textbox("dedi").setValue(dia + "/" + mes + "/" + anyo);
        }

        browser.select("expire").choose("1440");
        browser.textbox("Numero").setValue(string1);
        browser.textbox("Texto").setValue(string2);
        browser.submit("Enviar").click();
        browser.button("<< Volver").click();

    }

    @Step("Transaccionar EnvioEmail <> <EtiquetaEmail> <> <HomeLimsp.png>")
    public void transaccionarEnvioEmail(String TO, String etiqueta, String plantilla, String adjunto) throws Exception {
        String initPath = new File("").getAbsolutePath();
        String fullPath = null;
        if(initPath.contains("\\")){//WINDOWS
             fullPath = initPath.substring(0, initPath.lastIndexOf("\\"));
        }else if(initPath.contains("/")){//LINUX
             fullPath = initPath.substring(0, initPath.lastIndexOf("/"));
        }
        logger.info("Path del proyecto:" + fullPath);
        String titulo = datosGlobales.get(Constantes.TITULO).toString();
        String texto = datosGlobales.get(Constantes.TEXTO).toString();
        String idioma = datosGlobales.get(Constantes.IDIOMA).toString();
        String parametro = datosGlobales.get(Constantes.PARAMETRO).toString();
        String valor = datosGlobales.get(Constantes.VALOR).toString();

        browser.textbox("To").setValue(TO);
        browser.textbox("Subject").setValue(titulo);
        browser.textarea("Texto").setValue(texto);
        if (!etiqueta.equals(" ")) {
            browser.textbox("label").setValue(etiqueta);
        }
        if (!plantilla.equals("")) {
            browser.select("selectedTemplate").choose(plantilla);
        }

        if(!parametro.equals("parametro") && !valor.equals("valor")){
            browser.textbox("_TEMVAR_"+parametro).in(browser.div(plantilla)).setValue(valor);
        }
        if (!idioma.equals("--")) {
            logger.info("idioma: " + idioma);
            logger.info("plantilla: " + plantilla);
            browser.select("_TEMVAR_lang").in(browser.div(plantilla)).choose(idioma); //Utilizo 'in' porque el under no funciona.
        }
        if (!adjunto.equals(" ")) {

            browser.file("file_0").setFile(fullPath + "/Util/Archivos/" + adjunto);
        }

        browser.submit("[0]").click();
        if (!browser.button("<< Volver").exists() || (!browser.cell("Mensaje enviado").exists())) {
            logger.error("pulsando el boton de enviar el Email (action=\"send.jsp\")");
            throw new Exception("");
        } else {
            browser.button("<< Volver").click();
        }

    }

    @Step("Transaccionar EnvioPNS <> <> <> <> <> <>")
    public void transaccionarEnvioPNS(String Contrato, String KeyName, String KeyValue, String Textopub, String Textopriv, String Etiqueta) throws Exception {
        browser.select("id_contract2").choose(Contrato);
        browser.select("KeyName").choose(KeyName);
        browser.textbox("KeyValue").setValue(KeyValue);
        browser.textbox("Texto").setValue(Textopub);
        browser.textbox("TextoPrivado").setValue(Textopriv);
        browser.textbox("Etiqueta").setValue(Etiqueta);
        browser.submit("[0]").click();
    }

    @Step("Transaccionar EnvioTweet virtual <user>")
    public void transaccionarEnvioTweetVirtual(String tweetname) throws Exception {

        if (browser.link("Diagnosis").exists()) {
            browser.link("Diagnosis").click();
        }

        browser.link("Envíos a Twitter").click();
        browser.select("KeyName").choose("twitter_name");
        browser.textbox("KeyValue").setValue(tweetname);
        browser.textbox("Texto").setValue("Envio Tweet Automated");
        browser.textbox("Etiqueta").setValue("EtiquetaTweet");
        browser.submit("[0]").click();
    }

    @Step("ComprobarClausulas <Operadora>")
    public void comprobarClausulas(String Operadora) throws Exception {
        int cambios = 0;
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        //String ref = datosGlobales.get("refcontract").toString();
        String nomcontrato = datosGlobales.get(Constantes.NOM_CONTRATO).toString();
        String tipoclause = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();


        if (browser.image("ico_contratos.gif").rightOf(browser.cell(empresa.toUpperCase())).exists()) {
            browser.image("ico_contratos.gif").rightOf(browser.cell(empresa.toUpperCase())).click();
        }
        browser.image("ico_clausulas.gif").rightOf(browser.cell(nomcontrato)).click();

        //Caso de verificar clausulas tipo SMS
        if (tipoclause.equalsIgnoreCase("sms")) {
            //Si la clausula ya existe no hago nada, y si no, la creo. Excepto la clausula 'Virtual' que la creo siempre
            if (browser.cell(Operadora).exists()) {
                if (Operadora.equals("Virtual")) {
                    browser.image("button_create_clauses.gif").click();
                    browser.button("Siguiente >>>").click();

                    browser.select("channel").choose(new String[]{Operadora + " - +000001 Virtual"});
                    browser.button("Crear").click();

                    browser.expectConfirm("¿Desea crear la cláusula?", true);
                    browser.button("/Volver/").click();

                    cambios = 1; //utilizo este flag para forzarrecarga
                }

            } else {
                browser.image("button_create_clauses.gif").click();
                browser.button("Siguiente >>>").click();

                browser.select("channel").choose(new String[]{Operadora + " - +000001 Virtual"});
                browser.button("Crear").click();

                browser.expectConfirm("¿Desea crear la cláusula?", true);
                browser.button("/Volver/").click();

                cambios = 1; //utilizo este flag para forzarrecarga
            }
        }

        //Caso de verificar clausulas tipo PNS
        if (tipoclause.equalsIgnoreCase("pns")) {
            //Si la clausula ya existe no hago nada, y si no, la creo.
            if (browser.cell(Operadora).exists()) {

            } else {
                browser.image("button_create_clauses.gif").click();
                browser.submit("Siguiente >>>[1]").click();

                browser.select("channel").choose(new String[]{Operadora + " - * Virtual"});
                browser.button("Crear").click();

                browser.expectConfirm("¿Desea crear la cláusula?", true);
                browser.button("/Volver/").click();

                cambios = 1; //utilizo este flag para forzarrecarga
            }
        }

        //Caso de verificar clausulas tipo EMAIL
        if (tipoclause.equalsIgnoreCase("email")) {
            //Si la clausula ya existe no hago nada, y si no, la creo.
            if (browser.cell(Operadora).exists()) {

            } else {
                browser.image("button_create_clauses.gif").click();
                browser.submit("Siguiente >>>[0]").click();

                browser.select("channel").choose(new String[]{Operadora + " - comercial@latinia.com EMail-SMTP"});
                browser.button("Crear").click();

                browser.expectConfirm("¿Desea crear la cláusula?", true);
                browser.button("/Volver/").click();

                cambios = 1; //utilizo este flag para forzarrecarga
            }
        }
        //Caso de proceder con descarte de mensajes, lo que hacemos es eliminar clausulas para 'unknown'
        if (tipoclause.equalsIgnoreCase("Descartar")) {
            //Si existen clausulas las borro, para que así se descarten los mensajes
            if (browser.cell(Operadora).exists()) {
                browser.image("ico_delete.gif").near(browser.cell(Operadora)).click();
                browser.expectConfirm("¿Desea eliminar la cláusula/", true);
                if (browser.button("/Caducar/").exists()) {
                    browser.button("/Caducar/").click();
                }
                browser.button("/Volver/").click();
                cambios = 1; //utilizo este flag para forzarrecarga
            }
        }

        if (cambios == 1) {
            if (browser.span("hbtn home").exists()) { //Ultima release de R3.9.4
                browser.span("hbtn home").click();
            }
            browser.link("Forzar recarga").click();
            browser.button("Volver").click();
        }


    }


}
