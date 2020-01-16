package LData_Testing;

import Testing.Constantes;
import com.latinia.limsp.ldata.mproductfacade.ws.Ws_ld_mproductPortStub;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import com.latinia.util.lxobjects.LXList;
import com.latinia.util.lxobjects.LXObject;
import com.latinia.util.lxobjects.LXSerializer;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by amartinez on 6/03/2018.
 */
public class MProduct {
    private static Logger logger = LogManager.getLogger(MProduct.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    AccesoWSLData accesoWSLData;

    List<Object> retorna;

    public MProduct() {
        this.accesoWSLData = new AccesoWSLData();
    }

    /**
     * This method assign a one role to the one application by means of WS
     *
     * @param refProduct It is the application to which the role will be added
     * @param role       Is the role to assign
     * @throws Exception
     */
    @Step("Asignar Rol WS <> <>")
    public void asignarRol(String refProduct, String role) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTPRODUCT, Constantes.LATINIA, Constantes.WlURL_MPRODUCT,
                Constantes.WASURL_MPRODUCT, Constantes.WS_LD_MPRODUCT_SERVICE, Constantes.WS_LD_MPRODUCT_LOCATOR,
                Constantes.WS_LD_MPRODUCT_PORTSTUB, Constantes.WS_LD_MPRODUCT_METHOD, Constantes.CONT_WGESTPRODUCT);
        Ws_ld_mproductPortStub portStub = (Ws_ld_mproductPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idProduct = obtenerIdProduct(refProduct);

        portStub.bindRole(validation.toString(), idProduct, role);
    }

    /**
     * Este método permite obtener el idProduct a partir del refProduct
     *
     * @param refproduct Aplicación a la cual se le quiere obtener el id
     * @return
     * @throws Exception
     */
    public String obtenerIdProduct(String refproduct) throws Exception {
        String idProduct = "";
        LXList lxProducts = obtenerListaProductos();

        for (int idx = 0; idx < lxProducts.getSize(); idx++) {
            LXObject xObj = (LXObject) lxProducts.getObject(idx);
            if (xObj.getPropertyValue(Constantes.REF_PRODUCT).equalsIgnoreCase(refproduct)) {
                idProduct = xObj.getPropertyValue(Constantes.ID_PRODUCT);
            }
        }
        return idProduct;
    }

    /**
     * Este método permite obtener el idProduct a partir del refProduct
     *
     * @param refproduct Aplicación a la cual se le quiere obtener el id
     * @return
     * @throws Exception
     */
    public String obtenerNomProduct(String refproduct) throws Exception {
        String nomProducto = "";
        LXList lxProducts = obtenerListaProductos();

        for (int idx = 0; idx < lxProducts.getSize(); idx++) {
            LXObject xObj = (LXObject) lxProducts.getObject(idx);
            if (xObj.getPropertyValue(Constantes.REF_PRODUCT).equalsIgnoreCase(refproduct)) {
                nomProducto = xObj.getPropertyValue("name");
            }
        }
        return nomProducto;
    }

    /**
     * Se obtiene la lista de aplicaciones a través del llamado WS
     *
     * @return
     * @throws Exception
     */
    public LXList obtenerListaProductos() throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTPRODUCT, Constantes.LATINIA, Constantes.WlURL_MPRODUCT,
                Constantes.WASURL_MPRODUCT, Constantes.WS_LD_MPRODUCT_SERVICE, Constantes.WS_LD_MPRODUCT_LOCATOR,
                Constantes.WS_LD_MPRODUCT_PORTSTUB, Constantes.WS_LD_MPRODUCT_METHOD, Constantes.CONT_WGESTPRODUCT);
        Ws_ld_mproductPortStub portStub = (Ws_ld_mproductPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String listaProductos;
        listaProductos = portStub.listProducts(validation.toString());
        LXList lxProductos = (LXList) LXSerializer.readLX(listaProductos);

        return lxProductos;
    }

    /**
     * Este método permite actualizar la propiedad de una aplicación especifica
     *
     * @param refProduct    aplicación a la cual se quiere actualizar la propiedad
     * @param refPlataforma LIMSP - LGUI
     * @param propiedad     Propiedad a actualizar
     * @param valor         valor de la propiedad
     * @param visible       determina si será visible en contratos o no
     * @throws Exception
     */
    @Step("Actualizar propiedad aplicación WS refProduct<> refPlataforma<> propiedad<> valor<> visible<>")
    public void actualizarPropiedadAplicacionWS(String refProduct, String refPlataforma, String propiedad, String valor, boolean visible) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTPRODUCT, Constantes.LATINIA, Constantes.WlURL_MPRODUCT,
                Constantes.WASURL_MPRODUCT, Constantes.WS_LD_MPRODUCT_SERVICE, Constantes.WS_LD_MPRODUCT_LOCATOR,
                Constantes.WS_LD_MPRODUCT_PORTSTUB, Constantes.WS_LD_MPRODUCT_METHOD, Constantes.CONT_WGESTPRODUCT);
        Ws_ld_mproductPortStub portStub = (Ws_ld_mproductPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        //EL booleano exisetPropiedad hace referencia a la existencia de la propiedad en la licencia
        boolean existePropiedad = false;
        boolean prop_licenciada;
        //El booleano existeEnProducto hace referencia a la existencia de la propiedad dentro de la lista de propiedades en la aplicación
        //Y existeEnPlataforma la existencia de la propiedad sin estar activa en el producto/aplicación
        boolean existeEnProducto = false, existeEnPlataforma = false;
        String[] listaPropiedadesLicencia = (String[]) datosGlobales.get(Constantes.LIST_PROPERTIES);
        LXList listaPropiedadesProducto = (LXList) LXSerializer.readLX(listaPropiedadesProducto(refProduct, refPlataforma));
        LXList listaPropiedadesPlataforma = (LXList) LXSerializer.readLX(listaPropiedadesPlataforma(refPlataforma));

        String idProduct = obtenerIdProduct(refProduct);
        //Esto porque hay licencias de desarrollo que no tiene la entrada de propiedades en la licencia, sin embargo, existen las propiedades
        if (listaPropiedadesLicencia[0].equalsIgnoreCase("TEST")) {
            existePropiedad = true;
        } else {
            for (int i = 0; i < listaPropiedadesLicencia.length && !existePropiedad; i++) {
                if (listaPropiedadesLicencia[i].equalsIgnoreCase(propiedad)) {
                    existePropiedad = true;
                }
            }
        }
        //Se verifica la existencia de la propiedad en la licencia
        if (existePropiedad) {
            prop_licenciada = true;
            for (int idx = 0; idx < listaPropiedadesProducto.getSize(); idx++) {
                LXObject xObj = (LXObject) listaPropiedadesProducto.getObject(idx);
                if (xObj.getPropertyValue(Constantes.PROP).equalsIgnoreCase(propiedad)) {
                    logger.info("Existe propiedad " + propiedad + " en la aplicación " + refProduct);
                    existeEnProducto = true;
                }
            }
            // Se verifica la existencia de la propiedad en la aplicación/producto
            if (existeEnProducto) {
                //Actualizo el valor de la propiedad
                portStub.updateProductPlatformPropertyExport(validation.toString(), refPlataforma, idProduct, propiedad, valor, visible);
            } else {
                logger.info("NO existe propiedad " + propiedad + " en la aplicación " + refProduct + ", buscaré en la plataforma para activarla en la aplicación");
                for (int idx = 0; idx < listaPropiedadesPlataforma.getSize(); idx++) {
                    LXObject xObj = (LXObject) listaPropiedadesPlataforma.getObject(idx);
                    if (xObj.getPropertyValue(Constantes.PROP).equalsIgnoreCase(propiedad)) {
                        logger.info("Existe propiedad " + propiedad + " en la plataforma " + refPlataforma);
                        existeEnPlataforma = true;
                    }
                }
                //Se verifica la existencia de la propiedad por plataforma LGUI-LIMSP, si existe se activa en la aplicación/producto
                if (existeEnPlataforma) {
                    logger.info("Existe propiedad en la plataforma, se activa ");
                    portStub.createProductPlatformProperty(validation.toString(), idProduct, refPlataforma, propiedad);
                    portStub.updateProductPlatformPropertyExport(validation.toString(), refPlataforma, idProduct, propiedad, valor, visible);
                } else {
                    logger.error("No existe la propiedad " + propiedad + " en la plataforma a pesar de que existe en la licencia");
                    throw new Exception(" ");
                }
            }
        } else {
            logger.info("La propiedad " + propiedad + " No está licenciada");
            prop_licenciada = false;
        }
        datosGlobales.put(Constantes.PROP_LICENCIADA, prop_licenciada);
    }

    /**
     * Verifica en la lista de aplicaciones si existe una aplicación específica
     *
     * @param refProduct producto a buscar
     * @return
     * @throws Exception
     */
    public boolean existeAplicacion(String refProduct) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTPRODUCT, Constantes.LATINIA, Constantes.WlURL_MPRODUCT,
                Constantes.WASURL_MPRODUCT, Constantes.WS_LD_MPRODUCT_SERVICE, Constantes.WS_LD_MPRODUCT_LOCATOR,
                Constantes.WS_LD_MPRODUCT_PORTSTUB, Constantes.WS_LD_MPRODUCT_METHOD, Constantes.CONT_WGESTPRODUCT);
        Ws_ld_mproductPortStub portStub = (Ws_ld_mproductPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        boolean existe = false;

        LXList lxProductos = obtenerListaProductos();

        for (int idx = 0; idx < lxProductos.getSize(); idx++) {
            LXObject xObj = (LXObject) lxProductos.getObject(idx);
            if (xObj.getPropertyValue(Constantes.REF_PRODUCT).equalsIgnoreCase(refProduct)) {
                existe = true;
            }
        }

        return existe;
    }

    /**
     * Permite crear una nueva aplicación
     *
     * @param refProduct referencia de la nueva aplicación
     * @throws Exception
     */
    @Step("Crear aplicación WS refProduct<> refPlataforma<> ")
    public void crearAplicacionWS(String refProduct, String refPlataforma) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTPRODUCT, Constantes.LATINIA, Constantes.WlURL_MPRODUCT,
                Constantes.WASURL_MPRODUCT, Constantes.WS_LD_MPRODUCT_SERVICE, Constantes.WS_LD_MPRODUCT_LOCATOR,
                Constantes.WS_LD_MPRODUCT_PORTSTUB, Constantes.WS_LD_MPRODUCT_METHOD, Constantes.CONT_WGESTPRODUCT);
        Ws_ld_mproductPortStub portStub = (Ws_ld_mproductPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);
        //Se verifica la existencia del producto, si no existe se crea, de lo contrario se informa que ya está creada
        if (!existeAplicacion(refProduct)) {
            String idProduct = portStub.createProduct(validation.toString(), refProduct, refProduct);
            portStub.bindPlatform(validation.toString(), refPlataforma, idProduct);
            portStub.updateProductPassword(validation.toString(), idProduct, Constantes.LATINIA);
            logger.info("Se crea la aplicación " + refProduct + " ---> " + idProduct);
            datosGlobales.put(Constantes.ID_PRODUCT, idProduct);
        } else {
            logger.info("Ya existe la aplicación " + refProduct);
        }
    }

    /**
     * Devuelve la lista de propiedades activas de un producto/aplicación especifica
     *
     * @param refProduct  Aplicación a consultar propiedades
     * @param refPlatform LIMSP- LGUI
     * @return
     * @throws Exception
     */
    public String listaPropiedadesProducto(String refProduct, String refPlatform) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTPRODUCT, Constantes.LATINIA, Constantes.WlURL_MPRODUCT,
                Constantes.WASURL_MPRODUCT, Constantes.WS_LD_MPRODUCT_SERVICE, Constantes.WS_LD_MPRODUCT_LOCATOR,
                Constantes.WS_LD_MPRODUCT_PORTSTUB, Constantes.WS_LD_MPRODUCT_METHOD, Constantes.CONT_WGESTPRODUCT);
        Ws_ld_mproductPortStub portStub = (Ws_ld_mproductPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idProduct = obtenerIdProduct(refProduct);

        return portStub.listProductPlatformProperties(validation.toString(), refPlatform, idProduct);
    }

    /**
     * Devuelve la lista de propiedades por plataforma (LIMSP-LGUI)
     *
     * @param refPlatform LIMSP - LGUI
     * @return
     * @throws Exception
     */
    public String listaPropiedadesPlataforma(String refPlatform) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTPRODUCT, Constantes.LATINIA, Constantes.WlURL_MPRODUCT,
                Constantes.WASURL_MPRODUCT, Constantes.WS_LD_MPRODUCT_SERVICE, Constantes.WS_LD_MPRODUCT_LOCATOR,
                Constantes.WS_LD_MPRODUCT_PORTSTUB, Constantes.WS_LD_MPRODUCT_METHOD, Constantes.CONT_WGESTPRODUCT);
        Ws_ld_mproductPortStub portStub = (Ws_ld_mproductPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        return portStub.listPlatformProperties(validation.toString(), refPlatform);
    }
}
