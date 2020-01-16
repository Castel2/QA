package LData_Testing;

import Testing.Constantes;
import Testing.LatiniaScenarioUtil;
import com.google.gson.*;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by amartinez on September 2019
 */
public class AWSTools {
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private static Logger logger = LogManager.getLogger(AWSTools.class);
    private static final String URL_BASE = "https://awstools.corp.latiniaservices.com/api/v1/instance";

    /**
     * Created by amartinez
     * Permite obtener el nombre de la instancia de la AMI AWS desde el archivo 'dataAE',
     * de no encontrarlo, obtiene el host estabecido en el archivo 'logIN'
     * @throws Exception
     */
    @Step("Establecer Instancia")
    public void establecerInstancia() throws Exception {
        Properties propiedades = new Properties();
        String nomInstancia = "";

        FileInputStream in = null;
        try {
            in = LatiniaScenarioUtil.readPropertiesDataAE();
            propiedades.load(in);

            nomInstancia = propiedades.getProperty("instance");

            if (nomInstancia == null || nomInstancia.equals("")) {
                logger.info("No se especifica la entrada 'instance' en el archivo 'dataAE', por lo tanto se procede a tomar el valor " +
                        "del archivo 'logIN'");
                nomInstancia = datosGlobales.get(Constantes.HOST).toString();
            }
            datosGlobales.put(Constantes.NOM_INSTANCIA, nomInstancia);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Created by amartinez
     * Permite obtener las credenciales en base64 del archivo 'dataAE'
     * @throws Exception
     */
    @Step("Establecer datos de acceso a AWS Tools")
    public void establecerDatosAccesoAWS() throws Exception {
        Properties propiedades = new Properties();
        String credenciales = "";

        FileInputStream in = null;
        try {
            in = LatiniaScenarioUtil.readPropertiesDataAE();
            propiedades.load(in);

            credenciales = propiedades.getProperty("credentials");

            if (credenciales.equals("") || credenciales == null) {
                logger.error("No se especifica la entrada 'credentials' en el archivo 'dataAE'");
                throw new Exception("");
            }
            datosGlobales.put(Constantes.CREDENCIALES, credenciales);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Created by amartinez
     * A partir del llamado REST a AWS TOOLS, se obtiene el id de la instancia a partir del nombre de la instancia
     * @param nombre
     * @return
     * @throws IOException
     */
    public String obtenerIdInstancia(String nombre) throws Exception {
        String response = listarInstancias();
        String idInstancia = null;
        try {
            JsonParser parser = new JsonParser();
            JsonArray arrayInstancias = parser.parse(response).getAsJsonArray();

            if (arrayInstancias.size() > 0) {
                String nombreInstancia = null;

                for (JsonElement instancia : arrayInstancias
                ) {
                    JsonObject gsonObj = instancia.getAsJsonObject();
                    nombreInstancia = gsonObj.get("name").getAsString();
                    if (nombreInstancia.equalsIgnoreCase(nombre)) {
                        idInstancia = gsonObj.get("id").getAsString();
                    }
                }
            } else {
                logger.error("No existen Instancias");
                throw new Exception("");
            }
        } catch (JsonParseException e) {
            e.getMessage();
        }
        return idInstancia;
    }

    /**
     * Created by amartinez
     * A partir del llamado REST a AWS TOOLS, se obtiene la lista de todas las instancias que pueden ser
     * gestionadas por el usuario autenticado
     * @return
     * @throws IOException
     */
    public String listarInstancias() throws IOException {
        int codeHTTP;
        String response = "";
        HttpClient httpClient = new HttpClient();
        GetMethod method = new GetMethod(URL_BASE);
        String credenciales = datosGlobales.get(Constantes.CREDENCIALES).toString();

        method.setRequestHeader("Authorization", "Basic" + " " + credenciales);

        codeHTTP = httpClient.executeMethod(method);

        if (codeHTTP == 200) {
            response = new String(IOUtils.toByteArray(method.getResponseBodyAsStream()));
        }
        return response;
    }
}
