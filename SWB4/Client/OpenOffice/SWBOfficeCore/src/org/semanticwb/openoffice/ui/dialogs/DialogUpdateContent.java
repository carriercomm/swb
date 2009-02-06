/*
 * DialogSummaryPublish.java
 *
 * Created on 4 de junio de 2008, 11:38 AM
 */
package org.semanticwb.openoffice.ui.dialogs;

import java.awt.Cursor;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.semanticwb.openoffice.OfficeDocument;
import org.semanticwb.xmlrpc.Attachment;

/**
 *
 * @author  victor.lorenzana
 */
public class DialogUpdateContent extends javax.swing.JDialog
{

    private boolean updated = false;
    private String workspaceid,  contentid;
    private OfficeDocument document;

    class Update extends Thread
    {

        File zipFile;
        JDialog dialog;
        public Update(File zipFile,JDialog dialog)
        {
            this.zipFile = zipFile;
            this.dialog=dialog;
        }

        @Override
        public void run()
        {
            try
            {                
                jLabel1.setText("Enviando archivo de publicación "+zipFile.getName());
                jLabel1.repaint();
                String name = document.getLocalPath().getName().replace(document.getDefaultExtension(), document.getPublicationExtension());
                document.getOfficeDocumentProxy().updateContent(workspaceid, contentid, name);
                jProgressBar.setValue(2);
                jLabel1.setText("Actualización terminada");
                //summaryPublish1.setVisible(true);
                summaryPublish1.loadVersions(contentid, workspaceid);
                jButtonUpdate.setEnabled(false);
                JOptionPane.showMessageDialog(dialog, "¡Contenido actualizado!",dialog.getTitle(),JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage(),"Actualización de contenido",JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
            }
            finally
            {
                if (zipFile != null && zipFile.exists())
                {
                    zipFile.delete();
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    /** Creates new form DialogSummaryPublish */
    public DialogUpdateContent(java.awt.Frame parent, boolean modal, String wokspaceid, String contentid, OfficeDocument document)
    {
        super(parent, modal);
        initComponents();
        this.workspaceid = wokspaceid;
        this.contentid = contentid;
        //summaryPublish1.setVisible(false);
        summaryPublish1.loadVersions(contentid, wokspaceid);
        this.document = document;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jProgressBar = new javax.swing.JProgressBar();
        jButtonUpdate = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        summaryPublish1 = new org.semanticwb.openoffice.ui.wizard.SummaryPublish();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Actualizacón de contenido");
        setLocationByPlatform(true);
        setModal(true);

        jPanel1.setPreferredSize(new java.awt.Dimension(350, 50));

        jButtonClose.setText("Cerrar");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jButtonUpdate.setText("Actualizar");
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        jLabel1.setText("Selecione la opción de actualizar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonClose)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonUpdate))
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonUpdate)
                        .addComponent(jButtonClose))
                    .addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        summaryPublish1.setPreferredSize(new java.awt.Dimension(400, 250));
        getContentPane().add(summaryPublish1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButtonCloseActionPerformed

private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
    if (!updated)
    {
        File zipFile = null;
        try
        {

            jProgressBar.setMaximum(2);
            this.jLabel1.setText("Creando archivo para publicación ...");
            jLabel1.repaint();
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            jProgressBar.setValue(0);
            zipFile = document.createZipFile();
            this.jLabel1.setText("Archivo de publicación creado");
            jLabel1.repaint();
            jProgressBar.setValue(1);
            document.getOfficeDocumentProxy().addAttachment(new Attachment(zipFile, zipFile.getName()));            
            Update up = new Update(zipFile,this);
            up.start();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(),this.getTitle(),JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
        }

    }

    updated = true;
}//GEN-LAST:event_jButtonUpdateActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar;
    private org.semanticwb.openoffice.ui.wizard.SummaryPublish summaryPublish1;
    // End of variables declaration//GEN-END:variables
}
