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
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.xml.SimpleCloner;

/**
 * @author huliqing
 */
@Serializable
public class SkillData extends ObjectData {
    
    // 技能的执行时间，单位秒
    private float useTime;

    // 技能的冷却时间,单位秒
    private float cooldown;
    
    // 武器状态限制
    private long[] weaponStateLimit;
    
    // 定义当前技能需要消耗的角色的属性值
    private List<AttributeUse> useAttributes;
    
    // 技能等级
    private int level;
    
    // 技能允许的最高等级
    private int maxLevel;
    
    
    // 技能类型
    private long types;
    // 例外的，在排除优先级比较的前提下，如果一个技能可以覆盖另一个技能，则不需要比较优先级。
    private long overlapTypes;
    // 例外的，在排除优先级比较的前提下，如果一个技能可以打断另一个技能，则不需要比较优先级。
    private long interruptTypes;
    // 技能的优先级,优先级高的可以打断优先级低的技能
    private int prior;
    
    // 技能点数（技能熟练度），每次执行技能该值会递增，并经验公式来判断是否升级技能。
    private int playCount;
    
    // 最近一次使用技能的时间,用于判断技能冷却限制
    private long lastPlayTime;
    
    public String getIcon() {
        return getAsString("icon");
    }
    
    public float getUseTime() {
        return useTime;
    }

    public void setUseTime(float useTime) {
        this.useTime = useTime;
    }

    /**
     * 获取技能的冷却时间，单位秒
     * @return 
     */
    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * 获取技能的武器状态限制。
     * @return 
     */
    public long[] getWeaponStateLimit() {
        return weaponStateLimit;
    }
    
    /**
     * 设置武器状态限制, 数组中的每个值代表一种武器组类型。
     * @param weaponStateLimit 
     */
    public void setWeaponStateLimit(long[] weaponStateLimit) {
        this.weaponStateLimit = weaponStateLimit;
    }
    
    /**
     * 获取技能需要消耗的属性
     * @return 
     */
    public List<AttributeUse> getUseAttributes() {
        return useAttributes;
    }

    /**
     * 设置技能需要消息的属性
     * @param useAttributes 
     */
    public void setUseAttributes(List<AttributeUse> useAttributes) {
        this.useAttributes = useAttributes;
    }

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    /**
     * 获取技能的当前等级
     * @return 
     */
    public int getLevel() {
        return level;
    }

    /**
     * 设置技能的当前等级
     * @param level 
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 获取技能可以升级的最高等级限制
     * @return 
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * 设置技能可以升级的最高等级限制
     * @param maxLevel 
     */
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
    
    /**
     * 获取技能的执行次数
     * @return 
     */
    public int getPlayCount() {
        return playCount;
    }

    /**
     * 设置技能的执行次数
     * @param playCount 
     */
    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
        
    /**
     * 获取技能标记
     * @return 
     */
    public long getTypes() {
        return types;
    }
    
    /**
     * 设置技能标记
     * @param types 
     */
    public void setTypes(long types) {
        this.types = types;
    }
    
    /**
     * 获取当前技能类型可以覆盖的其它技能的类型，以二进制位表示，返回的整形中每个位代表一个技能类型。
     * @return 
     */
    public long getOverlapTypes() {
        return overlapTypes;
    }
    
    /**
     * 设置当前技能类型可以覆盖的其它技能类型列表，以二进制位表示，整形中
     * 每个位代表一个技能类型。
     * @param overlapTypes 技能类型
     */
    public void setOverlapTypes(long overlapTypes) {
        this.overlapTypes = overlapTypes;
    }

    /**
     * 获取当前技能类型可以打断的其它技能的类型，以二进制位表示，返回的整形中
     * 每个位代表一个技能类型。
     * @return 
     */
    public long getInterruptTypes() {
        return interruptTypes;
    }

    /**
     * 设置当前技能类型可以打断的其它技能类型列表，以二进制位表示，整形中
     * 每个位代表一个技能类型。
     * @param interruptTypes
     */
    public void setInterruptTypes(long interruptTypes) {
        this.interruptTypes = interruptTypes;
    }

    /**
     * 获取技能的优先级
     * @return 
     */
    public int getPrior() {
        return prior;
    }

    /**
     * 设置技能优先级
     * @param prior 
     */
    public void setPrior(int prior) {
        this.prior = prior;
    }
    
    @Override
    public SkillData clone() {
        SimpleCloner cloner = new SimpleCloner();
        SkillData clone = (SkillData) super.clone();
        clone.weaponStateLimit = cloner.clone(weaponStateLimit);
        clone.useAttributes = cloner.clone(useAttributes);
        return clone;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(useTime, "useTime", 1);
        oc.write(cooldown, "cooldown", 0);
        oc.write(weaponStateLimit, "weaponStateLimit", null);
        if (useAttributes != null) {
            oc.writeSavableArrayList(new ArrayList<AttributeUse>(useAttributes), "useAttributes", null);
        }
        oc.write(level, "level", 1);
        oc.write(maxLevel, "maxLevel", 1);
        oc.write(playCount, "playCount", 0);
        oc.write(lastPlayTime, "lastPlayTime", 0);
        oc.write(types, "types", 0);
        oc.write(overlapTypes, "overlapTypes", 0);
        oc.write(interruptTypes, "interruptTypes", 0);
        oc.write(prior, "prior", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        useTime = ic.readFloat("useTime", 1);
        cooldown = ic.readFloat("cooldown", 0);
        weaponStateLimit = ic.readLongArray("weaponStateLimit", null);
        useAttributes = ic.readSavableArrayList("useAttributes", null);
        level = ic.readInt("level", 1);
        maxLevel = ic.readInt("maxLevel", 1);
        playCount = ic.readInt("playCount", 0);
        lastPlayTime = ic.readLong("lastPlayTime", 0);
        types = ic.readLong("types", 0);
        overlapTypes = ic.readLong("overlapTypes", 0);
        interruptTypes = ic.readLong("interruptTypes", 0);
        prior = ic.readInt("prior", 0);
    }
}
