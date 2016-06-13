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
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.AbstractPlayObject;
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
    protected Env sky;
    protected Env terrain;
    protected Node localRoot;
    protected Spatial boundaryGeo;
    protected BulletAppState bulletAppState;
    // 直射光源
    protected DirectionalLight directionalLight;
    // 环境光源,格式"r,g,b,a"
    protected AmbientLight ambientLight;
    // 阴影处理器
    protected SceneProcessor shadowSceneProcessor;
    
    public Scene() {}
    
    @Override
    public void initialize() {
        super.initialize(); 
        localRoot = new Node("SceneRoot");
        
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
            localRoot.attachChild(boundaryGeo);
            addPhysicsObject(boundaryGeo);
        }
        
        // sky 天空盒
        if (data.getSky() != null) {
            sky = Loader.loadEnv(data.getSky());
            localRoot.attachChild(sky.getModel());
        }
        
        // terrain 地形
        if (data.getTerrain() != null) {
            terrain = Loader.loadEnv(data.getTerrain());
            localRoot.attachChild(terrain.getModel());
            addPhysicsObject(terrain.getModel());
        }
        
        // Lights
        if (data.getDirectionalLightColor() != null) {
            directionalLight = new DirectionalLight();
            directionalLight.setColor(data.getDirectionalLightColor());
            if (data.getDirectionalLightDir() != null) {
                directionalLight.setDirection(data.getDirectionalLightDir());
            }
            localRoot.addLight(directionalLight);
        }
        if (data.getAmbientLightColor() != null) {
            ambientLight = new AmbientLight();
            ambientLight.setColor(data.getAmbientLightColor());
            localRoot.addLight(ambientLight);
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
        
        // 直接添加到viewPort下
        playService.getApplication().getViewPort().attachScene(localRoot);
        localRoot.updateGeometricState();
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
        // LocalRoot是直接attach到ViewPort上去的，所以要这样移除。
        if (localRoot != null) {
            playService.getApplication().getViewPort().detachScene(localRoot);
            localRoot = null;
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
    
    public Spatial getRoot() {
        return localRoot;
    }

    public Spatial getTerrain() {
        return terrain != null ? terrain.getModel() : null;
    }

    public PhysicsSpace getPhysicsSpace() {
        return data.isUsePhysics() ? bulletAppState.getPhysicsSpace() : null;
    }
    
    /**
     * 添加节点到物理空间
     * @param s 
     */
    protected void addPhysicsObject(Spatial s) {
        if (!data.isUsePhysics() ||  bulletAppState == null || s == null)
            return;
        RigidBodyControl rbc = s.getControl(RigidBodyControl.class);
        if (rbc != null) {
            bulletAppState.getPhysicsSpace().add(rbc);
        }
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
     * 判断一个位置是否为空白区域，即无树木等障碍物之类。
     * @param x
     * @param z
     * @param radius
     * @return 
     */
    public abstract boolean checkIsEmptyZone(float x, float z, float radius);
}
