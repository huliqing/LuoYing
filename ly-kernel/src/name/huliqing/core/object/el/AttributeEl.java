/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.el;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ElData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.utils.ConvertUtils;

/**
 * 通过源角色和目标角色的属性来计算一个值。支持参数：{s_attributeName}源角色属性; {t_attributeName}目标角色属性
 * @author huliqing
 * @param <T>
 */
public class AttributeEl<T extends ElData> extends AbstractEl<T> {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    
    // key = param
    private final Map<String, Object> valueMap = new HashMap<String, Object>(5);

    // remove20160829
//    /**
//     * 获取经验奖励
//     * @param sourceLevel 源角色等级,经验掉落者的等级
//     * @param targetLevel 目标角色等级,攻击者等级
//     * @return 
//     */
//    public int getValue(int sourceLevel, int targetLevel) {
//        String strResult;
//        if (params.size() <= 0) {
//            strResult = eval(null);
//            return (int) ConvertUtils.toFloat(strResult, 0);
//        }
//        
//        valueMap.clear();
//        for (String p : params) {
//            if (p.startsWith("s_level")) {
//                valueMap.put(p, sourceLevel);
//                
//            } else if (p.startsWith("t_level")) {
//                
//                valueMap.put(p, targetLevel);
//            }
//        }
//        
//        strResult = eval(valueMap);
//        return (int) ConvertUtils.toFloat(strResult, 0);
//    }
    
    /**
     * @param source
     * @param target
     * @return 
     */
    public synchronized float getValue(Actor source, Actor target) {
        String strResult;
        
        // params中包含的是带有"{}"符号的参数，如果params为空，则说明没有表达式中没有特殊参数需要替换值,
        // 则直接计算这个表达式就可以, 也就是允许表达式中直接使用javascript的普通表达式。
        if (params.size() <= 0) {
            strResult = eval(null);
            return ConvertUtils.toFloat(strResult, 0);
        }
        
        // 替换参数值
        valueMap.clear();
        for (String p : params) {
            if (p.startsWith("s_")) {
                valueMap.put(p, getAttributeValue(source, p.substring(2)));
            } else if (p.startsWith("t_")) {
                valueMap.put(p, getAttributeValue(target, p.substring(2)));
            }
        }
        
        strResult = eval(valueMap);
        return ConvertUtils.toFloat(strResult, 0);
    }
    
    private float getAttributeValue(Actor actor, String attributeName) {
        return attributeService.getNumberAttributeValue(actor, attributeName, 0);
    }
}
