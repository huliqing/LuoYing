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
package name.huliqing.luoying.object.anim;

/**
 * @author huliqing
 */
public enum Loop {

    /** 只执行一次，模式：A -> B */
    dontLoop,
    
    /** 始终循环,循环模式：A -> B, A -> B, A -> B */
    loop,
    
    /** 周期性循环，循环模式： A -> B -> A -> B -> ... */
    cycle;
    
    public static Loop identify(String name) {
       Loop[] values = Loop.values();
       for (Loop v : values) {
           if (v.name().equals(name)) {
               return v;
           }
       }
       throw new UnsupportedOperationException("Unknow type: name=" + name);
    }
}
