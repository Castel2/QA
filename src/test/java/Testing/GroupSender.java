package Testing;// JUnit Assert framework can be used for verification

import java.util.TimeZone;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Property;

public class GroupSender {

	private Browser browser;
	private LatiniaScenarioUtil scenarioUtil = new LatiniaScenarioUtil();
	private static Logger logger = LogManager.getLogger(GroupSender.class);
	DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

	public GroupSender() {
		browser = LatiniaUtil.getBrowser();
			}

	/**
	 * (XR: 27/01/2015) Este metodo esta inacabado porque no podemos probar la subida de archivos a causa de como esta implementada la web.
	 * @throws Exception
	 */
	@Step("subir")
	public void subirArchivo() throws Exception {
		String archivo = datosGlobales.get(Constantes.ARCHIVO).toString();

		browser.link("Archivos almacenados").click();
		
		//if (browser.cell(archivo).exists(true)){
		//	browser.italic("icon-trash").rightOf(browser.cell(archivo)).click();
		//}

		//browser.link("Añadir archivos").click();
		browser.link("Añadir archivos").setFile("C:\\Testing\\Gauge_Projects\\Util\\MaterialGroupSender\\"+archivo);
		//browser.submit("Subir").click();
		//browser.file("fileupload").setFile2("..\\Util\\MaterialGroupSender\\"+archivo);
		//browser.navigateTo("",true);
		//String fichero = (browser.file("files[]")).toString();
		//browser.table("tbl-fileupload").style("style=\"\"");
		//browser.row("filerow-template").style("style=\"\"");

		//System.out.pr

		
		/*
		browser.link("Añadir archivos").click();
		browser.file("files[]").setFile("");
		browser.submit("Subir[2]").click();
		*/
	/*
		browser.italic("icon-trash").click();
		browser.expectConfirm("Está seguro de que desea borrar el archivo \"promo_enero_email con errores expresos.csv\"", true);
	*/
		
	}


	/**
	 *En la herramienta grafica 'lapp-groupsender', programa un envio. Dicho envio requerira ser iniciado para que se envie. Este metodo unicamente lo prepara.
	 * @param contenido Nombre de la plantilla que se va a utilizar, tanto predefinida, como volatil, como tambien podria utilizar se como texto fijo del mensaje
	 * @param canales Marcara los checkbox para que los iNOT utilicen preferencia de canal. Este parametro es simplemente un string en el que se esperan valores PNS,SMS,Tweet,Mail
	 * @param planificacion Marcara los dias de la semana L,M,X,J,V,S,D 
	 * @param prioridad Prioridad que se va a seleccionar en el el desplegable correspondiente
	 * @author xruizs
	 */
	@Step("ProgramarEnvio <> <> <> <>")
	public void programarEnvio(String contenido, String canales, String planificacion, String prioridad) throws Exception {
		String archivo = datosGlobales.get(Constantes.ARCHIVO).toString();
		
		int randomNum = scenarioUtil.randInt(1,99999); //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido de este campo, asi podre encontralo en Estadisticas
		datosGlobales.put(Constantes.RANDOM_NUM, randomNum); //guardo el valor del 'randomNum' generado para utilizarlo despues en las búsquedas
		
		browser.link("Programar envío").click();
		
		//Identificación del envío
		browser.textbox("batchname").setValue("Paquete-Automated_" + randomNum);
		browser.textbox("batchlabel").setValue("Label-Automated_" + randomNum);
		
		
		//Selección de destinatarios
		if (browser.radio(archivo).exists()){
			browser.radio(archivo).click();
		} else {
			logger.error("No se encuentra el Archivo '"+ archivo + "'. Comprueba que existe en el repositorio");
			throw new Exception("");
		}
	
		//Canales de envío
		if (!canales.equalsIgnoreCase("--")) { browser.radio("channelContractConfig").click(); }
		if (canales.contains("PNS")){ browser.checkbox("batchchannel[][0]").in(browser.div("channel-list")).check(); }
		if (canales.contains("SMS")){ browser.checkbox("batchchannel[][1]").in(browser.div("channel-list")).check(); }
		if (canales.contains("Tweet")){ browser.checkbox("batchchannel[][2]").in(browser.div("channel-list")).check(); }
        if (canales.contains("EMail")){ browser.checkbox("batchchannel[][3]").in(browser.div("channel-list")).check(); }

		//MENSAJE Selecciona el tipo de contenido a usar, dependiendo del nombre que le hayamos pasado como parametro.
		if (contenido.substring(contenido.length()-4, contenido.length()).equalsIgnoreCase(".zip")){ //Mensaje contenido en plantillas volatiles '.ZIP'	
			browser.radio("contentModeTZ").click();
            browser.file("templateZip").setFile2("C:\\Testing\\Gauge_Projects\\Util\\MaterialGroupSender\\" + contenido);
		} else if (contenido.contains(" - ")){ //Mensaje contenido plantillas predefinidas
			browser.radio("contentModePT").click();
			browser.select("publictemplate").choose("/refPlantilla0/");
			browser.select("privatetemplate").choose("/refPlantilla0/");
		} else { //Mensaje contenido fijo
			browser.radio("contentModeFM").click();
			browser.textbox("fixedMessage").setValue("Automated GroupSender Texto fijo " + contenido + " " + randomNum);
		}
			
		Thread.sleep(5000);
		
		//Planificacion
		if (!planificacion.equalsIgnoreCase("--")){
			browser.radio("schedule-select").click();
			browser.span("Lunes").click();
			browser.span("Martes").click();
			browser.span("Miércoles").click();
			browser.span("Jueves").click();
			browser.span("Viernes").click();
			browser.span("Sábado").click();
			browser.span("Domingo").click();
			
			browser.select("intervalIni").choose("09:00");
			browser.select("intervalEnd").choose("20:00");
			
			browser.radio("retryDays[1]").click();
			
			browser.italic("icon-calendar[0]").click();
			browser.button("Hoy").click();

			browser.italic("icon-calendar[1]").click();
			browser.button("Hoy").click();
			browser.button("Cerrar").click();
			browser.italic("icon-trash[1]").click();
		}
		
		//Prioridad
		if (!prioridad.equalsIgnoreCase("--")){ //En caso de haberle asignado un valor a 'prioridad', se selecciona dicho valor (del -1 al 8)
			browser.select("priority").choose(prioridad);
		}
		browser.div("Preparar envío").click();
	}
	
	
	
	/**
	 * Prueba una instalacion de GroupSender con el Monitor Version 1.0.0
	 * Inicia el ultimo envio programado que fue Programado. Utiliza para identificarlo el ultimo 'randomNum' que se generado por el metodo 'programarEnvio'
	 * @param checkprogress Si vale distinto de "--", entonces el proceso se detiene a chequear que el envio llegue al 100%
	 * @author xruizs
	 */
	@Step("EnviosProgramadosR100 <>")
	public void enviosProgramadosR100(String checkprogress) throws Exception {
		String archivo = datosGlobales.get("archivo").toString();
		String randomNum = "vacio";
		String host = datosGlobales.get(Constantes.HOST).toString();
		String port = datosGlobales.get(Constantes.PORT).toString();
		String urlMM = "http://" + host + ":" + port + "/lman-massmonitor/index.jsp";
		String urlGS = "http://" + host + ":" + port + "/lapp-groupsender/index.jsp?tab=2";
		
		browser.link("Envíos programados").click();
		try{
			randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString(); //Es el numero random que guardamos anteriormente para poder encontrarlo ahora
		}catch(Exception e) {
			logger.error("No se encuentra 'randomNum'. Revisa que se ejecuto previamente el 'ProgramarEnvio'");
			throw new Exception ("");
		}
		
		if (randomNum.equals("vacio")){
			logger.error("No se encuentra el envio programado. Problema con el randomNum que se utilizo en la composicion del envio.");
			throw new Exception("");
		}

		if (browser.div("Paquete-Automated_" + randomNum).exists()){
			logger.info("El envio 'Paquete-Automated_" + randomNum + "' existe");
			
			boolean iniciar = false;
			int segundos = 1;
			int recarga = 0;
			
			//Chekeo que el boton INICIAR aparece para el lote. A saber que el boton INICIAR aparece cuando se ha terminado de cargar el lote.
			while (iniciar==false){
				if (browser.button("Iniciar").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
					logger.info("INICIADO 'Paquete-Automated_" + randomNum + "'");
					iniciar = true;
			
				} else{ //Si no existe el boton INICIAR es porque (A)aun esta cargando o (B)porque la carga ha resultado erronea
					if (segundos == 1) logger.info("Esperando INICIAR 'Paquete-Automated_\" + randomNum + \"'");//System.out.println("INF: Esperando INICIAR 'Paquete-Automated_" + randomNum + "'");
					
					if (segundos>=100) { //Esto es un timeOut por si una carga no termina en el tiempo indicado, entonces termino el programa
						logger.error("Timeout cargando 'Paquete-Automated_" + randomNum + "' tarda demasiado. Se ha terminado la prueba");

						throw new Exception("");
					}
					
					Thread.sleep(1000); //Espero para dar tiempo a que se muestre el boton INICIAR
					if (segundos == 1) {
						logger.info("Esperando " + segundos + ",");
					} else {
						logger.info(segundos + ",");
					}
					segundos++;
					
					recarga++; //Voy recargando la pagina cada 5sec hasta que aparezca el boton INICIAR
					if (recarga>=5){
						String url = browser.link("Envíos programados").getAttribute("href");
						browser.navigateTo(url, true);
					}
					
					if (browser.div("FINALIZADO con ERROR").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
						logger.error("La carga de 'Paquete-Automated_" + randomNum + "' ha finalizado en error");
						throw new Exception("");
					}
				}
				
			}
				
			//Compruebo la existencia de los datos esperados en la información del lote.
			//Archivo CSV cargado:
			if (browser.div("/" + archivo + "/").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
				logger.info("Campo Archivo '" + archivo +"' asignado");

			} else {
				logger.error("No se encuentra el archivo '" + archivo + "' en 'Paquete-Automated_" + randomNum + "'");
				throw new Exception("");
			}
			//Etiqueta:
			if (browser.div("/Label-Automated_" + randomNum +"/").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
				logger.info("Campo Etiqueta 'GroupSender " + randomNum +"' asignado");
			} else {
				logger.error("No se encuentra el campo Etiqueta 'Label-Automated_" + randomNum +"'");
				throw new Exception("");
			}
			//Estado CARGA FINALIZADA:
			if (browser.div("/CARGA FINALIZADA/").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
				logger.info("Campo Estado 'CARGA FINALIZADA' OK");

			} else {
				logger.error("No se encuentra el campo Estado: 'CARGA FINALIZADA' en 'Alerta Automated " + randomNum + "'");
				throw new Exception("");
			}
			
			
			//Voy a la herramienta MM de 'Batch Monitor' para comprobar que el lote esta cargado y sin iniciarse		
			browser.navigateTo(urlMM);
			if (browser.div("CARGADO").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
				logger.info("Monitor de Carga 'Paquete-Automated_" + randomNum + "' CARGADO");
			} else {
				logger.error("No se encuentra en Monitor de Carga 'Paquete-Automated_" + randomNum + "'");
				throw new Exception("");
			}
			
			
			
			//Regreso a GS para INICIAR el lote cargado anteriormente. Podria iniciarse tambien desde el propio MM, pero en esta prueba lo hacemos desde GS
			browser.navigateTo(urlGS);
			browser.button("Iniciar").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).click();
			logger.info("Iniciando envio de 'Paquete-Automated_" + randomNum + "'");
			
			//Estado 'PROCESANDO BATCH':
			boolean procesando = false;
			segundos = 1;
			while (procesando==false){
				if (browser.div("/PROCESANDO BATCH/").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
					procesando=true;
					logger.info("'Paquete-Automated_" + randomNum + "' 'PROCESANDO BATCH' OK");

					
				} else {				
				
					if (segundos == 1)  logger.info("Esperando el procesado de 'Paquete-Automated_\" + randomNum + \"'");//System.out.println("INF: Esperando el procesado de 'Paquete-Automated_" + randomNum + "'");
					
					if (segundos>=100) { //Esto es un timeOut por si el procesado no termina en el tiempo indicado, entonces termino el programa
						logger.error("Timeout procesando. El iniciado 'Paquete-Automated_" + randomNum + "' tarda demasiado. Se ha terminado la prueba");
						throw new Exception("");
					}
					
					Thread.sleep(1000); //Espero para dar tiempo a que se muestre el mensaje de PROCESANDO BATCH
					if (segundos == 1) {
						logger.info("Esperando " + segundos + ",");
					} else {
						logger.info(segundos + ",");
					}
					segundos++;
				}
				
			}	
			
			if (!checkprogress.equalsIgnoreCase("--")){ //Si le pasamos el parametro, entonces el proceso se detiene a chequear que el envio llegue al 100%
				//Voy por segunda vez a la herramienta de 'Batch Monitor' (lman-massmonitor). Esta vez para comprobar que el envio se realiza al 100%		
				browser.navigateTo(urlMM);
				browser.select("updateRate").choose("5"); //Activo el refresco del GUI. Si no funcionase seria un BUG


				boolean completado = false;
				segundos = 1;
	
				while (!completado){
					
					String progreso = (browser.span("percent[0]").under(browser.div("Paquete-Automated_" + randomNum))).getText();
					if (browser.span("percent[0]").under(browser.div("Paquete-Automated_" + randomNum)).containsText("100")){
						logger.info("Progreso 100% ENVIO FINALIZADO 'Paquete-Automated_" + randomNum + "");
						completado = true;
						
					} else {				
					
						if (segundos == 1) logger.info("Se esta enviando 'Paquete-Automated_\" + randomNum + \"'");//System.out.println("INF: Se esta enviando 'Paquete-Automated_" + randomNum + "'");
						if (segundos%30==0){ //Cada 30 segundos baja de linea. Solo confines visuales en la consola
							logger.info("");

						}
						if (segundos>=600) { //Esto es un timeOut por si el procesado no termina en el tiempo indicado, entonces termino el programa
							browser.button("Cancelar").under(browser.div("Paquete-Automated_" + randomNum)).click();
							logger.error("Timeout despues de " + segundos + ". CANCELADO 'Paquete-Automated_" + randomNum + "'");
							throw new Exception("");
						}
						
						Thread.sleep(1000); //Espero 1sec para dar tiempo a que se muestre el mensaje de PROCESANDO BATCH
						if (segundos == 1) {
							logger.info("Esperando " + segundos + ",");
						} else {
							if (segundos%30==0){ //Cada 30 segundos saca el contador de progreso. (http://lineadecodigo.com/java/multiplo-de-un-numero-en-java)
								logger.info("Progreso=" + progreso + "%");

							}
							logger.info(segundos + ",");
						}
						segundos++;
					}
					
				}
				browser.navigateTo(urlGS); //Regreso a GroupSender
			}
			
			
		} else {
			logger.error("No se encuentra el envio generado.");
			throw new Exception("");
		}
		
		
		

	}


	/**
	 * Prueba una instalacion de GroupSender con el Monitor Version 2.1.0
	 * Inicia el ultimo envio programado que fue Programado. Utiliza para identificarlo el ultimo 'randomNum' que se generado por el metodo 'programarEnvio'
	 * @param checkprogress Si vale distinto de "--", entonces el proceso se detiene a chequear que el envio llegue al 100%
	 * @author xruizs
	 */
	@Step("EnviosProgramadosR210 <>")
	public void enviosProgramadosR210(String checkprogress) throws Exception {
		String archivo = datosGlobales.get(Constantes.ARCHIVO).toString();
		String randomNum = "vacio";
		String host = datosGlobales.get(Constantes.HOST).toString();
		String port = datosGlobales.get(Constantes.PORT).toString();
		String urlMM = "http://" + host + ":" + port + "/lman-massmonitor/index.jsp";
		String urlGS = "http://" + host + ":" + port + "/lapp-groupsender/index.jsp?tab=2";
		
		browser.link("Envíos programados").click();
		try{
			randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString(); //Es el numero random que guardamos anteriormente para poder encontrarlo ahora
		}catch(Exception e) {
			logger.error("No se encuentra 'randomNum'. Revisa que se ejecuto previamente el 'ProgramarEnvio'");
			throw new Exception ("");
		}
		
		if (randomNum.equals("vacio")){
			logger.error("No se encuentra el envio programado. Problema con el randomNum que se utilizo en la composicion del envio.");
			throw new Exception("");
		}

		if (browser.div("Paquete-Automated_" + randomNum).exists()){
			logger.info("El envio 'Paquete-Automated_" + randomNum + "' existe");
			
			boolean iniciar = false;
			int segundos = 1;
			int recarga = 0;
			long horainicio = 0;
			long horafin = 0;
			
			//Chekeo que el boton INICIAR aparece para el lote. A saber que el boton INICIAR aparece cuando se ha terminado de cargar el lote.
			while (iniciar==false){
				if (browser.button("Iniciar").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
					logger.info("INICIADO 'Paquete-Automated_" + randomNum + "' a las " + DateFormatUtils.format(horainicio, "HH:mm:ss", TimeZone.getTimeZone("Europe/Madrid")));
					horainicio = System.currentTimeMillis();

					iniciar = true;

				} else{ //Si no existe el boton INICIAR es porque (A)aun esta cargando o (B)porque la carga ha resultado erronea
					if (segundos == 1) logger.info("Esperando INICIAR 'Paquete-Automated_\" + randomNum + \"'");//System.out.println("INF: Esperando INICIAR 'Paquete-Automated_" + randomNum + "'");
					
					if (segundos>=100) { //Esto es un timeOut por si una carga no termina en el tiempo indicado, entonces termino el programa
						logger.error("Timeout cargando 'Paquete-Automated_" + randomNum + "' tarda demasiado. Se ha terminado la prueba");
						throw new Exception("");
					}
					
					Thread.sleep(1000); //Espero para dar tiempo a que se muestre el boton INICIAR
					if (segundos == 1) {
						logger.info("Esperando " + segundos + ",");
					} else {
						logger.info(segundos + ",");
					}
					segundos++;
					
					recarga++; //Voy recargando la pagina cada 5sec hasta que aparezca el boton INICIAR
					if (recarga>=5){
						String url = browser.link("Envíos programados").getAttribute("href");
						browser.navigateTo(url, true);
					}
					
					if (browser.div("FINALIZADO con ERROR").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
						logger.error("La carga de 'Paquete-Automated_" + randomNum + "' ha finalizado en erro");
						throw new Exception("");
					}
				}
				
			}
				
			//Compruebo la existencia de los datos esperados en la información del lote.
			//Archivo CSV cargado
			if (browser.div("/" + archivo + "/").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
				logger.info("Campo Archivo '" + archivo +"' asignado");
			} else {
				logger.error("No se encuentra el archivo '" + archivo + "' en 'Paquete-Automated_" + randomNum + "'");
				throw new Exception("");
			}
			//Etiqueta:
			if (browser.div("/Label-Automated_" + randomNum +"/").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
				logger.info("Campo Etiqueta 'GroupSender " + randomNum +"' asignado");
			} else {
				logger.error("No se encuentra el campo Etiqueta 'Label-Automated_" + randomNum +"'");
				throw new Exception("");
			}
			//Estado CARGA FINALIZADA:
			if (browser.div("/CARGA FINALIZADA/").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
				logger.info("Campo Estado 'CARGA FINALIZADA' OK");
			} else {
				logger.error("No se encuentra el campo Estado: 'CARGA FINALIZADA' en 'Alerta Automated " + randomNum + "'");
				throw new Exception("");
			}
			
			
			//Voy a la herramienta MM de 'Batch Monitor' para comprobar que el lote esta cargado y sin iniciarse		
			browser.navigateTo(urlMM);
			//if (browser.div("CARGADO").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists(true)){
			if (browser.div("CARGADO").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
				logger.info("Monitor de Carga 'Paquete-Automated_" + randomNum + "' CARGADO");
			} else {
				logger.error("No se encuentra en Monitor de Carga 'Paquete-Automated_" + randomNum + "'");
				throw new Exception("");
			}
			
			
			
			//Regreso a GS para INICIAR el lote cargado anteriormente. Podria iniciarse tambien desde el propio MM, pero en esta prueba lo hacemos desde GS
			browser.navigateTo(urlGS);
			browser.button("Iniciar").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).click();
			logger.info("Iniciando envio de 'Paquete-Automated_" + randomNum + "'");
						
			//Estado 'PROCESANDO BATCH':
			boolean procesando = false;
			segundos = 1;
			while (procesando==false){
				if (browser.div("/PROCESANDO BATCH/").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
					procesando=true;
					logger.info("'Paquete-Automated_" + randomNum + "' 'PROCESANDO BATCH' OK");

					
				} else {				
				
					if (segundos == 1) logger.info("Esperando el procesado de 'Paquete-Automated_\" + randomNum + \"'");//System.out.println("INF: Esperando el procesado de 'Paquete-Automated_" + randomNum + "'");
					
					if (segundos>=100) { //Esto es un timeOut por si el procesado no termina en el tiempo indicado, entonces termino el programa
						logger.error("Timeout procesando. El iniciado 'Paquete-Automated_" + randomNum + "' tarda demasiado. Se ha terminado la prueba");
						throw new Exception("");
					}
					
					Thread.sleep(1000); //Espero para dar tiempo a que se muestre el mensaje de PROCESANDO BATCH
					if (segundos == 1) {
						logger.info("Esperando " + segundos + ",");
					} else {
						logger.info(segundos + ",");
					}
					segundos++;
				}
				
			}	
			

			if (!checkprogress.equalsIgnoreCase("--")){ //Si le pasamos el parametro, entonces el proceso se detiene a chequear que el envio llegue al 100%
				//Voy por segunda vez a la herramienta de 'Batch Monitor' (lman-massmonitor). Esta vez para comprobar que el envio se realiza al 100%		
				browser.navigateTo(urlMM);
				browser.italic("icon-refresh").click(); //Activo el refresco del GUI. Si no funcionase seria un BUG
				boolean completado = false;
				segundos = 1;
				
				String num1inotprocesadasactual = "";
				String num2inotprocesadastotal = "";
				String num3mensajesenviadosactual = "";
				String num4mensajesenviadostotal = "";

				while (!completado){
					String progreso = browser.span("percent").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).getText();
					if (browser.span("percent").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).containsText("100")){
						segundos = 1;
						logger.info("Progreso 100% 'Paquete-Automated_" + randomNum + "'. Esperando FINALIZACION del lote.");

						
						num1inotprocesadasactual = browser.div("num1[0]").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).getText();
						num2inotprocesadastotal = browser.div("num2[0]").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).getText();
						num3mensajesenviadosactual = browser.div("num1[1]").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).getText();
						num4mensajesenviadostotal = browser.div("num2[1]").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).getText();
						
						num2inotprocesadastotal = num2inotprocesadastotal.substring(3);
						num4mensajesenviadostotal = num4mensajesenviadostotal.substring(3);
						
						if (num2inotprocesadastotal.equals("10000")){ //solo verifico los contadores para el envio de 10000 que es el unico que considero debe coincidir
							if ((num1inotprocesadasactual.equals(num2inotprocesadastotal)) && (num2inotprocesadastotal.equals(num3mensajesenviadosactual)) && (num3mensajesenviadosactual.equals(num4mensajesenviadostotal))){
								logger.info("Los contadores coinciden");
								logger.info("iNOT procesadas: " + num1inotprocesadasactual + " de " + num2inotprocesadastotal);
								logger.info("Mensajes enviados: " + num3mensajesenviadosactual + " de " + num4mensajesenviadostotal);
							} else{
								logger.error("Paquete-Automated_" + randomNum + " esta al 100% pero los contadores no coinciden. Revisa que sucede");
								logger.error("iNOT procesadas: " + num1inotprocesadasactual + " de " + num2inotprocesadastotal);
								logger.error("Menasjes enviados: " + num3mensajesenviadosactual + " de " + num4mensajesenviadostotal);
								throw new Exception("");
							}
						}
						
						while ((!browser.div("FINALIZADO").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists())&&(segundos<60)){
							browser.link("Actualizar").click();
							Thread.sleep(1000);
							segundos++;
							if (segundos>=99){
								logger.error("Paquete-Automated_\" + randomNum + \" esta al 100% pero no indica FINALIZADO. Revisa que sucede");
								throw new Exception("");
							}
						}
						horafin = System.currentTimeMillis();
						logger.info("Progreso 100% 'Paquete-Automated_" + randomNum + "'. ENVIO FINALIZADO a las " + DateFormatUtils.format(horafin, "HH:mm:ss", TimeZone.getTimeZone("Europe/Madrid")));
						logger.info("Ha tardado: " + DateFormatUtils.format((horafin - horainicio), "HH:mm:ss") + "");
						completado = true;
	
					} else {				
						
						if (segundos == 1) logger.info("Se esta enviando 'Paquete-Automated_\" + randomNum + \"'");//System.out.println("INF: Se esta enviando 'Paquete-Automated_" + randomNum + "'");
						if (segundos%30==0){ //Cada 30 segundos baja de linea. Solo confines visuales en la consola

							browser.link("Actualizar").click();
							if (!browser.div("PROCESANDO").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
								logger.error("El Paquete-Automated_" + randomNum + " no se esta procesando despues de " + segundos + ". Revisa que sucede");
								throw new Exception("");
							}
						}
						int timeout = 600;
						if (browser.div("num2").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).containsText("10000")){
							timeout = 3600; // establezco el timeout, solo en el caso que este procesando un envio de 10.000 mensajes
						}
						if (segundos>=timeout) { //Esto es un timeOut por si el procesado no termina en el tiempo indicado, entonces termino el programa
							browser.italic("icon-remove").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).click();
							browser.expectConfirm("¿Desea cancelar todo el envío?", true);

							browser.link("Actualizar").click();
							logger.error("Timeout despues de " + segundos + ". CANCELANDO 'Paquete-Automated_" + randomNum + "' porque tarda mucho en procesar");
							Thread.sleep(2000); //Espero 2sec para dar tiempo a que se muestre el mensaje de CANCELADO
							if (!browser.div("CANCELADO").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists()){
								logger.error("Tras haber cancelado el 'Paquete-Automated_" + randomNum + "' no se muestra CANCELADO en MONITOR. Revisa que sucede");
							}
							throw new Exception("");
						}
						
						Thread.sleep(1000); //Espero 1sec para dar tiempo a que se muestre el mensaje de PROCESANDO BATCH
						if (segundos == 1) {
							if (timeout>=7200) {
								logger.info("Esperando (saltara Timeout en " + timeout/3600 + "Horas). Contando segundos: " + segundos + ",");
							} else {
								logger.info("Esperando (saltara Timeout en " + timeout + "seg). Contando segundos: " + segundos + ",");
							}
							
						} else {
							if (segundos%30==0){ //Cada 30 segundos saca el contador de progreso. (http://lineadecodigo.com/java/multiplo-de-un-numero-en-java)
								logger.info("Progreso=" + progreso + " ");

							}						
							logger.info(segundos + ",");

						}
						segundos++;
					}
					
				}
				
				String fcreacion = browser.div("date").under(browser.div("Paquete-Automated_" + randomNum)).getText();
				
				//Voy al apartado del detalle del envio, donde compruebo  la existencia de los datos esperados en la informacion del lote.
				browser.italic("icon-fullscreen").rightOf(browser.div("Paquete-Automated_" + randomNum).parentNode()).click();

				//Comprueba que estoy dentro del lote que debo estar
				String description = browser.span("description").getText(); //Recojo el valor del campo descripcion, para compararlo con lo que deberia haber
				if (description.equals("Paquete-Automated_" + randomNum)){
					logger.info("Campo descripcion: '" + description +"' OK");
				} else {
					logger.error("No se encuentra el campo '" + description + "' para detalle del 'Paquete-Automated_" + randomNum + "'");
					throw new Exception(" ");
				}
				
				//Fecha de creacion
				String fcreaciondetalle = browser.span("ts-create").getText(); //Recojo el valor del campo descripcion, para compararlo con lo que deberia haber
				if (fcreaciondetalle.equals(fcreacion)){
					logger.info("Campo ts-create: '" + fcreaciondetalle +"' OK");
				} else {
					logger.error("No se encuentra el campo '" + fcreaciondetalle + "' para detalle del 'Paquete-Automated_" + randomNum + "'");
					throw new Exception(" ");
				}
				
				//ESTADO 
				if (browser.span("Envío finalizado").exists()){
					logger.info("Campo ESTADO: 'Envío finalizado' OK");
				} else {
					logger.error("El campo ESTADO no es 'Envío finalizado");
					throw new Exception(" '");
				}
				
				//Archivo CSV cargado:
				if (browser.span("/" + archivo + "/").exists()){
					logger.info("Campo Fichero: '" + archivo +"' asignado OK");
				} else {
					logger.error("No se encuentra el Fichero '" + archivo + "' en 'Paquete-Automated_" + randomNum + "'");
					throw new Exception(" ");
				}
				
				//Contadores del envio. Recojo los valores en pantalla y los comparo con los valores que recogi anteriormente en el listado general
				String mmsgSent = browser.span("mmsgSent").getText();
				String mmsgTotal = browser.span("mmsgTotal").getText();
				String dmsgSent = browser.span("dmsgSent").getText();
				String dmsgStored = browser.span("dmsgStored").getText();
				if (mmsgSent.equals(num1inotprocesadasactual) && mmsgTotal.equals(num2inotprocesadastotal) && dmsgSent.equals(num3mensajesenviadosactual) && dmsgStored.equals(num4mensajesenviadostotal)){
					logger.info("Los contadores coinciden");
					logger.info("    iNOT procesadas: " + num1inotprocesadasactual + " de " + num2inotprocesadastotal);
					logger.info("    Mensajes enviados: " + num3mensajesenviadosactual + " de " + num4mensajesenviadostotal);
				} else{
					logger.error("Paquete-Automated_" + randomNum + " esta al 100% pero los contadores no coinciden. Revisa que sucede");
					logger.error("    iNOT procesadas: " + num1inotprocesadasactual + " de " + num2inotprocesadastotal);
					logger.error("    Mensajes enviados: " + num3mensajesenviadosactual + " de " + num4mensajesenviadostotal);
					throw new Exception(" ");
				}
				
				//Porcentaje:
				if (browser.span("percent").containsText("100")){
					logger.info("Campo progreso 100%: OK");
				} else {
					logger.error("Campo progreso no es 100% en detalle 'Paquete-Automated_" + randomNum + "'");
					throw new Exception(" ");
				}
				
				//Dias semana:
				if (browser.span("Lunes, Martes, Miercoles, Jueves, Viernes, Sábado, Domingo").in(browser.div("planning")).exists()){
					logger.info("Campo Dias semana: 'Lunes, Martes, Miercoles, Jueves, Viernes, Sábado, Domingo': OK");
				} else {
					logger.info("No se encuentra campo Dias semana: 'Lunes, Martes, Miercoles, Jueves, Viernes, Sábado, Domingo' en detalle 'Paquete-Automated_" + randomNum + "'");
					throw new Exception(" ");
				}
				
				//Intervalo horario:
				if (browser.span("09:00-20:00").in(browser.div("planning")).exists()){
					logger.info("Campo Intervalo horario: '09:00-20:00': OK");
				} else {
					logger.info("No se encuentra campo Intervalo horario: '09:00-20:00' en detalle 'Paquete-Automated_" + randomNum + "'");
					throw new Exception(" ");
				}
				
				//Referencia de contrato:
				if (browser.span("#lapp-groupsender").in(browser.div("contractData")).exists()){
					logger.info("Campo Referencia: '#lapp-groupsender': OK");
				} else {
					logger.error("No se encuentra campo Referencia: '#lapp-groupsender' en detalle 'Paquete-Automated_" + randomNum + "'");
					throw new Exception(" ");
				}
				
				//Producto:
				if (browser.span("Group Sender").in(browser.div("contractData")).exists()){
					logger.info("Campo Producto: 'Group Sender': OK");
				} else {
					logger.error(" No se encuentra campo Producto: 'Group Sender' en detalle 'Paquete-Automated_" + randomNum + "'");
					throw new Exception(" ");
				}
				
				//*****VOY POR AQUI, ESTOY ESPERANDO QUE RG LE PONGA UNA 'CLASS' A ESTE CAMPO
				//Etiqueta:
				/*if (browser.div("/Label-Automated_" + randomNum +"/").in(browser.div("Paquete-Automated_" + randomNum).parentNode()).exists(true)){
					System.out.println("INF: Campo Etiqueta 'GroupSender " + randomNum +"' asignado");
				} else {
					System.out.println("ERR No se encuentra el campo Etiqueta 'Label-Automated_" + randomNum +"'");
					throw new Exception("ERR No se encuentra el campo Etiqueta 'Label-Automated_" + randomNum +"'");
				}*/
				
				
				browser.navigateTo(urlGS); //Regreso a GroupSender
			}
			
			
		} else {
			logger.error("No se encuentra el envio generado.");
			throw new Exception(" ");
		}
		
		
		

	}

}