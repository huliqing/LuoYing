/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill.impl;

import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.manager.SoundManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkinService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.object.skill.HitSkill;
import name.huliqing.fighter.object.skill.PointChecker;

/**
 * 普通攻击技能,会根据damagePoint计算任害值等.主要用于近战类攻击
 *
 * @author huliqing
 * @param <T>
 */
public class AttackSkill<T extends SkillData> extends HitSkill<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    
    // 是否允许攻击到多个敌人，默认情况下只能攻击到当前的目标敌人。
    // 如果打开该功能，则在攻击范围内的敌人都可能被攻击到。比如挥剑
    // 的时候可能同时击中多个敌人。默认值false
    protected boolean multHit;
    protected float[] checkPoint;
    // 技能是否为可防守的
    protected boolean defendable = true;
    // 攻击被防守时的碰撞位置，主要用于产生碰撞声音，碰撞特效的位置。
    // 相对于当前攻击者的本地坐标偏移。
    protected Vector3f collisionOffset = new Vector3f(0, 2, 2.5f);
    
    // ---- 内部参数
    protected final PointChecker pointChecker = new PointChecker();
    // 实际的攻击技能检测点，这个会受cutTime的影响，如果cutTime都为0,则该参数
    // 应该完全与checkPoint一致。
    protected float[] trueCheckPoint;

    @Override
    public void initData(T data) {
        super.initData(data); 
        multHit = data.getAsBoolean("multHit", multHit);
        checkPoint = data.getAsFloatArray("checkPoint");
        defendable = data.getAsBoolean("defendable", defendable);
        collisionOffset = data.getAsVector3f("collisionOffset", collisionOffset);
    }

    @Override
    public void init() {
        super.init();
        
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
    protected void doUpdateLogic(float tpf) {
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
            List<Actor> targets = playService.findAllActor();
            for (Actor target : targets) {
                // 只有在技能作用范围内　及　在攻击角度内才视为可能被击中
                if (!isInHitDistance(target) 
                        || !isInHitAngle(target)
                        || !hitChecker.canHit(actor, target)
                        ) {
                    continue;
                }
                // 防守成功(角色正在防守,并且必须是正面防守)
                if (skillDefendable 
                        && target.isDefending() 
                        && target.getViewAngle(actor.getModel().getWorldTranslation()) < 90) {
                    doDefendResult(target);
                } else {
                    doHitResult(target);
                }
            }
        } else {
            Actor target = actorService.getTarget(actor);
            
            // 只有在技能作用范围内　及　在攻击角度内才视为可能被击中
            if (!isInHitDistance(target) 
                    || !isInHitAngle(target)
                    || !hitChecker.canHit(actor, target)
                    ) {
                return;
            }
            // 防守成功(角色正在防守,并且必须是正面防守)
            if (skillDefendable && target.isDefending() && target.getViewAngle(actor.getModel().getWorldTranslation()) < 90) {
                doDefendResult(target);
            } else {
                doHitResult(target);
            }
        }
    }
    
    /**
     * 处理防守
     * @param target 
     */
    protected void doDefendResult(Actor target) {
        TempVars tv = TempVars.get();
        Vector3f collisionPos = tv.vect1.set(collisionOffset);
        tv.quat1.lookAt(actor.getViewDirection(), Vector3f.UNIT_Y);
        tv.quat1.mult(collisionPos, collisionPos);
        collisionPos.addLocal(actor.getModel().getWorldTranslation());
        Collision.playDefend(collisionPos, actor, target, null, null);
        tv.release();
    }
    
    protected void doHitResult(Actor target) {
        // 伤害声音
        List<SkinData> weaponSkins = skinService.getCurrentWeaponSkin(actor);
        SkinData wd1 = weaponSkins.isEmpty() ? null : weaponSkins.get(0);
        
        ProtoData od1 = wd1 != null ? wd1 : actor.getData();
        SoundManager.getInstance().playCollision(od1, target.getData(), actor.getModel().getWorldTranslation());
        
        // 伤害计算
        applyHit(target);
    }
    
    public boolean isDefendable() {
        return defendable;
    }
}
