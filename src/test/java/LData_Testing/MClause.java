package LData_Testing;

import Testing.Constantes;
import com.latinia.limsp.ldata.mclausefacade.ws.Ws_ld_mclausePortStub;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
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
public class MClause {
    private static Logger logger = LogManager.getLogger(MClause.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    AccesoWSLData accesoWSLData;
    MChannel mChannel;
    MContract mContract;
    List<Object> retorna;

    public MClause() {
        this.accesoWSLData = new AccesoWSLData();
        this.mChannel = new MChannel();
        this.mContract = new MContract();
    }

    /**
     * Permite asignar una clausula a un contrato específico por llamado WS
     *
     * @param tipoClausula pull. push. pullpush
     * @param formato      sms, email,pns, etc....
     * @param operador
     * @param canal
     * @param credito
     * @throws Exception
     */
    @Step("Asignar Clausula tipoClausula<> formato<> operador<> canal<> credito<>")
    public void asignarClausulaContratoWS(String tipoClausula, String formato, String operador, String canal, String credito) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCLAUSE,
                Constantes.WASURL_MCLAUSE, Constantes.WS_LD_MCLAUSE_SERVICE, Constantes.WS_LD_MCLAUSE_LOCATOR,
                Constantes.WS_LD_MCLAUSE_PORTSTUB, Constantes.WS_LD_MCLAUSE_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mclausePortStub portStub = (Ws_ld_mclausePortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String refContrato = datosGlobales.get(Constantes.REF_CONTRACT).toString();
        String idFormato = mChannel.obtenerIdFormatoMensajeWS(formato);
        String idLineIn, idLineOut, idClausula = "";
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String alias = "";
        String idContrato = "";
        if (datosGlobales.get(Constantes.ID_CONTRATO) != null) {
            if (refContrato.equalsIgnoreCase("")) {
                idContrato = datosGlobales.get(Constantes.ID_CONTRATO).toString();
            } else {
                idContrato = mContract.obtenerIdContrato(empresa, refContrato);
            }
        } else {

            if (!refContrato.equalsIgnoreCase("")) {
                idContrato = mContract.obtenerIdContrato(empresa, refContrato);
            } else {
                logger.error("El campo refContract está vacío");
                throw new Exception(" ");
            }
        }

        try {
            if (tipoClausula.equalsIgnoreCase("MT")) {
                idLineIn = mChannel.obtenerIdLineaWS(operador, "MANUAL");
                idLineOut = mChannel.obtenerIdLineaWS(operador, canal);
                idClausula = portStub.createClausePush(validation.toString(), idContrato, alias, idLineIn, idLineOut, idFormato, credito);
                logger.info("Se crea la clausula push " + idClausula + " " + operador + "-MANUAL -->" + operador + "-" + canal);
            } else if (tipoClausula.equalsIgnoreCase("MO")) {
                alias = "ALIAS"+empresa+"1";
                idLineIn = mChannel.obtenerIdLineaWS(operador, canal);
                idLineOut = mChannel.obtenerIdLineaWS(operador, "NO_MSG");
                idClausula = portStub.createClausePull(validation.toString(), idContrato, alias, idLineIn, idLineOut);
                logger.info("Se crea la clausula pull " + idClausula + " " + operador + "-" + canal + "-->" + operador + "-NO_MSG");
            } else if (tipoClausula.equalsIgnoreCase("MOMT") || tipoClausula.equalsIgnoreCase("pushpull")) {
                alias = "ALIAS"+empresa+"1";
                idLineIn = mChannel.obtenerIdLineaWS(operador, canal);
                idLineOut = mChannel.obtenerIdLineaWS(operador, canal);
                idClausula = portStub.createClausePullPush(validation.toString(), idContrato, alias, idLineIn, idLineOut, idFormato);
                logger.info("Se crea la clausula pullpush " + idClausula + " " + operador + "-" + canal + "-->" + operador + "-" + canal);
            }
            datosGlobales.put(Constantes.ID_CONTRACT, idContrato);
            datosGlobales.put(Constantes.ID_CLAUSE, idClausula);
        } catch (Exception e) {
            logger.info("Colisión de cláusulas al insertar una nueva cláusula.");
        }
    }

    /**
     * Permite eliminar la clausula previamnete creada
     *
     * @throws RemoteException
     */
    @Step("Eliminar clausula WS")
    public void eliminarClausulaWS() throws RemoteException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WlURL_MCLAUSE,
                Constantes.WASURL_MCLAUSE, Constantes.WS_LD_MCLAUSE_SERVICE, Constantes.WS_LD_MCLAUSE_LOCATOR,
                Constantes.WS_LD_MCLAUSE_PORTSTUB, Constantes.WS_LD_MCLAUSE_METHOD, Constantes.CONT_WGESTCONTRACT);
        Ws_ld_mclausePortStub portStub = (Ws_ld_mclausePortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idContrato = datosGlobales.get(Constantes.ID_CONTRACT).toString();
        String idClausula = datosGlobales.get(Constantes.ID_CLAUSE).toString();

        portStub.delContractClause(validation.toString(), idContrato, idClausula);
    }
}
