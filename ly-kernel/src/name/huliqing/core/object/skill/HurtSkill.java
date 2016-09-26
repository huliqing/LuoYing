/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ChannelModule;

/**
 * 执行角色受伤技能.
 * @author huliqing
 */
public class HurtSkill extends AbstractSkill {
    private ChannelModule channelModule;

    @Override
    public void setActor(Actor actor) {
        super.setActor(actor); 
        channelModule = actor.getModule(ChannelModule.class);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        if (animation == null) {
            channelModule.reset();
        }
    }

    @Override
    protected void doUpdateLogic(float tpf) {
        // ignore
    }
    
}
