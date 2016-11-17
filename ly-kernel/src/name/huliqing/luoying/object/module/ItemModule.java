/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.DataHandler;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.item.Item;

/**
 * @author huliqing
 */
public class ItemModule extends AbstractModule implements DataHandler<ItemData> {
    
    private List<ItemData> items;

    @Override
    public void updateDatas() {
        // xxx updateDatas.
    }
    
    @Override
    public void initialize(Entity actor) {
        super.initialize(actor);
        
         // 从角色身上取出ItemData类型数据（不取出也可以，但是把数据拿出来放在这里比较高效。）
        List<ItemData> tempDatas = actor.getData().getObjectDatas(ItemData.class, null);
        if (tempDatas != null && !tempDatas.isEmpty()) {
            items = new ArrayList<ItemData>(tempDatas.size());
            for (ItemData itemData : tempDatas) {
                items.add(itemData);
            }
        }
    }
    
    @Override
    public void cleanup() {
        if (items != null) {
            items.clear();
        }
        super.cleanup();
    }
    
    @Override
    public final Class<ItemData> getHandleType() {
        return ItemData.class;
    }
    
    @Override
    public boolean handleDataAdd(ItemData data, int count) {
        if (count <= 0) 
            return false;
        
        ItemData item = find(data.getId());
        if (item == null) {
            item = data;
            item.setTotal(count);
            if (items == null) {
                items = new ArrayList<ItemData>();
            }
            items.add(item);
            entity.getData().addObjectData(item);
        } else {
            item.setTotal(item.getTotal() + count);
        }
        return true;
    }
    
    @Override
    public boolean handleDataRemove(ItemData data, int count) {
        if (count <= 0)
            return false;
        
        ItemData item = find(data.getId());
        
        if (item == null)
            return false;
        
        if (!item.isDeletable())
            return false;
        
        item.setTotal(item.getTotal() - count);
        if (item.getTotal() <= 0) {
            items.remove(item);
            entity.getData().getObjectDatas().remove(item);
        }
        return true;
    }

    @Override
    public boolean handleDataUse(ItemData data) {
        Item item = Loader.load(data);
        item.use(entity);
        return true;
    }
    
    private ItemData find(String itemId) {
        if (items == null) 
            return null;
        for (ItemData item : items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }
   
}
