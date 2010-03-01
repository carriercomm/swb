
/**
 * SemanticWebBuilder es una plataforma para el desarrollo de portales y aplicaciones de integración,
 * colaboración y conocimiento, que gracias al uso de tecnología semántica puede generar contextos de
 * información alrededor de algún tema de interés o bien integrar información y aplicaciones de diferentes
 * fuentes, donde a la información se le asigna un significado, de forma que pueda ser interpretada y
 * procesada por personas y/o sistemas, es una creación original del Fondo de Información y Documentación
 * para la Industria INFOTEC, cuyo registro se encuentra actualmente en trámite.
 *
 * INFOTEC pone a su disposición la herramienta SemanticWebBuilder a través de su licenciamiento abierto al público (‘open source’),
 * en virtud del cual, usted podrá usarlo en las mismas condiciones con que INFOTEC lo ha diseñado y puesto a su disposición;
 * aprender de él; distribuirlo a terceros; acceder a su código fuente y modificarlo, y combinarlo o enlazarlo con otro software,
 * todo ello de conformidad con los términos y condiciones de la LICENCIA ABIERTA AL PÚBLICO que otorga INFOTEC para la utilización
 * del SemanticWebBuilder 4.0.
 *
 * INFOTEC no otorga garantía sobre SemanticWebBuilder, de ninguna especie y naturaleza, ni implícita ni explícita,
 * siendo usted completamente responsable de la utilización que le dé y asumiendo la totalidad de los riesgos que puedan derivar
 * de la misma.
 *
 * Si usted tiene cualquier duda o comentario sobre SemanticWebBuilder, INFOTEC pone a su disposición la siguiente
 * dirección electrónica:
 *  http://www.semanticwebbuilder.org
 */
package org.semanticwb.model;

//~--- non-JDK imports --------------------------------------------------------

import org.semanticwb.SWBPlatform;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.platform.SemanticLiteral;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticProperty;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectMultiple.
 */
public class SelectMultiple extends org.semanticwb.model.base.SelectMultipleBase {
    
    /**
     * Instantiates a new select multiple.
     * 
     * @param base the base
     */
    public SelectMultiple(org.semanticwb.platform.SemanticObject base) {
        super(base);
    }

    /* (non-Javadoc)
     * @see org.semanticwb.model.base.FormElementBase#process(javax.servlet.http.HttpServletRequest, org.semanticwb.platform.SemanticObject, org.semanticwb.platform.SemanticProperty)
     */
    @Override
    public void process(HttpServletRequest request, SemanticObject obj, SemanticProperty prop) {

        // super.process(request, obj, prop);
        // System.out.println("Process...");
        // System.out.println("Prop:"+prop);
        // System.out.println("obj:"+obj);
        String vals[] = request.getParameterValues(prop.getName());

        if (vals == null) {
            vals = new String[0];
        }

        obj.removeProperty(prop);

        if (prop.isObjectProperty()) {
            for (int x = 0; x < vals.length; x++) {
                obj.addObjectProperty(prop, SemanticObject.createSemanticObject(vals[x]));
            }
        } else {
            for (int x = 0; x < vals.length; x++) {
                obj.addLiteralProperty(prop, new SemanticLiteral(vals[x]));

                // System.out.println("val"+x+":"+vals[x]);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.semanticwb.model.base.FormElementBase#renderElement(javax.servlet.http.HttpServletRequest, org.semanticwb.platform.SemanticObject, org.semanticwb.platform.SemanticProperty, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String renderElement(HttpServletRequest request, SemanticObject obj, SemanticProperty prop, String type,
                                String mode, String lang) {
        if (obj == null) {
            obj = new SemanticObject();
        }

////        boolean IPHONE = false;
//        boolean XHTML  = false;
        boolean DOJO   = false;

//        if (type.equals("iphone")) {
//            IPHONE = true;
//        } else if (type.equals("xhtml")) {
//            XHTML = true;
//        } else
        if (type.equals("dojo")) {
            DOJO = true;
        }

        StringBuffer   ret          = new StringBuffer();
        String         name         = prop.getName();
        String         label        = prop.getDisplayName(lang);
        SemanticObject sobj         = prop.getDisplayProperty();
        boolean        required     = prop.isRequired();
        String         pmsg         = null;
        String         imsg         = null;
        String         selectValues = null;
        boolean        disabled     = false;

        if (sobj != null) {
            DisplayProperty dobj = new DisplayProperty(sobj);

            pmsg         = dobj.getPromptMessage();
            imsg         = dobj.getInvalidMessage();
            selectValues = dobj.getDisplaySelectValues(lang);
            disabled     = dobj.isDisabled();
        }

        if (DOJO) {
            if (imsg == null) {
                if (required) {
                    imsg = label + " es requerido.";

                    if (lang.equals("en")) {
                        imsg = label + " is required.";
                    }
                } else {
                    imsg = "Dato invalido.";

                    if (lang.equals("en")) {
                        imsg = "Invalid data.";
                    }
                }
            }

            if (pmsg == null) {
                pmsg = "Captura " + label + ".";

                if (lang.equals("en")) {
                    pmsg = "Enter " + label + ".";
                }
            }
        }

        String ext = "";

        if (disabled) {
            ext += " disabled=\"disabled\"";
        }

        if (prop.isObjectProperty()) {
            ArrayList<String> vals   = new ArrayList();
            String            auxs[] = request.getParameterValues(prop.getName());

            if (auxs == null) {
                auxs = new String[0];
            }

            for (int x = 0; x < auxs.length; x++) {
                vals.add(auxs[x]);
            }

            Iterator<SemanticObject> it2 = obj.listObjectProperties(prop);

            while (it2.hasNext()) {
                SemanticObject semanticObject = it2.next();

                // System.out.println("semanticObject:"+semanticObject+" vals:"+vals);
                vals.add(semanticObject.getURI());
            }

            String value = obj.getDisplayName(lang);

            if (mode.equals("edit") || mode.equals("create")) {
                ret.append("<select name=\"" + name + "\" multiple=\"true\"");
                ret.append(" style=\"width:300px;\"");

                if (DOJO) {
                    ret.append(" dojoType=\"dijit.form.MultiSelect\" invalidMessage=\"" + imsg + "\"");
                }

                ret.append(" " + ext + ">");

                // onChange="dojo.byId('oc1').value=arguments[0]"
                SemanticClass            cls = prop.getRangeClass();
                Iterator<SemanticObject> it  = null;

                if (isGlobalScope()) {
                    if (cls != null) {
                        it = SWBComparator.sortSermanticObjects(lang, cls.listInstances());
                    } else {
                        it = SWBComparator.sortSermanticObjects(lang,
                                SWBPlatform.getSemanticMgr().getVocabulary().listSemanticClassesAsSemanticObjects());
                    }
                } else {
                    it = SWBComparator.sortSermanticObjects(lang, getModel().listInstancesOfClass(cls));
                }

                while (it.hasNext()) {
                    SemanticObject sob = it.next();

                    if (sob.getURI() != null) {
                        ret.append("<option value=\"" + sob.getURI() + "\" ");

                        if (vals.contains(sob.getURI())) {
                            ret.append("selected=\"selected\"");
                        }

                        ret.append(">" + sob.getDisplayName(lang) + "</option>");
                    }
                }

                ret.append("</select>");
            } else if (mode.equals("view")) {
                ret.append("<span _id=\"" + name + "\" name=\"" + name + "\">" + value + "</span>");
            }
        } else {
            if (selectValues != null) {
                ArrayList<String> vals   = new ArrayList();
                String            auxs[] = request.getParameterValues(prop.getName());

                if (auxs == null) {
                    auxs = new String[0];
                }

                for (int x = 0; x < auxs.length; x++) {
                    vals.add(auxs[x]);
                }

                Iterator<SemanticLiteral> it2 = obj.listLiteralProperties(prop);

                while (it2.hasNext()) {
                    SemanticLiteral lit = it2.next();

                    vals.add(lit.getString());
                }

                ret.append("<select name=\"" + name + "\" multiple=\"true\"");

                if (DOJO) {
                    ret.append(" dojoType=\"dijit.form.MultiSelect\" invalidMessage=\"" + imsg + "\"");
                }

                ret.append(" " + ext + ">");

                StringTokenizer st = new StringTokenizer(selectValues, "|");

                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    int    ind = tok.indexOf(':');
                    String id  = tok;
                    String val = tok;

                    if (ind > 0) {
                        id  = tok.substring(0, ind);
                        val = tok.substring(ind + 1);
                    }

                    ret.append("<option value=\"" + id + "\" ");

                    if (vals.contains(id)) {
                        ret.append("selected=\"selected\"");
                    }

                    ret.append(">" + val + "</option>");
                }

                ret.append("</select>");
            }
        }

        return ret.toString();
    }
}