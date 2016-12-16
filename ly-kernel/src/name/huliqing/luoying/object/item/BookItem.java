/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 技能书, 使用后消耗技能书,并获得一个指定的技能
 * @author huliqing
 */
public class BookItem extends AbstractItem {
    // 要学习的技能ID
    private String skill;
    
    @Override
    public void setData(ItemData data) {
        super.setData(data);
        skill = data.getAsString("skill");
        if (skill == null) {
            throw new NullPointerException("Skill could not be null, itemId=" + data.getId());
        }
    }
    
    @Override
    public int checkStateCode(Entity actor) {
        if (actor.getData().getObjectData(skill) != null) {
            return StateCode.DATA_USE_FAILURE;
        }
        return super.checkStateCode(actor); 
    }
    
    @Override
    protected void doUse(Entity actor) {
        // 学习技能
        SkillData skillData = Loader.loadData(skill);
        actor.addObjectData(skillData, 1);
        actor.removeObjectData(data, 1);
    }
    
}
