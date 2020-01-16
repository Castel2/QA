package Testing;

import LData_Testing.MProduct;
import com.sahipro.lang.java.client.Browser;
import com.sahipro.lang.java.client.ElementStub;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amartinez on 15/07/2019.
 */

public class DetalleDeMensajes {
    private static final String PNS = "notifications_active";
    private static final String SMS = "sms";
    private static final String EMAIL = "email";

    private Browser browser;
    private static Logger logger = LogManager.getLogger(Estadisticas.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    MProduct mProduct;
    LatiniaScenarioUtil latiniaScenarioUtil;

    public DetalleDeMensajes() {
        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
        mProduct = new MProduct();
        latiniaScenarioUtil =  new LatiniaScenarioUtil();
    }

    /**
     * Permite verificar la existencia de diferentes tipos de mensajes en la herramienta detalle de mensajes, en este caso PNS, SMS y EMAIL
     *
     * @throws Exception
     */
    @Step("Verificar tipo Mensajes")
    public void verificarTipoMensajes() throws Exception {
        String randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
        String celdaMensaje = "/(.*)" + randomNum + "(.*)/";
        ElementStub elementEMAIL;
        ElementStub elementSMS;
        ElementStub elementPNS;
        int sms = 0;
        int pns = 0;
        int email = 0;
        int cont = 0;

        ElementStub celdaMensaje2 = browser.cell(celdaMensaje);
        latiniaScenarioUtil.waitElement(celdaMensaje2,5,2);
        while (cont < 2) {
            if (browser.cell(celdaMensaje).exists()) {
                cont = 2;
            } else {
                if (browser.button("Fecha y hora").exists()) {
                    browser.button("Fecha y hora").click();
                    logger.info("Se han reordenado los mensajes por fecha y hora");
                    cont++;
                } else {
                    logger.error("No existe el campo \"Fecha y hora\" para reordenar los mensajes");
                    throw new Exception("");
                }
            }
        }

        // Se verifica la existencia del random con el cual identificamos los mensajes en detalle de mensajes
        if (browser.cell(celdaMensaje).exists()) {
            //Se almacena en una lista de elementos las celdas (elementos) que contienen el random
            List<ElementStub> listaElementos = browser.cell(celdaMensaje).collectSimilar();
            //Por cada elemento de la lista se identifica a que tipo de mensaje corresponde (SMS|PNS|EMAIL)
            //Se almacena el elemento en datos globales y se cuenta la cantidad por tipo de mensaje y que contengan el random
            for (ElementStub elemento : listaElementos
            ) {
                if (browser.cell("").leftOf(elemento).getText().equalsIgnoreCase(PNS)) {
                    logger.info("Existe mensaje para PNS");
                    elementPNS = elemento;
                    datosGlobales.put(Constantes.ELEMENTO_PNS, elementPNS);
                    pns++;
                } else if (browser.cell("").leftOf(elemento).getText().equalsIgnoreCase(SMS)) {
                    logger.info("Existe mensaje para SMS");
                    elementSMS = elemento;
                    datosGlobales.put(Constantes.ELEMENTO_SMS, elementSMS);
                    sms++;
                } else if (browser.cell("").leftOf(elemento).getText().equalsIgnoreCase(EMAIL)) {
                    logger.info("Existe mensaje para EMAIL");
                    elementEMAIL = elemento;
                    datosGlobales.put(Constantes.ELEMENTO_EMAIL, elementEMAIL);
                    email++;
                }
            }
            //confirmo si existen 3 mensajes que contienen el número aleatorio random, uno por cada tipo de mensaje
            if ((pns + sms + email) == 3) {
                logger.info("Existe el mensaje " + randomNum + " para los 3 canales SMS - EMAIL - PNS ");
            } else if ((pns + sms + email) > 3) {
                logger.error("Existe el mensaje para más de 3 canales, existen " + pns + " para PNS," +
                        sms + " para SMS y " + email + " para EMAIL");
                throw new Exception("");
            } else if ((pns + sms + email) > 0 && (pns + sms + email) < 3) {
                logger.error("No existe el mensaje para los 3 canales, existen " + pns + " para PNS," +
                        sms + " para SMS y " + email + " para EMAIL");
                throw new Exception("");
            }
        } else {
            logger.error("No existe el mensaje " + randomNum);
            throw new Exception("");
        }
    }

    /**
     * Permite filtrar por tipo de mensaje desde la opción de filtro básico
     *
     * @param tipoMensaje tipo de mensaje por el cual se quiere filtrar
     * @throws Exception
     */
    @Step("Filtro Básico filtrar por tipo Mensaje <>")
    public void filtrarPorTipoMensajeBasico(String tipoMensaje) throws Exception {
        String randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
        String celdaMensaje = "/(.*)" + randomNum + "(.*)/";
        if (browser.submit("msg-details-filter").exists()) {
            logger.info("Existe msg-details-filter");
            browser.submit("msg-details-filter").click();
            String tipoMensajeFiltrar = "";

            if (tipoMensaje.equalsIgnoreCase("SMS")) {
                tipoMensajeFiltrar = "message-details-filter-channel-toggle-button-1-button";
            } else if (tipoMensaje.equalsIgnoreCase("EMAIL")) {
                tipoMensajeFiltrar = "message-details-filter-channel-toggle-button-2-button";
            } else if (tipoMensaje.equalsIgnoreCase("PNS")) {
                tipoMensajeFiltrar = "message-details-filter-channel-toggle-button-3-button";
            }
            //Se verifica la existencia del random con el cual identificamos los mensajes en detalle de mensajes
            ElementStub celdaMensaje2 = browser.cell(celdaMensaje);
            latiniaScenarioUtil.waitElement(celdaMensaje2,5,2);
            if (browser.cell(celdaMensaje).exists()) {
                //Se verifica la existencia del botón del tipo de mensaje específico
                if (browser.button(tipoMensajeFiltrar).exists()) {
                    logger.info("Se filtra por " + tipoMensaje);
                    browser.button(tipoMensajeFiltrar).click();
                    //Se verifica que solo existen mensajes del tipo de mensaje por el cual se ha filtrado
                    //Y que no existen mensajes de otro tipo de mensaje
                    latiniaScenarioUtil.waitElement(celdaMensaje2,5,2);
                    if (tipoMensaje.equalsIgnoreCase("SMS")) {
                        if (browser.cell(SMS).leftOf(browser.cell(celdaMensaje)).exists() &&
                                !browser.cell(EMAIL).leftOf(browser.cell(celdaMensaje)).exists() &&
                                !browser.cell(PNS).leftOf(browser.cell(celdaMensaje)).exists()) {
                            logger.info("Resultado correcto, sólo existen mensajes de tipo " + tipoMensaje);
                        } else {
                            logger.error("Existe un error al aplicar el filtro básico de " + tipoMensaje);
                            throw new Exception("");
                        }
                    } else if (tipoMensaje.equalsIgnoreCase("PNS")) {
                        if (!browser.cell(SMS).leftOf(browser.cell(celdaMensaje)).exists() &&
                                !browser.cell(EMAIL).leftOf(browser.cell(celdaMensaje)).exists() &&
                                browser.cell(PNS).leftOf(browser.cell(celdaMensaje)).exists()) {
                            logger.info("Resultado correcto, sólo existen mensajes de tipo " + tipoMensaje);
                        } else {
                            logger.error("Existe un error al aplicar el filtro básico de " + tipoMensaje);
                            throw new Exception("");
                        }
                    } else if (tipoMensaje.equalsIgnoreCase("EMAIL")) {
                        if (!browser.cell(SMS).leftOf(browser.cell(celdaMensaje)).exists() &&
                                browser.cell(EMAIL).leftOf(browser.cell(celdaMensaje)).exists() &&
                                !browser.cell(PNS).leftOf(browser.cell(celdaMensaje)).exists()) {
                            logger.info("Resultado correcto, sólo existen mensajes de tipo " + tipoMensaje);
                        } else {
                            logger.error("Existe un error al aplicar el filtro básico de " + tipoMensaje);
                            throw new Exception("");
                        }
                    }
                    //Se cliquea nuevamente en el botón del tipo de mensaje por el que se filtró, esto para que se listen nuevamente mensajes de todos los tipos de mensaje
                    browser.button(tipoMensajeFiltrar).click();
                } else {
                    logger.error("No existe el elemnto HTML " + tipoMensajeFiltrar + " para el tipo de mensaje " + tipoMensaje);
                    throw new Exception("");
                }
            } else {
                logger.error("No existe el mensaje " + randomNum);
                throw new Exception("");
            }
            browser.submit("msg-details-filter").click();
        } else {
            logger.error("No existe el botón de filtro básico \"msg-details-filter\"");
            throw new Exception("");
        }
    }

    /**
     * Permite filtrar por tipo de mensaje desde la opción de filtro avanzado
     *
     * @param tipoMensaje tipo de mensaje por el cual se quiere filtrar
     */
    @Step("Filtro avanzado Filtrar por tipo de mensaje <>")
    public void filtrarPorTipoMensajeAvanzado(String tipoMensaje) throws Exception {
        String randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
        String celdaMensaje = "/(.*)" + randomNum + "(.*)/";
        String btnTipoMensajeFiltrar = "";

        if (browser.submit("msg-details-search").exists()) {
            logger.info("Existe msg-details-search");
            browser.submit("msg-details-search").click();
            //Dependiendo del tipo de mensaje que se quiere filtrar, se establecen los identificadores de los botones
            //dentro del browser, esto para no liarnos con los nombres de los identificadores dentro del código
            if (tipoMensaje.equalsIgnoreCase("SMS")) {
                btnTipoMensajeFiltrar = "search-msg-sms-toggle-button";
            } else if (tipoMensaje.equalsIgnoreCase("EMAIL")) {
                btnTipoMensajeFiltrar = "search-msg-email-toggle-button";
            } else if (tipoMensaje.equalsIgnoreCase("PNS")) {
                btnTipoMensajeFiltrar = "search-msg-pns-toggle-button";
            }

            //Si existe el botón correspondiente al tipo de mensaje requerido para el filtro entonces se aplica el filtro
            //y se comprueba la existencia de mensajes de solo el tipo de mensaje filtrado y no de otros
            ElementStub btnFiltrar = browser.button(btnTipoMensajeFiltrar);
            latiniaScenarioUtil.waitElement(btnFiltrar,5,2);
            if (browser.button(btnTipoMensajeFiltrar).exists()) {
                browser.button(btnTipoMensajeFiltrar).click();
                ElementStub buscar = browser.submit("BUSCAR");
                latiniaScenarioUtil.waitElement(buscar,5,2);
                if (browser.submit("BUSCAR").exists()) {
                    browser.submit("BUSCAR").click();
                    if (tipoMensaje.equalsIgnoreCase("SMS")) {
                        ElementStub celdaMensaje2 = browser.cell(celdaMensaje);
                        latiniaScenarioUtil.waitElement(celdaMensaje2,5,2);
                        if (browser.cell(SMS).leftOf(browser.cell(celdaMensaje)).exists() &&
                                !browser.cell(EMAIL).leftOf(browser.cell(celdaMensaje)).exists() &&
                                !browser.cell(PNS).leftOf(browser.cell(celdaMensaje)).exists()) {
                            logger.info("Resultado correcto, sólo existen mensajes de tipo " + tipoMensaje);
                        } else {
                            logger.error("Existe un error al aplicar el filtro avanzado de " + tipoMensaje);
                            throw new Exception("");
                        }
                    } else if (tipoMensaje.equalsIgnoreCase("PNS")) {
                        ElementStub celdaMensaje2 = browser.cell(celdaMensaje);
                        latiniaScenarioUtil.waitElement(celdaMensaje2,5,2);
                        if (!browser.cell(SMS).leftOf(browser.cell(celdaMensaje)).exists() &&
                                !browser.cell(EMAIL).leftOf(browser.cell(celdaMensaje)).exists() &&
                                browser.cell(PNS).leftOf(browser.cell(celdaMensaje)).exists()) {
                            logger.info("Resultado correcto, sólo existen mensajes de tipo " + tipoMensaje);
                        } else {
                            logger.error("Existe un error al aplicar el filtro avanzado de " + tipoMensaje);
                            throw new Exception("");
                        }
                    } else if (tipoMensaje.equalsIgnoreCase("EMAIL")) {
                        ElementStub celdaMensaje2 = browser.cell(celdaMensaje);
                        latiniaScenarioUtil.waitElement(celdaMensaje2,5,2);
                        if (!browser.cell(SMS).leftOf(browser.cell(celdaMensaje)).exists() &&
                                browser.cell(EMAIL).leftOf(browser.cell(celdaMensaje)).exists() &&
                                !browser.cell(PNS).leftOf(browser.cell(celdaMensaje)).exists()) {
                            logger.info("Resultado correcto, sólo existen mensajes de tipo " + tipoMensaje);
                        } else {
                            logger.error("Existe un error al aplicar el filtro avanzado de " + tipoMensaje);
                        }
                    }
                    //Finalmente se elimina el filtro
                    ElementStub icon_filter = browser.submit("msgs-filter-chip-icon");
                    latiniaScenarioUtil.waitElement(icon_filter,5,2);
                    if (browser.submit("msgs-filter-chip-icon").exists()) {
                        browser.submit("msgs-filter-chip-icon").click();
                    } else {
                        logger.error("No existe el icono para borrar filtros \"msgs-filter-chip-icon\"");
                        throw new Exception("");
                    }
                } else {
                    logger.error("No existe el botón BUSCAR para el filtro avanzado");
                    throw new Exception("");
                }
            } else {
                logger.error("No existe el elemnto HTML " + btnTipoMensajeFiltrar + " para el tipo de mensaje " + tipoMensaje);
                throw new Exception("");
            }
        } else {
            logger.error("No existe el botón de filtro avanzado \"msg-details-search\"");
            throw new Exception("");
        }
    }

    /**
     * Permite ver el detalle del mensaje en el apartado notificación
     *
     * @param tipoMensaje
     * @param proveedor
     * @param canal
     * @param destinatario
     * @throws Exception
     */
    @Step("Ver detalle de Mensaje Notificación tipoMensaje <> proveedor<> canal<> destinatario<>")
    public void detalleNotificacion(String tipoMensaje, String proveedor, String canal, String destinatario) throws Exception {
        ElementStub elementoTipoMensaje = null;
        String valorEmpresa = datosGlobales.get(Constantes.EMPRESA).toString();
        String valorServicio = datosGlobales.get(Constantes.NOM_CONTRATO).toString();
        String valorProducto = datosGlobales.get(Constantes.REF_PRODUCT).toString();
        String valorProveedor = proveedor;
        String valorRemitente = canal;
        String valorCliente = destinatario;
        String valorEnviado = "";
        String valorIdTransaccion = "";
        String valorIdMensaje = "";
        String valorIdMensajeInterno = "";

        List<String> listaCampos = new ArrayList<>();
        List<String> listaValores = new ArrayList<>();

        listaCampos.add("Empresa");
        listaCampos.add("Servicio");
        listaCampos.add("Aplicación/Producto");
        listaCampos.add("Proveedor de Canal");
        listaCampos.add("Cliente");
        listaCampos.add("ID Transacción");
        listaCampos.add("ID Mensaje");
        listaCampos.add("ID Mensaje interno");

        listaValores.add(valorEmpresa);
        listaValores.add(valorServicio);
        listaValores.add(valorProducto);
        listaValores.add(valorProveedor);
        listaValores.add(valorCliente);
        listaValores.add(valorEnviado);
        listaValores.add(valorIdTransaccion);
        listaValores.add(valorIdMensaje);
        listaValores.add(valorIdMensajeInterno);

        if (tipoMensaje.equalsIgnoreCase("SMS")) {
            listaCampos.add("Núm. remitente");
            listaValores.add(valorRemitente);
            elementoTipoMensaje = (ElementStub) datosGlobales.get(Constantes.ELEMENTO_SMS);
        } else if (tipoMensaje.equalsIgnoreCase("PNS")) {
            elementoTipoMensaje = (ElementStub) datosGlobales.get(Constantes.ELEMENTO_PNS);
        } else if (tipoMensaje.equalsIgnoreCase("EMAIL")) {
            listaCampos.add("Remitente de email");
            listaValores.add(valorRemitente);
            elementoTipoMensaje = (ElementStub) datosGlobales.get(Constantes.ELEMENTO_EMAIL);
        }
        if (elementoTipoMensaje != null) {
            elementoTipoMensaje.click();
            ElementStub notificacion = browser.span("/(.*)" + "Notificación" + "(.*)/");
            latiniaScenarioUtil.waitElement(notificacion,5,2);
            //Se verifica que exista el apartado Notificación
            if (browser.span("/(.*)" + "Notificación" + "(.*)/").exists()) {
                browser.span("/(.*)" + "Notificación" + "(.*)/").click();
                for (int i = 0; i < listaCampos.size(); i++) {
                    ElementStub th_listaCampos = browser.tableHeader(listaCampos.get(i));
                    latiniaScenarioUtil.waitElement(th_listaCampos,5,2);
                    if (browser.tableHeader(listaCampos.get(i)).exists()) {
                        if (listaCampos.get(i).equalsIgnoreCase("ID Transacción") || listaCampos.get(i).equalsIgnoreCase("ID Mensaje")
                                || listaCampos.get(i).equalsIgnoreCase("ID Mensaje interno")) {
                            if (listaCampos.get(i).equalsIgnoreCase("ID Transacción")) {
                                valorIdTransaccion = browser.span("/(.*)break-word(.*)/").in(browser.cell("/icon-cell/").rightOf(browser.tableHeader(listaCampos.get(i)))).getText();
                                if (valorIdTransaccion.length() > 0) {
                                    logger.info("Resultado correcto, existe campo " + listaCampos.get(i) + " con el valor " + valorIdTransaccion +
                                            " para el tipo de mensaje " + tipoMensaje);
                                    datosGlobales.put(Constantes.ID_TRANSACCION, valorIdTransaccion);
                                    datosGlobales.put(Constantes.TIPO_MENSAJE, tipoMensaje);
                                } else {
                                    logger.error("No existe el valor para el campo " + listaCampos.get(i));
                                    throw new Exception("");
                                }
                            } else {
                                valorIdMensaje = browser.span("/(.*)break-word(.*)/").in(browser.cell("/icon-cell/").rightOf(browser.tableHeader(listaCampos.get(i)))).getText();
                                if (valorIdMensaje.length() > 0) {
                                    logger.info("Resultado correcto, existe campo " + listaCampos.get(i) + " con el valor " + valorIdMensaje +
                                            " para el tipo de mensaje " + tipoMensaje);
                                } else {
                                    logger.error("No existe el valor para el campo " + listaCampos.get(i));
                                    throw new Exception("");
                                }
                            }
                        } else {
                            if (browser.cell("/(.*)" + listaValores.get(i) + "(.*)/").rightOf(browser.tableHeader(listaCampos.get(i))).exists()) {
                                logger.info("Resultado correcto, existe campo " + listaCampos.get(i) + " con el valor " + listaValores.get(i) +
                                        " para el tipo de mensaje " + tipoMensaje);
                            } else {
                                logger.error("No existe el valor " + listaValores.get(i) + " para el campo " + listaCampos.get(i));
                                throw new Exception("");
                            }
                        }

                    } else {
                        logger.error("No existe el campo " + listaCampos.get(i));
                        throw new Exception("");
                    }
                }
            } else {
                logger.error("No existe el apartado Notificación");
                throw new Exception("");
            }
            if (browser.button("search-msg-icon-close").exists()) {
                browser.button("search-msg-icon-close").click();
            } else {
                logger.error("No existe el notón CERRAR");
            }
        } else {
            logger.error("El elemento tipo mensaje es nulo");
            throw new Exception("");
        }
    }

    @Step("ver detalle contenido mensaje tipoMensaje<>")
    public void detalleContenidoMensaje(String tipoMensaje) throws Exception {
        ElementStub elementoTipoMensaje = null;
        String randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
        String celdaMensaje = "/(.*)" + randomNum + "(.*)/";
        String campoContenido = "";

        if (tipoMensaje.equalsIgnoreCase("SMS")) {
            elementoTipoMensaje = (ElementStub) datosGlobales.get(Constantes.ELEMENTO_SMS);
            campoContenido = "Mensaje";
        } else if (tipoMensaje.equalsIgnoreCase("PNS")) {
            elementoTipoMensaje = (ElementStub) datosGlobales.get(Constantes.ELEMENTO_PNS);
            campoContenido = "Mensaje público";
        } else if (tipoMensaje.equalsIgnoreCase("EMAIL")) {
            elementoTipoMensaje = (ElementStub) datosGlobales.get(Constantes.ELEMENTO_EMAIL);
            campoContenido = "Contenido principal";
        }

        if (elementoTipoMensaje != null) {
            elementoTipoMensaje.click();
            //Se verifica que exista el apartado Contenido
            if (browser.span("/(.*)" + "Contenido" + "(.*)/").exists()) {
                browser.span("/(.*)" + "Contenido" + "(.*)/").click();
                if (tipoMensaje.equalsIgnoreCase("EMAIL")) {
                    if (browser.span("msg-custom-box__title").exists()) {
                        if (browser.span(celdaMensaje).in(browser.div("msg-custom-box__content").rightOf(browser.span("msg-custom-box__title"))).exists()) {
                            logger.info("Resultado correcto, existe el campo " + "\"" + browser.span("msg-custom-box__title").getText() + "\"" + " con el valor " + randomNum);
                        } else {
                            logger.error("No existe el asunto " + randomNum);
                        }
                    } else {
                        logger.error("No existe \"msg-custom-box__title\" (Asunto)");
                        throw new Exception("");
                    }
                }
                if (browser.span(campoContenido).in(browser.div("release--box--header")).exists()) {
                    if (browser.div(celdaMensaje).in(browser.div("release--box--content")).exists()) {
                        logger.info("Resultado correcto, existe el campo " + campoContenido + " con el valor " + randomNum);
                    } else {
                        logger.error("No existe el contenido del mensaje " + randomNum);
                        throw new Exception("");
                    }
                } else {
                    logger.error("No existe el campo \"Contenido principal\"");
                    throw new Exception("");
                }
            } else {
                logger.error("No existe el apartado Contenido");
                throw new Exception("");
            }
            if (browser.button("search-msg-icon-close").exists()) {
                browser.button("search-msg-icon-close").click();
            } else {
                logger.error("No existe el notón CERRAR");
            }
        } else {
            logger.error("El elemento tipo mensaje es nulo");
            throw new Exception("");
        }
    }

    @Step("ver detalle estados mensaje tipoMEnsaje<>")
    public void detalleEstados(String tipoMensaje) throws Exception {
        ElementStub elementoTipoMensaje = null;
        String campoDetalle = "";

        if (tipoMensaje.equalsIgnoreCase("SMS")) {
            elementoTipoMensaje = (ElementStub) datosGlobales.get(Constantes.ELEMENTO_SMS);
            campoDetalle = "/(.*)" + "Detalle de estados" + "(.*)/";
        } else if (tipoMensaje.equalsIgnoreCase("PNS")) {
            elementoTipoMensaje = (ElementStub) datosGlobales.get(Constantes.ELEMENTO_PNS);
            campoDetalle = "/(.*)" + "Detalle de estados" + "(.*)/";
        } else if (tipoMensaje.equalsIgnoreCase("EMAIL")) {
            elementoTipoMensaje = (ElementStub) datosGlobales.get(Constantes.ELEMENTO_EMAIL);
            campoDetalle = "/(.*)" + "Destinatarios" + "(.*)/";
        }

        if (elementoTipoMensaje != null) {
            elementoTipoMensaje.click();
            //Se verifica que exista el apartado Contenido
            if (browser.span(campoDetalle).exists()) {
                browser.span(campoDetalle).click();
                if (browser.span("states__chip u-blue-color").exists()) {
                    if (browser.span("states__chip u-blue-color").getText().equals("1/2")) {
                        if (browser.div("/(.*)" + "Entregado al proveedor" + "(.*)/").exists()) {
                            logger.info("Existe el estado 1/2 - Entregado al proveedor para el tipo de mensaje " + tipoMensaje);
                        } else {
                            logger.error("No existe la descripción Entregado al proveedor");
                            throw new Exception("");
                        }
                    } else {
                        logger.error("No existe el estado 1/2");
                        throw new Exception("");
                    }
                } else {
                    logger.error("No existe \"states__chip u-blue-color\"");
                    throw new Exception("");
                }

                if (browser.span("states__chip u-green-color").exists()) {
                    if (browser.span("states__chip u-green-color").getText().equals("2/1")) {
                        if (browser.div("/(.*)" + "Finalizado en el proveedor" + "(.*)/").exists()) {
                            logger.info("Existe el estado 2/1 - Finalizado en el proveedor para el tipo de mensaje " + tipoMensaje);
                        } else {
                            logger.error("No existe la descripción Finalizado en el proveedor");
                            throw new Exception("");
                        }
                    } else {
                        logger.error("No existe el estado 2/1");
                        throw new Exception("");
                    }
                } else {
                    logger.error("No existe \"states__chip u-green-color\"");
                    throw new Exception("");
                }

            } else {
                logger.error("No existe el apartado " + campoDetalle);
                throw new Exception("");
            }
            if (browser.button("search-msg-icon-close").exists()) {
                browser.button("search-msg-icon-close").click();
            } else {
                logger.error("No existe el notón CERRAR");
            }
        } else {
            logger.error("El elemento tipo mensaje es nulo");
            throw new Exception("");
        }
    }

    /**
     * Permite buscar un mensaje específico por el id de la transacción
     */
    @Step("Filtro Avanzado filtrar mensaje por ID de Transacción")
    public void buscarPorIdTransaccionFiltroAvanzado() throws Exception {
        String randomNum = datosGlobales.get(Constantes.RANDOM_NUM).toString();
        String celdaMensaje = "/(.*)" + randomNum + "(.*)/";
        String tipoMensaje = datosGlobales.get(Constantes.TIPO_MENSAJE).toString();
        if (datosGlobales.get(Constantes.ID_TRANSACCION) != null) {
            String idTransaccion = datosGlobales.get(Constantes.ID_TRANSACCION).toString();
            ElementStub msgDetails = browser.submit("msg-details-search");
            latiniaScenarioUtil.waitElement(msgDetails,5,2);
            if (browser.submit("msg-details-search").exists()) {
                logger.info("Existe msg-details-search");
                browser.submit("msg-details-search").click();
                //Se verifica la existencia de la pestaña MENSAJES
                if (browser.div("/MENSAJES/").exists()) {
                    browser.div("/MENSAJES/").click();
                    //Se verifica la existencia de la opción de búsqueda por id de transacción
                    if (browser.radio("search-msg-transaction-radio-button-input").exists()) {
                        browser.radio("search-msg-transaction-radio-button-input").click();
                        //Se realiza la búsqueda por el id de transacción
                        if (browser.textbox("search-msg-transaction-input").exists()) {
                            browser.textbox("search-msg-transaction-input").setValue(idTransaccion);
                            browser.submit("BUSCAR").click();
                            List<ElementStub> listaElementos = browser.row("list-row-element mat-row ng-star-inserted").collectSimilar();
                            //Se comprueba que solo existe un elemento y que este corresponde al filtro realizado
                            browser.waitFor(5000);
                            if (listaElementos.size() == 1) {
                                if (tipoMensaje.equalsIgnoreCase("SMS")) {
                                    ElementStub celdaMensaje2 = browser.cell(celdaMensaje);
                                    latiniaScenarioUtil.waitElement(celdaMensaje2,5,2);
                                    if (browser.cell(SMS).leftOf(browser.cell(celdaMensaje)).exists() &&
                                            !browser.cell(EMAIL).leftOf(browser.cell(celdaMensaje)).exists() &&
                                            !browser.cell(PNS).leftOf(browser.cell(celdaMensaje)).exists()) {
                                        logger.info("Resultado correcto, sólo existe el mensaje " + idTransaccion + " de tipo " + tipoMensaje);
                                    } else {
                                        logger.error("Existe un error al aplicar el filtro avanzado de " + tipoMensaje);
                                        throw new Exception("");
                                    }
                                } else if (tipoMensaje.equalsIgnoreCase("PNS")) {
                                    ElementStub celdaMensaje2 = browser.cell(celdaMensaje);
                                    latiniaScenarioUtil.waitElement(celdaMensaje2,5,2);
                                    if (!browser.cell(SMS).leftOf(browser.cell(celdaMensaje)).exists() &&
                                            !browser.cell(EMAIL).leftOf(browser.cell(celdaMensaje)).exists() &&
                                            browser.cell(PNS).leftOf(browser.cell(celdaMensaje)).exists()) {
                                        logger.info("Resultado correcto, sólo existe el mensaje " + idTransaccion + " de tipo " + tipoMensaje);
                                    } else {
                                        logger.error("Existe un error al aplicar el filtro avanzado de " + tipoMensaje);
                                        throw new Exception("");
                                    }
                                } else if (tipoMensaje.equalsIgnoreCase("EMAIL")) {
                                    ElementStub celdaMensaje2 = browser.cell(celdaMensaje);
                                    latiniaScenarioUtil.waitElement(celdaMensaje2,5,2);
                                    if (!browser.cell(SMS).leftOf(browser.cell(celdaMensaje)).exists() &&
                                            browser.cell(EMAIL).leftOf(browser.cell(celdaMensaje)).exists() &&
                                            !browser.cell(PNS).leftOf(browser.cell(celdaMensaje)).exists()) {
                                        logger.info("Resultado correcto, sólo existe el mensaje " + idTransaccion + " de tipo " + tipoMensaje);
                                    } else {
                                        logger.error("Existe un error al aplicar el filtro avanzado de " + tipoMensaje);
                                        throw new Exception("");
                                    }
                                }

                                if (browser.submit("msgs-filter-chip-icon").exists()) {
                                    browser.submit("msgs-filter-chip-icon").click();
                                } else {
                                    logger.error("No existe el icono para borrar filtros \"msgs-filter-chip-icon\"");
                                    throw new Exception("");
                                }
                            } else {
                                System.out.println(listaElementos.size());
                                logger.error("La lista de mensajes después del filtro por transacción es diferente de 1");
                                throw new Exception("");
                            }
                        } else {
                            logger.error("No existe el campo de texto \"search-msg-id-message-input\"");
                            throw new Exception("");
                        }
                    } else {
                        logger.error("NO Existe el radioButton \"search-msg-id-message-radio-button-input\"");
                        throw new Exception("");
                    }
                } else {
                    logger.error("No Existe la pestaña (div) MENSAJES");
                    throw new Exception("");
                }
            } else {
                logger.error("No existe el botón \"msg-details-search\"");
                throw new Exception("");
            }
        } else {
            logger.error("No hay datos para buscar, idTransacción = null");
            throw new Exception("");
        }
    }
}
