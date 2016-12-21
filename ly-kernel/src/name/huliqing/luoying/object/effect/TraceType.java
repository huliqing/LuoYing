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
 * 跟随类型
 * @author huliqing
 */
public enum TraceType {
    
    /** 不跟随 */
    no,
    
    /** 跟随一次 */
    once,
    
    /** 始终、持续跟随 */
    always;
    
    public static TraceType identity(String name) {
       for (TraceType tt : values()) {
           if (tt.name().equals(name)) {
               return tt;
           }
       }
       throw new UnsupportedOperationException("不支持的TraceType, name=" + name);
    }
}
