package Testing;

import java.util.Properties;
import java.lang.Exception;

import LData_Testing.AccesoWSInf;
import com.latinia.limsp.util.message.client.*;
import com.latinia.limsp.util.message.server.*;
import com.latinia.limspinf.stubs.storage.*;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InsercionWSsdp {

    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    private static Logger logger = LogManager.getLogger(InsercionWSsdp.class);
    private LatiniaScenarioUtil scenarioUtil;
    AccesoWSInf accesoWSInf;
    private Storage storage;

    public InsercionWSsdp() {
        //Instanciacion de la clase 'LatiniaScenarioUtil' para utilizar sus metodos
        scenarioUtil = new LatiniaScenarioUtil();
        accesoWSInf = new AccesoWSInf();
        storage = (Storage)accesoWSInf.wsINFGeneric(Constantes.APP_LMAN_VCONTENT, Constantes.LATINIA,
                Constantes.WASURL_STORAGE, Constantes.WLURL_STORAGE, Constantes.WS_INF_STORAGE_SERVICE,
                Constantes.WS_INF_STORAGE_METHOD);
    }


    @Step("Mensaje Malformado XML <> <> <> <> <> <> <> <> <> <>")
    public void msgalformed(String fname, String empresa, String ref_product, String ref_contract, String gsmdest, String ref_app, String clave, String clavevalor, String destinatario, String subject) throws Exception {
        String data = LatiniaScenarioUtil.readResource("xml/" + fname);
        logger.info("Leido RAW: " + data);
        //System.out.println("Leido RAW: " + data);
        Properties props = new Properties();
        props.put("loginenterprise", empresa);
        props.put("ref_contract", ref_contract);

        int randomNum = scenarioUtil.randInt(1, 99999);
        //guardo el valor del 'randomNum' generado para utilizarlo despues en la busqueda de mensajes descratados con el id
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum);
        props.put("id", "xmlMalformado" + randomNum);

        if (fname.contains("PNS")) {
            props.put("pns_ref_app", ref_app);
            if (clave.equalsIgnoreCase("refUser")) {
                //Cuando envio utilizando el refUser, se requiere que el XML no tenga el campo  "KeyName='refUser'"
                props.put("clave", "");
            } else {
                props.put("clave", "KeyName='" + clave + "'");
            }
            props.put("clavevalor", clavevalor);
        } else if (fname.contains("SMS")) {
            logger.info("entra si es SMS");
            //System.out.println("entra si es SMS");
            props.put("gsmdest", gsmdest);
        } else if (fname.contains("EMAIL")) {
            props.put("destinatario", destinatario);
            props.put("subject", subject);
        }

    }

    /**
     * Inserta un XML utilizando el componente "wtest/sendMsg"
     * Detecta el tipo de transaccion y
     * Informacion adicional puede encontrarse en el Email 	De: Jordi Perez
     * Enviado el: lunes, 30 de marzo de 2015 10:06
     * Para: Xavier Ruiz
     * CC: Ricard Rovira
     * Asunto: SDP-1010 Desarrollo de aplicaciÃƒÂ³n wtest para Testing y Diagnosis de Plataforma
     * El path de los XML es "C:\Testing\Gauge_Projects\<nombre_proyecto>\recursos\xml"
     *
     * @param fname        Nombre del fichero en disco que se va a leer
     * @param ref_product  Producto/aplicacion que se utilizara en la inyeccion
     * @param ref_contract Contrato que se utilizara en la inyeccion al adaptador WS
     * @throws Exception
     * @author @xruizs
     */
    public void pushSMSwtest(String fname, String empresa, String ref_product, String ref_contract, String gsmdest, String content) throws Exception {
        String resultado = "";
        //Lee el contenido del fichero de texto y lo entrega convertido en 'string'
        String data = LatiniaScenarioUtil.readResource("xml/" + fname);
        logger.info("Leido RAW: " + data);
        //System.out.println("Leido RAW: " + data);
        Properties props = new Properties();
        props.put("loginenterprise", empresa);
        props.put("ref_contract", ref_contract);
        props.put("gsmdest", gsmdest);
        //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido del mensaje, asi podre encontralo en Estadisticas
        int randomNum = scenarioUtil.randInt(1, 99999);
        //guardo el valor del 'randomNum' generado para utilizarlo despues en la busqueda de estadisticas
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum);
        props.put("content", content + randomNum + " EUR");
        if (//Comprueba que el XML leido sea de tipo SMS
                data.contains("<type ref='sms'>")) {
            //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
            data = LatiniaScenarioUtil.replaceDataParams(data, props);
            //Hasta aqui la composicion del XML que se va a inyectar. Ahora se realiza el POST del XML al componente WTEST
            logger.info("Procesado: " + data);
            logger.info("Insertando SMS por 'wtest/sendMsg'");
            //System.out.println("Procesado: " + data);
            //System.out.println("Insertando SMS por 'wtest/sendMsg'");
            //Se tienen que pasar asi las credenciales en parametros porque el elemento WTEST que se imboca esta bajo la autentificacion del contenedor.
            resultado = LatiniaScenarioUtil.conectaURLpost(ref_product, data, "INNOVUS.VALIDACION", "BENCHMARK");
            //AtenciÃƒÂ³n, acordarse de crear un contrato para que pueda autentificarse en el GUI
            logger.info("WTEST devuelve: " + resultado);
            logger.info("Comprobando el contenido devuelto");
            //System.out.println("INF: WTEST devuelve: " + resultado);
            //System.out.println("INF: Comprobando el contenido devuelto");
            //Meto el String que contiene la transacciÃƒÂ³n devuelta, en un objeto 'Formatter'; de este modo podre operar con el, y sacarle datos
            FormatterTransactionAdmin trans = (FormatterTransactionAdmin) FormatterFactory.createFormatter(resultado);
            //Espera encontrar, para un SMS, el campo <mroute>MT</mroute>.
            FormatterMessageAdmin messageOut = (FormatterMessageAdmin) trans.getMessageByMRoute("MT");
            if (messageOut == null) {
                logger.error("<mroute> incorrecto. Se esperaba 'MT'");
                //System.out.println("ERR: <mroute> incorrecto. Se esperaba 'MT'");
                throw new Exception("");
            } else {
                //Agarro el contenido que fue incluido como propiedad en el XML original, para compararlo con lo que se ha recibido en el colector. 
                String contenido = props.getProperty("content");
                if (contenido.equals(messageOut.getContentText())) {
                    logger.info("BIEN: " + contenido + " ==  " + messageOut.getContentText());
                    //System.out.println("BIEN: " + contenido + " ==  " + messageOut.getContentText());
                } else {
                    logger.error("La entrada: " + contenido + " != Salida: " + messageOut.getContentText());
                    //System.out.println("ERR: La entrada: " + contenido + " != Salida: " + messageOut.getContentText());
                    throw new Exception("");
                }
            }
        } else {
            logger.error("El fichero leido no contiene un <type ref='sms'>");
            //System.out.println("ERR: El fichero leido no contiene un <type ref='sms'>");
            throw new Exception("");
        }
    }

    /**
     * Inserta un XML utilizando el componente "wtest/sendMsg"
     * Detecta el tipo de transaccion y
     * Informacion adicional puede encontrarse en el Email 	De: Jordi Perez
     * Enviado el: lunes, 30 de marzo de 2015 10:06
     * Para: Xavier Ruiz
     * CC: Ricard Rovira
     * Asunto: SDP-1010 Desarrollo de aplicaciÃƒÂ³n wtest para Testing y Diagnosis de Plataforma
     * El path de los XML es "C:\Testing\Gauge_Projects\<nombre_proyecto>\recursos\xml"
     *
     * @param fname        Nombre del fichero en disco que se va a leer
     * @param ref_product  Producto/aplicacion que se utilizara en la inyeccion
     * @param ref_contract Contrato que se utilizara en la inyeccion al adaptador WS
     * @throws Exception
     * @author @xruizs
     */
    public void pushPNSwtest(String fname, String empresa, String ref_product, String ref_contract, String ref_app, String clave, String clavevalor, String content) throws Exception {
        String resultado = "";
        //Lee el contenido del fichero de texto y lo entrega convertido en 'string'
        String data = LatiniaScenarioUtil.readResource("xml/" + fname);
        logger.error("Leido RAW: " + data);
        //System.out.println("Leido RAW: " + data);
        Properties props = new Properties();
        props.put("loginenterprise", empresa);
        props.put("ref_contract", ref_contract);
        props.put("pns_ref_app", ref_app);
        if (clave.equalsIgnoreCase("refUser")) {
            //Cuando envio utilizando el refUser, se requiere que el XML no tenga el campo  "KeyName='refUser'"
            props.put("clave", "");
        } else {
            props.put("clave", "KeyName='" + clave + "'");
        }
        props.put("clavevalor", clavevalor);
        //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido del mensaje, asi podre encontralo en Estadisticas
        int randomNum = scenarioUtil.randInt(1, 99999);
        //guardo el valor del 'randomNum' generado para utilizarlo despues en la busqueda de estadisticas
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum);
        props.put("content", content + randomNum + "EUR");
        props.put("idMsgExt", String.valueOf(randomNum));
        if (//Comprueba que el XML leido sea de tipo PNS
                data.contains("<type ref='pns'>")) {
            //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
            data = LatiniaScenarioUtil.replaceDataParams(data, props);
            //Hasta aqui la composicion del XML que se va a inyectar. Ahora se realiza el POST del XML al componente WTEST
            logger.info("Procesado: " + data);
            logger.info("Insertando PNS por 'wtest/sendMsg'");
            //System.out.println("Procesado: " + data);
            //System.out.println("Insertando PNS por 'wtest/sendMsg'");
            //Se tienen que pasar asi las credenciales en parametros porque el elemento WTEST que se imboca esta bajo la autentificacion del contenedor.
            resultado = LatiniaScenarioUtil.conectaURLpost(ref_product, data, "INNOVUS.VALIDACION", "BENCHMARK");
            //**AtenciÃƒÂ³n, acordarse de crear un contrato de la App 'Push_Testing_y_Diagnosis', para que pueda autentificarse en el GUI**
            if (resultado == null) {
                logger.error("WTEST devuelve: " + resultado);
                //System.out.println("ERR: WTEST devuelve: " + resultado);
                throw new Exception("");
            } else {
                logger.info("WTEST devuelve: " + resultado);
                //System.out.println("INF: WTEST devuelve: " + resultado);
            }
            logger.info("Comprobando el contenido devuelto");
            //System.out.println("INF: Comprobando el contenido devuelto");
            //Meto el String que contiene la transacciÃƒÂ³n devuelta, en un objeto 'Formatter'; de este modo podre operar con el, y sacarle datos
            FormatterTransactionAdmin trans = (FormatterTransactionAdmin) FormatterFactory.createFormatter(resultado);
            //Espera encontrar, para un PNS, el campo <mroute>out</mroute>.
            FormatterMessageAdmin messageOut = (FormatterMessageAdmin) trans.getMessageByMRoute("out");
            if (messageOut == null) {
                logger.error("<mroute> incorrecto. Se esperaba 'out'");
                //System.out.println("ERR: <mroute> incorrecto. Se esperaba 'out'");
                throw new Exception("");
            } else {
                //Agarro el contenido que fue incluido como propiedad en el XML original, para compararlo con lo que se ha recibido en el colector. 
                String contenido = props.getProperty("content");
                if (contenido.equals(messageOut.getContentText())) {
                    logger.info("BIEN: " + contenido + " ==  " + messageOut.getContentText());
                    //System.out.println("BIEN: " + contenido + " ==  " + messageOut.getContentText());
                    // VerificaciÃƒÂ³n del contenido privado "Private content" de una PNS.
                    String errText = null;
                    try {
                        String idMsgAddress = ((MsgAddressAdmin) messageOut.listAddressesByType("to").next()).getId();
                        String idUser = messageOut.getCustomerId();
                        Delivery delivery = storage.getDelivery("automated", idMsgAddress, Integer.parseInt(idUser));
                        if (delivery != null && delivery.getContent() != null) {
                            if (("Private: " + contenido).equals(delivery.getContent().getPrivateContentText())) {
                                logger.info("BIEN: 'Private: " + contenido + "' == '" + delivery.getContent().getPrivateContentText() + "'");
                                //System.out.println("BIEN: 'Private: " + contenido + "' == '" + delivery.getContent().getPrivateContentText() + "'");
                            } else {
                                errText = "La entrada: 'Private: " + contenido + "' != Salida: '" + delivery.getContent().getPrivateContentText() + "'";
                            }
                        } else {
                            errText = "LIMSP-INF no ha devuelto ningÃƒÂºn contenido.";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        errText = "No se ha podido acceder al contenido privado. " + e.getMessage();
                    } finally {
                        if (errText != null) {
                            logger.error(errText);
                            //System.err.println(errText);
                            throw new Exception("");
                        }
                    }
                } else {
                    logger.error("La entrada: " + contenido + " != Salida: " + messageOut.getContentText());
                    //System.out.println("ERR: La entrada: " + contenido + " != Salida: " + messageOut.getContentText());
                    throw new Exception("");
                }
            }
        } else {
            logger.error("El fichero leido no contiene un <type ref='pns'>");
            //System.out.println("ERR: El fichero leido no contiene un <type ref='pns'>");
            throw new Exception("");
        }
    }

    /**
     * Confecciona una iNOT empleando los formatters.
     * La inyeccion va contra el componente "wtest/sendMsg" diseÃƒÂ±ado por JP
     * Informacion adicional puede encontrarse en el Email 	De: Jordi Perez
     * Enviado el: lunes, 30 de marzo de 2015 10:06
     * Para: Xavier Ruiz
     * CC: Ricard Rovira
     * Asunto: SDP-1010 Desarrollo de aplicaciÃƒÂ³n wtest para Testing y Diagnosis de Plataforma
     *
     * @param ref_product  Producto/aplicacion que se utilizara en la inyeccion
     * @param ref_contract Contrato que se utilizara en la inyeccion al adaptador WS
     * @throws Exception
     * @author @xruizs
     */
    public String pushiNOTwtest(String canal, String empresa, String ref_product, String ref_contract, String ref_mapp, String clave, String clavevalor, String content) throws Exception {
        String resultado = "";
        //Se crea un mensaje con formato INot
        FormatterMessage msg = FormatterFactory.newFormatterMessageOut("inot", "inot");
        //Se establecen los datos bÃƒÂ¡sicos del envÃƒÂ­o
        msg.setLoginEnterprise(empresa);
        msg.setRefContract(ref_contract);
        //Se establece las diferentes direcciones finales
        msg.setCustomerKeyName(clave);
        msg.setCustomerKeyValue(clavevalor);
        //Se establece orden de preferencia
        //Lista de canales a enviar por ejemplo {"pns","sms"};
        String[] canales = {canal.toLowerCase()};
        msg.setDeliveryChannels(canales);
        msg.setMaxDeliveryChannels("1");
        //Se establece el contenido del mensaje
        //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido del mensaje, asi podre encontralo en Estadisticas
        int randomNum = scenarioUtil.randInt(1, 99999);
        //guardo el valor del 'randomNum' generado para utilizarlo despues en la busqueda de estadisticas
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum);
        content = content + " " + randomNum + "EUR";
        msg.setContent(content);
        //Se establece la plantilla pÃƒÂºblica
        /*MsgContent contentpublic=msg.setContent("inot");
            contentpublic.setRefTemplate("templatePublic");
			contentpublic.setRefTemplateParams("templateParams");
	
			//Se estable la plantilla privada
			MsgContent contentPrivate=msg.addContent("inot","text/plain");
			contentPrivate.setPrivateContent(true);
			contentPrivate.setRefTemplate("templatePrivate");
			contentPrivate.setRefTemplateParams("templateParams");
	
			//ParÃƒÂ¡metros para las dos plantillas
			MsgTemplateParams msgTemplateParams=msg.addTemplateParams("templateParams");
			msgTemplateParams.setParamValue("name","Juan V");
			msgTemplateParams.setParamValue("amount","1010");
			*/
        String data = msg.toString();
        //Se tienen que pasar asi las credenciales en parametros porque el elemento WTEST que se imboca esta bajo la autentificacion del contenedor.
        resultado = LatiniaScenarioUtil.conectaURLpost(ref_product, data, "INNOVUS.VALIDACION", "BENCHMARK");
        //**AtenciÃƒÂ³n, acordarse de crear un contrato de la App 'Push_Testing_y_Diagnosis', para que pueda autentificarse en el GUI**
        logger.info("WTEST devuelve: " + resultado);
        //System.out.println("INF: WTEST devuelve: " + resultado);
        return resultado;
    }

    /**
     * @param fname
     * @param empresa
     * @param ref_product
     * @param ref_contract
     * @param ref_app
     * @param content
     * @throws Exception
     */
    @Step("When inserta PNSMasivo <> empresa=<> ref_product=<> contrato=<> ref_app=<> content=<> adaptor=<>")
    public void pushPNSmasivo(String fname, String empresa, String ref_product, String ref_contract, String ref_app, String content, String adaptorws) throws Exception {
        //Lee el contenido del fichero de texto y lo entrega convertido en 'string'
        String data = LatiniaScenarioUtil.readResource("xml/" + fname);
        logger.error("Leido RAW: " + data);
        //System.out.println("Leido RAW: " + data);
        Properties props = new Properties();
        props.put("loginenterprise", empresa);
        props.put("ref_contract", ref_contract);
        props.put("pns_ref_app", ref_app);
        //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido del mensaje, asi podre encontralo en Estadisticas
        int randomNum = scenarioUtil.randInt(1, 99999);
        //guardo el valor del 'randomNum' generado para utilizarlo despues en la busqueda de estadisticas
        datosGlobales.put(Constantes.RANDOM_NUM, randomNum);
        props.put("content", content + randomNum + " EUR");
        if (//Comprueba que el XML leido sea de tipo PNS
                data.contains("<type ref='pns'>")) {
            //Remplaza en 'data', el conjunto de datos en forma de propiedades nombre=valor proporcionados en 'props'
            data = LatiniaScenarioUtil.replaceDataParams(data, props);
            logger.info("Procesado: " + data);
            logger.info("Insertando PNS MASSIVO por WS");
            logger.info("En caso de que 'pete' con un error:  'DeferredDeliver WRN Funcionalidad no permitida por licencia: 'Massive Messages'; debes comprobar que la licencia permite uso de masivos. En caso contrario mostrara el log Limsp_EXT:");
            //System.out.println("Procesado: " + data);
            //System.out.println("Insertando PNS MASSIVO por WS");
            //System.out.println("INFO: En caso de que 'pete' con un error:  'DeferredDeliver WRN Funcionalidad no permitida por licencia: 'Massive Messages'; debes comprobar que la licencia permite uso de masivos. En caso contrario mostrara el log Limsp_EXT:");
            LatiniaScenarioUtil.sendMessageWS(ref_product, "latinia", data, adaptorws);
        } else {
            logger.error("El fichero leido no contiene un <type ref='sms'>");
            //System.out.println("ERR: El fichero leido no contiene un <type ref='sms'>");
            throw new Exception("");
        }
        // LTimers ltimers = new LTimers();
        //Llamo a mi propia funciÃƒÂ³n 'setDatosGlobales' en LTimers, que lo que hace es pasarle 'datosglobales', ya que de lo contrario, al tratar de usar 'datosglobales' en LTimers, sale 'null'
//        ltimers.setdatosGlobales(DatosGlobales);
        //ltimers.lservmassive();//Ejecuto el Timer para confeccion de los PNS Masivos BULK "DEPRECADO SDPR3.9.5"
    }


    /**
     * *BUG:SDP-1022*" Enviando una iNOT que terminará siendo un Tweet, y utilizando el Email como 'clave' de usuario,
     * se deberia observar en estadisticas la clave utilizada (e-mail), sin embargo lo que se muestra es el destinatario Tweet.
     * Para comprobar este BUG lo que hacemos es buscar si en el mensaje de salida existe el tag <alias>, en caso de no existir, sera un BUG
     *
     * @param canal        Tipo de mensaje al que serÃƒÂ¡ convertida la transaccion final
     * @param empresa      Empresa que se utiliza para el envio
     * @param ref_product
     * @param ref_contract
     * @param clavevalor   Cuenta de Email del usuario que recibira el Tweet
     * @param content      Texto que sera enviado como contenido del mensaje. Lo que se reciba en el colector debe ser igual.
     * @throws Exception
     */
    public void checkBugSDP1022(String canal, String empresa, String ref_product, String ref_contract, String ref_mapp, String clave, String clavevalor, String content) throws Exception {
        String resultado = "";
        String randomNum = "vacio";
        //Inyeccion del iNOT y captura del resultado
        resultado = pushiNOTwtest(canal, empresa, ref_product, ref_contract, ref_mapp, clave, clavevalor, content);
        try {
            //Es el numero random que se guardo durante la generacion de la iNOT.
            randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
        } catch (Exception e) {
        }
        content = content + " " + randomNum + "EUR";
        logger.info("Testing *BUG:SDP-1022*");
        //System.out.println("INF: Testing *BUG:SDP-1022*");
        //Meto el String que contiene la transacciÃƒÂ³n devuelta, en un objeto 'Formatter'; de este modo podre operar con el, y sacarle datos
        FormatterTransactionAdmin trans = (FormatterTransactionAdmin) FormatterFactory.createFormatter(resultado);
        FormatterMessageAdmin messageOut;
        if (canal.equalsIgnoreCase("sms")) {
            //Espera encontrar el campo <mroute>MT</mroute> para un SMS.
            messageOut = (FormatterMessageAdmin) trans.getMessageByMRoute("MT");
        } else if (canal.equalsIgnoreCase("email") || canal.equalsIgnoreCase("pns") || canal.equalsIgnoreCase("tweet")) {
            //Espera encontrar el campo <mroute>out</mroute>.
            messageOut = (FormatterMessageAdmin) trans.getMessageByMRoute("out");
        } else{
            logger.error("Canal invalido " + canal);
            throw new Exception("");
        }
        if (messageOut == null) {
            logger.error("<mroute> incorrecto. Se esperaba 'out' para una transaccion tipo " + canal);
            throw new Exception("");
        } else {
            if (//comparo el contenido original que fue inyectado, con lo que se ha recibido en el colector.
                    content.equals(messageOut.getContentText())) {
                MsgAddress to = messageOut.getAddressByType("to");
                if (to != null && clavevalor != null) {
                    if (clavevalor.equals(to.getAlias())) {
                        logger.error("Identificado *BUG:SDP-1022*");
                        throw new Exception("");
                    } else {
                        logger.info("Superado *BUG:SDP-1022*");
                    }
                }
                logger.info("BIEN: " + content + " ==  " + messageOut.getContentText());
            } else {
                logger.error("La entrada: " + content + " != Salida: " + messageOut.getContentText());
                throw new Exception("");
            }
        }
    }

}