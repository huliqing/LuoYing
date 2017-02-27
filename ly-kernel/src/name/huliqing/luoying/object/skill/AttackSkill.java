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
package name.huliqing.luoying.object.skill;

import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.luoying.object.module.SkinModule;
import name.huliqing.luoying.object.skin.Skin;
import name.huliqing.luoying.object.skin.Weapon;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 * 普通攻击技能,会根据damagePoint计算任害值等.主要用于近战类攻击
 * @author huliqing
 */
public class AttackSkill extends HitSkill {
    private final DefineService defineService = Factory.get(DefineService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private ActorModule actorModule;
    private SkinModule skinModule;
    
    // 是否允许攻击到多个敌人，默认情况下只能攻击到当前的目标敌人。
    // 如果打开该功能，则在攻击范围内的敌人都可能被攻击到。比如挥剑
    // 的时候可能同时击中多个敌人。默认值false
    protected boolean multHit;
    protected float[] checkPoint;
    // 技能是否为可防守的
    protected boolean defendable = true;
    // 指定防守技能的标记，格式 : "skillTag1,skillTag2,...", 这表示当这个技能在执行时，如果目标角色正在执行属
    // 于这些技能标记的技能时，目标可以视为“正在防守”
    private long defendSkillTags;
    
    // 攻击被防守时的碰撞位置，主要用于产生碰撞声音，碰撞特效的位置。
    // 相对于当前攻击者的本地坐标偏移。
    protected Vector3f collisionOffset = new Vector3f(0, 2, 2.5f);
    
    // ---- 内部参数
    protected final PointChecker pointChecker = new PointChecker();
    // 实际的攻击技能检测点，这个会受cutTime的影响，如果cutTime都为0,则该参数
    // 应该完全与checkPoint一致。
    protected float[] trueCheckPoint;
    
    protected List<Actor> tempStore = new ArrayList<Actor>();

    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        multHit = data.getAsBoolean("multHit", multHit);
        checkPoint = data.getAsFloatArray("checkPoint");
        defendable = data.getAsBoolean("defendable", defendable);
        defendSkillTags = skillService.convertSkillTypes(data.getAsArray("defendSkillTags"));
        collisionOffset = data.getAsVector3f("collisionOffset", collisionOffset);
    }

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor);
        actorModule = actor.getModuleManager().getModule(ActorModule.class);
        skinModule = actor.getModuleManager().getModule(SkinModule.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        
        // 计算实际的技能检测点，主要受cutTime影响，要按比例缩放
        if (checkPoint != null) {
            if (trueCheckPoint == null) {
                trueCheckPoint = new float[checkPoint.length];
            }
            // 重新计算时间插值点，这些插值点受cut影响
            for (int i = 0; i < checkPoint.length; i++) {
                trueCheckPoint[i] = fixTimePointByCutTime(checkPoint[i]);
            } 
            pointChecker.setCheckPoint(trueCheckPoint);
        }
        pointChecker.setMaxTime(trueUseTime);
        
        // 将标记已经处理的攻击判定时间点清0
        pointChecker.rewind();
    }

    @Override
    protected void doSkillUpdate(float tpf) {
        while (pointChecker.nextPoint(time) != -1) {
            processCheckPoint(pointChecker.getIndex());
        }
    }
    
    /**
     * 伤害检测
     * @param checkPointIndex
     */
    protected void processCheckPoint(int checkPointIndex) {
        boolean skillDefendable = isDefendable();
        // 如果允许多重目标攻击
        if (multHit) {
            tempStore.clear();
            List<Actor> targets = actor.getScene().getEntities(Actor.class, actor.getSpatial().getWorldTranslation(), this.hitDistance, tempStore);
            for (Entity target : targets) {
                // 只有在技能作用范围内　及　在攻击角度内才视为可能被击中
                if (!isInHitDistance(target) 
                        || !isInHitAngle(target)
                        || !hitCheckEl.setTarget(target.getAttributeManager()).getValue()
                        ) {
                    continue;
                }
                // 防守成功(角色正在防守,并且必须是正面防守)
                if (skillDefendable 
                        && skillService.isPlayingSkill(target, defendSkillTags)
                        && actorService.getViewAngle(target, actor.getSpatial().getWorldTranslation()) < 90) {
                    doDefendResult(target);
                } else {
                    doHitResult(target);
                }
            }
        } else {
            Entity target = getTarget();
            
            // 只有在技能作用范围内　及　在攻击角度内才视为可能被击中
            if (!isInHitDistance(target) 
                    || !isInHitAngle(target)
                    || !hitCheckEl.setTarget(target.getAttributeManager()).getValue()
                    ) {
                return;
            }
            // 防守成功(角色正在防守,并且必须是正面防守)
            if (skillDefendable 
                    && skillService.isPlayingSkill(target, defendSkillTags)
                    && actorService.getViewAngle(target, actor.getSpatial().getWorldTranslation()) < 90) {
                doDefendResult(target);
            } else {
                doHitResult(target);
            }
        }
    }
    
    // 获取角色的武器质地
    private int getWeaponMat(SkinModule sm) {
        if (sm == null) {
            return -1;
        }
        List<Skin> usingSkins = sm.getUsingSkins();
        if (usingSkins != null) {
            for (Skin s : usingSkins) {
                if (s instanceof Weapon) {
                    if ( s.getData().getMat() != -1) {
                        return s.getData().getMat();
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * 处理防守的声效，特效
     * @param target 
     */
    protected void doDefendResult(Entity target) {
        int mat1 = getWeaponMat(skinModule);
        int mat2 = getWeaponMat(target.getModuleManager().getModule(SkinModule.class));
        if (mat1 < 0 || mat2 < 0) {
            return;
        }
        
        // 播放碰撞声效
        String soundId = defineService.getMatDefine().getCollisionSound(mat1, mat2);
        if (soundId != null) {
            SoundManager.getInstance().playSound(soundId, actor.getSpatial().getWorldTranslation());
        }
        String effectId = defineService.getMatDefine().getCollisionEffect(mat1, mat2);
        if (effectId != null) {
            TempVars tv = TempVars.get();
            Vector3f collisionPos = tv.vect1.set(collisionOffset);
            tv.quat1.lookAt(actorModule.getViewDirection(), Vector3f.UNIT_Y);
            tv.quat1.mult(collisionPos, collisionPos);
            collisionPos.addLocal(actor.getSpatial().getWorldTranslation());
            
            Effect effect = Loader.load(effectId);
            effect.setLocalTranslation(collisionPos);
            effect.getLocalRotation().lookAt(actorModule.getViewDirection(), Vector3f.UNIT_Y);
            effect.initialize();
            actor.getScene().getRoot().attachChild(effect);
            tv.release();
        }
    }
    
    protected void doHitResult(Entity target) {
        // 播放击中声效
        int weaponMat = getWeaponMat(skinModule);
        int targetMat = target.getData().getMat();
        String collisionSound = defineService.getMatDefine().getCollisionSound(weaponMat, targetMat);
        if (collisionSound != null) {
            SoundManager.getInstance().playSound(collisionSound, actor.getSpatial().getWorldTranslation());
        }
        
        applyHit(target);
    }
    
    public boolean isDefendable() {
        return defendable;
    }
    
}
