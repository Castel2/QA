package Testing;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

import javax.naming.Context;
import javax.xml.rpc.ServiceException;

import com.latinia.util.lxobjects.LXList;
import com.latinia.util.lxobjects.LXObject;
import com.sahipro.lang.java.client.ElementStub;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.latinia.limsp.j2ee.adaptors.ws.Ws_adaptor;
import com.latinia.limsp.j2ee.adaptors.ws.Ws_adaptorLocator;
import com.latinia.limsp.j2ee.adaptors.ws.Ws_adaptorPortStub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LatiniaScenarioUtil {
    private Random rand;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private static Logger logger = LogManager.getLogger(LatiniaScenarioUtil.class);

    public LatiniaScenarioUtil() {

        rand = new Random(System.currentTimeMillis());
    }


    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public int randInt(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        //Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    /*************************************************************************/
    /************	Métodos para Envio de mensajes, insercion en WS	**********/
    /*************************************************************************/


    /**
     * Carga en un String el contenido del fichero "fnsme" desde la ubicación "recursos/" del proyecto.
     *
     * @param fname Nombre del fichero a cargar. Puede contener un path relativo al directorio de recursos.
     * @return String con el contenido del fichero.
     */
    public static String readResource(String fname) {
        FileInputStream in = null;
        try {
            in = new FileInputStream("recursos/" + fname);
            String ret = inputStreamToString(in);
            in.close();
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileInputStream readPropertiesLogIN() {
        FileInputStream in;
        try {
            in = new FileInputStream("../Util/Propiedades/logIN.properties");
            return in;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Devuelve el contenido de un InputStream en un String
     *
     * @param in InputStream
     * @return String con el contenido
     */
    public static String inputStreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader is = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(is);
            String read = br.readLine();
            while (read != null) {
                sb.append(read);
                read = br.readLine();
            }
            br.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Carga en un String el contenido del archivo dataAE
     *
     * @return String con el contenido del fichero
     */
    public static FileInputStream readPropertiesDataAE() {
        FileInputStream in;
        try {
            in = new FileInputStream(Constantes.PATH_DATA_AE);
            return in;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Remplaza en un texto pasado como parametro "data", un conjunto de datos en forma de propiedades nombre=valor.
     * Los datos que aparezcan en el XML con la expresion "#nombre#", seran sustituidos por el valor de la
     * propiedad correspondiente a "nombre".
     *
     * @param data  String del cual se quieren remplazar parametros.
     * @param props Propiedades nombre=valor para remplazar los parametros del string
     * @return String con los datos ya reemplazados.
     */
    public static String replaceDataParams(String data, Properties props) {
        Set conjuntodeClaves = props.keySet();
        for (Object prop : conjuntodeClaves) { //Itera por todas las claves que encuentra, y les establece el valor de la propiedad
            String propName = (String) prop;
            String propVal = props.getProperty(propName);
            data = data.replaceAll("#" + propName + "#", propVal);
        }
        return data;
    }


    /**
     * @param refProduct
     * @param pswProduct
     * @param xml
     * @throws MalformedURLException
     * @throws RemoteException
     * @throws ServiceException
     */
    public static void sendMessageWS(String refProduct, String pswProduct, String xml, String adaptorws) throws MalformedURLException, RemoteException, ServiceException {
        DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
        String host = datosGlobales.get(Constantes.HOST).toString();
        String port = datosGlobales.get(Constantes.PORT).toString();
        String wlUrl = "";
        String wasUrl = "";
        String url;
        // Se instancian las clases del stub
        if (adaptorws.equalsIgnoreCase("normalAuth")) {
            wlUrl = Constantes.WL_NORMAL_AUTH;
            wasUrl = Constantes.WAS_NORMAL_AUTH;
            logger.info("adaptador WS " + adaptorws);


        } else if (adaptorws.equalsIgnoreCase("priorityAuth")) {
            wlUrl = Constantes.WL_PRIORITY_AUTH;
            wasUrl = Constantes.WAS_PRIORITY_AUTH;
            logger.info("adaptador WS " + adaptorws);


        } else if (adaptorws.equalsIgnoreCase("massiveAuth")) {
            wlUrl = Constantes.WL_MASSIVE_AUTH;
            wasUrl = Constantes.WAS_MASSIVE_AUTH;
            logger.info("adaptador WS " + adaptorws);


        } else {
            logger.warn("No se ha especificado el adaptador WS válido");

        }

        if (port.equals(Constantes.PORT7001) || port.equals(Constantes.PORT7002)) {
            url = "http://" + host + ":" + port + wlUrl;
        } else {
            url = "http://" + host + ":" + port + wasUrl;
        }

        Ws_adaptor adaptor = new Ws_adaptorLocator();
        Ws_adaptorPortStub portStub = (Ws_adaptorPortStub) adaptor.getws_adaptorPort(new URL(url));

        // Se establecen las credenciales refProduct y psw de la aplicación
        portStub.setUsername(refProduct);
        portStub.setPassword(pswProduct);

        // Se envía el mensaje invocando al método remoto “putMessage”
        portStub.putMessage(xml);

    }


    /*********************************************************/
    /************	Métodos para invocacion a url's	**********/
    /*********************************************************/

    /**
     * Utilizando las librerias APACHE se conecta a una URL.
     *
     * @param url url destino a la que solicitara GET
     * @return OK o KO
     * @throws Exception
     */
    public static String conectaURL(String url) throws Exception {
        String resultado = "OK";
        HttpClient client = new HttpClient();
        client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("", ""));

        GetMethod get = null;
        try {
            get = new GetMethod(url);
            get.setDoAuthentication(false);
            logger.info("Invocando: " + url);
            int status = client.executeMethod(get);
            if (status != HttpStatus.SC_OK) {
                logger.error("Status =  '" + status + "'. url = '" + url + "'");
                resultado = "KO";
            }
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
        return resultado;
    }


    /**
     * Utilizando las librerias APACHE se conecta a una URL. Permite utilizacion de user y pass
     *
     * @param url  url destino a la que solicitara GET
     * @param user usuario de autentificacion
     * @param pass
     * @return true en caso que el GET resulte correcto
     * @throws Exception
     */
    public static Boolean conectaURL(String url, String user, String pass) throws Exception {
        HttpClient client = new HttpClient();
        client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));

        GetMethod get = null;
        try {
            get = new GetMethod(url);
            get.setDoAuthentication(true);
            logger.info("Invocando: " + url);
            int status = client.executeMethod(get);
            if (status != HttpStatus.SC_OK) {
                logger.error("Error de autentificacion. Status =  '" + status + "'. url = '" + url + "'");
                throw new Exception("");
            }
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
        return true;
    }


    /**
     * Utilizando las librerias APACHE se conecta a una URL en la que se encuentra el componente 'wtest' el cual permite realizar inserciones con objeto de Testing
     * Informacion adicional sobre wtest puede encontrarse en el Email 	De: Jordi Perez
     * Enviado el: lunes, 30 de marzo de 2015 10:06
     * Para: Xavier Ruiz
     * CC: Ricard Rovira
     * Asunto: SDP-1010 Desarrollo de aplicación wtest para Testing y Diagnosis de Plataforma
     *
     * @param refproduct Referencia del producte a utilitzar en l’enviament del missatge push.
     * @param xml        Contingut del missatge PUSH a enviar.
     * @return OK o KO
     * @throws Exception
     * @author xruizs
     */
    public static String conectaURLpost(String refproduct, String xml, String userName, String password) throws MalformedURLException, RemoteException, ServiceException {
        DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
        String host = datosGlobales.get(Constantes.HOST).toString();
        String port = datosGlobales.get(Constantes.PORT).toString();

        String url = "http://" + host + ":" + port + "/wtest/sendMsg";    //El acceso GUI es: http://host:port/wtest/index.jsp
        String resultado = "";
        String authCookie = null;

        //Se debe realizar la autentificacion durante el propio objeto 'client'. Para ello en la llamada POST iran incluidas las credenciales LIMSP
        if (userName != null && password != null) {
            authCookie = authURLpost(url, userName, password);
        }

        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Automated");

        BufferedReader br = null;

        //Configuro la llamada POST con sus correspondientes parametros
        PostMethod post = new PostMethod(url); // Debe ser del tipo 'http://host:port/wtest/sendMsg'
        post.addParameter("refProduct", refproduct);
        post.addParameter("xml", xml);
        post.addParameter("sync", "on"); //Especifica que esperi a la resposta del missatge desde el colector virtual. Si no es “on” no espera cap missatge.
        // post.addParameter("wait", "Long"); //Si s’especifica, retorna la resposta codificada en html. Com que retorna un xml amb la resposta, substitueix els < i els > per &lt; i &gt;
        // post.addParameter("html", "true"); “Long”, per defecte 10000. Temps que ha d’esperar per rebre la transacció PUSH desde el colector virtual en milisegons.
        if (authCookie != null) {
            post.setRequestHeader("Cookie", authCookie);
        }

        try {
            logger.info("Invocando POST: " + url + " refproduct=" + refproduct);
            int returnCode = client.executeMethod(post);

            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                resultado = "KO";
                logger.error("El metodo Post no esta implemantado en esta URL");
                // Aunque haya fallado, igualmente recogemos el 'response body'
                post.getResponseBodyAsString();
            } else {
                br = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream())); //Metemos la respuesta del POST en 'br'

                //**Ahora le hacemos varias conversiones a 'br' hasta que acabemos por tener un 'String'**//
                StringBuilder stBuilder = null; //Aqui meteremos lo que ahora tenemos en 'br', pero primero Calculamos el tamaño de lo que se ha metido en 'br' (esto no es obligatorio, pero es mas optimo hacerlo)
                long contentLength = post.getResponseContentLength();
                if (contentLength > 0) {
                    stBuilder = new StringBuilder((int) contentLength);
                } else {
                    stBuilder = new StringBuilder(10000);
                }

                //Se va leyendo cada linea de 'br' y metiendola en 'stBuilder'
                String readLine;
                while (((readLine = br.readLine()) != null)) {
                    stBuilder.append(readLine);
                }
                resultado = stBuilder.toString(); //Metemos ya todo el resultado en un String
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            post.releaseConnection();
            if (br != null) try {
                br.close();
            } catch (Exception fe) {
            }
        }

        return resultado; //Ya tenemos todo el POST metido en un String. Lo devolvemos como respuesta de la funcion

    }


    /**
     * Se conecta a una url para autenticarse y devuelve el valor de la cookie de seguridad.
     */
    public static String authURLpost(String url, String userName, String password) throws MalformedURLException, RemoteException, ServiceException {
        String cookieValue = null;
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Automated");

        //Configuro la llamada POST con sus correspondientes parametros
        PostMethod post = new PostMethod(url + "/j_security_check");
        post.addParameter("j_username", userName);
        post.addParameter("j_password", password);

        try {
            logger.info("Invocando POST de autentificacion: " + url + "/j_security_check");
            int returnCode = client.executeMethod(post);
            //consume the response body
            post.getResponseBodyAsString();

            if (returnCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                cookieValue = post.getResponseHeader("Set-Cookie").getValue();
            } else {
                logger.error("Invalid response code: " + returnCode);
                logger.error("Expected: " + HttpStatus.SC_MOVED_TEMPORARILY);

            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            post.releaseConnection();
        }
        // Obtenemos todo el texto hasta el primer ;
        if (cookieValue != null) {
            cookieValue = cookieValue.substring(0, cookieValue.indexOf(';') + 1);
        }
        return cookieValue;

    }

    /**
     * Consulta en Weblogic el valor de una propiedad del LConfig
     *
     * @param ctxt
     * @param param
     * @return valor de la propiedad leida
     */
    public static String lookupCtxt(Context ctxt, String param) {
        String ret = null;
        try {
            ret = (String) ctxt.lookup(param);
        } catch (Exception ex) {
        }
        logger.info("-config: " + param + "=" + ret);
        return ret;
    }

    /**
     * This method get the value of the property that is passed by parameter, this property is in the file any file with extension .properties of LCONFIG
     * For example, a property that is passed by parameter could be "license/entry/enabledProductProperties"
     *
     * @param archivoPropiedades is the file where is got the "propiedad", for example "license.properties"
     * @param propiedad          is the property to lookup, for example "license/entry/enabledProductProperties"
     * @return
     */
    public String leerPropiedadesLConfig(String archivoPropiedades, String propiedad) {
        String host = datosGlobales.get(Constantes.HOST).toString();
        String port = datosGlobales.get(Constantes.PORT).toString();
        //Reading of properties from file license.properties
        PropertiesLimsp propertiesLimsp = new PropertiesLimsp();
        propertiesLimsp.init(host.toString(), Integer.parseInt(port), "limsp3.deployer", "latinia");
        String ficheropropiedades = archivoPropiedades;
        //propertiesLimsp.readPropertyFiles("lconf-general.properties", "lmod-core.properties", "lmod-core.properties", "lmod-core.properties");
        propertiesLimsp.readPropertyFiles(ficheropropiedades);
        //propertiesLimsp.dumpProperties(); //Printa por pantalla lo que ha leido
        String val = propertiesLimsp.getProp(propiedad, null);
        logger.info("Esto es el valor de " + propiedad + ": " + val);

        return val;
    }

    /**
     * Este método obtiene la lista de objetos a partir de una etiqueta y la conviete en una lista String
     *
     * @param listalx
     * @param etiqueta
     * @return
     */
    public List<String> convertirXMLDatatoString(LXList listalx, String etiqueta) {
        List<String> listaValores = new ArrayList<>();
        for (int i = 0; i < listalx.getSize(); i++) {
            LXObject obj = (LXObject) listalx.getObject(i);
            listaValores.add(obj.getPropertyValue(etiqueta));
        }
        return listaValores;
    }

    /**
     * Este método nos devuelve la cantida de elementos a partir de una entrada en la licencia específica, por ejemplo, la maxima cantidad de usuarios, la maxima cantidad de usuarios
     *
     * @param valor Hace referencia a la entrada que específica de la licencia
     * @return
     */
    public int cantidadElementos(String valor) {
        String cant = leerPropiedadesLConfig("license.properties", valor);
        return Integer.parseInt(cant);
    }


    /**
     *
     * @param element elemento a buscar
     * @param timeout en segundos
     * @param interval cada cuanto se validará la carga del elemento
     * @throws Exception
     */
    public void waitElement(ElementStub element, int timeout, int interval) throws Exception {
        int cont = 0;

        while (!element.exists() && cont < timeout){
            logger.info("Cargando página... " + cont);
            Thread.sleep(interval * 1000);
            cont ++;
        }

        if (cont == timeout){
            logger.error("Error al cargar la página, no se encuentra el elemento");
            throw new Exception("Timeout");
        } else {
            logger.info("Existe el elemento");
        }
    }

}
