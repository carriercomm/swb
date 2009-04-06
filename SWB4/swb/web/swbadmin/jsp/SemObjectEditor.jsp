<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%>
<%@page import="org.semanticwb.*,org.semanticwb.platform.*,org.semanticwb.portal.*,org.semanticwb.model.*,java.util.*,org.semanticwb.base.util.*"%>
<%
    User user=SWBPortal.getSessionUser();
    String lang="es";
    if(user!=null)lang=user.getLanguage();
    response.setHeader("Cache-Control", "no-cache"); 
    response.setHeader("Pragma", "no-cache"); 
    String suri=request.getParameter("suri");
    String scls=request.getParameter("scls");
    String sref=request.getParameter("sref");
    String sprop=request.getParameter("sprop");
    //System.out.println("SemObjectEditor suri:"+suri+" scls:"+scls+" sref:"+sref+" sprop:"+sprop);
    
    if(suri==null && scls==null)
    {
        String code=SWBUtils.IO.readInputStream(request.getInputStream());
        //System.out.println(code);
        String uri=SWBContext.getAdminWebSite().getHomePage().getEncodedURI();
        String cls=SWBContext.getAdminWebSite().getHomePage().getSemanticObject().getSemanticClass().getEncodedURI();

        //uri=SWBContext.getWebSite("sep").getProject("proyecto1").getEncodedURI();
        //cls=SWBContext.getWebSite("sep").getProject("proyecto1").getSemanticObject().getSemanticClass().getEncodedURI();
%>
        <a href="?suri=<%=uri%>">edit</a>
        <a href="?scls=<%=cls%>&sref=<%=uri%>">create</a>
<%
        return;
    }
    String smode=request.getParameter("smode");
try
{
    SemanticOntology ont=SWBPlatform.getSemanticMgr().getOntology();
    if(suri==null) //es una creacion
    {
        SemanticClass cls=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass(scls);
        SemanticObject ref=SWBPlatform.getSemanticMgr().getOntology().getSemanticObject(sref);
        SWBFormMgr frm=new SWBFormMgr(cls,ref,null);
        frm.addHiddenParameter("sprop", sprop);

        SemanticObject obj=frm.processForm(request);
        if(obj!=null)
        {
            if(smode.equals(SWBFormMgr.MODE_CREATE))
            {
                SemanticProperty prop=null;
                if(sprop!=null)prop=SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(sprop);

                if(prop!=null && prop.hasInverse())
                {
                    //System.out.println("prop:"+prop.getURI()+" "+prop.hasInverse()+" "+prop.getInverse());
                    obj.setObjectProperty(prop.getInverse(), ref);
                }
                //GenericObject gobj=cls.newGenericInstance(obj);
                //if(gobj instanceof WebPage)
                //{
                //    ((WebPage)gobj).setParent((WebPage)cls.newGenericInstance(ref));
                //}
                
                out.println("<script type=\"text/javascript\">");
                out.println("hideDialog();");
                out.println("reloadTreeNodeByURI('"+ref.getURI()+"');");
                out.println("showStatus('"+obj.getSemanticClass().getDisplayName(lang)+" creado');");
                out.println("addNewTab('"+obj.getURI()+"','"+SWBPlatform.getContextPath()+"/swbadmin/jsp/objectTab.jsp"+"','"+obj.getDisplayName(lang)+"');");
                out.println("</script>");
            }
        }else
        {
            frm.setAction("/swb/swbadmin/jsp/SemObjectEditor.jsp");
            out.println(frm.renderForm(request));
            //out.println("hola...");
        }
    }else
    {
        SemanticObject obj=ont.getSemanticObject(suri);
        SemanticClass cls=obj.getSemanticClass();

        //out.println("<fieldset>");
        //out.println("<div class=\"swbIcon2"+cls.getName()+"\"></div>");
        //out.println("</fieldset>");

        String mode=SWBFormMgr.MODE_EDIT;

        //TODO: revisar mejor opcion
        String view=null;
        if(obj.getSemanticClass().equals(Resource.sclass))
        {
            int pmode=((org.semanticwb.model.Resource)obj.createGenericInstance()).getResourceType().getResourceMode();
            //view="resourceMode"+pmode;
        }
        //System.out.println(view);

        SWBFormMgr frm=new SWBFormMgr(obj, view,mode);
        frm.setLang(lang);
        if(smode!=null)
        {
            frm.processForm(request);
            out.println("<script type=\"text/javascript\">");
            //out.println("alert('mtreeStore:'+mtreeStore);");
            out.println("updateTreeNodeByURI('"+obj.getURI()+"');");

            String icon=SWBContext.UTILS.getIconClass(obj);
            out.println("setTabTitle('"+obj.getURI()+"','"+obj.getDisplayName(lang)+"','"+icon+"');");
            Iterator<SemanticObject> it2=obj.listRelatedObjects();
            while(it2.hasNext())
            {
                SemanticObject aux=it2.next();
                out.println("reloadTab('"+aux.getURI()+"');");
            }
            out.println("showStatus('"+obj.getSemanticClass().getDisplayName(lang)+" actualizado');");
            out.println("</script>");
        }
        frm.setAction("/swb/swbadmin/jsp/SemObjectEditor.jsp");


        if(user!=null)
        {
            boolean isfavo=user.hasFavorite(obj);
            if(!isfavo)
            {
                frm.addButton("<button dojoType='dijit.form.Button' onclick=\"showStatusURL('"+SWBPlatform.getContextPath()+"/swbadmin/jsp/favorites.jsp?suri="+obj.getEncodedURI()+"&act=active"+"');\">Agregar a Favoritos</button>");
            }else
            {
                frm.addButton("<button dojoType='dijit.form.Button' onclick=\"showStatusURL('"+SWBPlatform.getContextPath()+"/swbadmin/jsp/favorites.jsp?suri="+obj.getEncodedURI()+"&act=unactive"+"');\">Eliminar de Favoritos</button>");
            }
        }
        if(obj.getBooleanProperty(Undeleteable.swb_undeleteable)==false)
        {
            frm.addButton("<button dojoType='dijit.form.Button' onclick=\"if(confirm('Eliminar el elemento?'))showStatusURL('"+SWBPlatform.getContextPath()+"/swbadmin/jsp/delete.jsp?suri="+obj.getEncodedURI()+"');\">Eliminar</button>");
        }

        out.println(frm.renderForm(request));
     }
}catch(Exception e){e.printStackTrace();}
%>
<!-- a href="#" onclick="submitUrl('/swb/swb',this); return false;">click</a -->