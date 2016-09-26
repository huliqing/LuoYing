/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.ModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.attribute.AttributeStore;
import name.huliqing.core.object.attribute.AttributeStore.AttributeConflictException;
import name.huliqing.core.object.attribute.NumberAttribute;

/**
 * 属性模块
 * @author huliqing
 */
public class AttributeModule extends AbstractModule<ModuleData> {

    private static final Logger LOG = Logger.getLogger(AttributeModule.class.getName());

    private final AttributeStore store = new AttributeStore();
    private List<AttributeListener> listeners;
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        
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
    
    /**
     * 给指定“名称”的属性添加值。注:所指定的属性必须存在，并且必须是 {@link NumberAttribute}类型的属性，
     * 否则什么也不做。
     * @param attrName 属性名称
     * @param value 
     */
    public void addNumberAttributeValue(String attrName, float value) {
        Attribute attr = getAttributeByName(attrName);
        if (attr instanceof NumberAttribute) {
            ((NumberAttribute)attr).add(value);
        } else {
            LOG.log(Level.WARNING, "Could not addNumberAttributeValue, attrName is not a NumberAttribute,"
                    + " actorId={0}, attrName={1}, value={2}", new Object[] {actor.getData().getId(), attrName, value});
        }
    }
    
    /**
     * 获取指定“名称“的NumberAttribute类型的属性的值，目标属性必须存在，并且必须是NubmerAttribute，
     * 否则这个方法将指返回defValue值。
     * @param attrName 属性"名称"非id.
     * @param defValue 默认值，如果找不到指定的属性或属性不是NumberAttribute类型则返回这个默认值。
     * @return 
     */
    public float getNumberAttributeValue(String attrName, float defValue) {
        Attribute attr = getAttributeByName(attrName);
        if (attr instanceof NumberAttribute) {
            return ((NumberAttribute) attr).floatValue();
        }
        return defValue;
    }
}
