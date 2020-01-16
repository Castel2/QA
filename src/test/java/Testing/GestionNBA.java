package Testing;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by xruiz on 15/11/2016.
 */

public class GestionNBA {

    private static Logger logger = LogManager.getLogger(GestionNBA.class);
    private final Browser browser;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

    public GestionNBA() {
        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
    }

   @Step("Gestion NBA")
    public void GestionNBA(){
        
        browser.click(browser.submit("Nueva NBA"));
        browser.setValue(browser.textbox("campaign-name"), "PruebaSahi1");
        browser.select("campaignGroup").choose("Categoria A");
        browser.select("refContract").choose("Aplicación de prueba WebService");
        browser.click(browser.checkbox("sms"));
        browser.click(browser.div("EMAIL"));
        browser.click(browser.checkbox("pns"));
        browser.click(browser.checkbox("impactosCheck"));
        browser.setValue(browser.numberbox("impactos"), "1");

        browser.click(browser.button("OK"));
        browser.setValue(browser.textbox("minute"), "42");
        browser.click(browser.button("OK"));
        browser.select("userRegistered").choose("Suscritos");
        browser.select("alertGenerated").choose("Que generan alertas");
        browser.setValue(browser.numberbox("delay"), "6");
        browser.setValue(browser.numberbox("delay"), "7");
        browser.setValue(browser.numberbox("delay"), "8");
        browser.click(browser.submit("Guardar"));
        browser.setValue(browser.numberbox("roi"), "");
        browser.click(browser.submit("Guardar"));
        browser.click(browser.span("onoffswitch-inner"));
        browser.click(browser.checkbox("onoffswitch"));
        browser.click(browser.italic("btnXML"));
        browser.click(browser.button("Cerrar"));
        browser.click(browser.span("Inicio"));
        browser.click(browser.span("Inicio"));
        browser.click(browser.link("Finalizar sesión"));

    }
}
