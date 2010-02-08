/*
 * Main.fx
 *
 * Created on 15/11/2009, 02:46:27 AM
 */

package org.semanticwb.process.modeler;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.scene.text.TextOrigin;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import javafx.scene.effect.Lighting;
import javafx.scene.effect.light.DistantLight;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.lang.Math;
import javafx.scene.layout.ClipView;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.Flow;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.control.TextBox;
import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeLineCap;

import applets.commons.*;

/**
 * @author javier.solis
 */

var stylesheets = "{__DIR__}style.css";
var color="#6060FF";
var color_over="#FF6060";
var color_focused="#FF8080";
var color_fill="#f5f5ff";
var color_fill_pool="#d5d5ff";

var style_task="fill: {color_fill}; stroke: {color}; strokeWidth: 2; arcWidth: 15; arcHeight: 15;";
var style_task_text="font-size: 14px; font-family: \"'Verdana'\"; fill: #000000; font-weight: bold;";
//var style_task_textbox="font-size: 14px; font-family: \"Helvetica, Arial\"; font-weight: bold; border-fill:transparent; background-fill:transparent; focus-fill:transparent; shadow-fill:transparent";
var style_task_textbox="font-size: 14px; font-family: \"Helvetica, Arial\"; font-weight: bold; focus-fill:transparent; shadow-fill:transparent";
var style_gateway="fill: {color_fill}; stroke: {color}; strokeWidth: 2;";
var style_message="fill: {color_fill}; stroke: {color}; strokeWidth: 2;";
var style_simbol="fill: {color_fill}; stroke: {color}; strokeWidth: 4;";
var style_event="fill: {color_fill}; stroke: {color}; strokeWidth: 2;";
var style_connection="stroke: {color}; strokeWidth: 2;";
var style_connection_arrow="stroke: {color}; strokeWidth: 2;";
var style_toolbar="fill: #f0f0f0; stroke: #909090; strokeWidth: 2;";
var style_pool="fill: {color_fill_pool}; stroke: {color}; strokeWidth: 1;";


var maxx : Number = bind scene.width on replace{ modeler.organizeMap();};
var maxy : Number = bind scene.height on replace{ modeler.organizeMap();};

var conn:WBConnection = new WBConnection(FX.getArgument(WBConnection.PRM_JSESS).toString(),FX.getArgument(WBConnection.PRM_CGIPATH).toString(),FX.getProperty("javafx.application.codebase"));

var dropShadow = DropShadow {
    offsetX: 3
    offsetY: 3
    radius: 10.0
    color: Color.web("#707070")
}

var lighting = Lighting{
    surfaceScale: 5
    light: DistantLight{
        azimuth: 10
        elevation: 40
    }
}

var modeler:Modeler = Modeler
{
    layoutX:0
    layoutY:23
    width:bind maxx
    height:bind maxy-23
    pannable:true
    cursor:Cursor.CROSSHAIR
}

var counter: Integer;

modeler.load("home");
modeler.organizeMap();

var scene : Scene = Scene {
    content: [
            modeler,
            ToolBar{},
    ]
    width: 600
    height: 300
    stylesheets: bind stylesheets
    //fill: Color.TRANSPARENT
}

var stage : Stage = Stage {
    title: "BPMN Modeler"
    resizable: true
    scene: scene
    //style: StageStyle.TRANSPARENT //StageStyle.UNDECORATED
}
/******************************************************************************/
class ToolBar extends CustomNode
{
    public override function create(): Node
    {
         var ret=Group
         {
             content: [
                Rectangle{
                    width:bind maxx
                    height:23
                    style:style_toolbar
                },
                Flow {
                    height: 23
                    width:bind maxx
                    content: [
                        Button{
                            text:"Save"
                            action: function():Void
                            {
                                var obj:JSONObject =new JSONObject();
                                obj.put("uri","test");
                                var nodes:JSONArray =new JSONArray();
                                obj.putOpt("nodes",nodes);
                                for(node in modeler.contents)
                                {
                                    var ele:JSONObject=new JSONObject();
                                    nodes.put(ele);
                                    if(node instanceof GraphElement)
                                    {
                                       var ge=node as GraphElement;
                                       ele.put("class",ge.getClass().getName());
                                       ele.put("title",ge.title);
                                       ele.put("uri",ge.uri);
                                       ele.put("x",ge.x);
                                       ele.put("y",ge.y);
                                    }
                                    if(node instanceof FlowObject)
                                    {
                                       var ge=node as FlowObject;
                                       ele.put("lane",ge.pool.uri);
                                    }
                                    if(node instanceof ConnectionObject)
                                    {
                                       var ge=node as ConnectionObject;
                                       ele.put("class",ge.getClass().getName());
                                       ele.put("uri",ge.uri);
                                       ele.put("title",ge.title);
                                       ele.put("start",ge.ini.uri);
                                       ele.put("end",ge.end.uri);
                                    }

                                }
                                println(obj.toString());
                            }
                        },
                        Button{
                            text:"Task"
                            action: function():Void {
                                modeler.disablePannable=true;
                                modeler.tempNode=Task
                                {
                                    title:"Task"
                                    uri:"new:task:{counter++}"
                                }
                            }
                        },
                        Button{
                            text:"SubProcess"
                            action: function():Void {
                                modeler.disablePannable=true;
                                modeler.tempNode=SubProcess
                                {
                                    title:"SubProcess"
                                    uri:"new:subprocess:{counter++}"
                                }
                            }
                        },
                        Button{
                            text:"Start Event"
                            action: function():Void {
                                modeler.disablePannable=true;
                                modeler.tempNode=StartEvent
                                {
                                    title:"Start Event"
                                    uri:"new:startevent:{counter++}"
                                }
                            }
                        },
                        Button{
                            text:"End Event"
                            action: function():Void {
                                modeler.disablePannable=true;
                                modeler.tempNode=EndEvent
                                {
                                    title:"End Event"
                                    uri:"new:endevent:{counter++}"
                                }
                            }
                        },
                        Button{
                            text:"Inter Event"
                            action: function():Void {
                                modeler.disablePannable=true;
                                modeler.tempNode=InterEvent
                                {
                                    title:"Inter Event"
                                    uri:"new:interevent:{counter++}"
                                }
                            }
                        },
                        Button{
                            text:"Gateway"
                            action: function():Void {
                                modeler.disablePannable=true;
                                modeler.tempNode=GateWay
                                {
                                    title:"Gateway"
                                    uri:"new:gateway:{counter++}"
                                }
                            }
                        },
                        Button{
                            text:"OR Gateway"
                            action: function():Void {
                                modeler.disablePannable=true;
                                modeler.tempNode=ORGateWay
                                {
                                    title:"OR Gateway"
                                    uri:"new:orgateway:{counter++}"
                                }
                            }
                        },
                        Button{
                            text:"AND Gateway"
                            action: function():Void {
                                modeler.disablePannable=true;
                                modeler.tempNode=ANDGateWay
                                {
                                    title:"AND Gateway"
                                    uri:"new:andgateway:{counter++}"
                                }
                            }
                        },
                        Button{
                            text:"Flow Link"
                            action: function():Void {
                                modeler.disablePannable=true;
                                modeler.tempNode=FlowLink
                                {
                                    uri:"new:flowlink:{counter++}"
                                }
                            }
                        },
                        Button{
                            text:"Pool"
                            action: function():Void {
                                modeler.disablePannable=true;
                                modeler.tempNode=Pool
                                {
                                    title:"Pool"
                                    uri:"new:pool:{counter++}"
                                }
                            }
                        }
                    ]
                }
             ]
         };
         return ret;
    }
}


/******************************************************************************/
class Modeler extends CustomNode
{
    public var width:Number;
    public var height:Number;
    public var contents: Node[];
    public var pannable: Boolean;
    public var disablePannable: Boolean=false;
    public var tempNode: Node;                          //Nodo temporal por toolbar
    public var focusedNode: Node;
    public var clickedNode: Node;
    public var overNode: Node;
    public var mousex:Number;
    public var mousey:Number;
    public var clipView:ClipView;

    public override function create(): Node
    {
         clipView=ClipView
         //var ret=ScrollPane
         {
             node:Group
             //content:Group
             {
                 content: bind contents
             }
             width:bind width
             height:bind height
             pannable: bind pannable and not disablePannable
             //translateX:40;
             onMousePressed: function( e: MouseEvent ):Void
             {
                println("onMousePressed modeler:{e}");
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
                    }else if(tempNode instanceof ConnectionObject)
                    {
                        var a=tempNode as ConnectionObject;
                        if(modeler.clickedNode!=null and modeler.clickedNode instanceof FlowObject)
                        {
                            a.ini=clickedNode as FlowObject;
                            add(tempNode);
                            close=false;
                            clickedNode=null;
                        }
                    }

                    if(close)
                    {
                        tempNode=null;
                        modeler.disablePannable=false;
                        println(e);
                        println(tempNode);
                    }
                }
             }
             onMouseDragged: function( e: MouseEvent ):Void
             {
                //println("onMouseDragged modeler:{e}");
                if(tempNode!=null)
                {
                    mousex=e.x+clipView.clipX;
                    mousey=e.y+clipView.clipY;
                    if(tempNode instanceof ConnectionObject)
                    {
                        var a=tempNode as ConnectionObject;
                        if(overNode!=null)
                        {
                            a.end=overNode as FlowObject;
                        }else
                        {
                            a.end=null;
                        }
                    }
                }
                else if(clickedNode instanceof ConnectionObject)
                {
                    tempNode=clickedNode;
                    var a=tempNode as ConnectionObject;
                    a.end=null;
                }
             }
             onMouseReleased: function( e: MouseEvent ):Void
             {
                 println("onMouseReleased modeler:{e}");
                 if(tempNode!=null)
                 {
                     if(tempNode instanceof ConnectionObject)
                     {
                         if(overNode==null)
                         {
                             remove(tempNode);
                         }
                         tempNode=null;
                         modeler.disablePannable=false;
                     }
                 }
             }
//             onKeyTyped: function( e: KeyEvent ):Void
//             {
//                 println(e);
//             }
         };
         return clipView;
    }

    public function load(home: String)
    {
        /*
        var t1= Task {
            x : 50, y : 100
            title : "Tarea 1"
            uri : "task1"
            //cursor:Cursor.CROSSHAIR;
        };

        var t2= SubProcess {
            x : 100, y : 100
            title : "Javier Solis Gonzalez"
            uri : "task2"
        };

        var se= StartEvent {
            x : 200, y : 100
            title : "Inicio"
            uri : "ini1"
        };

        var ee= EndEvent {
            x : 250, y : 100
            title : "Inicio"
            uri : "end1"
        };

        var p1= Pool {
            x : 400, y : 300
            title : "Pool"
            uri : "pool"
        };
        add(p1);

        add(ANDGateWay {
            x : 300, y : 100
            uri : "gateway1"
        });

        t1.pool=p1;


        add(t1);
        add(t2);
        add(se);
        add(ee);
        */
        //addRelation("home","padre1","Hijo","Padre");
    }

    public function organizeMap()
    {
    }

    public function addRelation(tpuri1:String, tpuri2:String, tpr1:String, tpr2:String)
    {
    }

    public function add(obj:Node)
    {
        insert obj into contents;
    }

    public function remove(obj:Node)
    {
        delete obj from contents;
    }
}

/******************************************************************************/
class EndEvent extends Event
{
    public override function create(): Node
    {
         var ret=super.create();
         shape.strokeWidth=4;
         stkw=4;
         stkwo=5;
         return ret;
    }
}

/******************************************************************************/
class InterEvent extends Event
{
    public override function create(): Node
    {
        cursor=Cursor.HAND;
        w=30;
        h=30;
        stkw=2;
        stkwo=2;

        shape= Circle
        {
            centerX: bind x
            centerY: bind y
            radius: bind w/2
            //styleClass: "event"
            style: style_event
            smooth:true;
        };

        return Group
        {
            content: [
                shape,text,
                Circle
                {
                    centerX: bind x
                    centerY: bind y
                    radius: bind w/2-3
                    stroke: bind shape.stroke
                    //styleClass: "event"
                    style: style_event
                    smooth:true;
                }
            ]
            scaleX: bind s;
            scaleY: bind s;
            opacity: bind o;
            effect: dropShadow
        };
    }
}

/******************************************************************************/
class StartEvent extends Event
{

}

/******************************************************************************/
class Event extends FlowObject
{
    public override function create(): Node
    {
        cursor=Cursor.HAND;
        w=30;
        h=30;

        shape= Circle
        {
            centerX: bind x
            centerY: bind y
            radius: bind w/2
            //styleClass: "event"
            style: style_event
            smooth:true;
        };

        return Group
        {
            content: [
                shape,text
            ]
            scaleX: bind s;
            scaleY: bind s;
            opacity: bind o;
            effect: dropShadow
        };
    }
}

/******************************************************************************/
class ANDGateWay extends FlowObject
{
    public override function create(): Node
    {
        cursor=Cursor.HAND;
        w=50;
        h=50;
        shape= Polygon
        {
            points: [w/2,0,w,h/2,w/2,h,0,h/2]
            style: style_gateway
            smooth: true;
        };

        return Group
        {
            content: [
                shape,
                Line{
                    startX: w/2-w/4
                    startY: h/2
                    endX: w/2+w/4
                    endY: h/2
                    style: style_simbol
                    smooth: true;
                    strokeLineCap: StrokeLineCap.ROUND
                }, Line{
                    startX: w/2
                    startY: h/2-h/4
                    endX: w/2
                    endY: h/2+h/4
                    style: style_simbol
                    smooth: true;
                    strokeLineCap: StrokeLineCap.ROUND
                }
            ]
            translateX: bind x - w/2
            translateY: bind y - w/2
            scaleX: bind s;
            scaleY: bind s;
            opacity: bind o;
            effect: dropShadow
        };
    }
}

/******************************************************************************/
class ORGateWay extends FlowObject
{
    public override function create(): Node
    {
        cursor=Cursor.HAND;
        w=50;
        h=50;
        shape= Polygon
        {
            points: [w/2,0,w,h/2,w/2,h,0,h/2]
            style: style_gateway
            smooth: true;
        };

        return Group
        {
            content: [
                shape, Circle
                {
                    centerX: w/2
                    centerY: h/2
                    radius: w/4
                    style: style_simbol
                    smooth: true;
                }
            ]
            translateX: bind x - w/2
            translateY: bind y - w/2
            scaleX: bind s;
            scaleY: bind s;
            opacity: bind o;
            effect: dropShadow
        };
    }
}


/******************************************************************************/
class GateWay extends FlowObject
{
    public override function create(): Node
    {
        cursor=Cursor.HAND;
        w=50;
        h=50;
        shape= Polygon
        {
            points: [w/2,0,w,h/2,w/2,h,0,h/2]
            style: style_gateway
            smooth: true;
        };

        return Group
        {
            content: [
                shape
            ]
            translateX: bind x - w/2
            translateY: bind y - w/2
            scaleX: bind s;
            scaleY: bind s;
            opacity: bind o;
            effect: dropShadow
        };
    }
}

/******************************************************************************/
class SubProcess extends FlowObject
{
    public override function create(): Node
    {
        cursor=Cursor.HAND;
        w=100;
        h=60;
        text=EditableText
        {
            text: bind title with inverse
            x:bind x
            y:bind y - h/5
            width: bind w
            height: bind h/2
        }

        shape= Rectangle
        {
            x: bind x-w/2
            y: bind y-h/2
            width: w
            height: h
            //effect: lighting
            //styleClass: "task"
            style: style_task
            smooth:true;
        };

        return Group
        {
            content: [
                shape,text, Rectangle
                {
                    x: bind x-10
                    y: bind y+2
                    width: 20
                    height: 20
                    style: style_message
                    smooth:true;
                }, Line{
                    startX: bind x-6
                    startY: bind y+12
                    endX: bind x+6
                    endY: bind y+12
                    style: style_message
                    smooth:true;
                }, Line{
                    startX: bind x
                    startY: bind y+12-6
                    endX: bind x
                    endY: bind y+12+6
                    style: style_message
                    smooth:true;
                }
            ]
            scaleX: bind s;
            scaleY: bind s;
            opacity: bind o;
            effect: dropShadow
        };
    }

}

/******************************************************************************/
class Task extends FlowObject
{
    public override function create(): Node
    {
        cursor=Cursor.HAND;
        w=100;
        h=60;
        text=EditableText
        {
            text: bind title with inverse
            x:bind x
            y:bind y
            width: bind w
            height: bind h
        }

        shape= Rectangle
        {
            x: bind x-w/2
            y: bind y-h/2
            width: w
            height: h
            //effect: lighting
            //styleClass: "task"
            style: style_task
            smooth:true;
        };

        return Group
        {
            content: [
                shape,text
            ]
            scaleX: bind s;
            scaleY: bind s;
            opacity: bind o;
            effect: dropShadow
        };
    }
}

/******************************************************************************/
class FlowObject extends GraphElement
{
    public var pool : Pool;
    public var dpx : Number;                        //diferencia de pool
    public var dpy : Number;                        //diferencia de pool

    override public function create(): Node
    {
        text=EditableText
        {
            text: bind title with inverse
            width: bind w;
            height: bind h;
        }
        return text;
    }

    var px = bind pool.x on replace
    {
        if(pool!=null)x=px+dpx;
    }
    var py = bind pool.y on replace
    {
        if(pool!=null)y=py+dpy;
    }


    override public function mousePressed( e: MouseEvent )
    {
        super.mousePressed(e);
        if(modeler.clickedNode==this)
        {
            if(e.secondaryButtonDown)
            {
                modeler.tempNode=FlowLink
                {
                    uri:"flowlink"
                }
            }
        }
    }

    override public function mouseReleased( e: MouseEvent )
    {
        super.mouseReleased(e);
        if(pool!=null)
        {
            dpx=x-pool.x;
            dpy=y-pool.y;
        }

    }

    override public function remove()
    {
        super.remove();
        for(connection in modeler.contents where connection instanceof ConnectionObject)
        {
            var c=connection as ConnectionObject;
            if(c.end == this)c.remove();
            if(c.ini == this)c.remove();
        }
    }


}

/******************************************************************************/
class Pool extends GraphElement
{
    override public function create(): Node
    {
        cursor=Cursor.HAND;
        w=400;
        h=100;
        text=EditableText
        {
            text: bind title with inverse
            x:bind x-w/2+10
            y:bind y
            width: bind h
            height: 20
            rotate: -90
        }

        shape= Rectangle
        {
            x: bind x-w/2
            y: bind y-h/2
            width: w
            height: h
            style: style_pool
            smooth:true;
        };

        return Group
        {
            content: [
                shape,text
            ]
            scaleX: bind s;
            scaleY: bind s;
            opacity: bind o;
            effect: dropShadow
        };
    }

    override public function mousePressed( e: MouseEvent )
    {
       super.mousePressed(e);
    }

    override public function remove()
    {
       super.remove();
    }

}


/******************************************************************************/
class GraphElement extends CustomNode
{
    public var x : Number;
    public var y : Number;
    public var w : Number;
    public var h : Number;

    public var title : String;
    public var uri : String;

    var shape : Shape;
    var text : EditableText;

    var mx : Number;                        //temporal movimiento x
    var my : Number;                        //temporal movimiento y
    var s : Number = 1;                     //temporal size
    var o : Number = 0.8;                   //opacity
    var dx : Number;                        //temporal drag x
    var dy : Number;                        //temporal drag y
    var stkw : Number = 2;                  //strokeWidth
    var stkwo : Number = 3;                 //strokeWidth Over

    var focusState = bind focused on replace
    {
        if (focused)
        {
            println("focused");
        }
        else
        {
            shape.stroke=Color.web(color);
            println("lost focus");
        }
    }

    public override function create(): Node
    {
        text=EditableText
        {
            text: bind title with inverse
            width: bind w;
            height: bind h;
        }
        return text;
    }

    override var onMouseClicked = function ( e: MouseEvent ) : Void
    {
        mouseClicked(e);
    }

    public function mouseClicked( e: MouseEvent )
    {
        println("onMouseClicked node:{e}");
        if(modeler.focusedNode==this)
        {
            if(e.clickCount >= 2)
            {
                println("starEditing");
                text.startEditing();
            }
        }
    }


    override var onMouseDragged = function ( e: MouseEvent ) : Void
    {
        mouseDragged(e);
    }

    public function mouseDragged( e: MouseEvent )
    {
        if(modeler.clickedNode==this)
        {
            var ax=dx+e.sceneX;
            var ay=dy+e.sceneY;
            if(ax-w/2>0)x=ax else x=w/2;
            if(ay-h/2>0)y=ay else y=h/2;
        }
    }

    override var onMousePressed = function( e: MouseEvent ):Void
    {
        mousePressed(e);
    }

    public function mousePressed( e: MouseEvent )
    {
        if(modeler.clickedNode==null)
        {
            modeler.clickedNode=this;
            modeler.focusedNode=this;
            //if(modeler.tempNode==null)
                modeler.disablePannable=true;
            dx=x-e.sceneX;
            dy=y-e.sceneY;
            //toFront();
            requestFocus();
        }
        println("onMousePress node:{e}");
    }


    override var onMouseReleased = function( e: MouseEvent ):Void
    {
        mouseReleased(e);
    }

    public function mouseReleased( e: MouseEvent )
    {
        if(modeler.clickedNode==this)
        {
            modeler.clickedNode=null;
            //if(modeler.tempNode==null)modeler.disablePannable=false;
            x=(Math.round(x/25))*25;            //grid
            y=(Math.round(y/25))*25;            //grid
        }
        println("onMouseRelease node");
    }

    override var onMouseEntered = function(e)
    {
        modeler.overNode=this;
        shape.stroke=Color.web(color_over);
        shape.strokeWidth=stkwo;
        //overtimer.playFromStart();
        if(modeler.tempNode==null)modeler.disablePannable=true;
    }

    override var onMouseExited = function(e)
    {
        if(modeler.overNode==this and modeler.clickedNode==null)
        {
                modeler.overNode=null;
                if(modeler.tempNode==null)modeler.disablePannable=false;
        }
        if(focused) shape.stroke=Color.web(color_focused)
        else shape.stroke=Color.web(color);
        shape.strokeWidth=stkw;
        //normaltimer.playFromStart();
    }

    override var onKeyPressed = function( e: KeyEvent )
    {
        keyPressed(e);
    }

    public function keyPressed( e: KeyEvent )
    {
        if(e.code==e.code.VK_DELETE)
        {
            remove();
        }
        println(e);
    }


    public function remove()
    {
        modeler.remove(this);
    }
}

/******************************************************************************/
class FlowLink extends ConnectionObject
{

}

/******************************************************************************/
class ConnectionObject extends CustomNode
{
    public var ini : FlowObject;
    public var end : FlowObject;

    public var title : String;
    public var uri : String;

    var text : Text;

    var points : Point[];
    var path : Path;

    var o : Number = 0.8;                   //opacity

    public override function create(): Node
    {
        cursor=Cursor.HAND;
//        text= Text
//        {
//             content: bind title
//             styleClass: "task-text"
//             textOrigin: TextOrigin.TOP
//             transforms: [
//                 Translate{
//                     x: bind x-(text.boundsInLocal.width)/2+2
//                     y: bind y-(text.boundsInLocal.height)/2+2
//                 }
//             ]
//             smooth:true;
//        };

//                HLineTo { x: 70 },
//                QuadCurveTo { x: 120  y: 60  controlX: 100  controlY: 0 },
//                ArcTo { x: 10  y: 50  radiusX: 100  radiusY: 100  sweepFlag: true },

        var pini=Point{ x: bind getConnectionX(ini,end) y: bind getConnectionY(ini,end) };
        var pend=Point{ x: bind getConnectionX(end,ini) y: bind getConnectionY(end,ini) };
        var pinter1=Point{ x: bind getInter1ConnectionX(ini,end,pini,pend) y: bind getInter1ConnectionY(ini,end,pini,pend) };
        var pinter2=Point{ x: bind getInter2ConnectionX(ini,end,pini,pend) y: bind getInter2ConnectionY(ini,end,pini,pend) };
        points=[pini,pinter1,pinter2,pend];

        path=Path {
            elements: [
                MoveTo{x:bind pini.x,y:bind pini.y},
                LineTo{x:bind pinter1.x,y:bind pinter1.y},
                LineTo{x:bind pinter2.x,y:bind pinter2.y},
                LineTo{x:bind pend.x,y:bind pend.y}
            ]
            style: style_connection
            smooth:true;
            strokeLineCap: StrokeLineCap.ROUND
            strokeLineJoin: StrokeLineJoin.ROUND

        };

        return Group
        {
            content: [
                path, text,
                Line{
                    startX: bind pend.x;
                    startY: bind pend.y;
                    endX: bind pend.x+6*Math.cos(getArrow(points, -45));
                    endY: bind pend.y-6*Math.sin(getArrow(points, -45));
                    style: style_connection_arrow
                    stroke: bind path.stroke;
                    strokeLineCap: StrokeLineCap.ROUND
                    smooth:true;
                },
                Line{
                    startX: bind pend.x;
                    startY: bind pend.y;
                    endX: bind pend.x+6*Math.cos(getArrow(points, 45));
                    endY: bind pend.y-6*Math.sin(getArrow(points, 45));
                    style: style_connection_arrow
                    stroke: bind path.stroke;
                    strokeLineCap: StrokeLineCap.ROUND
                    smooth:true;
                }
            ]
            opacity: bind o;
            effect: dropShadow
        };
    }

    public function remove()
    {
        modeler.remove(this);
    }

//    override var onMouseDragged = function ( e: MouseEvent ) : Void
//    {
//        if(modeler.clickedNode==this)
//        {
////            x=dx+e.sceneX;
////            y=dy+e.sceneY;
//        }
//    }

    override var onMousePressed = function( e: MouseEvent ):Void
    {
        if(modeler.clickedNode==null)
        {
            modeler.clickedNode=this;
            modeler.focusedNode=this;
        }
    }

    override var onMouseReleased = function( e: MouseEvent ):Void
    {
        if(modeler.clickedNode==this)
        {
            modeler.clickedNode=null;
        }
    }

    override var onMouseEntered = function(e)
    {
        if(modeler.tempNode==null and modeler.clickedNode==null)modeler.disablePannable=true;
        path.stroke=Color.web(color_over);
        path.strokeWidth=3;
    }

    override var onMouseExited = function(e)
    {
        if(modeler.tempNode==null and modeler.clickedNode==null)modeler.disablePannable=false;
        path.stroke=Color.web(color);
        path.strokeWidth=2;
    }

    bound function getConnectionX(ini: FlowObject, end: FlowObject): Number
    {
        if(ini!=null)
        {
            if(end!=null)
            {
                var dx=end.x-ini.x;
                var dy=end.y-ini.y;
                if(Math.abs(dx)>=Math.abs(dy))
                {
                    if(dx>0)
                    {
                        ini.x+ini.w/2+2;
                    }else
                    {
                        ini.x-ini.w/2-2;
                    }
                }else
                {
                    ini.x;
                }
            }else
            {
                var dx=modeler.mousex-ini.x;
                var dy=modeler.mousey-ini.y;
                if(Math.abs(dx)>=Math.abs(dy))
                {
                    if(dx>0)
                    {
                        ini.x+ini.w/2+2;
                    }else
                    {
                        ini.x-ini.w/2-2;
                    }
                }else
                {
                    ini.x;
                }
            }
        }else
        {
            var dx=end.x-modeler.mousex;
            var dy=end.y-modeler.mousey;
            if(Math.abs(dx)>=Math.abs(dy))
            {
                if(dx>0)
                {
                    modeler.mousex+10/2+2;
                }else
                {
                    modeler.mousex-10/2-2;
                }
            }else
            {
                modeler.mousex;
            }
        }
    }

    bound function getConnectionY(ini: FlowObject, end: FlowObject): Number
    {
        if(ini!=null)
        {
            if(end!=null)
            {
                var dx=end.x-ini.x;
                var dy=end.y-ini.y;
                if(Math.abs(dy)>Math.abs(dx))
                {
                    if(dy>0)
                    {
                        ini.y+ini.h/2+2;
                    }else
                    {
                        ini.y-ini.h/2-2;
                    }
                }else
                {
                    ini.y;
                }
            }else
            {
                var dx=modeler.mousex-ini.x;
                var dy=modeler.mousey-ini.y;
                if(Math.abs(dy)>Math.abs(dx))
                {
                    if(dy>0)
                    {
                        ini.y+ini.h/2+2;
                    }else
                    {
                        ini.y-ini.h/2-2;
                    }
                }else
                {
                    ini.y;
                }
            }
        }else
        {
            var dx=end.x-modeler.mousex;
            var dy=end.y-modeler.mousey;
            if(Math.abs(dy)>Math.abs(dx))
            {
                if(dy>0)
                {
                    modeler.mousey+10/2+2;
                }else
                {
                    modeler.mousey-10/2-2;
                }
            }else
            {
                modeler.mousey;
            }
        }
    }

    bound function getInter1ConnectionX(ini: FlowObject, end: FlowObject, pini: Point,pend: Point): Number
    {
        if(end!=null)
        {
            if(ini.y!=pini.y)
            {
                pini.x
            }else
            {
                pini.x+(pend.x-pini.x)/2;
            }
        }else
        {
            pini.x;
        }

    }

    bound function getInter1ConnectionY(ini: FlowObject, end: FlowObject, pini: Point,pend: Point): Number
    {
        if(end!=null)
        {
            if(ini.y!=pini.y)
            {
                pini.y+(pend.y-pini.y)/2;
            }else
            {
                pini.y
            }
        }else
        {
            pini.y;
        }
    }

    bound function getInter2ConnectionX(ini: FlowObject, end: FlowObject, pini: Point,pend: Point): Number
    {
        if(end!=null)
        {
            if(end.y!=pend.y)
            {
                pend.x
            }else
            {
                pini.x+(pend.x-pini.x)/2;
            }
        }else
        {
            getInter1ConnectionX(ini, end, pini, pend);
        }
    }

    bound function getInter2ConnectionY(ini: FlowObject, end: FlowObject, pini: Point,pend: Point): Number
    {
        if(end!=null)
        {
            if(end.y!=pend.y)
            {
                pini.y+(pend.y-pini.y)/2;
            }else
            {
                pend.y
            }
        }else
        {
            getInter1ConnectionY(ini, end, pini, pend);
        }
    }

    bound function getArrow(points:Point[], grad: Number) : Number
    {
        var pini:Point=points[(sizeof points)-2];
        var pend:Point=points[(sizeof points)-1];

        if(pend.x >= pini.x)
        {
            Math.PI-Math.atan((pend.y-pini.y)/(pend.x-pini.x))+(grad*Math.PI)/180;
        }else
        {
            2*Math.PI-Math.atan((pend.y-pini.y)/(pend.x-pini.x))+(grad*Math.PI)/180;
        }
    }
}

/******************************************************************************/
class Point
{
    public var x : Number;
    public var y : Number;
}

/******************************************************************************/
class EditableText extends CustomNode
{
    public var x : Number;
    public var y : Number;
    public var width : Number;
    public var height : Number;

    public var text : String;

    var textb : TextBox;
    var textl : Text;

    public function stopEditing() :Void
    {
        //textb.unselect();
        text=textb.text;
        cancelEditing();
    }
    
    public function cancelEditing() :Void
    {
        textb.visible=false;
        textl.visible=true;
    }


    public function startEditing() :Void
    {
        textb.text=text;
        textl.visible=false;
        textb.visible=true;
        textb.selectAll();
        textb.requestFocus();
    }

    public override function create(): Node
    {
        textl= Text
        {
             content: bind text
             style: style_task_text
             textOrigin: TextOrigin.TOP
             wrappingWidth: bind width
             transforms: [
                 Translate{
                     x: bind x-(textl.boundsInLocal.width)/2+2
                     y: bind y-(textl.boundsInLocal.height)/2
                 }
             ]
             //smooth:true;
             visible: true
        };
        textb= TextBox
        {
             text: text
             style: style_task_textbox
             translateX:bind x - width/2
             translateY:bind y -10
             width:bind width
             height: 20
             visible: false
             selectOnFocus:true
             onKeyTyped:function(k:KeyEvent)
             {
                 //if(k.char=="\n")stopEditing();
                 var c=0+k.char.charAt(0);
                 if(c==27)cancelEditing();
                 //println(c);
             }
             action: function() {
                stopEditing();
             }

        };
        return Group
        {
            content: [
               textl,textb
            ]
        };
    }
}
