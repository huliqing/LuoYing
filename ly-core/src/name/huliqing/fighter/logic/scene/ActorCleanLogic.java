/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.logic.scene;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.constants.ActorConstants;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.object.IntervalLogic;

/**
 * 场景清洁器,用于清理场景中已经不需要的，或者已经死亡的角色之类的功能
 * @author huliqing
 */
public class ActorCleanLogic extends IntervalLogic {
    private final StateService stateService = Factory.get(StateService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    // 默认角色死亡后被清理出战场的时间
    private float clearTime = 5;
    
    private List<Actor> temps = new ArrayList<Actor>();

    public ActorCleanLogic() {
        super(5);
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
            if (!a.isDead() || a.isPlayer() || actorService.isEssential(a)) {
                a.getModel().getUserDataKeys().remove(ActorConstants.USER_DATA_DEAD_TIME_FLAG);
                continue;
            }
            deadTime = (Long) a.getModel().getUserData(ActorConstants.USER_DATA_DEAD_TIME_FLAG);
            if (deadTime == null) {
                a.getModel().setUserData(ActorConstants.USER_DATA_DEAD_TIME_FLAG, Common.getGameTime());
            } else {
                if (Common.getGameTime() - deadTime > clearTime * 1000) {
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

    public float getClearTime() {
        return clearTime;
    }
        
    /**
     * 设置角色死亡后经多少时间会被清理出场景，单位秒
     * @param clearTime 
     */
    public void setClearTime(float clearTime) {
        this.clearTime = clearTime;
    }
}
