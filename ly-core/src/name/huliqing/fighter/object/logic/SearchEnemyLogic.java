/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.logic;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.StateService;

/**
 * 逻辑：
 * 1.每隔一定时间自动搜寻可视范围内的敌人
 * 该逻辑不会有战斗行为或IDLE行为，需要和其它逻辑配合才有意义。
 * @author huliqing
 */
public class SearchEnemyLogic extends ActorLogic {
//    private final StateService stateService = Factory.get(StateService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);;
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);;
    
    // 自动频率，
    private boolean autoInterval = true;
    private float maxInterval = 3;
    private float minInterval = 1;

    public SearchEnemyLogic() {}
    
    public SearchEnemyLogic(LogicData data) {
        super(data);
        this.autoInterval = data.getProto().getAsBoolean("autoInterval", autoInterval);
        this.maxInterval = data.getProto().getAsFloat("maxInterval", maxInterval);
        this.minInterval = data.getProto().getAsFloat("minInterval", minInterval);
    }

    public boolean isAutoInterval() {
        return autoInterval;
    }

    public void setAutoInterval(boolean autoInterval) {
        this.autoInterval = autoInterval;
    }

    public float getMaxInterval() {
        return maxInterval;
    }

    public void setMaxInterval(float maxInterval) {
        this.maxInterval = maxInterval;
    }

    public float getMinInterval() {
        return minInterval;
    }

    public void setMinInterval(float minInterval) {
        this.minInterval = minInterval;
    }
    
    @Override
    protected void doLogic(float tpf) {
        
        // 只有打开了自动侦察功能才执行逻辑
        if (!actorService.isAutoDetect(self)) {
            return;
        }
        
        // 增加自动频率的逻辑
        Actor target = actorService.getTarget(self);
        
        if (target == null || target.isDead() || !target.isEnemy(self) 
                || !playService.isInScene(target)) {
            
            Actor enemy = actorService.findNearestEnemyExcept(self, actorService.getViewDistance(self), null);
            if (enemy != null) {
                actorNetwork.setTarget(self, enemy);
            }
            // 如果是自动间隔，则在有敌人时频率降低，在没有敌人时频率加大 
            if (autoInterval) {
                setInterval(enemy != null ? maxInterval : minInterval);
            }
        }
    }
    
}
