/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.gamelogic;

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 用于修改场景中角色属性值的游戏逻辑，可用于如：为场景中所有角色提示生命值恢复，魔法值恢复，等。
 * 但是并不局限于恢复，还可用于减少。
 * @author huliqing
 * @param <T>
 */
public class AttributeChangeGameLogic<T extends GameLogicData> extends AbstractGameLogic<T> {
    private final PlayService playService = Factory.get(PlayService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
    private final EntityService entityService = Factory.get(EntityService.class);
    private final ElService elService = Factory.get(ElService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);

    // 指定要修改的角色的属性值,角色必须有这个属性，否则没有意义。
    private String applyAttribute;
    
    // 指定要影响最终修改值的目标角色的属性值。
    private String useAttribute;
    
    // 基本的值，用于修改角色的属性
    private float baseValue;
    
    // 速度值，值越大，改变的量越大。
    private float speed = 1.0f;
    
    // 这个EL用于判断一个角色是否可以应用于当前的逻辑。
    private SBooleanEl checkEl;
    
    // ---- inner
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        applyAttribute = data.getAsString("applyAttribute");
        useAttribute = data.getAsString("useAttribute");
        baseValue = data.getAsFloat("baseValue", baseValue);
        speed = data.getAsFloat("speed", speed);
//        applyToDead = data.getAsBoolean("applyToDead", applyToDead);
        checkEl = elService.createSBooleanEl(data.getAsString("checkEl", "#{true}"));

        // 不允许interval小于0
        if (interval <= 0) {
            interval = 1;
        }
    }
    
    @Override
    protected void doLogic(float tpf) {
        List<Actor> actors = playService.getEntities(Actor.class, null);
        if (actors == null || actors.isEmpty())
            return;
        
        for (Actor actor : actors) {
            if (checkEl.setSource(actor.getAttributeManager()).getValue()) {
                updateAttribute(actor);
            }
        }
    }
    
    private void updateAttribute(Entity actor) {
        // useAttribute是角色的已有属性，这个属性的值将影响最终的apply值。
        // 比如角色的属性（生命恢复速度)将影响这个游戏逻辑最终要修改角色生命值的属性。
        float useAttributeValue = entityService.getNumberAttributeValue(actor, useAttribute, 0);
        
        float applyValue = (baseValue + useAttributeValue) * interval * speed;
        
        // 注意：applyValue 有可能大于0或小于0,只有等于0时才没有意义（这里用一个接近0的值代替）
        if (Math.abs(applyValue) > 0.0001f) {
            entityNetwork.applyNumberAttributeValue(actor, applyAttribute, applyValue, null);
        }
    }
}
