/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.entity.Entity;

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
 */
public class NotifyLogic extends AbstractLogic {
//    private static final Logger LOG = Logger.getLogger(NotifyLogic.class.getName());
    
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    // 默认通知周围10码范围内的友军单位
    private float distance = 15;
    
    // 通知的频率,单位秒
    private final float notifyInterval = 15;
    
    // ---- inner
    // 临时缓存
    private final List<Actor> tempStore = new ArrayList<Actor>();
    private float notifyTimeUsed;
    
    @Override
    public void setData(LogicData data) {
        super.setData(data); 
        this.interval = 1;
        this.distance = data.getAsFloat("distance", distance);
    }

    @Override
    public void initialize() {
        super.initialize(); 
    }
    
    @Override
    protected void doLogic(float tpf) {
        // 通知的时间间隔
        notifyTimeUsed += tpf;
        if (notifyTimeUsed < notifyInterval) {
            return;
        }
        notifyTimeUsed = 0;
        
        // 无绑定目标属性
        if (targetAttribute == null) {
            return;
        }
        // 目标不是角色
        Entity target = getTarget();
        if (!(target instanceof Actor)) {
            return;
        }
        // 目标不是敌人
        if (!isEnemy(target)) {
            return;
        }
        // 查找友军单位并通知
        actor.getScene().getEntities(Actor.class, actor.getSpatial().getWorldTranslation(), distance, tempStore);
        for (Actor temp : tempStore) {
            if (isEnemy(temp)) {
                continue; // 不是友军
            }
            // 设置友军单位的目标对象
            entityNetwork.setAttribute(temp, targetAttribute.getName(), target.getEntityId());
        }
        // 清除列表
        tempStore.clear();
    }
    
    
}
