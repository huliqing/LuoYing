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
package name.huliqing.luoying.enums;

/**
 * 定义几个常用的平面
 * @author huliqing
 */
public enum Plane {
    
    xy,
    
    xz, 
    
    yz;
    
    /**
     * 通过名称来识别Plane,名称列表：xy\xz\yz, 注意大小写
     * @param name
     * @return 
     */
    public static Plane identify(String name) {
       Plane[] values = Plane.values();
       for (Plane v : values) {
           if (v.name().equals(name)) {
               return v;
           }
       }
       throw new UnsupportedOperationException("Unknow type: name=" + name);
    }
}
