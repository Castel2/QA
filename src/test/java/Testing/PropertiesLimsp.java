package Testing;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;
import java.util.TreeMap;

/**
* Created by rrovira on 3/9/15.
*/
public class PropertiesLimsp {

    private Properties properties;
    private String host;
    private int port;
    private String user;
    private String psw;
    private static Logger logger = LogManager.getLogger(PropertiesLimsp.class);


    public PropertiesLimsp() {
        properties = new Properties();
    }

    public void init(String host, int port, String user, String psw) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.psw = psw;
    }

    public void readPropertyFiles(String... fname) {

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(host, port), // 443
                new UsernamePasswordCredentials(user, psw));

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();

        HttpGet httpget = null;
        InputStream instream = null;

        try {

            for (String file: fname) {

            String url = "http://" + host + ":" + port + "/lconfig/view.jsp?res=" + file;

                httpget = new HttpGet(url);
                logger.info("Invocando: " + url);

                CloseableHttpResponse response = httpclient.execute(httpget);

                int status = response.getStatusLine().getStatusCode();
                if (status != HttpStatus.SC_OK) {
                    logger.error("Error de conexión. Status =  '" + status + "'. url = '" + url + "'");
                    throw new Exception("");
                }

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    instream = entity.getContent();

                    properties.load(instream);

                    instream.close();
                }

            }
        } catch (Exception ex) {
            logger.error(" Error de comunicación. Ex:\n" + ex);
            ex.printStackTrace();
        } finally {
            if (instream != null) try {
                instream.close();
            } catch (Exception e) {
            }
            if (httpget != null) try {
                httpget.releaseConnection();
            } catch (Exception e) {
            }
            if (httpclient != null) try {
                httpclient.close();
            } catch (Exception e) {
            }
        }
    }


    public Properties getProperties() {
        return properties;
    }

    public String getProp(String name, String def) {
        String val = properties.getProperty(name);
        if (val != null) return val;
        return def;
    }

    public int getPropInt(String name, int def) {
        String val = properties.getProperty(name);
        try {
            if (val != null) return Integer.parseInt(val);
        } catch (Exception ex) { }
        return def;
    }

    public long getPropLong(String name, long def) {
        String val = properties.getProperty(name);
        try {
            if (val != null) return Long.parseLong(val);
        } catch (Exception ex) { }
        return def;
    }

    public String noNull(String val) {
        if (val == null) return "";
        return val;
    }

    public String noVoid(String val) {
        if (val == null) return null;
        if (val.trim().length() == 0) return null;
        return val;
    }

    public void dumpProperties() {

        TreeMap mapProps = new TreeMap();
        for (Object prop: properties.keySet()) {
            mapProps.put(prop, properties.get(prop));
        }
        logger.info("\nVolcado de propiedades LIMSP:");
        for (Object prop: mapProps.keySet()) {
            logger.info(" - "+ prop +"="+ properties.getProperty(prop.toString()));
        }
        System.out.println("\n");
        }

}