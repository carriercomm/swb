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
 * WBCreateTopic.java
 *
 * Created on 6 de septiembre de 2002, 13:15
 */

package applets.mapsadm;

import javax.swing.*;
import java.util.Locale;

/**
 *
 * @author  Administrador
 */
public class WBCreateTopic extends javax.swing.JDialog {

    private boolean accept=false;
    private String prefix=null;
    private String lang="Espa�ol";
    private Locale locale=new Locale("es");
    
    /** Creates new form WBCreateTopic */
    public WBCreateTopic(java.awt.Frame parent, boolean modal,Locale locale,String langName) {
        super(parent, modal);
        lang=langName;
        this.locale=locale;
        initComponents();
        jTextField1.setText("");
        jTextField2.setText("");
        jLabel2.setText(jLabel2.getText()+" ("+lang+")");
        this.setSize(245,210);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(java.util.ResourceBundle.getBundle("applets/mapsadm/locale",locale).getString("frmTopicCreate.title.create"));
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                closeDialog(evt);
            }
        });

        jLabel1.setText(java.util.ResourceBundle.getBundle("applets/mapsadm/locale",locale).getString("frmTopicCreate.lbl.id"));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 60, 210, 20);

        jTextField1.setText("jTextField1");
        jTextField1.setMaximumSize(new java.awt.Dimension(50, 1));
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                jTextField1KeyTyped(evt);
            }
        });

        getContentPane().add(jTextField1);
        jTextField1.setBounds(10, 80, 220, 20);

        jLabel2.setText(java.util.ResourceBundle.getBundle("applets/mapsadm/locale",locale).getString("frmTopicCreate.lbl.name"));
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 10, 210, 16);

        jTextField2.setText("jTextField2");
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                jTextField2KeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                jTextField2KeyReleased(evt);
            }
        });

        getContentPane().add(jTextField2);
        jTextField2.setBounds(10, 30, 220, 20);

        jButton1.setText(java.util.ResourceBundle.getBundle("applets/mapsadm/locale",locale).getString("frmTopicCreate.btn.accept"));
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton1);
        jButton1.setBounds(10, 120, 100, 26);

        jButton2.setText(java.util.ResourceBundle.getBundle("applets/mapsadm/locale",locale).getString("frmTopicCreate.btn.cancel"));
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton2);
        jButton2.setBounds(120, 120, 110, 26);

        pack();
    }//GEN-END:initComponents

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // Add your handling code here:
        StringBuffer aux=new StringBuffer();
        if(prefix!=null)
        {
            aux.append(prefix);
            aux.append("_");
        }
        aux.append(jTextField2.getText());
        int l=aux.length();
        
        StringBuffer aux2=new StringBuffer();
        for(int x=0;x<l;x++)
        {
            char ch=aux.charAt(x);
            if(!((ch>='0' && ch<='9')||(ch>='a' && ch<='z')||(ch>='A' && ch<='Z')||(ch=='_')))
            {
                if     (ch==' ')ch='_';
                else if(ch=='�')ch='a';
                else if(ch=='�')ch='e';
                else if(ch=='�')ch='i';
                else if(ch=='�')ch='o';
                else if(ch=='�')ch='u';
                else if(ch=='�')ch='a';
                else if(ch=='�')ch='e';
                else if(ch=='�')ch='i';
                else if(ch=='�')ch='o';
                else if(ch=='�')ch='u';
                else if(ch=='�')ch='u';
                else if(ch=='�')ch='n';
                else if(ch=='�')ch='A';
                else if(ch=='�')ch='E';
                else if(ch=='�')ch='I';
                else if(ch=='�')ch='O';
                else if(ch=='�')ch='U';
                else if(ch=='�')ch='A';
                else if(ch=='�')ch='E';
                else if(ch=='�')ch='I';
                else if(ch=='�')ch='O';
                else if(ch=='�')ch='U';
                else if(ch=='�')ch='U';
                else if(ch=='�')ch='N';
                else ch='\0';
            }
            if(ch!='\0')aux2.append(ch);
        }
/*        
        aux=aux.toLowerCase();
        aux=aux.replace('�','o');
        aux=aux.replace('�','u');
        aux=aux.replace('�','a');
        aux=aux.replace('�','e');
        aux=aux.replace('�','i');
        aux=aux.replace('�','o');
        aux=aux.replace('�','u');
        aux=aux.replace('�','u');
        aux=aux.replace('�','n');
        aux=aux.replace(' ','_');
 */
        String ret;
        if(aux2.length()>50)
            ret=aux2.substring(0,50);
        else 
            ret=aux2.toString();
        if(jTextField1.getText()!=ret)
            jTextField1.setText(ret);
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // Add your handling code here:
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // Add your handling code here:
        if(evt.getKeyChar()!=evt.VK_BACK_SPACE  && jTextField1.getText().length()>=50)
        {
            evt.consume();
        }
        if(!((evt.getKeyChar()>='0' && evt.getKeyChar()<='9')||(evt.getKeyChar()>='a' && evt.getKeyChar()<='z')||(evt.getKeyChar()>='A' && evt.getKeyChar()<='Z')||(evt.getKeyChar()=='_')||(evt.getKeyChar()==evt.VK_BACK_SPACE)))
        {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Add your handling code here:
        if(jTextField1.getText().length()==0 || jTextField2.getText().length()==0)
        {
            JOptionPane.showMessageDialog(this,java.util.ResourceBundle.getBundle("applets/mapsadm/locale",locale).getString("frmTopicCreate.usrmsg.empty"),java.util.ResourceBundle.getBundle("applets/mapsadm/locale",locale).getString("frmTopicCreate.title.create"),JOptionPane.ERROR_MESSAGE);
        }else
        {
            accept=true;
            setVisible(false);
            this.hide();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Add your handling code here:
        accept=false;
        setVisible(false);
        this.hide();
    }//GEN-LAST:event_jButton2ActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        accept=false;
        setVisible(false);
        hide();
    }//GEN-LAST:event_closeDialog

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        new WBCreateTopic(new javax.swing.JFrame(), true,new Locale("es"),"Espa�ol").show();
    }
    
    public String getTopicId()
    {
        return jTextField1.getText();
    }

    public String getTopicName()
    {
        return jTextField2.getText();
    }

    /** Getter for property accept.
     * @return Value of property accept.
     */
    public boolean isAccepted() {
        return accept;
    }    

    /** Getter for property prefix.
     * @return Value of property prefix.
     */
    public java.lang.String getPrefix() {
        return prefix;
    }
    
    /** Setter for property prefix.
     * @param prefix New value of property prefix.
     */
    public void setPrefix(java.lang.String prefix) {
        this.prefix = prefix;
    }
    
    /** Getter for property lang.
     * @return Value of property lang.
     */
    public java.lang.String getLang() {
        return lang;
    }
    
    /** Setter for property lang.
     * @param lang New value of property lang.
     */
    public void setLang(java.lang.String lang) {
        this.lang = lang;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

}
