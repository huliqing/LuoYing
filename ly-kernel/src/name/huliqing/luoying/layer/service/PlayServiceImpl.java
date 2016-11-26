/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public class PlayServiceImpl implements PlayService {
    private static final Logger LOG = Logger.getLogger(PlayServiceImpl.class.getName());
    
    // 当前游戏实例
    private static Game game;
    private ActionService actionService;
    
    @Override
    public void inject() {
        actionService = Factory.get(ActionService.class);
    }

    @Override
    public void registerGame(Game game) {
        PlayServiceImpl.game = game;
    }
    
    @Override
    public Game getGame() {
        if (game == null) {
            LOG.log(Level.WARNING, "Game not found or not registered! "
                    + "Use playService to registerGame or use GameAppState to attach to StateManager.");
        }
        return game;
    }

    @Override
    public void addEntity(Entity entity) {
        game.getScene().addEntity(entity);
    }
    
    @Override
    public void addGuiEntity(Entity entity) {
        game.getGuiScene().addEntity(entity);
    }
    
    // remove20161126
//    @Override
//    public void addEntity(Scene scene, Entity entity) {
//        scene.addEntity(entity);
//    }

    /**
     * 不要直接调用这个方法。
     * @param conn
     * @param entity 
     */
    @Override
    public void addEntityToClient(HostedConnection conn, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeEntity(Entity entity) {
        entity.getScene().removeEntity(entity);
    }

    @Override
    public Entity getEntity(long entityId) {
        Entity entity = game.getScene().getEntity(entityId);
        if (entity != null) {
            return entity;
        }
        return game.getGuiScene().getEntity(entityId);
    }

    @Override
    public <T extends Entity> List<T> getEntities(Class<T> type, List<T> store) {
        return game.getScene().getEntities(type, store);
    }
    
    @Override
    public float getScreenWidth() {
        return LuoYing.getSettings().getWidth();
    }

    @Override
    public float getScreenHeight() {
        return LuoYing.getSettings().getHeight();
    }

    @Override
    public Vector3f getTerrainHeight(Scene scene, float x, float z) {
        // 在场景载入完毕之后将植皮位置移到terrain节点的上面。
        List<TerrainEntity> sos = scene.getEntities(TerrainEntity.class, new ArrayList<TerrainEntity>());
        Vector3f result = null;
        for (TerrainEntity terrain : sos) {
            Vector3f tp = terrain.getHeight(x, z);
            if (tp != null) {
                if (result == null || tp.y > result.y) {
                    result = tp;
                }
            }
        }
        return result;
    }
    
    @Override
    public void attack(Entity actor, Entity target) {
        // remove20161102
//         // 如果角色已经死亡
//        if (actorService.isDead(actor)) {
//            return;
//        }
        
        // 执行战斗行为
        if (target != null) {
            actionService.playFight(actor, target, null);
        }

        // remove20161103
//        // 打开或关闭侦察敌人的逻辑,autoDetect不需要广播到客户端，因为客户端不会有
//        // 逻辑
//        logicService.setAutoDetect(actor, skinService.isWeaponTakeOn(actor));
    }
    
    @Override
    public void updateRandomSeed() {
        // 不实现这个方法的原因是：因为单独在服务端或客户端直接调用这个方法(PlayService.updateRandomSeed())可能
        // 会导致服务端和客户端的随机种子不一致。
        throw new UnsupportedOperationException("Do not use this method, use PlayNetwork.updateRandomSeed instead!");
    }
    
}
