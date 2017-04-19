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

import com.jme3.animation.LoopMode;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ChannelModule;

/**
 * 行路的技能
 * @see RunSkill
 * @author huliqing
 */
public class WalkSkill extends SimpleAnimationSkill implements Walk{
    private final ActorService actorService = Factory.get(ActorService.class);
    private ChannelModule channelModule;
    
    // 步行或跑步循环动画的播放速度。
    protected float animSpeed = 1.0f;
    // 基本移动速度
    protected float baseSpeed = 1.5f;
    
    protected final Vector3f walkDirection = new Vector3f();
    protected final Vector3f viewDirection = new Vector3f();
    
    private final Vector3f usedWalkDirection = new Vector3f();

    @Override
    public void setData(SkillData data) {
        super.setData(data);
        animSpeed = data.getAsFloat("animSpeed", animSpeed);
        baseSpeed = data.getAsFloat("baseSpeed", baseSpeed);
    }

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor);
        channelModule = actor.getModule(ChannelModule.class);
    }
    
    public Vector3f getWalkDirection() {
        return walkDirection;
    }
    
    /**
     * 设置行动方向（非位置）
     * @param walkDirection 
     */
    @Override
    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection.set(walkDirection);
    }
    
    /**
     * 设置视角方向(非位置)
     * @param viewDirection 
     */
    @Override
    public void setViewDirection(Vector3f viewDirection) {
        this.viewDirection.set(viewDirection);
    }
    
    @Override
    public void setBaseSpeed(float baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    @Override
    public void initialize() {
        super.initialize();
        usedWalkDirection.set(walkDirection).setY(0).normalizeLocal().multLocal(baseSpeed * getSpeed());
//        actorService.setWalkDirection(actor, walkDirection.setY(0).normalizeLocal().mult(baseSpeed * getSpeed())); // remove20170420
        actorService.setWalkDirection(actor, usedWalkDirection);
        
        if (viewDirection != null) {
            actorService.setViewDirection(actor, viewDirection);
        }
        
        // 添加角色自动位置同步
        Network.getInstance().addSyncEntity(actor);
    }
    
    @Override
    protected void doSkillUpdate(float tpf) {}

    @Override
    protected void doUpdateAnimation(String animation, boolean loop
            , float animFullTime, float animStartTime) {
        super.doUpdateAnimation(animation, loop, animFullTime / animSpeed, animStartTime);
    }

    @Override
    public void restoreAnimation() {
        if (animation != null) {
            channelModule.restoreAnimation(animation
                    , channels
                    , loop ? LoopMode.Loop : LoopMode.DontLoop
                    , getAnimFullTime() / animSpeed
                    , 0);
        }
        // 需要恢复角色动量
        actorService.setWalkDirection(actor, usedWalkDirection);
    }
    
    @Override
    public void cleanup() {
        actorService.setWalkDirection(actor, Vector3f.ZERO.clone());
        
        // 移除角色位置自动同步(平滑同步)
        Network.getInstance().removeSyncEntity(actor);
        
        super.cleanup();
    }
    
}
