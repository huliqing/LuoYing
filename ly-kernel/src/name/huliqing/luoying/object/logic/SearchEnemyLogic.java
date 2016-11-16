/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 逻辑：
 * 1.每隔一定时间自动搜寻可视范围内的敌人
 * 该逻辑不会有战斗行为或IDLE行为，需要和其它逻辑配合才有意义。
 * @author huliqing
 */
public class SearchEnemyLogic extends AbstractLogic {
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    // ---- inner
    private final List<Actor> tempStore = new ArrayList<Actor>();
    // 最近一个找到的敌人,避免频繁的去查找敌人。
    private Entity lastFindEnemy;
    
    private boolean isAvailableEnemy(Entity target) {
        if (target == null // 目标不存在
                || target.getScene() == null // 目标已经移出场景
                || !(target instanceof Actor)) {  // 目标不是角色
            return false;
        }
        return isEnemy(target);
    }
  
    @Override
    protected void doLogic(float tpf) {
        if (targetAttribute == null || viewAttribute == null) {
            return;
        }
        
        // 如果最近找到的敌人仍然有效，则不需要重新查找。
        if (lastFindEnemy != null 
                && lastFindEnemy.getEntityId() == targetAttribute.longValue()
                && isAvailableEnemy(lastFindEnemy)) {
            return;
        }
        
        // 当前目标是有效的乱人, 记住就可以，不需要再去场景中查找
        Entity target = getTarget();
        if (isAvailableEnemy(target)) {
            lastFindEnemy = target;
            return;
        }
        
        // 需要偿试查找敌人
        tempStore.clear();
        actor.getScene().getEntities(Actor.class, actor.getSpatial().getWorldTranslation(), getViewDistance(), tempStore);
        if (!tempStore.isEmpty()) {
            for (Actor a : tempStore) {
                if (isAvailableEnemy(a)) {
                    lastFindEnemy = a;
                    break;
                }
            }
        }
        
        // <<<<找到敌人>>>>
        if (lastFindEnemy != null) {
            entityNetwork.hitAttribute(actor, targetAttribute.getName(), lastFindEnemy.getEntityId(), null);
        }

    }
    
}
