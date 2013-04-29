/*
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
 */
package org.semanticwb.portal.admin.resources;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.semanticwb.Logger;
import org.semanticwb.SWBUtils;
import org.semanticwb.model.AdminAlert;
import org.semanticwb.model.FormValidateException;
import org.semanticwb.model.SWBContext;
import org.semanticwb.portal.SWBFormButton;
import org.semanticwb.portal.SWBFormMgr;
import org.semanticwb.portal.api.*;
import org.semanticwb.servlet.internal.Monitor;

/**
 *
 * @author serch
 */
public class SWBASetAlert extends GenericResource {
    private Logger log = SWBUtils.getLogger(SWBASetAlert.class);

    @Override
    public void doView(HttpServletRequest request, HttpServletResponse response, SWBParamRequest paramRequest) throws SWBResourceException, IOException {
        AdminAlert aa = AdminAlert.ClassMgr.getAdminAlert("1", SWBContext.getAdminWebSite());
        if (null==aa){
            aa = AdminAlert.ClassMgr.createAdminAlert("1", SWBContext.getAdminWebSite());
            aa.setAlertSistemStatus(false);
            aa.setAlertMailList("webbuilder@infotec.com.mx");
            aa.setAlertCPUTH(85.0f);
            aa.setAlertTimeTH(250);
            aa.setAlertPPSTH(50);
        }
        PrintWriter out=response.getWriter();
        SWBFormMgr fm = new SWBFormMgr(aa.getSemanticObject(), null, SWBFormMgr.MODE_EDIT);
        fm.setSubmitByAjax(true);
        fm.setFilterHTMLTags(false);
        fm.addButton(SWBFormButton.newSaveButton());
        fm.setType(SWBFormMgr.TYPE_DOJO);
        if("update".equals(paramRequest.getAction()))
        {
            try
            {
                fm.processForm(request);
                Monitor.setAlertParameter(aa);
            }catch(FormValidateException e){log.error(e);}
            response.sendRedirect(paramRequest.getRenderUrl().setAction(null).setParameter("saved", "true").toString());
        }else
        {
            fm.setAction(paramRequest.getRenderUrl().setAction("update").toString());
            out.print(fm.renderForm(request));
            if ("true".equals(request.getParameter("saved"))){
                out.print("<script type=\"text/javascript\">showStatus('Alarma actualizada');</script>");
            }
        }
    }

}
