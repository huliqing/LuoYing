/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.module.AttributeModuleData;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class AttributeModule<T extends AttributeModuleData> extends AbstractModule<T> {

    // 用于提升获取属性的速度，这个map中的内容与data.getAttributes()中的内容是一样的。
    private final Map<String, AttributeData> attributeMap = new HashMap<String, AttributeData>();
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        
        List<ObjectData> ods = actor.getData().getObjectDatas();
        if (ods != null && !ods.isEmpty()) {
            for (ObjectData od : ods) {
                if (od instanceof AttributeData) {
                    attributeMap.put(od.getId(), (AttributeData) od);
                }
            }
        }
    }

    @Override
    public void cleanup() {
        attributeMap.clear();
        super.cleanup(); 
    }
    
    /**
     * 获取指定id的属性，如果不存在，则返回null.
     * @param attributeId
     * @return 
     */
    public AttributeData getAttribute(String attributeId) {
        return attributeMap.get(attributeId);
    }
    
//    /**
//     * 获取所有的属性，如果不存在则返回null.
//     * @return 
//     */
//    public List<AttributeData> getAttributes() {
//        return data.getAttributes();
//    }
    
}
