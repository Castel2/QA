package Testing;// JUnit Assert framework can be used for verification

//import net.sf.sahi.client.Browser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import LData_Testing.AccesoWSLData;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;

import com.latinia.limsp.ldata.pstoragefacade.ws.Ws_ld_pstoragePortStub;
import com.latinia.limsp.ldata.qbasicfacade.ws.Ws_ld_qbasicPortStub;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import com.latinia.util.lxobjects.LXList;
import com.latinia.util.lxobjects.LXObject;
import com.latinia.util.lxobjects.LXSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InterfazLDataSDP {


    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    AccesoWSLData accesoWSLData;
    private static Logger logger = LogManager.getLogger(InterfazLDataSDP.class);

    public InterfazLDataSDP() {
        accesoWSLData = new AccesoWSLData();
    }

    /**
     * @param refProduct
     * @param pswProduct
     * @param refContract
     * @param loginEnterprise
     * @param user
     * @param diasatras
     * @throws Exception
     */

    @Step("Interfaz LD_SDP <> <> <> <> <> <>")
    public void interfazLDSDP(String refProduct, String pswProduct, String refContract, String loginEnterprise, String user, int diasatras) throws Exception {
        String ids[] = new String[2];
        ids = pruebasDeQBasic(refProduct, pswProduct, refContract, loginEnterprise, user);
        pruebasDePStorage(refProduct, pswProduct, refContract, loginEnterprise, user, diasatras, ids[1]);

    }


    public String[] pruebasDeQBasic(String refProduct, String pswProduct, String refContract, String loginEnterprise, String user) throws Exception {
        String ids[] = new String[2];
        //ids[0]-->idUsuario
        //ids[1]-->Lista de contratos del usuario

        List<Object> retorna;
        retorna = accesoWSLData.wsLDataGeneric(refProduct, pswProduct, Constantes.WlURL_QBASIC,
                Constantes.WASURL_QBASIC, Constantes.WS_LD_QBASIC_SERVICE, Constantes.WS_LD_QBASIC_LOCATOR,
                Constantes.WS_LD_QBASIC_PORTSTUB, Constantes.WS_LD_QBASIC_METHOD, refContract);

        Ws_ld_qbasicPortStub portStub = (Ws_ld_qbasicPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        logger.info("Invocando QBASIC");
        String xmlUser = portStub.getUser(validation.toString()); //Se realiza la invocacion al WS, pasandole todos los datos establecidos anteriormente. Se recogen el XML devuelto
        LXObject xUser = (LXObject) LXSerializer.readLX(xmlUser); // Lee el XML devuelto anteriormente
        ids[0] = xUser.getPropertyValue("idUser"); //Extrae el campo "idUser" del XML devuelto
        logger.info("ID-USER:" + ids[0]);


        String xmlContracts = portStub.listContracts(validation.toString(), 0); //Invoco para obtener un XML con la lista de contratos que tiene el usuario
        LXList lxContracts = (LXList) LXSerializer.readLX(xmlContracts);
        logger.info("Se han encontrado " + lxContracts.getSize() + " contratos\n");
        //Itero el XML y voy sacando los Ids de los contratos
        ids[1] = "";
        for (int idx = 0; idx < lxContracts.getSize(); idx++) {
            LXObject xObj = (LXObject) lxContracts.getObject(idx);
            ids[1] = ids[1] + xObj.getPropertyValue(Constantes.ID_CONTRACT);
            if (idx < ((lxContracts.getSize()) - 1)) {
                ids[1] = ids[1] + ",";
            } //Solo lo anoto si tiene clausulas
        }

        return ids; //La posicion [0] contiene el idUser. La posicion [1] contiene los ids de los contratos obtenidos para dicho usuario
    }


    public void pruebasDePStorage(String refProduct, String pswProduct, String refContract, String loginEnterprise, String user, int diasatras, String listacontratosQbasic) throws Exception {
//        String host = datosGlobales.get(Constantes.HOST).toString();
//        String port = datosGlobales.get(Constantes.PORT).toString();

        String tsfrom;
        String tsto;
        String dtExpire;
        long[] misfechas = new long[2];
        //Obtiene las fechas a utilizar en los filtros
        misfechas = Limspinfltest.fechas(diasatras);
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //Instancio el objeto para poder formatear desde Timestamp a yyyy-MM-dd HH:mm:ss
        tsfrom = date.format(misfechas[0]);
        tsto = date.format(misfechas[1]);

        logger.info("tsfrom: " + tsfrom);
        logger.info("tsto:   " + tsto);

        // Instancio el Stub con las clases de PStorage
        List<Object> retorna;
        retorna = accesoWSLData.wsLDataGeneric(refProduct, pswProduct, Constantes.WLURL_PSTORAGE,
                Constantes.WASURL_PSTORAGE, Constantes.WS_LD_PSTORAGE_SERVICE, Constantes.WS_LD_PSTORAGE_LOCATOR,
                Constantes.WS_LD_PSTORAGE_PORTSTUB, Constantes.WS_LD_PSTORAGE_METHOD, refContract);

        Ws_ld_pstoragePortStub portStub = (Ws_ld_pstoragePortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);


        /**
         * Construcción del XML de parámetros que se utilizaran para la consulta
         */
        LXObject lxParams = new LXObject("params", "mislistados");
        DateFormat datesinhora = new SimpleDateFormat("yyyy-MM-dd"); //Instancio el objeto para formatear desde Timestamp a yyyy-MM-dd
        dtExpire = datesinhora.format(misfechas[0]);
        lxParams.setPropertyValue("dtExpire", dtExpire);


        /**
         * Invocacion a PStorage listContracts.
         * Obtiene los mismos resultados que se han recogido anteriormente desde QBasic en el parametro 'idContractlist'.
         * Aqui lo obtenemos simplemente para comprobar que ambos son iguales.
         */
        logger.info("Invocando 'PStorage' para listContracts");
        LXList listContracts = (LXList) LXSerializer.readLX(portStub.invoke(validation.toString(), "listContracts", null, LXSerializer.serializeLX(lxParams), null));
        logger.info("Se han encontrado " + listContracts.getSize() + " contratos\n");

        String idContractlist = "";
        for (int idx = 0; idx < listContracts.getSize(); idx++) {
            LXObject xObj = (LXObject) listContracts.getObject(idx);
            idContractlist = idContractlist + xObj.getPropertyValue("idContract");
            if (idx < ((listContracts.getSize()) - 1)) {
                idContractlist = idContractlist + ",";
            }
        }

        logger.info("Lista contratos desde PStorage=" + idContractlist);
        logger.info("Lista contratos desde QBasic=" + listacontratosQbasic + "\n");


        //Ajuste de parámetros para las siguientes comprobaciones
        lxParams.deleteProperty("dtExpire"); //Borro del objeto la propiedad dtExpire porque no es necesario para las siguientes invocaciones
        lxParams.setPropertyValue("idContract-list", listacontratosQbasic); //Contratos a consultar
        lxParams.setPropertyValue("tsFrom", tsfrom); //Intervalo de tiempo. Formato yyyy-MM-dd HH:mm:ss
        lxParams.setPropertyValue("tsTo", tsto);


        //Invocacion a PStorage listContent
        logger.info("Invocando 'PStorage' para listContent");
        LXList listacontenidos = (LXList) LXSerializer.readLX(portStub.invoke(validation.toString(), "listContent", null, LXSerializer.serializeLX(lxParams), null));
        LXList estadostransaccion = null;
        int items = 0;
        int id = 0;
        String idTrans = "";
        while (items == 0) {//Leo la siguiente transaccion, hasta que alguna contenga estados.
            LXObject xObjlistacontenidos = (LXObject) listacontenidos.getObject(id); //Recojo el idTrans del primer mensaje, para utilizarlo en la llamada a 'MessageStatusDetail'
            idTrans = xObjlistacontenidos.getPropertyValue("idTrans");

            //Ajuste de parámetros para comprobacion de los estados
            lxParams.setPropertyValue("idTrans", idTrans); //Transaccion a consultar
            estadostransaccion = (LXList) LXSerializer.readLX(portStub.invoke(validation.toString(), "MessageStatusDetail", null, LXSerializer.serializeLX(lxParams), null)); //Consulta los estados de la transaccion
            items = estadostransaccion.getSize();
            lxParams.deleteProperty("idTrans");
            id++;
        }
        logger.info("Se han encontrado " + listacontenidos.getSize() + " transacciones\n");

        //Invocacion a PStorage MessageStatusDetail
        logger.info("Invocando 'PStorage' para MessageStatusDetail= " + idTrans);
        if (items > 0) {
            String estados = "";
            for (int idx = 0; idx < estadostransaccion.getSize(); idx++) {
                LXObject xObj2 = (LXObject) estadostransaccion.getObject(idx);
                estados = estados + xObj2.getPropertyValue("sent") + "/" + xObj2.getPropertyValue("state");
                if (idx < ((estadostransaccion.getSize()) - 1)) {
                    estados = estados + " >> ";
                }
            }
            logger.info("Estados de la transaccion '" + idTrans + "' [" + estados + "]\n");
        }

        //Invocacion a PStorage listContentEMail
        //lxParams.setPropertyValue("addrType", "to"); //Sentido del mensaje (to, cc, bcc o from)
        logger.info("Invocando 'PStorage' para listContentEMail");
        LXList listacontenidosemail = (LXList) LXSerializer.readLX(portStub.invoke(validation.toString(), "listContentEMail", null, LXSerializer.serializeLX(lxParams), null));
        logger.info("Se han encontrado " + listacontenidosemail.getSize() + " contenidos de Email");
    }


}