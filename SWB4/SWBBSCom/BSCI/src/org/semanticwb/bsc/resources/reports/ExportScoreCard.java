package org.semanticwb.bsc.resources.reports;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.semanticwb.SWBPlatform;
import org.semanticwb.bsc.BSC;
import org.semanticwb.bsc.accessory.Period;
import org.semanticwb.bsc.element.Indicator;
import org.semanticwb.bsc.element.Objective;
import org.semanticwb.model.Resource;
import org.semanticwb.model.Resourceable;
import org.semanticwb.model.WebPage;
import org.semanticwb.model.WebSite;
import org.semanticwb.portal.api.GenericResource;
import org.semanticwb.portal.api.SWBParamRequest;
import org.semanticwb.portal.api.SWBResourceException;
import org.semanticwb.portal.api.SWBResourceURL;

/**
 *
 * @author ana.garcias
 */
public class ExportScoreCard extends GenericResource {

    public void doExport(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        response.setContentType("application/pdf");
        WebSite ws = paramRequest.getWebPage().getWebSite();

        if (ws instanceof BSC) {
            String objective = "";
            String theme = "";
            String perspective = "";
            String indicator = "";
            String scoreCard = "";

            LinkedList<ParamsScoreCard> lista = new LinkedList<ParamsScoreCard>();
            Period period = getPeriod(request);
            scoreCard = ws.getTitle();
            JasperTemplates jasperLogo = JasperTemplates.LOGO;
            Iterator<Objective> itObje = Objective.ClassMgr.listObjectiveByPeriod(period, ws);
            int exp = 0;
            while (itObje.hasNext()) {
                Objective ob = itObje.next();
                if (ob.isActive()) {
                    exp = 1;
                    boolean hasIndicator = false;
                    objective = ob.getTitle();
                    theme = ob.getTheme().getTitle();
                    perspective = ob.getTheme().getPerspective().getTitle();
                    //Saca los indicadores del periodo y evalua si pertenecen al mismo objetivo
                    Iterator<Indicator> itIndPeriod = Indicator.ClassMgr.listIndicatorByPeriod(period, ws);
                    int count = 0;

                    while (itIndPeriod.hasNext()) {
                        Indicator indic = itIndPeriod.next();
                        if (indic.getObjective().equals(ob) && indic.isActive()) {
                            hasIndicator = true;
                            indicator = indic.getTitle();
                            if (count == 0) {
                                lista.add(new ParamsScoreCard(perspective, theme, objective, indicator, scoreCard, "", this.getClass().getResourceAsStream(jasperLogo.getTemplatePath())));
                            } else {
                                lista.add(new ParamsScoreCard("", "", "", indicator, scoreCard, "", this.getClass().getResourceAsStream(jasperLogo.getTemplatePath())));
                            }
                            count++;
                        }
                    }
                    if (hasIndicator == false) {
                        lista.add(new ParamsScoreCard(perspective, theme, objective, "", scoreCard, "", this.getClass().getResourceAsStream(jasperLogo.getTemplatePath())));
                    }
                }
            }

            if (exp == 0) {
                lista.add(new ParamsScoreCard("", "", "", "", scoreCard, "No existen objetivos válidos para el periodo seleccionado", this.getClass().getResourceAsStream(jasperLogo.getTemplatePath())));
            }
            try {
                JasperTemplates jasperTemplate = JasperTemplates.SCORECARD;
                InputStream is = getClass().getResourceAsStream(jasperTemplate.getTemplatePath());
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JRBeanCollectionDataSource(lista));
                JRExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "iso-8859-1");
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
                exporter.exportReport();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void doView(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        //super.doView(request, response, paramRequest); //To change body of generated methods, choose Tools | Templates.
        /*PrintWriter out = response.getWriter();
         StringBuilder liga = new StringBuilder(128);

         SWBResourceURL url = paramRequest.getRenderUrl();
         url.setCallMethod(SWBResourceURL.Call_DIRECT);
         url.setMode("export");
         liga.append("<table height=\"200px\"><tr><td>H</td></tr></table>");
         liga.append("<a href=\"");
         liga.append(url);
         liga.append("\" target=\"_new\">Exportar AQUI</a>");
         out.write(liga.toString());*/
        SWBResourceURL url = paramRequest.getRenderUrl();
        url.setCallMethod(SWBResourceURL.Call_DIRECT);
        url.setMode("export");
        response.sendRedirect(url.toString());
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        //Evaluar el mode
        if (paramRequest.getCallMethod() == SWBParamRequest.Call_STRATEGY) {
            doViewStrategy(request, response, paramRequest);
        } else if (paramRequest.getMode().equalsIgnoreCase("export")) {
            doExport(request, response, paramRequest);
        } else {
            super.processRequest(request, response, paramRequest); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private Period getPeriod(HttpServletRequest request) {
        //String id = getResourceBase().getWebSiteId();
        WebSite ws = getResourceBase().getWebSite();
        Period period = null;
        //if (request.getSession(true).getAttribute(ws.getId()) != null) {
        String pid = (String) request.getSession(true).getAttribute(ws.getId());
        if (Period.ClassMgr.hasPeriod(pid, ws)) {
            period = Period.ClassMgr.getPeriod(pid, ws);
        }
        //}
        return period;
    }

    /**
     * Genera el despliegue de la liga que redireccionar&aacute; al recurso que 
     * exporta un Scorecard.
     * 
     * @param request Proporciona informaci&oacute;n de petici&oacute;n HTTP
     * @param response Proporciona funcionalidad especifica HTTP para
     * envi&oacute; en la respuesta
     * @param paramRequest Objeto con el cual se acceden a los objetos de SWB
     * @return el objeto String que representa el c&oacute;digo HTML con la liga
     * y el icono correspondiente al elemento a exportar.
     * @throws SWBResourceException SWBResourceException Excepti&oacute;n
     * utilizada para recursos de SWB
     * @throws IOException Excepti&oacute;n de IO
     */
    public void doViewStrategy(HttpServletRequest request, HttpServletResponse response,
            SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        PrintWriter out = response.getWriter();
        Resource base = paramRequest.getResourceBase();
        String surl = paramRequest.getWebPage().getUrl();
        Iterator<Resourceable> res = base.listResourceables();
        while (res.hasNext()) {
            Resourceable re = res.next();
            if (re instanceof WebPage) {
                surl = ((WebPage) re).getUrl();
                break;
            }
        }
        String webWorkPath = SWBPlatform.getContextPath() + "/swbadmin/icons/";
        String image = "exportScoreCard.png";
        String alt = paramRequest.getLocaleString("alt");
        out.println("<a href=\"" + surl + "\" class=\"swb-toolbar-stgy\" title=\"image\">");
        out.println("<img src=\"" + webWorkPath + image + "\" alt=\"" + alt + "\" class=\"toolbar-img\" />");
        out.println("</a>");
    }
}
