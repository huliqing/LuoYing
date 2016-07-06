/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.scene;

import com.jme3.app.Application;
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
    // 天空盒
    private final Node skyRoot = new Node("SkyRoot");
    // 地型，允许多个地形
    private final Node terrainRoot = new Node("terrainRoot");
    
    // 物理空间
    protected BulletAppState bulletAppState;
    
    public Scene() {}
    
    @Override
    public void initialize(Application app) {
        super.initialize(app); 
        sceneRoot = new Node("SceneRoot");
        sceneRoot.attachChild(skyRoot);
        sceneRoot.attachChild(terrainRoot);
        
        playService.addObject(sceneRoot);
//        app.getViewPort().attachScene(sceneRoot);
        
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
                env.initialize(app, this);
            }
        }
        
        sceneRoot.updateGeometricState();
    }

    @Override
    public void cleanup() {
       
        if (shadowSceneProcessor != null) {
            playService.getApplication().getViewPort().removeProcessor(shadowSceneProcessor);
            shadowSceneProcessor = null;
        }
        
        // LocalRoot是直接attach到ViewPort上去的，所以要这样移除。
        if (sceneRoot != null) {
//            playService.getApplication().getViewPort().detachScene(sceneRoot);
            sceneRoot.removeFromParent();
            sceneRoot = null;
        }

        skyRoot.detachAllChildren();
        terrainRoot.detachAllChildren();

        if (envs != null) {
            for (Env env : envs) {
                env.cleanup();
            }
        }
        if (bulletAppState != null) {
            playService.getApplication().getStateManager().detach(bulletAppState);
            bulletAppState = null;
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

    public List<Env> getEnvs() {
        return envs;
    }
    
    public Spatial getSceneRoot() {
        return sceneRoot;
    }
    
    public Node getSkyRoot() {
        return skyRoot;
    }
    
    /**
     * 把目标设置为sky,同时只能存在一个sky,如果已经存在其它SKY，则其它sky会被清除掉。
     * @param spatial 
     */
    public void setSky(Spatial spatial) {
        this.skyRoot.detachAllChildren();
        this.skyRoot.attachChild(spatial);
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
        sceneRoot.updateGeometricState();
        addPhysicsObject(spatial);
    }
    
    /**
     * 从场景中移除节点，除了将节点从场景移除之外，该方法还会偿试把节点中的RigidBodyControl也从物理空间中移除。
     * @param spatial 
     */
    public void removeSceneObject(Spatial spatial) {
        if (bulletAppState != null) {
            RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
            if (rbc != null) {
                bulletAppState.getPhysicsSpace().remove(rbc);
            }
        }
        spatial.removeFromParent();
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
