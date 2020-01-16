package Testing;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.InputStream;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.Properties;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * Created by amartinez on 17/11/2016.
 */
public class GFileReader {

    private Browser browser;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private LatiniaScenarioUtil scenarioUtil = new LatiniaScenarioUtil();
    private static final String ARCHIVOFR = "archivofr";

    String unidad = datosGlobales.get(Constantes.UNIDAD).toString();
    String directorio = datosGlobales.get(Constantes.DIRECTORIO).toString();
    String rutades = datosGlobales.get(Constantes.RUTA_DESTINO).toString();
    String user = datosGlobales.get(Constantes.USER_LINUX).toString();
    String pass = datosGlobales.get(Constantes.PASSWORD_LINUX).toString();
    String hostsftp = datosGlobales.get(Constantes.HOST_SFTP).toString();
    int portsftp = Integer.parseInt(datosGlobales.get(Constantes.PORT_SFTP).toString());
    private static Logger logger = LogManager.getLogger(GFileReader.class);


    public GFileReader() {
        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
    }

    /**
     * Se renombra el .build por .ready
     * En este método se instancia un canal sftp para transferir archivos desde windows al servidor, el archivo que se transfiere es el archivo generado con extensión .ready
     *
     * @param nomArchivo
     * @throws Exception
     */

    public void enviarArchivo(String nomArchivo) throws Exception {
//Primero se renombra el archivo .buid a .ready
        File archivo1 = new File(unidad + ":\\" + directorio + "\\" + nomArchivo + ".build");
        File archivo2 = new File(unidad + ":\\" + directorio + "\\" + nomArchivo + ".ready");
        archivo1.renameTo(archivo2);
        String unidadgen = unidad + ":\\" + directorio + "\\" + nomArchivo + ".ready";
        logger.info("la ruta origen es " + unidadgen);
        String rutaDestino = rutades + "/" + nomArchivo + ".ready";
        logger.info("la ruta destino es " + rutaDestino);
        JSch sftp = new JSch();
        // Instancio el objeto session para la transferencia
        Session session = null;
        // instancio el canal sftp
        ChannelSftp channelSftp = null;
        try {
            // Inciciamos el JSch con el usuario, host y puerto
            session = sftp.getSession(user, hostsftp, portsftp);
            // Seteamos el password
            session.setPassword(pass);
            // El SFTP requiere un intercambio de claves, con esta propiedad le decimos que acepte la clave
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);

            session.connect();

            // Abrimos el canal de sftp y conectamos
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            // Convertimos el archivo a transferir en un InputStream
            BufferedInputStream os = new BufferedInputStream(new FileInputStream(unidadgen));
            // Iniciamos la transferencia
            channelSftp.put(os, rutaDestino);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            // Cerramos el canal y session
            if (channelSftp.isConnected())
                channelSftp.disconnect();
            if (session.isConnected())
                session.disconnect();
        }
    }


    /**
     * En este método se escribe el archivo pruebaPNSxxx.build, donde las xxx es un random generado por el método para guardar el archivo y posteriormente buscarlo en los logs y observar si se renombró con la extensión .closed
     * @param nameArchivo
     * @param posRandom posición del mensaje en la tabla donde se quiere poner el random para identificar los mensajes en estadisticas
     */
    @Step("Escribir Archivo <> <>")
    public void escribirArchivo(String nameArchivo, int posRandom) {

        Table cabecera = (Table) datosGlobales.get(Constantes.CABECERA);
        Table cuerpo = (Table) datosGlobales.get(Constantes.CUERPO);

        List<TableRow> rowsCab = cabecera.getTableRows();
        List<String> columnNamesCab = cabecera.getColumnNames();

        List<TableRow> rowsCuer = cuerpo.getTableRows();
        List<String> columnNamesCuer = cuerpo.getColumnNames();

        String archivofr;
        int randomNum = scenarioUtil.randInt(1, 99999); //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido de este campo, asi podre encontralo en Estadisticas
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum); //guardo el valor del 'randomNum' generado para utilizarlo despues en las búsquedas, tambien me sirve para cuardar el archivo para posteriormnete

        File f;
        f = new File(unidad + ":\\" + directorio + "\\" + nameArchivo + randomNum + ".build");
        String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String refContract = datosGlobales.get(Constantes.REF_CONTRACT).toString();
        Date date;
        //Escritura
        try {
            FileWriter w = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);
            //Se escribe la cabecera

            for (TableRow row : rowsCab) {
                String contenido = "";
                for (int i = 0; i < row.size(); i++) {

                    if(row.getCell(columnNamesCab.get(i)).contentEquals("inicio")){
                    //Se toma la fecha del método deStringToDate(). se le agrega el tiempo deseado y se convierte a String
                        date = deStringToDate();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date); // Configuramos la fecha que se recibe
                        calendar.add(Calendar.MINUTE, 5);
                        logger.info(calendar.getTime().toString());
                        DateFormat fechaHora = new SimpleDateFormat("yyyyMMddHHmm");
                        String convertido = fechaHora.format(calendar.getTime());

                        contenido += row.getCell(columnNamesCab.get(i)).replace("inicio",convertido) + "|";

                    } else if(row.getCell(columnNamesCab.get(i)).contentEquals("expire")){
                        date = deStringToDate();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date); // Configuramos la fecha que se recibe
                        calendar.add(Calendar.MINUTE, -5);
                        logger.info(calendar.getTime().toString());
                        DateFormat fechaHora = new SimpleDateFormat("yyyyMMddHHmm");
                        String convertido = fechaHora.format(calendar.getTime());

                        contenido += row.getCell(columnNamesCab.get(i)).replace("expire",convertido) + "|";
                    }

                    else {

                        contenido += row.getCell(columnNamesCab.get(i)) + "|";
                    }
                }

                contenido = contenido.substring(0, contenido.length() - 1);
                wr.write(contenido);
                wr.append("\r\n");
            }
            //Se escribe el cuerpo
            //pos-random se refiere a la posición que se desea llevar el random para identificar el mensaje en estadisticas
            // posRandom == -1 cuando se desea poner al final
            if(posRandom == -1) {

                for (TableRow row : rowsCuer) {
                    String contenido = "";
                    for (int i = 0; i < row.size(); i++) {

                        if(row.getCell(columnNamesCuer.get(i)).contentEquals("diferido")){

                            date = deStringToDate();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date); // Configuramos la fecha que se recibe
                            calendar.add(Calendar.MINUTE, 11);
                            logger.info(calendar.getTime().toString());
                            DateFormat fechaHora = new SimpleDateFormat("yyyyMMddHHmm");
                            String convertido = fechaHora.format(calendar.getTime());

                            contenido += row.getCell(columnNamesCuer.get(i)).replace("diferido",convertido) + "|";

                        } else if(row.getCell(columnNamesCuer.get(i)).contentEquals("caducidad")){
                            date = deStringToDate();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date); // Configuramos la fecha que se recibe
                            calendar.add(Calendar.MINUTE, -15);
                            logger.info(calendar.getTime().toString());
                            DateFormat fechaHora = new SimpleDateFormat("yyyyMMddHHmm");
                            String convertido = fechaHora.format(calendar.getTime());

                            contenido += row.getCell(columnNamesCuer.get(i)).replace("caducidad",convertido) + "|";

                        } else {

                            contenido += row.getCell(columnNamesCuer.get(i)) + "|";
                        }
                    }

                    contenido = contenido.substring(0, contenido.length() - 1);
                    contenido = contenido.concat(Integer.toString(randomNum));
                    wr.append(contenido);
                    wr.append("\r\n");
                }
            } else { // Sino es -1, se toma el valor por parametro y se ubica el random en la posición deseada

                for (TableRow row : rowsCuer) {
                    String contenido = "";

                    for (int i = 0; i < row.size(); i++) {
                        if(i==posRandom) {
                            if(row.getCell(columnNamesCuer.get(i)).contentEquals("diferido")){

                                date = deStringToDate();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date); // Configuramos la fecha que se recibe
                                calendar.add(Calendar.MINUTE, 15);
                                logger.info(calendar.getTime().toString());
                                DateFormat fechaHora = new SimpleDateFormat("yyyyMMddHHmm");
                                String convertido = fechaHora.format(calendar.getTime());

                                contenido += row.getCell(columnNamesCuer.get(i)).replace("diferido",convertido) + "|";

                            } else if(row.getCell(columnNamesCuer.get(i)).contentEquals("caducidad")){
                                date = deStringToDate();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date); // Configuramos la fecha que se recibe
                                calendar.add(Calendar.MINUTE, -15);
                                logger.info(calendar.getTime().toString());
                                DateFormat fechaHora = new SimpleDateFormat("yyyyMMddHHmm");
                                String convertido = fechaHora.format(calendar.getTime());

                                contenido += row.getCell(columnNamesCuer.get(i)).replace("caducidad",convertido) + "|";

                            } else {

                                contenido += row.getCell(columnNamesCuer.get(i)) + randomNum + "|";
                            }
                        } else {

                            if(row.getCell(columnNamesCuer.get(i)).contentEquals("diferido")){

                                date = deStringToDate();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date); // Configuramos la fecha que se recibe
                                calendar.add(Calendar.MINUTE, 15);
                                logger.info(calendar.getTime().toString());
                                DateFormat fechaHora = new SimpleDateFormat("yyyyMMddHHmm");
                                String convertido = fechaHora.format(calendar.getTime());

                                contenido += row.getCell(columnNamesCuer.get(i)).replace("diferido",convertido) + "|";

                            } else if(row.getCell(columnNamesCuer.get(i)).contentEquals("caducidad")){
                                date = deStringToDate();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date); // Configuramos la fecha que se recibe
                                calendar.add(Calendar.MINUTE, -15);
                                logger.info(calendar.getTime().toString());
                                DateFormat fechaHora = new SimpleDateFormat("yyyyMMddHHmm");
                                String convertido = fechaHora.format(calendar.getTime());

                                contenido += row.getCell(columnNamesCuer.get(i)).replace("caducidad",convertido) + "|";

                            } else {
                                contenido += row.getCell(columnNamesCuer.get(i)) + "|";
                            }
                        }
                    }
                    contenido = contenido.substring(0, contenido.length() - 1);
                    wr.append(contenido);
                    wr.append("\r\n");
                }
            }
            wr.append("<EOF>");
            wr.close();
            bw.close();

            archivofr = nameArchivo + randomNum;
            datosGlobales.put(ARCHIVOFR, archivofr);
            enviarArchivo(archivofr);
        } catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * De la misma manera que se transfiere el archivo .ready, se trae el archivo ya renombrado desde el servidor hasta windows, en la misma ubicación donde inicialmente se escribe el .ready
     * @param extension Tipo de extensión que se desea verificar. .ready, .closed, .expire
     * @throws Exception
     */
    @Step("Traer Archivo <>")
    public void traerArchivo(String extension) throws Exception {
        String namearchivo = datosGlobales.get(ARCHIVOFR).toString();
        String rutaO = rutades + "/" + namearchivo + "*" + extension;
        String rutaD = unidad + ":\\" + directorio + "\\" + namearchivo + extension;

        JSch sftp = new JSch();
        // Instancio el objeto session para la transferencia
        Session session = null;
        // instancio el canal sftp
        ChannelSftp channelSftp = null;
        try {
            // Inciciamos el JSch con el usuario, host y puerto
            session = sftp.getSession(user, hostsftp, portsftp);
            // Seteamos el password
            session.setPassword(pass);
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();
            // Abrimos el canal de sftp y conectamos
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            // Convertimos el archivo a transferir en un OutputStream
            OutputStream os = new BufferedOutputStream(new FileOutputStream(rutaD));
            channelSftp.get(rutaO, os);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.getStackTrace());
            logger.error(e.getMessage());
            logger.error(e.toString());
            throw new Exception(e);

        } finally {
            // Cerramos el canal y session
            if (channelSftp.isConnected())
                channelSftp.disconnect();
            if (session.isConnected())
                session.disconnect();
        }


    }


    /**
     * Este método utiliza traerArchivo para verificar que se hizo el cambio de extensión .ready por .closed
     */
    @Step("Es Igual")
    public void esIgual() throws Exception {
        File file = new File(unidad + ":\\" + directorio + "\\");
        String[] files = file.list();
        String namearchivo = datosGlobales.get(ARCHIVOFR).toString();
        String ready = null;
        String closed = null;
        for (String f : files) {
            if (f.contains(namearchivo)) {
                if (f.endsWith(".ready")) {
                    ready = f;
                    logger.info(ready);
                }

                if (f.endsWith(".closed")) {

                    closed = f;
                    logger.info(closed);
                }
            }
        }
        int end = closed.indexOf(".");
        if (closed.substring(0, end).equals(ready.substring(0, end))) {
            logger.info("Son iguales");
        }
    }

    /**
     * En este método se estable una conexión ssh para ejecutar comandos en linux, se obtine la fecha mediante el comando date +%Y\ %m\ %d\ %H\ %M
     * @return Retorna la fecha en formato String
     */

    public String getDate() {

        String command1 = "date +%Y\\ %m\\ %d\\ %H\\ %M";
        String fecha = "";
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, hostsftp, portsftp);
            session.setPassword(pass);
            session.setConfig(config);
            session.connect();
            logger.info("Connected for time");

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command1);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];

            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;

                    fecha = new String(tmp, 0, i);

                }
                if (channel.isClosed()) {
                    logger.info("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            channel.disconnect();
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }


    /**
     * Se pasa una fecha de String a formato date
     * @return la fecha en formato date
     */
    public  Date deStringToDate() {
        String fecha = getDate();
        fecha = getDate().replace(" ","");

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyyMMddHHmm");
        Date fechaEnviar = null;
        try {
            fechaEnviar = formatoDelTexto.parse(fecha);

            return fechaEnviar;

        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}


