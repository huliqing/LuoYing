/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import name.huliqing.editor.tiles.Axis.Type;

/**
 *
 * @author huliqing
 */
public class AxisNode extends Node {
    
    private final Axis axis;
    private final AxisLine axisLine = new AxisLine();
    private final Vector3f temp = new Vector3f();
    
    public AxisNode(Axis axis, ColorRGBA colorRGBA) {
        this.axis = axis;
        axisLine.setColor(colorRGBA);
        axisLine.setCullHint(CullHint.Always);
        attachChild(axis);
        attachChild(axisLine);
    }
    
    public void setAxisVisible(boolean visible) {
        axis.setCullHint(visible ? CullHint.Never : CullHint.Always);
    }
    
    public void setAxisLineVisible(boolean visible) {
        axisLine.setCullHint(visible ? CullHint.Never : CullHint.Always);
        if (axis != null) {
            axisLine.setDirection(axis.getWorldDirection(temp));
        }
    }
    
    public Type getType() {
        return axis.getType();
    }
    
    public Vector3f getDirection(Vector3f store) {
        return axis.getDirection(store);
    }
    
    public CollisionResults getCollisions(Ray ray, CollisionResults store) {
        return axis.getCollisions(ray, store);
    }
}
