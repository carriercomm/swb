/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.semanticwb.portal.admin.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.semanticwb.Logger;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBPortal;
import org.semanticwb.SWBUtils;
import org.semanticwb.model.GenericObject;
import org.semanticwb.model.Resource;
import org.semanticwb.model.Template;
import org.semanticwb.model.User;
import org.semanticwb.model.VersionInfo;
import org.semanticwb.model.Versionable;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticOntology;
import org.semanticwb.portal.SWBFormMgr;
import org.semanticwb.portal.api.GenericResource;
import org.semanticwb.portal.api.SWBActionResponse;
import org.semanticwb.portal.api.SWBParamRequest;
import org.semanticwb.portal.api.SWBResourceException;
import org.semanticwb.portal.api.SWBResourceURL;

/**
 *
 * @author juan.fernandez
 */
public class SWBATemplateEdit extends GenericResource {


    private Logger log = SWBUtils.getLogger(SWBAVersionInfo.class);
    String webpath = SWBPlatform.getContextPath();
    Resource base;
    SemanticOntology ont = SWBPlatform.getSemanticMgr().getOntology();

    @Override
    public void doView(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        response.setContentType("text/html; charset=ISO-8859-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        base = getResourceBase();
        User user = paramRequest.getUser();
        PrintWriter out = response.getWriter();
        String id = request.getParameter("suri");

        if(request.getParameter("dialog")!=null&&request.getParameter("dialog").equals("close"))
        {
            out.println("<script type=\"javascript\">");
            out.println(" hideDialog(); ");
//            SWBResourceURL urlview = paramRequest.getRenderUrl();
//            urlview.setParameter("suri", id);
//            urlview.setParameter("act","");
            out.println(" reloadTab('"+id+"'); ");
            out.println("</script>");
        }
   

        if (null == id) {
            out.println("<fieldset>");
            out.println("URI faltante");
            out.println("</fieldset>");
        } else {
            String action = request.getParameter("act");
            GenericObject obj = ont.getGenericObject(id);

            Template tmpl = (Template) obj;

//            System.out.println("web Work Path:"+SWBPlatform.getWebWorkPath());
//            System.out.println("context Path:"+SWBPlatform.getContextPath());
//            System.out.println("Work Path:"+SWBPlatform.getWorkPath()+"/models/"+tmpl.getWebSiteId()+"/Template/");
//            System.out.println("template work path: "+tmpl.getWorkPath());


            log.debug("doView(), suri: "+id);
            VersionInfo via = null;
            VersionInfo vio = null;

            if (obj instanceof Versionable) {
                vio = (VersionInfo) findFirstVersion(obj);
                via = ((Versionable) obj).getActualVersion();

                if (action == null || action.equals("")) {

                    log.debug("act:''");
                    out.println("<div class=\"swbform\">");
                    out.println("<fieldset>");
                    out.println("<table width=\"98%\" >");
                    out.println("<thead>");
                    out.println("<tr>");
                    out.println("<th>");
                    out.println(paramRequest.getLocaleString("msgVersion"));
                    out.println("</th>");
                    out.println("<th>");
                    out.println(paramRequest.getLocaleString("msgAction"));
                    out.println("</th>");
                    out.println("<th>");
                    out.println(paramRequest.getLocaleString("msgComment"));
                    out.println("</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody>");
                    if (null != vio) {
                        while (vio != null) {

                            out.println("<tr>");
                            out.println("<td align=\"center\">");
                            out.println(vio.getVersionNumber());
                            out.println("</td>");
                            out.println("<td>");

                            SWBResourceURL urle = paramRequest.getRenderUrl();
                            urle.setParameter("suri", id);
                            urle.setParameter("sobj", vio.getURI());
                            urle.setParameter("vnum", Integer.toString(vio.getVersionNumber()));
                            urle.setParameter("act", "edit_temp");
                            urle.setMode(SWBResourceURL.Mode_EDIT);
                            out.println("<a href=\"#\" onclick=\"submitUrl('" + urle + "',this); return false;\"><img src=\""+SWBPlatform.getContextPath()+"/swbadmin/icons/editar_1.gif\" border=\"0\" alt=\"editar version\"></a>");

                            SWBResourceURL urlnv = paramRequest.getRenderUrl();
                            urlnv.setParameter("suri", id);
                            urlnv.setParameter("sobj", vio.getURI());
                            urlnv.setParameter("vnum", Integer.toString(vio.getVersionNumber()));
                            urlnv.setParameter("act", "newversion");
                            urlnv.setMode(SWBResourceURL.Mode_EDIT);
                            out.println("<a href=\"#\" onclick=\"showDialog('" + urlnv + "','Nueva versión de Plantilla');\"><img src=\""+SWBPlatform.getContextPath()+"/swbadmin/icons/nueva_version.gif\" border=\"0\" alt=\"nueva version\"></a>");

                            if (!vio.equals(via)) {
                                SWBResourceURL urlsa = paramRequest.getActionUrl();
                                urlsa.setParameter("suri", id);
                                urlsa.setParameter("sval", vio.getURI());
                                urlsa.setAction("setactual");
                                out.println("<a href=\"#\" onclick=\"submitUrl('" + urlsa + "',this); return false;\"><img src=\""+SWBPlatform.getContextPath()+"/swbadmin/icons/cambio_version.gif\" border=\"0\" alt=\"cambiar version\"></a>");
                            } else {
                                out.println("<img src=\""+SWBPlatform.getContextPath()+"/swbadmin/icons/activa.gif\" border=\"0\" alt=\"version actual\">");
                            }

                            out.println("<a href=\"#\" onclick=\"window.open('"+SWBPlatform.getWebWorkPath()+tmpl.getWorkPath()+"/"+vio.getVersionNumber()+"/"+tmpl.getFileName(vio.getVersionNumber())+"','Preview','scrollbars, resizable, width=550, height=550');\"><img src=\""+SWBPlatform.getContextPath()+"/swbadmin/icons/preview.gif\" border=\"0\" alt=\"previsualizar\"></a>"); //submitUrl('" + urlec + "',this); return false;

                            SWBResourceURL urlr = paramRequest.getActionUrl();
                            urlr.setParameter("suri", id);
                            urlr.setParameter("sval", vio.getURI());
                            urlr.setAction("remove");
                            out.println("<a href=\"#\" onclick=\"submitUrl('" + urlr + "',this); return false;\"><img src=\"" + SWBPlatform.getContextPath() + "/swbadmin/images/delete.gif\" border=\"0\" alt=\"eliminar version\"></a>");
                            if (vio.equals(via)) {
                                out.println("( Version Actual ) ");
                            }
                            out.println("</td>");
                            out.println("<td>");
                            
                            String comment = vio.getVersionComment()!=null&&vio.getVersionComment().trim().length()>0&&!vio.getVersionComment().equals("null")?vio.getVersionComment():"";
                            out.println(comment+"</td>");
                            out.println("</tr>");
                            vio = vio.getNextVersion();
                        }
                    }
                    out.println("</tbody>");
                    out.println("</table>");
                    out.println("</fieldset>");
                    out.println("<fieldset>");
                    SWBResourceURL urlNew = paramRequest.getRenderUrl();
                    urlNew.setParameter("suri", id);
                    urlNew.setParameter("act","newversion");
                    urlNew.setMode(SWBResourceURL.Mode_EDIT);
                    out.println("<button dojoType=\"dijit.form.Button\" onclick=\"showDialog('" + urlNew + "','Agregar plantilla de defecto');\">" + paramRequest.getLocaleString("btn_addnew") + "</button>");
                    SWBResourceURL urlVersionReset = paramRequest.getRenderUrl();
                    urlVersionReset.setParameter("suri", id);
                    urlVersionReset.setParameter("act","resetversion");
                    out.println("<button dojoType=\"dijit.form.Button\" onclick=\"submitUrl('" + urlVersionReset + "',this); return false;\">" + paramRequest.getLocaleString("btn_versionreset") + "</button>");
                    out.println("</fieldset>");
                    out.println("</div>");
                }
            }
        }
    }

    private VersionInfo findFirstVersion(GenericObject obj) {
        VersionInfo ver = null;
        if (obj != null) {
            ver = ((Versionable) obj).getActualVersion();
        }
        if (null != ver) {
            while (ver.getPreviousVersion() != null) { //
                ver = ver.getPreviousVersion();
            }
        }
        return ver;
    }

    // Edición de la VersionInfo dependiendo el SemanticObject relacionado
    @Override
    public void doEdit(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        response.setContentType("text/html; charset=ISO-8859-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        base = getResourceBase();
        log.debug("doEdit");
        User user = paramRequest.getUser();
        String action = request.getParameter("act");
        String id = request.getParameter("suri");
        String idvi = request.getParameter("sobj");
        String vnum = request.getParameter("vnum");
        SemanticObject so = null;
        PrintWriter out = response.getWriter();
        SWBFormMgr fm = null;

        if (action.equals("newversion")) {

            SemanticObject soref = ont.getSemanticObject(id);
            SWBResourceURL urla = paramRequest.getActionUrl();
            urla.setAction("newversion");

            //WBFormElement sfe = new SWBFormElement(so);

            fm = new SWBFormMgr(Versionable.swb_VersionInfo, soref,null);
            fm.addHiddenParameter("suri", id);
            fm.addHiddenParameter("psuri", id);
            if(vnum!=null) fm.addHiddenParameter("vnum", vnum);
            //fm.addHiddenParameter("sobj", so.getURI());
            fm.setAction(urla.toString());
            out.println("<div class=\"swbform\">");
            out.println("<form id=\""+id+"/"+idvi+"/"+base.getId()+"/FVIComment\" action=\""+urla+"\" method=\"post\" onsubmit=\"submitForm('"+id+"/"+idvi+"/"+base.getId()+"/FVIComment');return false;\">");
            out.println("<input type=\"hidden\" name=\"suri\" value=\""+id+"\">");
            if(vnum!=null) out.println("<input type=\"hidden\" name=\"vnum\" value=\""+vnum+"\">");
            out.println("<fieldset>");
            out.println("<table>");
            out.println("<tbody>");
            out.println("<tr>");
            out.println("<td>");
            out.println(fm.renderElement(request, VersionInfo.swb_versionComment.getLabel())!=null?fm.renderElement(request, VersionInfo.swb_versionComment.getLabel()):"Comment");
            out.println("</td>");
            out.println("<td>");
            out.println(fm.renderElement(request, VersionInfo.swb_versionComment,SWBFormMgr.MODE_EDIT));
            out.println("</td>");
            out.println("</tr>");
            out.println("</tbody>");
            out.println("</table>");
            out.println("</filedset>");
            out.println("<filedset>");
            //out.println("<hr noshade>");
            out.println("<button dojoType=\"dijit.form.Button\" type=\"submit\" >Guardar</button>"); //_onclick=\"submitForm('"+id+"/"+idvi+"/"+base.getId()+"/FVIComment');return false;\"
            //out.println("<button dojoType=\"dijit.form.Button\">Favoritos</button>");
            //out.println("<button dojoType=\"dijit.form.Button\">Eliminar</button>");
            SWBResourceURL urlb = paramRequest.getRenderUrl();
            urlb.setMode(SWBResourceURL.Mode_VIEW);
            urlb.setParameter("act","");
            urlb.setParameter("suri",id);
            out.println("<button dojoType=\"dijit.form.Button\" onclick=\"submitUrl('" + urlb + "',this.domNode); return false;\">Cancelar</button>");
            out.println("</filedset>");
            out.println("</form>");
            out.println("</div>");

        } else if (action.equals("edit")) {

            SWBResourceURL urla = paramRequest.getActionUrl();
            urla.setAction("update");
            log.debug("VI id:"+idvi);
            so = ont.getSemanticObject(idvi);
            fm = new SWBFormMgr(so, null, SWBFormMgr.MODE_EDIT);
            fm.addHiddenParameter("suri", id);
            fm.addHiddenParameter("psuri", id);
            fm.addHiddenParameter("sobj", so.getURI());
            fm.setAction(urla.toString());

            out.println(fm.renderForm(request));
        } else if (action.equals("edit_temp")) {
            System.out.println("VNUM: "+vnum);
            SemanticObject obj=SemanticObject.createSemanticObject(id);
            if(obj!=null)
            {
                //User user=SWBPortal.getSessionUser();
                out.println("<div class=\"applet\">");
                SWBAEditor.getTemplateApplet(new java.io.PrintWriter(out), obj.getModel().getName(), obj.getId(), Integer.parseInt(vnum), user);
                SWBResourceURL urlb = paramRequest.getRenderUrl();
                urlb.setMode(SWBResourceURL.Mode_VIEW);
                urlb.setParameter("act","");
                urlb.setParameter("suri",id);
                out.println("<button dojoType=\"dijit.form.Button\" onclick=\"submitUrl('" + urlb + "',this.domNode); return false;\">Cancelar</button>");
                out.println("</div>");
            }
        }
    }

    @Override
    public void processAction(HttpServletRequest request, SWBActionResponse response) throws SWBResourceException, IOException {
        String id = request.getParameter("suri"); // uri de la plantilla
        String act = response.getAction();
        log.debug("Action:" + act + ", suri: " + id);
        GenericObject go = ont.getGenericObject(id);
        VersionInfo vio = null;
        VersionInfo via = null;
        VersionInfo vil = null;
        VersionInfo vin = null;
        if (act == null) {
            act = "";
        }
        if ("newversion".equals(act)) {
            if (go instanceof Versionable) {



                log.debug("processAction. newVersion(Versionable)");
                Versionable gov = (Versionable) go;
                SemanticObject sobase = ont.getSemanticObject(id);
                SemanticClass sc = VersionInfo.swb_VersionInfo;
                long lid = 0;
                if (sc.isAutogenId()) {
                    lid = sobase.getModel().getCounter(sc);
                }
                SemanticObject nvinf = sobase.getModel().createSemanticObject(sobase.getModel().getObjectUri("" + lid, sc), sc);
                GenericObject ngo = ont.getGenericObject(nvinf.getURI());
                vin = (VersionInfo) ngo;
                int vnum = 1;
                vio = (VersionInfo) findFirstVersion(go);
                if (vio != null) {
                    vil = gov.getLastVersion();
                    vnum = vil.getVersionNumber() + 1;
                    log.debug("version num:"+vnum);
                    nvinf.setObjectProperty(VersionInfo.swb_previousVersion, vil.getSemanticObject()); //vin.setVersionComment(VersionComment);
                    vil.getSemanticObject().setObjectProperty(VersionInfo.swb_nextVersion, nvinf);
                } else {
                    gov.getSemanticObject().setObjectProperty(Versionable.swb_actualVersion, nvinf);
                }
                nvinf.setIntProperty(VersionInfo.swb_versionNumber, vnum);
                nvinf.setProperty(VersionInfo.swb_versionFile, "template.html");
                String VersionComment = request.getParameter("versionComment");
                log.debug(VersionComment);
                if(VersionComment!=null) nvinf.setProperty(VersionInfo.swb_versionComment, VersionComment); //vin.setVersionComment(VersionComment);

                gov.getSemanticObject().setObjectProperty(Versionable.swb_lastVersion, nvinf);

                Template tmpl = (Template)go;
                if(request.getParameter("vnum")!=null)
                {
                    // copiar archivos
                    String rutaFS_source_path =SWBPlatform.getWorkPath()+tmpl.getWorkPath()+"/"+request.getParameter("vnum")+"/";
                    String rutaFS_target_path =SWBPlatform.getWorkPath()+tmpl.getWorkPath()+"/"+vnum+"/";
                    String rutaWeb_source_path =SWBPlatform.getWebWorkPath()+tmpl.getWorkPath()+"/"+request.getParameter("vnum");
                    String rutaWeb_target_path =SWBPlatform.getWebWorkPath()+tmpl.getWorkPath()+"/"+vnum;

                    if(SWBUtils.IO.copyStructure(rutaFS_source_path, rutaFS_target_path, true, rutaWeb_source_path, rutaWeb_target_path))
                    {
                        System.out.println("Copied OK");
                    }
                }
                else
                {
                    StringBuffer defaultTPL = new StringBuffer();
                    defaultTPL.append("<template method=\"setHeaders\" Content-Type=\"text/html\"  response=\"{response}\" />");
                    defaultTPL.append("\n<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
                    defaultTPL.append("\n<html>");
                    defaultTPL.append("\n<head>");
                    defaultTPL.append("\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
                    defaultTPL.append("\n<title>");
                    defaultTPL.append("\n   <TOPIC METHOD=\"getDisplayName\" LANGUAGE=\"{user@getLanguage}\"/>");
                    defaultTPL.append("\n</title>");
                    defaultTPL.append("\n<style type=\"text/css\">");
                    defaultTPL.append("\n    @import \"/swb/swbadmin/js/dojo/dijit/themes/soria/soria.css\";");
                    defaultTPL.append("\n    @import \"/swb/swbadmin/css/swb_portal.css\";");
                    defaultTPL.append("\n</style>");
                    defaultTPL.append("\n<script type=\"text/javascript\" src=\"{webpath}/swbadmin/js/dojo/dojo/dojo.js\" djConfig=\"parseOnLoad: true, isDebug: false\"></script>");
                    defaultTPL.append("\n<script type=\"text/javascript\" src=\"{webpath}/swbadmin/js/swb.js\"></script>");
                    defaultTPL.append("\n</head>");
                    defaultTPL.append("\n <body>");
                    defaultTPL.append("\n   <p style=\"margin-top: 0\">");
                    defaultTPL.append("\n   <Content></Content>");
                    defaultTPL.append("\n</p>");
                    defaultTPL.append("\n </body>");
                    defaultTPL.append("\n</html>\"");

                    String rutaFS_target_path =SWBPlatform.getWorkPath()+tmpl.getWorkPath()+"/"+vnum+"/";
                    File f = new File(rutaFS_target_path);
                    if(!f.exists()) f.mkdirs();

                    File ftmpl = new File(SWBPlatform.getWorkPath()+tmpl.getWorkPath()+"/"+vnum+"/template.html");
                    Writer output = new BufferedWriter(new FileWriter(ftmpl));
                    try {
                      output.write( defaultTPL.toString());
                    }
                    finally {
                      output.close();
                    }
                }

                response.setRenderParameter("dialog", "close");
                response.setRenderParameter("act", "");
                response.setMode(response.Mode_VIEW);
            }
        } else if ("update".equals(act)) {
            id=request.getParameter("psuri");
            response.setRenderParameter(act, "");
            response.setMode(response.Mode_VIEW);
        } else if ("setactual".equals(act)) {
            String idval = request.getParameter("sval");
            SemanticObject sobase = ont.getSemanticObject(id);
            SemanticObject soactual = ont.getSemanticObject(idval);
            sobase.setObjectProperty(Versionable.swb_actualVersion, soactual);
            response.setRenderParameter(act, "");
            response.setMode(response.Mode_VIEW);
        } else if ("remove".equals(act)) {
            log.debug("remove");
            String idval = request.getParameter("sval"); // version a eliminar
            log.debug("suri:"+id+"sval:"+idval);
            SemanticObject sobj = ont.getSemanticObject(id);
            GenericObject sobase = ont.getGenericObject(idval); //version info a eliminar
            vio = null;
            VersionInfo vip = null;
            vin = null;

            GenericObject gobj = ont.getGenericObject(id); // se obtiene la plantilla y se verifica que sea versionable
            if(gobj instanceof Versionable)
            {
                Versionable vigo = (Versionable) gobj;
                via = vigo.getActualVersion(); // Version Actual de la plantilla
                vil = vigo.getLastVersion(); // Última versión de la plantilla
                if(sobase instanceof VersionInfo)
                {
                    vio = (VersionInfo)sobase; // version a eliminar
                    vip = vio.getPreviousVersion();
                    vin = vio.getNextVersion();

                    if(null!=vip) // si es una versión intermedia ó la última versión
                    {
                        log.debug("version intermedia o ultima --- ");
                        if(null!=vin) // si es una version intermedia a eliminarse
                        {
                            log.debug("Version intermedia");
                            vip.setNextVersion(vin);
                            vin.setPreviousVersion(vip);

                            if(via.equals(vio)) sobj.setObjectProperty(Versionable.swb_actualVersion, vin.getSemanticObject());
                            if(vil.equals(vio)) sobj.setObjectProperty(Versionable.swb_lastVersion, vin.getSemanticObject());
                        }
                        else // la ultima version
                        {
                            log.debug("Ultima version");
                            vip.getSemanticObject().removeProperty(VersionInfo.swb_nextVersion);
                            if(via.equals(vio)) sobj.setObjectProperty(Versionable.swb_actualVersion, vip.getSemanticObject());
                            if(vil.equals(vio)) sobj.setObjectProperty(Versionable.swb_lastVersion, vip.getSemanticObject());
                        }
                    }
                    else if(null!=vin) //  era la primera version
                    {
                        log.debug("primera version");
                        vin.getSemanticObject().removeProperty(VersionInfo.swb_previousVersion);
                        if(via.equals(vio)) sobj.setObjectProperty(Versionable.swb_actualVersion, vin.getSemanticObject());
                        if(vil.equals(vio)) sobj.setObjectProperty(Versionable.swb_lastVersion, vin.getSemanticObject());
                    }
                    else if(vip==null&&vin==null) // es la única version
                    {
                        log.debug("Unica version");
                        gobj.getSemanticObject().removeProperty(Versionable.swb_actualVersion);
                        gobj.getSemanticObject().removeProperty(Versionable.swb_lastVersion);
                    }
                    int vnumdel = vio.getVersionNumber();
                    Template tmpl = (Template)go;
                    String rutaFS_source_path =SWBPlatform.getWorkPath()+tmpl.getWorkPath()+"/"+vnumdel;
                    if(SWBUtils.IO.removeDirectory(rutaFS_source_path))
                    {
                        System.out.println("Remove OK");
                    }

                    sobase.dispose();
                }
            }

            response.setRenderParameter(act, "");
            response.setMode(response.Mode_VIEW);
        }
        response.setRenderParameter("suri", id);
    }
}
