/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * 操作行为物体
 * @author huliqing
 */
public abstract class TransformObj extends Node {

    protected final DebugLine debugLine = new DebugLine();
    
    public TransformObj() {
        attachChild(debugLine);
        debugLine.setCullHint(CullHint.Always);
    }
    
    public boolean isVisible() {
        return getCullHint() == CullHint.Never;
    }

    public void setVisible(boolean visible) {
        setCullHint(visible ? CullHint.Never : CullHint.Always);
    }
    
    public void showDebugLine(Axis axis, boolean visible) {
        debugLine.setCullHint(visible ? CullHint.Never : CullHint.Always);
        if (axis != null) {
            debugLine.setDirection(axis.getWorldDirection(new Vector3f()));
        }
    }
    
    /**
     * 通过射线获取一个操作轴，如果没有可用的操作轴则返回null.
     * @param ray
     * @return 
     */
    public abstract Axis pickTransformAxis(Ray ray);
    
}
