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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.semanticwb.openoffice.writer;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.document.XDocumentInfo;
import com.sun.star.document.XDocumentInfoSupplier;
import com.sun.star.frame.XController;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextGraphicObjectsSupplier;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XModifiable;
import com.sun.star.util.XPropertyReplace;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.semanticwb.openoffice.DocumentType;
import org.semanticwb.openoffice.ErrorLog;
import org.semanticwb.openoffice.NoHasLocationException;
import org.semanticwb.openoffice.OfficeDocument;
import org.semanticwb.openoffice.SaveDocumentFormat;
import org.semanticwb.openoffice.WBAlertException;
import org.semanticwb.openoffice.WBException;
import org.semanticwb.openoffice.WBOfficeException;
import static org.semanticwb.openoffice.util.FileUtil.getExtension;
import static org.semanticwb.openoffice.util.FileUtil.getFileFromURL;
import static org.semanticwb.openoffice.util.FileUtil.getPathURL;

/**
 * Class to Wrap an Open Office Writer Document
 * @author victor.lorenzana
 */
public class WB4Writer extends OfficeDocument
{

    private static final String NL = "\r\n";
    private static final String ERROR_DOCUMENT_NOT_FOUND = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/writer/WB4Writer").getString("THERE_IS_NOT_A_DOCUMENT_ACTIVE_IN_THE_DESKTOP");
    private static final String HTML_EXPORT_FORMAT = "HTML (StarWriter)";
    private static final String OFFICE97_FORMAT = "MS Word 97";
    private static final String DESKTOP_NOT_FOUND = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/writer/WB4Writer").getString("ERROR_AL_OBTENER_EL_ESCRITORIO_DE_OPEN_OFFICE");
    private static final String DESKTOP_PATH = "com.sun.star.frame.Desktop";
    private static final String INDEXOFBOUNDERROR = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/writer/WB4Writer").getString("THERE_WAS_AN_ERROR_SAVING_CUSTOM_PROPERTIES");
    private static final String ERROR_DOCUMENT_NOT_MODIFIED = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/writer/WB4Writer").getString("THE_DOCUMENT_HAS_NOT_BEEN_MODIFIED");
    private static final String ERROR_DOCUMENT_NOT_SAVED_BEFORE = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/writer/WB4Writer").getString("THE_DOCUMENT_HAS_NOT_BEEN_SAVED_BEFORE");
    private static final String ERROR_DOCUMENT_READ_ONLY = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/writer/WB4Writer").getString("THE_DOCUMENT_IS_READ_ONLY");
    /**
     * The default Open Office Extension OPENOFFICE_EXTENSION=".odt"
     */
    private static final String OPENOFFICE_EXTENSION = ".odt";
    private static final String ERROR_NO_SAVE = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/writer/WB4Writer").getString("THE_DOCUMENT_CAN_NOT_BE_SAVED");
    /**
     * The xtension to a Word Document in format XP/2000/2003 WORD_EXTENSION=".doc"
     */
    private static final String WORD_EXTENSION = ".doc";
    /**
     * The extension of a HTML Document HTML_EXTENSION=".html"
     */
    private static final String HTML_EXTENSION = ".html";
    private static final String FILTER_NAME = "FilterName";
    private static final String HYPERLINK_VALUE = "HyperLinkURL";
    private static final String OVERWRITE = "Overwrite";
    private static final String WRITER_FORMAT = "Writer8";
    /**
     * The Open Office Writer Document that Wraps
     */
    private final XComponent document;

    /**
     * Create a representation of a Writer Document
     * @param document Representation of a Writer Document
     * @see XComponent
     */
    public WB4Writer(XComponent document)
    {
        this.document = document;

    }

    /**
     * Create a representation of a Writer Document
     * @param m_xContext Context of Writer Application
     * @throws org.semanticwb.openoffice.WBOfficeException If the Desktop can not be used
     * @see XComponentContext
     */
    public WB4Writer(XComponentContext m_xContext) throws WBOfficeException
    {        
        XMultiComponentFactory serviceManager = m_xContext.getServiceManager();
        try
        {
            Object desktop = serviceManager.createInstanceWithContext(
                    DESKTOP_PATH, m_xContext);
            XDesktop xdesktop = (XDesktop) UnoRuntime.queryInterface(XDesktop.class, desktop);
            document = xdesktop.getCurrentComponent();
            if (document == null)
            {
                throw new WBOfficeException(ERROR_DOCUMENT_NOT_FOUND);
            }
        }
        catch (com.sun.star.uno.Exception e)
        {
            throw new WBOfficeException(DESKTOP_NOT_FOUND, e);
        }
    }

    /**
     * Gets the Application version string, allways returns 2.4
     * @return Application version String     * 
     */
    @Override
    public String getApplicationVersion()
    {

        return "2.4";
    }

    /**
     * Gets all the files in the document
     * @return List of files in the document
     * @throws org.semanticwb.openoffice.NoHasLocationException The document has not saved before, and the document has hyperlinks relatives to the document.
     */
    @Override
    public final List<File> getAllAttachments() throws NoHasLocationException
    {
        List<File> attachments = new ArrayList<File>();
        try
        {
            XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, this.document);
            XSearchDescriptor xUrlSearchDesc = xSearchable.createSearchDescriptor();

            PropertyValue[] aSearchArgs = new PropertyValue[1];
            aSearchArgs[0] = new PropertyValue();
            aSearchArgs[0].Name = HYPERLINK_VALUE;

            XPropertyReplace xPropSear = (XPropertyReplace) UnoRuntime.queryInterface(XPropertyReplace.class, xUrlSearchDesc);
            xPropSear.setSearchAttributes(aSearchArgs);
            xPropSear.setValueSearch(false);
            Object linkResult = xSearchable.findFirst(xUrlSearchDesc);
            while (linkResult != null)
            {
                try
                {
                    XTextRange xTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, linkResult);
                    XTextCursor xTextCursor = xTextRange.getText().createTextCursorByRange(xTextRange);
                    XPropertySet xPropSet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
                    Object hiperlink = xPropSet.getPropertyValue(HYPERLINK_VALUE);
                    attachments.addAll(this.addLink(hiperlink.toString()));
                    linkResult = xSearchable.findNext(xTextRange.getEnd(), xUrlSearchDesc);
                }
                catch (com.sun.star.uno.Exception ukpe)
                {
                    ErrorLog.log(ukpe);
                }

            }
        }
        catch (com.sun.star.uno.Exception ukpe)
        {
            ErrorLog.log(ukpe);
        }

        return attachments;
    }

    /**
     * Gets al the custom properties of the document
     * @return A Map of custum properties
     * @throws org.semanticwb.openoffice.WBException If the list of properties are more that four
     */
    @Override
    public Map<String, String> getCustomProperties()
    {
        HashMap<String, String> properties = new HashMap<String, String>();
        XTextDocument xtd =
                (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, this.document);
        XDocumentInfoSupplier xdis =
                (XDocumentInfoSupplier) UnoRuntime.queryInterface(XDocumentInfoSupplier.class, xtd);
        XDocumentInfo xdi = xdis.getDocumentInfo();

        for (short i = 0;
                i < xdi.getUserFieldCount();
                i++)
        {
            try
            {
                String name = xdi.getUserFieldName(i);
                String value = xdi.getUserFieldValue(i);
                properties.put(name, value);
            }
            catch (com.sun.star.lang.ArrayIndexOutOfBoundsException aibe)
            {
                ErrorLog.log(aibe);
            }
        }




        return properties;
    }

    /**
     * Gets the type of document
     * @return DocumentType.WORD
     * @see DocumentType
     */
    @Override
    public DocumentType getDocumentType()
    {
        return DocumentType.WORD;
    }

    /**
     * Gets the path of the fisical document
     * @return A File with the fisical path of the document
     * @throws org.semanticwb.openoffice.NoHasLocationException If the document has not been saved
     */
    @Override
    public File getLocalPath()
            throws NoHasLocationException
    {

        XTextDocument xtd = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, this.document);
        XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
        if (xStorable.hasLocation())
        {
            return getFileFromURL(xtd.getURL());
        }
        else
        {
            throw new NoHasLocationException();
        }
    }

    /**
     * Save the document
     * @throws org.semanticwb.openoffice.WBException If the document has not been saved before, if the document has not been modified, or if the document is read only
     */
    @Override
    public final void save() throws WBException
    {
        try
        {
            XModifiable xModified = (XModifiable) UnoRuntime.queryInterface(XModifiable.class, document);
            if (xModified.isModified())
            {
                XStorable xStorable =
                        (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
                if (xStorable.hasLocation())
                {
                    if (!xStorable.isReadonly())
                    {
                        xStorable.store();
                    }
                    else
                    {
                        throw new WBAlertException(ERROR_DOCUMENT_READ_ONLY);
                    }
                }
                else
                {
                    throw new WBAlertException(ERROR_DOCUMENT_NOT_SAVED_BEFORE);
                }
            }
            else
            {
                //throw new WBAlertException(ERROR_DOCUMENT_NOT_MODIFIED);
            }
        }
        catch (IOException ioe)
        {
            throw new WBOfficeException(ERROR_NO_SAVE, ioe);

        }

    }

    /**
     * Save the document in default format
     * @param file The full path of the file, overwrite the document if exists
     * @throws org.semanticwb.openoffice.WBException If can not be saved
     * @throws IllegalArgumentException If the path is a directory
     */
    @Override
    public void save(File file) throws WBException
    {
        if (file.isDirectory())
        {
            throw new IllegalArgumentException();
        }

        try
        {
            PropertyValue[] storeProps = new PropertyValue[2];
            storeProps[0] = new PropertyValue();
            storeProps[0].Name = FILTER_NAME;
            storeProps[0].Value = WRITER_FORMAT;

            storeProps[1] = new PropertyValue();
            storeProps[1].Name = OVERWRITE;
            storeProps[1].Value = true;

            XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
            String url = getPathURL(file);
            xStorable.storeAsURL(url, storeProps);
        }
        catch (IOException wbe)
        {
            throw new WBOfficeException(ERROR_NO_SAVE, wbe);
        }

    }

    /**
     * Save the document in selected a format
     * @param dir The path of the file
     * @param format The SaveDocumentFormat to use
     * @return a File with the full path of the new document
     * @throws org.semanticwb.openoffice.WBException If the document can not be saved
     * @throws IllegalArgumentException If the parameter is a file, must be a directory
     */
    @Override
    public File saveAs(
            File dir, SaveDocumentFormat format) throws WBException
    {
        File result;
        switch (format)
        {
            case HTML:
                result = this.saveAsHtml(dir);
                break;

            case OFFICE_2003:
                result = saveAsOffice2003(dir);
                break;

            default:

                result = saveAsOpenOffice(dir);
        }

        return result;
    }

    /**
     * Save the couemnt in Open Office format (.ods)
     * @param dir The directory to save the document
     * @return the full path of the new document
     * @throws org.semanticwb.openoffice.WBException If the document can not be saved
     * @throws IllegalArgumentException If the parameter is a file, must be a directory
     */
    private File saveAsOpenOffice(File dir) throws WBException
    {
        if (dir.isFile())
        {
            throw new IllegalArgumentException();
        }

        try
        {
            File docFile = this.getLocalPath();
            String extension = getExtension(docFile);
            String name = null;
            if (extension == null)
            {
                name = docFile.getName();
            }
            else
            {
                name = docFile.getName().replace(extension, OPENOFFICE_EXTENSION);
            }
// guarda el documento en .doc en directorio Temporal            

            File DocFile = new File(dir.getPath() + File.separatorChar + name);
            PropertyValue[] storeProps = new PropertyValue[2];
            storeProps[0] = new PropertyValue();
            storeProps[0].Name = FILTER_NAME;
            storeProps[0].Value = WRITER_FORMAT;

            storeProps[1] = new PropertyValue();
            storeProps[1].Name = OVERWRITE;
            storeProps[1].Value = true;
            XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);









            if (!dir.exists())
            {
                dir.mkdirs();
            }
            String url = getPathURL(DocFile);
            xStorable.storeToURL(url, storeProps);




            return DocFile;
        }
        catch (IOException ioe)
        {
            throw new WBOfficeException(ERROR_NO_SAVE, ioe);
        }

    }

    /**
     * Save the document in Office 2003 format
     * @param dir File Path directory to save the cocument
     * @return The full path of the new file
     * @throws org.semanticwb.openoffice.WBException
     * @throws IllegalArgumentException If the parameter is a file
     */
    private File saveAsOffice2003(File dir) throws WBException
    {
        if (dir.isFile())
        {
            throw new IllegalArgumentException();
        }

        try
        {
            File docFile = this.getLocalPath();
            String extension = getExtension(docFile);
            String name = null;
            if (extension == null)
            {
                name = docFile.getName();
            }
            else
            {
                name = docFile.getName().replace(extension, WORD_EXTENSION);
            }
// guarda el documento en .doc en directorio Temporal

            File DocFile = new File(dir.getPath() + File.separatorChar + name);
            PropertyValue[] storeProps = new PropertyValue[2];
            storeProps[0] = new PropertyValue();
            storeProps[0].Name = FILTER_NAME;
            storeProps[0].Value = OFFICE97_FORMAT;

            storeProps[1] = new PropertyValue();
            storeProps[1].Name = OVERWRITE;
            storeProps[1].Value = true;
            XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);









            if (!dir.exists())
            {
                dir.mkdirs();
            }
            String url = getPathURL(DocFile);
            xStorable.storeToURL(url, storeProps);




            return DocFile;
        }
        catch (IOException ioe)
        {
            throw new WBOfficeException(ERROR_NO_SAVE, ioe);
        }

    }

    /**
     * Save the document in Html format
     * @param dir Directory to save the document
     * @return The full path of the document
     * @throws org.semanticwb.openoffice.WBException If the document can not be saved
     * @throws IllegalArgumentException If the File is a file and not a directory
     */
    @Override
    public File saveAsHtml(
            File dir) throws WBException
    {
        if (dir.isFile())
        {
            throw new IllegalArgumentException();
        }

        try
        {
            File docFile = this.getLocalPath();
            File HTMLfile;

            if (docFile.getName().endsWith(OPENOFFICE_EXTENSION))
            {
                HTMLfile = new File(dir.getPath() + File.separatorChar + docFile.getName().replace(OPENOFFICE_EXTENSION, HTML_EXTENSION));
                String name = docFile.getName().replace(OPENOFFICE_EXTENSION, WORD_EXTENSION);
                // guarda el documento en .doc en directorio Temporal
                File DocFile = new File(dir.getPath() + File.separatorChar + name);
                PropertyValue[] storeProps = new PropertyValue[2];
                storeProps[0] = new PropertyValue();
                storeProps[0].Name = FILTER_NAME;
                storeProps[0].Value = OFFICE97_FORMAT;

                storeProps[1] = new PropertyValue();
                storeProps[1].Name = OVERWRITE;
                storeProps[1].Value = true;
                XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
                String url = getPathURL(DocFile);
                xStorable.storeToURL(url, storeProps);
            }
            else
            {
                try
                {
                    String name = docFile.getName();
                    File DocFile = new File(dir.getPath() + File.separatorChar + name);
                    DocFile.getParentFile().mkdirs();
                    OutputStream out=new FileOutputStream(DocFile);
                    InputStream in=new FileInputStream(docFile.getAbsoluteFile());
                    byte[] bcont=new byte[2048];
                    int read=in.read(bcont);
                    while(read!=-1)
                    {
                        out.write(bcont,0, read);
                        read=in.read(bcont);
                    }
                }
                catch(Exception e)
                {
                    String msg="Error: detail: "+NL+e.getLocalizedMessage();
                    JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.OK_OPTION  | JOptionPane.ERROR_MESSAGE );
                    throw new WBException(msg, e);
                }
                HTMLfile = new File(dir.getPath() + File.separatorChar + docFile.getName().replace(WORD_EXTENSION, HTML_EXTENSION));
            }

            PropertyValue[] storeProps = new PropertyValue[2];
            storeProps[0] = new PropertyValue();
            storeProps[0].Name = FILTER_NAME;
            storeProps[0].Value = HTML_EXPORT_FORMAT;

            storeProps[1] = new PropertyValue();
            storeProps[1].Name = OVERWRITE;
            storeProps[1].Value = true;
            XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            String url = getPathURL(HTMLfile);
            xStorable.storeToURL(url, storeProps);
            return HTMLfile;
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            throw new WBOfficeException(ERROR_NO_SAVE, ioe);
        }

    }

    /**
     * Save the properties in custom properties in the document
     * @param properties Properties to save
     * @throws org.semanticwb.openoffice.WBException if the properties are more than four
     */
    @Override
    public void saveCustomProperties(Map<String, String> properties) throws WBException
    {
        XTextDocument xtd =
                (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, this.document);
        XDocumentInfoSupplier xdis =
                (XDocumentInfoSupplier) UnoRuntime.queryInterface(XDocumentInfoSupplier.class, xtd);
        XDocumentInfo xdi = xdis.getDocumentInfo();
        short index = 0;
        // solo puede tener 4 propiedades
        for (String key : properties.keySet())
        {
            String value = properties.get(key);
            try
            {
                xdi.setUserFieldName(index, key);
                xdi.setUserFieldValue(index, value);
            }
            catch (com.sun.star.lang.ArrayIndexOutOfBoundsException aibe)
            {
                throw new WBOfficeException(INDEXOFBOUNDERROR, aibe);
            }
            index++;
        }
        try
        {
            this.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Prepare the html to be published
     * @param htmlFile The full path of the Html document exported by the application
     * @throws IllegalArgumentException If the path is a directory
     */
    @Override
    public final void prepareHtmlFileToSend(File htmlFile)
    {
        if (htmlFile.isDirectory())
        {
            throw new IllegalArgumentException();
        }
// TODO: Falta implementar    

    }

    /**
     * Gets is the document is new, it means that the document has not been saved before
     * @return True if the document is new, false otherwise
     */
    @Override
    public boolean isNewDocument()
    {
        XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
        return !xStorable.hasLocation();
    }

    /**
     * Gets if the document is readonly or not
     * @return True if the document is readonly or not
     */
    @Override
    public boolean isReadOnly()
    {
        XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
        return xStorable.isReadonly();
    }

    /**
     * Gets if the document has been modified and can be saves
     * @return True if the document has been modified, false otherwise
     */
    @Override
    public boolean isModified()
    {
        XModifiable xModified = (XModifiable) UnoRuntime.queryInterface(XModifiable.class, document);
        return xModified.isModified();
    }

    /**
     * Gets the Default extension used by the application
     * @return A string with the default extension, allways returns .odt
     */
    @Override
    public String getDefaultExtension()
    {
        return OPENOFFICE_EXTENSION;
    }

    @Override
    public String getPublicationExtension()
    {
        return WORD_EXTENSION;
    }

    @Override
    public void insertLink(String url, String text)
    {

        XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, this.document);

        Object selection = xTextDocument.getCurrentSelection();
        XTextRange xTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, selection);
        if (xTextRange == null)
        {
            XModel xModel = (XModel) UnoRuntime.queryInterface(XModel.class, this.document);
            XController xController = xModel.getCurrentController();
            XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier) UnoRuntime.queryInterface(XTextViewCursorSupplier.class, xController);
            XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor();
            if(xViewCursor.getString()==null || xViewCursor.getString().equals(""))
            {
                XText xDocumentText = xViewCursor.getText();
                XTextCursor xTextCursor = xDocumentText.createTextCursorByRange(xViewCursor.getStart());
                XText xText = xTextCursor.getText();
                XPropertySet xTextCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                        XPropertySet.class, xTextCursor);
                try
                {
                    xTextCursorProps.setPropertyValue(HYPERLINK_VALUE, url);
                    xText.insertString(xTextCursor, text, false);
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
                    XPropertySet xTextCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                            XPropertySet.class, xViewCursor);
                    xTextCursorProps.setPropertyValue(HYPERLINK_VALUE, url);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
        else
        {

            XText range = xTextRange.getText();
            if (range != null)
            {
                XText xText = range.getText();
                XTextCursor xTextCursor = xText.createTextCursor();
                XPropertySet xTextCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                        XPropertySet.class, xTextCursor);
                try
                {
                    xTextCursorProps.setPropertyValue(HYPERLINK_VALUE, url);
                    xText.insertString(xTextCursor, text, false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String[] getLinks()
    {
        HashSet<String> links = new HashSet<String>();
        try
        {
            XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, this.document);
            XSearchDescriptor xUrlSearchDesc = xSearchable.createSearchDescriptor();

            PropertyValue[] aSearchArgs = new PropertyValue[1];
            aSearchArgs[0] = new PropertyValue();
            aSearchArgs[0].Name = HYPERLINK_VALUE;

            XPropertyReplace xPropSear = (XPropertyReplace) UnoRuntime.queryInterface(XPropertyReplace.class, xUrlSearchDesc);
            xPropSear.setSearchAttributes(aSearchArgs);
            xPropSear.setValueSearch(false);
            Object linkResult = xSearchable.findFirst(xUrlSearchDesc);
            while (linkResult != null)
            {
                try
                {
                    XTextRange xTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, linkResult);
                    XTextCursor xTextCursor = xTextRange.getText().createTextCursorByRange(xTextRange);
                    XPropertySet xPropSet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
                    Object hiperlink = xPropSet.getPropertyValue(HYPERLINK_VALUE);
                    if (hiperlink != null)
                    {
                        links.add(hiperlink.toString());
                    }
                    linkResult = xSearchable.findNext(xTextRange.getEnd(), xUrlSearchDesc);
                }
                catch (com.sun.star.uno.Exception ukpe)
                {
                    ErrorLog.log(ukpe);
                }

            }
        }
        catch (com.sun.star.uno.Exception ukpe)
        {
            ErrorLog.log(ukpe);
        }
        return links.toArray(new String[links.size()]);
    }

    @Override
    public int getCountImages()
    {
        int images = 0;
        XTextGraphicObjectsSupplier XTxtGraphObjSupplier = (XTextGraphicObjectsSupplier) UnoRuntime.queryInterface(XTextGraphicObjectsSupplier.class, this.document);        
        XNameAccess XNameAcc = XTxtGraphObjSupplier.getGraphicObjects();
        String[] allImages = XNameAcc.getElementNames();
        if(allImages!=null)
        {
            images += allImages.length;
        }
        return images;
    }

    @Override
    public String getSelectedText()
    {
       XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, this.document);

        Object selection = xTextDocument.getCurrentSelection();
        XTextRange xTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, selection);
        if (xTextRange == null)
        {
            XModel xModel = (XModel) UnoRuntime.queryInterface(XModel.class, this.document);
            XController xController = xModel.getCurrentController();
            XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier) UnoRuntime.queryInterface(XTextViewCursorSupplier.class, xController);
            XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor();
            if(xViewCursor!=null)
            {
                return xViewCursor.getString();
            }
            else
            {
                return null;
            }
        }
        return null;
    }
}
