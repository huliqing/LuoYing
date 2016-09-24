/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import com.jme3.animation.LoopMode;
import java.util.ArrayList;
import name.huliqing.core.GameException;
import name.huliqing.core.data.AttributeUse;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.object.define.DefineFactory;
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
        
        data.setUseTime(proto.getAsFloat("useTime", 1));
        data.setAnimation(proto.getAsString("animation"));
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
        data.setSpeedAttribute(proto.getAsString("speedAttribute"));
        // CutTimeEnd的剪裁
        data.setCutTimeEndAttribute(proto.getAsString("cutTimeEndAttribute"));
        
        String loopTemp = proto.getAsString("loopMode");
        if ("1".equals(loopTemp)) {
            data.setLoopMode(LoopMode.Loop);
        } else if ("2".equals(loopTemp)) {
            data.setLoopMode(LoopMode.Cycle);
        } else {
            data.setLoopMode(LoopMode.DontLoop);
        }
        
        // 时间\动画剪裁参数
        data.setCutTimeStartMax(proto.getAsFloat("cutTimeStartMax", 0));
        data.setCutTimeEndMax(proto.getAsFloat("cutTimeEndMax", 0));
        
        data.setLevel(proto.getAsInteger("level", 1));
        data.setMaxLevel(proto.getAsInteger("maxLevel", 1));
        data.setLevelEl(proto.getAsString("levelEl"));
        data.setSkillPoints(proto.getAsInteger("skillPoints", 0));
        data.setLevelUpEl(proto.getAsString("levelUpEl"));
        data.setNeedLevel(proto.getAsInteger("needLevel", 0));
        data.setTags(DefineFactory.getSkillTagDefine().convert(proto.getAsArray("tags")));
        data.setOverlapTags(DefineFactory.getSkillTagDefine().convert(proto.getAsArray("overlapTags")));
        data.setInterruptTags(DefineFactory.getSkillTagDefine().convert(proto.getAsArray("interruptTags")));
        data.setPrior(proto.getAsInteger("prior", 0));
        
        // ---- 这几个参数是动态参数,不应该作为配置放到xml
        data.setCutTimeStart(0);
        data.setCutTimeEnd(0);
    }
    
    
}
