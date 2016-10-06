/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.shortcut;

import com.jme3.math.Vector3f;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.data.ShortcutData;
import name.huliqing.ly.object.actor.Actor;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractShortcut<T extends ObjectData> implements Shortcut<T> {

    // 暂未用到,暂不开放到xml配置
    private ShortcutData data;
    
    protected T objectData;
    
    protected Actor actor;
    protected float width;
    protected float height;
    protected final Vector3f location = new Vector3f();
    protected boolean dragEnabled;
    
    protected boolean initialized;
    
    @Override
    public void setData(ShortcutData data) {
        this.data = data;
    }

    @Override
    public ShortcutData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Shortcut already initialized!");
        }
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
    @Override
    public T getObjectData() {
        return objectData;
    }

    @Override
    public void setObjectData(T objectData) {
        this.objectData = objectData;
    }
    
    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public Vector3f getLocation() {
        return location;
    }

    @Override
    public void setLocation(Vector3f location) {
        this.location.set(location);
    }
 
    @Override
    public boolean isDragEnabled() {
        return dragEnabled;
    }

    @Override
    public void setDragEnagled(boolean dragEnabled) {
        this.dragEnabled = dragEnabled;
    }

    
}
