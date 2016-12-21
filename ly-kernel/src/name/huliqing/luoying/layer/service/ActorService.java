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
import java.util.List;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 */
public interface ActorService extends ActorNetwork {
    
    /**
     * 获取角色周围一定围围内的其它角色，包含死亡的角色，但不包含角色自身(actor).
     * 可以使用angle来限制角度，即表示在actor前方的一定角度范围内的角色。如果
     * angle小于或等于0 则将不会获得任何角色。
     * @param actor
     * @param maxDistance 限制距离
     * @param angle 限制角度取值: [0.0~360]
     * @param store
     * @return 
     */
    List<Entity> findNearestActors(Entity actor, float maxDistance, float angle, List<Entity> store);
    
    /**
     * 同步角色的变换信息
     * @param actor 同步的角色
     * @param location 同步位置
     * @param viewDirection 同步视角方向
     */
    void syncTransform(Entity actor, Vector3f location, Vector3f viewDirection);
        
    /**
     * 获取角色当前位置
     * @param actor
     * @return 
     */
    Vector3f getLocation(Entity actor);
    
    /**
     * 判断目标角色是否打开了物理功能
     * @param actor
     * @return 
     */
    boolean isPhysicsEnabled(Entity actor);
    
    /**
     * 获取角色视角方向
     * @param actor
     * @return 
     */
    Vector3f getViewDirection(Entity actor);
    
    /**
     * 设置角色步行方向
     * @param actor
     * @param walkDirection 
     */
    void setWalkDirection(Entity actor, Vector3f walkDirection);
    
    /**
     * 获取角色步行方向
     * @param actor
     * @return 
     */
    Vector3f getWalkDirection(Entity actor);
   
    /**
     * 锁定或解锁指定的动画通道，当一个动画通道被锁定之后将不能再执行任何动画，包
     * 括reset，除非重新进行解锁
     * @param actor
     * @param locked true锁定通道，false解锁通道
     * @param channelIds 指定的通道列表，如果为null或指定的通道不存在则什么也不做。
     */
    void setChannelLock(Entity actor, boolean locked, String... channelIds);
    
    /**
     * 把骨骼动画定位在当前所播放动画的第一帧处．
     * 可使用该方法来使角色停止活动。如：当角色没有“死亡”动画时，
     * 角色在死后需要停止活动，则可使用该方法来停止正在执行的动画。
     * @param actor
     * @return 
     * @see #resetToAnimationTime(Actor, String, float) 
     */
    boolean reset(Entity actor);
    
    /**
     * 获取目标位置与当前角色的正前方的夹角度数.简单的说,即当前角色要转多少度才能正视
     * 到position位
     * @param actor
     * @param position
     * @return 返回值为角度,不是弧度
     */
    float getViewAngle(Entity actor, Vector3f position);
        
    boolean isKinematic(Entity actor);
    
    void setKinematic(Entity actor, boolean kinematic);
    
    /**
     * 判断角色与目标位置的距离
     * @param actor
     * @param position
     * @return 
     */
    float distance(Entity actor, Vector3f position);
    
    /**
     * 判断两个角色的距离
     * @param actor
     * @param target
     * @deprecated 
     * @return 
     */
    float distance(Entity actor, Entity target);
 
    /**
     * @deprecated 
     * @param actor
     * @param target
     * @return 
     */
    float distanceSquared(Entity actor, Entity target);
    
}
