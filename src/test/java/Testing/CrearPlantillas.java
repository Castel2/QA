package Testing;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import com.sahipro.lang.java.client.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CrearPlantillas {

    private static Logger logger = LogManager.getLogger(CrearPlantillas.class);
    private Browser browser;
    GestionDeCanales gestionDeCanales;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

    public CrearPlantillas() {
        gestionDeCanales = new GestionDeCanales();
        this.browser = LatiniaUtil.getBrowser(); //Instanciacion del Browser
    }

    @Step("Crear Seccion generica <0 - Encabezado> <\"\">")
    public void crearSeccionGenerica(String nombre, String texto) throws Exception {
        boolean existe = (boolean) datosGlobales.get(Constantes.EXISTE);
        String tipo = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();
        String idiomatexto = datosGlobales.get(Constantes.IDIOMA_TEXTO).toString();
        String nombreseccion = nombre + " " + tipo;
        if (existe) {

            if (tipo.equals("Email")) {
                texto = "<head>&#13;" +
                        "<meta http-equiv=Content-Type content=\"text/html; charset=windows-1252\">&#13;" +
                        "</head>&#13;" +
                        "<body lang=ES link=blue vlink=purple style='tab-interval:35.45pt'>&#13;" +
                        "<div class=Section1>&#13;" +
                        "<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=497 style='width:372.75pt;mso-cellspacing:0cm;mso-yfti-tbllook:1184;mso-padding-alt:0cm 0cm 0cm 0cm'>&#13;" +
                        " <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;mso-yfti-lastrow:yes'>&#13;" +
                        "  <td valign=top style='border:none; 1.0pt;padding: 7.5pt 0cm 0cm 3.75pt'>&#13;" +
                        "  <p class=MsoNormal style='margin-bottom:12.0pt'>&#13;" +
                        "\t<span style='font-size:9.0pt'>&#13;" +
                        "\t\t&#13;" +
                        "\t\t<br>&#13;" +
                        "\t\t<strong><span style='color:#999999'>Un camino juntos por recorrer</span></strong><br>&#13;" +
                        "\t</span>&#13;" +
                        "\t<span style='font-size:9.0pt;mso-no-proof:yes'><img width=99 height=49 id=\"_x0000_i1025\" src=\"http://www.bci.cl/medios/mails/bci/logo_new_bci.gif\" alt=\"logo_bci\"></span>&#13;" +
                        "\t<span style='font-size:9.0pt'></span></p>&#13;" +
                        "  </td>&#13;" +
                        " </tr>&#13;" +
                        "</table>&#13;" +
                        "<p class=MsoNormal></p>&#13;" +
                        "</div>&#13;" +
                        "</body>&#13;" +
                        "</html>&#13;" +
                        "&#13;" +
                        "<body lang=ES link=blue vlink=purple style='tab-interval:35.45pt'>&#13;" +
                        "<div class=Section1>&#13;" +
                        "<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=497 style='width:372.75pt;mso-cellspacing:0cm;mso-yfti-tbllook:1184;mso-padding-alt: 0cm 0cm 0cm 0cm'>&#13;" +
                        "\t<tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;mso-yfti-lastrow:yes'>&#13;" +
                        "\t\t<td valign=top style='border:none;border-top:solid #CCCCCC 1.0pt;background: #F2F2F2;mso-background-themecolor:background1;mso-background-themeshade:242; padding:7.5pt 0cm 0cm 3.75pt'>&#13;" +
                        "\t\t\t<p class=MsoNormal style='margin-bottom:12.0pt'>&#13;" +
                        "\t\t\t\t<span style='font-size:9.0pt;mso-no-proof:yes'><img width=197 height=72 id=\"_x0000_i1025\" src=\"http://www.bci.cl/medios/mails/bci/tj_009.gif\" alt=\"http://www.bci.cl/medios/mails/bci/tj_009.gif\"></span>&#13;" +
                        "\t\t\t\t<span style='font-size:9.0pt'></span>&#13;" +
                        "\t\t\t</p>&#13;" +
                        "\t\t</td>&#13;" +
                        "\t</tr>&#13;" +
                        "</table>&#13;" +
                        "</div>&#13;" +
                        "</body>&#13;" +
                        "</html>";
            }


            if (browser.link(tipo).exists()) {
                browser.link(tipo).click();
            } else {
                if (browser.listItem(tipo).exists()) {

                } else {
                    logger.error("No se encuentra el tipo" + tipo);
                    throw new Exception(" ");
                }
            }

            //crea una sección generica
            if (browser.link("Secciones genéricas").exists()) {
                browser.link("Secciones genéricas").click();
            }

            //Si existe la sección le creo un idioma dentro, si no, creo una sección nueva
            if (browser.cell(nombreseccion).exists()) {
                browser.image("ico_expand.png").near(browser.cell(nombreseccion)).click();
                browser.link("Crear").near(browser.cell(idiomatexto)).click();
                browser.textarea("secContent").setValue(texto);
                browser.submit("Guardar").click();

            } else {
                browser.button("Nueva sección").click();
                browser.textbox("nombre").setValue(nombreseccion);
                browser.submit("Aceptar").click();
                browser.button("Inicio").click();
                browser.image("ico_expand.png").near(browser.cell(nombreseccion)).click();
                browser.link("Crear").near(browser.cell(idiomatexto)).click();
                browser.textarea("secContent").setValue(texto);
                browser.submit("Guardar").click();
            }

            datosGlobales.put(Constantes.NOMBRE_SECCION, nombreseccion);

        } else

        {
            logger.info("No se puede crear la sección porque el tipo de mensaje NO está licenciado");
        }

    }

    @Step("Crear Plantilla <grupo Creadas Automated> <Mi plantilla> <refPlantilla0>")
    public void crearPlantilla(String grupo, String nombre, String reftemplate) throws Exception {
        boolean existe = (boolean) datosGlobales.get(Constantes.EXISTE);
        System.out.println(existe);
        String tipo = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();
        String idiomatexto = datosGlobales.get(Constantes.IDIOMA_TEXTO).toString();

        if (existe) {
            if (browser.link(tipo).exists()) {
                browser.link(tipo).click();
            } else {
                if (browser.listItem(tipo).exists()) {

                } else {
                    logger.error("No se encuentra el tipo " + tipo);
                    throw new Exception(" ");
                }

            }

            if (browser.link("Plantillas").exists()) {
                browser.link("Plantillas").click();
            }

            if (browser.cell(reftemplate).exists()) {
                browser.image("ico_expand.png").near(browser.cell(nombre)).click(); //Abro el menu
                if (browser.link("Editar").rightOf(browser.cell(idiomatexto)).under(browser.cell(reftemplate)).exists()) {
                    //Para el caso que la plantilla y el idioma ya existan
                    browser.link("Editar").rightOf(browser.cell(idiomatexto)).under(browser.cell(reftemplate)).click();
                } else if (browser.link("Crear").rightOf(browser.cell(idiomatexto)).under(browser.cell(reftemplate)).exists()) {
                    //Si existe la plantilla pero no el idioma, entonces agrego el idioma correspondiente
                    browser.link("Crear").rightOf(browser.cell(idiomatexto)).under(browser.cell(reftemplate)).click();
                    //browser.link("Crear").rightOf(browser.cell(idiomatexto)).click();
                    browser.submit("Aceptar").click();
                    browser.button("Continuar").click();
                    browser.button("<< Volver").click();
                }
            } else { //Si no existe la plantilla la creo de nueva
                browser.button("Nueva plantilla").click();
                browser.textbox("grupo").setValue(grupo);
                browser.textbox("nombre").setValue(nombre);
                browser.textbox("refTemplate").setValue(reftemplate);
                browser.submit("Aceptar").click();
                browser.button("Inicio").click();

                browser.image("ico_expand.png").near(browser.cell(nombre)).click();
                browser.link("Crear").near(browser.cell(idiomatexto)).click();
                browser.submit("Aceptar").click();
                browser.button("Continuar").click();
                browser.button("<< Volver").click();
            }
        } else {
            logger.info("No se puede crear la plantilla porque el tipo de mensaje " + tipo + " NO es licenciable");
        }

    }

    @Step("Asignar Seccion generica <0 - Encabezado> <refPlantilla0>")
    public void asignarSeccionGenerica(String nombresec, String reftemplate) throws Exception {
        boolean existe = (boolean) datosGlobales.get(Constantes.EXISTE);
        String tipo = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();
        String idiomatexto = datosGlobales.get(Constantes.IDIOMA_TEXTO).toString();

        if (existe) {

            if (browser.link(tipo).exists()) {
                browser.link(tipo).click();
            } else {
                if (browser.listItem(tipo).exists()) {

                } else {
                    logger.error("No se encuentra el tipo " + tipo);
                    throw new Exception(" ");
                }

            }

            if (browser.link("Plantillas").exists()) {
                browser.link("Plantillas").click();
            }

            if (browser.cell(reftemplate).exists()) {
                //Si existe la plantilla, me aseguro que el desplegable está abierto y entonces entro en el idioma correspondiente
                if (browser.image("ico_expand.png").leftOf(browser.cell(reftemplate)).exists()) {
                    browser.image("ico_expand.png").leftOf(browser.cell(reftemplate)).click();
                }
                browser.link("Editar").under(browser.cell(reftemplate)).rightOf(browser.cell(idiomatexto)).click();

            } else {
                throw new Exception("La Plantilla " + reftemplate + " no existe");
            }
            //Asigno a la plantilla indicada, las secciones genericas pasadas en el parámetro
            browser.select("sel-sec-gen").choose(nombresec + " " + tipo);
            browser.image("ico_arrow_right_yellow.png").near(browser.div("Secciones generales")).click();
            browser.button("<< Volver").click();
        } else {
            logger.info("No se puede asignar la sección genérica porque el tipo de mensaje no está licenciado");
        }
    }

    @Step("Crear Seccion especifica <Mi sección especifica0> <Contenido sección específica> <refPlantilla0> <forzarNO>")
    public void crearSeccionEspecifica(String nombre, String contenido, String reftemplate, String forzar) throws Exception {
        boolean existe = (boolean) datosGlobales.get(Constantes.EXISTE);
        String idioma = datosGlobales.get(Constantes.IDIOMA).toString();
        String idiomatexto = datosGlobales.get(Constantes.IDIOMA_TEXTO).toString();
        String tipo = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();
        String nombreseccionesp = idioma + " " + nombre + " " + tipo;
        String proposito = datosGlobales.get(Constantes.PROPOSITO).toString();
        if(existe) {

            if (browser.link(tipo).exists()) {
                browser.link(tipo).click();
            } else {
                if (browser.listItem(tipo).exists()) {

                } else {
                    logger.error("No se encuentra la pestaña para" + tipo);
                    throw new Exception(" ");
                }

            }

            if (browser.link("Plantillas").exists()) {
                browser.link("Plantillas").click();
            }

            if (browser.cell(reftemplate).exists()) {
                //Si existe la plantilla, me aseguro que el desplegable está abierto y entonces entro en el idioma correspondiente
                if (browser.image("ico_expand.png").leftOf(browser.cell(reftemplate)).exists()) {
                    browser.image("ico_expand.png").leftOf(browser.cell(reftemplate)).click();
                }
                browser.link("/Editar/").under(browser.cell(reftemplate)).rightOf(browser.cell(idiomatexto)).click();

            } else {
                logger.error("La Plantilla " + reftemplate + " no existe");
                throw new Exception(" ");
            }


            String selseccon = "";
            String selsecesp = "";

            try { //Tengo que hacer esta guarrada con el try-catch porque cuando "select("sel-sec-con")" está vacio, entonces peta el 'containsText'
                if (browser.containsText(browser.select("sel-sec-con"), nombreseccionesp)) {
                    selseccon = "siesta";
                } else {
                    selseccon = "noesta";
                }
            } catch (Exception e) {
                selseccon = "noesta";
            }

            try { //Tengo que hacer esta guarrada con el try-catch porque cuando "select("sel-sec-esp")" está vacio, entonces peta el 'containsText'
                if (browser.containsText(browser.select("sel-sec-esp"), nombreseccionesp)) {
                    selsecesp = "siesta";
                } else {
                    selsecesp = "noesta";
                }
            } catch (Exception e) {
                selsecesp = "noesta";
            }


            //Si la seccion que intento crear no existe ni asignada ni creada, entonces la creo
            if (selseccon.equals("noesta") && (selsecesp.equals("noesta"))) {
                //Si no existe la seccion, la creo
                browser.image("ico_paper_yellow.png").near(browser.div("Secciones específicas")).click();
                browser.textbox("nombre").setValue(nombreseccionesp);
                if ((tipo.equals("Email")) && (proposito.equalsIgnoreCase("FuncionesLeftRight"))) { //Si estoy probando las funciones "Left" y "Right" de plantillas

                    String texto = "<head>&#13;" +
                            "<meta http-equiv=Content-Type content=\"text/html; charset=windows-1252\">&#13;" +
                            "</head>&#13;" +
                            "<body lang=ES link=blue vlink=purple style='tab-interval:35.45pt'>&#13;" +
                            "<div id=\"contenido\">&#13;" +
                            "<p><font face=\"verdana\" color=\"green\">&#13;" +

                            "\t\t<br><limsp:left size='11'>Funcion IZQ<font face=\"verdana\" color=\"red\">ERROR lo ROJO a la derecha no debe aparecer: " + contenido + "</font></limsp:left><br>&#13;" + //Solamente aparecera la palabra 'aparezcoIZQ'; esto es asi debido a la función LEFT
                            "\t\t<limsp:right size='11'><font face=\"verdana\" color=\"red\">" + contenido + "ERROR lo ROJO a la izquierda no debe aparecer</font> Funcion DER</limsp:right><br>&#13;" + //Solamente aparecera la palabra 'aparezcoDER'; esto es asi debido a la función RIGHT
                            "\t\t" + idiomatexto + "&#13;" +

                            "</font></p><br>&#13;" +
                            "</div>&#13;" +
                            "</body>&#13;" +
                            "</html>";

                    browser.textarea("secContent").setValue(texto);
                } else if ((tipo.equals("Email")) && (proposito.equalsIgnoreCase("FuncionesIMG"))) { //Si estoy probando la funcion "IMG" de plantillas
                    String texto = "<head>&#13;" +
                            "<meta http-equiv=Content-Type content=\"text/html; charset=windows-1252\">&#13;" +
                            "</head>&#13;" +
                            "<body lang=ES link=blue vlink=purple style='tab-interval:35.45pt'>&#13;" +
                            "<div id=\"contenido\">&#13;" +
                            "<p><font face=\"verdana\" color=\"green\">Aqui debajo sale la imagen 'HomeLimsp.png' &#13;" +
                            "\t\t<br><limsp:img>HomeLimsp.png</limsp:img><br>&#13;" +
                            "\t\t" + idiomatexto + "&#13;" +
                            "</font></p><br>&#13;" +
                            "</div>&#13;" +
                            "</body>&#13;" +
                            "</html>";

                    browser.textarea("secContent").setValue(texto);
                } else if ((tipo.equals("Email")) && (proposito.equalsIgnoreCase("FuncionesEQUALS"))) { //Si estoy probando la funcion "EQUALS" de plantillas
                    String texto = "<head>&#13;" +
                            "<meta http-equiv=Content-Type content=\"text/html; charset=windows-1252\">&#13;" +
                            "</head>&#13;" +
                            "<body lang=ES link=blue vlink=purple style='tab-interval:35.45pt'>&#13;" +
                            "<div id=\"contenido\">&#13;" +
                            "<p><font face=\"verdana\" color=\"green\">&#13;" +
                            "\t\t<br><limsp:equals left=\"Hola\" IGNORECASE=\"true\" right=\"hola\">OK</limsp:equals><br>&#13;" +
                            "\t\t" + idiomatexto + "&#13;" +
                            "</font></p><br>&#13;" +
                            "</div>&#13;" +
                            "</body>&#13;" +
                            "</html>";

                    browser.textarea("secContent").setValue(texto);
                } else if ((tipo.equals("Email")) && (proposito.equalsIgnoreCase("FuncionesURLEnconde"))) { //Si estoy probando la funcion "EQUALS" de plantillas
                    String texto = "<head>&#13;" +
                            "<meta http-equiv=Content-Type content=\"text/html; charset=windows-1252\">&#13;" +
                            "</head>&#13;" +
                            "<body lang=ES link=blue vlink=purple style='tab-interval:35.45pt'>&#13;" +
                            "<div id=\"contenido\">&#13;" +
                            "<p><font face=\"verdana\" color=\"green\">&#13;" +
                            "\t\t<br><limsp:urlEnconde>/TZlwXl8ZYyIh4GGv/QR8UnL0k4=</limsp:urlEnconde><br>&#13;" +
                            "\t\t" + idiomatexto + "&#13;" +
                            "</font></p><br>&#13;" +
                            "</div>&#13;" +
                            "</body>&#13;" +
                            "</html>";

                    browser.textarea("secContent").setValue(texto);
                }else if (tipo.equals("Email")) { //Si es email, pero no pruebo las "funciones" de plantillas

                    String texto = "<head>&#13;" +
                            "<meta http-equiv=Content-Type content=\"text/html; charset=windows-1252\">&#13;" +
                            "</head>&#13;" +
                            "<body lang=ES link=blue vlink=purple style='tab-interval:35.45pt'>&#13;" +
                            "<div id=\"contenido\">&#13;" +
                            "<p><font face=\"verdana\" color=\"green\">&#13;" +

                            "\t\t<br>" + contenido + "<br>&#13;" +
                            "\t\t" + idiomatexto + "&#13;" +

                            "</font></p><br>&#13;" +
                            "</div>&#13;" +
                            "</body>&#13;" +
                            "</html>";

                    browser.textarea("secContent").setValue(texto);
                } else {
                    browser.textarea("secContent").setValue(" " + contenido + " " + idiomatexto + " ");
                }

                browser.submit("Aceptar").click();


                if (browser.containsText(browser.select("sel-sec-esp"), nombreseccionesp)) {
                    selsecesp = "siesta";
                    logger.info("Creada seccion especifica " + nombreseccionesp);
                } else {
                    logger.error("Problema creando seccion especifica " + nombreseccionesp);
                    throw new Exception(" ");
                }

            }


            if (selsecesp.equals("noesta") && selseccon.equals("siesta")) {
                //Si ya está asignada la seccion especifica
                logger.info("La seccion " + nombreseccionesp + " ya esta asignada. No hago nada");
            } else if (selsecesp.equals("siesta") && selseccon.equals("noesta")) {
                //Asigno la seccion especifica
                browser.select("sel-sec-esp").choose(nombreseccionesp);
                browser.image("ico_arrow_right_yellow.png").near(browser.div("Secciones específicas")).click();
                browser.select("sel-sec-con").choose(nombreseccionesp);
                browser.image("ico_arrow_up_yellow.png").click();
            }


            browser.button("<< Volver").click();
            //browser.link("Secciones específicas").click();


            if (forzar.equals("forzar")) {
                if (browser.span("hbtn home").exists()) { //Ultima release de R3.9.4
                    browser.span("hbtn home").click();
                }
                browser.link("Forzar recarga").click();
                browser.button("Volver").click();
            }

        }else{
            logger.info("No se puede crear la sección específica porque el tipo de mensaje no esta licenciado");
        }

    }


    @Step("Borrar Antigua <plantilla> <refPlantilla0>")
    public void borrarAntigua(String elque, String nombre) throws Exception {
        boolean existe = (boolean) datosGlobales.get(Constantes.EXISTE);
        String tipo = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();
        String alerta = "falso"; //Lo utilizo para el caso en que aparece el aviso de borrar secciones con vinculos a plantillas.

        if (browser.link(tipo).exists()) {
            browser.link(tipo).click();
        } else {
            if (browser.listItem(tipo).exists()) {

            } else {
                logger.error("No se encuentra el tipo " + tipo);
                throw new Exception(" ");
            }

        }

        if (existe) {
            if (elque.equals("plantilla")) {
                if (browser.link("Plantillas").exists()) {
                    browser.link("Plantillas").click();
                }
                //Comprueba si existe la Plantilla y la borra
                if (browser.cell(nombre).exists()) {
                    browser.link("Borrar").rightOf(browser.cell(nombre)).click();
                    if (browser.button("Borrar").exists()) {
                        browser.button("Borrar").click();
                        logger.info("Borrada plantilla " + nombre);
                    }
                    browser.button("Inicio").click();
                }
            }

            if (elque.equals("seccion")) {
                if (browser.link("Secciones genéricas").exists()) {
                    browser.link("Secciones genéricas").click();
                }

                //Comprueba si existe la seccion y la borra
                if (browser.cell(nombre + " " + tipo).exists()) {
                    browser.link("Borrar").rightOf(browser.cell(nombre + " " + tipo)).click();
                    logger.info("Borrando seccion " + nombre + " " + tipo);

                    //Confirmo el borrado de la seccion
                    try {
                        //Compruebo si aparece el mensaje de aviso para secciones CON vinculos
                        alerta = browser.getText(browser.div("infotext alert[0]"));
                    } catch (Exception e) {
                    }

                    if (alerta.equals("falso")) {
                        //Para el caso SIN vinculos
                        browser.expectConfirm("/¿Desea borrar la sección (" + nombre + " " + tipo + ")?/", true);
                        logger.info("Se ha borrado la seccion SIN vinculos " + nombre + " " + tipo);
                    } else {
                        //Para el caso CON vinculos
                        browser.button("Borrar").click();
                        logger.info("Se ha borrado la seccion CON vinculos " + nombre + " " + tipo);
                    }

                    //Borrada la seccion, regreso al menu de plantillas.
                    if (browser.div("infotext ok[0]").exists()) {
                        browser.button("Inicio").click();
                    } else {
                        logger.error("Borrando seccion " + nombre + " " + tipo);
                        throw new Exception(" ");
                    }
                }
            }
        } else {
            logger.info("No Se pueden crear los elementos porque el tipo de mensaje " + tipo + " NO está licenciado");
        }
    }

    @Step("VisualizarPlantilla <refPlantilla0>")
    public void visualizarPlantilla(String reftemplate) throws Exception {
        boolean existe = (boolean) datosGlobales.get(Constantes.EXISTE);
        String tipo = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();
        String idiomatexto = datosGlobales.get(Constantes.IDIOMA_TEXTO).toString();
        if(existe) {
            if (browser.link(tipo).exists()) {
                browser.link(tipo).click();
            } else {
                if (browser.listItem(tipo).exists()) {

                } else {
                    throw new Exception("No se encuentra el tipo " + tipo);
                }
            }

            if (browser.link("Plantillas").exists()) {
                browser.link("Plantillas").click();
            }

            if (browser.image("ico_expand.png").near(browser.cell(reftemplate)).exists()) {
                browser.image("ico_expand.png").near(browser.cell(reftemplate)).click();
            }

            logger.info("Abriendo preview de plantilla");
            browser.link("Visualizar").near(browser.cell(idiomatexto)).click();
            Browser templWin = browser.popup("template"); //Obtenemos acceso a la nueva ventana (cuyo nombre es 'template'). Para ello instancio la clase Browser en el objeto templWin, a la cual sahi obtiene acceso
            //Compruebo el contenido de la plantilla
            logger.info("Comprobando la plantilla " + tipo + " en la ventana de popup");
            if (tipo.equals("Email")) {
                if (templWin.containsText(templWin.div("contenido"), "/Contenido/")) {
                    logger.info("BIEN: Plantilla " + tipo + " correcta");
                } else {
                    logger.error("Plantilla " + tipo + " >> " + reftemplate + ". No contiene el resultado esperado");
                    throw new Exception(" ");
                }
            } else {
                if (templWin.containsText(templWin.byXPath("/html/body"), "/Contenido/")) {
                    logger.info("BIEN: Plantilla " + tipo + " correcta");
                } else {
                    logger.error("Plantilla " + tipo + " >> " + reftemplate + ". No contiene el resultado esperado");
                    throw new Exception(" ");
                }
            }


            logger.info("Cerrando ventana de 'preview'");
            //templWin.close();


        }else{
            logger.info("No se puede visualizar la plantilla porque el tipo de mensaje no esta licenciado");
        }

    }

    @Step("Establece Idioma")
    public void estableceIdioma() throws Exception {
        String idioma = datosGlobales.get(Constantes.IDIOMA).toString();
        String idiomatexto = datosGlobales.get(Constantes.IDIOMA_TEXTO).toString();


        if (!idioma.equals("--")) {

            if (browser.span("hbtn help").exists()) {
                browser.link("Idiomas[1]").click(); //Esto para cuando esta instalada la documentacion
                //System.out.println("INF: Doc instalada");
                logger.info("Doc instalada");
            } else {
                browser.link("Idiomas").click(); //Esto para cuando NO esta instalada la documentacion
                logger.info("Doc NO instalada");
//                if(browser.select("refLang").exists()){
//                    if(browser.cell(idioma).exists()) {
//                        System.out.println("Existe idioma " + idioma);
//                        if (browser.select("refLang").containsText(idioma)) {
//                            browser.select("refLang").choose(idioma);
//                            browser.button("/Añadir/").click();
//                        }
//                    }else{
//                        System.out.println("NO Existe idioma " + idioma);
//                    }
//                }
            }


            if (browser.cell(idioma).exists()) {
                //Compruebo si existe el idioma, y si no existe lo creo
            } else {
                browser.select("refLang").choose(idiomatexto);
                browser.submit("Añadir").click();
                if (browser.cell(idioma).exists()) {
                    //Aqui compruebo si ha creado el idioma
                } else {
                    logger.error("La creacion del idioma ha ido mal");
                    throw new Exception(" ");
                }
            }
        }

    }

    /**
     * dramirez Permite crear una variable (emojis)
     * para usarse en las plantillas
     * @param nombre valor correspondiente al nombre
     *  de la variable a crear
     * @throws Exception
     */
    @Step("Crear Variable <Emoji Automated>")
    public void crearVariable(String nombre)throws Exception{
        String valor = datosGlobales.get(Constantes.VALOR).toString();
        menuPlantillaVariables();
        if (existeVariableDirecta(nombre.toLowerCase())){
            if(borrarVariable(nombre.toLowerCase())){
                browser.link("Email").click();
                crearVariable(nombre.toLowerCase());
            }else{
                logger.error("No se pudo borrar la variable directa");
                throw new Exception("");
            }
        }else{
            browser.textbox("varName").setValue(nombre);
            browser.textarea("varValue").setValue(valor);
            browser.textbox("varDesc").setValue("descripción de la variable " + nombre);
            browser.submit("Crear").click();
            //Verifica si el GUI reporta error en la operacion de creación y se sale del programa en tal caso
            if (browser.div("La petición no se ha procesado correctamente").exists()) {
                logger.error("Problema al crear una variable directa");
                throw new Exception(" ");
            } else {
                logger.info("Creada variable directa " + nombre);
            }
        }
    }

    /**
     * dramirez, Permite cambiar entre sección
     * en el menu de gestión de plantillas
     */
    public void menuPlantillaVariables(){
        browser.link("Variables").click();
        if (browser.link("Directas").exists()) {
            browser.link("Directas").click();
        }
    }

    /**
     * dramirez, borra una variable que se encuentre
     * creada, la eliminación la hace de acuerdo a su nombre
     * @param nombre corresponde al nombre de la variable a borrar
     * @return falso en caso de no borrar la variable, true en caso de borrarla
     * @throws Exception
     */
    public boolean borrarVariable(String nombre)throws Exception{
        boolean borrada = false;
        if (existeVariableDirecta(nombre)) {
            browser.span("Borrar").near(browser.cell(nombre)).click();
            browser.expectConfirm("¿Desea borrar la variable '" + nombre + "'?", true);
            logger.info("Borrada variable directa " + nombre);
            if(existeVariableDirecta(nombre)){
                borrada = false;
                logger.error("No se borro correctamente la variable directa");
                throw new Exception("");
            }else{
                borrada =true;
            }
        }else{
            logger.error("No existe la variable directa a borrar");
            throw new Exception("");
        }
        return borrada;
    }

    /**
     * dramirez, retorna un estado dependiendo
     * de si existe o no una variable
     * @param nombre
     * @return
     */
     public boolean existeVariableDirecta(String nombre){
        boolean existe =false;
         if (browser.cell(nombre).exists()) {
             existe = true;
         }else{
             existe = false;
         }
         return existe;
     }

    /**
     * dramirez, Crea un mapa de valores en
     * la herramienta de gestión de plantillas
     * @param nombre corresponde al valor del nombre del
     *         mapa de valores
     * @throws Exception
     */
    @Step("Crear Mapa Valores <Emoji Automated>")
    public void crearMapaValores(String nombre)throws Exception{
        menuMapaValores();
        if(browser.cell(nombre.toLowerCase()).exists()){
            logger.info("Ya existe el mapa de valores, se borrará para crear uno nuevo");
            if(borrarMapaValores(nombre.toLowerCase())){
                crearMapaValores(nombre.toLowerCase());
            }else{
                logger.error("No se pudo borrar el Mapa de valores");
                throw new Exception("");
            }
        }else{
            browser.textbox("varName").setValue(nombre);
            browser.textbox("varDesc").setValue(nombre);
            browser.submit("Crear").click();
            editarMapaValores(nombre.toLowerCase());
        }

    }

    /**
     * dramirez, borra un mapa de valores en caso de que exista
     * la eliminación la hace de acuerdo al nombre
     * @param nombre corresponde al mapa de valores a eliminar
     * @return
     * @throws Exception
     */
    public boolean borrarMapaValores(String nombre) throws Exception {
        boolean borrado = false;
        if (browser.cell("/"+nombre+"/").exists()) {
            browser.span("Borrar").near(browser.cell(nombre)).click();
            if(existeMapaValores(nombre)){
                logger.error("No se borro correctamente el Mapa de valores");
                throw new Exception("");
            }else{
                logger.info("Mapa de valores borrado correctamente");
                borrado = true;
            }
        }else{
            logger.error("No existe el Mapa de Valores a borrar");
            throw  new Exception("");
        }
        return borrado;
    }

    /**
     * dramirez, se verifica si existe o no
     * el mapa de valores de acuerdo a su nombre
     * @param nombre corresponde al mapa de valores
     * al cual se verificara su existencia
     * @return
     */
    public boolean existeMapaValores(String nombre){
        boolean existe = false;
        if(browser.cell(nombre.toLowerCase()).exists()) {
            existe = true;
        }else{
            existe = false;
        }
        return existe;
    }

    /**
     * dramirez, se edita un mapa de valores
     * se le crea una clave y se le asocia un valor
     * @param nombre corresponde al mapa de valores a editar
     * @throws Exception
     */
    public void editarMapaValores(String nombre) throws Exception {
        if (browser.cell("/"+nombre+"/").exists()) {
            logger.info("Mapa de valores creado correctamente");
            browser.span("Editar").near(browser.cell(nombre)).click();
            browser.textbox("mapName").setValue("clave_automated");
            browser.textarea("mapValue").setValue("😍 🤪 🤓" + "\uD83D\uDE0E");
            browser.submit("Crear").click();
            if(verificarCreacionClaves("clave_automated")){
                logger.info("Se creo correctamente la clave del mapa de valores");
            }else{
                logger.error("No se creo correctamente la clave del mapa de valores");
                throw new Exception("");
            }
        }else{
            logger.error("No se creo el mapa de valores");
            throw new Exception("");
        }
    }

    /**
     * dramirez, permite hacer cambios
     * de secciones en la herramienta
     * gestión de plantillas.
     */
    public void menuMapaValores(){
        browser.link("Email").click();
        browser.link("Variables").click();
        if (browser.link("Mapas de valores").exists()) {
            browser.link("Mapas de valores").click();
        }
    }

    /**
     * dramirez, se verifica la existencia o no
     * de una clave y su valor, en un mapa de valores
     * @param nombre
     * @return
     */
    public boolean verificarCreacionClaves(String nombre){
        boolean existe = false;
        if(browser.cell(nombre).in(browser.table("tbl-var-map-val")).exists()){
            existe= true;
        } else{
            existe=false;
        }
        return  existe;
    }



    @Step("Establece Variables <mi_variable_directa>")
    public void estableceVariables(String nombre) throws Exception {

        //Borro antigua variable en caso que existiese previamente
        browser.link("Variables").click();

        if (browser.link("Directas").exists()) {
            browser.link("Directas").click();
        }
        if (browser.cell(nombre).exists()) {
            browser.span("Borrar").near(browser.cell(nombre)).click();
            browser.expectConfirm("¿Desea borrar la variable '" + nombre + "'?", true);

            logger.info("Borrada variable directa " + nombre);
        }

        //Creo variable DIRECTA
        browser.textbox("varName").setValue(nombre);
        browser.textarea("varValue").setValue("valor de " + nombre);
        browser.textbox("varDesc").setValue("descripción de la variable " + nombre);
        browser.submit("Crear").click();
        //Verifica si el GUI reporta error en la operacion de creación y se sale del programa en tal caso
        if (browser.div("La petición no se ha procesado correctamente").exists()) {

            logger.error("Problema al crear una variable directa");
            throw new Exception(" ");
        } else {

            logger.info("Creada variable directa " + nombre);
        }

        //Borro la variable DIRECTA que acabo de crear
        if (browser.cell(nombre).exists()) {
            browser.span("Borrar").rightOf(browser.cell(nombre)).click();
            browser.expectConfirm("¿Desea borrar la variable '" + nombre + "'?", true);
            logger.info("Borrada variable directa " + nombre);
        }
        //Verifico que se haya borrado
        if (browser.cell(nombre).exists()) {
            logger.error("Borrando variable directa " + nombre);
            throw new Exception(" ");
        }


        //Voy a la pestaña de RESERVADAS
        if (browser.link("Reservadas").exists()) {
            browser.link("Reservadas").click();
        } else {
            logger.error("No existe la pestaña de 'variables reservadas'");
            throw new Exception(" ");
        }
    }

    @Step("Existe tipo de mensaje")
    public boolean existeTipoMsg() {
        String tipoClause = datosGlobales.get(Constantes.TIPO_CLAUSE).toString();
        String[] typeMsg = gestionDeCanales.tipoMensaje();
        boolean exist = false;
        boolean flag = false;
        for (String val : typeMsg) {
            if (val.equalsIgnoreCase(tipoClause)) {
                flag = true;
                break;
            }
        }
        if (!browser.link(tipoClause).exists()) {
            if (!flag) {
                exist = false;
            }
        } else {
            exist = true;
        }

        datosGlobales.put(Constantes.EXISTE, exist);
        return exist;
    }

}