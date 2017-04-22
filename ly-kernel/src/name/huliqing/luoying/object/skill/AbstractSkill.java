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

import com.jme3.math.FastMath;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.data.AttributeUse;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.MagicData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.manager.RandomManager;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actoranim.ActorAnim;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.define.WeaponTypeDefine;
import name.huliqing.luoying.object.el.LNumberEl;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.impl.EffectEntity;
import name.huliqing.luoying.object.magic.Magic;
import name.huliqing.luoying.object.module.SkinModule;
import name.huliqing.luoying.object.sound.SoundManager;
import name.huliqing.luoying.utils.ConvertUtils;
 
/**
 * 技能的基本实现类，技能由技能模块控制(SkillModule)和执行
 * @author huliqing
 */
public abstract class AbstractSkill implements Skill {
    private static final Logger LOG = Logger.getLogger(AbstractSkill.class.getName());
    
    private final ElService elService = Factory.get(ElService.class);
    private final DefineService defineService = Factory.get(DefineService.class);
    
    private SkinModule skinModule;
    
    protected SkillData data;

    /** 技能的优先级,优先级高的可以打断优先级低的技能 */
    protected int prior;
    
    /** 技能类型*/
    protected long types;
    
    /** 例外的，在排除优先级比较的前提下，如果一个技能可以覆盖另一个技能，则不需要比较优先级。*/
    protected long overlapTypes;
    
    /** 例外的，在排除优先级比较的前提下，如果一个技能可以打断另一个技能，则不需要比较优先级。*/
    protected long interruptTypes;
    
    /** 武器类型限制：数组中的每一个值代表一组武器限制类型。 */
    protected long[] weaponStateLimit;
    
    /** 技能的等级公式，该公式与技能等级（level）可以计算出当前技能的一个等级值。*/
    protected LNumberEl levelEl;
    
    /** 技能升级等级公式，该公式中的每一个等级值表示每次技能升级时需要的sp数(skill points)*/
    protected LNumberEl levelUpEl;
    
    /** checkEl用于检查角色是否可以使用这个技能 */
    protected SBooleanEl checkEl;
    
    /**
     * 绑定一个角色属性，这个属性将直接控制技能的执行速度，默认技能的执行速度为1。
     */
    protected String bindSpeedAttribute;
    
    /** 绑定一个防止技能被中断的“概率”属性。*/
    protected String bindInterruptRateAttribute;
    
    /**  技能执行速度 */
    protected float speed = 1.0f;

    // ---- 内部参数 ----
    
    // 当前执行技能的角色
    protected Entity actor;
    
    /**  当前技能已经运行的时间 */
    protected float time;
    
    /** 技能是否已经开始运行 */
    protected boolean initialized;
    
     // remove20170422
//    // 优化性能,这样就不需要在update中不停的去计算trueUseTime
//    // 只在start的时候计算一次，在update中不再去计算
//    protected float trueUseTime;
    
    // remove20170422
//    // 优化性能，因为特效的速度需要和技能的速度同步，所以在执行特效的时候也需要
//    // 同步设置速度,这个trueSpeed用于缓存技能的实际速度，在每次初始化的时候计算一次
//    // 在执行过程中就不再需要计算。
//    protected float trueSpeed;
    
    @Override
    public void setData(SkillData data) {
        this.data = data;
        
        // 格式： weaponStateLimit="rightSword,leftSword|rightSword|..." 
        String[] wslArr = data.getAsArray("weaponStateLimit");
        if (wslArr != null && wslArr.length > 0) {
            weaponStateLimit = new long[wslArr.length];
            WeaponTypeDefine wtDefine = defineService.getWeaponTypeDefine();
            for (int i = 0; i < wslArr.length; i++) {
                weaponStateLimit[i] = wtDefine.convert(wslArr[i].split("\\|"));
            }
        }
        
        time = data.getAsFloat("time", time);
        prior = data.getAsInteger("prior", prior);
        types = defineService.getSkillTypeDefine().convert(data.getTypes());
        overlapTypes = defineService.getSkillTypeDefine().convert(data.getAsArray("overlapTypes"));
        interruptTypes = defineService.getSkillTypeDefine().convert(data.getAsArray("interruptTypes"));
        
        bindSpeedAttribute = data.getAsString("bindSpeedAttribute");
        bindInterruptRateAttribute = data.getAsString("bindInterruptRateAttribute");
        speed = data.getAsFloat("speed", speed);

        // 等级公式
        String levelElStr = data.getAsString("levelEl");
        if (levelElStr != null) {
            levelEl = elService.createLNumberEl(levelElStr);
        }
        String levelUpElStr = data.getAsString("levelUpEl");
        if (levelUpElStr != null) {
            levelUpEl = elService.createLNumberEl(levelUpElStr);
        }
        // el检查器
        String tempCheckEl = data.getAsString("checkEl");
        if (tempCheckEl != null) {
            checkEl = elService.createSBooleanEl(tempCheckEl);
        }
    }

    @Override
    public SkillData getData() {
        return data;
    }
    
    @Override
    public void updateDatas() {
        data.setAttribute("time", time);
    }
    
    @Override
    public void setActor(Entity actor) {
        this.actor = actor;
        skinModule = actor.getModule(SkinModule.class);
        if (checkEl != null) {
            checkEl.setSource(actor.getAttributeManager());
        }
    }
    
    @Override
    public Entity getActor() {
        return actor;
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        
        // --技能消耗
        List<AttributeUse> uas = data.getUseAttributes();
        if (uas != null) {
            for (AttributeUse ua : uas) {
                addNumberAttributeValue(actor, ua.getAttribute(), -ua.getAmount());
            }
        }

        // --记录技能执行时间及增加技能点数
        data.setLastPlayTime(LuoYing.getGameTime());
        data.setPlayCount(data.getPlayCount() + 1);
        
        // 检查技能等级提升
        levelUpCheck();
    }
    
    // 检查并升级技能
    private void levelUpCheck() {
        if (data.getLevel() >= data.getMaxLevel()) 
            return;
        if (levelUpEl == null)
            return;
        int levelPoints = levelUpEl.setLevel(data.getLevel() + 1).getValue().intValue();
        if (data.getPlayCount() >= levelPoints) {
            data.setLevel(data.getLevel() + 1);
            data.setPlayCount(data.getPlayCount() - levelPoints);
            levelUpCheck();
        }
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void update(float tpf) {
        if (!initialized) {
            return;
        }
        
        // 检查是否结束
        time += tpf;
        
        // 6.update logic
        doSkillUpdate(tpf);
    }
    
    @Override
    public void cleanup() {
        time = 0;
        initialized = false;
    }

    @Override
    public int getPrior() {
        return prior;
    }

    @Override
    public long getTypes() {
        return types;
    }

    @Override
    public long getOverlapTypes() {
        return overlapTypes;
    }

    @Override
    public long getInterruptTypes() {
        return interruptTypes;
    }

    @Override
    public boolean isCooldown() {
        return LuoYing.getGameTime() - data.getLastPlayTime() < data.getCooldown() * 1000;
    }

    /**
     * 获取技能的等级值,由等级公式计算出来，这个方法返回的是当前等级下技能的等级值，
     * 如果技能没有配置等级公式 ，则该方法将返回null.
     * @return 
     */
    protected Number getLevelValue() {
        if (levelEl == null) {
            return null;
        }
        return levelEl.setLevel(data.getLevel()).getValue();
    }
    
    @Override
    public int checkState() {
        // 武器必须取出
        if (weaponStateLimit != null) {
            if (skinModule == null || !skinModule.isWeaponTakeOn()) {
                return StateCode.SKILL_USE_FAILURE_WEAPON_NEED_TAKE_ON;
            }            
        }
        
        // 冷却中
        if (isCooldown()) {
            return StateCode.SKILL_USE_FAILURE_COOLDOWN;
        }
        
        // 武器状态检查,有一些技能需要拿特定的武器才能执行。
        if (!isPlayableByWeapon()) {
            return StateCode.SKILL_USE_FAILURE_WEAPON_NOT_ALLOW;
        }
        
        // 技能需要消耗一定的属性值，而角色属性值不足
        if (!isPlayableByAttributeLimit()) {
            return StateCode.SKILL_USE_FAILURE_ATTRIBUTE_NOT_ENOUGH;
        }
        
        // EL检查器，例如：属性值不足等
        if (!isPlayableByElCheck()) {
            return StateCode.SKILL_USE_FAILURE_ELCHECK;
        }
        
        return StateCode.SKILL_USE_OK;
    }
    
    @Override
    public boolean canInterruptBySkill(Skill newSkill) {
        if (bindInterruptRateAttribute == null) {
            return true;
        }
        
        NumberAttribute nAttr = actor.getAttributeManager().getAttribute(bindInterruptRateAttribute, NumberAttribute.class);
        float resistRate = nAttr != null ? nAttr.floatValue() : 0;
        if (resistRate <=0) { // 抵抗率为0
            return true;
        }
        if (resistRate >= 1.0f) { // 抵抗率为100%
            return false;
        }
        
        float randomValue = RandomManager.getNextValue();
        return randomValue > resistRate;
    }
    
    @Override
    public boolean isPlayableByWeapon() {
        if (weaponStateLimit == null)
            return true;
        
        long weaponState = skinModule.getWeaponState();
        for (long ws : weaponStateLimit) {
            if (ws == weaponState) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isPlayableByAttributeLimit() {
        List<AttributeUse> uas = data.getUseAttributes();
        if (uas != null) {
            for (AttributeUse ua : uas) {
                if (getNumberAttributeValue(actor, ua.getAttribute(), 0) < ua.getAmount()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean isPlayableByElCheck() {
        boolean result = checkEl == null || checkEl.getValue();
//        if (Config.debug) {
//            String el = checkEl != null ? checkEl.getData().getId() : null;
//            String expression = checkEl != null ? checkEl.getExpression() : null;
//            LOG.log(Level.INFO, "Playable check by el, result={0}, el={1}, expression={2}, skillId={3}, entity={4}"
//                    , new Object[] {result, el, expression, data.getId(), actor.getData().getId()});
//        }
        return result;
    }
    
    /**
     * 获取技能的执行速度,技能的执行速度受技能自身速度设置以及角色属性的影响（如果绑定了角色属性）, 
     * 这个方法不会返回小于或等于0的速度 。
     * @return 返回的最小值为0.0001f，为避免除0错误，速度不能小于或等于0
     */
    public float getSpeed() {
        float tempSpeed = speed;
        if (bindSpeedAttribute != null) {
            tempSpeed *= getNumberAttributeValue(actor, bindSpeedAttribute, 1.0f);
        }
        if (tempSpeed <= 0) {
            tempSpeed = 0.0001f;
        }
        return tempSpeed;
    }
    
    /**
     * 获取指定Entity的Number属性的值，如果不存在指定的属性值，则返回defValue
     * @param entity
     * @param attributeName
     * @param defValue
     * @return 
     */
    protected final float getNumberAttributeValue(Entity entity, String attributeName, float defValue) {
         NumberAttribute nattr = entity.getAttributeManager().getAttribute(attributeName, NumberAttribute.class);
         return nattr != null ? nattr.floatValue() : defValue;
    }
    
    /**
     * 给指定Entity的Number属性添加值，如果不存在指定的属性值，则什么也不做。
     * @param entity
     * @param attributeName
     * @param value 
     */
    protected final void addNumberAttributeValue(Entity entity, String attributeName, float value) {
        NumberAttribute nattr = entity.getAttributeManager().getAttribute(attributeName, NumberAttribute.class);
        if (nattr != null) {
            nattr.add(value);
        }
    }
    
    /**
     * 实现技能逻辑
     * @param tpf 
     */
    protected abstract void doSkillUpdate(float tpf);
    
}
