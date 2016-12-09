/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

import name.huliqing.luoying.object.entity.Entity;

/**
 * Entity消息类型，用于定义实体添加物品，使用物品，删除物品时的消息。
 * @author huliqing
 */
public class EntityMessage extends AbstractMessage {
    
    private final Entity entity;
    
    /**
     * @param stateCode 消息状态码
     * @param message 消息内容
     * @param entity 消息源实体
     */
    public EntityMessage(int stateCode, String message, Entity entity) {
        super(stateCode, message);
        this.entity = entity;
    }
    
    /**
     * 获取消息的实体源
     * @return 
     */
    public Entity getEntity() {
        return entity;
    }
    
}
