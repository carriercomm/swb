/*
 * ToolBar.fx
 *
 * Created on 13/02/2010, 11:37:17 AM
 */

package org.semanticwb.process.modeler;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.layout.Flow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import applets.commons.JSONObject;
import javafx.stage.Stage;
import applets.commons.WBConnection;
import org.semanticwb.process.modeler.SubMenu;
import java.lang.Exception;
import org.semanticwb.process.modeler.StartEvent;
import org.semanticwb.process.modeler.SequenceFlow;
import org.semanticwb.process.modeler.SubProcess;
import org.semanticwb.process.modeler.ConditionalFlow;
import org.semanticwb.process.modeler.ComplexGateway;
import applets.commons.JSONArray;
import javafx.stage.AppletStageExtension;
import javafx.stage.Alert;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import org.semanticwb.process.modeler.GraphicalElement;
import org.semanticwb.process.modeler.ModelerUtils;
import java.lang.Class;
import java.lang.String;
import org.semanticwb.process.modeler.MessageStartEvent;
import org.semanticwb.process.modeler.TimerStartEvent;
import org.semanticwb.process.modeler.RuleStartEvent;
import org.semanticwb.process.modeler.SignalStartEvent;
import org.semanticwb.process.modeler.MultipleStartEvent;
import org.semanticwb.process.modeler.MessageIntermediateCatchEvent;
import org.semanticwb.process.modeler.TimerIntermediateCatchEvent;
import org.semanticwb.process.modeler.ErrorIntermediateCatchEvent;
import org.semanticwb.process.modeler.MessageIntermediateThrowEvent;
import org.semanticwb.process.modeler.CancelationIntermediateCatchEvent;
import org.semanticwb.process.modeler.RuleIntermediateCatchEvent;
import org.semanticwb.process.modeler.LinkIntermediateCatchEvent;
import org.semanticwb.process.modeler.LinkIntermediateThrowEvent;
import org.semanticwb.process.modeler.SignalIntermediateCatchEvent;
import org.semanticwb.process.modeler.SignalIntermediateThrowEvent;
import org.semanticwb.process.modeler.CompensationIntermediateCatchEvent;
import org.semanticwb.process.modeler.CompensationIntermediateThrowEvent;
import org.semanticwb.process.modeler.MultipleIntermediateCatchEvent;
import org.semanticwb.process.modeler.MultipleIntermediateThrowEvent;
import org.semanticwb.process.modeler.MessageEndEvent;
import org.semanticwb.process.modeler.ErrorEndEvent;
import org.semanticwb.process.modeler.CancelationEndEvent;
import org.semanticwb.process.modeler.SignalEndEvent;
import org.semanticwb.process.modeler.CompensationEndEvent;
import org.semanticwb.process.modeler.MultipleEndEvent;
import org.semanticwb.process.modeler.TerminationEndEvent;
import org.semanticwb.process.modeler.EventSubProcess;
import org.semanticwb.process.modeler.Activity;
import org.semanticwb.process.modeler.AdhocSubProcess;

public var counter: Integer;
public var conn:WBConnection = new WBConnection(FX.getArgument(WBConnection.PRM_JSESS).toString(),FX.getArgument(WBConnection.PRM_CGIPATH).toString(),FX.getProperty("javafx.application.codebase"));

/**
 * @author javier.solis
 */

public class ToolBar extends CustomNode
{
    public var modeler:Modeler;
    public var showHelp:Boolean;
    public var stage:Stage;
    public var x:Number;
    public var y:Number;
    public var w:Number;
    public var h:Number;
    public var simpleMode:Boolean=false;
    var dx : Number;                        //temporal drag x
    var dy : Number;                        //temporal drag y
    var isApplet:Boolean=FX.getArgument(WBConnection.PRM_CGIPATH).toString()!=null;
    var ret:Group;
    var simpleBar: Group;
    var fullBar: Group;

    var fileChooser = javax.swing.JFileChooser{};
    var xpdlFileChooser = javax.swing.JFileChooser{};
    var imageFileChooser = javax.swing.JFileChooser{};
    var hidden:Boolean = false;

    def imgDiv: Image = Image {
        url: "{__DIR__}images/barra_division.png"
    }
    def imgSpace: Image = Image {
        url: "{__DIR__}images/barra_espacio.png"
    }
    def imgTitleBar: Image = Image {
        url: "{__DIR__}images/barra_mov.png"
    }
    def imgBottomBar: Image = Image {
        url: "{__DIR__}images/barra_bottom.png"
    }

    def undoMgr: UndoMgr = UndoMgr
    {
       override public function getState () : String {
           return getProcess();
       }
    }

    public function openProcess(): Void
    {
        fileChooser.setDialogTitle(##"openTitle");
        //fileChooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
        if (fileChooser.showOpenDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION)
        {
            var file = fileChooser.getSelectedFile();
            //println(file);
            try
            {
                var in=new FileInputStream(file);
                var proc=WBConnection.readEncInputStream(in,"UTF-8");
                //println(proc);
                delete modeler.contents;
                modeler.containerElement=null;
                createProcess(proc);
            }catch(e:Exception){Alert.inform("Error",e.getMessage());}
        }
    }

    public function saveAsImage(): Void
    {
        //fileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        if (imageFileChooser.showSaveDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION) {
            var file = imageFileChooser.getSelectedFile();
            if(not file.getName().toLowerCase().endsWith("png"))
            {
                file=new File("{file.getPath()}.png");
            }
            //println(file);
            var bufferedImage=modeler.renderToImage(25);
            //println(bufferedImage);
            try
            {
                var out=new FileOutputStream(file);
                def bufferedOutputStream = new BufferedOutputStream(out);
                //println(bufferedOutputStream);
                javax.imageio.ImageIO.write( bufferedImage, "PNG", bufferedOutputStream );
                bufferedOutputStream.close();
                out.close();
                //println("end");
            }catch(e:Exception)
            {
                //println(e.getMessage());
                Alert.inform("Error",e.getMessage());
            }
        }
    }

    public function saveProcess(): Void
    {
        var valCode = validateProcessModel();
        var confirmError = ##"msgModelErrorConfirm";
        var invalidMsg = ##"msgModelError";
        if (valCode.equals("OK") or (not valCode.equals("OK") and Alert.confirm("SemanticWebBuilder Process", "{invalidMsg} {valCode}. {confirmError}"))) {
            fileChooser.setDialogTitle(##"saveTitle");
            if (fileChooser.showSaveDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION)
            {
                var file = fileChooser.getSelectedFile();
                if(not file.getName().toLowerCase().endsWith("swp"))
                {
                    file=new File("{file.getPath()}.swp");
                }

                //println(file);
                var proc=getProcess();
                try
                {
                    var out=new FileOutputStream(file);
                    out.write(proc.getBytes("UTF-8"));
                    out.close();
                    //var o= new ObjectOutputStream(out);
                    //o.writeObject(modeler);
                    //o.close();
                }catch(e:Exception){Alert.inform("Error",e.getMessage());}
            }
        }
    }

    public function storeProcess(): Void
    {
        var valCode = validateProcessModel();
        var confirmError = ##"msgModelErrorConfirm";
        var invalidMsg = ##"msgModelError";
        if (valCode.equals("OK") or (not valCode.equals("OK") and Alert.confirm("SemanticWebBuilder Process", "{invalidMsg} {valCode}. {confirmError}"))) {
            var process=getProcess();
            //var processString = WBXMLParser.encode("JSONSTART{process}JSONEND","UTF8");
            var processString = "JSONSTART{process}JSONEND";

            //var comando="<?xml version=\"1.0\" encoding=\"UTF-8\"?><req><cmd>updateModel</cmd><json>{WBXMLParser.encode(processString,"UTF8")}</json></req>";
            var comando="<?xml version=\"1.0\" encoding=\"UTF-8\"?><req><cmd>updateModel</cmd><json>{processString}</json></req>";
            //println("applet updateModel - JSON ENVIADO:");
            //println(processString);
            var data=conn.getEncData(comando);
            AppletStageExtension.eval("parent.reloadTreeNodeByURI('{conn.getUri()}')");
            if(data.indexOf("OK")>0)
            {
                Alert.inform("SemanticWebBuilder Process",##"msgSent");
                loadProcess();
            }else
            {
                Alert.inform("Error",data);
            }
        }
    }

    public function loadProcess(): Void
    {
        try
        {
            var comando="<?xml version=\"1.0\" encoding=\"UTF-8\"?><req><cmd>getProcessJSON</cmd></req>";
            var json=conn.getEncData(comando);
            //println("applet getProcessJSON - JSON RECIBIDO:");
            //println(json);
            //println("json:{json}");
            delete modeler.contents;
            modeler.containerElement=null;
            createProcess(json);
        }catch(e:Exception){println(e);}

    }

    public function validateProcessModel() : String {
        var ret = "OK";
        for (node in modeler.contents) {
            if (node instanceof GraphicalElement) {
                if (node instanceof Activity or node instanceof Event or node instanceof Gateway) {
                    if (not (node instanceof EventSubProcess)) {
                        var ge = node as GraphicalElement;
                        if (not (ge.getContainer() instanceof AdhocSubProcess)) {
                            //Validate node's connectivity
                            if (ge.getInputConnectionObjects().isEmpty() and ge.getOutputConnectionObjects().isEmpty()) {
                                ret = ##"msgModelErrorUlinked";
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    /**
    * Serialyze the process to JSON
    */
    public function getProcess(): String
    {
        var obj:JSONObject =new JSONObject();
        obj.put("uri","test");
        var nodes:JSONArray =new JSONArray();
        obj.putOpt("nodes",nodes);
        for(node in modeler.contents)
        {
            var ele=getJSONObject(node);
            nodes.put(ele);

            if(node instanceof Pool)
            {
                var pool=node as Pool;
                for(lane in pool.lanes)
                {
                    nodes.put(getJSONObject(lane));
                }
            }
        }
        return obj.toString();
    }

    function getClassName(cls:Class) : String
    {
        var name=cls.getSimpleName();
        var i=name.indexOf("$");
        if(i>0)     //por lo menos un caracter
        {
            name=name.substring(0,i);
        }
        //println("name:{name}");
        return name;
    }


    function getJSONObject(node:Node):JSONObject
    {
        var ele:JSONObject=new JSONObject();
        if(node instanceof GraphicalElement)
        {
           var ge=node as GraphicalElement;
           //println("{ge.getClass().getName()} {ge.getClass().getCanonicalName()} {ge.getClass().getPackage()} {ge.getClass().getSimpleName()}");
           ele.put("class",getClassName(ge.getClass()));
           ele.put("container",ge.getContainer().uri);
           ele.put("parent",ge.getGraphParent().uri);
           ele.put("title",ge.title);
           ele.put("labelSize",ge.text.getSize());
           ele.put("description",ge.description);
           //ele.put("type",ge.type);
           ele.put("uri",ge.uri);
           ele.put("x",ge.x);
           ele.put("y",ge.y);
           ele.put("w",ge.w);
           ele.put("h",ge.h);
           ele.put("isMultiInstance",ge.isMultiInstance);
           ele.put("isSequentialMultiInstance",ge.isSequentialMultiInstance);
           ele.put("isLoop", ge.isLoop);
           ele.put("isForCompensation", ge.isForCompensation);
           ele.put("isInterrupting", ge.isInterrupting);
           if (ge instanceof DataObject) {
               ele.put("isCollection", (ge as DataObject).isCollection);
           }
           if (ge instanceof Lane) {
               var l = ge as Lane;
               ele.put("index", l.idx);
           }
        }

        if(node instanceof ConnectionObject)
        {
           var ge=node as ConnectionObject;
           ele.put("class",getClassName(ge.getClass()));
           ele.put("uri",ge.uri);
           ele.put("title",ge.title);
           ele.put("start",ge.ini.uri);
           ele.put("end",ge.end.uri);
           if(node instanceof ConditionalFlow)
           {
               //var con=node as ConditionalFlow;
               ele.put("action", ge.action);
           }
           var points = "";
           for (handle in ge.handles) {
               points = "{points}{handle.x},{handle.y}|";
           }
           ele.put("connectionPoints", points);
        }
        return ele;
    }


    /**
    * Increment the internal counter for new uris
    */
    function validateUri(uri:String):String
    {
        if(uri.startsWith("new:"))
        {
            var c=Integer.parseInt(uri.substring(uri.lastIndexOf(":")+1));
            if(c>counter)counter=c+1;
        }
        return uri;
    }

    /**Sustituye los nombres de clases de versiones anteriores de Modeler*/
    function assertOldClassName(className: String) : String {
        var ret = className;
        if (className.equals("Artifact")) {
            ret = "DataObject";
        } else if (className.equals("InputArtifact")) {
            ret = "DataInput";
        } else if (className.equals("OutputArtifact")) {
            ret = "DataOutput";
        } else if (className.equals("DataStoreArtifact")) {
            ret = "DataStore";
        } else if (className.equals("CollectionArtifact")) {
            ret = "DataObject";
        }
        return ret;
    }

    /**
    * Create a process from a JSON
    */
    public function createProcess(json:String): Void
    {
        //println("Arguments:{FX.getArgument}");

        var pkg:String="org.semanticwb.process.modeler";

        var jsobj=new JSONObject(json);
        var jsarr = jsobj.getJSONArray("nodes");
        var i=0;
        //GraphicElements
        while(i<jsarr.length())
        {
            //generic
            var js = jsarr.getJSONObject(i);
            var _cls = js.getString("class");
            if (_cls.equals("CollectionArtifact")) {
                js.put("isCollection", true);
            }

            var cls:String="{pkg}.{assertOldClassName(_cls)}";
            var uri:String=validateUri(js.getString("uri"));
            var clss=getClass().forName(cls);
            var node=clss.newInstance() as Node;
            var ge:GraphicalElement=null;
            var labelSize:Number;
            labelSize = js.optInt("labelSize", 10);
            if(node instanceof GraphicalElement)
            {
                ge=node as GraphicalElement;
            }

            if(ge!=null and not (ge instanceof Lane))
            {
                var title=js.getString("title");
                var description=js.optString("description", "");
                var isLoop=js.optBoolean("isLoop", false);
                var isMultiInstance=js.optBoolean("isMultiInstance", false);
                var isSequentialMultiInstance=js.optBoolean("isSequentialMultiInstance", false);
                var isForCompensation=js.optBoolean("isForCompensation", false);
                var isInterrupting=js.optBoolean("isInterrupting", false);
                //var type=js.getString("type");
                var x=js.getInt("x");
                var y=js.getInt("y");
                var w=js.getInt("w");
                var h=js.getInt("h");
                if (ge instanceof DataObject) {
                    var isCollection = js.optBoolean("isCollection", false);
                    (ge as DataObject).isCollection = isCollection;
                }

                ge.modeler=modeler;
                ge.uri=uri;
                ge.isLoop = isLoop;
                ge.isMultiInstance = isMultiInstance;
                ge.isSequentialMultiInstance = isSequentialMultiInstance;
                ge.isForCompensation = isForCompensation;
                ge.isInterrupting = isInterrupting;
                //println("uri:{ge.uri}");
                ge.title=title;
                ge.text.setSize(labelSize);
                ge.description=description;
                //ge.setType(type);
                ge.x=x;
                ge.y=y;
                if(w>0)ge.w=w;
                if(h>0)ge.h=h;
                if(ge instanceof Pool)
                {
                    modeler.addFirst(ge);
                }else
                {
                    modeler.add(ge);
                }
                //println("jsobj:{js.toString()}, i: {i}");
            }
            i++;
        }


        //Lanes
        i=0;
        while(i<jsarr.length())
        {
            //generic
            var js = jsarr.getJSONObject(i);
            var _cls = js.getString("class");
            var cls:String="{pkg}.{assertOldClassName(_cls)}";
            var uri:String=validateUri(js.getString("uri"));

            var clss=getClass().forName(cls);
            var node=clss.newInstance() as Node;

            if(node instanceof Lane)
            {                
                var parent=js.getString("parent");
                var title=js.getString("title");
                var idx = js.optString("index", "-1");
                //println("Datos recuperados -> parent: {parent}, title: {title}, index: {idx}");
                //var type=js.getString("type");
                var h=js.getInt("h");
                var p=modeler.getGraphElementByURI(parent);
                if(p instanceof Pool)
                {
                    var pool=p as Pool;
                    var lane=pool.addLane();
                    lane.title=title;
                    //lane.type=type;
                    lane.h=h;
                    lane.uri=uri;
                    if (not idx.equals("-1")) {
                        var iidx = js.getInt("index");
                        lane.idx = iidx;
                        pool.sortLanes();
                    }
                }
            }
            i++;
        }

        //ConnectionObjects
        i=0;
        while(i<jsarr.length())
        {
            //generic
            var js = jsarr.getJSONObject(i);
            var _cls = js.getString("class");
            var cls:String="{pkg}.{assertOldClassName(_cls)}";
            var uri:String=validateUri(js.getString("uri"));

            var clss=getClass().forName(cls);
            var node=clss.newInstance() as Node;

            //Parents
            var ge:GraphicalElement=null;
            if(node instanceof GraphicalElement)
            {
                ge=modeler.getGraphElementByURI(uri);
            }

            if(ge!=null)
            {
                var parent=js.getString("parent");
                ge.setGraphParent(modeler.getGraphElementByURI(parent));
                var container=js.getString("container");
                ge.setContainer(modeler.getGraphElementByURI(container));
                //println("{ge} parent:{ge.getGraphParent()}");
            }

            //Connections
            var co:ConnectionObject=null;
            var co2: ConnectionObject=null;
            if(node instanceof ConnectionObject)
            {
                co=node as ConnectionObject;                
            }

            if(co!=null)
            {
                //ConnectionObjects
                var start=js.getString("start");
                var end=js.getString("end");
                var title=js.getString("title");
                var points: String = js.optString("connectionPoints", null);
                var s: String[];                

                co.modeler=modeler;
                co.uri=uri;
                co.title=title;
                co.ini=modeler.getGraphElementByURI(start);
                co.end=modeler.getGraphElementByURI(end);
                if (points != null and not (points.trim().equals(""))) {
                    s = points.split("\\|");
                    for (_s in s) {
                        var p = Point.fromString(_s);
                        if (p != null) {
                            co.addLineHandler(p, false);
                        }
                    }
                }

                modeler.add(co);
                co.updatePoints();
                //println("jsobj:{js.toString()}, i: {i}");
            }
            i++;
        }
    }

    public override function create(): Node
    {
        //println("charset:{Charset.defaultCharset()}");
        //println("áéíóú");

        var filter = FileFilter{};
        var imgFilter = ImageFileFilter{};
        var xpdlFilter = XPDLFileFilter{};
        fileChooser.setFileFilter(filter);
        imageFileChooser.setFileFilter(imgFilter);
        xpdlFileChooser.setFileFilter(xpdlFilter);
        imageFileChooser.setDialogTitle(##"saveImageTitle");
        xpdlFileChooser.setDialogTitle(##"saveXPDLTitle");
        if(isApplet)loadProcess();
        
        var file:SubMenu=SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"file"
            image: "images/file1.png"
            imageOver: "images/file2.png"
            imageClicked: "images/file3.png"
            visible: bind not hidden
            buttons: [
                ImgButton {
                    text: ##"new"//ModelerUtils.getLocalizedString("new")
                    image: "images/file_nuevo1.png"
                    imageOver: "images/file_nuevo2.png"
                    action: function():Void
                    {
                        var tit = ##"alertMsg";
                        var msg = ##"msgSaveAlert";
                        if(modeler.contents != null and not modeler.contents.isEmpty()) {
                            if (Alert.confirm(tit, msg)) {
                                ModelerUtils.clickedNode=null;
                                ModelerUtils.setResizeNode(null);
                                modeler.containerElement=null;
                                modeler.disablePannable=false;
                                delete modeler.contents;
                            }
                        } else {
                            ModelerUtils.clickedNode=null;
                            ModelerUtils.setResizeNode(null);
                            modeler.containerElement=null;
                            modeler.disablePannable=false;
                            delete modeler.contents;
                        }
                        file.closeSubMenu();
                    }
                },
                ImgButton {
                    text: ##"open"//ModelerUtils.getLocalizedString("open")
                    image: "images/file_abrir1.png"
                    imageOver: "images/file_abrir2.png"
                    action: function():Void
                    {
                        file.closeSubMenu();
                        var tit = ##"alertMsg";
                        var msg = ##"msgSaveAlert";
                        if(modeler.contents != null and not modeler.contents.isEmpty()) {
                            if (Alert.confirm(tit, msg)) {
                                ModelerUtils.clickedNode=null;
                                ModelerUtils.setResizeNode(null);
                                modeler.containerElement=null;
                                modeler.disablePannable=false;
                                openProcess();
                            }
                        } else {
                            ModelerUtils.clickedNode=null;
                            ModelerUtils.setResizeNode(null);
                            modeler.containerElement=null;
                            modeler.disablePannable=false;
                            openProcess();
                        }
                    }
                },
                ImgButton {
                    text: ##"save"//ModelerUtils.getLocalizedString("save")
                    image: "images/file_guardar1.png"
                    imageOver: "images/file_guardar2.png"
                    action: function():Void
                    {
                        ModelerUtils.clickedNode=null;
                        modeler.disablePannable=false;
                        saveProcess();
                    }
                },
                ImgButton {
                    text: ##"send"//ModelerUtils.getLocalizedString("new")
                    image: bind if (isApplet) "images/file_enviar1.png" else "images/v_separator.png"
                    imageOver: bind if (isApplet) "images/file_enviar2.png" else "images/v_separator.png"
                    action: bind if (isApplet) function():Void {
                            ModelerUtils.clickedNode=null;
                            modeler.disablePannable=false;
                            storeProcess();
                        }
                        else function ():Void {}
                },
                ImgButton {
                    text: ##"export"//ModelerUtils.getLocalizedString("export")
                    image: "images/file_saveasimage1.png"
                    imageOver: "images/file_saveasimage2.png"
                    action: function():Void
                    {
                        ModelerUtils.clickedNode=null;
                        modeler.disablePannable=false;
                        saveAsImage();
                    }
                },
                ImgButton {
                    text: ##"exportxpdl"//ModelerUtils.getLocalizedString("export")
                    image: "images/file_saveasxpdl1.png"
                    imageOver: "images/file_saveasxpdl2.png"
                    action: function():Void
                    {
                        ModelerUtils.clickedNode=null;
                        modeler.disablePannable=false;
                        if (xpdlFileChooser.showSaveDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION)
                        {
                            var file = xpdlFileChooser.getSelectedFile();
                            if(not file.getName().toLowerCase().endsWith("xpdl"))
                            {
                                file=new File("{file.getPath()}.xpdl");
                            }

                            try
                            {
                                var trans = XPDLTransformer{};
                                trans.getXPDLDocument(modeler);
                                trans.saveXPDL(file);
                            }catch(e:Exception){Alert.inform("Error",e.getMessage());}
                        }
                    }
                },
                /*ImgButton {
                    text: ##"print"//ModelerUtils.getLocalizedString("print")
                    image: "images/file_print1.png"
                    imageOver: "images/file_print2.png"
                    action: function():Void
                    {
                        var print=PrintUtil{};
                        var aux=modeler.containerElement;
                        modeler.containerElement=null;
                        var arr=[modeler.renderToImage(1)];
                        for(node in modeler.contents)
                        {
                            if(node instanceof GraphicalElement)
                            {
                                var ge=node as GraphicalElement;
                                if(ge.containerable)
                                {
                                    modeler.containerElement=ge;
                                    insert modeler.renderToImage(1) into arr;
                                }
                            }
                        }
                        modeler.containerElement=aux;
                        //print.print(arr);
                        //TODO:
                    }
                },*/
                ImgButton {
                    text: ##"about"//ModelerUtils.getLocalizedString("about")
                    image: "images/file_about1.png"
                    imageOver: "images/file_about2.png"
                    action: function():Void
                    {
                        ModelerUtils.clickedNode=null;
                        modeler.disablePannable=false;
                        ModelerUtils.splash.showDialog(modeler.width/2,modeler.height/2);
                    }
                },
            ]
        };

        var task=SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"task"
            toolTipText: bind if(showHelp) ##"taskDescription" else ""
            image: "images/task_1.png"
            imageOver: "images/task_2.png"
            imageClicked: "images/task_3.png"
            visible: bind not hidden
            buttons: [
                ImgButton {
                    text: ##"abstractTask"//ModelerUtils.getLocalizedString("abstractTask")
                    toolTipText: bind if(showHelp) ##"abstractTaskDescription" else ""
                    image: "images/task_normal1.png"
                    imageOver: "images/task_normal2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=Task
                        {
                            modeler:modeler
                            title: ##"abstractTask"//ModelerUtils.getLocalizedString("abstractTask")
                            uri:"new:task:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"userTask"//ModelerUtils.getLocalizedString("userTask")
                    toolTipText: bind if(showHelp) ##"userTaskDescription" else ""
                    image: "images/task_usr1.png"
                    imageOver: "images/task_usr2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=UserTask
                        {
                            modeler:modeler
                            title: ##"userTask"//ModelerUtils.getLocalizedString("userTask")
                            uri:"new:usertask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"serviceTask"//ModelerUtils.getLocalizedString("serviceTask")
                    toolTipText: bind if(showHelp) ##"serviceTaskDescription" else ""
                    image: "images/task_servicio1.png"
                    imageOver: "images/task_servicio2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ServiceTask
                        {
                            modeler:modeler
                            title: ##"serviceTask"//ModelerUtils.getLocalizedString("serviceTask")
                            uri:"new:servicetask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"scriptTask"//ModelerUtils.getLocalizedString("scriptTask")
                    toolTipText: bind if(showHelp) ##"scriptTaskDescription" else ""
                    image: "images/task_script1.png"
                    imageOver: "images/task_script2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ScriptTask
                        {
                            modeler:modeler
                            title: ##"scriptTask"//ModelerUtils.getLocalizedString("scriptTask")
                            uri:"new:scripttask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"ruleTask"//ModelerUtils.getLocalizedString("scriptTask")
                    toolTipText: bind if(showHelp) ##"ruleTaskDescription" else ""
                    image: "images/task_rule1.png"
                    imageOver: "images/task_rule2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=BusinessRuleTask
                        {
                            modeler:modeler
                            title: ##"ruleTask"//ModelerUtils.getLocalizedString("scriptTask")
                            uri:"new:ruletask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"manualTask"//ModelerUtils.getLocalizedString("manualTask")
                    toolTipText: bind if(showHelp) ##"manualTaskDescription" else ""
                    image: "images/task_manual1.png"
                    imageOver: "images/task_manual2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ManualTask
                        {
                            modeler:modeler
                            title: ##"manualTask"//ModelerUtils.getLocalizedString("manualTask")
                            uri:"new:manualtask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"sendTask"//ModelerUtils.getLocalizedString("sendTask")
                    toolTipText: bind if(showHelp) ##"sendTaskDescription" else ""
                    image: "images/task_envio1.png"
                    imageOver: "images/task_envio2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=SendTask
                        {
                            modeler:modeler
                            title: ##"sendTask"//ModelerUtils.getLocalizedString("sendTask")
                            uri:"new:sendtask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"receiveTask"//ModelerUtils.getLocalizedString("receiveTask")
                    toolTipText: bind if(showHelp) ##"receiveTaskDescription" else ""
                    image: "images/task_recive1.png"
                    imageOver: "images/task_recive2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ReceiveTask
                        {
                            modeler:modeler
                            title: ##"receiveTask"//ModelerUtils.getLocalizedString("receiveTask")
                            uri:"new:receivetask:{counter++}"
                        }
                    }
                },
            ]
        };

        var subtask=SubMenu
        {
            modeler: modeler
            toolBar:this
            toolTipText: bind if (showHelp) ##"subTaskDescription" else ""
            text: ##"subTask"//ModelerUtils.getLocalizedString("subTask")
            image: "images/subtask_1.png"
            imageOver: "images/subtask_2.png"
            imageClicked: "images/subtask_3.png"
            visible: bind not hidden
            buttons: [
                ImgButton {
                    text: ##"subProcess"//ModelerUtils.getLocalizedString("subProcess")
                    toolTipText: bind if(showHelp) ##"subProcessDescription" else ""
                    image: "images/subtask_colapsado1.png"
                    imageOver: "images/subtask_colapsado2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=SubProcess
                        {
                            modeler:modeler
                            title: ##"subProcess"//ModelerUtils.getLocalizedString("subProcess")
                            uri:"new:subprocess:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"adhocSubProcess"//ModelerUtils.getLocalizedString("adhocSubProcess")
                    toolTipText: bind if(showHelp) ##"adhocSubProcessDescription" else ""
                    image: "images/subtask_adhoc+1.png"
                    imageOver: "images/subtask_adhoc+2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=AdhocSubProcess
                        {
                            modeler:modeler
                            title: ##"adhocSubProcess"//ModelerUtils.getLocalizedString("adhocSubProcess")
                            uri:"new:adhocsubprocess:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"eventSubProcess"//ModelerUtils.getLocalizedString("eventSubProcess")
                    toolTipText: bind if(showHelp) ##"eventSubProcessDescription" else ""
                    image: "images/subtask_evento1.png"
                    imageOver: "images/subtask_evento2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=EventSubProcess
                        {
                            modeler:modeler
                            title: ##"eventSubProcess"//ModelerUtils.getLocalizedString("eventSubProcess")
                            uri:"new:eventsubprocess:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"transaction"//ModelerUtils.getLocalizedString("transaction")
                    toolTipText: bind if(showHelp) ##"transactionDescription" else ""
                    image: "images/subtask_transaccion1.png"
                    imageOver: "images/subtask_transaccion2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=TransactionSubProcess
                        {
                            modeler:modeler
                            title: ##"transaction"//ModelerUtils.getLocalizedString("transaction")
                            uri:"new:transactionsubprocess:{counter++}"
                        }
                    }
                },
            ]
        };

        var callactivity=SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"callActivity"//ModelerUtils.getLocalizedString("subTask")
            toolTipText: bind if (showHelp) ##"callActivityDescription" else ""
            image: "images/task_call_1.png"
            imageOver: "images/task_call_2.png"
            imageClicked: "images/task_call_3.png"
            visible: bind not hidden
            buttons: [
                ImgButton {
                    text: ##"callTask"//ModelerUtils.getLocalizedString("abstractTask")
                    toolTipText: bind if(showHelp) ##"callTaskDescription" else ""
                    image: "images/task_llamada1.png"
                    imageOver: "images/task_llamada2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=CallTask
                        {
                            modeler:modeler
                            title: ##"callTask"//ModelerUtils.getLocalizedString("abstractTask")
                            uri:"new:calltask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"callManualTask"//ModelerUtils.getLocalizedString("abstractTask")
                    toolTipText: bind if(showHelp) ##"callManualTaskDescription" else ""
                    image: "images/task_call_manual1.png"
                    imageOver: "images/task_call_manual2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=CallManualTask
                        {
                            modeler:modeler
                            title: ##"callManualTask"//ModelerUtils.getLocalizedString("abstractTask")
                            uri:"new:callmanualtask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"callRulelTask"//ModelerUtils.getLocalizedString("abstractTask")
                    toolTipText: bind if(showHelp) ##"callRulelTaskDescription" else ""
                    image: "images/task_call_rule1.png"
                    imageOver: "images/task_call_rule2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=CallBusinessRuleTask
                        {
                            modeler:modeler
                            title: ##"callRulelTask"//ModelerUtils.getLocalizedString("abstractTask")
                            uri:"new:callbusinessruletask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"callScriptTask"//ModelerUtils.getLocalizedString("abstractTask")
                    toolTipText: bind if(showHelp) ##"callScriptTaskDescription" else ""
                    image: "images/task_call_script1.png"
                    imageOver: "images/task_call_script2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=CallScriptTask
                        {
                            modeler:modeler
                            title: ##"callScriptTask"//ModelerUtils.getLocalizedString("abstractTask")
                            uri:"new:callscripttask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"callUserTask"//ModelerUtils.getLocalizedString("abstractTask")
                    toolTipText: bind if(showHelp) ##"callUserTaskDescription" else ""
                    image: "images/task_call_usr1.png"
                    imageOver: "images/task_call_usr2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=CallUserTask
                        {
                            modeler:modeler
                            title: ##"callUserTask"//ModelerUtils.getLocalizedString("abstractTask")
                            uri:"new:callusertask:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"callProcess"//ModelerUtils.getLocalizedString("abstractTask")
                    toolTipText: bind if(showHelp) ##"callProcessDescription" else ""
                    image: "images/subtask_llamada1.png"
                    imageOver: "images/subtask_llamada2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=CallProcess
                        {
                            modeler:modeler
                            title: ##"callProcess"//ModelerUtils.getLocalizedString("abstractTask")
                            uri:"new:callprocess:{counter++}"
                        }
                    }
                },
            ]
        };

        var startEvent=SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"startEvents"//ModelerUtils.getLocalizedString("startEvents")
            toolTipText: bind if (showHelp) ##"startEventDescription" else ""
            image: "images/start_1.png"
            imageOver: "images/start_2.png"
            imageClicked: "images/start_3.png"
            visible: bind not hidden
            buttons: [
                ImgButton {
                    text: ##"normalStart"//ModelerUtils.getLocalizedString("normalStart")
                    toolTipText: bind if (showHelp) ##"normalStartDescription" else ""
                    image: "images/start_normal1.png"
                    imageOver: "images/start_normal2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=StartEvent
                        {
                            modeler:modeler
                            title: ##"normalStart"//ModelerUtils.getLocalizedString("normalStart")
                            uri:"new:startevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"messageStart"//ModelerUtils.getLocalizedString("messageStart")
                    toolTipText: bind if (showHelp) ##"messageStartDescription" else ""
                    image: "images/start_msj1.png"
                    imageOver: "images/start_msj2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=MessageStartEvent
                        {
                            modeler:modeler
                            title: ##"messageStart"//ModelerUtils.getLocalizedString("messageStart")
                            uri:"new:startevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"timerStart"//ModelerUtils.getLocalizedString("timerStart")
                    toolTipText: bind if (showHelp) ##"timerStartDescription" else ""
                    image: "images/start_tmp1.png"
                    imageOver: "images/start_tmp2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=TimerStartEvent
                        {
                            modeler:modeler
                            title: ##"timerStart"//ModelerUtils.getLocalizedString("timerStart")
                            uri:"new:startevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"ruleStart"//ModelerUtils.getLocalizedString("ruleStart")
                    toolTipText: bind if (showHelp) ##"ruleStartDescription" else ""
                    image: "images/start_cond1.png"
                    imageOver: "images/start_cond2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=RuleStartEvent
                        {
                            modeler:modeler
                            title: ##"ruleStart"//ModelerUtils.getLocalizedString("ruleStart")
                            uri:"new:startevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"signalStart"//ModelerUtils.getLocalizedString("signalStart")
                    toolTipText: bind if (showHelp) ##"signalStartDescription" else ""
                    image: "images/start_senal1.png"
                    imageOver: "images/start_senal2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=SignalStartEvent
                        {
                            modeler:modeler
                            title: ##"signalStart"//ModelerUtils.getLocalizedString("signalStart")
                            uri:"new:startevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"multipleStart"//ModelerUtils.getLocalizedString("multipleStart")
                    toolTipText: bind if (showHelp) ##"multipleStartDescription" else ""
                    image: "images/start_multi1.png"
                    imageOver: "images/start_multi2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=MultipleStartEvent
                        {
                            modeler:modeler
                            title: ##"multipleStart"//ModelerUtils.getLocalizedString("multipleStart")
                            uri:"new:startevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"parallelStart"//ModelerUtils.getLocalizedString("parallelStart")
                    toolTipText: bind if (showHelp) ##"parallelStartDescription" else ""
                    image: "images/start_paralelo1.png"
                    imageOver: "images/start_paralelo2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ParallelStartEvent
                        {
                            modeler:modeler
                            title: ##"parallelStart"//ModelerUtils.getLocalizedString("parallelStart")
                            uri:"new:startevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"scalationStart"//ModelerUtils.getLocalizedString("scalationStart")
                    toolTipText: bind if (showHelp) ##"scalationStartDescription" else ""
                    image: "images/start_escala1.png"
                    imageOver: "images/start_escala2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ScalationStartEvent
                        {
                            modeler:modeler
                            title: ##"scalationStart"//ModelerUtils.getLocalizedString("scalationStart")
                            uri:"new:startevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"errorStart"//ModelerUtils.getLocalizedString("errorStart")
                    toolTipText: bind if (showHelp) ##"errorStartDescription" else ""
                    image: "images/start_error1.png"
                    imageOver: "images/start_error2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ErrorStartEvent
                        {
                            modeler:modeler
                            title: ##"errorStart"//ModelerUtils.getLocalizedString("errorStart")
                            uri:"new:startevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"compensationStart"//ModelerUtils.getLocalizedString("compensationStart")
                    toolTipText: bind if (showHelp) ##"compensationStartDescription" else ""
                    image: "images/start_compensa1.png"
                    imageOver: "images/start_compensa2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=CompensationStartEvent
                        {
                            modeler:modeler
                            title: ##"compensationStart"//ModelerUtils.getLocalizedString("compensationStart")
                            uri:"new:startevent:{counter++}"
                        }
                    }
                }
            ]
        };

        var interEvent=SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"interEvents"//ModelerUtils.getLocalizedString("interEvents")
            toolTipText: bind if (showHelp) ##"interEventsDescription" else ""
            image:"images/inter_1.png"
            imageOver:"images/inter_2.png"
            imageClicked: "images/inter_3.png"
            visible: bind not hidden
            buttons: [
//                ImgButton {
//                    text: ModelerUtils.getLocalizedString("normalInter")
//                    image: "images/inter_normal1.png"
//                    imageOver: "images/inter_normal2.png"
//                    action: function():Void {
//                        modeler.disablePannable=true;
//                        modeler.tempNode=IntermediateCatchEvent
//                        {
//                            modeler:modeler
//                            title: ModelerUtils.getLocalizedString("normalInter")
//                            uri:"new:interevent:{counter++}"
//                            //type: Event.RULE;
//                        }
//                    }
//                },
                ImgButton {
                    text: ##"messageInterCatch"//ModelerUtils.getLocalizedString("messageInterCatch")
                    toolTipText: bind if (showHelp) ##"messageInterCatchDescription" else ""
                    image: "images/inter_msj_b_1.png"
                    imageOver: "images/inter_msj_b_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=MessageIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"messageInterCatch"//ModelerUtils.getLocalizedString("messageInterCatch")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"messageInterThrow"//ModelerUtils.getLocalizedString("messageInterThrow")
                    toolTipText: bind if (showHelp) ##"messageInterThrowDescription" else ""
                    image: "images/inter_msj_n_1.png"
                    imageOver: "images/inter_msj_n_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=MessageIntermediateThrowEvent
                        {
                            modeler:modeler
                            title: ##"messageInterThrow"//ModelerUtils.getLocalizedString("messageInterThrow")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"timerInter"//ModelerUtils.getLocalizedString("timerInter")
                    toolTipText: bind if (showHelp) ##"timerInterDescription" else ""
                    image: "images/inter_tmp1.png"
                    imageOver: "images/inter_tmp2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=TimerIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"timerInter"//ModelerUtils.getLocalizedString("timerInter")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"errorInter"//ModelerUtils.getLocalizedString("errorInter")
                    toolTipText: bind if (showHelp) ##"errorInterDescription" else ""
                    image: "images/inter_error1.png"
                    imageOver: "images/inter_error2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ErrorIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"errorInter"//ModelerUtils.getLocalizedString("errorInter")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"cancelInter"//ModelerUtils.getLocalizedString("cancelInter")
                    toolTipText: bind if (showHelp) ##"cancelInterDescription" else ""
                    image: "images/inter_cancel_1.png"
                    imageOver: "images/inter_cancel_2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=CancelationIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"cancelInter"
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"compensationInterCatch"//ModelerUtils.getLocalizedString("compensationInterCatch")
                    toolTipText: bind if (showHelp) ##"compensationInterCatchDescription" else ""
                    image: "images/inter_compensa_b_1.png"
                    imageOver: "images/inter_compensa_b_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=CompensationIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"compensationInterCatch"//ModelerUtils.getLocalizedString("compensationInterCatch")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"compensationInterThrow"//ModelerUtils.getLocalizedString("compensationInterThrow")
                    toolTipText: bind if (showHelp) ##"compensationInterThrowDescription" else ""
                    image: "images/inter_compensa_n_1.png"
                    imageOver: "images/inter_compensa_n_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=CompensationIntermediateThrowEvent
                        {
                            modeler:modeler
                            title: ##"compensationInterThrow"//ModelerUtils.getLocalizedString("compensationInterThrow")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"ruleInter"//ModelerUtils.getLocalizedString("ruleInter")
                    toolTipText: bind if (showHelp) ##"ruleInterDescription" else ""
                    image: "images/inter_cond1.png"
                    imageOver: "images/inter_cond2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=RuleIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"ruleInter"//ModelerUtils.getLocalizedString("ruleInter")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"linkInterCatch"//ModelerUtils.getLocalizedString("linkInterCatch")
                    toolTipText: bind if (showHelp) ##"linkInterCatchDescription" else ""
                    image: "images/inter_enlace_b_1.png"
                    imageOver: "images/inter_enlace_b_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=LinkIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"linkInterCatch"//ModelerUtils.getLocalizedString("linkInterCatch")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"linkInterThrow"//ModelerUtils.getLocalizedString("linkInterThrow")
                    toolTipText: bind if (showHelp) ##"linkInterThrowDescription" else ""
                    image: "images/inter_enlace_n_1.png"
                    imageOver: "images/inter_enlace_n_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=LinkIntermediateThrowEvent
                        {
                            modeler:modeler
                            title: ##"linkInterThrow"//ModelerUtils.getLocalizedString("linkInterThrow")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"signalInterCatch"//ModelerUtils.getLocalizedString("signalInterCatch")
                    toolTipText: bind if (showHelp) ##"signalInterCatchDescription" else ""
                    image: "images/inter_senal_b_1.png"
                    imageOver: "images/inter_senal_b_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=SignalIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"signalInterCatch"//ModelerUtils.getLocalizedString("signalInterCatch")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"signalInterThrow"//ModelerUtils.getLocalizedString("signalInterThrow")
                    toolTipText: bind if (showHelp) ##"signalInterThrowDescription" else ""
                    image: "images/inter_senal_n_1.png"
                    imageOver: "images/inter_senal_n_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=SignalIntermediateThrowEvent
                        {
                            modeler:modeler
                            title: ##"signalInterThrow"//ModelerUtils.getLocalizedString("signalInterThrow")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"multInterCatch"//ModelerUtils.getLocalizedString("multInterCatch")
                    toolTipText: bind if (showHelp) ##"multInterCatchDescription" else ""
                    image: "images/inter_multi_b_1.png"
                    imageOver: "images/inter_multi_b_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=MultipleIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"multInterCatch"//ModelerUtils.getLocalizedString("multInterCatch")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"multInterThrow"//ModelerUtils.getLocalizedString("multInterThrow")
                    toolTipText: bind if (showHelp) ##"multInterThrowDescription" else ""
                    image: "images/inter_multi_n_1.png"
                    imageOver: "images/inter_multi_n_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=MultipleIntermediateThrowEvent
                        {
                            modeler:modeler
                            title: ##"multInterThrow"//ModelerUtils.getLocalizedString("multInterThrow")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"scalInterCatch"//ModelerUtils.getLocalizedString("scalInterCatch")
                    toolTipText: bind if (showHelp) ##"scalInterCatchDescription" else ""
                    image: "images/inter_escala_b_1.png"
                    imageOver: "images/inter_escala_b_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=ScalationIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"scalInterCatch"//ModelerUtils.getLocalizedString("scalInterCatch")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"scalInterThrow"//ModelerUtils.getLocalizedString("scalInterThrow")
                    toolTipText: bind if (showHelp) ##"scalInterThrowDescription" else ""
                    image: "images/inter_escala_n_1.png"
                    imageOver: "images/inter_escala_n_2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=ScalationIntermediateThrowEvent
                        {
                            modeler:modeler
                            title: ##"scalInterThrow"//ModelerUtils.getLocalizedString("scalInterThrow")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"parallelInter"//ModelerUtils.getLocalizedString("parallelInter")
                    toolTipText: bind if (showHelp) ##"parallelInterDescription" else ""
                    image: "images/inter_paralelo1.png"
                    imageOver: "images/inter_paralelo2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=ParallelIntermediateCatchEvent
                        {
                            modeler:modeler
                            title: ##"parallelInter"//ModelerUtils.getLocalizedString("parallelInter")
                            uri:"new:interevent:{counter++}"
                        }
                    }
                }
            ]
        };

        var endEvent=SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"endEvents"//ModelerUtils.getLocalizedString("endEvents")
            toolTipText:bind if (showHelp) ##"endEventsDescription" else ""
            image: "images/end_1.png"
            imageOver: "images/end_2.png"
            imageClicked: "images/end_3.png"
            visible: bind not hidden
            buttons: [
                ImgButton {
                    text: ##"normalEnd"//ModelerUtils.getLocalizedString("normalEnd")
                    toolTipText: bind if (showHelp) ##"normalEndDescription" else ""
                    image: "images/end_normal1.png"
                    imageOver: "images/end_normal2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=EndEvent
                        {
                            modeler:modeler
                            title: ##"normalEnd"//ModelerUtils.getLocalizedString("normalEnd")
                            uri:"new:endevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"messageEnd"//ModelerUtils.getLocalizedString("messageEnd")
                    toolTipText: bind if (showHelp) ##"messageEndDescription" else ""
                    image: "images/end_msj1.png"
                    imageOver: "images/end_msj2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=MessageEndEvent
                        {
                            modeler:modeler
                            title: ##"messageEnd"//ModelerUtils.getLocalizedString("messageEnd")
                            uri:"new:endevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"errorEnd"//ModelerUtils.getLocalizedString("errorEnd")
                    toolTipText: bind if (showHelp) ##"errorEndDescription" else ""
                    image: "images/end_error1.png"
                    imageOver: "images/end_error2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ErrorEndEvent
                        {
                            modeler:modeler
                            title: ##"errorEnd"//ModelerUtils.getLocalizedString("errorEnd")
                            uri:"new:endevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"cancelEnd"//ModelerUtils.getLocalizedString("cancelEnd")
                    toolTipText: bind if (showHelp) ##"cancelEndDescription" else ""
                    image: "images/end_cancel1.png"
                    imageOver: "images/end_cancel2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=CancelationEndEvent
                        {
                            modeler:modeler
                            title: ##"cancelEnd"//ModelerUtils.getLocalizedString("cancelEnd")
                            uri:"new:endevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"compensationEnd"//ModelerUtils.getLocalizedString("compensationEnd")
                    toolTipText: bind if (showHelp) ##"compensationEndDescription" else ""
                    image: "images/end_compensa1.png"
                    imageOver: "images/end_compensa2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=CompensationEndEvent
                        {
                            modeler:modeler
                            title: ##"compensationEnd"//ModelerUtils.getLocalizedString("compensationEnd")
                            uri:"new:endevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"signalEnd"//ModelerUtils.getLocalizedString("signalEnd")
                    toolTipText: bind if (showHelp) ##"signalEndDescription" else ""
                    image: "images/end_senal1.png"
                    imageOver: "images/end_senal2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=SignalEndEvent
                        {
                            modeler:modeler
                            title: ##"signalEnd"//ModelerUtils.getLocalizedString("signalEnd")
                            uri:"new:endevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"multipleEnd"//ModelerUtils.getLocalizedString("multipleEnd")
                    toolTipText: bind if (showHelp) ##"multipleEndDescription" else ""
                    image: "images/end_multi1.png"
                    imageOver: "images/end_multi2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=MultipleEndEvent
                        {
                            modeler:modeler
                            title: ##"multipleEnd"//ModelerUtils.getLocalizedString("multipleEnd")
                            uri:"new:endevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"scalationEnd"//ModelerUtils.getLocalizedString("scalationEnd")
                    toolTipText: bind if (showHelp) ##"scalationEndDescription" else ""
                    image: "images/end_escala1.png"
                    imageOver: "images/end_escala2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ScalationEndEvent
                        {
                            modeler:modeler
                            title: ##"scalationEnd"//ModelerUtils.getLocalizedString("scalationEnd")
                            uri:"new:endevent:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"terminateEnd"//ModelerUtils.getLocalizedString("terminateEnd")
                    toolTipText: bind if (showHelp) ##"terminateEndDescription" else ""
                    image: "images/end_termina1.png"
                    imageOver: "images/end_termina2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=TerminationEndEvent
                        {
                            modeler:modeler
                            title: ##"terminateEnd"//ModelerUtils.getLocalizedString("terminateEnd")
                            uri:"new:endevent:{counter++}"
                        }
                    }
                }
            ]
        };

        var gateWay=SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"gateways"//ModelerUtils.getLocalizedString("gateways")
            toolTipText:bind if (showHelp) ##"gatewaysDescription" else ""
            image: "images/if_1.png"
            imageOver: "images/if_2.png"
            imageClicked: "images/if_3.png"
            visible: bind not hidden
            buttons: [
//                ImgButton {
//                    text:"Gateway"
//                    image: "images/gate_normal1.png"
//                    imageOver: "images/gate_normal2.png"
//                    action: function():Void
//                    {
//                        modeler.disablePannable=true;
//                        modeler.tempNode=Gateway
//                        {
//                            modeler:modeler
//                            title:"Gateway"
//                            uri:"new:gateway:{counter++}"
//                        }
//                    }
//                },
                ImgButton {
                    text: ##"xorGateway"//ModelerUtils.getLocalizedString("xorGateway")
                    toolTipText:bind if (showHelp) ##"xorGatewayDescription" else ""
                    image: "images/gate_datos1.png"
                    imageOver: "images/gate_datos2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ExclusiveGateway
                        {
                            modeler:modeler
                            title: ##"xorGateway"//ModelerUtils.getLocalizedString("xorGateway")
                            uri:"new:exclusivegateway:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"orGateway"//ModelerUtils.getLocalizedString("orGateway")
                    toolTipText: bind if (showHelp) ##"orGatewayDescription" else ""
                    image: "images/gate_inclusiva_1.png"
                    imageOver: "images/gate_inclusiva_2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=InclusiveGateway
                        {
                            modeler:modeler
                            title: ##"orGateway"//ModelerUtils.getLocalizedString("orGateway")
                            uri:"new:inclusivegateway:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"xorGatewayStart"//ModelerUtils.getLocalizedString("xorGatewayStart")
                    toolTipText: bind if (showHelp) ##"xorGatewayStartDescription" else ""
                    image: "images/gate_eventos_str_1.png"
                    imageOver: "images/gate_eventos_str_2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ExclusiveStartEventGateway
                        {
                            modeler:modeler
                            title: ##"xorGatewayStart"//ModelerUtils.getLocalizedString("xorGatewayStart")
                            uri:"new:exclusivestarteventgateway:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"evtXorGateway"//ModelerUtils.getLocalizedString("evtXorGateway");
                    toolTipText: bind if (showHelp) ##"evtXorGatewayDescription" else ""
                    image: "images/gate_eventos_int_1.png"
                    imageOver: "images/gate_eventos_int_2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ExclusiveIntermediateEventGateway
                        {
                            modeler:modeler
                            title: ##"evtXorGateway"//ModelerUtils.getLocalizedString("evtXorGateway")
                            uri:"new:exclusiveintermediateeventgateway:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"parallelGateway"//ModelerUtils.getLocalizedString("parallelGateway")
                    toolTipText: bind if (showHelp) ##"parallelGatewayDescription" else ""
                    image: "images/gate_paralela_n_1.png"
                    imageOver: "images/gate_paralela_n_2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ParallelGateway
                        {
                            modeler:modeler
                            title: ##"parallelGateway"//ModelerUtils.getLocalizedString("parallelGateway")
                            uri:"new:parallelgateway:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"parallelGatewayStart"//ModelerUtils.getLocalizedString("parallelGatewayStart")
                    toolTipText: bind if (showHelp) ##"parallelGatewayStartDescription" else ""
                    image: "images/gate_paralela_b_1.png"
                    imageOver: "images/gate_paralela_b_2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ParallelStartEventGateway
                        {
                            modeler:modeler
                            title: ##"parallelGatewayStart"//ModelerUtils.getLocalizedString("parallelGatewayStart")
                            uri:"new:parallelstarteventgateway:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"complexGateway"//ModelerUtils.getLocalizedString("complexGateway")
                    toolTipText: bind if (showHelp) ##"complexGatewayDescription" else ""
                    image: "images/gate_compleja_1.png"
                    imageOver: "images/gate_compleja_2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=ComplexGateway
                        {
                            modeler:modeler
                            title: ##"complexGateway"//ModelerUtils.getLocalizedString("complexGateway")
                            uri:"new:complexgateway:{counter++}"
                        }
                    }
                },
            ]
        }

        var sequence=SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"connObjects"//ModelerUtils.getLocalizedString("connObjects")
            toolTipText:bind if (showHelp) ##"connObjectsDescription" else ""
            image:"images/flow_1.png"
            imageOver:"images/flow_2.png"
            imageClicked: "images/flow_3.png"
            visible: bind not hidden
            buttons: [
                ImgButton {
                    text: ##"sequenceFlow"//ModelerUtils.getLocalizedString("sequenceFlow")
                    toolTipText:bind if (showHelp) ##"sequenceFlowDescription" else ""
                    image: "images/flow_normal1.png"
                    imageOver: "images/flow_normal2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=SequenceFlow
                        {
                            modeler:modeler
                            uri:"new:sequenceflow:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"condFlow"//ModelerUtils.getLocalizedString("condFlow")
                    toolTipText:bind if (showHelp) ##"condFlowDescription" else ""
                    image: "images/flow_condicion1.png"
                    imageOver: "images/flow_condicion2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=ConditionalFlow
                        {
                            modeler:modeler
                            uri:"new:conditionalflow:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"defFlow"//ModelerUtils.getLocalizedString("defFlow")
                    toolTipText:bind if (showHelp) ##"defFlowDescription" else ""
                    image: "images/flow_defecto1.png"
                    imageOver: "images/flow_defecto2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=DefaultFlow
                        {
                            modeler:modeler
                            uri:"new:defaultflow:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"msgFlow"//ModelerUtils.getLocalizedString("msgFlow")
                    toolTipText:bind if (showHelp) ##"msgFlowDescription" else ""
                    image: "images/flow_msj1.png"
                    imageOver: "images/flow_msj2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=MessageFlow
                        {
                            modeler:modeler
                            uri:"new:messageflow:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"assocFlow"//ModelerUtils.getLocalizedString("assocFlow")
                    toolTipText:bind if (showHelp) ##"assocFlowDescription" else ""
                    image: "images/doc_dir_asocia1.png"
                    imageOver: "images/doc_dir_asocia2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=AssociationFlow
                        {
                            modeler:modeler
                            uri:"new:associationflow:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"dirAssocFlow"//ModelerUtils.getLocalizedString("dirAssocFlow")
                    toolTipText:bind if (showHelp) ##"dirAssocFlowDescription" else ""
                    image: "images/doc_asocia1.png"
                    imageOver: "images/doc_asocia2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=DirectionalAssociation
                        {
                            modeler:modeler
                            uri:"new:dirassociationflow:{counter++}"
                        }
                    }
                },
            ]
        }

        var artifacts=SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"artifacts"//ModelerUtils.getLocalizedString("artifacts")
            toolTipText: bind if (showHelp) ##"artifactsDescription" else ""
            image: "images/anota_1.png"
            imageOver: "images/anota_2.png"
            imageClicked: "images/anota_3.png"
            visible: bind not hidden
            buttons: [
                ImgButton {
                    text: ##"textAnnotation"//ModelerUtils.getLocalizedString("textAnnotation")
                    toolTipText: bind if (showHelp) ##"textAnnotationDescription" else ""
                    image: "images/doc_anota1.png"
                    imageOver: "images/doc_anota2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=AnnotationArtifact
                        {
                            modeler:modeler
                            title: ##"textAnnotation"//ModelerUtils.getLocalizedString("textAnnotation")
                            uri:"new:annotationartifact:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"group"//ModelerUtils.getLocalizedString("group")
                    toolTipText: bind if (showHelp) ##"groupDescription" else ""
                    image: "images/doc_grupo1.png"
                    imageOver: "images/doc_grupo2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=GroupArtifact
                        {
                            modeler:modeler
                            title: ##"group"//ModelerUtils.getLocalizedString("group")
                            uri:"new:groupartifact:{counter++}"
                        }
                    }
                }
            ]
        }

        var dataObj = SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"dataObjs"//ModelerUtils.getLocalizedString("dataObjs")
            toolTipText:bind if (showHelp) ##"dataObjsDescription" else ""
            image:"images/doc_1.png"
            imageOver:"images/doc_2.png"
            imageClicked: "images/doc_3.png"
            visible: bind not hidden
            buttons: [
                ImgButton {
                    text: ##"dataObj"//ModelerUtils.getLocalizedString("dataObj")
                    toolTipText: bind if (showHelp) ##"dataObjDescription" else ""
                    image: "images/doc_normal1.png"
                    imageOver: "images/doc_normal2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=DataObject
                        {
                            modeler:modeler
                            title: ##"dataObj"//ModelerUtils.getLocalizedString("dataObj")
                            uri:"new:dataobject:{counter++}"
                        }
                    }
                },
//                ImgButton {
//                    text: ##"collection"//ModelerUtils.getLocalizedString("collection")
//                    toolTipText: bind if (showHelp) ##"collectionDescription" else ""
//                    image: "images/doc_objeto1.png"
//                    imageOver: "images/doc_objeto2.png"
//                    action: function():Void
//                    {
//                        modeler.disablePannable=true;
//                        modeler.tempNode=CollectionArtifact
//                        {
//                            modeler:modeler
//                            title: ##"collection"//ModelerUtils.getLocalizedString("collection")
//                            uri:"new:datacollection:{counter++}"
//                        }
//                    }
//                },
                ImgButton {
                    text: ##"dataInput"//ModelerUtils.getLocalizedString("dataInput")
                    toolTipText: bind if (showHelp) ##"dataInputDescription" else ""
                    image: "images/doc_entrada1.png"
                    imageOver: "images/doc_entrada2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=DataInput
                        {
                            modeler:modeler
                            title: ##"dataInput"//ModelerUtils.getLocalizedString("dataInput")
                            uri:"new:datainput:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"dataOutput"//ModelerUtils.getLocalizedString("dataOutput")
                    toolTipText: bind if (showHelp) ##"dataOutputDescription" else ""
                    image: "images/doc_salida1.png"
                    imageOver: "images/doc_salida2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=DataOutput
                        {
                            modeler:modeler
                            title: ##"dataOutput"//ModelerUtils.getLocalizedString("dataOutput")
                            uri:"new:dataoutput:{counter++}"
                        }
                    }
                },
                ImgButton {
                    text: ##"dataStore"//ModelerUtils.getLocalizedString("dataStore")
                    toolTipText: bind if (showHelp) ##"dataStoreDescription" else ""
                    image: "images/doc_base1.png"
                    imageOver: "images/doc_base2.png"
                    action: function():Void
                    {
                        modeler.disablePannable=true;
                        modeler.tempNode=DataStore
                        {
                            modeler:modeler
                            title: ##"dataStore"//ModelerUtils.getLocalizedString("dataStore")
                            uri:"new:datastore:{counter++}"
                        }
                    }
                }
            ]
        }

        var pool=SubMenu
        {
            modeler: modeler
            toolBar:this
            text: ##"swimLanes"//ModelerUtils.getLocalizedString("swimLanes")
            toolTipText: bind if (showHelp) ##"swimLanesDescription" else ""
            image: "images/pool_1.png"
            imageOver: "images/pool_2.png"
            imageClicked: "images/pool_3.png"
            visible: bind not hidden
            buttons: [
                ImgButton
                {
                    text: ##"pool"//ModelerUtils.getLocalizedString("pool")
                    toolTipText: bind if (showHelp) ##"poolDescription" else ""
                    image: "images/pool_pool1.png"
                    imageOver: "images/pool_pool2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=Pool
                        {
                            modeler:modeler
                            title: ##"pool"//ModelerUtils.getLocalizedString("pool")
                            uri:"new:pool:{counter++}"
                        }
                    }
                },
                ImgButton
                {
                    text: ##"lane"//ModelerUtils.getLocalizedString("lane")
                    toolTipText: bind if (showHelp) ##"laneDescription" else ""
                    image: "images/pool_lane1.png"
                    imageOver: "images/pool_lane2.png"
                    action: function():Void {
                        modeler.disablePannable=true;
                        modeler.tempNode=Lane
                        {
                            modeler:modeler
                            title: ##"lane"//ModelerUtils.getLocalizedString("lane")
                            uri:"new:Lane:{counter++}"
                        }
                    }
                }
            ]
        };
        var flow: Flow;
        var imt: ImageView = null;

        simpleBar = Group {
             layoutX:bind x
             layoutY:bind y
             content: [
                flow = Flow {
                    height: bind h
                    width: bind w
                    content: [
                        ImageView {
                            image: imgTitleBar
                            smooth: false
                            cursor:Cursor.MOVE
                            onMousePressed: function (e: MouseEvent): Void
                            {
                                ModelerUtils.clickedNode=this;
                                modeler.disablePannable=true;
                                dx=x-e.sceneX;
                                dy=y-e.sceneY;
                            }
                            onMouseDragged: function (e: MouseEvent): Void
                            {
                                var tx = dx + e.sceneX;
                                var ty = dy + e.sceneY;
                                if (tx >= 0 and e.sceneX <= stage.scene.width) {
                                    x=dx+e.sceneX;
                                }
                                if (ty >= 0 and e.sceneY <= stage.scene.height) {
                                    y=dy+e.sceneY;
                                }
                            }
                            onMouseReleased: function (e: MouseEvent): Void
                            {
                                ModelerUtils.clickedNode=null;
                                modeler.disablePannable=false;
                            }
                        },
                        ImgButton {
                            text: bind if (not hidden) ##"hideTooltip" else ##"showTooltip"
                            toolBar:this;
                            image: bind if (not hidden) "images/sube_1.png" else "images/baja_1.png"
                            imageOver: bind if (not hidden) "images/sube_2.png" else "images/baja_2.png"
                            action: function():Void
                            {
                                ModelerUtils.stopToolTip();
                                hidden = not hidden;
                                if (hidden) {
                                    imt = ImageView {
                                        image: imgBottomBar
                                        smooth: false
                                    };
                                    insert imt after flow.content[1];
                                } else {
                                    if (imt != null) {
                                        delete imt from flow.content;
                                        imt = null;
                                    }
                                }
                            }
                        },
                        ImgButton {
                            text: bind if(not stage.fullScreen) ##"maximizeTooltip" else ##"minimizeTooltip"
                            toolBar:this;
                            image: bind if(not stage.fullScreen) "images/maxim_1.png" else "images/minim_1.png"
                            imageOver: bind if(not stage.fullScreen) "images/maxim_2.png" else "images/minim_2.png"
                            action: function():Void
                            {
                                ModelerUtils.clickedNode=null;
                                modeler.disablePannable=false;
                                stage.fullScreen = not stage.fullScreen;
                            }
                            visible: bind not hidden
                        },
                        ImageView {
                            image: imgBottomBar
                            smooth: false
                            visible: bind not hidden
                        }
                    ]
                }
             ]
             cursor:Cursor.HAND;
             blocksMouse:true
             visible: bind modeler.isLocked()
            };

            fullBar=Group
            {
             layoutX:bind x
             layoutY:bind y
             content: [
                flow = Flow {
                    height: bind h
                    width: bind w
                    content: [
                        ImageView {
                            image: imgTitleBar
                            smooth: false
                            cursor:Cursor.MOVE
                            onMousePressed: function (e: MouseEvent): Void
                            {
                                ModelerUtils.clickedNode=this;
                                modeler.disablePannable=true;
                                dx=x-e.sceneX;
                                dy=y-e.sceneY;
                            }
                            onMouseDragged: function (e: MouseEvent): Void
                            {
                                var tx = dx + e.sceneX;
                                var ty = dy + e.sceneY;
                                if (tx >= 0 and e.sceneX <= stage.scene.width) {
                                    x=dx+e.sceneX;
                                }
                                if (ty >= 0 and e.sceneY <= stage.scene.height) {
                                    y=dy+e.sceneY;
                                }
                            }
                            onMouseReleased: function (e: MouseEvent): Void
                            {
                                ModelerUtils.clickedNode=null;
                                modeler.disablePannable=false;
                            }
                        },
                        ImgButton {
                            text: bind if (not hidden) ##"hideTooltip" else ##"showTooltip"
                            toolBar:this;
                            image: bind if (not hidden) "images/sube_1.png" else "images/baja_1.png"
                            imageOver: bind if (not hidden) "images/sube_2.png" else "images/baja_2.png"
                            action: function():Void
                            {
                                ModelerUtils.stopToolTip();
                                hidden = not hidden;
                                if (hidden) {
                                    imt = ImageView {
                                        image: imgBottomBar
                                        smooth: false
                                    };
                                    insert imt after flow.content[1];
                                } else {
                                    if (imt != null) {
                                        delete imt from flow.content;
                                        imt = null;
                                    }
                                }
                            }
                        },
                        ImgButton {
                            text: bind if(not stage.fullScreen) ##"maximizeTooltip" else ##"minimizeTooltip"
                            toolBar:this;
                            image: bind if(not stage.fullScreen) "images/maxim_1.png" else "images/minim_1.png"
                            imageOver: bind if(not stage.fullScreen) "images/maxim_2.png" else "images/minim_2.png"
                            action: function():Void
                            {
                                ModelerUtils.clickedNode=null;
                                modeler.disablePannable=false;
                                stage.fullScreen = not stage.fullScreen;
                            }
                            visible: bind not hidden
                        },
                        file,
                        ImageView {
                            image: imgDiv
                            smooth: false
                            visible: bind not hidden
                            //blocksMouse:true
                        },
                        task,
                        ImageView {
                            image: imgSpace
                            smooth: false
                            visible: bind not hidden
                        },
                        subtask,
                        ImageView {
                            image: imgSpace
                            smooth: false
                            visible: bind not hidden
                        },
                        callactivity,
                        ImageView {
                            image: imgSpace
                            smooth: false
                            visible: bind not hidden
                        },
                        startEvent,
                        ImageView {
                            image: imgSpace
                            smooth: false
                            visible: bind not hidden
                        },
                        interEvent,
                        ImageView {
                            image: imgSpace
                            smooth: false
                            visible: bind not hidden
                        },
                        endEvent,
                        ImageView {
                            image: imgSpace
                            smooth: false
                            visible: bind not hidden
                        },
                        gateWay,
                        ImageView {
                            image: imgDiv
                            smooth: false
                            visible: bind not hidden
                        },
                        sequence,
                        ImageView {
                            image: imgDiv
                            smooth: false
                            visible: bind not hidden
                        },
                        artifacts,
                        ImageView {
                            image: imgDiv
                            smooth: false
                            visible: bind not hidden
                        },
                        dataObj,
                        ImageView {
                            image: imgDiv
                            smooth: false
                            visible: bind not hidden
                        },
                        pool,
                        ImageView {
                            image: imgBottomBar
                            smooth: false
                            visible: bind not hidden
                        }
                    ]
                },
                file.subBar,
                task.subBar,
                subtask.subBar,
                callactivity.subBar,
                startEvent.subBar,
                interEvent.subBar,
                endEvent.subBar,
                gateWay.subBar,
                sequence.subBar,
                artifacts.subBar,
                dataObj.subBar,
                pool.subBar
             ]
             cursor:Cursor.HAND;
             blocksMouse:true
             visible: bind not (modeler.isLocked())
        };

        if (simpleMode) {
            ret = simpleBar;
        } else  {
            ret = fullBar;
        }

        return ret;
    }

    public function undo() : Void
    {
        var aux=undoMgr.undo();
        if(aux!=null)
        {
            delete modeler.contents;
            //modeler.containerElement=null;
            createProcess(aux);
        }
    }

    public function redo() : Void
    {
        var aux=undoMgr.redo();
        if(aux!=null)
        {
            delete modeler.contents;
            //modeler.containerElement=null;
            createProcess(aux);
        }
    }
}

class FileFilter extends javax.swing.filechooser.FileFilter {
    override public function getDescription() : String {
        return ##"swpFileFilter"//ModelerUtils.getLocalizedString("swpFileFilter");
    }

    override public function accept(f: java.io.File) : Boolean {
        return f.isDirectory() or f.getName().endsWith(".swp")
    }
}

class ImageFileFilter extends javax.swing.filechooser.FileFilter {
    override public function getDescription() : String {
        return ##"exportFileFilter"//ModelerUtils.getLocalizedString("exportFileFilter");
    }

    override public function accept(f: java.io.File) : Boolean {
        return f.isDirectory() or f.getName().endsWith(".png")
    }
}

class XPDLFileFilter extends javax.swing.filechooser.FileFilter {
    override public function getDescription() : String {
        return ##"xpdlFileFilter"//ModelerUtils.getLocalizedString("exportFileFilter");
    }

    override public function accept(f: java.io.File) : Boolean {
        return f.isDirectory() or f.getName().endsWith(".xpdl")
    }
}