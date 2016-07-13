/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.state;

import com.jme3.app.Application;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.StateData;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.service.AttributeService;
import name.huliqing.fighter.game.service.EffectService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.object.effect.Effect;
import name.huliqing.fighter.object.skill.Skill;

/**
 * 速度效果，提升角色或降低角色的速度
 * @author huliqing
 */
public class MoveSpeedState extends AttributeState {
    private final PlayService playService = Factory.get(PlayService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final EffectService effectService = Factory.get(EffectService.class);
    
    private String moveEffect;
    
    // ---- inner
    private Effect tempMoveEffect;

    @Override
    public void setData(StateData data) {
        super.setData(data);
        moveEffect = data.getAttribute("moveEffect");
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
        if (tempMoveEffect != null && !tempMoveEffect.isEnd()) {
            tempMoveEffect.jumpToEnd();
            tempMoveEffect = null;
        }
        super.cleanup();
    }
    
    private void checkEffectTrace() {
        if (moveEffect != null) {
            if (actor.isRunning()) {
                startMoveEffect();
            } else {
                endMoveEffect();
            }
        }
    }
    
    private void startMoveEffect() {
        if (tempMoveEffect == null) {
            tempMoveEffect = effectService.loadEffect(moveEffect);
            tempMoveEffect.setTraceObject(actor.getModel());
            playService.addEffect(tempMoveEffect);
        }
    }
    
    private void endMoveEffect() {
        if (tempMoveEffect != null && !tempMoveEffect.isEnd()) {
            tempMoveEffect.jumpToEnd();
            tempMoveEffect = null;
        }
    }
    
    
}
