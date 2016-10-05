/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.List;

/**
 * @author huliqing
 */
@Serializable
public class SkillData extends ObjectData {
    
    // 技能的执行时间，单位秒
    private float useTime;

    // 技能的冷却时间,单位秒
    private float cooldown;
    
    // 指定技能需要多少等级才能使用,只有角色达到指定等级时才能使用该技能.
    private int levelLimit;
    
    // 武器状态限制
    private long[] weaponStateLimit;
    
    // 定义当前技能需要消耗的角色的属性值
    private List<AttributeUse> useAttributes;
    
    // 技能等级
    private int level;
    
    // 技能允许的最高等级
    private int maxLevel;
    
    // 技能点数（技能熟练度），每次执行技能该值会递增，并经验公式来判断是否升级技能。
    private int playCount;
    
    // 技能标记
    private long tags;
    // 例外的，在排除优先级比较的前提下，如果一个技能可以覆盖另一个技能，则不需要比较优先级。
    private long overlapTags;
    // 例外的，在排除优先级比较的前提下，如果一个技能可以打断另一个技能，则不需要比较优先级。
    private long interruptTags;
    // 技能的优先级,优先级高的可以打断优先级低的技能
    private int prior;
    
    /** 最近一次使用技能的时间,用于判断技能冷却限制 */
    private long lastPlayTime;
    
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
        oc.write(levelLimit, "levelLimit", 0);
        oc.write(lastPlayTime, "lastPlayTime", 0);
        oc.write(tags, "tags", 0);
        oc.write(overlapTags, "overlapTags", 0);
        oc.write(interruptTags, "interruptTags", 0);
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
        levelLimit = ic.readInt("levelLimit", 0);
        lastPlayTime = ic.readLong("lastPlayTime", 0);
        tags = ic.readLong("tags", 0);
        overlapTags = ic.readLong("overlapTags", 0);
        interruptTags = ic.readLong("interruptTags", 0);
        prior = ic.readInt("prior", 0);
    }
    
    @Override
    public int getTotal() {
        return 1;
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
     * 设置武器状态限制。
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
     * 获取技能的等级限制，这个限制表示目标角色只有达到（大于或等于）这个等级时才可以使用这个技能。
     * @return 
     */
    public int getLevelLimit() {
        return levelLimit;
    }

    /**
     * 设置技能的等级限制，这个限制表示目标角色只有达到（大于或等于）这个等级时才可以使用这个技能。
     * @param levelLimit 
     */
    public void setLevelLimit(int levelLimit) {
        this.levelLimit = levelLimit;
    }
        
    /**
     * 获取技能标记
     * @return 
     */
    public long getTags() {
        return tags;
    }
    
    /**
     * 设置技能标记
     * @param tags 
     */
    public void setTags(long tags) {
        this.tags = tags;
    }
    
    /**
     * 获取当前技能类型可以覆盖的其它技能的类型，以二进制位表示，返回的整形中每个位代表一个技能类型。
     * @return 
     */
    public long getOverlapTags() {
        return overlapTags;
    }
    
    /**
     * 设置当前技能类型可以覆盖的其它技能类型列表，以二进制位表示，整形中
     * 每个位代表一个技能类型。
     * @param overlapTags 技能类型
     */
    public void setOverlapTags(long overlapTags) {
        this.overlapTags = overlapTags;
    }

    /**
     * 获取当前技能类型可以打断的其它技能的类型，以二进制位表示，返回的整形中
     * 每个位代表一个技能类型。
     * @return 
     */
    public long getInterruptTags() {
        return interruptTags;
    }

    /**
     * 设置当前技能类型可以打断的其它技能类型列表，以二进制位表示，整形中
     * 每个位代表一个技能类型。
     * @param interruptTags
     */
    public void setInterruptTags(long interruptTags) {
        this.interruptTags = interruptTags;
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
    
    
}
