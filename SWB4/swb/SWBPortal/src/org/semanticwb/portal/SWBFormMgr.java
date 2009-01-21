package org.semanticwb.portal;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.semanticwb.SWBPlatform;
import org.semanticwb.model.DisplayProperty;
import org.semanticwb.model.FormElement;
import org.semanticwb.model.FormView;
import org.semanticwb.model.GenericFormElement;
import org.semanticwb.model.PropertyGroup;
import org.semanticwb.model.SWBComparator;
import org.semanticwb.model.SWBContext;
import org.semanticwb.model.SWBVocabulary;
import org.semanticwb.platform.*;

/**
 *
 * @author Javier Solis Gonzalez
 */
public class SWBFormMgr 
{
    public static final String MODE_VIEW="view";
    public static final String MODE_EDIT="edit";
    public static final String MODE_CREATE="create";
    public static final String TYPE_XHTML="xhtml";
    public static final String TYPE_IPHONE="iphone";

    public static final String PRM_ID="id";
    public static final String PRM_MODE="smode";
    public static final String PRM_REF="sref";
    public static final String PRM_URI="suri";
    public static final String PRM_CLS="scls";
    
    private SemanticObject m_obj;
    private SemanticObject m_ref;
    private SemanticClass m_cls;
    private FormView m_fview=null;
    private Map<SemanticProperty, String> m_propmap=null;
    private String m_mode="view";
    private String m_action="";
    private String m_method="POST";
    private String m_lang="es";
    private String m_type=TYPE_XHTML;
    private PropertyGroup m_general=null;

    private HashMap<String, String> hidden=null;
    
    private HashMap<PropertyGroup, TreeSet> groups=null;
    
    public SWBFormMgr(SemanticObject obj, String frmview, String mode)
    {
        this.m_obj=obj;
        this.m_mode=mode;
        this.m_cls=obj.getSemanticClass();
        this.m_fview=SWBContext.getFormView(frmview);
        init();
    }

    /**
     * Modo creacion
     * @param cls
     * @param frmview
     */
    public SWBFormMgr(SemanticClass cls, SemanticObject ref, String frmview)
    {
        this.m_mode=MODE_CREATE;
        this.m_cls=cls;
        this.m_ref=ref;
        this.m_fview=SWBContext.getFormView(frmview);
        init();
    }

    
    public void init()
    {
        m_general=(PropertyGroup)SWBPlatform.getSemanticMgr().getOntology().getGenericObject(SemanticVocabulary.SWBXF_URI+"pg_General");
        //System.out.println("********************"+m_general+"***************************");
        if(m_fview!=null)
        {
            m_propmap=m_fview.getPropertyMap(m_mode);
        }
        //System.out.println("m_fview:"+m_fview+" m_propmap:"+m_propmap);
        groups=new HashMap();
        hidden=new HashMap();
        Iterator<SemanticProperty> it=m_cls.listProperties();
        while(it.hasNext())
        {
            SemanticProperty prop=it.next();
            addProperty(prop);
        }
    }
    
    public void addProperty(SemanticProperty prop)
    {
        boolean createGroup=false;
        boolean addProp=false;
        SemanticObject obj=prop.getDisplayProperty();
        PropertyGroup grp=null;
        boolean hidden=false;
        boolean required=prop.isRequired();
        if(obj!=null)
        {
            DisplayProperty disp=new DisplayProperty(obj);       
            grp=disp.getGroup();
            hidden=disp.isHidden();
        }

        if(grp==null)grp=m_general;
        TreeSet<SemanticProperty> props=groups.get(grp);
        if(props==null)
        {
            props=new TreeSet(new Comparator(){

                public int compare(Object o1, Object o2){
                    SemanticObject sobj1=((SemanticProperty)o1).getDisplayProperty();
                    SemanticObject sobj2=((SemanticProperty)o2).getDisplayProperty();
                    int v1=999999999;
                    int v2=999999999;
                    if(sobj1!=null)v1=new DisplayProperty(sobj1).getIndex();
                    if(sobj2!=null)v2=new DisplayProperty(sobj2).getIndex();
                    return v1<v2?-1:1;
                }
            });
            createGroup=true;
        }
        if(m_fview!=null)           //valida si la propiedad se encuentra dentro de la vista
        {
            if(m_propmap.containsKey(prop))
            {
                addProp=true;
            }
        }else
        {
            if(!hidden)
            {
                if(m_mode.equals(MODE_CREATE))      //solo se agregan las requeridas
                {
                    if(required)
                    {
                        addProp=true;
                    }
                }else
                {
                    addProp=true;
                }
            }
        }
        if(addProp)
        {
            props.add(prop);
            //System.out.println("put:"+grp);
            if(createGroup)groups.put(grp, props);
        }
    }
    
    public String getAction() {
        return m_action;
    }

    public void setAction(String action) {
        this.m_action = action;
    }

    public String getMethod() {
        return m_method;
    }

    public void setMethod(String method) {
        this.m_method = method;
    }
    
    public String getLang() {
        return m_lang;
    }

    public void setLang(String lang) {
        this.m_lang = lang;
    }    
    
    public String getType() {
        return m_type;
    }

    public void setType(String type) {
        this.m_type = type;
    }      
    
    public String renderForm()
    {
        String ret="";
        if(m_type.equals(TYPE_XHTML))
        {
            ret=renderXHTMLForm();
        }else if(m_type.equals(TYPE_IPHONE))
        {
            ret=renderIphoneForm();
        }
        return ret;
    }
    
    public SemanticObject processForm(HttpServletRequest request)
    {
        SemanticObject ret=m_obj;
        String smode=request.getParameter(PRM_MODE);
        if(smode!=null)
        {
            if(smode.equals(MODE_CREATE))
            {
                SemanticModel model=m_ref.getModel();
                if(!m_cls.isAutogenId())
                {
                    String id=request.getParameter(PRM_ID);
                    ret=model.createSemanticObjectById(id, m_cls);
                }else
                {
                    long id=SWBPlatform.getSemanticMgr().getCounter(model.getName()+"/"+m_cls.getName());
                    ret=model.createSemanticObjectById(""+id,m_cls);
                }
                m_obj=ret;
            }
            //else
            {
                Iterator<SemanticProperty> it=m_cls.listProperties();
                while(it.hasNext())
                {
                    SemanticProperty prop=it.next();
                    processElement(request, prop, smode);
                }
            }
        }
        return ret;
    }
    
    private void renderProp(StringBuffer ret, SemanticProperty prop, FormElement ele)
    {
        String label=null;
        String element=null;
        if(m_propmap!=null)
        {
            label=ele.renderLabel(m_obj, prop, m_type, m_propmap.get(prop), m_lang);
            element=ele.renderElement(m_obj, prop, m_type, m_propmap.get(prop), m_lang);
        }else
        {
            label=ele.renderLabel(m_obj, prop, m_type, m_mode, m_lang);
            element=ele.renderElement(m_obj, prop, m_type, m_mode, m_lang);
        }
        if(element!=null && element.length()>0)
        {
            if(!m_mode.equals(MODE_CREATE))
            {
                ret.append("<tr><td width=\"200px\" align=\"right\">");
            }else
            {
                ret.append("<tr><td align=\"right\">");
            }
            ret.append(label.replaceAll(" ", "&nbsp;"));
            ret.append("</td><td>");
            ret.append(element);
            ret.append("</td></tr>");
        }
    }

    
    public String renderXHTMLForm()
    {
        StringBuffer ret=new StringBuffer();
        String uri;
        String name;
        String frmname=null;
        if(m_mode.equals(MODE_CREATE))
        {
            uri=m_cls.getURI();
        }else
        {
            uri=m_obj.getURI();
        }
        frmname=uri+"/form";
        ret.append("<form id=\""+frmname+"\" dojoType=\"dijit.form.Form\" class=\"swbform\" action=\""+m_action+"\" onSubmit=\"submitForm('"+frmname+"');return false;\" method=\""+m_method+"\">");
        if(m_obj!=null)ret.append("    <input type=\"hidden\" name=\""+PRM_URI+"\" value=\""+m_obj.getURI()+"\">");
        if(m_cls!=null)ret.append("    <input type=\"hidden\" name=\""+PRM_CLS+"\" value=\""+m_cls.getURI()+"\">");
        if(m_mode!=null)ret.append("    <input type=\"hidden\" name=\""+PRM_MODE+"\" value=\""+m_mode+"\">");
        if(m_ref!=null)ret.append("    <input type=\"hidden\" name=\""+PRM_REF+"\" value=\""+m_ref.getURI()+"\">");
        Iterator<Map.Entry<String,String>> hit=hidden.entrySet().iterator();
        while(hit.hasNext())
        {
            Map.Entry entry=hit.next();
            ret.append("    <input type=\"hidden\" name=\""+entry.getKey()+"\" value=\""+entry.getValue()+"\">");
        }

        if(!m_mode.equals(MODE_CREATE))
        {
            ret.append("	<fieldset>");
            ret.append("	    <table><tr><td width=\"200px\" align=\"right\">");
            ret.append("                <label>Identificador &nbsp;</label>");
            ret.append("        </td><td>");
            ret.append("                <span>"+m_obj.getId()+"</span>");
            ret.append("	    </td></tr></table>");
            ret.append("	</fieldset>");

            Iterator<PropertyGroup> itgp=SWBComparator.sortSortableObject(groups.keySet().iterator());
            while(itgp.hasNext())
            {
                PropertyGroup group=itgp.next();
                ret.append("	<fieldset>");
                ret.append("	    <legend>"+group.getSemanticObject().getDisplayName(m_lang)+"</legend>");
                ret.append("	    <table>");

                Iterator<SemanticProperty> it=groups.get(group).iterator();
                while(it.hasNext())
                {
                    SemanticProperty prop=it.next();
                    FormElement ele=getFormElement(prop);
                    renderProp(ret, prop,ele);
                }
                ret.append("	    </table>");
                ret.append("	</fieldset>");
            }

            ret.append("<fieldset><span align=\"center\">");
            ret.append("<button dojoType='dijit.form.Button' type=\"submit\">Guardar</button>");
            ret.append("<button dojoType='dijit.form.Button' >Favoritos</button>");
            ret.append("<button dojoType='dijit.form.Button' >Eliminar</button>");
            ret.append("</span></fieldset>");

        }else
        {
            ret.append("	<fieldset>");
            //ret.append("	    <legend>"+group.getSemanticObject().getDisplayName(m_lang)+"</legend>");
            ret.append("	    <table>");
            Iterator<PropertyGroup> itgp=SWBComparator.sortSortableObject(groups.keySet().iterator());
            while(itgp.hasNext())
            {
                PropertyGroup group=itgp.next();

                Iterator<SemanticProperty> it=groups.get(group).iterator();
                while(it.hasNext())
                {
                    SemanticProperty prop=it.next();
                    FormElement ele=getFormElement(prop);
                    ele.setAttribute("onchange", "alert(this.value);dojo.byId('swb_create_id').value=this.value;");
                    renderProp(ret, prop, ele);
                }
            }
            if(m_mode.equals(MODE_CREATE) && !m_cls.isAutogenId())
            {
                String model=m_ref.getModel().getName();
                String clsid=m_cls.getClassId();
                ret.append("	    <tr><td align=\"right\">");
                ret.append("                <label>Identificador <em>*</em></label>");
                ret.append("        </td><td>");
                ret.append("                <input type=\"text\" id=\"swb_create_id\" name=\""+PRM_ID+"\" dojoType=\"dijit.form.ValidationTextBox\" required=\"true\" promptMessage=\"Captura Identificador.\" isValid=\"return canCreateSemanticObject('"+model+"','"+clsid+"',this.textbox.value);\" invalidMessage=\"Identificador invalido.\" trim=\"true\"/>");
                ret.append("	    </td></tr>");
            }
            //ret.append("        <tr><td align=\"center\" colspan=\"2\"><hr/></td></tr>");
            ret.append("        <tr><td align=\"center\" colspan=\"2\">");
            ret.append("            <button dojoType='dijit.form.Button' type=\"submit\">Guardar</button>");
            ret.append("            <button dojoType='dijit.form.Button' onclick=\"dijit.byId('swbDialog').hide();\">Cancelar</button>");
            ret.append("	    </td></tr>");
            ret.append("	    </table>");
            ret.append("	</fieldset>");
        }
        ret.append("</form>");
        return ret.toString();
    }    
    
    public String renderIphoneForm()
    {
        //TODO
        return "";
    }        
    

    
    public String renderElement(String propName)
    {
        //TODO
        return null;
    }
    
    private FormElement getFormElement(SemanticProperty prop)
    {
        SemanticObject obj=prop.getDisplayProperty();
        FormElement ele=null;
        if(obj!=null)
        {
            DisplayProperty disp=new DisplayProperty(obj);
            SemanticObject feobj=disp.getFormElement();
            if(feobj!=null)
            {
                ele=(FormElement)feobj.createGenericInstance();
            }
        }
        //System.out.println("obj:"+obj+" prop:"+prop+" ele:"+ele);
        if(ele==null)ele=new GenericFormElement();
        return ele;
    }
    
    public void processElement(HttpServletRequest request, SemanticProperty prop, String mode)
    {
        FormElement ele=getFormElement(prop);
        ele.process(request, m_obj, prop, m_type, mode, m_lang);
    }
    
    public String renderLabel(SemanticProperty prop, String mode)
    {
        String ret=null;
        //System.out.println("prop:"+prop+" mode:"+mode);
        FormElement ele=getFormElement(prop);
        ret=ele.renderLabel(m_obj, prop, m_type, mode, m_lang);
        return ret;
    }

    public String renderElement(SemanticProperty prop, String mode)
    {
        String ret=null;
        //System.out.println("prop:"+prop+" mode:"+mode);
        FormElement ele=getFormElement(prop);
        ret=ele.renderElement(m_obj, prop, m_type, mode, m_lang);
        return ret;
    }
    
    public void addHiddenParameter(String key, String value)
    {
        if(key!=null && value!=null)hidden.put(key, value);
    }
}
