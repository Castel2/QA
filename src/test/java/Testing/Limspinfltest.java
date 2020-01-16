package Testing;// JUnit Assert framework can be used for verification

import java.util.Date;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Property;

//estos imports son para las funciones de trabajo con fechas
import java.text.DateFormat;
import java.text.SimpleDateFormat;



public class Limspinfltest {

	private Browser browser;

	DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
	private static Logger logger = LogManager.getLogger(Limspinfltest.class);

	public Limspinfltest(Browser browser) {
		this.browser = browser;
	}

	
	/**
	 * Abre el navegador y cumplimenta los campos obligatorios del formulario (empresa, user, mapp).
	 * @throws Exception
	 * @author xruizs
	 */
	public void llamaltest() throws Exception {
		String host = datosGlobales.get(Constantes.HOST).toString();
		String port = datosGlobales.get(Constantes.PORT).toString();
		String empresa = datosGlobales.get(Constantes.EMPRESA).toString();
		String user = datosGlobales.get(Constantes.USER).toString();
		String mapp = datosGlobales.get(Constantes.MAPP).toString();
		
		browser.navigateTo("http://" + host + ":" + port + "/limspinf-ltest/");

		//Campos obligatorios del formulario
		browser.textbox("refCompany").setValue(empresa);
		browser.textbox("keyValue").setValue(user);
		browser.textbox("refApp").setValue(mapp);
		
	}
	
	
	public void checknovacio(String metodo) throws Exception { //Reutilizo este metodo en algunas invocaciones
		//Compruebo que se ha listado algo, como minimo un(1) registro
		try{
			browser.getText(browser.cell("[0]").under(browser.cell("IdMsgExt")));
		} catch(Exception e){
			logger.error("'"+ metodo + "'. No se han listado los registros");
			throw new Exception("");
		}
		logger.info("Correcto '"+ metodo + "'.");

	}
	
	
	/**
	 * 	&gt; Paso1 Listar contenidos de una MApp<br>
	 *	&gt; Paso2 Listar contenidos de una MApp limitando a 2 registros<br>
	 *	&gt; Paso3 Listar utilizando el idMsgExt del segundo mensaje de los que se listaron anteriormente<br>
	 *	&gt; Paso4 Listar utilizando los idMsgExt de los dos(2) mensajes que se listaron anteriormente<br>
	 * @throws Exception
	 * @author xruizs
	 */
	@Step("secuencia1")
	public void secuencia1() throws Exception {
		
		listContents(); //CASO1 Listar contenidos de una MApp
		listContents(2); //CASO2 Listar contenidos de una MApp limitando a 2 registros
		
		//Agarro los idMsgExt de los dos primeros registros
		String idmsgext[] = new String[2];
		idmsgext[0] = browser.getText(browser.cell("[0]").under(browser.cell("IdMsgExt")));
		logger.info("Recoger 'idMsgExt' del primer registro: "+idmsgext[0]);
		
		idmsgext[1] = browser.getText(browser.cell("[1]").under(browser.cell("IdMsgExt")));
		logger.info("Recoger 'idMsgExt' del segundo registro: "+idmsgext[1]);
		
		
		//Los siguientes metodos llamados deben listar lo mismo, es simplemente dos formas de mostrar lo mismo.
		listContentsByIdExt(idmsgext[0]);
		listContentsByIdExt(idmsgext[0],idmsgext[1]);
		listContents(idmsgext[0],"","");
		listContents(idmsgext[0],idmsgext[1]);
	}
	
	
	/**
	 * 	&gt; Paso1 - Observar que los contenidos entre 2 fechas NO estan leídos (READ=FALSE)<br>
	 * 	&gt; Paso2 - Marcar como leídos los contenidos entre 2 fechas<br>
	 * 	&gt; Paso3 - Observar que se han marcado como leídos (READ=TRUE)<br>
	 * 	&gt; Paso4 - Tomar el idMsgExt del segundo registro, y marcarlo como HIDDEN=TRUE<br>
	 * 	&gt; Paso5 - Observar, utilizando el idMsgExt que dicho registro se ha marcado como oculto (HIDDEN=TRUE)<br>
	 * 	&gt; Paso6 - Observar, utilizando idMsgExt que dicho registro se ha marcado como oculto (HIDDEN=TRUE)<br>
	 * @param diasatras
	 * @throws Exception
	 * @author xruizs
	 */
	@Step("secuencia2 <>")
	public void secuencia2(long diasatras) throws Exception {//Valida la funcionalidad de cambiar los contenidos 'leidos' y contenidos 'hidden'
		long tsfrom;
		long tsto;
		long[] misfechas = new long[2];
		
		//Obtiene las fechas a utilizar en los filtros
		misfechas = fechas(diasatras);
		tsfrom = misfechas[0];
		tsto = misfechas[1];
		

		//*****FASE 1*****//
		//***************//
		//Listar entre 2 fechas
		//Preparo la fecha actual en el formato adecuado dd/MM/yyyy HH:mm:ss
		DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		logger.info("tsfrom: " + date.format(tsfrom));
		logger.info("tsto:   " + date.format(tsto));
		logger.info("//Listar entre 2 fechas");
		listContents(tsfrom, tsto);

		
		
		//Verifico que todos los registros tienen READ=FALSE
		logger.info("//Observar que los contenidos NO estan leidos (FALSE)");

		String resultados[] = new String[3];
			/*
			 *[0] "estado resuperado"
			 *[1] "IdMsgExt"
			 *[2] "resultado correcto o incorrecto"
			*/
		
		String columna = "Read"; //Valores posibles: "Deleted", "DeviceFound", "Hidden", "Read", "ReadByDevice"
		String status = "false"; //Valores posibles: "true", "false"
		int i=0;
		while (i>=0){
			try { //Pruebo a leer el siguiente registro, hasta que pete porque no hay más registros

				resultados = checkColumnStatus(columna, status, i, ""); //"nombre de columna", "estado en que deberian estar todas las columnas", "contador"
	
			} catch(Exception e){
				logger.info(" Se ha alcanzado el final de la lista. Se han leido " + (i) + " registros");
				i = -2;
			}
			
			if (resultados[2].equals("correcto")&&(i!=-2)){

				logger.info("Verifica registro " + resultados[1] + " columna " + columna + "=" + resultados[0] + "  OK");

				
			} else if (resultados[2].equals("incorrecto")){
				logger.error("Verifica registro " + resultados[1] + " columna " + columna + "=" + resultados[0] + ". Deberia ser " + status);
				browser.close();
				throw new Exception("");
				
			}
			
		i++;
		
		}
		logger.info("Correcto,  todos los registros tienen READ=" + status);
		
	
		
		//*****FASE 2*****//
		//***************//
		//Marcar como leidos los contenidos entre dos fechas
		logger.info("//Marcar como leidos los contenidos entre dos fechas");
		status = "true"; //Valores posibles: "true", "false"
		markReadContents(tsfrom, tsto, status);
			
		
		//Verifico que los registros cambiados entre dos fechas, tienen ahora READ=TRUE
		logger.info("//Observar que se han marcado como leidos (READ=TRUE)");
		//resultados[]
			/*
			 *[0] "estado resuperado"
			 *[1] "IdMsgExt"
			 *[2] "resultado correcto o incorrecto"
			*/
		
		listContents(tsfrom, tsto); //Listo entre las dos fechas de antes
		
		columna = "Read"; //Valores posibles: "Deleted", "DeviceFound", "Hidden", "Read", "ReadByDevice"

		i=0;
		while (i>=0){
			try { //Pruebo a leer el siguiente registro, hasta que pete porque no hay más registros

				resultados = checkColumnStatus(columna, status, i, ""); //"nombre de columna", "estado en que deberian estar todas las columnas", "contador"
	
			} catch(Exception e){
				logger.info("Se ha alcanzado el final de la lista. Se han leido " + (i) + " registros.");
				i = -2;
			}
			
			if (resultados[2].equals("correcto")&&(i!=-2)){
				logger.info("Verifica registro " + resultados[1] + " columna " + columna + "=" + resultados[0] + "  OK");
				
			} else if (resultados[2].equals("incorrecto")){
				logger.error("Verifica registro " + resultados[1] + " columna " + columna + "=" + resultados[0] + ". Deberia ser " + status);
				browser.close();
				throw new Exception("");
				
			}
			
		i++;
		
		}
		logger.info("Correcto,  todos los registros tienen READ=" + status);

		
		
		
		//*****FASE 3*****//
		//***************//
		//Tomar el 'idMsgExt' del segundo registro, y marcarlo como HIDDEN=TRUE
		logger.info("//Tomar el 'idMsgExt' del segundo registro, y marcarlo como HIDDEN=TRUE");
		
		String idmsgext[] = new String[2];
		idmsgext[0] = browser.getText(browser.cell("[0]").under(browser.cell("IdMsgExt"))); //Agarro el idMsgExt del primer registro
		logger.info("Recoger 'idMsgExt' del primer registro: "+idmsgext[0]);

		
		idmsgext[1] = browser.getText(browser.cell("[1]").under(browser.cell("IdMsgExt")));  //Agarro el idMsgExt del segundo registro
		logger.info("Recoger 'idMsgExt' del segundo registro: "+idmsgext[1]);

		
		
		//Marcar como oculto un idMsgExt
		status = "true"; //Valores posibles: "true", "false"
		markHideContents(idmsgext[1], status);
	
		
		
		//Verifico que el idMsgExt que no ha cambiado, continua teniendo HIDDEN=FALSE
		String resultado;
		resultado = listContents(idmsgext[0],"","false"); //Listo el 'idMsgExt' que no ha cambiado. (String idmsgext, String read, String hidden)
		if (resultado.equals("correcto")){
			logger.info("Verifica registro " + idmsgext[0] + " columna Hidden=false  OK");
		} else {
			logger.error("Verifica registro "  + idmsgext[0] + " columna Hidden=false  KO");
			browser.close();
			throw new Exception("");
		}
		
		//Verifico que el idMsgExt que SI ha cambiado, tiene HIDDEN=TRUE
		logger.info("//Verificar que el idMsgExt tiene ahora HIDDEN=TRUE");
		resultado = listContents(idmsgext[1],"","true"); //Listo el 'idMsgExt' que SI ha cambiado. (String idmsgext, String read, String hidden)
		if (resultado.equals("correcto")){
			logger.info("Verifica registro " + idmsgext[1] + " columna Hidden=" + status + " OK");
		} else {
			logger.error("Verifica registro "  + idmsgext[1] + " columna Hidden=" + status + " KO");
			browser.close();
			throw new Exception("");
		}
		//*************************************VOY POR AQUI******************************//
		//*************************************VOY POR AQUI******************************//
		//*************************************VOY POR AQUI******************************//
		//*************************************VOY POR AQUI******************************//
		//*************************************VOY POR AQUI******************************//
		
				
		browser.close();
		
	}
	
	/**
	 * Lista los contenidos generados por una determinada de una MApp.
	 * La MApp de la que se trata se define en el 'TwistScenarioDataStore'
	 * @throws Exception
	 * @author xruizs
	 */
	public void listContents() throws Exception {//CASO1 Listar contenidos de una MApp
		
		llamaltest();
		
		browser.select("method").choose("listContents");
		browser.submit("ejecutar").click();
		logger.info("\nLlamado metodo 'listContents' Caso1. //Listar contenidos de una MApp.");
		checknovacio("listContents"); //Compruebo que se ha listado algo, como minimo un(1) registro

	}
	
	
	/**
	 * Lista, limitando a n registros, los contenidos generados por una determinada de una MApp.
	 * La MApp de la que se trata se define en el 'TwistScenarioDataStore'
	 * @param limit indica el n maximo de registros que devuelve el listado
	 * @throws Exception
	 * @author xruizs
	 */
	public void listContents(int limit) throws Exception {//CASO2 Listar contenidos de una MApp limitando a 2 registros
		
		llamaltest();
				
		browser.select("method").choose("listContents");
		browser.textbox("limit").setValue(String.valueOf(limit));
		browser.submit("ejecutar").click();
		logger.info("\nLlamado metodo 'listContents' Caso2. //Listar contenidos de una M-App limitando a " + limit + " registros.");
		
		
		int i = limit + 1;
		
		//Verifico que no se hayan listado mas registros de los indicados en limit
		try {
			browser.getText(browser.cell("[" + i + "]").under(browser.cell("IdMsgExt")));
			logger.error("Se han listado mas de " + limit + " registros que era el limite establecido");
			//System.out.println("ERR: Se han listado mas de " + limit + " registros que era el limite establecido");
			browser.close();
			throw new Exception("");
		}
		catch(Exception e){
			logger.info("Correcto se han listado unicamente " + limit + " registros");
		}
				
	}
	
	
	/**
	 * Lista los contenidos, entre 2 fechas, generados por una determinada de una MApp.
	 * @param tsfrom
	 * @param tsto
	 * @throws Exception
	 * @author xruizs
	 */	
	public void listContents(long tsfrom, long tsto) throws Exception {//Listar contenidos entre 2 fechas
		
		llamaltest();
						
		browser.select("method").choose("listContents");
		browser.textbox("tsFrom").setValue(String.valueOf(tsfrom));
		browser.textbox("tsTo").setValue(String.valueOf(tsto));
		
		browser.submit("ejecutar").click();	
		logger.info("\nLlamado metodo 'listContents' . //Listar contenidos entre 2 fechas.");
			
		checknovacio("listContents"); //Compruebo que se ha listado algo, como minimo un(1) registro
				
	}
	
	
	/**
	 * Lista los contenidos, buscando por un determinado idmsgext
	 * adicionalmente se le pueden agregar los criterios de filtrado por 'Read' o por 'Hidden'. El criterio 'Hidden' es util cuando un determinado idmsgext está oculto, ya que en tal caso no se muestra en el listado a no ser que se establezca el criterio hidden=true
	 * @param idmsgext
	 * @param read
	 * @param hidden
	 * @return 'correcto' o 'incorrecto'
	 * @throws Exception
	 * @author xruizs
	 */	
	public String listContents(String idmsgext, String read, String hidden) throws Exception {
		
		llamaltest();
		
		browser.select("method").choose("listContents");
		
		//Cumplimentar los campos que toquen
		browser.textbox("idMsgExtList").setValue(idmsgext);
		if (!read.equals("")) browser.textbox("read").setValue(read);		
		if (!hidden.equals("")) browser.textbox("hidden").setValue(hidden);
		browser.submit("ejecutar").click();
		logger.info("\nLlamado metodo 'listContents'. Parametros: '" + idmsgext + "' Read=''" + read + " Hidden='" + hidden + "'");
		
		/*
		 * //Codigo de Ricard
		TableBrowser tableBrowser = new TableBrowser(browser, null, true);
		System.out.println("\nTabla de "+ tableBrowser.getNumRows() +"x"+ tableBrowser.getNumCols());
		
		String val = tableBrowser.getElement("refUser", 5);
		int row = tableBrowser.getElementRow("refUser", "lilicapital");
			
		if (val == null) {
		}
		*/
		String resultados[] = new String[3];
			/*
			 *[0] "estado recuperado"
			 *[1] "IdMsgExt" del registro que estamos leyendo
			 *[2] "resultado 'correcto' o 'incorrecto'"
			*/
		
		//Verifico cada una de las columnas que haya utilizado como criterio a la hora de listar
		String resultado;
		String salidaconsola = "";
		
		if ((!idmsgext.equals(""))&&(browser.cell(idmsgext).exists())) {
			resultados[2] = "correcto";
			salidaconsola = "Correcto 'listContents' listando " + idmsgext;
		} else return resultado="incorrecto";
		
		
		if (!read.equals("")) {
			resultados = checkColumnStatus("Read", read, 0, ""); //Verifico la columna Read
			if (resultados[2].equals("correcto")) {
				salidaconsola = "" + salidaconsola + " Read=" + read;
			} else 	return resultado="incorrecto";
		}  
		
		
		if (!hidden.equals("")) {
			resultados = checkColumnStatus("Hidden", hidden, 0, ""); //Verifico la columna Hidden
			if (resultados[2].equals("correcto")) {
				salidaconsola = "" + salidaconsola + " Hidden=" + hidden + "";
			} else return resultado="incorrecto";
		} 
		logger.info(salidaconsola);

		
		resultado = resultados[2];
	
		return resultado;

	}
	
	
	/**
	 * Lista los contenidos, buscando por dos idmsgext especificados
	 * @param idmsgext1
	 * @param idmsgext2
	 * @throws Exception
	 * @author xruizs
	 */
	public void listContents(String idmsgext1, String idmsgext2) throws Exception {
		
		llamaltest();
		
		boolean correcto1 = false;
		boolean correcto2 = false;
		browser.select("method").choose("listContents");
		browser.textbox("idMsgExtList").setValue(idmsgext1+","+idmsgext2);
		browser.submit("ejecutar").click();				
		logger.info("\nLlamado metodo 'listContents'. Listar por idMsgExt: " + idmsgext1 + " y " + idmsgext2 + " recogido anteriormente");

		
		
		//Verifico que se ha listado el primer IdMsgExt solicitado
		String idmsgextcheck = browser.getText(browser.cell("[0]").under(browser.cell("IdMsgExt")));
		if (idmsgextcheck.equals(idmsgext1)){
			correcto1 = true;
		}
		
		//Verifico que se ha listado el segundo IdMsgExt solicitado		
		idmsgextcheck = browser.getText(browser.cell("[1]").under(browser.cell("IdMsgExt")));
		if (idmsgextcheck.equals(idmsgext2)){
			correcto2 = true;
		}
		

		if (correcto1 && correcto2){
			logger.info("Correcto 'listContents' listando los dos 'idMsgExt': '"+idmsgext1+"' y '"+idmsgext2+"'");
			
		}else {
			browser.close();

			if (!correcto1){
				logger.error("'listContents'. Los IdMsgExt listados no son iguales que los IdMsgExt pasados como parametro");
				throw new Exception("");
			}
			if (!correcto2){
				logger.error("'listContents'. Los IdMsgExt listados no son iguales que los IdMsgExt pasados como parametro");
				throw new Exception("");
			}
		}
				
	}
	
	
	
	/**
	 * Lista los contenidos, buscando por un determinado idmsgext.
	 * Es lo mismo que hace 'listContents' que es mas nueva.
	 * listContentsByIdExt puede considerarse en desuso
	 * @param idmsgext
	 * @throws Exception
	 * @author xruizs
	 */
	public void listContentsByIdExt(String idmsgext) throws Exception {
		
		llamaltest();
		
		browser.select("method").choose("listContentsByIdExt");
		browser.textbox("idMsgExtList").setValue(idmsgext);
		browser.submit("ejecutar").click();
		logger.info("\nLlamado metodo 'listContentsByIdExt'. Listar por idMsgExt: " + idmsgext + " recogido anteriormente");
				
		//Verifico que se ha listado el IdMsgExt solicitado
		String idmsgextcheck = browser.getText(browser.cell("[0]").under(browser.cell("IdMsgExt")));
		if (idmsgextcheck.equals(idmsgext)){
			logger.info("Correcto 'listContentsByIdExt' listando por 1 'idMsgExt'");
		}else {
			logger.error("IdMsgExt listado "+idmsgextcheck+" es distinto al IdMsgExt "+idmsgext+" pasado como parametro");
			browser.close();
			throw new Exception("");
		}
				
	}
	
	
	/**
	 * Lista los contenidos, buscando por dos idmsgext especificados
	 * Es lo mismo que hace 'listContents' que es mas nueva.
	 * listContentsByIdExt puede considerarse en desuso
	 * @param idmsgext1
	 * @param idmsgext2
	 * @throws Exception
	 * @author xruizs
	 */
	public void listContentsByIdExt(String idmsgext1, String idmsgext2) throws Exception {
	
		llamaltest();
		
		boolean correcto1 = false;
		boolean correcto2 = false;
		browser.select("method").choose("listContentsByIdExt");
		browser.textbox("idMsgExtList").setValue(idmsgext1+","+idmsgext2);
		browser.submit("ejecutar").click();
		logger.info("\nMetodo 'listContentsByIdExt'. Listar por idMsgExt: " + idmsgext1 + " y " + idmsgext2 + " recogidos anteriormente");

				
		//Verifico que se ha listado el primer IdMsgExt solicitado
		String idmsgextcheck = browser.getText(browser.cell("[0]").under(browser.cell("IdMsgExt")));
		if (idmsgextcheck.equals(idmsgext1)){
			correcto1 = true;
		}
		//Verifico que se ha listado el segundo IdMsgExt solicitado		
		idmsgextcheck = browser.getText(browser.cell("[1]").under(browser.cell("IdMsgExt")));
		if (idmsgextcheck.equals(idmsgext2)){
			correcto2 = true;
		}
		
		if (correcto1 && correcto2){
			logger.info("Correcto 'listContentsByIdExt' listando los dos 'idMsgExt': '"+idmsgext1+"' y '"+idmsgext2+"'");
		}else {
			browser.close();
			if (!correcto1){
				logger.info("'listContentsByIdExt'. Los IdMsgExt listados no son iguales que los IdMsgExt pasados como parametro");
				throw new Exception("");
			}
			if (!correcto2){
				logger.info("'listContentsByIdExt'. Los IdMsgExt listados no son iguales que los IdMsgExt pasados como parametro");
				throw new Exception("");
			}
		}
		
		
	}
	
	
	
	/**
	 * 
	 * @param columna
	 * @param status
	 * @param registro
	 * @param idmsgext
	 * @return Devuelve un String[3] con la siguiente informacion:
	 * <br>[0] "estado recuperado"
	 * <br>[1] "IdMsgExt" del registro que estamos leyendo
	 * <br>[2] "resultado 'correcto' o 'incorrecto'"
	 * @throws Exception
	 * @author xruizs
	 */	
	public String[] checkColumnStatus(String columna, String status, int registro, String idmsgext) throws Exception {
		
		String resultados[] = new String[3];
		
		if (idmsgext.equals("")) { //Leyendo el estado del numero de registro pasado por parametro

			resultados[0] = browser.getText(browser.cell("[" + registro + "]").under(browser.cell(columna)));
			resultados[1] = browser.getText(browser.cell("[" + registro + "]").under(browser.cell("IdMsgExt")));
			
			if (resultados[0].equals(status)){
				resultados[2]="correcto";
			} else {
				resultados[2]="incorrecto";
			}

		} else { //Leyendo el estado de una columna, a partir del 'idmsgext' pasado por parametro
			resultados[0] = browser.getText(browser.cell(status).rightOf(browser.cell(idmsgext)).under(browser.cell(columna)));
			resultados[1] = idmsgext;
			
			if (resultados[0].equals(status)){
				resultados[2]="correcto";
			} else {
				resultados[2]="incorrecto";
			}
		}
		
		return resultados;
	}
	
	
	/**
	 * Permite establecer el campo READ a los contenidos entre dos fechas
	 * @param tsfrom
	 * @param tsto
	 * @param status
	 * @throws Exception
	 */	
	public void markReadContents(long tsfrom, long tsto, String status) throws Exception {//Marcar leidos contenidos entre 2 fechas
		
		llamaltest();
						
		browser.select("method").choose("markReadContents");
		browser.textbox("tsFrom").setValue(String.valueOf(tsfrom));
		browser.textbox("tsTo").setValue(String.valueOf(tsto));
		browser.textbox("read").setValue(status);
		
		browser.submit("ejecutar").click();
		logger.info("\nLlamado metodo 'markReadContents' . //Marcar leidos contenidos entre 2 fechas.");
		
			
	}
	
	
	/**
	 * Permite establecer el campo HIDDEN a un 'idmsgext' concreto
	 * @param idmsgext a buscar
	 * @param status estado que se ha de establecer
	 * @throws Exception
	 * @author xruizs
	 */
	public void markHideContents(String idmsgext, String status) throws Exception {//Marcar oculto un determinado 'IdMsgExt'
		
		llamaltest();
						
		browser.select("method").choose("markHideContents");
		browser.textbox("idMsgExtList").setValue(idmsgext);
		browser.textbox("hidden").setValue(status);
		
		browser.submit("ejecutar").click();
		logger.info("\nLlamado metodo 'markHideContents'. //Marcar oculto el IdMsgExt=" + idmsgext);
				
	}


	/**
	 * Establece todos los contenidos existentes a READ=FALSE y HIDDEN=FALSE
	 * @throws Exception
	 * @author xruizs
	 */
	@Step("resetear")
	public void resetear() throws Exception {
	
		llamaltest();
		browser.select("method").choose("markHideContents");
		browser.textbox("hidden").setValue("false");
		
		browser.submit("ejecutar").click();
		logger.info("\nLlamado metodo 'markHideContents'. //Marcar todo HIDDEN=FALSE");

		
		
		llamaltest();
		browser.select("method").choose("markReadContents");
		browser.textbox("read").setValue("false");
		
		browser.submit("ejecutar").click();
		logger.info("\nLlamado metodo 'markReadContents' . //Marcar todo READ=FALSE");

		
	}
	
	/**
	 * A partir de la fecha actual le resta los dias indicados en el param <code>diasatras</code>.
	 * @param diasatras
	 * @return
	 * @throws Exception
	 * @author xruizs
	 */
	public static long[] fechas(long diasatras) throws Exception {
		
		//Obtengo la fecha de hoy y la meto en 'ahora'
		Date ahora = new Date(); 
	
		long tsto = ahora.getTime();
		long tsfrom = tsto - (diasatras * (1000 * 60 * 60 * 24)); //A la fecha actual le resto los dias indicados
		
		long[] fechas = new long[2];
		fechas[0] = tsfrom;
		fechas[1] = tsto;
		return fechas;
		
	}
}