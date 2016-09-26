/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ChannelModule;

/**
 * 让角色“等待”的技能
 * @author huliqing
 */
public class WaitSkill extends AbstractSkill {
    private ChannelModule channelModule;

    @Override
    public void setActor(Actor actor) {
        super.setActor(actor); 
        channelModule = actor.getModule(ChannelModule.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        
        // 对于一些没有“Wait动画”的角色必须想办法让它静止下来
        if (animation == null) {
            channelModule.reset();
        }
    }

    
    
    @Override
    protected void doUpdateLogic(float tpf) {
    }

    
}
