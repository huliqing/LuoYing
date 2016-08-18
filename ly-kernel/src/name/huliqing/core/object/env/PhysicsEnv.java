/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.env;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.BulletAppState.ThreadingType;
import com.jme3.bullet.PhysicsSpace.BroadphaseType;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.core.data.env.EnvData;
import name.huliqing.core.object.scene.Scene;

/**
 * 物理环境，添加这个Env到场景后，场景中的所有绑定有RigidBodyControl的物体将受到物理影响。
 * @author huliqing
 * @param <T>
 */
public class PhysicsEnv <T extends EnvData> extends AbstractEnv <T> implements Scene.SceneListener {

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
    private Application app;
    private BulletAppState bulletAppState;
    
    @Override
    public void setData(T data) {
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
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        this.app = app;
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
        
        app.getStateManager().attach(bulletAppState);
        
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
        
        // 将当前场景中已经存在的所有物体添加到物理环境中
        bulletAppState.getPhysicsSpace().addAll(scene.getSceneRoot());
        
        // 以监听动态添加的物体
        scene.addSceneListener(this);
    }

    @Override
    public void cleanup() {
        scene.removeSceneListener(this);
        app.getStateManager().detach(bulletAppState);
        super.cleanup();
    }
    
    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    @Override
    public void onSceneInitialized(Scene scene) {
        // ignore
    }
    
    @Override
    public void onSceneObjectAdded(Scene scene, Spatial objectAdded) {
        bulletAppState.getPhysicsSpace().addAll(objectAdded);
    }

    @Override
    public void onSceneObjectRemoved(Scene scene, Spatial objectRemoved) {
        bulletAppState.getPhysicsSpace().removeAll(objectRemoved);
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

}
