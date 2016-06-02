/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill.impl;

import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.SkillData;

/**
 * 执行角色受伤技能.
 * @author huliqing
 */
public class HurtSkill extends SimpleSkill {

    public HurtSkill() {}
    
    public HurtSkill(SkillData skillData) {
       super(skillData);
    }
    
    @Override
    public void init() {
        super.init();
        
        if (data.getAnimation() == null && channelProcessor != null) {
            // Reset，对于没有“死亡”动画的角色，在死亡时必须让它“静止”
            // 让角色的其它动画停止播放，以防止角色在死亡之后仍然在做其它动作的奇怪现象。
            channelProcessor.reset();
        }
    }

//    @Override
//    public boolean isInRange(Actor actor) {
//        return false;
//    }
    
}
