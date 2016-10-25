/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.env;

import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.NoneModelEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.object.scene.SceneListenerAdapter;

/**
 *
 * @author huliqing
 */
public class DirectionalLightShadowEnv extends NoneModelEntity{

    private int shadowMapSize = 1024;
    private int shadowMaps = 1;
    
    private DirectionalLightShadowRenderer shadowProcessor;
    private SceneListener sceneListener;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        shadowMapSize = data.getAsInteger("shadowMapSize", shadowMapSize);
        shadowMaps = data.getAsInteger("shadowMaps", shadowMaps);
    }
    
    @Override
    protected void initEntity() {
        shadowProcessor = new DirectionalLightShadowRenderer(LuoYing.getAssetManager(), shadowMapSize, shadowMaps);
//        shadowProcessor.setShadowZExtend(500f);
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        sceneListener = new SceneListenerAdapter() {
            @Override
            public void onSceneInitialized(Scene scene) {
                shadowProcessor.setLight(findDirectionalLight());
            }
        };
        if (scene.isInitialized()) {
            shadowProcessor.setLight(findDirectionalLight());
        } else {
            scene.addSceneListener(sceneListener);
        }
        scene.addProcessor(shadowProcessor);
    }

    @Override
    public void cleanup() {
        scene.removeProcessor(shadowProcessor);
        scene.removeSceneListener(sceneListener);
        shadowProcessor = null;
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
