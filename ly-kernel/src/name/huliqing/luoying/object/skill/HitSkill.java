/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.skill;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.SkillConstants;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.StateNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.el.STBooleanEl;
import name.huliqing.luoying.object.el.STNumberEl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.magic.Magic;
import name.huliqing.luoying.utils.ConvertUtils;

/**
 * Hit技能，可与目标角色进行交互的技能。例如攻击技能，BUFF技能，射击技能。
 * @author huliqing
 */
public abstract class HitSkill extends AbstractSkill {
    private static final Logger LOG = Logger.getLogger(HitSkill.class.getName());
    
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ElService elService = Factory.get(ElService.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    // hitCheckEl用于检查当前角色的技能是否作为作用(hit)于一个目标角色。
    protected STBooleanEl hitCheckEl;
    // 指定HIT目标的哪一个属性
    protected String hitAttribute;
    // HIT的属性值，可正，可负。
    protected float hitValue;
    // Hit公式，这个公式用于计算当前角色对目标角色可以产生的属性影响值，不包含hitValue.
    // hitEl计算出的值会和hitValue一起加成，作为最终的hit结果值。
    protected STNumberEl hitEl;
    // hit的距离限制，注：hitDistance和hitAngle决定了hit的面积,举例，
    // 如果 hitDistance = 3, hitAngle=360， 则在角色周围3码内的目标都在这个技能范围内
    protected float hitDistance = 3;
    // Hit的角度范围限制
    protected float hitAngle = 30;
    // HIT后可添加到目标角色上时的状态列表
    protected List<SkillStateWrap> hitStates;
    // HIT后可添加到目标角色上的魔法
    protected String[] hitMagics;
    // 绑定“目标”属性，这个属性的值用于查找当前角色的目标对象。
    protected String bindTargetAttribute;
    
    // ---- inner
    private TargetsComparator sorter;
    // 缓存hitDistance,优化inHitDistance
    private float hitDistanceSquared;
    // 角色的目标对象属性
    private NumberAttribute targetAttribute;
    
    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        
        hitCheckEl = elService.createSTBooleanEl(data.getAsString("hitCheckEl", "#{true}")); // #{true} -> 除非设置了hitCheckEl否则默认任何目标都可以hit
        hitAttribute = data.getAsString("hitAttribute");
        hitValue = data.getAsFloat("hitValue", 0);
        hitEl = elService.createSTNumberEl(data.getAsString("hitEl", "#{0}"));
        hitDistance = data.getAsFloat("hitDistance", hitDistance);
        hitDistanceSquared = hitDistance * hitDistance;
        hitAngle = data.getAsFloat("hitAngle", hitAngle);
        
        // 状态和机率，　格式："stateId1|factor, stateId2|factor"
        String[] tempHitStates = data.getAsArray("hitStates");
        if (tempHitStates != null && tempHitStates.length > 0) {
            hitStates = new ArrayList<SkillStateWrap>(tempHitStates.length);
            String[] tempArr;
            for (String ts : tempHitStates) {
                if (ts.trim().equals("")) {
                    continue;
                }
                tempArr = ts.split("\\|");
                SkillStateWrap sw = new SkillStateWrap();
                sw.stateId = tempArr[0];
                if (tempArr.length >= 2) {
                    sw.factor = ConvertUtils.toFloat(tempArr[1], 1.0f);
                } else {
                    sw.factor = 1.0f;
                }
                hitStates.add(sw);
            }
        }
        hitMagics = data.getAsArray("hitMagics");
        bindTargetAttribute = data.getAsString("bindTargetAttribute");
    }

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor); 
        hitCheckEl.setSource(actor.getAttributeManager());
        targetAttribute = actor.getAttributeManager().getAttribute(bindTargetAttribute, NumberAttribute.class);
        if (targetAttribute == null) {
            LOG.log(Level.WARNING, "Could not found targetAttribute. HitSkill need to set bindTargetAttribute, "
                    + " Entity={0}, Skill={1}, bindTargetAttribute={2}", new Object[] {actor.getData().getId(), data.getId(), bindTargetAttribute});
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        
        // 注：这里target必须不能是自己(actor),否则在faceTo时会导致在执行animation
        // 的时候模型的头发和武器错位,即不能faceTo自己，所以target != actor的判断必须的。
        Entity target = getTarget();
        if (target != null && target != actor) {
            actorService.setLookAt(actor, actorService.getLocation(target));
        }
        hitCheckEl.setSource(actor.getAttributeManager());
    }
    
    /**
     * 获取角色的当前目标对象, 如果不存在，或者没有指定任何targetAttribute则该方法返回null.
     * @return 
     */
    protected Entity getTarget() {
        if (targetAttribute == null) {
            return null;
        }
        return actor.getScene().getEntity(targetAttribute.getValue().longValue());
    }
    
    @Override
    protected float getSkillValue() {
        float lv = getLevelValue();
        if (lv != -1) {
            return lv * hitValue;
        }
        return hitValue;
    }
    
    /**
     * 获取技能范围内及限制角度内的所有角色，但不包含当前角色自身.
     * @param store
     * @param sort 是否进行排序（从离当前角色近到远进行排序）
     */
    protected void getCanHitActors(List<Entity> store, boolean sort) {
        // 查找角度限制范围内的敌人。
        actorService.findNearestActors(actor, hitDistance, hitAngle, store);

        // 移除不能作为目标的角色,注：mainTarget要单独处理
        Entity mainTarget = getTarget();
        Iterator<Entity> it = store.iterator();
        Entity temp;
        while (it.hasNext()) {
            temp = it.next();
            hitCheckEl.setTarget(temp.getAttributeManager());
            if (temp == mainTarget || !hitCheckEl.getValue()) {
                it.remove();
            }
        }
        
        // 进行排序，从离当前角色“近到远”
        if (sort) {
            if (sorter == null) {
                sorter = new TargetsComparator();
            }
            Collections.sort(store, sorter);
        }
        
        // 添加角色自身
        hitCheckEl.setTarget(actor.getAttributeManager());
        if (hitCheckEl.getValue()) {
            store.add(0, actor);
        }
        
        // 添加主目标（注意避免重覆）
        // 主目标是放在最优先，然后是自身角色
        hitCheckEl.setTarget(mainTarget.getAttributeManager());
        if (mainTarget != actor && hitCheckEl.getValue()) {
            store.add(0, mainTarget);
        }
    }
    
    /**
     * HIT目标角色。由子类调用
     * @param target 
     */
    protected void applyHit(Entity target) {
        // 角色刚好已经脱离场景，则什么也不做。
        if (target.getScene() == null) {
            return;
        }
        hitCheckEl.setTarget(target.getAttributeManager());
        if (hitCheckEl.getValue()) {
            // 状态和魔法先添加，然后再添加applyHit.
            // 1.因为applyHit后角色可能死亡，这个时候如果再添加状态，可能会导致角色
            // 重新做出一些奇怪动作，因为状态和魔法可能会让角色重新执行一些技能,如reset,
            // 而本应该是“死亡”动作。
            // 2.可能一些状态如“冰冻”要冻住目标角色，如果先执行applyHit可能会
            // 一直冻住“受伤”时的角色动画，无法冻住其它角色动画。
            applyStates(target, hitStates);
            applyMagics(target, hitMagics);
            if (hitAttribute != null) {
                applyHit(actor, target, getSkillValue(), hitEl, hitAttribute);
            }
        }
    }
    
    /**
     * 给角色添加魔法
     * @param target
     * @param hitMagics 
     */
    private void applyMagics(Entity target, String[] hitMagics) {
        if (hitMagics == null)
            return;
        for (String mId : hitMagics) {
            Magic magic = Loader.load(mId);
            magic.setSource(actor);
            magic.getSpatial().setLocalTranslation(target.getSpatial().getWorldTranslation());
            target.getScene().addEntity(magic);
            
            // remove201610xx
//            md.setTargetActor(target.getData().getUniqueId());
//            md.setTraceActor(target.getData().getUniqueId());
        }
    }
    
    private void applyStates(Entity target, List<SkillStateWrap> stateWraps) {
        if (stateWraps == null)
            return;
        
        // 在角色死亡的时候仍可以添加状态，以避免角色刚好被射死后却没有看到状态特效.
        // 如：当寒冰箭刚好射击"死"敌人后应允许添加状态，否则会看不到冰冻效果
        // 因为“冰冻效果”是在状态上的，只有冰冻状态运行的时候才能看到。
        for (SkillStateWrap sw : stateWraps) {
            if (sw.factor >= FastMath.nextRandomFloat()) {
                // 状态存在机率影响，为同步服务端与客户端状态所以统以服务端为准。
                stateNetwork.addState(target, sw.stateId, actor);
            }
        }
    }
    
    /**
     * @param attacker 攻击者
     * @param target 被攻击者
     * @param hitValue 技能值
     * @param hitEl 技能计算公式
     * @param attribute 指定攻击的是哪一个属性
     */
    private void applyHit(Entity attacker, Entity target, float hitValue, STNumberEl hitEl, String attribute) {
        // remove20161031
//        if (hitEl == null) {
//            return;
//        }
//        float finalValue = 0;
//        HitEl de = elService.getHitEl(hitEl);
//        if (de != null) {
//            finalValue = de.getValue(attacker, skillValue, target);
//        }
//        HitUtils.getInstance().applyHit(attacker, target, hitAttribute, finalValue);

        hitEl.setSource(attacker.getAttributeManager());
        hitEl.setTarget(target.getAttributeManager());
        float finalHitValue = hitValue + hitEl.getValue().floatValue();
        entityNetwork.hitNumberAttribute(target, attribute, finalHitValue, attacker);
    }

    @Override
    public int checkState() {
        if (actor == null) {
            throw new NullPointerException("Actor could not be null! skillId=" + data.getId());
        }
        
        Entity target = getTarget();
        if (target == null) {
            return SkillConstants.STATE_TARGET_NOT_FOUND;
        }
        
        if (!isInHitDistance(target))
            return SkillConstants.STATE_TARGET_NOT_IN_RANGE;
        
        hitCheckEl.setTarget(target.getAttributeManager());
        if (!hitCheckEl.getValue())
            return SkillConstants.STATE_TARGET_UNSUITABLE_BY_ELCHECK;
        
        return super.checkState();
    }
    
    /**
     * 判断目标是否在技能的hit距离之内
     * @param target
     * @return 
     */
    public boolean isInHitDistance(Entity target) {
        if (target == null) {
            return false;
        }
        return actor.getSpatial().getWorldTranslation().distanceSquared(target.getSpatial().getWorldTranslation()) 
                <= hitDistanceSquared
                || actor.getSpatial().getWorldBound().intersects(target.getSpatial().getWorldBound());
    }
    
    /**
     * 判断目标是否在技能的hit角度之内
     * @param target
     * @return 
     */
    public boolean isInHitAngle(Entity target) {
        // 如果在技能的攻击视角之外，则视为false(限制distance > 1是避免当距离太近时角度判断不正确。)
        return actorService.getViewAngle(actor, target.getSpatial().getWorldTranslation()) * 2 < hitAngle;
    }
    
    /**
     * 用于来对目标进行排序，按距离当前角色从近到远
     */
    private class TargetsComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity o1, Entity o2) {
            Vector3f selfPos = actorService.getLocation(actor);
            float dis1 = o1.getSpatial().getWorldTranslation().distanceSquared(selfPos);
            float dis2 = o2.getSpatial().getWorldTranslation().distanceSquared(selfPos);
            if (dis1 < dis2) {
                return -1;
            } else if (dis1 > dis2) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
