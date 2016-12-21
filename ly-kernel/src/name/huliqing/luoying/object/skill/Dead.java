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
 * 定义死亡技能
 * @author huliqing
 */
public interface Dead {
    
    /**
     * 指定一个力的方向和大小,当某些使用物理特性的死亡技能时比较有用,比如布娃娃系
     * 统, 这个力指定了死亡时的受攻击方向.
     * 比如角色A攻击B,则这个力可计算为 B.subtract(A).normalizeLocal().multLocal(力的大小).
     * 力的大小可以使用攻击力的大小作为参考
     * @param force 
     */
    void applyForce(Vector3f force);
    

}
