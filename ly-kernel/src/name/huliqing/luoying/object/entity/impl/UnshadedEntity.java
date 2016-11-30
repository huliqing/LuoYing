/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.service.SystemService;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * 这个实体用于将场景中指定类型的物体转化为unshaded方式的
 * @author huliqing
 */
public class UnshadedEntity extends NonModelEntity<EntityData> implements SceneListener {
    private final SystemService systemService = Factory.get(SystemService.class);

    // 是否转化地形为unshaded
    private boolean unshadedTerrain;
    private boolean unshadedPlant;
    private boolean unshadedActor;
    // 默认打开preferEnabled，但是可以指定只有在特定的平台上时才使用unshaded,在其它平台上不使用。
    private List<String> onlyPlatforms;
    
    // ---- 
    private boolean preferEnabled;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        unshadedTerrain = data.getAsBoolean("unshadedTerrain", false);
        unshadedPlant = data.getAsBoolean("unshadedPlant", false);
        unshadedActor = data.getAsBoolean("unshadedActor", false);
        onlyPlatforms = data.getAsStringList("onlyPlatforms");
    }
    
    @Override
    protected void initEntity() {
        String currentPlatform = systemService.getPlatformName();
        if (onlyPlatforms == null || onlyPlatforms.contains(currentPlatform)) {
            preferEnabled = true;
        } else {
            preferEnabled = false;
        }
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (scene.isInitialized()) {
            List<Entity> entities = scene.getEntities();
            for (Entity e : entities) {
                unshadedEntity(e);
            }
        }
        scene.addSceneListener(this);
    }

    @Override
    public void cleanup() {
        scene.removeSceneListener(this);
        super.cleanup(); 
    }
    
    @Override
    public void onSceneInitialized(Scene scene) {
        List<Entity> entities = scene.getEntities();
        for (Entity e : entities) {
            unshadedEntity(e);
        }
    }
    
    @Override
    public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
        unshadedEntity(entityAdded);
    }
    
    @Override
    public void onSceneEntityRemoved(Scene scene, Entity entityRemoved) {
        // ignore
    }
    
    private void unshadedEntity(Entity entity) {
        // 这一句是必须要的，因为data有可能在不同平台上传输，有可能从PC平台到移动平台，而preferEnabled的设置不一样。
        // 所以，当一个entity从PC到移动平台时，这里需要重新设置prefer参数。
        if (entity instanceof ModelEntity) {
            ((ModelEntity)entity).getData().setPreferUnshaded(preferEnabled);
        }
        
        if (preferEnabled) {
            boolean unshaded = false;
            if (unshadedTerrain &&  entity instanceof TerrainEntity) {
                unshaded = true;
            }
            if (unshadedPlant && entity instanceof PlantEntity) {
                unshaded = true;
            }
            if (unshadedActor && entity instanceof Actor) {
                unshaded = true;
            }
            if (unshaded) {
                GeometryUtils.makeUnshaded(entity.getSpatial());
            }
        }
    }
   
}
