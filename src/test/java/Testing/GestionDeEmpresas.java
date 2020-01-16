package Testing;// JUnit Assert framework can be used for verification

import LData_Testing.AccesoWSInf;
import LData_Testing.AccesoWSLData;
import LData_Testing.MEnterprise;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;

import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GestionDeEmpresas {

    private static Logger logger = LogManager.getLogger(GestionDeEmpresas.class);
    private Browser browser;
    LatiniaScenarioUtil latiniaScenarioUtil;
    AccesoWSInf accesoWSInf;
    AccesoWSLData accesoWSLData;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();
    MEnterprise mEnterprise;

    public GestionDeEmpresas() {
        browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
        latiniaScenarioUtil = new LatiniaScenarioUtil();
        accesoWSInf = new AccesoWSInf();
        accesoWSLData = new AccesoWSLData();
        mEnterprise = new MEnterprise();
    }

    @Step("GEmpresas <CAPITAL> <sin cambio>")
    public void gEmpresas(String refempresa1, String cambioorga) throws Exception {
        boolean activar = true;
        String refempresa = refempresa1;
        Properties propiedades;
        propiedades = new Properties();
        FileInputStream in = null;
        if (refempresa1.equals("")) {

            try {
                in = LatiniaScenarioUtil.readPropertiesLogIN();
                propiedades.load(in);
                refempresa = propiedades.getProperty("login");
                logger.info("Tomando del archivo de propiedades EMPRESA " + refempresa);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            logger.info("Tomando EMPRESA desde parámetros " + refempresa);
            refempresa = refempresa1;
        }


        String proposito = datosGlobales.get(Constantes.PROPOSITO).toString();
        String mngorganizations = datosGlobales.get(Constantes.MNG_ORGANIZATIONS).toString();
        int creado = 0;
        int creadoconsufijo = 0;
        int cont = 0;
        refempresa = refempresa.toUpperCase();

        browser.link("entTab").click();

        //Compruebo que la empresa INNOVUS tiene la organización LATINIA. Esto es simplemente una validacion para ver que todo esta correcto.
        if (!proposito.equals("provision")) {
            browser.div("INNOVUS").click();
            if (browser.div("LATINIA").exists()) {

            } else {
                logger.error("No se encuentra organización LATINIA");
                throw new Exception();
            }
            browser.div("INNOVUS").click();
        }

        //Creo la empresa 'refempresa' si no existe
        do {
            if (browser.italic("icon-plus").exists()) {
                browser.italic("icon-plus").click();
            }


            //Si la empresa no existe previamente, entonces la creo sin el sufijo del contador.
            if ((browser.div(refempresa).exists()) && (!proposito.equals("provision"))) {
                logger.info("La empresa " + refempresa + " ya existe. Se sigue con la validacion.");
            } else if (!browser.div(refempresa).exists()) {
                if (mEnterprise.poderCrearEmpresas()) {
                    if (cont == 0) {
                        logger.info("Creando empresa " + refempresa);
                        String empresaCreada = refempresa;
                        datosGlobales.put(Constantes.EMPRESA_CREADA, empresaCreada);
                        browser.textbox("login").setValue(refempresa);
                        browser.textbox("name").setValue(refempresa);
                        browser.textbox("nif").setValue(refempresa);
                        browser.button("btnSubmit").click();
                        if (browser.div("alert alert-error").exists() || (!browser.div(refempresa).exists())) {
                            logger.error("Creando la empresa " + refempresa + ". Revisa que el NIF no esté repetido. Revisa cuantas empresas permite crear la licencia");
                            throw new Exception(" ");
                        } else {
                            creado = 1;
                        }
                    }
                } else {
                    logger.info("INF: Se ha superado la cantidad máxima estabecida en la licencia, No se puede crear la nueva empresa");
                    creado = -1;
                }
            }

            //Cuando una empresa ya existia previamente, lo que hago es crear una nueva empresa con el mismo nomnbre pero agregando el sufijo numerico tras el nombre.
            if (proposito.equals("provision")) {
                if (mEnterprise.poderCrearEmpresas()) {
                    logger.info("Se puede crear la nueva empresa");
                    if (creado != 1) {
                        if (browser.div(refempresa + "_" + cont).exists()) {

                            cont++;

                        } else {
                            logger.info("Creando empresa " + refempresa + "_" + cont);
                            String empresaCreada = refempresa + "_" + cont;
                            datosGlobales.put(Constantes.EMPRESA_CREADA, empresaCreada);
                            browser.textbox("login").setValue(refempresa + "_" + cont);
                            browser.textbox("name").setValue(refempresa + "_" + cont);
                            browser.textbox("nif").setValue(refempresa + "_" + cont);
                            browser.button("btnSubmit").click();

                            if (browser.div("alert alert-error").exists()) {
                                logger.error("Error creando la empresa " + refempresa + "" + cont + ". Revisa que el NIF no esté repetido. Revisa cuantas empresas permite crear la licencia");
                                throw new Exception(" ");
                            } else {
                                creado = 1;
                                creadoconsufijo = 1;
                            }
                        }
                    }
                } else {
                    logger.info("Se ha superado la cantidad máxima estabecida en la licencia, No se puede crear la nueva empresa");
                    creado = -1;
                }
            }

            if (!proposito.equals("provision"))
                creado = 1; //Esto lo hago para que en caso de no estar aprovisionando, no entre en bucle

        } while (creado == 0);

        if (creado != -1) {
            //Desactivo y Activo la 'empresa' para comprobar funcionamiento de la funcionalidad.
            if ((creadoconsufijo == 0) && (proposito.equals("provision"))) {
                if (browser.italic("icon-pause").rightOf(browser.div(refempresa)).exists()) {
                    browser.italic("icon-pause").rightOf(browser.div(refempresa)).click();
                    browser.link("action1 btn btn-mini btn-warning").click();
                }
                if (browser.div("/Verifique que no tenga contratos y cláusulas activos./").exists()) {
                    logger.info("La empresa " + refempresa + " no pudo ser desactivada. Verifique que no tenga contratos y clausulas activos.");
                    activar = false;
                }

                if (browser.italic("icon-plus").exists()) {
                    browser.italic("icon-plus").click();
                }

                if (!refempresa.equals("ARBEITS")) { //En caso de ser ARBEITS no lo activo, para que así se queden cosas desactivadas
                    if (activar) {
                        if (browser.italic("icon-play btn-activate").rightOf(browser.div(refempresa)).exists()) {
                            browser.italic("icon-play btn-activate").rightOf(browser.div(refempresa)).click();
                            if (browser.div("alert alert-success").exists()) {

                            } else {
                                logger.error("Activacion de la empresa " + refempresa + " incorrecta");
                                throw new Exception(" ");
                            }
                        }
                    } else {
                        logger.info("Como la empresa no se pudo desactivar, entonces no se activa");
                    }
                }
            }

            if ((creadoconsufijo == 1) && (proposito.equals("provision"))) {
                if (browser.italic("icon-pause").near(browser.div(refempresa)).exists()) {
                    browser.italic("icon-pause").near(browser.div(refempresa + "_" + cont)).click();
                    browser.link("action1 btn btn-mini btn-warning").click();
                }


                if (browser.div("/Verifique que no tenga contratos y cláusulas activos./").exists()) {
                    logger.info("La empresa " + refempresa + "_" + cont + " no pudo ser desactivada. Verifique que no tenga contratos y clausulas activos.");
                }

                if (browser.italic("icon-plus").exists()) {
                    browser.italic("icon-plus").click();
                }

                if (!refempresa.equals("ARBEITS")) { //En caso de ser ARBEITS no lo activo, para que así se queden cosas desactivadas
                    browser.italic("icon-play btn-activate").near(browser.div(refempresa + "_" + cont)).click();
                    if (browser.div("alert alert-success").exists()) {

                    } else {
                        logger.error("Activacion de la empresa " + refempresa + "_" + cont + " incorrecta");
                        throw new Exception(" ");
                    }
                }

            }
            //**Me voy a 'organizaciones' solo si existe la propiedad 'MNG_ORGANIZATIONS'
            if (mngorganizations.equals("true")) {
                //Verifico que se ha creado la organización a partir de la creación anteriormente de una empresa
                if (!proposito.equals("provision")) {
                    //** Entro a Organizaciones **
                    browser.link("/Organiza/").click();

                    if (creadoconsufijo == 0) {
                        if (browser.div(refempresa).exists()) {

                        } else {
                            logger.error("La 'organización' no se ha creado para la 'empresa'" + refempresa, new Exception());
                            throw new Exception(" ");
                        }
                    }

                    if (creadoconsufijo == 1) {
                        if (browser.div(refempresa + "_" + cont).exists()) {

                        } else {
                            logger.error("La 'organización' no se ha creado para la 'empresa'" + refempresa + "_" + cont);
                            throw new Exception(" ");
                        }
                    }
                    browser.textbox("refOrg").setValue("miorgaREF");
                    browser.textbox("nameOrg").setValue("MiOrganizacion-AUTOMATED-Nombre");
                    browser.select("idEnt").choose("INNOVUS");

                }
                //Cambio de 'empresa' a la 'organización' que así lo indique en el parámetro "cambioorga"

                if (!cambioorga.equals("sin cambio")) {
                    if (browser.link("/Organiza/").exists()) {
                        browser.link("/Organiza/").click();
                    }

                    //Busco el link de 'edición', al lado de la organización indicada
                    if (creadoconsufijo == 0) {

                        browser.italic("icon-edit").rightOf(browser.div(refempresa).rightOf(browser.div(refempresa + "[3]"))).click();

                        if (browser.button("btnSubmit").exists()) {
                            //Si existe el botón quiere decir que ha encontrado correctamente la organización.
                        } else {
                            browser.link("action2 btn btn-mini").click();
                            browser.link("[1]").near(browser.div("col-icons").near(browser.div(refempresa + "[2]"))).click();
                        }
                    }
                    //lo mismo de antes pero con el sufijo del contador
                    if (creadoconsufijo == 1) {
//						browser.link("[1]").rightOf(browser.div(cambioorga).rightOf(browser.div(refempresa +"_" + cont + "[1]"))).click();
//                        browser.italic("icon-edit").rightOf(browser.div(refempresa + "_" + cont).rightOf(browser.div(refempresa + "_" + cont + "[2]"))).click();
                        browser.italic("icon-edit").rightOf(browser.cell(refempresa + "_" + cont)).click();

                        if (browser.button("btnSubmit").exists()) {
                            //Si existe el botón quiere decir que ha encontrado correctamente la organización.
                        } else {
                            browser.link("action2 btn btn-mini").click();
                            browser.link("[1]").near(browser.div("col-icons").near(browser.cell(refempresa + "_" + cont + "[2]"))).click();
                        }
                    }
                    String[] empresaanterior = browser.select("idEnt").getSelectedText(); //guardo el valor asignado actualmente, para poder restablecerlo mas tarde
                    browser.select("idEnt").choose(cambioorga.toUpperCase());
                    browser.button("btnSubmit").click(); //btn btn-mini btn-primary
                    browser.link("action1 btn btn-mini btn-warning").click(); //action1 btn btn-mini btn-warning

                    if (browser.div("alert alert-success").exists()) {
                        logger.info("La 'organizacion' " + empresaanterior + " ha sido cambiada de la empresa " + empresaanterior + " a la empresa " + cambioorga);
                    } else {
                        logger.error("Cambio de empresa fallido. La organizacion " + empresaanterior + " no ha sido cambiada de empresa.");
                        throw new Exception(" ");
                    }
                }
            }
        } else {
            logger.info("No se continua con las organizaciones porque la empresa no pudo crearse por cuestión de licencia");
        }
    }

    /**
     * Se verifica que existe la última empresa creada
     */
    @Step("Existe Empresa Creada")
    public void existeEmpresa() {
        String empresa;
        empresa = datosGlobales.get(Constantes.EMPRESA_CREADA).toString();
        if ((browser.div(empresa).exists())) {
            logger.info("la empresa " + empresa + "creada anteriormente Existe");
        }
    }

    /**
     * Se crea una organizacion asociada a una empresa
     *
     * @param organization nombre de la organización a crear
     * @param empresa      a la que se asociará la organización
     */
    @Step("Crear Organizacion <> <>")
    public void crearOrganizacion(String organization, String empresa) {
        if (browser.link("/Organiza/").exists()) {
            browser.link("/Organiza/").click();
        }
        //Borro la organización si existe previamente
        if (browser.cell(organization + "[1]").exists()) {
            logger.info("Existe");
            if (browser.italic("/icon-trash/").rightOf(browser.cell(organization + "[1]")).exists()) {
                browser.italic("/icon-trash/").rightOf(browser.cell(organization + "[1]")).click();
            }
            browser.link("Borrar").click();
            if (browser.div("La organización " + organization + " se ha eliminado correctamente").exists()) {
                logger.info("Se ha eliminado exitosamente la organización " + organization);
            }
        } else if (browser.cell(organization).exists()) { //Hago este else if porque por alguna razón que dezconozco la primera vez que creo una organización aparace con el sufijo [1], despues de hacer cambios desaparece el sufijo
            logger.info("Existe1");

            if (browser.italic("/icon-trash/").rightOf(browser.cell(organization)).exists()) {
                browser.italic("/icon-trash/").rightOf(browser.cell(organization)).click();
            }
            browser.link("Borrar").click();
            if (browser.div("La organización " + organization + " se ha eliminado correctamente").exists()) {
                logger.info("Se ha eliminado exitosamente la organización " + organization);
            }

        }
        // Creo la organización
        browser.textbox("refOrg").setValue(organization);
        browser.textbox("nameOrg").setValue(organization.toLowerCase());
        browser.select("idEnt").choose(empresa);
        browser.submit("Crear").click();
        if (browser.div("Organización " + organization.toLowerCase() + " creada correctamente").exists()) {
            logger.info("Se ha creado exitosamente la organización " + organization + " " + organization.toLowerCase() + " " + empresa);
        }
    }

}

