/**  
* SWB Social es una plataforma que descentraliza la publicación, seguimiento y monitoreo hacia las principales redes sociales. 
* SWB Social escucha y entiende opiniones acerca de una organización, sus productos, sus servicios e inclusive de su competencia, 
* detectando en la información sentimientos, influencia, geolocalización e idioma, entre mucha más información relevante que puede ser 
* útil para la toma de decisiones. 
* 
* SWB Social, es una herramienta basada en la plataforma SemanticWebBuilder. SWB Social, como SemanticWebBuilder, es una creación original 
* del Fondo de Información y Documentación para la Industria INFOTEC, cuyo registro se encuentra actualmente en trámite. 
* 
* INFOTEC pone a su disposición la herramienta SWB Social a través de su licenciamiento abierto al público (‘open source’), 
* en virtud del cual, usted podrá usarla en las mismas condiciones con que INFOTEC la ha diseñado y puesto a su disposición; 
* aprender de élla; distribuirla a terceros; acceder a su código fuente y modificarla, y combinarla o enlazarla con otro software, 
* todo ello de conformidad con los términos y condiciones de la LICENCIA ABIERTA AL PÚBLICO que otorga INFOTEC para la utilización 
* del SemanticWebBuilder 4.0. y SWB Social 1.0
* 
* INFOTEC no otorga garantía sobre SWB Social, de ninguna especie y naturaleza, ni implícita ni explícita, 
* siendo usted completamente responsable de la utilización que le dé y asumiendo la totalidad de los riesgos que puedan derivar 
* de la misma. 
* 
* Si usted tiene cualquier duda o comentario sobre SemanticWebBuilder o SWB Social, INFOTEC pone a su disposición la siguiente 
* dirección electrónica: 
*  http://www.semanticwebbuilder.org
**/ 
 
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semanticwb.social.admin.resources;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.semanticwb.Logger;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBUtils;
import org.semanticwb.model.WebPage;
import org.semanticwb.model.WebSite;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.portal.api.GenericResource;
import org.semanticwb.portal.api.SWBActionResponse;
import org.semanticwb.portal.api.SWBParamRequest;
import org.semanticwb.portal.api.SWBResourceException;
import org.semanticwb.portal.api.SWBResourceURL;
import org.semanticwb.social.FacePageTab;
import org.semanticwb.social.Facebook;
import org.semanticwb.social.FacebookFanPage;
import org.semanticwb.social.SocialSite;

/**
 *
 * @author francisco.jimenez
 */
public class ImportFanPages extends GenericResource{
    public static Logger log = SWBUtils.getLogger(ImportFanPages.class);
    public static String IMPORT_FB_PAGES = "importFBPages";
    public static String IMPORT_FB_PAGES_FOR_ADMIN = "importFBPagesForAdmin";
    
    @Override
    public void doView(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        ////System.out.println("loading JSP importFP");
        final String path = SWBPlatform.getContextPath() + "/work/models/" + paramRequest.getWebPage().getWebSiteId() + "/jsp/fanPages/importFPFacebook.jsp";
        request.setAttribute("paramRequest", paramRequest);
        
        try{
            RequestDispatcher rd = request.getRequestDispatcher(path);
            rd.include(request, response);
        }catch(Exception e){
            log.error("Error in: " + e);
        }
    }

    @Override
    public void processAction(HttpServletRequest request, SWBActionResponse response) throws SWBResourceException, IOException {
        String action = response.getAction();
        ////System.out.println("process action:" + action);
        WebPage rootPage = null;
        if(action.equals(IMPORT_FB_PAGES)){
            String suri = request.getParameter("suri") == null ? "" : request.getParameter("suri");
            String site = request.getParameter("site") == null ? "" : request.getParameter("site");
            String pages[] = request.getParameterValues("pages") == null ? null : request.getParameterValues("pages");
            ////System.out.println("suri:" + suri);
            ////System.out.println("site:" + site);
            ////System.out.println("pages[]:" + pages);
            //add facebook pages
            if(suri.isEmpty() || site.isEmpty() || pages == null){
                response.setMode(SWBResourceURL.Mode_HELP);
                return;
            }
            
            Facebook facebook = (Facebook) SemanticObject.createSemanticObject(suri).createGenericInstance();
            WebSite wsite = WebSite.ClassMgr.getWebSite(site);
            
            //WebPage homePage = wsite.getHomePage();
            WebPage homePage = WebPage.ClassMgr.getWebPage("Fan_Pages", wsite);
            if(homePage == null){
                homePage = WebPage.ClassMgr.createWebPage("Fan_Pages", wsite);
                homePage.setTitle("Fan Pages");
                homePage.setParent(wsite.getHomePage());
                homePage.setActive(true);
            }
            WebPage rootFP = WebPage.ClassMgr.getWebPage(facebook.getFacebookUserId(), wsite);
            if(rootFP == null){
                rootFP = WebPage.ClassMgr.createWebPage(facebook.getFacebookUserId(), wsite);
                rootFP.setActive(true);
            }
            rootPage = rootFP;
            rootFP.setTitle(facebook.getTitle() + " Fan Pages");
            rootFP.setParent(homePage);
            HashMap<String, String> params = new HashMap<String, String>(2);
        
            params.put("access_token", facebook.getAccessToken());
            String respFanPage = postRequest(params, "https://graph.facebook.com/me/accounts",
                                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95", "GET");
            ////System.out.println("respFanPage:" + respFanPage);
            try{
                JSONObject responseFP = new JSONObject(respFanPage);
                if(!responseFP.isNull("data")){
                    if(responseFP.getJSONArray("data").length()>0){
                        JSONArray jarr = responseFP.getJSONArray("data");
                        for(int i = 0 ; i <  jarr.length() ; i++){                            
                            JSONObject entryFP = jarr.getJSONObject(i);
                            boolean selectedFanPage = false;
                            for(int j = 0; j < pages.length; j++){//check if the page was selected
                                if(pages[j].equals(entryFP.getString("id"))){
                                    selectedFanPage = true;
                                    break;
                                }
                            }
                            
                            if(selectedFanPage){//import this page                                
                                String id = "";
                                String name = "";
                                String token = "";
                                if(!entryFP.isNull("id")){
                                    id = entryFP.getString("id");
                                }
                                if(!entryFP.isNull("name")){
                                    name = entryFP.getString("name");
                                }
                                if(!entryFP.isNull("access_token")){
                                    token = entryFP.getString("access_token");
                                }
                                if(!id.isEmpty() && !token.isEmpty()){//Add the Fan Pages
                                    FacebookFanPage ffp = FacebookFanPage.ClassMgr.getFacebookFanPage(id, wsite);
                                    if(ffp == null){
                                        ffp = FacebookFanPage.ClassMgr.createFacebookFanPage(id, wsite);
                                        ffp.setActive(true);
                                    }
                                    ffp.setPage_id(id);
                                    ffp.setTitle(name);
                                    ffp.setPageAccessToken(token);
                                    ffp.setParent(rootFP);
                                    ffp.setSn_socialNet(facebook);
                                    createFPTabs(facebook, ffp, wsite);
                                }
                            }                            
                        }
                    }
                }
            }catch(JSONException jsone){
                log.error("Unable to add facebook pages", jsone);
            }
            
            
            /*for(int i = 0; i < pages.length; i++){
                String pageId = pages[i];
                
                try{
                    JSONObject page = new JSONObject(respFanPage);
                    if(page.isNull("error")){
                        log.error("Fan page with id does not exist." + page);
                        continue;
                    }
                    String id = "";
                    String name = "";
                    String description ="";
                    String token = "";
                }catch(JSONException jsone){
                    log.error("Unable to add fan page ", jsone );
                    continue;
                }
                
            }*/
            response.setRenderParameter("accountName", facebook.getTitle());
            response.setRenderParameter("homePageSuri", wsite.getHomePage().getEncodedURI());
            response.setMode(SWBResourceURL.Mode_HELP);
        }else if(action.equals(IMPORT_FB_PAGES_FOR_ADMIN)){
            String suri = request.getParameter("suri") == null ? "" : request.getParameter("suri");
            String site = request.getParameter("site") == null ? "" : request.getParameter("site");
            String pages[] = request.getParameterValues("pages") == null ? null : request.getParameterValues("pages");
            ////System.out.println("suri:" + suri);
            ////System.out.println("site:" + site);
            ////System.out.println("pages[]:" + pages);
            //add facebook pages
            if(suri.isEmpty() || site.isEmpty() || pages == null){
                response.setMode(SWBResourceURL.Mode_HELP);
                return;
            }
            Facebook facebook = (Facebook) SemanticObject.createSemanticObject(suri).createGenericInstance();
            SocialSite wsite = SocialSite.ClassMgr.getSocialSite(site);
            HashMap<String, String> params = new HashMap<String, String>(2);
            params.put("access_token", facebook.getAccessToken());
            
            String respFanPage = postRequest(params, "https://graph.facebook.com/me/accounts",
                                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95", "GET");
            ////System.out.println("respFanPage:" + respFanPage);
            try{
                JSONObject responseFP = new JSONObject(respFanPage);
                if(!responseFP.isNull("data")){
                    if(responseFP.getJSONArray("data").length()>0){
                        JSONArray jarr = responseFP.getJSONArray("data");
                        for(int i = 0 ; i <  jarr.length() ; i++){                            
                            JSONObject entryFP = jarr.getJSONObject(i);
                            boolean selectedFanPage = false;
                            for(int j = 0; j < pages.length; j++){//check if the page was selected
                                if(pages[j].equals(entryFP.getString("id"))){
                                    selectedFanPage = true;
                                    break;
                                }
                            }
                            
                            if(selectedFanPage){//import this page                                
                                String id = "";
                                String name = "";
                                String token = "";
                                
                                if(!entryFP.isNull("id")){
                                    id = entryFP.getString("id");
                                }
                                if(!entryFP.isNull("name")){
                                    name = entryFP.getString("name");
                                }
                                if(!entryFP.isNull("access_token")){
                                    token = entryFP.getString("access_token");
                                }
                                if(!id.isEmpty() && !token.isEmpty()){//Add the Fan Pages
                                    Facebook fanpage = Facebook.ClassMgr.createFacebook(wsite);
                                    fanpage.setAccessToken(token);
                                    fanpage.setSn_authenticated(true);
                                    fanpage.setTitle(name);
                                    fanpage.setActive(true);
                                    fanpage.setFacebookUserId(id);
                                    fanpage.setIsFanPage(true);
                                }
                            }                            
                        }
                    }
                }
                response.setRenderParameter("accountName", facebook.getTitle());                
                response.setRenderParameter("reloadGroup", wsite.getEncodedURI());
                response.setMode(SWBResourceURL.Mode_HELP);
            }catch(JSONException jsone){
                log.error("Unable to add facebook pages", jsone);
            }
        }
    }

    @Override
    public void doHelp(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        PrintWriter out = response.getWriter();        
        ////System.out.println("--->" + request.getParameter("homePageSuri"));
        ////System.out.println("------>" + URLDecoder.decode(request.getParameter("homePageSuri")));
        String brandSuri = request.getParameter("reloadGroup") == null ? null : URLDecoder.decode(request.getParameter("reloadGroup"));
        //WebPage reloadPage = (WebPage)SemanticObject.createSemanticObject(URLDecoder.decode(request.getParameter("homePageSuri"))).createGenericInstance();
                
        out.println("<div id=\"configuracion_redes\">");
        out.println("<p>Las p&aacute;ginas de Fans de la cuenta <b>" + request.getParameter("accountName") + "</b> fueron importadas correctamente.</p>");
        out.println("</div>");
        out.println("<script type=\"text/javascript\">");        
        if(brandSuri != null){
            out.println("try{");
            out.println("reloadTreeNodeByURI(\"HN|" + brandSuri + "|http://www.semanticwebbuilder.org/swb4/social#hn_Facebook\");");
            out.println("}catch(e){}");
        }else{
            out.println("updateTreeNodeByURI('"+ URLDecoder.decode(request.getParameter("homePageSuri"))+"');");
            out.println("parent.updateTreeNodeByURI('"+ URLDecoder.decode(request.getParameter("homePageSuri"))+"');");
            //out.println("parent.parent.updateTreeNodeByURI('"+ URLDecoder.decode(request.getParameter("homePageSuri"))+"');");
        }
        //out.println("addItemByURI(mtreeStore, null, '" + reloadPage.getURI() + "');");
        //out.printl("alert('done');");
        out.println("</script>");

    }
    
    private boolean createFPTabs(Facebook facebook, FacebookFanPage fp, WebSite wsite){
    
        if(fp == null || facebook == null){
                return false;
        }

        try {
            HashMap<String, String> params = new HashMap<String, String>(2);
            params.put("access_token", fp.getPageAccessToken());
            String pageTabs = postRequestParams(params, "https://graph.facebook.com/" + fp.getPage_id() +"/tabs",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95", "GET");
            ////System.out.println("\n\n--------------------\n" + pageTabs);
            JSONObject jsonObject = new JSONObject(pageTabs);            
            if(jsonObject.has("data")){
                JSONArray data = jsonObject.getJSONArray("data");
                if(data.length() > 0 ){
                    for(int i =0 ; i < data.length() ; i++){
                        JSONObject tmp = data.getJSONObject(i);
                        String id = "";
                        String name = "";
                        String position = "";
                        String appId = "";
                        
                        //Do not import page if is not an application
                        //or its the likes/photos tab.
                        if(tmp.isNull("application") ||
                                tmp.getString("name").equalsIgnoreCase("likes") ||
                                tmp.getString("name").equalsIgnoreCase("photos")){
                            continue;
                        }
                        
                        if(!tmp.isNull("id")){
                            id = tmp.getString("id");
                            id = id.replace('/', '_');
                            //appId = id.substring(id.lastIndexOf("_") + 1);
                            appId = tmp.getJSONObject("application").getString("id");
                        }
                        if(!tmp.isNull("name")){
                            name = tmp.getString("name");
                        }
                        if(!tmp.isNull("position")){
                            position = tmp.getLong("position") +"";
                        }
                        
                        ////System.out.println("\n\n\n\nid:" + id  + " name:" + name + " position:" + position);
                        if(id.isEmpty() || name.isEmpty() || position.isEmpty()){
                            log.error("Fan page tab not added to Social:" + tmp);
                            continue;
                        }
                        
                        FacePageTab tmpftp = FacePageTab.ClassMgr.getFacePageTab(id, wsite);
                        if(tmpftp == null){
                            //Search a facebook tab defined manually inside the fan page
                            Iterator<WebPage> tabs = fp.listChilds();
                            while(tabs.hasNext()){
                                FacePageTab tab = (FacePageTab)tabs.next();
                                if(tab.getFace_appid() != null){
                                    if(tab.getFace_appid().equals(appId)){
                                        tmpftp = tab;
                                        break;
                                    }
                                }
                            }
                            
                            //If no fan page tab found then create it!
                            if( tmpftp == null){
                                tmpftp = FacePageTab.ClassMgr.createFacePageTab(id, wsite);
                                tmpftp.setActive(true);
                            }
                        }                       
                        tmpftp.setTitle(name);
                        tmpftp.setFace_appid(appId);
                        tmpftp.setSortName(position);
                        //set the social account!!
                        tmpftp.setParent(fp);
                    }
                }
            }
            ////System.out.println("SE ENCUENTRA ACTIVO EL TAB: " + isActive);
        } catch (Exception ex) {
            log.error("Problem getting list of current tabs from page ", ex );
        }
        return false;
    }
    
    
    /**
     * Realiza peticiones al grafo de Facebook que deban ser enviadas por
     * alg&uacute;n m&eacute;todo en particular
     *
     * @param params contiene los par&aacute;metros a enviar a Facebook para
     * realizar la operaci&oacute;n deseada
     * @param url especifica el objeto del grafo de Facebook con el que se desea
     * interactuar
     * @param userAgent indica el navegador de Internet utilizado en la
     * petici&oacute;n a realizar
     * @param method indica el m&eacute;todo de la petici&oacute; HTTP requerido
     * por Facebook para realizar una operaci&oacute;n, como:
     * {@literal POST}, {@literal DELETE} o {@literal GET}
     * @return un {@code String} que representa la respuesta generada por el
     * grafo de Facebook
     * @throws IOException en caso de que se produzca un error al generar la
     * petici&oacute;n o recibir la respuesta del grafo de Facebook
     */
    public static String postRequest(Map<String, String> params, String url,
            String userAgent, String method) throws IOException {

        CharSequence paramString = (null == params) ? "" : delimit(params.entrySet(), "&", "=", true);
        ////System.out.println("2URL : " + url + "?" + paramString);
        URL serverUrl = new URL(url + "?" + paramString);

        HttpURLConnection conex = null;
        OutputStream out = null;
        InputStream in = null;
        String response = null;

        if (method == null) {
            method = "POST";
        }
        try {
            conex = (HttpURLConnection) serverUrl.openConnection();
            if (userAgent != null) {
                conex.setRequestProperty("user-agent", userAgent);
            }
            conex.setConnectTimeout(30000);
            conex.setReadTimeout(60000);
            conex.setRequestMethod(method);
            conex.setDoOutput(true);
            conex.connect();

            ////System.out.println("CONNECT:" + conex);
            //   out = conex.getOutputStream();
            //   out.write(paramString.toString().getBytes("UTF-8"));
            in = conex.getInputStream();
            response = getResponse(in);

        } catch (java.io.IOException ioe) {
            if (conex.getResponseCode() >= 400) {
                ////System.out.println("ERROR:" +   getResponse(conex.getErrorStream()));
                response = getResponse(conex.getErrorStream());
                log.error("\n\nERROR:" + response);
            }
            //ioe.printStackTrace();
        } finally {
            close(in);
            //close(out);
            if (conex != null) {
                conex.disconnect();
            }
        }
        if (response == null) {
            response = "";
        }
        return response;
    }

    public static String getRequest(Map<String, String> params, String url,
            String userAgent) throws IOException {

        CharSequence paramString = (null == params) ? "" : delimit(params.entrySet(), "&", "=", true);
        URL serverUrl = new URL(url + "?" + paramString);
        HttpURLConnection conex = null;
        InputStream in = null;
        String response = null;

        try {
            conex = (HttpURLConnection) serverUrl.openConnection();
            if (userAgent != null) {
                conex.setRequestProperty("user-agent", userAgent);
            }
            conex.setConnectTimeout(30000);
            conex.setReadTimeout(60000);
            conex.setRequestMethod("GET");
            conex.setDoOutput(true);
            conex.connect();
            in = conex.getInputStream();
            response = getResponse(in);
            ////System.out.println("RESPONSE:" + response);

        } catch (java.io.IOException ioe) {
            if (conex.getResponseCode() >= 400) {
                response = getResponse(conex.getErrorStream());
                log.error("\n\n\nERROR:" + response);
            }
            ioe.printStackTrace();
        } finally {
            close(in);
            if (conex != null) {
                conex.disconnect();
            }
        }
        if (response == null) {
            response = "";
        }
        return response;
    }

    public static CharSequence delimit(Collection<Map.Entry<String, String>> entries,
            String delimiter, String equals, boolean doEncode)
            throws UnsupportedEncodingException {

        if (entries == null || entries.isEmpty()) {
            return null;
        }
        StringBuilder buffer = new StringBuilder(64);
        boolean notFirst = false;
        for (Map.Entry<String, String> entry : entries) {
            if (notFirst) {
                buffer.append(delimiter);
            } else {
                notFirst = true;
            }
            CharSequence value = entry.getValue();
            buffer.append(entry.getKey());
            buffer.append(equals);
            buffer.append(doEncode ? encode(value) : value);
        }
        return buffer;
    }

    /**
     * Codifica el valor de {@code target} de acuerdo al c&oacute;digo de
     * caracteres UTF-8
     *
     * @param target representa el texto a codificar
     * @return un {@code String} que representa el valor de {@code target} de
     * acuerdo al c&oacute;digo de caracteres UTF-8
     * @throws UnsupportedEncodingException en caso de ocurrir algun problema en
     * la codificaci&oacute;n a UTF-8
     */
    private static String encode(CharSequence target) throws UnsupportedEncodingException {

        String result = "";
        if (target != null) {
            result = target.toString();
            result = URLEncoder.encode(result, "UTF8");
        }
        return result;
    }

    public static String getResponse(InputStream data) throws IOException {

        Reader in = new BufferedReader(new InputStreamReader(data, "UTF-8"));
        StringBuilder response = new StringBuilder(256);
        char[] buffer = new char[1000];
        int charsRead = 0;
        while (charsRead >= 0) {
            response.append(buffer, 0, charsRead);
            charsRead = in.read(buffer);
        }
        in.close();
        return response.toString();
    }

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ex) {
                log.error("Error at closing object: " + c.getClass().getName(),
                        ex);
            }
        }
    }
    
    public static String postRequestParams(Map<String, String> params, String url,
            String userAgent, String method) throws IOException {
        CharSequence paramString = (null == params) ? "" : delimit(params.entrySet(), "&", "=", true);
        URL serverUrl = new URL(url + "?" + paramString);
        HttpURLConnection conex = null;
        OutputStream out = null;
        InputStream in = null;
        String response = null;

        if (method == null) {
            method = "POST";
        }
        try {
            conex = (HttpURLConnection) serverUrl.openConnection();
            if (userAgent != null) {
                conex.setRequestProperty("user-agent", userAgent);
            }
            conex.setConnectTimeout(30000);
            conex.setReadTimeout(60000);
            conex.setRequestMethod(method);
            conex.setDoOutput(true);
            conex.connect();


            in = conex.getInputStream();
            response = getResponse(in);

        } catch (java.io.IOException ioe) {
            if (conex.getResponseCode() >= 400) {
                response = getResponse(conex.getErrorStream());
                log.error("\n\nERROR for url:" + serverUrl + " -- " + response);
            }
        } finally {
            close(in);
            if (conex != null) {
                conex.disconnect();
            }
        }
        if (response == null) {
            response = "";
        }        
        return response;
    }
}
