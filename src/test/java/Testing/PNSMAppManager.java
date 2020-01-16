package Testing;// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PNSMAppManager {

	private Browser browser;

	DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
	private static Logger logger = LogManager.getLogger(PNSMAppManager.class);

	public PNSMAppManager() {
		browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}

	@Step("PNS_MApp_Manager <lat1MobileApp> <LATINIA>")
	public void pNS_MApp_Manager(String mapp, String company) throws Exception {
		String proposito = datosGlobales.get(Constantes.PROPOSITO).toString();
		
		if (browser.select("selCompany").exists()){
			browser.select("selCompany").choose(company);
		}
		
		int serie = 0;
		int existe = 1;
		int activada = 0;
		mapp = mapp.toLowerCase();
		String aplicacion = mapp + String.valueOf(serie);
		do {
				if (browser.cell(aplicacion).exists()){
					//Intento creación de un refApp duplicado. Lo esperado es que NO me deje crearlo
					if (!proposito.equals("provision")){
						browser.textbox("refApp").setValue(aplicacion);
						browser.textbox("nameApp").setValue("M-App creada con Automated " + aplicacion);
						browser.submit("btn btn-mini btn-primary").click();
						if (browser.div("alert alert-error").exists()){
							logger.info("Control de duplicidad superado " + aplicacion);
							} else {
							logger.error("Permitida creación de refApp duplicado '" + aplicacion + "'. Esto está MAL");
							throw new Exception("");
						}
					}
					
					//Si llego aqui es que no me ha dejado crear la M-App duplicada así que vamos bien
					serie = serie + 1;
					
					//Si la aplicación estaba desactivada, la activo.
					if (!proposito.equals("provision")){
						if (browser.italic("icon-play").rightOf(browser.cell(aplicacion)).exists()){
							browser.italic("icon-play").rightOf(browser.cell(aplicacion)).click();
							if (browser.italic("icon-pause").rightOf(browser.cell(aplicacion)).exists()){
								logger.info("Activada aplicacion" + aplicacion);
							} else {
								logger.error("activando la aplicación '" + aplicacion + "'");
								throw new Exception("");
							}
								activada = 1; //Llegado aqui, la M-App, en caso de existir previamente, estará activada; bien porque la he reactivado, o bien porque ya se encontraba activada.
								break;
							}
						
					}
					
					if (activada != 1) {
						aplicacion = mapp + String.valueOf(serie);
					}
 					
				} else existe = 0;
		} while (existe == 1);
		
		if (proposito.equals("provision")){ //Creo una M-App nueva en todo caso
			browser.textbox("refApp").setValue(aplicacion);
			browser.textbox("nameApp").setValue("M-App creada con Automated" + aplicacion);
			browser.submit("btn btn-mini btn-primary").click();
		}else {
			if (activada == 0){ //En caso que NO existiese previamente la M-App, la creo. En cambio si ya existia, no creo ninguna nueva.
				browser.textbox("refApp").setValue(aplicacion);
				browser.textbox("nameApp").setValue("M-App creada con Automated" + aplicacion);
				browser.submit("btn btn-mini btn-primary").click();
			}
		}
		//Renombro la MApp
		browser.italic("icon-edit").rightOf(browser.cell(aplicacion)).click();
		browser.textbox("nameApp").setValue(aplicacion + " renombrado con Automated");
		browser.submit("btn btn-mini btn-primary").click();
		if (browser.cell(aplicacion + " renombrado con Automated").exists() && browser.div("alert alert-success").exists()){
			logger.info("Renombrado correcto " + aplicacion + " renombrado con Automated");
		}
		//Vuelvo a dejarlo como estaba
		browser.italic("icon-edit").rightOf(browser.cell(aplicacion + " renombrado con Automated")).click();
		browser.textbox("nameApp").setValue(aplicacion);
		browser.submit("btn btn-mini btn-primary").click();
		if (browser.cell(aplicacion).exists() && browser.div("alert alert-success").exists()){
			logger.info("Renombrado correcto " + aplicacion);
		}
		
		//Pruebo la desactivación
		if (!proposito.equals("provision")){
			browser.italic("icon-pause").rightOf(browser.cell(aplicacion)).click();
			browser.link("btn btn-mini btn-warning").click();
			if (browser.italic("icon-play").rightOf(browser.cell(aplicacion)).exists()){
				logger.info("Desactivacion correcta " + aplicacion);
			} else {
				logger.error("Desactivando " + aplicacion);
				throw new Exception("");
			}
				
			
			if (!proposito.equals("inf-103")){ //Vuelvo a activar la aplicacion
				browser.italic("icon-play").rightOf(browser.cell(aplicacion)).click();
				if (browser.italic("icon-pause").rightOf(browser.cell(aplicacion)).exists()){
					logger.info("activacion correcta " + aplicacion);
				} else {
					logger.error("activando " + aplicacion);
					throw new Exception("");
				}
			}
		}
	}

}