package Testing;

import com.sahipro.lang.java.client.Browser;
import com.sahipro.lang.java.client.ElementStub;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

/**
 * Created by amartinez on 5/06/2019
 */
public class DetalleDeTransaccionesEmail {
    private Browser browser;
    private static Logger logger = LogManager.getLogger(DetalleDeTransacciones.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

    public DetalleDeTransaccionesEmail() {
        this.browser = LatiniaUtil.getBrowser();
    }

    /**
     * @Author amartinez on June 2019
     * Permite verificar el BUG SDP-891, el cual consiste en duplicar el FROM de un email a partir del envío de un inot
     * @throws Exception
     */
    @Step("Verificar from duplicado email")
    public void fromDuplicadoEmail() throws Exception {
        String randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
        //Seleccionamos los mensajes que contienen el random que identifican el mensaje y los guardamos en una lista
        List<ElementStub> listaElementos = browser.cell("/(.*)" + randomNum + "(.*)/").collectSimilar();
        int contarFrom = 0;
        int contarTo = 0;

        //Recorremos la lista y comprobamos si es de tipo FROM o de tipo TO y contamos cada ocurrencia
        for (ElementStub es : listaElementos
        ) {
            if (browser.image("icon-from1.gif").leftOf(es).exists()) {
                contarFrom++;
            } else if (browser.image("icon-to1.gif").leftOf(es).exists()) {
                contarTo++;
            } else {
                logger.error("No existe ni la imagen \"icon-from1.gif\", ni la imagen \"icon-to1.gif\"");
                throw new Exception("");
            }
        }
        //Si el mensaje contiene sólo un FROM y un TO, el resultado es correcto, de lo contrario el resultado es incorrecto
        if (contarFrom == 1) {
            if (contarTo == 1) {
                logger.info("El resultado es correcto, sólo existe un (1) FROM y un (1) TO para el mensaje " + randomNum);
            } else {
                logger.error("Error, existe " + contarTo + " TO para el mensaje " + randomNum);
                throw new Exception("");
            }
        } else if (contarFrom == 2) {
            logger.error("BUG SDP-891 detectado, existe duplicidad (2) del FROM para el mensaje " + randomNum);
            throw new Exception("BUG SDP-891 detectado, existe duplicidad (2) del FROM para el mensaje " + randomNum);
        } else {
            logger.error("Error, existe " + contarFrom + " FROM para el mensaje " + randomNum);
            throw new Exception("");
        }
    }
}
