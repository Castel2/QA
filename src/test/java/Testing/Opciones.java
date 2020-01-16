package Testing;// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Opciones {

	private Browser browser;

	private static Logger logger = LogManager.getLogger(Opciones.class);
	public Opciones() {
		browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}

	@Step("Vista <classic>")
	public void vista(String string1) throws Exception {
		//'string1' debe valer "classic" o "extended"
		
		//Se comprueba si el gui se está mostrando correctamente
		if (string1.equals("classic")){
			if (!browser.link("Diagnosis").exists()){
				logger.error("Estamos en vista 'clasica', cuando deberiamos estar en 'extendida'");
				throw new Exception("");
			} else {
				logger.info("Verificado que existe link 'Diagnosis'");
			}
			
			if (!browser.link("Análisis").exists()){
				logger.error("Estamos en vista 'clasica', cuando deberiamos estar en 'extendida'");
				throw new Exception("");
			} else {
				logger.info("Verificado que existe link 'Analisis'");
			}
			
			if (!browser.link("Control").exists()){
				logger.error("Estamos en vista 'clasica', cuando deberiamos estar en 'extendida'");
				throw new Exception("");
			} else {
				logger.info("Verificado que existe link 'Control'");
			}
			
			browser.link("Opciones").click();
			
		}
		
		if (string1.equals("extended")){
			if (browser.link("Diagnosis").exists()){
				logger.error("Estamos en vista 'extendida', cuando deberiamos estar en 'clasica'");
				throw new Exception("");
			} else {
				logger.info("Verificado que no existe el link 'Diagnosis'");
			}
			if (browser.link("Análisis").exists()){
				logger.error("Estamos en vista 'extendida', cuando deberiamos estar en 'clasica'");
				throw new Exception("");
			} else {
				logger.info("Verificado que no existe el link 'Analisis'");
			}
			if (browser.link("Control").exists()){
				logger.error("Estamos en vista 'extendida', cuando deberiamos estar en 'clasica'");
				throw new Exception("");
			} else {
				logger.info("Verificado que no existe el link 'Control'");
			}
			
			browser.image("button_contract4.gif").click();
		}
		//Establecemos el GUI a su vista 'clásica' o 'extendida' según indicado por el parámetro
		if (browser.radio(string1).exists()){
			browser.radio(string1).click();
		} else {
			if (string1.equals("classic")){
				string1="clásica";
			}
			if (string1.equals("extended")){
				string1="extendida";
			}
			logger.error("No se encuentra la opción de Vista " + string1);
			throw new Exception("");
		}
		
		
		browser.submit("Aceptar").click();
		browser.button("Volver").click();
	
	}

}