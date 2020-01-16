package Testing;

import com.sahipro.lang.java.client.Browser;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Permite comprobar difrentes elementos gráficos de la nueva herramienta Estadísticas de tráfico
 */
public class EstadisticasDeTrafico {
    private Browser browser;
    private static Logger logger = LogManager.getLogger(Estadisticas.class);
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

    public EstadisticasDeTrafico() {
        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
    }

    /**
     * Permite verificar la existencia de diferentes elementos en la pestaña "Tráfico por servicios"
     *
     * @throws Exception
     */
    @Step("Verificar tráfico por servicio")
    public void verificarTraficoPorServicios() throws Exception {
        //Se valida la existencia de la pestaña "Tráfico por servicios
        if (browser.link("statistics-contract-link").exists()) {
            browser.link("statistics-contract-link").click();
            validarCantidadDeMensajesEnviados();
            validarFechasPorPeriodoDeTiempo();
            validarEstadisticasPorCanales();
            validarEstadisticasPorPeriodoDeTiempo();
        } else {
            logger.error("No existe el link \"statistics-contract-link\"");
            throw new Exception("");
        }
    }

    /**
     * Permite comprobar que las fechas tanto inicial como final corresponden al periodo de tiempo seleccionado
     * Por DIA, SEMANA, MES o AÑO
     *
     * @throws Exception
     */
    private void validarFechasPorPeriodoDeTiempo() throws Exception {
        //Se obtiene la fecha actual en formato dd/MM/yyyy
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy");
        calendar.setTime(date);
        String diaActual = hourdateFormat.format(calendar.getTime());
        //Se obtiene la fecha correspondiente al día inicial de la semana actual
        calendar.set(calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        String primerDiaSemana = hourdateFormat.format(calendar.getTime());
        //Se obtiene la fecha correspondiente al día final de la semana actual
        calendar.add(calendar.DAY_OF_WEEK, 6);
        String ultimoDiaSemana = hourdateFormat.format(calendar.getTime());
        //Se obtiene la fecha correspondiente al día inicial del mes actual
        calendar.set(calendar.DAY_OF_MONTH, 1);
        String primerDiaMes = hourdateFormat.format(calendar.getTime());
        //Se obtiene la fecha correspondiente al día final del mes actual
        calendar.set(calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
        String ultimoDiaMes = hourdateFormat.format(calendar.getTime());
        //Se obtiene la fecha correspondiente al día inicial del año actual
        calendar.set(calendar.DAY_OF_YEAR, 1);
        String primerDiaAno = hourdateFormat.format(calendar.getTime());
        //Se obtiene la fecha correspondiente al día final del año actual
        calendar.set(calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        String ultimoDiaAno = hourdateFormat.format(calendar.getTime());

        List<String> listaPeriodoTiempo = new ArrayList<>();
        listaPeriodoTiempo.add("DÍA");
        listaPeriodoTiempo.add("SEMANA");
        listaPeriodoTiempo.add("MES");
        listaPeriodoTiempo.add("AÑO");
        String fechaInicial = "";
        String fechaFinal = "";
        String botonPeriodo = "";

        for (String periodo : listaPeriodoTiempo
        ) {
            if (periodo.equalsIgnoreCase("DÍA")) {
                fechaInicial = diaActual;
                fechaFinal = diaActual;
                botonPeriodo = "statistics-contracts-day-toggle-button";
            } else if (periodo.equalsIgnoreCase("SEMANA")) {
                fechaInicial = primerDiaSemana;
                fechaFinal = ultimoDiaSemana;
                botonPeriodo = "statistics-contracts-week-toggle-button";
            } else if (periodo.equalsIgnoreCase("MES")) {
                fechaInicial = primerDiaMes;
                fechaFinal = ultimoDiaMes;
                botonPeriodo = "statistics-contracts-month-toggle-button";
            } else if (periodo.equalsIgnoreCase("AÑO")) {
                fechaInicial = primerDiaAno;
                fechaFinal = ultimoDiaAno;
                botonPeriodo = "statistics-contracts-year-toggle-button";
            }
            //Se valida la existencia del botón del periodo en el filtro por fechas
            if (browser.button(botonPeriodo).exists()) {
                browser.button(botonPeriodo).click();
                //Se valida que la fecha inicial y la fecha final coincidan con el periodo actual
                if (browser.textbox("statistics-contracts-initial-date-input").exists()) {
                    if (browser.textbox("statistics-contracts-initial-date-input").getText().equals(fechaInicial)) {
                        if (browser.textbox("statistics-contracts-final-date-input").exists()) {
                            if (browser.textbox("statistics-contracts-final-date-input").getText().equals(fechaFinal)) {
                                logger.info("Resultado correcto para " + periodo + " " + fechaInicial + " - " + fechaFinal);
                            } else {
                                logger.error("No existe para fecha final " + fechaFinal + " para el rango de fecha " + periodo);
                                throw new Exception("");
                            }
                        } else {
                            logger.error("No existe el textbox \"statistics-contracts-final-date-input\"");
                            throw new Exception("");
                        }
                    } else {
                        logger.error("No existe para fecha inicial " + fechaInicial + " para el rango de fecha " + periodo);
                        throw new Exception("");
                    }
                } else {
                    logger.error("No existe el textbox \"statistics-contracts-initial-date-input\"");
                    throw new Exception("");
                }
            } else {
                logger.error("No existe el botón de priodo de tiempo " + "\"" + botonPeriodo + "\"");
                throw new Exception("");
            }
        }
    }

    /**
     * Permite comprobar la existencia de estadísticas por canal
     * Por SMS, EMAIL, PNS
     *
     * @throws Exception
     */
    private void validarEstadisticasPorCanales() throws Exception {
        //Se valida la existencia del botón DIA en el filtro por fechas
        if (browser.button("statistics-contracts-day-toggle-button").exists()) {
            browser.button("statistics-contracts-day-toggle-button").click();
            //Se valida la existencia del apartado estadísticas por canales
            if (browser.div("statistic-channels").exists()) {
                //Se construye una lista de canales
                List<String> listaCanales = new ArrayList<>();
                listaCanales.add("SMS");
                listaCanales.add("EMAIL");
                listaCanales.add("PNS");
                //Por cada canal se comprueba que existan mensajes enviados
                for (String canal : listaCanales
                ) {
                    if (browser.div("/" + canal + "/").in(browser.div("statistic-channels")).exists()) {
                        logger.info("Existen estadísticas por tipo de mensaje " + canal);
                        if (browser.div("statistic-channel-card--total").near(browser.div("/" + canal + "/").in(browser.div("statistic-channels"))).exists()) {
                            int cantidadMensajes = Integer.parseInt(browser.div("statistic-channel-card--total").near(browser.div("/" + canal + "/").in(browser.div("statistic-channels"))).getText());
                            if (cantidadMensajes > 0) {
                                logger.info("Resultado correcto para " + canal + ", existen " + cantidadMensajes + " mensajes");
                            } else {
                                logger.error("Cantidad de mensajes igual a " + cantidadMensajes + " para " + canal);
                                throw new Exception("");
                            }
                        } else {
                            logger.error("No existe el div \"statistic-channel-card--total\"");
                            throw new Exception("");
                        }
                    } else {
                        logger.error("No existen estadísticas por tipo de mensaje " + canal);
                        throw new Exception("");
                    }
                }
            } else {
                logger.error("No existe el apartado de estadísticas por canales (statistic-channels)");
                throw new Exception("");
            }
        } else {
            logger.error("No existe el botón \"statistics-contracts-day-toggle-button\"");
            throw new Exception("");
        }
    }

    /**
     * Permite comprobar la existencia de estadísticas por periodo de tiempo
     * Por DIA, SEMANA, MES, AÑO
     *
     * @throws Exception
     */
    private void validarEstadisticasPorPeriodoDeTiempo() throws Exception {
        //Se valida la existencia del apartado estadísticas por periodo de tiempo
        if (browser.div("period-charts").exists()) {
            //Se construye una lista de periodos de tiempo
            List<String> listaPeriodoDeTiempo = new ArrayList<>();
            listaPeriodoDeTiempo.add("DÍA");
            listaPeriodoDeTiempo.add("SEMANA");
            listaPeriodoDeTiempo.add("MES");
            listaPeriodoDeTiempo.add("AÑO");
            //Por cada periodo se comprueba que existan mensajes enviados
            for (String periodo : listaPeriodoDeTiempo
            ) {
                if (browser.div("/" + periodo + "/").in(browser.div("period-charts")).exists()) {
                    logger.info("Existen estadísticas por periodo de tiempo " + periodo);
                    int cantidadMensajes = Integer.parseInt(browser.div("mat-chip-list-wrapper").in(browser.div("/" + periodo + "/").in(browser.div("period-charts"))).getText());
                    if (cantidadMensajes > 0) {
                        logger.info("Resultado correcto para " + periodo + ", existen " + cantidadMensajes + " mensajes");
                    } else {
                        logger.error("Cantidad de mensajes igual a " + cantidadMensajes + " para " + periodo);
                        throw new Exception("");
                    }
                } else {
                    logger.error("No existen estadísticas por periodo de tiempo " + periodo);
                    throw new Exception("");
                }
            }
        } else {
            logger.error("No existe el apartado de estadísticas por periodo de tiempo (period-charts)");
            throw new Exception("");
        }
    }

    /**
     * Permite obtener la cantidad de mensajes enviados previos al envío de prueba
     *
     * @throws Exception
     */
    @Step("Número de mensajes privios al envío")
    public void contarMensajesPriviosAlEnvio() throws Exception {
        //Se valida la existencia del botón DIA en el filtro por fechas
        if (browser.button("statistics-contracts-day-toggle-button").exists()) {
            browser.button("statistics-contracts-day-toggle-button").click();
            //Se comprueba que exista el apartado Envío totales
            if (browser.div("statistics-total-sent-period").exists()) {
                logger.info(browser.div("statistics-total-sent-period").getText());
                //Se obtiene la cantidad de mensajes enviados
                int numeroMensajesActuales = Integer.parseInt(browser.span("").in(browser.div("statistics-total-sent-period")).getText());
                datosGlobales.put("numeroMensajesActuales", numeroMensajesActuales);
            } else {
                logger.error("No existe el campo Enviados (statistics-total-sent-period)");
                throw new Exception("");
            }
        } else {
            logger.error("No existe el botón \"statistics-contracts-day-toggle-button\"");
            throw new Exception("");
        }
    }

    /**
     * Permite comprobar que se han incrementado los mmnesajes enviados en 3, de este modo
     * se valida que se están registrando los mensajes enviados en el envío de prueba
     *
     * @throws Exception
     */
    private void validarCantidadDeMensajesEnviados() throws Exception {
        if (browser.div("statistics-total-sent-period").exists()) {
            int numeroMensajes = Integer.parseInt(browser.span("").in(browser.div("statistics-total-sent-period")).getText());
            int numeroMensajesActuales = (int) datosGlobales.get("numeroMensajesActuales");
            if ((numeroMensajes - numeroMensajesActuales) == 3) {
                logger.info("CORRECTO, mensajes antes " + numeroMensajesActuales + " - mensajes actuales " + numeroMensajes);
            } else {
                logger.error("Existe una diferencia de " + (numeroMensajes - numeroMensajesActuales));
                throw new Exception("");
            }
        } else {
            logger.error("No existe el div \"statistics-total-sent-period\"");
            throw new Exception("");
        }
    }
}
