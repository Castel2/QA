package Testing;
// JUnit Assert framework can be used for verification
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;

import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Estadisticas {

	private Browser browser;
	private static Logger logger = LogManager.getLogger(Estadisticas.class);
	DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
	
	public Estadisticas() {
		browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}

	@Step("Estadisticas <miCSVEstadisticas creado con Automated>")
	public void estadisticas(String string1) throws Exception {
		boolean existe = false;
		//clicka en el primer link que se encuentra cerca de #wapppush, o si no cerca de #ServiceA*
		if (browser.link(0).rightOf(browser.cell("#wapppush")).exists()){
			existe = true;
			browser.link(0).rightOf(browser.cell("#wapppush")).click();
		}
		if (browser.link(0).rightOf(browser.cell("/ServiceA/")).exists()){
			existe = true;
			browser.link(0).rightOf(browser.cell("/ServiceA/")).click();

		}
		if (existe==true){
			browser.image("icon_big_states_on.gif").click();
			browser.image("icon_big_operator2_on.gif").click();
			browser.image("Ver sin operadoras").click();
			browser.image("Ver con operadoras y canales").click();
			browser.image("Exportar datos").click();
			browser.textbox("exportFileName").setValue(string1);
			browser.submit("Exportar").click();
		} else {
			logger.error("No hay estadisticas. Comprueba la consolidacion");
			throw new Exception(" ");
		}
	}

}
