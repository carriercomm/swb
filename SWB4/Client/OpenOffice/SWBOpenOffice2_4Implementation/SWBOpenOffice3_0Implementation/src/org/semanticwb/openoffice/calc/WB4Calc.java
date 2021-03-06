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
package org.semanticwb.openoffice.calc;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XEnumeration;
import com.sun.star.document.XDocumentInfo;
import com.sun.star.document.XDocumentInfoSupplier;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPageSupplier;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.sheet.*;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XModifiable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import static org.semanticwb.openoffice.util.FileUtil.saveContent;
import static org.semanticwb.openoffice.util.FileUtil.loadResourceAsString;
import static org.semanticwb.openoffice.util.FileUtil.loadFileAsString;
import static org.semanticwb.openoffice.util.FileUtil.getExtension;
import static org.semanticwb.openoffice.util.FileUtil.getPathURL;
import static org.semanticwb.openoffice.util.FileUtil.getFileFromURL;

/**
 * Class to wrap a Open Office Calc Document
 * @author victor.lorenzana
 */
public class WB4Calc extends OfficeDocument
{

    private static final String NL = "\r\n";
    private static final String ERROR_DOCUMENT_NOT_FOUND = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/calc/WB4Calc").getString("THERE_IS_NOT_A_DOCUMENT_ACTIVE_IN_THE_DESKTOP");
    private static final String CALC_FORMAT = "Calc8";
    private static final String DESKTOP_NOT_FOUND = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/calc/WB4Calc").getString("THE_DESKTOP_WAS_NOT_FOUND");
    private static final String DESKTOP_PATH = "com.sun.star.frame.Desktop";
    private static final String ERROR_DOCUMENT_NOT_MODIFIED = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/calc/WB4Calc").getString("THE_DOCUMENT_HAS_NOT_BEEN_MODIFIED");
    private static final String ERROR_DOCUMENT_NOT_SAVED_BEFORE = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/calc/WB4Calc").getString("THE_DOCUMENT_HAS_NOT_BEEN_SAVED_BEFORE");
    private static final String HTML_EXPORT_FORMAT = "HTML (StarCalc)";
    private static final String INDEXOFBOUNDERROR = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/calc/WB4Calc").getString("THERE_WAS_AN_ERROR_SAVING_CUSTOM_PROPERTIES");
    private static final String ERROR_DOCUMENT_READ_ONLY = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/calc/WB4Calc").getString("THE_DOCUMENT_IS_READ_ONLY");
    private static final String OFFICE97_FORMAT = "MS Excel 97";
    private static final String OPENOFFICE_EXTENSION = ".ods";
    private static final String EXCEL_EXTENSION = ".xls";
    private static final String HTML_EXTENSION = ".html";
    private static final String ERROR_NO_SAVE = java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/calc/WB4Calc").getString("THE_DOCUMENT_CAN_NOT_BE_SAVED");
    private static final String FILTER_NAME = "FilterName";
    private static final String OVERRIDE_OPTION = "Overwrite";
    private static final String REPRESENTATION = "Representation";
    private static final String TARGETFRAME = "TargetFrame";
    private static final String URL = "URL";
    private static final String _BLANK = "_blank";
    private static final String tabstrip;
    private static final NumberFormat formatter = new DecimalFormat("000");
    private final XComponent document;

    static
    {
        tabstrip = loadResourceAsString(WB4Calc.class, "tabstrip.htm");
    }

    /**
     * Create a representation of a Calc Document
     * @param document Representation of a Calc Document
     * @see XComponent
     */
    public WB4Calc(XComponent document)
    {
        this.document = document;
    }

    /**
     * Create a representation of a Calc Document
     * @param m_xContext Context of Calc Application
     * @throws org.semanticwb.openoffice.WBOfficeException If the Desktop can not be used
     * @see XComponentContext
     */
    public WB4Calc(XComponentContext m_xContext) throws WBOfficeException
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
     * @return Application version String      
     */
    @Override
    public final String getApplicationVersion()
    {
        return "2.4";
    }

    /**
     * Gets all the files that contains a cell
     * @param xcell the XCell to search the files
     * @return List of files found
     * @throws org.semanticwb.openoffice.NoHasLocationException The document has not be saved before
     * @see XCell
     */
    private final List<File> getAttachments(XCell xcell) throws NoHasLocationException
    {
        List<File> attachments = new ArrayList<File>();
        XTextFieldsSupplier xTextFieldsSupplier = (XTextFieldsSupplier) UnoRuntime.queryInterface(XTextFieldsSupplier.class, xcell);
        XEnumeration textFields = xTextFieldsSupplier.getTextFields().createEnumeration();
        while (textFields.hasMoreElements())
        {
            try
            {
                Object textField1 = textFields.nextElement();
                if (textField1 != null)
                {
                    XPropertySet xps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, textField1);
                    if (xps != null)
                    {
                        String path = xps.getPropertyValue(URL).toString();
                        attachments.addAll(this.addLink(path));
                    }
                }
            }
            catch (com.sun.star.uno.Exception nse)
            {
                ErrorLog.log(nse);
            }
        }
        return attachments;
    }

    private final List<String> getAttachmentsAsString(XCell xcell) throws NoHasLocationException
    {
        List<String> attachments = new ArrayList<String>();
        XTextFieldsSupplier xTextFieldsSupplier = (XTextFieldsSupplier) UnoRuntime.queryInterface(XTextFieldsSupplier.class, xcell);
        XEnumeration textFields = xTextFieldsSupplier.getTextFields().createEnumeration();
        while (textFields.hasMoreElements())
        {
            try
            {
                Object textField1 = textFields.nextElement();
                if (textField1 != null)
                {
                    XPropertySet xps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, textField1);
                    if (xps != null)
                    {
                        String path = xps.getPropertyValue(URL).toString();
                        attachments.add(path);
                    }
                }
            }
            catch (com.sun.star.uno.Exception nse)
            {
                ErrorLog.log(nse);
            }
        }
        return attachments;
    }

    /**
     * Gets all the files in the document
     * @return List of files in the document
     * @throws org.semanticwb.openoffice.NoHasLocationException The document has not saved before, and the document has hyperlinks relatives to the document
     */
    @Override
    public final List<File> getAllAttachments() throws NoHasLocationException
    {
        List<File> attachments = new ArrayList<File>();
        XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, this.document);
        XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
        for (String name : xSpreadsheets.getElementNames())
        {
            try
            {
                Object obSpreadsheet = xSpreadsheets.getByName(name);
                XSpreadsheet sheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, obSpreadsheet);
                XCellRangesQuery xRangesQuery = (XCellRangesQuery) UnoRuntime.queryInterface(XCellRangesQuery.class, sheet);
                XSheetCellRanges xCellRanges = xRangesQuery.queryContentCells((short) (CellFlags.VALUE | CellFlags.STRING));
                XEnumeration cells = xCellRanges.getCells().createEnumeration();
                while (cells.hasMoreElements())
                {
                    Object ocell = cells.nextElement();
                    XCell xcell = (XCell) UnoRuntime.queryInterface(XCell.class, ocell);
                    attachments.addAll(getAttachments(xcell));
                }
            }
            catch (com.sun.star.uno.Exception upe)
            {
                ErrorLog.log(upe);
            }
        }
        return attachments;
    }

    /**
     * Gets al the custom properties of the document
     * @return A Map of custum properties
     * @throws org.semanticwb.openoffice.WBException If the list of properties are more that four
     */
    @Override
    public final Map<String, String> getCustomProperties()
    {
        HashMap<String, String> properties = new HashMap<String, String>();
        XSpreadsheetDocument xtd =
                (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, this.document);

        XDocumentInfoSupplier xdis =
                (XDocumentInfoSupplier) UnoRuntime.queryInterface(XDocumentInfoSupplier.class, xtd);
        XDocumentInfo xdi = xdis.getDocumentInfo();
        for (short i = 0; i < xdi.getUserFieldCount(); i++)
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
     * @return DocumentType.Excel
     * @see DocumentType
     */
    @Override
    public final DocumentType getDocumentType()
    {
        return DocumentType.EXCEL;
    }

    /**
     * Gets the path of the fisical document
     * @return A File with the fisical path of the document
     * @throws org.semanticwb.openoffice.NoHasLocationException If the document has not been saved
     */
    @Override
    public final File getLocalPath() throws NoHasLocationException
    {
        XModel xtd = (XModel) UnoRuntime.queryInterface(XModel.class, this.document);
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
                XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
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
                throw new WBAlertException(ERROR_DOCUMENT_NOT_MODIFIED);
            }
        }
        catch (IOException ioe)
        {
            throw new WBOfficeException(ERROR_NO_SAVE, ioe);

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
    public final File saveAs(File dir, SaveDocumentFormat format) throws WBException
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
            storeProps[0].Value = CALC_FORMAT;

            storeProps[1] = new PropertyValue();
            storeProps[1].Name = OVERRIDE_OPTION;
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
                name = docFile.getName().replace(extension, EXCEL_EXTENSION);
            }
            // guarda el documento en .doc en directorio Temporal
            File DocFile = new File(dir.getPath() + File.separatorChar + name);
            PropertyValue[] storeProps = new PropertyValue[2];
            storeProps[0] = new PropertyValue();
            storeProps[0].Name = FILTER_NAME;
            storeProps[0].Value = OFFICE97_FORMAT;

            storeProps[1] = new PropertyValue();
            storeProps[1].Name = OVERRIDE_OPTION;
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
            storeProps[0].Value = "Calc8";

            storeProps[1] = new PropertyValue();
            storeProps[1].Name = OVERRIDE_OPTION;
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
     * Save the document in Html format
     * @param dir Directory to save the document
     * @return The full path of the document
     * @throws org.semanticwb.openoffice.WBException If the document can not be saved
     * @throws IllegalArgumentException If the File is a file and not a directory
     */
    @Override
    public final File saveAsHtml(File dir) throws WBException
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
                String name = docFile.getName().replace(OPENOFFICE_EXTENSION, EXCEL_EXTENSION);
                // guarda el documento en .doc en directorio Temporal
                File DocFile = new File(dir.getPath() + File.separatorChar + name);
                PropertyValue[] storeProps = new PropertyValue[2];
                storeProps[0] = new PropertyValue();
                storeProps[0].Name = FILTER_NAME;
                storeProps[0].Value = OFFICE97_FORMAT;

                storeProps[1] = new PropertyValue();
                storeProps[1].Name = OVERRIDE_OPTION;
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
                    OutputStream out = new FileOutputStream(DocFile);
                    InputStream in = new FileInputStream(docFile.getAbsoluteFile());
                    byte[] bcont = new byte[2048];
                    int read = in.read(bcont);
                    while (read != -1)
                    {
                        out.write(bcont, 0, read);
                        read = in.read(bcont);
                    }
                }
                catch (Exception e)
                {
                    String msg = "Error: detail: " + NL + e.getLocalizedMessage();
                    JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
                    throw new WBException(msg, e);
                }
                HTMLfile = new File(dir.getPath() + File.separatorChar + docFile.getName().replace(EXCEL_EXTENSION, HTML_EXTENSION));
            }

            PropertyValue[] storeProps = new PropertyValue[2];
            storeProps[0] = new PropertyValue();
            storeProps[0].Name = FILTER_NAME;
            storeProps[0].Value = HTML_EXPORT_FORMAT;

            storeProps[1] = new PropertyValue();
            storeProps[1].Name = OVERRIDE_OPTION;
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
            throw new WBOfficeException(ERROR_NO_SAVE, ioe);

        }
    }

    /**
     * Save the properties in custom properties in the document
     * @param properties Properties to save
     * @throws org.semanticwb.openoffice.WBException if the properties are more than four
     */
    @Override
    public final void saveCustomProperties(Map<String, String> properties) throws WBException
    {
        XSpreadsheetDocument xtd =
                (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, this.document);
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
        this.save();
    }

    /**
     * Prepare the html to be published
     * @param htmlFile The full path of the Html document exported by the application
     * @throws IllegalArgumentException If the path is a directory
     */
    public final void prepareHtmlFileToSend(File htmlFile)
    {
        if (htmlFile.isDirectory())
        {
            throw new IllegalArgumentException();
        }
        Map<String, String> sheets = createSheets(htmlFile);
        createTabStrip(htmlFile.getParentFile(), sheets, htmlFile.getName());
        changeContentToViewTabStrip(htmlFile);
        // TODO: Falta implementar
    }

    private void changeContentToViewTabStrip(File htmlFile)
    {
        StringBuilder content = new StringBuilder("<html>" + NL);
        content.append("<frameset rows=\"*,39\" border=0 width=0 frameborder=no framespacing=0>" + NL);
        content.append("<frame src=\"sheet000.html\" name=\"frSheet\">" + NL);
        content.append("<frame src=\"tabstrip.html\" name=\"frTabs\" marginwidth=0 marginheight=0>" + NL);
        content.append("<noframes>" + NL);
        content.append("<body>" + NL);
        content.append(java.util.ResourceBundle.getBundle("org/semanticwb/openoffice/calc/WB4Calc").getString("<P>ESTA_PÁGINA_UTILIZA_MARCOS_QUE_SU_EXPLORADOR_NO_ADMITE.</P>") + NL);
        content.append("</body></noframes>" + NL);
        content.append("</frameset>" + NL);
        content.append("</html>" + NL);
        saveContent(content, htmlFile);
    }

    private void createTabStrip(File dir, Map<String, String> sheets, String filecontentName)
    {
        File tabStrip = new File(dir.getPath() + File.separatorChar + "tabstrip.html");
        StringBuilder sheetstable = new StringBuilder();
        for (String sheetTitle : sheets.keySet())
        {
            String sheetName = sheets.get(sheetTitle);
            sheetstable.append("<td bgcolor=\"#FFFFFF\" nowrap><b><small><small>&nbsp;<a href=\"" + sheetName + ".html\" target=\"frSheet\"><font face=\"Arial\" color=\"#000000\">" + sheetTitle + "</font></a>&nbsp;</small></small></b></td>" + NL);
        }
        String tabStripFinal = tabstrip.replace("[file]", filecontentName);
        tabStripFinal = tabStripFinal.replace("[sheetstable]", sheetstable.toString());
        saveContent(tabStripFinal, tabStrip);
    }

    private void saveTable(String table, File dir, String name)
    {
        File sheet = new File(dir.getPath() + File.separatorChar + name + HTML_EXTENSION);
        saveContent(table, sheet);
    }

    private Map<String, String> createSheets(File htmlFile)
    {
        XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, this.document);
        XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
        Map<String, String> sheets = new HashMap<String, String>();
        try
        {
            String builder = loadFileAsString(htmlFile);
            int iSheet = 0;
            int posInit = 0;
            while (posInit >= 0)
            {
                posInit = builder.indexOf("<A NAME=", posInit);
                if (posInit != -1)
                {
                    posInit = builder.indexOf("<TABLE", posInit + 9);
                    if (posInit != -1)
                    {
                        int posFin = builder.indexOf("</TABLE>", posInit);
                        String table = builder.substring(posInit, posFin + 8);
                        String name = "sheet" + formatter.format(iSheet);
                        saveTable(table, htmlFile.getParentFile(), name);
                        String title = xSpreadsheets.getElementNames()[iSheet];
                        sheets.put(title, name);
                        iSheet++;
                    }
                }
                posInit = builder.indexOf("<A NAME=", posInit);
            }
        }
        catch (Exception ex)
        {
            ErrorLog.log(ex);
        }
        return sheets;
    }

    /**
     * Gets is the document is new, it means that the document has not been saved before
     * @return True if the document is new, false otherwise
     */
    public boolean isNewDocument()
    {
        XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
        return !xStorable.hasLocation();
    }

    /**
     * Gets if the document is readonly or not
     * @return True if the document is readonly or not
     */
    public boolean isReadOnly()
    {
        XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, document);
        return xStorable.isReadonly();
    }

    /**
     * Gets if the document has been modified and can be saves
     * @return True if the document has been modified, false otherwise
     */
    public boolean isModified()
    {
        XModifiable xModified = (XModifiable) UnoRuntime.queryInterface(XModifiable.class, document);
        return xModified.isModified();
    }

    /**
     * Gets the Default extension used by the application
     * @return A string with the default extension, allways returns .ods
     */
    public String getDefaultExtension()
    {
        return OPENOFFICE_EXTENSION;
    }

    @Override
    public String getPublicationExtension()
    {
        return EXCEL_EXTENSION;
    }

    public void insertLink(String url, String text)
    {
        XSpreadsheetDocument xSheetDocument =
                (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class,
                this.document);
        XModel xModel = (XModel) UnoRuntime.queryInterface(XModel.class,
                xSheetDocument);

        XCell xCell = (XCell) UnoRuntime.queryInterface(XCell.class, xModel.getCurrentSelection());
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);
        XTextCursor xTextCursor = xCellText.createTextCursor();
        XMultiServiceFactory xDocFactory = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, this.document);
        try
        {
            Object objtextfied = xDocFactory.createInstance("com.sun.star.text.TextField.URL");
            XPropertySet xTextFieldProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, objtextfied);
            xTextFieldProps.setPropertyValue(REPRESENTATION, text);
            xTextFieldProps.setPropertyValue(TARGETFRAME, _BLANK);
            xTextFieldProps.setPropertyValue(URL, url);
            XText xShapeText = (XText) UnoRuntime.queryInterface(XText.class, xCell);
            XTextContent xFieldTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTextFieldProps);
            xShapeText.insertTextContent(xTextCursor, xFieldTextContent, false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getLinks()
    {
        HashSet<String> links = new HashSet<String>();
        XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, this.document);
        XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
        for (String name : xSpreadsheets.getElementNames())
        {
            try
            {
                Object obSpreadsheet = xSpreadsheets.getByName(name);
                XSpreadsheet sheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, obSpreadsheet);
                XCellRangesQuery xRangesQuery = (XCellRangesQuery) UnoRuntime.queryInterface(XCellRangesQuery.class, sheet);
                XSheetCellRanges xCellRanges = xRangesQuery.queryContentCells((short) (CellFlags.VALUE | CellFlags.STRING));
                XEnumeration cells = xCellRanges.getCells().createEnumeration();
                while (cells.hasMoreElements())
                {
                    Object ocell = cells.nextElement();
                    XCell xcell = (XCell) UnoRuntime.queryInterface(XCell.class, ocell);
                    links.addAll(getAttachmentsAsString(xcell));
                }
            }
            catch (com.sun.star.uno.Exception upe)
            {
                ErrorLog.log(upe);
            }
            catch (Exception upe)
            {
                ErrorLog.log(upe);
            }
        }
        return links.toArray(new String[links.size()]);
    }

    @Override
    public int getCountImages()
    {
        int images = 0;

        XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, this.document);
        XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
        for (String name : xSpreadsheets.getElementNames())
        {
            try
            {
                Object obSpreadsheet = xSpreadsheets.getByName(name);
                XSpreadsheet sheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, obSpreadsheet);
                XDrawPageSupplier oDPS = (XDrawPageSupplier) UnoRuntime.queryInterface(
                        XDrawPageSupplier.class, sheet);
                XDrawPage xDrawPage = oDPS.getDrawPage();
                images += xDrawPage.getCount();

            }
            catch (com.sun.star.uno.Exception upe)
            {
                ErrorLog.log(upe);
            }
        }

        return images;
    }

    @Override
    public String getSelectedText()
    {
        return null;
    }
}
