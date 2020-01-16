package Testing;// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class GestionPropiedades {

    private final Browser browser;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private static Logger logger = LogManager.getLogger(GestionPropiedades.class);
    private char selectObject;

    public GestionPropiedades() {
        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
    }

    // Estos Strings se utilizan en los siguientes metodos de creacion de propiedades
    private String[] propsimple = new String[]{"chn_pref", "robinsongeneral", "robinsonchnlist", "cclist", "noads", "scoring", "vip", "gender"};
    private String[] propclave = new String[]{"gsm", "email", "dni", "twitter_name"};
    private String[] propgrupo = new String[]{"pais"};
    private String[] propimportant = new String[]{"gsm", "email", "twitter_name", "vip"}; //seran marcadas como importantes
    private String[] propencripted = new String[]{"cclist"}; //seran marcadas como encriptadas
    private String[] propshared = new String[]{"email", "scoring", "twitter_name", "dni", "gsm"}; //seran marcadas como compartidas

    private String[] operprop = new String[]{"channels", "merchant_type"}; //propiedad de operacion

    public GestionPropiedades(Browser browser) {
        this.browser = browser;
    }


    /**
     * Crea propiedades de usuario, de operación, indices y fuentes
     * Comprueba el funcionamiento de los controles en el gui
     *
     * @throws Exception
     */
    @Step("UserProps")
    public void userProps() throws Exception {
        logger.info("****PROBANDO USER PROPS****");
        //Recojo el HOST y PORT para los usos en las conexiones a WL
        String host = datosGlobales.get(Constantes.HOST).toString();
        String port = datosGlobales.get(Constantes.PORT).toString();


        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        if (browser.select("idCompany").exists()) {
            if (!empresa.equals(browser.select("idCompany").getSelectedText())) {
                browser.select("idCompany").choose(empresa);
            }
        } else {
            logger.error("No se encuentra el selector de Organizaciones");
            throw new Exception(" ");
        }


        if (browser.image("ico-prop-list.png").exists()) {
            browser.image("ico-prop-list.png").click();
        } else {
            logger.error("No se encuentra pestaña 'PROPIEDADES DE USUARIO'");
            throw new Exception(" ");
        }


        //Crea las propiedades, solo en caso que no existiesen previamente.
        for (String valor : propsimple) {
            if (!browser.cell(valor).exists()) {
                browser.select("propertySource").choose("ws");
                browser.select("propertyType").choose("Simple");
                browser.textbox("propertyName").setValue(valor);
                browser.submit("btn btn-mini btn-primary").click();
                if (browser.cell(valor).exists()) {
                    logger.info("'" + valor + "' creado");
                } else {
                    logger.error(" creando '" + valor + "'");
                    throw new Exception(" ");
                }
            } else {
                logger.info("'" + valor + "' existe");
            }
        }


        for (String valor : propclave) {
            if (!browser.cell(valor).exists()) {
                browser.select("propertySource").choose("host");
                browser.select("propertyType").choose("Clave");
                browser.textbox("propertyName").setValue(valor);
                browser.submit("btn btn-mini btn-primary").click();
                if (browser.cell(valor).exists()) {
                    logger.info("'" + valor + "' creado");
                } else {
                    logger.error("creando '" + valor + "'");
                    throw new Exception(" ");
                }
            } else {
                logger.info("'" + valor + "' existe");
            }
        }


        for (String valor : propgrupo) {
            if (!browser.cell(valor).exists()) {
                browser.select("propertySource").choose("ws");
                browser.select("propertyType").choose("Grupo");
                browser.textbox("propertyName").setValue(valor);
                browser.submit("btn btn-mini btn-primary").click();
                if (browser.cell(valor).exists()) {
                    logger.info("'" + valor + "' creado");
                } else {
                    logger.error("creando '" + valor + "'");
                    throw new Exception(" ");
                }
            } else {
                logger.info("'" + valor + "' existe");
            }
        }


        //Marcar algunas propiedades como 'importantes'
        for (String valor : propimportant) {
            browser.checkbox("cbImportant").rightOf(browser.cell(valor)).check();
            Thread.sleep(500);
            if (!browser.checkbox("cbImportant").near(browser.cell(valor)).checked()) {
                logger.error("La propiedad '" + valor + "' no se ha activado correctamente como 'importante'");
                throw new Exception(" ");
            } else {
                logger.info("'" + valor + "' importante");
            }
        }


        //Lectura de propiedades de ficheros de configuracion LCONFIG
        PropertiesLimsp propertiesLimsp = new PropertiesLimsp();
        propertiesLimsp.init(host.toString(), Integer.parseInt(port), "limsp3.deployer", "latinia");
        String ficheropropiedades = "lconf-general.properties";
        //propertiesLimsp.readPropertyFiles("lconf-general.properties", "lmod-core.properties", "lmod-core.properties", "lmod-core.properties");
        propertiesLimsp.readPropertyFiles(ficheropropiedades);
        //propertiesLimsp.dumpProperties(); //Printa por pantalla lo que ha leido
        String val = propertiesLimsp.getProp("config/dataEncryptionAlgorithm", null);
        logger.info("Esto es el valor de dataEncryptionAlgorithm: " + val);



        //Marcar las propiedades 'encriptadas', teniendo en cuenta el LConfig 'dataEncryptionAlgorithm' de 'lconf-general.properties'.
        //Si el LConfig vale 'null' las casillas deben aparecer atenuadas por lo que no podremos marcarlas.

        if (val == null) {
            //Pruebo a cambiar el checkbox. Si lo consigo es 'ERROR'
            if (!browser.checkbox("cbEncrypted").rightOf(browser.cell(propencripted[0])).checked()) {
                browser.checkbox("cbEncrypted").rightOf(browser.cell(propencripted[0])).check();
                Thread.sleep(500);
                if (browser.checkbox("cbEncrypted").rightOf(browser.cell(propencripted[0])).checked()) {
                    logger.error("los checkbox para'config/dataEncryptionAlgorithm' deberian estar atenuados'");
                    throw new Exception(" ");
                }
            } else {
                browser.checkbox("cbEncrypted").rightOf(browser.cell(propencripted[0])).uncheck();
                Thread.sleep(500);
                if (!browser.checkbox("cbEncrypted").rightOf(browser.cell(propencripted[0])).checked()) {
                    logger.error("los checkbox para'config/dataEncryptionAlgorithm' deberian estar atenuados'");
                    throw new Exception(" ");
                }
            }

            logger.info("correcto atenuados los checkbox para'config/dataEncryptionAlgorithm'");

        } else { // Si la encriptacion esta activada procedo a marcar los checkbox

            for (String valor : propencripted) {
                if (browser.checkbox("cbEncrypted").rightOf(browser.cell(valor)).checked()) { //Si me la encuentro ya 'activada' realizo el ciclo de desactivar>>activar

                    browser.checkbox("cbEncrypted").rightOf(browser.cell(valor)).uncheck();
                    Thread.sleep(500);
                    if (browser.checkbox("cbEncrypted").rightOf(browser.cell(valor)).checked()) {
                        logger.error("La propiedad '" + valor + "' no se ha desactivado correctamente como 'Encriptada'");
                        throw new Exception("");
                    } else {
                        logger.info("'" + valor + "' desencriptada");
                    }

                    browser.checkbox("cbEncrypted").rightOf(browser.cell(valor)).check();
                    Thread.sleep(500);
                    if (!browser.checkbox("cbEncrypted").rightOf(browser.cell(valor)).checked()) {
                        logger.error("La propiedad '" + valor + "' no se ha activado correctamente como 'Encriptada'");
                        throw new Exception(" ");
                    } else {
                        logger.info("'" + valor + "' encriptada");
                    }

                } else { //Si me la encuentro 'desactivada', la activo
                    browser.checkbox("cbEncrypted").rightOf(browser.cell(valor)).check();
                    Thread.sleep(500);
                    if (!browser.checkbox("cbEncrypted").rightOf(browser.cell(valor)).checked()) {
                        logger.error("La propiedad '" + valor + "' no se ha activado correctamente como 'Encriptada'");
                        throw new Exception(" ");
                    } else {
                        logger.info("'" + valor + "' encriptada");
                    }
                }

            }
        }


        //Marcar las propiedades 'compartidas'
        for (String valor : propshared) {
            browser.checkbox("cbShared").rightOf(browser.cell(valor)).check();
            Thread.sleep(500);
            if (!browser.checkbox("cbShared").rightOf(browser.cell(valor)).checked()) {
                logger.error("La propiedad '" + valor + "' no se ha activado correctamente como 'Compartida'");
                throw new Exception(" ");
            } else {
                logger.info(" '" + valor + "' compartida");
            }
        }

    }


    /**
     * Verifica la pestaña de INDICES
     * INDICES solo aparece en el caso de tener contratado alguno de los siguientes productos:
     * -limspae-subscrules
     * -limspae-frameedit
     * -limspae-transfrules
     * En el caso de no estar contratado ninguno de ellos, no se realiza la prueba de indices.
     *
     * @throws Exception
     */
    @Step("Indices")
    public void indices() throws Exception {
        String host = datosGlobales.get(Constantes.HOST).toString();
        String port = datosGlobales.get(Constantes.PORT).toString();

        String urls[] = new String[]{"http://" + host + ":" + port + "/limspae-subscrules", "http://" + host + ":" + port + "/limspae-frameedit", "http://" + host + ":" + port + "/limspae-transfrules"};

        boolean probarindices = false;
        logger.info("****PROBANDO INDICES***");

        //invoco todas las URLs del Array de Strings 'urls', en este caso 3 * 1-limspae-subscrules * 2-limspae-frameedit * 3-limspae-transfrules
        for (String valor : urls) {
            //Invoca la ejecucion de la URL que le toca segun itera

            String resultado = LatiniaScenarioUtil.conectaURL(valor);

            if (resultado.equals("OK")) {
                probarindices = true;
            } else {
                logger.info("'" + valor + "' no existe");
            }

        }


        if (probarindices == true) {
            String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
            if (browser.select("idCompany").exists()) {
                if (!empresa.equals(browser.select("idCompany").getSelectedText())) {
                    browser.select("idCompany").choose(empresa);
                }
            } else {
                logger.error("No se encuentra el selector de Organizaciones");
                throw new Exception(" ");
            }

            if (browser.image("ico-indexes.png").exists()) {
                browser.image("ico-indexes.png").click();
            } else {
                logger.error("No se encuentra pestaña 'INDICES'. Si estas probando una instalacion sin AE(AlertEngine), debes deshabilitar en el escenario la linea de comprobacion de INDICES");
                throw new Exception(" ");
            }

            for (String valor : propclave) {
                if (!browser.cell(valor).exists()) { //Si ya existe, no la creo
                    browser.select("propertyName").choose(valor);
                    browser.textbox("refIndex").setValue(valor);
                    browser.textbox("maxLength").setValue("10");
                    browser.submit("btn btn-mini btn-primary").click();
                    Thread.sleep(500);
                    if ((browser.italic("icon-ok-sign icon-green").rightOf(browser.cell(valor)).exists())) {
                        logger.info("Creado indice '" + valor + "'");
                        //Desactivo y Activo el indice
                        browser.italic("icon-ok-sign icon-green").rightOf(browser.cell(valor)).click();
                        browser.expectConfirm("/!!!/", true);
                        logger.info("Desactivado");
                        Thread.sleep(500);

                        if (!browser.italic("icon-ok-sign").rightOf(browser.cell(valor)).exists()) {
                            logger.error("Desactivando indice '" + valor + "'");
                            throw new Exception(" ");
                        } else {
                            browser.italic("icon-ok-sign").rightOf(browser.cell(valor)).click();
                            logger.info("Activado");
                            if (!browser.italic("icon-ok-sign icon-green").rightOf(browser.cell(valor)).exists()) {
                                logger.error("Activando indice '" + valor + "'");
                                throw new Exception(" ");
                            }
                        }
                    } else {
                        logger.error("Creando el indice '" + valor + "'");
                        throw new Exception(" ");
                    }
                } else {
                    logger.error("Existe el indice '" + valor + "'");
                }
            }

            //Creo el indice 'mi_INDICE_pa_borrar' expresamente para borrarlo de inmediato
            if (!browser.cell("mi_INDICE_pa_borrar").exists()) {
                browser.select("propertyName").choose("vip");
                browser.textbox("refIndex").setValue("mi_INDICE_pa_borrar");
                browser.textbox("maxLength").setValue("20");
                browser.submit("btn btn-mini btn-primary").click();
                Thread.sleep(1000);
                if (browser.cell("mi_INDICE_pa_borrar").exists()) {
                    logger.error("Creado Origen 'mi_INDICE_pa_borrar'");

                } else {
                    logger.error("creando Indice 'mi_INDICE_pa_borrar'");
                    throw new Exception(" ");
                }
            }
            //Borro el indice 'mi_INDICE_para_borrar'
            browser.italic("icon-trash").rightOf(browser.cell("mi_INDICE_pa_borrar")).click();
            browser.expectConfirm("/mi_INDICE_para_borrar/", true);
            Thread.sleep(1000);
            if (browser.cell("mi_INDICE_pa_borrar").exists()) {
                logger.error("borrando el Indice 'mi_INDICE_pa_borrar'");
                throw new Exception(" ");
            } else {
                logger.info("Borrado Indice 'mi_INDICE_pa_borrar'");
            }


        } else {
            logger.info("Saltando prueba de INDICES");
        }

    }


    /**
     * dramirez Realiza el cambio de vista
     * se dirige a los objetos de la compañia
     * @throws Exception
     */
    @Step("Objetos")
    public void objetos() throws Exception{
        logger.info("****PROBANDO OBJETOS****");
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        if (browser.select("idCompany").exists()){
            if (!empresa.equals(browser.select("idCompany").getSelectedText())) {
                browser.select("idCompany").choose(empresa);
            }
        }else{
            logger.error("No se encuentra el selector de Organizaciones");
            throw new Exception(" ");
        }
        if(browser.image("w_mol.png").exists()){
            browser.image("w_mol.png").click();
        }else{
            logger.error("No se encuentra la pestaña 'Objetos'");
            throw new Exception(" ");
        }

    }
    /**
     * dramirez Verificá la licencia
     * revisa valores de Lconfig
     * para identificar si es posible o no la creación
     * de objetos por medio grafico
     * @return returna un booleano, de acuerdo a si esta
     * licenciado o no la posibilidad de crear objetos
     * @throws Exception
     */
    public boolean VerificarValorLicencia() throws Exception{
        int lice =  Integer.parseInt(datosGlobales.get(Constantes.MAX_OBJECTS).toString());
        if(lice > 0 ){
            logger.info("Creación de objetos permitidos, de acuerdo a la licencia");
            return true;
        }else {
            logger.error("Creación de ibjetos No permitidos, de acuerdo a la licencia");
            return false;
        }
    }

    /**
     * dramirez Verifica la pestaña de INDICES
     * Realiza conteo de la cantidad de objetos
     * creados en la tabla, retorna el valor luego
     * de finalizar el conteo
     * @return total de la canditad de objetos creados actualmente
     * @throws Exception
     */
    public int ContarObjetos() throws Exception{
        int objetos =0;
        if(browser.table("table-obj-type-def").exists()){
            objetos= browser.cell("/col-obj-ref/").countSimilar();
            logger.info("Se encontraron " + objetos + " objetos creados");
        }else{
            logger.info("No hay objetos creados");
        }
        return objetos;
    }

    /**
     * dramirez metodo control  para crear objetos
     * hace llamados a metodos para:
     * verificar licencia
     * si existe o no la propiedad
     * si se alcanzo o no el maximo
     * el maximo de creación permitido
     * y verificación de respuesta de mensaje de creación del objeto
     * @throws Exception
     */
    @Step("Crear Objeto ")
    public void crearObjeto() throws Exception{
        String nombre = datosGlobales.get(Constantes.NOM_OBJETO).toString();
        if(VerificarValorLicencia()){
            logger.info("Verificación de licencia: válida");
            if(browser.submit("btn btn-mini btn-primary").exists()){
                logger.info("Se puede crear correctamente objetos");
                if(objExis(nombre)){
                    logger.info("El objeto a crear ya existe, se eliminara");
                    if(eliminarObjeto(nombre)){
                        logger.info("Objeto eliminado, se procedera a su creación de nuevo");
                        crearObjeto();
                    }else{
                        logger.error("El objeto " + nombre+" se encontraba ya creado y no se pudo eliminar");
                        throw new Exception();
                    }
                }else{
                    browser.textbox("objTypeRef").setValue(nombre);
                    browser.textbox("objTypeDesc").setValue(nombre);
                    browser.submit("btn btn-mini btn-primary").click();
                    if(browser.div("info-message-success").getText().equals("Definición de objeto creada correctamente.")){
                        logger.info("Se creo correctamente el objeto");
                        idobjetoPropiedad(nombre);
                    }else if(browser.div("info-message-error").getText().equals("Error creando definición de objeto")){
                        logger.error("No se creo correctamente el objeto");
                        throw  new Exception();
                    }
                }
            }else{
                if(ContarObjetos() == Integer.parseInt(datosGlobales.get(Constantes.MAX_OBJECTS).toString())){
                    logger.error("Límite máximo de creación de objetos alcanzado, no puede crear más objetos");
                    throw  new Exception();
                }else if(ContarObjetos() <= Integer.parseInt(datosGlobales.get(Constantes.MAX_OBJECTS).toString())){
                    logger.error("No se cargó correctamente la GUI de creación de objetos, aún no se ha alcanzado el máximo de creación permitido.");
                    throw new Exception();
                }
            }
        }else{
            logger.error("Verificación de licencia: No válida");
            throw  new Exception();
        }
    }

    /**
     * dramriez Indica si existe o no
     * un objeto
     * @param nombre
     * @return
     */
    public boolean objExis(String nombre){
        if(browser.cell((nombre)).exists()){
            return true;
        }else{
            return false;
        }
    }


    /**
     * dramirez Identifica el (id) puesto del objeto en la tabla
     * para evitar hacer constantemente recorridos, se almacena
     * en una constante la cantidad de propiedades que tiene un
     * objeto con el mismo fin.
     * @param nombre valor del nombre de la propiedad a identificar
     * @throws Exception
     */
    public void idobjetoPropiedad(String nombre) throws Exception {
        int obj = ContarObjetos();
        int ind = -1;
        for (int i =0; i< obj; i ++){
            if(browser.cell("col-obj-ref["+i+"]").getText().equals(nombre)){
                ind = i;
                datosGlobales.put(Constantes.ID_OBJETO,i);
                break;
            }
        }
        if(ind >=0){
            int tpropiedades = CountPropiedades(ind);
            datosGlobales.put(Constantes.ID_PROPIEDAD,tpropiedades);
        }
    }


    /**
     * dramirez Agrega propiedad a un objeto
     * verifica que este o no duplicada la propiedad
     * verifica mensajes de creación o de duplicidad de la propiedad     *
     * @throws Exception
     */
    @Step("Agregar propiedades")
    public void agregarPropiedes() throws Exception {
        String nombre = datosGlobales.get(Constantes.NOM_OBJETO).toString();
        String prop = datosGlobales.get(Constantes.PROP_OBJETO).toString();
        if(datosGlobales.get(Constantes.ID_OBJETO) == null || datosGlobales.get(Constantes.ID_PROPIEDAD) == null){
            logger.error("Valor de ID propiedad o  ID objeto es nulo");
            throw  new Exception();
        } else {
            int id_objeto = Integer.parseInt(datosGlobales.get(Constantes.ID_OBJETO).toString());
            int id_propiedad = Integer.parseInt(datosGlobales.get(Constantes.ID_PROPIEDAD).toString());
            if (obtenerObjeto(nombre)) {
                logger.info("Agregando propiedades");
                idobjetoPropiedad(nombre);
                int ind = Integer.parseInt(datosGlobales.get(Constantes.ID_OBJETO).toString());
                if (ind >= 0) {
                    logger.info("Agregando propiedades al objeto");
                    for (int i = 0; i < id_propiedad; i++) {
                        if (browser.textbox("name[" + i + "]").near(browser.link("Nueva propiedad[" + id_objeto + "]")).getValue().equals("#ref")) {
                            browser.setValue(browser.textbox("/size/").near(browser.link("Nueva propiedad[" + ind + "]")), "15");
                        }
                    }
                    browser.link("Nueva propiedad[" + ind + "]").click();
                    browser.setValue(browser.textbox("name[" + id_propiedad + "]").near(browser.link("Nueva propiedad[" + ind + "]")), prop);
                    browser.setValue(browser.textbox("size[" + id_propiedad + "]").near(browser.link("Nueva propiedad[" + ind + "]")), "32");
                    browser.button("Guardar[" + ind + "]").click();
                    if (browser.div("Hay nombres de campo duplicados.").exists()) {
                        logger.info("Propiedad de objeto duplicada");
                        if (eliminarPropiedad(prop, id_propiedad, id_objeto)) {
                            logger.info("La propiedad " + prop + " se elimino correctamente, se procedera a crear de nuevo");
                            agregarPropiedes();
                        } else {
                            logger.error("No se pudo eliminar la propiedad " + prop);
                            throw new Exception();
                        }
                    } else if (browser.span("Definición de objeto guardada correctamente.").exists()) {
                        logger.info("Se creo correctamente la propiedad");
                        idobjetoPropiedad(nombre);
                    }
                } else {
                    logger.error("No se ubica el objeto al cual se le quiere editar sus propiedades");
                    throw new Exception();
                }
            }
        }
    }

    /**
     * dramirez Cuenta la cantidad
     * de propiedades que tiene
     * un objeto, retorna el total
     * @param id posición de ubicación del objeto
     *  al que se le contaran las propiedades
     * @return cantidad total de propiedades del objeto
     */
    public int CountPropiedades(int id){
        int propiedades =0;
        if(browser.table("table-std tbl-properties["+id+"]").exists()){
            propiedades =browser.row("/property/").in(browser.table("table-std tbl-properties["+id+"]")).countSimilar();
            logger.info("Se encontraron " + propiedades + " propiedades creadas");
        }else{
            logger.info("No hay propiedades creadas");
        }
        return propiedades;
    }

    /**
     * dramirez Abre la opción de
     * editar un objeto en especifico
     * @param nombre valor del nombre del objeto a editar
     * @return
     */
    public boolean obtenerObjeto(String nombre){
       if (objExis(nombre)){
           browser.click(browser.italic(0,"icon-edit").near(browser.cell(nombre)));
           return true;
       }else{
            return false;
       }
    }

    /**
     * dramirez Metodo control para verificar la existencia del objeto creado
     * y de la propiedad creada en el objeto.
     * @throws Exception
     */
    @Step("Verificar ObjetoPropiedad")
    public void VeriObjetoPropiedad() throws Exception {
        String objeto = datosGlobales.get(Constantes.NOM_OBJETO).toString();
        String propiedad = datosGlobales.get(Constantes.PROP_OBJETO).toString();
        idobjetoPropiedad(objeto);
        if(objExis(objeto)){
            logger.info("Existe el objeto, verificación correcta");
            if(propExis(objeto, propiedad)){
                logger.info("Propiedad: " + propiedad + " encontrada");
            }else{
                logger.error("No se encuentra la propiedad" + propiedad);
                throw new Exception();
            }
        }else{
            logger.error("No se encuentra el objeto" + objeto);
            throw new Exception();
        }
    }

    /**
     * dramirez Veirifca la existencia de
     * una propiedad en un objeto
     * @param objeto valor del nombre de un objeto
     * @param propiedad valor del nombre de una propiedad
     * @return valor booleano, verdadero para la existencia de una propiedad en un objeto
     * @throws Exception
     */
    public boolean propExis(String objeto, String propiedad) throws Exception {
        boolean existe = false;
        if (objExis(objeto)) {
            if (obtenerObjeto(objeto)) {
                int ind = Integer.parseInt(datosGlobales.get(Constantes.ID_OBJETO).toString());
                if (ind >= 0) {
                    int tpropiedades = Integer.parseInt(datosGlobales.get(Constantes.ID_PROPIEDAD).toString());
                    for (int i = 0; i < tpropiedades; i++) {
                        if (browser.textbox("name["+i+"]").near(browser.link("Nueva propiedad[" +ind+ "]")).getValue().equals(propiedad)) {
                            existe = true;
                            break;
                        } else {
                            existe = false;
                        }
                    }
                }
            } else {
                existe = false;
                logger.info("El objeto al que se le busca la propiedad, no existe, por ende la propiedad no se encuentra");
            }
        }
        return existe;
    }

    /**
     * dramirez Metodo de control
     * para la eliminación del objeto
     * y de la propiedad creada.
     * @throws Exception
     */
    @Step("Eliminar PropiedadObjeto")
    public void eliminar() throws Exception {
        String objeto = datosGlobales.get(Constantes.NOM_OBJETO).toString();
        String propiedad = datosGlobales.get(Constantes.PROP_OBJETO).toString();
        int id_objeto = Integer.parseInt(datosGlobales.get(Constantes.ID_OBJETO).toString());
        int id_propiedad = Integer.parseInt(datosGlobales.get(Constantes.ID_PROPIEDAD).toString());
        if(eliminarPropiedad(propiedad,id_propiedad,id_objeto)){
            logger.info("Propiedad " + propiedad + " eliminada correctamente");
            if(eliminarObjeto(objeto)){
                logger.info("Objeto "+ objeto + " eliminado correctamente");
            }else{
                logger.error("No se puede borrar la definicion de objeto " +objeto+ " tiene objetos vinculados");
                throw new Exception();
            }
        }else{
            logger.error("No se pudo eliminar la propiedad " + propiedad + " del objeto " + objeto);
            throw new Exception();
        }
    }


    /**
     * dramirez elimina la propiedad de un objeto
     * @param propiedad nombre de la propiedad
     * @param id_propiedad posición de la propiedad en la lista de propiedades del objeto
     * @param id_objeto posición del objeto en la lista de objetos
     * @return
     */
    public boolean eliminarPropiedad(String propiedad, int id_propiedad, int id_objeto){
        boolean bandera = false;
        for (int i = 0; i < id_propiedad; i++){
            if (browser.textbox("name["+i+"]").near(browser.link("Nueva propiedad["+id_objeto+"]")).getValue().equals(propiedad)){
                browser.click(browser.italic("icon-trash").near(browser.textbox("name["+i+"]").near(browser.link("Nueva propiedad["+id_objeto+"]"))));
                browser.button("Guardar["+id_objeto+"]").click();
                break;
            }
        }
        if(browser.div("info-message-success").getText().equals("Definición de objeto guardada correctamente.")){
            bandera = true;
        }else if(browser.div("info-message-error").getText().equals("Error creando definición de objeto")){
            bandera = false;
        }
        return bandera;
    }


    /**
     * dramirez elimina un objeto
     * @param nombre valor del nombre del objeto a eliminar
     * @return valor booleano para validar si se elimino o no el objeto
     * @throws Exception
     */
    public boolean eliminarObjeto(String nombre) throws Exception {
        boolean bandera = false;
        if (objExis(nombre)) {
            browser.click(browser.italic("icon-trash").rightOf(browser.cell(nombre)));
            if(browser.div("info-message-success").getText().equals("Definición de objeto borrada.")){
                bandera = true;
            }else if(browser.div("info-message-error").getText().equals("No se pudo eliminar la definición de objeto.")){
                bandera = false;
            }
        }else{
            logger.error("El objeto "+nombre+" a eliminar, no existe");
            throw  new Exception();
        }

        return bandera;
    }
    /**
     * dramirez Verifica
     * la existencia del objeto
     * luego de su eliminación
     *
     * @throws Exception
     */
    @Step("Eliminacion correcta")
    public void eliminacionCorrecta() throws Exception {
        String objeto = datosGlobales.get(Constantes.NOM_OBJETO).toString();
        if(objExis(objeto)){
            logger.error("No se elimino correctamente el objeto: "+ objeto);
            throw new Exception();
        }else{
            logger.info("Eliminación correcta del objeto: " + objeto);
        }
    }

    @Step("Origenes")
    public void origenes() throws Exception {
        logger.info("****PROBANDO ORIGENES***");

        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        if (browser.select("idCompany").exists()) {
            if (!empresa.equals(browser.select("idCompany").getSelectedText())) {
                browser.select("idCompany").choose(empresa);
            }
        } else {
            logger.error("No se encuentra el selector de Organizaciones");
            throw new Exception(" ");
        }


        if (browser.image("ico-origins.png").exists()) {
            browser.image("ico-origins.png").click();
        } else {
            logger.error(" No se encuentra pestaña 'ORIGENES'");
            throw new Exception(" ");
        }


        //Creo origenes
        if (!browser.cell("host").exists()) {
            browser.textbox("sourceName").setValue("host");
            browser.submit("btn btn-mini btn-primary").click();
            if (browser.cell("host").exists()) {
                logger.info("Creado Origen 'host'");
            } else {
                logger.error("creando Origen 'host'");
                throw new Exception(" ");
            }
        }

        if (!browser.cell("ws").exists()) {
            browser.textbox("sourceName").setValue("ws");
            browser.submit("btn btn-mini btn-primary").click();
            if (browser.cell("ws").exists()) {
                logger.info(" Creado Origen 'ws'");
            } else {
                logger.error("creando Origen 'ws'");
                throw new Exception(" ");
            }
        }

        if (!browser.cell("mi_ORIGEN_para_borrar").exists()) {
            browser.textbox("sourceName").setValue("mi_ORIGEN_para_borrar");
            browser.submit("btn btn-mini btn-primary").click();
            if (browser.cell("mi_ORIGEN_para_borrar").exists()) {
                logger.info("Creado Origen 'mi_ORIGEN_para_borrar'");
            } else {
                logger.error("creando Origen 'mi_ORIGEN_para_borrar'");
                throw new Exception(" ");
            }
        }
        //Borro el origen 'mi_ORIGEN_para_borrar'
        browser.italic("icon-trash").rightOf(browser.cell("mi_ORIGEN_para_borrar")).click();
        browser.expectConfirm("/mi_ORIGEN_para_borrar/", true);
        Thread.sleep(500);
        if (browser.cell("mi_ORIGEN_para_borrar").exists()) {
            logger.error("borrando el Origen 'mi_ORIGEN_para_borrar'");
            throw new Exception(" ");
        } else {
            logger.info("Borrando Origen 'mi_ORIGEN_para_borrar'");
        }


        //Este Origen lo utilizo despues, en la pestaña de 'propiedades de operacion' simplemente para ver si aparece en el desplegable
        if (!browser.cell("mi automated").exists()) {
            browser.textbox("sourceName").setValue("mi automated");
            browser.submit("btn btn-mini btn-primary").click();
            if (browser.cell("mi automated").exists()) {
                logger.info("Creado Origen 'mi automated'");
            } else {
                logger.error("creando Origen 'mi automated'");
                throw new Exception(" ");
            }
        }

    }


    /**
     * @param proposito incica que tras la provision, los datos no deben borrarse, dejandolos de este modo aprovisionados para futuros usos
     * @throws Exception
     */
    @Step("OperProps <>")
    public void operProps(String proposito) throws Exception {
        logger.info("****PROBANDO OPERPROPS***");
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        if (browser.select("idCompany").exists()) {
            if (!empresa.equals(browser.select("idCompany").getSelectedText())) {
                browser.select("idCompany").choose(empresa);
            }
        } else {
            logger.error("No se encuentra el selector de Organizaciones");
            throw new Exception(" ");
        }


        if (browser.image("ico-prop-list.png[1]").exists()) {
            browser.image("ico-prop-list.png[1]").click();
        } else {
            logger.error(" No se encuentra pestaña 'Propiedades de Operacion'");
            throw new Exception(" ");
        }


        //Creo propiedades de Operacion
        //Crea las propiedades, solo en caso que no existiesen previamente.
        for (String valor : operprop) {
            if (browser.cell(valor).exists()) {
                logger.info(" propiedad de operacion '" + valor + "' existe");
            } else {
                browser.select("propertySource").choose("host");
                browser.textbox("propertyName").setValue(valor);
                browser.submit("btn btn-mini btn-primary").click();
                if (browser.cell(valor).exists()) {
                    logger.info("propiedad de operacion '" + valor + "' creada");
                } else {
                    logger.error("creando propiedad de operacion '" + valor + "'");
                    throw new Exception(" ");
                }
            }
            browser.checkbox("cbShared").rightOf(browser.cell(valor)).check();
            Thread.sleep(500);
            if (!browser.checkbox("cbShared").rightOf(browser.cell(valor)).checked()) {
                logger.error("La propiedad de operacion '" + valor + "' no se ha activado correctamente como 'Compartida'");
                throw new Exception(" ");
            } else {
                logger.info("propiedad de operacion '" + valor + "' compartida");
            }

        }

        //Compruebo que existe el Source(Origen) creado en la pestaña de Sources(Origenes)
        if (!browser.select("propertySource").containsText("mi automated")) {
            logger.error("no se ha encontrado el origen 'mi automated' en el desplegable de 'propiedades de operacion'. Asegurese que existe en 'Origenes'");
            throw new Exception(" ");
        }


        //Borro los origenes creados, solo si 'proposito!=provision'
        if (!proposito.equals("provision")) {
            for (String valor : operprop) {
                if (browser.italic("icon-trash").rightOf(browser.cell(valor)).exists()) {
                    browser.italic("icon-trash").rightOf(browser.cell(valor)).click();
                    Thread.sleep(1500);
                    if (browser.cell(valor).exists()) {
                        logger.error("Borrando la propiedad de operacion '" + valor + "'");

                        throw new Exception(" ");
                    } else {
                        logger.info("Borrada la propiedad de operacion'" + valor + "'");

                    }
                }
            }
        }

    }

    /**
     * @throws Exception Metodo que permite validar si existe la pestaña Objeto y si una propiedad se agrega exitosamente,
     *                   Ademas permite validar los valores ingresados que caracteres permite, y si una propiedad se guarda exitosamente.
     */
    @Step("Crear nueva propiedad de objeto")
    public void nuevaPropiedadObjeto() throws Exception {
        Table tabProps = (Table) datosGlobales.get(Constantes.TABLA);
        List<TableRow> rows = tabProps.getTableRows();
        List<String> columnNames = tabProps.getColumnNames();
        String org = datosGlobales.get(Constantes.ORGANIZACION).toString();

        if (browser.select("idCompany").exists()) {
            browser.select("idCompany").choose(org);

            if (browser.image("w_mol.png").exists()) {
                logger.info("Entrando a la pestaña OBJETOS");
                browser.image("w_mol.png").click();

                if (browser.select("selectObject").exists()) {
                    String objetos = browser.select("selectObject").getText();
                    String[] listaObjetos = objetos.split(",");
                    for (int i = 0; i < listaObjetos.length; i++) {
                        String b = listaObjetos[i];
                        if (!b.equals("---")) {
                            browser.select("selectObject").choose(b);
                            i = listaObjetos.length + 1;
                        }
                    }
                    for (TableRow row : rows) {
                        if (browser.link("Nueva propiedad").exists()) {
                            browser.link("Nueva propiedad").click();
                            boolean existe = false;
                            int i = 0;
                            while (!existe) {
                                if (browser.textbox("name[" + i + "]").getText().equals("")) {
                                    browser.textbox("name[" + i + "]").setValue(row.getCell(columnNames.get(0)));
                                    browser.textbox("size[" + i + "]").setValue(row.getCell(columnNames.get(1)));
                                    browser.textbox("dateformat[" + i + "]").setValue(row.getCell(columnNames.get(2)));
                                    MsmConfirmacionGuardado();
                                    existe = true;
                                } else {
                                    i++;
                                }
                            }

                        } else {
                            logger.error("No existe  la opcion Nueva propiedad");
                            throw new Exception(" ");
                        }
                    }


                } else {
                    logger.error("No existe lista de tipos de objeto");
                    throw new Exception(" ");
                }
            } else {
                logger.error("La pestaña Objetos no existe");
                throw new Exception(" ");
            }

        } else {
            logger.error("La pestaña Objetos no existe");
            throw new Exception(" ");
        }
    }

    /**
     * @throws Exception Metodo que permite confirmar que existe el boton para guaradar la informacion ingresada y
     *                   que al guardar(Dar clic en el boton) la informacion en este caso una propiedad
     *                   el sistema arroja un mensaje de confirmación indicando que se guardo exitosamente
     */

    public void MsmConfirmacionGuardado() throws Exception {

        if (browser.button("Guardar").exists()) {
            logger.info("guardando el objeto");
            browser.button("Guardar").click();

            if (browser.div("info-message-success").exists()) {
                logger.info("EL Objeto se guardo satisfactoriamente");


            } else {
                logger.error("No existe mensaje de confirmacion de guardado");
                throw new Exception(" ");
            }

        } else {
            logger.error("El boton Guardar no existe");
            throw new Exception(" ");
        }
    }

    /**
     * @throws Exception Metodo que permite confirmar que existe el boton que para eliminar la informacion y
     */
    @Step("Eliminar")
    public void MsmConfirmacionElimiado() throws Exception {

        browser.navigateTo("", true);
        String objetos = browser.select("selectObject").getText();
        String[] listaObjetos = objetos.split(",");
        for (int i = 0; i < listaObjetos.length; i++) {
            String b = listaObjetos[i];
            if (!b.equals("---")) {
                browser.select("selectObject").choose(b);
                i = listaObjetos.length + 1;
            }
        }

        Table tabProps = (Table) datosGlobales.get(Constantes.TABLA);
        List<TableRow> rows = tabProps.getTableRows();
        List<String> columnNames = tabProps.getColumnNames();

        for (TableRow row : rows) {

                boolean existe = false;
                int i = 0;
                while (!existe) {
                    if (browser.textbox("name[" + i + "]").getText().equals(row.getCell(columnNames.get(0)))) {
                        if (browser.italic("icon-trash[" + i + "]").exists()) {
                            browser.italic("icon-trash[" + i + "]").click();
                            browser.button("Guardar").click();

                            if (browser.div("info-message-success").exists()) {
                                logger.info("La propiedad se Elimino satisfactoriamente");

                            } else {
                                logger.error("No existe mensaje de confirmacion de eliminado");
                                throw new Exception(" ");
                            }
                            existe = true;
                        }
                    } else {
                        i++;
                    }
                }


        }

    }

    @Step("Verificar propiedad objeto")
    public void verificarExistePropiedadObjeto() {
        browser.navigateTo("", true);

        Table tabProps = (Table) datosGlobales.get(Constantes.TABLA);
        List<TableRow> rows = tabProps.getTableRows();
        List<String> columnNames = tabProps.getColumnNames();

        String objetos = browser.select("selectObject").getText();
        String[] listaObjetos = objetos.split(",");
        for (int i = 0; i < listaObjetos.length; i++) {
            String b = listaObjetos[i];
            if (!b.equals("---")) {
                browser.select("selectObject").choose(b);
                i = listaObjetos.length + 1;
            }
        }


        for (TableRow row : rows) {
            boolean existe = false;
            int i = 0;
            while (!existe) {

                if (browser.textbox("name[" + i + "]").getText().equals(row.getCell(columnNames.get(0)))) {
                    if (browser.textbox("size[" + i + "]").getText().equals(row.getCell(columnNames.get(1)))) {
                        if (browser.textbox("dateformat[" + i + "]").getText().equals(row.getCell(columnNames.get(2)))) {
                            logger.info("Existe");
                            existe = true;
                        }
                    }


                } else {
                    i++;
                }
            }
        }
    }

    @Step("Verificar eliminado de  la propiedad objeto")
    public void verificarEliminado() {
        browser.navigateTo("", true);

        Table tabProps = (Table) datosGlobales.get(Constantes.TABLA);
        List<TableRow> rows = tabProps.getTableRows();
        List<String> columnNames = tabProps.getColumnNames();

        String objetos = browser.select("selectObject").getText();
        String[] listaObjetos = objetos.split(",");
        for (int i = 0; i < listaObjetos.length; i++) {
            String b = listaObjetos[i];
            if (!b.equals("---")) {
                browser.select("selectObject").choose(b);
                i = listaObjetos.length + 1;
            }
        }

        for (TableRow row : rows) {
            boolean existe = false;
            int i = 0;
            while (!existe) {

                if (!browser.textbox("name[" + i + "]").getText().equals(row.getCell(columnNames.get(0)))) {
                    if (!browser.textbox("size[" + i + "]").getText().equals(row.getCell(columnNames.get(1)))) {
                        if (!browser.textbox("dateformat[" + i + "]").getText().equals(row.getCell(columnNames.get(2)))) {
                            logger.info("La propiedad ya no existe."+ existe);
                            existe = true;
                        }
                    }

                } else {
                    i++;
                }
            }
        }
    }

}