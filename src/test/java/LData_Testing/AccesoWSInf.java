package LData_Testing;

import Testing.Constantes;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.ws.BindingProvider;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * En esta clase se establece la conección con los WS del informacional (INF)
 * Created by amartinez on 14/11/2017.
 */
public class AccesoWSInf {

    private static Logger logger = LogManager.getLogger(AccesoWSInf.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

    public AccesoWSInf() {
    }

    public Object wsINFGeneric(String userAPP, String passAPP, String wasUrl, String wlUrl, String wsInf_service, String wsInf_method) {
        String host = datosGlobales.get(Constantes.HOST).toString();
        String port = datosGlobales.get(Constantes.PORT).toString();
        String url;

        if (port.equals(Constantes.PORT7001) || port.equals(Constantes.PORT7002)){
            url = "http://" + host + ":" + port + wlUrl;
        } else {
            url = "http://" + host + ":" + port + wasUrl;
        }
       try {
            Class ws_inf_service = Class.forName(wsInf_service);
            Constructor constructor = ws_inf_service.getConstructor(URL.class);
            Object service = constructor.newInstance(new URL(url));
            Method method = ws_inf_service.getDeclaredMethod(wsInf_method);
            Object portstub = method.invoke(service);

            logger.info(url);

            //Credenciales de aplicación
            ((BindingProvider) portstub).getRequestContext().put(
                    BindingProvider.USERNAME_PROPERTY, userAPP);
            ((BindingProvider) portstub).getRequestContext().put(
                    BindingProvider.PASSWORD_PROPERTY, passAPP);

            return portstub;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
