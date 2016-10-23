/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.shortcut;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.network.ItemNetwork;
import name.huliqing.luoying.layer.service.ItemService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.item.Item;
import name.huliqing.luoying.object.module.ItemListener;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 用于普通物品(Item)的快捷方式
 * @author huliqing
 */
public class ItemShortcut extends BaseUIShortcut<ItemData> implements ItemListener {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    private final ItemNetwork itemNetwork = Factory.get(ItemNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
        
    @Override
    public void initialize() {
        super.initialize();
        itemService.addItemListener(actor, this);
    }

    @Override
    public void cleanup() {
        itemService.removeItemListener(actor, this);
        super.cleanup(); 
    }

    @Override
    public void removeObject() {
        itemNetwork.removeItem(actor, objectData.getId(), objectData.getTotal());
    }
    
    @Override
    public void onShortcutClick(boolean pressed) {
        if (!pressed) {
            // 一些物品在执行前必须设置目标对象。
            // 注意：这个方法必须放在这里，playService.getTarget()是获取当前游戏主目标，是“玩家行为”，不能把它
            // 放到skillNetwork.playSkill中去。
            actorNetwork.setTarget(actor, gameService.getTarget());
            
            itemNetwork.useItem(actor, objectData.getId());
        }
    }
    
    @Override
    public void onItemAdded(Entity actor, Item item, int trueAdded) {
        if (!item.getId().equals(objectData.getId()))
            return;
        
        updateObjectData(item.getData());
    }

    @Override
    public void onItemRemoved(Entity actor, Item item, int trueRemoved) {
        if (!item.getId().equals(objectData.getId()))
            return;
        
        updateObjectData(item.getData());
    }

    @Override
    public void onItemUsed(Entity source, Item item) {
        if (!item.getId().equals(objectData.getId()))
            return;
        
        updateObjectData(item.getData());
    }
    

}
