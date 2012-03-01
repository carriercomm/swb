package org.semanticwb.portal.resources;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.semanticwb.Logger;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBPortal;
import org.semanticwb.SWBUtils;
import org.semanticwb.SWBUtils.TEXT;
import org.semanticwb.base.util.SFBase64;
import org.semanticwb.model.Resource;
import org.semanticwb.model.User;
import org.semanticwb.model.UserRepository;
import org.semanticwb.model.WebPage;
import org.semanticwb.model.WebSite;
import org.semanticwb.portal.SWBUserMgr;
import org.semanticwb.portal.api.GenericAdmResource;
import org.semanticwb.portal.api.SWBActionResponse;
import org.semanticwb.portal.api.SWBParamRequest;
import org.semanticwb.portal.api.SWBResourceException;
import org.semanticwb.portal.api.SWBResourceURL;

public class Login extends GenericAdmResource
{
  static Logger log = SWBUtils.getLogger(Login.class);
  private static final String FRM_LOGIN = "frmlogin";
  private static final String FRM_LOGOUT = "frmlogout";
  private static final String INPLACE = "inPlace";
  private static final String TXT_login = "<fieldset><form action=\"{?loginurl}\" method=\"post\">\n<label>Usuario:</label><input type=\"text\" id=\"wb_username\" name=\"wb_username\" /><br />\n<label>Contrase&ntilde;a:</label><input type=\"password\" id=\"wb_password\" name=\"wb_password\" /><br />\n<input type=\"submit\" value=\"Enviar\" /></form></fieldset>\n";
  private static final String TXT_logout = "<a href=\"{?logouturl}?wb_logout=true\" >Logout</a>";

  public void doView(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramsRequest)
    throws SWBResourceException, IOException
  {
    System.out.println("Entra");
    PrintWriter out = response.getWriter();
    String frmlogin = getResourceBase().getAttribute("frmlogin");
    String frmlogout = getResourceBase().getAttribute("frmlogout");
    boolean inPlace = Boolean.parseBoolean(getResourceBase().getAttribute("inPlace", "false"));
    if (("ok".equals(request.getParameter("ErrorMgs"))) && (null != request.getSession(true).getAttribute("ErrorMsg")))
    {
      out.println(new StringBuilder().append("\n<script> alert('").append(request.getSession(true).getAttribute("ErrorMsg")).append("');</script>\n").toString());
      request.getSession(true).removeAttribute("ErrorMsg");
    }
    if (null == frmlogin)
    {
      frmlogin = "<fieldset><form action=\"{?loginurl}\" method=\"post\">\n<label>Usuario:</label><input type=\"text\" id=\"wb_username\" name=\"wb_username\" /><br />\n<label>Contrase&ntilde;a:</label><input type=\"password\" id=\"wb_password\" name=\"wb_password\" /><br />\n<input type=\"submit\" value=\"Enviar\" /></form></fieldset>\n";
    }
    if (null == frmlogout)
    {
      frmlogout = "<a href=\"{?logouturl}?wb_logout=true\" >Logout</a>";
    }

    String url = null;
    System.out.println(new StringBuilder().append("checa ").append(!paramsRequest.getUser().isSigned()).toString());
    if (!paramsRequest.getUser().isSigned())
    {
      if (inPlace)
      {
        SWBResourceURL aurl = paramsRequest.getActionUrl();
        aurl.setCallMethod(3);
        url = aurl.toString();
      }
      else {
        url = new StringBuilder().append(SWBPlatform.getContextPath()).append("/login/").append(paramsRequest.getWebPage().getWebSiteId()).append("/").append(paramsRequest.getWebPage().getId()).toString();
      }
      out.println(replaceTags(frmlogin, request, paramsRequest, url));
    }
    else
    {
      url = new StringBuilder().append(SWBPlatform.getContextPath()).append("/login/").append(paramsRequest.getWebPage().getWebSiteId()).append("/").append(paramsRequest.getWebPage().getId()).toString();
      out.println(replaceTags(frmlogout, request, paramsRequest, new StringBuilder().append(url).append("?wb_logout=true").toString()));
    }

    System.out.println("Sale");
  }

  public void doAdmin(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramsRequest)
    throws SWBResourceException, IOException
  {
    PrintWriter out = response.getWriter();
    String frmlogin = getResourceBase().getAttribute("frmlogin");
    String frmlogout = getResourceBase().getAttribute("frmlogout");
    boolean inPlace = Boolean.parseBoolean(getResourceBase().getAttribute("inPlace", "false"));
    if (null == frmlogin)
    {
      frmlogin = "<fieldset><form action=\"{?loginurl}\" method=\"post\">\n<label>Usuario:</label><input type=\"text\" id=\"wb_username\" name=\"wb_username\" /><br />\n<label>Contrase&ntilde;a:</label><input type=\"password\" id=\"wb_password\" name=\"wb_password\" /><br />\n<input type=\"submit\" value=\"Enviar\" /></form></fieldset>\n";
    }
    if (null == frmlogout)
    {
      frmlogout = "<a href=\"{?logouturl}?wb_logout=true\" >Logout</a>";
    }
    String act = request.getParameter("act");
    if (act != null)
    {
      frmlogin = request.getParameter("frmlogin");
      getResourceBase().setAttribute("frmlogin", frmlogin);
      frmlogout = request.getParameter("frmlogout");
      getResourceBase().setAttribute("frmlogout", frmlogout);
      getResourceBase().setAttribute("inPlace", request.getParameter("inPlace"));
      inPlace = Boolean.parseBoolean(getResourceBase().getAttribute("inPlace", "false"));
      try
      {
        getResourceBase().updateAttributesToDB();
      }
      catch (Exception e) {
        log.error(e);
      }
    }

    out.println("<script type=\"text/javascript\">");
    out.println("  dojo.require(\"dijit.form.Form\");");
    out.println("  dojo.require(\"dijit.form.Button\");");
    out.println("  dojo.require(\"dijit.form.CheckBox\");");
    out.println("</script>");

    out.println("<div class=\"swbform\">");
    out.println(new StringBuilder().append("<form dojoType=\"dijit.form.Form\" id=\"").append(getResourceBase().getId()).append("/sparql\" action=\"").append(paramsRequest.getRenderUrl()).append("\" method=\"post\" >").toString());
    out.println("<input type=\"hidden\" name=\"act\" value=\"upd\">");

    out.println("<fieldset>");
    out.println("<legend>Configuraci&oacute;n Despliegue</legend>");
    out.println("<br/>");
    out.println("Forma de autenticaci&oacute;n:");
    out.println("<br/>");
    out.print("<textarea name=\"frmlogin\" rows=10 cols=80>");
    out.print(frmlogin);
    out.println("</textarea>");
    out.println("<br/>");
    out.println("Pie:");
    out.println("<br/>");
    out.print("<textarea name=\"frmlogout\" rows=10 cols=80>");
    out.print(frmlogout);
    out.println("</textarea>");
    out.println("<br/>Autenticar en sitio:");
    out.println("<br/>");
    String chk = inPlace ? "checked=\"checked\"" : "";
    out.println(new StringBuilder().append("<input id=\"inPlace\" dojotype=\"dijit.form.CheckBox\" name=\"inPlace\" ").append(chk).append(" value=\"true\" type=\"checkbox\" />").toString());

    out.println("<br/>");
    out.println("<font style=\"color: #428AD4; font-family: Verdana; font-size: 10px;\">");
    out.println("\t\t<b>Tags:</b><br/>");
    out.println("       &nbsp;&nbsp;{?XXXXXX} Para las formas.<BR>");
    out.println("       &nbsp;&nbsp;{?loginurl}<BR>");
    out.println("       &nbsp;&nbsp;{?logouturl}<BR>");
    out.println("       &nbsp;&nbsp;{user.login}<BR>");
    out.println("       &nbsp;&nbsp;{user.email}<BR>");
    out.println("       &nbsp;&nbsp;{user.language}<BR>");
    out.println("       &nbsp;&nbsp;{getEnv(\"XXXXX\")}<BR>");
    out.println("       &nbsp;&nbsp;{request.getParameter(\"XXXXX\")}<BR>");
    out.println("       &nbsp;&nbsp;{session.getAttribute(\"XXXXX\")}<BR>");
    out.println("       &nbsp;&nbsp;{encodeB64(\"XXXXX\")}<BR>");

    out.println("       &nbsp;&nbsp;{webpath}<BR>");
    out.println("       &nbsp;&nbsp;{distpath}<BR>");
    out.println("       &nbsp;&nbsp;{webworkpath}<BR>");
    out.println("       &nbsp;&nbsp;{websiteid}<BR>");
    out.println("       &nbsp;&nbsp;{workpath}<BR>");

    out.println("       <BR>&nbsp;&nbsp;<b>Note:</b> XXXXX=Text<BR><BR>");
    out.println("\t</font>");
    out.println("</fieldset>");
    out.println("<fieldset>");
    out.println("<button dojoType=\"dijit.form.Button\" type=\"submit\" name=\"submit/btnSend\" >Enviar</button>");
    out.println("</fieldset>");
    out.println("</form>");
    out.println("</div>");
  }

  private String replaceTags(String str, HttpServletRequest request, SWBParamRequest paramRequest, String url)
  {
    if ((str == null) || (str.trim().length() == 0))
    {
      return null;
    }
    str = str.trim();

    Iterator it = SWBUtils.TEXT.findInterStr(str, "{encodeB64(\"", "\")}");
    while (it.hasNext())
    {
      String s = (String)it.next();
      str = SWBUtils.TEXT.replaceAll(str, new StringBuilder().append("{encodeB64(\"").append(s).append("\")}").toString(), SFBase64.encodeString(replaceTags(s, request, paramRequest, url)));
    }

    it = SWBUtils.TEXT.findInterStr(str, "{request.getParameter(\"", "\")}");
    while (it.hasNext())
    {
      String s = (String)it.next();
      str = SWBUtils.TEXT.replaceAll(str, new StringBuilder().append("{request.getParameter(\"").append(s).append("\")}").toString(), request.getParameter(replaceTags(s, request, paramRequest, url)));
    }

    it = SWBUtils.TEXT.findInterStr(str, "{session.getAttribute(\"", "\")}");
    while (it.hasNext())
    {
      String s = (String)it.next();
      str = SWBUtils.TEXT.replaceAll(str, new StringBuilder().append("{session.getAttribute(\"").append(s).append("\")}").toString(), (String)request.getSession().getAttribute(replaceTags(s, request, paramRequest, url)));
    }

    it = SWBUtils.TEXT.findInterStr(str, "{getEnv(\"", "\")}");
    while (it.hasNext())
    {
      String s = (String)it.next();
      str = SWBUtils.TEXT.replaceAll(str, new StringBuilder().append("{getEnv(\"").append(s).append("\")}").toString(), SWBPlatform.getEnv(replaceTags(s, request, paramRequest, url)));
    }

    it = SWBUtils.TEXT.findInterStr(str, "{?", "}");
    while (it.hasNext())
    {
      String s = (String)it.next();
      str = SWBUtils.TEXT.replaceAll(str, new StringBuilder().append("{?").append(s).append("}").toString(), url);
    }

    str = SWBUtils.TEXT.replaceAll(str, "{rows.number}", request.getAttribute("rowsnum") != null ? (String)request.getAttribute("rowsnum") : "N/A");
    str = SWBUtils.TEXT.replaceAll(str, "{exec.time}", (String)request.getAttribute("extime"));
    str = SWBUtils.TEXT.replaceAll(str, "{user.login}", paramRequest.getUser().getLogin());
    str = SWBUtils.TEXT.replaceAll(str, "{user.email}", paramRequest.getUser().getEmail());
    str = SWBUtils.TEXT.replaceAll(str, "{user.language}", paramRequest.getUser().getLanguage());
    str = SWBUtils.TEXT.replaceAll(str, "{webpath}", SWBPortal.getContextPath());
    str = SWBUtils.TEXT.replaceAll(str, "{distpath}", SWBPortal.getDistributorPath());
    str = SWBUtils.TEXT.replaceAll(str, "{webworkpath}", SWBPortal.getWebWorkPath());
    str = SWBUtils.TEXT.replaceAll(str, "{workpath}", SWBPortal.getWorkPath());
    str = SWBUtils.TEXT.replaceAll(str, "{websiteid}", paramRequest.getWebPage().getWebSiteId());

    return str;
  }

  public void processAction(HttpServletRequest request, SWBActionResponse response)
    throws SWBResourceException, IOException
  {
    UserRepository ur = response.getWebPage().getWebSite().getUserRepository();
    String authMethod = ur.getAuthMethod();
    String context = ur.getLoginContext();
    String CBHClassName = ur.getCallBackHandlerClassName();
    Subject subject = SWBPortal.getUserMgr().getSubject(request, response.getWebPage().getWebSiteId());
    try
    {
      CallbackHandler cbh = org.semanticwb.servlet.internal.Login.getHandler(CBHClassName, request, null, authMethod, response.getWebPage().getWebSiteId());

      String matchKey = new StringBuilder().append(response.getWebPage().getWebSiteId()).append("|").append(request.getParameter("wb_username")).toString();
      org.semanticwb.servlet.internal.Login.doLogin(cbh, context, subject, request, matchKey, response.getWebPage().getWebSite().getUserRepository().getId());
    }
    catch (Exception e)
    {
      org.semanticwb.servlet.internal.Login.markFailedAttepmt(request.getParameter("wb_username"));
      String alert = "No se pudo autenticar, datos incorrectos.";
      if (org.semanticwb.servlet.internal.Login.isblocked(request.getParameter("wb_username"))) {
        alert = "Usuario temporalmente inhabilitado";
      }
      request.getSession(true).setAttribute("ErrorMsg", alert);
      response.setRenderParameter("ErrorMgs", "ok");
      response.setCallMethod(1);
      return;
    }
    response.sendRedirect(response.getWebPage().getRealUrl());
  }
}