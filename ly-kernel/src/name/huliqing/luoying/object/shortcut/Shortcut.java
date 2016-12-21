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
import com.jme3.scene.Spatial;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.data.ShortcutData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 快捷方式接口
 * @author huliqing
 * @param <T>
 */
public interface Shortcut<T extends ObjectData> extends DataProcessor<ShortcutData>{
    
    /**
     * 初始化快捷方式
     */
    void initialize();
    
    /**
     * 判断快捷方式是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理快捷方式
     */
    void cleanup();
   
    /**
     * 获取快捷方式界面节点。
     * @return 
     */
    Spatial getView();

    /**
     * 获取快捷方式的操作角色
     * @return 
     */
    Entity getActor();
    
    /**
     * 设置设置快捷方式的操作角色
     * @param actor 
     */
    void setActor(Entity actor);
    
    /**
     * @return 
     */
    T getObjectData();
    
    /**
     * @param objectData 
     */
    void setObjectData(T objectData);
   
    /**
     * 获取快捷方式的宽度
     * @return 
     */
    float getWidth();
    
    /**
     * 设置快捷方式宽度
     * @param width 
     */
    void setWidth(float width);
    
    /**
     * 获取快捷方式的高度
     * @return 
     */
    float getHeight();
    
    /**
     * 设置快捷方式高度
     * @param height 
     */
    void setHeight(float height);

    /**
     * 获取快捷方式的位置
     * @return 
     */
    Vector3f getLocation();
    
    /**
     * 设置快捷方式的位置
     * @param location 
     */
    void setLocation(Vector3f location);

    /**
     * 判断快捷方式是否可以拖动
     * @return 
     */
    boolean isDragEnabled();
    
    /**
     * 设置快捷方式是否可以拖动。
     * @param dragEnabled 
     */
    void setDragEnagled(boolean dragEnabled);
    
    /**
     * 删除物品
     */
    void removeObject();
}
