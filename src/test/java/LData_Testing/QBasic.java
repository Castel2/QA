package LData_Testing;

import Testing.Constantes;
import com.latinia.limsp.ldata.pstoragefacade.ws.Ws_ld_pstoragePortStub;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by amartinez on 6/03/2018.
 */
public class QBasic {

    private static Logger logger = LogManager.getLogger(QBasic.class);
    List<Object> retorna;
    AccesoWSLData accesoWSLData;

    public QBasic() {
        accesoWSLData = new AccesoWSLData();
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTCONTRACT, Constantes.LATINIA, Constantes.WLURL_PSTORAGE,
                Constantes.WASURL_PSTORAGE, Constantes.WS_LD_PSTORAGE_SERVICE, Constantes.WS_LD_PSTORAGE_LOCATOR,
                Constantes.WS_LD_PSTORAGE_PORTSTUB, Constantes.WS_LD_PSTORAGE_METHOD, Constantes.CONT_WGESTCONTRACT);
    }

    //Pendiente
    public void obtenerIdTipoMensaje() {
        Ws_ld_pstoragePortStub portStub = (Ws_ld_pstoragePortStub) retorna.get(0);
        LXValidationLData validation = (LXValidationLData) retorna.get(1);
    }

}
