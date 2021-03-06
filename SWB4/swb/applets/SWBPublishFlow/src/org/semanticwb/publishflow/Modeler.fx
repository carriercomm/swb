/*
 * Modeler.fx
 *
 * Created on 26/02/2010, 12:34:46 PM
 */

package org.semanticwb.publishflow;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.ClipView;
import javafx.scene.input.MouseEvent;
import org.semanticwb.publishflow.FlowObject;
import javafx.scene.Group;
import applets.commons.WBTreeNode;
import applets.commons.WBXMLParser;
import org.semanticwb.publishflow.StartEvent;
import javax.swing.JOptionPane;
import java.util.ResourceBundle;
import java.util.Locale;
import org.semanticwb.publishflow.ConnectionObject;
import java.lang.String;
import java.lang.System;
import java.util.Iterator;
import org.semanticwb.publishflow.EndEvent;
import java.lang.Exception;
import org.semanticwb.publishflow.NoAuthorizeLink;
import org.semanticwb.publishflow.AuthorizeLink;
import java.net.URL;
import javafx.scene.input.MouseButton;


/**
 * @author victor.lorenzana
 */

public class Modeler extends CustomNode
{
    public var locale:Locale;
    public var width:Number;
    public var height:Number;
    public var contents: Node[];
    public var pannable: Boolean;
    public var disablePannable: Boolean=false;
    public var tempNode: Node;                          //Nodo temporal por toolbar
    public var focusedNode: Node;                       //Nodo con el foco
    //public var clickedNode: Node;
    public var overNode: GraphElement;
    public var mousex:Number;
    public var mousey:Number;
    public var clipView:ClipView;
    public var tm:String;
    public var version:String="1.0";
    public var canEdit:Boolean;
    public var id_workflow:String;
    public var name:String="name workflow 1";
    public var description:String="workflow 1";
    public var resourceTypes:String[];
    public var roles:String[];
    public var users:String[];
    public override function create(): Node
    {
        if(FX.getArgument("tm")!=null)
        {
            tm=FX.getArgument("tm").toString();
        }
        locale=new Locale("es");
        if (FX.getArgument("locale")!=null)
        {
            try
            {

                locale = new Locale(FX.getArgument("locale").toString());
            }
            catch (e: Exception)
            {
                e.printStackTrace(System.out);
            }
        }
         clipView=ClipView
         //var ret=ScrollPane
         {
            node:Group             
             {
                 content: bind contents
             }
             width:bind width
             height:bind height
             pannable: bind pannable and not disablePannable
             //translateX:40;
            
             onMousePressed: function( e: MouseEvent ):Void
             {


                mousex=e.x+clipView.clipX;
                mousey=e.y+clipView.clipY;
                if(tempNode!=null)
                {
                    var close: Boolean=true;

                    if(tempNode instanceof GraphElement)
                    {
                        add(tempNode);
                        var a=tempNode as GraphElement;
                        a.x=e.x+clipView.clipX;
                        a.y=e.y+clipView.clipY;
                        a.snapToGrid();
                    }else if(tempNode instanceof ConnectionObject)
                    {
                        var a=tempNode as ConnectionObject;
                        if(ModelerUtils.clickedNode instanceof GraphElement)
                        {
                            var ge=ModelerUtils.clickedNode as GraphElement;
                            if(ge.canIniLink(a) and ge instanceof FlowObject)
                            {
                                a.ini=ge as FlowObject;
                                add(tempNode);
                            }
                            close=false;
                            ModelerUtils.clickedNode=null;
                        }
                    }

                    if(close)
                    {
                        tempNode=null;
                        disablePannable=false;                        
                    }
                }
               

             }
             onMouseDragged: function( e: MouseEvent ):Void
             {                
                if(tempNode!=null)
                {
                    mousex=e.x+clipView.clipX;
                    mousey=e.y+clipView.clipY;
                    if(tempNode instanceof ConnectionObject)
                    {
                        var co: ConnectionObject=tempNode as ConnectionObject;
                        if(overNode!=null and overNode instanceof FlowObject)
                        {
                            if(overNode.canEndLink(co))
                            {
                               if(co.ini!=null and co.ini!=overNode)
                               {
                                    co.end=overNode as FlowObject;
                               }
                            }
                        }else
                        {                            
                            co.end=null;
                        }
                    }
                }
                else if(ModelerUtils.clickedNode instanceof ConnectionObject)
                {
                    tempNode=ModelerUtils.clickedNode;
                    var co: ConnectionObject=tempNode as ConnectionObject;                                        
                    co.end=null;
                }
             }
             onMouseReleased: function( e: MouseEvent ):Void
             {                 
                 if(tempNode!=null)
                 {
                     if(tempNode instanceof ConnectionObject)
                     {
                         var con=tempNode as ConnectionObject;
                         if(con.end==null)
                         {                            
                            remove(tempNode);                            
                         }
                         tempNode=null;
                         disablePannable=false;
                     }
                 }
             }
             onMouseMoved:function( e: MouseEvent ):Void
             {
                 if(overNode!=null)
                 {
                     overNode.mouseMoved(e);
                 }

             }
             

         };

         return clipView;
    }
    public function existsRol(value:String) :Boolean
    {
        for(role in roles)
        {
            System.out.println("role cat: {role}");
            System.out.println("value: {value}");
            if(role.startsWith(value))
            {
                return true
            }

        }
        return false;

    }
    public function existsUser(value:String) :Boolean
    {
        for(user in users)
        {
            System.out.println("user cat: {user}");
            System.out.println("value: {value}");
            if(user.startsWith(value))
            {
                return true
            }

        }
        return false;

    }
    public function loadRoles() :Void
    {
        if(FX.getArgument("tm")!=null)
        {
            tm = FX.getArgument("tm").toString();
        }
        if (tm != null)
        {
            var  xml : String= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><req><cmd>getcatRoles</cmd><tm>{tm}</tm></req>";
            xml = ToolBar.conn.getData(xml);
            System.out.println("xmlrolescat: {xml}");
            var  parser : WBXMLParser = new WBXMLParser();
            var exml : WBTreeNode = parser.parse(xml);
            var eusers : Iterator = exml.getFirstNode().getNodesbyName("role");
            while (eusers.hasNext())
            {
                var wuser : WBTreeNode= eusers.next() as WBTreeNode;
                var idrole: String = wuser.getAttribute("id");
                var oname : String= wuser.getAttribute("name");
                var repository :String = wuser.getAttribute("repository");
                var role:String="{idrole}@{repository}";
                insert role into roles

            }
        }

    }
    public function loadUsers() :Void
    {
        if(FX.getArgument("tm")!=null)
        {
            tm = FX.getArgument("tm").toString();
        }
        if (tm != null)
        {
            var  xml : String= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><req><cmd>getcatUsers</cmd><tm>{tm}</tm></req>";
            xml = ToolBar.conn.getData(xml);
            System.out.println("xmluserscat: {xml}");
            var  parser : WBXMLParser = new WBXMLParser();
            var exml : WBTreeNode = parser.parse(xml);
            var eusers : Iterator = exml.getFirstNode().getNodesbyName("user");
            while (eusers.hasNext())
            {
                var wuser : WBTreeNode= eusers.next() as WBTreeNode;
                var iduser: String = wuser.getAttribute("id");
                var user:String="{iduser}";
                insert user into users

            }
        }

    }

    public function loadWorkflow() : Void
    {        

        if(ToolBar.conn==null or FX.getArgument("idworkflow")==null)
        {
            return
        }
        if(FX.getArgument("idworkflow")!=null)
        {
            id_workflow = FX.getArgument("idworkflow").toString();
        }
        if(FX.getArgument("tm")!=null)
        {
            tm = FX.getArgument("tm").toString();
        }
        users=null;
        roles=null;
        loadRoles();
        loadUsers();
        var xml:String = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><req><cmd>getWorkflow</cmd><id>{id_workflow}</id><tm>{tm}</tm></req>";
        var respxml : String= ToolBar.conn.getData(xml);
        //var respxml : String= "<res><workflow id=\"http://www.Capacitacion.swb#swb_PFlow:1_Capacitacion\"           name=\"Flujo del Curso\"             version=\"3.0\">      <description>_</description>      <activity days=\"0\" hours=\"3\" name=\"Revision 1\" type=\"Activity\">         <description>Primera Revision</description>         <role id=\"Instructor\" name=\"Instructor\" repository=\"uradm\"/>         <role id=\"Revisor1\" name=\"Revisor 1\" repository=\"uradm\"/>      </activity>      <activity days=\"0\" hours=\"3\" name=\"Revision 2\" type=\"Activity\">         <description>Segunda revision</description>         <role id=\"Instructor\" name=\"Instructor\" repository=\"uradm\"/>         <role id=\"Revisor_2\" name=\"Revisor 2\" repository=\"uradm\"/>      </activity>      <activity days=\"0\" hours=\"3\" name=\"Revision 3\" type=\"Activity\">        <description>Tercera Revision</description>         <role id=\"Instructor\" name=\"Instructor\" repository=\"uradm\"/>         <role id=\"Revisor_3\" name=\"Revisor 3\" repository=\"uradm\"/>      </activity>      <activity name=\"Generador de contenido\" type=\"AuthorActivity\"/>      <activity name=\"Terminar flujo\" type=\"EndActivity\"/>      <link authorized=\"false\" from=\"Revision 1\" publish=\"false\"            to=\"Generador de contenido\"            type=\"unauthorized\">         <service>mail</service>         <service>noauthorize</service>      </link>      <link authorized=\"false\" from=\"Revision 1\" publish=\"false\" to=\"Revision 2\"            type=\"authorized\">         <service>mail</service>      </link>      <link authorized=\"false\" from=\"Revision 2\" publish=\"false\"            to=\"Generador de contenido\"            type=\"unauthorized\">         <service>mail</service>         <service>noauthorize</service>      </link>      <link authorized=\"false\" from=\"Revision 2\" publish=\"false\" to=\"Revision 3\"            type=\"authorized\">         <service>mail</service>      </link>      <link authorized=\"true\" from=\"Revision 3\" publish=\"true\" to=\"Terminar flujo\"            type=\"authorized\">         <service>mail</service>         <service>publish</service>         <service>authorize</service>      </link>      <link authorized=\"false\" from=\"Revision 3\" publish=\"false\"            to=\"Generador de contenido\"            type=\"unauthorized\">         <service>mail</service>         <service>noauthorize</service>      </link>      <resourceType id=\"ExcelContent\" name=\"ExcelContent\" topicmap=\"Capacitacion\"/>      <resourceType id=\"PPTContent\" name=\"PPTContent\" topicmap=\"Capacitacion\"/>      <resourceType id=\"WordContent\" name=\"WordContent\" topicmap=\"Capacitacion\"/>   </workflow></res>";
        System.out.println("respxml: {respxml}");
        var parser : WBXMLParser= new WBXMLParser();
        var exml:WBTreeNode = parser.parse(respxml);
        if (exml.getFirstNode() != null and exml.getFirstNode().getFirstNode() != null)
        {
            var eworkflow:WBTreeNode = exml.getFirstNode().getFirstNode();
            if (eworkflow.getName().equals("workflow"))
            {
                name=eworkflow.getAttribute("name");
                version=eworkflow.getAttribute("version");
                if(eworkflow.getAttribute("canEdit")!=null and not eworkflow.getAttribute("canEdit").equals("") and eworkflow.getAttribute("canEdit").equals("true"))
                {
                    canEdit=true;
                }
                var edescription:WBTreeNode=eworkflow.getNodebyName("description");
                if(edescription!=null)
                {
                    description=edescription.getFirstNode().getText();
                }
                var startEvent:StartEvent;
                
                startEvent=StartEvent
                {
                    x:100
                    y:100
                    modeler:this
                    uri:"new:startevent:{ToolBar.counter++}"                                      
                }
                add(startEvent);
                
                


                var initActivity=true;
                var activities: Iterator=eworkflow.getNodesbyName("activity");
                while(activities.hasNext())
                {


                    var eactivity: WBTreeNode=activities.next() as WBTreeNode;
                    var type:String=eactivity.getAttribute("type");
                    if(type.equals("EndActivity"))
                    {
                        if(not hasAnElementWith("new:endevent:"))
                        {
                            var end:EndEvent=EndEvent
                            {
                                x:100
                                y:100
                                type:{type};
                                modeler:this
                                uri:"new:endevent:{ToolBar.counter++}"
                            }
                            add(end);
                        }

                    }
                    else if(type.equals("AuthorActivity"))
                    {
                        if(not hasAnElementWith("new:authoractivity:"))
                        {
                            var act:AuthorActivity=AuthorActivity
                            {
                                x:100
                                y:100
                                type:{type};
                                modeler:this
                                uri:"new:authoractivity:{ToolBar.counter++}:"
                            }
                            add(act);
                        }
                    }
                    else
                    {

                        var desc: String="";

                        var edesc : WBTreeNode=eactivity.getNodebyName("description");

                        if(edesc!=null)
                        {
                            desc=edesc.getFirstNode().getText();
                        }
                        var name:String=eactivity.getAttribute("name");
                        var title:String=name;
                        if(eactivity.getAttribute("title")!=null and not eactivity.getAttribute("title").equals(""))
                        {
                            title=eactivity.getAttribute("title");
                        }

                        var activity: Task =Task
                        {
                            x:100
                            y:100
                            description:{desc}
                            title:title
                            modeler:this
                            uri:"new:task:{ToolBar.counter++}"

                        }
                        add(activity);
                        if(initActivity)
                        {
                            var co:ConnectionObject=ConnectionObject
                            {
                                ini:startEvent;
                                end:activity;                                
                                modeler:{this};
                            }
                            initActivity=not initActivity;
                            add(co);
                        }
                        
                        var days:Integer=0;
                        var minutes:Integer=0;
                        var seconds:Integer =0;
                        var hours:Integer=0;
                        try
                        {
                            //days=Integer.parseInt(eactivity.getAttribute("days"));
                            if(eactivity.getAttribute("hours")!=null and not eactivity.getAttribute("hours").equals(""))
                            {
                                hours=Integer.parseInt(eactivity.getAttribute("hours"));
                            }
                            if(eactivity.getAttribute("days")!=null and not eactivity.getAttribute("days").equals(""))
                            {
                                days=Integer.parseInt(eactivity.getAttribute("days"));
                            }
                        }
                        catch( e: Exception)
                        {
                            e.printStackTrace(System.out);
                        }
                        //activity.setDuraction(days,hours);
                        activity.days=days;
                        activity.hours=hours;
                        var roles: Iterator=eactivity.getNodesbyName("role");
                        while(roles.hasNext())
                        {

                            var erole: WBTreeNode=roles.next() as WBTreeNode;
                            //var role: Role=new Role(erole.getAttribute("id"),erole.getAttribute("name"),erole.getAttribute("repository"));
                            var rolid:String =erole.getAttribute("id");
                            //var rolname:String=erole.getAttribute("name");
                            var rep:String=erole.getAttribute("repository");
                            var srole:String="{rolid}@{rep}";                
                            System.out.println("srole: {srole}");
                            System.out.println("existsRol(srole): {existsRol(srole)}");
                            if(existsRol(srole))
                            {
                                insert srole into activity.roles;
                            }
                        }
                        var users: Iterator=eactivity.getNodesbyName("user");
                        while(users.hasNext())
                        {
                            var euser: WBTreeNode=users.next() as WBTreeNode;
                            //User user=new User(,);
                            var userid:String=euser.getAttribute("id");
                            //var username:String=euser.getAttribute("name");
                            var suser:String="{userid}";
                            System.out.println("suser: {suser}");
                            System.out.println("existsUser(suser): {existsUser(suser)}");
                            if(existsUser(suser))
                            {
                                insert suser into activity.users;
                            }
                        }
                    }
                }

                var links: Iterator=eworkflow.getNodesbyName("link");
                while(links.hasNext())
                {
                    var elink : WBTreeNode=links.next() as WBTreeNode;
                    var type:String=elink.getAttribute("type");
                    var nameto:String=elink.getAttribute("to");
                    var namefrom:String=elink.getAttribute("from");
                    var activityto:FlowObject;
                    var activityfrom:FlowObject;
                    for(node in this.contents)
                    {
                        if(node instanceof FlowObject)
                        {
                            var activity:FlowObject=node as FlowObject;                            
                            if(activity.title.equals(nameto))
                            {
                                activityto=activity;
                            }
                            if(activity.title.equals(namefrom))
                            {
                                activityfrom=activity;
                            }
                        }

                        if(activityto!=null and activityfrom!=null)
                        {                            
                            var link:LinkConnection;                            
                            if(type.equals("authorized"))
                            {
                                link=AuthorizeLink
                                {
                                    ini: activityfrom
                                    end: activityto                                    
                                    modeler:this;
                                    uri:"new:sequenceflow:{ToolBar.counter++}"
                                }
                            }
                            else
                            {
                                link=NoAuthorizeLink
                                {
                                    ini: activityfrom
                                    end: activityto                                    
                                    uri:"new:sequenceflow:{ToolBar.counter++}"
                                    modeler:this;
                                }
                            }
                            add(link);
                            var itNotifications : Iterator=elink.getNodesbyName("notification");
                            while(itNotifications.hasNext())
                            {
                                var eNotification: WBTreeNode=itNotifications.next() as WBTreeNode;
                                if(eNotification.getAttribute("type").equals("user"))
                                {
                                    var userid:String=eNotification.getAttribute("to");
                                    var user:String="{userid}";
                                    if(existsUser(user))
                                    {
                                        insert user into link.users;
                                    }
                                }
                                else
                                {
                                    var roleID:String=eNotification.getAttribute("to");
                                    var repository:String=eNotification.getAttribute("repository");
                                    var role:String="{roleID}@{repository}";
                                    if(existsRol(role))
                                    {
                                        insert role into link.roles;
                                    }
                                }
                            }
                            link.authorized=Boolean.valueOf(elink.getAttribute("authorized"));
                            link.published=(Boolean.valueOf(elink.getAttribute("publish")).booleanValue());
                            break;
                        }
                    }
                }
                var resourceTypes:Iterator=eworkflow.getNodesbyName("resourceType");
                while(resourceTypes.hasNext())
                {
                    var resourceType: WBTreeNode=resourceTypes.next() as WBTreeNode;
                    var idres:String=resourceType.getAttribute("id");
                    var tmr:String=resourceType.getAttribute("topicmap");
                    var res:String="{idres}@{tmr}";
                    insert res into this.resourceTypes;
                }

            }
        }
        for(node in contents)
        {
            if(node instanceof Task)
            {
                var task:Task=node as Task;
                task.onConected();
            }
        }

        organizeMap();
    }
    
    public function editProps() : Void
    {
        var dialog:EditWorkflow;
        dialog=new EditWorkflow();
        dialog.resourceTypes=resourceTypes;
        dialog.name=name;
        dialog.locale=locale;
        dialog.version=version;
        dialog.description=description;
        dialog.tm=tm;
        dialog.con=ToolBar.conn;
        dialog.init();
        dialog.setVisible(true);
        if(not dialog.cancel)
        {
            resourceTypes=dialog.resourceTypes;
        }
    }

    public function save() : Void
    {
        if(name.trim().equals(""))
        {
            JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("indicar_titutlo"), java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"), JOptionPane.ERROR_MESSAGE);
            editProps();
            return;
        }
        if(description.trim().equals(""))
        {
            JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("Favor_de_indicar_la_descripcion_del_flujo"), java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"), JOptionPane.ERROR_MESSAGE);
            editProps();
            return;
        }
        var count:Integer=sizeof resourceTypes;        
        if(count==0)
        {
            JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("Debe_indicar_a_que_tipos_de_recursos_aplica_el_flujo_de_publicacion"), java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"), JOptionPane.ERROR_MESSAGE);
            editProps();
            return;
        }

        for(content in contents)
        {
            if(content instanceof Task)
            {
                var task: Task=content as Task;
                if(sizeof task.users==0 and sizeof task.roles==0)
                {
                    JOptionPane.showMessageDialog(null, "La tarea: {task.title}, no tiene indicado que usuarios o que roles tienen permisos de autorizacion", java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"), JOptionPane.ERROR_MESSAGE);
                    task.editTask();
                    return;
                }

            }
        }

        var endEvent:EndEvent;
        var startEvent:StartEvent;
        for(content in contents)
        {
            if(content instanceof StartEvent)
            {
                startEvent=content as StartEvent;
                break;
            }
        }
        var countTask:Integer=0;
        for(content in contents)
        {
            if(content instanceof Task)
            {
                countTask++;
                break;
            }
        }
        if(startEvent==null)
        {
            JOptionPane.showMessageDialog(null, "Debe indicar un nodo de inicio", "Guardar Flujo", JOptionPane.OK_OPTION + JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(startEvent.getBusyPoints()==0)
        {
            JOptionPane.showMessageDialog(null, "Debe inidicar cual es la actividad inicial", "Guardar Flujo", JOptionPane.OK_OPTION + JOptionPane.ERROR_MESSAGE);
            startEvent.requestFocus();
            return;
        }
        if(countTask==0)
        {
            JOptionPane.showMessageDialog(null, "Debe indicar por lo menos una tarea", "Guardar Flujo", JOptionPane.OK_OPTION + JOptionPane.ERROR_MESSAGE);
            return;
        }

        for(content in contents)
        {
            if(content instanceof EndEvent)
            {
                endEvent=content as EndEvent;
                break;
            }
        }
        if(endEvent==null)
        {
            JOptionPane.showMessageDialog(null, "Debe indicar un nodo final", "Guardar Flujo", JOptionPane.OK_OPTION + JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(endEvent.getBusyPoints()==0)
        {
            JOptionPane.showMessageDialog(null, "El nodo final no tiene una conexión", "Guardar Flujo", JOptionPane.OK_OPTION + JOptionPane.ERROR_MESSAGE);
            return;
        }

        for(content in contents)
        {
            if(content instanceof Task)
            {
                var task=content as Task;
                if(not task.hasIniAuthorizeLink())
                {
                    JOptionPane.showMessageDialog(null, "La tarea {task.title} no tiene conexión en caso de autorización", "Guardar Flujo", JOptionPane.OK_OPTION + JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(not task.hasIniNoAuthorizeLink())
                {
                    JOptionPane.showMessageDialog(null, "La tarea {task.title} no tiene conexión en caso de no autorización", "Guardar Flujo", JOptionPane.OK_OPTION + JOptionPane.ERROR_MESSAGE);
                    return;
                }

                break;
            }
        }

        




        if (id_workflow != null)
        {
            var ask:String=ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("ask1");
            var msg:String=ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("msg2");
            var status:Integer = JOptionPane.showConfirmDialog(
                    null,"{ask}\r\n{msg}",
                    ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"),
                    JOptionPane.YES_NO_OPTION);
            if (status == JOptionPane.NO_OPTION)
            {
                return;
            }

        }

        var iniActivity:Task=startEvent.getConnectionObject(0).end as Task;





        var  parse : WBXMLParser = new WBXMLParser();
        var  node: WBTreeNode = parse.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        var wf:WBTreeNode = node.addNode();
        wf.setName("workflow");
        wf.addAttribute("version", version);
        wf.addAttribute("canEdit", canEdit.toString());
        if (id_workflow != null)
        {
            wf.addAttribute("id", id_workflow);
        }
        wf.addAttribute("name", name);
        var desc: WBTreeNode = wf.addNode();
        desc.setName("description");
        desc.setText(description.trim());
        var eactivity : WBTreeNode = wf.addNode();
        eactivity.setName("activity");
        eactivity.addAttribute("name", iniActivity.title);
        eactivity.addAttribute("type", "Activity");
        eactivity.addAttribute("days", String.valueOf(iniActivity.days));
        eactivity.addAttribute("hours", String.valueOf(iniActivity.hours));
        var edesc : WBTreeNode = eactivity.addNode();
        edesc.setName("description");
        edesc.setText(iniActivity.description);
        for(svalue in iniActivity.roles)
        {
            var values:String[]=svalue.split("@");
            var erole : WBTreeNode = eactivity.addNode();
            erole.setName("role");
            erole.addAttribute("id", values[0]);            
            erole.addAttribute("repository",values[1]);
        }
        for(svalue in iniActivity.users)
        {
            var values:String[]=svalue.split("@");
            var euser: WBTreeNode = eactivity.addNode();
            euser.setName("user");
            euser.addAttribute("id", values[0]);
            //euser.addAttribute("name", values[1]);
        }
        for(content in contents)
        {
            if(content instanceof FlowObject)
            {
                if(iniActivity!=content)
                {
                var activity:FlowObject=content as FlowObject;
                eactivity = wf.addNode();
                eactivity.setName("activity");
                eactivity.addAttribute("name", activity.uri);
                if (activity instanceof EndEvent)
                {
                    eactivity.addAttribute("name", "EndActivity");
                    eactivity.addAttribute("type", "EndActivity");
                }
                else if (activity instanceof AuthorActivity)
                {
                    eactivity.addAttribute("name", "AuthorActivity");
                    eactivity.addAttribute("type", "AuthorActivity");
                }
                else if (activity instanceof Task)
                {
                    var task:Task=activity as Task;
                    eactivity.addAttribute("title", task.title);
                    eactivity.addAttribute("type", "Activity");
                    eactivity.addAttribute("days", String.valueOf(task.days));
                    eactivity.addAttribute("hours", String.valueOf(task.hours));
                    edesc = eactivity.addNode();
                    edesc.setName("description");
                    edesc.setText(task.description);
                    for(svalue in task.roles)
                    {
                        var values:String[]=svalue.split("@");
                        var erole : WBTreeNode = eactivity.addNode();
                        erole.setName("role");
                        erole.addAttribute("id", values[0]);
                        //erole.addAttribute("name", values[2]);
                        erole.addAttribute("repository",values[1]);
                    }
                    for(svalue in task.users)
                    {
                        var values:String[]=svalue.split("@");
                        var euser: WBTreeNode = eactivity.addNode();
                        euser.setName("user");
                        euser.addAttribute("id", values[0]);
                        //euser.addAttribute("name", values[1]);
                    }
                }
                }
            }
        }
        for(content in contents)
        {
            if(content instanceof LinkConnection)
            {
                var link:LinkConnection=content as LinkConnection;
                var elink :WBTreeNode = wf.addNode();
                elink.setName("link");


                for(svalue in link.users)
                {
                    var values:String[]=svalue.split("@");
                    var notifica : WBTreeNode = elink.addNode();
                    notifica.setName("notification");
                    notifica.addAttribute("to", values[0]);
                    notifica.addAttribute("type", "user");
                }

                for(svalue in link.roles)
                {
                    var values:String[]=svalue.split("@");
                    var notifica : WBTreeNode = elink.addNode();
                    notifica.setName("notification");
                    notifica.addAttribute("to", values[0]);
                    notifica.addAttribute("repository", values[1]);
                    notifica.addAttribute("type", "role");
                }

                elink.addAttribute("from", link.ini.title);
                elink.addAttribute("to", link.end.title);
                elink.addAttribute("type", link.type);
                elink.addAttribute("publish", String.valueOf(link.published));
                elink.addAttribute("authorized", String.valueOf(link.authorized));
                var eservicemail : WBTreeNode = elink.addNode();
                eservicemail.setName("service");
                eservicemail.setText("mail");
                var activityTo:FlowObject=link.end;
                if (link.end instanceof AuthorActivity)
                {
                    if (link.authorized)
                    {
                        var eservice : WBTreeNode= elink.addNode();
                        eservice.setName("service");
                        eservice.setText("authorize");
                    }
                    else
                    {
                        var eservice : WBTreeNode = elink.addNode();
                        eservice.setName("service");
                        eservice.setText("noauthorize");
                    }
                }                
                else if (activityTo instanceof EndEvent)
                {
                    if (link.published)
                    {
                        var eservice : WBTreeNode= elink.addNode();
                        eservice.setName("service");
                        eservice.setText("publish");
                    }
                    if (link.authorized)
                    {
                        var eservice : WBTreeNode = elink.addNode();
                        eservice.setName("service");
                        eservice.setText("authorize");
                    }
                    else
                    {
                        var eservice : WBTreeNode = elink.addNode();
                        eservice.setName("service");
                        eservice.setText("noauthorize");
                    }
                }
                else
                {
                    if (not link.type.equalsIgnoreCase("authorized"))
                    {
                        var eservice : WBTreeNode= elink.addNode();
                        eservice.setName("service");
                        eservice.setText("noauthorize");
                    }
                }

            }

        }

        for(svalue in resourceTypes)
        {
                var values:String[]=svalue.split("@");
                var etype : WBTreeNode = wf.addNode();
                etype.setName("resourceType");
                etype.addAttribute("name", values[0]);
                etype.addAttribute("id", values[0]);
                etype.addAttribute("topicmap", values[1]);

        }
        var xml : String = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><req><cmd>update</cmd><tm>{tm}</tm>{node.getFirstNode().getXML()}</req>";
        
        if(ToolBar.conn==null)
        {
            var msg:String=ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("err1");
            JOptionPane.showMessageDialog(null,   msg, java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        var resxml: String = ToolBar.conn.getData(xml);
        
        try
        {
            var parser : WBXMLParser = new WBXMLParser();
            var respnode : WBTreeNode= parser.parse(resxml);
            if (respnode.getFirstNode() != null and respnode.getFirstNode().getFirstNode() != null)
            {
                var eid : WBTreeNode = respnode.getFirstNode().getFirstNode();
                if (eid.getName().equals("workflowid"))
                {
                    if (id_workflow != null)
                    {
                        var iversion : Integer = Double.parseDouble(this.version) as Integer;
                        iversion++;
                        this.version="{iversion}.0";
                        
                    }
                    var version : WBTreeNode = respnode.getFirstNode().getNodebyName("version");                    
                    id_workflow=eid.getFirstNode().getText();

                    try
                    {
                        var url_script:String=FX.getArgument("script").toString();
                        var newurl : String= "{url_script}tm={tm}&id={eid.getFirstNode().getText()}";
                        var _url : URL= new URL(ToolBar.conn.getApplet().getCodeBase().getProtocol(), ToolBar.conn.getApplet().getCodeBase().getHost(), ToolBar.conn.getApplet().getCodeBase().getPort(), newurl);
                        ToolBar.conn.getApplet().getAppletContext().showDocument(_url, "status");
                    }
                    catch (e: Exception)
                    {
                        System.out.println(e.getMessage());
                        e.printStackTrace(System.out);
                    }
                    JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("save"), java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"), JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                else
                {
                    var err : WBTreeNode = respnode.getFirstNode().getFirstNode();
                    if (err != null)
                    {
                        JOptionPane.showMessageDialog(null, err.getFirstNode().getText(), java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    else
                    {

                        JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("err1"), java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

            }
            else
            {
                JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("err1"), java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        catch ( e: Exception)
        {
            var msg:String=ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("err1");
            JOptionPane.showMessageDialog(null,   "{msg} : {e.getMessage()}\r\n{resxml}", java.util.ResourceBundle.getBundle("org/semanticwb/publishflow/EditWorkflow", locale).getString("title"), JOptionPane.ERROR_MESSAGE);            
            e.printStackTrace(System.out);
            return;
        }
        

        
    }


    public function organizeMap()
    {            
            var newy:Number=80;
            var newx:Number=80;
            def spacingx:Number=30;            
            for(node in this.contents)
            {
                if(node instanceof FlowObject)
                {                    
                    var fo:FlowObject=node as FlowObject;
                    var testx:Number=newx+fo.w/2;
                    if(testx+fo.w/2>=width)
                    {
                        testx=80;
                        newy+=80;
                        newx=80;
                    }
                    else
                    {
                        newx=testx;
                    }

                    fo.x=testx;
                    fo.y=newy;
                    newx+=fo.w+spacingx;
                }

            }

    }



    public function add(obj:Node)
    {
        insert obj into contents;
    }

    public function addFirst(obj:Node)
    {
        insert obj before contents[0];
    }

    public function remove(obj:Node)
    {
        delete obj from contents;
        if(obj instanceof ConnectionObject)
        {
            var con=obj as ConnectionObject;
            con.deleted=true;
            if(con.ini!=null)
            {
                for(cp in con.ini.connectionPoints)
                {
                    if(cp.connectionObject!=null and cp.connectionObject.uri.equals(con.uri))
                    {
                        cp.connectionObject=null;
                    }
                }
            }
            if(con.end!=null)
            {
                for(cp in con.end.connectionPoints)
                {
                    if(cp.connectionObject!=null and cp.connectionObject.uri.equals(con.uri))
                    {
                        cp.connectionObject=null;
                    }
                }
            }
             
        }

    }

    public function getGraphElementByURI(uri:String):GraphElement
    {
        for(node in contents)
        {
            if(node instanceof GraphElement)
            {
                var ge=node as GraphElement;
                if(ge.uri.equals(uri))return ge;
            }
        }
        return null;
    }

    public function hasAnElementWith(uri:String):Boolean
    {
        for(node in contents)
        {
            if(node instanceof GraphElement)
            {
                var ge=node as GraphElement;
                if(ge.uri.startsWith(uri))return true;
            }
        }
        return false;
    }

}
