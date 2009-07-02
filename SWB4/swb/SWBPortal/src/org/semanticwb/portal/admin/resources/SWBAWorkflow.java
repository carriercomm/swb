/*
 * INFOTEC WebBuilder es una herramienta para el desarrollo de portales de conocimiento, colaboraci�n e integraci�n para Internet,
 * la cual, es una creaci�n original del Fondo de Informaci�n y Documentaci�n para la Industria INFOTEC, misma que se encuentra
 * debidamente registrada ante el Registro P�blico del Derecho de Autor de los Estados Unidos Mexicanos con el
 * No. 03-2002-052312015400-14, para la versi�n 1; No. 03-2003-012112473900 para la versi�n 2, y No. 03-2006-012012004000-01
 * para la versi�n 3, respectivamente.
 *
 * INFOTEC pone a su disposici�n la herramienta INFOTEC WebBuilder a trav�s de su licenciamiento abierto al p�blico (�open source�),
 * en virtud del cual, usted podr� usarlo en las mismas condiciones con que INFOTEC lo ha dise�ado y puesto a su disposici�n;
 * aprender de �l; distribuirlo a terceros; acceder a su c�digo fuente y modificarlo, y combinarlo o enlazarlo con otro software,
 * todo ello de conformidad con los t�rminos y condiciones de la LICENCIA ABIERTA AL P�BLICO que otorga INFOTEC para la utilizaci�n
 * de INFOTEC WebBuilder 3.2.
 *
 * INFOTEC no otorga garant�a sobre INFOTEC WebBuilder, de ninguna especie y naturaleza, ni impl�cita ni expl�cita,
 * siendo usted completamente responsable de la utilizaci�n que le d� y asumiendo la totalidad de los riesgos que puedan derivar
 * de la misma.
 *
 * Si usted tiene cualquier duda o comentario sobre INFOTEC WebBuilder, INFOTEC pone a su disposici�n la siguiente
 * direcci�n electr�nica:
 *
 *                                          http://www.webbuilder.org.mx
 */
/*
 * WBAWorkflow.java
 *
 * Created on 28 de octubre de 2004, 12:35 AM
 */
package org.semanticwb.portal.admin.resources;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;
import org.semanticwb.portal.api.SWBParamRequest;
import org.semanticwb.portal.api.SWBResourceException;
import org.w3c.dom.*;

import java.util.*;
import java.sql.*;
import org.semanticwb.Logger;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBUtils;
import org.semanticwb.model.GenericObject;
import org.semanticwb.model.PFlow;
import org.semanticwb.model.ResourceType;
import org.semanticwb.model.Role;
import org.semanticwb.model.SWBContext;
import org.semanticwb.model.User;
import org.semanticwb.model.WebPage;
import org.semanticwb.model.WebSite;
import org.semanticwb.platform.SemanticOntology;
import org.semanticwb.portal.admin.resources.workflow.proxy.WorkflowResponse;
import org.semanticwb.portal.api.GenericResource;
import org.semanticwb.portal.api.SWBResourceURL;

/**
 * Recurso para la administraci�n de WebBuilder que permite la administraci�n de
 * los flujos de trabajo.
 *
 * WebBuilder administration resource that allows the work flow administration.
 *
 * @author Victor Lorenzana
 */
public class SWBAWorkflow extends GenericResource
{

    private Logger log = SWBUtils.getLogger(SWBAWorkflow.class);

    /** Creates a new instance of WBAWorkflow */
    public SWBAWorkflow()
    {
    }

    /**
     *
     * @param request
     * @param response
     * @param paramRequest
     * @throws AFException
     * @throws IOException
     */
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException
    {
        if (paramRequest.getMode().equals("gateway"))
        {
            doGateway(request, response, paramRequest);
        }
        else
        {
            super.processRequest(request, response, paramRequest);
        }

    }

    /**
     *
     * @param cmd
     * @param src
     * @param user
     * @param request
     * @param response
     * @param paramRequest
     * @return
     */
    private Document getService(String cmd, Document src, User user, HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest)
    {
        return getDocument(user, src, cmd, paramRequest, request);
    }

    /**
     *
     * @param res
     * @param root
     */
    public void getPermisosGral(Element res, WebPage root)
    {

        ArrayList arr = new ArrayList((Collection) root.listChilds());

        for (int i = 0; i < arr.size(); i++)
        {
            WebPage child = (WebPage) arr.get(i);
            Element etopic = addNode("topic", child.getId(), child.getDisplayName(), res);
            getPermisosGral(etopic, child);
        }
    }

    /**
     *
     * @param res
     * @param tm
     */
    public void getCatalogRoles(Element res, String tm)
    {        
        Vector<Role> roles = new Vector<Role>();     
        WebSite map = SWBContext.getWebSite(tm);

        Iterator<Role> it = map.getUserRepository().listRoles();
        while (it.hasNext())
        {
            Role role = it.next();
            roles.add(role);
        }
        //Collections.sort(roles, new OrdenaRole());
        Iterator itRoles = roles.iterator();
        while (itRoles.hasNext())
        {
            Role role = (Role) itRoles.next();
            Element erole = addNode("role", "" + role.getId(), role.getTitle(), res);
            erole.setAttribute("id", "" + role.getId());
            erole.setAttribute("repository", "" + role.getUserRepository().getId());
        }

    }
//    /**
//     *
//     * @param res
//     * @param tm
//     * @param src
//     */
//    public void getProcessCount(Element res,String tm,Document src)
//    {
//        if(src.getElementsByTagName("workflow").getLength()>0)
//        {
//            Element workflow=(Element)src.getElementsByTagName("workflow").item(0);
//            String workflowID=workflow.getAttribute("id");
//            String topicmap="";
//            String version=workflow.getAttribute("version");
//            try
//            {
//                //TODO:WBProxyWorkflow.getProcessList
////                ArrayList processes=WBProxyWorkflow.getProcessList(topicmap, workflowID, version);
////                addElement("err",String.valueOf(processes.size()), res);
//                addElement("err","1", res);
//            }
//            catch(Exception e)
//            {
//                e.printStackTrace(System.out);
//                log.error(e);
//                addElement("err",e.getMessage(), res);
//            }
//        }
//        addElement("err","The element workflow was not found", res);
//    }

    /**
     *
     * @param res
     * @param tm
     * @param src
     */
    public void getWorkflow(Element res, String tm, Document src)
    {
        if (src.getElementsByTagName("id").getLength() > 0)
        {
            Element eid = (Element) src.getElementsByTagName("id").item(0);
            Text etext = (Text) eid.getFirstChild();
            String id = etext.getNodeValue();
            PFlow pflow = (PFlow) SWBPlatform.getSemanticMgr().getOntology().getGenericObject(id);
            try
            {
                String xml = pflow.getXml();

                Document doc = SWBUtils.XML.xmlToDom(xml);

                Element ele = (Element) doc.getElementsByTagName("workflow").item(0);

                ele.setAttribute("name", pflow.getTitle());
                Element desc = doc.createElement("description");
                desc.appendChild(doc.createTextNode(pflow.getDescription() != null ? pflow.getDescription() : "_"));

                ele.replaceChild(desc, ele.getChildNodes().item(0));

                Node eworkflow = res.getOwnerDocument().importNode(ele, true);
                res.appendChild(eworkflow);
            }
            catch (Exception e)
            {
                e.printStackTrace(System.out);
                addElement("err", e.getMessage(), res);
            }
        }
        else
        {
            addElement("err", java.util.ResourceBundle.getBundle("org/semanticwb/portal/admin/resources/SWBAWorkflow").getString("err1"), res);
        }
    }

    /**
     *
     * @param res
     * @param tm
     */
    public void getCatalogUsers(Element res, String tm)
    {
        Vector<User> users = new Vector<User>();        
        WebSite map = SWBContext.getAdminWebSite();
        Iterator<User> it = map.getUserRepository().listUsers();
        while (it.hasNext())
        {
            User user = it.next();
            if (map.getUserRepository().getId().equals(user.getUserRepository().getId()))
            {
                try
                {
                    //TODO: user.havePermission
                    //if(user.havePermission(SWBContext.getAdminWebSite().getWebPage("WBAd_per_Administrator")))
                    {
                        users.add(user);
                    }
                }
                catch (Exception ue)
                {
                    ue.printStackTrace(System.out);
                    log.error(ue);
                }
            }
        }

        Collections.sort(users, new OrdenaUsuarios());
        Iterator itUsers = users.iterator();
        while (itUsers.hasNext())
        {
            User user = (User) itUsers.next();
            try
            {
                Element erole = addNode("user", "" + user.getId(), user.getName(), res);
            }
            catch (Exception ue)
            {
                ue.printStackTrace(System.out);
                log.error(ue);
            }
        }

    }

    /**
     *
     * @param res
     * @param wf
     * @param user
     * @param tm
     * @param paramRequest
     * @param request
     */
//    public void add(Element res,Element wf,User user,String tm,SWBParamRequest paramRequest,HttpServletRequest request)
//    {
//        //PFlowSrv srv=new PFlowSrv();
//        String name=wf.getAttribute("name");
//        String description="";
//        if(wf.getElementsByTagName("description").getLength()>0)
//        {
//            Element edesc=(Element)wf.getElementsByTagName("description").item(0);
//            Text etext=(Text)edesc.getFirstChild();
//            description=etext.getNodeValue();
//
//        }
//        try
//        {
//            WebSite ws = SWBContext.getWebSite(tm);
//            PFlow pflow=ws.createPFlow();
//            pflow.setTitle(name);
//            pflow.setDescription(description);
//            pflow.setXml(SWBUtils.XML.domToXml(wf.getOwnerDocument()));
//            //TODO:
//            //PFlow pflow=srv.createPFlow(tm, name, description, wf.getOwnerDocument(), user.getId());
//            addElement("workflowid",  String.valueOf(pflow.getId()), res);
//            addElement("version", "1", res);
//        }
//        catch(Exception e)
//        {
//            log.error(e);
//            e.printStackTrace(System.out);
//            addElement("err", java.util.ResourceBundle.getBundle("org/semanticwb/portal/admin/resources/SWBAWorkflow").getString("msg1"), res);
//        }
//
//    }
    /**
     *
     * @param res
     * @param src
     * @param user
     * @param tm
     * @param paramRequest
     * @param request
     */
    public void update(Element res, Document src, User user, String tm, SWBParamRequest paramRequest, HttpServletRequest request)
    {
        try
        {
            Element wf = (Element) src.getElementsByTagName("workflow").item(0);
            String id = wf.getAttribute("id");
//            if(id==null || id.trim().equals(""))
//            {
//                add(res,wf,user,tm,paramRequest,request);
//            }
//            else
            {
                String idpflow = wf.getAttribute("id");
//                PFlowSrv sPFlowSrv=new PFlowSrv();
                try
                {
                    WorkflowResponse wresp = updatePflow(tm, src, user.getId());
                    addElement("workflowid", idpflow, res);
                    addElement("version", String.valueOf(wresp.getVersion()), res);
                }
                catch (Exception e)
                {
                    log.error(e);
                    e.printStackTrace(System.out);
                    addElement("err", java.util.ResourceBundle.getBundle("org/semanticwb/portal/admin/resources/SWBAWorkflow").getString("msg1"), res);
                }
            }
        }
        catch (Exception e)
        {
            log.error(e);
            e.printStackTrace(System.out);
            addElement("err", java.util.ResourceBundle.getBundle("org/semanticwb/portal/admin/resources/SWBAWorkflow").getString("msg1") + " error: " + e.getMessage(), res);
        }

    }

    /**
     *
     * @param res
     * @param tm
     */
    public void getResourceTypeCat(Element res, String tm)
    {
        HashSet<String> resources = new HashSet<String>();
        WebSite map = SWBContext.getWebSite(tm);
        Iterator<ResourceType> elements = map.listResourceTypes();
        while (elements.hasNext())
        {
            ResourceType obj = elements.next();
            if (obj.getResourceMode() == 1 || obj.getResourceMode() == 3)
            {
                if (!resources.contains(obj.getId()))
                {
                    resources.add(obj.getId());
                    Element erole = addNode("resourceType", "" + obj.getId(), obj.getTitle(), res);
                    erole.setAttribute("topicmap", map.getId());
                    erole.setAttribute("topicmapname", map.getTitle());
                    String description = "_";
                    if (obj.getDescription() != null)
                    {
                        description = obj.getDescription();
                    }
                    addElement("description", description, erole);
                }
            }
        }
    }

    /**
     *
     * @param user
     * @param src
     * @param cmd
     * @param paramRequest
     * @param request
     * @return
     */
    public Document getDocument(User user, Document src, String cmd, SWBParamRequest paramRequest, HttpServletRequest request)
    {
        Document dom = null;
        try
        {
            String tm = null;
            if (src.getElementsByTagName("tm").getLength() > 0)
            {
                Node etm = src.getElementsByTagName("tm").item(0);
                Text etext = (Text) etm.getFirstChild();
                tm = etext.getNodeValue();
            }

            dom = SWBUtils.XML.getNewDocument();
            Element res = dom.createElement("res");
            dom.appendChild(res);

            if (cmd.equals("getcatRoles"))
            {
                getCatalogRoles(res, tm);
            }
            else if (cmd.equals("getResourceTypeCat"))
            {
                getResourceTypeCat(res, tm);
            }
            else if (cmd.equals("getcatUsers"))
            {
                getCatalogUsers(res, tm);
            }
            else if (cmd.equals("getWorkflow"))
            {


                getWorkflow(res, tm, src);
            }
            else if (cmd.equals("update"))
            {
                update(res, src, user, tm, paramRequest, request);
            }
        }
        catch (Exception e)
        {
            log.error(e);
            return getError(3);
        }
        return dom;
    }

    /**
     *
     * @param user
     * @param src
     * @param action
     * @return
     */
    public Document getPath(User user, Document src, String action)
    {
        Document dom = null;
        try
        {
            dom = SWBUtils.XML.getNewDocument();
            Element res = dom.createElement("res");
            dom.appendChild(res);

            StringTokenizer st = new StringTokenizer(action, ".");
            String cmd = st.nextToken();

            if (cmd.equals("topic"))
            {
                String tmid = st.nextToken();
                String tpid = st.nextToken();

                StringBuffer ret = new StringBuffer();
                WebPage tp = SWBContext.getWebSite(tmid).getWebPage(tpid);
                ret.append(tp.getId());
                while (tp != tp.getWebSite().getHomePage())
                {
                    tp = tp.getParent();
                    ret.insert(0, tp.getId() + ".");
                }
                ret.insert(0, tmid + ".");
                res.appendChild(dom.createTextNode(ret.toString()));
            }


        }
        catch (Exception e)
        {
            log.error(e);
            return getError(3);
        }
        return dom;
    }

    /**
     *
     * @param node
     * @param id
     * @param name
     * @param parent
     * @return
     */
    private Element addNode(String node, String id, String name, Element parent)
    {
        Element ret = addElement(node, null, parent);
        if (id != null)
        {
            ret.setAttribute("id", id);
        }
        if (name != null)
        {
            ret.setAttribute("name", name);
        }
        return ret;
    }

    /**
     *
     * @param name
     * @param value
     * @param parent
     * @return
     */
    private Element addElement(String name, String value, Element parent)
    {
        Document doc = parent.getOwnerDocument();
        Element ele = doc.createElement(name);
        if (value != null)
        {
            ele.appendChild(doc.createTextNode(value));
        }
        parent.appendChild(ele);
        return ele;
    }

    /**
     *
     * @param id
     * @return
     */
    private Document getError(int id)
    {
        Document dom = null;
        try
        {
            dom = SWBUtils.XML.getNewDocument();
            Element res = dom.createElement("res");
            dom.appendChild(res);
            Element err = dom.createElement("err");
            res.appendChild(err);
            addElement("id", "" + id, err);
            if (id == 0)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_loginfail") + "...", err);
            }
            else if (id == 1)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_nouser") + "...", err);
            }
            else if (id == 2)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_noservice") + "...", err);
            }
            else if (id == 3)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_serviceprocessfail") + "...", err);
            }
            else if (id == 4)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_parametersprocessfail") + "...", err);
            }
            else if (id == 5)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_noTopicmap") + "...", err);
            }
            else if (id == 6)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_noTopic") + "...", err);
            }
            else if (id == 7)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_usernopermiss") + "...", err);
            }
            else if (id == 8)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_TopicAlreadyexist") + "...", err);
            }
            else if (id == 9)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_byImplement") + "...", err);
            }
            else if (id == 10)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_TopicMapAlreadyExist") + "...", err);
            }
            else if (id == 11)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_FileNotFound") + "...", err);
            }
            else if (id == 12)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_noversions") + "...", err);
            }
            else if (id == 13)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getError_xmlinconsistencyversion") + "...", err);
            }
            else if (id == 14)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getError_noResourcesinMemory") + "...", err);
            }
            else if (id == 15)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getError_noTemplatesinMemory") + "...", err);
            }
            else if (id == 16)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getError_TemplatenotRemovedfromFileSystem") + "...", err);
            }
            else if (id == 17)
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getError_adminUsernotCreated") + "...", err);
            }
            else
            {
                addElement("message", SWBUtils.TEXT.getLocaleString("locale_Gateway", "usrmsg_Gateway_getService_errornotfound") + "...", err);
            }
        }
        catch (Exception e)
        {
            log.error(SWBUtils.TEXT.getLocaleString("locale_Gateway", "error_Gateway_getService_documentError") + "...", e);
        }
        return dom;
    }

    /**
     *
     * @param request
     * @param response
     * @param paramRequest
     * @throws AFException
     * @throws IOException
     */
    public void doGateway(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException
    {
        PrintWriter out = response.getWriter();
        ServletInputStream in = request.getInputStream();
        Document dom = SWBUtils.XML.xmlToDom(in);
//        System.out.println("gateway: " + SWBUtils.XML.domToXml(dom));
        if (!dom.getFirstChild().getNodeName().equals("req"))
        {
            response.sendError(404, request.getRequestURI());
            return;
        }
        String cmd = null;
        if (dom.getElementsByTagName("cmd").getLength() > 0)
        {
            cmd = dom.getElementsByTagName("cmd").item(0).getFirstChild().getNodeValue();
        }

        if (cmd == null)
        {
            response.sendError(404, request.getRequestURI());
            return;
        }
        String ret = "";
        try
        {
            Document res = getService(cmd, dom, paramRequest.getUser(), request, response, paramRequest);
            if (res == null)
            {
                ret = SWBUtils.XML.domToXml(getError(3));
            }
            else
            {
                ret = SWBUtils.XML.domToXml(res, true);
            }
//            System.out.println("ret:" + ret);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        out.print(new String(ret.getBytes()));
    }

    @Override
    public void doView(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException
    {
        String id = request.getParameter("suri");
        SemanticOntology ont = SWBPlatform.getSemanticMgr().getOntology();
        GenericObject go = ont.getGenericObject(id);
        PFlow pfgo = (PFlow) go;
//        System.out.println("pf xml: " + pfgo.getXml());
        if (pfgo != null && (pfgo.getXml() == null || (pfgo.getXml() != null && pfgo.getXml().trim().length() == 0)))
        {
//            System.out.println("pf xml es NULL");
            Document newdoc = SWBUtils.XML.getNewDocument();
            Element wfs = newdoc.createElement("workflows");
            Element wf = newdoc.createElement("workflow");
            wf.setAttribute("id", id);
            wf.setAttribute("name", pfgo.getTitle());
            wf.setAttribute("version", "1.0");
            wfs.appendChild(wf);
            Element edes = newdoc.createElement("description");
            edes.appendChild(newdoc.createTextNode(pfgo.getDescription() != null ? pfgo.getDescription() : "_"));
            wf.appendChild(edes);
            newdoc.appendChild(wfs);
            String xmlpflow = SWBUtils.XML.domToXml(newdoc);
//            System.out.println("XML: " + xmlpflow);
            pfgo.setXml(xmlpflow);

        }

//        System.out.println("XML AFTER: " + pfgo.getXml());

        String tm = pfgo.getWebSite().getId();
        try
        {
            User user = paramRequest.getUser();
            PrintWriter out = response.getWriter();
            String act = "edit";
            if (request.getParameter("act") != null)
            {
                act = request.getParameter("act");
            }
            else if (act.equals("edit") && id != null && user != null && tm != null)
            {

//                System.out.println("Con OBJECT");
//
//                out.println("<OBJECT id=\"apptree\" name=\"editrole\" classid=\"clsid:CAFEEFAC-0014-0002-0000-ABCDEFFEDCBA\" ");
//                out.println("width=\"100%\" height=\"350\" >");
//                //out.println("codebase=\"http://java.sun.com/products/plugin/autodl/jinstall-1_4_2-windows-i586.cab#Version=1,4,2,0\"> ");
//                out.println("<PARAM name=\"java_code\" value=\"applets.workflowadmin.EditWorkflow.class\">");
//                out.println("<PARAM name=\"java_codebase\" value=\"" + SWBPlatform.getContextPath() + "\">");
//                out.println("<PARAM name=\"java_archive\" value=\"swbadmin/lib/SWBAplWorkFlowAdmin.jar, swbadmin/lib/SWBAplCommons.jar\">");
//
                SWBResourceURL url = paramRequest.getRenderUrl();
                url.setMode("gateway");
                url.setCallMethod(SWBResourceURL.Call_DIRECT);
//                out.println("<PARAM NAME =\"idworkflow\" VALUE=\"" + id + "\">");
//                out.println("<PARAM NAME =\"cgipath\" VALUE=\"" + url + "\">");
//                out.println("<PARAM NAME =\"locale\" VALUE=\"" + user.getLanguage() + "\">");
//                out.println("<PARAM NAME =\"tm\" VALUE=\"" + tm + "\">");
//                out.println("    No Java 2 SDK, Standard Edition v 1.5.0 support for APPLET!!");
//                out.println("</OBJECT>");

//                System.out.println("Con APPLET");

                out.println("<div class=\"applet\">");
                out.println("<APPLET id=\"apptree\" name=\"editrole\" code=\"applets.workflowadmin.EditWorkflow.class\" codebase=\"" + SWBPlatform.getContextPath() + "/\" ARCHIVE=\"swbadmin/lib/SWBAplWorkFlowAdmin.jar, swbadmin/lib/SWBAplCommons.jar\" width=\"100%\" height=\"350\">");
                out.println("<PARAM NAME =\"idworkflow\" VALUE=\"" + id + "\">");
                out.println("<PARAM NAME =\"cgipath\" VALUE=\"" + url + "\">");
                out.println("<PARAM NAME =\"locale\" VALUE=\"" + user.getLanguage() + "\">");
                out.println("<PARAM NAME =\"tm\" VALUE=\"" + tm + "\">");
                out.println("</APPLET>");
                out.println("</div>");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            log.error(e);
            return;
        }
    }

    private class OrdenaUsuarios implements Comparator
    {

        /**
         *
         * @param o1
         * @param o2
         * @return
         */
        public int compare(Object o1, Object o2)
        {
            if (o1 instanceof User && o2 instanceof User)
            {
                User u1 = (User) o1;
                User u2 = (User) o2;
                return u1.getName().trim().toLowerCase().compareTo(u2.getName().trim().toLowerCase());
            }
            return 0;
        }
    }

    private class OrdenaRole implements Comparator
    {

        /**
         *
         * @param o1
         * @param o2
         * @return
         */
        public int compare(Object o1, Object o2)
        {
            if (o1 instanceof Role && o2 instanceof Role)
            {
                Role u1 = (Role) o1;
                Role u2 = (Role) o2;
                return u1.getTitle().trim().toLowerCase().compareTo(u2.getTitle().trim().toLowerCase());
            }
            return 0;
        }
    }

    public WorkflowResponse updatePflow(String tm, Document xml, String userid) throws SWBResourceException, Exception
    {
        //regreso inicial WorkflowResponse
        if (xml.getElementsByTagName("workflow").getLength() > 0)
        {
            Element wf = (Element) xml.getElementsByTagName("workflow").item(0);
            String idpflow = wf.getAttribute("id");
            wf.setAttribute("id", idpflow + "_" + tm);
            String name = wf.getAttribute("name");
            String description = "";
            if (wf.getElementsByTagName("description").getLength() > 0)
            {
                Element edesc = (Element) wf.getElementsByTagName("description").item(0);
                Text etext = (Text) edesc.getFirstChild();
                description = etext.getNodeValue();
            }
            //User user = SWBContext.getWebSite(tm).getUserRepository().getUser(userid);
            PFlow pflow = (PFlow) SWBPlatform.getSemanticMgr().getOntology().getGenericObject(idpflow);
            //pflow.setCreator(user);
//            pflow.setUpdated(new Timestamp(System.currentTimeMillis()));
//            pflow.setDescription(description);
//            pflow.setTitle(name);

            try
            {
                Document docworkflows = SWBUtils.XML.xmlToDom(pflow.getXml());
                if (docworkflows.getElementsByTagName("workflows").getLength() > 0)
                {
                    Element workflows = (Element) docworkflows.getElementsByTagName("workflows").item(0);
                    Element nodewf = (Element) docworkflows.importNode(wf, true);
                    nodewf = (Element) workflows.insertBefore(nodewf, docworkflows.getElementsByTagName("workflow").item(0));
                    if (nodewf.getAttribute("version") != null && !nodewf.getAttribute("version").equals(""))
                    {
                        String sversion = nodewf.getAttribute("version");
                        int iversion = (int) Double.parseDouble(sversion);
                        iversion++;
                        nodewf.setAttribute("version", iversion + ".0");
                    }
                    try
                    {
                        Document doc = SWBUtils.XML.getNewDocument();
                        doc.appendChild(doc.importNode(nodewf, true));

                        //TODO: WBProxyWorkflow.PublisFlow(doc)
                        //WorkflowResponse wresp=WBProxyWorkflow.PublisFlow(doc);
                        WorkflowResponse wresp = new WorkflowResponse("1", 1);
                        Document docenc = SWBUtils.XML.xmlToDom(pflow.getXml());
                        NodeList nlWorkflows = docenc.getElementsByTagName("workflow");
                        int l = nlWorkflows.getLength();
                        switch (l)
                        {
                            case 0:
                            default:
                                Element oldworkflow = (Element) nlWorkflows.item(0);
                                int version = (int) Double.parseDouble(oldworkflow.getAttribute("version"));
                                version++;
                                nodewf.setAttribute("version", version + ".0");
                        }
                        pflow.setXml(SWBUtils.XML.domToXml(docworkflows));
                        return wresp;
                    }
                    catch (Exception e)
                    {
                        log.error(e);
                        e.printStackTrace(System.out);
                        throw e;
                    }
                }
                else
                {
                    throw new SWBResourceException("The xml has not a workflows element (updatePflow)");
                }
            }
            catch (Exception e)
            {
                log.error(e);
                throw e;
            }
        }
        else
        {
            throw new SWBResourceException("The xml has not a workflow element (updatePflow)");
        }

    }
}
