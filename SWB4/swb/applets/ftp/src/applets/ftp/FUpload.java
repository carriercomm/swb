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
 * FUpload.java
 *
 * Created on 15 de noviembre de 2004, 07:00 PM
 */
package applets.ftp;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Formulario que muestra el avanze de env�ar un archivo al servidor, muestra el
 * avance en la transmisi�n.
 * @author Victor Lorenzana
 */
public class FUpload extends javax.swing.JDialog implements FileUploadListener
{

    /** Creates new form FUpload */
    URL url;
    String jsess;
    Vector listeners = new Vector();
    String path;
    java.io.File f;
    jTableFileModel model;
    Directory dir;
    Locale locale;

    public FUpload(java.awt.Frame parent, boolean modal, String jsess, URL url, Locale locale)
    {
        super(parent, modal);
        initComponents();
        this.jsess = jsess;
        this.url = url;
        this.locale = locale;

    }

    public void addSendListener(FileUploadListener listener)
    {
        listeners.add(listener);
    }

    public void fireSend(int size, int value)
    {
        Iterator it = listeners.iterator();
        while (it.hasNext())
        {
            FileUploadListener fl = (FileUploadListener) it.next();
            fl.onSend(size, value);
        }
    }

    public void sendFile(String path, java.io.File f, jTableFileModel model, Directory dir)
    {
        this.setTitle(f.getName());
        this.setVisible(true);
        this.path = path;
        this.f = f;
        this.model = model;
        this.dir = dir;
        Worker worker = new Worker();
        this.addSendListener(this);
        worker.start();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.add(jProgressBar1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    public void onSend(int size, int value)
    {
        this.jProgressBar1.setValue(value);
        //this.jProgressBar1.repaint();
        //this.jProgressBar1.updateUI();
        jPanel1.updateUI();
        SwingUtilities.updateComponentTreeUI(this.jProgressBar1);
    }

    private byte[] xor(byte[] cont, int start, int offset)
    {
        byte[] xor = new byte[cont.length];
        for (int i = start; i < offset; i++)
        {
            byte value = cont[i];
            xor[i] = (byte) (value ^ 0xF3);
        }
        return xor;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables

    private class Worker extends Thread
    {

        Worker()
        {
        }

        public void run()
        {
            try
            {
                int max = (int) (f.length() / 8192);
                max++;
                jProgressBar1.setMaximum(max);
                jProgressBar1.setIndeterminate(false);
                jProgressBar1.setStringPainted(true);
                jProgressBar1.setValue(0);
                jProgressBar1.updateUI();
                if (f.exists())
                {
                    try
                    {
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDefaultUseCaches(false);
                        con.setFixedLengthStreamingMode((int) f.length());
                        if (jsess != null)
                        {
                            con.setRequestProperty("Cookie", "JSESSIONID=" + jsess);
                        }
                        con.addRequestProperty("PATHFILEWB", path);
                        con.addRequestProperty("CIPHER", "true");                        
                        con.setDoOutput(true);
                        OutputStream out = con.getOutputStream();
                        FileInputStream fin = new FileInputStream(f);
                        byte[] bcont = new byte[8192];
                        int ret = fin.read(bcont);
                        int ivalue = 0;
                        while (ret != -1)
                        {
                            bcont = xor(bcont, 0, ret);
                            out.write(bcont, 0, ret);
                            ivalue++;
                            fireSend(max, ivalue);
                            ret = fin.read(bcont);
                            out.flush();
                        }
                        out.close();
                        fin.close();
                        String resp = con.getHeaderField(0);

                        StringTokenizer st = new StringTokenizer(resp, " ");
                        if (st.countTokens() >= 2)
                        {
                            String intcode = st.nextToken();
                            intcode = st.nextToken();
                            if (intcode.equals("200"))
                            {
                                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                                java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
                                applets.ftp.File newfile = new applets.ftp.File(dir, f.getName(), path, String.valueOf(f.length()), df.format(date));
                                try
                                {
                                    synchronized (model)
                                    {
                                        model.addFile(newfile);
                                    }
                                }
                                catch (Exception e)
                                {
                                }
                            }
                            else
                            {
                                if (st.countTokens() >= 3)
                                {
                                    System.out.println("fileupload http code: " + resp);
                                    JOptionPane.showMessageDialog(null, st.nextToken(), java.util.ResourceBundle.getBundle("applets/ftp/ftp", locale).getString("title"), JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace(System.out);
                        if (e.getMessage() != null)
                        {
                            JOptionPane.showMessageDialog(null, "error: " + e.getMessage(), java.util.ResourceBundle.getBundle("applets/ftp/ftp", locale).getString("title"), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
            catch (Exception err)
            {
                System.out.println("error: " + err.getMessage());
            }
            setVisible(false);
            dispose();
        }
    }
}
