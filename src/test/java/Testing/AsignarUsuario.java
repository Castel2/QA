package Testing;// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsignarUsuario {

	private static Logger logger = LogManager.getLogger(AsignarUsuario.class);
	DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
	
	private Browser browser;

	public AsignarUsuario() {
		this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}

	

	/**
	 * Asigna un usuario a un contrato
	 * @throws Exception
	 */
	@Step("Asignar user")
	public void asignarUser() throws Exception {
		String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
		String user = datosGlobales.get(Constantes.USER).toString();
		String nomcontrato = datosGlobales.get(Constantes.NOM_CONTRATO).toString();

		browser.image("/ico_permisos/").rightOf(browser.cell(empresa)).click();
		System.out.println();

		browser.link(user).click();
		if (empresa.equalsIgnoreCase("INNOVUS")){
			browser.checkbox("id_contract2").rightOf(browser.cell(nomcontrato)).check();
		}else{
			browser.checkbox("id_contract2").rightOf(browser.cell(nomcontrato+ " Automated")).check();
		}
			
		browser.button("guardar").click();
		browser.expectConfirm("/?/", true);
			
		if (browser.image("button_inicio.gif").exists()){ //Hasta R3.9.3
			browser.image("button_inicio.gif").click();
		}
		
		
		if (browser.link("header-btn-home").exists()){ //Desde las primeras releases de R3.9.4
			browser.link("header-btn-home").click();
		}
		
		
		if (browser.span("hbtn home").exists()){ //Ultima release de R3.9.4
			browser.span("hbtn home").click();
		}
			

	}

}