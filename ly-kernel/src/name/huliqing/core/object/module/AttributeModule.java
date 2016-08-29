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
        
        // 注：这里不要直接调用addAttribute,直接调用会在添加的时候同时初始化attribute,
        // 这会造成一些attribute在初始化的时候找不到被依赖的属性，因为一些属性可能还没有添加到store中。
        // 所以在这里初始化的时候先把attribute添加到attributeStore中，再一并初始化attribute.
        List<AttributeData> ods = actor.getData().getObjectDatas(AttributeData.class, null);
        if (ods != null && !ods.isEmpty()) {
            for (AttributeData od : ods) {
                try {
                    store.addAttribute((Attribute) Loader.load(od));
                } catch (AttributeConflictException e) {
                    throw new RuntimeException("Could not addAttribute: actorId=" + actor.getData().getId() 
                            + ", error=" + e.getMessage());
                }
            }
        }
        
        // 初始化所有attribute.
        List<Attribute> as = store.getAttributes();
        if (as != null && !as.isEmpty()) {
            for (Attribute a : as) {
                a.initialize(store);
            }
        }
        
    }

    @Override
    public void cleanup() {
        store.clear();
        super.cleanup(); 
    }
    
    /**
     * 添加新的属性，如果属性已经存在则抛出异常，相同实例的属性，或相同id,或相同名称的属性都会被视为相同。
     * @param attribute 
     * @throws name.huliqing.core.object.attribute.AttributeStore.AttributeConflictException 
     */
    public void addAttribute(Attribute attribute) throws AttributeConflictException{
        store.addAttribute(attribute);
        actor.getData().addObjectData(attribute.getData());
        if (!attribute.isInitialized()) {
            attribute.initialize(store);
        }
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onAttributeAdded(actor, attribute);
            }
        }
    }
    
    /**
     * 移除指定的属性。
     * @param attribute
     * @return 
     */
    public boolean removeAttribute(Attribute attribute) {
        boolean result = store.removeAttribute(attribute);
        attribute.cleanup();
        if (result && listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onAttributeRemoved(actor, attribute);
            }
        }
        return result;
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
