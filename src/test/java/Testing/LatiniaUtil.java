package Testing; /**
 * Created by @xruizs on 11/12/2015.
 * Inicializacion del Browser y del Proxy SAHI
 */

import com.thoughtworks.gauge.*;
import net.sf.sahi.Proxy;
import com.sahipro.lang.java.client.Browser;
import net.sf.sahi.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;

public class LatiniaUtil {


    public static final String LOCAL_HOST = "localhost";
    private static final String DEFAULT_BROWSER = "firefox";
    //private static final String DEFAULT_BROWSER = "chrome";
    private static final int DEFAULT_PORT = 9999;
    private static Browser browser;
    private static Proxy proxy;

    public static final String LOGIN_EMPRESA = "loginEmpresa";
    public static final String LOGIN_USER = "loginUser";
    static final String LOGIN_PASSWORD = "loginPassword";
    public static final String CUSTOMER = "customer";
    static final String NAVEGADOR = "DEFAULT_BROWSER";

    private static Logger logger = LogManager.getLogger(LatiniaUtil.class);


    @BeforeSuite
    public void setup() {
        contextlog();
        String sahiInstallDir = System.getenv("sahi.install_dir");
        if (sahiInstallDir == null || sahiInstallDir.isEmpty()) {
            logger.error("Sahi Install directory not specified. Set in env/default/sahi_config.properties file");
            throw new RuntimeException("");
        }
        Configuration.init(sahiInstallDir, userDataDir());
        launchBrowser();
    }


    @AfterSuite
    public void tearDown() throws InterruptedException {
        if (browser != null) {
            browser.close();
            logger.info("Cerrada sesion del browser");
        }
        if (proxy != null && proxy.isRunning()) {
            proxy.stop();
            logger.info("Detenido el PROXY");
        }
    }


    private void launchBrowser() {
        int port = portNumber();
        startProxy(port);
        browser = new Browser(browserName());
        browser.open();
    }

    private void startProxy(int port) {
        proxy = new Proxy(port);
        proxy.start(true);
        waitForProxy(10000);
    }

    public static Browser getBrowser() {
        return browser;
    }

    private int portNumber() {
        return DEFAULT_PORT;
    }

    private String browserName() {
        String browserName = System.getenv("sahi.browser_name");
        if (!(browserName == null || browserName.isEmpty())) {
            return browserName;
        }
        return DEFAULT_BROWSER;
    }

    private String userDataDir() {
        String sahiUserdata = System.getenv("sahi.userdata");
        logger.info("El valor de sahi_userdata es: " + sahiUserdata);
        return new File("sahi", sahiUserdata).getAbsolutePath();
    }

    private void waitForProxy(int waitUntil) {
        int counter = 0;
        while (!proxy.isRunning() && counter < waitUntil) {
            try {
                Thread.sleep(1000);
                counter += 1000;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!proxy.isRunning()) {
            logger.error("Sahi Proxy server is not starting");
            throw new RuntimeException("");
        }
    }


    /**
     * Busca en la pantalla actual el objeto que se le haya pasado como parametro
     *
     * @param nombre nombre del objeto que se busca
     * @throws Exception
     */
    public static boolean checkNombre(String nombre) throws Exception {
        boolean resultado = false;

        if (browser.cell(nombre).exists()) {
            resultado = true;
        } else {
            resultado = false;
        }

        return resultado;

    }

    public void contextlog(){
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File("src/test/java/log4j2.xml");
        context.setConfigLocation(file.toURI());
    }


}