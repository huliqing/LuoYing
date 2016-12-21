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
package name.huliqing.luoying.layer.service;

import java.util.List;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public interface EntityService extends EntityNetwork {
    
    /**
     * 获取指定属性的数值，如果属性不存在或者不是Number类型，则返回defValue。
     * @param entity
     * @param attributeName
     * @param defValue
     * @return 
     */
    Number getNumberAttributeValue(Entity entity, String attributeName, Number defValue);
    
    /**
     * 获取指定Boolean属性的值，如果属性不存在或者不是Boolean类型，则返回defValue。
     * @param entity
     * @param attributeName
     * @param defValue
     * @return 
     */
    boolean getBooleanAttributeValue(Entity entity, String attributeName, boolean defValue);
    
    /**
     * 获取指定属性的值, 如果指定的属性不存在，则返回defValue.
     * @param <T>
     * @param entity
     * @param attributeName
     * @param defValue
     * @return 
     */
    <T> T getAttributeValue(Entity entity, String attributeName, T defValue);
    
    /**
     * 获取角色身上所有的物体,注：返回的列表不可以直接修改,只能作为只读使用。
     * @param entity
     * @return 
     */
    List<ObjectData> getObjectDatas(Entity entity);
    
    /**
     * 获取角色身上指定id类型的所有物体
     * @param entity
     * @param id
     * @return 
     */
    List<ObjectData> getObjectData(Entity entity, String id);
    
    /**
     * 获取角色身上指定id类型的物体，如果存在多个相同类型的物体，则<b>只返回第一个找到的</b>。
     * @param entity
     * @param id
     * @return 
     */
    ObjectData getObjectDataByTypeId(Entity entity, String id);
    
    /**
     * 从entity身上获取指定id(唯一id)的物体.
     * @param entity
     * @param uniqueId
     * @return 
     */
    ObjectData getObjectDataByUniqueId(Entity entity, long uniqueId);
    
    /**
     * 克隆一个实体
     * @param entity 被克隆的实体
     * @return 新的克隆体
     */
    Entity cloneEntity(Entity entity);
}
