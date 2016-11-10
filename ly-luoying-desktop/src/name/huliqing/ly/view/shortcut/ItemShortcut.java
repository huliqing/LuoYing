/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.shortcut;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.EntityDataListener;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 用于普通物品(Item)的快捷方式
 * @author huliqing
 */
public class ItemShortcut extends BaseUIShortcut<ItemData> implements EntityDataListener {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
        
    @Override
    public void initialize() {
        super.initialize();
        actor.addEntityDataListener(this);
    }

    @Override
    public void cleanup() {
        actor.removeEntityDataListener(this);
        super.cleanup(); 
    }
    
    @Override
    public void removeObject() {
        entityNetwork.removeData(actor, objectData, objectData.getTotal());
    }
    
    @Override
    public void onShortcutClick(boolean pressed) {
        if (!pressed) {
            // 一些物品在执行前必须设置目标对象。
            // 注意：这个方法必须放在这里，playService.getTarget()是获取当前游戏主目标，是“玩家行为”，不能把它
            // 放到skillNetwork.playSkill中去。
            Entity target = gameService.getTarget();
            if (target != null) {
                gameNetwork.setTarget(actor, target.getEntityId());
            }
            entityNetwork.useData(actor, objectData);
        }
    }
    
    @Override
    public void onDataAdded(ObjectData data, int amount) {
        if (!data.getId().equals(objectData.getId()))
            return;
        
        updateObjectData(objectData);
    }

    @Override
    public void onDataRemoved(ObjectData data, int amount) {
        if (!data.getId().equals(objectData.getId()))
            return;
        
        updateObjectData(objectData);
    }

    @Override
    public void onDataUsed(ObjectData data) {
        // ignore
    }
    
    
}
