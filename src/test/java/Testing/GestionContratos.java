package Testing;// JUnit Assert framework can be used for verification

import LData_Testing.*;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GestionContratos {

    private static Logger logger = LogManager.getLogger(GestionContratos.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private Browser browser;
    AccesoWSLData accesoWSLData;
    GestionDeEmpresas gestionDeEmpresas;
    //GestionDeCanales gestionDeCanales;
    MChannel mChannel;
    AltaAplicacion altaAplicacion;
    LatiniaScenarioUtil latiniaScenarioUtil;

    public GestionContratos() {
        this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
        this.accesoWSLData = new AccesoWSLData();
        this.gestionDeEmpresas = new GestionDeEmpresas();
        //gestionDeCanales = new GestionDeCanales();
        this.mChannel = new MChannel();
        this.altaAplicacion = new AltaAplicacion();
        this.latiniaScenarioUtil = new LatiniaScenarioUtil();
    }

    @Step("SinClause <>")
    public void sinClause(String forzarrecarga) throws Exception {
        //Crea contrato sin clausulas, pasados como parámetro
        int cambios = 0;
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        //String ref = datosGlobales.get("refcontract").toString();
        String nomcontrato = datosGlobales.get(Constantes.NOM_CONTRATO).toString();

        if (browser.image("/ico_contratos/").rightOf(browser.cell(empresa)).exists()) {

            browser.image("/ico_contratos/[0]").rightOf(browser.cell(empresa)).click();

            if (browser.cell(nomcontrato + " Automated").exists()) {
                logger.info("'" + nomcontrato + " Automated' ya existe");
            } else {
                browser.image("/button_create_contract/").click();
                browser.select("id_product2").choose("/" + nomcontrato + "/");
                browser.textbox("description").setValue(nomcontrato + " Automated");
                browser.textbox("dfin").setValue("01/01/2099");
                browser.textbox("hfin").setValue("23:59:59");
                browser.submit("[0]").click();
                browser.expectConfirm("¿Los datos de creación del contrato son correctos?", true);
                browser.button("[0]").click();
                if (browser.cell(nomcontrato + " Automated").exists()) {
                    logger.info("Creado '" + nomcontrato + " Automated'");
                } else {
                    logger.error("ERR: Creando '" + nomcontrato + " Automated'");
                    throw new Exception(" ");
                }

                cambios = 1;
            }

        } else {
            logger.error("No se encuentra icono 'ico_contratos.gif' al lado de la empresa " + empresa + " para crear contratos");
            throw new Exception(" ");
        }

        if (browser.link("header-btn-home").exists()) { //Desde las primeras releases de R3.9.4
            browser.link("header-btn-home").click();
        }


        if (browser.span("hbtn home").exists()) { //Ultima release de R3.9.4
            browser.span("hbtn home").click();
        }

        if (cambios == 1 && forzarrecarga.equals("forzarrecarga")) {
            browser.link("Forzar recarga").click();
            browser.button("Volver").click();
        }

    }

    /**
     * Crea un contrato con clausulas
     *
     * @param forzarrecarga
     * @throws Exception
     */
    @Step("ConClause <\"\">")
    public void conClause(String forzarrecarga) throws Exception {
        int cambios = 0;
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String refcontract = datosGlobales.get(Constantes.REF_CONTRACT).toString();
        String nomcontrato = datosGlobales.get(Constantes.NOM_CONTRATO).toString();
        String tipoclause = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();

        if (browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).exists()) {

            browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).click();

            if (browser.cell(nomcontrato + " Automated").exists()) {
                browser.image("ico_clausulas.gif").rightOf(browser.cell(nomcontrato + " Automated")).click();
                browser.image("button_create_clauses.gif").click();

            } else { //Si no existia el contrato lo creo, y despues le agrego clausulas
                logger.info("Creando contrato" + nomcontrato + " Automated");
                //System.out.println("INF: Creando contrato" + nomcontrato + " Automated");
                browser.image("button_create_contract.gif").click();
                browser.select("id_product2").choose("/(.*)" + nomcontrato + " - " + "(.*)/");
                browser.textbox("refContract").setValue(refcontract);
                browser.textbox("description").setValue(nomcontrato + " Automated");
                browser.textbox("dfin").setValue("01/01/2099");
                browser.textbox("hfin").setValue("23:59:59");
                browser.submit("[0]").click();
                browser.expectConfirm("¿Los datos de creación del contrato son correctos?", true);
                browser.button("Añadir cláusulas").click();
            }


            //*BUG:SDP-972* Cuando se produce este bug el escenario no pasará, puesto que la pantalla queda 'en blanco'; el escenario peta


            //Agrego las clausulas
            logger.info("Creando clausula '" + tipoclause + "'");
            //System.out.println("INF: Creando clausula '" + tipoclause + "'");
            if (tipoclause.equalsIgnoreCase("sms")) {
                browser.radio("type[2]").click();
                browser.button("Siguiente >>>").click();
                browser.textbox("ref_alias2").setValue("alias" + empresa + "1");
                browser.select("channel").choose("+000001 Virtual - +000001 Virtual");
                browser.submit("Crear").click();
            }
            if (tipoclause.equalsIgnoreCase("smsMT")) {
                browser.button("Siguiente >>>").click();
                browser.select("channel").choose("Virtual - +000001 Virtual");
//                browser.select("ref_credit2").choose("");
                browser.button("Crear").click();
            }
            if (tipoclause.equalsIgnoreCase("pns")) {
                browser.submit("Siguiente >>>[1]").click();
                browser.select("channel").choose(new String[]{"Google - * Google", "Apple - * Apple"});
                browser.button("Crear").click();
            }
            if (tipoclause.equalsIgnoreCase("email")) {
                browser.submit("Siguiente >>>[0]").click();
                browser.select("channel").choose(new String[]{"EMail-SMTP - comercial@latinia.com EMail-SMTP"});
                //   browser.select("ref_credit2").choose("");
                browser.button("Crear").click();
            }


            browser.expectConfirm("¿Desea crear la cláusula?", true);
            browser.button("/Volver/").click();
            browser.button("/Volver/").click();
            cambios = 1;

        } else {
            logger.error("No se encuentra icono 'ico_contratos.gif'");
            throw new Exception(" ");
        }


        if (browser.span("hbtn home").exists()) { //Ultima release de R3.9.4
            browser.span("hbtn home").click();
        }

        if (cambios == 1 && forzarrecarga.equals("forzarrecarga")) {
            browser.link("Forzar recarga").click();
            browser.button("Volver").click();
        }

    }


    /**
     * Crea un contrato duplicado y comprueba dos cosas
     * 1.- Que no permite crearlo
     * 2.- Que el mensaje que se muestra como error es el esperado para este tipo de evento
     * "El contrato "dsf" no ha podido ser creado correctamente. Revise la posible colision de parametros con otros contratos."
     *
     * @throws Exception
     */
    public void conDuplicado() throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String refcontract = datosGlobales.get(Constantes.REF_CONTRACT).toString();
        String nomcontrato = datosGlobales.get(Constantes.NOM_CONTRATO).toString();

        if (browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).exists()) {

            browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).click();

            if (browser.cell(nomcontrato + " " + Constantes.AUTOMATED).exists()) { //Si ya existia el contrato, creo uno duplicado (lo cual se espera que falle)
                browser.image("button_create_contract.gif").click();
                browser.select("id_product2").choose("/" + nomcontrato + "/");
                browser.textbox("refContract").setValue(refcontract);
                browser.textbox("description").setValue(nomcontrato + " " + Constantes.AUTOMATED);
                browser.textbox("dfin").setValue("01/01/2099");
                browser.textbox("hfin").setValue("23:59:59");
                browser.submit("[0]").click();
                browser.expectConfirm("¿Los datos de creación del contrato son correctos?", true);

                //Si muestra el mensaje de error correspondiente, no hago nada. De lo contrario
                if (browser.cell("/Revise la posible colision de parametros con otros contratos./").exists()) {

                } else {

                }


            }
        } else {


        }


        if (browser.span("hbtn home").exists()) { //Ultima release de R3.9.4
            browser.span("hbtn home").click();
        }

    }


    @Step("EliminaContrato")
    public void eliminaContrato() throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String nomcontrato = datosGlobales.get(Constantes.NOM_CONTRATO).toString();

        //Comprueba si existe el contrato, y en tal caso lo borra
        browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).click();

        if (browser.cell(nomcontrato + " Automated").exists()) {
            browser.image("ico_delete.gif").rightOf(browser.cell(nomcontrato + " Automated")).click();
            browser.expectConfirm("¿Desea eliminar el contrato '" + nomcontrato + " Automated'?", true);
            browser.button("Contratos").click();
        }


        if (browser.image("button_inicio.gif").exists()) { //Hasta R3.9.3
            browser.image("button_inicio.gif").click();
        }


        if (browser.link("header-btn-home").exists()) { //Desde las primeras releases de R3.9.4
            browser.link("header-btn-home").click();
        }


        if (browser.span("hbtn home").exists()) { //Ultima release de R3.9.4
            browser.span("hbtn home").click();
        }

    }


    /**
     * Modifica las propiedades de un contrato
     *
     * @param nomcontrato
     * @param propiedad
     * @param estado
     * @throws Exception
     */

    @Step("ConProps < > < > < >")
    public void conProps(String nomcontrato, String propiedad, String estado) throws Exception {
        //Activación de propiedades de contrato
        boolean existProperty = false;
        String[] listProperties = (String[]) datosGlobales.get(Constantes.LIST_PROPERTIES);

        if (listProperties[0].equalsIgnoreCase(Constantes.TEST)) {
            System.out.println();
            existProperty = true;
        } else {
            for (int i = 0; i < listProperties.length && !existProperty; i++) {
                if (listProperties[i].equalsIgnoreCase(propiedad)) {
                    existProperty = true;
                }
            }
        }

        if (existProperty) {
            logger.info("Existe PROP " + propiedad);
            //int cambios = 0;
            int existe = 0;
            String empresa = datosGlobales.get(Constantes.EMPRESA).toString();


            if (browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).exists()) {

                browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).click();

                if (browser.cell(nomcontrato).exists()) {
                    browser.image("ico_propiedades.gif").rightOf(browser.cell(nomcontrato)).click();

                    try {
                        browser.select("prod_prop").choose("/" + propiedad + "/");
                        existe = 1;
                    } catch (Exception e) {
                        logger.warn("No se encuentra '" + propiedad + "', en las propiedades de aplicación " + e.toString());
                    }

                    if (existe == 1) {
                        browser.button(">>>").click();
                        browser.select("cont_prop").choose("/" + propiedad + "/");
                    } else {
                        browser.select("cont_prop").choose("/" + propiedad + "/");
                    }
                }


//                if (estado.equalsIgnoreCase("true") || estado.equalsIgnoreCase("false") || estado.equalsIgnoreCase("APPLICATION") || estado.equalsIgnoreCase("CONTRACT")) { //Cuando es 'true' o 'false' debe ir en MAYÚSCULAS, de lo contrario todo a minúsculas
//                    estado = estado.toUpperCase();
//                } else {
//                    estado = estado.toLowerCase();
//                }
//
                browser.button("editar").click();

                if (browser.select("combo_edit").isVisible()) {
                    if (estado.equalsIgnoreCase("*")) {
                        browser.select("combo_edit").choose("*");
                    } else {
                        browser.select("combo_edit").choose(estado);
                    }
                    browser.button("button_set").click();
                } else if (browser.textbox("text_edit").isVisible()) {
                    estado = estado.toUpperCase();
                    browser.textbox("text_edit").setValue(estado);
                    browser.button("button_set[1]").click();
                }
//
//
//                if (estado.equalsIgnoreCase("*")) {
//
//                    browser.select("combo_edit").choose("*"); //Tengo que hacer esta guarrada porque cuando 'estado = *', no lo coge bien
//                    browser.button("button_set").click();
//
//                } else if (propiedad.equalsIgnoreCase("ORGANIZATION")) {//Hago esto para que me pueda tomar un valor que no esté en el select
//                    estado = estado.toUpperCase();
//                    browser.textbox("text_edit").setValue(estado);
//                    browser.button("button_set[1]").click();
//
//                } else {
//                    browser.select("combo_edit").choose(estado);
//
//                    browser.button("button_set").click();
//                }

            } else {
                logger.error("No se encuentra icono 'ico_contratos.gif'");
                throw new Exception(" ");
            }
        } else {
            logger.info("Atencion NO Existe la propiedad " + propiedad + " en la licencia, las pruebas relacionadas con esta propiedad seran ingnoradas. Se recomienda comprobar que esto sea apropiado");
        }

        if (browser.image("button_inicio.gif").

                exists()) { //Hasta R3.9.3
            browser.image("button_inicio.gif").click();
        }


        if (browser.link("header-btn-home").

                exists()) { //Desde las primeras releases de R3.9.4
            browser.link("header-btn-home").click();
        }


        if (browser.span("hbtn home").

                exists()) { //Ultima release de R3.9.4
            browser.span("hbtn home").click();
        }


    }


    //En construccion//

    /**
     * Elimina una clausula y comprueba que se ha eliminado efectivamente
     */
    @Step("eliminaClausula")
    public void eliminaClausula() throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String nomcontrato = datosGlobales.get(Constantes.NOM_CONTRATO).toString();
        String tipoclause = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();

        if (browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).exists()) {

            browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).click();

            if (browser.cell(nomcontrato + " Automated").exists()) { //Si ya existia el contrato le agrego clausulas al ya existente
                browser.image("ico_clausulas.gif").rightOf(browser.cell(nomcontrato + " Automated")).click();

                if (tipoclause.equalsIgnoreCase("email")) {
                    if (browser.image("ico_delete.gif").rightOf(browser.cell("EMail-SMTP")).exists()) {
                        browser.image("ico_delete.gif").rightOf(browser.cell("EMail-SMTP")).click();
                        browser.expectConfirm("/Desea eliminar la/", true);
                        if (browser.cell("/La cláusula ha sido eliminada/").exists()) {
                            browser.button("/Volver/").click();
                        }
                    }

                    if (browser.cell("EMail-SMTP").exists()) {
                        logger.error("No se ha borrado la clausula de tipo '" + tipoclause + "'");
                        throw new Exception(" ");
                    }

                }
            }
        }

        if (browser.span("hbtn home").exists()) {
            browser.span("hbtn home").click();
        } else {
            logger.error("Reventada al borrar clausula. No se encuentra boton HOME");
            throw new Exception(" ");
        }


    }

    @Step("forzarrecarga")
    public void forzarrecarga() throws Exception {

        if (browser.link("header-btn-home").exists()) { //Desde las primeras releases de R3.9.4
            browser.link("header-btn-home").click();
        }


        if (browser.span("hbtn home").exists()) { //Ultima release de R3.9.4
            browser.span("hbtn home").click();
        }

        browser.link("Forzar recarga").click();
        browser.button("Volver").click();

    }

    @Step("Cambiar Propiedad Contrato <>")
    public void cambiarPropiedadContrato(String propiedad) {
        String contrato = datosGlobales.get(Constantes.NOM_CONTRATO).toString();
        String proposito = datosGlobales.get(Constantes.PROPOSITO).toString();
        String valor = "";
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        boolean eProperty;
        //Recorro la lista de propiedades que se encuentra en la licencia y verifico si existe o no la propiedad que entra por parámetro
        boolean existePropiedad = false;
        String[] listaPropiedades = (String[]) datosGlobales.get(Constantes.LIST_PROPERTIES);

        if (listaPropiedades[0].equalsIgnoreCase(Constantes.TEST)) {
            System.out.println();
            existePropiedad = true;
        } else {
            for (int i = 0; i < listaPropiedades.length && !existePropiedad; i++) {
                if (listaPropiedades[i].equalsIgnoreCase(propiedad)) {
                    existePropiedad = true;
                }
            }
        }
        if (existePropiedad) {
            if (browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).exists()) {
                browser.image("ico_contratos.gif").rightOf(browser.cell(empresa)).click();
                if (browser.cell(contrato).exists()) {
                    browser.image("ico_propiedades.gif").rightOf(browser.cell(contrato)).click();
                    if (browser.select("cont_prop").getText().contains(propiedad)) {
                        eProperty = true;
                        logger.info("Existe la propiedad CONTENT_TEMPLATE");
                    } else {
                        logger.info("No existe la propiedad CONTENT_TEMPLATE en el contrato");
                        eProperty = false;
                    }
                    if (eProperty) {
                        browser.select("cont_prop").choose("/" + propiedad + "/");
                        browser.button("editar").click();
                        if (proposito.equalsIgnoreCase("devolver")) {
                            valor = datosGlobales.get(Constantes.VALUE_CONTENT).toString();
                        } else if (proposito.equalsIgnoreCase("cambiar")) {
                            String[] valuecontent = browser.select("combo_edit").getSelectedText();
                            datosGlobales.put(Constantes.VALUE_CONTENT, valuecontent);
                            valor = "NONE";
                        }
                        browser.select("combo_edit").choose("/" + valor + "/");
                        browser.button("button_set").click();
                    }
                }
            }
        } else {
            logger.info("Atencion NO Existe la propiedad " + propiedad + " en la licencia, las pruebas relacionadas con esta propiedad seran ingnoradas. Se recomienda comprobar que esto sea apropiado");
        }
    }

    //---------------------REFACTORY-----------------------------//
    //-----------------------GUI---------------------------------//
    @Step("crear Contrato <> <> <>")
    public void crearContrato(String refContract, boolean clausula, boolean forzarrecarga) throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String tipoClausula;
        int cambios = 0;
        String nomproducto = datosGlobales.get(Constantes.NOM_CONTRATO).toString();
        String nomcontrato = nomproducto + " " + Constantes.AUTOMATED;

        if (browser.image("/ico_contratos/").rightOf(browser.cell(empresa)).exists()) {
            browser.image("/ico_contratos/[0]").rightOf(browser.cell(empresa)).click();

            if (browser.cell(nomcontrato).exists()) {
                logger.info("'" + nomcontrato + "' ya existe");
                if (clausula) {
                    tipoClausula = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();
                    browser.image("ico_clausulas.gif").rightOf(browser.cell(nomcontrato)).click();
                    browser.image("button_create_clauses.gif").click();
                    agregarClausula(tipoClausula);
                    cambios = 1;
                }
            } else {
                nuevoContrato(nomproducto, refContract, nomcontrato);
                //browser.button("[0]").click();
                if (!clausula) {
                    browser.button("Lista de contratos").click();
                    if (browser.cell(nomcontrato).exists()) {
                        logger.info("Creado '" + nomcontrato + "'");
                    } else {
                        logger.error("Creando '" + nomcontrato + "'");
                        throw new Exception(" ");
                    }
                } else {
                    browser.button("Añadir cláusulas").click();
                    logger.info(" Creado '" + nomcontrato + "'");
                    tipoClausula = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();
                    agregarClausula(tipoClausula);
                }
                cambios = 1;
            }
        } else {
            logger.error("No se encuentra icono 'ico_contratos.gif' al lado de la empresa " + empresa + " para crear contratos");
            throw new Exception(" ");
        }
        if (browser.span("hbtn home").exists()) { //Ultima release de R3.9.4
            browser.span("hbtn home").click();
        }

        if (cambios == 1 && forzarrecarga) {
            browser.link("Forzar recarga").click();
            browser.button("Volver").click();
        }
    }

    public void nuevoContrato(String nomproducto, String refContract, String nomcontrato) {
        browser.image("/button_create_contract/").click();
        browser.select("id_product2").choose("/" + nomproducto + "/");
        browser.textbox("refContract").setValue(refContract);
        browser.textbox("description").setValue(nomcontrato);
        browser.textbox("dfin").setValue("01/01/2099");
        browser.textbox("hfin").setValue("23:59:59");
        browser.submit("[0]").click();
        browser.expectConfirm("¿Los datos de creación del contrato son correctos?", true);
    }

    public void agregarClausula(String tipoclause) {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        logger.info("Creando clausula '" + tipoclause + "'");
        if (tipoclause.equalsIgnoreCase("sms")) {
            browser.radio("type[2]").click();
            browser.button("Siguiente >>>").click();
            browser.textbox("ref_alias2").setValue("alias" + empresa + "1");
            browser.select("channel").choose("+000001 Virtual - +000001 Virtual");
            browser.submit("Crear").click();
        }
        if (tipoclause.equalsIgnoreCase("smsMT")) {
            browser.button("Siguiente >>>").click();
            browser.select("channel").choose("Virtual - +000001 Virtual");
//                browser.select("ref_credit2").choose("");
            browser.button("Crear").click();
        }
        if (tipoclause.equalsIgnoreCase("pns")) {
            browser.submit("Siguiente >>>[1]").click();
            browser.select("channel").choose(new String[]{"Google - * Google", "Apple - * Apple"});
            browser.button("Crear").click();
        }
        if (tipoclause.equalsIgnoreCase("email")) {
            browser.submit("Siguiente >>>[0]").click();
            browser.select("channel").choose(new String[]{"EMail-SMTP - comercial@latinia.com EMail-SMTP"});
            //   browser.select("ref_credit2").choose("");
            browser.button("Crear").click();
        }
        browser.expectConfirm("¿Desea crear la cláusula?", true);
        browser.button("/Volver/").click();
        browser.button("/Volver/").click();
    }
}

