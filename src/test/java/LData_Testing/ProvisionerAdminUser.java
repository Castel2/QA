package LData_Testing;

import Testing.Constantes;
import Testing.LatiniaUtil;
import com.latinia.limspinf.stubs.admin.Exception_Exception;
import com.latinia.limspinf.stubs.admin.ProvisionerAdmin;
import com.latinia.limspinf.stubs.data.*;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

/**
 * Created by amartinez on 8/03/2018.
 */
public class ProvisionerAdminUser {

    private static Logger logger = LogManager.getLogger(ProvisionerAdminUser.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    AccesoWSInf accesoWSInf;
    MEnterprise mEnterprise;

    public ProvisionerAdminUser() {
        accesoWSInf = new AccesoWSInf();
        mEnterprise = new MEnterprise();
    }

    /**
     * Permite verificar si la empresa tiene organizaciones asociadas
     *
     * @return
     * @throws Exception
     */
    public boolean existCompaniesInEnterprise() throws Exception {
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);
        String enterprise = datosGlobales.get(LatiniaUtil.LOGIN_EMPRESA).toString();
        int idEnterprise = Integer.parseInt(mEnterprise.obtenerIdEmpresa(enterprise));
        List<Company> listCompanies = provisionerAdmin.getCompaniesListByEnterprise("", idEnterprise);
        if (listCompanies.isEmpty()) {
            logger.info("La empresa no tiene asociada ninguna organización");
            return false;
        } else {
            int count = listCompanies.size();
            logger.info("La empresa si tiene asociada " + count + " organizaciones");
            return true;
        }
    }

    /**
     * Este método permite crear una nueva organización y asignarla a una empresa
     *
     * @throws Exception
     */
    @Step("Crear ORG")
    public void crearOrgWS() throws Exception {
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);

        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String organizacion = datosGlobales.get(Constantes.ORGANIZACION).toString();

        if (mEnterprise.existeEmpresa(empresa)) {
            if (!existeOrgWS(organizacion)) {
                String refCompany = organizacion.toUpperCase();
                int idEnterprise = Integer.parseInt(mEnterprise.obtenerIdEmpresa(empresa));
                provisionerAdmin.createCompany("", refCompany, refCompany);
                provisionerAdmin.setCompanyIdEnterprise("", refCompany, idEnterprise);
                logger.info("Se crea la organización " + organizacion);
            } else {
                logger.info("La organización " + organizacion + " ya existe");
            }
        } else {
            logger.error("No existe la empresa " + empresa);
            throw new Exception();
        }
    }


    /**
     * Permite eliminar una Organización
     *
     * @param organizacion
     * @throws Exception
     */
    @Step("Elimina ORG <>")
    public void eliminarOrgWS(String organizacion) throws Exception {
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);
        String refCompany = organizacion.toUpperCase();
        boolean eliminar;
        eliminar = provisionerAdmin.deleteCompany("", refCompany);
        if (eliminar == true) {
            logger.info("Se ha eliminado la ORG " + organizacion);
        } else {
            logger.info("No se ha podido eliminar la ORG " + organizacion + " compruebe si no existe restricción");
        }

    }

    /**
     * Peromite obtener la lista de organizaciones
     *
     * @return
     * @throws Exception
     */
    public List<Company> listarOrgWS() throws Exception {
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);
        return provisionerAdmin.getCompaniesList("");
    }

    /**
     * Permite verificar la existencia de una Organización
     *
     * @param organizacion
     * @return
     * @throws Exception
     */
    public boolean existeOrgWS(String organizacion) throws Exception {
        String refCompany = organizacion.toUpperCase();

        List<Company> organizaciones = listarOrgWS();
        for (Company org : organizaciones) {
            if (org.getRefCompany().equals(refCompany)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Permite obtener el id de la organización
     *
     * @param refCompany
     * @return
     * @throws Exception
     */
    public int obtenerIdCompany(String refCompany) throws Exception {
        List<Company> organizaciones = listarOrgWS();
        int idCompany = -1;

        for (Company c : organizaciones
                ) {
            if (c.getRefCompany().equalsIgnoreCase(refCompany)) {
                idCompany = c.getId();
            }
        }
        return idCompany;
    }

    /**
     * Permite dar de alta a una nueva MApp en una organizacion especifica
     *
     * @param refApp
     * @throws Exception_Exception
     */
    @Step("Dar de alta a MApp refApp<>")
    public void crearMAppOrganizacion(String refApp) throws Exception_Exception {
        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);

        if (!existeAppEnOrganizacion(refApp, refCompany)) {

            provisionerAdmin.createCompanyApp("", refCompany, refApp, refApp);
            logger.info("Se crea la MApp "
                    + refApp + " para la organización " + refCompany);
        } else {
            logger.info(" La aplicación " + refApp + " ya existe para la organización "
                    + refCompany);
        }
    }

    /**
     * Permite verificar la existencia de una mapp en una organización
     *
     * @param refApp
     * @param refCompany
     * @return
     * @throws Exception_Exception
     */
    public boolean existeAppEnOrganizacion(String refApp, String refCompany) throws Exception_Exception {
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);

        boolean existe = false;

        List<Application> listaApps = provisionerAdmin.listCompanyApps("", refCompany);

        for (Application app : listaApps
                ) {
            if (app.getRefApp().equalsIgnoreCase(refApp)) {
                existe = true;
            }
        }

        return existe;
    }

    /**
     * Permite verificar  si una mapp esta activa en  una organización
     *
     * @param refApp
     * @param refCompany
     * @return boolean
     * @throws Exception_Exception
     * @Autor JMH
     */

    public boolean existeAppActiva(String refApp, String refCompany) throws Exception_Exception {
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);

        boolean existe = false;

        List<Application> listaApps = provisionerAdmin.listCompanyActiveApps("", refCompany);

        for (Application app : listaApps
                ) {
            if (app.getRefApp().equalsIgnoreCase(refApp)) {
                existe = true;
            }
        }

        return existe;
    }

    /**
     * Permite crear propiedades (gestion de propiedades de usuario)
     *
     * @throws Exception
     */
    @Step("Definir propiedades de Usuario")
    public void definirPropiedadesUsuario() throws Exception {
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);

        String organizacion = datosGlobales.get(Constantes.ORGANIZACION).toString();
        Table tabProps = (Table) datosGlobales.get(Constantes.TABLA);
        List<TableRow> rows = tabProps.getTableRows();
        List<String> columnNames = tabProps.getColumnNames();

        String nombrePropiedad;
        int tipoPropiedad = -1;
        String propiedadImportante;
        String propiedadCompartida;
        //String propiedadEncriptada;

        for (TableRow row : rows) {
            nombrePropiedad = row.getCell(columnNames.get(0));
            propiedadImportante = row.getCell(columnNames.get(2));
            //propiedadEncriptada = row.getCell(columnNames.get(3));
            propiedadCompartida = row.getCell(columnNames.get(4));

            if (!existePropiedad(row.getCell(columnNames.get(0)), organizacion)) {


                if (row.getCell(columnNames.get(1)).equalsIgnoreCase("simple")) {
                    tipoPropiedad = 0;
                } else if (row.getCell(columnNames.get(1)).equalsIgnoreCase("clave")) {
                    tipoPropiedad = 1;
                } else if (row.getCell(columnNames.get(1)).equalsIgnoreCase("grupo")) {
                    tipoPropiedad = 2;
                }
                PropertyType tipo = provisionerAdmin.defineUserProperty("", organizacion, nombrePropiedad, tipoPropiedad);

                logger.info("Se crea propiedad " + nombrePropiedad);
                int idProp = tipo.getIdProp();
                int idString = tipo.getIdString();
                int idCompany = obtenerIdCompany(organizacion);
                if (propiedadImportante.equalsIgnoreCase("t")) {
                    provisionerAdmin.markPreferedUserPropertyById("", idCompany, idProp, true);
                }

                if (propiedadCompartida.equalsIgnoreCase("t")) {
                    provisionerAdmin.setUserPropertySharedById("", idCompany, idString, true);
                }
            } else {
                logger.info("La propiedad " + nombrePropiedad + " ya existe");
            }
        }
    }

    /**
     * Permite verificar la existencia de propiedades de usuario en una organización
     *
     * @param nombrePropiedad
     * @param organizacion
     * @return
     * @throws Exception_Exception
     */
    public boolean existePropiedad(String nombrePropiedad, String organizacion) throws Exception_Exception {
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);

        boolean existe = false;
        List<PropertyType> listaPropiedades = provisionerAdmin.listDefinedUsersProperties("", organizacion);

        for (PropertyType pt : listaPropiedades
                ) {
            if (pt.getName().equals(nombrePropiedad)) {
                existe = true;
                break;
            }
        }

        return existe;
    }


    /**
     * Permite verificar la existencia de una mapp Deshabilitada
     *
     * @Autor JMH
     */
    public boolean existeAppEnInactivas(String refApp, String refCompany) throws Exception_Exception {
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);

        boolean existe = false;

        List<Application> listaApps = provisionerAdmin.listCompanyDisabledApps("", refCompany);

        for (Application app : listaApps
                ) {
            if (app.getRefApp().equalsIgnoreCase(refApp)) {
                existe = true;
            }
        }

        return existe;
    }

    /**
     * Permite habilitar una mapp de una organizacion
     *
     * @Autor JMH
     */
    @Step("Habilitar una MApp refApp <>")
    public void habilitarAppEnOrganizacion(String refApp) throws Exception_Exception {
        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);


        boolean existe = existeAppActiva(refApp, refCompany);
        if (existe == true) {

            logger.info("La APP se encuentra habilitada");

        } else {

            boolean existeIna = existeAppEnInactivas(refApp, refCompany);
            if (existeIna == true) {

                provisionerAdmin.enableCompanyApplication("", refCompany, refApp);
                logger.info("La aplicacion " + refApp + "Habilitada satisfactoriamente");

            } else {
                logger.info("La aplicación " + refApp + " No existe Se procede a crearla");
                crearMAppOrganizacion(refApp);
                logger.info("La aplicación " + refApp + "ha sido creada");
            }
        }
    }

    /**
     * Permite deshabilitar una mapp de una organizacion
     *
     * @Autor JMH
     */
    @Step("Deshabilitar una MApp refApp <>")
    public void deshabilitarAppEnOrganizacion(String refApp) throws Exception_Exception {
        String refCompany = datosGlobales.get(Constantes.ORGANIZACION).toString();
        ProvisionerAdmin provisionerAdmin;
        provisionerAdmin = (ProvisionerAdmin) accesoWSInf.wsINFGeneric(Constantes.APP_WSUBSCRIBERS,
                Constantes.LATINIA, Constantes.WASURL_PROVISIONER_ADMIN, Constantes.WLURL_PROVISIONER_ADMIN,
                Constantes.WS_INF_PROVISIONER_ADMIN_SERVICE, Constantes.WS_INF_PROVISIONER_ADMIN_METHOD);

        if (existeAppEnOrganizacion(refApp, refCompany)) {

            provisionerAdmin.disableCompanyApplication("", refCompany, refApp);
            logger.info("La aplicacion " + refApp + "Deshabilitada satisfactoriamente");

        } else {
            logger.info("La aplicación " + refApp + " No existe para la organización " + refCompany);
        }
    }

}
