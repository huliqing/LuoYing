/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import java.util.List;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.module.AttributeModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.attribute.AttributeStore;
import name.huliqing.core.object.attribute.AttributeStore.AttributeConflictException;

/**
 * 属性模块
 * @author huliqing
 * @param <T>
 */
public class AttributeModule<T extends AttributeModuleData> extends AbstractModule<T> {

    private Actor actor;
    private final AttributeStore store = new AttributeStore();
    
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
    }
    
    /**
     * 移除指定的属性。
     * @param attribute
     * @return 
     */
    public boolean removeAttribute(Attribute attribute) {
        boolean result = store.removeAttribute(attribute);
        attribute.cleanup();
        return result;
    }
    
    /**
     * 获取指定id的属性，如果不存在，则返回null.
     * @param attrId
     * @return 
     */
    public Attribute getAttributeById(String attrId) {
        return store.getAttributeById(attrId);
    }

    public Attribute getAttributeByName(String attrName) {
        return store.getAttributeByName(attrName);
    }
    
    public List<Attribute>getAttributes() {
        return store.getAttributes();
    }
}
