package Testing;// JUnit Assert framework can be used for verification

import com.thoughtworks.gauge.Step;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Esperar {

	private Browser browser;
	private static Logger logger = LogManager.getLogger(Esperar.class);
	public Esperar() {
		browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
	}
	@Step("Esperar <3>")
	public void esperar(Integer integer1) throws Exception {
		logger.info("Esperando " + integer1 + " Segundos");
		Thread.sleep(integer1*1000);
	}

}