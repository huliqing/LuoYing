/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 向指定的场景添加实体
 * @author huliqing
 */
@Serializable
public class MessEntityAdd extends MessBase {
    
    // 向指定的场景ID添加实体
    private boolean guiScene;
    
    // 实体数据
    private EntityData entityData;

    public boolean isGuiScene() {
        return guiScene;
    }

    /**
     * 是否添加到GUI场景，默认情况下Entity将添加到主场景。
     * @param guiScene 
     */
    public void setGuiScene(boolean guiScene) {
        this.guiScene = guiScene;
    }

    public EntityData getEntityData() {
        return entityData;
    }

    public void setEntityData(EntityData entityData) {
        this.entityData = entityData;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        PlayService playService = Factory.get(PlayService.class);
        if (guiScene) {
            playService.addGuiEntity((Entity) Loader.load(entityData));
        } else {
            playService.addEntity((Entity) Loader.load(entityData));
        }
    }
    
}
