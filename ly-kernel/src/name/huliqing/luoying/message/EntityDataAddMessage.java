/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 实体添加物品时的消息
 * @author huliqing
 * @param <T>
 */
public class EntityDataAddMessage<T extends ObjectData> extends EntityMessage {

    private final T objectData;
    private final int count;
    
    /**
     * @param stateCode
     * @param message
     * @param entity
     * @param objectData
     * @param count 获得的物体数量
     */
    public EntityDataAddMessage(int stateCode, String message, Entity entity, T objectData, int count) {
        super(stateCode, message, entity);
        this.objectData = objectData;
        this.count = count;
    }

    /**
     * 获取添加的物体
     * @return 
     */
    public T getObjectData() {
        return objectData;
    }
    
    /**
     * 获得的物体数量
     * @return 
     */
    public int getCount() {
        return count;
    }
    
    
}
