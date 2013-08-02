package org.semanticwb.bsc.element;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.*;
import org.semanticwb.Logger;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBPortal;
import org.semanticwb.SWBUtils;
import org.semanticwb.bsc.BSC;
import org.semanticwb.model.GenericIterator;
import org.semanticwb.model.SWBComparator;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticProperty;
import org.semanticwb.portal.SWBFormMgr;
import org.semanticwb.portal.api.*;
import org.semanticwb.bsc.element.base.SummaryViewManagerBase;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.platform.SemanticOntology;


/**
 * Recurso que administra instancias de tipo {@code SummaryView}. Permite crear,
 * editar y eliminar objetos {@code SummaryView} y asignar como vista de despliegue 
 * una en particular.
 * @author jose.jimenez
 */
public class SummaryViewManager extends SummaryViewManagerBase {


    /** Realiza operaciones en la bitacora de eventos. */
    private static Logger log = SWBUtils.getLogger(GenericSemResource.class);

    /**
     * Construye una instancia de tipo {@code SummaryViewManager}
     */
    public SummaryViewManager() {
    }

   /**
   * Constructs a SummaryViewManager with a SemanticObject
   * @param base The SemanticObject with the properties for the SummaryViewManager
   */
    public SummaryViewManager(SemanticObject base) {

        super(base);
    }

    /**
     * Genera el c&oacute;digo HTML que representa la vista de este recurso en el 
     * espacio asignado del lado de la aplicaci&oacute;n. Por si mismo genera el 
     * c&oacute;digo HTML o delega esta tarea a otros m&eacute;todos o JSP's.
     * @param request la petici&oacute;n HTTP enviada por el cliente
     * @param response la respuesta HTTP que se enviar&aacute; al cliente
     * @param paramRequest objeto por el que se accede a varios exclusivos de SWB
     * @throws SWBResourceException si se presenta alg&uacute;n problema dentro 
     *         de la plataforma de SWB para la correcta ejecuci&oacute;n del m&eacute;todo.
     *         Como la extracci&oacute;n de valores para par&aacute;metros de i18n.
     * @throws IOException  si ocurre un problema con la lectura/escritura de la petici&oacute;n/respuesta.
     */
    @Override
    public void doView(HttpServletRequest request, HttpServletResponse response,
            SWBParamRequest paramRequest) throws SWBResourceException, IOException {

        PrintWriter out = response.getWriter();
        out.println("Hello SummaryViewManager...");
    }

    /**
     * Muestra la interface por la que el usuario puede especificar la clase de 
     * objetos que se desplegar&aacute;n en la vista de la aplicaci&oacute;n o aquella
     * en la que se administran las instancias de la clase especificada. La clase 
     * de objetos a administrar solo puede especificarse una vez y a apartir de ese momento
     * se puede administrar las instancias de la clase seleccionada.
     * @param request la petici&oacute;n HTTP enviada por el cliente
     * @param response la respuesta HTTP que se enviar&aacute; al cliente
     * @param paramRequest objeto por el que se accede a varios exclusivos de SWB
     * @throws SWBResourceException si se presenta alg&uacute;n problema dentro 
     *         de la plataforma de SWB para la correcta ejecuci&oacute;n del m&eacute;todo.
     *         Como la extracci&oacute;n de valores para par&aacute;metros de i18n.
     * @throws IOException si ocurre un problema con la lectura/escritura de la petici&oacute;n/respuesta.
     */
    @Override
    public void doAdmin(HttpServletRequest request, HttpServletResponse response,
            SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        
        boolean workClassIsValid = false;
        if (this.getWorkClass() != null) {
            SemanticClass semWorkClass = this.getWorkClass().transformToSemanticClass();
            workClassIsValid = semWorkClass.getRootClass().equals(BSCElement.bsc_BSCElement);
        }
        if (workClassIsValid) {
            //Una vez que se especifica la clase de objetos, se puede administrar las instancias
            doShowForm(request, response, paramRequest);
        } else {
            super.doAdmin(request, response, paramRequest);
        }
    }

    /**
     * Muestra la interface que permite al usuario administrar las vistas resumen.
     * @param request la petici&oacute;n HTTP enviada por el cliente
     * @param response la respuesta HTTP que se enviar&aacute; al cliente
     * @param paramRequest objeto por el que se accede a varios exclusivos de SWB
     * @throws SWBResourceException si se presenta alg&uacute;n problema dentro 
     *         de la plataforma de SWB para la correcta ejecuci&oacute;n del m&eacute;todo.
     *         Como la extracci&oacute;n de valores para par&aacute;metros de i18n.
     * @throws IOException si ocurre un problema con la lectura/escritura de la petici&oacute;n/respuesta.
     */
    public void doShowForm(HttpServletRequest request, HttpServletResponse response,
            SWBParamRequest paramRequest) throws SWBResourceException, IOException {

        PrintWriter out = response.getWriter();
        StringBuilder output = new StringBuilder(512);
        String viewUri = request.getParameter("viewUri") != null
                         ? request.getParameter("viewUri") : "";
        String suri = request.getParameter("suri");
        String operation = request.getParameter("operation") == null
                           ? "add" : request.getParameter("operation");
        String statusMsg = request.getParameter("statusMsg");
        StringBuilder baseListHtml = new StringBuilder(256);
        StringBuilder viewListHtml = new StringBuilder(256);
        
        SemanticClass semWorkClass = this.getWorkClass().transformToSemanticClass();
        
        //Poner validacion de clase a utilizar, debe ser descendiente de BSCElement
        String lang = paramRequest.getUser().getLanguage();
        Iterator<SemanticProperty> basePropertiesList = semWorkClass.listProperties();
        SWBFormMgr formMgr = null;
        String modeUsed = null;
        SWBResourceURL url = paramRequest.getActionUrl();
        SummaryView viewSemObject = !viewUri.isEmpty()
                ? (SummaryView) SemanticObject.getSemanticObject(viewUri).createGenericInstance()
                : null;

        if (operation.equals("edit")) {
            if (viewSemObject == null) {
                throw new SWBResourceException("View URI is null while editing");
            }
            
            modeUsed = SWBFormMgr.MODE_EDIT;
            formMgr = new SWBFormMgr(SummaryView.bsc_SummaryView, viewSemObject.getSemanticObject(), modeUsed);
            url.setAction("editView");
            
            if (semWorkClass != null) {
                GenericIterator<PropertyListItem> viewPropertiesList = viewSemObject.listPropertyListItems();
                HashMap<Integer, String> selectedOptions = new HashMap<Integer, String>(16);

                if (viewPropertiesList != null) {
                    while (viewPropertiesList.hasNext()) {
                        PropertyListItem listItem = viewPropertiesList.next();
                        SemanticProperty elementPropertySO = listItem.getElementProperty().transformToSemanticProperty();
                        if (listItem != null && elementPropertySO != null) {
                            selectedOptions.put(listItem.getPropertyOrder(),
                                    elementPropertySO.getURI() + "|" +
                                    elementPropertySO.getDisplayName(lang));
                        }
                    }
                }
                
                //generar un listado de HTML con las propiedades en baseList menos las de viewList
                while (basePropertiesList.hasNext()) {
                    SemanticProperty prop = basePropertiesList.next();
                    if (!selectedOptions.containsValue(prop.getURI() +
                            "|" + prop.getDisplayName(lang))) {
                        
                        //generar HTML de la opcion
                        baseListHtml.append("                        <option value=\"");
                        baseListHtml.append(prop.getURI());
                        baseListHtml.append("\">");
                        baseListHtml.append(prop.getDisplayName(lang));
                        baseListHtml.append("</option>\n");
                    }
                }
                //y otro con las de viewList
                for (int i = 0; i < selectedOptions.size(); i++) {
                    String optionData = selectedOptions.get(i);
                    if (optionData != null) {
                        viewListHtml.append("                        <option value=\"");
                        viewListHtml.append(optionData.indexOf("|") > 0 
                                            ? optionData.substring(0, optionData.indexOf("|"))
                                            : optionData);
                        viewListHtml.append("\">");
                        viewListHtml.append(optionData.indexOf("|") > 0
                                            ? optionData.substring(optionData.indexOf("|") + 1)
                                            : optionData);
                        viewListHtml.append("</option>\n");
                    }
                }
            }
            
        } else if (operation.equals("add")) {
            modeUsed = SWBFormMgr.MODE_CREATE;
            formMgr = new SWBFormMgr(SummaryView.sclass, null, modeUsed);
            url.setAction("addView");

            //obtener valores de propiedades del tipo de elemento (BSCElement) a usar
            while (basePropertiesList.hasNext()) {
                SemanticProperty prop = basePropertiesList.next();
                //generar HTML de la opcion
                baseListHtml.append("                        <option value=\"");
                baseListHtml.append(prop.getURI());
                baseListHtml.append("\">");
                baseListHtml.append(prop.getDisplayName(lang));
                baseListHtml.append("</option>\n");
            }
        }
        output.append("<script type=\"text/javascript\">\n");
        output.append("  dojo.require('dojo.parser');\n");
        output.append("  dojo.require('dojox.layout.ContentPane');\n");
        output.append("  dojo.require('dijit.form.Form');\n");
        output.append("  dojo.require('dijit.form.Button');\n");
        output.append("  dojo.require('dijit.form.MultiSelect');\n");
        output.append("</script>\n");
        
        output.append("        <div>\n");
        output.append("            <form dojoType=\"dijit.form.Form\" name=\"vistaForm");
        output.append(this.getId());
        output.append("\" ");
        output.append("id=\"vistaForm");
        output.append(this.getId());
        output.append("\" class=\"swbform\" onsubmit=\"submitForm('vistaForm");
        output.append(this.getId());
        output.append("');return false;\" method=\"post\" action=\"");
        output.append(url.toString());
        output.append("\">\n");
        output.append("                <input type=\"hidden\" id=\"propsIn2Save");
        output.append(this.getId());
        output.append("\" name=\"propsIn2Save\" value=\"\">\n");
        output.append("                <input type=\"hidden\" name=\"suri\" value=\"");
        output.append(suri);
        output.append("\">\n");
        if (operation.equals("edit")) {
            output.append("                <input type=\"hidden\" id=\"svUri");
            output.append(this.getId());
            output.append("\" name=\"svUri\" value=\"" + viewUri + "\">\n");
        }
        output.append("<fieldset>\n");
        output.append("    <legend>");
        output.append(paramRequest.getLocaleString("lbl_pageTitle"));
        output.append("</legend>\n");
        output.append("    <table>\n");
        output.append("        <tbody>\n");
        output.append("            <tr>\n");
        output.append("                <td width=\"200px\" align=\"right\">\n");
        output.append("                    ");
        output.append(formMgr.renderLabel(request, SummaryView.swb_title, SummaryView.swb_title.getName(), modeUsed));
        output.append("                </td>\n");
        output.append("                <td>\n");
        output.append("                    ");
        if (operation.equals("edit") && viewSemObject != null) {
            output.append(
                    formMgr.getFormElement(SummaryView.swb_title).renderElement(
                            request, viewSemObject.getSemanticObject(), SummaryView.swb_title,
                            SummaryView.swb_title.getName(), "dojo", modeUsed, lang)
                );
        } else if (operation.equals("add")) {
            output.append(
                    formMgr.getFormElement(SummaryView.swb_title).renderElement(
                            request, null, SummaryView.swb_title, SummaryView.swb_title.getName(),
                            "dojo", modeUsed, lang)
                );
        }
        output.append("                </td>\n");
        output.append("            </tr>\n");
        output.append("            <tr>\n");
        output.append("                <td width=\"200px\" align=\"right\">\n");
        output.append("                    ");
        output.append(formMgr.renderLabel(request, SummaryView.swb_description, modeUsed));
        output.append("                </td>\n");
        output.append("                <td>\n");
        output.append("                    ");
        if (operation.equals("edit") && viewSemObject != null) {
            output.append(
                    formMgr.getFormElement(SummaryView.swb_description).renderElement(
                            request, viewSemObject.getSemanticObject(), SummaryView.swb_description,
                            SummaryView.swb_description.getName(), "dojo", modeUsed, lang)
                    );
        } else if (operation.equals("add")) {
            output.append(
                    formMgr.getFormElement(SummaryView.swb_description).renderElement(
                            request, null, SummaryView.swb_description, SummaryView.swb_description.getName(),
                            "dojo", modeUsed, lang)
                    );
        }
        output.append("                </td>\n");
        output.append("            </tr>\n");
        output.append("            <tr>\n");
        output.append("                <td width=\"200px\" align=\"right\">\n");
        output.append("                    &nbsp;\n");
        output.append("                </td>\n");
        output.append("                <td>\n");
        output.append("                <div>\n");
        output.append("                <div style=\"float:left;\">");
        output.append("                    <span>");
        output.append(paramRequest.getLocaleString("lbl_sourceList"));
        output.append("</span><br>\n");
        output.append("                    <select id=\"baseList");
        output.append(this.getId());
        output.append("\" dojoType=\"dijit.form.MultiSelect\" size=\"10\" style=\"min-width:150px;\">\n");
        output.append(baseListHtml);
        output.append("                    </select>\n");
        output.append("                </div>\n");
        output.append("                <div style=\"float:left;\">");
        output.append("                    <p>&nbsp;</p>");
        output.append("                    <span dojoType='dijit.form.Button'>\n");
        output.append("                        ->");
        output.append("                       <script type=\"dojo/method\" event='onClick' args='evt'>\n");
        output.append("                            var base = document.getElementById(\"baseList");
        output.append(this.getId());
        output.append("\");\n");
        output.append("                            var view = document.getElementById(\"viewList");
        output.append(this.getId());
        output.append("\");\n");
        output.append("                            \n");
        output.append("                            if (base.selectedIndex > -1) {\n");
        output.append("                                var i = 0;\n");
        output.append("                                while (i < base.options.length) {\n");
        output.append("                                    if (base.options[i].selected) {\n");
        output.append("                                        var selectedOption = base.options[i];\n");
        output.append("                                        view.add(selectedOption);\n");
        output.append("                                    } else {\n");
        output.append("                                        i++;\n");
        output.append("                                    }\n");
        output.append("                                }\n");
        output.append("                            }\n");
        output.append("                       </script>\n");
        output.append("                    </span>");
        output.append("                    <br>");
        output.append("                    <span dojoType=\"dijit.form.Button\">\n");
        output.append("                        <-\n");
        output.append("                        <script type=\"dojo/method\" event=\"onClick\" args=\"evt\">\n");
        output.append("                            var base = document.getElementById(\"baseList");
        output.append(this.getId());
        output.append("\");\n");
        output.append("                            var view = document.getElementById(\"viewList");
        output.append(this.getId());
        output.append("\");\n");
        output.append("                            ");
        output.append("                            if (view.selectedIndex > -1) {\n");
        output.append("                                var i = 0;\n");
        output.append("                                while (i < view.options.length) {\n");
        output.append("                                    if (view.options[i].selected) {\n");
        output.append("                                        var selectedOption = view.options[i];\n");
        output.append("                                        base.add(selectedOption);\n");
        output.append("                                    } else {\n");
        output.append("                                        i++;\n");
        output.append("                                    }\n");
        output.append("                                }\n");
        output.append("                            }\n");
        output.append("                        </script>\n");
        output.append("                    </span>\n");
        output.append("                </div>\n");
        output.append("                <div style=\"float:left;\">\n");
        output.append("                    <span>");
        output.append(paramRequest.getLocaleString("lbl_inViewList"));
        output.append("</span><br>\n");
        output.append("                    <select id=\"viewList");
        output.append(this.getId());
        output.append("\" dojoType=\"dijit.form.MultiSelect\" name=\"viewList\" size=\"10\" style=\"min-width:150px;\">\n");
        if (operation.equals("edit") && viewListHtml.length() > 0) {
            output.append(viewListHtml);
        }
        output.append("                    </select>\n");
        output.append("                </div>\n");
        output.append("                <div style=\"float:left;\">\n");
        output.append("                    <p>&nbsp;</p>\n");
        output.append("                    <span dojoType=\"dijit.form.Button\">\n");
        output.append("                        ");
        output.append(paramRequest.getLocaleString("lbl_elementUp"));
        output.append("                        <script type=\"dojo/method\" event=\"onClick\" args=\"evt\">\n");
        output.append("                            var view = document.getElementById(\"viewList");
        output.append(this.getId());
        output.append("\");\n");
        output.append("                            if (view.options.length > 0) {\n");
        output.append("                                var itemsSelected = 0;\n");
        output.append("                                for (var i = 0; i < view.options.length; i++) {\n");
        output.append("                                    if (view.options[i].selected) {\n");
        output.append("                                        itemsSelected++;\n");
        output.append("                                    }\n");
        output.append("                                }\n");
        output.append("                                if (itemsSelected == 1) {\n");
        output.append("                                    var oldIndex = view.selectedIndex;\n");
        output.append("                                    if (oldIndex > 0) {\n");
        output.append("                                        var optionMoved = new Option(view.options[oldIndex].text, view.options[oldIndex].value, false, true);\n");
                                                //crear una nueva coleccion de options con el nuevo orden
        output.append("                                        var newOptions = new Array();\n");
        output.append("                                        for (var i = 0; i < view.options.length; i++) {\n");
        output.append("                                            var optionToAdd = new Option(view.options[i].text, view.options[i].value, false, false);\n");
        output.append("                                            if (i == oldIndex - 1) {\n");
        output.append("                                                newOptions.push(optionMoved);\n");
        output.append("                                            }\n");
        output.append("                                            if (i != oldIndex) {\n");
        output.append("                                                newOptions.push(optionToAdd);\n");
        output.append("                                            }\n");
        output.append("                                        }\n");
                                                //se eliminan los elementos del select
        output.append("                                        while (view.options.length > 0) {\n");
        output.append("                                            view.remove(view.options.length - 1);\n");
        output.append("                                        }\n");
                                                //se agregan los elementos ordenados
        output.append("                                        for (var i = 0; i < newOptions.length; i++) {\n");
        output.append("                                            try {\n");
                                                                        // for IE earlier than version 8
        output.append("                                                view.add(newOptions[i], view.options[null]);\n");
        output.append("                                            } catch (e) {\n");
        output.append("                                                view.add(newOptions[i], null);\n");
        output.append("                                            }\n");
        output.append("                                        }\n");
        output.append("                                    }\n");
        output.append("                                } else {\n");
        output.append("                                    alert(\"");
        output.append(paramRequest.getLocaleString("msg_moreThanOneSelected"));
        output.append("\");\n");
        output.append("                                }\n");
        output.append("                            }\n");
        output.append("                        </script>\n");
        output.append("                    </span>\n");
        output.append("                    <br>\n");
        output.append("                    <span dojoType=\"dijit.form.Button\">\n");
        output.append("                        ");
        output.append(paramRequest.getLocaleString("lbl_elementDown"));
        output.append("                        <script type=\"dojo/method\" event=\"onClick\" args=\"evt\">\n");
        output.append("                            var view = document.getElementById(\"viewList");
        output.append(this.getId());
        output.append("\");\n");
        output.append("                            if (view.options.length > 0) {\n");
        output.append("                                var itemsSelected = 0;\n");
        output.append("                                for (var i = 0; i < view.options.length; i++) {\n");
        output.append("                                    if (view.options[i].selected) {\n");
        output.append("                                        itemsSelected++;\n");
        output.append("                                    }\n");
        output.append("                                }\n");
        output.append("                                if (itemsSelected == 1) {\n");
        output.append("                                    var oldIndex = view.selectedIndex;\n");
        output.append("                                    if (oldIndex < view.options.length - 1) {\n");
        output.append("                                        var optionMoved = new Option(");
        output.append("view.options[oldIndex].text, view.options[oldIndex].value, false, true);\n");
                                                //crear una nueva coleccion de options con el nuevo orden
        output.append("                                        var newOptions = new Array();\n");
        output.append("                                        for (var i = 0; i < view.options.length; i++) {\n");
        output.append("                                            var optionToAdd = new Option(");
        output.append("view.options[i].text, view.options[i].value, false, false);\n");
        output.append("                                            if (i != oldIndex) {\n");
        output.append("                                                newOptions.push(optionToAdd);\n");
        output.append("                                            }\n");
        output.append("                                            if (i == oldIndex + 1) {\n");
        output.append("                                                newOptions.push(optionMoved);\n");
        output.append("                                            }\n");
        output.append("                                        }\n");
                                                //se eliminan los elementos del select
        output.append("                                        while (view.options.length > 0) {\n");
        output.append("                                            view.remove(view.options.length - 1);\n");
        output.append("                                        }\n");
                                                //se agregan los elementos ordenados
        output.append("                                        for (var i = 0; i < newOptions.length; i++) {\n");
        output.append("                                            try {\n");
                                                                       // for IE earlier than version 8
        output.append("                                                view.add(newOptions[i], view.options[null]);\n");
        output.append("                                            } catch (e) {\n");
        output.append("                                                view.add(newOptions[i], null);\n");
        output.append("                                            }\n");
        output.append("                                        }\n");
        output.append("                                    }\n");
        output.append("                                } else {\n");
        output.append("                                    alert(\"");
        output.append(paramRequest.getLocaleString("msg_moreThanOneSelected"));
        output.append("\");\n");
        output.append("                                }\n");
        output.append("                            }\n");
        output.append("                        </script>\n");
        output.append("                    </span>\n");
        output.append("                </div>\n");
        output.append("                </div>\n");
        output.append("                </td>\n");
        output.append("            </tr>\n");
        output.append("        </tbody>\n");
        output.append("    </table>\n");
        output.append("</fieldset>\n");
        //despliegue de botones
        output.append("<fieldset>\n");
        output.append("    <button dojoType=\"dijit.form.Button\" id=\"btnSave");
        output.append(this.getId());
        output.append("\" type=\"button\">");
        output.append(paramRequest.getLocaleString("lbl_btnSubmit"));
        output.append("                            <script type=\"dojo/method\" event=\"onClick\" args=\"evt\">\n");
        output.append("                                var view = document.getElementById(\"viewList");
        output.append(this.getId());
        output.append("\");\n");
        output.append("                                var propsIn2Save = document.getElementById(\"propsIn2Save");
        output.append(this.getId());
        output.append("\");\n");
        output.append("                                var form2Send = document.getElementById(\"vistaForm");
        output.append(this.getId());
        output.append("\");\n");
        output.append("                                if (view.options.length > 0) {\n");
        output.append("                                    var propsUri = \"\";\n");
        output.append("                                    for (var i = 0; i < view.options.length; i++) {\n");
        output.append("                                        if (i > 0) {\n");
        output.append("                                            propsUri += \"|\";\n");
        output.append("                                        }\n");
        output.append("                                        propsUri += view.options[i].value;\n");
        output.append("                                    }\n");
        output.append("                                    propsIn2Save.value = propsUri;\n");
        output.append("                                    \n");
        output.append("                                }\n");
        output.append("                                if (propsIn2Save.value.length > 0 && form2Send.title.value != \"\") {\n");
        output.append("                                    submitForm(form2Send.id);\n");
        output.append("                                } else if (form2Send.title.value == \"\") {\n");
        output.append("                                    alert(\"");
        output.append(paramRequest.getLocaleString("msg_noTitleEntered"));
        output.append("\");\n");
        output.append("                                } else if (propsIn2Save.value.length == 0) {\n");
        output.append("                                    alert(\"");
        output.append(paramRequest.getLocaleString("msg_noPropsSelected"));
        output.append("\");\n");
        output.append("                                }\n");
        output.append("                                return false;\n");
        output.append("                            </script>\n");
        output.append("    </button>\n");
        if (operation.equals("edit")) {
            SWBResourceURL urlCancel = paramRequest.getRenderUrl();
            urlCancel.setMode(SWBResourceURLImp.Mode_ADMIN);
            urlCancel.setParameter("operation", "add");
            output.append("    <button dojoType=\"dijit.form.Button\" ");
            output.append("type=\"button\" onClick=\"reloadTab('");
            output.append(this.getResource().getURI());
            output.append("');\">\n");
            output.append(paramRequest.getLocaleString("lbl_btnCancel"));
            output.append("    </button>\n");
        }
        output.append("</fieldset>\n");
        output.append("            </form>\n");
        output.append("        </div>\n");
        output.append(generateViewsList(paramRequest));
        
        //Muestra mensaje sobre resultado de la operacion realizada
        if (statusMsg != null && !statusMsg.isEmpty()) {
            output.append("<div dojoType=\"dojox.layout.ContentPane\">\n");
            output.append("    <script type=\"dojo/method\">\n");
            output.append("        showStatus('" + statusMsg + "');\n");
            output.append("    </script>\n");
            output.append("</div>\n");
        }

        out.println(output);
    }

    /**
     * Realiza operaciones con la informaci&oacute;n de manera segura evitando la ejecuci&oacute;n
     * de una misma operaci&oacute;n m&aacute;s de una vez, teniendo en cuenta el estado de la aplicaci&oacute;n.
     * @param request la petici&oacute;n HTTP enviada por el cliente
     * @param response la respuesta HTTP que se enviar&aacute; al cliente
     * @throws SWBResourceException  si se presenta alg&uacute;n problema dentro 
     *         de la plataforma de SWB para la correcta ejecuci&oacute;n del m&eacute;todo.
     *         Como la extracci&oacute;n de valores para par&aacute;metros de i18n.
     * @throws IOException si ocurre un problema con la lectura/escritura de la petici&oacute;n/respuesta.
     */
    @Override
    public void processAction(HttpServletRequest request, SWBActionResponse response)
            throws SWBResourceException, IOException {
        
        String action = response.getAction();
        String title = request.getParameter("title");
        String descrip = request.getParameter("description");
        String propertyUries = request.getParameter("propsIn2Save") != null
                               ? request.getParameter("propsIn2Save")
                               : "";
        String summaryViewUri = request.getParameter("svUri");
        String statusMsg = null;
        String lang = response.getUser().getLanguage();
        SummaryView summaryView = null;
        BSC bscModel = (BSC) this.getResource().getWebSite();
        boolean redirect = false;

        if (!"addView".equals(action)) {
                summaryView = SemanticObject.getSemanticObject(summaryViewUri) != null
                              ? ((SummaryView) SemanticObject.getSemanticObject(
                                      summaryViewUri).createGenericInstance())
                              : null;
        }
        if ("addView".equals(action) ||
                "editView".equals(action)) {
            
            SemanticOntology ontology = SWBPlatform.getSemanticMgr().getOntology();
            String[] uries = propertyUries.split("\\|");
            
            if ("addView".equals(action)) {
                SemanticClass semWorkClass = this.getWorkClass().transformToSemanticClass();
                
                if (semWorkClass != null) {
                    summaryView = SummaryView.ClassMgr.createSummaryView(bscModel);
                    summaryView.setObjectsType(semWorkClass.getURI());
                    statusMsg = response.getLocaleString("msg_ViewCreated");
                }
            } else if ("editView".equals(action)) {
                
                if (summaryView != null && uries.length > 0) {
                    removeAllPropertyListItem(summaryView);
                }
                statusMsg = response.getLocaleString("msg_ViewUpdated");
            }
            if (summaryView != null && !propertyUries.isEmpty()) {
                
                for (int i = 0; i < uries.length; i++) {
                    //Se crean instancias de PropertyListItem con los uries y el orden seleccionado, se almacenan en summaryView
                    SemanticObject propSO = SemanticObject.getSemanticObject(uries[i]);
                    SemanticProperty prop = ontology.getSemanticProperty(uries[i]);
                        
                        if (prop == null) {
                            prop = propSO.transformToSemanticProperty();
                        }
                        if (prop != null) {
                            PropertyListItem property = PropertyListItem.ClassMgr.createPropertyListItem(bscModel);
                            property.setPropertyOrder(i);
                            property.setElementProperty(prop.getSemanticObject());
                            summaryView.addPropertyListItem(property);
                        }
                }
                //Se actualiza informacion de Traceable
                SWBPortal.getServiceMgr().updateTraceable(summaryView.getSemanticObject(), response.getUser());
                summaryView.setTitle(title, lang);
                summaryView.setTitle(title);
                summaryView.setDescription(descrip, lang);
                summaryView.setDescription(descrip);
                response.setRenderParameter("viewUri", summaryView.getURI());
                response.setRenderParameter("operation", "edit");
            }
            redirect = true;
            
        } else if ("deleteView".equals(action)) {
            
            if (summaryView != null) {
                if (this.getActiveView() != null && this.getActiveView() == summaryView) {
                    this.setActiveView(null);
                }
                removeAllPropertyListItem(summaryView);
                SummaryView.ClassMgr.removeSummaryView(summaryView.getId(), bscModel);
                statusMsg = response.getLocaleString("msg_ViewDeleted");
            }
            redirect = true;
            
        } else if (action.equalsIgnoreCase("makeActive") && summaryView != null) {
            //asignar a SummaryViewAdm la instancia de SummaryView correspondiente al uri recibido en request
            this.setActiveView(summaryView);
            statusMsg = response.getLocaleString("msg_ContentViewAssigned");
            redirect = true;
        } else {
            super.processAction(request, response);
        }
        
        if (redirect) {
            response.setRenderParameter("statusMsg", statusMsg);
            response.setMode("showForm");
        }
    }
    
    /**
     * Elimina todos los elementos PropertyListItem asociados al objeto summaryView proporcionado
     * @param summaryView la vista resumen a la que se le eliminar&aacute;n los
     *        elementos PropertyListItem relacionados
     */
    private void removeAllPropertyListItem(SummaryView summaryView) {
        
        GenericIterator<PropertyListItem> listItems = summaryView.listPropertyListItems();
        if (listItems != null && listItems.hasNext()) {
            while (listItems.hasNext()) {
                PropertyListItem item = listItems.next();
                summaryView.removePropertyListItem(item);
            }
        }
    }
    
    /**
     * Genera el c&oacute;digo HTML del listado de vistas resumen a administrar
     * @return un objeto {@code String} que representa el c&oacute;digo HTML del
     *         listado de vistas resumen disponibles
     * @throws SWBResourceException si se presenta alg&uacute;n problema dentro 
     *         de la plataforma de SWB para la correcta ejecuci&oacute;n del m&eacute;todo.
     *         Como la extracci&oacute;n de valores para par&aacute;metros de i18n.
     */
    private String generateViewsList(SWBParamRequest paramRequest)
            throws SWBResourceException {
        
        StringBuilder listCode = new StringBuilder(256);
        String lang = paramRequest.getUser().getLanguage();
        BSC bsc = (BSC) this.getResource().getWebSite();
        SemanticClass workClassSC = this.getWorkClass() != null
                                    ? this.getWorkClass().transformToSemanticClass()
                                    : null;
        String objectsType = workClassSC != null
                             ? workClassSC.getURI() : null;
        
        if (bsc != null && objectsType != null) {
            Iterator listOfViews = SummaryView.ClassMgr.listSummaryViews(bsc);
            
            if (listOfViews != null && listOfViews.hasNext()) {
                ArrayList<SummaryView> viewsList = new ArrayList<SummaryView>(32);
                while (listOfViews.hasNext()) {
                    SummaryView view2Include = (SummaryView) listOfViews.next();
                    if (view2Include.getObjectsType() != null &&
                            view2Include.getObjectsType().equals(objectsType)) {
                            viewsList.add(view2Include);
                    }
                }
                listOfViews = viewsList.iterator();
            }
            if (listOfViews != null && listOfViews.hasNext()) {
                listOfViews = SWBComparator.sortByDisplayName(listOfViews, lang);
                
                listCode.append("<div>");
                if (this.getActiveView() == null) {
                    listCode.append(paramRequest.getLocaleString("msg_noActiveView"));
                }
                listCode.append("</div>");
                listCode.append("    <form dojoType=\"dijit.form.Form\" name=\"listViewsForm");
                listCode.append(this.getId());
                listCode.append("\" id=\"listViewsForm");
                listCode.append(this.getId());
                listCode.append("\" class=\"swbform\" method=\"post\">\n");
                listCode.append("<fieldset>\n");
                listCode.append("    <legend>");
                listCode.append(paramRequest.getLocaleString("lbl_listingCaption"));
                listCode.append("</legend>\n");
                listCode.append("<table width=\"60%\" border=\"0\" align=\"left\" cellpadding=\"1\" cellspacing=\"1\">\n");
                listCode.append("    <thead>\n");
                listCode.append("        <tr>\n");
                listCode.append("            <th scope=\"col\" align=\"center\">\n");
                listCode.append(paramRequest.getLocaleString("lbl_listingTitle1"));
                listCode.append("</th>\n");
                listCode.append("            <th scope=\"col\" colspan=\"3\" align=\"center\">\n");
                listCode.append(paramRequest.getLocaleString("lbl_listingOperations"));
                listCode.append("</th>\n");
                listCode.append("        </tr>\n");
                listCode.append("    </thead>\n");
                listCode.append("    <tbody>\n");
                
                //Se agrega al listado cada vista creada
                while (listOfViews.hasNext()) {
                    SummaryView view = (SummaryView) listOfViews.next();
                    
                    SWBResourceURL urlEdit = paramRequest.getRenderUrl();
                    urlEdit.setParameter("operation", "edit");
                    urlEdit.setParameter("viewUri", view.getURI());
                    
                    SWBResourceURL urlDelete = paramRequest.getActionUrl();
                    urlDelete.setAction("deleteView");
                    urlDelete.setParameter("svUri", view.getURI());
                    
                    SWBResourceURL urlMakeActive = paramRequest.getActionUrl();
                    urlMakeActive.setAction("makeActive");
                    urlMakeActive.setParameter("svUri", view.getURI());
                    
                    listCode.append("        <tr>");
                    listCode.append("            <td>" + view.getDisplayTitle(lang) + "</td>\n");
                    listCode.append("            <td align=\"center\"><a href=\"void(0);\" onclick=\"submitUrl('");
                    listCode.append(urlEdit);
                    listCode.append("', this);return false;\">");
                    listCode.append(paramRequest.getLocaleString("lbl_lnkEdit"));
                    listCode.append("</a></td>\n");
                    listCode.append("            <td align=\"center\"><a href=\"void(0);\" onclick=\"if (confirm('");
                    listCode.append(paramRequest.getLocaleString("msg_confirmRemove"));
                    listCode.append(SWBUtils.TEXT.scape4Script(view.getDisplayTitle(lang)));
                    listCode.append("?')){submitUrl('");
                    listCode.append(urlDelete);
                    listCode.append("', this);}return false;\">");
                    listCode.append(paramRequest.getLocaleString("lbl_lnkDelete"));
                    listCode.append("</a></td>\n");
                    //La opcion para asignar como activa se muestra para aquellas vistas que no lo son
                    if (this.getActiveView() == null || (this.getActiveView() != null &&
                            !this.getActiveView().getURI().equals(view.getURI()))) {
                        listCode.append("            <td align=\"center\"><a href=\"void(0);\" onclick=\"submitUrl('");
                        listCode.append(urlMakeActive);
                        listCode.append("', this);return false;\">");
                        listCode.append(paramRequest.getLocaleString("lbl_lnkAsContent"));
                        listCode.append("</a></td>");
                    } else {
                        listCode.append("            <td>&nbsp;</td>");
                    }
                    listCode.append("        </tr>");
                }
                listCode.append("    </tbody>");
                listCode.append("</table>");
                listCode.append("</fieldset>\n");
                listCode.append("    </form>");
                listCode.append("");
            }
        }
        
        return listCode.toString();
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response,
            SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        
        if (paramRequest.getMode().equals("showForm")) {
            doShowForm(request, response, paramRequest);
        } else {
            super.processRequest(request, response, paramRequest);
        }
    }
    
}
