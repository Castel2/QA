package Testing;

import LData_Testing.*;
import com.latinia.limsp.ldata.pstoragefacade.ws.Ws_ld_pstoragePortStub;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import com.latinia.util.lxobjects.LXList;
import com.latinia.util.lxobjects.LXObject;
import com.latinia.util.lxobjects.LXSerializer;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import com.sahipro.lang.java.client.ElementStub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//estos imports son para las funciones de trabajo con fechas
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class DetalleDeTransacciones {

    private Browser browser;
    private static Logger logger = LogManager.getLogger(DetalleDeTransacciones.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    LatiniaScenarioUtil latiniaScenarioUtil;
    VerificacionLIMSP verificacionLIMSP;
    GestionContratos gestionContratos;
    MContract mContract;
    MProduct mProduct;
    ProvisionerAdminUser provisionerAdminUser;
    InsercionWSJMS insercionWSJMS;
    AlmacenDeDatos almacenDeDatos;
    Navegacion navegacion;
    AccesoWSLData accesoWSLData;

    public DetalleDeTransacciones() {
        this.latiniaScenarioUtil = new LatiniaScenarioUtil();
        this.verificacionLIMSP = new VerificacionLIMSP();
        this.gestionContratos = new GestionContratos();
        this.mContract = new MContract();
        this.mProduct = new MProduct();
        this.provisionerAdminUser = new ProvisionerAdminUser();
        this.insercionWSJMS = new InsercionWSJMS();
        this.almacenDeDatos = new AlmacenDeDatos();
        this.navegacion = new Navegacion();
        this.accesoWSLData = new AccesoWSLData();
        this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser

    }

    @Step("Ver listado <Envíos SMS>")
    public void verListado(String contrato) throws Exception {

        browser.link("Filtro").click();
        browser.radio("sel_service[1]").click();
        browser.select("idx_contract").choose("/(.*)" + contrato + "(.*)/");
        browser.submit("Aceptar").click();

    }

    @Step("Exportacion <Exportacion Automática con Automated>")
    public void exportacion(String string1) throws Exception {
        if (!browser.button("Exportar").exists()) {
            browser.link("Listado").click();
        }
        browser.button("Exportar").click();
        browser.textbox("exportFileName").setValue(string1);
        browser.submit("Exportar").click();
    }


    /**
     * En 'detalle de transacciones' reviso si el adjunto del Email enviado está allí.
     * Utilizo esta función despues de probar el envio de un Email con adjuntos
     *
     * @throws Exception
     * @author "@xruizs"
     */
    @Step("Ver Adjunto")
    public void verAdjunto() throws Exception {

        String titulo = datosGlobales.get(Constantes.TITULO).toString();
        String texto = datosGlobales.get(Constantes.TEXTO).toString();

        if (browser.image("icon-undef1.gif").rightOf(browser.cell(titulo)).exists()) {
            browser.image("icon-undef1.gif").rightOf(browser.cell(titulo)).click();
        }
        if (browser.image("icon-ok.gif").rightOf(browser.cell(titulo)).exists()) {
            browser.image("icon-ok.gif").rightOf(browser.cell(titulo)).click();
        }

        if (browser.div(texto).exists()) {
            //Si existe en estadísticas el contenido del Email no hago nada
        } else {
            logger.error("No veo en estadísticas el texto del Email enviado", new Exception());
            throw new Exception(" ");
        }


        browser.image("icon_clip.gif").click();

        if (browser.link("homelimsp.png").exists() || browser.div("homelimsp.png").exists() || browser.link("HomeLimsp.png").exists() || browser.div("HomeLimsp.png").exists()) {
            //no hago nada simplemente compruebo que allí está el adjunto
            //El IF tiene 4 condiciones porque busca con mayúsculas y minusculas puesto que depende qué versión de Limsp lo pasa todo a 'lowercase' mientras que otras Limsp no lo hacen así.
        } else {
            logger.error("No veo el adjunto del Email en las estadísticas", new Exception());
            throw new Exception(" ");
        }

    }

    /**
     * En 'detalle de transacciones' reviso si la plantilla adjunta al Email es correcta.
     * Utilizo esta función despues de probar el envio de un Email con plantillas
     *
     * @throws Exception
     * @author "@xruizs"
     */
    @Step("Ver Plantilla")
    public void verPlantilla() throws Exception {
        String titulo = datosGlobales.get(Constantes.TITULO).toString();
        String texto = datosGlobales.get(Constantes.TEXTO).toString();
        String valor = datosGlobales.get(Constantes.VALOR).toString();
        String parametro = datosGlobales.get(Constantes.PARAMETRO).toString();

        String tipofuncion = "RIGHTLEFT";

        if (titulo.contains("RIGHT")) {
            tipofuncion = "RIGHTLEFT";
        } else if (titulo.contains("IMG")) {
            tipofuncion = "IMG";
        } else if (titulo.contains("EQUALS")) {
            tipofuncion = "EQUALS";
        }else if (titulo.contains("EMOJIS")){
            tipofuncion =  "EMOJIS";
        }else if (!titulo.contains("IMG") && !titulo.contains("RIGHT")) {
            tipofuncion = "--";
        }else if (titulo.contains("URLEnconde")){
            tipofuncion =  "URLEnconde";
        }

        if (browser.image("icon-undef1.gif").rightOf(browser.cell(titulo + "[0]")).exists()) {
            browser.image("icon-undef1.gif").rightOf(browser.cell(titulo + "[0]")).click();
        }
        if (browser.image("icon-ok.gif").rightOf(browser.cell(titulo + "[0]")).exists()) {
            browser.image("icon-ok.gif").rightOf(browser.cell(titulo + "[0]")).click();
        }

        if (!browser.cell("/" + titulo + "/").exists()) {
            logger.error("No veo en estadísticas el ASUNTO del Email enviado", new Exception());
            throw new Exception(" ");
        }
        if(parametro.equals("parametro")){
            logger.info("No se valida parametro");
            logger.info("BUG SDP-894 de emojis superado");
        }else if(browser.div(parametro +" = " + valor).exists()){
            logger.info("Carga correctamente de emojis en detalle de mensaje e-mail");
            logger.info("BUG SDP-894 de emojis superado");
        }else{
            logger.error("Carga incorrecta de emojis en detalle de mensaje e-mail");
            throw new Exception();
        }

        browser.image("icon_view_content.gif").click();
        Browser templWin = browser.popup("_new"); //Obtenemos acceso a la nueva ventana. Para ello instancio la clase Browser en el objeto templWin, a la cual sahi obtiene acceso
        //Compruebo el contenido de la plantilla en caso de probar las funciones RIGHT LEFT
        if (tipofuncion.equals("RIGHTLEFT")) {
            logger.info("Comprobando funciones RIGHT o LEFT en la ventana de popup");
            if (templWin.containsText(templWin.div("contenido"), "ERROR")) { //Trabajo con el objeto templWin que es la venta de popup
                logger.error("La funcion RIGHT o LEFT de plantillas no funciona", new Exception());
                throw new Exception(" ");
            } else {
                logger.info("BIEN! Las funciones RIGHT o LEFT de plantillas funcionan");
            }
        }
        //Compruebo el contenido de la plantilla en caso de probar las funciones IMG
        else if (tipofuncion.equals("IMG")) {
            logger.info("Comprobando la funcion IMG en la ventana de popup");
            try {
                ElementStub imagen = templWin.image("[0]").in(templWin.div("contenido")); //Trabajo con el objeto templWin que es la venta de popup
                if (!imagen.getAttribute("src").startsWith("cid")) { //Leo el atributo 'src' de la imagen
                    logger.error("La funcion IMG de plantillas no funciona", new Exception());
                    throw new Exception(" ");
                } else {
                    logger.info("BIEN! La funcion IMG de plantillas funciona");
                }
            } catch (Exception e) {
                logger.error("No se encuentra la imagen en la plantilla. Revisa que la imagen esta en el BinaryStore. Pejen WAS: /J2EE/IBM/WebSphere/AppServer/profiles/AppSrv01/extern/innovus/img", new Exception());
                throw new Exception(" ");
            }
        } else if (tipofuncion.equals("EQUALS")) {
            logger.info("Comprobando funcion EQUALS en la ventana de popup");
            if (templWin.containsText(templWin.div("contenido"), "OK")) { //Trabajo con el objeto templWin que es la venta de popup
                logger.info("BIEN! Las funcion EQUALS de plantillas funciona");
            } else {
                logger.error("La funcion EQUALS de plantillas no funciona", new Exception());
                throw new Exception(" ");
            }
        } else if (tipofuncion.equals("--")) {
            logger.info("Comprobando el contenido de la plantilla en la ventana de popup");
            try {
                if (templWin.containsText(templWin.div("contenido"), texto)) { //Trabajo con el objeto templWin que es la venta de popup
                    logger.info("BIEN! Se encuentra el contenido en la plantilla");
                }
            } catch (Exception e) {
                logger.error("no se encuentra el contenido en la plantilla", new Exception());
                throw new Exception(" ");
            }

        }else if (tipofuncion.equals("URLEnconde")) {
            logger.info("Comprobando el contenido de la plantilla en la ventana de popup");
            try {
                if (templWin.containsText(templWin.div("contenido"), " %2FTZlwXl8ZYyIh4GGv%2FQR8UnL0k4%3D")) { //Trabajo con el objeto templWin que es la venta de popup
                    logger.info("BIEN! La función URLEnconde funciona");
                }
            } catch (Exception e) {
                logger.error("a funcion URLEnconde de plantillas no funciona", new Exception());
                throw new Exception(" ");
            }

        } else if (tipofuncion.equals("EMOJIS")) {
            logger.info("Comprobando el contenido de Emojis de la plantilla en la ventana de popup");
            if (templWin.containsText(templWin.div("contenido[0]"), "Probando variables de emojis: " + valor) && templWin.containsText(templWin.div("contenido[1]"), "Probando variables de emojis: \uD83D\uDE0D \uD83E\uDD2A \uD83E\uDD13\uD83D\uDE0E")) {
                logger.info("BIEN! La función EMOJIS funciona");
            }else{
                logger.error("No contiene el contenido de EMOJIS");
                throw new Exception("");
            }
        }
        logger.info("Cerrando ventana templWin");
//        templWin.close();
    }

    /**
     * Comprueba que en el detalle de un SMS existe el estado indicado.
     * Tambien comprueba la existencia de los campos esperados en el detalle de transacciones SMS y Email
     *
     * @param estado
     * @throws Exception
     * @author "@xruizs"
     */
    @Step("Ver estados <estado>")
    public void verEstados(String estado) throws Exception {
        //Verifico si la entrada "config/bus/collectors/vcollector/mode" está en BUS o por default GUI

        String valProperty = "";
        String contrato = "";
        String randomNum = "vacio";

        try {
            if (latiniaScenarioUtil.leerPropiedadesLConfig("lmod-vcollector.properties", "config/bus/collectors/vcollector/mode") == null) {
                valProperty = "null";
            } else {
                valProperty = latiniaScenarioUtil.leerPropiedadesLConfig("lmod-vcollector.properties", "config/bus/collectors/vcollector/mode");
            }
            try {
                contrato = datosGlobales.get(Constantes.CRITERIO).toString();
            } catch (Exception e) {
            }

            try {
                randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString(); //Es el numero random que guardamos para poder encontrarlo ahora en las Estadisticas
            } catch (Exception e) {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Preparo la fecha actual en el formato adecuado dd/MM/yyyy HH:mm:ss
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        //Obtengo la fecha de hoy y la meto en 'hoy'
        Date ahora = new Date();

        String fecha0 = dateFormat.format(ahora.getTime());
        //String fecha1 = dateFormat.format(ahora.getTime() - 1 * 60 *1000); //A la fecha actual le resto 1 minutos porque voy a verificar en estadisticas las transacciones generadas hace menos de 1 minutos


        if (contrato.equalsIgnoreCase("No.Debe.Salir")) {
            if (browser.cell(contrato).exists()) {
                logger.error("Se ha encontrado BUG: 'SDP-950' en el envio de PNS masivos");
                throw new Exception(" ");
            }
        }


        //Entro en el detalle de la transaccion
        if (!estado.equals("Rechazado por itinerancia")) {


            if (browser.image("icon-ok2.png[0]").rightOf(browser.cell("ws1.probando estado MO")).exists()) {
                browser.image("icon-ok2.png[0]").rightOf(browser.cell("ws1.probando estado MO")).click();
            }

            //Para ver el detalle de un mensaje concreto
            if (randomNum.equals("vacio")) { //En el caso que no haya utilizado un 'uuid' en la composicion del mensaje
                if (browser.image("icon-ok2.png[0]").rightOf(browser.cell(contrato + "[0]")).exists()) {
                    browser.image("icon-ok2.png[0]").rightOf(browser.cell(contrato + "[0]")).click();
                } else if (browser.image("icon-undef1.gif").rightOf(browser.cell(contrato + "[0]")).exists()) {
                    browser.image("icon-undef1.gif").rightOf(browser.cell(contrato + "[0]")).click();
                } else if (browser.image("icon-ok.gif").rightOf(browser.cell(contrato + "[0]")).exists()) {
                    browser.image("icon-ok.gif").rightOf(browser.cell(contrato + "[0]")).click();
                } else if (browser.image("icon-undef2.png[0]").rightOf(browser.cell(contrato + "[0]")).exists()) {
                    browser.image("icon-undef2.png[0]").rightOf(browser.cell(contrato + "[0]")).click();
                } else if (browser.image("icon-err2.png[0]").rightOf(browser.cell(contrato + "[0]")).exists()) {
                    browser.image("icon-err2.png[0]").rightOf(browser.cell(contrato + "[0]")).click();
                }
            } else {

                logger.info("Entrando detalle de mensaje para random: " + randomNum);

                if (browser.image("icon-ok2.png[0]").rightOf(browser.cell("/(.*)" + randomNum + "(.*)/")).exists()) {
                    browser.image("icon-ok2.png[0]").rightOf(browser.cell("/(.*)" + randomNum + "(.*)/")).click();
                } else if (browser.image("icon-undef1.gif").rightOf(browser.cell("/(.*)" + randomNum + "(.*)/")).exists()) {
                    browser.image("icon-undef1.gif").rightOf(browser.cell("/(.*)" + randomNum + "(.*)/")).click();
                } else if (browser.image("icon-ok.gif").rightOf(browser.cell("/(.*)" + randomNum + "(.*)/")).exists()) {
                    browser.image("icon-ok.gif").rightOf(browser.cell("/(.*)" + randomNum + "(.*)/")).click();
                } else if (browser.image("icon-undef2.png[0]").rightOf(browser.cell("/(.*)" + randomNum + "(.*)/")).exists()) {
                    browser.image("icon-undef2.png[0]").rightOf(browser.cell("/(.*)" + randomNum + "(.*)/")).click();
                } else if (browser.image("icon-err2.png[0]").rightOf(browser.cell("/(.*)" + randomNum + "(.*)/")).exists()) {
                    browser.image("icon-err2.png[0]").rightOf(browser.cell("/(.*)" + randomNum + "(.*)/")).click();
                }
            }

            logger.info("Comprobando estados");

            if (valProperty.equalsIgnoreCase("BUS")) {
                comprobarEstados(estado, contrato, randomNum);
            } else if (valProperty.equalsIgnoreCase("GUI") || valProperty.equals("null")) {
                if (estado.equals("Entrega confirmada a usuario")) {
                    if (browser.div(estado).exists()) {
                        logger.error("ERR: El estado " + estado + " no debería existir si la entrada \"config/bus/collectors/vcollector/mode\" = GUI");
                        throw new Exception(" ");
                    }
                } else {
                    comprobarEstados(estado, contrato, randomNum);
                }
            }


            //Compruebo que existen los campos esperados en el 'detalle de la transaccion' de tipo SMS
            if (browser.image("icon-detail-sms.gif").exists()) {
                if (!browser.div("content_title").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'content_title'");
                    throw new Exception(" ");
                }
                if (!browser.cell("ID Transacción:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'ID Transaccion:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Tiempo:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Tiempo:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Consolidación:").exists()){
                    logger.error("'Detalle de transacciones', No existe el campo 'Consolidacion:'");
                   throw new Exception(" ");
                }
                if (!browser.cell("Empresa:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Empresa:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Contrato/Servicio:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Contrato/Servicio:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Aplicación/Producto:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Aplicacion/Producto:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("ID Mensaje:").exists()) {
                    logger.error("No existe 'ID Mensaje:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Etiqueta:").exists()){
                    logger.error("No existe 'Etiqueta:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Operadora:").exists()) {
                    logger.error("No existe 'Operadora:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Canal:").exists()){
                    logger.error("No existe 'Canal:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Teléfono:").exists()){
                    logger.error("No existe 'Telefono:'");
                    throw new Exception(" ");
                }
            }

            //Compruebo que existen los campos esperados en el 'detalle de la transaccion' de tipo PNS
            if (browser.image("icon-detail-pns.gif").exists()) {
                if (!browser.div("content_title").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'content_title'");
                    throw new Exception(" ");
                }
                if (!browser.cell("ID Transacción:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'ID Transaccion:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Tiempo:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Tiempo:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Consolidación:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Consolidacion:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Empresa:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Empresa:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Contrato/Servicio:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Contrato/Servicio:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Aplicación/Producto:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Aplicacion/Producto:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("ID Mensaje:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'ID Mensaje:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Etiqueta:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'ID Mensaje:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Proveedor:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Operadora:'");
                    throw new Exception(" ");
                }
                if (!browser.cell("Destinatario:").exists()) {
                    logger.error("'Detalle de transacciones', No existe el campo 'Destinatario:'");
                    throw new Exception(" ");
                }
                if (datosGlobales.get("PNS_DOWNLOADABLE") != null) {
                    if (!datosGlobales.get("PNS_DOWNLOADABLE").toString().equalsIgnoreCase("false")) {
                        //*BUG:SDP-949*
                        if (!browser.cell("M-App").exists()) {
                            logger.error("*BUG:SDP-949* No existe el campo con el nombre de la 'M-App' en 'Detalle de Transacciones'");
                            throw new Exception(" ");
                        }

                        if (!browser.cell("UUID").exists())
                            logger.error("'Detalle de transacciones', No existe el campo 'UUID:'");
                        if (!browser.cell("Device").exists())
                            logger.error("'Detalle de transacciones', No existe el campo 'Device:'");
                    }
                }
            }


            if (!randomNum.equals("vacio")) {
                if (!browser.div("/(.*)" + randomNum + "(.*)/").exists()) {
                    logger.error("No se encuentra el rand:'" + randomNum + "' en el contenido del mensaje");
                    throw new Exception(" ");
                }
            }

        } else {
            //Para el caso de comprobar un rechazado por itinerancia
            //ElementStub primermsj = browser.cell("lili[0]").leftOf(browser.cell(contrato + "[0]")).leftOf(browser.cell(fecha));

            ElementStub primermsj = browser.cell("lili[0]").leftOf(browser.cell(contrato + "[0]"));
            ElementStub segundomsj = browser.cell("lili[0]").leftOf(browser.cell(contrato + "[1]"));
            ElementStub tercermsjyrechazado = browser.cell("lili[0]").leftOf(browser.cell(contrato + "[2]"));

            if (browser.exists(primermsj) && browser.exists(segundomsj) && browser.exists(tercermsjyrechazado)) {
                logger.info("Detectados los tres (3) mensajes");
                browser.image("icon-err2.png").rightOf(tercermsjyrechazado).click(); //Busco el icono de 'error' a la derecha del tercero, puesto que el msj debe estar rechazado
                if (browser.div(estado).exists()) {
                    //Si existe el estado 'Rechazado por roaming', no hago nada
                    logger.info("Correcto: El estado '" + estado + "' existe para " + contrato);
                } else {
                    logger.error("No existe en estadísticas el estado '" + estado + "' para " + contrato);
                    throw new Exception(" ");
                }
            } else {
                logger.error("No se encuentra en las estadiísticas el 'segundopns'");
                throw new Exception(" ");
            }
        }


    }

    public void comprobarEstados(String estado, String contrato, String randomNum) throws Exception {
        boolean completado = false;
        int segundos = 5;

        while (!completado) {

            if (browser.div(estado).exists()) {
                //Si existe el estado indicado no hago nada
                logger.info("Resultado correcto: El estado '" + estado + "' existe para " + contrato + " rand:'" + randomNum + "'");
                completado = true;
            } else {

                if (segundos == 1)
                           logger.info("Esperando estados para random: " + randomNum + "'");
                if (segundos % 200 == 0) { //Cada 100 segundos baja de linea. Solo confines visuales en la consola

                    logger.info(" ");
                }
                if (segundos >= 1800) { //Esto es un timeOut por si jamas aparecen estados, entonces termino el programa

                    System.out.println("");
                    logger.info("");
                    logger.error("No existe en estadísticas el estado '" + estado + "' para " + contrato + " rand:'" + randomNum + "'");
                    throw new Exception(" ");
                }
                browser.navigateTo("", true); //Refresco a ver si aparecen las estados
                Thread.sleep(5000); //Espero 5sec para dar tiempo a que carge
                if (segundos == 5) {
                    logger.info("Esperando " + segundos + ",");
                } else {
                    logger.info(segundos + ",");
                }
                segundos = segundos + 5;
            }
        }
    }

    /**
     * Comprueba el detalle de una PNS
     *
     * @throws Exception
     * @author "@xruizs"
     */
    public void verMApp(String mApp) throws Exception {
        String contrato = "";
        String randomNum = "vacio";

        try {
            contrato = datosGlobales.get(Constantes.NOM_CONTRATO).toString();
        } catch (Exception e) {

        }

        try {
            randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString(); //Es el numero random que guardamos para poder encontrarlo ahora en las Estadisticas
        } catch (Exception e) {
        }


        //Para ver el detalle de un mensaje concreto
        if (randomNum.equals("vacio")) { //En el caso que no haya utilizado un 'uuid' en la composicion del mensaje
            if (browser.image("icon-ok2.png[0]").rightOf(browser.cell(contrato + "[0]")).exists()) {
                browser.image("icon-ok2.png[0]").rightOf(browser.cell(contrato + "[0]")).click();
            }
            if (browser.image("icon-undef1.gif").rightOf(browser.cell(contrato + "[0]")).exists()) {
                browser.image("icon-undef1.gif").rightOf(browser.cell(contrato + "[0]")).click();
            }
            if (browser.image("icon-ok.gif").rightOf(browser.cell(contrato + "[0]")).exists()) {
                browser.image("icon-ok.gif").rightOf(browser.cell(contrato + "[0]")).click();
            }
            if (browser.image("icon-undef2.png[0]").rightOf(browser.cell(contrato + "[0]")).exists()) {
                browser.image("icon-undef2.png[0]").rightOf(browser.cell(contrato + "[0]")).click();
            }
        } else {
            if (browser.image("icon-ok2.png[0]").rightOf(browser.cell("/." + randomNum + "./")).exists()) {
                browser.image("icon-ok2.png[0]").rightOf(browser.cell("/." + randomNum + "./")).click();
            }
            if (browser.image("icon-undef1.gif").rightOf(browser.cell("/." + randomNum + "./")).exists()) {
                browser.image("icon-undef1.gif").rightOf(browser.cell("/." + randomNum + "./")).click();
            }
            if (browser.image("icon-ok.gif").rightOf(browser.cell("/." + randomNum + "./")).exists()) {
                browser.image("icon-ok.gif").rightOf(browser.cell("/." + randomNum + "./")).click();
            }
            if (browser.image("icon-undef2.png[0]").rightOf(browser.cell("/." + randomNum + "./")).exists()) {
                browser.image("icon-undef2.png[0]").rightOf(browser.cell("/." + randomNum + "./")).click();
            }
        }

        //Compruebo que existen los campos esperados para un 'detalle de transaccion' de tipo PNS
        if (browser.image("icon-detail-pns.gif").exists()) {
            if (!browser.div("content_title").exists()) {
                logger.error("No existe 'content_title'");
                throw new Exception("");
            }
            if (!browser.cell("ID Transacción:").exists()) {
                logger.error("No existe 'ID Transaccion:'");
                throw new Exception(" ");
            }
            if (!browser.cell("Tiempo:").exists()) {
                logger.error("No existe 'Tiempo:'");
                throw new Exception(" ");
            }
            if (!browser.cell("Consolidación:").exists()) {
                logger.error("No existe 'Consolidacion:'");
                throw new Exception(" ");
            }
            if (!browser.cell("Empresa:").exists()){
                logger.error("No existe 'Empresa:'");
                throw new Exception(" ");
            }
            if (!browser.cell("Contrato/Servicio:").exists()) {
                logger.error("No existe 'Contrato/Servicio:'");
                throw new Exception(" ");
            }
            if (!browser.cell("Aplicación/Producto:").exists()) {
                logger.error("No existe 'Aplicacion/Producto:'");
                throw new Exception(" ");
            }
            if (!browser.cell("ID Mensaje:").exists()) {
                logger.error("No existe 'ID Mensaje:'");
                throw new Exception(" ");
            }
            if (!browser.cell("Etiqueta:").exists()) {
                logger.error("No existe 'Etiqueta:'");
                throw new Exception(" ");
            }
            if (!browser.cell("Proveedor:").exists()) {
                logger.error("No existe 'Proveedor:'");
                throw new Exception(" ");
            }
            if (!browser.cell("Destinatario:").exists()) {
                logger.error("No existe 'Destinatario:'");
                throw new Exception(" ");
            }

            //*BUG:SDP-949*
            if (!browser.cell("M-App").exists()){
                logger.error("No existe 'M-App'");
                throw new Exception(" ");
            }
            if (browser.div("/" + mApp + "/").rightOf(browser.cell("M-App")).exists()) {
                logger.info("Requisito 'SDP-RQPNS-3'. Se muestra M-App(refApp) para la que se envía el PNS.");
            } else {
                logger.error("*BUG:SDP-949* No existe el campo con el nombre de la 'M-App' en 'Detalle de Transacciones'");
                throw new Exception(" ");
            }


            if (!browser.cell("UUID").exists()){
                logger.error("No existe 'UUID:'");
                throw new Exception(" ");
            }
            if (!browser.cell("Device").exists()){
                logger.error("No existe 'Device:'");
                throw new Exception(" ");
            }
            if (!browser.div("Mensaje:").exists()) {
                logger.error("No existe 'Mensaje:'");
                throw new Exception(" ");
            }
            if (!randomNum.equals("vacio")) {
                String idtransaccion = browser.div("content_text_code[0]").getText();
                String contenidopublico = browser.div("content_text_code[11]").getText();
                if (browser.containsText(browser.div("content_text_code[11]"), randomNum)) {
                    logger.info("'Detalle de Transacciones', contenido publico correcto para PNS idTrans: '" + idtransaccion);
                    logger.info("'Detalle de Transacciones', contenido publico correcto para PNS idTrans: '" + idtransaccion);
                } else {
                    logger.error("Contenido publico de PNS idTransaccion: '" + idtransaccion + "' no se muestra correctamente en 'Detalle de Transacciones'");
                    throw new Exception(" ");
                }
            }


        }


    }


    /**
     * Busca en Detalle de Transacciones >> Filtro, un criterio dado.
     *
     * @param clave
     * @param valor
     * @param noaparecer Si vale "", indica que el criterio buscado debe existir, de lo contrario petará.
     *                   Si vale distinto de "", indicará que el criterio buscado NO deberia aparecer en las estadisticas.
     * @throws Exception
     */
    @Step("BusquedaCriterio <clave> <valor> <\"\"> <>")
    public void busquedaCriterio(String clave, String valor, String noaparecer, String refCompany) throws Exception {
        String code = verificacionLIMSP.obtenerCodigoPais();
        String number = verificacionLIMSP.obtenerNumeroVirtual();
        String randomNum = "vacio";
        int segundos = 1;
        //Tomo el valor del número virtual
        if (clave.equalsIgnoreCase("telefono")) {
            if (valor.equalsIgnoreCase(Constantes.VIRTUAL) || valor.equalsIgnoreCase(Constantes.UNKNOWN)) {
                if (datosGlobales.get(Constantes.GSM) != null) {
                    valor = datosGlobales.get(Constantes.GSM).toString();

                } else {
                    if (code != null && number != null) {
                        valor = code + number;
                    }
                }
            }
        }

        try {
            randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString(); //Es el numero random que guardamos para poder encontrarlo ahora en las Estadisticas
        } catch (Exception e) {

        }


        //hago la busqueda
        browser.link("Filtro").click();
        browser.radio("sel_service[0]").click(); //establezco para TODOS los contratos

        if (clave.equals("telefono") || clave.equals("email")) {
            browser.checkbox("sel_tel").check();
            browser.textbox("val_address").setValue(valor);
            if (browser.checkbox("check_key").exists()) {
                browser.checkbox("check_key").uncheck();
            }
        } else {
            browser.checkbox("sel_tel").uncheck();
            browser.checkbox("check_key").check();
            browser.select("sel_key").choose(clave);
            if (!refCompany.equals("")) {
                browser.select("refCompany").choose(refCompany);
            }
            browser.textbox("val_key").setValue(valor);
        }

        browser.submit("Aceptar").click();

        //Comprueba si aparece el criterio buscado
        if (randomNum.equals("vacio")) { //En el caso que no haya utilizado un 'uuid' en la composicion del mensaje
            if (browser.cell("/." + valor + "./").exists()) {
                //Si existe el valor indicado no hago nada
                logger.info("Resultado correcto: El criterio '" + valor + "' existe");
            } else {
                logger.error("No existen transacciones con el criterio de busqueda: '" + valor + "'");
                throw new Exception(" ");
            }
        } else {
            boolean completado = false;
            segundos = 5;

            while (!completado) {

                //String progreso = (browser.span("percent[0]").under(browser.div("Paquete-Twist_" + randomNum))).getText();

                if (browser.cell("/." + randomNum + "./").exists()) {

                    if (!noaparecer.equals("")) {
                        //Si existe el valor indicado es que se ha enviado la trasaccion cuando no debiera
                        logger.error("Se ha encontrado el criterio '" + randomNum + "' cuando no debiera");
                        throw new Exception(" ");
                    } else {
                        //Si existe el valor indicado es que la transaccion ha ido bien
                        logger.info("");
                        logger.info("Resultado correcto: El criterio '" + randomNum + "' existe");
                    }

                    completado = true;

                } else {

                    if (segundos == 5)
                        logger.info("Esperando estadisticas para random: " + randomNum + "'");
                    if (segundos % 100 == 0) { //Cada 100 segundos baja de linea. Solo confines visuales en la consola
                        logger.info("");
                    }
                    if (segundos >= 20) { //Esto es un timeOut por si jamas aparecen estadisticas, entonces termino el programa. Tambien sirve para a los segundos indicados dar por buena una transacción que no tenia en realidad que aparecer porque debia ser descartada

                        if (noaparecer.equals("")) {
                            logger.info("");
                            logger.error("No existen transacciones con el criterio de busqueda: '" + randomNum + "'");
                            throw new Exception(" ");

                        } else {
                            //Si no existe el valor indicado es correcto porque se ha descartado
                            logger.info("Resultado correcto: El criterio '" + randomNum +"' NO existe, lo cual es correcto porque se ha descartado.");
                            completado = true;
                        }
                    }

                    if (!completado) {
                        browser.button("Actualizar").click(); //Refresco las estadisticas a ver si aparecen las transacciones
                        Thread.sleep(5000); //Espero 5sec para dar tiempo a que cargen las estadisticas
                        if (segundos == 5) {
                            logger.info("Esperando " + segundos + ",");
                        } else {
                            logger.info(segundos + ",");
                        }
                        segundos = segundos + 5;
                    }
                }
            }
        }
    }

    @Step("Verificar UUID")
    public void verificarUUID() throws Exception {
        String uuid = datosGlobales.get(Constantes.UUID).toString();
        if (browser.cell("UUID").exists()) {
            if (browser.cell(uuid).rightOf(browser.cell("UUID")).exists()) {
                logger.info("Existe UUID " + uuid);
            } else {
                logger.error("No existe UUID " + uuid);
                throw new Exception(" ");
            }
        }
    }

    @Step("Obtener IdMsg")
    public void obtenerIdMsg() throws Exception {
//	if (browser.image("icon-ok2.png[0]").exists(true)){
//		browser.image("icon-ok2.png[0]").click();
//	}
        if (browser.cell("ID Mensaje:").exists()) {
            ElementStub idMsg = browser.cell("").rightOf(browser.cell("ID Mensaje:"));
            datosGlobales.put(Constantes.IDMSG, idMsg.getText());
        } else {
            logger.error("No existe el campo ID Mensaje");
            throw new Exception(" ");
        }
    }

    /**
     * This method do search one mail since search criteria
     *
     * @param clave
     * @param valor
     * @param refCompany
     * @throws Exception
     */
    @Step("Busqueda Emails por clave de usuario <> <> <>")
    public void searchEmailsUserKey(String clave, String valor, String refCompany) throws Exception {
        String randomNum;
        if (provisionerAdminUser.existCompaniesInEnterprise()) {
            mContract.asignarPropiedadContratoWS("#wapppushemail", Constantes.LIMSP,
                    "CUSTOMER_PREFERENCES", Constantes.TRUE);
            mContract.asignarPropiedadContratoWS("#wapppushemail", Constantes.LIMSP,
                    "ORGANIZATION", Constantes.LATINIA.toUpperCase());
            mProduct.asignarRol("lman-vcontentmail", "LS_PROVISIONER");

            gestionContratos.forzarrecarga();
            almacenDeDatos.menu("Análisis");
            almacenDeDatos.herramienta("Detalle de contenidos de transacciones de email");
            navegacion.menuHerramienta();

            insercionWSJMS.setXMLEmail("push-email.xml", "INNOVUS", "#wapppushemail",
                    valor, "Test AUTOMATED", "Test busqueda email por clave de usuario");
            insercionWSJMS.insercionWS("email", "wapppushemail", "normalAuth");
            randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
            //hago la busqueda
            browser.link("Filtro").click();

            browser.checkbox("check_key").check();
            logger.info(browser.select("sel_key").getText());

            if (!refCompany.equals("")) {
                browser.select("refCompany").choose(refCompany);
            }
            browser.select("sel_key").choose("/email/");
            browser.textbox("val_key").setValue(valor);
            browser.submit("Aceptar").click();

            if (browser.cell("/" + randomNum + "/").exists()) {
                logger.info("Resultado correcto: El criterio '" + valor + randomNum + "' existe");
            } else {
                logger.error("NO se encunetra el criterio de búsqueda");
                throw new Exception(" ");
            }

            mContract.eliminarPropiedadContratoWS("#wapppushemail", Constantes.LIMSP,
                    "CUSTOMER_PREFERENCES");
            mContract.eliminarPropiedadContratoWS("#wapppushemail", Constantes.LIMSP,
                    "ORGANIZATION");
            gestionContratos.forzarrecarga();
        }
    }


    //-----------------------------------------WS----------------------------------------------

    public LXList obtenerListaMensajesGlobal(String refContract) throws Exception {
        List<Object> retorna;
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_LMAN_VCONTENT, Constantes.LATINIA, Constantes.WLURL_PSTORAGE,
                Constantes.WASURL_PSTORAGE, Constantes.WS_LD_PSTORAGE_SERVICE, Constantes.WS_LD_PSTORAGE_LOCATOR,
                Constantes.WS_LD_PSTORAGE_PORTSTUB, Constantes.WS_LD_PSTORAGE_METHOD, Constantes.APP_LMAN_VCONTENT);
        Ws_ld_pstoragePortStub portStub = (Ws_ld_pstoragePortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();

        String tsIni = "";
        String tsEnd = "";
        String idContract = mContract.obtenerIdContrato(empresa, refContract);
        //limit vacio porque en algunos JEE me toma los primeros 5 mas antiguos y en otros los primeros 5 mas nuevos
        String limit = "";
        //Obtener la fecha en formato yyyy-MM-dd
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(date);
        //Fecha inicial, un día anterior a la fecha actual
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        tsIni = hourdateFormat.format(calendar.getTime());

        calendar.setTime(date);
        //Se obtiene la fecha de dos dias más, es decir, la fecha final será un dia despues de la actual
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        tsEnd = hourdateFormat.format(calendar.getTime());
        //Construcción del XML de parámetros
        LXObject lxParams = new LXObject("parameters", "1");
        //Ajuste de parámetros para los filtros de selección //Intervalo de tiempo. Formato yyyy-MM-dd
        lxParams.setPropertyValue("tsFrom", tsIni);
        lxParams.setPropertyValue("tsTo", tsEnd);
        //Contratos a consultar
        lxParams.setPropertyValue("idContract-list", idContract);
        //Invocación del método
        LXList lista = (LXList) LXSerializer.readLX(portStub.invoke(validation.toString(), "listContentGlobal",
                limit, LXSerializer.serializeLX(lxParams), null));

        return lista;
    }

    @Step("Comprobar cantidad de mensajes cantidad<> refContract<>")
    public void comprobarCantidadMsg(int cantidad, String refContract) throws Exception {
        if (existenMensajes(cantidad, refContract)) {
            logger.info("EXISTEN " + cantidad + " mensajes en Detalle de transacciones");
        } else {
            logger.error("NO existen " + cantidad + " mensajes");
            throw new Exception(" ");
        }
    }

    @Step("Comprobar Msg refContract<>")
    public void comprobarExisteMsg(String refContract) throws Exception {
        String randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
        if (existenMensajes(1,refContract)) {
            logger.info("INF: Resultado Correcto, existe el mensaje " + randomNum);
        } else {
            logger.error("NO Existe el mensaje " + randomNum);
            throw new Exception(" ");
        }
    }


    public boolean existenMensajes(int cantidad, String refContract) throws Exception {
        boolean enviarIdTrans = false;
        String idTrans = null;
        int contador = 0;
        int cantidadIicial = cantidad;
        //Se hace esta validación por si mas adelante se quiere ver el detalle de los estados
        if (cantidad == 1) {
            enviarIdTrans = true;
        }
        logger.info("cantidad " + cantidad);
        String randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
        LXList lista = obtenerListaMensajesGlobal(refContract);

        Iterator iterador = lista.getIterator();
//        while (iterador.hasNext()) {
//            LXObject lx = (LXObject) iterador.next();
//            System.out.println("cant mensajes " + lista.getSize());
//            System.out.println("mensaje " + lx.getPropertyValue("message"));
//        }
        while (iterador.hasNext() && cantidad != 0) {
            LXObject lx = (LXObject) iterador.next();
            if (lx.getPropertyValue("message").contains(randomNum)) {
                cantidad--;
                contador++;
                if (enviarIdTrans) {
                    idTrans = lx.getPropertyValue("idTrans");
                }
            }
        }

        if (idTrans != null) {
            System.out.println();
            datosGlobales.put(Constantes.ID_TRANSACTION, idTrans);
        }

        if (cantidadIicial == contador) {
            return true;
        } else {
            return false;
        }
    }

    @Step("Comprobar estadosWS <>")
    public void comprobarEstadosWS(String estado) throws Exception {
        if (datosGlobales.get(Constantes.ID_TRANSACTION) != null) {
            String valProperty;
            if (latiniaScenarioUtil.leerPropiedadesLConfig("lmod-vcollector.properties", "config/bus/collectors/vcollector/mode") == null) {
                valProperty = "null";
            } else {
                valProperty = latiniaScenarioUtil.leerPropiedadesLConfig("lmod-vcollector.properties", "config/bus/collectors/vcollector/mode");
            }

            String idTransaccion = datosGlobales.get(Constantes.ID_TRANSACTION).toString();
            String limit = "5";
            String sent = "";
            String state = "";
            List<Object> retorna;
            retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_LMAN_VCONTENT, Constantes.LATINIA, Constantes.WLURL_PSTORAGE,
                    Constantes.WASURL_PSTORAGE, Constantes.WS_LD_PSTORAGE_SERVICE, Constantes.WS_LD_PSTORAGE_LOCATOR,
                    Constantes.WS_LD_PSTORAGE_PORTSTUB, Constantes.WS_LD_PSTORAGE_METHOD, Constantes.APP_LMAN_VCONTENT);
            Ws_ld_pstoragePortStub portStub = (Ws_ld_pstoragePortStub) retorna.get(0);
            LXValidationLData validation = (LXValidationLData) retorna.get(1);
            //Construcción del XML de parámetros
            LXObject lxParams = new LXObject("parameters", "1");
            //Ajuste de parámetros para los filtros de selección //Intervalo de tiempo. Formato yyyy-MM-dd
            lxParams.setPropertyValue("idTrans", idTransaccion);

            //De pendiendo de la descripción damos valor a sent y state
            if (estado.equalsIgnoreCase("Entregado al proveedor")) {
                sent = "1";
                state = "2";
            } else if (estado.equalsIgnoreCase("Entrega confirmada a usuario")) {
                sent = "1";
                state = "4";
            } else if (estado.equalsIgnoreCase("Finalizado en el proveedor")) {
                sent = "2";
                state = "1";
            }

            LXList lista = (LXList) LXSerializer.readLX(portStub.invoke(validation.toString(), "MessageStatusDetail",
                    limit, LXSerializer.serializeLX(lxParams), null));

            Iterator iterador = lista.getIterator();
            boolean encontrar = false;
            while (iterador.hasNext() && encontrar == false) {
                LXObject lx = (LXObject) iterador.next();
                if (valProperty.equalsIgnoreCase("BUS")) {
                    if (lx.getPropertyValue("sent").equals(sent) && lx.getPropertyValue("state").equals(state)) {
                        logger.info("INF: Correcto, se encuentra el estado " + estado + " (" + sent + "," + state + ")");
                        encontrar = true;
                    }
                } else if (valProperty.equalsIgnoreCase("GUI") || valProperty.equals("null")) {
                    if (estado.equals("Entrega confirmada a usuario")) {
                        if (lx.getPropertyValue("sent").equals(sent) && lx.getPropertyValue("state").equals(state)) {
                            logger.error("ERR: El estado " + estado + " no debería existir si la entrada \"config/bus/collectors/vcollector/mode\" = GUI");
                            throw new Exception(" ");
                        }
                    } else {
                        if (lx.getPropertyValue("sent").equals(sent) && lx.getPropertyValue("state").equals(state)) {
                            logger.info("Correcto, se encuentra el estado " + estado + " (" + sent + "," + state + ")");
                            encontrar = true;
                        }
                    }
                }
            }
            if (!encontrar) {
                logger.info("INF: No se encuentra el estado " + estado);
            }
        } else {
            logger.error("ERR: No existe transacción");
            throw new Exception(" ");
        }
    }
}
