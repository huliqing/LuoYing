/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.skill;

import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ChannelModule;

/**
 * 执行死亡技能,简单的死亡效果
 * @author huliqing
 */
public class DeadSkill extends AbstractSkill {
    private ChannelModule channelModule;
    
    // 是否死亡后立即移出场景
    private boolean remove;
    
    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        remove = data.getAsBoolean("remove", remove);
    }

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor); 
        channelModule = actor.getModuleManager().getModule(ChannelModule.class);
    }

    @Override
    public void initialize() {
        super.initialize(); 

        // Reset，对于没有“死亡”动画的角色，在死亡时必须让它“静止”
        // 让角色的其它动画停止播放，以防止角色在死亡之后仍然在做其它动作的奇怪现象。
        if (animation == null) {
            channelModule.reset();
        }
    }
    
    @Override
    protected void doSkillUpdate(float tpf) {
        // ignore
    }

    @Override
    protected void doSkillEnd() {
        super.doSkillEnd();
        // 不要放在cleanup中移除角色,因为这可能会在场景清理(场景cleanup)的时候冲突，
        // 可能造成无限递归异常(StackOverflow)
        if (remove) {
            actor.removeFromScene();
        }
    }
    
}
