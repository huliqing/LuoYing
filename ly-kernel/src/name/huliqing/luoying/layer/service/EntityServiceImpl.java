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

import java.util.Collections;
import java.util.List;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public class EntityServiceImpl implements EntityService {
    
    @Override
    public void inject() {
        // ignore
    }
        
    @Override
    public void hitAttribute(Entity entity, String attribute, Object value, Entity hitter) {
        entity.hitAttribute(attribute, value, hitter);
    }
    
    @Override
    public void hitNumberAttribute(Entity entity, String attribute, float addValue, Entity hitter) {
        NumberAttribute nattr = entity.getAttributeManager().getAttribute(attribute, NumberAttribute.class);
        if (nattr != null) {
            entity.hitAttribute(attribute, nattr.getValue().floatValue() + addValue, hitter);
        } 
    }
    
    @Override
    public Number getNumberAttributeValue(Entity entity, String attributeName, Number defValue) {
        NumberAttribute nattr = entity.getAttributeManager().getAttribute(attributeName, NumberAttribute.class);
        if (nattr != null) {
            return nattr.getValue();
        }
        return defValue;
    }

    @Override
    public boolean getBooleanAttributeValue(Entity entity, String attributeName, boolean defValue) {
        BooleanAttribute attr = entity.getAttributeManager().getAttribute(attributeName, BooleanAttribute.class);
        if (attr != null) {
            return attr.getValue();
        }
        return defValue;
    }

    @Override
    public <T> T getAttributeValue(Entity entity, String attributeName, T defValue) {
        Attribute attribute = entity.getAttributeManager().getAttribute(attributeName);
        if (attribute != null) {
            return (T) attribute.getValue();
        }
        return defValue;
    }
    
    @Override
    public List<ObjectData> getObjectDatas(Entity entity) {
        return Collections.unmodifiableList(entity.getData().getObjectDatas());
    }
    
    @Override
    public List<ObjectData> getObjectData(Entity entity, String id) {
        return entity.getData().getObjectDatas(id, null);
    }
    
    @Override
    public ObjectData getObjectDataByTypeId(Entity entity, String id) {
        return entity.getData().getObjectData(id);
    }

    @Override
    public ObjectData getObjectDataByUniqueId(Entity entity, long uniqueId) {
        return entity.getData().getObjectDataByUniqueId(uniqueId);
    }

    @Override
    public boolean addObjectData(Entity entity, ObjectData data, int amount) {
        return entity.addObjectData(data, amount);
    }

    // remove20161122不使用直接id添加物品的方式，这会造成添加后的物品的唯一id(uniqueId)在客户端和服务端不一致的问题。
//    @Override
//    public boolean addObjectData(Entity entity, String objectId, int amount) {
//        return entity.addObjectData(Loader.loadData(objectId), amount);
//    }

    @Override
    public boolean removeObjectData(Entity entity, long objectUniqueId, int amount) {
        ObjectData od = entity.getData().getObjectDataByUniqueId(objectUniqueId);
        return od != null && entity.removeObjectData(od, amount);
    }
    
    // remove20161211,看接口说明
//    @Override
//    public boolean useObjectData(Entity entity, ObjectData data) {
//        return entity.useObjectData(data);
//    }
    
    @Override
    public boolean useObjectData(Entity entity, long objectUniqueId) {
        ObjectData od = entity.getData().getObjectDataByUniqueId(objectUniqueId);
        return od != null && entity.useObjectData(od);
    }

    @Override
    public Entity cloneEntity(Entity entity) {
        if (!entity.isInitialized()) {
            entity.initialize();
        }
        // 注：克隆后的实体的唯一ID必须改变，否则放到场景的时候会冲突。
        // 实体内的其它物体则没有关系.
        entity.updateDatas();
        EntityData cloneData = (EntityData) entity.getData().clone();
        cloneData.setUniqueId(DataFactory.generateUniqueId());
        Entity clone = Loader.load(cloneData);
        return clone;
    }
    
    
}
