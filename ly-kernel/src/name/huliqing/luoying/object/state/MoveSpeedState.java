/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
        skillModule = entity.getModule(SkillModule.class);
        
        // 查找"run"技能
        List<Skill> runSkills = skillModule.getSkillRun(null);
        if (runSkills != null && !runSkills.isEmpty()) {
            runSkill = runSkills.get(0);
        }
        
        // 如果角色当前正在执行“跑路”技能，则强制重新执行，以适应速度的变化。
        if (skillModule.isRunning() && runSkill != null) {
            skillModule.playSkill(runSkill, false);
        }
        
        if (moveEffect == null && moveEffectId != null) {
            moveEffect = Loader.load(moveEffectId);
            moveEffect.setTraceObject(entity.getSpatial());
            moveEffect.initialize();
            if (entity.getScene() != null) {
                entity.getScene().getRoot().attachChild(moveEffect);
            }
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
                skillModule.playSkill(runSkill, false);
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
            entity.getScene().getRoot().attachChild(moveEffect);
        }
        moveEffect.setCullHint(Spatial.CullHint.Never);
    }
    
    private void hideMoveEffect() {
        moveEffect.setCullHint(Spatial.CullHint.Always);
    }
    
    
}
