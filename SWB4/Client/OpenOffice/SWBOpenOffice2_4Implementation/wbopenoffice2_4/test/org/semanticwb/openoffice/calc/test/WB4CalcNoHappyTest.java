
package org.semanticwb.openoffice.calc.test;



import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticwb.openoffice.DocumentType;
import org.semanticwb.openoffice.NoHasLocationException;
import org.semanticwb.openoffice.SaveDocumentFormat;
import org.semanticwb.openoffice.WBException;
import org.semanticwb.openoffice.WBOfficeException;
import org.semanticwb.openoffice.calc.WB4Calc;
import org.semanticwb.openoffice.impress.WB4Impress;



/**
 *
 * @author Edgar Chavrria
 */
public class WB4CalcNoHappyTest {
    
    private XComponentLoader xCompLoader=null;
    private PropertyValue[] loadProps;
    private XComponentContext xContext;
    private XComponent xCompDest = null;
    private XComponent xCompSrc = null;
    private XDesktop oDesktop = null;
    private File sUrlDestiny = new File("C:/NegativeTest/PruebaSave.odp");
    
    private File tempDir = new File("C:/NegativeTest/");

    
    
    @BeforeClass
    public static void setUpClass() throws Exception{
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception{
    }
    
    @Before
    public void setUp(){
        try
        {
            xContext = Bootstrap.bootstrap();

            // Obtener la factoria de servicios de OpenOffice   
            XMultiComponentFactory xMCF = xContext.getServiceManager();
            // Obtener la ventana principal (Desktop) de OpenOffice   
            Object oRawDesktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);
            oDesktop = (XDesktop) UnoRuntime.queryInterface(XDesktop.class, oRawDesktop);
            // Obtener interfaz XComponentLoader del XDesktop   
            xCompLoader = (XComponentLoader) UnoRuntime.queryInterface(com.sun.star.frame.XComponentLoader.class, oDesktop);
            // Definir URL del fichero a cargar (de destino, o sea, el que recogera las nuevas diapositivas   

            // Cargar el documento en una nueva ventana oculta del XDesktop   
            loadProps = new PropertyValue[0];
            /*loadProps[0] = new PropertyValue();
            loadProps[0].Name = "Hidden";
            loadProps[0].Value = new Boolean(false);*/
            String url = "file:///" + sUrlDestiny.getPath().replace('\\', '/');
            xCompDest = xCompLoader.loadComponentFromURL(url, "_blank", 0, loadProps);
        }
        catch (com.sun.star.uno.Exception e){
            e.printStackTrace(System.out);
        }
        catch (BootstrapException be){
            be.printStackTrace(System.out);
        }
        catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
    @After
    public void tearDown(){
        //xCompDest.dispose();
        // Opcionalmente, cerrar el ejecutable de OpenOffice (solo si no vamos a extraer nada mas)   
        oDesktop.terminate();
        xCompDest = null;
        oDesktop = null;
    //    DeleteTemporalDirectory(this.tempDir);
    }
/*    
    public void DeleteTemporalDirectory(File dir){
        File[] files = dir.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if (file.isFile())
                {
                    file.delete();
                }
                else
                {
                    DeleteTemporalDirectory(file);
                    file.delete();
                }
            }
        }
        dir.delete();
    }
    
  */  
    
    @Test(expected=NoHasLocationException.class)
    @Ignore
    public void getLocalPathTest() throws WBOfficeException, NoHasLocationException {
        try{
            String url = "private:factory/swriter";
            xCompDest = xCompLoader.loadComponentFromURL(url, "_blank", 0, loadProps);
        }               
        catch (Exception wbe){
            Assert.fail(wbe.getMessage());
        }
        
        WB4Calc write =new WB4Calc(xContext);
        write.getLocalPath();
        
    }
    
    /*deleted
    @Test
    @Ignore
    public void getCustomPropertiesTest(){
        try{
            WB4Calc writer = new WB4Calc(this.xContext);
            Map<String, String> properties = writer.getCustomProperties();
            for (String prop : properties.keySet())
            {
                System.out.println(prop + "=" + properties.get(prop));
            }
            Assert.assertEquals(properties.size(), 4);
        }
        catch (WBException wbe){
            Assert.fail(wbe.getMessage());
        }
    }

    */
    
    @Test(expected=java.lang.IllegalArgumentException.class)//the path is a file
    @Ignore
    public void saveAsHTMLTest()throws IllegalArgumentException, WBException, IOException{
        
        String url = "file:///c:/NegativeTest/TestSave.ods";
        xCompDest = xCompLoader.loadComponentFromURL(url, "_blank", 0, loadProps);
        
        try{
            WB4Calc writer = new WB4Calc(xContext);
            File actual=writer.saveAsHtml(new File("C:/NegativeTest/Document.ods"));
            
        }
        catch(WBOfficeException WBE){
            
        
        }
    }
    
    @Test(expected=WBException.class)//if cant be saved
    @Ignore
    public void saveAsHTMLTest2()throws IllegalArgumentException, WBException, IOException{

        String url = "file:///c:/NegativeTest/TestSave.ods";
        xCompDest = xCompLoader.loadComponentFromURL(url, "_blank", 0, loadProps);

        WB4Calc writer = new WB4Calc(xContext);
        File actual=writer.saveAsHtml(new File("C:/NegativeTest/ReadOnly"));

        
    }
    
     @Test(expected=WBException.class)//the document never has been save
     @Ignore
     public void saveTest1() throws WBException, IOException, IllegalArgumentException{
           
        String url = "private:factory/swriter";
        xCompDest = xCompLoader.loadComponentFromURL(url, "_blank", 0, loadProps);
      
        WB4Calc writer = new WB4Calc(this.xContext);
        writer.save();
      
    }
     
     @Test(expected=WBException.class)//the document never has been modified
     public void saveTest2() throws WBException, IOException, IllegalArgumentException{
           
        String url = "file:///C:/NegativeTest/TestSave.ods";
        xCompDest = xCompLoader.loadComponentFromURL(url, "_blank", 0, loadProps);
      
        WB4Calc writer = new WB4Calc(xContext);
        writer.save();
      
    }
     
    @Test(expected=WBException.class)//the document is read only
    public void saveTest3() throws WBException, IOException, IllegalArgumentException{
           
        String url = "file:///C:/NegativeTest/ReadOnly/TestSave.ods";
        xCompDest = xCompLoader.loadComponentFromURL(url, "_blank", 0, loadProps);
      
        WB4Calc writer = new WB4Calc(xContext);
        writer.save();
      
    }
     
    @Test(expected=java.lang.IllegalArgumentException.class)//the parameter is a file
    public void saveAsSaveDocumentFormatHTMLTest1() throws WBOfficeException, WBException, IOException, IllegalArgumentException{

        String url = "file:///C:/NegativeTest/TestSave.ods";
        xCompDest = xCompLoader.loadComponentFromURL(url, "_blank", 0, loadProps);
      
        
        WB4Calc writer = new WB4Calc(this.xContext);
        File actual=writer.saveAs(new File("C:/NegativeTest/TestSave.ods"), SaveDocumentFormat.HTML);
        
    }
    
    @Test(expected=WBException.class)//the documetn can not be saved
    public void saveAsSaveDocumentFormatHTMLTest2() throws WBOfficeException, WBException, IOException, IllegalArgumentException{

        String url = "file:///C:/NegativeTest/TestSave.ods";
        xCompDest = xCompLoader.loadComponentFromURL(url, "_blank", 0, loadProps);
      
        
        WB4Calc writer = new WB4Calc(this.xContext);
        File actual=writer.saveAs(new File("C:/NegativeTest/ReadOnly"), SaveDocumentFormat.HTML);
        
    }
    
    @Test(expected=WBException.class)
    public void saveCustomPropertiesTest() throws WBException, WBException{
        
        WB4Calc writer = new WB4Calc(this.xContext);
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("id", "1");
        properties.put("tp", "Hola");
        properties.put("X", "a");
        properties.put("Y", "Local");
        properties.put("Z", "2");

        writer.saveCustomProperties(properties);

    }
    
    
    
    
    
    
    
    

}
