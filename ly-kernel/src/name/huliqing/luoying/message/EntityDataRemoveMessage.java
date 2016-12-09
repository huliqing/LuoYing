/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 实体移除物品时的消息
 * @author huliqing
 * @param <T>
 */
public class EntityDataRemoveMessage<T extends ObjectData> extends EntityMessage {

    private final T objectData;
    private final int count;
    
    /**
     * @param stateCode 状态码
     * @param message 消息内容
     * @param entity 删除了物品的实体
     * @param objectData 被删除的物品
     * @param count 删除的数量
     */
    public EntityDataRemoveMessage(int stateCode, String message, Entity entity, T objectData, int count) {
        super(stateCode, message, entity);
        this.objectData = objectData;
        this.count = count;
    }

    /**
     * 获取删除的物体
     * @return 
     */
    public T getObjectData() {
        return objectData;
    }

    /**
     * 获取移除的物品数量
     * @return 
     */
    public int getCount() {
        return count;
    }
    
}
