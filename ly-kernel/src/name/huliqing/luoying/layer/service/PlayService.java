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

import java.util.List;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;

/**
 * @author huliqing
 */
public interface PlayService extends PlayNetwork {
    
    /**
     * 注册当前游戏
     * @param game 
     */
    void registerGame(Game game);
    
    /**
     * 获取当前的游戏实例
     * @return 
     */
    Game getGame();
    
    /**
     * 从当前游戏中查找指定的实体
     * @param entityId
     * @return 
     */
    Entity getEntity(long entityId);
    
    /**
     * @param <T>
     * @param type
     * @param store
     * @return 
     */
    <T extends Entity> List<T> getEntities(Class<T> type, List<T> store);
    
    /**
     * 获取当前游戏主屏幕宽度
     * @return 
     */
    float getScreenWidth();
    
    /**
     * 获取当前游戏主屏幕高度
     * @return 
     */
    float getScreenHeight();
    
//    /**
//     * 获取场景指定位置处的高度，如果指定位置超出地面范围，则返回null.
//     * @param scene
//     * @param x
//     * @param z
//     * @return 
//     */
//    Vector3f getTerrainHeight(Scene scene, float x, float z);
    
}
