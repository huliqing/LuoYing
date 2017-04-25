/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.editor.edit.controls;

import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
import name.huliqing.editor.edit.JmeEdit;
import name.huliqing.luoying.manager.PickManager;

/**
 * 控制组件
 * @author huliqing
 * @param <T>
 * @param <E>
 */
public abstract class ControlTile<T, E extends JmeEdit> {
    
    public enum Type {
        location, rotation, scale;
    }
    
    protected ControlTile parent;
    protected final SafeArrayList<ControlTile> children = new SafeArrayList<>(ControlTile.class);
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
     * @param edit 
     */
    public void initialize(E edit) {
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
    
    /**
     * 当ControlTile的目标对象发生变化时调用这个方法来更新ControlTile的状态。
     */
    public void updateState() {
        // 由子类覆盖调用，更新状态
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
