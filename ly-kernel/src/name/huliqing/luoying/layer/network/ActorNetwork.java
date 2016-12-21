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
package name.huliqing.luoying.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 */
public interface ActorNetwork extends Inject {
    
    /**
     * 打开或关闭角色的物理功能
     * @param actor
     * @param enabled 
     */
    void setPhysicsEnabled(Entity actor, boolean enabled);

    /**
     * 设置角色位置
     * @param actor
     * @param location
     */
    void setLocation(Entity actor, Vector3f location);
    
    /**
     * 设置角色视角方向
     * @param actor
     * @param viewDirection 
     */
    void setViewDirection(Entity actor, Vector3f viewDirection);
    
    /**
     * 让角色看向指定<b>位置</b>(非方向)
     * @param actor
     * @param position 
     */
    void setLookAt(Entity actor, Vector3f position);
    
}
