package org.semanticwb.process.modeler;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.input.*;

/**
 * @author Hasdai Pacheco {haxdai@gmail.com}
 */
public class MenuPopup extends CustomNode {
public var x: Number = 0;
    public var y: Number = 0;
    public var optionsMargin: Number = 2;
    public var optionsPadding: Number = 1;
    public var miParent: MenuItem;
    public var modeler: Modeler;
    var maxH: Number = 0;
    var maxW: Number = 0;
    var os: MenuItem[];

    public var items:MenuItem[] = null on replace {
        buildOptions(items);
    }

    override public function create(): Node {
        blocksMouse = true;
        this.visible = false;

        var r = Rectangle {
            x: bind x
            y: bind y
            styleClass: "menu"
            width: bind maxW + optionsMargin * 2
            height: bind maxH + optionsMargin * 2 - optionsPadding * 2
        }

        return Group {
            cache: true;
            content: bind [r,os]
        }
    }

    function buildOptions(options: MenuItem[]) {
        delete os;
        maxH = optionsMargin;
        maxW = 0;

        for (option in options) {
            option.offsety = maxH;

            var o = MenuItem {
                owner:this
                x: bind x + optionsMargin
                y: bind y + option.offsety
                caption: bind option.caption
                textOffsetX: bind x + optionsMargin * 3
                isSeparator: bind option.isSeparator
                status: bind option.status
            }

            if (option.items != null and option.items.size() > 0) {
                o.mchild = MenuPopup {
                    items: option.items
                    miParent:o;
                }
                o.action = function (e: MouseEvent) {
                    if (e.button == e.button.PRIMARY) {
                        o.owner.closeMenus(o);
                        o.showChild(o.x + o.w + 4, o.y + o.h - 2);
                    }
                }
            } else {
                o.action = option.action;
            }

            if (o.getWidth() > maxW) {
                maxW = o.getWidth();
            }

            maxH += o.getHeight() + optionsPadding;
            insert o into os;
        }

        for (o in os) {
            o.sizeToText = false;
            o.w = maxW;
        }
    }

    public function show (x: Float, y: Float) : Void {
        this.x = x;
        this.y = y;
        this.show();
    }

    public function show(): Void {
        this.visible = true;
        for (o in os) {
            o.show();
        }
    }

    public function show (e: MouseEvent): Void {
        show(e.sceneX - 10, e.sceneY - 10);
    }

    public function hide(): Void {
        this.visible = false;
        for (o in os) {
            o.hide();
        }
    }

    public function closeMenus(m: MenuItem) {
        for (o in os) {
            if (o.mchild != null and o.mchild.visible and o != m) {
                o.mchild.hide();
            }
        }
    }

    public function setOptions(acts: MenuItem[]) {
        if (acts != null and acts.size() > 0) {
            this.items = acts;
            buildOptions(acts);
        }
    }
}
