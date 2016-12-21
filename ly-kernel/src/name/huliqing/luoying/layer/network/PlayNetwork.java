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

import com.jme3.network.HostedConnection;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 
 * @author huliqing
 */
public interface PlayNetwork extends Inject {
    
    /**
     * 向当前游戏主场景添加物体.(非GUI场景）
     * @param entity 
     */
    void addEntity(Entity entity);
    
    /**
     * 向当前游戏的Gui场景中添加物体
     * @param entity 
     */
    void addGuiEntity(Entity entity);
    
    /**
     * 向指定的场景添加物体。
     * @param scene
     * @param entity 
     */
    void addEntity(Scene scene, Entity entity);
    
    /**
     * 向指定的客户端添加场景实体，不进行广播，也不在本地服务端中添加,一般用于向客户端初始化场景时使用.
     * @param conn
     * @param entity 
     */
    void addEntityToClient(HostedConnection conn, Entity entity);
    
    /**
     * 从指定的场景中移除物体
     * @param entity 
     */
    void removeEntity(Entity entity);
    
    /**
     * 让目标进行攻击。
     * @param actor 源角色
     * @param target 攻击目标
     */
    void attack(Entity actor, Entity target);
    
    /**
     * 更新服务端和客户端的随机种子以重新生成随机数。服务端可以定时调用这个方法来更新服务端和客户端的随机数。
     */
    void updateRandomSeed();
    
}
