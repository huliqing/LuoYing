/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;

/**
 * 删除一个场景实体
 * @author huliqing
 */
@Serializable
public class EntityRemoveMess extends GameMess {
    
    private long entityId;

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    @Override
    public void applyOnClient(GameClient gameClient) {
        super.applyOnClient(gameClient);
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
