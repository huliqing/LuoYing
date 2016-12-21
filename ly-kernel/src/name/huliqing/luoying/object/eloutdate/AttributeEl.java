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
// * @deprecated 
// * 通过源角色和目标角色的属性来计算一个值。支持参数：{s_attributeName}源角色属性; {t_attributeName}目标角色属性
// * @author huliqing
// * @param <T>
// */
//public class AttributeEl<T extends ElData> extends AbstractEl<T> {
//    private final EntityService entityService = Factory.get(EntityService.class);
//    
//    // key = param
//    private final Map<String, Object> valueMap = new HashMap<String, Object>(5);
//
//    /**
//     * @param source
//     * @param target
//     * @return 
//     */
//    public synchronized float getValue(Entity source, Entity target) {
//        String strResult;
//        
//        // params中包含的是带有"{}"符号的参数，如果params为空，则说明没有表达式中没有特殊参数需要替换值,
//        // 则直接计算这个表达式就可以, 也就是允许表达式中直接使用javascript的普通表达式。
//        if (params.size() <= 0) {
//            strResult = eval(null);
//            return ConvertUtils.toFloat(strResult, 0);
//        }
//        
//        // 替换参数值
//        valueMap.clear();
//        for (String p : params) {
//            if (p.startsWith("s_")) {
//                valueMap.put(p, getAttributeValue(source, p.substring(2)));
//            } else if (p.startsWith("t_")) {
//                valueMap.put(p, getAttributeValue(target, p.substring(2)));
//            }
//        }
//        
//        strResult = eval(valueMap);
//        return ConvertUtils.toFloat(strResult, 0);
//    }
//    
//    private float getAttributeValue(Entity actor, String attributeName) {
////        return actor.getAttributeManager().getNumberAttributeValue(attributeName, 0);
//        return entityService.getNumberAttributeValue(actor, attributeName, 0);
//    }
//}
