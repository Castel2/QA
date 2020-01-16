package Testing;
// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GestionDescartados {

	private static Logger logger = LogManager.getLogger(GestionDescartados.class);
	private Browser browser;

	public GestionDescartados() {

		browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}

	@Step("NavegarDescartados <wapppush> <-->")
	public void navegarDescartados(String app, String codigo) throws Exception {
		
		if (!app.equals("--")) {
			try {
				
				browser.select("idProduct").choose(app);
				
			} catch(Exception e) {
				logger.error("No se encuentra la aplicación '" + app);
				throw new Exception(" ");
			}
			
			browser.submit("seleccionar").click();
		}
		
		if (codigo.equals("--")) {
			try {
				
				browser.image("ico_info.gif[0]").click();
				
			} catch (Exception e) {
				logger.info("No se prueba la herramienta de 'mensajes descartados'");
			}
		} else{
			try {
				
				browser.image("ico_info.gif[0]").near(browser.cell(codigo + "[0]")).click();
				
			} catch (Exception e) {
				logger.error("No existen transacciones descartadas. No se ha probado por tanto la herramienta");
				throw new Exception(" ");
			}
		}
		
	}

	@Step("Buscar descartados por el idMsg <>")
	public void searchDescartedByIdmsg(String idMessage)throws Exception{
 		if(browser.cell(idMessage).exists()){
			logger.info("Existe el mensaje " + idMessage + " en descartados");
		}else{
			logger.error("No existe el mensaje " + idMessage + " en descartados");
			throw new Exception(" ");
		}
	}

}
