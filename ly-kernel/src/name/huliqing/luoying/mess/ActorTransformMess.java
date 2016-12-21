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
package name.huliqing.luoying.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.util.TempVars;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActorModule;

/**
 * 用于动态同步角色的位置,使用各种方式来平滑同步客户端与服务端角色的位置
 * @author huliqing
 */
@Serializable
public class ActorTransformMess extends GameMess {
    
    private long actorId = -1;
    private Vector3f location = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    private Vector3f viewDirection = new Vector3f();
    
    public ActorTransformMess() {}

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }
    
    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location.set(location);
    }

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection.set(walkDirection);
    }

    public Vector3f getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(Vector3f viewDirection) {
        this.viewDirection.set(viewDirection);
    }
    
    // ------v5,测试中
    @Override
    public void applyOnClient(GameClient gameClient) {
        super.applyOnClient(gameClient);
        Entity actor = Factory.get(PlayService.class).getEntity(actorId);
        if (actor == null || !actor.isInitialized()) {
            return;
        }
        ActorModule actorModule = actor.getModuleManager().getModule(ActorModule.class);
        if (actorModule == null) 
            return;
        
        TempVars tv = TempVars.get();
        Vector3f sourcePos = tv.vect1.set(actor.getSpatial().getWorldTranslation());
        
        Vector3f targetPos = tv.vect2.set(location);
        float distanceSquared = sourcePos.distanceSquared(targetPos);
        // 1.如果距离太小，在向量计算上容易出现意料之外的错误,所以不处理,直接同步位置
        if (distanceSquared < 0.0001f) {
//            if (Config.debug) {
//                LOG.log(Level.INFO, "--MATransform:........，距离太小，绝对同步:actor={0},distance={1}"
//                        , new Object[]{actor.getModel().getName(), FastMath.sqrt(distanceSquared)});
//            }
            
            actorModule.setLocation(location);
            actorModule.setWalkDirection(walkDirection);
            actorModule.setViewDirection(viewDirection);
            tv.release();
            return;
        }
        
        Vector3f targetDirection = tv.vect3.set(targetPos).subtractLocal(sourcePos);
        Vector3f sourceWalkDirection = tv.vect4.set(actorModule.getWalkDirection());
        
        // 2.如果距离大于一定程度，这个时候要判断WalkDirection的偏移程度，如果是
        // 在接近平行的情况下，即基本没有偏移的情况，则仍然可以使用拉伸的方式，
        // 这会产生比较平滑的视觉体验。如果角度偏移太大，则应该立即绝对同步，否则
        // 由于已经产生比较大的偏移的原因会越拉越大。
        // 注：这里的distanceSquared=4.0(distance=2.0)是按照角色的跑步速度进行大
        // 概的估算的，普通跑动速度6.0/perSec，2.0/6.0=0.333,即大概可以处理延迟330毫秒
        // ，超过这个延迟时:
        // A.如果WalkDirection偏移太大则直接绝对同步，这会让画面上的角色产生跳跃感。
        // B.如果基本没有偏移则仍然使用拉伸方式进行处理（由步骤3处理），这会产生平滑感。
        if (distanceSquared > 4.0f) {
            // 如果超偏移太大则直接绝对同步
            Vector3f targetDirectionNor = tv.vect5.set(targetDirection).normalizeLocal();
            Vector3f sourceWalkDirectionNor = tv.vect6.set(actorModule.getWalkDirection()).normalizeLocal();
            float d2 = sourceWalkDirectionNor.dot(targetDirectionNor);
            if ((d2 < 0.95f && d2 > -0.95f)) {
//                if (Config.debug) {
//                    LOG.log(Level.INFO, "--MATransform:OOOOOOOO，距离和偏移太大，直接绝对同步:actor={0},distance={1},d2={2}"
//                            , new Object[]{actor.getModel().getName(), FastMath.sqrt(distanceSquared), d2});
//                }

                actorModule.setLocation(location);
                actorModule.setWalkDirection(walkDirection);
                actorModule.setViewDirection(viewDirection);
                tv.release();
                return;
            }
        }
        
        // 3.平滑拉伸,这会有一个比较好的体验效果
        // 这里用targetDirection.setY(0)，以避免把角色拉到空中去，这经常发生在当
        // 主机FPS比较低，但是客户端FPS非常高的情况下，客户端角色走动时经常会被拉到空中
//        sourceWalkDirection.addLocal(targetDirection.setY(0));
        sourceWalkDirection.addLocal(targetDirection).setY(0);
        
        actorModule.setWalkDirection(sourceWalkDirection);
        actorModule.setViewDirection(viewDirection);
        
        tv.release();
    }
    
}
