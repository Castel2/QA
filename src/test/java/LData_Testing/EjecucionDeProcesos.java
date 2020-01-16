package LData_Testing;

import Testing.Constantes;
import com.google.gson.*;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amartinez on September 2019
 */
public class EjecucionDeProcesos {
    private static Logger logger = LogManager.getLogger(EjecucionDeProcesos.class);
    private static final String URL_BASE = "https://awstools.corp.latiniaservices.com/api/v1/";
    private static final String SEND = "send";
    private static final String SYSTEM_MANAGER_SERVICE = "systemmanager/document/";
    private AWSTools awsTools;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

    public EjecucionDeProcesos() {
        awsTools = new AWSTools();
    }

    /**
     * Created by amartinez
     * Permite ejecutar un documento del servicio systemmanager de AWSTOOLS
     *
     * @param proceso referencia del documento a ejecutar
     * @throws IOException
     */
    @Step("Ejecutar proceso <>")
    public void ejecutarProceso(String proceso) throws Exception {
        String url = URL_BASE + SYSTEM_MANAGER_SERVICE + proceso + "/" + SEND;
        String credenciales = datosGlobales.get(Constantes.CREDENCIALES).toString();
        String nomInstancia = datosGlobales.get(Constantes.NOM_INSTANCIA).toString();
        String idInstancia = awsTools.obtenerIdInstancia(nomInstancia);
        int codeHTTP;
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);

        method.setRequestHeader("Authorization", "Basic" + " " + credenciales);
        JsonObject body = new JsonObject();
        JsonArray instanceIds = new JsonArray();
        //Instancia en la cual se ejecutará el script y/o documento
        instanceIds.add(idInstancia);
        body.add("instanceIds", instanceIds);

        method.setRequestEntity(new StringRequestEntity(body.toString(), "application/json", "UTF-8"));

        codeHTTP = httpClient.executeMethod(method);

        if (codeHTTP == 204) {
            logger.info("Se ha ejecutado con éxito el proceso " + proceso);
        } else {
            logger.error("Existe un problema con la ejecución del proceso " + proceso
                    + ", el código HTTPS es: " + codeHTTP);
            throw new Exception("");
        }
    }

    /**
     * Created by dramirez
     * Revisa fechas actuales en zona horaria ES
     * almacena fechas en constantes
     */
    public void establecerFechas() {
        DateFormat timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        timeStamp.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        Calendar now = Calendar.getInstance();
        Date fecha_ini = now.getTime();
        String fecha_i = timeStamp.format(fecha_ini);
        datosGlobales.put(Constantes.FECHA_INI, fecha_i);
        //se agrega un minuto a la fecha obtenida
        now.add(Calendar.MINUTE, 1);
        Date fecha_fin = now.getTime();
        String fecha_f = timeStamp.format(fecha_fin);
        datosGlobales.put(Constantes.FECHA_FIN, fecha_f);
    }


    /**
     * Created by dramirez
     * Valida el contenido de los logs del lprocess
     *
     * @return Retorna el código HTTP resultado de la ejecución
     * @throws Exception
     */
    @Step("Analizar logs lproces")
    public void analizarLogsLprocess() throws Exception {
        establecerFechas();
        String rutaQAtools = datosGlobales.get(Constantes.PATH_QA_TOOLS).toString();
        String instancia = datosGlobales.get(Constantes.NOM_INSTANCIA).toString();
        File archivo;
        String path = rutaQAtools + Constantes.PATH_SYSTEM_MANAGER + "/resultadoLogs-" + instancia + ".log";
        System.out.println(path + " path");
        archivo = new File(path);
        borrarArchivo(archivo);
        String port = datosGlobales.get(Constantes.PORT).toString();
        String usuario = null;
        logger.info("Puerto:" + port);
        if (port.equals("9080")) {
            usuario = "websphere";
        } else if (port.equals("7001")) {
            usuario = "bea";
        }

        int codeHTTP = ejecutarScriptRemoto("ReadLogHist", usuario, "LAT_SWF_QA_RunSDPLogs");
        logger.info("codigo de ejecución " + codeHTTP);


        if (codeHTTP == 204) {
            int cont = 0;
            //Cada 10 segundos se busca el archivo con el resultado de la ejecución del datagen
            // Hasta llegar a 3 minutos aprox
            while (!archivo.exists() && cont < 18) {
                logger.info("Esperando archivo ...");
                Thread.sleep(10000);
                cont++;
            }
            if (cont == 18) {
                logger.error("No se encuentra el archivo resultadoLogs-" + instancia + ".log");
                throw new Exception("");
            } else {
                String fecha_inicial = datosGlobales.get(Constantes.FECHA_INI).toString();
                String fecha_fin = datosGlobales.get(Constantes.FECHA_FIN).toString();
                boolean ini = false, end = false, transactions = false, messages = false, status = false, summary = false;
                logger.info("Existe el archivo en el repositorio, inicia analisis de logs");
                ini = existelineaEnFicheroLog(archivo, "(" + fecha_inicial + ".*histAndSummary.*INI$|" + fecha_fin + ".*histAndSummary.*INI$)");
                transactions = existelineaEnFicheroLog(archivo, "(" + fecha_inicial + ".*histAndSummary.*histTransactions.*OK$|" + fecha_fin + ".*histAndSummary.*histTransactions.*OK$)");
                messages = existelineaEnFicheroLog(archivo, "(" + fecha_inicial + ".*histAndSummary.*histMessages.*OK$|" + fecha_fin + ".*histAndSummary.*histMessages.*OK$)");
                status = existelineaEnFicheroLog(archivo, "(" + fecha_inicial + ".*histAndSummary.*histStatus.*OK$|" + fecha_fin + ".*histAndSummary.*histStatus.*OK$)");
                summary = existelineaEnFicheroLog(archivo, "(" + fecha_inicial + ".*histAndSummary.*Summary.*OK$|" + fecha_fin + ".*histAndSummary.*Summary.*OK$)");
                end = existelineaEnFicheroLog(archivo, "(" + fecha_inicial + ".*histAndSummary.*END$|" + fecha_fin + ".*histAndSummary.*END$)");
                if (!ini || !end) {
                    System.out.println(ini + " " + end);
                    logger.error("Analisis de logs: Error en el log de inicio o fin del proceso del histAndSummary");
                    throw new Exception("");
                } else {
                    if (!transactions) {
                        logger.error("Analisis de logs: no se encuentra el log de la tarea histTransactions del proceso del histAndSummary");
                        throw new Exception("");
                    }
                    if (!messages) {
                        logger.error("Analisis de logs: no se encuentra  el log de la tarea histMessages del proceso del histAndSummary");
                        throw new Exception("");
                    }
                    if (!status) {
                        logger.error("Analisis de logs: no se encuentra  el log de la tarea histStatus del proceso del histAndSummary");
                        throw new Exception("");
                    }
                    if (!summary) {
                        logger.error("Analisis de logs: no se encuentra el log de la tarea Summary del proceso del histAndSummary");
                        throw new Exception("");
                    }
                    logger.info("Analisis de logs de Lprocess correcto, HistAndSymmary consolido correctamente.");
                    validarFicherolog(archivo);
                    borrarArchivo(archivo);
                }
            }
        } else if (codeHTTP == 500) {
            logger.error("Error en la ejecución del documento");
            throw new Exception("");
        }
    }

    /**
     * Created by jnavas
     *
     * @param archivo          archivo a analizar
     * @param expresionRegular estructura de la linea que se quiere analizar que sea correcta
     * @throws IOException
     */
    public void estructura_mnewtrans(File archivo, String expresionRegular) throws IOException {
        FileReader fr = null;
        BufferedReader br;
        boolean existe = false;
        int contadorlinea = 0;
        int totalLineas = numeroLineasFichero(archivo);
        try {
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            Pattern expresion = Pattern.compile(expresionRegular);
            while ((linea = br.readLine()) != null) {
                contadorlinea++;
                logger.info("Analizado linea " + contadorlinea + ":" + linea);
                Matcher compararlinea = expresion.matcher(linea);
                if ((compararlinea.find())) {
                    existe = true;
                    logger.info("Linea correcta");
                } else {
                    if (existelineaEnFicheroLog(archivo, linea)) {
                        existe = true;
                        logger.info("Linea correcta");
                    } else {
                        existe = false;
                        logger.error("Error al analizar la linea " + linea);
                        throw new Exception("");
                    }
                }
            }
            if ((contadorlinea == totalLineas) && existe) {
                logger.info("Analisis de logs de actividad mnewtrans correcto.");
                borrarArchivo(archivo);
            } else {
                if (contadorlinea != totalLineas) {
                    logger.error("El numero de lineas analizadas no coincide con el total de lineas del log");
                    throw new Exception("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Created by jnavas
     *
     * @param archivo archivo del que se quiere conocer el numero de lineas que posee
     * @return Retorna las lineas totales del archivo
     * @throws IOException
     */
    public int numeroLineasFichero(File archivo) throws IOException {
        FileReader fr = null;
        try {
            fr = new FileReader(archivo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bf = new BufferedReader(fr);
        int numeroLineas = 0;
        String linea;
        while ((linea = bf.readLine()) != null) {
            numeroLineas++;
        }
        return numeroLineas;
    }

    /**
     * Created by ycastelblanco
     *
     * Ejecuta los documentos (sh) para la lectura de los logs de actividad
     * @param proceso
     * @throws Exception
     */
    @Step("Analizar logs actividad <proceso>")
    public void analizarLogsActividad(String proceso) throws Exception {
        int codeHTTP = 0;
        String path = "";
        String usuario = null;
        boolean bandera = false;
        String rutaQAtools = datosGlobales.get(Constantes.PATH_QA_TOOLS).toString();
        String instancia = datosGlobales.get(Constantes.NOM_INSTANCIA).toString();
        String port = datosGlobales.get(Constantes.PORT).toString();
        logger.info("Puerto:" + port);
        if (port.equals("9080")) {
            usuario = "websphere";
        } else if (port.equals("7001")) {
            usuario = "bea";
        }
        if (proceso.equals("prioridad")) {
            codeHTTP = ejecutarScriptRemoto("logger_priority", usuario, "LAT_SWF_QA_RunSDPLogs");
            path = rutaQAtools + Constantes.PATH_SYSTEM_MANAGER + "/log_priority-" + instancia + ".log";
            bandera = true;
        } else if (proceso.equals("mnewtrans")) {
            codeHTTP = ejecutarScriptRemoto("logger_mnewtrans", usuario, "LAT_SWF_QA_RunSDPLogs");
            path = rutaQAtools + Constantes.PATH_SYSTEM_MANAGER + "/log_mnewtrans-" + instancia + ".log";
            bandera = false;
        }
        File archivo = new File(path);
        logger.info("codigo de ejecución " + codeHTTP);
        if (codeHTTP == 204) {
            int cont = 0;
            while (!archivo.exists() && cont < 18) {
                logger.info("Esperando archivo ...");
                Thread.sleep(10000);
                cont++;
            }
            if (cont == 18) {
                if (bandera) {
                    logger.error("No se encuentra el archivo log_priority-" + instancia + ".log");
                    throw new Exception("");
                } else {
                    logger.error("No se encuentra el archivo log_mnewtrans-" + instancia + ".log");
                    throw new Exception("");
                }
            } else {
                if (bandera) {
                    logsPriority(archivo);
                } else {
                    String fecha_inicial = datosGlobales.get(Constantes.FECHA_INI).toString();
                    String fecha_fin = datosGlobales.get(Constantes.FECHA_FIN).toString();
                    String exp_regular = "(" + fecha_inicial + ".*MNEWTRANS.*ServiceCenterOu.*transaction:.*transaction:.*$|" + fecha_fin + ".*MNEWTRANS.*ServiceCenterOu.*transaction:.*transaction:.*$)";
                    estructura_mnewtrans(archivo,exp_regular);
                }
            }
        } else if (codeHTTP == 500) {
            logger.error("Error en la ejecución del documento");
            throw new Exception("");
        }
        borrarArchivo(archivo);
    }


    /**
     * Created by ycastelblanco
     *
     * Revisa que en los logs de actividad exista la palabra pri: con la priorioridad que se le haya asiganado al mensaje
     * @param archivo
     * @throws Exception
     */
    public void logsPriority(File archivo) throws Exception {
        String fecha_inicial = datosGlobales.get(Constantes.FECHA_INI).toString();
        String fecha_fin = datosGlobales.get(Constantes.FECHA_FIN).toString();
        boolean inSC = false, outFail = false, outSC = false;
        logger.info("Existe el archivo en el repositorio, inicia analisis de logs");
        inSC = existelineaEnFicheroLog(archivo, "(" + fecha_inicial + ".*IN.*ServiceCenterOu.*pri:4|" + fecha_fin + ".*IN.*ServiceCenterOu.*pri:4)");
        outSC = existelineaEnFicheroLog(archivo, "(" + fecha_inicial + ".*OUT.*ServiceCenterOu.*pri:" + datosGlobales.get(Constantes.PRIORITY) + "|" + fecha_fin + ".*OUT.*ServiceCenterOu.*pri:" + datosGlobales.get(Constantes.PRIORITY) + ")");
        outFail = existelineaEnFicheroLog(archivo, "(" + fecha_inicial + ".*OUT.*FailOverManager.*pri:" + datosGlobales.get(Constantes.PRIORITY) + "|" + fecha_fin + ".*OUT.*FailOverManager.*pri:" + datosGlobales.get(Constantes.PRIORITY) + ")");
        if (inSC && outSC && outFail) {
            logger.info("Lectura de logs de actividad (prioridad) fue correcta");
        } else {
            if (!inSC) {
                logger.info("No se encuantra el log de IN del ServieCenterOut con prioridad 4");
                throw new Exception("");
            }
            if (!outSC) {
                logger.info("No se encuantra el log de Out del ServieCenterOut con prioridad " + datosGlobales.get(Constantes.PRIORITY));
                throw new Exception("");
            }
            if (!outFail) {
                logger.info("No se encuantra el log de Out del FailOverManager con prioridad " + datosGlobales.get(Constantes.PRIORITY));
                throw new Exception("");
            }
        }
    }

    /**

     * Created by amartinez
     *
     * @param nombreScript a ejecutar en la maquina
     * @param usuario      con el que se ejecutara el script
     * @param documento    a por el cual se accede a realizar la ejecución.
     * @return Retorna el código HTTP resultado de la ejecución
     * @throws IOException
     * @throws InterruptedException
     */
    public int ejecutarScriptRemoto(String nombreScript, String usuario, String documento) throws Exception {
        String credenciales = datosGlobales.get(Constantes.CREDENCIALES).toString();
        String nomInstancia = datosGlobales.get(Constantes.NOM_INSTANCIA).toString();
        String idInstancia = awsTools.obtenerIdInstancia(nomInstancia);
        //Nombre del documento que se invocará a través de AWS TOOLS
        String url = URL_BASE + SYSTEM_MANAGER_SERVICE + documento + "/" + SEND;
        int codeHTTP;
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);

        method.setRequestHeader("Authorization", "Basic" + " " + credenciales);
        JsonObject body = new JsonObject();
        JsonArray instanceIds = new JsonArray();

        JsonArray parameters = new JsonArray();
        JsonObject parameter = new JsonObject();
        JsonArray values = new JsonArray();
        if (documento.equals("LAT_SWF_QA_RunSDPLogs")) {
            //primer parametro
            parameter.addProperty("parameter", "OsUsername");
            values.add(usuario);
            parameter.add("values", values);
            parameters.add(parameter);

            parameter = new JsonObject();
            values = new JsonArray();
            parameter.addProperty("parameter", "ScriptSDPLogs");
            values.add(nombreScript + ".sh");
            parameter.add("values", values);
            parameters.add(parameter);
        } else if (documento.equals("LAT_SWF_QA_RunLimspae")) {
            parameter.addProperty("parameter", "ScriptLimspae");
            values.add(nombreScript + ".sh");
            parameter.add("values", values);
            parameters.add(parameter);
        } else {
            logger.error("No se indico documento a ejecutar");
            throw new Exception("");
        }
        instanceIds.add(idInstancia);
        body.add("instanceIds", instanceIds);
        body.add("parameters", parameters);
        method.setRequestEntity(new StringRequestEntity(body.toString(), "application/json", "UTF-8"));
        codeHTTP = httpClient.executeMethod(method);
        return codeHTTP;
    }

    /**
     * Created by amartinez
     * Permite buscar una línea especíca en un archivo específico
     *
     * @param archivo      Archivo en el que se va a realizar la busqueda
     * @param lineaAbuscar Línea a buscar en el archivo
     * @return Retorna verdadero cuando la linea existe y falso si no existe
     * @throws InterruptedException
     */
    public boolean existelineaEnFicheroLog(File archivo, String lineaAbuscar) throws InterruptedException {
        FileReader fr = null;
        BufferedReader br;
        boolean existe = false;
        try {
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                Pattern expresion = Pattern.compile(lineaAbuscar);
                Matcher compararlinea = expresion.matcher(linea);
                if ((compararlinea.find())) {
                    existe = true;
                    break;
                } else {
                    existe = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return existe;
    }

    public void validarFicherolog(File archivo) throws Exception {
        String port = datosGlobales.get(Constantes.PORT).toString();
        logger.info("Puerto:" + port);
        if (port.equals("9080")) {
            boolean maquina = existelineaEnFicheroLog(archivo, "was");
            boolean fileLog = existelineaEnFicheroLog(archivo, "lprocess_0.log");
            if (maquina && fileLog) {
                logger.info("Se valida correctamente la versión del contenedor y su fichero de log respectivo: WAS - lprocess_0.log");
            } else {
                logger.error("no concuerda la version del contenedor y el fichero de log analizado: WAS - lprocess_0.log");
                throw new Exception("");
            }
        } else if (port.equals("7001")) {
            boolean wl10 = existelineaEnFicheroLog(archivo, "wl10");
            boolean wl12 = existelineaEnFicheroLog(archivo, "wl12");
            boolean fileLog = existelineaEnFicheroLog(archivo, "lprocess_0.log");
            boolean fileLog1 = existelineaEnFicheroLog(archivo, "lprocess_0.log.1");
            if (wl10 && fileLog) {
                logger.info("Se valida correctamente la versión del contenedor y su fichero de log respectivo: WL10 - lprocess_0.log");
            }
            if (wl12 && fileLog1) {
                logger.info("Se valida correctamente la versión del contenedor y su fichero de log respectivo: WL12 - lprocess_0.log.1 (Persiste lo reportado en LIMSP-62) ");
            }
            if (wl12 && fileLog) {
                logger.info("Se corrigio la alerta LIMSP-62, este mensaje es de control para realizar ajuste en el proyecto");
            }
        }

    }

    public void borrarArchivo(File archivo) {
        if (archivo.exists()) {
            archivo.delete();
            logger.info("Borrado el archivo de logs");
        } else {
            logger.info("No existe el archivo de logs a borrar");
        }
    }

}