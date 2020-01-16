package Testing; /**
 * Created by @xruizs on 11/12/2015.
 */

import com.thoughtworks.gauge.Step;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Property;

public class LogOut {

    private final Browser browser;
    private static Logger logger = LogManager.getLogger(LogOut.class);
    public LogOut() {

        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
    }

    @Step("LogOut")
    public void logOut() throws Exception {
        if (browser.span("hbtn logout").exists()) { //A partir de la ultima release de R3.9.4 en adelante
            logger.info("Cerrando la sesión GUI");
            browser.span("hbtn logout").click();
        } else {
            logger.error("No se encuentra el botón de 'Finalizar sesion GUI'");
            throw new Exception("");
        }

    }

}
