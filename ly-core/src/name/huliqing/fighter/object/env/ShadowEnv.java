/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.scene.Spatial;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.object.scene.Scene;

/**
 * 阴影处理器,目前不支持移动设备.
 * @author huliqing
 * @param <T>
 */
public class ShadowEnv <T extends EnvData> extends AbstractEnv <T> implements Scene.SceneListener{
    private final ConfigService configService = Factory.get(ConfigService.class);

    private float shadowIntensity = 0.7f;
    private int shadowMapSize = 1024;
    private int shadowMaps = 1;
    
    // ---- inner
    private Application app;
    private DirectionalLightShadowFilter filter;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        shadowIntensity = data.getAsFloat("shadowIntensity", shadowIntensity);
        shadowMapSize = data.getAsInteger("shadowMapSize", shadowMapSize);
        shadowMaps = data.getAsInteger("shadowMaps", shadowMaps);
    }

    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        this.app = app;
        scene.addSceneListener(this);
    }

    @Override
    public void cleanup() {
        scene.removeFilter(filter);
        scene.removeSceneListener(this);
        // 注意：这里要把filter设置为null以让系统释放内存，否则即使cleanup后，该filter内部使用中的frameBuffer仍然会
        // 占用内存(从stateAppState的debug中可以看到FrameBuffers(M)一下在增加)。
        // 这是一个特殊的情况，在其它Filter还没有发现这个问题。
        filter = null;
        super.cleanup();
    }

    @Override
    public void onSceneInitialized(Scene scene) {
        // 影阴处理器
        if (configService.isUseShadow()) {
            filter = new DirectionalLightShadowFilter(app.getAssetManager(), shadowMapSize, shadowMaps);
            filter.setLight(findLight());
            filter.setLambda(0.55f);
            filter.setShadowIntensity(shadowIntensity);
            filter.setShadowCompareMode(CompareMode.Hardware);
            filter.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
            scene.addFilter(filter);
        }
    }
    
    private DirectionalLight findLight() {
        // 找出当前场景中的第一个直射光
        DirectionalLight light = null;
        LightList lightList = scene.getSceneRoot().getLocalLightList();
        if (lightList.size() > 0) {
            for (int i = 0; i < lightList.size(); i++) {
                Light l = lightList.get(i);
                if (l instanceof DirectionalLight) {
                    light = (DirectionalLight) l;
                    break;
                }
            }
        }
        return light;
    }
    
    @Override
    public void onSceneObjectAdded(Scene scene, Spatial objectAdded) {
        // ignore
    }

    @Override
    public void onSceneObjectRemoved(Scene scene, Spatial objectRemoved) {
        // ignore
    }
    
}
