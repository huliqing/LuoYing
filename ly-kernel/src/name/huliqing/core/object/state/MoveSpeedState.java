/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import com.jme3.app.Application;
import com.jme3.scene.Spatial;
import name.huliqing.core.Factory;
import name.huliqing.core.data.StateData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.service.EffectService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.effect.Effect;
import name.huliqing.core.object.effect.EffectManager;
import name.huliqing.core.object.skill.Skill;

/**
 * 速度效果，提升角色或降低角色的速度
 * @author huliqing
 */
public class MoveSpeedState extends AttributeState {
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final EffectService effectService = Factory.get(EffectService.class);
    
    private String moveEffectId;
    
    // ---- inner
    private Effect moveEffect;

    @Override
    public void setData(StateData data) {
        super.setData(data);
        moveEffectId = data.getAttribute("moveEffect");
    }

    @Override
    public void initialize(Application app) {
        super.initialize(app);
        
        // 如果角色当前正在执行“跑路”技能，则强制重新执行，以适应速度的变化。
        Skill running = skillService.getPlayingSkill(actor, SkillType.run);
        if (running != null) {
            skillService.playSkill(actor, running.getSkillData().getId(), true);
        }
        
        checkEffectTrace();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        checkEffectTrace();
    }
    
    @Override
    public void cleanup() {
        // 如果角色当前正在执行“跑路”技能，则强制重新执行，以适应速度的变化。
        Skill running = skillService.getPlayingSkill(actor, SkillType.run);
        if (running != null) {
            skillService.playSkill(actor, running.getSkillData().getId(), true);
        }
        
        // 移除效果
        if (moveEffect != null) {
            EffectManager.getInstance().removeEffect(moveEffect);
        }
        super.cleanup();
    }
    
    private void checkEffectTrace() {
        if (moveEffectId != null) {
            if (skillService.isRunning(actor)) {
                showMoveEffect();
            } else {
                hideMoveEffect();
            }
        }
    }
    
    private void showMoveEffect() {
        if (moveEffect == null) {
            moveEffect = effectService.loadEffect(moveEffectId);
            moveEffect.setTraceObject(actor.getModel());
            EffectManager.getInstance().addEffect(moveEffect);
        }
        if (moveEffect != null) {
            if (moveEffect.isEnd()) {
                moveEffect.initialize();
            }
            moveEffect.setCullHint(Spatial.CullHint.Never);
        }
    }
    
    private void hideMoveEffect() {
        if (moveEffect != null) {
            moveEffect.setCullHint(Spatial.CullHint.Always);
        }
    }
    
    
}
