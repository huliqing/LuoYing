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

import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public interface FollowAction extends Action {
    
    /**
     * 设置要跟随的目标
     * @param target 
     */
    public void setFollow(Spatial target);
    
    /**
     * 设置跟随的最近距离，当跟随到该目标以内时，即停止跟随，该值不需要太小。
     * 也不能小于0。
     * @param nearest 
     */
    public void setNearest(float nearest);
}
