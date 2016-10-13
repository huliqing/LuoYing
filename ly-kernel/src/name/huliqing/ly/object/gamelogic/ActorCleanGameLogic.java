/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.gamelogic;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.LuoYing;
import name.huliqing.ly.Factory;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.constants.ActorConstants;
import name.huliqing.ly.data.GameLogicData;
import name.huliqing.ly.layer.network.PlayNetwork;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.entity.Entity;

/**
 * 场景清洁器,用于清理场景中已经死亡的角色之类的功能
 * @author huliqing
 * @param <T>
 */
public class ActorCleanGameLogic<T extends GameLogicData> extends AbstractGameLogic<T> {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    // 默认角色死亡后被清理出战场的时间
    private float cleanInterval = 10;
    
    // ---- inner
    private final List<Entity> temps = new ArrayList<Entity>();

    @Override
    public void setData(T data) {
        super.setData(data);
        cleanInterval = data.getAsFloat("cleanInterval", cleanInterval);
    }
    
    @Override
    protected void doLogic(float tpf) {
        List<Actor> actors = playService.getEntities(Actor.class, null);
        if (actors == null || actors.isEmpty())
            return;
        
        // 记录需要被清理的角色
        Long deadTime;
        for (Entity a : actors) {
            // “Player”、“未死亡”、“必要”的角色都不能移除
            if (!actorService.isDead(a) || actorService.isPlayer(a) || actorService.isEssential(a)) {
                a.getSpatial().getUserDataKeys().remove(ActorConstants.USER_DATA_DEAD_TIME_FLAG);
                continue;
            }
            deadTime = (Long) a.getSpatial().getUserData(ActorConstants.USER_DATA_DEAD_TIME_FLAG);
            if (deadTime == null) {
                a.getSpatial().setUserData(ActorConstants.USER_DATA_DEAD_TIME_FLAG, LuoYing.getGameTime());
            } else {
                if (LuoYing.getGameTime() - deadTime > cleanInterval * 1000) {
                    a.getSpatial().getUserDataKeys().remove(ActorConstants.USER_DATA_DEAD_TIME_FLAG);
                    temps.add(a);
                }
            }
        }
        
        // 清理角色
        if (!temps.isEmpty()) {
            for (Entity a : temps) {
                playNetwork.removeEntity(a);
            }
            temps.clear();
        }
    }
    
    @Override
    public void cleanup() {
        temps.clear();
        super.cleanup();
    }
}
