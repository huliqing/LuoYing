/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
