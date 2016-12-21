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
package name.huliqing.luoying.object.effect;

/**
 * 跟随的位置类型
 * @author huliqing
 */
public enum TraceOffsetType {
    
    /** 以原点作为跟随位置(默认方式) */
    origin,
    
    /** 以原点但是y值使用的是包围盒中心点处的值作为跟随位置。 */
    origin_bound_center,

    /** 以原点但是y值使用的是包围盒顶部最高位置点处的y值作为跟随位置 */
    origin_bound_top,
    
    // 暂不支持bound_bottom，好像没有意义
//    /** 以包含围盒的“底部”中心点作为跟随位置 */
//    bound_bottom,

    /** 以包围盒的“中心点”作为跟随位置 */
    bound_center,

    /** 以包围盒的“顶部”中心点作为跟随位置  */
    bound_top;

    public static TraceOffsetType identify(String name) {
        TraceOffsetType[] types = values();
        for (TraceOffsetType type : types) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        throw new UnsupportedOperationException("Unknow TraceOffsetType:" + name);
    }
}
