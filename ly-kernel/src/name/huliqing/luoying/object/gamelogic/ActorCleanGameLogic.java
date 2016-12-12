/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.gamelogic;

import com.jme3.util.SafeArrayList;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;

/**
 * 场景清洁器,用于清理场景中已经死亡的角色之类的功能
 * @author huliqing
 */
public class ActorCleanGameLogic extends AbstractGameLogic {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ElService elService = Factory.get(ElService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    /**
     * USER_Data的死亡时间标记,角色死亡后会被标记.
     */
    private final static String USER_DATA_DEAD_TIME_FLAG = "DEAD_TIME_FLAG";
    
    // 这条表达式用于判断哪些角色可以清理
    private SBooleanEl checkEl;
    // 默认角色死亡后被清理出战场的时间延迟
    private float deathDelay = 60;
    
    // ---- inner
    private final SafeArrayList<Actor> actorStore = new SafeArrayList<Actor>(Actor.class);
    private final SafeArrayList<Entity> removeStore = new SafeArrayList<Entity>(Entity.class);
    // 将deathDelay转为毫秒
    private float deathDelayAsMS;

    public ActorCleanGameLogic() {
        super(10); // 默认每10秒检查一次战场景进行清理。
    }
    
    @Override
    public void setData(GameLogicData data) {
        super.setData(data); 
        checkEl = elService.createSBooleanEl(data.getAsString("checkEl", "#{false}"));
        deathDelay = data.getAsFloat("deathDelay", deathDelay);
        deathDelayAsMS = deathDelay * 1000;
    }

    @Override
    protected void logicInit(Game game) {}
    
    @Override
    protected void logicUpdate(float tpf) {
        playService.getEntities(Actor.class, actorStore);
        if (actorStore.isEmpty())
            return;
        
        // 记录需要被清理的角色
        Long deadTime;
        for (Entity a : actorStore.getArray()) {
            if (checkEl.setSource(a.getAttributeManager()).getValue()) {
                deadTime = (Long) a.getSpatial().getUserData(USER_DATA_DEAD_TIME_FLAG);
                if (deadTime == null) {
                    a.getSpatial().setUserData(USER_DATA_DEAD_TIME_FLAG, LuoYing.getGameTime());
                } else {
                    if (LuoYing.getGameTime() - deadTime > deathDelayAsMS) {
                        a.getSpatial().getUserDataKeys().remove(USER_DATA_DEAD_TIME_FLAG);
                        removeStore.add(a);
                    }
                }
            }
        }
        
        // 清理角色
        if (!removeStore.isEmpty()) {
            for (Entity a : removeStore.getArray()) {
                playNetwork.removeEntity(a);
            }
            removeStore.clear();
        }
        actorStore.clear();
    }
    
    @Override
    public void cleanup() {
        actorStore.clear();
        removeStore.clear();
        super.cleanup();
    }
}
