package LData_Testing;

import Testing.Constantes;
import Testing.GestionDeCanales;
import com.latinia.limsp.ldata.mtemplatemanagerfacade.ws.Ws_ld_mtemplatemanagerPortStub;
import com.latinia.util.ldata.lxobjects.LXValidationLData;
import com.latinia.util.lxobjects.LXException;
import com.latinia.util.lxobjects.LXList;
import com.latinia.util.lxobjects.LXObject;
import com.latinia.util.lxobjects.LXSerializer;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by amartinez on 3/05/2018.
 */
public class MTemplateMAnager {

    private static Logger logger = LogManager.getLogger(MTemplateMAnager.class);
    AccesoWSLData accesoWSLData;
    List<Object> retorna;
    GestionDeCanales gestionDeCanales;
    DataStore datosGlobales = DataStoreFactory.getSpecDataStore();

    String contenido = " ";
    String contenidoEmail = "<head><meta http-equiv=Content-Type content=\"text/html; charset=windows-1252\">" +
            "</head><body lang=ES link=blue vlink=purple style='tab-interval:35.45pt'><div class=Section1>" +
            "<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=497 style='width:372.75pt;" +
            "mso-cellspacing:0cm;mso-yfti-tbllook:1184;mso-padding-alt:0cm 0cm 0cm 0cm'><tr style='mso-yfti-irow:0;" +
            "mso-yfti-firstrow:yes;mso-yfti-lastrow:yes'><td valign=top style='border:none; 1.0pt;padding: 7.5pt 0cm 0cm 3.75pt'>" +
            "<p class=MsoNormal style='margin-bottom:12.0pt'><span style='font-size:9.0pt'><br><strong>" +
            "<span style='color:#999999'>Un camino juntos por recorrer</span></strong><br></span>" +
            "<span style='font-size:9.0pt;mso-no-proof:yes'><img width=99 height=49 id=\"_x0000_i1025\" " +
            "src=\"http://www.bci.cl/medios/mails/bci/logo_new_bci.gif\" alt=\"logo_bci\"></span><span " +
            "style='font-size:9.0pt'></span></p></td></tr></table><p class=MsoNormal>" +
            "</p></div></body></html><body lang=ES link=blue vlink=purple style='tab-interval:35.45pt'>" +
            "<div class=Section1><table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=497 " +
            "style='width:372.75pt;mso-cellspacing:0cm;mso-yfti-tbllook:1184;mso-padding-alt: 0cm 0cm 0cm 0cm'>" +
            "<tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;mso-yfti-lastrow:yes'><td valign=top style='border:none;" +
            "border-top:solid #CCCCCC 1.0pt;background: #F2F2F2;mso-background-themecolor:background1;" +
            "mso-background-themeshade:242; padding:7.5pt 0cm 0cm 3.75pt'><p class=MsoNormal " +
            "style='margin-bottom:12.0pt'><span style='font-size:9.0pt;mso-no-proof:yes'><img width=197 height=72 " +
            "id=\"_x0000_i1025\" src=\"http://www.bci.cl/medios/mails/bci/tj_009.gif\" " +
            "alt=\"http://www.bci.cl/medios/mails/bci/tj_009.gif\"></span><span style='font-size:9.0pt'></span></p>" +
            "</td></tr></table></div></body></html>";
    // String contenidoEmail = "<head><meta http-equiv=Content-Type content=\"text/html; charset=windows-1252\"></head><body lang=ES link=blue vlink=purple style='tab-interval:35.45pt'><div id=\"contenido\"><p><font face=\"verdana\" color=\"green\"><br>#contenido#<br>#idioma#</font></p><br></div></body></html>";

    public MTemplateMAnager() {
        accesoWSLData = new AccesoWSLData();
        gestionDeCanales = new GestionDeCanales();
    }


    /**
     * Permite crear la plantilla y contenido de plantilla vacío
     *
     * @throws Exception
     */
    @Step("Crear Plantilla WS")
    public void crearPlantilla() throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTTEMPLATE, Constantes.LATINIA, Constantes.WlURL_MTEMPLATE,
                Constantes.WASURL_MTEMPLATE, Constantes.WS_LD_MTEMPLATE_SERVICE, Constantes.WS_LD_MTEMPLATE_LOCATOR,
                Constantes.WS_LD_MTEMPLATE_PORTSTUB, Constantes.WS_LD_MTEMPLATE_METHOD, Constantes.CONT_WGESTTEMPLATE);
        Ws_ld_mtemplatemanagerPortStub portStub = (Ws_ld_mtemplatemanagerPortStub) retorna.get(0);
        //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
        LXValidationLData validation = (LXValidationLData) retorna.get(1);


        String refTemplate = datosGlobales.get(Constantes.REF_TEMPLATE).toString();
        String tipoMensaje = datosGlobales.get(Constantes.TIPO_MENSAJE).toString();
        String grupo = datosGlobales.get(Constantes.GRUPO).toString();
        String nomPlantilla = datosGlobales.get(Constantes.NOM_PLANTILLA).toString();
        String tipoContenido = datosGlobales.get(Constantes.TIPO_CONTENIDO).toString();
        String idTemplate = "";
        String idContenidoPlantilla = "";
        String idioma = datosGlobales.get(Constantes.IDIOMA).toString();
        //Se verifica si el tipo de mensaje está licenciado
        if (existeMensajeLicencia(tipoMensaje)) {
            //Si existe la plantilla se procede a borrarla para crearla posteriormente si no existe
            if (existePlantilla(refTemplate)) {
                String idTemplateBorrar = datosGlobales.get(Constantes.ID_TEMPLATE).toString();
                eliminarPlantilla(idTemplateBorrar);
                //System.out.println("INF: La plantilla " + refTemplate + " ya existe para el tipo de mensaje " + tipoMensaje + ", se procede a eliminarla");
                logger.info("La plantilla " + refTemplate + " ya existe para el tipo de mensaje " + tipoMensaje + ", se procede a eliminarla");
            }

            idTemplate = portStub.createTemplate(validation.toString(), refTemplate, tipoMensaje, grupo, nomPlantilla, tipoContenido);
            idContenidoPlantilla = portStub.createTemplateContent(validation.toString(), idTemplate, idioma);

            if (!idTemplate.isEmpty()) {
                //System.out.println("INF: Se crea la plantilla " + refTemplate);
                logger.info("Se crea la plantilla " + refTemplate);
                datosGlobales.put(Constantes.ID_TEMPLATE, idTemplate);
            } else {
                logger.error("La plantilla " + refTemplate + " no se pudo crear");
                //System.out.println("ERR: La plantilla " + refTemplate + " no se pudo crear");
                throw new Exception(" ");
            }

            if (!idContenidoPlantilla.isEmpty()) {
                //System.out.println("INF: Se crea contenido para plantilla " + refTemplate);
                logger.info("Se crea contenido para plantilla " + refTemplate);
                datosGlobales.put(Constantes.ID_CONTENIDO_PLANTILLA, idContenidoPlantilla);
            } else {
                //System.out.println("ERR: El contenido para plantilla " + refTemplate + " no se pudo crear");
                //throw new Exception("ERR: El contenido para plantilla " + refTemplate + " no se pudo crear");
                logger.error("El contenido para plantilla " + refTemplate + " no se pudo crear");
                throw new Exception(" ");
            }


        } else {
            //System.out.println("INF: El tipo de mensaje " + tipoMensaje + " no esta licenciado, NO se puede crear la plantilla");
            logger.info("El tipo de mensaje " + tipoMensaje + " no esta licenciado, NO se puede crear la plantilla");
        }
    }

    /**
     * Permite crear la seccion general de un canal e idioma especificos
     *
     * @throws RemoteException
     */
    @Step("Crear seccion general")
    public void crearSeccionGeneral() throws RemoteException, LXException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTTEMPLATE, Constantes.LATINIA, Constantes.WlURL_MTEMPLATE,
                Constantes.WASURL_MTEMPLATE, Constantes.WS_LD_MTEMPLATE_SERVICE, Constantes.WS_LD_MTEMPLATE_LOCATOR,
                Constantes.WS_LD_MTEMPLATE_PORTSTUB, Constantes.WS_LD_MTEMPLATE_METHOD, Constantes.CONT_WGESTTEMPLATE);
        Ws_ld_mtemplatemanagerPortStub portStub = (Ws_ld_mtemplatemanagerPortStub) retorna.get(0);
        //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String nomSeccion = datosGlobales.get(Constantes.NOMBRE_SECCION).toString();
        String idSeccionGeneral;
        String idContenidoSeccionGeneral;
        String tipoMensaje = datosGlobales.get(Constantes.TIPO_MENSAJE).toString();
        String tipoContenido = datosGlobales.get(Constantes.TIPO_CONTENIDO).toString();
        String idioma = datosGlobales.get(Constantes.IDIOMA).toString();
        String texto = "";
        if (existeMensajeLicencia(tipoMensaje)) {
            String idContenidoPlantilla = datosGlobales.get(Constantes.ID_CONTENIDO_PLANTILLA).toString();
            if (!existeSeccionGeneral(nomSeccion)) {
                if (tipoMensaje.equalsIgnoreCase("email")) {
                    texto = contenidoEmail;
                } else {
                    texto = contenido;

                }
                idSeccionGeneral = portStub.createTemplateSection(validation.toString(), nomSeccion, "0", tipoMensaje, tipoContenido);
                idContenidoSeccionGeneral = portStub.createTemplateSectionContent(validation.toString(), idSeccionGeneral, idioma);
                portStub.updateTemplateSectionContentData(validation.toString(), idContenidoSeccionGeneral, texto);
                portStub.linkSectionToTemplateContent(validation.toString(), idContenidoPlantilla, idContenidoSeccionGeneral);
                datosGlobales.put(Constantes.ID_CONTENIDO_SECCION_GENERAL, idContenidoSeccionGeneral);
            } else {
                //System.out.println("INF: La sección " + nomSeccion + " ya existe");
                logger.info("La sección " + nomSeccion + " ya existe");
                idSeccionGeneral = datosGlobales.get(Constantes.ID_TEMPLATE_SECTION).toString();
                idContenidoSeccionGeneral = obtenerIdContenidoSeccion(idSeccionGeneral, idioma);
                portStub.linkSectionToTemplateContent(validation.toString(), idContenidoPlantilla, idContenidoSeccionGeneral);
            }
        } else {
            //System.out.println("INF: El tipo de mensaje " + tipoMensaje + " no esta licenciado, NO se puede crear contenido para sección general");
            logger.info("El tipo de mensaje " + tipoMensaje + " no esta licenciado, NO se puede crear contenido para sección general");
        }
    }


    /**
     * Permite crear la seccion general de un canal e idioma especificos
     *
     * @throws RemoteException
     */
    @Step("Crear seccion especifica")
    public void crearSeccionEspecifica() throws RemoteException, LXException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTTEMPLATE, Constantes.LATINIA, Constantes.WlURL_MTEMPLATE,
                Constantes.WASURL_MTEMPLATE, Constantes.WS_LD_MTEMPLATE_SERVICE, Constantes.WS_LD_MTEMPLATE_LOCATOR,
                Constantes.WS_LD_MTEMPLATE_PORTSTUB, Constantes.WS_LD_MTEMPLATE_METHOD, Constantes.CONT_WGESTTEMPLATE);
        Ws_ld_mtemplatemanagerPortStub portStub = (Ws_ld_mtemplatemanagerPortStub) retorna.get(0);
        //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idSeccionEspecifica;
        String idContenidoSeccionEspecifica = "";
        String tipoMensaje = datosGlobales.get(Constantes.TIPO_MENSAJE).toString();
        String tipoContenido = datosGlobales.get(Constantes.TIPO_CONTENIDO).toString();
        String nomSeccion = datosGlobales.get(Constantes.NOMBRE_SECCION).toString();
        String idioma = datosGlobales.get(Constantes.IDIOMA).toString();
        if (existeMensajeLicencia(tipoMensaje)) {
            String texto = " contenido sección especifica para " + tipoMensaje + " para idioma " + idioma;

            String idContenidoPlantilla = datosGlobales.get(Constantes.ID_CONTENIDO_PLANTILLA).toString();
            idSeccionEspecifica = portStub.createTemplateSpecificSectionContent(validation.toString(), idContenidoPlantilla, tipoMensaje, tipoContenido, idioma, nomSeccion, texto);
            idContenidoSeccionEspecifica = obtenerIdContenidoSeccion(idSeccionEspecifica, idioma);
            portStub.linkSectionToTemplateContent(validation.toString(), idContenidoPlantilla, idContenidoSeccionEspecifica);
            datosGlobales.put(Constantes.ID_CONTENIDO_SECCION_ESPECIFICA, idSeccionEspecifica);
        } else {
            //System.out.println("INF: El tipo de mensaje " + tipoMensaje + " no esta licenciado, NO se puede crear contenido para sección especifico");
            logger.info("El tipo de mensaje " + tipoMensaje + " no esta licenciado, NO se puede crear contenido para sección especifico");
        }
    }

    /**
     * Permite obtener el id del contenido de seccion de una plantilla a apartir del id de la seccion
     *
     * @param idSeccion id de seccion a la cual se le obtendrá el id de contenido de seccion de un idioma especifico
     * @param idioma    idioma del contenido que se quiere obtener
     * @return
     * @throws RemoteException
     * @throws LXException
     */
    public String obtenerIdContenidoSeccion(String idSeccion, String idioma) throws RemoteException, LXException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTTEMPLATE, Constantes.LATINIA, Constantes.WlURL_MTEMPLATE,
                Constantes.WASURL_MTEMPLATE, Constantes.WS_LD_MTEMPLATE_SERVICE, Constantes.WS_LD_MTEMPLATE_LOCATOR,
                Constantes.WS_LD_MTEMPLATE_PORTSTUB, Constantes.WS_LD_MTEMPLATE_METHOD, Constantes.CONT_WGESTTEMPLATE);
        Ws_ld_mtemplatemanagerPortStub portStub = (Ws_ld_mtemplatemanagerPortStub) retorna.get(0);
        //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String idContenidoSeccion = "";

        String listaContenidoSeccionPlantillas = portStub.listTemplateSectionContents(validation.toString(), idSeccion);

        LXList lxListaContenidoSeccionPlantillas = (LXList) LXSerializer.readLX(listaContenidoSeccionPlantillas);
        for (int idx = 0; idx < lxListaContenidoSeccionPlantillas.getSize(); idx++) {
            LXObject xObj = (LXObject) lxListaContenidoSeccionPlantillas.getObject(idx);
            if (xObj.getPropertyValue(Constantes.REF_LANG).equalsIgnoreCase(idioma)) {
                idContenidoSeccion = xObj.getPropertyValue(Constantes.ID_TEMPLATE_SECTION_CONTENT);
                break;
            }
        }
        return idContenidoSeccion;
    }

    /**
     * Permite verificar la existencia de una seccion general
     *
     * @param nomSeccion nombre de la sección a verificar
     * @return
     * @throws RemoteException
     * @throws LXException
     */
    public boolean existeSeccionGeneral(String nomSeccion) throws RemoteException, LXException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTTEMPLATE, Constantes.LATINIA, Constantes.WlURL_MTEMPLATE,
                Constantes.WASURL_MTEMPLATE, Constantes.WS_LD_MTEMPLATE_SERVICE, Constantes.WS_LD_MTEMPLATE_LOCATOR,
                Constantes.WS_LD_MTEMPLATE_PORTSTUB, Constantes.WS_LD_MTEMPLATE_METHOD, Constantes.CONT_WGESTTEMPLATE);
        Ws_ld_mtemplatemanagerPortStub portStub = (Ws_ld_mtemplatemanagerPortStub) retorna.get(0);
        //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
        LXValidationLData validation = (LXValidationLData) retorna.get(1);
        String tipoMensaje = datosGlobales.get(Constantes.TIPO_MENSAJE).toString();
        String listaSeccionGeneral = portStub.listCommonTemplateSections(validation.toString(), tipoMensaje);
        LXList lxSecGeneral = (LXList) LXSerializer.readLX(listaSeccionGeneral);

        boolean existe = false;

        for (int idx = 0; idx < lxSecGeneral.getSize() && !existe; idx++) {
            LXObject xObj = (LXObject) lxSecGeneral.getObject(idx);
            if (xObj.getPropertyValue(Constantes.NAME).equalsIgnoreCase(nomSeccion)) {
                existe = true;
                String idSeccionGeneral = xObj.getPropertyValue(Constantes.ID_TEMPLATE_SECTION);
                datosGlobales.put(Constantes.ID_TEMPLATE_SECTION, idSeccionGeneral);
            }
        }

        return existe;
    }

    /**
     * Permite eliminar una plantilla
     *
     * @param idTemplate
     * @throws RemoteException
     */
    public void eliminarPlantilla(String idTemplate) throws RemoteException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTTEMPLATE, Constantes.LATINIA, Constantes.WlURL_MTEMPLATE,
                Constantes.WASURL_MTEMPLATE, Constantes.WS_LD_MTEMPLATE_SERVICE, Constantes.WS_LD_MTEMPLATE_LOCATOR,
                Constantes.WS_LD_MTEMPLATE_PORTSTUB, Constantes.WS_LD_MTEMPLATE_METHOD, Constantes.CONT_WGESTTEMPLATE);
        Ws_ld_mtemplatemanagerPortStub portStub = (Ws_ld_mtemplatemanagerPortStub) retorna.get(0);
        //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        portStub.deleteTemplate(validation.toString(), idTemplate);
    }

    /**
     * Permite verificar la existencia de una plantilla
     *
     * @param refTemplate plantilla a verificar
     * @return
     * @throws Exception
     */
    public boolean existePlantilla(String refTemplate) throws Exception {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTTEMPLATE, Constantes.LATINIA, Constantes.WlURL_MTEMPLATE,
                Constantes.WASURL_MTEMPLATE, Constantes.WS_LD_MTEMPLATE_SERVICE, Constantes.WS_LD_MTEMPLATE_LOCATOR,
                Constantes.WS_LD_MTEMPLATE_PORTSTUB, Constantes.WS_LD_MTEMPLATE_METHOD, Constantes.CONT_WGESTTEMPLATE);
        Ws_ld_mtemplatemanagerPortStub portStub = (Ws_ld_mtemplatemanagerPortStub) retorna.get(0);
        //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
        LXValidationLData validation = (LXValidationLData) retorna.get(1);

        String tipoMensaje = datosGlobales.get(Constantes.TIPO_MENSAJE).toString();
        boolean existe = false;

        String listaPlantillas = portStub.listTemplates(validation.toString(), tipoMensaje);

        LXList lxPlantillas = (LXList) LXSerializer.readLX(listaPlantillas);

        for (int idx = 0; idx < lxPlantillas.getSize() && !existe; idx++) {
            LXObject xObj = (LXObject) lxPlantillas.getObject(idx);

            if (xObj.getPropertyValue(Constantes.REF_TEMPLATE).equalsIgnoreCase(refTemplate)) {
                existe = true;
                String idTemplate = xObj.getPropertyValue(Constantes.ID_TEMPLATE);
                datosGlobales.put(Constantes.ID_TEMPLATE, idTemplate);
            }
        }
        return existe;
    }

    /**
     * Permite verificar en la licencia un tipo de mensaje
     *
     * @param tipoMensaje
     * @return
     */
    public boolean existeMensajeLicencia(String tipoMensaje) {
        boolean existe = false;

        String[] typeMsg = gestionDeCanales.tipoMensaje();

        for (String val : typeMsg
        ) {
            if (val.equalsIgnoreCase(tipoMensaje)) {
                existe = true;
            }
        }
        return existe;
    }

    @Step("Agregar idioma Plantilla pordefecto<>")
    public void agregarIdiomaPlantillas(boolean defecto) throws Exception, LXException {
        retorna = accesoWSLData.wsLDataGeneric(Constantes.APP_WGESTTEMPLATE, Constantes.LATINIA, Constantes.WlURL_MTEMPLATE,
                Constantes.WASURL_MTEMPLATE, Constantes.WS_LD_MTEMPLATE_SERVICE, Constantes.WS_LD_MTEMPLATE_LOCATOR,
                Constantes.WS_LD_MTEMPLATE_PORTSTUB, Constantes.WS_LD_MTEMPLATE_METHOD, Constantes.CONT_WGESTTEMPLATE);

        Ws_ld_mtemplatemanagerPortStub portStub = (Ws_ld_mtemplatemanagerPortStub) retorna.get(0);
        //En la segunda posición (1) de "retorna" se obtiene el string de validacion al LData
        LXValidationLData validation = (LXValidationLData) retorna.get(1);
        boolean existe = false;
        boolean existe1 = false;
        String idioma = datosGlobales.get(Constantes.IDIOMA).toString();
        String def;

        String listaLenguajesPlantillas = portStub.listTemplatesLanguages(validation.toString());
        LXList lxLenguajesPlantillas = (LXList) LXSerializer.readLX(listaLenguajesPlantillas);

        for (int idx = 0; idx < lxLenguajesPlantillas.getSize() && !existe1; idx++) {
            LXObject xObj = (LXObject) lxLenguajesPlantillas.getObject(idx);

            if (xObj.getPropertyValue(Constantes.LANG).equalsIgnoreCase(idioma)) {
                existe1 = true;
            }
        }

        if (defecto) {
            def = "1";
        } else {
            def = "0";
        }

        if (existe1) {
            logger.info("Se agrega el idioma al mapa de lenguajes");
            portStub.deleteLanguageMapping(validation.toString(), idioma);
            portStub.addLanguageMapping(validation.toString(), idioma, idioma, def);
        } else {
            logger.error("No existe el idioma " + idioma + " para plantillas");
            throw new Exception(" ");
        }

    }
}


