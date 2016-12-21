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
package name.huliqing.luoying.object.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 一个属性管理器的基本实现。
 * @author huliqing
 */
public class AttributeManagerImpl implements AttributeManager {
    private static final Logger LOG = Logger.getLogger(AttributeManagerImpl.class.getName());
    
    protected final AttributeStore store = new AttributeStore();
    protected List<AttributeListener> listeners;
    
    /**
     * 更新属性值到data中
     */
    public void updateDatas() {
        List<Attribute> attributes = getAttributes();
        if (!attributes.isEmpty()) {
            for (Attribute attr : attributes) {
                attr.updateDatas();
            }
        }
    }
    
    /**
     * 添加新的属性，注：如果已经存在相同id或名称的属性，则旧的属性会被替换掉。
     * @param attribute 
     */
    @Override
    public void addAttribute(Attribute attribute) {
        // 如果已经存在指定属性，则替换掉
        Attribute oldAttriubteById = store.getAttributeById(attribute.getId());
        Attribute oldAttriubteByName = store.getAttributeByName(attribute.getName());
        if (oldAttriubteById != null) {
            removeAttribute(oldAttriubteById);
        }
        if (oldAttriubteByName != null) {
            removeAttribute(oldAttriubteByName);
        }
        try {
            store.addAttribute(attribute);
            if (!attribute.isInitialized()) {
                attribute.initialize(this);
            }
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onAttributeAdded(this, attribute);
                }
            }
        } catch (AttributeStore.AttributeConflictException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * 移除指定的属性。
     * @param attribute
     * @return 
     */
    @Override
    public boolean removeAttribute(Attribute attribute) {
        if (store.removeAttribute(attribute)) {
            attribute.cleanup();
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onAttributeRemoved(this, attribute);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 通过属性名称获取指定的属性，如果属性不存在则返回null<br>
     * 注：返回值为泛型(Attribute)，如果使用了特定的属性类型来接收返回值，
     * 则当返回类型不能转换为指定类型时会导致转换异常。
     * 要避免转换类型异常可使用{@link #getAttribute(java.lang.String, java.lang.Class) } 
     * @param <T>
     * @param attrName 属性名称，<b>不</b>是属性ID(<b>Not</b>id)
     * @return 
     * @see #getAttribute(java.lang.String, java.lang.Class) 
     */
    @Override
    public <T extends Attribute> T getAttribute(String attrName) {
        return (T) store.getAttributeByName(attrName);
    }

    @Override
    public <T extends Attribute> T getAttribute(long attrUniqueId) {
        return (T) store.getAttributeByUniqueId(attrUniqueId);
    }
    
    /**
     * 查找指定的属性，如果找不到或者指定的属性类型不匹配则返回null.
     * @param <T>
     * @param attrName 属性名称，<b>不</b>是属性ID(<b>Not</b>id)
     * @param type 如果不为null, 则找到的属性必须符合这个类型，否则返回null.
     * @return 
     * @see #getAttribute(java.lang.String) 
     */
    @Override
    public <T extends Attribute> T getAttribute(String attrName, Class<T> type) {
        Attribute attribute = store.getAttributeByName(attrName);
        if (attribute == null) {
            return null;
        }
        if (type != null && !type.isAssignableFrom(attribute.getClass())) {
            LOG.log(Level.WARNING, "Attribute {0} is not type of {1}, attributeManager={2}"
                    , new Object[] {attrName, type.getName(), this.toString()});
            return null;
        }
        return (T) attribute;
    }
    
    /**
     * 获取角色当前的所有属性，注：返回的列表只能只读，否则报错。
     * @return 
     */
    @Override
    public List<Attribute>getAttributes() {
        return store.getAttributes();
    }
    
    /**
     * 添加属性侦听器
     * @param attributeListener 
     */
    @Override
    public void addListener(AttributeListener attributeListener) {
        if (listeners == null) {
            listeners = new ArrayList<AttributeListener>();
        }
        if (!listeners.contains(attributeListener)) {
            listeners.add(attributeListener);
        }
    }
    
    /**
     * 移除指定的属性侦听器
     * @param attributeListener
     * @return 
     */
    @Override
    public boolean removeListener(AttributeListener attributeListener) {
        return listeners != null && listeners.remove(attributeListener);
    }

}
