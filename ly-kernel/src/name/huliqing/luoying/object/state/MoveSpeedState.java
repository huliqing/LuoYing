/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;

/**
 * 速度效果，提升角色或降低角色的速度
 * @author huliqing
 */
public class MoveSpeedState extends AttributeState {
    private SkillModule skillModule;
    
    private String moveEffectId;
    
    // ---- inner
    private Effect moveEffect;
    private Skill runSkill;
    
    @Override
    public void setData(StateData data) {
        super.setData(data);
        moveEffectId = data.getAsString("moveEffect");
    }

    @Override
    public void initialize() {
        super.initialize();
        skillModule = actor.getEntityModule().getModule(SkillModule.class);
        
        // 查找"run"技能
        List<Skill> runSkills = skillModule.getSkillRun(null);
        if (runSkills != null && !runSkills.isEmpty()) {
            runSkill = runSkills.get(0);
        }
        
        // 如果角色当前正在执行“跑路”技能，则强制重新执行，以适应速度的变化。
        if (skillModule.isRunning() && runSkill != null) {
            skillModule.playSkill(runSkill, false, skillModule.checkNotWantInterruptSkills(runSkill));
        }
        
        if (moveEffect == null && moveEffectId != null) {
            moveEffect = Loader.load(moveEffectId);
            moveEffect.setTraceEntity(actor.getEntityId());
            actor.getScene().addEntity(moveEffect);
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
        if (skillModule != null && skillModule.isRunning()) {
            if (runSkill != null) {
                skillModule.playSkill(runSkill, false, skillModule.checkNotWantInterruptSkills(runSkill));
            }
        }
        
        // 移除效果
        if (moveEffect != null) {
            moveEffect.requestEnd();
        }
        super.cleanup();
    }
    
    private void checkEffectTrace() {
        if (moveEffect != null) {
            if (skillModule.isRunning()) {
                showMoveEffect();
            } else {
                hideMoveEffect();
            }
        }
    }
    
    private void showMoveEffect() {
        if (moveEffect.isEnd()) {
            moveEffect.initialize();
//            actor.getScene().addEntity(moveEffect);
        }
        moveEffect.getSpatial().setCullHint(Spatial.CullHint.Never);
    }
    
    private void hideMoveEffect() {
        moveEffect.getSpatial().setCullHint(Spatial.CullHint.Always);
    }
    
    
}
