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
package name.huliqing.luoying.object.shortcut;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.data.ShortcutData;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 快捷方式基类
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractShortcut<T extends ObjectData> implements Shortcut<T> {

    // 暂未用到,暂不开放到xml配置
    private ShortcutData data;
    
    protected T objectData;
    
    protected Entity entity;
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
    public Entity getActor() {
        return entity;
    }

    @Override
    public void setActor(Entity actor) {
        this.entity = actor;
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
