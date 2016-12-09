/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

import name.huliqing.luoying.object.entity.Entity;

/**
 * 实体等级获得提升时的消息。
 * @author huliqing
 */
public class EntityLevelUpMessage extends EntityMessage {
    
    private final int level;
    
    public EntityLevelUpMessage(int stateCode, String message, Entity entity, int level) {
        super(stateCode, message, entity);
        this.level = level;
    }

    /**
     * 获取升级后的等级数
     * @return 
     */
    public int getLevel() {
        return level;
    }
    
}
