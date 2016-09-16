/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.ActorService;

/**
 * 执行角色受伤技能.
 * @author huliqing
 */
public class HurtSkill extends AbstractSkill {
    private final ActorService actorService = Factory.get(ActorService.class);
    
    @Override
    public void initialize() {
        super.initialize();
        
        if (data.getAnimation() == null) {
            actorService.reset(actor);
        }
    }

    @Override
    protected void doUpdateLogic(float tpf) {
        // ignore
    }
    
}
