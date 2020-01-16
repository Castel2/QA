package Testing;

import com.thoughtworks.gauge.Step;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrearInformePDF {

	private static Logger logger = LogManager.getLogger(CrearInformePDF.class);
	private Browser browser;

	public CrearInformePDF() {
		this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}

	@Step("Crear")
	public void crear() throws Exception {
		//Borra el informe antiguo en caso de que exista
		if (browser.cell("Informe PDF automated testing").exists()){
			browser.image("ico_report_delete.gif").near(browser.cell("Informe PDF automated testing")).click();
			browser.expectConfirm("¿Desea eliminar el informe Informe PDF  automated testing?", true);
			browser.button("Aceptar").click();
		}
		
		//Crea el nuevo informe, con el mismo nombre del anterior
		browser.link("Nuevo informe").click();	
		browser.textbox("reportName").setValue("Informe PDF automated testing");
		browser.textarea("reportDesc").setValue("Este es un informe PDF");
		browser.textbox("refReport").setValue("esteeselnombredelarchivo");
		browser.select("reportContracts").choose("/wapppush/");
		browser.submit("Crear informe").click();
		browser.expectConfirm("¿Los datos de creación del informe son correctos?",true);

		//Recoge el resultado mostrado en pantalla	
		if (browser.div("El informe se ha creado correctamente.").exists()){
			browser.button("Aceptar").click();
		} else {
			try {
				browser.link("header-btn-end").click();
			}catch(Exception e){
				logger.warn("No se encuentra 'header-btn-end', probando con 'button_finalizar.gif'" + e.toString());
				browser.image("button_finalizar.gif").click();
			}
		}
	}

	@Step("Programar")
	public void programar() throws Exception {
		browser.image("ico_report_schedule.gif").near(browser.cell("Informe PDF automated testing")).click();
		browser.submit("Aceptar").click();
		browser.expectConfirm("¿Los datos de programación del informe son correctos?", true);
		Thread.sleep(500);
		browser.button("Aceptar").click();
	}

	@Step("Email")
	public void email() throws Exception {
		browser.image("ico_report_email.gif").near(browser.cell("Informe PDF automated testing")).click();
		browser.textbox("subject").setValue("Este es el asunto. Creado con Automated");
		browser.textarea("to").setValue("testing@latinia.com");
		browser.submit("Aceptar").click();
		browser.expectConfirm("¿Los datos de email del informe son correctos?",
				true);
		browser.button("Aceptar").click();
	
	}

	@Step("Activar")
	public void activar() throws Exception {
		browser.expectConfirm("¿Desea activar el informe Mi informe PDF?", true);
		browser.checkbox("on").near(browser.cell("Informe PDF automated testing")).click();
	
	}

}
