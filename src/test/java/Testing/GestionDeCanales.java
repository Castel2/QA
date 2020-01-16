package Testing;// JUnit Assert framework can be used for verification

import LData_Testing.AccesoWSLData;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import com.sahipro.lang.java.client.Browser;
import com.sahipro.lang.java.client.ElementStub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;


public class GestionDeCanales {

    private static Logger logger = LogManager.getLogger(GestionDeCanales.class);
    private Browser browser;
    AccesoWSLData accesoWSLData;
    LatiniaScenarioUtil latiniaScenarioUtil;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

    public GestionDeCanales() {
        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
        accesoWSLData = new AccesoWSLData();
        latiniaScenarioUtil = new LatiniaScenarioUtil();
    }

    @Step("FailOver")
    public void failOver() throws Exception {
        String proposito = datosGlobales.get(Constantes.PROPOSITO).toString();

        //Voy a la pestaña FailOver
        if (browser.link("Failover").exists()) {
            browser.link("Failover").click();
            if (browser.link("failover_act").exists()) {
            } else {
                logger.error("El link de FailOver ha cambiado su referencia. Corregir el código HTML");
                throw new Exception(" ");
            }
        }
        //Si existe ">>>[2]" es porque existen rutas
        if (browser.cell(">>>[2]").exists()) {
            if (browser.cell("Virtual").exists()) {
                int i = 2;
                int ch = 0;
//Con este While elimino todas las rutas porque no he encontrado la forma de borrar la Fila donde se encuentra el Virtual que quiero borrar
                while (ch == 0) {
                    logger.info("Entra al while " + ch);
                    String pasa = ">>>" + "[" + i + "]";
                    if (browser.cell(pasa).exists()) {
                        logger.info("EXISTE AGAIN " + pasa);
                        ElementStub deleteico = browser.image("ico_delete.gif").rightOf(browser.cell(pasa));
                        if (browser.exists(deleteico)) {
                            //Borro la ruta
                            deleteico.click();
                            browser.expectConfirm("¿Desea eliminar la ruta de failover?", true);
                            logger.info("Borrada regla 'Virtual >> Unknown'");
                            if (browser.cell("/Error/").exists()) {
                                logger.error("Problema Borrando la ruta");
                                throw new Exception(" ");
                            } else {
                                browser.button("<< Volver").click();
                            }
                        } else {
                            ch = 1;
                            //Podrian no existir mas deleico del tipo "Virtual >> Unknown 0000001", a no ser que ahora desactive algu checkbox y por tanto aparezca un nuevo deleteico
                            //Desactivo checkbox de dicha ruta para poder después borrarla
                            ElementStub checkbox = browser.checkbox(0).leftOf(browser.cell(pasa));
                            //pasada++;
                            if (browser.exists(checkbox)) {
                                checkbox.uncheck();
                                browser.submit("Aplicar cambios").click();
                                browser.expectConfirm("¿Confirma que desea aplicar la nueva configuración de Failover?", true);
                                logger.info("Desactivada regla 'Virtual >> Unknown'");
                                ch = 0; //Vuelve a ser cero, porque al hacer 'uncheck', aparece un nuevo boton 'deleteico' y hay que repasar
                                if (browser.cell("/Error/").exists()) {
                                    logger.error("Problema Desactivando la regla");
                                    throw new Exception(" ");
                                } else {
                                    browser.button("<< Volver").click();
                                }
                                browser.waitFor(3000);

                            }
                        }

                    } else {
                        ch = 1;
                    }
                }
            }
        }

        //Creo las rutas de desvío FailOver
        if (!proposito.equals("sindesvio")) { //En el caso de no querer tener activado ningun desvio FailOver
            logger.info("ENTRO proposito");
            //Creo la ruta de failover "Virtual >> Unknown"
            browser.select("idLineOut").choose("/Virtual/[0]");
            String[] prioridades = browser.select("").getOptions();
            String prioridad = "";
            for (String p:prioridades
                 ) {
                if (p.equalsIgnoreCase("Alta y Media")){
                    prioridad = p;
                }
            }
            if(prioridad.equals("")){
                logger.error("No existe la prioridad Alta y Media");
            }

            browser.select("priority").choose(prioridad);
            browser.select("idLineFailover").choose("/Unknown/[0]");
            browser.submit("Crear").click();
            if (browser.cell("/Error/").exists()) {
                logger.error("Problema Creando la regla");
                throw new Exception(" ");
            } else {
                browser.button("<< Volver").click();
                logger.info("Creada regla 'Virtual >> Unknown'");
            }

            //Activo la ruta creada
            ElementStub checkbox = browser.checkbox(0).leftOf(browser.cell("Virtual").leftOf(browser.cell("Unknown")));
            if (browser.exists(checkbox)) {
                //browser.waitFor(1000);
                checkbox.check();
                browser.submit("Aplicar cambios").click();
                browser.expectConfirm("¿Confirma que desea activar la nueva configuración de Failover?", true);
                if (browser.cell("/Error/").exists()) {
                    logger.error("ERR: Problema Activando la ruta");
                    throw new Exception(" ");
                } else {
                    browser.button("<< Volver").click();
                    logger.info("Activada regla 'Virtual >> Unknown'");
                }
            }

            //Desactivo la ruta creada
            if (checkbox.checked()) {
                checkbox.uncheck();
                browser.submit("Aplicar cambios").click();
                browser.expectConfirm("¿Confirma que desea aplicar la nueva configuración de Failover?", true);
                logger.info("Desactivada regla 'Virtual >> Unknown'");
                if (browser.cell("/Error/").exists()) {
                    logger.error("Problema Desactivando la regla");
                    throw new Exception(" ");
                } else {
                    browser.button("<< Volver").click();
                }
                browser.waitFor(3000);
            }
        }

        //System.out.println("INFO: GUI FailOver funciona");
        logger.info("GUI FailOver funciona");
    }


    @Step("Modelos <Virtual>")
    public void modelos(String Operadora) throws Exception {
        //Voy a la pestaña canales Mail
        if (browser.link("models").exists()) {
            browser.link("models").click();
            if (browser.link("models_act").exists()) {
            } else {
                logger.error("El link de Modelos ha cambiado su referencia. Corregir el código HTML");
                throw new Exception(" ");
            }
        }

        browser.select("idLineIn").choose("MANUAL - " + Operadora);
        browser.select("idLineOut").choose("/0001 - Virtual/");
        browser.submit("Crear").click();
        browser.button("<< Volver").click();

    }


    @Step("CanalesEmail <comercial@latinia.com>")
    public void canalesEmail(String mail) throws Exception {
        String proposito = datosGlobales.get(Constantes.PROPOSITO).toString();
        int eliminado = 1; //vale 1 cuando se desactiva un canal que no estaba vinculado a clausulas, y por tanto queda eliminado por completo
        int desactivado = 0; //vale 1 cuando el canal se muestra en el apartado de 'no activo'
        int predesactivado = 0; //vale 1 cuando el canal ya está 'no activo' al comenzar el test

        //Voy a la pestaña canales Mail
        if (browser.link("mail").exists()) {
            browser.link("mail").click();
        }

        if (browser.link("mail_act").exists()) {    //Si estoy dentro de la pestaña Email y existe el canal, lo borro
            if (browser.cell(mail).exists()) {
                if (!proposito.equals("provision")) {
                    if (browser.image("ico_delete.gif").rightOf(browser.cell(mail)).exists()) {
                        browser.image("ico_delete.gif").rightOf(browser.cell(mail)).click();
                        browser.expectConfirm("¿Desea eliminar el canal?'" + mail + "'", true);
                        if (browser.cell("/Error/").exists()) {
                            logger.error("Ha habido un problema eliminando el canal de Email" + mail);
                            throw new Exception(" ");
                        } else {
                            browser.button("<< Volver").click();
                        }
                    } else {
                        predesactivado = 1; // el canal se encuentra 'no activo' ya al comenzar el test
                    }

                    //Compruebo si se ha desactivado, o si por el contrario se ha borrado completamente, o si debido a un error no se ha desactivado correctamente.
                    if (browser.cell(mail).under(browser.cell("Canales no activos")).exists()) {
                        desactivado = 1;
                        if (predesactivado == 1) {
                            //System.out.println("INF: Canal " + mail + " se encuentra desactivado");
                            logger.info("Canal " + mail + " se encuentra desactivado");
                        }
                        if (predesactivado == 0) {
                            logger.info("Desactivado canal de Email \" + mail");
                        }

                    } else if (browser.cell(mail).exists()) {
                        logger.error("ERR: El canal " + mail + " no se ha desactivado o eliminado correctamente. OJO! que el GUI ha dicho que si se ha eliminado, pero es mentira");
                        throw new Exception(" ");
                    } else if (!browser.cell(mail).exists()) {
                        eliminado = 1; //vale 1 cuando se ha eliminado correctamente el canal
                        logger.info("Eliminado canal de Email " + mail);
                    }

                    //Lo vuelvo a activar en el caso que se encontrare desactivado
                    if (desactivado == 1) {
                        browser.image("ico_activate.gif").rightOf(browser.cell(mail)).click();
                        browser.expectConfirm("¿Desea activar el canal?'" + mail + "'", true);
                        browser.button("<< Volver").click();
                        logger.info("Activado canal de Email " + mail);
                    }
                }


            }

            if (!browser.cell(mail).exists()) { //Creo el canal en caso que no existiera, o en caso que lo hubiese eliminado en el paso anterior
                browser.textbox("refChannel").setValue(mail);
                browser.submit("Crear").click();
                browser.expectConfirm("¿Desea crear el canal?", true);
                logger.info("Se ha creado el canal de Email " + mail);
                if (browser.cell("/Error/").exists()) {
                    logger.error("Ha habido un problema creando el remitente de Email" + mail);
                    throw new Exception(" ");
                } else {
                    browser.button("<< Volver").click();
                }

                //Compruebo que realmente se ha creado el remitente de Email
                if (!browser.cell(mail).exists()) {
                    logger.error("El remitente de Email " + mail + " no se ha creado. OJO! que el GUI ha dicho que si se ha creado, pero es mentira");
                    throw new Exception(" ");
                } else if (browser.cell(mail).under(browser.cell("Canales no activos")).exists()) {
                    logger.error("ERR: El remitente de Email " + mail + " no se ha activado.");
                    throw new Exception(" ");
                }
            }


        } else {

            String[] typeMsg = tipoMensaje();
            boolean flag = false;
            for (String val : typeMsg) {
                if (val.equalsIgnoreCase("email")) {
                    flag = true;
                    break;
                }
            }
            if (flag) {

            } else {
                logger.info("No se puede crear el canal porque el tipo de mensaje email no está licenciado");
            }
            //System.out.println("BUG: El link de Email ha cambiado su referencia. Corregir el código HTML");
            //throw new Exception("BUG: El link de Email ha cambiado su referencia. Corregir el código HTML");
        }
    }


    @Step("Colectores <PruebaEntornoGrafico>")
    public void colectores(String colector) throws Exception {

        String proposito = datosGlobales.get(Constantes.PROPOSITO).toString();

        //Voy a la pestaña colectores
        if (browser.link("colectores").exists()) {
            browser.link("colectores").click();
            if (browser.link("colectores_act").exists()) {
            } else {
                logger.error("ERR: El link de colectores ha cambiado su referencia. Corregir el código HTML");
                throw new Exception(" ");
            }
        }

        //Compruebo si existe el colector
        int existe = 0;

        if (browser.cell(colector).exists()) {
            existe = 1;
        }

        //Crear colector
        browser.textbox("refCollector").setValue(colector);
        browser.textbox("description").setValue("Colector " + colector);
        browser.submit("Crear").click();

        if (browser.cell("/Error/").exists()) {
            if (existe == 1) {
                //Que ofrezca este error es correcto porque impide creación de duplicados
                browser.button("<< Volver").click();
            }
            if (existe == 0) {
                //Aqui no deberia dar error porque no es duplicado, de modo que si produce un error debe detenerse el testing
                logger.error("Problema al crear el colector");
                throw new Exception(" ");
            }

        } else {
            logger.info("Creado colector" + colector);
            if (existe == 1) {
                logger.error("Ha permitido la creación de un duplicado sin ofrecer error, luego es incorrecto");
                throw new Exception(" ");
            }
            if (existe == 0) {
                browser.button("<< Volver").click();

            }
        }

        //Borro el colector en el caso que el proposito NO sea 'provision', ni tampoco 'traffictesting', y sólo si NO existia previamente
        if (!proposito.equals("provision")) {
            if (!proposito.equals("traffictesting")) {
                if (existe == 0) {
                    browser.image("ico_delete.gif").rightOf(browser.cell(colector)).click();
                    browser.expectConfirm("/Desea eliminar el colector/", true);
                    if (browser.cell("/Error/").exists()) {
                        logger.error("Error borrando el colector");
                        throw new Exception(" ");
                    } else {
                        logger.info("Borrado colector" + colector);
                        browser.button("<< Volver").click();
                    }
                }
            }
        }
    }

    @Step("Rutas <operador> <canal> <formato> <colector>")
    public void rutas(String operador, String canal, String formato, String colector) throws Exception {
        browser.link("rutas").click();
        int existe = 0;
        ElementStub selectColector;
        selectColector = ElementSelect(operador, canal, formato);


        if (selectColector != null && selectColector.exists()) { //Comprueba si existe la ruta, y en tal caso la reasigna de nuevo. En esta accion no se estaria aprovisionando nada nuevo, pero sirve para probar el GUI.
            logger.info("Existe ruta " + operador + " " + canal + " " + formato + " " + colector);
            existe = 1;
            selectColector.choose(colector);
            browser.link("Actualizar").rightOf(selectColector).click();
            browser.button("<< Volver").click();
        } else { //De no existir, la crea
            browser.select("idLineOut").choose(canal + " - " + operador);
            browser.select("idType").choose(formato);
            browser.select("refCollector").choose(colector);
            browser.submit("[0]").click();

            if (browser.cell("/Error/").exists()) {
                logger.error("Error creando la nueva ruta");
                throw new Exception(" ");
            } else {
                if (browser.button("<< Volver").exists()) browser.button("<< Volver").click();
                if (browser.button("<< Back").exists()) browser.button("<< Back").click();
            }

            selectColector = ElementSelect(operador, canal, formato);

            if ((selectColector != null && selectColector.exists()) && (existe == 0)) {//Comprueba que realmente se ha creado la nueva ruta
                logger.info("Creada nueva ruta " + operador + " " + canal + " " + formato + " " + colector);
            }
        }


        if (browser.link("header-btn-home").exists()) { //Desde las primeras releases de R3.9.4
            browser.link("header-btn-home").click();
        }

        if (browser.span("hbtn home").exists()) { //Ultima release de R3.9.4
            browser.span("hbtn home").click();
        }


        browser.link("Forzar recarga").click();
        browser.button("Volver").click();

    }

    public ElementStub ElementSelect(String operador, String canal, String formato) {
        int i = 0;
        ElementStub selectColector = null;
        ElementStub canal2;
        ElementStub formato2;
        String chanel = canal.substring(1, canal.length());
        String oper = operador;

        //Se itera a lo largo de la tabla y buscala ruta de salida
        while (browser.cell(oper).exists()) {
            if (browser.cell("/" + chanel + ".*/").rightOf(browser.cell(oper)).exists()) {
                canal2 = browser.cell("/" + chanel + ".*/").rightOf(browser.cell(oper));
                if (browser.cell("/" + formato + ".*/").rightOf(canal2).exists()) {
                    formato2 = browser.cell("/" + formato + ".*/").rightOf(canal2);
                    if (browser.select("").rightOf(formato2).exists()) {
                        selectColector = browser.select("").rightOf(browser.cell("/" + formato + ".*/").rightOf(browser.cell("/" + chanel + ".*/").rightOf(browser.cell(oper))));
                        return selectColector;
                    } else {
                        i++;
                        oper = operador + "[" + i + "]";
                    }
                } else {
                    i++;
                    oper = operador + "[" + i + "]";
                }
            } else {
                i++;
                oper = operador + "[" + i + "]";
            }
        }

        return selectColector;

    }


    /**
     * En este método se envía un mensaje por el simulador móvil y se verifica que la respuesta que devuelve sea igual a la respuesta esperada.
     *
     * @param table contiene el alias, el texto a enviar en el mensaje y la respuesta esperada por cada fila
     * @throws Exception
     * @author " @amartinez"
     */
    @Step("TransaccionesPullPush <table>")
    public void transaccionesPullPush(Table table) throws Exception {
        List<TableRow> rows = table.getTableRows();
        List<String> columnNames = table.getColumnNames();
        for (TableRow row : rows) {
            browser.textarea("contentIn").setValue(row.getCell(columnNames.get(0)) + "." + row.getCell(columnNames.get(1)));
            if(existennumeros()){
                String numero = datosGlobales.get(Constantes.COUNTRY_CODE).toString()+datosGlobales.get(Constantes.NUM_VCOLLECTOR).toString();
                browser.select("mobileOrg").choose(numero);
                logger.info("Se inyecta pull-push: " + browser.textarea("contentIn").text());
                browser.submit("Enviar").click();
                String resultado = browser.textarea("contentIn").text();
                logger.info("Se obtiene respuesta: " + browser.textarea("contentIn").text());
                if (resultado.equals(row.getCell(columnNames.get(2)))) {
                    logger.info("Correcto Respuesta igual a la entrada");
                } else {
                    //Pendiente de revisar el fail
                    logger.error("Respuesta no corresponde a lo esperado:");
                    throw new Exception(" ");

                }
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

    //----------------WS-----------------//



    /**
     * Este método obtiene la lista de tipos de mensajes, por ejemplo, sms, email, pns, ...
     */

    public String[] tipoMensaje() {
        String[] listaPropiedades;
        String valPropiedades = latiniaScenarioUtil.leerPropiedadesLConfig("license.properties", "license/entry/listMsgTypes");
        listaPropiedades = valPropiedades.split("\\|");

        return listaPropiedades;
    }




}