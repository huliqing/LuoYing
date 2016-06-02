/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.el;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ElData;
import name.huliqing.fighter.game.service.AttributeService;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 * 用于计算技能影响值的公式，比如技能对目标角色产生的各种伤害输出，各种BUFF
 * 加成等。
 * @author huliqing
 */
public class HitEl extends AbstractEl {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    
    // key = param
    private Map<String, Object> valueMap;

    public HitEl(ElData data) {
        super(data);
    }

    /**
     * 计算技能所产生的作用值
     * @param source 技能使用角色
     * @param sourceSkillValue 技能使用角色所使用技能的值
     * @param target 技能目标角色
     * @return 
     */
    public float getValue(Actor source, float sourceSkillValue, Actor target) {
        String strResult;
        if (params.size() <= 0) {
            strResult = eval(null);
            return ConvertUtils.toFloat(strResult, 0);
        }
        
        if (valueMap == null) {
            valueMap = new HashMap<String, Object>(params.size());
        }
        
        valueMap.clear();
        for (String p : params) {
            float attributeValue = 0;
            if (p.startsWith("s_")) {
                attributeValue = attributeService.getDynamicValue(source, p.substring(2));
            } else if (p.startsWith("t_")) {
                attributeValue = attributeService.getDynamicValue(target, p.substring(2));
            } else if (p.startsWith("sk_value")) {
                attributeValue = sourceSkillValue;
            }
            valueMap.put(p, attributeValue);
        }
        
        strResult = eval(valueMap);
        return ConvertUtils.toFloat(strResult, 0);
    }
    
}
