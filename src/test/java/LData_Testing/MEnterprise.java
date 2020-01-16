package LData_Testing;

import Testing.Constantes;
import Testing.LatiniaScenarioUtil;
import com.latinia.limsp.ldata.menterprisefacade.ws.Ws_ld_menterprisePortStub;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import com.latinia.util.lxobjects.LXList;
import com.latinia.util.lxobjects.LXObject;
import com.latinia.util.lxobjects.LXSerializer;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * Created by amartinez on 6/03/2018.
 */
public class MEnterprise {
    private static Logger logger = LogManager.getLogger(MEnterprise.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    AccesoWSLData accesoWSLData;
    LatiniaScenarioUtil latiniaScenarioUtil;
    public MEnterprise() {
        accesoWSLData = new AccesoWSLData();
        latiniaScenarioUtil = new LatiniaScenarioUtil();
    }

    /**
     * Se obtiene la lista de las empresas a través del llamado WS
     *
     * @return
     * @throws Exception
     */
    public LXList obtenerListaEmpresas() throws Exception {
        List<Object> retorna;
        String listaEmpresas;
        //Se obtiene el acceso(portStub) al ws y al string de validacion
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTENTERPRISE,Constantes.LATINIA, Constantes.WlURL_MENTERPRISE,
                Constantes.WASURL_MENTERPRISE, Constantes.WS_LD_MENTERPRISE_SERVICE, Constantes.WS_LD_MENTERPRISE_LOCATOR,
                Constantes.WS_LD_MENTERPRISE_PORTSTUB, Constantes.WS_LD_MENTERPRISE_METHOD, Constantes.CONT_WGESTENTERPRISE);
        //En la primera posicion (0) de "retorna" se obtiene el portstub
        Ws_ld_menterprisePortStub portStub = (Ws_ld_menterprisePortStub) retorna.get(0);
        //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
        LXValidationLData validation = (LXValidationLData) retorna.get(1);
        //Se obtiene la lista de empresas
        listaEmpresas = portStub.listEnterprises(validation.toString());

        LXList xEmpresas = (LXList) LXSerializer.readLX(listaEmpresas);
        return xEmpresas;
    }

    /**
     * Este método obtiene el IdEterprise apartir del nombre de la empresa
     *
     * @param empresa
     * @return
     * @throws Exception
     */
    @Step("obtener ID empresa <>")
    public String obtenerIdEmpresa(String empresa) throws Exception {
        String idEmpresa = "";
        LXList xEmpresas = obtenerListaEmpresas();
        Iterator<LXObject> iEnterprises = xEmpresas.getIterator();
        while (iEnterprises.hasNext()) {
            LXObject enterprise = iEnterprises.next();
            if (enterprise.getPropertyValue("login").equalsIgnoreCase(empresa)) {
                idEmpresa = enterprise.getPropertyValue("idEnterprise");
            }
        }
        return idEmpresa;
    }

    /**
     * Este método verifica la existencia de una empresa a partir del llamado WS
     *
     * @param enterprise
     * @return
     * @throws Exception
     */
    @Step("Existe Empresa <>")
    public boolean existeEmpresa(String enterprise) throws Exception {
        boolean existe = false;
        //Se obtiene la lista de empresas
        LXList xEmpresas = obtenerListaEmpresas();
        Iterator<LXObject> iEmpresas = xEmpresas.getIterator();
        while (iEmpresas.hasNext() && existe == false) {
            LXObject empresa = iEmpresas.next();
            if (empresa.getPropertyValue("login").equalsIgnoreCase(enterprise)) {
                existe = true;
            } else {
                existe = false;
            }
        }

        return existe;
    }

    /**
     * Permite crear una empresa a partir de llamado WS
     * @throws Exception
     */
    @Step("Crear empresa WS")
    public void crearEmpresaWS() throws Exception {
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String idEmpresa;
        if (!existeEmpresa(empresa)) {
            if (!poderCrearEmpresas()) {
                logger.info("La empresa " + empresa + " NO existe y NO se puede crear porque el máximo de empresas permitido por licencia llegó al limite");
            }else{
                empresa = empresa.toUpperCase();
                List<Object> retorna;
                retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTENTERPRISE,Constantes.LATINIA, Constantes.WlURL_MENTERPRISE,
                        Constantes.WASURL_MENTERPRISE, Constantes.WS_LD_MENTERPRISE_SERVICE, Constantes.WS_LD_MENTERPRISE_LOCATOR,
                        Constantes.WS_LD_MENTERPRISE_PORTSTUB, Constantes.WS_LD_MENTERPRISE_METHOD, Constantes.CONT_WGESTENTERPRISE);
                //En la primera posicion (0) de "retorna" se obtiene el portstub
                Ws_ld_menterprisePortStub portStub = (Ws_ld_menterprisePortStub) retorna.get(0);
                //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
                LXValidationLData validation = (LXValidationLData) retorna.get(1);

                idEmpresa = portStub.createEnterprise(validation.toString(),empresa,empresa,empresa);
                if (Integer.parseInt(idEmpresa) > 0){
                    logger.info("Se ha creado la empresa " + empresa);
                }else{
                    logger.error("No se ha podido crear la empresa " + empresa);
                    throw new Exception(" ");
                }
            }
        }else{
            logger.info("La empresa " + empresa + " ya existe");
        }

    }

    @Step("Eliminar empresa <empresa>")
    public void removerEmpresaWs(String empresa) throws Exception {
        empresa =  empresa.toUpperCase();
        String idEmpresa =  obtenerIdEmpresa(empresa);
        boolean eliminado;
        boolean existeEmpresa =  existeEmpresa(empresa);

        if (existeEmpresa(empresa)) {
            List<Object> retorna;
            retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTENTERPRISE, Constantes.LATINIA, Constantes.WlURL_MENTERPRISE,
                    Constantes.WASURL_MENTERPRISE, Constantes.WS_LD_MENTERPRISE_SERVICE, Constantes.WS_LD_MENTERPRISE_LOCATOR,
                    Constantes.WS_LD_MENTERPRISE_PORTSTUB, Constantes.WS_LD_MENTERPRISE_METHOD, Constantes.CONT_WGESTENTERPRISE);
            //En la primera posicion (0) de "retorna" se obtiene el portstub
            Ws_ld_menterprisePortStub portStub = (Ws_ld_menterprisePortStub) retorna.get(0);
            //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
            LXValidationLData validation = (LXValidationLData) retorna.get(1);
            eliminado = portStub.removeEnterprise(validation.toString(), idEmpresa);

            if (eliminado) {
                logger.info("Se ha eliminado la empresa " + empresa);
            } else {
                logger.error("No se ha podido eliminar la empresa " + empresa);
                throw new Exception(" ");
            }
        }else{
            logger.info("La empresa " + empresa + " No existe");
        }

    }

    /**
     * Este método obtiene la cantidad de empresas existentes en la plataforma a partir de llamado WS
     *
     * @return
     * @throws Exception
     */
    @Step("Cantidad Empresas")
    public int cantidadEmpresas() throws Exception {
        int cant;
        LXList xEmpresas = obtenerListaEmpresas();
        cant = xEmpresas.getSize();
        return cant;
    }

    /**
     * Este método verifica la cantidad maxima de empresas que se pueden crear según la licencia y la cantidad actual de empresas existentes en la plataforma
     *
     * @return Si la cantidad max es mayor a la cantidad actual retorna true, de lo contrario false
     * @throws Exception
     */
    public boolean poderCrearEmpresas() throws Exception {
        int cantLicencia, cantPlataforma;
        boolean sePuedeCrear;
        cantLicencia = latiniaScenarioUtil.cantidadElementos("license/entry/maxEnterprises");
        cantPlataforma = cantidadEmpresas();

        if (cantPlataforma < cantLicencia) {
            sePuedeCrear = true;
        } else {
            sePuedeCrear = false;
        }
        return sePuedeCrear;
    }

    @Step("Validar si se puede crear la empresa <CAPITAL>")
    public void validacionCreacionEmpresa(String enterprise) throws Exception {
        if (!existeEmpresa(enterprise)) {
            if (!poderCrearEmpresas()) {
                logger.info("******************************************************************************************************************************************");
                logger.info("La empresa " + enterprise + " NO existe y NO se puede crear porque el máximo de empresas permitido por licencia llegó al limite \n" +
                        "LO SENTIMOS, si no existe la empresa " + enterprise + " No puedes proceder con la ejecución de los demás test cases");
                logger.info("******************************************************************************************************************************************");
            }
        }else{
            logger.info("Existe la empresa "
                    + enterprise);
        }
    }

}
