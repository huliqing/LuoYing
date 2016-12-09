/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

import name.huliqing.luoying.object.entity.Entity;

/**
 * 实体获得天赋点数的消息。
 * @author huliqing
 */
public class EntityTalentPointAddedMessage extends EntityMessage {

    private final int talentPointsAdded;
    
    public EntityTalentPointAddedMessage(int stateCode, String message, Entity entity, int talentPointsAdded) {
        super(stateCode, message, entity);
        this.talentPointsAdded = talentPointsAdded;
    }

    /**
     * 获得<b>新增</b>的天赋点数。
     * @return 
     */
    public int getTalentPointsAdded() {
        return talentPointsAdded;
    }
    
}
