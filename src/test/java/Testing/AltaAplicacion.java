package Testing;

import LData_Testing.AccesoWSLData;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class AltaAplicacion {

    private static Logger logger = LogManager.getLogger(AltaAplicacion.class);
    private final Browser browser;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    AccesoWSLData accesoWSLData;

    public AltaAplicacion() {
        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
        accesoWSLData = new AccesoWSLData();
    }


    /**
     * Crea una aplicacion
     *
     * @throws Exception
     */
    @Step("Crear <refapp>")
    public void crear(String refapp) throws Exception {

        //Si no le hemos pasado el nombre de la app, pues trabajaremos con 'MiAplicacionPIR'.
        if (refapp.equals("")) {
            refapp = "MiAplicacionPIR";
        }


        //Si ya existe la borro, pero solamente en caso que estemos trabajando con 'MiAplicacionPIR'
        if (refapp.equals("MiAplicacionPIR")) {
            if (browser.link(refapp).exists()) {
                browser.image("ico_delete.gif").rightOf(browser.link(refapp)).click();
                Thread.sleep(1000);
                browser.expectConfirm("¿Desea eliminar la aplicación '" + refapp + "'?", true);
                browser.button("Inicio").click();
            }
            Thread.sleep(500);
            if (browser.link(refapp).exists()) {
                logger.error("Ha existido un problema borrando la Aplicacion '" + refapp + "'");
                throw new Exception(" ");
            }
        }


        if (browser.link(refapp).exists()) {
            logger.info("Existe la aplicación " + refapp);
        } else {

            //Crea la aplicación
            browser.textbox("ref_product").setValue(refapp);
            if (refapp.equals("wtest")) { //El refProduct 'wtest' esta reservado para la App que utiliza el componente de pruebas WTEST
                browser.textbox("name").setValue("Push_Testing_y_Diagnosis");
            } else {
                browser.textbox("name").setValue(refapp);
            }
            browser.submit("[0]").rightOf(browser.textbox("name")).click();
            browser.expectConfirm("¿Los datos de creación de la aplicación son correctos?", true);
            browser.button("Plataformas").click();
            browser.checkbox("LIMSP").click();
            if (refapp.equals("MiAplicacionPIR")) browser.checkbox("LGUI").click();
            browser.submit("Actualizar").click();

            browser.link("DELIVERY_REPORT").click();
            browser.select("prop_value").choose("TRUE - Solicitar estado del mensaje");

            browser.checkbox("export").click();
            browser.submit("Actualizar").click();

            if (browser.link("PNS_APP").exists()) { //Hago la propiedad visible en el contrato
                browser.link("PNS_APP").click();
                browser.checkbox("export").click();
                browser.submit("Actualizar").click();
            }

            //*BUG:SDP-964*
            System.out.println("INF: Testing *BUG:SDP-964*");
            if (browser.link("INOT_CHN_PREF").exists()) {
                browser.link("INOT_CHN_PREF").click();
//				if ((browser.select("prop_value").containsText("CONTRACT - Las preferencias se establecen desde las claúsulas del contrato")) && (browser.select("prop_value").containsText("APP_CONTRACT - Las preferencias se establecen según el orden: Aplicación y Contrato")) && (browser.select("prop_value").containsText("USER_CONTRACT - Las preferencias se establecen según el orden: Usuario y Contrato")) && (browser.select("prop_value").containsText("USER_APP_CONTRACT - Las preferencias se establecen según el orden: Usuario, Aplicación y Contrato"))) {
                //if ((browser.select("prop_value").containsText("CONTRACT - Las preferencias se establecen desde las claúsulas del contrato")) && (browser.select("prop_value").containsText("APP_CONTRACT - Las preferencias se establecen según el orden: Aplicación y Contrato"))) {
                if ((browser.select("prop_value").containsText("CONTRACT - Las preferencias se establecen desde las cláusulas del contrato")) && (browser.select("prop_value").containsText("APP_CONTRACT - Las preferencias se establecen según el orden: Aplicación y Contrato"))) {
                    browser.select("prop_value").choose("APP_CONTRACT - Las preferencias se establecen según el orden: Aplicación y Contrato");
                    //browser.select("prop_value").choose("USER_CONTRACT - Las preferencias se establecen según el orden: Usuario y Contrato");
                    //browser.select("prop_value").choose("USER_APP_CONTRACT - Las preferencias se establecen según el orden: Usuario, Aplicación y Contrato");
                    browser.select("prop_value").choose("CONTRACT - Las preferencias se establecen desde las cláusulas del contrato");
                    browser.submit("Actualizar").click();
                    logger.info("Superado  *BUG:SDP-964*");

                } else {
                    logger.error("Se ha encontrado el *BUG:SDP-964*");
                    throw new Exception(" ");
                }
            }

            browser.button("Inicio").click();
        }
    }


    /**
     * Agregar ROLES a una APP
     *
     * @param app
     * @param role
     * @throws Exception
     * @author @xruizs
     */
    @Step("Roles <app> <role>")
    public void roles(String app, String role) throws Exception {
        browser.image("ico_roles.gif").rightOf(browser.cell(app)).click();
        if (browser.checkbox("checkPassw").exists()) {
            browser.checkbox("checkPassw").click();
            browser.textbox("chpasswd").setValue("latinia");
            browser.submit("Aceptar").click();
            browser.checkbox("checkPassw").click();
            browser.checkbox("checkPassw").click();
        }

        //Este IF es porque por configuración, el campo 'np' podria ser de tipo TEXT o de tipo PASSWORD. En lconfig propiedad: config/showProductPasswords =
        if (browser.submit("Editar").exists()) {
            browser.submit("Editar").click();
            if (browser.password("np").exists()) {
                browser.password("np").setValue(Constantes.LATINIA);
                browser.password("vnp").setValue(Constantes.LATINIA);
            } else {
                browser.textbox("np").setValue(Constantes.LATINIA);
                browser.textbox("vnp").setValue(Constantes.LATINIA);
            }
            browser.button("Aceptar").click();
            browser.expectConfirm("¿Desea cambiar la contraseña?", true);
        }

        if (browser.select("roleadd").containsText(role)) {
            browser.select("roleadd").choose(role);
            browser.submit(">>>").click();
        }
        Thread.sleep(1000);
        browser.button("Inicio").click();
        browser.link(app).click();
        browser.button("Inicio").click();

    }


    @Step("Roles <app> <role> <accion>")
    /*
     * Agregar o Quitar ROLE a una Aplicacion
	 * @throws Exception
	 * @author xruizs
	 */
    public void roles(String app, String role, String accion) throws Exception {
        Boolean asignado = false;
        role = role.toUpperCase();

        browser.image("ico_roles.gif").rightOf(browser.cell(app)).click();

        //Compruebo si en la plataforma existe dicho role
        if (browser.select("roleadd").containsText(role)) {
            asignado = false;
        } else if (browser.select("rolesub").containsText(role)) {
            asignado = true;
        } else {
            logger.error("El role " + role + " no existe para la aplicacion " + app);
            throw new Exception(" ");
        }


        //Agregar role si necesario
        if (asignado == false && accion.equalsIgnoreCase("agregar")) {
            browser.select("roleadd").choose(role);
            browser.submit(">>>").click();
            if (browser.select("rolesub").containsText(role)) {
                logger.info("Asignado " + role + " a la aplicacion " + app);
            } else {
                logger.error("Error asignando el role " + role.toUpperCase() + " a la aplicacion " + app);
                throw new Exception(" ");
            }
        }

        //Quitar role si necesario
        if (asignado == true && accion.equalsIgnoreCase("quitar")) {
            browser.select("rolesub").choose(role);
            browser.submit("<<<").click();
            if (browser.select("roleadd").containsText(role)) {
                logger.info("Quitado " + role + " a la aplicacion " + app);
            } else {
                logger.error("Error quitando " + role + " a la aplicacion " + app);
                throw new Exception(" ");
            }

        }

        browser.button("Inicio").click();
    }


    /**
     * Agrega el valor de las propiedades
     *
     * @param aplicacion
     * @param propiedad
     * @param valor
     * @throws Exception
     */
    @Step("propiedades <aplicacion> <propiedad> <valor>")
    public void propiedades(String aplicacion, String propiedad, String valor) throws Exception {
        //EL booleano existProperty hace referencia a la existencia de la propiedad en la licencia
        boolean existProperty = false;
        //El booleano existProperty1 hace referencia a la existencia de la propiedad dentro de la lista de propiedades en la aplicación
        boolean existProperty1 = false;
        String[] listProperties = (String[]) datosGlobales.get(Constantes.LIST_PROPERTIES);

        if (listProperties[0].equalsIgnoreCase("TEST")) {
            existProperty = true;
        } else {
            for (int i = 0; i < listProperties.length && !existProperty; i++) {
                if (listProperties[i].equalsIgnoreCase(propiedad)) {
                    existProperty = true;
                }
            }
        }

        if (existProperty) {
            if (browser.image("ico_limsp.gif").rightOf(browser.cell(aplicacion)).exists())
                browser.image("ico_limsp.gif").rightOf(browser.cell(aplicacion)).click();
            if (browser.link(propiedad).exists()) {
                existProperty1 = true;
            } else if (browser.button("Añadir propiedades").exists()) {
                browser.button("Añadir propiedades").click();
                if (browser.checkbox("/" + propiedad + "/").exists()) {
                    browser.checkbox("/" + propiedad + "/").click();
                    browser.submit("Activar").click();
                    existProperty1 = true;
                } else {
                    existProperty1 = false;
                }
            } else {
                logger.info("EL botón \"Añadir propiedades\" no existe, es  posible que no existan propiedades para agregar");
            }

            if (existProperty1) {
                browser.link(propiedad).click();
                if (valor.equalsIgnoreCase("visible")) {
                    browser.checkbox("export").check(); //clico el 'visible en el contrato'
                } else {
                    browser.select("prop_value").choose(valor);
                }
                logger.info("Actualizando la propiedad " + propiedad + " de la aplicacion");
                browser.submit("Actualizar").click();
                if ((!browser.cell(valor).rightOf(browser.cell(propiedad)).exists()) && !valor.equalsIgnoreCase("visible")) {
                    logger.info("Propiedad " + browser.cell(valor).rightOf(browser.link(propiedad)).exists());
                    logger.error("La actualización de la propiedad de aplicacion no ha sido correcta");
                    throw new Exception(" ");
                }
            } else {
                browser.close();
                logger.error("La propiedad " + propiedad + " no existe");
                throw new Exception(" ");
            }
        } else {
            logger.info("Atencion NO Existe la propiedad " + propiedad + "en la licencia, las pruebas relacionadas con esta propiedad seran ingnoradas. Se recomienda comprobar que esto sea apropiado");
        }
    }


    /**
     * Habilita, en "Gestion de Aplicaciones", la propiedad MNG_ORGANIZATIONS; Pero unicamente si la propiedad existe,
     * dado que no en todas las instalaciones existe dicha propiedad
     *
     * @throws Exception
     */
    @Step("EmpresasOrganizaciones")
    public void empresasOrganizaciones() throws Exception {
        String mngorganizations = datosGlobales.get(Constantes.MNG_ORGANIZATIONS).toString();
        if (mngorganizations.equals("true")) {
            browser.image("ico_lgui.gif").rightOf(browser.link("wgestenterprise")).click();
            browser.link(Constantes.MNG_ORGANIZATIONS).click();
            browser.select("prop_value").choose("TRUE - Habilita la gestión de organizaciones");
            browser.submit("[0]").click();
        }

    }


    /**
     * Comprueba si la propiedad/aplicacion MNG_ORGANIZATIONS existe o no,.....
     * El resultado lo guarda en repositorio de DatosGlobales.
     *
     * @throws Exception
     */
    @Step("checkMNG_ORGANIZATIONS")
    public void checkMNG_ORGANIZATIONS() throws Exception {
        String existe = "false";
        browser.image("ico_lgui.gif").rightOf(browser.link("wgestenterprise")).click();
        if (browser.link("MNG_ORGANIZATIONS").exists()) {
            existe = "true";
        }

        datosGlobales.put(Constantes.MNG_ORGANIZATIONS, existe);
        if (existe.equals("false"))
            logger.info("Atencion no existe la propiedad de aplicacion MNG_ORGANIZACTIONS. Las pruebas relacionadas con 'organizaciones' seran ingnoradas. Se recomienda comprobar que esto sea apropiado");

        browser.button("Inicio").click();
    }


    /**
     * Establece el valor de una de las propiedades LGUI de una Aplicacion
     *
     * @param refapp    referencia de la APP en que se va a establecer una propiedad
     * @param nomprop   nombre de la propiedad a la que se le va a establecer el valor
     * @param valorprop valor que se le va a establecer a la propiedad
     * @throws Exception
     */
    @Step("setLGUI <wsubscribers> <ADMIN_MODE > <TRUE>")
    public void setLGUI(String refapp, String nomprop, String valorprop) throws Exception {
        String existe = "false";
        valorprop = valorprop.toUpperCase();

        if (browser.image("ico_lgui.gif").rightOf(browser.cell(refapp)).exists()) {

            browser.image("ico_lgui.gif").rightOf(browser.cell(refapp)).click();

            if (browser.link(nomprop).exists()) {
                if (nomprop.equals(Constantes.MNG_ORGANIZATIONS)) {
                    existe = "true";
                    datosGlobales.put(Constantes.MNG_ORGANIZATIONS, existe);
                    if (existe.equals("false"))
                        logger.info("Atencion no existe la propiedad de aplicacion MNG_ORGANIZACTIONS. Las pruebas relacionadas con 'organizaciones' seran ingnoradas. Se recomienda comprobar que esto sea apropiado");
                }
                browser.link(nomprop).click();
                browser.select("prop_value").choose("/" + valorprop + "/");
                browser.submit("Actualizar").click();
            }

        } else {
            logger.error("No se encuentra el botón LGUI para la aplicacion " + refapp);
            throw new Exception(" ");
        }

        browser.button("Inicio").click();
    }


    /**
     * Verifica en la ventana actual la existencia de uno o varios valores
     *
     * @param lista
     * @throws Exception
     */
    @Step("Verifica Nombres <lista>")
    public void checknombres(String lista) throws Exception {
        String[] nombres = lista.split(",");

        for (String nombre : nombres) {
            if (LatiniaUtil.checkNombre(nombre)) {
                logger.info("Encontrado " + nombre + " ");
            } else {
                logger.error("No se encuentra " + nombre + " ");
                throw new Exception(" ");
            }
        }
    }
}
