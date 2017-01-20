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
package name.huliqing.luoying.object.entity.impl;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.BulletAppState.ThreadingType;
import com.jme3.bullet.PhysicsSpace.BroadphaseType;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.NonModelEntity;

/**
 * 物理环境，添加这个Env到场景后，场景中的所有绑定有RigidBodyControl的物体将受到物理影响。
 * @author huliqing
 */
public class PhysicsEntity extends NonModelEntity implements SceneListener {

    // 是否打开physics调试
    private boolean debug;

    // 重力方向及大小
    private Vector3f gravity = new Vector3f(0, -9.81f, 0);
    private String broadphaseType;
    private String threadingType;
    private float speed = 1.0f;
    private Vector3f worldMax;
    private Vector3f worldMin;
    private float accuracy = 1f / 60f;
    private int maxSubSteps = 4;
    private int solverNumIterations = 10;
    
    // ---- inner
    private BulletAppState bulletAppState;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        debug = data.getAsBoolean("debug", debug);
        gravity = data.getAsVector3f("gravity", gravity);
        broadphaseType = data.getAsString("broadphaseType");
        threadingType = data.getAsString("threadingType");
        speed = data.getAsFloat("speed", speed);
        worldMax = data.getAsVector3f("worldMax");
        worldMin = data.getAsVector3f("worldMin");
        accuracy = data.getAsFloat("accuracy", accuracy);
        maxSubSteps = data.getAsInteger("maxSubSteps", maxSubSteps);
        solverNumIterations = data.getAsInteger("solverNumIterations", solverNumIterations);
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
    }

    @Override
    public void initEntity() {
        bulletAppState = new BulletAppState();
        
        BroadphaseType bt = getBroadphaseType(broadphaseType);
        if (bt != null) {
            bulletAppState.setBroadphaseType(bt);
        }
        // ThreadingType.PARALLEL可能只有在多个PhysicsSpace时才有意义
        // 注：setThreadingType必须放在attach(bulletAppState)之前
        ThreadingType tt = getThreadingType(threadingType);
        if (tt != null) {
            bulletAppState.setThreadingType(tt);
        }
        
        LuoYing.getApp().getStateManager().attach(bulletAppState);
        
        bulletAppState.setSpeed(speed);
        if (worldMax != null) {
            bulletAppState.setWorldMax(worldMax);
        }
        if (worldMin != null) {
            bulletAppState.setWorldMin(worldMin);
        }
        bulletAppState.setDebugEnabled(debug);
        // 注：这些设置要放在attach到stateManager之后
        bulletAppState.getPhysicsSpace().setGravity(gravity);
        bulletAppState.getPhysicsSpace().setAccuracy(accuracy);
        bulletAppState.getPhysicsSpace().setMaxSubSteps(maxSubSteps);
        bulletAppState.getPhysicsSpace().setSolverNumIterations(solverNumIterations);
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene); 
        // 将当前场景中已经存在的所有物体添加到物理环境中
        bulletAppState.getPhysicsSpace().addAll(scene.getRoot());
        
        // 以监听动态添加的物体
        scene.addSceneListener(this);
    }

    @Override
    public void cleanup() {
        scene.removeSceneListener(this);
        bulletAppState.getPhysicsSpace().removeAll(scene.getRoot());
        LuoYing.getApp().getStateManager().detach(bulletAppState);
        super.cleanup();
    }
    
    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }
    
    private BroadphaseType getBroadphaseType(String name) {
        if (name == null)
            return null;
        BroadphaseType[] bts = BroadphaseType.values();
        for (BroadphaseType bt : bts) {
            if (bt.name().equals(name)) {
                return bt;
            }
        }
        return null;
    }
    
    private ThreadingType getThreadingType(String name) {
        if (name == null)
            return null;
        
        ThreadingType[] tts = ThreadingType.values();
        for (ThreadingType tt : tts) {
            if (tt.name().equals(name)) {
                return tt;
            }
        }
        return null;
    }

    // ---- listener
    
    @Override
    public void onSceneLoaded(Scene scene) {
        // ignore
    }
    
    @Override
    public void onSceneEntityAdded(Scene scene, Entity objectAdded) {
        if (objectAdded == this) 
            return;
        
        if (objectAdded.getSpatial() != null) {
            bulletAppState.getPhysicsSpace().addAll(objectAdded.getSpatial());
        }
    }

    @Override
    public void onSceneEntityRemoved(Scene scene, Entity objectRemoved) {
        if (objectRemoved.getSpatial() != null) {
            bulletAppState.getPhysicsSpace().removeAll(objectRemoved.getSpatial());
        }
    }


}
