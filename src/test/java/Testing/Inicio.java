package Testing;

import com.thoughtworks.gauge.Step;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Inicio {

    private final Browser browser;
    private static Logger logger = LogManager.getLogger(LogIN.class);
    public Inicio() {
        this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
    }

    @Step("InicioHome")
    public void inicioHome() throws Exception {
        if (browser.span("hbtn home").exists()){ //A partir de la ultima release de R3.9.4
            browser.span("hbtn home").click();
        } else if (browser.image("Latinia Logo").exists()){
           logger.info("Existe Latinia logo");
            browser.image("Latinia Logo").click();
        }
    }
}