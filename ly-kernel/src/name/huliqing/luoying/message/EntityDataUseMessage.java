/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 实体使用物品时的消息
 * @author huliqing
 * @param <T>
 */
public class EntityDataUseMessage<T extends ObjectData> extends EntityMessage {

    private final T objectData;
    
    public EntityDataUseMessage(int stateCode, String message, Entity entity, T objectData) {
        super(stateCode, message, entity);
        this.objectData = objectData;
    }

    /**
     * 获取使用的物体
     * @return 
     */
    public T getObjectData() {
        return objectData;
    }
}
