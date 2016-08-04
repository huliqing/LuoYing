/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import com.jme3.animation.LoopMode;
import java.util.ArrayList;
import name.huliqing.core.GameException;
import name.huliqing.core.data.AttributeUse;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.utils.ConvertUtils;

/**
 *
 * @author huliqing
 */
public class SkillDataLoader implements DataLoader<SkillData> {

    @Override
    public void load(Proto proto, SkillData data) {
        if (!(data instanceof SkillData)) {
            throw new GameException("Not a skill data. proto=" + proto);
        }
        
        // remove20160105，这里不能搞特殊，默认的SkillHandler已经放在SkillData内部处理.
        // 放在这里可能造成载入“存档角色后”skillData"没有hander的BUG，因为proto是不保存到
        // 存档中的,并且因为skill.xml中不配置handler,所以proto也就不存在,在存档载入
        // 后就会找不到handler而无法执行技能。
//        if (proto.getAttribute("handler") == null) {
//            proto.putAttribute("handler", IdConstants.HANDLER_SKILL);
//        }
        
        data.setSkillType(identifySkillType(proto));
        data.setUseTime(proto.getAsFloat("useTime", 1));
        
        // remove,不再需要这个属性
//        data.setRadius(proto.getAsFloat("radius", 1));
        
        data.setAnimation(proto.getAttribute("animation"));
        data.setChannels(proto.getAsArray("channels"));
        data.setChannelLocked(proto.getAsBoolean("channelLocked", false));
        data.setCooldown(proto.getAsFloat("cooldown", 0));
        // 重建武器状态限制，这里比较特殊。因为武器状态列表是一个动态值，每次起动
        // 游戏都不一样，所以必须在创建data和载入存档的时候重新读取。放在data内
        // 部是为了重用这一代码
        data.rebuildWeaponStateLimit();
        
        // 技能消耗的属性值
        String[] useAttributesStr = proto.getAsArray("useAttributes");
        if (useAttributesStr != null) {
            ArrayList<AttributeUse> useAttributes = new ArrayList<AttributeUse>(useAttributesStr.length);
            for (int i = 0; i < useAttributesStr.length; i++) {
                String[] temp = useAttributesStr[i].split("\\|");
                useAttributes.add(new AttributeUse(temp[0], ConvertUtils.toFloat(temp[1], 0)));
            }
            data.setUseAttributes(useAttributes);
        }
        
        // 影响技能执行速度的目标属性ID
        data.setSpeedAttribute(proto.getAttribute("speedAttribute"));
        // CutTimeEnd的剪裁
        data.setCutTimeEndAttribute(proto.getAttribute("cutTimeEndAttribute"));
        
        String loopTemp = proto.getAttribute("loopMode");
        if ("1".equals(loopTemp)) {
            data.setLoopMode(LoopMode.Loop);
        } else if ("2".equals(loopTemp)) {
            data.setLoopMode(LoopMode.Cycle);
        } else {
            data.setLoopMode(LoopMode.DontLoop);
        }
        
        // extOverlaps存取的是一个一个的技能类型的value, 格式如："skillTypeValue1,skillTypeValue2,..."
        // 完整的overlaps是包含默认设置的和额外各个独立技能定制的
        long overlaps = data.getSkillType().getOverlaps();           // 系统默认的类型
        int[] extOverlaps = proto.getAsIntegerArray("extOverlaps"); // 技能定制的类型
        if (extOverlaps != null) {
            for (int eo : extOverlaps) {
                overlaps |= 1 << eo;
            }
        }
        data.setOverlaps(overlaps);
        
        // 同上
        long interrupts = data.getSkillType().getInterrupts();       
        int[] extInterrupts = proto.getAsIntegerArray("extInterrupts"); 
        if (extInterrupts != null) {
            for (int ei : extInterrupts) {
                interrupts |= 1 << ei;
            }
        }
        data.setInterrupts(interrupts);
        
        // 时间\动画剪裁参数
        data.setCutTimeStartMax(proto.getAsFloat("cutTimeStartMax", 0));
        data.setCutTimeEndMax(proto.getAsFloat("cutTimeEndMax", 0));
        
        data.setLevel(proto.getAsInteger("level", 1));
        data.setMaxLevel(proto.getAsInteger("maxLevel", 1));
        data.setLevelEl(proto.getAttribute("levelEl"));
//        data.setSkillEl(proto.getAttribute("skillEl"));
        data.setSkillPoints(proto.getAsInteger("skillPoints", 0));
        data.setLevelUpEl(proto.getAttribute("levelUpEl"));
        data.setNeedLevel(proto.getAsInteger("needLevel", 0));
        
        // ---- 这几个参数是动态参数,不应该作为配置放到xml
        data.setCutTimeStart(0);
        data.setCutTimeEnd(0);
//        data.setSpeed(1f); // 不再使用speed,现在默认为1
        
    }
    
    private SkillType identifySkillType(Proto proto) {
        String tagName = proto.getTagName();
        if (tagName.equals("skillWalk")) {
            return SkillType.walk;
        } 

        if (tagName.equals("skillRun")) {
            return SkillType.run;
        } 

        if (tagName.equals("skillWait")) {
            return SkillType.wait;
        }

        if (tagName.equals("skillIdle")) {
            return SkillType.idle;
        } 

        if (tagName.equals("skillHurt")) {
            return SkillType.hurt;
        }

        if (tagName.equals("skillDead")
                || tagName.equals("skillDeadRagdoll")) {
            return SkillType.dead;
        } 

        // 技能箭\多重射击术
        if (tagName.equals("skillAttack") 
                || tagName.equals("skillShot") 
                || tagName.equals("skillShotBow")) {
            String stName = proto.getAttribute("skillType");
            if (stName == null) {
                throw new NullPointerException("Need specify a skillType, tagName=" + tagName + ", proto=" + proto);
            }
            return SkillType.identifyByName(stName);
        }

        // 召唤技\回城技
        if (tagName.equals("skillSummon")
                || tagName.equals("skillBack")) { 
            return SkillType.magic;
        }

        if (tagName.equals("skillDefend")) {
            return SkillType.defend;
        }

        if (tagName.equals("skillDuck")) {
            return SkillType.duck;
        }
        
        if (tagName.equals("skillReset")) {
            return SkillType.reset;
        }
        
        if (tagName.equals("skillSkin")) {
            return SkillType.skin;
        }
        
        throw new UnsupportedOperationException("Unsupported skill type, tagName=" + tagName + ", proto=" + proto);
    }
}
