/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.xml.DataFactory;

/**
 * @author huliqing
 */
public class ItemModule extends AbstractModule {
    
    private Actor actor;
    // 监听角色物品的增删
    private List<ItemListener> itemListeners;

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor); 
        this.actor = actor;
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    /**
     * 添加物品侦听器
     * @param itemListener 
     */
    public void addItemListener(ItemListener itemListener) {
        if (itemListeners == null) {
            itemListeners = new ArrayList<ItemListener>();
        }
        if (!itemListeners.contains(itemListener)) {
            itemListeners.add(itemListener);
        }
    }
    
    /**
     * 删除物品侦听器
     * @param itemListener
     * @return 
     */
    public boolean removeItemListener(ItemListener itemListener) {
        return itemListeners != null && itemListeners.remove(itemListener);
    }
    
    public boolean addItem(String itemId, int amount) {
        if (amount <= 0) {
            return false;
        }
        ItemData item = getItem(itemId);
        if (item == null) {
            item = DataFactory.createData(itemId);
            item.setTotal(amount);
            actor.getData().addObjectData(item);
        } else {
            item.increaseTotal(amount);
        }
        
        // 触发侦听器
        if (itemListeners != null) {
            for (int i = 0; i < itemListeners.size(); i++) {
                itemListeners.get(i).onItemAdded(actor, item, amount);
            }
        }
        
        return true;
    }
    
    /**
     * 删除物品
     * @param itemId 物品id
     * @param amount 要删除的数量，不能小于0
     * @return 
     */
    public boolean removeItem(String itemId, int amount) {
        if (itemId == null || amount <= 0) {
            return false;
        }
        ItemData item = getItem(itemId);
        if (item == null || !item.isDeletable())
            return false;
        
        int oldTotal = item.getTotal();
        int trueRemoved;
        item.increaseTotal(-1 * amount);
        if (item.getTotal() <= 0) {
            actor.getData().getObjectDatas().remove(item);
            trueRemoved = oldTotal;
        } else {
            trueRemoved = oldTotal - item.getTotal();
        }
        
        // 触发侦听器
        if (itemListeners != null) {
            for (int i = 0; i < itemListeners.size(); i++) {
                itemListeners.get(i).onItemRemoved(actor, item, trueRemoved);
            }
        }
        
        return true;
    }
    
    /**
     * 获取指定ID的物品
     * @param itemId
     * @return 
     */
    public ItemData getItem(String itemId) {
        return actor.getData().getObjectData(itemId);
    }
    
     /**
     * 获取所有物品列表,如果没有任何物品则返回null
     * 注意: 不要手动移除列表中的物品.移除物品应该使用：
     * {@link #removeItem(name.huliqing.fighter.data.ProtoData) }方法
     * @return 
     */
    public List<ItemData> getItems() {
        return actor.getData().getObjectDatas(ItemData.class, null);
    }
    

    
    
}
