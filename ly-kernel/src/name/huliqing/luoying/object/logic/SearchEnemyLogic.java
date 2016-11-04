/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.ElService;
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
    
    // 自动频率，
    private boolean autoInterval = true;
    private float maxInterval = 3;
    private float minInterval = 1;
    
    // ---- inner
    private float viewDistanceSquared;

    @Override
    public void setData(LogicData data) {
        super.setData(data); 
        this.autoInterval = data.getProto().getAsBoolean("autoInterval", autoInterval);
        this.maxInterval = data.getProto().getAsFloat("maxInterval", maxInterval);
        this.minInterval = data.getProto().getAsFloat("minInterval", minInterval);

    }

    @Override
    public void initialize() {
        super.initialize(); 
        if (viewAttribute != null) {
            viewDistanceSquared = viewAttribute.floatValue() * viewAttribute.floatValue();
        }
    }
  
    @Override
    protected void doLogic(float tpf) {
        if (targetAttribute == null || viewAttribute == null) {
            return;
        }
        
        // 增加自动频率的逻辑
        // 目标不是角色
        Entity target = getTarget();
        if (!(target instanceof Actor)) {
            return;
        }
        
        // 已经从场景移除
        if (target.getScene() == null) {
            return;
        }
        
        // 目标在视线之外
        if (target.getSpatial().getWorldTranslation().distanceSquared(actor.getSpatial().getWorldTranslation()) > viewDistanceSquared) {
            return;
        }
        
        // 非敌人
        if (!isEnemy(target)) {
             // 自动间隔，在没有敌人时频率加快 
             if (autoInterval) {
                setInterval(minInterval);
             }
            return;
        }
        
        // <<<<找到敌人>>>>
        entityNetwork.hitAttribute(actor, targetAttribute.getName(), target.getEntityId(), null);
        
        // 自动间隔，在有敌人时频率降低
        if (autoInterval) {
            setInterval(maxInterval);
        }
    }
    
}
