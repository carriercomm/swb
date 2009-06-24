/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semanticwb.portal.admin.resources;

import java.io.*;
import java.util.Date;
import java.util.Enumeration;
//import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.semanticwb.*;
import org.semanticwb.model.*;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticOntology;
import org.semanticwb.platform.SemanticProperty;
import org.semanticwb.portal.api.*;
import org.w3c.dom.*;

/**
 *
 * @author juan.fernandez
 */
public class SWBASchedule extends GenericResource {

    /** Nombre del recurso */
    private Logger log = SWBUtils.getLogger(SWBASchedule.class);
    public String strRscType;

    public void init() {
        try {
            strRscType = getResourceBase().getResourceType().getTitle();
        } catch (Exception e) {
            strRscType = "SWBASchedule";
        }
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
    public void doView(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        response.setContentType("text/html; charset=ISO-8859-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        log.debug("----doView() ");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("suri"); //uri del elemento
        String idp = request.getParameter("sprop");
        String idpref = request.getParameter("spropref");
        //User user = paramRequest.getUser();
        //String idCalendar = request.getParameter("sval");
        String idCalendar = id;
        // Parámetros
        String view = request.getParameter("view");
        String action = request.getParameter("act") != null ? request.getParameter("act") : "";
        String topic = paramRequest.getTopic().getId();
        //String iId = "0";
        Document doc = null;
        String strXmlConf = null;
        String tm = request.getParameter("tm");
        String tp = request.getParameter("tp");
        String strId = request.getParameter("id");
        if (strId == null) {
            strId = id;
        }

        SemanticOntology ont = SWBPlatform.getSemanticMgr().getOntology();
        SemanticObject obj = ont.getSemanticObject(id);
        SemanticObject so_cal = null;
        if (null != idCalendar) {
            so_cal = ont.getSemanticObject(idCalendar);
        }
        SemanticClass cls = obj.getSemanticClass();
        String title = cls.getName();

        SWBResourceURL url = paramRequest.getActionUrl();
        url.setAction("update");

        log.debug("Action: " + action);
        out.println("<div class=\"swbform\">");
        out.println("<form  action=\"" + url + "\"  id=\"" + id + "/calendar\" name=\"" + id + "/calendar\" method=\"post\" onsubmit=\"submitForm('" + id + "/calendar'); return false;\">"); //id=\"calendar\" name=\"calendar\" dojoType=\"dijit.form.Form\"
        out.println("<fieldset>");

        // Recurso de calendarización

        String createdate = "";
        String usercreate = "";
        String active = "";
        String cal = "";
        String sendselect = "noend";
        String inidate = "";
        String enddate = "";
        String speriod = "";
        String starthour = "";
        String endhour = "";
        String wday1 = "";
        String wday2 = "";
        String wday3 = "";
        String wday4 = "";
        String wday5 = "";
        String wday6 = "";
        String wday7 = "";
        String mday1 = "";
        String mday2 = "";
        String mday3 = "";
        String mday4 = "";
        String mday5 = "";
        String mday6 = "";
        String mday7 = "";
        String yday1 = "";
        String yday2 = "";
        String yday3 = "";
        String yday4 = "";
        String yday5 = "";
        String yday6 = "";
        String yday7 = "";
        String ssmonth = "";
        String mweek = "";
        String mmonths2 = "";
        String mmday = "";
        String mmonths1 = "";
        String ssyear = "";
        String yyday = "";
        String ymonth1 = "";
        String yyears1 = "";
        String ymonth2 = "";
        String yyears2 = "";
        String yweek = "";
        String day = "";
        String wdays = "";
        String week = "";
        String months = "";
        String years = "";

        String hourc = "";
        String hourcd = "disabled";

        String yearlyd = "disabled";
        String pchk = "";
        String periodd = "disabled";
        String period1c = "", period2c = "", period3c = "";
        String period1cd = "disabled", period2cd = "disabled", period3cd = "disabled";
        String smonth1c = "", smonth2c = "";
        String smonth1cd = "disabled", smonth2cd = "disabled";
        String radio1c = "", radio2c = "";
        String radio1cd = "disabled", radio2cd = "disabled";
        String op1 = "", op2 = "", op3 = "", op4 = "", op5 = "", op6 = "", op7 = "", op8 = "", op9 = "", op10 = "", op11 = "", op12 = "";
        // Genera forma de administración de calendarizaciones
        {
            if (so_cal != null) {
                strXmlConf = so_cal.getProperty(XMLable.swb_xml);
            }
            if (strXmlConf != null && strXmlConf.trim().length() > 0) {
                doc = SWBUtils.XML.xmlToDom(strXmlConf);
                active = SWBUtils.XML.getAttribute(doc, "active");
                createdate = SWBUtils.XML.getAttribute(doc, "createdate");
                usercreate = SWBUtils.XML.getAttribute(doc, "usercreate");
                inidate = cambiaFormato(SWBUtils.XML.getAttribute(doc, "inidate"), 2);
                enddate = SWBUtils.XML.getAttribute(doc, "enddate");
                if (null != enddate) {
                    enddate = cambiaFormato(SWBUtils.XML.getAttribute(doc, "enddate"), 2);
                    sendselect = "endselect";
                }
                starthour = SWBUtils.XML.getAttribute(doc, "starthour");
                endhour = SWBUtils.XML.getAttribute(doc, "endhour");

                log.debug("start hour:"+starthour);

                if(starthour!=null&&starthour.trim().length()>0)
                {
                    hourc = "checked";
                    hourcd = "";
                }



                NodeList nodes = doc.getElementsByTagName("interval");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    NodeList nodesChild = node.getChildNodes();
                    for (int j = 0; j < nodesChild.getLength(); j++) {
                        Node nodeChild = nodesChild.item(j);
                        String name = nodeChild.getNodeName();
                        cal = "on";
                        if (name != null && name.equals("iterations")) {
                            if (nodeChild.getFirstChild() != null) {
                                String strChildName = nodeChild.getFirstChild().getNodeName();
                                NodeList nodesGrandChild = nodeChild.getFirstChild().getChildNodes();
                                speriod = strChildName;
                                for (int k = 0; k < nodesGrandChild.getLength(); k++) {
                                    Node nodeGrandChild = nodesGrandChild.item(k);
                                    String valChild = nodeGrandChild.getFirstChild().getNodeValue();
                                    strChildName = nodeGrandChild.getNodeName();
                                    if (speriod.equals("weekly")) {
                                        pchk = "checked";
                                        period1c = "checked";
                                        if (strChildName.equals("wdays")) {
                                            wdays = valChild;
                                        }
                                    } else if (speriod.equals("monthly")) {
                                        pchk = "checked";
                                        period2c = "checked";
                                        if (strChildName.equals("months")) {
                                            months = valChild;
                                        } else if (strChildName.equals("wdays")) {
                                            wdays = valChild;
                                        } else if (strChildName.equals("week")) {
                                            week = valChild;
                                        } else if (strChildName.equals("day")) {
                                            day = valChild;
                                        }
                                    } else if (speriod.equals("yearly")) {
                                        pchk = "checked";
                                        period3c = "checked";
                                        yearlyd = "";
                                        if (strChildName.equals("day")) {
                                            day = valChild;
                                        } else if (strChildName.equals("month")) {
                                            months = valChild;
                                        } else if (strChildName.equals("years")) {
                                            years = valChild;
                                        } else if (strChildName.equals("week")) {
                                            week = valChild;
                                        } else if (strChildName.equals("wdays")) {
                                            wdays = valChild;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (pchk.equals("checked")) {
                    periodd = "";
                }

                // Obtiene los días seleccionados
                if (wdays != null && !wdays.equals("")) {
                    String[] days = {"", "", "", "", "", "", ""};
                    int dias = 0;
                    if (wdays != null) {
                        dias = Integer.parseInt(wdays);
                    }
                    int res = 0;
                    int cdias = 0;
                    while (dias > 0) {
                        res = dias % 2;
                        days[cdias] = Integer.toString(res);
                        dias = dias / 2;
                        cdias++;
                    }
                    for (int i = 0; i < days.length; i++) {
                        if (days[i] != null) {
                            // Domingo
                            if (i == 0 && days[i].equals("1")) {
                                if (speriod.equals("weekly")) {
                                    wday1 = "checked";
                                } else if (speriod.equals("monthly")) {
                                    mday1 = "checked";
                                } else if (speriod.equals("yearly")) {
                                    yday1 = "checked";
                                }
                            }
                            // Lunes
                            if (i == 1 && days[i].equals("1")) {
                                if (speriod.equals("weekly")) {
                                    wday2 = "checked";
                                } else if (speriod.equals("monthly")) {
                                    mday2 = "checked";
                                } else if (speriod.equals("yearly")) {
                                    yday2 = "checked";
                                }
                            }
                            // Martes
                            if (i == 2 && days[i].equals("1")) {
                                if (speriod.equals("weekly")) {
                                    wday3 = "checked";
                                } else if (speriod.equals("monthly")) {
                                    mday3 = "checked";
                                } else if (speriod.equals("yearly")) {
                                    yday3 = "checked";
                                }
                            }
                            // Miércoles
                            if (i == 3 && days[i].equals("1")) {
                                if (speriod.equals("weekly")) {
                                    wday4 = "checked";
                                } else if (speriod.equals("monthly")) {
                                    mday4 = "checked";
                                } else if (speriod.equals("yearly")) {
                                    yday4 = "checked";
                                }
                            }
                            // Jueves
                            if (i == 4 && days[i].equals("1")) {
                                if (speriod.equals("weekly")) {
                                    wday5 = "checked";
                                } else if (speriod.equals("monthly")) {
                                    mday5 = "checked";
                                } else if (speriod.equals("yearly")) {
                                    yday5 = "checked";
                                }
                            }
                            // Viernes
                            if (i == 5 && days[i].equals("1")) {
                                if (speriod.equals("weekly")) {
                                    wday6 = "checked";
                                } else if (speriod.equals("monthly")) {
                                    mday6 = "checked";
                                } else if (speriod.equals("yearly")) {
                                    yday6 = "checked";
                                }
                            }
                            // Sábado
                            if (i == 6 && days[i].equals("1")) {
                                if (speriod.equals("weekly")) {
                                    wday7 = "checked";
                                } else if (speriod.equals("monthly")) {
                                    mday7 = "checked";
                                } else if (speriod.equals("yearly")) {
                                    yday7 = "checked";
                                }
                            }
                        }
                    }
                }

                // Depende del tipo de periodo
                if (speriod.equals("monthly")) {
                    if (day != null && !day.equals("")) {
                        ssmonth = "day";
                        mmday = day;
                        mmonths1 = months;
                        smonth1c = "checked";
                        smonth1cd = "";
                    } else {
                        ssmonth = "week";
                        mweek = week;
                        mmonths2 = months;
                        smonth2c = "checked";
                        smonth2cd = "";
                    }
                }
                if (speriod.equals("yearly")) {
                    if (day != null && !day.equals("")) {
                        ssyear = "day";
                        yyday = day;
                        ymonth1 = months;
                        yyears1 = years;
                        radio1c = "checked";
                        radio1cd = "";
                    } else {
                        ssyear = "week";
                        yweek = week;
                        ymonth2 = months;
                        yyears2 = years;
                        radio2c = "checked";
                        radio2cd = "";
                    }
                }
            }


        }

        out.println("<fieldset>");
        out.println(" <legend> Definici&oacute;n Periodicidad </legend>");
        out.println("  <table cellSpacing=0 cellPadding=1 width=\"98%\" border=0>");
        out.println("  <tbody>");
        out.println("  <tr>");
        out.println("    <td>");
        out.println("      <table cellSpacing=0 cellPadding=1 width=\"100%\" border=0>");
        out.println("        <tbody>");
        out.println("        <tr>");
        out.println("          <td width=100 >" + paramRequest.getLocaleString("frmStartDate") + ":</td>");
        out.println("          <td colSpan=4 ><input type=\"text\" name=\"" + id + "/inidate\" id=\"" + id + "/inidate\" dojoType=\"dijit.form.DateTextBox\"  size=\"11\" style=\"width:110px;\" hasDownArrow=\"true\" value=\"" + inidate + "\"></td>");
        out.println("        </tr>");
        out.println("        </tbody>");
        out.println("      </table>");
        out.println("    </td>");
        out.println("  </tr>");
        out.println("  <tr><td><hr size=\"1\" noshade></td></tr>");
        String endselect1 = "checked", endselect2 = "", endselectd = "false";

        if (enddate != null && enddate.trim().length() > 0) {
            endselect1 = "checked";
            endselect2 = "";
            endselectd = "";
        } else {
            endselect1 = "";
            endselect2 = "checked";
            endselectd = "disabled";
        }
        
        log.debug("Cal-end-date-active=" + endselectd);

        out.println("  <tr>");
        out.println("    <td>");
        out.println("      <table cellSpacing=0 cellPadding=1 width=\"100%\" border=0>");
        out.println("        <tbody>");
        out.println("        <tr>");
        out.println("          <td width=\"20\" ><input type=\"radio\" dojoType=\"dijit.form.RadioButton\" value=\"enddate\" id=\"" + id + "/endselect1\" name=\"" + id + "/endselect\" onclick=\"dijit.byId('" + id + "/enddate').setDisabled(false); dijit.byId('" + id + "/enddate').focus();\" " + endselect1 + "></td>");
        out.println("          <td width=\"77\" >" + paramRequest.getLocaleString("frmEndDate") + ":</td>");
        out.println("          <td ><input type=\"text\" name=\"" + id + "/enddate\" id=\"" + id + "/enddate\" dojoType=\"dijit.form.DateTextBox\" size=\"11\" style=\"width:110px;\" hasDownArrow=\"true\" value=\"" + enddate + "\" required=\"true\" " + endselectd + " ></td>"); //disabled=\"" + endselectd + "\"
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td ><input type=\"radio\" dojoType=\"dijit.form.RadioButton\" value=\"noend\" id=\"" + id + "/endselect2\" name=\"" + id + "/endselect\" onclick=\"dijit.byId('" + id + "/enddate').setDisabled(true);\" " + endselect2 + "></td>");
        out.println("          <td colSpan=2 >" + paramRequest.getLocaleString("frmNoEndDate") + "</td>");
        out.println("        </tr>");
        out.println("        </tbody>");
        out.println("      </table>");
        out.println("    </td>");
        out.println("  </tr>");
        out.println("  <tr><td><hr size=\"1\" noshade></td></tr>");
        out.println("  <tr>");
        out.println("    <td>");
        out.println("      <table cellSpacing=\"0\" cellPadding=\"1\" width=\"100%\" border=0>");
        out.println("        <tbody>");
        out.println("        <tr>");
        out.println("          <td width=20 ><input type=\"checkbox\" id=\"" + id + "/time\" name=\"" + id + "/time\" dojoType=\"dijit.form.CheckBox\" onClick=\"enableTime('" + id + "');\" " + hourc + "></td>");
        out.println("          <td >" + paramRequest.getLocaleString("frmStartHour") + ":&nbsp;<input name=\"" + id + "/starthour\" id=\"" + id + "/starthour\" dojoType=\"dojox.form.TimeSpinner\" value=\"" + (starthour!=null&&starthour.trim().length() > 0 ? starthour : "00:00") + "\" hours=\"24\" smalldelta=\"10\" maxLength=\"10\" size=\"10\" " + hourcd + " style=\"width:100px;\" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + paramRequest.getLocaleString("frmEndHour") + ":&nbsp;<input name=\"" + id + "/endhour\" id=\"" + id + "/endhour\" dojoType=\"dojox.form.TimeSpinner\" value=\"" + (endhour!=null&&endhour.trim().length() > 0 ? endhour : "00:00") + "\"  hours=\"24\" smalldelta=\"10\"  maxLength=\"10\" size=\"10\" " + hourcd + " style=\"width:100px;\" /></td>");
        out.println("         </tr>");
        out.println("        </tbody>");
        out.println("      </table>");
        out.println("    </td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td>");
        out.println("      <hr size=\"1\" noshade>");
        out.println("    </td>");
        out.println("  </tr>");

        //PERIODICIDAD
        if (pchk.equals("")) {
            //Valores por defecto
            periodd ="disabled";
            period1c = "checked";
            period1cd = "disabled";
            period2c = "";
            period2cd = "disabled";
            period3c = "";
            period3cd = "disabled";
            smonth1c = "checked";
            smonth2c = "";
            smonth1cd = "disabled";
            smonth2cd = "disabled";
            radio1c = "checked";
            radio2c = "";
            radio1cd = "disabled";
            radio2cd = "disabled";
        }

        out.println("  <tr>");
        out.println("    <td ><input id=\"" + id + "/periodicidad\" type=\"checkbox\"  dojoType=\"dijit.form.CheckBox\" name=\"" + id + "/periodicidad\" onClick=\"disablePeriodicity('" + id + "');\" " + pchk + ">&nbsp;" + paramRequest.getLocaleString("frmRegularity") + "</td>");
        out.println("  </tr>");
        // SEMANAL
        if (pchk.equals("checked") && period1c.equals("checked")) {
            period1cd = "";
        }
        out.println("  <tr>");
        out.println("    <td>");
        out.println("      <table cellSpacing=0 cellPadding=1 width=\"100%\" border=0>");
        out.println("        <tbody>");
        out.println("        <tr>");
        out.println("          <td width=10 rowSpan=3>&nbsp;</td>");
        out.println("          <td width=100 rowSpan=2 ><input id=\"" + id + "/period1\" type=\"radio\" dojoType=\"dijit.form.RadioButton\" value=\"weekly\" name=\"" + id + "/period\" onClick=\"disablePeriodicity('" + id + "');\" " + period1c + " " + periodd + ">" + paramRequest.getLocaleString("frmWeekly") + "</td>");
        out.println("          <td ><input type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/wday1\" name=\"" + id + "/wday1\" " + wday1 + " " + period1cd + ">" + paramRequest.getLocaleString("frmSunday") + "</td>");
        out.println("          <td ><input type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/wday2\" name=\"" + id + "/wday2\" " + wday2 + " " + period1cd + ">" + paramRequest.getLocaleString("frmMonday") + "</td>");
        out.println("          <td ><input type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/wday3\" name=\"" + id + "/wday3\" " + wday3 + " " + period1cd + ">" + paramRequest.getLocaleString("frmTuesday") + "</td>");
        out.println("          <td ><input type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/wday4\" name=\"" + id + "/wday4\" " + wday4 + " " + period1cd + ">" + paramRequest.getLocaleString("frmWednesday") + "</td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td><input type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/wday5\" name=\"" + id + "/wday5\" " + wday5 + " " + period1cd + ">" + paramRequest.getLocaleString("frmThursday") + "</td>");
        out.println("          <td><input type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/wday6\" name=\"" + id + "/wday6\" " + wday6 + " " + period1cd + ">" + paramRequest.getLocaleString("frmFriday") + "</td>");
        out.println("          <td><input type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/wday7\" name=\"" + id + "/wday7\" " + wday7 + " " + period1cd + ">" + paramRequest.getLocaleString("frmSaturday") + "</td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td colSpan=5>");
        out.println("            <hr size=\"1\" noshade>");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        </tbody>");
        out.println("      </table>");
        out.println("    </td>");
        out.println("  </tr>");

        // MENSUAL
        if (pchk.equals("checked") && period2c.equals("checked")) {
            period2cd = "";
            if (smonth1c.equals("checked")) {
                smonth1cd = "";
                smonth2c = "";
                smonth2cd = "disabled";
            } else if (smonth2c.equals("checked")) {
                smonth1c = "";
                smonth1cd = "disabled";
                smonth2cd = "";
            }

        }
        out.println("  <tr>");
        out.println("    <td>");
        out.println("      <table cellSpacing=\"0\" cellPadding=\"1\" width=\"100%\" border=\"0\">");
        out.println("        <tbody>");
        out.println("        <tr>");
        out.println("          <td width=\"10\" rowSpan=\"5\">&nbsp;</td>");
        out.println("          <td width=\"100\" rowSpan=\"4\" >");
        out.println("           <input id=\"" + id + "/period2\" type=\"radio\" dojoType=\"dijit.form.RadioButton\" value=\"monthly\"  name=\"" + id + "/period\" onClick=\"disablePeriodicity('" + id + "');\" " + period2c + " " + periodd + ">" + paramRequest.getLocaleString("frmMonthly"));
        out.println("            </td>");
        out.println("          <td ><input type=\"radio\" dojoType=\"dijit.form.RadioButton\" id=\"" + id + "/smonth1\" name=\"" + id + "/smonth\"  value=\"day\" onClick=\"enableMonthly('" + id + "');\" " + smonth1c + " " + period2cd + "></td>");
        out.println("          <td colSpan=\"3\" >" + paramRequest.getLocaleString("frmTheDay") + " <input  type=\"text\" dojoType=\"dijit.form.TextBox\" maxLength=\"2\" size=\"2\" id=\"" + id + "/mmday\" name=\"" + id + "/mmday\" value=\"" + mmday + "\"  style=\"width:30px;\" " + smonth1cd + ">&nbsp;" + paramRequest.getLocaleString("frmOfEvery") + " <input maxLength=\"2\"  size=\"2\" id=\"" + id + "/mmonths1\" type=\"text\" dojoType=\"dijit.form.TextBox\" name=\"" + id + "/mmonths1\" value=\"" + mmonths1 + "\"  style=\"width:30px;\" " + smonth1cd + ">&nbsp;" + paramRequest.getLocaleString("frmMonths") + "</td>");
        out.println("        </tr>");
        out.println("        <tr >");
        out.println("          <td colSpan=\"4\">");
        out.println("            <hr size=\"1\" noshade>");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td ><input type=\"radio\" dojoType=\"dijit.form.RadioButton\" id=\"" + id + "/smonth2\" name=\"" + id + "/smonth\"  value=\"week\" onClick=\"enableMonthly('" + id + "');\" " + smonth2c + " " + period2cd + "></td>");
        out.println("          <td >" + paramRequest.getLocaleString("frmThe") + " &nbsp; ");
        if ("add".equals(action)) {
            op1 = "selected";
        } else if (mweek != null && !mweek.equals("")) {
            op1 = mweek.equals("1") ? "selected" : "";
            op2 = mweek.equals("2") ? "selected" : "";
            op3 = mweek.equals("3") ? "selected" : "";
            op4 = mweek.equals("4") ? "selected" : "";
            op5 = mweek.equals("5") ? "selected" : "";
        }
        out.println("               <select  id=\"" + id + "/mweek\" name=\"" + id + "/mweek\"  dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"true\" hasDownArrow=\"true\" style=\"width:90px;\" " + smonth2cd + ">");
        out.println("                   <option value=\"1\" " + op1 + " >" + paramRequest.getLocaleString("frmFirst") + "</option>");
        out.println("                   <option value=\"2\" " + op2 + " >" + paramRequest.getLocaleString("frmSecond") + "</option>");
        out.println("                   <option value=\"3\" " + op3 + " >" + paramRequest.getLocaleString("frmThird") + "</option>");
        out.println("                   <option value=\"4\" " + op4 + " >" + paramRequest.getLocaleString("frmFourth") + "</option>");
        out.println("                   <option value=\"5\" " + op5 + " >" + paramRequest.getLocaleString("frmLast") + "</option>");
        out.println("               </select> </td>");
//            if (mweek != null && !mweek.equals("")) {
//                out.println("       <script language=\"JavaScript\" type=\"text/JavaScript\">");
//                out.println("           selectCombo(dijit.byId('"+id+"/mweek').domNode,'" + mweek + "');");
//                out.println("       </script>");
//            }
        out.println("          <td>");
        out.println("            <table cellSpacing=\"0\" cellPadding=\"1\" width=\"100%\" border=\"0\">");
        out.println("              <tbody>");
        out.println("              <tr>");
        out.println("                <td ><input type=\"checkbox\"  dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/mday1\" name=\"" + id + "/mday1\" " + mday1 + " " + smonth2cd + ">" + paramRequest.getLocaleString("frmSun") + "</td>");
        out.println("                <td ><input type=\"checkbox\"  dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/mday2\" name=\"" + id + "/mday2\" " + mday2 + " " + smonth2cd + ">" + paramRequest.getLocaleString("frmMon") + "</td>");
        out.println("                <td ><input type=\"checkbox\"  dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/mday3\" name=\"" + id + "/mday3\" " + mday3 + " " + smonth2cd + ">" + paramRequest.getLocaleString("frmTue") + "</td>");
        out.println("                <td ><input type=\"checkbox\"  dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/mday4\" name=\"" + id + "/mday4\" " + mday4 + " " + smonth2cd + ">" + paramRequest.getLocaleString("frmWed") + "</td>");
        out.println("              </tr>");
        out.println("              <tr>");
        out.println("                <td ><input type=\"checkbox\"  dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/mday5\" name=\"" + id + "/mday5\" " + mday5 + " " + smonth2cd + ">" + paramRequest.getLocaleString("frmThu") + "</td>");
        out.println("                <td ><input type=\"checkbox\"  dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/mday6\" name=\"" + id + "/mday6\" " + mday6 + " " + smonth2cd + ">" + paramRequest.getLocaleString("frmFri") + "</td>");
        out.println("                <td ><input type=\"checkbox\"  dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/mday7\" name=\"" + id + "/mday7\" " + mday7 + " " + smonth2cd + ">" + paramRequest.getLocaleString("frmSat") + "</td>");
        out.println("              </tr>");
        out.println("              </tbody>");
        out.println("            </table>");
        out.println("          </td>");
        out.println("          <td >&nbsp;" + paramRequest.getLocaleString("frmOfEvery") + " &nbsp;<input  dojoType=\"dijit.form.TextBox\" maxLength=\"2\" size=\"2\" id=\"" + id + "/mmonths2\" name=\"" + id + "/mmonths2\" style=\"width:30px;\" value=\"" + mmonths2 + "\" " + smonth2cd + ">&nbsp;" + paramRequest.getLocaleString("frmMonths") + "</td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td colSpan=\"5\">");
        out.println("            <HR size=\"1\" noshade>");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("      </tbody>");
        out.println("    </table>");
        out.println("   </td>");
        out.println("  </tr>");
        // ANUAL
            if(pchk.equals("checked")&&period3c.equals("checked"))
            {
                period3cd="";
                if(smonth1c.equals("checked")){
                    smonth1cd="";
                    smonth2c="";
                    smonth2cd="disabled";
                } else if(smonth2c.equals("checked")){
                    smonth1c="";
                    smonth1cd="disabled";
                    smonth2cd="";
                }
                    
            }
        out.println("  <tr>");
        out.println("    <td >");
        out.println("      <table cellSpacing=\"0\" cellPadding=\"1\" width=\"100%\" border=\"0\">");
        out.println("        <tbody>");
        out.println("        <tr>");
        out.println("          <td width=\"10\" rowSpan=\"4\">&nbsp;</td>");
        out.println("          <td width=\"100\" rowSpan=\"4\" >");
        out.println("          <input  id=\"" + id + "/period3\"  type=\"radio\" dojoType=\"dijit.form.RadioButton\" value=\"yearly\" name=\"" + id + "/period\" onClick=\"disablePeriodicity('" + id + "');\" " + period3c + " " + periodd + ">" + paramRequest.getLocaleString("frmYearly") + "</td>");


        out.println("          <td ><input id=\"" + id + "/radio1\" type=\"radio\"  dojoType=\"dijit.form.RadioButton\" name=\"" + id + "/syear\" value=\"day\" onClick=\"enableYearly('" + id + "');\" " + radio1c + " " + yearlyd + "></td>");
        out.println("          <td colSpan=3 >" + paramRequest.getLocaleString("frmTheDay"));
        out.println("            <input type=\"text\" dojoType=\"dijit.form.TextBox\" id=\"" + id + "/text1\" maxLength=\"2\" size=\"2\" name=\"" + id + "/yyday\" style=\"width:30px;\" value=\"" + yyday + "\" " + radio1cd + ">&nbsp;" + paramRequest.getLocaleString("frmOf"));
        op1 = "";
        op2 = "";
        op3 = "";
        op4 = "";
        op5 = "";
        op6 = "";
        op7 = "";
        op8 = "";
        op9 = "";
        op10 = "";
        op11 = "";
        op12 = "";
        if ("add".equals(action)) {
            op1 = "selected";
        } else if (ymonth1 != null && !ymonth1.equals("")) {
            op1 = ymonth1.equals("1") ? "selected" : "";
            op2 = ymonth1.equals("2") ? "selected" : "";
            op3 = ymonth1.equals("3") ? "selected" : "";
            op4 = ymonth1.equals("4") ? "selected" : "";
            op5 = ymonth1.equals("5") ? "selected" : "";
            op6 = ymonth1.equals("6") ? "selected" : "";
            op7 = ymonth1.equals("7") ? "selected" : "";
            op8 = ymonth1.equals("8") ? "selected" : "";
            op9 = ymonth1.equals("9") ? "selected" : "";
            op10 = ymonth1.equals("10") ? "selected" : "";
            op11 = ymonth1.equals("11") ? "selected" : "";
            op12 = ymonth1.equals("12") ? "selected" : "";
        }
        out.println("            <select id=\"" + id + "/selectm1\"  name=\"" + id + "/ymonth1\" dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"true\" hasDownArrow=\"true\" style=\"width:110px;\" " + radio1cd + ">");
        out.println("                <option value=\"1\" " + op1 + " >" + paramRequest.getLocaleString("frmJanuary") + "</option>");
        out.println("                <option value=\"2\" " + op2 + " >" + paramRequest.getLocaleString("frmFebruary") + "</option>");
        out.println("                <option value=\"3\" " + op3 + " >" + paramRequest.getLocaleString("frmMarch") + "</option>");
        out.println("                <option value=\"4\" " + op4 + " >" + paramRequest.getLocaleString("frmApril") + "</option>");
        out.println("                <option value=\"5\" " + op5 + " >" + paramRequest.getLocaleString("frmMay") + "</option>");
        out.println("                <option value=\"6\" " + op6 + " >" + paramRequest.getLocaleString("frmJune") + "</option>");
        out.println("                <option value=\"7\" " + op7 + " >" + paramRequest.getLocaleString("frmJuly") + "</option>");
        out.println("                <option value=\"8\" " + op8 + " >" + paramRequest.getLocaleString("frmAugust") + "</option>");
        out.println("                <option value=\"9\" " + op9 + " >" + paramRequest.getLocaleString("frmSeptember") + "</option>");
        out.println("                <option value=\"10\" " + op10 + " >" + paramRequest.getLocaleString("frmOctober") + "</option>");
        out.println("                <option value=\"11\" " + op11 + " >" + paramRequest.getLocaleString("frmNovember") + "</option>");
        out.println("                <option value=\"12\" " + op12 + " >" + paramRequest.getLocaleString("frmDecember") + "</option>");
        out.println("            </select>");
//            if (ymonth1!=null && !ymonth2.equals("")) {
//                out.println("       <script language=\"JavaScript\" type=\"text/JavaScript\">");
//                out.println("           selectCombo(dijit.byId('"+id+"/selectm1').domNode,'"+ymonth1+"');");
//                out.println("       </script>");
//            }
        if (yyears1 != null && !yyears1.equals("")) {
            out.println("            &nbsp;" + paramRequest.getLocaleString("frmOfEvery") + " <input  type=\"text\" dojoType=\"dijit.form.TextBox\" id=\"" + id + "/text2\" maxLength=\"2\" size=\"2\" style=\"width:30px;\" name=\"" + id + "/yyears1\" value=\"" + yyears1 + "\" " + radio1cd + ">&nbsp;" + paramRequest.getLocaleString("frmYears") + "");
        } else {
            out.println("            &nbsp;" + paramRequest.getLocaleString("frmOfEvery") + "<input  type=\"text\" dojoType=\"dijit.form.TextBox\" id=\"" + id + "/text2\" maxLength=\"2\" size=\"2\" style=\"width:30px;\" name=\"" + id + "/yyears1\" value=\"1\" " + radio1cd + ">&nbsp;" + paramRequest.getLocaleString("frmYears") + "");
        }
        out.println("          </td></tr>");
        out.println("        <tr >");
        out.println("          <td colSpan=\"4\" >");
        out.println("            <hr size=\"1\" noshade>");
        out.println("          </td></tr>");
        out.println("        <tr>");
        out.println("          <td ><input id=\"" + id + "/radio2\" type=\"radio\"  dojoType=\"dijit.form.RadioButton\" name=\"" + id + "/syear\" value=\"week\" onClick=\"enableYearly('" + id + "');\" " + radio2c + " " + yearlyd + "></td>");
        out.println("          <td >" + paramRequest.getLocaleString("frmThe") + " &nbsp;");
        op1 = "";
        op2 = "";
        op3 = "";
        op4 = "";
        op5 = "";
        if ("add".equals(action)) {
            op1 = "selected";
        } else if (yweek != null && !yweek.equals("")) {
            op1 = yweek.equals("1") ? "selected" : "";
            op2 = yweek.equals("2") ? "selected" : "";
            op3 = yweek.equals("3") ? "selected" : "";
            op4 = yweek.equals("4") ? "selected" : "";
            op5 = yweek.equals("5") ? "selected" : "";
        }
        out.println("             <select id=\"" + id + "/select1\" name=\"" + id + "/yweek\"  dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"true\" hasDownArrow=\"true\" style=\"width:90px;\" " + radio2cd + ">");
        out.println("              <option value=\"1\" " + op1 + " >" + paramRequest.getLocaleString("frmFirst") + "</option>");
        out.println("              <option value=\"2\" " + op2 + " >" + paramRequest.getLocaleString("frmSecond") + "</option>");
        out.println("              <option value=\"3\" " + op3 + " >" + paramRequest.getLocaleString("frmThird") + "</option>");
        out.println("              <option value=\"4\" " + op4 + " >" + paramRequest.getLocaleString("frmFourth") + "</option>");
        out.println("              <option value=\"5\" " + op5 + " >" + paramRequest.getLocaleString("frmLast") + "</option>");
        out.println("             </select>");
        out.println("          </td>");
//            if (yweek!=null && !yweek.equals("")) {
//                out.println("       <script language=\"JavaScript\" type=\"text/JavaScript\">");
//                out.println("           selectCombo(dijit.byId('"+id+"/select1').domNode,'"+yweek+"');");
//                out.println("       </script>");
//            }
        out.println("          <td>");
        out.println("            <table cellSpacing=\"0\" cellPadding=\"1\" width=\"100%\" border=\"0\">");
        out.println("              <tbody>");
        out.println("              <tr>");
        out.println("                <td ><input  type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/yday1\" name=\"" + id + "/yday1\" " + yday1 + " " + radio2cd + ">" + paramRequest.getLocaleString("frmSun") + "</td>");
        out.println("                <td ><input  type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/yday2\" name=\"" + id + "/yday2\" " + yday2 + " " + radio2cd + ">" + paramRequest.getLocaleString("frmMon") + "</td>");
        out.println("                <td ><input  type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/yday3\" name=\"" + id + "/yday3\" " + yday3 + " " + radio2cd + ">" + paramRequest.getLocaleString("frmTue") + "</td>");
        out.println("                <td ><input  type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/yday4\" name=\"" + id + "/yday4\" " + yday4 + " " + radio2cd + ">" + paramRequest.getLocaleString("frmWed") + "</td>");
        out.println("              </tr>");
        out.println("              <tr>");
        out.println("                <td ><input  type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/yday5\" name=\"" + id + "/yday5\" " + yday5 + " " + radio2cd + ">" + paramRequest.getLocaleString("frmThu") + "</td>");
        out.println("                <td ><input  type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/yday6\" name=\"" + id + "/yday6\" " + yday6 + " " + radio2cd + ">" + paramRequest.getLocaleString("frmFri") + "</td>");
        out.println("                <td ><input  type=\"checkbox\" dojoType=\"dijit.form.CheckBox\" id=\"" + id + "/yday7\" name=\"" + id + "/yday7\" " + yday7 + " " + radio2cd + ">" + paramRequest.getLocaleString("frmSat") + "</td>");
        out.println("              </tr>");
        out.println("              </tbody>");
        out.println("            </table>");
        out.println("          </td>");
        out.println("          <td >&nbsp;" + paramRequest.getLocaleString("frmOf"));
        op1 = "";
        op2 = "";
        op3 = "";
        op4 = "";
        op5 = "";
        op6 = "";
        op7 = "";
        op8 = "";
        op9 = "";
        op10 = "";
        op11 = "";
        op12 = "";
        if ("add".equals(action)) {
            op1 = "selected";
        } else if (ymonth2 != null && !ymonth2.equals("")) {
            op1 = ymonth2.equals("1") ? "selected" : "";
            op2 = ymonth2.equals("2") ? "selected" : "";
            op3 = ymonth2.equals("3") ? "selected" : "";
            op4 = ymonth2.equals("4") ? "selected" : "";
            op5 = ymonth2.equals("5") ? "selected" : "";
            op6 = ymonth2.equals("6") ? "selected" : "";
            op7 = ymonth2.equals("7") ? "selected" : "";
            op8 = ymonth2.equals("8") ? "selected" : "";
            op9 = ymonth2.equals("9") ? "selected" : "";
            op10 = ymonth2.equals("10") ? "selected" : "";
            op11 = ymonth2.equals("11") ? "selected" : "";
            op12 = ymonth2.equals("12") ? "selected" : "";
        }
        out.println("             <select id=\"" + id + "/selectm2\" name=\"" + id + "/ymonth2\" dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"true\" hasDownArrow=\"true\" style=\"width:110px;\" " + radio2cd + ">");
        out.println("                <option value=\"1\" " + op1 + " >" + paramRequest.getLocaleString("frmJanuary") + "</option>");
        out.println("                <option value=\"2\" " + op2 + " >" + paramRequest.getLocaleString("frmFebruary") + "</option>");
        out.println("                <option value=\"3\" " + op3 + " >" + paramRequest.getLocaleString("frmMarch") + "</option>");
        out.println("                <option value=\"4\" " + op4 + " >" + paramRequest.getLocaleString("frmApril") + "</option>");
        out.println("                <option value=\"5\" " + op5 + " >" + paramRequest.getLocaleString("frmMay") + "</option>");
        out.println("                <option value=\"6\" " + op6 + " >" + paramRequest.getLocaleString("frmJune") + "</option>");
        out.println("                <option value=\"7\" " + op7 + " >" + paramRequest.getLocaleString("frmJuly") + "</option>");
        out.println("                <option value=\"8\" " + op8 + " >" + paramRequest.getLocaleString("frmAugust") + "</option>");
        out.println("                <option value=\"9\" " + op9 + " >" + paramRequest.getLocaleString("frmSeptember") + "</option>");
        out.println("                <option value=\"10\" " + op10 + " >" + paramRequest.getLocaleString("frmOctober") + "</option>");
        out.println("                <option value=\"11\" " + op11 + " >" + paramRequest.getLocaleString("frmNovember") + "</option>");
        out.println("                <option value=\"12\" " + op12 + " >" + paramRequest.getLocaleString("frmDecember") + "</option>");
        out.println("             </select>");
//            if (ymonth2 != null && !ymonth2.equals("")) {
//                out.println("       <script language=\"JavaScript\" type=\"text/JavaScript\">");
//                out.println("           selectCombo(dijit.byId('"+id+"/selectm2').domNode,'" + ymonth2 + "');");
//                out.println("       </script>");
//            }
        if (yyears2 != null && !yyears2.equals("")) {
            out.println("               &nbsp;" + paramRequest.getLocaleString("frmOfEvery") + " &nbsp;");
            out.println("               <input   id=\"" + id + "/text3\" type=\"text\" dojoType=\"dijit.form.TextBox\" maxLength=\"2\" size=\"2\" name=\"" + id + "/yyears2\" value=\"" + yyears2 + "\"  style=\"width:30px;\" " + radio2cd + ">");
            out.println("               &nbsp;" + paramRequest.getLocaleString("frmYears") + "</td></tr></tbody></table></td></tr>");
        } else {
            out.println("               &nbsp;" + paramRequest.getLocaleString("frmOfEvery") + " &nbsp;");
            out.println("               <input  id=\"" + id + "/text3\" type=\"text\" dojoType=\"dijit.form.TextBox\" maxLength=\"2\" size=\"2\" name=\"" + id + "/yyears2\" value=\"1\"  style=\"width:30px;\" " + radio2cd + ">");
            out.println("               &nbsp;" + paramRequest.getLocaleString("frmYears") + "</td></tr></tbody></table></td></tr>");
        }

        out.println("</tbody></table>");

        out.println("<input type=\"hidden\" name=\"id\" value=\"" + strId + "\">");
        out.println("<input type=\"hidden\" name=\"view\" value=\"" + view + "\"> ");
        out.println("<input type=\"hidden\" name=\"tp\" value=\"" + topic + "\"> ");
        out.println("<input type=\"hidden\" name=\"tm\" value=\"" + tm + "\"> ");
        //out.println("<input type=\"hidden\" name=\"type\" value=\"" + strType + "\"> ");
        out.println("<input type=\"hidden\" name=\"oldtitle\" value=\"" + title + "\"> ");
        out.println("<input type=\"hidden\" name=\"createdate\" value=\"" + createdate + "\"> ");
        out.println("<input type=\"hidden\" name=\"usercreate\" value=\"" + usercreate + "\"> ");
        out.println("<input type=\"hidden\" name=\"active\" value=\"" + active + "\"> ");
        out.println("<input type=\"hidden\" name=\"suri\" value=\"" + id + "\"> ");
        out.println("<input type=\"hidden\" name=\"sprop\" value=\"" + idp + "\"> ");
        out.println("<input type=\"hidden\" name=\"spropref\" value=\"" + idpref + "\"> ");
        if (null != idCalendar) {
            out.println("<input type=\"hidden\" name=\"sval\" value=\"" + idCalendar + "\"> ");
        }
        out.println("</fieldset>");
        out.println("<fieldset>");
        out.println("<button dojoType=\"dijit.form.Button\" name=\"enviar\" onclick=\"submitForm('" + id + "/calendar'); return false;\">" + paramRequest.getLocaleString("btnSend") + "</button>");
        if (id != null && idp != null) {
            SWBResourceURL urlb = paramRequest.getRenderUrl();
            urlb.setParameter("suri", id);
            urlb.setParameter("sprop", idp);
            if (idpref != null) {
                urlb.setParameter("spropref", idpref);
            }
            urlb.setMode(SWBResourceURL.Mode_VIEW);
            out.println("<button dojoType=\"dijit.form.Button\" id=\"" + id + "/bckbutton\" name=\"bckbutton\" onclick=\"submitUrl('" + urlb + "',document.getElementById('" + id + "/calendar')); return false;\">Regresar</button>"); //dijit.byId('"+id+"/calendar').domNode
        }
        out.println("</fieldset>");
        out.println("</form>");
        out.println("</div>");
//            out.println("<script>");
//            out.println("  window.onload=function() { ");
//            out.println("   alert('onload...');");
//            out.println("   disablePeriodicity('"+id+"');");
//            out.println("};");
//            out.println("</script>"); 

    }

    /**
     *
     * @param request
     * @param response
     * @throws AFException
     * @throws IOException
     */
    @Override
    public void processAction(HttpServletRequest request, SWBActionResponse response) throws SWBResourceException, IOException {

        String id = request.getParameter("suri");
        String sval = id;
        User user = response.getUser();
        String action = response.getAction();
        if (null == action) {
            action = "";
        }
        String stype = request.getParameter(id + "/type") != null ? request.getParameter("type") : "";
        String sres = request.getParameter(id + "/res");
        String suserid = request.getParameter(id + "/userid");
        String act = request.getParameter(id + "/act");
        String strTp = request.getParameter(id + "/tp");
        String strTm = request.getParameter(id + "/tm");
        String strId = request.getParameter(id + "/id");


        String strCreateDate = "";
        //String strUserCreate=response.getUser().getId();
        String strUserCreate = request.getParameter(id + "/usercreate");
        String strModDate = Long.toString(System.currentTimeMillis());
        String strUserMod = response.getUser().getId();
        String strActive = request.getParameter(id + "/active");

        log.debug("SWBASchedule.processAction()--------------------------------------");
        Enumeration<String> enup = request.getParameterNames();
        while (enup.hasMoreElements()) {
            String param = enup.nextElement();
            log.debug(param + ": " + request.getParameter(param));
        }

        String rid = request.getParameter("rsuri");
        String sprop = request.getParameter("sprop");
        String spropref = request.getParameter("spropref");
        SemanticOntology ont = SWBPlatform.getSemanticMgr().getOntology();
        SemanticObject obj = ont.getSemanticObject(id); //// object al cual se le va a asociar el calendario
        SemanticClass cls = obj.getSemanticClass();

        String idCalendar = sval;
        if ("update".equals(action)) {
            if (sval != null) {
                obj = ont.getSemanticObject(sval);
            }
            idCalendar = obj.getURI();
            cls = obj.getSemanticClass();
            Iterator<SemanticProperty> it = cls.listProperties();
            while (it.hasNext()) {
                SemanticProperty prop = it.next();
                if (prop.isDataTypeProperty()) {
                    String value = request.getParameter(id + "/" + prop.getName());
                    log.debug("SWBASchedule: ProcessAction(update): " + prop.getName() + " -- >" + value);
                    if (value != null) {
                        if (value.length() > 0) {
                            if (prop.isBoolean()) {
                                obj.setBooleanProperty(prop, true);
                                if (prop.getName().equals(Activeable.swb_active.getName())) {
                                    strActive = "true";
                                }
                            }
                            if (prop.isInt()) {
                                obj.setLongProperty(prop, Integer.parseInt(value));
                            }
                            if (prop.isString()) {
                                obj.setProperty(prop, value);
                            }
                            if (prop.isFloat()) {
                                obj.setFloatProperty(prop, Float.parseFloat(value));
                            }
                            if (prop.isDate() || prop.isDateTime()) {
                                if (prop.getName().equals(Traceable.swb_updated.getName()) || prop.getName().equals(Traceable.swb_created.getName())) {
                                    obj.setDateProperty(prop, new Date(System.currentTimeMillis()));
                                }
                            }
                        } else {
                            if (prop.isBoolean()) {
                                obj.setBooleanProperty(prop, false);
                                if (prop.getName().equals(Activeable.swb_active.getName())) {
                                    strActive = "false";
                                }
                            }
                        }
                    }
                }
            }
            Date mdate = new Date(System.currentTimeMillis());
            obj.setDateProperty(Traceable.swb_updated, mdate);
            strUserMod = user.getId();
            strModDate = Long.toString(mdate.getTime());
            response.setRenderParameter("statmsg", response.getLocaleString("statmsg2"));
            if (id != null) {
                response.setRenderParameter("suri", id);
                response.setRenderParameter("sval", id);
            }
            if (sprop != null) {
                response.setRenderParameter("sprop", sprop);
            }
            if (spropref != null) {
                response.setRenderParameter("spropref", spropref);
            }
            response.setRenderParameter("act", "");
            response.setMode(response.Mode_VIEW);
        }
        if ("update".equals(action) && null != idCalendar) {
            log.debug("Id Calendario: " + idCalendar);
            String xmlOrig = null;
            Document doc = null;
            SemanticObject so = ont.getSemanticObject(idCalendar);
            if (request.getParameter(id + "/createdate") != null && !request.getParameter(id + "/createdate").equals("")) {
                strCreateDate = request.getParameter(id + "/createdate");
            }
            if (request.getParameter(id + "/usercreate") != null && !request.getParameter(id + "/usercreate").equals("")) {
                strUserCreate = request.getParameter(id + "/usercreate");
            }
            try {
                try {
                    xmlOrig = so.getProperty(XMLable.swb_xml);
                } catch (Exception xmle) {   // no tiene XML definido, genera un nuevo XML
                    doc = SWBUtils.XML.getNewDocument();
                    xmlOrig = "";
                }
                doc = SWBUtils.XML.getNewDocument();
                Element interval = doc.createElement("interval");
                doc.appendChild(interval);
                String fechaIni = request.getParameter(id + "/inidate");

                addElem(doc, interval, "inidate", cambiaFormato(fechaIni, 1));
                addElem(doc, interval, "active", strActive);
                addElem(doc, interval, "createdate", strCreateDate);
                addElem(doc, interval, "usercreate", strUserCreate);
                addElem(doc, interval, "moddate", strModDate);
                addElem(doc, interval, "usermod", user.getId());
                String endselect = request.getParameter(id + "/endselect");
                if (endselect != null && endselect.equals("enddate")) {
                    String fechaFin = request.getParameter(id + "/enddate");
                    addElem(doc, interval, "enddate", cambiaFormato(fechaFin, 1));
                }
                if (request.getParameter(id + "/time") != null) {
                    String starthour = request.getParameter(id + "/starthour");
                    log.debug("Hora inicial:" + starthour);
                    starthour = starthour.substring(11, 16);
                    addElem(doc, interval, "starthour", starthour);
                    String endhour = request.getParameter(id + "/endhour");
                    log.debug("Hora final:" + endhour);
                    endhour = endhour.substring(11, 16);
                    addElem(doc, interval, "endhour", endhour);
                }
                if (request.getParameter(id + "/periodicidad") != null) {
                    Element iterations = doc.createElement("iterations");
                    interval.appendChild(iterations);
                    String period = request.getParameter(id + "/period");
                    if (period != null) {
                        Element eleperiod = doc.createElement(period);
                        iterations.appendChild(eleperiod);
                        if (period.equals("weekly")) {
                            int x = 0;
                            if (request.getParameter(id + "/wday1") != null) {
                                x += 1;
                            }
                            if (request.getParameter(id + "/wday2") != null) {
                                x += 2;
                            }
                            if (request.getParameter(id + "/wday3") != null) {
                                x += 4;
                            }
                            if (request.getParameter(id + "/wday4") != null) {
                                x += 8;
                            }
                            if (request.getParameter(id + "/wday5") != null) {
                                x += 16;
                            }
                            if (request.getParameter(id + "/wday6") != null) {
                                x += 32;
                            }
                            if (request.getParameter(id + "/wday7") != null) {
                                x += 64;
                            }
                            addElem(doc, eleperiod, "wdays", "" + x);
                        }
                        if (period.equals("monthly")) {
                            String smonth = request.getParameter(id + "/smonth");
                            if (smonth != null && smonth.equals("day")) {
                                if (request.getParameter(id + "/mmday") != null) {
                                    addElem(doc, eleperiod, "day", request.getParameter(id + "/mmday"));
                                }
                                if (request.getParameter(id + "/mmonths1") != null) {
                                    addElem(doc, eleperiod, "months", request.getParameter(id + "/mmonths1"));
                                }
                            } else if (smonth != null && smonth.equals("week")) {
                                if (request.getParameter(id + "/mweek") != null) {
                                    addElem(doc, eleperiod, "week", request.getParameter(id + "/mweek"));
                                }
                                int x = 0;
                                if (request.getParameter(id + "/mday1") != null) {
                                    x += 1;
                                }
                                if (request.getParameter(id + "/mday2") != null) {
                                    x += 2;
                                }
                                if (request.getParameter(id + "/mday3") != null) {
                                    x += 4;
                                }
                                if (request.getParameter(id + "/mday4") != null) {
                                    x += 8;
                                }
                                if (request.getParameter(id + "/mday5") != null) {
                                    x += 16;
                                }
                                if (request.getParameter(id + "/mday6") != null) {
                                    x += 32;
                                }
                                if (request.getParameter(id + "/mday7") != null) {
                                    x += 64;
                                }
                                addElem(doc, eleperiod, "wdays", "" + x);
                                if (request.getParameter(id + "/mmonths2") != null) {
                                    addElem(doc, eleperiod, "months", request.getParameter(id + "/mmonths2"));
                                }
                            }
                        }
                        if (period.equals("yearly")) {
                            log.debug("Entro a YEARLY");
                            String syear = request.getParameter(id + "/syear");
                            if (syear != null && syear.equals("day")) {
                                if (request.getParameter(id + "/yyday") != null) {
                                    log.debug("YYDAY:" + request.getParameter(id + "/yyday"));
                                    addElem(doc, eleperiod, "day", request.getParameter(id + "/yyday"));
                                }
                                if (request.getParameter(id + "/ymonth1") != null) {
                                    addElem(doc, eleperiod, "month", request.getParameter(id + "/ymonth1"));
                                }
                                if (request.getParameter(id + "/yyears1") != null) {
                                    addElem(doc, eleperiod, "years", request.getParameter(id + "/yyears1"));
                                }
                            } else if (syear != null && syear.equals("week")) {
                                if (request.getParameter(id + "/yweek") != null) {
                                    addElem(doc, eleperiod, "week", request.getParameter(id + "/yweek"));
                                }
                                int x = 0;
                                if (request.getParameter(id + "/yday1") != null) {
                                    x += 1;
                                }
                                if (request.getParameter(id + "/yday2") != null) {
                                    x += 2;
                                }
                                if (request.getParameter(id + "/yday3") != null) {
                                    x += 4;
                                }
                                if (request.getParameter(id + "/yday4") != null) {
                                    x += 8;
                                }
                                if (request.getParameter(id + "/yday5") != null) {
                                    x += 16;
                                }
                                if (request.getParameter(id + "/yday6") != null) {
                                    x += 32;
                                }
                                if (request.getParameter(id + "/yday7") != null) {
                                    x += 64;
                                }
                                addElem(doc, eleperiod, "wdays", "" + x);
                                if (request.getParameter(id + "/ymonth2") != null) {
                                    addElem(doc, eleperiod, "month", request.getParameter(id + "/ymonth2"));
                                }
                                if (request.getParameter(id + "/yyears2") != null) {
                                    addElem(doc, eleperiod, "years", request.getParameter(id + "/yyears2"));
                                }
                            }
                        }
                    }
                }
                String strXml = SWBUtils.XML.domToXml(doc);
                log.debug(strXml);
                so.setProperty(XMLable.swb_xml, strXml);

            } catch (Exception e) {
                log.error("The XML schedule can't be generated", e);
                // No se actualiza y conserva el XML original
                so.setProperty(XMLable.swb_xml, xmlOrig);
            }
        }
        if (null != id) {
            response.setRenderParameter("suri", id);
            response.setRenderParameter("sval", id);
        }
        if (null != sprop) {
            response.setRenderParameter("sprop", sprop);
        }
        if (null != spropref) {
            response.setRenderParameter("spropref", spropref);
        }
    }

    /**
     *
     * @param doc
     * @param parent
     * @param elemName
     * @param elemValue
     */
    public void addElem(Document doc, Element parent, String elemName, String elemValue) {
        Element elem = doc.createElement(elemName);
        elem.appendChild(doc.createTextNode(elemValue));
        parent.appendChild(elem);
    }

//    public String getDateFormat(long dateTime, String lang) {
//        if (null == lang) {
//            lang = "es";
//        }
//        Locale local = new Locale(lang);
//        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, local);
//        return df.format(new Date(dateTime));
//    }
    public String getDisplaySemObj(SemanticObject obj, String lang) {
        String ret = obj.getRDFName();
        try {
            ret = obj.getDisplayName(lang);
        } catch (Exception e) {
            ret = obj.getProperty(Descriptiveable.swb_title);
        }
        return ret;
    }

    public String getValueSemProp(SemanticObject obj, SemanticProperty prop) {
        String ret = "";
        try {
            if (prop.isDataTypeProperty()) {
                log.debug("getValueSemProp..." + obj.getProperty(prop));
                if (prop.isBoolean()) {
                    ret = "" + obj.getBooleanProperty(prop);
                }
                if (prop.isInt() || prop.isFloat()) {
                    ret = "" + obj.getLongProperty(prop);
                }
                if (prop.isString()) {
                    ret = obj.getProperty(prop);
                }
                if (prop.isDateTime()) {
                    try {
                        ret = "" + obj.getProperty(prop);
                    } catch (Exception e1) {
                        ret = obj.getProperty(prop);
                    }
                }
            }
        } catch (Exception e) {
            ret = "Not set";
        }
        return ret;
    }

    private String cambiaFormato(String fecha, int formato) {
        String nf = fecha;
        String y = "";
        String m = "";
        String d = "";
        if (formato == 1) {
            StringTokenizer st = new StringTokenizer(fecha, "-");
            if (st.hasMoreTokens()) {
                y = st.nextToken();
                if (st.hasMoreTokens()) {
                    m = st.nextToken();
                }
                if (st.hasMoreTokens()) {
                    d = st.nextToken();
                }
                nf = m + "/" + d + "/" + y;
            }
        } else if (formato == 2) {
            StringTokenizer st = new StringTokenizer(fecha, "/");
            if (st.hasMoreTokens()) {
                m = st.nextToken();
                if (st.hasMoreTokens()) {
                    d = st.nextToken();
                }
                if (st.hasMoreTokens()) {
                    y = st.nextToken();
                }
                nf = y + "-" + m + "-" + d;
            }
        }
        return nf;
    }
}

