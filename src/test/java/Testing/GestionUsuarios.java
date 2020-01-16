package Testing;// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

import javax.naming.InitialContext; //Libreria para conexion a Weblogic



public class GestionUsuarios {

	private Browser browser;

	DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
	private static Logger logger = LogManager.getLogger(GestionUsuarios.class);
	public GestionUsuarios() {
		browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}

	@Step("Crear user")
	public void crearUser1() throws Exception {
		String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
		String user = datosGlobales.get(Constantes.USER).toString();

				
		//Seleccion de empresa en el 'combo'
		if (browser.select("selectEnterprise").exists()){
			if (!empresa.equals(browser.select("selectEnterprise").getSelectedText())) {
				browser.select("selectEnterprise").choose(empresa);
			}
		} else {
			logger.error(" No se encuentra el selector de Empresas");
			throw new Exception(" ");
		}
		//Creo el usuario
		if (browser.cell(user).exists()){
			logger.info("'" + user + "' ya existe");
		}
		else {
			browser.textbox("nu_userName").setValue(user);
			browser.password("nu_password").setValue(user);
			browser.submit("btn_new_user").click();
			
			Thread.sleep(1000); //Espero a que aparezcan los elementos en pantalla
			if (browser.div("lite-msg success").exists() && browser.cell(user).exists()){
				logger.info("Creado '" + user + "'");
			} else {
				logger.error("Creando '" + user + "'");
				throw new Exception(" ");
			}
			
		}
		
		//En caso que el usuario sea 'SinActividadUser', compruebo el borrado sin actividad 'SDP-REQGEN-162'
		if (user.equalsIgnoreCase("SinActividadUser")){
			//Preparo lo necesario para consultar al WL el estado de la propiedad 'clearGuiUsers'
			//Recojo el HOST y PORT para los usos en las conexiones a WL
			String host = datosGlobales.get(Constantes.HOST).toString();
			String port = datosGlobales.get(Constantes.PORT).toString();
			
			//Objeto de conexion a WebLogic 8.1/10.3
			Properties props = new Properties();
			// Variables de inicialización para un servidor WebLogic 8.1/10.3
			props.setProperty("java.naming.factory.initial","weblogic.jndi.WLInitialContextFactory");
			props.setProperty("java.naming.provider.url", "t3://" + host + ":" + port); // URL del servidor weblogic.

			// Inicialización de contexto remoto para acceder al contenedor J2EE.
			logger.info("Conectando WebLogic " + host + ":" + port);
			InitialContext context = new InitialContext(props);				
			logger.info("Consultando WebLogic " + host + ":" + port);
			String olderDays = null;
			try {
				olderDays = (String)context.lookup("latinia/limsp/config/lprocess/lp-maintenance/configs/clearGuiUsers/olderDays");
			} 
			catch(Exception e) {
				logger.info(e);
			}
			
			if (olderDays == null) {
				//si no está establecida la propiedad no puedo probar la funcionalidad
				logger.info("Propiedad 'clearGuiUsers/olderDays' no establecida. No se realiza la prueba");
			} else {
				logger.info("Propiedad clearGuiUsers/olderDays == " + olderDays);
				//Se invoca al LTimer para que borre los usuarios sin actividad
				//1.- Invocar el LTimer
				LTimers ltimers = new LTimers();
				//ltimers.setDatosGlobales(datosGlobales);//Llamo a mi propia función 'setDatosGlobales' en LTimers, que lo que hace es pasarle 'datosglobales', ya que de lo contrario, al tratar de usar 'datosglobales' en LTimers, sale 'null'
				ltimers.lpmaintenance("clearGuiUsers");	//Ejecuto el Timer para borrado de usuarios
				
				//2.- Comprobar que el usuario se ha borrado
				browser.navigateTo("http://"+host+":"+port+"/wgestuser/index.jsp", true);
				Thread.sleep(1000); //Espero a que aparezcan los elementos en pantalla
				if (browser.cell(user).exists()){
					logger.error("Usuario sin actividad no ha sido eliminado. Revisar la funcionalidad 'SDP-REQGEN-162'");
					throw new Exception(" ");
				}
				else {
					logger.info("Usuario '" + user + "' eliminado");
				}
			}
			
		} 

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