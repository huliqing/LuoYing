/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActorModule;

/**
 * 这是一个通知逻辑，若当角色存在目标敌人时，会通知周围一定范围内的友军单位进
 * 行攻击目标，比如当前角色受到攻击时，会通知同伴友军进行攻击目标。具体逻辑：
 * 1.当前角色存在目标, 该目标必须是敌方,该目标未死亡
 * 2.该逻辑会在当前角色周围一定范围内（可设置）查找还没有目标的友军单位。
 * 3.通过设置可强制周围友军将目标都锁定在与当前角色相同的目标上,即使友军已经
 * 存在目标。
 * 注意:该逻辑不应该随便滥用,一般只用在主角,或敌军主力,队长,侦察兵之类,随便滥用会造成循环调用和冲突,资源浪费.
 * 这个逻辑的频率可以不用太高.一般应该高于或等于1秒.如果需要在某一方使用多个当前逻辑,则建议最多只让其中一个逻辑的force属性
 * 设置为true就可以,避免ActorNotifyLogic之间的冲突.
 * @author huliqing
 * @param <T>
 */
public class NotifyLogic<T extends LogicData> extends Logic<T> {
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private ActorModule actorModule;
    
    // 默认通知周围10码范围内的友军单位
    private float distance = 15;
    
    // 是否强制通知周围的友军单位,如果打开该选项,则不管周围友军是否存在目标,都
    // 会被重置为与当前角色目标一致的目标.
    private boolean force;
    // 临时缓存
    private final List<Actor> tempStore = new ArrayList<Actor>();
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        this.interval = 1;
        this.distance = data.getAsFloat("distance", distance);
        this.force = data.getAsBoolean("force", force);
    }

    @Override
    public void setActor(Entity self) {
        super.setActor(self);
        actorModule = self.getEntityModule().getModule(ActorModule.class);
    }
    
    @Override
    protected void doLogic(float tpf) {
        Entity target = actorModule.getTarget();
        if (target == null) {
            return;
        }
        ActorModule targetActorModule = target.getEntityModule().getModule(ActorModule.class);
        if (actorModule.isEnemy(target) && !targetActorModule.isDead()) {
            tempStore.clear();
            findNearestFriendly(actor, distance, tempStore);
            if (!tempStore.isEmpty()) {
                Entity fTarget;
                for (Entity friend : tempStore) {
                    fTarget = actorService.getTarget(friend);
                    if (force 
                            // 以下:如果友军单位没有存在为"敌人"的目标,则可重置它的目标.
                            || fTarget == null || !actorService.isEnemy(fTarget, friend)) {
                        actorNetwork.setTarget(friend, target);
                    }
                }
            }
            // 通知后释放当前目标,避免一直通知
            actorNetwork.setTarget(actor, null);
            
            // 不要去维持这个列表
            tempStore.clear();
        }
    }
    
    private List<Actor> findNearestFriendly(Entity actor, float maxDistance, List<Actor> store) {
        List<Actor> actors = actor.getScene().getEntities(Actor.class, actor.getSpatial().getWorldTranslation(), maxDistance, tempStore);
//        ActorModule actorModule = actor.getEntityModule().getModule(ActorModule.class);
        ActorModule targetActorModule;
        for (Actor a : actors) {
            targetActorModule = a.getEntityModule().getModule(ActorModule.class);
            if (targetActorModule.isDead() || targetActorModule.isEnemy(actor)) {
                continue;
            }
            if (targetActorModule.getGroup() == actorModule.getGroup()) {
                store.add(a);
            }
        }
        return store;
    }
    
}
