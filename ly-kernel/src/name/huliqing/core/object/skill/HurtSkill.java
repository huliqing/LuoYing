/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import name.huliqing.core.Factory;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.mvc.service.ActorService;

/**
 * 执行角色受伤技能.
 * @author huliqing
 * @param <T>
 */
public class HurtSkill<T extends SkillData> extends AbstractSkill<T> {
    private final ActorService actorService = Factory.get(ActorService.class);
    
    @Override
    public void init() {
        super.init();
        
        if (data.getAnimation() == null) {
            actorService.reset(actor);
        }
    }

    @Override
    protected void doUpdateLogic(float tpf) {
        // ignore
    }
    
}
