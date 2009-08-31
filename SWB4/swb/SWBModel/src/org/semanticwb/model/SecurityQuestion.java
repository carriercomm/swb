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
**/ 
 
package org.semanticwb.model;

import java.util.Iterator;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticProperty;


public class SecurityQuestion extends org.semanticwb.model.base.SecurityQuestionBase 
{
    public SecurityQuestion(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    @Override
    public void process(HttpServletRequest request, SemanticObject obj, SemanticProperty prop)
    {
        super.process(request, obj, prop);
    }

    @Override
    public String renderElement(HttpServletRequest request, SemanticObject obj, SemanticProperty prop, String type, String mode, String lang)
    {
         if(obj==null)obj=new SemanticObject();
        String ret="";

        if(type.endsWith("iphone"))
        {
            ret=renderIphone(obj, prop, type, mode, lang);
        }else
        {
            ret=renderXHTML(obj, prop, type, mode, lang);
        }
        return ret;
    }

    public String renderIphone(SemanticObject obj, SemanticProperty prop, String type, String mode, String lang)
    {
        return "";
    }

    public String renderXHTML(SemanticObject obj, SemanticProperty prop, String type, String mode, String lang)
    {
        StringBuilder ret=new StringBuilder(250);
        String name=prop.getName();
        String label=prop.getDisplayName(lang);
        SemanticObject sobj=prop.getDisplayProperty();
        boolean required=prop.isRequired();

        String pmsg=null;
        String imsg=null;
        String selectValues=null;
//        System.out.println("name:"+name);
//        System.out.println("label:"+label);
//        System.out.println("sobj:"+sobj);
//        System.out.println("m_obj:"+obj.getModel().getName());
//        System.out.println("prop:"+prop);
//        System.out.println("DC:"+prop.getURI());
        if(sobj!=null)
        {
            DisplayProperty dobj=new DisplayProperty(sobj);
            pmsg=dobj.getPromptMessage();
            imsg=dobj.getInvalidMessage();
            selectValues= SWBContext.getUserRepository(getModel().getName()).getUserRepSecurityQuestionList();
                    //dobj.getSelectValues(lang); //TODO:
        }

        if(imsg==null)
        {
            if(required)
            {
                imsg=label+" es requerido.";
                if(lang.equals("en"))
                {
                    imsg=label+" is required.";
                }
            }
        }

        if(pmsg==null)
        {
            pmsg="Captura "+label+".";
            if(lang.equals("en"))
            {
                pmsg="Enter "+label+".";
            }
        }

        if(prop.isObjectProperty())
        {
            SemanticObject val=obj.getObjectProperty(prop);
            String uri="";
            String value="";
            if(val!=null)
            {
                uri=val.getURI();
                value=obj.getDisplayName(lang);
            }
            if(mode.equals("edit") || mode.equals("create") )
            {
                ret.append("<select name=\"").append(name).append("\" dojoType=\"dijit.form.FilteringSelect\" autoComplete=\"true\" invalidMessage=\"").append(imsg).append("\">");
                //onChange="dojo.byId('oc1').value=arguments[0]"
                ret.append("<option value=\"\"></option>");
                SemanticClass cls=prop.getRangeClass();
                Iterator<SemanticObject> it=null;

                it=SWBComparator.sortSermanticObjects(lang, getModel().listInstancesOfClass(cls));

                while(it!=null && it.hasNext())
                {
                    SemanticObject sob=it.next();
                    ret.append("<option value=\"").append(sob.getURI()).append("\" ");
                    if(sob.getURI().equals(uri))ret.append("selected");
                    ret.append(">").append(sob.getDisplayName(lang)).append("</option>");
                }
                ret.append("</select>");
            }else if(mode.equals("view"))
            {
                ret.append("<span _id=\"").append(name).append("\" name=\"").append(name).append("\">").append(value).append("</span>");
            }
        }else
        {
            if(selectValues!=null)
            {
                String value=obj.getProperty(prop);
                ret.append("<select name=\"").append(name).append("\" dojoType=\"dijit.form.FilteringSelect\" autoComplete=\"true\" invalidMessage=\"").append(imsg).append("\">");
                StringTokenizer st=new StringTokenizer(selectValues,"|");
                ret.append("<option value=\"\"></option>");
                while(st.hasMoreTokens())
                {
                    String tok=st.nextToken();
                    int ind=tok.indexOf(':');
                    String id=tok;
                    String val=tok;
                    if(ind>0)
                    {
                        id=tok.substring(0,ind);
                        val=tok.substring(ind+1);
                    }
                    ret.append("<option value=\"").append(id).append("\" ");
                    if(id.equals(value))ret.append("selected");
                    ret.append(">").append(val).append("</option>");
                }
                ret.append("</select>");
            }
        }

        

        return ret.toString();
    }

}
