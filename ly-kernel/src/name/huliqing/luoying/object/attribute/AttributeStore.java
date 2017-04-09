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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 属性容器,用于包装属性列表，对属性列表进行增、删、改、查。
 * @author huliqing
 */
public class AttributeStore {
    
    private final List<Attribute> attributes = new ArrayList<Attribute>();
    
    private final List<Attribute> unmodifiableAttributes = Collections.unmodifiableList(attributes);
    
    // 使用attrId -> attribute方式存放属性列表
    private final Map<String, Attribute> attrIdMap = new HashMap<String, Attribute>();
    
    // 使用attrName -> attribute方式存放属性列表
    private final Map<String, Attribute> attrNameMap = new HashMap<String, Attribute>();
    
    /**
     * 添加一个属性到属性列表，属性ID和属性名称必须唯一，如果指定的属性的id或名称已经存在于属性列表中，
     * 则抛出AttributeConflictException异常。
     * @param attribute 
     * @throws name.huliqing.luoying.object.attribute.AttributeStore.AttributeConflictException 
     */
    public synchronized void addAttribute(Attribute attribute) throws AttributeConflictException{
        if (attributes.contains(attribute) 
                || attrIdMap.containsKey(attribute.getId())
                || attrNameMap.containsKey(attribute.getName())) {
            throw new AttributeConflictException("Attribute already exists! attribute=" + attribute 
                    + ", attributeId=" + attribute.getId() 
                    + ", attributeName=" + attribute.getName());
        }
        attributes.add(attribute);
        attrIdMap.put(attribute.getId(), attribute);
        attrNameMap.put(attribute.getName(), attribute);
    }
    
    /**
     * 移除一个指定属性，如果当前属性列表中不包含指定的属性则什么也不做，并返回false.
     * @param attribute
     * @return 
     */
    public synchronized boolean removeAttribute(Attribute attribute) {
        if (!attributes.contains(attribute)) {
            return false;
        }
        attributes.remove(attribute);
        attrIdMap.remove(attribute.getId());
        attrNameMap.remove(attribute.getName());
        return true;
    }
    
    /**
     * 清空属性列表
     */
    public synchronized void clear() {
        attributes.clear();
        attrIdMap.clear();
        attrNameMap.clear();
    }
    
    /**
     * 通过属性<b>id</b>来获取指定的属性 
     * @param attrId
     * @return 
     */
    public Attribute getAttributeById(String attrId) {
        return attrIdMap.get(attrId);
    }
    
    /**
     * 通过属性<b>名称</b>来获取属性。
     * @param attrName
     * @return 
     */
    public Attribute getAttributeByName(String attrName) {
        return attrNameMap.get(attrName);
    }
    
    /**
     * 获取所有的属性，返回的列表不能修改，只能只读，否则将报错。
     * @return 
     */
    public List<Attribute> getAttributes() {
        return unmodifiableAttributes;
    }
    
    public class AttributeConflictException extends Exception {
        public AttributeConflictException(String message) {
            super(message);
        }
    }
    
}
