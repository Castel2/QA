package Testing;// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GestionCredito {

	private Browser browser;
	private static Logger logger = LogManager.getLogger(GestionCredito.class);
	public GestionCredito() {
		this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}

	@Step("Credito <GENERAL>")
	public void credito(String cuenta) throws Exception {
		if (browser.link("INNOVUS").exists()) browser.link("INNOVUS").click();
		
		if (browser.link(cuenta).exists()) {
		}else{
			browser.link("Crear crédito").click();
			browser.textbox("refCredit").setValue(cuenta);
			browser.textbox("limitLower").setValue("-10000000");
			browser.submit("Crear").click();
			browser.expectConfirm("¿Los datos de creación del crédito son correctos?", true);
			browser.button("<<< Volver").click();
		}
		browser.image("ico_mas.gif").near(browser.link(cuenta)).click();
		browser.textbox("description").setValue("Automated_1");
		browser.textbox("amount").setValue("9000000");
		browser.submit("Añadir").click();
		browser.expectConfirm("¿Los datos para añadir crédito son correctos?", true);
		browser.button("Lista créditos").click();
		browser.link(cuenta).click();
	
	}

}