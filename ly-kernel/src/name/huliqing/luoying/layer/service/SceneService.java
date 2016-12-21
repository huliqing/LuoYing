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
package name.huliqing.luoying.layer.service;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public interface SceneService extends Inject {

    /**
     * 获取当前游戏场景指定位置高度点，如果位置点超出地形外部，则该方法返回null.
     * @param x
     * @param z
     * @return null或地形高度位置点
     */
    Vector3f getSceneHeight(float x, float z);
    
    /**
     * 获取当前游戏场景指定位置的高度点，并将结果存放到store中，如果位置点超出地形外部则该方法什么也不会做，
     * 只返回store.
     * @param x
     * @param z
     * @param store 如果值为null则自动创建(默认(0,0,0))
     * @return 返回地形高度位置点或直接返回store.
     */
    Vector3f getSceneHeight(float x, float z, Vector3f store);
    
    /**
     * 获取场景指定位置高度点，如果位置点超出地形外部，则该方法返回null.
     * @param scene
     * @param x
     * @param z
     * @return 
     */
    Vector3f getSceneHeight(Scene scene, float x, float z);
    

}
