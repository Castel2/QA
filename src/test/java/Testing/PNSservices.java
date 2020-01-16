package Testing; /**
 * Created by amartinez on 4/07/2017.
 */

import LData_Testing.AccesoWSInf;
import com.latinia.limspinf.stubs.data.*;
import com.latinia.limspinf.stubs.data.Device;
import com.latinia.limspinf.stubs.storage.*;
import com.latinia.limspinf.stubs.storage.Exception_Exception;
import com.latinia.limspinf.stubs.user.*;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PNSservices {


    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    AccesoWSInf accesoWSInf;
    VerificacionLIMSP verificacionLIMSP;
    private ProvisionerUser provisionerUser;
    private Storage storage;
    private static Logger logger = LogManager.getLogger(PNSservices.class);



    public PNSservices() {
        accesoWSInf = new AccesoWSInf();
        verificacionLIMSP = new VerificacionLIMSP();
        provisionerUser = (ProvisionerUser) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_USER, Constantes.WLURL_PROVISIONER_USER,
                Constantes.WS_INF_PROVISIONER_USER_SERVICE, Constantes.WS_INF_PROVISIONER_USER_METHOD);
        storage = (Storage) accesoWSInf.wsINFGeneric(Constantes.APP_LMAN_VCONTENT, Constantes.LATINIA,
                Constantes.WASURL_STORAGE, Constantes.WLURL_STORAGE, Constantes.WS_INF_STORAGE_SERVICE,
                Constantes.WS_INF_STORAGE_METHOD);
    }

    /**
     * Cambia el valor de force por llamado ws
     * force = TRUE -----> Establece un dispositivo uuid como confidencial de un usuario pasado por parámetro
     *
     * @param refCompany Organización a la que pertenece el usuario
     * @param refUser    Usuario dueño del dispositivo
     * @param force      parámetro que estable a un dispositivo como confidencial o no
     * @throws Exception
     */
    @Step("Establecer Mapp Confidencial <> <> <>")
    public void setConfidentialMApp(String refCompany, String refUser, String force) throws Exception {
        String uuid = datosGlobales.get(Constantes.UUID).toString();

        //force es la variable que determina si el dispositivo se determina confidencial (true)
        if (provisionerUser.setConfidentialApp("", refCompany, refUser, uuid, force)) {
            logger.info("Se ha establecido como confidencial el dispositivo " + uuid);
        } else {
            logger.error(new Exception("Error al establecer el dispositivo " + uuid + " como Confidencial, verifica que la compañia, el usuario y el uuid existen en la plataforma"));
            throw new Exception("");
        }
    }

    /**
     * Permite establecer como confidencial un dispositivo
     * @param refCompany Organización a la que pertenece el usuario
     * @param refUser usuario dueño del dispositivo
     * @param llaveUUID Dispositivo que se establecerá como confidencial
     * @param force true,false o vacío ""
     */
    @Step("SetConfidentialApp refCompany<> refUser<> llaveUUID<> force<>")
    public void setConfidentialApp(String refCompany, String refUser, String llaveUUID, String force) {
        String uuid = datosGlobales.get(llaveUUID).toString();
        try {
            provisionerUser.setConfidentialApp("", refCompany, refUser, uuid, force);
        } catch (com.latinia.limspinf.stubs.user.Exception_Exception e) {
            logger.info(e.getMessage());
        }

    }

    /**
     * Método que me permite verificar si al establecer como confidencial a un dispositivo inexistente, el método me devuelve false
     * @param refCompany
     * @param refUser
     * @throws Exception
     */
    @Step("Verificar respuesta uuid FALSE <> <>")
    public void setConfidentialuuidFalse(String refCompany, String refUser) throws Exception {
        String uuidError = "2099pruebaAutomated2099";
        String resultado;

        try {
            resultado = String.valueOf(provisionerUser.setConfidentialApp("", refCompany, refUser, uuidError, "true"));

            if (resultado.equals("false")) {
                logger.info("BUG INF-299 Superado - El método devuelve false como se esperaba");
            } else if (resultado.equals("true")) {
                logger.info("El método devuelve true, es posible que uuid sea correcto");
            }
        } catch (Exception e) {
            logger.error("BUG INF-299 Encontrado");
            throw new Exception("");
        }
    }

    /**
     * Verifica que un dispositivo es confidencial, por llamado ws con metodo listUserTokens
     *
     * @param refCompany
     * @param refUser
     * @throws Exception
     */

    @Step("Verificar confidencial TRUE <> <> <>")
    public boolean verifyConfidentialTRUE(String refCompany, String refUser, String llaveUUID) throws Exception {
        String uuid = datosGlobales.get(llaveUUID).toString();
        boolean existe = false;
        boolean confidencial = false;
        List<PnsToken> listTokens = provisionerUser.listUserTokens("", refCompany, refUser);

        for (int i = 0; i < listTokens.size() && !existe; i++) {
            if (listTokens.get(i).getUuidDevice().equals(uuid)) {
                if (listTokens.get(i).isConfidential()) {
                    confidencial = true;
                    logger.info("El " + llaveUUID + " es confindencial");
                } else {
                    logger.error("El " + llaveUUID + " no se ha establecido como confindencial");
                    throw new Exception("");
                }
                existe = true;
            }
        }

        if (!existe) {
            logger.error("El " + uuid + " no se encuentra");
            throw new Exception("");
        }

        if (confidencial) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica que un dispositivo es confidencial, por llamado ws con metodo listAppUserTokens
     *
     * @param refCompany
     * @param refUser
     * @throws Exception
     */

    @Step("Verificar confidencial por APP TRUE <> <> <> <>")
    public boolean verifyAppConfidentialTRUE(String refCompany, String refUser, String refApp, String llaveUUID) throws Exception {
        String uuid = datosGlobales.get(llaveUUID).toString();
        boolean existe = false;
        boolean confidencial = false;
        List<PnsToken> listTokens = provisionerUser.listAppUserTokens("", refCompany, refUser, refApp);

        for (int i = 0; i < listTokens.size() && !existe; i++) {
            if (listTokens.get(i).getUuidDevice().equals(uuid)) {
                if (listTokens.get(i).isConfidential()) {
                    confidencial = true;
                    logger.info("El " + llaveUUID + " es confindencial, solucionado BUG INF-294");
                } else {
                    logger.error("El " + llaveUUID + " no se ha establecido como confindencial, BUG INF-294");
                    throw new Exception("");
                }
                existe = true;
            }
        }

        if (!existe) {
            logger.error("El " + uuid + " no se encuentra");
            throw new Exception("");
        }

        if (confidencial) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica si el un dispositivo es o no confidencial
     * @param refCompany
     * @param refUser
     * @param llaveUUID
     * @return
     * @throws Exception
     */
    @Step("Verificar confidencial <> <> <>")
    public boolean verifyConfidential(String refCompany, String refUser, String llaveUUID) throws Exception {
        String uuid = datosGlobales.get(llaveUUID).toString();
        boolean existe = false;
        boolean confidencial = false;
        List<PnsToken> listTokens = provisionerUser.listUserTokens("", refCompany, refUser);

        for (int i = 0; i < listTokens.size() && !existe; i++) {

            if (listTokens.get(i).getUuidDevice().equals(uuid)) {
                if (listTokens.get(i).isConfidential()) {
                    confidencial = true;
                    logger.info("El " + llaveUUID + " es confindencial");
                } else {
                    confidencial = false;
                    logger.info("El " + llaveUUID + " NO es confindencial");
                }
                existe = true;
            }
        }

        if (!existe) {
            logger.error("El " + uuid + " no se encuentra");
            throw new Exception("");
        }

        if (confidencial) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Por medio de llamado ws, me obtiene el valor UUID especifico de un usuario
     *
     * @param refCompany  Organización a la que pertenece el usuario
     * @param refUser
     * @param dispositivo Me indica la posición del dispositivo dentro de la lista de dispositivos del usuario
     *                    0 me indica el primer dispositivo
     * @throws Exception
     */
    @Step("Obtener UUID <> <> <>")
    public void getUUID(String refCompany, String refUser, int dispositivo) throws Exception {
        List<PnsToken> listTokens = provisionerUser.listUserTokens("", refCompany, refUser);

        String uuid = listTokens.get(dispositivo).getUuidDevice();
        datosGlobales.put(Constantes.UUID, uuid);
    }

//En proceso, se debe probar
    @Step("Obtener contenido PNS <> <> <>")
    public void getPNScontent(String refCompany, String refApp, String refUser) {
        String publicContent;
        String privateContent;
        String idMsg = datosGlobales.get(Constantes.IDMSG).toString();
        Params params = new Params();
        params.getIdMsgExtList().add(idMsg);
        params.setRefCompany(refCompany);
        params.setRefApp(refApp);
        params.setKeyValue(refUser);

        try {
            ContentList contentList = storage.listContents(params);
            List<Content> contenidos = contentList.getContents();
            if (contenidos.isEmpty()) {
                logger.info("No hay contenidos que mostrar");
            } else {
                publicContent = contenidos.get(0).getPublicContentText();
                privateContent = contenidos.get(0).getPrivateContentText();
                logger.info("El CONTENIDO PUBLICO es " + publicContent);
                logger.info("y el CONTENIDO PRIVADO es " + privateContent);
            }
        } catch (Exception_Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * A partir del llamado WS(stub) al método getUserByRef se obtienen las propiedades del usuario
     *
     * @throws Exception
     */
    @Step("Obtener Propiedades desde Usuario")
    public void getPropfromUser() throws Exception {
        ProvisionerUser provisionerUser;
        provisionerUser = (ProvisionerUser) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_USER, Constantes.WLURL_PROVISIONER_USER,
                Constantes.WS_INF_PROVISIONER_USER_SERVICE, Constantes.WS_INF_PROVISIONER_USER_METHOD);

        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        String refUser = datosGlobales.get(Constantes.REF_USER).toString();

        User user1 = provisionerUser.getUserByRef("", refCompany, refUser);
        List<Property> props = user1.getProps();
        datosGlobales.put(Constantes.PROPS, props);
    }

    /**
     * Por medio del llamado Ws al método setUserSomeProperties, establece propiedades al usuario sin eliminar las existentes
     *
     * @throws Exception
     */
    @Step("Agregar propiedad desde WS")
    public void setUserProperties() throws Exception {
        ProvisionerUser provisionerUser;
        provisionerUser = (ProvisionerUser) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_USER, Constantes.WLURL_PROVISIONER_USER,
                Constantes.WS_INF_PROVISIONER_USER_SERVICE, Constantes.WS_INF_PROVISIONER_USER_METHOD);

        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        String refUser = datosGlobales.get(Constantes.REF_USER).toString();
        Table tabProps = (Table) datosGlobales.get(Constantes.TABLA);

        List<TableRow> rows = tabProps.getTableRows();
        List<String> columnNames = tabProps.getColumnNames();

        for (TableRow row : rows) {
            Property property = new Property();
            List<Property> props = new ArrayList<Property>();
            property.setName(row.getCell(columnNames.get(0)));
            if (row.getCell(columnNames.get(1)).contains(",")) {
                String[] valores = row.getCell(columnNames.get(1)).split(",");
                for (int i = 0; i < valores.length; i++) {
                    property.getValues().add(valores[i]);
                    logger.info("Se ha agregado la propiedad " + property.getName() + " ----> " + property.getValues().get(i));
                }
            } else {
                //Si la propiedad es gsm y su valor es virtual, se agrega el numero virtual que se encuentra en el archivo provision-naming.xml
                if (property.getName().equals("gsm")) {
                    String code = verificacionLIMSP.obtenerCodigoPais();
                    String number = verificacionLIMSP.obtenerNumeroVirtual();
                    String gsm = "";
                    if (row.getCell(columnNames.get(1)).equalsIgnoreCase("Virtual")) {
                        if (code != null && number != null) {
                            gsm = number;
                        }
                    } else {
                        gsm = row.getCell(columnNames.get(1));
                    }
                    property.getValues().add(gsm);
                    logger.info("Se ha agregado la propiedad " + property.getName() + " ----> " + gsm);
                    //--------------------------------------------------------------------------------------------------------
                } else if (property.getName().equals("twitter_name")) {
                    String twitter_name = refUser + "twtter";
                    property.getValues().add(twitter_name);
                    logger.info("Se ha agregado la propiedad " + property.getName() + " ----> " + twitter_name);
                } else if (property.getName().equals("email")) {
                    String email = refUser + "@latinia.com";
                    property.getValues().add(email);
                    logger.info("Se ha agregado la propiedad " + property.getName() + " ----> " + email);
                } else {
                    property.getValues().add(row.getCell(columnNames.get(1)));
                    logger.info("Se ha agregado la propiedad " + property.getName() + " ----> " + property.getValues().get(0));
                }
            }

            props.add(property);
            provisionerUser.setUserSomeProperties("", refCompany, refUser, props);
        }

    }

    /**
     * Permite registrar una MApp y asociarlo a un usuario por llamado WS al método "registerUserAppDevice"
     *
     * @param confidential estable si el dispositivo es confidencial
     * @param app          aplicación asociada
     * @throws Exception
     */
    @Step("Registrar AppDevice a un usuario WS <> <>")
    public void registerUserAppDevice(boolean confidential, String app) throws Exception {
        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        String refUser = datosGlobales.get(Constantes.REF_USER).toString();

        ProvisionerUser provisionerUser;
        provisionerUser = (ProvisionerUser) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_USER, Constantes.WLURL_PROVISIONER_USER,
                Constantes.WS_INF_PROVISIONER_USER_SERVICE, Constantes.WS_INF_PROVISIONER_USER_METHOD);

        //Se crea un nuevo dispositivo
        Device dispositivo = new Device();
        dispositivo.setDevModel(Constantes.POP2);
        dispositivo.setDevModel(Constantes.ALCATEL);
        dispositivo.setOsVendor(Constantes.ANDROID);
        dispositivo.setOsVersion(Constantes.LOLLIPOP);
        //Se define un nuevo token
        PnsToken pnsToken = new PnsToken();
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        pnsToken.setAppVersion("1.0");
        pnsToken.setApplication(app);
        pnsToken.setChannelProvider(Constantes.GOOGLE);
        pnsToken.setDevice(dispositivo);
        pnsToken.setConfidential(confidential);
        pnsToken.setToken(token);
        pnsToken.setUuidDevice(uuid);

        int respuesta = provisionerUser.registerUserAppDevice("", refCompany, refUser, pnsToken);

        List<PnsToken> listaTokens = provisionerUser.listUserTokens("", refCompany, refUser);
        //Los gusardo en datos globales para eliminarlo en otro método o para posible búsqueda
        datosGlobales.put(Constantes.TOKEN, token);
        datosGlobales.put(Constantes.UUID, uuid);
        datosGlobales.put(Constantes.REF_APP, app);
        if (respuesta == 1) {
            logger.info("Se ha resgistrado una nueva MApp " + app + " con TOKEN " + token + " y UUID " + uuid);
            datosGlobales.put(Constantes.TOKEN, token);
            datosGlobales.put(Constantes.UUID, uuid);
            datosGlobales.put(Constantes.REF_APP, app);
            datosGlobales.put("confidential", confidential);
            datosGlobales.put("listaTokens", listaTokens);
        }
    }

    /**
     * Permite registrar una MApp y asociarlo a un usuario por llamado WS al método "registerUserAppDevice"
     *
     * @param confidential estable si el dispositivo es confidencial
     * @param app          aplicación asociada
     * @throws Exception
     */
    @Step("Registrar AppDevice Apple a un usuario WS <> <>")
    public void registerUserAppDeviceAPPLE(boolean confidential, String app) throws Exception {
        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        String refUser = datosGlobales.get(Constantes.REF_USER).toString();

        ProvisionerUser provisionerUser;
        provisionerUser = (ProvisionerUser) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_USER, Constantes.WLURL_PROVISIONER_USER,
                Constantes.WS_INF_PROVISIONER_USER_SERVICE, Constantes.WS_INF_PROVISIONER_USER_METHOD);

        //Se crea un nuevo dispositivo
        Device dispositivo = new Device();
        dispositivo.setDevModel(Constantes.I8000);
        dispositivo.setDevModel(Constantes.APPLE);
        dispositivo.setOsVendor(Constantes.IOS);
        dispositivo.setOsVersion(Constantes.APPLE);
        //Se define un nuevo token
        PnsToken pnsToken = new PnsToken();
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        pnsToken.setAppVersion("1.0");
        pnsToken.setApplication(app);
        pnsToken.setChannelProvider(Constantes.APPLE);
        pnsToken.setDevice(dispositivo);
        pnsToken.setConfidential(confidential);
        pnsToken.setToken(token);
        pnsToken.setUuidDevice(uuid);

        int respuesta = provisionerUser.registerUserAppDevice("", refCompany, refUser, pnsToken);

        List<PnsToken> listaTokens = provisionerUser.listUserTokens("", refCompany, refUser);
        //Los gusardo en datos globales para eliminarlo en otro método o para posible búsqueda
        datosGlobales.put(Constantes.TOKEN, token);
        datosGlobales.put(Constantes.UUID, uuid);
        datosGlobales.put(Constantes.REF_APP, app);
        if (respuesta == 1) {
            logger.info("Se ha resgistrado una nueva MApp " + app + " con TOKEN " + token + " y UUID " + uuid);
            datosGlobales.put(Constantes.TOKEN, token);
            datosGlobales.put(Constantes.UUID, uuid);
            datosGlobales.put(Constantes.REF_APP, app);
            datosGlobales.put("confidential", confidential);
            datosGlobales.put("listaTokens", listaTokens);
        }
    }

    /**
     * Permite verificar que el campo "confidential" devuelve el resultado correcto
     * @throws Exception
     */
    @Step("isConfidential")
    public void esConfidencial() throws Exception {
        String token = datosGlobales.get(Constantes.TOKEN).toString();
        String uuid = datosGlobales.get(Constantes.UUID).toString();
        boolean confidential = (boolean) datosGlobales.get("confidential");
        List<PnsToken> listaTokens = (List<PnsToken>) datosGlobales.get("listaTokens");

        for (PnsToken pt : listaTokens) {
            if (pt.getToken().equals(token) && pt.getUuidDevice().equals(uuid)) {
                if (confidential) {
                    if (pt.isConfidential()) {
                        logger.info("BUG INF-314 Superado");
                    } else {
                        logger.error("BUG INF-314 Encontrado");
                        throw new Exception("");
                    }
                } else {
                    if (!pt.isConfidential()) {
                        logger.info("BUG INF-314 Superado");
                    } else {
                        logger.error("BUG INF-314 Encontrado");
                        throw new Exception("");
                    }
                }
            }
        }
    }

//    /**
//     * Permite registrar una Mapp a un usuario por medio de llamado WS utilizando el metodo "regidterUserApp"
//     * @param app
//     * @param confidential
//     * @throws Exception
//     * @JMH
//     */
//    @Step("Registrar UserApp a un usuario WS <> <>")
//    public void registerUserApp(String app, boolean confidential)throws Exception{
//        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
//        String refUser = datosGlobales.get(Constantes.REF_USER).toString();
//
//
//        //Se crea un nuevo dispositivo
//        Device dispositivo = new Device();
//        dispositivo.setDevModel(Constantes.POP2);
//        dispositivo.setDevModel(Constantes.ALCATEL);
//        dispositivo.setOsVendor(Constantes.ANDROID);
//        dispositivo.setOsVersion(Constantes.LOLLIPOP);
//        //Se define un nuevo token
//        PnsToken pnsToken = new PnsToken();
//        String token = UUID.randomUUID().toString().replaceAll("-", "");
//        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//        pnsToken.setAppVersion("1.0");
//        pnsToken.setApplication(app);
//        pnsToken.setChannelProvider(Constantes.GOOGLE);
//        pnsToken.setDevice(dispositivo);
//        pnsToken.setConfidential(confidential);
//        pnsToken.setToken(token);
//        pnsToken.setUuidDevice(uuid);
//
//        boolean registrado = provisionerUser.registerUserApp("", refCompany, refUser, pnsToken);
//
//        if(registrado == true){
//            System.out.println("Se ha registrado una nueva Mapp: " + app + " con token " + token +" y UUID " + uuid);
//
//        }else{
//            System.out.println("No se ha registrado la Mapp");
//            throw new Exception("No se ha registrado la Mapp");
//        }
//    }


    /**
     * Permite eliminar o "desregistrar" una MApp asociada a un usuario
     *
     * @throws Exception
     */
    @Step("Eliminar MApp de usuario WS llaveToken<>")
    public void unregisterUserApp(String llaveToken) throws Exception {
        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        String refUser = datosGlobales.get(Constantes.REF_USER).toString();
        String refApp = datosGlobales.get(Constantes.REF_APP).toString();
        String token = datosGlobales.get(llaveToken).toString();

        ProvisionerUser provisionerUser;
        provisionerUser = (ProvisionerUser) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_USER, Constantes.WLURL_PROVISIONER_USER,
                Constantes.WS_INF_PROVISIONER_USER_SERVICE, Constantes.WS_INF_PROVISIONER_USER_METHOD);

        boolean eliminar = provisionerUser.unregisterUserApp("", refCompany, refUser, refApp, token);

        if (eliminar) {
            logger.info("Se ha eliminado correctamente la MApp " + refApp + " con TOKEN " + token);
        } else {
            logger.error("NO Se ha podido eliminar la MApp, Puede ser que el usuario no existe o no está registrado");
            throw new Exception("");
        }
    }

    /**
     * Permite guardar token y uuid con una llave especifica, esto por si se debe guardar mas de un token y mas de un uuid y así las llaves no colisionen
     * @param token
     * @param uuid
     */
    @Step("Guardar Token y UUID llaveToken <> llaveUUID <>")
    public void guardarTokenUuid(String token, String uuid) {
        String valorToken = datosGlobales.get(Constantes.TOKEN).toString();
        logger.info("Valor token " + valorToken);
        String valorUUID = datosGlobales.get(Constantes.UUID).toString();
        logger.info("Valor UUID " + valorUUID);
        datosGlobales.put(token, valorToken);
        datosGlobales.put(uuid, valorUUID);
    }

    /**
     * Permite verificar que solo un dispositivo sea confidencial, si existen mas de uno es un bug
     * @throws Exception
     */
    @Step("Verificar si los dispositivos son confidenciales BUG-INF-346")
    public void verificarConfidencialesBugINF346() throws Exception {
        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        String refUser = datosGlobales.get(Constantes.REF_USER).toString();


        boolean resultUUID1 = verifyConfidential(refCompany, refUser, "uuid1");

        boolean resultUUID2 = verifyConfidential(refCompany, refUser, "uuid2");


        if (resultUUID1 && !resultUUID2) {
            logger.info("BUG INF-346 superado - Correcto uuid1 es confidencial y uuid2 no lo es");
        } else if (!resultUUID1 && resultUUID2) {
            logger.info("BUG INF-346 superado - Correcto uuid2 es confidencial y uuid1 no lo es");
        } else if (!resultUUID1 && !resultUUID2) {
            logger.info("BUG INF-346 superado - Correcto ninguno de los dispositivos es confidencial");
        } else if (resultUUID1 && resultUUID2) {
            logger.error("BUG INF-346 Encontrado - Los dos dispositivos son confidenciales, " +
                    "solo puede existir un dispositivo confidencial por usuario");
            throw new Exception(" ");

        }
    }

    /**
     * Permite eliminar todas las mapps asociadas a un usuario
     * @throws com.latinia.limspinf.stubs.user.Exception_Exception
     */
    @Step("Eliminar todas las MApps asociadas a usuario WS")
    public void deleteAllMAppsfromUSerWS() throws com.latinia.limspinf.stubs.user.Exception_Exception {
        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        String refUser = datosGlobales.get(Constantes.REF_USER).toString();

        List<PnsToken> listTokens = provisionerUser.listUserTokens("", refCompany, refUser);

        if (!listTokens.isEmpty()) {

            for (PnsToken token : listTokens
                    ) {

                logger.info("app" + token.getApplication());
                provisionerUser.unregisterUserApp("", refCompany, refUser, token.getApplication(), token.getToken());
            }
        } else {
            logger.info("El usuario no tiene MApps asignadas");

        }
    }


}
