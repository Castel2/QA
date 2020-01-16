package LData_Testing;

import Testing.Constantes;
import com.latinia.limsp.ldata.muserfacade.ws.Ws_ld_muserPortStub;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import com.latinia.util.lxobjects.LXException;
import com.latinia.util.lxobjects.LXList;
import com.latinia.util.lxobjects.LXObject;
import com.latinia.util.lxobjects.LXSerializer;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by amartinez on 9/03/2018.
 */
public class MUser {

    private static Logger logger = LogManager.getLogger(MUser.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    AccesoWSLData accesoWSLData;
    MEnterprise mEnterprise;

    List<Object> retorna;

    public MUser() {
        this.accesoWSLData = new AccesoWSLData();
        this.mEnterprise = new MEnterprise();
    }

    /**
     * Permite crear un nuevo usuario en una empresa especifica
     *
     * @throws Exception
     */
    @Step("Crear nuevo usuario WS")
    public void agregarUsuarioWS() throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTUSER, Constantes.LATINIA, Constantes.WlURL_MUSER,
                Constantes.WASURL_MUSER, Constantes.WS_LD_MUSER_SERVICE, Constantes.WS_LD_MUSER_LOCATOR,
                Constantes.WS_LD_MUSER_PORTSTUB, Constantes.WS_LD_MUSER_METHOD, Constantes.CONT_WGESTUSER);
        Ws_ld_muserPortStub portStub = (Ws_ld_muserPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String usuario = datosGlobales.get(Constantes.USER).toString();
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String idEmpresa = mEnterprise.obtenerIdEmpresa(empresa);
        usuario = usuario.toUpperCase();
        //Se verifica si ya existe el usuario

        //Se verifica que no se haya alcanzado el maximo número de usuarios permitidos por la licencia
        if (obtenerIdUsuario(usuario, idEmpresa).equalsIgnoreCase("")) {
            if (!portStub.maxUsersReached(validation.toString())) {
                //Se crea el usuario con los parametros (validacion,idEnterprise,user,password)
                portStub.addUser(validation.toString(), idEmpresa, usuario, usuario);

                logger.info("Se crea el usuario " + usuario);
            } else {
                logger.error("El usuario no se puede crear porque se ha alcanzado el maximo número de usuarios que permite la licencia");
                throw new Exception(" ");
            }
        } else {
            logger.info("El usuario " + usuario + " ya existe para la empresa " + empresa);
        }
    }

    /**
     * Permite obtener el IdUser de un usuario perteneciente a una empresa especifica
     *
     * @param usuario
     * @param idEmpresa
     * @return
     * @throws RemoteException
     * @throws LXException
     */
    @Step("ID USER <> <>")
    public String obtenerIdUsuario(String usuario, String idEmpresa) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTUSER, Constantes.LATINIA, Constantes.WlURL_MUSER,
                Constantes.WASURL_MUSER, Constantes.WS_LD_MUSER_SERVICE, Constantes.WS_LD_MUSER_LOCATOR,
                Constantes.WS_LD_MUSER_PORTSTUB, Constantes.WS_LD_MUSER_METHOD, Constantes.CONT_WGESTUSER);
        Ws_ld_muserPortStub portStub = (Ws_ld_muserPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idUsuario = "";
        String listaUsuarios = portStub.listEnterpriseUsers(validation.toString(), idEmpresa);
        LXList lxUsuarios = (LXList) LXSerializer.readLX(listaUsuarios);
        for (int idx = 0; idx < lxUsuarios.getSize(); idx++) {
            LXObject xObj = (LXObject) lxUsuarios.getObject(idx);
            if (xObj.getPropertyValue(Constantes.LOGIN).equalsIgnoreCase(usuario)) {
                idUsuario = xObj.getPropertyValue(Constantes.ID_USER);
                break;
            }
        }

        return idUsuario;
    }

    /**
     * Permite eliminar un usuario especifico
     *
     * @param usuario
     * @throws Exception
     */
    @Step("Eliminar usuario WS usuario<>")
    public void eliminarUsuarioWS(String usuario) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTUSER, Constantes.LATINIA, Constantes.WlURL_MUSER,
                Constantes.WASURL_MUSER, Constantes.WS_LD_MUSER_SERVICE, Constantes.WS_LD_MUSER_LOCATOR,
                Constantes.WS_LD_MUSER_PORTSTUB, Constantes.WS_LD_MUSER_METHOD, Constantes.CONT_WGESTUSER);
        Ws_ld_muserPortStub portStub = (Ws_ld_muserPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String idEmpresa = mEnterprise.obtenerIdEmpresa(empresa);
        usuario = usuario.toUpperCase();
        String idUsuario = obtenerIdUsuario(usuario, idEmpresa);
        System.out.println(obtenerIdUsuario(usuario, idEmpresa).length());
        if (!obtenerIdUsuario(usuario, idEmpresa).equalsIgnoreCase("")) {

            portStub.deleteUser(validation.toString(), idEmpresa, idUsuario);
            logger.info("Se elimina usuario " + usuario);
        }
    }
}
