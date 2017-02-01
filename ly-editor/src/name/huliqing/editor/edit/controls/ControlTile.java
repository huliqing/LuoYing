/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.controls;

import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
import name.huliqing.luoying.manager.PickManager;

/**
 * 控制组件
 * @author huliqing
 * @param <T>
 */
public abstract class ControlTile<T> {
    
    public enum Type {
        location, rotation, scale;
    }
    
    protected ControlTile parent;
    protected final SafeArrayList<ControlTile> children = new SafeArrayList<ControlTile>(ControlTile.class);
    protected boolean initialized;
    
    protected T target;
    
    public void setTarget(T target) {
        if (this.target != null) {
            throw new IllegalStateException("target already set, could not change!, target=" 
                    + this + ", target=" + target);
        }
        this.target = target;
    }
    
    public T getTarget() {
        return target;
    }
    
    /**
     * 初始化,该方法在ControlTile放到场景时调用一次，之后不再调用。
     * @param parent 
     */
    public void initialize(Node parent) {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public void cleanup() {
        initialized = false;
    }
    
    public final void setLocalTranslation(Vector3f location) {
        getControlSpatial().setLocalTranslation(location);
        onLocationUpdated(location);
        if (parent != null) {
            parent.onChildUpdated(this, Type.location);
        }
    }
    
    public final void setLocalRotation(Quaternion rotation) {
        getControlSpatial().setLocalRotation(rotation);
        onRotationUpdated(rotation);
        if (parent != null) {
            parent.onChildUpdated(this, Type.rotation);
        }
    }
    
    public final void setLocalScale(Vector3f scale) {
        getControlSpatial().setLocalScale(scale);
        onScaleUpdated(scale);
        if (parent != null) {
            parent.onChildUpdated(this, Type.scale);
        }
    }
    
    /**
     * 添加一个子控制物体
     * @param child 
     */
    protected void addChildControl(ControlTile child) {
        if (!children.contains(child)) {
            child.parent = this;
            children.add(child);
        }
    }
    
    /**
     * 判断当前ControlTile是否与给定的射线相交叉，并返回最近的交叉点，如果不存在该点则返回null.
     * 该方法用于ControlTile的选择控制。
     * @param ray
     * @return 
     */
    public Float pickCheck(Ray ray) {
        Spatial cs = getControlSpatial();
        if (cs == null) {
            return null;
        }
        return PickManager.distanceOfPick(ray, cs);
    }
    
    /**
     * 获取控制物体,只用于只读，不要直接写操作该空间的变换。
     * @return 
     */
    public abstract Spatial getControlSpatial();
    
    /**
     * 当控制物体位置更新时该方法被调用。
     * @param location 
     */
    protected abstract void onLocationUpdated(Vector3f location);
    
    /**
     * 当控制物体旋转更新时该方法被调用。
     * @param rotation 
     */
    protected abstract void onRotationUpdated(Quaternion rotation);
    
    /**
     * 当控制物体缩放更新时该方法被调用。
     * @param scale 
     */
    protected abstract void onScaleUpdated(Vector3f scale);
    
    /**
     * 当子控制物体发生变化时该方法被调用。
     * @param childUpdated 
     * @param type 
     */
    protected abstract void onChildUpdated(ControlTile childUpdated, Type type);
}
