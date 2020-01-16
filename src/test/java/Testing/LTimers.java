package Testing;// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LTimers {
	
	DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
	private static Logger logger = LogManager.getLogger(LTimers.class);

	public LTimers() {

	}
	
	@Step("ldmcontable <nomproceso>")
	public void ldmcontable(String nomproceso) throws Exception {
		String host = datosGlobales.get(Constantes.HOST).toString();
		String port = datosGlobales.get(Constantes.PORT).toString();
		nomproceso = nomproceso.toLowerCase();
		String myurl = null;
		String user = "limsp3.contable";
		String pass = Constantes.LATINIA;
		
		//Establece la URL apropiada
		if (nomproceso.equalsIgnoreCase(("histAndSummary"))){
			myurl = "http://" + host + ":"+port+"/ld-mcontable/histAndSummary/";
		}
		
		if (nomproceso.equalsIgnoreCase("consolidatestates")){
			myurl = "http://" + host + ":"+port+"/ld-mcontable/consolidate?mode=STATES";
		}
		
		if (nomproceso.equalsIgnoreCase("consolidatedb")){
			myurl = "http://" + host + ":"+port+"/ld-mcontable/consolidate?mode=DB";			
		}
		
		if (nomproceso.equalsIgnoreCase("consolidateall")){
			myurl = "http://" + host + ":"+port+"/ld-mcontable/consolidate?mode=ALL";
		}

		if (nomproceso.equalsIgnoreCase("filereader")){
			myurl = "http://" + host + ":"+port + "/filereader/sendFiles";
		}

		if (nomproceso.equalsIgnoreCase("filereadermass")){
			myurl = "http://" + host + ":"+port + "/filereadermassive/run";
		}
		
		//Invoca la ejecucion de la URL indicada
		//Boolean resultado = LatiniaScenarioUtil.conectaURL(myurl, user, pass);
		Boolean resultado;
		resultado = LatiniaScenarioUtil.conectaURL(myurl, user, pass);

		if (resultado){
			logger.info("Invocacion de '" + nomproceso + "' correcta. URL=" + myurl + "\n");
		}

}
	
	
	public void lservdelivery() throws Exception {
		String host = datosGlobales.get(Constantes.HOST).toString();
		String port = datosGlobales.get(Constantes.PORT).toString();
		String myurl = "http://" + host + ":" + port + "/lserv-delivery/execute";
		String user = "limsp3.bus";
		String pass = Constantes.LATINIA;
						
		//Invoca la ejecucion de la URL indicada
		Boolean resultado = LatiniaScenarioUtil.conectaURL(myurl, user, pass);
		
		if (resultado){
			logger.info("Invocacion de 'clearGuiUsers' correcta. URL=" + myurl + "\n");
		}
		
	}
	
	@Step("lpmaintenance <>")
	public void lpmaintenance(String nomproceso) throws Exception {
		String host = datosGlobales.get(Constantes.HOST).toString();
		String port = datosGlobales.get(Constantes.PORT).toString();
		nomproceso = nomproceso.toLowerCase();
		String myurl = null;
		String user = "limsp3.maintainer";
		String pass = Constantes.LATINIA;
		String date;
		//Establece de la URL apropiada
		if (nomproceso.equalsIgnoreCase("clearBinary")){
			date = date("MES", -6);
			myurl = "http://" + host + ":"+port+"/lp-maintenance/clearBinary?tfOld="+date;//***********OJO!!!***//
			user = "limsp3.maintainer";
		}
		
		if (nomproceso.equalsIgnoreCase("clearHistory")){
			date = date("MES", -6);
			myurl = "http://" + host + ":"+port+"/lp-maintenance/clearHistory?tsOld="+date;
			//myurl = "http://" + host + ":"+port+"/lp-maintenance/clearHistory?tfOld="+date;
		}
		
		if (nomproceso.equalsIgnoreCase("clearCahe")){
			date = date("DIA", -1);
			myurl = "http://" + host + ":"+port+"/lp-maintenance/clearCache?tfOld="+date;
		}

		if (nomproceso.equalsIgnoreCase(("clearInf").toLowerCase())){
			date = date("DIA", -5);
			myurl = "http://" + host + ":"+port+"/lp-maintenance/clearInf?tfOld="+date;
		}

		if (nomproceso.equalsIgnoreCase("clearPrivateContent")){
			date = date("MES", -6);
			myurl = "http://" + host + ":"+port+"/lp-maintenance/clearPrivateContent?tsOld="+date;
			//myurl = "http://" + host + ":"+port+"/lp-maintenance/clearPrivateContent?tfOld="+date;
		}
	
		if (nomproceso.equalsIgnoreCase("clearGuiUsers")){
			myurl = "http://" + host + ":"+port+"/lp-maintenance/clearGuiUsers";
		}
		
		
		//Invoca la ejecucion de la URL indicada
		Boolean resultado = LatiniaScenarioUtil.conectaURL(myurl, user, pass);
		
		if (resultado){
			logger.info("Invocacion de '" + nomproceso + "' correcta. URL=" + myurl + "\n");
		}
	}


	public static String date(String tiempo, int cantidad){
		Date ahora = new Date();


		Calendar calendar = Calendar.getInstance();

		calendar.setTime(ahora);

		if(tiempo.equalsIgnoreCase("MES")){
			calendar.add(Calendar.MONTH, cantidad);
		} else if(tiempo.equalsIgnoreCase("DIA")){
			calendar.add(Calendar.DAY_OF_MONTH,cantidad);
		}

		SimpleDateFormat formateador = new SimpleDateFormat("yyyyMMdd");
		String date = formateador.format(calendar.getTime());
		return date;
	}
	
	
	/* DEPRECADO DESDE LIMSP-SDP R3.9.5. Ya no se utilizan URL's para el envio de PNS masivos
	 * Proceso de BULK PNS o masivos de PNS. El gestor de masivos requiere ejecución de 2 url's para su funcionamiento
	 * 
	 * http://${bus-urlhttp-base}/busext/sendmassive
	 * >> La primera URL tiene la misión de entrar dentro del BUS los mensajes cargados como masivos.
	 *    Una vez enviados hacia el bus, estos mensajes son procesados y retenidos dentro de la base de datos.
	 *    Todos los mensajes enviados como masivos se retienen SIEMPRE como si fuesen mensajes con entrega diferida y gestionada por el BUS.
	 *    Estos mensajes ya deberían de aparecer en las estadísticas puesto que ya han sido enviados hacia el contable.
	 * 
	 * http://${bus-urlhttp-base}/lserv-bus/delivery?mode=2
	 * >> La segunda url se encarga de obtener los mensajes retenidos e iniciar el proceso de envío hacia los colectores.
	 *    Estos mensajes no se envían directamente a los colectores sino que se envían hacia la cola “toCollectors”, 
	 *    cola que gestiona el componente del control de flujo para cumplir con los requerimientos de licencia y no saturar o colapsar el BUS.
	 */
	/*
	 * public void lservmassive() throws Exception {
		String host = DatosGlobales.get("host").toString();
		String port = DatosGlobales.get("port").toString();
		String urlloadmassive = "http://" + host + ":" + port + "/busext/loadmassivexxx";
		String urlsendmassive = "http://" + host + ":" + port + "/busext/sendmassivexxx";
		String urldelivery2 = "http://" + host + ":" + port + "/lserv-bus/delivery?mode=2";
		
		String user = "limsp3.bus";
		String pass = "latinia";
				
		
		
		//Invoca la ejecucion de las URLs de envios de masivos, que son 3
		int segundos = 5;
		System.out.print("INF: Esperando " + segundos + " segundos para la ejecucion del Timer 'loadmassive': ");
		for (int i=1; i<=segundos; i++){
			Thread.sleep(1000); //Espero para dar tiempo a que se haya procesado internamente el mensaje, antes de invocar la ejecucion del Timer
			System.out.print(i + ",");
		}
		System.out.println("");
		Boolean resultadourlloadmassive = LatiniaScenarioUtil.conectaURL(urlloadmassive, user, pass);
		if (resultadourlloadmassive){
			System.out.println("INF: Invocacion de 'loadmassive' correcta. URL=" + resultadourlloadmassive + "\n");
		}
				
		//Segunda URL
		segundos = 5;
		System.out.print("INF: Esperando " + segundos + " segundos para la ejecucion del Timer 'sendmassive': ");
		for (int i=1; i<=segundos; i++){
			Thread.sleep(1000); //Espero para dar tiempo a que se haya procesado internamente el mensaje, antes de invocar la ejecucion del Timer
			System.out.print(i + ",");
		}
		System.out.println("");
		Boolean resultadourlsendmassive = LatiniaScenarioUtil.conectaURL(urlsendmassive, user, pass);
		if (resultadourlsendmassive){
			System.out.println("INF: Invocacion de 'sendmassive' correcta. URL=" + resultadourlsendmassive + "\n");
		}
		
		//Tercera URL
		segundos = 5;
		System.out.print("INF: Esperando " + segundos + " segundos para la ejecucion del Timer 'delivery?mode=2': ");
		for (int i=1; i<=segundos; i++){
			Thread.sleep(1000); //Espero para dar tiempo a que se haya procesado internamente el mensaje, antes de invocar la ejecucion del Timer
			System.out.print(i + ",");
		}
		System.out.println("");
		Boolean resultadourldelivery2 = LatiniaScenarioUtil.conectaURL(urldelivery2, user, pass);
		if (resultadourldelivery2){
			System.out.println("INF: Invocacion de 'delivery mode=2' correcta. URL=" + resultadourldelivery2 + "\n");
		}		

	}
	*/
	
}