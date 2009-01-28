/*
 * TitleAndDescription.java
 *
 * Created on 3 de junio de 2008, 10:57 AM
 */
package org.semanticwb.openoffice.ui.wizard;

import java.util.Map;
import javax.swing.JOptionPane;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPanelNavResult;
import org.semanticwb.office.interfaces.ContentType;
import org.semanticwb.openoffice.OfficeApplication;
import org.semanticwb.openoffice.util.FixedLengthPlainDocument;

/**
 *
 * @author  victor.lorenzana
 */
public class TitleAndDescription extends WizardPage
{

    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String NODE_TYPE = "NODE_TYPE";
    boolean showTypeOfcontent = true;

    /** Creates new form TitleAndDescription */
    public TitleAndDescription(boolean showTypeOfcontent)
    {
        this();
        this.showTypeOfcontent = showTypeOfcontent;
        this.jComboBoxType.setVisible(showTypeOfcontent);
        this.jLabelType.setVisible(showTypeOfcontent);

    }

    public TitleAndDescription()
    {
        initComponents();
        this.jTextFieldName.setDocument(new FixedLengthPlainDocument(255));
        this.jTextAreaDescription.setDocument(new FixedLengthPlainDocument(255));
        this.jComboBoxType.removeAllItems();
        try
        {
            String repository = SelectCategory.map.get(SelectCategory.REPOSITORY_ID).toString();
            for (ContentType type : OfficeApplication.getOfficeApplicationProxy().getContentTypes(repository))
            {
                this.jComboBoxType.addItem(type);
            }
        }
        catch (Exception e)
        {
        }
    }

    public static String getDescription()
    {
        return "Información del contenido";
    }

    @Override
    public WizardPanelNavResult allowNext(String arg, Map map, Wizard wizard)
    {
        return allowFinish(arg, map, wizard);
    }

    /**     *
     * @param arg
     * @param map
     * @param wizard
     * @return
     */
    @Override
    public WizardPanelNavResult allowFinish(String arg, Map map, Wizard wizard)
    {
        WizardPanelNavResult result = WizardPanelNavResult.REMAIN_ON_PAGE;
        if (this.jTextFieldName.getText().trim().equals(""))
        {
            JOptionPane.showMessageDialog(null, "¡Debe indicar el título del contenido!", getDescription(), JOptionPane.ERROR_MESSAGE);
            this.jTextFieldName.requestFocus();
        }
        else if (this.jTextAreaDescription.getText().trim().equals(""))
        {
            JOptionPane.showMessageDialog(null, "¡Debe indicar la descripción del contenido!", getDescription(), JOptionPane.ERROR_MESSAGE);
            this.jTextAreaDescription.requestFocus();
        }
        if (showTypeOfcontent)
        {
            if (this.jComboBoxType.getSelectedItem() == null)
            {
                JOptionPane.showMessageDialog(null, "¡Debe indicar el tipo de contenido!", getDescription(), JOptionPane.ERROR_MESSAGE);
                this.jComboBoxType.requestFocus();
            }
            else
            {
                map.put(TITLE, this.jTextFieldName.getText().trim());
                map.put(DESCRIPTION, this.jTextAreaDescription.getText().trim());
                map.put(NODE_TYPE, ((ContentType) this.jComboBoxType.getSelectedItem()).id);
                result = WizardPanelNavResult.PROCEED;
            }
        }
        else
        {
            map.put(TITLE, this.jTextFieldName.getText().trim());
            map.put(DESCRIPTION, this.jTextAreaDescription.getText().trim());            
            result = WizardPanelNavResult.PROCEED;
        }
        return result;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelName = new javax.swing.JLabel();
        jLabelDescription = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescription = new javax.swing.JTextArea();
        jLabelType = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox();

        jLabelName.setText("Título:");

        jLabelDescription.setText("Descripción:");

        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setLineWrap(true);
        jTextAreaDescription.setRows(5);
        jTextAreaDescription.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextAreaDescription);

        jLabelType.setText("Tipo de Contenido:");

        jComboBoxType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelType)
                    .addComponent(jLabelDescription)
                    .addComponent(jLabelName))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(jTextFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(jComboBoxType, 0, 294, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelName))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelDescription)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelType)
                    .addComponent(jComboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(76, 76, 76))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBoxType;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelType;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaDescription;
    private javax.swing.JTextField jTextFieldName;
    // End of variables declaration//GEN-END:variables
}
