package Testing;

import LData_Testing.AccesoWSLData;
import LData_Testing.MContract;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import com.latinia.util.lxobjects.LXList;
import com.latinia.util.lxobjects.LXSerializer;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

import com.latinia.limsp.ldata.mproductfacade.ws.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Created by amartinez on 6/07/2017.
 */
public class VerificacionLIMSP {

    LatiniaScenarioUtil latiniaScenarioUtil;
    AccesoWSLData accesoWSLData;
    MContract mContract;

    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private static Logger logger = LogManager.getLogger(VerificacionLIMSP.class);

    public VerificacionLIMSP() {
        latiniaScenarioUtil = new LatiniaScenarioUtil();
        accesoWSLData = new AccesoWSLData();
        mContract = new MContract();
    }

    /**
     * Este método obtiene la lista de propiedades de la aplicación, es decir, propiedades liceciables
     */
    @Step("Lista Propiedades desde licencia")
    public void propiedadesAplicacion() throws Exception {
        String[] listaPropiedades;
        String customer = datosGlobales.get(Constantes.CUSTOMER).toString();
        String valPropiedades = latiniaScenarioUtil.leerPropiedadesLConfig("license.properties", "license/entry/enabledProductProperties");
        if (valPropiedades != null) {
            listaPropiedades = valPropiedades.split("\\|");
            datosGlobales.put(Constantes.LIST_PROPERTIES, listaPropiedades);
        } else if (customer.equalsIgnoreCase(Constantes.TEST)) {

            listaPropiedades = new String[1];
            listaPropiedades[0] = customer;
            datosGlobales.put(Constantes.LIST_PROPERTIES, listaPropiedades);
        } else {
            logger.error("La entrada \"license/entry/enabledProductProperties\" en la licencia no existe o está comentada");
            throw new Exception("");
        }
    }

    /**
     * Este método NUEVO obtiene la cantidad de objetos permitidos para crear
     */
    @Step("Licencia max objetos propobject <probobject>")
    public void propiedadObjetos(String propiedad) throws Exception {
        String valPropiedad = latiniaScenarioUtil.leerPropiedadesLConfig("license.properties",propiedad);
        if (valPropiedad != null){
            datosGlobales.put(Constantes.MAX_OBJECTS,valPropiedad);
        }else{
            logger.error("La entrada \"license/entry/maxInfObjTypes\" en la licencia no existe o esta comentada");
            throw new Exception("");
        }

    }

    @Step("Verificar properties emojis <fichero> <propiedad>")
    public void propertiesEmojis(String fichero, String propiedad) throws Exception {
        String valPropiedad = latiniaScenarioUtil.leerPropiedadesLConfig(fichero,propiedad);
        if (valPropiedad == null){
            logger.info("La propiedad " + propiedad +" en el fichero " + fichero +" es null, permite guardar todos los parametros de plantillas");
        }else{
            if(valPropiedad.contains("emoji_automated1")){
                logger.info("La propiedad " + propiedad +" en el fichero " + fichero +" permite guardar el parametro de plantilla 'emoji_automated1'");
            }else{
                logger.error("La propiedad "+ propiedad + " en el fichero "+ fichero +" no permite guardar el parametro de plantilla 'emoji_automated1'");
                throw new Exception("");
            }

        }
    }

    public void listaPrefijosMT() {
        String[] listaPropiedades;
        String valPropiedades = latiniaScenarioUtil.leerPropiedadesLConfig("license.properties", "license/entry/limitGsmInternationalMt");
        listaPropiedades = valPropiedades.split("\\|");
        for (String val : listaPropiedades) {
            System.out.println(val);
        }
    }

    //-------------------Validación de la existencia de contratos, aplicaiones, canales, ... ------------------------------//

    /**
     * Este método nos permite leer desde un XML y retornar una lista de nodos a partir de un tag específico
     *
     * @param file
     * @param tag
     */
    public List<NodeList> leerArchivoXML(String file, String tag) {
        try {

            File fXmlFile = new File(file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            int lenght = doc.getElementsByTagName(tag).getLength();
            List<NodeList> lista = new ArrayList<>();
            for (int i = 0; i < lenght; i++) {

                lista.add(doc.getElementsByTagName(tag).item(i).getChildNodes());
            }
            return lista;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Este método nos permite obtener la lista de contratos oficiales, específicados en el archivo "provision-naming.xml"
     *
     * @return
     */
    public List<String> obtenerContratosOficiales() {
        String customer = datosGlobales.get(Constantes.CUSTOMER).toString();
        List<NodeList> nList = leerArchivoXML(Constantes.PROVISION_NAMING, Constantes.CONTRACTS);
        List<String> listaValores = new ArrayList<>();
        for (int i = 0; i < nList.size(); i++) {
            if (nList.get(i).item(i).getParentNode().getParentNode().getAttributes().getNamedItem(Constantes.CUSTOMER).getTextContent()
                    .equalsIgnoreCase(customer)) {
                for (int j = 0; j < nList.get(i).getLength(); j++) {
                    Node node = nList.get(i).item(j).getTextContent().trim().length() > 0 ? nList.get(i).item(j) : null;
                    if (node != null) {
                        if (node.getNodeName().equals("official-contract")) {

                            listaValores.add(node.getTextContent().trim());
                        }
                    }
                }
            }
        }
        datosGlobales.put(Constantes.LIST_VALUES, listaValores);

        return listaValores;
    }

    /**
     * A partir de la lista de contratos oficiales y la lista de los contratos de la plataforma del cliente, Se verifica la existencia de los contratos oficiales en la plataforma del cliente
     * Se notifica por consola la existencia o la no existencia de algún contrato oficial
     */
    @Step("Validar Contratos <>")
    public void validarContratos(String empresa) {
        List<String> listaNueva = new ArrayList<>();
        String host = datosGlobales.get(Constantes.HOST).toString();
        //Lista de los cotratos oficiales
        List<String> contrato1 = obtenerContratosOficiales();
        List<String> contrato2 = null;
        try {
            //Lista de los contratos de la plataforma del cliente
//            contrato2 = listaContratosWS(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA);
            contrato2 = mContract.listaContratosWS(empresa);
            boolean flag = true;
            for (int i = 0; i < contrato1.size(); i++) {
                for (int j = 0; j < contrato2.size(); j++) {
                    if (contrato1.get(i).equals(contrato2.get(j))) {
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }
                }
                if (!flag) {
                    listaNueva.add(contrato1.get(i));

                }
            }
            if (listaNueva.isEmpty()) {
                logger.info("*****************************************************************************");
                logger.info("IMPORTANTE: TODOS LOS CONTRATOS OFICIALES SE ENCUENTRAN EN LA PLATAFORMA!!!!");
                logger.info("*****************************************************************************");
            } else {
                logger.info("*****************************************************************************");
                for (String newValue : listaNueva) {
                    logger.info("CUIDADO: NO SE ENCUENTRA EL CONTRATO ===> " + newValue);
                }
                logger.info("Se ha detectado en " + host + " uno o varios contratos oficiales que no corresponden con los contratos de la plataforma o no existen,\n" +
                        "Por favor dirigete al archivo \"provision-naming.xml\" y realiza los cambios correspondientes.");
                logger.info("*****************************************************************************");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método obtiene el código del país desde el archivo "provision-naming" correspondiente al país.
     *
     * @return
     * @throws Exception
     */
    public String obtenerCodigoPais() throws Exception {
        String codigo = null;
        String pais = obtenerPais();
        if (pais != null) {
            List<NodeList> nList1 = leerArchivoXML(Constantes.PROVISION_NAMING, "country_code");
            for (int i = 0; i < nList1.size(); i++) {
                for (int j = 0; j < nList1.get(i).getLength(); j++) {
                    if (nList1.get(i).item(j).getNodeName().equals(pais)) {
                        codigo = nList1.get(i).item(j).getTextContent();
                    }
                }
            }
        } else {
            logger.error("No existe Pais, verifica el archivo \"provision-naming\"");
            throw new Exception("");
        }
        return codigo;
    }

    /**
     * Este método obtiene el número virtual desde el archivo "provision-naming" correspondiente al país.
     *
     * @return
     * @throws Exception
     */
    public String obtenerNumeroVirtual() throws Exception {
        String numero = null;
        String pais = obtenerPais();
        if (pais != null) {
            List<NodeList> nList1 = leerArchivoXML(Constantes.PROVISION_NAMING, "vcollector_phones");

            for (int i = 0; i < nList1.size(); i++) {
                for (int j = 0; j < nList1.get(i).getLength(); j++) {
                    if (nList1.get(i).item(j).getNodeName().equals(pais)) {
                        if(numero == null){
                            numero = nList1.get(i).item(j).getTextContent();
                            break;
                        }
                    }
                }
            }
        } else {
            logger.error("No existe Pais, verifica el archivo \"provision-naming\"");
            throw new Exception("");
        }
        return numero;
    }

     //Se obtiene lista de numeros del fichero provision-naming, se obtiene codigo de pais
    // almacena estos valores en valores CONSTANTES
    public static void ReadProvision(String customer) throws ParserConfigurationException, IOException, SAXException {
        DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
        String country=null;
        File provisionNaming = new File("../Util/Propiedades/provision-naming.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.parse(provisionNaming);
        document.getDocumentElement().normalize();
        NodeList customers = document.getElementsByTagName("provision-naming");
        for (int i=0; i<customers.getLength();i ++){
            Node nodo = customers.item(i);
            Element elementcustomer = (Element) nodo;
            if(elementcustomer.getAttribute("customer").equals(customer)){
                datosGlobales.put(Constantes.COUNTRY, elementcustomer.getAttribute("country"));
                country = elementcustomer.getAttribute("country");
                break;
            }
        }
        NodeList codes = document.getElementsByTagName("country_code");
        Node nodocountry = codes.item(0);
        Element elementcode = (Element) nodocountry;
        if(elementcode.getNodeName().equals("country_code")){
            String codeconuntry = elementcode.getElementsByTagName(country).item(0).getTextContent();
            datosGlobales.put(Constantes.COUNTRY_CODE, codeconuntry);
        }

        LinkedList listcollector = new LinkedList();
        NodeList vcollector = document.getElementsByTagName("vcollector_phones");
        Node nodocollector = vcollector.item(0);
        Element elementcollector = (Element) nodocollector;
        if(elementcollector.getNodeName().equals("vcollector_phones")){
            NodeList countvcoller = elementcollector.getElementsByTagName(country);
            Node nodocountcollectro = countvcoller.item(0);
            datosGlobales.put(Constantes.NUM_VCOLLECTOR, nodocountcollectro.getTextContent());
            for(int i =0;i<countvcoller.getLength();i++){
                nodocountcollectro = countvcoller.item(i);
                listcollector.add(nodocountcollectro.getTextContent());
            }
            datosGlobales.put(Constantes.LISTCOLLECTOR,listcollector);
        }


    }

    /**
     * Este método obtiene el país desde el archivo "provision-naming"
     *
     * @return
     */
    public String obtenerPais() {
        String customer = datosGlobales.get(Constantes.CUSTOMER).toString();
        String pais = null;
        List<NodeList> nList = leerArchivoXML(Constantes.PROVISION_NAMING, Constantes.CONTRACTS);
        for (int i = 0; i < nList.size(); i++) {
            if (nList.get(i).item(i).getParentNode().getParentNode().getAttributes().getNamedItem(Constantes.CUSTOMER).getTextContent()
                    .equalsIgnoreCase(customer)) {
                pais = nList.get(i).item(i).getParentNode().getParentNode().getAttributes().getNamedItem("country").getTextContent();
            }
        }
        return pais;
    }

    /**
     * Este método obtiene la lista de aplicaciones de la plataforma del cliente a partir del llamado a mproduct
     *
     * @param refProduct
     * @param pswProduct
     * @throws Exception
     */
    public void listaAplicacionesWS(String refProduct, String pswProduct) throws Exception {
        String loginEnterprise = datosGlobales.get(LatiniaUtil.LOGIN_EMPRESA).toString();
        String loginUser = datosGlobales.get(LatiniaUtil.LOGIN_USER).toString();
        List<Object> retorna;
        retorna = accesoWSLData.wsLDataGeneric(refProduct, pswProduct, Constantes.WlURL_MPRODUCT,
                Constantes.WASURL_MPRODUCT, Constantes.WS_LD_MPRODUCT_SERVICE, Constantes.WS_LD_MPRODUCT_LOCATOR,
                Constantes.WS_LD_MPRODUCT_PORTSTUB, Constantes.WS_LD_MPRODUCT_METHOD, Constantes.CONT_WGESTPRODUCT);
        Ws_ld_mproductPortStub portStub = (Ws_ld_mproductPortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);
        //Is list the contracts of an enterprise
        String listProduct = portStub.listProducts(validation.toString());
        LXList xProductos = (LXList) LXSerializer.readLX(listProduct);
        List<String> valuesPlatform = latiniaScenarioUtil.convertirXMLDatatoString(xProductos, "name");
        datosGlobales.put(Constantes.VALUES_PLATFORM, valuesPlatform);
    }


    /**
     * This method get the list of values of the  a node  XML
     *
     * @param etiqueta The tag match with node
     * @return
     */
    public List<String> obtenerListaXML(String etiqueta) {
        String customer = datosGlobales.get(Constantes.CUSTOMER).toString();
        List<NodeList> nList = leerArchivoXML(Constantes.PROVISION_NAMING, etiqueta);
        List<String> listaValores = new ArrayList<>();
        for (int i = 0; i < nList.size(); i++) {
            if (nList.get(i).item(i).getParentNode().getParentNode().getAttributes().getNamedItem(Constantes.CUSTOMER).getTextContent()
                    .equalsIgnoreCase(customer)) {
                for (int j = 0; j < nList.get(i).getLength(); j++) {
                    //for any reason that I don't know, exist empty nodes, then I do the next validation, if next to execute the method trim() to delete the spaces between letters, its length is greater than zero, entonces I get the node, otherwise the result is null
                    Node node = nList.get(i).item(j).getTextContent().trim().length() > 0 ? nList.get(i).item(j) : null;
                    //for previous line, in this validation is discarded the null values and only is saved the valid values
                    if (node != null) {
                        listaValores.add(node.getTextContent().trim());
                    }
                }
            }
        }
        return listaValores;
    }


    /**
     * Este método guarda en un mapa los contratos de los clientes que coinciden con los contratos oficiales
     */
    @Step("Hash Map")
    public void hashmap() {
        List<String> listacontratos = obtenerListaXML(Constantes.CONTRACTS);
        Map map = new HashMap();
        String clave;
        String valor;
        //Este ciclo es diferente, incrementa de 2 en 2 porque cada uno de los dos valores de la lista se ditribuye de la siguiente manera, uno es la clave del mapa y el otro el valor del mapa
        for (int i = 0; i < listacontratos.size(); i += 2) {
            clave = listacontratos.get(i);
            valor = listacontratos.get(i + 1);
            map.put(clave, valor);
        }
        datosGlobales.put(Constantes.HASHMAP, map);

    }
}
