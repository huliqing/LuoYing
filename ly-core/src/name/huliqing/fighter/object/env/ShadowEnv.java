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
public class ShadowEnv <T extends EnvData> extends Env <T> implements Scene.Listener{
    private final ConfigService configService = Factory.get(ConfigService.class);

    private float shadowIntensity = 0.7f;
    private int shadowMapSize = 1024;
    private int shadowMaps = 1;
    
    // ---- inner
    private Application app;
    private DirectionalLightShadowFilter filter;
    
    @Override
    public void initData(T data) {
        super.initData(data);
        shadowIntensity = data.getAsFloat("shadowIntensity", shadowIntensity);
        shadowMapSize = data.getAsInteger("shadowMapSize", shadowMapSize);
        shadowMaps = data.getAsInteger("shadowMaps", shadowMaps);
    }

    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        this.app = app;
        scene.addListener(this);
    }

    @Override
    public void cleanup() {
        if (scene != null) {
            scene.removeFilter(filter);
            scene.removeListener(this);
        }
        super.cleanup();
    }

    @Override
    public void onInitialized(Scene scene) {
        // 影阴处理器
        if (scene.getData().isUseShadow() && configService.isUseShadow()) {
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
    public void onAdded(Scene scene, Spatial objectAdded) {
        // ignore
    }

    @Override
    public void onRemoved(Scene scene, Spatial objectRemoved) {
        // ignore
    }
    
}
