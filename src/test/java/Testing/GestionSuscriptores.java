package Testing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import LData_Testing.AccesoWSInf;
import com.latinia.limspinf.stubs.data.*;
import com.latinia.limspinf.stubs.user.Exception_Exception;
import com.latinia.limspinf.stubs.user.ProvisionerUser;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import com.thoughtworks.gauge.Step;
import com.sahipro.lang.java.client.ElementStub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GestionSuscriptores {

    private final Browser browser;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private static Logger logger = LogManager.getLogger(GestionSuscriptores.class);
    AccesoWSInf accesoWSInf;
    VerificacionLIMSP verificacionLIMSP;
    DetalleDeTransacciones detalleDeTransacciones;
    // Estos Strings se utilizan en el metodo 'checkDatos()', de comprobacion de los DATOS de un suscriptor
    private String[] propsimple = new String[]{"chn_pref", "robinsongeneral", "robinsonchnlist", "cclist", "noads", "scoring", "vip", "gender"};
    private String[] propclave = new String[]{"gsm", "email", "dni", "twitter_name"};
    private String[] propgrupo = new String[]{"pais"};

    public GestionSuscriptores() {
        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
        accesoWSInf = new AccesoWSInf();
        verificacionLIMSP = new VerificacionLIMSP();
        detalleDeTransacciones = new DetalleDeTransacciones();
    }

    /**
     * Este método tiene como objetivo aprovisionar un suscriptor
     * Crea un suscriptor y le asigna 'Datos'. No lo asocia a ninguna 'MApp' ni tampoco a 'S.Subscripcion'
     *
     * @param user nombre del suscriptor que se va a crear
     * @throws Exception
     */
    @Step("CrearSuscriptor <> <>")
    public void crearSuscriptor(String user, String crearPropiedades) throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();

        int existe = 1;
        int serie = 0;
        user = user.toLowerCase();
        String suscriptor = user + String.valueOf(serie);

        if (browser.select("idCompany").exists()) {
            if (!empresa.equals(browser.select("idCompany").getSelectedText())) {
                browser.select("idCompany").choose(empresa);
            }
        } else {
            logger.error("No se encuentra el selector de Organizaciones");
            throw new Exception(" ");
        }
        //Pestaña BUSCAR
        browser.image("w_search.png").click();

        do {
            //Previo a crear el suscriptor, verifico que no existiera con anterioridad
            if (serie == 0) {
                if (searchSuscriptor(user)) {
                    serie = serie + 1;

                    browser.image("w_search.png").click();
                } else existe = 0;
            } else if (searchSuscriptor(suscriptor)) {
                serie = serie + 1;
                suscriptor = user + String.valueOf(serie);

                browser.image("w_search.png").click();
            } else existe = 0;

            Thread.sleep(500);


        } while (existe == 1);

		/* Creo el suscriptor en el caso que no existiese previamente */
        browser.image("w_search.png").click(); //Voy a la pestaña de 'buscar' que es donde se crean los suscriptores
        if (serie == 0) {
            newSuscriptor(user);
        } else newSuscriptor(suscriptor);

        Thread.sleep(500);

        if (crearPropiedades.equalsIgnoreCase("conProp")) {
            //Creo propiedades para el usuario
            browser.image("w_man.png").click();
            String propName = "propName";
            String propValue = "propValue";
            String btn_btn_mini_btn_primary = "btn btn-mini btn-primary";
            //Crea las propiedades, solo en caso que no existiesen previamente.
            browser.select(propName).choose("twitter_name");
            if (serie == 0) {
                browser.textbox(propValue).setValue(user + "twitter");
            } else browser.textbox(propValue).setValue(suscriptor + "twitter");
            browser.submit(btn_btn_mini_btn_primary).click();

            browser.select(propName).choose("cclist");
            browser.textbox(propValue).setValue("12345|54321|9876");
            browser.submit(btn_btn_mini_btn_primary).click();

            browser.select(propName).choose("noads");
            browser.textbox(propValue).setValue("TRUE");
            browser.submit(btn_btn_mini_btn_primary).click();

            browser.select(propName).choose("chn_pref");
            browser.textbox(propValue).setValue("pns");
            browser.submit(btn_btn_mini_btn_primary).click();

            browser.select(propName).choose("robinsonchnlist");
            browser.textbox(propValue).setValue("pns");
            browser.submit(btn_btn_mini_btn_primary).click();

            browser.select(propName).choose("robinsongeneral");
            browser.textbox(propValue).setValue("FALSE");
            browser.submit(btn_btn_mini_btn_primary).click();

            browser.select(propName).choose("scoring");
            browser.textbox(propValue).setValue("111");
            browser.submit(btn_btn_mini_btn_primary).click();

            browser.select(propName).choose("vip");
            browser.textbox(propValue).setValue("no");
            browser.submit(btn_btn_mini_btn_primary).click();

            browser.select(propName).choose("gender");
            browser.textbox(propValue).setValue("M");
            browser.submit(btn_btn_mini_btn_primary).click();

            browser.select(propName).choose("dni");
            browser.textbox(propValue).setValue("555888999");
            browser.submit(btn_btn_mini_btn_primary).click();

            browser.select(propName).choose("email");
            if (user.equals("lilicapital")) {
                browser.textbox(propValue).setValue(user+"@latinia.com");
            } else {
                browser.textbox(propValue).setValue(user+"@latinia.com");
            }
            browser.submit(btn_btn_mini_btn_primary).click();


            browser.select(propName).choose("gsm");
            if (user.equals("lilicapital")) {
                browser.textbox(propValue).setValue("659666666");
            } else {
                String code = verificacionLIMSP.obtenerCodigoPais();
                String number = verificacionLIMSP.obtenerNumeroVirtual();
                String gsm = "";
                if (code != null && number != null) {
                    gsm = number;
                }else{ //Si no existe número en el archivo naming, entonces por defecto será el virtual de España
                    gsm = "659000001";
                }
                browser.textbox(propValue).setValue(gsm);
            }
            browser.submit(btn_btn_mini_btn_primary).click();


            browser.select(propName).choose("pais");
            browser.textbox(propValue).setValue("España");
            browser.submit(btn_btn_mini_btn_primary).click();


            //Creo propiedades que borro inmediatamente
            browser.select(propName).choose("#description");
            browser.textbox(propValue).setValue("BORRAR BORRAR BORRAR");
            browser.submit(btn_btn_mini_btn_primary).click();
            browser.link("[0]").rightOf(browser.cell("BORRAR BORRAR BORRAR")).click();
            Thread.sleep(500);
            if (browser.cell("BORRAR BORRAR BORRAR").exists()) {
                logger.error("Borrando propiedad 'BORRAR BORRAR BORRAR");
                throw new Exception(" ");
            } else {
                logger.info("Borrada propiedad 'BORRAR BORRAR BORRAR");
            }
        }
    }

    /**
     * Verifica que no existe el suscriptor para proceder a crearlo
     *
     * @param refUser referencia del nuevo suscriptor
     * @throws Exception
     */
    @Step("Agregar Suscriptor <>")
    public void addSuscriptor(String refUser) throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        if (browser.select("idCompany").exists()) {
            if (!empresa.equals(browser.select("idCompany").getSelectedText())) {
                browser.select("idCompany").choose(empresa);
            }
        } else {
            logger.error("No se encuentra el selector de Organizaciones");
            throw new Exception(" ");
        }

        if (!searchSuscriptor(refUser)) {
            logger.info("El suscriptor " + refUser + " no existe, procedemos a crearlo ....");
            newSuscriptor(refUser);
        } else {
            logger.info("El suscriptor " + refUser + " ya existe");
        }
    }


    /**
     * Asocia al usuario con una MApp existente
     *
     * @param user usuario existente, que será asociado a la MApp
     * @param app  refapp que sera asociada
     */
    @Step("MApps <> <>")
    public void mApps(String user, String app) throws Exception {
        String proposito = datosGlobales.get(Constantes.PROPOSITO).toString();
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();

        app = app.toLowerCase();
        if (browser.select("idCompany").exists()) {
            browser.select("idCompany").choose(empresa);
        }

        browser.image("w_search.png").click();
        browser.textbox("keyValue").setValue(user);
        browser.submit("btn-search").click();


        browser.image("w_circle_ok.png").click();


        browser.textbox("token").setValue(UUID.randomUUID().toString().replaceAll("-", ""));


        if (proposito.equals("inf-103")) {
            boolean correcto = true;
            try {
                browser.select("application").choose("/.*" + app + ".*/");
                correcto = false; //En caso que encuentre el registro indicado, controla un Bug por el cual una Mapp desactivada no debe estar disponible
            } catch (Exception e) {
                logger.info("La MApp desctivada no esta disponible.");
            }
            if (correcto == false) {
                logger.error("BUG encontrado en Limsp: Se ha permitido que una MApp 'desactivada' se vincule a un suscriptor");
                throw new Exception(" ");
            }

        } else {
            browser.select("application").choose(app);
        }


        if (!proposito.equals("inf-103")) {

            browser.textbox("app_version").setValue("1.1");
            browser.select("channel_provider").choose("Apple");
            browser.textbox("uuid_device").setValue(UUID.randomUUID().toString().replaceAll("-", ""));
            browser.textbox("os_version").setValue("iOS");
            browser.textbox("os_vendor").setValue("Apple");
            browser.textbox("dev_vendor").setValue("Apple");
            browser.textbox("dev_model").setValue("i8000");
            Thread.sleep(1000);
            browser.submit("btn btn-mini btn-primary").click();


            if (!proposito.equals("provision")) {
                browser.div("delete").near(browser.cell(app)).click();
                browser.textbox("token").setValue(UUID.randomUUID().toString().replaceAll("-", ""));
                browser.select("application").choose(app);
                browser.textbox("app_version").setValue("1.2");
                browser.select("channel_provider").choose("Apple");
                browser.textbox("uuid_device").setValue(UUID.randomUUID().toString().replaceAll("-", ""));
                browser.textbox("os_version").setValue("iOS");
                browser.textbox("os_vendor").setValue("Apple");
                browser.textbox("dev_vendor").setValue("Apple");
                browser.textbox("dev_model").setValue("i8000");
                browser.submit("btn btn-mini btn-primary").click();
            }
        }
    }

    public void sSubscripcion(String refuser, String refoperacion) throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        boolean vinculado = false; //indica si el servicio esta vinculado al usuario

        refoperacion = refoperacion.toUpperCase();
        if (browser.select("idCompany").exists()) {
            browser.select("idCompany").choose(empresa);
        }

        browser.image("w_search.png").click();
        browser.textbox("keyValue").setValue(refuser);
        browser.submit("btn-search").click();


        browser.image("draft-ico-subscr2.png").click();

        if (browser.cell(refoperacion).exists()) {
            if (browser.italic("icon-play").rightOf(browser.cell(refoperacion)).exists()) {//Detecto si esta o no vinculado al servicio
                vinculado = false;
            } else if (browser.italic("icon-eject").rightOf(browser.cell(refoperacion)).exists()) {
                vinculado = true;
            }

            if (vinculado == false) { //En caso de estar desvinculado, lo vinculo
                browser.italic("icon-play").rightOf(browser.cell(refoperacion)).click();
                Thread.sleep(1000);
                if (browser.italic("icon-eject").rightOf(browser.cell(refoperacion)).exists()) {
                    logger.info("Vinculado '" + refuser + "' al servicio '" + refoperacion + "'");
                    vinculado = true;
                } else {
                    logger.info("Vinculando '" + refuser + "' al servicio '" + refoperacion + "'");
                    throw new Exception(" ");
                }
            }


            if (vinculado == true) { //Si ya estaba vinculado, compruebo si tiene el campo 'channels' informado
                if (browser.cell("channels").exists()) {
                    logger.info("RV 'channels' ya esta informado con el valor '" + browser.getValue(browser.cell("rv_value")) + "'");
                } else {
                    //Cumplimento el valor de un RV para este servicio
                    browser.cell(refoperacion).click();
                    browser.select("rv_name").choose("channels");
                    browser.textbox("rv_value").setValue("SMS|EMAIL");
                    browser.submit("btn_add_rv btn btn-mini btn-primary").click();
                    Thread.sleep(1000);
                    if (browser.cell("channels").exists()) {
                        logger.info("Vinculado RV 'channels'");
                    } else {
                        logger.error("Vinculando RV 'channels'");
                        throw new Exception(" ");
                    }
                }
            }


        } else {
            logger.error("No se encuentra la operacion '" + refoperacion + "'");
            throw new Exception(" ");
        }

    }


    /**
     * Busca el usuario pasado en el parametro
     *
     * @param user usuario a buscar
     * @throws Exception
     */
    @Step("Buscar <>")
    public void buscar(String user) throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();

        if (browser.select("idCompany").exists()) {
            browser.select("idCompany").choose(empresa);
        }

        //Compruebo que encuentra al usuario
        if (searchSuscriptor(user)) {
            logger.info("Encontrado: " + user);
        } else {
            logger.error("No se encuentra el usuario: " + user);
            throw new Exception(" ");
        }
    }

    /**
     * Verifica que la pestaña 360 del presente se monta con las secciones requeridas
     *
     * @throws Exception
     */

    @Step("check360")
    public void check360() throws Exception {
        ArrayList<Date> lastnotify = new ArrayList<>();
        browser.image("w_360.png").click();
        logger.info("Verificando apartado 360º");
        if (browser.span("Datos del Usuario").exists() || browser.span("User data").exists()) {
            logger.info("Area 'Datos de usuario' OK");
        } else {
            logger.error("Falta el cuadro 'Datos del usuario'");
            throw new Exception(" ");
        }

        if (browser.span("Últimas notificaciones").exists() || browser.span("Last notifications").exists()) {

            if ((browser.tableHeader("Fecha").in(browser.table("tbl-last-notif")).exists())) {

            } else {
                logger.error("Falta el campo 'Fecha' bajo 'Ultimas notificaciones'");
                throw new Exception(" ");
            }

            if ((browser.tableHeader("Tipo").in(browser.table("tbl-last-notif")).exists())) {

            } else {
                logger.error("Falta el campo 'Tipo' bajo 'Ultimas notificaciones'");
                throw new Exception(" ");
            }

            if ((browser.tableHeader("Servicio").in(browser.table("tbl-last-notif")).exists())) {

            } else {
                logger.error("Falta el campo 'Servicio' bajo 'Ultimas notificaciones'");
                throw new Exception(" ");
            }

            if ((browser.tableHeader("Contenido").in(browser.table("tbl-last-notif")).exists())) {

            } else {
                logger.error("Falta el campo 'Contenido' bajo 'Ultimas notificaciones'");
                throw new Exception(" ");
            }

            //Preparo el formato adecuado para la fecha dd/MM/yyyy HH:mm:ss
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            //*BUG:SDP-1048*
            //Comprueba que las ultimas notificaciones estan ordenadas de nuevo a viejo
            logger.info("Testing *BUG:SDP-964*");
            int j = 0;
            if (!browser.cell("No se ha enviado ninguna notificación.").exists()) {

                //Se toman los dos primeros Elementos
                ElementStub cells = browser.cell("[" + j + "]").under(browser.tableHeader("Fecha[0]"));
                ElementStub cells2 = browser.cell("").under(browser.cell(cells.getText()));
                lastnotify.add(dateFormat.parse(browser.getText(browser.cell("[0]").under(browser.tableHeader("Fecha[0]")))));

                while (cells.exists()) {

                    if (cells2.exists()) {

                        if ((cells.getText()).equals(cells2.getText())) {

                            j = j + 2;
                            //Como las fechas están repetidas, se toma el siguiente con j para tomar la siguiente celda del valor repetido
                            //Se asignan los valores siguientes de cada celda
                            cells = browser.cell("[" + j + "]").under(browser.tableHeader("Fecha[0]"));
                            cells2 = browser.cell("").under(browser.cell("[" + j + "]").under(browser.tableHeader("Fecha[0]")));

                        } else {
                            lastnotify.add(dateFormat.parse(cells2.getText()));
                            cells = cells2;
                            cells2 = browser.cell("").under(browser.cell(cells.getText()));
                        }
                    } else {
                        cells = cells2;
                    }

                }

                for (int i = 0; i < lastnotify.size(); i++) {
                    logger.info("Fecha " + lastnotify.get(i));
                }

                int pos = 0;
                System.out.println("");
                for (int i = 0; i < lastnotify.size() - 1; i++) {
                    if (lastnotify.get(pos).after(lastnotify.get(pos + 1))) {    //Aqui se realiza la comprobacion
                        logger.info("BIEN: " + dateFormat.format(lastnotify.get(i)) + " DESPUES QUE " + dateFormat.format(lastnotify.get(i + 1)));
                    } else {
                        logger.error("Identificado *BUG:SDP-1048* en ordenacion por fecha vista 360º del CMC");
                        throw new Exception(" ");
                    }

                }
                logger.info("Superado  *BUG:SDP-1048*");
            }
            logger.info("Area 'Ultimas notificaciones' OK");

        } else {
            logger.error("Falta el area 'Ultimas notificaciones'");
            throw new Exception(" ");
        }

        if (browser.span("Aplicaciones y dispositivos").exists() || browser.span("Applications and devices").exists()) {
            logger.info("Area 'Aplicaciones y dispositivos'");

        } else {
            logger.error("Falta el area 'Ultimas notificaciones'");
            throw new Exception(" ");
        }

        if (browser.span("Aplicaciones y dispositivos").exists() || browser.span("Applications and devices").exists()) {
            logger.info("Area 'Aplicaciones y dispositivos'");
        } else {
            logger.error("Falta el area 'Aplicaciones y dispositivos'");
            throw new Exception(" ");
        }
    }

    /**
     * Va a la pestaña DATOS del presente usuario y comprueba que se monta bien, teniendo en cuenta que se aprovisionaron
     * los datos con el metodo crearSuscriptor()
     *
     * @throws Exception
     */
    @Step("checkDatos")
    public void checkDatos() throws Exception {
        browser.image("w_man.png").click();
        logger.info("Verificando apartado DATOS");
        boolean salir = false;

        for (int i = 0; i <= propsimple.length - 1; i++) {
            if (browser.cell(propsimple[i]).exists()) {
                logger.info("Comprobado campo " + propsimple[i]);
            } else {
                logger.error("ERR: No existe el campo " + propsimple[i]);
                salir = true;
            }
        }

        for (int i = 0; i <= propclave.length - 1; i++) {
            if (browser.cell(propclave[i]).exists()) {
                logger.info("Comprobado campo " + propclave[i]);
            } else {
                logger.error("No existe el campo " + propclave[i]);
                salir = true;
            }
        }

        for (int i = 0; i <= propgrupo.length - 1; i++) {
            if (browser.cell(propgrupo[i]).exists()) {
                logger.info("Comprobado campo " + propgrupo[i]);
            } else {
                logger.error("No existe el campo " + propgrupo[i]);
                salir = true;
            }
        }


        if (salir == true) {
            logger.error("Terminado porque faltan campos");
            throw new Exception(" ");

        }

        //Compruebo que la pantalla se monta con las secciones requeridas
        if (browser.span("/(ref.)/").exists()) {
            logger.info("BIEN: Area 'ref.User'");
        } else {
            logger.error("No se muestra la sección: 'ref.User");
            throw new Exception(" ");
        }

        if (browser.div("Asignar valor de propiedad:").exists() || browser.div("Assign property value:").exists()) {
            logger.info("BIEN: Area 'Asignar valor de propiedad'");
        } else {
            logger.error("Falta el area 'Asignar valor de propiedad'");
            throw new Exception(" ");
        }
    }

    @Step("checkToken <> <>")
    public void checkToken(String user, String app) throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String tokenprimero = "vacio";
        try {
            tokenprimero = datosGlobales.get(Constantes.TOKEN_PRIMERO).toString(); //Es el token que guardamos durante la anterior ejecución de este método
        } catch (Exception e) {
        }


        app = app.toLowerCase();
        String tokenactual = ""; //recoje el token que la mapp tiene actualmente
        browser.select("idCompany").choose(empresa);

        browser.image("w_search.png").click();
        browser.textbox("keyValue").setValue(user);
        browser.submit("btn-search").click();
        browser.link("M-Apps").click();
        logger.info("Leyendo Token actual:");

        tokenactual = browser.span("apptoken").near(browser.span("Token").rightOf(browser.cell(app))).getText();
        logger.info(" " + tokenactual);


        if (tokenprimero.equals("vacio")) { //Si está vacio quiere decir que estamos en la primera pasada.
            datosGlobales.put(Constantes.TOKEN_PRIMERO, tokenactual); //guardo el valor del tokenactual en la variable global
        } else {
            if (tokenprimero.equals(tokenactual)) {
                logger.error("El Token no ha sido actualizado. Prueba fallida. \n" +
                        "  Anterior: '\" + tokenprimero + \"'\\n Actual: '\" + tokenactual + \"'");
                throw new Exception(" ");
            }
            if (!tokenprimero.equals(tokenactual)) {
                logger.info("Correcto: El Token ha sido actualizado. \n" +
                        "  Anterior: '\" + tokenprimero + \"'\\n  Actual: '\" + tokenactual + \"'");
            }
        }

    }

    @Step("cambiaToken <>")
    public void cambiaToken(String refapp) throws Exception { //Invoca el servlet para cambio de Token de una Mapp
        String host = datosGlobales.get(Constantes.HOST).toString();
        String port = datosGlobales.get(Constantes.PORT).toString();
        String tokenprimero = "vacio";
        try {
            tokenprimero = datosGlobales.get(Constantes.TOKEN_PRIMERO).toString(); //Es el token que guardamos anteriormente
        } catch (Exception e) {

        }

        final String newtoken = (UUID.randomUUID().toString().replaceAll("-", "")); //Genero un UUID para utilizarlo como nuevo token

        browser.navigateTo("http://" + host + ":" + port + "/limspinf-ltest/expireToken?action=update&oldToken=" + tokenprimero + "&refApp=" + refapp + "&provider=apple&newToken=" + newtoken + "");
        logger.info("Nuevo Token:" + newtoken + "\nInvocando URL de modificacion de Token: http://" + host + ":" + port + "/limspinf-ltest/expireToken?action=update&oldToken=" + tokenprimero + "&refApp=" + refapp + "&provider=apple&newToken=" + newtoken + "");

        if (browser.containsText(browser.byXPath("/html/body"), "ok")) {
            logger.info("Invocacion correcta.\nEntrando a verificar si Token antiguo ha sido modificado");
        } else {
            logger.error("Error en la invocacion de http://" + host + ":" + port + "/limspinf-ltest/expireToken?action=update&oldToken=" + tokenprimero + "&refApp=" + refapp + "&provider=apple&newToken=" + newtoken + "");
            throw new Exception(" ");
        }
    }


/* -----------------------------------------------------------------------//------------------------------------------------------- */

    /**
     * Elimina todas las M-Apps del usuario pasado por parametro
     */


    @Step("Delete Mapp from User <>")
    public void deleteMAppfromUser(String user) throws Exception {

        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();


        if (browser.select("idCompany").exists()) {
            browser.select("idCompany").choose(empresa);
        }

        browser.image("w_search.png").click();
        browser.textbox("keyValue").setValue(user);
        browser.submit("btn-search").click();


        if (browser.link("M-Apps").exists()) {
            browser.click(browser.link("M-Apps"));
        } else {
            logger.error("El link M-Apps no se encuentra");
            throw new Exception(" ");
        }

        if (browser.div("delete").exists()) {
            while (browser.div("delete").exists()) {
                browser.click(browser.div("delete"));
                logger.info("Se ha eliminado M-App del usuario");
            }
            logger.info("Se han eliminado todas las M-Apps del usuario");

        } else {
            logger.info("No existen M-Apps para el usuario");
        }
    }


    /**
     * Elimina una propiedad específica de un usuario específico
     *
     * @param refUser
     * @param propiedad
     * @throws Exception
     */
    @Step("Eliminar Propiedad <> <>")
    public void deleteProp(String refUser, String propiedad) throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();

        if (browser.select("idCompany").exists()) {
            browser.select("idCompany").choose(empresa);
        }
        browser.image("w_search.png").click();
        browser.textbox("keyValue").setValue(refUser);
        browser.submit("btn-search").click();
        if (browser.image("w_man.png").exists()) {
            browser.image("w_man.png").click();
            if (browser.cell(propiedad).exists()) {
                if (browser.link("/Borrar/").rightOf(browser.cell(propiedad)).exists()) {
                    browser.link("/Borrar/").rightOf(browser.cell(propiedad)).click();
                    logger.info("Se ha eliminado la propiedad " + propiedad);
                } else {
                    logger.error("No se ha podido eliminar la propiedad " + propiedad);
                    throw new Exception(" ");
                }
            } else {
                logger.info("La propiedad " + propiedad + " no existe");
            }
        } else {
            logger.error("El link Datos no se encuentra");
            throw new Exception(" ");
        }

    }

    /**
     * Establece el valor de una propiedad específica de un usuario específico
     *
     * @param refUser
     * @param propiedad
     * @param valor
     * @throws Exception
     */
    @Step("Establecer Propiedad <> <> <>")
    public void addProp(String refUser, String propiedad, String valor) throws Exception {
        String propName = "propName";
        String propValue = "propValue";
        String btn_btn_mini_btn_primary = "btn btn-mini btn-primary";

        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();

        if (browser.select("idCompany").exists()) {
            browser.select("idCompany").choose(empresa);
        }
        browser.image("w_search.png").click();
        browser.textbox("keyValue").setValue(refUser);
        browser.submit("btn-search").click();
        if (browser.image("w_man.png").exists()) {
            browser.image("w_man.png").click();

            browser.select(propName).choose(propiedad);
            browser.textbox(propValue).setValue(valor);
            browser.submit(btn_btn_mini_btn_primary).click();
            logger.info("Se ha agregado " + propiedad + "----> " + valor);

        } else {
            logger.error("El link Datos no se encuentra");
            throw new Exception(" ");
        }

    }

    /**
     * Busca un usuario pasado por parámetro
     *
     * @param refUser
     * @return
     */
    private boolean searchSuscriptor(String refUser) {
        browser.image("w_search.png").click();
        browser.select("keyName").choose("#refUser");
        browser.textbox("keyValue").setValue(refUser);
        browser.submit("btn-search").click();
        if (browser.cell(refUser).rightOf(browser.cell("/(#refUser)/")).exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Agrega un nuevo suscriptor
     *
     * @param refUser
     * @throws Exception
     */
    private void newSuscriptor(String refUser) throws Exception {
        browser.image("w_search.png").click();
        if (browser.textbox("refUser").exists()) {
            browser.textbox("refUser").setValue(refUser);
        } else {
            logger.error("No se encuentra el textbox 'refuser'. Comprueba que 'ADMIN_MODE=TRUE'");
            throw new Exception(" ");
        }
        browser.textbox("name").setValue(refUser + " creado con Automated");
        browser.textbox("description").setValue("Descripcion de: " + refUser + ", que es cliente");
        browser.submit("btn-create").click();
        Thread.sleep(500);
        if (searchSuscriptor(refUser)) {
            logger.info("Se ha creado correctamente el suscriptor " + refUser);
        } else {
            logger.error("El suscriptor no se ha creado correctamente");
            throw new Exception(" ");
        }
    }

    /**
     * verificamos graficamente que existan las propiedades de un usuario pasadas por una lista.
     */
    @Step("Existen Propiedades")
    public void existProperties() {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String refUser = datosGlobales.get(Constantes.REF_USER).toString();
        List<Property> props = (List<Property>) datosGlobales.get(Constantes.PROPS);
        int size;

        if (browser.select("idCompany").exists()) {
            browser.select("idCompany").choose(empresa);
        }
        browser.image("w_search.png").click();
        browser.textbox("keyValue").setValue(refUser);
        browser.submit("btn-search").click();
        if (browser.image("w_man.png").exists()) {
            browser.image("w_man.png").click();
        }
        for (int i = 0; i < props.size(); i++) {
            if (props.get(i).getValues().size() > 1) {
                size = props.get(i).getValues().size();
                for (int j = 0; j < size; j++) {
                    if (browser.cell(props.get(i).getName() + "[" + j + "]").exists()) {
                        logger.info("Existe propiedad " + props.get(i).getName() + "[" + j + "]");
                    } else {
                        logger.error("No existe la propiedad " + props.get(i).getName() + "[" + j + "]");
                    }
                }
            } else {
                if (browser.cell(props.get(i).getName()).exists()) {
                    logger.info("Existe propiedad " + props.get(i).getName());
                } else {
                    logger.error("No existe la propiedad " + props.get(i).getName());
                }
            }
        }

    }

    @Step("reenviar mensaje <>")
    public void reenviarMensaje(String tipo) throws Exception {
        tipo = tipo.toUpperCase();
        String randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
        //Se verifica si existe pestaña 360°
        if (browser.image("w_360.png").exists()) {
            browser.image("w_360.png").click();
            if (browser.cell("/(.*)" + tipo + randomNum + "(.*)/").exists()) {
                logger.info("Testing *BUG:SDP-553*");
                if (browser.italic("/(.*)" + "icon-repeat" + "(.*)/").leftOf(browser.cell(tipo).leftOf(browser.cell("/(.*)" + tipo + randomNum + "(.*)/"))).exists()) {
                    browser.italic("/(.*)" + "icon-repeat" + "(.*)/").leftOf(browser.cell(tipo).leftOf(browser.cell("/(.*)" + tipo + randomNum + "(.*)/"))).click();

                    browser.navigateTo("", true);

                    if (browser.cell("/(.*)" + tipo + randomNum + "(.*)/").exists()) {
                        //Guardo el contenido del mensaje con el criterio del randomNum, ya que para buscar dos veces el mismo mensaje no lo permite con expresiones regulares
                        //Debe ser el contenido completo
                        String contenidoMsg = browser.cell("/(.*)" + tipo + randomNum + "(.*)/").getText();

                        datosGlobales.put(Constantes.CONTENIDO_MSG, contenidoMsg);

                        if (browser.cell(contenidoMsg + "[1]").exists()) {
                            logger.info("EXISTEN los dos mensajes en CMC 360°");
                            logger.info("BUG-SDP-553 Superado -  EXISTEN los dos mensajes en CMC 360°");
                        } else {
                            logger.error("BUG-SDP-553 Encontrado - No existen los dos mensajes");
                            throw new Exception(" ");
                        }
                    }
                } else {
                    logger.error("No existe el icono para reenviar el mensaje");
                    throw new Exception(" ");
                }
            } else {
                logger.error("No existe mensaje con el criterio " + randomNum);
                throw new Exception(" ");
            }
        } else {
            logger.error("No existe la pestaña 360°");
            throw new Exception(" ");
        }
    }
//----------------------------------------------------WS----------------------------------------------------------------

    /**
     * Crea un suscriptor a partir del llamado WS
     *
     * @throws Exception_Exception
     */
    @Step("Crear suscriptor WS")
    public void crearSuscriptorWS() throws Exception_Exception {

        String organizacion = datosGlobales.get(Constantes.ORGANIZACION).toString();
        String refUser = datosGlobales.get(Constantes.REF_USER).toString();
        String nombreUsuario = "nombre usuario de "
                + organizacion;
        String descripcionUsuario = "descripcion usuario de " + organizacion;
        ProvisionerUser provisionerUser;
        provisionerUser = (ProvisionerUser) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_USER, Constantes.WLURL_PROVISIONER_USER,
                Constantes.WS_INF_PROVISIONER_USER_SERVICE, Constantes.WS_INF_PROVISIONER_USER_METHOD);
        String refCompany = organizacion.toUpperCase();
        if (provisionerUser.getCompanyUser("", refCompany, refUser) == null) {
            provisionerUser.createUser("", refCompany, refUser, nombreUsuario, descripcionUsuario, "");
            logger.info("Se ha creado el suscriptor " + refUser);
            //Las lineas (else if) siguientes se tienen en cuenta para alerta técnica INF-439
//        }else if (provisionerUser.getCompanyUser("", refCompany, refUser).getRefUser() == null){
//            provisionerUser.createUser("", refCompany, refUser, nombreUsuario, descripcionUsuario, "");
//
        } else {
            logger.info("El suscriptor " + refUser + " ya existe");
            }
    }

    /**
     * Permite eliminar un suscriptor
     *
     * @param organizacion organización a la que pertenece el usuario
     * @param refUser      referencia del usuario a eliminar
     * @throws Exception_Exception
     */
    @Step("Eliminar suscriptor WS <> <>")
    public void eliminarSuscriptorWS(String organizacion, String refUser) throws Exception_Exception {
        ProvisionerUser provisionerUser;
        provisionerUser = (ProvisionerUser) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_USER, Constantes.WLURL_PROVISIONER_USER,
                Constantes.WS_INF_PROVISIONER_USER_SERVICE, Constantes.WS_INF_PROVISIONER_USER_METHOD);
        String refCompany = organizacion.toUpperCase();
        boolean eliminar;
        eliminar = provisionerUser.deleteUser("", refCompany, refUser);
        if (eliminar) {
            logger.info("Se ha eliminado el usuario " + refUser);
        } else {
            logger.info("No se ha podido eliminar el usuario " + refUser + " compruebe si no existe restricción");
        }
    }



}
