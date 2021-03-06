/**  
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
 **/
/*
 * SelectPage.java
 *
 * Created on 26 de diciembre de 2008, 07:11 PM
 */
package org.semanticwb.openoffice.ui.wizard;

import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPanelNavResult;
import org.semanticwb.openoffice.components.NodeEvent;
import org.semanticwb.openoffice.components.SelectWebPageComponent;
import org.semanticwb.openoffice.components.WebPage;

/**
 *
 * @author  victor.lorenzana
 */
public abstract class SelectPage extends WizardPage implements NodeEvent
{

    private static final String NL = "\r\n";
    public static final String WEBPAGE = "WEBPAGE";
    String siteId;    
    protected final SelectWebPageComponent selectWebPageComponent;
    /** Creates new form SelectPage */
    public SelectPage(String siteid,boolean showTool)
    {
        initComponents();
        this.siteId = siteid;
        selectWebPageComponent=new SelectWebPageComponent(showTool, siteid);
        this.add(selectWebPageComponent);
        selectWebPageComponent.init();
        selectWebPageComponent.addAddNodeListener(this);
    }
    
    public WebPage getSelectedWebPage()
    {
        return selectWebPageComponent.getSelectedWebPage();
    }

    @Override
    public WizardPanelNavResult allowFinish(String stepName, Map map, Wizard wizard)
    {
        return this.allowNext(stepName, map, wizard);
    }

    @Override
    public WizardPanelNavResult allowNext(String stepName, Map map, Wizard wizard)
    {
        WizardPanelNavResult res = WizardPanelNavResult.PROCEED;
        if (map.get(WEBPAGE) == null)
        {
            res = WizardPanelNavResult.REMAIN_ON_PAGE;
            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/ui/wizard/SelectPage").getString("¡DEBE_INDICAR_UNA_PÁGINA_WEB!"), getDescription(), JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
            //this.jTreeSite.requestFocus();
        }
        return res;
    }

    public void selectNode(DefaultMutableTreeNode node)
    {
        if(node instanceof WebPage)
        {
            this.getWizardDataMap().put(WEBPAGE, node);
        }
    }

    public abstract void addNode(DefaultMutableTreeNode node);
    
    public static String getDescription()
    {
        return java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/ui/wizard/SelectPage").getString("SELECCIONAR_PÁGINA");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(500, 322));
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
