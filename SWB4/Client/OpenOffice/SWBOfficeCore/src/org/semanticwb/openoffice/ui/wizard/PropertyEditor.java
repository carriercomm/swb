/*
 * PropertyEditor.java
 *
 * Created on 26 de diciembre de 2008, 08:52 PM
 */
package org.semanticwb.openoffice.ui.wizard;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import org.netbeans.spi.wizard.WizardPage;


/**
 *
 * @author  victor.lorenzana
 */
public class PropertyEditor extends WizardPage
{

    

    /** Creates new form PropertyEditor */
    public PropertyEditor()
    {
        initComponents();
        //PropertyEditor editor=new PropertyEditor();
        
        //editor.addProperty(new JTextField(), this, "Demo");
        //editor.addProperty(new JCheckBox(), this, "Demo2");
        //this.add(editor);
        //editor.setVisible(true);
        
    }
    public static String getDescription()
    {
        return "Propiedades del contenido";
    }

    public static void main(String[] args)
    {
        JDialog dialog = new JDialog(new Frame(), true);
        dialog.add(new PropertyEditor());
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e)
            {
                
            }

            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

            public void windowClosed(WindowEvent e)
            {
                
            }

            public void windowIconified(WindowEvent e)
            {
                
            }

            public void windowDeiconified(WindowEvent e)
            {
                
            }

            public void windowActivated(WindowEvent e)
            {
                
            }

            public void windowDeactivated(WindowEvent e)
            {
                
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProperties = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jTableProperties.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Propiedad", "Valor"
            }
        ));
        jTableProperties.setCellSelectionEnabled(true);
        jTableProperties.setRowHeight(24);
        jTableProperties.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTableProperties);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableProperties;
    // End of variables declaration//GEN-END:variables
}
