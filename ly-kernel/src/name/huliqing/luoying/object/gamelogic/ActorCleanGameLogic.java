/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.gamelogic;

import java.util.ArrayList;
import java.util.List;
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
 * @param <T>
 */
public class ActorCleanGameLogic<T extends GameLogicData> extends AbstractGameLogic<T> {
//    private final ActorService actorService = Factory.get(ActorService.class);
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
    private final List<Actor> actorStore = new ArrayList<Actor>();
    private final List<Entity> removeStore = new ArrayList<Entity>();
    // 将deathDelay转为毫秒
    private float deathDelayAsMS;

    public ActorCleanGameLogic() {
        super(10); // 默认每10秒检查一次战场景进行清理。
    }
    
    @Override
    public void setData(T data) {
        super.setData(data);
        checkEl = elService.createSBooleanEl(data.getAsString("checkEl", "#{false}"));
        deathDelay = data.getAsFloat("deathDelay", deathDelay);
    }

    @Override
    public void initialize(Game game) {
        super.initialize(game); 
        deathDelayAsMS = deathDelay * 1000;
    }
    
    @Override
    protected void doLogic(float tpf) {
        playService.getEntities(Actor.class, actorStore);
        if (actorStore.isEmpty())
            return;
        
        // 记录需要被清理的角色
        Long deadTime;
        for (Entity a : actorStore) {
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
            for (Entity a : removeStore) {
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
