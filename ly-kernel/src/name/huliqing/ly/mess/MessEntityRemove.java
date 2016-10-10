/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.game.Game;

/**
 * 删除一个场景实体
 * @author huliqing
 */
@Serializable
public class MessEntityRemove extends MessBase {
    
    private long entityId;

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        PlayService playService = Factory.get(PlayService.class);
        Game game = playService.getGame();
        Entity entity = game.getScene().getEntity(entityId);
        if (entity == null) {
            entity = game.getGuiScene().getEntity(entityId);
        }
        if (entity != null) {
            playService.removeEntity(entity);
        }
    }
    
}
