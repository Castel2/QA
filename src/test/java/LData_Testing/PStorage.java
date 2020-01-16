package LData_Testing;

import Testing.Constantes;
import Testing.VerificacionLIMSP;
import com.latinia.limspinf.stubs.storage.*;
import com.latinia.limspinf.stubs.user.ProvisionerUser;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.Exception;
import java.util.List;


/**
 * Created by amartinez on 6/03/2018.
 */
public class PStorage {

    private static Logger logger = LogManager.getLogger(PStorage.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    AccesoWSInf accesoWSInf;
    MEnterprise mEnterprise;
    private Storage storage;
    VerificacionLIMSP verificacionLIMSP;



    public PStorage() {
        accesoWSInf = new AccesoWSInf();
        mEnterprise = new MEnterprise();
        accesoWSInf = new AccesoWSInf();
        verificacionLIMSP = new VerificacionLIMSP();
    }

    /**
     * Permite consultar la lista de contenidos asignados a uno o varios usuarios y generados por una aplicación específica
     *
     * @param refApp
     * @param llaveUUID
     * @param existeContenido
     * @throws Exception
     * @Autor JMH
     */
    @Step("Verificar contenido en APP  refApp<> llaveUUID<> existeContenido<>")
    public void verificarContenido(String refApp, String llaveUUID, boolean existeContenido) throws Exception {
        storage = (Storage) accesoWSInf.wsINFGeneric(Constantes.APP_LMAN_VCONTENT, Constantes.LATINIA,
                Constantes.WASURL_STORAGE, Constantes.WLURL_STORAGE, Constantes.WS_INF_STORAGE_SERVICE,
                Constantes.WS_INF_STORAGE_METHOD);

        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        String uuidDevice = datosGlobales.get(llaveUUID).toString();
        String refUser = datosGlobales.get(Constantes.REF_USER).toString();
        String idTransaction = datosGlobales.get(Constantes.ID_TRANSACTION).toString();
        Params params = new Params();
        params.setRefCompany(refCompany);
        params.setRefApp(refApp);
        params.setUuidDevice(uuidDevice);
        params.setKeyValue(refUser);
        params.getIdTransactionList().add(idTransaction);
        String publicContent;
        String privateContent;


        ContentList contentList = storage.listContents(params);
        List<Content> contenidos = contentList.getContents();
        logger.info("CONTENIDO--> " + contenidos.toString());
        if (existeContenido) {
            if (!contenidos.isEmpty()) {
                publicContent = contenidos.get(0).getPublicContentText();
                privateContent = contenidos.get(0).getPrivateContentText();

                logger.info("Publico = " + publicContent);
                logger.info("Private = " + privateContent);


            } else {

                logger.error("No hay contenido en la aplicacion " + refApp);
                throw new Exception(" ");

            }

        } else {
                    if (contenidos.isEmpty()) {
                        logger.info("No hay contenido en la aplicación " + refApp);
                    } else {
                        publicContent = contenidos.get(0).getPublicContentText();
                        privateContent = contenidos.get(0).getPrivateContentText();
                        logger.info("Publico = " + publicContent);
                        logger.info("Private = " + privateContent);
                        logger.error("Hay contenido en la aplicacion y esta activada la propiedad " + refApp);
                        throw new Exception(" ");
                    }
                }
        }

}
