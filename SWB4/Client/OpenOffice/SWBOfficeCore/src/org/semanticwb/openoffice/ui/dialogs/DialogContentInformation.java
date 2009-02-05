/*
 * DialogContentInformation.java
 *
 * Created on 4 de junio de 2008, 10:09 AM
 */
package org.semanticwb.openoffice.ui.dialogs;

import com.sun.star.deployment.VersionException;
import java.awt.Component;
import java.awt.Cursor;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.semanticwb.office.interfaces.PortletInfo;
import org.semanticwb.office.interfaces.VersionInfo;
import org.semanticwb.openoffice.ChangeCategoryResultProducer;
import org.semanticwb.openoffice.OfficeApplication;
import org.semanticwb.openoffice.OfficeDocument;
import org.semanticwb.openoffice.ui.wizard.SelectCategory;

/**
 *
 * @author  victor.lorenzana
 */
public class DialogContentInformation extends javax.swing.JDialog
{

    private String contentId,  repository;
    private OfficeDocument document;

    /** Creates new form DialogContentInformation */
    public DialogContentInformation(java.awt.Frame parent, boolean modal, String contentId, String repository, OfficeDocument document)
    {
        super(parent, modal);
        initComponents();
        this.contentId = contentId;
        this.repository = repository;
        this.document = document;
        TableColumn column = this.jTablePages.getColumnModel().getColumn(4);
        column.setCellEditor(new VersionEditor(repository, contentId));

        try
        {
            this.jTextFieldTitle.setText(OfficeApplication.getOfficeDocumentProxy().getTitle(repository, contentId));
            this.jTextAreaDescription.setText(OfficeApplication.getOfficeDocumentProxy().getDescription(repository, contentId));
            String date = OfficeApplication.iso8601dateFormat.format(OfficeApplication.getOfficeDocumentProxy().getLasUpdate(repository, contentId));
            this.jLabel1DisplayDateOfModification.setText(date);
            jLabelCategoryName.setText(OfficeApplication.getOfficeDocumentProxy().getCategory(repository, contentId));
            loadVersions(contentId, repository);
            loadPorlets(contentId, repository);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadVersions(String contentId, String repositoryName)
    {
        try
        {
            DefaultTableModel model = (DefaultTableModel) jTableSummary1.getModel();
            int rows = model.getRowCount();
            for (int i = 1; i <= rows; i++)
            {
                model.removeRow(0);
            }
            for (VersionInfo versionInfo : OfficeApplication.getOfficeDocumentProxy().getVersions(repositoryName, contentId))
            {
                String date = OfficeApplication.iso8601dateFormat.format(versionInfo.created);
                String[] rowData =
                {
                    versionInfo.nameOfVersion, date, versionInfo.user
                };
                model.addRow(rowData);
            }
        }
        catch (Exception e)
        {
        }

    }

    private void loadPorlets(String contentId, String repositoryName)
    {
        DefaultTableModel model = (DefaultTableModel) jTablePages.getModel();
        int rows = model.getRowCount();
        for (int i = 1; i <= rows; i++)
        {
            model.removeRow(0);
        }
        try
        {
            for (PortletInfo portletInfo : OfficeApplication.getOfficeDocumentProxy().listPortlets(repositoryName, contentId))
            {
                Version objVersion=new Version();
                objVersion.id=portletInfo.version;
                objVersion.title=portletInfo.version;
                if (objVersion.id.equals("*"))
                {
                    objVersion.title = "Mostrar la última versión";
                }
                Object[] rowData =
                {
                    portletInfo, portletInfo.page.site.title, portletInfo.page.title, portletInfo.active, objVersion
                };
                model.addRow(rowData);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelButtons = new javax.swing.JPanel();
        jButtonAccept = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelContentInformation = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldTitle = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescription = new javax.swing.JTextArea();
        jLabel1DisplayDateOfModification = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabelCategoryName = new javax.swing.JLabel();
        jButtonChangeCategory = new javax.swing.JButton();
        jPanelPublishInformation = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonPublish = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButtonViewPage = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePages = new javax.swing.JTable();
        jPanelVersions = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableSummary1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Información del Contenido");
        setModal(true);
        setResizable(false);

        jPanelButtons.setPreferredSize(new java.awt.Dimension(100, 50));

        jButtonAccept.setText("Aceptar");
        jButtonAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAcceptActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cerrar");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelButtonsLayout = new javax.swing.GroupLayout(jPanelButtons);
        jPanelButtons.setLayout(jPanelButtonsLayout);
        jPanelButtonsLayout.setHorizontalGroup(
            jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelButtonsLayout.createSequentialGroup()
                .addContainerGap(309, Short.MAX_VALUE)
                .addComponent(jButtonCancel)
                .addGap(18, 18, 18)
                .addComponent(jButtonAccept)
                .addContainerGap())
        );
        jPanelButtonsLayout.setVerticalGroup(
            jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelButtonsLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAccept)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        getContentPane().add(jPanelButtons, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(471, 250));

        jLabel1.setText("Título:");

        jLabel2.setText("Descripción:");

        jLabel3.setText("Fecha de última modificación:");

        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescription);

        jLabel1DisplayDateOfModification.setText("18 de Diciembre de 1973 20:30");

        jLabel4.setText("Categoria");

        jLabelCategoryName.setText("Categoria");

        jButtonChangeCategory.setText("...");
        jButtonChangeCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangeCategoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelContentInformationLayout = new javax.swing.GroupLayout(jPanelContentInformation);
        jPanelContentInformation.setLayout(jPanelContentInformationLayout);
        jPanelContentInformationLayout.setHorizontalGroup(
            jPanelContentInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContentInformationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelContentInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(30, 30, 30)
                .addGroup(jPanelContentInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jTextFieldTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jLabel1DisplayDateOfModification, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelContentInformationLayout.createSequentialGroup()
                        .addComponent(jLabelCategoryName, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonChangeCategory)))
                .addContainerGap())
        );
        jPanelContentInformationLayout.setVerticalGroup(
            jPanelContentInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContentInformationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelContentInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelContentInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(jPanelContentInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1DisplayDateOfModification))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelContentInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabelCategoryName)
                    .addComponent(jButtonChangeCategory))
                .addGap(71, 71, 71))
        );

        jTabbedPane1.addTab("Información del Contenido", jPanelContentInformation);

        jPanelPublishInformation.setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator1);

        jButtonPublish.setText("Publicar en una página");
        jButtonPublish.setFocusable(false);
        jButtonPublish.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonPublish.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonPublish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPublishActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonPublish);
        jToolBar1.add(jSeparator3);

        jButtonViewPage.setText("Ver página");
        jButtonViewPage.setEnabled(false);
        jButtonViewPage.setFocusable(false);
        jButtonViewPage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonViewPage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButtonViewPage);
        jToolBar1.add(jSeparator4);

        jButton5.setText("Eliminar");
        jButton5.setEnabled(false);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton5);

        jPanelPublishInformation.add(jToolBar1, java.awt.BorderLayout.NORTH);

        jTablePages.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Sitio", "Página", "Activo", "Version"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablePages.setColumnSelectionAllowed(true);
        jScrollPane2.setViewportView(jTablePages);
        jTablePages.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jPanelPublishInformation.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Información de Publicación", jPanelPublishInformation);

        jPanelVersions.setLayout(new java.awt.BorderLayout());

        jTableSummary1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Versión", "Fecha de creación", "Creador"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableSummary1.setColumnSelectionAllowed(true);
        jTableSummary1.setFocusable(false);
        jTableSummary1.setRowSelectionAllowed(false);
        jTableSummary1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(jTableSummary1);
        jTableSummary1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jPanelVersions.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Versiones del contenido", jPanelVersions);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonCancelActionPerformed
    {//GEN-HEADEREND:event_jButtonCancelActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonPublishActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonPublishActionPerformed
    {//GEN-HEADEREND:event_jButtonPublishActionPerformed
        document.publish();
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        loadPorlets(contentId, repository);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jButtonPublishActionPerformed

    private void jButtonChangeCategoryActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonChangeCategoryActionPerformed
    {//GEN-HEADEREND:event_jButtonChangeCategoryActionPerformed
        ChangeCategoryResultProducer resultProducer = new ChangeCategoryResultProducer(this.contentId, this.repository);
        WizardPage[] pages =
        {
            new SelectCategory(this.repository)
        };
        Wizard wiz = WizardPage.createWizard("Cambiar contenido de categoria", pages, resultProducer);
        wiz.show();
        try
        {
            String category = OfficeApplication.getOfficeDocumentProxy().getCategory(repository, contentId);
            jLabelCategoryName.setText(category);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonChangeCategoryActionPerformed

    private void jButtonAcceptActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonAcceptActionPerformed
    {//GEN-HEADEREND:event_jButtonAcceptActionPerformed

        //update porlets
        DefaultTableModel model = (DefaultTableModel) jTablePages.getModel();
        int rows = model.getRowCount();
        for (int i = 1; i <= rows; i++)
        {
            Object originalValue=model.getValueAt(i, 0);
            if(originalValue instanceof PortletInfo)
            {
                PortletInfo portletInfo=(PortletInfo)originalValue;
                Version newVersion=(Version)model.getValueAt(i, 4);
                if(!newVersion.id.equals(portletInfo.version))
                {
                    try
                    {
                        OfficeApplication.getOfficeDocumentProxy().changeVersionPorlet(portletInfo,newVersion.id);
                    }
                    catch(Exception e)
                    {

                    }
                }
            }
        }
        this.setVisible(true);

    }//GEN-LAST:event_jButtonAcceptActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonAccept;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonChangeCategory;
    private javax.swing.JButton jButtonPublish;
    private javax.swing.JButton jButtonViewPage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel1DisplayDateOfModification;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelCategoryName;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelContentInformation;
    private javax.swing.JPanel jPanelPublishInformation;
    private javax.swing.JPanel jPanelVersions;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTablePages;
    private javax.swing.JTable jTableSummary1;
    private javax.swing.JTextArea jTextAreaDescription;
    private javax.swing.JTextField jTextFieldTitle;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}

class VersionEditor extends AbstractCellEditor implements TableCellEditor
{

    private String repositoryName,  contentId;
    private JComboBox combo = new JComboBox();

    public VersionEditor(String repositoryName, String contentId)
    {
        this.repositoryName = repositoryName;
        this.contentId = contentId;
        Version objVersion=new Version();
        objVersion.id="*";
        objVersion.title="Mostrar la última versión";
        combo.addItem(objVersion);
        try
        {
            for (VersionInfo versionInfo : OfficeApplication.getOfficeDocumentProxy().getVersions(repositoryName, contentId))
            {
                combo.addItem(versionInfo);
            }
        }
        catch (Exception e)
        {
        }
    }

    public Object getCellEditorValue()
    {
        return combo.getSelectedItem();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        combo.setSelectedItem(value);
        return combo;
    }
}
class Version
{

    @Override
    public String toString()
    {
        return title;
    }
    String id;
    String title;

}