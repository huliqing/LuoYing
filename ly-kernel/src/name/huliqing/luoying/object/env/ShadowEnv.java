/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.env;

import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.NoneModelEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.object.scene.SceneListenerAdapter;

/**
 * 阴影环境(目前不支持移动设备), 给场景添加这个环境后，可以让场景中的物体有阴影效果
 * @author huliqing
 */
public class ShadowEnv extends NoneModelEntity {
//    private final ConfigService configService = Factory.get(ConfigService.class);

    private float shadowIntensity = 0.7f;
    private int shadowMapSize = 1024;
    private int shadowMaps = 1;
    
    // ---- inner
    private DirectionalLightShadowFilter filter;
    private final SceneListener sceneListener = new SceneListenerAdapter() {
        @Override
        public void onSceneInitialized(Scene scene) {
            setupFilter();
            scene.removeSceneListener(this);
        }
    };
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        shadowIntensity = data.getAsFloat("shadowIntensity", shadowIntensity);
        shadowMapSize = data.getAsInteger("shadowMapSize", shadowMapSize);
        shadowMaps = data.getAsInteger("shadowMaps", shadowMaps);
    }
    
    @Override
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void initEntity() {
        filter = new DirectionalLightShadowFilter(LuoYing.getApp().getAssetManager(), shadowMapSize, shadowMaps);
        filter.setLambda(0.55f);
        filter.setShadowIntensity(shadowIntensity);
        filter.setShadowCompareMode(CompareMode.Hardware);
        filter.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
//        filter.setShadowZExtend(500);
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (scene.isInitialized()) {
            setupFilter();
        } else {
            scene.addSceneListener(sceneListener);
        }
    }
    
    private void setupFilter() {
        filter.setLight(findDirectionalLight());
        scene.addFilter(filter);
    }
    
    @Override
    public void cleanup() {
        scene.removeFilter(filter);
        scene.removeSceneListener(sceneListener);
        // 注意：这里要把filter设置为null以让系统释放内存，否则即使cleanup后，该filter内部使用中的frameBuffer仍然会
        // 占用内存(从stateAppState的debug中可以看到FrameBuffers(M)一下在增加)。
        // 这是一个特殊的情况，在其它Filter还没有发现这个问题。
        filter = null;
        super.cleanup();
    }
    
    private DirectionalLight findDirectionalLight() {
        // 找出当前场景中的第一个直射光
        LightList lightList = scene.getRoot().getLocalLightList();
        if (lightList.size() > 0) {
            for (int i = 0; i < lightList.size(); i++) {
                Light l = lightList.get(i);
                if (l instanceof DirectionalLight) {
                    return (DirectionalLight) l;
                }
            }
        }
        // 如果找不到任何光源，则创建一个默认的
        return new DirectionalLight();
    }
    
}
