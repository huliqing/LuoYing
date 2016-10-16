/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.skill;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ChannelModule;

/**
 * 执行死亡技能,简单的死亡效果
 * @author huliqing
 */
public class DeadSkill extends AbstractSkill {
    private final PlayService playService = Factory.get(PlayService.class);
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
        channelModule = actor.getEntityModule().getModule(ChannelModule.class);
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
    protected void doUpdateLogic(float tpf) {
        // ignore
    }

    @Override
    public void cleanup() {
        if (remove) {
            
//            playService.removeObject(actor.getSpatial());

            actor.removeFromScene();
        }
        super.cleanup(); 
    }

    
}
