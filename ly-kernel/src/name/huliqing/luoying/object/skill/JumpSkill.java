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
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.module.ChannelModule;

/**
 * 跳跃技能, 注意：这个技能需要角色使用了BetterCharactorControl(但是这个Control存在一点小bug.
 * 最好使用MyBetterCharactorControl,参考这个类的说明。).
 * @author huliqing
 */
public class JumpSkill extends AbstractSkill {

    private static final Logger LOG = Logger.getLogger(JumpSkill.class.getName());
    private ChannelModule channelModule;
    
    // 角色的起跳、滞空、落地动画
    private String animStart;
    private String animInAir;
    private String animEnd;
    // 清空动画的循环模式
    private LoopMode animInAirLoop = LoopMode.Cycle;
    // 角色的起跳动作、空中动作、落地动作的时间，
    private float useTimeInStart = 0.5f;
    private float useTimeInAir = 1f;
    private float useTimeInEnd = 0.3f;
    // 跳跃作用力的开始时间
    private float forceApplyTime = 0.3f;
    // 跳跃的作用力这个力量强度和角色的质量大小有关系，当角色的质量越大，就需要更强的跳跃力
    private Vector3f jumpForce = new Vector3f(0, 100, 0);
    // 如果角色是在向前走动的时候进行跳跃，那么跳跃的时候会有一个向前的冲力，这个参数用于控制这个冲力的大小，
    // 例如，如果不想让角色跳得太远，则要减少这个值，否则增加这个值。
    private float walkForceIntensity = 0.3f;
    // 跳跃的强度(绑定实体属性)
    private String bindJumpIntensityAttribute;
    // 一个用于超时结束当前动作的限制,避免当角色卡在空中时无法退出当前技能的BUG
    private float timeout = 15;
    
    // ---- 不需要开放的参数
    // 标记相应的各动作的动画是否已经执行过
    private boolean animStartPlayed;
    private boolean animInAirPlayed;
    private boolean animEndPlayed;
    private boolean forceApplied;
    
    // 计算技能在空中的停留时间
    private float timeUsedInAir;
    // 计算技能在落下地面后的落地动画时间
    private float timeUsedInEnd;
    
    //  ---- inner
    private NumberAttribute jumpIntensityAttribute;
    
    // 角色控制器
    private BetterCharacterControl bcc;
    private final Vector3f lastWalkDirection = new Vector3f();
    private float lastPhysicsDamping;
    
    @Override
    public void setData(SkillData data) {
        super.setData(data);
        animStart = data.getAsString("animStart");
        animInAir = data.getAsString("animInAir");
        animEnd = data.getAsString("animEnd");
        animInAirLoop = getLoopMode(data.getAsString("animInAirLoop"));
        useTimeInStart = data.getAsFloat("useTimeInStart", useTimeInStart);
        useTimeInAir = data.getAsFloat("useTimeInAir", useTimeInAir);
        useTimeInEnd = data.getAsFloat("useTimeInEnd", useTimeInEnd);
        forceApplyTime = data.getAsFloat("forceApplyTime", forceApplyTime);
        
        jumpForce = data.getAsVector3f("jumpForce", jumpForce);
        walkForceIntensity = data.getAsFloat("walkForceIntensity", walkForceIntensity);
        bindJumpIntensityAttribute = data.getAsString("bindJumpIntensityAttribute");
        timeout = data.getAsFloat("timeout", timeout);
        
        animStartPlayed = data.getAsBoolean("animStartPlayed", animStartPlayed);
        animInAirPlayed = data.getAsBoolean("animInAirPlayed", animInAirPlayed);
        animEndPlayed = data.getAsBoolean("animEndPlayed", animEndPlayed);
        forceApplied = data.getAsBoolean("forceApplied", forceApplied);
        timeUsedInAir = data.getAsFloat("timeUsedInAir", timeUsedInAir);
        timeUsedInEnd = data.getAsFloat("timeUsedInEnd", timeUsedInEnd);
        
        // 内部参数
        Vector3f _lastWalkDirection = data.getAsVector3f("_lastWalkDirection");
        if (_lastWalkDirection != null) {
            lastWalkDirection.set(_lastWalkDirection);
        }
        lastPhysicsDamping = data.getAsFloat("_lastPhysicsDamping");
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("animStartPlayed", animStartPlayed);
        data.setAttribute("animInAirPlayed", animInAirPlayed);
        data.setAttribute("animEndPlayed", animEndPlayed);
        data.setAttribute("forceApplied", forceApplied);
        data.setAttribute("timeUsedInAir", timeUsedInAir);
        data.setAttribute("timeUsedInEnd", timeUsedInEnd);
        
        // 内部参数
        data.setAttribute("_lastWalkDirection", lastWalkDirection);
        data.setAttribute("_lastPhysicsDamping", lastPhysicsDamping);
        
        //  不会改变的数据不需要更新回去
    }

    @Override
    public void initialize() {
        super.initialize();
        channelModule = actor.getModule(ChannelModule.class);
        jumpIntensityAttribute = actor.getAttribute(bindJumpIntensityAttribute, NumberAttribute.class);
        
        // JumpStart动glb
        if (!animStartPlayed) {
            animStartPlayed = true;
            if (animStart != null) {
                channelModule.playAnim(animStart, null, LoopMode.DontLoop, useTimeInStart , 0);
            }
        }
        
        bcc = actor.getSpatial().getControl(BetterCharacterControl.class);
        if (bcc != null) {
            // 获取物理控制器，并记住跳跃之前角色的移动方向，跳跃之前必须先将角色的移动清0。
            // 因为这个WalkDirection是持续的作用力过程，会造成跳跃空中时，如果遇到障碍物有可能会导致角色紧贴着物体不会落下
            // 或者遇到障碍物后这个力仍然一直推着角色向前滑动的bug.
            lastWalkDirection.set(bcc.getWalkDirection());
            bcc.setWalkDirection(new Vector3f());
            
            //  记住这个阻尼值，当角色在跳跃时这个值必须清0，否则会导致很难跳起来。在角色跳到空中后，这个设置可以还原。
            lastPhysicsDamping = bcc.getPhysicsDamping();
        } else {
            LOG.log(Level.WARNING, "Jump failure! BetterCharacterControl not found from Entity, entityId={0}, uniqueId={1}"
                        , new Object[] {actor.getData().getId(), actor.getData().getUniqueId()});
        }
    }
    
    @Override
    public void cleanup() {
        animStartPlayed = false;
        animInAirPlayed = false;
        animEndPlayed = false;
        forceApplied = false;
        timeUsedInAir = 0;
        timeUsedInEnd = 0;
        if (bcc != null) {
            // 不应该恢复动量，这不是这个技能应该做的,有可能造成BUG，由walkSkill去处理就行（如果WalkSkill同时在执行）。
            // 那么当JumpSkill执行完之后，WalkSkill应该自己去恢复这个移动量.
//            bcc.setWalkDirection(lastWalkDirection) 

            // 这个应该恢复,因为技能有可能在中途被打断，所在必须确保技能结束的时候恢复这个参数
            bcc.setPhysicsDamping(lastPhysicsDamping);
        }
        super.cleanup();
    }

    @Override
    public int checkState() {
        bcc = actor.getSpatial().getControl(BetterCharacterControl.class);
        if (bcc == null || !bcc.isOnGround()) {
            return StateCode.SKILL_USE_FAILURE;
        }
        return super.checkState();
    }
    
    @Override
    protected void doSkillUpdate(float tpf) {
        if (bcc == null) {
            return;
        }
        
        // 跳跃的作用力是由jumpDir所设置方向上的力加上角色移动方向上的力合成的。
        if (!forceApplied && time >= forceApplyTime) {
            forceApplied = true;
            TempVars tv = TempVars.get();
            
            Vector3f finalJumpForce = tv.vect1.set(jumpForce);
            actor.getSpatial().getWorldRotation().mult(finalJumpForce, finalJumpForce);
            Vector3f walkDirectionForce = tv.vect2.set(lastWalkDirection).multLocal(jumpForce.length())
                    .multLocal(walkForceIntensity); // walkForceIntensity调整向前的冲力
            if (jumpIntensityAttribute != null) {
                finalJumpForce.multLocal(jumpIntensityAttribute.floatValue());
                walkDirectionForce.multLocal(jumpIntensityAttribute.floatValue());
            }
            finalJumpForce.addLocal(walkDirectionForce);
            bcc.setPhysicsDamping(0);
            bcc.setJumpForce(finalJumpForce);
            bcc.jump();
            tv.release();
        }
        
        // 执行空中落下时的动画
        if (!animInAirPlayed && time >= useTimeInStart) {
            animInAirPlayed = true;
            if (animInAir != null) {
                channelModule.playAnim(animInAir, null, animInAirLoop, useTimeInAir, 0);
            }
        }
        
        //  稍微延迟一下后再判断是否isOnGround()，因为bcc.jump()后角色不会立即离开地面.
        if (!animEndPlayed && timeUsedInAir > 0.05f) {
            if (bcc.isOnGround() || time >= timeout) {
                animEndPlayed = true;
                if (animEnd != null) {
                    channelModule.playAnim(animEnd, null, LoopMode.DontLoop, useTimeInEnd, 0);
                }
                // 当角色跳跃到空中之后重新设置回阻尼。
                bcc.setPhysicsDamping(lastPhysicsDamping);
//                LOG.log(Level.INFO, "animEndPlayed, bcc.isOnGround={0}", new Object[]{bcc.isOnGround()});
            }
        }
        
        if (animInAirPlayed) {
            timeUsedInAir += tpf;
        }
        
        if (animEndPlayed) {
            timeUsedInEnd += tpf;
            if (timeUsedInEnd >= useTimeInEnd) {
                bcc.setWalkDirection(lastWalkDirection); // 还原walkDirection
            }
        }
    }
    
    @Override
    public boolean isEnd() {
        //  remove20170418,因为跳跃技能有一个滞空的情况，这个情况会导致技能在空中的时间是不确定的。
        // 因而技能的整个执行时间也是不确定的。
//        super.isEnd(); 

        if (bcc == null) 
            return true;
        
        if (time > timeout) 
            return true;
        
        // 当最后一阶段（落地动作）时间执行完毕时，视为跳跃技能技能结束
        if (timeUsedInEnd >= useTimeInEnd) {
            return true;
        }
        
        return false;
    }

    @Override
    public void restoreAnimation() {
        // 只恢复空中动画
        if (animInAirPlayed && !animEndPlayed) {
            if (animInAir != null) {
                channelModule.restoreAnimation(animInAir, null, animInAirLoop, useTimeInAir, 0);
            }
        }
    }
    
    // 获取loopMode设置，如果找不到匹配，则默认使用loop模式
    private LoopMode getLoopMode(String name) {
        for (LoopMode lm : LoopMode.values()) {
            if (lm.name().equals(name)) {
                return lm;
            }
        }
        return LoopMode.Loop;
    }

}
