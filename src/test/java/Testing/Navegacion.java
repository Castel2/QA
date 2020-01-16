package Testing;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Navegacion {

	private final Browser browser;
	private static Logger logger = LogManager.getLogger(Navegacion.class);
	DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

	public Navegacion() {
			this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}

	@Step("Menu herramienta")
	public void menuHerramienta() throws Exception {
	//Esta función sirve para entrar en la herramienta pasada en los parámetros
		String string1 = (String) datosGlobales.get(Constantes.MENU);//.toString();
		String string2 = (String) datosGlobales.get(Constantes.HERRAMIENTA);//.toString();
		
		if (!string1.equals("--")){
			if (browser.image(string1).exists()){
				browser.image(string1).click();
			} else {
				// El format de dalt es el nou per la 392
				if (browser.link(string1).exists()){
					browser.link(string1).click();
				} else {
					logger.error("No se encuentra menú " + string1);
					throw new Exception("");
				}
			}
		}
		

		if (browser.link(string2).exists()){
			browser.link(string2).click();
		} else {
			logger.error("No se encuentra herramienta " + string2);
			throw new Exception("");
		}
	}

	//------------------------------------------------------------

}