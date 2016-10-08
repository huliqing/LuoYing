///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.ly.object.scene;
//
//import com.jme3.app.Application;
//import com.jme3.app.state.AbstractAppState;
//import com.jme3.app.state.AppStateManager;
//import com.jme3.bullet.control.RigidBodyControl;
//import com.jme3.light.Light;
//import com.jme3.post.Filter;
//import com.jme3.post.FilterPostProcessor;
//import com.jme3.post.filters.TranslucentBucketFilter;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.ly.Factory;
//import name.huliqing.ly.data.ObjectData;
//import name.huliqing.ly.data.SceneData;
//import name.huliqing.ly.xml.DataProcessor;
//import name.huliqing.ly.object.env.Env;
//import name.huliqing.ly.layer.service.EnvService;
//import name.huliqing.ly.object.Loader;
//
///**
// * @author huliqing
// * @param <T>
// */
//public class Scene<T extends SceneData> extends AbstractAppState implements DataProcessor<T>{
////    private final EnvService envService = Factory.get(EnvService.class);
//    
//    /**
//     * 场景侦听器，用于侦听场景的初始化，场景中新增加物体、移除物体等。
//     */
//    public interface SceneListener {
//        
//        /**
//         * 当场景刚初始化完毕后执行该方法, 这个方法会在场景中所有的Env都执行完initialize后被调用。
//         * @param scene 
//         */
//        void onSceneInitialized(Scene scene);
//        
//        /**
//         * 当场景新添加了物体后触发该方法。
//         * @param scene 
//         * @param objectAdded 
//         */
//        void onSceneObjectAdded(Scene scene, Spatial objectAdded);
//        
//        /**
//         * 当场景移除了物体之后触发该方法。
//         * @param scene 
//         * @param objectRemoved
//         */
//        void onSceneObjectRemoved(Scene scene, Spatial objectRemoved);
//        
//    }
//    
//    public interface SceneEnvListener {
//        
//        /**
//         * 当场景中初始化完一个env后触发该方法。
//         * @param scene
//         * @param envInitialized 
//         */
//        void onSceneEnvInitialized(Scene scene, Env envInitialized);
//        
//        /**
//         * 当场景中新增加了Env时触发该方法。
//         * @param scene
//         * @param envAdded 
//         */
//        void onSceneEnvAdded(Scene scene, Env envAdded);
//        
//        /**
//         * 当场景移除了一个Env后触发该方法.
//         * @param scene 当前场景
//         * @param envRemoved 已经被移除的Env, 并且已经被清理(cleanup)
//         */
//        void onSceneEnvRemoved(Scene scene, Env envRemoved);
//    }
//    
//    // ---- inner
//    protected T data;
//    
//    protected List<SceneListener> sceneListeners;
//    protected List<SceneEnvListener> sceneEnvListeners;
//    
//    // 所有的环境物体列表，包含天空盒和地形
//    protected final List<Env> envs = new ArrayList<Env>();
//    
//    // 场景根节点
//    protected Node sceneRoot = new Node("SceneRoot");
//    
//    // 地型，允许多个地形
//    protected  Node terrainRoot;
//    
//    protected Application app;
//    // 场景默认的FilterPostProcessor，在某些情况下，不能同时存在多个FilterPostProcessor,比如当WaterFilter和ShadowFilter同时
//    // 存在于不同的FilterPostProcessor时会出现渲染异常。应该把它们放在同一个FilterPostProcessor下面。
//    // 这个fppDefault就是作为默认的FilterPostProcessor存在的。
//    protected FilterPostProcessor fppDefault;
//    
//    // 用于处理粒子效果被“PostWater"挡住的问题 
//    protected final TranslucentBucketFilter translucentBucketFilter = new TranslucentBucketFilter();
//    
//    @Override
//    public T getData() {
//        return data;
//    }
//
//    @Override
//    public void setData(T data) {
//        this.data = data;
//    }
//
//    @Override
//    public void updateDatas() {
//        // ignore
//    }
//    
//    /**
//     * 获取场景根节点
//     * @return 
//     */
//    public Spatial getSceneRoot() {
//        return sceneRoot;
//    }
//    
//    /**
//     * 设置场景根节点
//     * @param sceneRoot 
//     */
//    public void setSceneRoot(Node sceneRoot) {
//        this.sceneRoot = sceneRoot;
//    }
//    
//    @Override
//    public void initialize(AppStateManager stateManager, Application app) {
//        super.initialize(stateManager, app);
//        this.app = app;
//        
//        // 添加默认的FilterPostProcessor.
//        if (fppDefault != null) {
//            fppDefault.setAssetManager(app.getAssetManager());
//            app.getViewPort().addProcessor(fppDefault);
//        }
//        
//        // 建立场景节点。
//        terrainRoot = new Node("terrainRoot");
//        sceneRoot.attachChild(terrainRoot);
//        
//        // 初始化Env列表
//        // 把数据复制到temp中进行处理，以避免在列表初始化的过程影响到原始列表，这种情况可能发生在：当一个Env在初始化的
//        // 时候在initialize方法又向场景动态添加Env.这会造成在循环data.getEnvs()列表的时候同时又修改（增、删）列表数据。
//        List<ObjectData> initEnvDatas = data.getEnvs();
//        if (initEnvDatas != null && !initEnvDatas.isEmpty()) {
//            List<ObjectData> temp = new ArrayList<ObjectData>(initEnvDatas);
//            for (ObjectData ed : temp) {
//                DataProcessor dp = Loader.load(ed);
//                if (!(dp instanceof Env)) {
//                    continue;
//                }
//                Env env = (Env) dp;
//                env.initialize(app, this);
//                envs.add(env);
//                notifySceneEnvListenerAdded(env);
//                notifySceneEnvListenerInitialized(env);
//            }
//        }
//        
//        // 触发侦听器
//        if (sceneListeners != null) {
//            for (SceneListener l : sceneListeners) {
//                l.onSceneInitialized(this);
//            }
//        }
//    }
//
//    @Override
//    public void cleanup() {
//        // 先清理listeners,避免在envs清理的时候又调用到listeners.
//        if (sceneListeners != null) {
//            sceneListeners.clear();
//        }
//        if (fppDefault != null) {
//            app.getViewPort().removeProcessor(fppDefault);
//            fppDefault = null;
//        }
//        if (sceneEnvListeners != null) {
//            sceneEnvListeners.clear();
//        }
//        for (Env env : envs) {
//            env.cleanup();
//        }
//        envs.clear();
//        if (sceneRoot != null) {
//            sceneRoot.detachAllChildren();
//            sceneRoot.removeFromParent();
//        }
//        super.cleanup();
//    }
//    
//    /**
//     * 获取当前场景的所有Env。
//     * 注：不要修改该返回列表，只允许只读。
//     * @return 
//     */
//    public List<Env> getEnvs() {
//        return envs;
//    }
//    
//    public Env getEnv(String envId) {
//        Env result = null;
//        for (Env env : envs) {
//            if (env.getData().getId().equals(envId)) {
//                return env;
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 获取场景的地形根节点
//     * @return 
//     */
//    public Spatial getTerrain() {
//        return terrainRoot;
//    }
//    
//    /**
//     * 添加物体到场景中
//     * @param spatial 
//     */
//    public void addSceneObject(Spatial spatial) {
//        if (spatial == null)
//            return;
//        
//        sceneRoot.attachChild(spatial);
//        // 触发侦听器
//        if (sceneListeners != null) {
//            for (SceneListener l : sceneListeners) {
//                l.onSceneObjectAdded(this, spatial);
//            }
//        }
//    }
//    
//    /**
//     * 从场景中移除节点，除了将节点从场景移除之外，该方法还会偿试把节点中的RigidBodyControl也从物理空间中移除。
//     * @param spatial 
//     */
//    public void removeSceneObject(Spatial spatial) {
//        if (spatial == null)
//            return;
//        
//        spatial.removeFromParent();
//        // 触发侦听器
//        if (sceneListeners != null) {
//            for (SceneListener l : sceneListeners) {
//                l.onSceneObjectRemoved(this, spatial);
//            }
//        }
//    }
//    
//    /**
//     * 把物体作为地型添加到场景中
//     * @param spatial 
//     */
//    public void addTerrainObject(Spatial spatial) {
//        terrainRoot.attachChild(spatial);
//        // 触发侦听器
//        if (sceneListeners != null) {
//            for (SceneListener l : sceneListeners) {
//                l.onSceneObjectAdded(this, spatial);
//            }
//        }
//    }
//    
//    /**
//     * 添加场景灯光。
//     * @param light 
//     */
//    public void addSceneLight(Light light) {
//        if (light == null) 
//            return;
//        sceneRoot.addLight(light);
//    }
//    
//    /**
//     * 移除场景灯光
//     * @param light 
//     */
//    public void removeSceneLight(Light light) {
//        if (light == null) 
//            return;
//        sceneRoot.removeLight(light);
//    }
//    
//    /**
//     * 添加一个Filter到场景默认的FilterPostProcessor.
//     * @param filter 
//     */
//    public void addFilter(Filter filter) {
//        if (fppDefault == null) {
//            fppDefault = new FilterPostProcessor();
//            if (app != null) {
//                fppDefault.setAssetManager(app.getAssetManager());
//                app.getViewPort().addProcessor(fppDefault);
//            }
//        }
//        if (!fppDefault.getFilterList().contains(filter)) {
//            fppDefault.addFilter(filter);
//            fppDefault.removeFilter(translucentBucketFilter);
//            fppDefault.addFilter(translucentBucketFilter);
//        }
//    }
//    
//    /**
//     * 从场景的默认FilterPostProcessor中移除Filter
//     * @param filter 
//     */
//    public void removeFilter(Filter filter) {
//        if (fppDefault != null && filter != null) {
//            fppDefault.removeFilter(filter);
//        }
//    }
//    
//    /**
//     * 判断一个位置是否为空白区域，即无树木等障碍物之类。
//     * @param x
//     * @param z
//     * @param radius
//     * @return 
//     */
//    public boolean checkIsEmptyZone(float x, float z, float radius) {
//        List<Spatial> children = sceneRoot.getChildren();
//        RigidBodyControl rbc;
//        float radiusSquare = radius * radius;
//        for (Spatial s : children) {
//            rbc = s.getControl(RigidBodyControl.class);
//            if (rbc == null) {
//                continue;
//            }
//            if (distanceSquare(x, z, rbc.getPhysicsLocation().x, rbc.getPhysicsLocation().z) < radiusSquare) {
//                return false;
//            }
//        }
//        return true;
//    }
//    
//    private double distanceSquare(float x, float z, float otherX, float otherZ) {
//        double dx = x - otherX;
//        double dz = z - otherZ;
//        return dx * dx + dz * dz;
//    }
//    
//    /**
//     * 添加一个侦听器
//     * @param listener 
//     */
//    public void addSceneListener(SceneListener listener) {
//        if (sceneListeners == null) 
//            sceneListeners = new ArrayList<SceneListener>();
//        
//        if (!sceneListeners.contains(listener)) {
//            sceneListeners.add(listener);
//        }
//    }
//    
//    /**
//     * 删除场景侦听器
//     * @param listener
//     * @return 
//     */
//    public boolean removeSceneListener(SceneListener listener) {
//        return sceneListeners != null && sceneListeners.remove(listener);
//    }
//    
//    public void addSceneEnvListener(SceneEnvListener sceneEnvListener) {
//        if (sceneEnvListeners == null) 
//            sceneEnvListeners = new ArrayList<SceneEnvListener>();
//        
//        if (!sceneEnvListeners.contains(sceneEnvListener)) {
//            sceneEnvListeners.add(sceneEnvListener);
//        }
//    }
//    
//    public boolean removeSceneEnvListener(SceneEnvListener sceneEnvListener) {
//        return sceneEnvListeners != null && sceneEnvListeners.remove(sceneEnvListener);
//    }
//    
//    private void notifySceneEnvListenerInitialized(Env env) {
//        if (sceneEnvListeners != null) {
//            for (SceneEnvListener l : sceneEnvListeners) {
//                l.onSceneEnvInitialized(this, env);
//            }
//        }
//    }
//    
//    private void notifySceneEnvListenerAdded(Env env) {
//        if (sceneEnvListeners != null) {
//            for (SceneEnvListener l : sceneEnvListeners) {
//                l.onSceneEnvAdded(this, env);
//            }
//        }
//    }
//    
//    private void notifySceneEnvListenerRemoved(Env env) {
//        if (sceneEnvListeners != null) {
//            for (SceneEnvListener l : sceneEnvListeners) {
//                l.onSceneEnvRemoved(this, env);
//            }
//        }
//    }
//}
