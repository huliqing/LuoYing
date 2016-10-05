/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.skill;

import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.module.ChannelModule;

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
