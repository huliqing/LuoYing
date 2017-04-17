/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.module;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.item.Item;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 物品模块，该模块主要用于让实体具有“包裹”功能，给实体添加这个模块之后，实体就拥有了“包裹”，
 * 这样就可以向实体添加、删除、使用物品（ItemData).
 * @author huliqing
 */
public class ItemModule extends AbstractModule {
    
    /**
     * 监听角色物品的增加及删除
     *
     * @author huliqing
     * @deprecated Use EntityDataListener instead
     */
    public interface ItemListener {

        /**
         * 监听角色物品添加，当角色包裹获得物品时该方法被调用。
         * @param source 源角色
         * @param item 新添加的物品
         * @param trueAdded 实际的添加数量
         */
        void onItemAdded(Entity source, Item item, int trueAdded);

        /**
         * 监听角色的物品删除,当角色包裹中的物品被移除时该方法被调用。
         * @param source 源角色
         * @param item 被删除的物品
         * @param trueRemoved 实际的删除数量
         */
        void onItemRemoved(Entity source, Item item, int trueRemoved);

        /**
         * 当角色使用了物品之后该方法被调用
         * @param source
         * @param item 被使用的物品
         */
        void onItemUsed(Entity source, Item item);
    }
    
    private List<ItemData> items;
    
    @Override
    public void initialize() {
        super.initialize();
         // 从角色身上取出ItemData类型数据（不取出也可以，但是把数据拿出来放在这里比较高效。）
        List<ItemData> tempDatas = entity.getData().getObjectDatas(ItemData.class, new ArrayList<ItemData>());
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
    public boolean handleDataAdd(ObjectData handleData, int count) {
        if (!(handleData instanceof ItemData)) 
            return false;
        
        if (count <= 0) 
            return false;
        
        ItemData item = find(handleData.getId());
        if (item == null) {
            item = (ItemData) handleData;
            item.setTotal(count);
            if (items == null) {
                items = new ArrayList<ItemData>();
            }
            items.add(item);
            entity.getData().addObjectData(item);
        } else {
            item.setTotal(item.getTotal() + count);
        }
        addEntityDataAddMessage(StateCode.DATA_ADD, handleData, count);
        return true;
    }
    
    @Override
    public boolean handleDataRemove(ObjectData handleData, int count) {
        if (!(handleData instanceof ItemData)) 
            return false;
        
        if (count <= 0)
            return false;
        
        ItemData item = find(handleData.getId());
        
        if (item == null) {
            addEntityDataRemoveMessage(StateCode.DATA_REMOVE_FAILURE_NOT_FOUND, handleData, count);
            return false;
        }
        
        if (!item.isDeletable()) {
            addEntityDataRemoveMessage(StateCode.DATA_REMOVE_FAILURE_UN_DELETABLE, handleData, count);
            return false;
        }
        
        item.setTotal(item.getTotal() - count);
        if (item.getTotal() <= 0) {
            items.remove(item);
            entity.getData().getObjectDatas().remove(item);
        }
        addEntityDataRemoveMessage(StateCode.DATA_REMOVE, handleData, count);
        return true;
    }

    @Override
    public boolean handleDataUse(ObjectData handleData) {
        if (!(handleData instanceof ItemData)) 
            return false;
        
        Item item = Loader.load(handleData);
        
        int stateCode = item.checkStateCode(entity);
        
        // 输出消息,不管物品使用是否成功都应该会有一个状态码
        addEntityDataUseMessage(stateCode, handleData);
        
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
