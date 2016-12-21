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
package name.huliqing.luoying.object.skill;

import com.jme3.math.Vector3f;

/**
 * 步行和跑步接口,注:步行和跑步逻辑是一样的,不同的只是animation动画和步行速度
 * @author huliqing
 */
public interface Walk {
    
    /**
     * 设置行动方向（非位置）
     * @param walkDirection 
     */
    void setWalkDirection(Vector3f walkDirection);
    
    /**
     * 设置视角方向(非位置)
     * @param viewDirection 
     */
    void setViewDirection(Vector3f viewDirection);
    
    /**
     * 设置行动的基本速度
     * @param baseSpeed 
     */
    void setBaseSpeed(float baseSpeed);
}
