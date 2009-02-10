/*
 * DialogConfigProxy.java
 *
 * Created on 3 de junio de 2008, 07:23 PM
 */
package org.semanticwb.openoffice.ui.dialogs;

import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import org.semanticwb.openoffice.Configuration;
import org.semanticwb.openoffice.util.FixedLengthPlainDocument;
import org.semanticwb.openoffice.util.NumericPlainDocument;
import static org.semanticwb.openoffice.Configuration.PROXY_PORT;
import static org.semanticwb.openoffice.Configuration.PROXY_SERVER;
/**
 *
 * @author  victor.lorenzana
 */
public class DialogConfigProxy extends javax.swing.JDialog
{
    
    Configuration configuration=new Configuration();

    /** Creates new form DialogConfigProxy */
    public DialogConfigProxy(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        jTextFieldPort.setDocument(new NumericPlainDocument(4, new DecimalFormat("####")));
        jTextFieldServer.setDocument(new FixedLengthPlainDocument(255));
        String proxyServer=configuration.get(PROXY_SERVER);
        String proxyPort=configuration.get(PROXY_PORT);
        if(proxyServer==null)
        {
            proxyServer="";
        }
        if(proxyPort==null)
        {
            proxyServer="";
        }            
        if(proxyServer.equals("") || proxyPort.equals(""))
        {
            proxyServer="";
            proxyServer="";
            this.jCheckBoxUsesServerProxy.setSelected(false);
            jTextFieldServer.setEnabled(false);
            jTextFieldPort.setEnabled(false);
        }
        else
        {
            this.jCheckBoxUsesServerProxy.setSelected(true);
            jTextFieldServer.setEnabled(true);
            jTextFieldPort.setEnabled(true);
        }
        jTextFieldServer.setText(proxyServer);
        jTextFieldPort.setText(proxyPort);
    }

    public boolean usesProxyServer()
    {
        return this.jCheckBoxUsesServerProxy.isSelected();
    }

    public void setUsesProxyServer(boolean usesProxyServer)
    {
        this.jCheckBoxUsesServerProxy.setSelected(usesProxyServer);
    }

    public void setServerProxy(String serverProxy)
    {
        this.jTextFieldServer.setText(serverProxy);
    }
    public String getServerProxy()
    {
        return this.jTextFieldServer.getText();
    }
    
    public int getProxyPort()
    {
        return Integer.parseInt(this.jTextFieldPort.getText());
    }

    public void setProxyPort(int proxyPort)
    {
        this.jTextFieldPort.setText(String.valueOf(proxyPort));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxUsesServerProxy = new javax.swing.JCheckBox();
        jPanelProxy = new javax.swing.JPanel();
        jLabelServerProxy = new javax.swing.JLabel();
        jLabelPort = new javax.swing.JLabel();
        jTextFieldServer = new javax.swing.JTextField();
        jTextFieldPort = new javax.swing.JTextField();
        jButtonAccept = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuración Proxy");

        jCheckBoxUsesServerProxy.setText("Usa Servidor Proxy ");
        jCheckBoxUsesServerProxy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUsesServerProxyActionPerformed(evt);
            }
        });

        jPanelProxy.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configuración", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP));

        jLabelServerProxy.setText("Servidor Proxy:");

        jLabelPort.setText("Puerto");

        jTextFieldServer.setEnabled(false);

        jTextFieldPort.setText("8080");
        jTextFieldPort.setEnabled(false);

        javax.swing.GroupLayout jPanelProxyLayout = new javax.swing.GroupLayout(jPanelProxy);
        jPanelProxy.setLayout(jPanelProxyLayout);
        jPanelProxyLayout.setHorizontalGroup(
            jPanelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProxyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProxyLayout.createSequentialGroup()
                        .addComponent(jLabelServerProxy)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldServer, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
                    .addGroup(jPanelProxyLayout.createSequentialGroup()
                        .addComponent(jLabelPort)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 238, Short.MAX_VALUE)
                        .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelProxyLayout.setVerticalGroup(
            jPanelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProxyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelServerProxy)
                    .addComponent(jTextFieldServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelProxyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPort)
                    .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jButtonAccept.setText("Aceptar");
        jButtonAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAcceptActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancelar");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelProxy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBoxUsesServerProxy)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancel)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonAccept, jButtonCancel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxUsesServerProxy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelProxy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonAccept, jButtonCancel});

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAcceptActionPerformed
        if (this.jCheckBoxUsesServerProxy.isSelected())
        {
            if (this.jTextFieldServer.getText().trim().equals(""))
            {
                JOptionPane.showMessageDialog(null, "¡Debe indicar el Servidor Proxy!", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                this.jTextFieldPort.requestFocus();                
            }
            else if (this.jTextFieldPort.getText().trim().equals(""))
            {
                JOptionPane.showMessageDialog(null, "¡Debe indicar el puerto del Servidor Proxy!", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                this.jTextFieldPort.requestFocus();                
            }
            else
            {   
                configuration.add(PROXY_SERVER,this.jTextFieldServer.getText().trim());
                configuration.add(PROXY_PORT,this.jTextFieldPort.getText().trim());
                this.setVisible(false);
            }
        } 
        else
        {
            configuration.add(PROXY_SERVER,"");
            configuration.add(PROXY_PORT,"");
            this.setVisible(false);
        }
    }//GEN-LAST:event_jButtonAcceptActionPerformed

    private void jCheckBoxUsesServerProxyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUsesServerProxyActionPerformed
        if(this.jCheckBoxUsesServerProxy.isSelected())
        {
            jTextFieldPort.setEnabled(true);
            jTextFieldServer.setEnabled(true);
        }
        else
        {
            jTextFieldPort.setEnabled(false);
            jTextFieldServer.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBoxUsesServerProxyActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAccept;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JCheckBox jCheckBoxUsesServerProxy;
    private javax.swing.JLabel jLabelPort;
    private javax.swing.JLabel jLabelServerProxy;
    private javax.swing.JPanel jPanelProxy;
    private javax.swing.JTextField jTextFieldPort;
    private javax.swing.JTextField jTextFieldServer;
    // End of variables declaration//GEN-END:variables
}
