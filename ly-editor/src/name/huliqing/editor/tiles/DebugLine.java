/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;
import com.jme3.util.TempVars;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class DebugLine extends Node {
    
    private final Geometry line;
    
    public DebugLine() {
        Line lineShape = new Line(new Vector3f(0, 0, -10000)
                , new Vector3f(0, 0, 10000));
        line = new Geometry("debugLine", lineShape);
        line.setMaterial(MaterialUtils.createUnshaded());
        attachChild(line);
    }
    
    public void setDirection(Vector3f direction) {
        TempVars tv = TempVars.get();
        tv.quat1.lookAt(direction, Vector3f.UNIT_Y);
        if (getParent() != null) {
            tv.quat2.set(getParent().getWorldRotation()).inverseLocal();
            tv.quat2.multLocal(tv.quat1);
            tv.quat2.normalizeLocal();
            setLocalRotation(tv.quat2);
        } else {
            setLocalRotation(tv.quat1);
        }
        tv.release();
    }
    
    public void setColor(ColorRGBA color) {
        line.getMaterial().setColor("Color", color);
    }
}
