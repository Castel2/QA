package LData_Testing;

import Testing.Constantes;
import com.latinia.limsp.ldata.mchannelfacade.ws.Ws_ld_mchannelPortStub;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import com.latinia.util.lxobjects.LXException;
import com.latinia.util.lxobjects.LXList;
import com.latinia.util.lxobjects.LXObject;
import com.latinia.util.lxobjects.LXSerializer;
import com.thoughtworks.gauge.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by amartinez on 6/03/2018.
 */
public class MChannel {

    private static Logger logger = LogManager.getLogger(MChannel.class);
    AccesoWSLData accesoWSLData;
    List<Object> retorna;

    public MChannel() {
        accesoWSLData = new AccesoWSLData();
    }

    /**
     * Permite crear un canal mediante llamado WS
     *
     * @param canal canal a crear
     * @throws RemoteException
     * @throws LXException
     */
    @Step("Crear Canal Email WS canal<>")
    public void crearCanalEmailWS(String canal) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCHANNEL, Constantes.LATINIA, Constantes.WlURL_MCHANNEL,
                Constantes.WASURL_MCHANNEL, Constantes.WS_LD_MCHANNEL_SERVICE, Constantes.WS_LD_MCHANNEL_LOCATOR,
                Constantes.WS_LD_MCHANNEL_PORTSTUB, Constantes.WS_LD_MCHANNEL_METHOD, Constantes.CONT_WGESTCHANNEL);
        Ws_ld_mchannelPortStub portStub = (Ws_ld_mchannelPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String operador = "EMail-SMTP";
        String idOperador = obtenerIdOperador(operador);
        String idFormato = obtenerIdFormatoMensajeWS("EMail");
        //Se crea el canal
        portStub.createChannel(validation.toString(), canal, false, true, false);
        logger.info("El canal " + canal + " fue creado exitosamente");
        //Se crea el modelo

        if (existeCanalModelo(false, canal)) {
            portStub.createModels(canal, idOperador, idOperador, idFormato, "Virtual");
            logger.info("Se crea nuevo modelo para email");
        } else {
            if (existeCanalModelo(true, canal)) {
                logger.info("El modelo de email " + operador + "-" + canal + " ya existe");
            } else {
                logger.error("El modelo no existe y no se ha podido crear");
                throw new Exception(" ");
            }
        }
    }

    /**
     * Permite conocer la existencia de un canal relacionado a un modelo
     *
     * @param conModelo
     * @param canal
     * @return
     * @throws RemoteException
     * @throws LXException
     */
    public boolean existeCanalModelo(boolean conModelo, String canal) throws RemoteException, LXException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCHANNEL, Constantes.LATINIA, Constantes.WlURL_MCHANNEL,
                Constantes.WASURL_MCHANNEL, Constantes.WS_LD_MCHANNEL_SERVICE, Constantes.WS_LD_MCHANNEL_LOCATOR,
                Constantes.WS_LD_MCHANNEL_PORTSTUB, Constantes.WS_LD_MCHANNEL_METHOD, Constantes.CONT_WGESTCHANNEL);
        Ws_ld_mchannelPortStub portStub = (Ws_ld_mchannelPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        boolean existe = false;

        String listaModelos = portStub.listChannels(conModelo);
        LXList lxModelos = (LXList) LXSerializer.readLX(listaModelos);

        for (int idx = 0; idx < lxModelos.getSize(); idx++) {
            LXObject xObj = (LXObject) lxModelos.getObject(idx);

            if (xObj.getPropertyValue(Constantes.REF_CHANNEL).equals(canal)) {
                existe = true;
            }
        }
        return existe;
    }

    /**
     * Permite obtener una el idOperator a partir de refOperator a partir de llamado WS
     *
     * @param refOperador
     * @return
     * @throws RemoteException
     * @throws LXException
     */
    public String obtenerIdOperador(String refOperador) throws RemoteException, LXException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCHANNEL, Constantes.LATINIA, Constantes.WlURL_MCHANNEL,
                Constantes.WASURL_MCHANNEL, Constantes.WS_LD_MCHANNEL_SERVICE, Constantes.WS_LD_MCHANNEL_LOCATOR,
                Constantes.WS_LD_MCHANNEL_PORTSTUB, Constantes.WS_LD_MCHANNEL_METHOD, Constantes.CONT_WGESTCHANNEL);
        Ws_ld_mchannelPortStub portStub = (Ws_ld_mchannelPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idOperador = "";
        String listaOperadores = portStub.listOperators(validation.toString());

        LXList lxOperadores = (LXList) LXSerializer.readLX(listaOperadores);
        for (int idx = 0; idx < lxOperadores.getSize(); idx++) {
            LXObject xObj = (LXObject) lxOperadores.getObject(idx);

            if (xObj.getPropertyValue(Constantes.REF_OPERATOR).equalsIgnoreCase(refOperador)) {
                idOperador = xObj.getPropertyValue(Constantes.ID_OPERATOR);
            }

        }
        return idOperador;
    }

    /**
     * Permite crear un modelo sms a partir de llamado WS
     *
     * @param modelo   MT, MO, MOMT
     * @param operador Virtual
     * @param canal    +000001
     * @throws Exception
     */
    @Step("Crear Modelo SMS WS modelo<> operador<Virtual> canal<+000001>")
    public void crearModeloSMSWS(String modelo, String operador, String canal) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCHANNEL, Constantes.LATINIA, Constantes.WlURL_MCHANNEL,
                Constantes.WASURL_MCHANNEL, Constantes.WS_LD_MCHANNEL_SERVICE, Constantes.WS_LD_MCHANNEL_LOCATOR,
                Constantes.WS_LD_MCHANNEL_PORTSTUB, Constantes.WS_LD_MCHANNEL_METHOD, Constantes.CONT_WGESTCHANNEL);
        Ws_ld_mchannelPortStub portStub = (Ws_ld_mchannelPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idLineIn = "";
        String idLineOut = "";
        String tipoMensaje = "sms";
        String idColector = "Virtual";

        if (modelo.equalsIgnoreCase("MT")) {
            idLineIn = obtenerIdLineaWS(operador, "MANUAL");
            idLineOut = obtenerIdLineaWS(operador, canal);
            portStub.createModel(idLineIn, idLineOut, tipoMensaje, idColector);
        } else if (modelo.equalsIgnoreCase("MO")) {
            idLineIn = obtenerIdLineaWS(operador, canal);
            idLineOut = obtenerIdLineaWS(operador, "NO_MSG");
            portStub.createModel(idLineIn, idLineOut, tipoMensaje, idColector);
        } else if (modelo.equalsIgnoreCase("MOMT")) {
            idLineIn = obtenerIdLineaWS(operador, canal);
            idLineOut = obtenerIdLineaWS(operador, canal);
            portStub.createModel(idLineIn, idLineOut, tipoMensaje, idColector);
        } else {
            logger.info("El modelo no equivale a los modelos existente MT, MO, MOMT");
        }
    }

    /**
     * Este método nos permite obtener el ID de una linea específica
     * Una línea = operador + canal
     *
     * @param refOperator referencia del operador de la línea
     * @param refChannel  referencia del canal de la línea
     * @return id de la línea
     * @throws Exception
     */
    public String obtenerIdLineaWS(String refOperator, String refChannel) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCHANNEL, Constantes.LATINIA, Constantes.WlURL_MCHANNEL,
                Constantes.WASURL_MCHANNEL, Constantes.WS_LD_MCHANNEL_SERVICE, Constantes.WS_LD_MCHANNEL_LOCATOR,
                Constantes.WS_LD_MCHANNEL_PORTSTUB, Constantes.WS_LD_MCHANNEL_METHOD, Constantes.CONT_WGESTCHANNEL);
        Ws_ld_mchannelPortStub portStub = (Ws_ld_mchannelPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idLinea = null;
        String lineas = portStub.listChannelLines(validation.toString());
        LXList lxLineas = (LXList) LXSerializer.readLX(lineas);
        String lineasGateway = portStub.listChannelLinesForGateway(validation.toString());
        LXList lxLineasGateway = (LXList) LXSerializer.readLX(lineasGateway);

        for (int idx = 0; idx < lxLineas.getSize() && idLinea == null; idx++) {
            LXObject xObj = (LXObject) lxLineas.getObject(idx);
            if (xObj.getPropertyValue(Constantes.REF_OPERATOR).equalsIgnoreCase(refOperator)) {
                if (xObj.getPropertyValue(Constantes.REF_CHANNEL).equalsIgnoreCase(refChannel)) {
                    idLinea = xObj.getPropertyValue(Constantes.ID_CHANNEL_LINE);
                }
            }
        }

        for (int idx = 0; idx < lxLineasGateway.getSize() && idLinea == null; idx++) {
            LXObject xObj = (LXObject) lxLineasGateway.getObject(idx);
            if (xObj.getPropertyValue(Constantes.REF_OPERATOR).equalsIgnoreCase(refOperator)) {
                if (xObj.getPropertyValue(Constantes.REF_CHANNEL).equalsIgnoreCase(refChannel)) {
                    idLinea = xObj.getPropertyValue(Constantes.ID_CHANNEL_LINE);
                }
            }
        }

        if (idLinea == null) {
            logger.error("El operador \" + refOperator + \" o el canal \" + refChannel + \" NO existe");
            throw new Exception(" ");
        }
        return idLinea;
    }

    /**
     * Este método nos permite obtener el id del un formato de mensaje específico (SMS, EMAIL, PNS ...)
     *
     * @param formatoMsj formato del mensaje del cual se quiere obtener su id
     * @return id del formato
     * @throws Exception
     */
    public String obtenerIdFormatoMensajeWS(String formatoMsj) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCHANNEL, Constantes.LATINIA, Constantes.WlURL_MCHANNEL,
                Constantes.WASURL_MCHANNEL, Constantes.WS_LD_MCHANNEL_SERVICE, Constantes.WS_LD_MCHANNEL_LOCATOR,
                Constantes.WS_LD_MCHANNEL_PORTSTUB, Constantes.WS_LD_MCHANNEL_METHOD, Constantes.CONT_WGESTCHANNEL);
        Ws_ld_mchannelPortStub portStub = (Ws_ld_mchannelPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idFormato = null;
        String formatos = portStub.listFormats(validation.toString());
        LXList lxFormatos = (LXList) LXSerializer.readLX(formatos);


        for (int idx = 0; idx < lxFormatos.getSize(); idx++) {
            LXObject xObj = (LXObject) lxFormatos.getObject(idx);
            if (xObj.getPropertyValue(Constantes.DESCRIPTION).equalsIgnoreCase(formatoMsj)) {
                idFormato = xObj.getPropertyValue(Constantes.ID_FORMAT);
                break;
            }
        }
        if (idFormato != null) {
            return idFormato;
        } else {
            logger.error("El formato de mensaje " + formatoMsj + " NO existe");
            throw new Exception(" ");
        }
    }

    /**
     * Permite establecer la ruta de salida a partir del llamado WS
     *
     * @param refOperador
     * @param canal
     * @param formato
     * @throws Exception
     */
    @Step("Establecer Ruta de Salida refOperador<> canal<> formato<>")
    public void establecerRutaSalida(String refOperador, String canal, String formato) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCHANNEL, Constantes.LATINIA, Constantes.WlURL_MCHANNEL,
                Constantes.WASURL_MCHANNEL, Constantes.WS_LD_MCHANNEL_SERVICE, Constantes.WS_LD_MCHANNEL_LOCATOR,
                Constantes.WS_LD_MCHANNEL_PORTSTUB, Constantes.WS_LD_MCHANNEL_METHOD, Constantes.CONT_WGESTCHANNEL);
        Ws_ld_mchannelPortStub portStub = (Ws_ld_mchannelPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idLine = "";
        idLine = obtenerIdLineaWS(refOperador, canal);
        String idFormato = obtenerIdFormatoMensajeWS(formato);

        portStub.updateGwRoute(idLine, idFormato, "Virtual");
    }

    public void crearColectorWS(String refColector) throws RemoteException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCHANNEL, Constantes.LATINIA, Constantes.WlURL_MCHANNEL,
                Constantes.WASURL_MCHANNEL, Constantes.WS_LD_MCHANNEL_SERVICE, Constantes.WS_LD_MCHANNEL_LOCATOR,
                Constantes.WS_LD_MCHANNEL_PORTSTUB, Constantes.WS_LD_MCHANNEL_METHOD, Constantes.CONT_WGESTCHANNEL);
        Ws_ld_mchannelPortStub portStub = (Ws_ld_mchannelPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        portStub.createCollector(refColector, "Colector " + refColector);
    }

    //FailOver

}
