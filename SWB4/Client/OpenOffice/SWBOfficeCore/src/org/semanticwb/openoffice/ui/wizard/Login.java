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
 * Login.java
 *
 * Created on 10 de diciembre de 2008, 09:56 AM
 */
package org.semanticwb.openoffice.ui.wizard;

import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;
import org.netbeans.spi.wizard.WizardPage;
import org.semanticwb.openoffice.ConfigurationListURI;
import org.semanticwb.openoffice.ui.dialogs.DialogConfigProxy;

/**
 *
 * @author  victor.lorenzana
 */
public class Login extends WizardPage
{

    ConfigurationListURI configurationListURI = new ConfigurationListURI();

    /** Creates new form Login */
    public Login()
    {
        initComponents();
        this.jComboBoxWebAddress.removeAllItems();
        for (URI uri : configurationListURI.getAddresses())
        {
            this.jComboBoxWebAddress.addItem(uri);
        }
        if (this.jComboBoxWebAddress.getSelectedItem() == null)
        {
            this.jComboBoxWebAddress.requestFocus();
        }
        else
        {
            if (this.jTextFieldClave.getText().equals(""))
            {
                this.jTextFieldClave.requestFocus();
            }
            else
            {
                this.jPassword.requestFocus();
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelPassword = new javax.swing.JLabel();
        jLabelWebAddress = new javax.swing.JLabel();
        jLabelClave = new javax.swing.JLabel();
        jTextFieldClave = new javax.swing.JTextField();
        jPassword = new javax.swing.JPasswordField();
        jComboBoxWebAddress = new javax.swing.JComboBox();
        jButtonDelete = new javax.swing.JButton();
        jButtonAvanced = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/ui/wizard/Login"); // NOI18N
        jLabelPassword.setText(bundle.getString("CONTRASEÑA_DE_ACCESO:")); // NOI18N

        jLabelWebAddress.setText(bundle.getString("DIRECCIÓN_WEB:")); // NOI18N

        jLabelClave.setText(bundle.getString("CLAVE_DE_ACCESO:")); // NOI18N

        jComboBoxWebAddress.setEditable(true);
        jComboBoxWebAddress.setAutoscrolls(true);
        jComboBoxWebAddress.setName("WebAddress"); // NOI18N
        jComboBoxWebAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxWebAddressActionPerformed(evt);
            }
        });

        jButtonDelete.setText("X"); // NOI18N
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonAvanced.setText(bundle.getString("AVANZADO")); // NOI18N
        jButtonAvanced.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAvancedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPassword)
                    .addComponent(jLabelWebAddress)
                    .addComponent(jLabelClave))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldClave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                    .addComponent(jPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBoxWebAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDelete)))
                .addGap(22, 22, 22))
            .addGroup(layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(jButtonAvanced, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(166, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelWebAddress)
                    .addComponent(jComboBoxWebAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDelete))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelClave)
                    .addComponent(jTextFieldClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPassword)
                    .addComponent(jPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButtonAvanced)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void jComboBoxWebAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxWebAddressActionPerformed
    if (this.jComboBoxWebAddress.getSelectedItem() != null)
    {
        String sUri = this.jComboBoxWebAddress.getSelectedItem().toString();
        try
        {
            URI uri = new URI(sUri);
            this.jTextFieldClave.setText(configurationListURI.getLogin(uri));
        }
        catch (URISyntaxException use)
        {
            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/ui/wizard/Login").getString("ERROR_AL_ESCRIBIR_LA_DIRECCIÓN_WEB"), getDescription(), JOptionPane.ERROR);
        }
    }
}//GEN-LAST:event_jComboBoxWebAddressActionPerformed

public static String getDescription()
    {
        return java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/ui/wizard/Login").getString("ACCESO_A_SITIO");
    }
private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
    int res = JOptionPane.showConfirmDialog(this, java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/ui/wizard/Login").getString("¿DESEA_BORRAR_ESTA_CONFIGURACIÓN_DE_CONEXIÓN?"), java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/ui/wizard/Login").getString("BORRADO_DE_CONFIGURACIÓN"), JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    if (res == JOptionPane.YES_OPTION)
    {
        URI uri = (URI) this.jComboBoxWebAddress.getSelectedItem();
        if (uri != null)
        {
            configurationListURI.removeAddress(uri);
            this.jComboBoxWebAddress.removeItem(uri);
        }
    }
}//GEN-LAST:event_jButtonDeleteActionPerformed

private void jButtonAvancedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAvancedActionPerformed
    DialogConfigProxy dialogConfigProxy = new DialogConfigProxy();
    dialogConfigProxy.setLocationRelativeTo(this);
    dialogConfigProxy.setVisible(true);
}//GEN-LAST:event_jButtonAvancedActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAvanced;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JComboBox jComboBoxWebAddress;
    private javax.swing.JLabel jLabelClave;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelWebAddress;
    private javax.swing.JPasswordField jPassword;
    private javax.swing.JTextField jTextFieldClave;
    // End of variables declaration//GEN-END:variables
}
