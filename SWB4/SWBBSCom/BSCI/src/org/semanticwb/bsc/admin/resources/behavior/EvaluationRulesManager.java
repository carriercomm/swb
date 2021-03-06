package org.semanticwb.bsc.admin.resources.behavior;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBUtils;
import org.semanticwb.base.util.GenericFilterRule;
import org.semanticwb.bsc.BSC;
import org.semanticwb.bsc.Measurable;
import org.semanticwb.bsc.SM;
import org.semanticwb.bsc.accessory.State;
import org.semanticwb.bsc.catalogs.Operation;
import org.semanticwb.bsc.element.Deliverable;
import org.semanticwb.bsc.element.Initiative;
import org.semanticwb.bsc.tracing.EvaluationRule;
import org.semanticwb.bsc.tracing.Series;
import org.semanticwb.model.GenericIterator;
import org.semanticwb.model.SWBContext;
import org.semanticwb.model.SWBModel;
import org.semanticwb.model.User;
import org.semanticwb.platform.SemanticClass;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticOntology;
import org.semanticwb.platform.SemanticProperty;
import org.semanticwb.portal.api.GenericAdmResource;
import org.semanticwb.portal.api.SWBActionResponse;
import org.semanticwb.portal.api.SWBParamRequest;
import org.semanticwb.portal.api.SWBResourceException;
import org.semanticwb.portal.api.SWBResourceURL;


/**
 * Se encarga de mostrar un formulario para definir las reglas de evaluación, de
 * una serie dada, para cada estado disponible en el indicador.
 * 
 * @author carlos.ramos
 * @version %I%, %G%
 * @since 1.0
 */
public class EvaluationRulesManager extends GenericAdmResource {
    public static final String Action_UPDT_SERIES = "updsrs";
    public static final String Action_UPDT_OPER = "updopr";
    public static final String Action_UPDT_FACTOR = "updftr";
    public static final String Action_UPDT_ACTIVE = "updactv";
    public static final String Action_ACTIVE_ALL = "actall";
    public static final String Action_DEACTIVE_ALL = "deactall";
    public static final String Action_DELETE_ALL = "delall";
    public static final String Action_UPDT_PROPERTY = "semprty";
    

    @Override
    public void doView(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        response.setContentType("text/html; charset=ISO-8859-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        
        User user = paramRequest.getUser();
        if(user==null || !user.isSigned())
        {
            response.sendError(403);
            return;
        }
         
        final String suri = request.getParameter("suri");
        if(suri==null) {
            response.getWriter().println("No se detect&oacute ning&uacute;n objeto sem&aacute;ntico!");
            response.flushBuffer();
            return;
        }
        
        PrintWriter out = response.getWriter();
        
        SemanticOntology ont = SWBPlatform.getSemanticMgr().getOntology();
        SemanticObject semObj = ont.getSemanticObject(suri);
        if (request.getParameter("statmsg") != null && request.getParameter("statmsg").trim().length() > 0) {
            out.println("<script type=\"text/javascript\">");
            out.println("   showStatus('" + request.getParameter("statmsg") + "');");
            out.println("updateTreeNodeByURI('" + semObj.getURI() + "');");
            String icon = SWBContext.UTILS.getIconClass(semObj);
            out.println("setTabTitle('" + semObj.getURI() + "','" + semObj.getDisplayName(user.getLanguage()) + "','" + icon + "');");
            out.println("</script>");
        }        
        Measurable measurable = (Measurable)semObj.getGenericInstance();
//        if(measurable instanceof Series)
//        {
            try {
                doEditSeries(request, response, paramRequest);
            }catch(ClassCastException cce) {
                response.getWriter().println("No se detect&oacute ninguna serie de medici&oacuten!");
            }finally {
                response.flushBuffer();
            }
//        }
//        else if(measurable instanceof Deliverable)
//        {
//            try {
//                doEditDeliverable(request, response, paramRequest);
//            }catch(ClassCastException cce) {
//                response.getWriter().println("No se detect&oacute ninguna serie de medici&oacuten!");
//            }finally {
//                response.flushBuffer();
//            }
//        }
    }

    private void doEditSeries(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException, ClassCastException {
        response.setContentType("text/html; charset=ISO-8859-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        
        User user = paramRequest.getUser();
        final String lang = user.getLanguage();
        boolean hasRules = Boolean.FALSE;
        
        final String suri = request.getParameter("suri");
        SemanticOntology ont = SWBPlatform.getSemanticMgr().getOntology();
        SemanticObject obj = ont.getSemanticObject(suri);
        Series series;
        
        PrintWriter out = response.getWriter();
        
        
        SWBResourceURL url = paramRequest.getActionUrl();
        url.setAction(SWBResourceURL.Action_ADD);
        String action = paramRequest.getAction();
        if(SWBResourceURL.Action_EDIT.equalsIgnoreCase(action))
        {
            out.println("<div class=\"swbform\">");
            out.println("<fieldset>");
            out.println("<table width=\"98%\">"); 
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>"+paramRequest.getLocaleString("lblAction")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblStatus")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblStatusGroup")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblOperation")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblSeries")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblFactor")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblActive")+"</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            
            series = (Series)obj.getGenericInstance();
            final SM sm = series.getSm();
            BSC bsc = sm.getBSC();

            // Obtener la lista de operaciones válidas
            List<Operation> operations = SWBUtils.Collections.filterIterator(Operation.ClassMgr.listOperations(bsc),
                                    new GenericFilterRule<Operation>(){
                                        @Override
                                        public boolean filter(Operation o) {
                                            User user = SWBContext.getAdminUser();
                                            return !o.isValid() || !user.haveAccess(o);
                                        } 
                                    });

            // Obtener la lista de series para contraparte
            List<Series> siblingSerieses = sm.listValidSerieses();
            // Eliminamos de la lista la serie actual
            siblingSerieses.remove(series);

            // Crear el conjunto de reglas, si 
            // una regla ya está en el conjunto no se agrega de nuevo 
            HashSet<State> configuredStates = new HashSet();            
            GenericIterator rules = series.listEvaluationRules();
            List<EvaluationRule> lrules = SWBUtils.Collections.copyIterator(rules);
            Collections.sort(lrules);
            Collections.reverse(lrules);
            //rules = lrules.iterator();
            //hasRules = rules.hasNext();
            hasRules = !lrules.isEmpty();
            //while(rules.hasNext()) {
            for(EvaluationRule rule : lrules) {
                //ObjectiveEvaluationRule rule = rules.next();
                out.println("  <tr>");                
                
                // Eliminar regla
                out.println("<td>");
                SWBResourceURL urlr = paramRequest.getActionUrl();
                urlr.setParameter("suri", suri);
                urlr.setParameter("sval", rule.getURI());
                urlr.setAction(SWBResourceURL.Action_REMOVE);
                out.println("<a href=\"#\" onclick=\"if(confirm('" + paramRequest.getLocaleString("queryRemove") + " " 
                        + (rule.getTitle(lang)==null?(rule.getTitle()==null?"Sin título":rule.getTitle().replaceAll("'","")):rule.getTitle(lang).replaceAll("'","")) 
                        + "?')){submitUrl('" + urlr + "',this);} else { return false;}\"><img src=\"" + SWBPlatform.getContextPath() + "/swbadmin/images/delete.gif\" border=0></a>");
                out.println("</td>");
                
                // Estado
                SWBResourceURL urlchoose = paramRequest.getRenderUrl();
                urlchoose.setParameter("suri", suri);
                urlchoose.setParameter("sval", rule.getURI());
                out.println("   <td>");
                if(rule.getAppraisal()==null) {
                    out.println("Not set");
                }else {
                    out.println("<a href=\"#\" onclick=\"addNewTab('" + rule.getAppraisal().getURI() + "','" + SWBPlatform.getContextPath() + "/swbadmin/jsp/objectTab.jsp" + "','" + (rule.getAppraisal().getTitle(lang)==null?(rule.getAppraisal().getTitle()==null?"Sin título":rule.getAppraisal().getTitle().replaceAll("'","")):rule.getAppraisal().getTitle(lang).replaceAll("'","")) + "');return false;\" >" + (rule.getAppraisal().getTitle(lang)==null?(rule.getAppraisal().getTitle()==null?"Sin título":rule.getAppraisal().getTitle().replaceAll("'","")):rule.getAppraisal().getTitle(lang).replaceAll("'","")) + "</a>");
                }
                out.println("   </td>");
                
                // Grupo del estado
                out.println("   <td>");
                if(rule.getAppraisal()==null) {
                    out.println("--");
                }else {
                    out.println((rule.getAppraisal().getStateGroup().getTitle(lang)==null?(rule.getAppraisal().getStateGroup().getTitle()==null?"Sin título":rule.getAppraisal().getStateGroup().getTitle().replaceAll("'","")):rule.getAppraisal().getStateGroup().getTitle(lang).replaceAll("'","")));
                }
                out.println("   </td>");
                
                // Lista de operadores
                SWBResourceURL urlopr = paramRequest.getActionUrl();
                urlopr.setParameter("suri", suri);
                urlopr.setParameter("sval", rule.getURI());
                urlopr.setAction(Action_UPDT_OPER);
                out.println("   <td>");
                out.println("    <select name=\"operId\" onchange=\"submitUrl('" + urlopr + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"false\" hasDownArrow=\"true\" style=\"width:120px;\">");
                out.println("     <option value=\"\"></option>");
                for(Operation operation:operations) {
                    out.println("     <option value=\""+operation.getId()+"\"");
                    out.println(operation.getId().equals(rule.getOperationId())?" selected=\"selected\"":"");
                    out.println("      >"+(operation.getTitle(lang)==null?(operation.getTitle()==null?"Sin título":operation.getTitle().replaceAll("'","")):operation.getTitle(lang).replaceAll("'",""))+"</option>");
                }            
                out.println("    </select>");
                out.println("   </td>");
                
                // Lista de series hermanas
                SWBResourceURL urluinh = paramRequest.getActionUrl();
                urluinh.setParameter("suri", suri);
                urluinh.setParameter("sval", rule.getURI());
                urluinh.setAction(Action_UPDT_SERIES);
                out.println("   <td>");
                out.println("    <select name=\"ssId\" onchange=\"submitUrl('" + urluinh + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"false\" hasDownArrow=\"true\" style=\"width:120px;\">");
                out.println("     <option value=\"\"></option>");
                for(Series s:siblingSerieses) {
                    out.println("     <option value=\""+s.getId()+"\"");
                    out.println(s.equals(rule.getAnotherSeries())?" selected=\"selected\"":"");
                    out.println("      >"+(s.getTitle(lang)==null?s.getTitle():s.getTitle(lang))+"</option>");
                }
                out.println("    </select>");
                out.println("   </td>");
                
                // Factor
                SWBResourceURL urlfctr = paramRequest.getActionUrl();
                urlfctr.setParameter("suri", suri);
                urlfctr.setParameter("sval", rule.getURI());
                urlfctr.setAction(Action_UPDT_FACTOR);
                out.println("   <td><input type=\"text\" name=\"fctr\" onchange=\"submitUrl('" + urlfctr + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.TextBox\" value=\""+(rule.getFactor()==null?"":rule.getFactor())+"\" /></td>");
                
                // Activo?
                SWBResourceURL urlactv = paramRequest.getActionUrl();
                urlactv.setParameter("suri", suri);
                urlactv.setParameter("sval", rule.getURI());
                urlactv.setAction(Action_UPDT_ACTIVE);
                out.println("   <td align=\"center\"><input type=\"checkbox\" name=\"act\" onchange=\"submitUrl('" + urlactv + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.CheckBox\" value=\""+rule.getId()+"\" "+(rule.isActive()?"checked=\"checked\"":"")+" /></td>");
                
                out.println("  </tr>");
                configuredStates.add(rule.getAppraisal());
            } //while
            
            List<State> validStates = sm.listValidStates();
            Collections.sort(validStates);
            Collections.reverse(validStates);
            if(!validStates.isEmpty())
            {
                for(State state:validStates)
                {
                    if( configuredStates.add(state) ) {
                        out.println("  <tr>");
                        // Columna vacía
                        out.println("   <td>&nbsp;</td>");
                        
                        // Estado
                        out.println("   <td>");
                        out.println("<a href=\"#\"  onclick=\"addNewTab('" + state.getURI() + "','" + SWBPlatform.getContextPath() + "/swbadmin/jsp/objectTab.jsp" + "','" + (state.getTitle(lang)==null?(state.getTitle()==null?"Sin título":state.getTitle()):state.getTitle(lang)) + "');return false;\" >" + (state.getTitle(lang)==null?(state.getTitle()==null?"Sin título":state.getTitle()):state.getTitle(lang)) + "</a>");
                        out.println("   </td>");
                        
                        // Grupo del estado
                        out.println("   <td>");
                        out.println((state.getStateGroup().getTitle(lang)==null?(state.getStateGroup().getTitle()==null?"Sin título":state.getStateGroup().getTitle()):state.getStateGroup().getTitle(lang)));
                        out.println("   </td>");
                        
                        // Lista de operadores
                        SWBResourceURL urlopr = paramRequest.getActionUrl();
                        urlopr.setParameter("suri", suri);
                        urlopr.setParameter("stateId", state.getId());
                        urlopr.setAction(Action_UPDT_OPER);
                        out.println("   <td>");
                        out.println("    <select name=\"operId\" onchange=\"submitUrl('" + urlopr + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"false\" hasDownArrow=\"true\" style=\"width:120px;\">");
                        out.println("     <option value=\"\"></option>");
                        for(Operation operation:operations) {
                            out.println("     <option value=\""+operation.getId()+"\">"+(operation.getTitle(lang)==null?(operation.getTitle()==null?"Sin título":operation.getTitle()):operation.getTitle(lang))+"</option>");
                        }            
                        out.println("    </select>");
                        out.println("    </td>");
                        
                        // Lista de series
                        SWBResourceURL urluinh = paramRequest.getActionUrl();
                        urluinh.setParameter("suri", suri);
                        urluinh.setParameter("stateId", state.getId());
                        urluinh.setAction(Action_UPDT_SERIES);
                        out.println("   <td>");
                        out.println("    <select name=\"ssId\" onchange=\"submitUrl('" + urluinh + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"false\" hasDownArrow=\"true\" style=\"width:120px;\">");
                        out.println("     <option value=\"\"></option>");
                        for(Series s:siblingSerieses) {
                            out.println(" <option value=\""+s.getId()+"\">"+(s.getTitle(lang)==null?(s.getTitle()==null?"Sin título":s.getTitle()):s.getTitle(lang))+"</option>");
                        }
                        out.println("    </select>");
                        out.println("   </td>");
                        
                        // Factor
                        SWBResourceURL urlfctr = paramRequest.getActionUrl();
                        urlfctr.setParameter("suri", suri);
                        urlfctr.setParameter("stateId", state.getId());
                        urlfctr.setAction(Action_UPDT_FACTOR);
                        out.println("   <td><input type=\"text\" name=\"fctr\" onchange=\"submitUrl('" + urlfctr + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.TextBox\" value=\"\" /></td>");
                        
                        // Columna vacía
                        out.println("   <td>&nbsp;</td>");
                        out.println("  </tr>");
                    }
                } //for
            } //if
            else
            {
                out.println("no hay estados asignados al indicador");
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</fieldset>");
            if(hasRules)
            {
                out.println("<fieldset>");
                SWBResourceURL urlAll = paramRequest.getActionUrl();
                urlAll.setParameter("suri", suri);
                
                urlAll.setAction(Action_ACTIVE_ALL);
                out.println("<button dojoType=\"dijit.form.Button\" onclick=\"submitUrl('" + urlAll + "',this.domNode); return false;\">" + paramRequest.getLocaleString("lblActiveAll") + "</button>");
                
                urlAll.setAction(Action_DEACTIVE_ALL);
                out.println("<button dojoType=\"dijit.form.Button\" onclick=\"submitUrl('" + urlAll + "',this.domNode); return false;\">" + paramRequest.getLocaleString("lblDeactiveAll") + "</button>");
                
                urlAll.setAction(Action_DELETE_ALL);
                out.println("<button dojoType=\"dijit.form.Button\" onclick=\"if(confirm('"+paramRequest.getLocaleString("queryRemoveAll")+"?')){submitUrl('" + urlAll + "',this.domNode);} return false;\">" + paramRequest.getLocaleString("lblRemoveAll") + "</button>");
                out.println("</fieldset>");
            }
            out.println("</div>");
        }
    }
    
    /*private void doEditDeliverable(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException, ClassCastException
    {
        response.setContentType("text/html; charset=ISO-8859-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        
        User user = paramRequest.getUser();
        final String lang = user.getLanguage();
        boolean hasRules = Boolean.FALSE;
        
        final String suri = request.getParameter("suri");
        SemanticOntology ont = SWBPlatform.getSemanticMgr().getOntology();
        SemanticObject obj = ont.getSemanticObject(suri);
        Deliverable deliverable;
        
        PrintWriter out = response.getWriter();
        
        
        SWBResourceURL url = paramRequest.getActionUrl();
        url.setAction(SWBResourceURL.Action_ADD);
        String action = paramRequest.getAction();
        if(SWBResourceURL.Action_EDIT.equalsIgnoreCase(action))
        {
            out.println("<div class=\"swbform\">");
            out.println("<fieldset>");
            out.println("<table width=\"98%\">"); 
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>"+paramRequest.getLocaleString("lblAction")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblStatus")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblStatusGroup")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblOperation")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblProgress")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblFactor")+"</th>");
            out.println("<th>"+paramRequest.getLocaleString("lblActive")+"</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            deliverable = (Deliverable)obj.getGenericInstance();
            final Initiative initiative;
            DeliverableAssignable deliverableAssig = deliverable.getDeliverableAssignable();
            if(deliverableAssig instanceof Initiative) {
                initiative = (Initiative)deliverableAssig;
            }else {
                initiative = null;
            }
            BSC bsc = initiative.getBSC();

            // Obtener la lista de operaciones válidas
            List<Operation> operations = SWBUtils.Collections.filterIterator(Operation.ClassMgr.listOperations(bsc),
                                    new GenericFilterRule<Operation>(){
                                        @Override
                                        public boolean filter(Operation o) {
                                            User user = SWBContext.getAdminUser();
                                            return !o.isValid() || !user.haveAccess(o);
                                        } 
                                    });

            // Obtener la lista de propiedades para contraparte
            SemanticClass sc = deliverable.getSemanticObject().getSemanticClass();
            Iterator<SemanticProperty> it = sc.listProperties();
            List<SemanticProperty> semPropsNumerics = new ArrayList<>();
            while(it.hasNext()) {
                SemanticProperty sp = it.next();
                if(!sp.isNumeric()) {
                    continue;
                }
                semPropsNumerics.add(sp);
            }

            // Crear el conjunto de reglas, si 
            // una regla ya está en el conjunto no se agrega de nuevo 
            HashSet<State> configuredStates = new HashSet<>();            
            GenericIterator<InitiativeEvaluationRule> rules = deliverable.listInitiativeEvaluationRules();
            List<InitiativeEvaluationRule> lrules = SWBUtils.Collections.copyIterator(rules);
            Collections.sort(lrules);
            Collections.reverse(lrules);
            //rules = lrules.iterator();
            //hasRules = rules.hasNext();
            hasRules = !lrules.isEmpty();
            //while(rules.hasNext()) {
            for(InitiativeEvaluationRule rule : lrules) {
System.out.println("\n\n1.-rule="+rule);
                //ObjectiveEvaluationRule rule = rules.next();
                out.println("  <tr>");                
                
                // Eliminar regla
                out.println("<td>");
                SWBResourceURL urlr = paramRequest.getActionUrl();
                urlr.setParameter("suri", suri);
                urlr.setParameter("sval", rule.getURI());
                urlr.setAction(SWBResourceURL.Action_REMOVE);
                out.println("<a href=\"#\" onclick=\"if(confirm('" + paramRequest.getLocaleString("queryRemove") + " " 
                        + (rule.getTitle(lang)==null?(rule.getTitle()==null?"Sin título":rule.getTitle().replaceAll("'","")):rule.getTitle(lang).replaceAll("'","")) 
                        + "?')){submitUrl('" + urlr + "',this);} else { return false;}\"><img src=\"" + SWBPlatform.getContextPath() + "/swbadmin/images/delete.gif\" border=0></a>");
                out.println("</td>");
                
                // Estado
                SWBResourceURL urlchoose = paramRequest.getRenderUrl();
                urlchoose.setParameter("suri", suri);
                urlchoose.setParameter("sval", rule.getURI());
                out.println("   <td>");
                if(rule.getAppraisal()==null) {
                    out.println("Not set");
                }else {
                    out.println("<a href=\"#\" onclick=\"addNewTab('" + rule.getAppraisal().getURI() + "','" + SWBPlatform.getContextPath() + "/swbadmin/jsp/objectTab.jsp" + "','" + (rule.getAppraisal().getTitle(lang)==null?(rule.getAppraisal().getTitle()==null?"Sin título":rule.getAppraisal().getTitle().replaceAll("'","")):rule.getAppraisal().getTitle(lang).replaceAll("'","")) + "');return false;\" >" + (rule.getAppraisal().getTitle(lang)==null?(rule.getAppraisal().getTitle()==null?"Sin título":rule.getAppraisal().getTitle().replaceAll("'","")):rule.getAppraisal().getTitle(lang).replaceAll("'","")) + "</a>");
                }
                out.println("   </td>");
                
                // Grupo del estado
                out.println("   <td>");
                if(rule.getAppraisal()==null) {
                    out.println("--");
                }else {
                    out.println((rule.getAppraisal().getStateGroup().getTitle(lang)==null?(rule.getAppraisal().getStateGroup().getTitle()==null?"Sin título":rule.getAppraisal().getStateGroup().getTitle().replaceAll("'","")):rule.getAppraisal().getStateGroup().getTitle(lang).replaceAll("'","")));
                }
                out.println("   </td>");
                
                // Lista de operadores
                SWBResourceURL urlopr = paramRequest.getActionUrl();
                urlopr.setParameter("suri", suri);
                urlopr.setParameter("sval", rule.getURI());
                urlopr.setAction(Action_UPDT_OPER);
                out.println("   <td>");
                out.println("    <select name=\"operId\" onchange=\"submitUrl('" + urlopr + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"false\" hasDownArrow=\"true\" style=\"width:120px;\">");
                out.println("     <option value=\"\"></option>");
                for(Operation operation:operations) {
                    out.println("     <option value=\""+operation.getId()+"\"");
                    out.println(operation.getId().equals(rule.getOperationId())?" selected=\"selected\"":"");
                    out.println("      >"+(operation.getTitle(lang)==null?(operation.getTitle()==null?"Sin título":operation.getTitle().replaceAll("'","")):operation.getTitle(lang).replaceAll("'",""))+"</option>");
                }            
                out.println("    </select>");
                out.println("   </td>");
                
                // Lista de propiedades
                SWBResourceURL urluinh = paramRequest.getActionUrl();
                urluinh.setParameter("suri", suri);
                urluinh.setParameter("sval", rule.getURI());
                urluinh.setAction(Action_UPDT_PROPERTY);
                out.println("   <td>");
                out.println("    <select name=\"ssId\" onchange=\"submitUrl('" + urluinh + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"false\" hasDownArrow=\"true\" style=\"width:120px;\">");
                out.println("     <option value=\"\"></option>");
                for(SemanticProperty s:semPropsNumerics) {
                    out.println("     <option value=\""+s.getEncodedURI()+"\"");
System.out.println("s.equals(rule.getAnotherProperty())="+s.equals(rule.getAnotherProperty()));
                    out.println(s.equals(rule.getAnotherProperty())?" selected=\"selected\"":"");
                    out.println("      >"+(s.getDisplayName(lang)==null?s.getDisplayName():s.getDisplayName(lang))+"</option>");
                }
                out.println("    </select>");
                out.println("   </td>");
                
                // Factor
                SWBResourceURL urlfctr = paramRequest.getActionUrl();
                urlfctr.setParameter("suri", suri);
                urlfctr.setParameter("sval", rule.getURI());
                urlfctr.setAction(Action_UPDT_FACTOR);
                out.println("   <td><input type=\"text\" name=\"fctr\" onchange=\"submitUrl('" + urlfctr + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.TextBox\" value=\""+(rule.getFactor()==null?"":rule.getFactor())+"\" /></td>");
                
                // Activo?
                SWBResourceURL urlactv = paramRequest.getActionUrl();
                urlactv.setParameter("suri", suri);
                urlactv.setParameter("sval", rule.getURI());
                urlactv.setAction(Action_UPDT_ACTIVE);
                out.println("   <td align=\"center\"><input type=\"checkbox\" name=\"act\" onchange=\"submitUrl('" + urlactv + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.CheckBox\" value=\""+rule.getId()+"\" "+(rule.isActive()?"checked=\"checked\"":"")+" /></td>");
                
                out.println("  </tr>");
                configuredStates.add(rule.getAppraisal());
            } //while
            
            List<State> validStates = initiative.listValidStates();
            Collections.sort(validStates);
            Collections.reverse(validStates);
            if(!validStates.isEmpty())
            {
                for(State state:validStates)
                {
                    if( configuredStates.add(state) ) {
                        out.println("  <tr>");
                        // Columna vacía
                        out.println("   <td>&nbsp;</td>");
                        
                        // Estado
                        out.println("   <td>");
                        out.println("<a href=\"#\"  onclick=\"addNewTab('" + state.getURI() + "','" + SWBPlatform.getContextPath() + "/swbadmin/jsp/objectTab.jsp" + "','" + (state.getTitle(lang)==null?(state.getTitle()==null?"Sin título":state.getTitle()):state.getTitle(lang)) + "');return false;\" >" + (state.getTitle(lang)==null?(state.getTitle()==null?"Sin título":state.getTitle()):state.getTitle(lang)) + "</a>");
                        out.println("   </td>");
                        
                        // Grupo del estado
                        out.println("   <td>");
                        out.println((state.getStateGroup().getTitle(lang)==null?(state.getStateGroup().getTitle()==null?"Sin título":state.getStateGroup().getTitle()):state.getStateGroup().getTitle(lang)));
                        out.println("   </td>");
                        
                        // Lista de operadores
                        SWBResourceURL urlopr = paramRequest.getActionUrl();
                        urlopr.setParameter("suri", suri);
                        urlopr.setParameter("stateId", state.getId());
                        urlopr.setAction(Action_UPDT_OPER);
                        out.println("   <td>");
                        out.println("    <select name=\"operId\" onchange=\"submitUrl('" + urlopr + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"false\" hasDownArrow=\"true\" style=\"width:120px;\">");
                        out.println("     <option value=\"\"></option>");
                        for(Operation operation:operations) {
                            out.println("     <option value=\""+operation.getId()+"\">"+(operation.getTitle(lang)==null?(operation.getTitle()==null?"Sin título":operation.getTitle()):operation.getTitle(lang))+"</option>");
                        }            
                        out.println("    </select>");
                        out.println("    </td>");
                        
                        // Lista de propiedades
                        SWBResourceURL urluinh = paramRequest.getActionUrl();
                        urluinh.setParameter("suri", suri);
                        urluinh.setParameter("stateId", state.getId());
                        //urluinh.setParameter("sval", rule.getURI());
                        urluinh.setAction(Action_UPDT_PROPERTY);
                        out.println("   <td>");
                        out.println("    <select name=\"ssId\" onchange=\"submitUrl('" + urluinh + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.FilteringSelect\" autocomplete=\"false\" hasDownArrow=\"true\" style=\"width:120px;\">");
                        out.println("     <option value=\"\"></option>");
                        for(SemanticProperty s:semPropsNumerics) {
                            out.println("     <option value=\""+s.getEncodedURI()+"\"");
                            out.println("      >"+(s.getDisplayName(lang)==null?s.getDisplayName():s.getDisplayName(lang))+"</option>");
                        }
                        out.println("    </select>");
                        out.println("   </td>");
                        
                        // Factor
                        SWBResourceURL urlfctr = paramRequest.getActionUrl();
                        urlfctr.setParameter("suri", suri);
                        urlfctr.setParameter("stateId", state.getId());
                        urlfctr.setAction(Action_UPDT_FACTOR);
                        out.println("   <td><input type=\"text\" name=\"fctr\" onchange=\"submitUrl('" + urlfctr + "&'+this.attr('name')+'='+this.attr('value'),this.domNode)\" dojoType=\"dijit.form.TextBox\" value=\"\" /></td>");
                        
                        // Columna vacía
                        out.println("   <td>&nbsp;</td>");
                        out.println("  </tr>");
                    }
                } //for
            } //if
            else
            {
                out.println("no hay estados asignados al indicador");
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</fieldset>");
            if(hasRules)
            {
                out.println("<fieldset>");
                SWBResourceURL urlAll = paramRequest.getActionUrl();
                urlAll.setParameter("suri", suri);
                
                urlAll.setAction(Action_ACTIVE_ALL);
                out.println("<button dojoType=\"dijit.form.Button\" onclick=\"submitUrl('" + urlAll + "',this.domNode); return false;\">" + paramRequest.getLocaleString("lblActiveAll") + "</button>");
                
                urlAll.setAction(Action_DEACTIVE_ALL);
                out.println("<button dojoType=\"dijit.form.Button\" onclick=\"submitUrl('" + urlAll + "',this.domNode); return false;\">" + paramRequest.getLocaleString("lblDeactiveAll") + "</button>");
                
                urlAll.setAction(Action_DELETE_ALL);
                out.println("<button dojoType=\"dijit.form.Button\" onclick=\"if(confirm('"+paramRequest.getLocaleString("queryRemoveAll")+"?')){submitUrl('" + urlAll + "',this.domNode);} return false;\">" + paramRequest.getLocaleString("lblRemoveAll") + "</button>");
                out.println("</fieldset>");
            }
            out.println("</div>");
        }
    }*/
    
    @Override
    public void processAction(HttpServletRequest request, SWBActionResponse response) throws SWBResourceException, IOException
    {
System.out.println("\n\nprocess action");
        final String suri = request.getParameter("suri");
        
//        response.setAction(SWBResourceURL.Action_EDIT);
//        response.setRenderParameter("suri", suri);
        
        SemanticOntology ont = SWBPlatform.getSemanticMgr().getOntology();
        SemanticObject obj = ont.getSemanticObject(suri);
        if(obj==null) {
            response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchSemanticElement"));
            return;
        }
        
        User user = response.getUser();
        Measurable measurable = (Measurable)obj.getGenericInstance();
        if(!user.isSigned() || !user.haveAccess(measurable)) {
            response.setRenderParameter("statmsg", response.getLocaleString("msgUnauthorizedUser"));
            return;
        }

//        if(measurable instanceof Series) {
            processObjectiveRuleAction(request, response);
//        }else if(measurable instanceof Deliverable) {
//            processInitiativeRuleAction(request, response);
//        }
        
        response.setAction(SWBResourceURL.Action_EDIT);
        response.setRenderParameter("suri", suri);

//        if(Action_UPDT_OPER.equalsIgnoreCase(action))
//        {
//            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
//            SWBModel model = (SWBModel)objSeries.getModel().getModelObject().getGenericInstance();
//            Series series = (Series)objSeries.getGenericInstance();
//            String operId = request.getParameter("operId");
//            EvaluationRule rule;
//
//            if(Operation.ClassMgr.hasOperation(operId, model)) {
//                if(objRule==null) {
//                    rule = EvaluationRule.ClassMgr.createEvaluationRule(model);
//                    rule.setTitle(rule.getId());
//                    rule.setTitle(rule.getId(), user.getLanguage());
//                    if(State.ClassMgr.hasState(request.getParameter("stateId"), SWBContext.getGlobalWebSite())) {
//                        State state = State.ClassMgr.getState(request.getParameter("stateId"), SWBContext.getGlobalWebSite());
//                        rule.setAppraisal(state);
//                        series.addEvaluationRule(rule);
//                    }
//                }else {
//                    rule = (EvaluationRule)objRule.getGenericInstance();
//                }
//                rule.setOperationId(operId);
//                response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtOperatorOk"));
//            }else {
//                response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchOperation"));
//            }
//        }
//        else if(Action_UPDT_SERIES.equalsIgnoreCase(action))
//        {
//            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
//            SWBModel model = (SWBModel)objSeries.getModel().getModelObject().getGenericInstance();
//            Series series = (Series)objSeries.getGenericInstance();
//            String siblingId = request.getParameter("ssId");
//            EvaluationRule rule;
//            
//            if(Series.ClassMgr.hasSeries(siblingId, model)) {
//                Series sibling = Series.ClassMgr.getSeries(siblingId, model);
//                if(series.getIndicator().hasSeries(sibling))
//                {
//                    if(objRule==null) {
//                        rule = EvaluationRule.ClassMgr.createEvaluationRule(model);
//                        rule.setTitle(rule.getId());
//                        rule.setTitle(rule.getId(), user.getLanguage());
//                        if(State.ClassMgr.hasState(request.getParameter("stateId"), SWBContext.getGlobalWebSite())) {
//                            State state = State.ClassMgr.getState(request.getParameter("stateId"), SWBContext.getGlobalWebSite());
//                            rule.setAppraisal(state);
//                            series.addEvaluationRule(rule);
//                        }
//                    }else {
//                        rule = (EvaluationRule)objRule.getGenericInstance();
//                    }
//                    rule.setAnotherSeries(sibling);
//                    response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtSeriesOk"));
//                }
//                else
//                {
//                    response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchSeries"));
//                }
//            }else {
//                response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchSeries"));
//            }
//        }
//        else if(Action_UPDT_FACTOR.equalsIgnoreCase(action))
//        {
//            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
//            SWBModel model = (SWBModel)objSeries.getModel().getModelObject().getGenericInstance();
//            Series series = (Series)objSeries.getGenericInstance();
//            String factor = request.getParameter("fctr")==null?"":request.getParameter("fctr");
//            EvaluationRule rule;
//            
//            if(objRule==null) {
//                rule = EvaluationRule.ClassMgr.createEvaluationRule(model);
//                rule.setTitle(rule.getId());
//                rule.setTitle(rule.getId(), user.getLanguage());
//                if(State.ClassMgr.hasState(request.getParameter("stateId"), SWBContext.getGlobalWebSite())) {
//                    State state = State.ClassMgr.getState(request.getParameter("stateId"), SWBContext.getGlobalWebSite());
//                    rule.setAppraisal(state);
//                    series.addEvaluationRule(rule);
//                }
//            }else {
//                rule = (EvaluationRule)objRule.getGenericInstance();
//            }
//            if(rule.validateFactor(factor)) {
//                rule.setFactor(factor);
//                response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtFactorOk"));
//            }else {
//                response.setRenderParameter("statmsg", response.getLocaleString("msgFactorBadFormat"));
//            }
//        }
//        else if(Action_UPDT_ACTIVE.equalsIgnoreCase(action))
//        {
//            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
//            EvaluationRule rule = (EvaluationRule)objRule.getGenericInstance();
//            Series series = (Series)objSeries.getGenericInstance();
//            if(series.hasEvaluationRule(rule)) {
//                rule.setActive(!rule.isActive());                    
//                response.setRenderParameter("statmsg", (rule.isActive()?response.getLocaleString("msgUpdtActiveOk"):response.getLocaleString("msgUpdtDeactiveOk")));
//            }else {
//                response.setRenderParameter("statmsg", response.getLocaleString("msgRemoveError"));
//            }
//        }
//        else if(Action_ACTIVE_ALL.equalsIgnoreCase(action))
//        {
//            Series series = (Series)objSeries.getGenericInstance();
//            Iterator<EvaluationRule> rules = series.listEvaluationRules();
//            while(rules.hasNext()) {
//                rules.next().setActive(Boolean.TRUE);
//            }
//            response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtAllActiveOk"));
//        }
//        else if(Action_DEACTIVE_ALL.equalsIgnoreCase(action))
//        {
//            Series series = (Series)objSeries.getGenericInstance();                
//            Iterator<EvaluationRule> rules = series.listEvaluationRules();
//            while(rules.hasNext()) {
//                rules.next().setActive(Boolean.FALSE);
//            }
//            response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtAllDeactiveOk"));
//        }
//        else if(SWBResourceURL.Action_REMOVE.equalsIgnoreCase(action))
//        {
//            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
//            EvaluationRule rule = (EvaluationRule)objRule.getGenericInstance();
//            Series series = (Series)objSeries.getGenericInstance();
//            if(series.hasEvaluationRule(rule)) {
//                series.removeEvaluationRule(rule);
//                response.setRenderParameter("statmsg", response.getLocaleString("msgRemoveOk"));
//            }else {
//                response.setRenderParameter("statmsg", response.getLocaleString("msgRemoveError"));
//            }
//        }
//        else if(Action_DELETE_ALL.equalsIgnoreCase(action))
//        {
//            Series series = (Series)objSeries.getGenericInstance();                
//            series.removeAllEvaluationRule();
//        }
    }
    
    private void processObjectiveRuleAction(HttpServletRequest request, SWBActionResponse response) throws SWBResourceException, IOException
    {
        final String action = response.getAction();
        final String suri = request.getParameter("suri");
        
        response.setAction(SWBResourceURL.Action_EDIT);
        response.setRenderParameter("suri", suri);
        
        SemanticOntology ont = SWBPlatform.getSemanticMgr().getOntology();
        SemanticObject semObj = ont.getSemanticObject(suri);
        if(semObj==null) {
            response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchSemanticElement"));
            return;
        }
        
        User user = response.getUser();
        if(!user.isSigned() || !user.haveAccess(semObj.getGenericInstance())) {
            response.setRenderParameter("statmsg", response.getLocaleString("msgUnauthorizedUser"));
            return;
        }
System.out.println("process objective action");
System.out.println("action="+action);
        if(Action_UPDT_OPER.equalsIgnoreCase(action))
        {
System.out.println("update operator");
            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
            SWBModel model = (SWBModel)semObj.getModel().getModelObject().getGenericInstance();
            Series series = (Series)semObj.getGenericInstance();
            String operId = request.getParameter("operId");
            EvaluationRule rule;

            if(Operation.ClassMgr.hasOperation(operId, model)) {
                if(objRule==null) {
                    rule = EvaluationRule.ClassMgr.createEvaluationRule(model);
                    rule.setTitle(rule.getId());
                    rule.setTitle(rule.getId(), user.getLanguage());
                    if(State.ClassMgr.hasState(request.getParameter("stateId"), SWBContext.getGlobalWebSite())) {
                        State state = State.ClassMgr.getState(request.getParameter("stateId"), SWBContext.getGlobalWebSite());
                        rule.setAppraisal(state);
                        series.addEvaluationRule(rule);
                    }
                }else {
                    rule = (EvaluationRule)objRule.getGenericInstance();
                }
                rule.setOperationId(operId);
                response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtOperatorOk"));
            }else {
                response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchOperation"));
            }
        }
        else if(Action_UPDT_SERIES.equalsIgnoreCase(action))
        {
            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
            SWBModel model = (SWBModel)semObj.getModel().getModelObject().getGenericInstance();
            Series series = (Series)semObj.getGenericInstance();
            String siblingId = request.getParameter("ssId");
            EvaluationRule rule;
            
            if(Series.ClassMgr.hasSeries(siblingId, model)) {
                Series sibling = Series.ClassMgr.getSeries(siblingId, model);
                if(series.getSm().hasSeries(sibling))
                {
                    if(objRule==null) {
                        rule = EvaluationRule.ClassMgr.createEvaluationRule(model);
                        rule.setTitle(rule.getId());
                        rule.setTitle(rule.getId(), user.getLanguage());
                        if(State.ClassMgr.hasState(request.getParameter("stateId"), SWBContext.getGlobalWebSite())) {
                            State state = State.ClassMgr.getState(request.getParameter("stateId"), SWBContext.getGlobalWebSite());
                            rule.setAppraisal(state);
                            series.addEvaluationRule(rule);
                        }
                    }else {
                        rule = (EvaluationRule)objRule.getGenericInstance();
                    }
                    rule.setAnotherSeries(sibling);
                    response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtSeriesOk"));
                }
                else
                {
                    response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchSeries"));
                }
            }else {
                response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchSeries"));
            }
        }
        else if(Action_UPDT_FACTOR.equalsIgnoreCase(action))
        {
            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
            SWBModel model = (SWBModel)semObj.getModel().getModelObject().getGenericInstance();
            Series series = (Series)semObj.getGenericInstance();
            String factor = request.getParameter("fctr")==null?"":request.getParameter("fctr");
            EvaluationRule rule;
            
            if(objRule==null) {
                rule = EvaluationRule.ClassMgr.createEvaluationRule(model);
                rule.setTitle(rule.getId());
                rule.setTitle(rule.getId(), user.getLanguage());
                if(State.ClassMgr.hasState(request.getParameter("stateId"), SWBContext.getGlobalWebSite())) {
                    State state = State.ClassMgr.getState(request.getParameter("stateId"), SWBContext.getGlobalWebSite());
                    rule.setAppraisal(state);
                    series.addEvaluationRule(rule);
                }
            }else {
                rule = (EvaluationRule)objRule.getGenericInstance();
            }
            if(rule.validateFactor(factor)) {
                rule.setFactor(factor);
                response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtFactorOk"));
            }else {
                response.setRenderParameter("statmsg", response.getLocaleString("msgFactorBadFormat"));
            }
        }
        else if(Action_UPDT_ACTIVE.equalsIgnoreCase(action))
        {
            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
            EvaluationRule rule = (EvaluationRule)objRule.getGenericInstance();
            Series series = (Series)semObj.getGenericInstance();
            if(series.hasEvaluationRule(rule)) {
                rule.setActive(!rule.isActive());                    
                response.setRenderParameter("statmsg", (rule.isActive()?response.getLocaleString("msgUpdtActiveOk"):response.getLocaleString("msgUpdtDeactiveOk")));
            }else {
                response.setRenderParameter("statmsg", response.getLocaleString("msgRemoveError"));
            }
        }
        else if(Action_ACTIVE_ALL.equalsIgnoreCase(action))
        {
            Series series = (Series)semObj.getGenericInstance();
            Iterator<EvaluationRule> rules = series.listEvaluationRules();
            while(rules.hasNext()) {
                rules.next().setActive(Boolean.TRUE);
            }
            response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtAllActiveOk"));
        }
        else if(Action_DEACTIVE_ALL.equalsIgnoreCase(action))
        {
            Series series = (Series)semObj.getGenericInstance();                
            Iterator<EvaluationRule> rules = series.listEvaluationRules();
            while(rules.hasNext()) {
                rules.next().setActive(Boolean.FALSE);
            }
            response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtAllDeactiveOk"));
        }
        else if(SWBResourceURL.Action_REMOVE.equalsIgnoreCase(action))
        {
            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
            EvaluationRule rule = (EvaluationRule)objRule.getGenericInstance();
            Series series = (Series)semObj.getGenericInstance();
            if(series.hasEvaluationRule(rule)) {
                series.removeEvaluationRule(rule);
                response.setRenderParameter("statmsg", response.getLocaleString("msgRemoveOk"));
            }else {
                response.setRenderParameter("statmsg", response.getLocaleString("msgRemoveError"));
            }
        }
        else if(Action_DELETE_ALL.equalsIgnoreCase(action))
        {
            Series series = (Series)semObj.getGenericInstance();                
            series.removeAllEvaluationRule();
        }
    }
    
    /*private void processInitiativeRuleAction(HttpServletRequest request, SWBActionResponse response) throws SWBResourceException, IOException
    {
        final String action = response.getAction();
        final String suri = request.getParameter("suri");
        
//        response.setAction(SWBResourceURL.Action_EDIT);
//        response.setRenderParameter("suri", suri);
        
        SemanticOntology ont = SWBPlatform.getSemanticMgr().getOntology();
        SemanticObject semObj = ont.getSemanticObject(suri);
//        if(semObj==null) {
//            response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchSemanticElement"));
//            return;
//        }
        
        String lang = response.getUser().getLanguage();
//        if(!user.isSigned() || !user.haveAccess(semObj.getGenericInstance())) {
//            response.setRenderParameter("statmsg", response.getLocaleString("msgUnauthorizedUser"));
//            return;
//        }

        if(Action_UPDT_OPER.equalsIgnoreCase(action))
        {
            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
            SWBModel model = (SWBModel)semObj.getModel().getModelObject().getGenericInstance();
            Deliverable deliverable = (Deliverable)semObj.getGenericInstance();
            String operId = request.getParameter("operId");
            InitiativeEvaluationRule rule;

            if(Operation.ClassMgr.hasOperation(operId, model)) {
                if(objRule==null) {
                    rule = InitiativeEvaluationRule.ClassMgr.createInitiativeEvaluationRule(model);
                    rule.setTitle(rule.getId());
                    rule.setTitle(rule.getId(), lang);
System.out.println("\n\nupdate oper...");
System.out.println("stateId= "+request.getParameter("stateId"));
                    if(State.ClassMgr.hasState(request.getParameter("stateId"), SWBContext.getGlobalWebSite())) {
                        State state = State.ClassMgr.getState(request.getParameter("stateId"), SWBContext.getGlobalWebSite());
                        rule.setAppraisal(state);
                        deliverable.addInitiativeEvaluationRule(rule);
                    }
                }else {
                    rule = (InitiativeEvaluationRule)objRule.getGenericInstance();
                }
                rule.setOperationId(operId);
                response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtOperatorOk"));
            }else {
                response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchOperation"));
            }
        }
        else if(Action_UPDT_PROPERTY.equalsIgnoreCase(action))
        {
System.out.println(" action= "+action);
            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
System.out.println("objRule= "+objRule);
            SWBModel model = (SWBModel)semObj.getModel().getModelObject().getGenericInstance();
System.out.println("model= "+model);
            Deliverable deliverable = (Deliverable)semObj.getGenericInstance();
System.out.println("deliverable= "+deliverable);
            String propertyUri = request.getParameter("ssId");
System.out.println("property Uri= "+propertyUri);
            InitiativeEvaluationRule rule;
System.out.println("property= "+SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(propertyUri));
            if(SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(propertyUri)!=null) {
                SemanticProperty semProp = SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty(propertyUri);
//System.out.println("deliverable tiene propieadad "+semProp+": "+deliverable.getSemanticObject().hasObjectProperty(semProp));
//                if(deliverable.getSemanticObject().hasObjectProperty(semProp))
//                {
                    if(objRule==null) {
System.out.println("nulo");
                        rule = InitiativeEvaluationRule.ClassMgr.createInitiativeEvaluationRule(model);
                        rule.setTitle(rule.getId());
                        rule.setTitle(rule.getId(), lang);
System.out.println("request.getParameter(stateId)="+request.getParameter("stateId"));
                        if(State.ClassMgr.hasState(request.getParameter("stateId"), SWBContext.getGlobalWebSite())) {
                            State state = State.ClassMgr.getState(request.getParameter("stateId"), SWBContext.getGlobalWebSite());
System.out.println("state="+state);
                            rule.setAppraisal(state);
                            deliverable.addInitiativeEvaluationRule(rule);
                        }
                    }else {
                        rule = (InitiativeEvaluationRule)objRule.getGenericInstance();
                    }
System.out.println("semobj de semprop="+semProp.getSemanticObject());
                    rule.setAnotherProperty(semProp.getSemanticObject());
                    response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtSeriesOk"));
//                }
//                else
//                {
//                    response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchSeries"));
//                }
            }else {
                response.setRenderParameter("statmsg", response.getLocaleString("msgNoSuchSeries"));
            }
        }
        else if(Action_UPDT_FACTOR.equalsIgnoreCase(action))
        {
            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
            SWBModel model = (SWBModel)semObj.getModel().getModelObject().getGenericInstance();
            Deliverable deliverable = (Deliverable)semObj.getGenericInstance();
            String factor = request.getParameter("fctr")==null?"":request.getParameter("fctr");
            InitiativeEvaluationRule rule;
            
            if(objRule==null) {
                rule = InitiativeEvaluationRule.ClassMgr.createInitiativeEvaluationRule(model);
                rule.setTitle(rule.getId());
                rule.setTitle(rule.getId(), lang);
                if(State.ClassMgr.hasState(request.getParameter("stateId"), SWBContext.getGlobalWebSite())) {
                    State state = State.ClassMgr.getState(request.getParameter("stateId"), SWBContext.getGlobalWebSite());
                    rule.setAppraisal(state);
                    deliverable.addInitiativeEvaluationRule(rule);
                }
            }else {
                rule = (InitiativeEvaluationRule)objRule.getGenericInstance();
            }
            if(rule.validateFactor(factor)) {
                rule.setFactor(factor);
                response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtFactorOk"));
            }else {
                response.setRenderParameter("statmsg", response.getLocaleString("msgFactorBadFormat"));
            }
        }
        else if(Action_UPDT_ACTIVE.equalsIgnoreCase(action))
        {
            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
            InitiativeEvaluationRule rule = (InitiativeEvaluationRule)objRule.getGenericInstance();
            Deliverable deliverable = (Deliverable)semObj.getGenericInstance();
            if(deliverable.hasInitiativeEvaluationRule(rule)) {
                rule.setActive(!rule.isActive());                    
                response.setRenderParameter("statmsg", (rule.isActive()?response.getLocaleString("msgUpdtActiveOk"):response.getLocaleString("msgUpdtDeactiveOk")));
            }else {
                response.setRenderParameter("statmsg", response.getLocaleString("msgRemoveError"));
            }
        }
        else if(Action_ACTIVE_ALL.equalsIgnoreCase(action))
        {
            Deliverable deliverable = (Deliverable)semObj.getGenericInstance();
            Iterator<EvaluationRule> rules = deliverable.listInitiativeEvaluationRules();
            while(rules.hasNext()) {
                rules.next().setActive(Boolean.TRUE);
            }
            response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtAllActiveOk"));
        }
        else if(Action_DEACTIVE_ALL.equalsIgnoreCase(action))
        {
            Deliverable deliverable = (Deliverable)semObj.getGenericInstance();                
            Iterator<EvaluationRule> rules = deliverable.listInitiativeEvaluationRules();
            while(rules.hasNext()) {
                rules.next().setActive(Boolean.FALSE);
            }
            response.setRenderParameter("statmsg", response.getLocaleString("msgUpdtAllDeactiveOk"));
        }
        else if(SWBResourceURL.Action_REMOVE.equalsIgnoreCase(action))
        {
            SemanticObject objRule = ont.getSemanticObject(request.getParameter("sval"));
            InitiativeEvaluationRule rule = (InitiativeEvaluationRule)objRule.getGenericInstance();
            Deliverable deliverable = (Deliverable)semObj.getGenericInstance();
            if(deliverable.hasInitiativeEvaluationRule(rule)) {
                deliverable.removeInitiativeEvaluationRule(rule);
                response.setRenderParameter("statmsg", response.getLocaleString("msgRemoveOk"));
            }else {
                response.setRenderParameter("statmsg", response.getLocaleString("msgRemoveError"));
            }
        }
        else if(Action_DELETE_ALL.equalsIgnoreCase(action))
        {
            Series series = (Series)semObj.getGenericInstance();                
            series.removeAllObjectiveEvaluationRule();
        }
    }*/
}
