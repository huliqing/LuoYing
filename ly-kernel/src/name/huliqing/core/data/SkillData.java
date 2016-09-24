/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.animation.LoopMode;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.object.skin.WeaponStateUtils;
import name.huliqing.core.utils.ConvertUtils;

/**
 *
 * @author huliqing
 */
@Serializable
public class SkillData extends ObjectData {
    
    // 技能的执行时间，单位秒
    private float useTime;
    
    // 技能动画名称
    private String animation;
    // 执行这个技能的角色动画通道名称，角色必须配置有这些动画骨骼通道才有用。
    private String[] channels;
    //当执行动画时是否锁定动画通道，这可以避免当技能交叉重叠执行时动画通道被互相覆盖。
    //比如在执行取武器的动画时，这时的手部通道的动画不能被重新执行的“跑路”动画的相关通道覆盖。
    //被锁定的通道应该在退出技能时(cleanup时)重新解锁，避免其它技能无法使用。
    private boolean channelLocked;
    private float cooldown;
    private List<Integer> weaponStateLimit;
    // 定义当前技能需要消耗的角色的属性值
    private ArrayList<AttributeUse> useAttributes;
    // 影响技能执行速度的角色属性，指向一个attribute id,默认技能的执行速度为1，当设置了
    // 这个值之后，目标角色的指定属性的值将会影响到技能的执行速度。
    private String speedAttribute;
    
    // 用于剪裁cutTimeEndMax的角色属性ID。
    private String cutTimeEndAttribute;
    // 让技能循环执行
    private boolean loop;
    
    // 这两个参数标记useTime中可以剔除掉的<b>最高</b>时间比率.
    // 分别标记可剔除的前面和后面的时间.比如: useTime=5秒,
    // cutTimeStartMax=0.1,cutTimeEndMax=0.1, 则最高允许剔除的时间 = 5 * (0.1 + 0.1) = 1秒
    // cutTime的影响不只是技能的实际使用时间,与speed作用不同的地方在于:speed只会影响动画的
    // 播放速度,但是cutTime除了影响动画速度之外还影响动画长度.cutTimeStart和cutTimeEnd同时会剪裁
    // 掉动画的前面和后面一部分的片段,这可以用于在一些"攻击"招式上去除掉"起招"和"收招"动作,实现"连招"
    // 的效果.
    // 这两个值加起来不应该超过1.0
    private float cutTimeStartMax;
    private float cutTimeEndMax;
    
    // 技能等级
    private int level;
    // 技能允许的最高等级
    private int maxLevel;
    // 技能的等级公式，该公式与技能等级（level）可以计算出当前技能的一个等级值。
    private String levelEl;
    
    // 技能点数（技能熟练度），每次执行技能该值会递增，并经验公式来判断是否升级技能。
    private int skillPoints;
    // 技能升级等级公式，该公式中的每一个等级值表示每次技能升级时需要的sp数（skillPoints)
    private String levelUpEl;
    
    // 指定技能需要多少等级才能使用,只有角色达到指定等级时才能使用该技能.
    private int needLevel;
    
    // 技能标记
    private long tags;
    // 例外的，在排除优先级比较的前提下，如果一个技能可以覆盖另一个技能，则不需要比较优先级。
    private long overlapTags;
    // 例外的，在排除优先级比较的前提下，如果一个技能可以打断另一个技能，则不需要比较优先级。
    private long interruptTags;
    // 技能的优先级,优先级高的可以打断优先级低的技能
    private int prior;
    
    //--------------------------------------------------------------------------
    // inner内部参数，不开放到xml中配置,这些参数作为动态参数进行配置
    //--------------------------------------------------------------------------
    
    // 一般只设置cutTimeEnd就可以了，同时设置cutTimeStart会比较狂暴！！
    // 这两个参数是动态参数，和speed参数一样动态设置，不要开放到xml中去配置。
    // 标准情况下这两个参数都应该是0
    private float cutTimeStart = 0f;
    private float cutTimeEnd = 0f;
    
    /**
     * 最近一次使用技能的时间,用于判断技能冷却限制
     */
    private long lastPlayTime;
  
    // remove20160503,后续技能的执行速度将由角色自身的属性决定
//    // 技能的执行速度,该参数作为动态配置参数,不作为xml配置项开放,这个参数将会作
//    // 为角色用于提升技能的执行速度的属性,即由执行该技能的角色的"技能执行速度"
//    // 属性来动态控制,如角色获得攻击速度提升状态,攻击效果加成等.
//    private float speed = 1f;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(useTime, "useTime", 1);
        oc.write(animation, "animation", null);
        oc.write(channels, "channels", null);
        oc.write(channelLocked, "channelLocked", false);
        oc.write(cooldown, "cooldown", 0);
        
        // remove20160105这个状态不能保存
//        if (weaponStateLimit != null) {
//            oc.write(ConvertUtils.toIntegerArray(weaponStateLimit), "weaponStateLimit", null);
//        }
        
        oc.writeSavableArrayList(useAttributes, "useAttributes", null);
        oc.write(speedAttribute, "speedAttribute", null);
        oc.write(cutTimeEndAttribute, "cutTimeEndAttribute", null);
        oc.write(loop, "loop", false);
        
        oc.write(cutTimeStartMax, "cutTimeStartMax", 0);
        oc.write(cutTimeEndMax, "cutTimeEndMax", 0);
        oc.write(level, "level", 1);
        oc.write(maxLevel, "maxLevel", 1);
        oc.write(levelEl, "levelEl", null);
        oc.write(skillPoints, "skillPoints", 0);
        oc.write(levelUpEl, "levelUpEl", null);
        oc.write(needLevel, "needLevel", 0);
        oc.write(cutTimeStart, "cutTimeStart", 0);
        oc.write(cutTimeEnd, "cutTimeEnd", 0);
        oc.write(lastPlayTime, "lastPlayTime", 0);
//        oc.write(speed, "speed", 1);
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
//        radius = ic.readFloat("radius", 1);
        animation = ic.readString("animation", null);
        channels = ic.readStringArray("channels", null);
        channelLocked = ic.readBoolean("channelLocked", false);
        cooldown = ic.readFloat("cooldown", 0);
        
        //remove20160105,不能将“武器限制状态”保存到存档，武器状态限制是一个动态变换的值
        // 每次启动游戏都不一样。需要使用rebuildWeaponStateLimit进行重建
//        int[] tempWeaponStateLimit = ic.readIntArray("weaponStateLimit", null);
//        weaponStateLimit = ConvertUtils.toIntegerList(tempWeaponStateLimit);
        rebuildWeaponStateLimit();
        
        useAttributes = ic.readSavableArrayList("useAttributes", null);
        speedAttribute = ic.readString("speedAttribute", null);
        cutTimeEndAttribute = ic.readString("cutTimeEndAttribute", null);
        loop = ic.readBoolean("loop", false);
        cutTimeStartMax = ic.readFloat("cutTimeStartMax", 0);
        cutTimeEndMax = ic.readFloat("cutTimeEndMax", 0);
        level = ic.readInt("level", 1);
        maxLevel = ic.readInt("maxLevel", 1);
        levelEl = ic.readString("levelEl", null);
        skillPoints = ic.readInt("skillPoints", 0);
        levelUpEl = ic.readString("levelUpEl", null);
        needLevel = ic.readInt("needLevel", 0);
        cutTimeStart = ic.readFloat("cutTimeStart", 0);
        cutTimeEnd = ic.readFloat("cutTimeEnd", 0);
        lastPlayTime = ic.readLong("lastPlayTime", 0);
//        speed = ic.readFloat("speed", 1);
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

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    /**
     * 获取执行动画的通道名称，如果没有指定则返回null.
     * @return 
     */
    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    public boolean isChannelLocked() {
        return channelLocked;
    }

    public void setChannelLocked(boolean channelLocked) {
        this.channelLocked = channelLocked;
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

    public List<Integer> getWeaponStateLimit() {
        return weaponStateLimit;
    }

    public void setWeaponStateLimit(ArrayList<Integer> weaponStateLimit) {
        this.weaponStateLimit = weaponStateLimit;
    }
    
    /**
     * 获取技能需要消耗的属性
     * @return 
     */
    public ArrayList<AttributeUse> getUseAttributes() {
        return useAttributes;
    }

    /**
     * 设置技能需要消息的属性
     * @param useAttributes 
     */
    public void setUseAttributes(ArrayList<AttributeUse> useAttributes) {
        this.useAttributes = useAttributes;
    }

    public String getSpeedAttribute() {
        return speedAttribute;
    }

    public void setSpeedAttribute(String speedAttribute) {
        this.speedAttribute = speedAttribute;
    }

    public String getCutTimeEndAttribute() {
        return cutTimeEndAttribute;
    }

    public void setCutTimeEndAttribute(String cutTimeEndAttribute) {
        this.cutTimeEndAttribute = cutTimeEndAttribute;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

//    /**
//     * 获取当前技能类型可以覆盖的其它技能的类型，以二进制位表示，返回的整形中
//     * 每个位代表一个技能类型。
//     * @return 
//     */
//    public long getOverlaps() {
//        return overlaps;
//    }
//
//    /**
//     * 设置当前技能类型可以覆盖的其它技能类型列表，以二进制位表示，整形中
//     * 每个位代表一个技能类型。
//     * @param overlaps 技能类型
//     */
//    public void setOverlaps(long overlaps) {
//        this.overlaps = overlaps;
//    }

//    /**
//     * 获取当前技能类型可以打断的其它技能的类型，以二进制位表示，返回的整形中
//     * 每个位代表一个技能类型。
//     * @return 
//     */
//    public long getInterrupts() {
//        return interrupts;
//    }
//
//    /**
//     * 设置当前技能类型可以打断的其它技能类型列表，以二进制位表示，整形中
//     * 每个位代表一个技能类型。
//     * @param interrupts
//     */
//    public void setInterrupts(long interrupts) {
//        this.interrupts = interrupts;
//    }

    public float getCutTimeStartMax() {
        return cutTimeStartMax;
    }

    public void setCutTimeStartMax(float cutTimeStartMax) {
        this.cutTimeStartMax = cutTimeStartMax;
    }

    public float getCutTimeEndMax() {
        return cutTimeEndMax;
    }

    public void setCutTimeEndMax(float cutTimeEndMax) {
        this.cutTimeEndMax = cutTimeEndMax;
    }

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    public float getCutTimeStart() {
        return cutTimeStart;
    }

    public void setCutTimeStart(float cutTimeStart) {
        this.cutTimeStart = cutTimeStart;
    }

    public float getCutTimeEnd() {
        return cutTimeEnd;
    }

    public void setCutTimeEnd(float cutTimeEnd) {
        this.cutTimeEnd = cutTimeEnd;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public String getLevelEl() {
        return levelEl;
    }

    public void setLevelEl(String levelEl) {
        this.levelEl = levelEl;
    }
    
    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    public String getLevelUpEl() {
        return levelUpEl;
    }

    public void setLevelUpEl(String levelUpEl) {
        this.levelUpEl = levelUpEl;
    }

    public int getNeedLevel() {
        return needLevel;
    }

    public void setNeedLevel(int needLevel) {
        this.needLevel = needLevel;
    }
   
    /**
     * 重新创建武器状态限制列表，这个方法必须在SkillData创建的时候或从存档读入的
     * 时候调用一次，以重新创建状态列表，因为武器状态列表是不固定的，每次重起游戏
     * 都会重新创建。
     */
    public void rebuildWeaponStateLimit() {
        // ---- 把武器状态限制的字符串状态设置转化为weaponState----
        // 参数的格式为："左武器类型|右武器类型|其它部位武器类型1|其它部位武器类型2|...", 右边如果无武器时可省略
        // 伪示例1："0|剑" => 表示只有右武器为剑时可执行该技能，其它部分不能装备武器（0表示空，无武器）
        // 伪示例2："匕首|剑" => 表示只有左武器为匕首，右武器为剑时可执行该技能
        // 伪示例3："剑" => 表示只有左武器为剑时才可使用该技能。
        // 伪示例4："剑|剑,剑|剑|剑" => 表示二刀流或三刀流可以使用该技能。
        // 这里的左武器，右武器通常为左手的武器和右手拿的武器。
        // 武器类型代码参考：剑(1),匕首（2），弓箭（3) 具体参考SkinConstants.java定义
        String[] weaponStateLimitArr = getProto().getAsArray("weaponStateLimit");
        if (weaponStateLimitArr != null && weaponStateLimitArr.length > 0) {
            if (weaponStateLimit == null) {
                weaponStateLimit = new ArrayList<Integer>(weaponStateLimitArr.length);
            }
            weaponStateLimit.clear();
            for (String wsStr : weaponStateLimitArr) {
                if (wsStr.trim().equals("")) {
                    continue;
                }
                String[] wsArr = wsStr.split("\\|");
                int weaponState = WeaponStateUtils.createWeaponState(ConvertUtils.toIntegerArray(wsArr));
                weaponStateLimit.add(weaponState);
            }
        } else {
            weaponStateLimit = null;
        }
    }
        
    public long getTags() {
        return tags;
    }
    
    public void setTags(long tags) {
        this.tags = tags;
    }
    
    public long getOverlapTags() {
        return overlapTags;
    }

    public void setOverlapTags(long overlapTags) {
        this.overlapTags = overlapTags;
    }

    public long getInterruptTags() {
        return interruptTags;
    }

    public void setInterruptTags(long interruptTags) {
        this.interruptTags = interruptTags;
    }

    public int getPrior() {
        return prior;
    }

    public void setPrior(int prior) {
        this.prior = prior;
    }
    
    
}
