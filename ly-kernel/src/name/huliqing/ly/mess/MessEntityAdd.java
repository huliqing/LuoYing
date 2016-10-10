/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.EntityData;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.game.Game;

/**
 *
 * @author huliqing
 */
@Serializable
public class MessEntityAdd extends MessBase {
    
    private long sceneId;
    private EntityData entityData;

    public long getSceneId() {
        return sceneId;
    }

    public void setSceneId(long sceneId) {
        this.sceneId = sceneId;
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
        Game game = playService.getGame();
        if (game.getScene().getData().getUniqueId() == sceneId) {
            playService.addEntity(game.getScene(), (Entity) Loader.load(entityData));
        } else if (game.getGuiScene().getData().getUniqueId() == sceneId) {
            playService.addEntity(game.getGuiScene(), (Entity) Loader.load(entityData));
        }
    }
    
}
