package Testing;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.thoughtworks.gauge.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class AlmacenDeDatos {


    private static Logger logger = LogManager.getLogger(AlmacenDeDatos.class);
    private VerificacionLIMSP verificacionLIMSP = new VerificacionLIMSP();
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

    private static final String LANG_es_ES = "es_ES";
    private static final String LANG_es_CA = "ca_ES";
    private static final String LANG_en_US = "en_US";
    private static final String LANG_en_CA = "en_CA";
    private static final String LANG_en_GB = "en_GB";

    public void host(String host) throws Exception {
        datosGlobales.put(Constantes.HOST, host);
    }

    public void port(String port) throws Exception {
        datosGlobales.put(Constantes.PORT, port);
    }

    @Step("Herramienta <herramienta>")
    public void herramienta(String herramienta) throws Exception {

        Map mapHerramienta = (Map) datosGlobales.get(Constantes.HASHMAP);

        if (mapHerramienta.containsKey(herramienta)) {
            herramienta = mapHerramienta.get(herramienta).toString();
        }
        datosGlobales.put(Constantes.HERRAMIENTA, herramienta);
    }

    @Step("Menu <menu>")
    public void menu(String menu) throws Exception {

        datosGlobales.put(Constantes.MENU, menu);
    }

    @Step("Texto <texto>")
    public void texto(String texto) throws Exception {
        datosGlobales.put(Constantes.TEXTO, texto);
    }

    @Step("Archivo <>")
    public void archivo(String archivo) throws Exception {
        datosGlobales.put(Constantes.ARCHIVO, archivo);
    }

    @Step("Titulo <Envio Email>")
    public void titulo(String titulo) throws Exception {
        datosGlobales.put(Constantes.TITULO, titulo);
    }

    @Step("Parametro Variable <>")
    public void parametro(String parametro) throws Exception {
        datosGlobales.put(Constantes.PARAMETRO, parametro);
    }

    @Step("Valor Parametro <>")
    public void valor(String valor) throws Exception {
        datosGlobales.put(Constantes.VALOR, valor);
    }
    @Step("Nombre contrato <>")
    public void nombreContrato(String nomcontrato) throws Exception {
        Map mapHerramienta = (Map) datosGlobales.get(Constantes.HASHMAP);

        if (mapHerramienta.containsKey(nomcontrato)) {
            nomcontrato = mapHerramienta.get(nomcontrato).toString();
        }
        datosGlobales.put(Constantes.NOM_CONTRATO, nomcontrato);
    }

    @Step("Buscar por <>")
    public void busquedaPor(String criterio) throws Exception {
        String code = verificacionLIMSP.obtenerCodigoPais();
        String number = verificacionLIMSP.obtenerNumeroVirtual();

        if (criterio.equalsIgnoreCase(Constantes.VIRTUAL) || criterio.equalsIgnoreCase(Constantes.UNKNOWN)) {
            if (datosGlobales.get(Constantes.GSM) != null) {
                criterio = datosGlobales.get(Constantes.GSM).toString();
            } else {
                if (code != null && number != null) {
                    criterio = code + number;
                }
            }
        }
        datosGlobales.put(Constantes.CRITERIO, criterio);
    }

    @Step("Usuario <>")
    public void usuario(String user) throws Exception {

        Properties propiedades;
        propiedades = new Properties();
        FileInputStream in;
        if (user.equals("")) {
            logger.info("Tomando del archivo de propiedades USER " + user);
            try {
                in = LatiniaScenarioUtil.readPropertiesLogIN();
                propiedades.load(in);
                user = propiedades.getProperty(Constantes.USER);
                datosGlobales.put(Constantes.USER, user);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            logger.info("Tomando USER desde parámetros " + user);
            datosGlobales.put(Constantes.USER, user);
        }
    }

    @Step("Empresa <INNOVUS>")
    public void empresa(String empresa) throws Exception {
        Properties propiedades;
        propiedades = new Properties();
        FileInputStream in;
        if (empresa.equals("")) {
            logger.info("Tomando del archivo de propiedades EMPRESA " + empresa);
            try {
                in = LatiniaScenarioUtil.readPropertiesLogIN();
                propiedades.load(in);
                empresa = propiedades.getProperty(Constantes.LOGIN);
                datosGlobales.put(Constantes.EMPRESA, empresa);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            logger.info("Tomando EMPRESA desde parámetros " + empresa);
            datosGlobales.put(Constantes.EMPRESA, empresa);
        }

    }

    @Step("Organizacion <>")
    public void organizacion(String organizacion){
        datosGlobales.put(Constantes.ORGANIZACION, organizacion);
    }

    @Step("refcontract <>")
    public void refcontract(String refcontract) throws Exception {
        datosGlobales.put(Constantes.REF_CONTRACT, refcontract);
    }

    @Step("refproduct <>")
    public void refproduct(String refproduct) throws Exception {
        datosGlobales.put(Constantes.REF_PRODUCT, refproduct);
    }

    @Step("refUser <>")
    public void refUser(String refUser) throws Exception {
        datosGlobales.put(Constantes.REF_USER, refUser);
    }

    @Step("tipoclause <sms>")
    public void tipoclause(String tipoclause) throws Exception {
        datosGlobales.put(Constantes.TIPO_CLAUSE, tipoclause);
    }

    @Step("tipoMensaje <>")
    public void tipoMensaje(String tipoMensaje) throws Exception {
        datosGlobales.put(Constantes.TIPO_MENSAJE, tipoMensaje);
    }

    @Step("tipoContenido <>")
    public void tipoContenido(String tipoContenido) throws Exception {
        datosGlobales.put(Constantes.TIPO_CONTENIDO, tipoContenido);
    }

    @Step("Grupo <>")
    public void grupo(String grupo) throws Exception {
        datosGlobales.put(Constantes.GRUPO, grupo);
    }

    @Step("refPlantilla <>")
    public void refPlantilla(String refPlantilla) throws Exception {
        datosGlobales.put(Constantes.REF_TEMPLATE, refPlantilla);
    }

    @Step("nomPlantilla <>")
    public void nomPlantilla(String nomPlantilla) throws Exception {
        datosGlobales.put(Constantes.NOM_PLANTILLA, nomPlantilla);
    }

    @Step("nomSeccion <>")
    public void nomSeccion(String nomSeccion) throws Exception {
        datosGlobales.put(Constantes.NOMBRE_SECCION, nomSeccion);
    }


    @Step("idioma <-->")
    public void idioma(String idioma) throws Exception {

        String idiomatexto;

        //Asigno el string del idioma correspondiente, que luego lo utilizo para seleccionar el menú apropiado en el GUI
        if (LANG_es_ES.equals(idioma)) {
            idiomatexto = "Español";
        } else if (LANG_es_CA.equals(idioma)) {
            idiomatexto = "Català";
        } else if (LANG_en_US.equals(idioma)) {
            idiomatexto = "English (United States)";
        } else if (LANG_en_GB.equals(idioma)) {
            idiomatexto = "'English (UK)";
        } else if (LANG_en_CA.equals(idioma)) {
            idiomatexto = "'English (Canada)";
        } else {
            idiomatexto = "Todos";
        }

        datosGlobales.put(Constantes.IDIOMA, idioma);
        datosGlobales.put(Constantes.IDIOMA_TEXTO, idiomatexto);

    }

    @Step("Proposito <provision>")
    public void proposito(String proposito) throws Exception {
        datosGlobales.put("proposito", proposito);
    }

    @Step("Mapp <>")
    public void mapp(String mapp) throws Exception {
        //Lo utilizo en el escenario "desRegistro de Token" para referenciar la mapp que buscaré
        // También lo utilizo probando "limspinf-ltest"
        mapp = mapp.toLowerCase();
        datosGlobales.put(Constantes.MAPP, mapp);
    }


    @Step("Ruta Origen <> <>")
    public void rutaOrigen(String unidad, String directorio) {
        datosGlobales.put(Constantes.UNIDAD, unidad);
        datosGlobales.put(Constantes.DIRECTORIO, directorio);
    }

    @Step("Credenciales Linux <oracletest> <> <> <>")
    public void passLinux(String hostsftp, int portsftp, String userlinux, String passlinux) {
        datosGlobales.put(Constantes.HOST_SFTP, hostsftp);
        datosGlobales.put(Constantes.PORT_SFTP, portsftp);
        datosGlobales.put(Constantes.USER_LINUX, userlinux);
        datosGlobales.put(Constantes.PASSWORD_LINUX, passlinux);
    }

    @Step("Ruta destino </home/weblogic/filereade>")
    public void rutaDestino(String rutades) {
        datosGlobales.put(Constantes.RUTA_DESTINO, rutades);
    }


    @Step("Cabecera <table>")
    public void cabecera(Table tableCab) {
        datosGlobales.put(Constantes.CABECERA, tableCab);
    }

    @Step("Cuerpo <table>")
    public void cuerpo(Table tableBody) {
        datosGlobales.put(Constantes.CUERPO, tableBody);
    }

    @Step("Nueva tabla <table>")
    public void nuevaTabla(Table table) {
        datosGlobales.put(Constantes.TABLA, table);
    }

    @Step("Establecer cliente")
    public void nombreCliente() {
        FileInputStream in;
        String customer;
        Properties propiedades = new Properties();
        try {
            in = LatiniaScenarioUtil.readPropertiesLogIN();
            propiedades.load(in);
            customer = propiedades.getProperty(Constantes.CUSTOMER);
            logger.info("El cliente a testear es: " + customer);
            datosGlobales.put(Constantes.CUSTOMER, customer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step("valor propiedad <> <>")
    public void valorPropiedad(String prop, String val) {

        datosGlobales.put(prop, val);
    }

    @Step("Guardar llave-valor <> <>")
    public void guardarLlaveValor(String key, String val) {

        datosGlobales.put(key, val);
    }

    @Step("Guardar objeto <>")
    public void guardarObjeto(String objeto){
        datosGlobales.put(Constantes.NOM_OBJETO,objeto);
    }
    @Step("Guardar propiedad <>")
    public  void guardarPropiedad(String propiedad){
        datosGlobales.put(Constantes.PROP_OBJETO,propiedad);
    }

    @Step("Obtener ruta QA TOOLS")
    public void obtenerRutaQATOOLS () throws Exception {
        Properties propiedades = new Properties();
        String rutaQAtools = "";

        FileInputStream in = null;
        try {
            in = LatiniaScenarioUtil.readPropertiesDataAE();
            propiedades.load(in);

            rutaQAtools = propiedades.getProperty("qatools_path");

            if (rutaQAtools == null || rutaQAtools.equals("")) {
                logger.info("No se especifica la entrada 'qatools_path' en el archivo 'dataAE', por lo tanto se procede a tomar el valor por defecto " + Constantes.DEFAULT_QA_TOOLS);
                rutaQAtools = Constantes.DEFAULT_QA_TOOLS;
            }
            datosGlobales.put(Constantes.PATH_QA_TOOLS, rutaQAtools);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}