/*
 * ContainerPath.fx
 *
 * Created on 13/04/2010, 11:37:17 AM
 */

package org.semanticwb.process.modeler;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.layout.Flow;
import javafx.scene.text.Text;
import javafx.geometry.HPos;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polyline;


/**
 * @author javier.solis
 */

public class ContainerPath extends CustomNode
{
    public var modeler:Modeler;
    public var x:Number;
    public var y:Number;
    public var w:Number;
    public var h:Number;
    public var flow:Flow;
    public var contents: Node[];

    var container = bind modeler.containerElement on replace
    {
        delete contents;
        addContainer(container);
    }

    public function getText(txt:String): Text
    {
        return Text
        {
            content: txt
            styleClass: "containerButton"
            id: "label"
        };
    }

    public function addContainer(container:GraphicalElement): Void
    {
        var button = ContainerButton
        {
            container:container
            modeler:modeler
        }
        
        insert button before contents[0];
        if(container!=null)
        {
            var t = getText(">");
            var r: Rectangle;
            var g = Group {
                content: [
                    r = Rectangle {
                        width: 10
                        height:bind t.boundsInParent.height + 5;
                        stroke: null
                        fill: null
                    },
                    Polyline {
                        points: bind [r.x, r.y, r.x + r.width, r.y + r.height / 2, r.x, r.y + r.height]
                        styleClass: "containerPath"
                        id: "separator"
                    }
                ]
            }
            
            insert g before contents[0];
            addContainer(container.container);
        }
    }

    public override function create(): Node
    {
        flow=Flow {
                    height: bind h
                    width: bind w
                    content: bind contents
                    nodeHPos:HPos.RIGHT
        };
        var ret=Group
        {
             layoutX:bind x+2
             layoutY:bind y+2
             content: [
                Rectangle{
                    x:bind flow.boundsInLocal.minX-3
                    y:bind flow.boundsInLocal.minY-1
                    width:bind flow.boundsInLocal.width+2
                    height:bind flow.boundsInLocal.height+2
                    styleClass: "containerPath"
                },
                flow
             ]
        };
        return ret;
    }
}