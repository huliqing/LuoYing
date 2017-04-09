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
package name.huliqing.luoying.object.scene;

import name.huliqing.luoying.object.entity.Entity;

/**
 * 场景侦听器
 * @author huliqing
 */
public interface SceneListener {

    /**
     * 当场景载入完毕后该方法被立即调用，这表示场景中的初始实体完全载入完毕。
     * @param scene 
     */
    void onSceneLoaded(Scene scene);

    /**
     * 当容器添加了一个物体之后该方法被立即调用。
     * @param scene
     * @param entityAdded 
     */
    void onSceneEntityAdded(Scene scene, Entity entityAdded);

    /**
     * 当容器移除了一个物体之后该方法被立即调用
     * @param scene
     * @param entityRemoved 
     */
    void onSceneEntityRemoved(Scene scene, Entity entityRemoved);
    
    /**
     * 当场景中的实体的某些状态发生变化时该方法被调用（例如：实体的开关状态(Enabled)），
     * @param scene
     * @param entity 
     */
    void onSceneEntityStateChanged(Scene scene, Entity entity);
}
