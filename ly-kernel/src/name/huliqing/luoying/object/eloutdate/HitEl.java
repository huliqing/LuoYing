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
//package name.huliqing.luoying.object.eloutdate;
//
//import java.util.HashMap;
//import java.util.Map;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.data.ElData;
//import name.huliqing.luoying.layer.service.EntityService;
//import name.huliqing.luoying.object.entity.Entity;
//import name.huliqing.luoying.utils.ConvertUtils;
//
///**
// * 用于计算技能影响值的公式，比如技能对目标角色产生的各种伤害输出，各种BUFF
// * 加成等。
// * @author huliqing
// * @deprecated 
// * @param <T>
// */
//public class HitEl<T extends ElData> extends AbstractEl<T> {
//    private final EntityService entityService = Factory.get(EntityService.class);
//    
//    // key = param
//    private Map<String, Object> valueMap;
//
//    /**
//     * 计算技能所产生的作用值
//     * @param source 技能使用角色
//     * @param sourceSkillValue 技能使用角色所使用技能的值
//     * @param target 技能目标角色
//     * @return 
//     */
//    public synchronized float getValue(Entity source, float sourceSkillValue, Entity target) {
//        String strResult;
//        
//        // params中包含的是带有"{}"符号的参数，如果params为空，则说明没有表达式中没有特殊参数需要替换值,
//        // 则直接计算这个表达式就可以, 也就是允许表达式中直接使用javascript的普通表达式。
//        if (params.size() <= 0) {
//            strResult = eval(null);
//            return ConvertUtils.toFloat(strResult, 0);
//        }
//        
//        if (valueMap == null) {
//            valueMap = new HashMap<String, Object>(params.size());
//        }
//        valueMap.clear();
//        for (String p : params) {
//            float attributeValue = 0;
//            if (p.startsWith("s_")) {
////                attributeValue = source.getAttributeManager().getNumberAttributeValue(p.substring(2), 0);
//                attributeValue = entityService.getNumberAttributeValue(source, p.substring(2), 0);
//                
//                
//            } else if (p.startsWith("t_")) {
////                attributeValue = target.getAttributeManager().getNumberAttributeValue(p.substring(2), 0);
//                attributeValue = entityService.getNumberAttributeValue(target, p.substring(2), 0);
//                
//            } else if (p.startsWith("sk_value")) {
//                attributeValue = sourceSkillValue;
//            }
//            valueMap.put(p, attributeValue);
//        }
//        
//        strResult = eval(valueMap);
//        return ConvertUtils.toFloat(strResult, 0);
//    }
//    
//}
