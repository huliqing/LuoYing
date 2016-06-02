/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.el;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.fighter.data.ElData;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 * XP奖励公式。
 * @author huliqing
 */
public class XpDropEl extends AbstractEl {
    
    // key = param
    private final Map<String, Object> valueMap = new HashMap<String, Object>(2);

    public XpDropEl(ElData data) {
        super(data);
    }

    /**
     * 获取经验奖励
     * @param sourceLevel 源角色等级,经验掉落者的等级
     * @param targetLevel 目标角色等级,攻击者等级
     * @return 
     */
    public int getValue(int sourceLevel, int targetLevel) {
        String strResult;
        if (params.size() <= 0) {
            strResult = eval(null);
            return (int) ConvertUtils.toFloat(strResult, 0);
        }
        
        valueMap.clear();
        for (String p : params) {
            if (p.startsWith("s_level")) {
                valueMap.put(p, sourceLevel);
            } else if (p.startsWith("t_level")) {
                valueMap.put(p, targetLevel);
            }
        }
        
        strResult = eval(valueMap);
        return (int) ConvertUtils.toFloat(strResult, 0);
    }
    
}
