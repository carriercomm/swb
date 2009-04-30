/*
 * DialogContentPublicationInformation.java
 *
 * Created on 29 de diciembre de 2008, 04:18 PM
 */
package org.semanticwb.openoffice.ui.dialogs;

import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.semanticwb.office.interfaces.CalendarInfo;
import org.semanticwb.office.interfaces.ResourceInfo;
import org.semanticwb.office.interfaces.PropertyInfo;
import org.semanticwb.office.interfaces.VersionInfo;
import org.semanticwb.openoffice.OfficeApplication;
import org.semanticwb.openoffice.components.PanelPropertyEditor;
import org.semanticwb.openoffice.ui.icons.ImageLoader;

/**
 *
 * @author  victor.lorenzana
 */
public class DialogEditResource extends javax.swing.JDialog
{

    private PanelPropertyEditor panelPropertyEditor1 = new PanelPropertyEditor();
    private String repositoryName,  contentID;
    private ResourceInfo pageInformation;
    public boolean isCancel = true;
    ArrayList<CalendarInfo> added = new ArrayList<CalendarInfo>();

    /** Creates new form DialogContentPublicationInformation */
    public DialogEditResource(ResourceInfo pageInformation, String repositoryName, String contentID)
    {
        super((Frame) null, ModalityType.TOOLKIT_MODAL);
        initComponents();
        this.jPanelInformation.add(panelPropertyEditor1);
        this.setIconImage(ImageLoader.images.get("semius").getImage());
        this.setModal(true);
        this.pageInformation = pageInformation;
        this.repositoryName = repositoryName;
        this.contentID = contentID;
        this.jTextFieldTitle.setText(pageInformation.title);
        this.jTextAreaDescription.setText(pageInformation.description);
        this.jCheckBoxActive.setSelected(pageInformation.active);
        this.jLabelSite.setText(pageInformation.page.site.title);
        this.jLabelPage.setText(pageInformation.page.title);
        loadProperties();
        loadCalendars();
        setLocationRelativeTo(null);

        jButtonSendToAuthorize.setVisible(false);
        try
        {
            if (OfficeApplication.getOfficeDocumentProxy().needsSendToPublish(pageInformation))
            {
                jButtonSendToAuthorize.setVisible(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.jTableScheduler.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {

            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                jButtonDeleteScheduler.setEnabled(false);
                jButtonEditEcheduler.setEnabled(false);
                if (e.getFirstIndex() != -1)
                {
                    jButtonDeleteScheduler.setEnabled(true);
                    jButtonEditEcheduler.setEnabled(true);
                }
            }
        });

        VersionInfo info = new VersionInfo();
        info.nameOfVersion = "*";
        jComboBoxVersion.setEditable(false);
        jComboBoxVersion.addItem(info);
        try
        {
            for (VersionInfo versionInfo : OfficeApplication.getOfficeDocumentProxy().getVersions(repositoryName, contentID))
            {
                jComboBoxVersion.addItem(versionInfo);
            }
            VersionInfo selected = new VersionInfo();
            selected.nameOfVersion = pageInformation.version;
            jComboBoxVersion.setSelectedItem(selected);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if (OfficeApplication.getOfficeDocumentProxy().needsSendToPublish(pageInformation))
            {
                this.jCheckBoxActive.setToolTipText("El contenido debe ser autorizado antes de activarse");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if (OfficeApplication.getOfficeDocumentProxy().isInFlow(pageInformation))
            {
                this.jCheckBoxActive.setToolTipText("El contenido esta en proceso de autorización, debe ser autorizado antes de activarse");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void loadCalendars()
    {
        DefaultTableModel model = (DefaultTableModel) jTableScheduler.getModel();
        int rows = model.getRowCount();
        for (int i = 1; i <= rows; i++)
        {
            model.removeRow(0);
        }
        try
        {
            for (CalendarInfo info : OfficeApplication.getOfficeDocumentProxy().getCalendars(pageInformation))
            {
                Object[] data =
                {
                    info, info.active
                };
                model.addRow(data);
            }
            for (CalendarInfo info : added)
            {
                Object[] data =
                {
                    info, info.active
                };
                model.addRow(data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void loadProperties()
    {
        try
        {
            HashMap<PropertyInfo, Object> properties = new HashMap<PropertyInfo, Object>();
            for (PropertyInfo info : OfficeApplication.getOfficeDocumentProxy().getResourceProperties(repositoryName, contentID))
            {
                String value = OfficeApplication.getOfficeDocumentProxy().getViewPropertyValue(pageInformation, info);
                properties.put(info, value);
            }
            this.panelPropertyEditor1.loadProperties(properties);
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanelOptions = new javax.swing.JPanel();
        jButtonSendToAuthorize = new javax.swing.JButton();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldTitle = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaDescription = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jCheckBoxActive = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jLabelSite = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelPage = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBoxVersion = new javax.swing.JComboBox();
        jPanelInformation = new javax.swing.JPanel();
        jPanelSchedule = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableScheduler = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonAddCalendar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonEditEcheduler = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonDeleteScheduler = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar Propiedades");
        setResizable(false);

        jPanelOptions.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonSendToAuthorize.setText("Enviar a autorización");
        jButtonSendToAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendToAuthorizeActionPerformed(evt);
            }
        });
        jPanelOptions.add(jButtonSendToAuthorize);

        jButtonOK.setText("Aceptar");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });
        jPanelOptions.add(jButtonOK);

        jButtonCancel.setText("Cerrar");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanelOptions.add(jButtonCancel);

        getContentPane().add(jPanelOptions, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(457, 300));

        jLabel1.setText("Título:");

        jLabel2.setText("Descripción:");

        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setRows(5);
        jScrollPane3.setViewportView(jTextAreaDescription);

        jLabel3.setText("Activo:");

        jLabel4.setText("Sitio:");

        jLabelSite.setText("Sitio de prueba");

        jLabel6.setText("Página:");

        jLabelPage.setText("Página");

        jLabel8.setText("Versión a publicar:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldTitle, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)))
                    .addComponent(jLabel8)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxActive, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelSite, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(jComboBoxVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabelPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelSite)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabelPage))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jCheckBoxActive))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBoxVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Información", jPanel2);

        jPanelInformation.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Propiedades de publicación", jPanelInformation);

        jPanelSchedule.setLayout(new java.awt.BorderLayout());

        jTableScheduler.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Titulo", "Activo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableScheduler.setColumnSelectionAllowed(true);
        jTableScheduler.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableScheduler.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableScheduler);
        jTableScheduler.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jPanelSchedule.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButtonAddCalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/semanticwb/openoffice/ui/icons/add.png"))); // NOI18N
        jButtonAddCalendar.setToolTipText("Agregar una calendarizacin para la publicación actual");
        jButtonAddCalendar.setFocusable(false);
        jButtonAddCalendar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAddCalendar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddCalendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddCalendarActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonAddCalendar);
        jToolBar1.add(jSeparator1);

        jButtonEditEcheduler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/semanticwb/openoffice/ui/icons/edit.png"))); // NOI18N
        jButtonEditEcheduler.setToolTipText("Editar la calendarización seleccionada");
        jButtonEditEcheduler.setEnabled(false);
        jButtonEditEcheduler.setFocusable(false);
        jButtonEditEcheduler.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonEditEcheduler.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonEditEcheduler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditEchedulerActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonEditEcheduler);
        jToolBar1.add(jSeparator2);

        jButtonDeleteScheduler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/semanticwb/openoffice/ui/icons/delete.png"))); // NOI18N
        jButtonDeleteScheduler.setToolTipText("Eliminar la calendarización seleccionada");
        jButtonDeleteScheduler.setEnabled(false);
        jButtonDeleteScheduler.setFocusable(false);
        jButtonDeleteScheduler.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDeleteScheduler.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDeleteScheduler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteSchedulerActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDeleteScheduler);

        jPanelSchedule.add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Calendarización", jPanelSchedule);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
}//GEN-LAST:event_jRadioButton1ActionPerformed

private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
}//GEN-LAST:event_jRadioButton2ActionPerformed

private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
    isCancel = true;
    this.setVisible(false);
}//GEN-LAST:event_jButtonCancelActionPerformed

private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
    isCancel = false;
    if (this.jTextFieldTitle.getText().isEmpty())
    {
        JOptionPane.showMessageDialog(this, "¡Debe indicar el título de la públicación!", this.getTitle(), JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
        jTextFieldTitle.requestFocus();
        return;
    }
    if (this.jTextAreaDescription.getText().isEmpty())
    {
        JOptionPane.showMessageDialog(this, "¡Debe indicar una descripción de la públicación!", this.getTitle(), JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
        jTextAreaDescription.requestFocus();
        return;
    }
    int res = JOptionPane.showConfirmDialog(this, "Se va a realizar los cambios de la información de publicación.\r\n¿Desea continuar?", this.getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (res == JOptionPane.YES_OPTION)
    {
        pageInformation.title = this.jTextFieldTitle.getText();
        pageInformation.description = this.jTextAreaDescription.getText();
        pageInformation.version = ((VersionInfo) this.jComboBoxVersion.getSelectedItem()).nameOfVersion;
        pageInformation.active = this.jCheckBoxActive.isSelected();
        try
        {
            OfficeApplication.getOfficeDocumentProxy().updatePorlet(pageInformation);
            if (this.jCheckBoxActive.isSelected())
            {
                if (this.pageInformation.active)
                {
                    try
                    {
                        OfficeApplication.getOfficeDocumentProxy().activateResource(pageInformation, this.jCheckBoxActive.isSelected());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try
                    {
                        if (OfficeApplication.getOfficeDocumentProxy().needsSendToPublish(pageInformation))
                        {
                            this.jCheckBoxActive.setSelected(pageInformation.active);
                            res = JOptionPane.showConfirmDialog(this, "El documento requiere una autorización para activarse\r\n¿Desea envíar a publicar el contenido?", this.getTitle(), JOptionPane.YES_NO_OPTION);
                            if (res == JOptionPane.YES_OPTION)
                            {
                                DialogSelectFlow formSendToAutorize = new DialogSelectFlow(pageInformation);
                                formSendToAutorize.setVisible(true);
                                if (formSendToAutorize.selected != null)
                                {
                                    OfficeApplication.getOfficeDocumentProxy().sendToAuthorize(pageInformation, formSendToAutorize.selected, formSendToAutorize.jTextAreaMessage.getText());
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(this, "El contenido no se activo, ya que se requiere una autorización", this.getTitle(), JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(this, "El contenido no se activo, ya que se requiere una autorización", this.getTitle(), JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        else if (OfficeApplication.getOfficeDocumentProxy().isInFlow(pageInformation))
                        {
                            this.jCheckBoxActive.setSelected(pageInformation.active);
                            JOptionPane.showMessageDialog(this, "El contenido se encuentra en proceso de ser autorizado.\r\nPara activarlo necesita terminar el proceso de autorización", this.getTitle(), JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
                        }
                        else if (OfficeApplication.getOfficeDocumentProxy().isAuthorized(pageInformation))
                        {
                            OfficeApplication.getOfficeDocumentProxy().activateResource(pageInformation, this.jCheckBoxActive.isSelected());
                        }
                        else
                        {
                            this.jCheckBoxActive.setSelected(pageInformation.active);
                            res = JOptionPane.showConfirmDialog(this, "El contenido fue rechazado.\r\nPara activarlo necesita enviarlo a autorización de nuevo\r\n¿Desea enviarlo a autorización?", this.getTitle(), JOptionPane.YES_NO_OPTION);
                            if (res == JOptionPane.YES_OPTION)
                            {
                                DialogSelectFlow formSendToAutorize = new DialogSelectFlow(pageInformation);
                                formSendToAutorize.setVisible(true);
                                if (formSendToAutorize.selected != null)
                                {
                                    OfficeApplication.getOfficeDocumentProxy().sendToAuthorize(pageInformation, formSendToAutorize.selected, formSendToAutorize.jTextAreaMessage.getText());
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(this, "El contenido no se activo, ya que se requiere una autorización", this.getTitle(), JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(this, "El contenido no se activo, ya que se requiere una autorización", this.getTitle(), JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                try
                {
                    OfficeApplication.getOfficeDocumentProxy().activateResource(pageInformation, this.jCheckBoxActive.isSelected());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            DefaultTableModel model = (DefaultTableModel) jTableScheduler.getModel();
            for (int i = 0; i < jTableScheduler.getRowCount(); i++)
            {
                CalendarInfo cal = (CalendarInfo) model.getValueAt(i, 0);
                if (cal.id == null)
                {
                    // insert
                    OfficeApplication.getOfficeDocumentProxy().insertCalendar(pageInformation, cal.title, cal.xml);
                }
                else
                {
                    // update
                    OfficeApplication.getOfficeDocumentProxy().updateCalendar(pageInformation, cal);
                }
            }

            for (PropertyInfo prop : this.panelPropertyEditor1.getProperties().keySet())
            {

                Object obj = this.panelPropertyEditor1.getProperties().get(prop);
                String value = null;
                if (obj != null)
                {
                    value = obj.toString();
                }
                OfficeApplication.getOfficeDocumentProxy().setResourceProperties(pageInformation, prop, value);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}//GEN-LAST:event_jButtonOKActionPerformed

private void jButtonAddCalendarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonAddCalendarActionPerformed
{//GEN-HEADEREND:event_jButtonAddCalendarActionPerformed
    DialogCalendar dialogCalendar = new DialogCalendar();
    dialogCalendar.setVisible(true);
    if (!dialogCalendar.isCanceled)
    {
        Document xmlCalendar = dialogCalendar.getDocument();
        XMLOutputter out = new XMLOutputter();
        CalendarInfo cal = new CalendarInfo();
        cal.xml = out.outputString(xmlCalendar);
        cal.title = dialogCalendar.jTextFieldTitle.getText();
        DefaultTableModel model = (DefaultTableModel) jTableScheduler.getModel();
        Object[] data =
        {
            cal, cal.active
        };
        added.add(cal);
        model.addRow(data);
    }
}//GEN-LAST:event_jButtonAddCalendarActionPerformed

private void jButtonEditEchedulerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonEditEchedulerActionPerformed
{//GEN-HEADEREND:event_jButtonEditEchedulerActionPerformed
    if (jTableScheduler.getSelectedRow() != -1)
    {
        CalendarInfo cal = (CalendarInfo) jTableScheduler.getModel().getValueAt(jTableScheduler.getSelectedRow(), 0);
        DialogCalendar dialogCalendar = new DialogCalendar();
        SAXBuilder SAXBuilder = new SAXBuilder();
        ByteArrayInputStream in = new ByteArrayInputStream(cal.xml.getBytes());
        try
        {
            Document document = SAXBuilder.build(in);
            dialogCalendar.setDocument(document, cal.title);
            dialogCalendar.setVisible(true);
            if (!dialogCalendar.isCanceled)
            {
                cal.title = dialogCalendar.jTextFieldTitle.getText();
                XMLOutputter out = new XMLOutputter();
                document = dialogCalendar.getDocument();
                cal.xml = out.outputString(document);
                loadCalendars();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}//GEN-LAST:event_jButtonEditEchedulerActionPerformed

private void jButtonDeleteSchedulerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonDeleteSchedulerActionPerformed
{//GEN-HEADEREND:event_jButtonDeleteSchedulerActionPerformed
    if (jTableScheduler.getSelectedRow() != -1)
    {
        int res = JOptionPane.showConfirmDialog(this, "¿Desea eliminar la calendarización?", this.getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION)
        {
            CalendarInfo cal = (CalendarInfo) jTableScheduler.getModel().getValueAt(jTableScheduler.getSelectedRow(), 0);
            if (cal.id == null)
            {
                DefaultTableModel model = (DefaultTableModel) jTableScheduler.getModel();
                model.removeRow(jTableScheduler.getSelectedRow());
                added.remove(cal);
                if (model.getRowCount() == 0 || jTableScheduler.getSelectedRow() == -1)
                {
                    this.jButtonDeleteScheduler.setEnabled(false);
                    this.jButtonEditEcheduler.setEnabled(false);
                }
            }
            else
            {
                try
                {
                    OfficeApplication.getOfficeDocumentProxy().deleteCalendar(pageInformation, cal);
                    loadCalendars();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}//GEN-LAST:event_jButtonDeleteSchedulerActionPerformed

private void jButtonSendToAuthorizeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonSendToAuthorizeActionPerformed
{//GEN-HEADEREND:event_jButtonSendToAuthorizeActionPerformed
    DialogSelectFlow dialogSelectFlow=new DialogSelectFlow(pageInformation);
    dialogSelectFlow.setVisible(true);
    if(dialogSelectFlow.selected!=null)
    {
        try
        {
            OfficeApplication.getOfficeDocumentProxy().sendToAuthorize(pageInformation, dialogSelectFlow.selected, dialogSelectFlow.jTextAreaMessage.getText());
            this.jButtonSendToAuthorize.setVisible(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}//GEN-LAST:event_jButtonSendToAuthorizeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonAddCalendar;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDeleteScheduler;
    private javax.swing.JButton jButtonEditEcheduler;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JButton jButtonSendToAuthorize;
    private javax.swing.JCheckBox jCheckBoxActive;
    private javax.swing.JComboBox jComboBoxVersion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelPage;
    private javax.swing.JLabel jLabelSite;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelInformation;
    private javax.swing.JPanel jPanelOptions;
    private javax.swing.JPanel jPanelSchedule;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableScheduler;
    private javax.swing.JTextArea jTextAreaDescription;
    private javax.swing.JTextField jTextFieldTitle;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
