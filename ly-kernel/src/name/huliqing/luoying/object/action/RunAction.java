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
package name.huliqing.luoying.object.action;

import com.jme3.math.Vector3f;

/**
 * 走路的行为,能够让角色走达到目标位置。
 * @author huliqing
 */
public interface RunAction extends Action {
    
    /**
     * 设置要行走的目标位置
     * @param positon 
     */
    public void setPosition(Vector3f positon);
    
    /**
     * 设置允许走到的最近位置,在到达该位置之内时即停止行走。
     * @param nearest 
     */
    public void setNearest(float nearest);
    
    /**
     * 判断当前是否已经行走到目标位置。只要当前角色与pos的距离小于或等于
     * nearest时就视为已经到达。
     * @param pos 
     * @return  
     */
    public boolean isInPosition(Vector3f pos);
    
    /**
     * 判断当前是否已经走到目标位置。
     * @return 
     */
    public boolean isEndPosition();
}
