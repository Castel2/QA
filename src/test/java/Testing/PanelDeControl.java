package Testing;
// JUnit Assert framework can be used for verification

//estos imports son para las funciones de trabajo con fechas

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PanelDeControl {

    private Browser browser;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private LatiniaScenarioUtil scenarioUtil;
    private static Logger logger = LogManager.getLogger(PanelDeControl.class);

    public PanelDeControl() {
        this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
        scenarioUtil = new LatiniaScenarioUtil();
    }

    @Step("General")
    public void general() throws Exception {
        browser.link("Plataforma").click();

    }

    @Step("Logs")
    public void logs() throws Exception {
        browser.link("Logs").click();
        browser.link("Bus").click();
        browser.link("Estándar").click();
        browser.link("Extendido").click();
        browser.link("Data").click();
        browser.link("Extendido").click();
        browser.link("Process").click();
        browser.textbox("log_filter").setValue("ERR");
        browser.imageSubmitButton("icon_search.gif").click();
        browser.link("Estándar").click();
        browser.link("Extendido").click();
        browser.link("Timers").click();
        browser.link("Estándar").click();
        browser.link("Extendido").click();
        browser.link("Descargas").click();

    }

    @Step("Generar_Exportacion actual <> <>")
    public void generar_Exportacion_actual(String string1, String string2) throws Exception {
        int randomNum = scenarioUtil.randInt(1, 99999);
        String cell = string1 + randomNum;
        browser.link("Data").click();
        browser.image("Descargar actual").click();
        browser.radio("date[1]").click();
        browser.select("date_ini_Day_ID").choose("1");
        browser.image("Calendar").click();
        browser.image("Calendar").click();
        browser.image("Calendar[1]").click();
        browser.image("Calendar[1]").click();
        browser.textbox("fileName").setValue(cell);
        browser.textarea("description").setValue(string2);
        browser.button("Descargar").click();
        //Espero 5 segundos refresco y espero 2 segundos mas
        Thread.sleep(5000);
        browser.navigateTo("", true);
        Thread.sleep(2000);
        //verifico que exista el ZIP generado
        if (browser.cell("/" + cell + "/").exists()) {
            logger.info("Se ha exportado " + cell + ".zip Exitosamente");
        } else if (browser.cell("No existen paquetes de logs generados actualmente.").exists()) {
            logger.info("Verifica en el archivo de configuración \"lmod-ld-logmanager.properties\" la entrada \"config/ldata/ld-logmanager/tlogmanager/maxTotalFilesSize\", si el valor es mayor a 0.");
            logger.error("No se ha podido generar el archivo ZIP");
            throw new Exception("");
        } else {
            logger.error("No se ha podido generar el archivo ZIP");
            throw new Exception("");
        }

    }

    @Step("Generar_Exportacion <Mis logs Automated> <Export logs automaticos con Automated>")
    public void generar_Exportacion(String string1, String string2) throws Exception {
        String cell = "";
        browser.link("Bus").click();
        browser.image("Descargar todo").click();
        browser.radio("date[1]").click();
        browser.select("date_ini_Day_ID").choose("1");
        browser.image("Calendar").click();
        browser.image("Calendar").click();
        browser.image("Calendar[1]").click();
        browser.image("Calendar[1]").click();
        browser.textbox("fileName").setValue(string1);
        browser.textarea("description").setValue(string2);
        browser.button("Descargar").click();
        //Espero 5 segundos refresco y espero 2 segundos mas
        Thread.sleep(5000);
        browser.navigateTo("", true);
        Thread.sleep(2000);

        if (browser.cell("Empaquetado de logs en curso.").exists()) {
            cell = browser.cell("/tmp/").above(browser.cell("Empaquetado de logs en curso.")).getText();
        }

        if (cell.contains(".")) {
            cell = cell.substring(0, cell.indexOf("."));
            logger.info("Se está generando el archivo " + cell + ".tmp");
            //Se espera mientras se genera el archivo ZIP
            while (browser.image("loading2.gif").exists()) {
            }
            Thread.sleep(5 * 1000);
            //verifico que exista el ZIP generado
            if (browser.cell("/" + cell + "/").exists()) {
                logger.info("Se ha exportado " + cell + ".zip Exitosamente");
            } else if (browser.cell("No existen paquetes de logs generados actualmente.").exists()) {
                logger.info("Verifica en el archivo de configuración \"lmod-ld-logmanager.properties\" la entrada \"config/ldata/ld-logmanager/tlogmanager/maxTotalFilesSize\", si el valor es mayor a 0.");
                logger.error("No se ha podido generar el archivo ZIP");
                throw new Exception("");
            } else {
                logger.error("No se ha podido generar el archivo ZIP");
                throw new Exception("");
            }
        }
    }


    @Step("Conectividad")
    public void conectividad() throws Exception {
        browser.link("Conectividad").click();

    }

    @Step("BBDD <ver>")
    public void bBDD(String string1) throws Exception {
        browser.link("Bases de datos").click();

        if (string1 == "ejecutar") {
            if (browser.submit("Realizar \"Benchmark\" ahora").exists()) {
                browser.submit("Realizar \"Benchmark\" ahora").click();
            }
            if (browser.submit("Ejecutar benchmark").exists()) {
                browser.submit("Ejecutar benchmark").click();
            }
        }
    }

    public void bajarDescargasGeneradas() throws Exception {
        browser.image("icon_download_file.gif").click();
        browser.image("icon_download_file.gif").near(browser.span("Export logs automaticos con Automated")).click();
        //browser.link("Editar nombre")                .near(browser.div(aplicacion))                         .click();

    }

    public void logCheckLprocess_Act(String nomproceso) throws Exception {
        browser.link("Logs").click();
        browser.link("Process").click();

        if (browser.cell("Actividad").exists()) { //Verifico que estoy en la pestaña 'Actividad', del Panel de Control

            boolean ok = true;
            //Preparo la fecha actual en el formato adecuado yyyy/MM/dd HH:mm:ss
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date hoy = new Date(); //obtengo la fecha de hoy y la meto en 'hoy'

            String limspact = browser.textarea("logdata").getText(); //Meto el log en el String denominado 'limspact'

            //**histAndSummary**//
            if (nomproceso.equalsIgnoreCase("ld-mcontablehistAndSummary")) {

                String fecha = dateFormat.format(hoy.getTime() - 20 * 60 * 1000); //A la fecha actual le resto 20minutos porque el Hist&Summary se ejecuta cada 15min, asi que querre ver por ejemplo 20 minutos atras

                int indicefecha = limspact.indexOf(fecha); //Obtengo posicion de limspact donde comienza el dia de hoy

                if (indicefecha >= 0) { //En el caso que obtenga resultados en la fecha de hoy
                    String datosHoy = limspact.substring(indicefecha); //Meto en 'datosHoy' la parte del log a partir de la fecha de hoy en adelante
                    if (datosHoy != null) { //Si hay datos prosigo comprobando cuales son estos datos. Realizo varias busquedas
                        if (datosHoy.indexOf("histAndSummary") < 0)
                            ok = false; //Si no aparece la cadena 'histAndSummary' en el log
                        if (ok && datosHoy.indexOf("deliveryLockProce") < 0)
                            ok = false; //Si no aparece la cadena 'deliveryLockProce' en el log

                        if (ok && datosHoy.indexOf("END") < 0) ok = false; //Si no aparecen 'END' en el dia de hoy

                        //En el caso que aparezcan ERR, TMO o ENT marco como que algo esta mal
                        if ((ok && datosHoy.indexOf("ERR") >= 0) || (ok && datosHoy.indexOf("TMO") >= 0) || (ok && datosHoy.indexOf("ENT") >= 0))
                            ok = false;

                    } else { //Si no hay datos quiere decir que no hay log, lo cual debe ser un fallo
                        logger.error("No se encuentran datos en el LOG para el dia de hoy.");
                        throw new Exception("");
                    }
                } else { // En caso de no haber obtenido datos para la fecha de hoy
                    logger.error("No se encuentra la fecha de hoy en el LOG.");
                    throw new Exception("");
                }

                if (ok == false) {
                    logger.error("Se han encontrado errores en el log Limsp_act 'histAndSummary'");
                    throw new Exception("");
                } else {
                    logger.info("Comprobado 'Limsp_act'. 'histAndSummary' Correcto");
                }
            }
            //**consolidate**//
            if (nomproceso.equalsIgnoreCase("ld-mcontableconsolidate")) {

                String fecha = dateFormat.format(hoy.getTime() - 20 * 60 * 1000); //A la fecha actual le resto 20minutos porque el Hist&Summary se ejecuta cada 15min, asi que querre ver por ejemplo 20 minutos atras

                int indicefecha = limspact.indexOf(fecha); //Obtengo posicion de limspact donde comienza el dia de hoy

                if (indicefecha >= 0) { //En el caso que obtenga resultados en la fecha de hoy
                    String datosHoy = limspact.substring(indicefecha); //Meto en 'datosHoy' la parte del log a partir de la fecha de hoy en adelante
                    if (datosHoy != null) { //Si hay datos prosigo comprobando cuales son estos datos. Realizo varias busquedas
                        if (datosHoy.indexOf("histAndSummary") < 0)
                            ok = false; //Si no aparece la cadena 'histAndSummary' en el log
                        if (ok && datosHoy.indexOf("deliveryLockProce") < 0)
                            ok = false; //Si no aparece la cadena 'deliveryLockProce' en el log

                        if (ok && datosHoy.indexOf("END") < 0) ok = false; //Si no aparecen 'END' en el dia de hoy

                        //En el caso que aparezcan ERR, TMO o ENT marco como que algo esta mal
                        if ((ok && datosHoy.indexOf("ERR") >= 0) || (ok && datosHoy.indexOf("TMO") >= 0) || (ok && datosHoy.indexOf("ENT") >= 0))
                            ok = false;

                    } else { //Si no hay datos quiere decir que no hay log, lo cual debe ser un fallo
                        logger.error(" No se encuentran datos en el LOG para el dia de hoy.");
                        throw new Exception("");
                    }
                } else { // En caso de no haber obtenido datos para la fecha de hoy
                    logger.error("No se encuentra la fecha de hoy en el LOG.");
                    throw new Exception("");
                }

                if (ok == false) {
                    logger.error("Se han encontrado errores en el log Limsp_act 'consolidate'");
                    throw new Exception("");
                } else {
                    logger.info("Comprobado 'Limsp_act'. 'consolidate' Correcto");
                }

            }
            //**lserv-delivery**//
            if (nomproceso.equalsIgnoreCase("lserv-delivery")) {

                String fecha = dateFormat.format(hoy.getTime() - 20 * 60 * 1000); //A la fecha actual le resto 20minutos porque el Hist&Summary se ejecuta cada 15min, asi que querre ver por ejemplo 20 minutos atras

                int indicefecha = limspact.indexOf(fecha); //Obtengo posicion de limspact donde comienza el dia de hoy

                if (indicefecha >= 0) { //En el caso que obtenga resultados en la fecha de hoy
                    String datosHoy = limspact.substring(indicefecha); //Meto en 'datosHoy' la parte del log a partir de la fecha de hoy en adelante
                    if (datosHoy != null) { //Si hay datos prosigo comprobando cuales son estos datos. Realizo varias busquedas
                        if (datosHoy.indexOf("deliveryLockProce") < 0)
                            ok = false; //Si no aparece la cadena 'deliveryLockProce' en el log

                        if (ok && datosHoy.indexOf("END") < 0) ok = false; //Si no aparecen 'END' en el dia de hoy

                        //En el caso que aparezcan ERR, TMO o ENT marco como que algo esta mal
                        if ((ok && datosHoy.indexOf("ERR") >= 0) || (ok && datosHoy.indexOf("TMO") >= 0) || (ok && datosHoy.indexOf("ENT") >= 0))
                            ok = false;

                    } else { //Si no hay datos quiere decir que no hay log, lo cual debe ser un fallo
                        logger.error("No se encuentran datos en el LOG para el dia de hoy.");
                        throw new Exception("");
                    }
                } else { // En caso de no haber obtenido datos para la fecha de hoy
                    logger.error("No se encuentra la fecha de hoy en el LOG.");
                    throw new Exception("");
                }

                if (ok == false) {
                    logger.error("Se han encontrado errores en el log Limsp_act proceso 'lserv-delivery'");
                    throw new Exception("");
                } else {
                    logger.info("Comprobado 'Limsp_act'. 'lserv-delivery' Correcto");
                }

            }

        } else {
            logger.error("No se encuentra el Area de texto del LOG");
            throw new Exception("");
        }
    }


    @Step("Mensajes Descartados")
    public void mensajesDescartados() throws Exception {
        String randomNum = "";
        randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();

//		browser.image("ico_info.gif").click();
//		browser.button("Detalle de la transacción (xml)").click();
//		browser.popup("/getErrorData?idEvent=/");

        if (browser.cell("/." + randomNum + "./").exists()) {
            //Si existe el valor indicado es que la transaccion ha ido bien
            logger.info("Resultado correcto: El criterio '" + randomNum + "' existe");

        } else {
            logger.error("No se encuentra el '" + randomNum + "' en mensajes descartados");
            throw new Exception("");
        }
    }

    /**
     * Este método nos permite verificar la funcionalida de la descarga de logs, desde el botón LogDown
     *
     * @throws Exception
     */
    @Step("LogDown")
    public void logDown() throws Exception {
        String cell = "";
        browser.link("Basic").click();
        //Se verifica si existe el botón LogDown
        if (browser.image("icon_logdown.gif").exists()) {
            browser.image("icon_logdown.gif").click();
            //Se verifica que se realice el proceso de empaquetado de logs
            if (browser.cell("Empaquetado de logs en curso.").exists()) {
                cell = browser.cell("/tmp/").above(browser.cell("Empaquetado de logs en curso.")).getText();
                if (cell.contains(".")) {
                    cell = cell.substring(0, cell.indexOf("."));
                    logger.info("Se está generando el archivo " + cell + ".tmp");
                    //Espero mientras se genera el archivo ZIP, es decir, mientras exista la imagen "loading2.gif" no hago nada
                    while (browser.image("loading2.gif").exists()) {
                    }
                    Thread.sleep(5 * 1000);
                    //Verifico que exista el archivo generado ZIP
                    if (browser.cell("/" + cell + ".zip/").exists()) {
                        logger.info("Se ha generado el archivo " + cell + ".zip");
                    } else if (browser.cell("No existen paquetes de logs generados actualmente.").exists()) {
                        logger.info("Verifica en el archivo de configuración \"lmod-ld-logmanager.properties\" la entrada \"config/ldata/ld-logmanager/tlogmanager/maxTotalFilesSize\", si el valor es mayor a 0.");
                        logger.error("No se ha podido generar el archivo ZIP");
                        throw new Exception("");
                    } else {
                        logger.error("No se ha podido generar el archivo ZIP");
                        throw new Exception("");
                    }

                } else {
                    logger.error("Al parecer no encuentra el formato deseado para el empaquetado de los logs, el valor de la celda es: " + cell);
                }
            } else {
                logger.info("No se detecta el mensaje \"Empaquetado de logs en curso.\"");
            }
        } else {
            logger.error("No existe el botón LogDown");
            throw new Exception("");
        }
    }
}
