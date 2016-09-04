/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.module.ModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.attribute.AttributeStore;
import name.huliqing.core.object.attribute.AttributeStore.AttributeConflictException;

/**
 * 属性模块
 * @author huliqing
 */
public class AttributeModule extends AbstractModule<ModuleData> {

    private Actor actor;
    private final AttributeStore store = new AttributeStore();
    private List<AttributeListener> listeners;
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;
        
        // 载入所有属性
        List<AttributeData> ods = actor.getData().getObjectDatas(AttributeData.class, null);
        if (ods != null && !ods.isEmpty()) {
            for (AttributeData od : ods) {
                addAttribute((Attribute) Loader.load(od));
            }
        }
        
    }

    @Override
    public void cleanup() {
        store.clear();
        super.cleanup(); 
    }
    
    /**
     * 添加新的属性，注：如果已经存在相同id或名称的属性，则旧的属性会被替换掉。
     * @param attribute 
     */
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
            actor.getData().addObjectData(attribute.getData());
            if (!attribute.isInitialized()) {
                attribute.initialize(this);
            }
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onAttributeAdded(actor, attribute);
                }
            }
        } catch (AttributeConflictException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * 移除指定的属性。
     * @param attribute
     * @return 
     */
    public boolean removeAttribute(Attribute attribute) {
        if (store.removeAttribute(attribute)) {
            attribute.cleanup();
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onAttributeRemoved(actor, attribute);
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * 使用”id“来获取指定的属性，如果找不到则返回null.
     * @param attrId
     * @return 
     */
    public Attribute getAttributeById(String attrId) {
        return store.getAttributeById(attrId);
    }

    /**
     * 使用”名称“来查找指定的属性，如果找不到则返回null.
     * @param attrName
     * @return 
     */
    public Attribute getAttributeByName(String attrName) {
        return store.getAttributeByName(attrName);
    }
    
    /**
     * 获取角色当前的所有属性，注：返回的列表只能只读，否则报错。
     * @return 
     */
    public List<Attribute>getAttributes() {
        return store.getAttributes();
    }
    
    public void addListener(AttributeListener attributeListener) {
        if (listeners == null) {
            listeners = new ArrayList<AttributeListener>();
        }
        if (!listeners.contains(attributeListener)) {
            listeners.add(attributeListener);
        }
    }
    
    public boolean removeListener(AttributeListener attributeListener) {
        return listeners != null && listeners.remove(attributeListener);
    }
}
