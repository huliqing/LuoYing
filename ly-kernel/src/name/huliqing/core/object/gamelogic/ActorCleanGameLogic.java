/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.gamelogic;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.constants.ActorConstants;
import name.huliqing.core.data.GameLogicData;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.PlayService;

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
    private float cleanInterval = 5;
    
    // ---- inner
    private final List<Actor> temps = new ArrayList<Actor>();

    @Override
    public void setData(T data) {
        super.setData(data);
        cleanInterval = data.getAsFloat("cleanInterval", cleanInterval);
    }
    
    @Override
    protected void doLogic(float tpf) {
        List<Actor> actors = playService.findAllActor();
        if (actors == null || actors.isEmpty())
            return;
        
        // 记录需要被清理的角色
        Long deadTime;
        for (Actor a : actors) {
            // “Player”、“未死亡”、“必要”的角色都不能移除
            if (!actorService.isDead(a) || actorService.isPlayer(a) || actorService.isEssential(a)) {
                a.getModel().getUserDataKeys().remove(ActorConstants.USER_DATA_DEAD_TIME_FLAG);
                continue;
            }
            deadTime = (Long) a.getModel().getUserData(ActorConstants.USER_DATA_DEAD_TIME_FLAG);
            if (deadTime == null) {
                a.getModel().setUserData(ActorConstants.USER_DATA_DEAD_TIME_FLAG, LY.getGameTime());
            } else {
                if (LY.getGameTime() - deadTime > cleanInterval * 1000) {
                    a.getModel().getUserDataKeys().remove(ActorConstants.USER_DATA_DEAD_TIME_FLAG);
                    temps.add(a);
                }
            }
        }
        
        // 清理角色
        if (!temps.isEmpty()) {
            for (Actor a : temps) {
                playNetwork.removeObject(a.getModel());
            }
            temps.clear();
        }
    }
    
    @Override
    public void cleanup() {
        temps.clear();
        super.cleanup();
    }

    // remove20160716
//    public float getClearTime() {
//        return cleanInterval;
//    }
//        
//    /**
//     * 设置角色死亡后经多少时间会被清理出场景，单位秒
//     * @param clearTime 
//     */
//    public void setClearTime(float clearTime) {
//        this.cleanInterval = clearTime;
//    }

}
