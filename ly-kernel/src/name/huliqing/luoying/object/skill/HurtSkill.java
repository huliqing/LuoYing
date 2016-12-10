/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.skill;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ChannelModule;

/**
 * 执行角色受伤技能.
 * @author huliqing
 */
public class HurtSkill extends AbstractSkill {
    private ChannelModule channelModule;

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor); 
        channelModule = actor.getModuleManager().getModule(ChannelModule.class);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        if (animation == null) {
            channelModule.reset();
        }
    }

    @Override
    protected void doSkillUpdate(float tpf) {
        // ignore
    }
    
}
