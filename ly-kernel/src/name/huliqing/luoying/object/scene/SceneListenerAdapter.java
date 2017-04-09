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
 * 场景侦听器的适配器
 * @author huliqing
 */
public class SceneListenerAdapter implements SceneListener {

    @Override
    public void onSceneLoaded(Scene scene) {
        // 子类覆盖
    }

    @Override
    public void onSceneEntityAdded(Scene scene, Entity objectAdded) {
        // 子类覆盖
    }

    @Override
    public void onSceneEntityRemoved(Scene scene, Entity objectRemoved) {
        // 子类覆盖
    }

    @Override
    public void onSceneEntityStateChanged(Scene scene, Entity entity) {
        // 子类覆盖
    }
    
}
