package Testing;// JUnit Assert framework can be used for verification

import java.io.File;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TrafficTest {

	private Browser browser;

	DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
	private static Logger logger = LogManager.getLogger(TrafficTest.class);

	public TrafficTest() {
		browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}
	
	private LatiniaScenarioUtil scenarioUtil = new LatiniaScenarioUtil();

	/**
	 * 
	 * @param numMsg Numero de mensajes que TrafficTest inyectara al colector
	 * @param company 		Company a utilizar para la inyeccion
	 * @param contract 		Contrato a utilizar para la inyeccion
	 * @param refProduct 	refProduct a utilizar para la inyeccion
	 * @param escenario		Identificador del escenario que utilizaremos. Los escenarios se definen en "<path>/settings_escenarios.xml"
	 * @throws Exception
	 * @author xruizs
	 */

	@Step("TrafficTest1 <> <> <> <> <>")
	public void trafficTest1(String numMsg,String company,String contract,String refProduct,String escenario) throws Exception {
		logger.info("Entro aquí");
		String host = datosGlobales.get(Constantes.HOST).toString();
		String port = datosGlobales.get(Constantes.PORT).toString();
		
		int randomNum = scenarioUtil.randInt(1,99999); //Genero un 'randomNum' entre dos valores para utilizarlo a continuacion del contenido de este campo, asi podre encontralo en Estadisticas
		datosGlobales.put(Constantes.RANDOM_NUM, randomNum); //guardo el valor del 'randomNum' generado para utilizarlo despues en las búsquedas
		
		//String path = System.getenv("GAUGE_UNIDAD")+"\\Gauge\\Util\\TrafficTest"; //Se requiere la existencia de la variable de entorno "GAUGE_UNIDAD", que indica donde esta el ejecutable de TraficTest
        String path = System.getenv("GAUGE_UNIDAD")+"\\Testing\\Gauge_Projects\\Util\\TrafficTest"; //Se requiere la existencia de la variable de entorno "GAUGE_UNIDAD", que indica donde esta el ejecutable de TraficTest
		String randomNumstr = String.valueOf(randomNum); //Este numero random se utiliza para localizar en estadisticas el mensaje que se ha inyectado
		ProcessBuilder pb = new ProcessBuilder(path + "\\execute.cmd",host,port,numMsg,company,contract,refProduct,randomNumstr,escenario);
				
		pb.directory(new File(path));
		final long tini = System.currentTimeMillis();

		logger.info("Arrancando TrafficTest con los siguientes parametos..");
		logger.info(" >> Directorio de ejecucion: " + pb.directory());
		logger.info(" >> HOST = " + host);
		logger.info(" >> PORT = " + port);
		logger.info(" >> Numero de mensajes = " + numMsg);
		logger.info(" >> Company = " + company);
		logger.info(" >> refContract = " + contract);
		logger.info(" >> refProduct = " + refProduct);
		logger.info(" >> CASO DE TEST = " + escenario);
		logger.info(" >> RANDOM DE CONTROL = " + randomNumstr);

				
		final Process proceso1 = pb.start();
		logger.info("Iniciado TrafficTest en: "+ ((System.currentTimeMillis() - tini) + " ms. Relajate mientras termina la ejecucion......."));


		
		//Hago un bucle para mostrar por consola un contador del tiempo que estoy esperando hasta que termine la ejecucion del proceso TrafficTest
		logger.info("Esperando para la ejecucion de 'TrafficTest' (segundos):");

		int i = 1;
		int pgactual = 1;
		int pganterior = 1;
		int proceso1status = 1;
		while (proceso1status != 0){
			Thread.sleep(1000); //Espero hasta que se termine el 'proceso1' que es el TraficTesting
			logger.info(i + ",");
			i++;
			
			//Esto es para inyecciones grandes, de manera que cada 40 segundos hago retorno de carro para que se vea bien en la consola
			if (i >= (pgactual + (40*pgactual))) {
				pgactual++;
			}
			if (pgactual>pganterior){
				System.out.println("");
				pganterior=pgactual;
			}
				
			//Aqui voy comprobando si el proceso de TrafficTest ha terminado ya, para detener el contador
			try {
				proceso1status = proceso1.exitValue(); //valdra '0' cuando proceso1 haya terminado normalmente.
			} catch(Exception e){
				
			}
		}
		//System.out.println("");
		
		//proceso1.waitFor(); //En lugar de hacer el bucle podria simplemente haber esperado su terminacion, pero con el bucle mola mas
		proceso1.destroy();
	    logger.info("Terminado TrafficTest. Tiempo de ejecucion: \" + ((System.currentTimeMillis() - tini)/1000 + \" segundos");

	    
	}

}