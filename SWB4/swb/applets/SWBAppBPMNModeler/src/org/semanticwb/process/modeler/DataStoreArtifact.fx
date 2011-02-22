/*
 * Pool.fx
 *
 * Created on 13/02/2010, 10:55:36 AM
 */

package org.semanticwb.process.modeler;

import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Path;
import javafx.scene.input.MouseEvent;

/**
 * @author javier.solis
 */

public class DataStoreArtifact extends Artifact
{
    override public function create(): Node
    {
        setCommonMenuOptions();
        w=80;
        h=60;
        text=EditableText
        {
            text: bind title with inverse
            x:bind x
            y:bind y+10
            width: bind w
            height: bind h
        }

        shape=Path
        {
            elements: [
                MoveTo { x: 0  y: bind h/7 },
                LineTo { x: 0  y: bind h-h/7 },
                ArcTo { x: bind w  y: bind h-h/7  radiusX: bind w/2  radiusY: bind h/7  sweepFlag: false },
                LineTo { x: bind w  y: bind h/7 },
                ArcTo { x: 0  y: bind h/7  radiusX: bind w/2  radiusY: bind h/7  sweepFlag: false },
                //ClosePath{},
                //MoveTo { x: 0  y: 0 },
                //ArcTo { x: bind w  y: 0  radiusX: 100  radiusY: 100  sweepFlag: true }
            ]
            styleClass: "dataObject"
            translateX:bind x-w/2
            translateY:bind y-h/2
            onKeyPressed: onKeyPressed
            onKeyReleased: onKeyReleased
        }

        var actions: MenuItem[] = [
            MenuItem {
                caption: ##"actCopy"
                action: function(e: MouseEvent) {
                    var t = copy();
                    modeler.copyNode = t;
                    ModelerUtils.popup.hide();
                }
            }
        ];
        insert actions before menuOptions[0];

        setType(TYPE_STORE);

        return Group
        {
            content: [
                shape,Path
                {
                    elements: [
                        MoveTo { x: bind w  y: bind h/7 },
                        ArcTo { x: 0  y: bind h/7  radiusX: bind w/2  radiusY: bind h/7  sweepFlag: true }
                    ]
                    stroke:bind shape.stroke
                    translateX:bind x-w/2
                    translateY:bind y-h/2
                },text
            ]
            scaleX: bind s;
            scaleY: bind s;
            visible: bind canView()
        };
    }

    override public function copy() : GraphicalElement {
        var t = DataStoreArtifact {
            title: this.title
            type: this.type
            modeler: this.modeler
            container: this.container
        }
        t.uri = "new:{type}:{modeler.toolBar.counter++}";
        return t;
    }
}