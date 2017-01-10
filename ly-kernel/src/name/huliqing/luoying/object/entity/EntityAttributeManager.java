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
package name.huliqing.luoying.object.entity;

import java.util.List;
import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.AttributeManagerImpl;

/**
 * Entity的属性管理器
 * @author huliqing
 */
public class EntityAttributeManager extends AttributeManagerImpl {
//    private static final Logger LOG = Logger.getLogger(EntityAttributeManager.class.getName());
    
    private final Entity entity;
    
    public EntityAttributeManager(Entity entity) {
        this.entity = entity;
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
    }
    
    public void initialize(Entity actor) {
        // 载入所有属性
        List<AttributeData> ods = actor.getData().getObjectDatas(AttributeData.class, null);
        if (ods != null && !ods.isEmpty()) {
            for (AttributeData od : ods) {
                addAttribute((Attribute) Loader.load(od));
            }
        }
    }
    
    public void cleanup() {
        // 清理的时候只从store中清理即可，不要从entity.getData()中清理掉，因为
        List<Attribute> attributes = store.getAttributes();
        for (Attribute attr : attributes) {
            attr.cleanup();
        }
        store.clear();
    }
    
    /**
     * 添加新的属性，注：如果已经存在相同id或名称的属性，则旧的属性会被替换掉。
     * @param attribute 
     */
    @Override
    public void addAttribute(Attribute attribute) {
        super.addAttribute(attribute);
        entity.getData().addObjectData(attribute.getData());
    }
    
    /**
     * 移除指定的属性。
     * @param attribute
     * @return 
     */
    @Override
    public boolean removeAttribute(Attribute attribute) {
        AttributeData data = attribute.getData();
        if (super.removeAttribute(attribute)) {
            entity.getData().removeObjectData(data);
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return super.toString() + ", entityId=" + entity.getData().getId();
    }
    
}
