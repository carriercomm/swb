/*
 * DialogLogin.java
 *
 * Created on 3 de junio de 2008, 10:28 AM
 */
package org.semanticwb.openoffice.ui.dialogs;

import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.semanticwb.openoffice.*;

/**
 *
 * @author  victor.lorenzana
 */
public class DialogLogin extends javax.swing.JDialog
{

    private int numTry = 0;
    private boolean canceled = true;
    private URI webAddress;
    private String login,  password;
    ConfigurationListURI configurationListURI = new ConfigurationListURI();

    /** Creates new form DialogLogin */
    public DialogLogin(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
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

    public boolean isCanceled()
    {
        return canceled;
    }

    public String getLogin()
    {
        return login;
    }

    public String getPassword()
    {
        return password;
    }

    public URI getWebAddress()
    {
        return webAddress;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonCancel = new javax.swing.JButton();
        jButtonAccept = new javax.swing.JButton();
        jLabelWebAddress = new javax.swing.JLabel();
        jComboBoxWebAddress = new javax.swing.JComboBox();
        jButtonAvanced = new javax.swing.JButton();
        jLabelClave = new javax.swing.JLabel();
        jTextFieldClave = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jPassword = new javax.swing.JPasswordField();
        jButtonDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Acceso a servicio de publicación");
        setLocationByPlatform(true);
        setModal(true);
        setResizable(false);

        jButtonCancel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonCancel.setText("Cancelar");
        jButtonCancel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonCancel.setContentAreaFilled(false);
        jButtonCancel.setNextFocusableComponent(jButtonAvanced);
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonAccept.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonAccept.setText("Aceptar");
        jButtonAccept.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonAccept.setContentAreaFilled(false);
        jButtonAccept.setNextFocusableComponent(jButtonCancel);
        jButtonAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAcceptActionPerformed(evt);
            }
        });

        jLabelWebAddress.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelWebAddress.setText("Dirección Web:");

        jComboBoxWebAddress.setEditable(true);
        jComboBoxWebAddress.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComboBoxWebAddress.setAutoscrolls(true);
        jComboBoxWebAddress.setName("WebAddress"); // NOI18N
        jComboBoxWebAddress.setNextFocusableComponent(jTextFieldClave);
        jComboBoxWebAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxWebAddressActionPerformed(evt);
            }
        });

        jButtonAvanced.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonAvanced.setText("Avanzado");
        jButtonAvanced.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonAvanced.setContentAreaFilled(false);
        jButtonAvanced.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAvancedActionPerformed(evt);
            }
        });

        jLabelClave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelClave.setText("Clave de acceso:");

        jTextFieldClave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextFieldClave.setNextFocusableComponent(jPassword);

        jLabelPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelPassword.setText("Contraseña:");

        jPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPassword.setNextFocusableComponent(jButtonAccept);

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/semanticwb/openoffice/ui/wizard/delete32.png"))); // NOI18N
        jButtonDelete.setToolTipText("Borrar conexión");
        jButtonDelete.setBorderPainted(false);
        jButtonDelete.setContentAreaFilled(false);
        jButtonDelete.setEnabled(false);
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelPassword)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(9, 9, 9)
                                    .addComponent(jLabelWebAddress))
                                .addComponent(jLabelClave)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldClave)
                            .addComponent(jPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                            .addComponent(jComboBoxWebAddress, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAvanced, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonAvanced, jButtonCancel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxWebAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDelete)
                    .addComponent(jLabelWebAddress))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelClave))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPassword)
                    .addComponent(jPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAvanced)
                    .addComponent(jButtonAccept, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(91, 91, 91))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonAvanced, jButtonCancel});

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jButtonAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAcceptActionPerformed

        numTry++;
        if (numTry < 3)
        {
            if (this.jComboBoxWebAddress.getSelectedItem() != null)
            {
                String sUri = this.jComboBoxWebAddress.getSelectedItem().toString();
                try
                {
                    URI uri = new URI(sUri);
                    if (this.jTextFieldClave.getText().isEmpty())
                    {
                        JOptionPane.showMessageDialog(this, "Debe indicar la clave de acceso", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                        this.jTextFieldClave.requestFocus();
                        return;
                    }
                    if (this.jPassword.getPassword().length == 0)
                    {
                        JOptionPane.showMessageDialog(this, "Debe indicar la contraseña de acceso", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                        this.jPassword.requestFocus();
                        return;
                    }
                    configurationListURI.addUserConfiguration(uri, this.jTextFieldClave.getText());
                    this.webAddress = uri;
                    this.login = this.jTextFieldClave.getText();
                    this.password = new String(this.jPassword.getPassword());
                    this.setVisible(false);
                    this.canceled = false;
                }
                catch (URISyntaxException use)
                {
                    JOptionPane.showMessageDialog(null, "La dirección Web no es válida", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    this.jComboBoxWebAddress.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Debe indicar una dirección Web", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                this.jComboBoxWebAddress.requestFocus();
            }

        }
        else
        {
            this.setVisible(false);
            this.canceled = true;
        }
    // TODO: Agregar logica de acceso
    }//GEN-LAST:event_jButtonAcceptActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.setVisible(false);
        this.canceled = true;
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonAvancedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAvancedActionPerformed
        DialogConfigProxy dialogConfigProxy = new DialogConfigProxy(new JFrame(), true);
        dialogConfigProxy.setLocationRelativeTo(this);
        dialogConfigProxy.setVisible(true);
    }//GEN-LAST:event_jButtonAvancedActionPerformed

    private void jComboBoxWebAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxWebAddressActionPerformed
        this.jButtonDelete.setEnabled(false);
        if (this.jComboBoxWebAddress.getSelectedItem() != null)
        {
            this.jButtonDelete.setEnabled(true);
            String sUri = this.jComboBoxWebAddress.getSelectedItem().toString();
            try
            {
                URI uri = new URI(sUri);
                this.jTextFieldClave.setText(configurationListURI.getLogin(uri));
            }
            catch (URISyntaxException use)
            {
                JOptionPane.showMessageDialog(this, "Error al escribir la dirección web", this.getTitle(), JOptionPane.ERROR);
            }
        }
    }//GEN-LAST:event_jComboBoxWebAddressActionPerformed

private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed

    URI uri = (URI) this.jComboBoxWebAddress.getSelectedItem();
    if (uri != null)
    {
        int res = JOptionPane.showConfirmDialog(this, "¿Desea borrar esta configuración de conexión?", "Borrado de configuración", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION)
        {
            configurationListURI.removeAddress(uri);
            this.jComboBoxWebAddress.removeItem(uri);

        }
    }
}//GEN-LAST:event_jButtonDeleteActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAccept;
    private javax.swing.JButton jButtonAvanced;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JComboBox jComboBoxWebAddress;
    private javax.swing.JLabel jLabelClave;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelWebAddress;
    private javax.swing.JPasswordField jPassword;
    private javax.swing.JTextField jTextFieldClave;
    // End of variables declaration//GEN-END:variables
}
