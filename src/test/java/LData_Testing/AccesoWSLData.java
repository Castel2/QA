package LData_Testing;

import Testing.*;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * En esta clase se establece la conexión con los WS del LData
 * Created by amartinez on 18/07/2017.
 */

public class AccesoWSLData {
    private static Logger logger = LogManager.getLogger(AccesoWSLData.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

    public AccesoWSLData() {
    }

    /**
     * @param refProduct  Usuario de acceso al WS
     * @param pswProduct  Contraseña de acceso al WS
     * @param wlUrl       Acceso al WS en WebLogic
     * @param wasUrl      Acceso al WS en WAS
     * @param ws_service  Clase del servicio
     * @param ws_locator  Clase "locator" del servicio
     * @param ws_portstub Acceso al servicio
     * @param ws_method   Método que invoca la URL del servicio
     * @param refContract refContract to create XML Validation
     * @return Una lista de dos posiciones, en la primera posición se encuentra el stub(conexión con el servicio)
     * y en la segunda posición se encuentro el string de validación para la conexión con el servicio
     * @throws Exception
     */
    public List<Object> wsLDataGeneric(String refProduct, String pswProduct, String wlUrl, String wasUrl, String ws_service, String ws_locator, String ws_portstub, String ws_method, String refContract) {
        // La invocación a ws "normal" desde latinia sin utilizar reflection de java se puede consultar en "Acceso a LDatas mediante WebService" de la documentación oficial
        List<Object> retornar = new ArrayList<>();
        String host = datosGlobales.get(Constantes.HOST).toString();
        String port = datosGlobales.get(Constantes.PORT).toString();
        String loginEnterprise = datosGlobales.get(LatiniaUtil.LOGIN_EMPRESA).toString();
        String loginUser = datosGlobales.get(LatiniaUtil.LOGIN_USER).toString();
        String url;
        if (port.equals(Constantes.PORT7001) || port.equals(Constantes.PORT7002)){
            url = "http://" + host + ":" + port + wlUrl;
        } else {
            url = "http://" + host + ":" + port + wasUrl;
        }

        //Se crean las clases para definir los tipos de parametros URL y String
        Class[] paramURL = {URL.class};
        Class[] paramString = {String.class};
        try {
            //Se crean las clases del tipo ws adaptor y locator
            Class ws_ld_service = Class.forName(ws_service);
            Class ws_ld_locator = Class.forName(ws_locator);
            Object adaptor = ws_ld_locator.newInstance();
            Class ws_ld_portStub = Class.forName(ws_portstub);
            Method method = ws_ld_service.getDeclaredMethod(ws_method, paramURL);

            Object portStub = ws_ld_portStub.cast(method.invoke(adaptor, new URL(url)));
            //Se guarda en la primera posición de retornar el acceso al ws a través de los stubs
            retornar.add(portStub);
            //Se declaran los métodos para actualizar user y password
            Method setUsername = ws_ld_portStub.getDeclaredMethod("setUsername", paramString);
            Method setPassword = ws_ld_portStub.getDeclaredMethod("setPassword", paramString);
            // Se establecen las credenciales refProduct y psw de la aplicación
            setUsername.invoke(portStub, refProduct);
            setPassword.invoke(portStub, pswProduct);
            logger.info(url);
            //Construcción del XML de Validación
            LXValidationLData validation = new LXValidationLData();
            validation.setLoginEnterprise(loginEnterprise);
            validation.setLoginUser(loginUser);
            validation.setRefContract(refContract);
            //Se guarda en la segunda posición de la lista retornar, el XML de validación "string de validación"
            retornar.add(validation);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return retornar;
    }
}
