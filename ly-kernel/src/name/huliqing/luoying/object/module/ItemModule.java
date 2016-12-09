/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.message.StateCode;
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
        List<ItemData> tempDatas = actor.getData().getObjectDatas(ItemData.class, new ArrayList<ItemData>());
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
        addEntityDataAddMessage(StateCode.DATA_ADD, data, count);
        return true;
    }
    
    @Override
    public boolean handleDataRemove(ItemData data, int count) {
        if (count <= 0)
            return false;
        
        ItemData item = find(data.getId());
        
        if (item == null) {
            addEntityDataRemoveMessage(StateCode.DATA_REMOVE_FAILURE_NOT_FOUND, data, count);
            return false;
        }
        
        if (!item.isDeletable()) {
            addEntityDataRemoveMessage(StateCode.DATA_REMOVE_FAILURE_UN_DELETABLE, data, count);
            return false;
        }
        
        item.setTotal(item.getTotal() - count);
        if (item.getTotal() <= 0) {
            items.remove(item);
            entity.getData().getObjectDatas().remove(item);
            addEntityDataRemoveMessage(StateCode.DATA_REMOVE, data, count);
        }
        return true;
    }

    @Override
    public boolean handleDataUse(ItemData data) {
        Item item = Loader.load(data);
        
        int stateCode = item.checkStateCode(entity);
        
        // 输出消息,不管物品使用是否成功都应该会有一个状态码
        addEntityDataUseMessage(stateCode, data);
        
        if (stateCode == StateCode.DATA_USE) {
            item.use(entity);
        }
        
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
