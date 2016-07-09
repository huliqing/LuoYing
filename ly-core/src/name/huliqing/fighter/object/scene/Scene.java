/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.scene;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.Light;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.object.AbstractPlayObject;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.DataProcessor;
import name.huliqing.fighter.object.env.Env;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class Scene<T extends SceneData> extends AbstractPlayObject implements DataProcessor<T>{
    private final PlayService playService = Factory.get(PlayService.class);
    
    public interface Listener {
        
        /**
         * 当场景新添加了物体后触发该方法。
         * @param scene 
         * @param objectAdded 
         */
        void onAdded(Scene scene, Spatial objectAdded);
        
        /**
         * 当场景移除了物体之后触发该方法。
         * @param scene 
         * @param objectRemoved
         */
        void onRemoved(Scene scene, Spatial objectRemoved);
        
        /**
         * 当场景刚初始化完毕后执行该方法, 这个方法会在场景中所有的Env都执行完initialize后被调用。
         * @param scene 
         */
        void onInitialized(Scene scene);
    }
    
    // ---- inner
    protected T data;
    
    protected List<Listener> listeners;
    
    // 所有的环境物体列表，包含天空盒和地形
    protected List<Env> envs;
    
    // 场景根节点
    protected Node sceneRoot;
    
    // 地型，允许多个地形
    protected  Node terrainRoot;
    
    // 物理空间
    protected BulletAppState bulletAppState;
    
    protected Application app;
    // 场景默认的FilterPostProcessor，在某些情况下，不能同时存在多个FilterPostProcessor,比如当WaterFilter和ShadowFilter同时
    // 存在于不同的FilterPostProcessor时会出现渲染异常。应该把它们放在同一个FilterPostProcessor下面。
    // 这个fppDefault就是作为默认的FilterPostProcessor存在的。
    protected FilterPostProcessor fppDefault;
    
    public Scene() {}
    
    @Override
    public void initialize(Application app) {
        super.initialize(app);
        this.app = app;
        
        // 添加默认的FilterPostProcessor.
        if (fppDefault != null) {
            fppDefault.setAssetManager(app.getAssetManager());
            app.getViewPort().addProcessor(fppDefault);
        }
        
        // 建立场景节点。
        sceneRoot = new Node("sceneRoot");
        terrainRoot = new Node("terrainRoot");
        sceneRoot.attachChild(terrainRoot);
        playService.addObject(sceneRoot);
        
        // use physics
        if (data.isUsePhysics()) {
            if (bulletAppState == null) {
                bulletAppState = new BulletAppState();
            }
            // ThreadingType.PARALLEL可能只有在多个PhysicsSpace时才有意义
            // 注：setThreadingType必须放在attach(bulletAppState)之前
    //        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
            app.getStateManager().attach(bulletAppState);
            // 如果重力太大会发生画面抖动现象，不要高于40.
            // 注：setGravity必须放在attach(bulletAppState)之后
            bulletAppState.getPhysicsSpace().setGravity(data.getGravity());
            bulletAppState.setDebugEnabled(data.isDebugPhysics());
        }
        
        // env列表
        List<EnvData> edList = data.getEnvs();
        if (edList != null && !edList.isEmpty()) {
            envs = new ArrayList<Env>(edList.size());
            for (EnvData ed : edList) {
                Env env = DataFactory.createProcessor(ed);
                envs.add(env);
                env.initialize(app, this);
            }
        }
        
        // 触发侦听器
        if (listeners != null) {
            for (Listener l : listeners) {
                l.onInitialized(this);
            }
        }
    }

    @Override
    public void cleanup() {
        // 先清理listeners,避免在envs清理的时候又调用到listeners.
        if (listeners != null) {
            listeners.clear();
        }
        if (envs != null) {
            for (Env env : envs) {
                env.cleanup();
            }
        }
        
        if (bulletAppState != null && app != null) {
            app.getStateManager().detach(bulletAppState);
            bulletAppState = null;
        }

        if (sceneRoot != null) {
            sceneRoot.removeFromParent();
            sceneRoot = null;
        }
        if (fppDefault != null) {
            app.getViewPort().removeProcessor(fppDefault);
            fppDefault = null;
        }
        super.cleanup();
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void initData(T data) {
        this.data = data;
    }

    /**
     * 获取当前场景的Env
     * @return 
     */
    public List<Env> getEnvs() {
        return envs;
    }
    
    public Spatial getSceneRoot() {
        return sceneRoot;
    }

    public Spatial getTerrain() {
        return terrainRoot;
    }
    
    public PhysicsSpace getPhysicsSpace() {
        return data.isUsePhysics() ? bulletAppState.getPhysicsSpace() : null;
    }
    
    /**
     * 添加物体到场景中
     * @param spatial 
     */
    public void addSceneObject(Spatial spatial) {
        if (spatial == null)
            return;
        
        sceneRoot.attachChild(spatial);
        addPhysicsObject(spatial);
        
        // 触发侦听器
        if (listeners != null) {
            for (Listener l : listeners) {
                l.onAdded(this, spatial);
            }
        }
    }
    
    /**
     * 从场景中移除节点，除了将节点从场景移除之外，该方法还会偿试把节点中的RigidBodyControl也从物理空间中移除。
     * @param spatial 
     */
    public void removeSceneObject(Spatial spatial) {
        if (spatial == null)
            return;
        
        if (bulletAppState != null) {
            RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
            if (rbc != null) {
                bulletAppState.getPhysicsSpace().remove(rbc);
            }
        }
        spatial.removeFromParent();
        
        // 触发侦听器
        if (listeners != null) {
            for (Listener l : listeners) {
                l.onRemoved(this, spatial);
            }
        }
    }
    
    /**
     * 把物体作为地型添加到场景中
     * @param spatial 
     */
    public void addTerrainObject(Spatial spatial) {
        terrainRoot.attachChild(spatial);
        addPhysicsObject(spatial);
    }
    
    /**
     * 添加场景灯光。
     * @param light 
     */
    public void addSceneLight(Light light) {
        if (light == null) 
            return;
        sceneRoot.addLight(light);
    }
    
    /**
     * 移除场景灯光
     * @param light 
     */
    public void removeSceneLight(Light light) {
        if (light == null) 
            return;
        sceneRoot.removeLight(light);
    }
    
    /**
     * 添加节点到物理空间
     * @param s 
     */
    private void addPhysicsObject(Spatial s) {
        if (!data.isUsePhysics() ||  bulletAppState == null || s == null)
            return;
        RigidBodyControl rbc = s.getControl(RigidBodyControl.class);
        if (rbc != null) {
            bulletAppState.getPhysicsSpace().add(rbc);
        }
    }
    
    /**
     * 添加一个Filter到场景默认的FilterPostProcessor.
     * @param filter 
     */
    public void addFilter(Filter filter) {
        if (fppDefault == null) {
            fppDefault = new FilterPostProcessor();
            if (app != null) {
                fppDefault.setAssetManager(app.getAssetManager());
                app.getViewPort().addProcessor(fppDefault);
            }
        }
        if (!fppDefault.getFilterList().contains(filter)) {
            fppDefault.addFilter(filter);
        }
    }
    
    /**
     * 从场景的默认FilterPostProcessor中移除Filter
     * @param filter 
     */
    public void removeFilter(Filter filter) {
        if (fppDefault != null && filter != null) {
            fppDefault.removeFilter(filter);
        }
    }
    
    /**
     * 判断一个位置是否为空白区域，即无树木等障碍物之类。
     * @param x
     * @param z
     * @param radius
     * @return 
     */
    public boolean checkIsEmptyZone(float x, float z, float radius) {
        List<Spatial> children = sceneRoot.getChildren();
        RigidBodyControl rbc;
        float radiusSquare = radius * radius;
        for (Spatial s : children) {
            rbc = s.getControl(RigidBodyControl.class);
            if (rbc == null) {
                continue;
            }
            if (distanceSquare(x, z, rbc.getPhysicsLocation().x, rbc.getPhysicsLocation().z) < radiusSquare) {
                return false;
            }
        }
        return true;
    }
    
    private double distanceSquare(float x, float z, float otherX, float otherZ) {
        double dx = x - otherX;
        double dz = z - otherZ;
        return dx * dx + dz * dz;
    }
    
    /**
     * 添加一个侦听器
     * @param listener 
     */
    public void addListener(Listener listener) {
        if (listeners == null) 
            listeners = new ArrayList<Listener>();
        
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * 删除场景侦听器
     * @param listener
     * @return 
     */
    public boolean removeListener(Listener listener) {
        return listeners != null && listeners.remove(listener);
    }
}
