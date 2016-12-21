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
 * 侦听器，用于侦听动画的执行是否周期结束。
 * @author huliqing
 */
public interface Listener {
    
    /**
     * 当动画周期结束时执行。根据loop模式的不同逻辑如下：<br>
     * dontloop: 动画结束后执行<br>
     * loop: 动画在每执行一次周期后调用一次该方法。<br>
     * cycle: 动画在每<b>来回</b>执行一次后调用一次该方法。<br>
     * @param anim 
     */
    void onDone(Anim anim);
    
}
