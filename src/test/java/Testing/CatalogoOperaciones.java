package Testing;// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CatalogoOperaciones {

	private static Logger logger = LogManager.getLogger(CatalogoOperaciones.class);
	private Browser browser;

	DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

	public CatalogoOperaciones() {
		browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}
	
@Step("Crear Operacion <> <> <> <> <>")
	public void crearOperacion(String grupo, String refoperacion, String nombre, String trama, String proposito) throws Exception {
		String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
		int creadacat = 0; //Indicara si la categoria estaba ya creada
		
		//Pruebo los botoncitos ON/OFF y despues entro en la empresa dada
		if (browser.link(empresa).exists()){
			if (browser.image("switch_on.png").near(browser.link(empresa)).exists()){
				browser.image("switch_on.png").near(browser.link( empresa )).click();
				browser.image("switch_off.png").near(browser.link(empresa )).click();
				logger.info("Boton 'Generar caches' correcto");
			} else if (browser.image("switch_off.png").near(browser.link( empresa)).exists()){
				browser.image("switch_off.png").near(browser.link(empresa)).click();
				browser.image("switch_on.png").near(browser.link(empresa )).click();
				logger.info("Boton 'Generar caches' correcto");
			}
			
			browser.link( empresa).click();
		}
		
		
		//Creo el grupo en caso que no exista
		if (!browser.cell("/" + grupo + "/i").exists()) {
			browser.textbox("category_name").setValue(grupo);
			browser.button("btn_add_category").click();
			creadacat = 1;
		}
		Thread.sleep(2000);
		browser.image("ico_expand.png").leftOf(browser.cell("/" + grupo + "/i")).click();	
		
		
		//Comprobar que la operacion no exista previamente. En tal caso, la borra.
		if (browser.cell(refoperacion).exists()){
			browser.image("ico_delete.gif").rightOf(browser.cell(refoperacion)).click();
			Thread.sleep(2000);
			if (browser.cell(refoperacion).exists()){
				logger.error("borrando la operacion: " + refoperacion);
				throw new Exception(" ");
			} else if (!browser.cell(refoperacion).exists()){
				logger.info("Borrada la operacion: " + refoperacion);
			}
		}
		if (browser.cell(refoperacion+"mod").exists()){
			browser.image("ico_delete.gif").rightOf(browser.cell(refoperacion+"mod")).click();
			Thread.sleep(2000);
			if (browser.cell(refoperacion+"mod").exists()){
				logger.error("borrando la operacion: " + refoperacion+"mod");
				throw new Exception(" ");
			} else if (!browser.cell(refoperacion+"mod").exists()){
				logger.info("Borrada la operacion: " + refoperacion+"mod");
			}
		}
		
		//Crear una operacion
		browser.textbox("service_ref").setValue(refoperacion);
		browser.textbox("service_name").setValue(nombre);
		if (!trama.equals("")) browser.select("service_dataframe").choose(trama); //El parametro 'trama' es opcional, dado que solo sirve para instalaciones con AE
		browser.button("btn_add_service").click();
		Thread.sleep(2000); //Espero para que de tiempo a que aparezca la celda creada
		if (browser.cell(refoperacion).exists()){
			logger.info("Creada la operacion: " + refoperacion);
		} else {
			logger.error("Creando la operacion: " + refoperacion + " Comprueba que no exista en otro grupo");
			throw new Exception(" ");
		}			
		
		//Renombrado de la operacion
		browser.span("lbutton lb1").rightOf(browser.cell(refoperacion)).click();
		browser.textbox("service_ref").setValue(refoperacion+"mod");
		browser.textbox("service_name").setValue(nombre+"mod");
		browser.select("service_dataframe").choose(trama);
		browser.button("btn_update_service").click();
		if (browser.cell(refoperacion+"mod").exists()){
			logger.info("Renombrado: " + refoperacion + " >> " + refoperacion+"mod");
		}else {
			logger.error("Renombrando: " + refoperacion + " >> " + refoperacion+"mod");
			throw new Exception(" ");
		}	
				
		//Borro la operacion que he creado, solo si 'proposito!=provision'
		if (proposito.equals("provision")){
			//Vuelvo a poner el nombre original
			browser.span("lbutton lb1").rightOf(browser.cell(refoperacion+"mod")).click();
			browser.textbox("service_ref").setValue(refoperacion);
			browser.textbox("service_name").setValue(nombre);
			browser.select("service_dataframe").choose(trama);
			browser.button("btn_update_service").click();
			Thread.sleep(1000);
			if (browser.cell(refoperacion+"mod").exists()){
				logger.error("Renombrando: " + refoperacion + "mod >> " + refoperacion);
				throw new Exception(" ");
			}else {
				logger.info("Renombrado: " + refoperacion + "mod >> " + refoperacion);
			}	
		} else {
			//Borro la operacion que he creado, solo si 'proposito!=provision'
			browser.image("ico_delete.gif").rightOf(browser.cell(refoperacion+"mod")).click();
			Thread.sleep(2000);
			if (browser.cell(refoperacion+"mod").exists()){
				logger.error("borrando la operacion: " + refoperacion+"mod");
				throw new Exception(" ");
			} else if (!browser.cell(refoperacion+"mod").exists()){
				logger.info("Borrada la operacion: " + refoperacion+"mod");
			}
			
			//Borro el grupo, solo en el caso que lo hubiese creado
			//No lo borro en caso que el grupo existiese previamente a la prueba
			if (creadacat == 1){
				browser.span("lbutton lb2").rightOf(browser.cell(grupo)).click();
				browser.expectConfirm("¿Desea borrar este grupo?", true);
				if (!browser.cell("/" + grupo + "/i").exists()){
					logger.info("Borrado grupo: " + grupo);
				}else {

					logger.error("Borrando grupo: " + grupo);
					throw new Exception(" ");
				}	
			}

		}
		
		
		if (browser.link("Organización").exists()) browser.link("Organización").click();
		if (browser.link("Companies").exists()) browser.link("Companies").click();
		
	
	}

}