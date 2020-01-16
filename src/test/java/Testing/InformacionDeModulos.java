package Testing;
// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.sahipro.lang.java.client.Browser;

public class InformacionDeModulos {

	private Browser browser;

	public InformacionDeModulos() {
		this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}

	@Step("Item1_AdaptorInJMSCollector")
	public void item1_AdaptorInJMSCollector() throws Exception {
		browser.image("ico_info.gif").click();
	
	}

	@Step("Atras")
	public void atras() throws Exception {
		browser.button("<<< Volver").click();	
	}

	@Step("Item2_Core")
	public void item2_Core() throws Exception {
		browser.image("ico_info.gif[3]").click();
	
	}

	@Step("ExportTodo")
	public void exportTodo() throws Exception {
		browser.image("Exportar datos a un fichero XML").click();
	
	}

	@Step("Export_busutil")
	public void export_Busutil() throws Exception {
		browser.image("ico_info.gif[5]").click();
		browser.image("Exportar datos a un fichero XML").click();
	
	}

}
