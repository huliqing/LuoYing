/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.scene;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.post.SceneProcessor;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.loader.Loader;
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
    private final ConfigService configService = Factory.get(ConfigService.class);
    
    // ---- inner
    protected T data;
    // 直射光源
    protected DirectionalLight directionalLight;
    // 环境光源,格式"r,g,b,a"
    protected AmbientLight ambientLight;
    // 阴影处理器
    protected SceneProcessor shadowSceneProcessor;
    
    // 所有的环境物体列表，包含天空盒和地形
    protected List<Env> envs;
    
    // 场景根节点
    protected Node sceneRoot;
    // 地型，允许多个地形
    private Node terrainRoot;
    // 边界盒
    protected Spatial boundaryGeo;
    // 物理空间
    protected BulletAppState bulletAppState;
    
    public Scene() {}
    
    @Override
    public void initialize() {
        super.initialize(); 
        sceneRoot = new Node("SceneRoot");
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
            playService.getApplication().getStateManager().attach(bulletAppState);
            // 如果重力太大会发生画面抖动现象，不要高于40.
            // 注：setGravity必须放在attach(bulletAppState)之后
            bulletAppState.getPhysicsSpace().setGravity(data.getGravity());
            bulletAppState.setDebugEnabled(data.isDebugPhysics());
        }
        
        // boundary边界盒
        if (data.getBoundary() != null) {
            boundaryGeo = Loader.loadModel("Scenes/boundary/boundaryBox.j3o");
            boundaryGeo.setLocalScale(data.getBoundary().mult(2));
            boundaryGeo.setCullHint(Spatial.CullHint.Always);
            boundaryGeo.addControl(new RigidBodyControl(0));
            sceneRoot.attachChild(boundaryGeo);
            addPhysicsObject(boundaryGeo);
        }
        
        // Lights
        if (data.getDirectionalLightColor() != null) {
            directionalLight = new DirectionalLight();
            directionalLight.setColor(data.getDirectionalLightColor());
            if (data.getDirectionalLightDir() != null) {
                directionalLight.setDirection(data.getDirectionalLightDir());
            }
            sceneRoot.addLight(directionalLight);
        }
        if (data.getAmbientLightColor() != null) {
            ambientLight = new AmbientLight();
            ambientLight.setColor(data.getAmbientLightColor());
            sceneRoot.addLight(ambientLight);
        }
        
        // 影阴处理器
        if (data.isUseShadow() && configService.isUseShadow()) {
            shadowSceneProcessor = shadowSceneProcessor != null 
                    ? shadowSceneProcessor : createShadowProcessor();
            if (!playService.getApplication().getViewPort().getProcessors()
                    .contains(shadowSceneProcessor)) {
                playService.getApplication().getViewPort()
                        .addProcessor(shadowSceneProcessor);
            }
        }
        
        // env列表
        List<EnvData> edList = data.getEnvs();
        if (edList != null && !edList.isEmpty()) {
            envs = new ArrayList<Env>(edList.size());
            for (EnvData ed : edList) {
                Env env = DataFactory.createProcessor(ed);
                envs.add(env);
                env.initialize(this);
            }
        }
    }

    @Override
    public void cleanup() {
        if (bulletAppState != null) {
            playService.getApplication().getStateManager().detach(bulletAppState);
            bulletAppState = null;
        }
        if (shadowSceneProcessor != null) {
            playService.getApplication().getViewPort().removeProcessor(shadowSceneProcessor);
            shadowSceneProcessor = null;
        }
        
//        // LocalRoot是直接attach到ViewPort上去的，所以要这样移除。
//        if (sceneRoot != null) {
//            playService.getApplication().getViewPort().detachScene(sceneRoot);
//            sceneRoot = null;
//        }

        sceneRoot.removeFromParent();

        if (envs != null) {
            for (Env env : envs) {
                env.cleanup();
            }
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
    
    public Spatial getSceneRoot() {
        return sceneRoot;
    }

    public Spatial getTerrain() {
        return terrainRoot;
    }

    public PhysicsSpace getPhysicsSpace() {
        return data.isUsePhysics() ? bulletAppState.getPhysicsSpace() : null;
    }

    
    private SceneProcessor createShadowProcessor() {
        DirectionalLightShadowRenderer ssp = new DirectionalLightShadowRenderer(
                Common.getAssetManager(), 2048, 2);
        ssp.setLight(directionalLight != null ? directionalLight : new DirectionalLight());
        ssp.setShadowIntensity(0.3f);
        ssp.setShadowCompareMode(CompareMode.Hardware);
        ssp.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        return ssp;
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
    
    protected double distanceSquare(float x, float z, float otherX, float otherZ) {
        double dx = x - otherX;
        double dz = z - otherZ;
        return dx * dx + dz * dz;
    }
}
