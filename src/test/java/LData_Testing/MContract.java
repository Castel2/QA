package LData_Testing;

import Testing.*;
import com.latinia.limsp.ldata.mcontractfacade.ws.Ws_ld_mcontractPortStub;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import com.latinia.util.lxobjects.LXList;
import com.latinia.util.lxobjects.LXObject;
import com.latinia.util.lxobjects.LXSerializer;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by amartinez on 6/03/2018.
 */
public class MContract {
    private static Logger logger = LogManager.getLogger(MContract.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    AccesoWSLData accesoWSLData;
    MProduct mProduct;
    MChannel mChannel;
    MUser mUser;
    LatiniaScenarioUtil latiniaScenarioUtil;
    MEnterprise mEnterprise;

    List<Object> retorna;

    public MContract() {
        this.accesoWSLData = new AccesoWSLData();
        this.mProduct = new MProduct();
        this.mChannel = new MChannel();
        this.mUser = new MUser();
        this.latiniaScenarioUtil = new LatiniaScenarioUtil();
        this.mEnterprise = new MEnterprise();
    }

    /**
     * Este método nos permite crear un contrato a través del llamado WS
     *
     * @throws Exception
     */
    @Step("crear contrato WS")
    public void crearContratoWS() throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCONTRACT,
                Constantes.WASURL_MCONTRACT, Constantes.WS_LD_MCONTRACT_SERVICE, Constantes.WS_LD_MCONTRACT_LOCATOR,
                Constantes.WS_LD_MCONTRACT_PORTSTUB, Constantes.WS_LD_MCONTRACT_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mcontractPortStub portStub = (Ws_ld_mcontractPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String refProduct = datosGlobales.get(Constantes.REF_PRODUCT).toString();
        String refContract = datosGlobales.get(Constantes.REF_CONTRACT).toString();
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        //El nombre del contrato sera el nombre del producto + Automated
        String idContrato = "";
        String nomproducto = mProduct.obtenerNomProduct(refProduct);
        String nomcontrato = nomproducto + " " + Constantes.AUTOMATED;
        String idEmpresa = mEnterprise.obtenerIdEmpresa(empresa);
        boolean existe = false;
        List<String> listaContratos = listaContratosWS(empresa);

        for (String contrato : listaContratos
                ) {
            if (contrato.equalsIgnoreCase(nomcontrato)) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            Timestamp timestampIni = new Timestamp(System.currentTimeMillis());
            String idProducto = mProduct.obtenerIdProduct(refProduct);
            String tsIni = String.valueOf(timestampIni.getTime());
            //String5 o tsEnd, ponemos una fecha igual en todos los contratos que será del año 2099
            idContrato = portStub.createContract(validation.toString(), idProducto, nomcontrato, idEmpresa, refContract, tsIni, "4070995199000");
            logger.info("CREANDO CONTRATO ..... " + idContrato + "-" + nomcontrato);
        } else {
            idContrato = obtenerIdContrato(empresa, nomcontrato);
            logger.info("CREANDO CONTRATO ..... " + idContrato + "-" + nomcontrato);
        }
        datosGlobales.put(Constantes.ID_CONTRATO, idContrato);
    }

    /**
     * Permite eliminar un contrato previamente creado, si no se puede eliminar se procede a caducarlo
     *
     * @throws Exception
     */
    @Step("Eliminar contrato WS")
    public void eliminarContratoWS() throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCONTRACT,
                Constantes.WASURL_MCONTRACT, Constantes.WS_LD_MCONTRACT_SERVICE, Constantes.WS_LD_MCONTRACT_LOCATOR,
                Constantes.WS_LD_MCONTRACT_PORTSTUB, Constantes.WS_LD_MCONTRACT_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mcontractPortStub portStub = (Ws_ld_mcontractPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idContrato = datosGlobales.get(Constantes.ID_CONTRATO).toString();

        boolean eliminar = portStub.deleteContract(validation.toString(), idContrato);
        if (eliminar) {
            logger.info("Se ha eliminado correctamente el contrato");
        } else {
            logger.info("El contrato no se puede eliminar, se procede a caducarlo");
            portStub.expireContract(validation.toString(), idContrato);
        }
    }

    /**
     * Este método permite asignar una propiedad a un contrato a partir del llamado WS
     *
     * @param refContract It is the contract to which the property will be added
     * @param refPlatform Is the reference of platform at witch belong the property, LIMSP or LGUI
     * @param propiedad   The property to assign
     * @param valor       Is the value to assign to the property
     * @throws Exception
     */
    @Step("Asignar Propiedad/Contrato WS <> <> <> <>")
    public void asignarPropiedadContratoWS(String refContract, String refPlatform, String propiedad, String valor) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCONTRACT,
                Constantes.WASURL_MCONTRACT, Constantes.WS_LD_MCONTRACT_SERVICE, Constantes.WS_LD_MCONTRACT_LOCATOR,
                Constantes.WS_LD_MCONTRACT_PORTSTUB, Constantes.WS_LD_MCONTRACT_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mcontractPortStub portStub = (Ws_ld_mcontractPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String idContrato = obtenerIdContrato(empresa, refContract);

        if(propiedad.equals("PRIORITY")){
            datosGlobales.put(Constantes.PRIORITY, valor);
        }

        portStub.setContractProperty(validation.toString(), idContrato, refPlatform, propiedad, valor);
    }

    /**
     * Este método elimina una propiedad de un contrato por medio de llamado WS
     *
     * @param refContract contrato al que se le elimina la propiedad
     * @param refPlatform Plataforma a la que pertenece la propiedad (LIMSP - GUI)
     * @param property    Propiedad a elimina
     * @throws Exception
     */
    @Step("Eliminar Propiedad/Contrato WS refContract<> refPlatform<> property<>")
    public void eliminarPropiedadContratoWS(String refContract, String refPlatform, String property) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCONTRACT,
                Constantes.WASURL_MCONTRACT, Constantes.WS_LD_MCONTRACT_SERVICE, Constantes.WS_LD_MCONTRACT_LOCATOR,
                Constantes.WS_LD_MCONTRACT_PORTSTUB, Constantes.WS_LD_MCONTRACT_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mcontractPortStub portStub = (Ws_ld_mcontractPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String idContrato = obtenerIdContrato(empresa, refContract);

        portStub.delContractProperty(validation.toString(), idContrato, refPlatform, property);
    }

    /**
     * Este método permite obtener el IdContract a partir del refContract por llamado WS
     *
     * @param contract Contrato al que se le quiere obtener el ID
     * @return idContract
     * @throws Exception
     */
    public String obtenerIdContrato(String empresa, String contract) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCONTRACT,
                Constantes.WASURL_MCONTRACT, Constantes.WS_LD_MCONTRACT_SERVICE, Constantes.WS_LD_MCONTRACT_LOCATOR,
                Constantes.WS_LD_MCONTRACT_PORTSTUB, Constantes.WS_LD_MCONTRACT_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mcontractPortStub portStub = (Ws_ld_mcontractPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String contracts;
        String idContract = "";
        String idEmpresa = mEnterprise.obtenerIdEmpresa(empresa);
        contracts = portStub.listEnterpriseContracts(validation.toString(), idEmpresa);
        LXList lxContracts = (LXList) LXSerializer.readLX(contracts);

        for (int idx = 0; idx < lxContracts.getSize(); idx++) {
            LXObject xObj = (LXObject) lxContracts.getObject(idx);
            if (xObj.getPropertyValue(Constantes.REF_CONTRACT) != null) {
                if (xObj.getPropertyValue(Constantes.REF_CONTRACT).equalsIgnoreCase(contract) || xObj.getPropertyValue(Constantes.DESCRIPTION).equalsIgnoreCase(contract)) {
                    idContract = xObj.getPropertyValue(Constantes.ID_CONTRACT);
                }
            } else {
                if (xObj.getPropertyValue(Constantes.DESCRIPTION).equalsIgnoreCase(contract)) {
                    idContract = xObj.getPropertyValue(Constantes.ID_CONTRACT);
                }
            }
        }
        return idContract;
    }

    /**
     * Permite listar los contratos pertenecientes a una empresa especifica a través de llamado WS
     *
     * @param empresa
     * @return
     * @throws Exception
     */
    @Step("Lista contratos <>")
    public List<String> listaContratosWS(String empresa) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCONTRACT,
                Constantes.WASURL_MCONTRACT, Constantes.WS_LD_MCONTRACT_SERVICE, Constantes.WS_LD_MCONTRACT_LOCATOR,
                Constantes.WS_LD_MCONTRACT_PORTSTUB, Constantes.WS_LD_MCONTRACT_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mcontractPortStub portStub = (Ws_ld_mcontractPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idEmpresa = mEnterprise.obtenerIdEmpresa(empresa);
        //Se listan los contratos de una empresa
        String listaContratos = portStub.listEnterpriseContracts(validation.toString(), idEmpresa);
        LXList xContratos = (LXList) LXSerializer.readLX(listaContratos);
        List<String> valoresPlataforma = latiniaScenarioUtil.convertirXMLDatatoString(xContratos, "description");
        datosGlobales.put(Constantes.VALUES_PLATFORM, valoresPlataforma);

        return valoresPlataforma;
    }

    /**
     * Permite asignar un contrato a un uauario a través de llamado WS
     *
     * @throws Exception
     */
    @Step("Asignar usuario a contrato")
    public void asignarUsuarioContrato() throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCONTRACT,
                Constantes.WASURL_MCONTRACT, Constantes.WS_LD_MCONTRACT_SERVICE, Constantes.WS_LD_MCONTRACT_LOCATOR,
                Constantes.WS_LD_MCONTRACT_PORTSTUB, Constantes.WS_LD_MCONTRACT_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mcontractPortStub portStub = (Ws_ld_mcontractPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String usuario = datosGlobales.get(Constantes.USER).toString();
        String refProduct = datosGlobales.get(Constantes.REF_PRODUCT).toString();
        //El nombre del contrato sera el nombre del producto + Automated
        String nomContrato = mProduct.obtenerNomProduct(refProduct) + " " + Constantes.AUTOMATED;
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String idEmpresa = mEnterprise.obtenerIdEmpresa(empresa);
        String idContrato = obtenerIdContrato(empresa, nomContrato);
        String idUsuario = mUser.obtenerIdUsuario(usuario, idEmpresa);
        if (!existeContratoPorUsuario(idContrato)) {
            portStub.bindUser(validation.toString(), idContrato, idUsuario);
        } else {
            logger.info("El contrato " + nomContrato + " ya está asociado al usuario " + usuario);
        }

    }

    /**
     * Permite verificar la relación entre un contrato y un usuario
     * @param idContrato
     * @return
     * @throws Exception
     */
    public boolean existeContratoPorUsuario(String idContrato) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCONTRACT,
                Constantes.WASURL_MCONTRACT, Constantes.WS_LD_MCONTRACT_SERVICE, Constantes.WS_LD_MCONTRACT_LOCATOR,
                Constantes.WS_LD_MCONTRACT_PORTSTUB, Constantes.WS_LD_MCONTRACT_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mcontractPortStub portStub = (Ws_ld_mcontractPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        boolean existe = false;
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String idEmpresa = mEnterprise.obtenerIdEmpresa(empresa);
        String usuario = datosGlobales.get(Constantes.USER).toString();
        String idUsuario = mUser.obtenerIdUsuario(usuario, idEmpresa);

        String lista = portStub.listUserContracts(validation.toString(), idEmpresa, idUsuario);
        LXList lxUsuarioContrato = (LXList) LXSerializer.readLX(lista);

        for (int idx = 0; idx < lxUsuarioContrato.getSize(); idx++) {
            LXObject xObj = (LXObject) lxUsuarioContrato.getObject(idx);
            if (xObj.getPropertyValue(Constantes.ID_CONTRACT).equalsIgnoreCase(idContrato))
                existe = true;

        }

        return existe;

    }

    /**
     * Permite realizar la acción forzar recarga a partir del llamado WS
     *
     * @throws RemoteException
     */
    @Step("Forzar recarga WS")
    public void forzarRecargaWS() throws RemoteException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCONTRACT,
                Constantes.WASURL_MCONTRACT, Constantes.WS_LD_MCONTRACT_SERVICE, Constantes.WS_LD_MCONTRACT_LOCATOR,
                Constantes.WS_LD_MCONTRACT_PORTSTUB, Constantes.WS_LD_MCONTRACT_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mcontractPortStub portStub = (Ws_ld_mcontractPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        portStub.refreshLimspData(validation.toString());
        logger.info("Forzando recarga ...");
    }
}
