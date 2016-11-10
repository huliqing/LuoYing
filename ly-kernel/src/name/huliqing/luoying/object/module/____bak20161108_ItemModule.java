///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.object.module;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import name.huliqing.luoying.data.ItemData;
//import name.huliqing.luoying.object.Loader;
//import name.huliqing.luoying.object.entity.Entity;
//import name.huliqing.luoying.object.item.Item;
//
///**
// * @author huliqing
// */
//public class ItemModule extends AbstractModule {
//    
//    // 监听角色物品的增删
//    private List<ItemListener> itemListeners;
//    
//    private List<Item> items;
//
//    @Override
//    public void updateDatas() {
//        // xxx updateDatas.
//    }
//    
//    @Override
//    public void initialize(Entity actor) {
//        super.initialize(actor);
//        
//         // 载入技能
//        List<ItemData> itemDatas = actor.getData().getObjectDatas(ItemData.class, null);
//        if (itemDatas != null && !itemDatas.isEmpty()) {
//            items = new ArrayList<Item>(itemDatas.size());
//            for (ItemData itemData : itemDatas) {
//                Item item = Loader.load(itemData);
//                items.add(item);
//            }
//        }
//    }
//    
//    @Override
//    public void cleanup() {
//        if (items != null) {
//            items.clear();
//        }
//        super.cleanup();
//    }
//    
//    public void addItem(String itemId, int amount) {
//        if (amount <= 0) {
//            return;
//        }
//        Item item = getItem(itemId);
//        if (item == null) {
//            item = Loader.load(itemId);
//            item.getData().setTotal(amount);
//            if (items == null) {
//                items = new ArrayList<Item>();
//            }
//            items.add(item);
//            entity.getData().addObjectData(item.getData());
//        } else {
//            item.getData().setTotal(item.getData().getTotal() + amount);
//        }
//        
//        if (itemListeners != null) {
//            for (int i = 0; i < itemListeners.size(); i++) {
//                itemListeners.get(i).onItemAdded(entity, item, item.getData().getTotal());
//            }
//        }
//    }
//    
//    /**
//     * 删除物品
//     * @param itemId 物品id
//     * @param amount 要删除的数量，不能小于0
//     * @return 
//     */
//    public boolean removeItem(String itemId, int amount) {
//        if (itemId == null || amount <= 0) {
//            return false;
//        }
//        Item item = getItem(itemId);
//        if (item == null || !item.getData().isDeletable())
//            return false;
//        
//        int oldTotal = item.getData().getTotal();
//        int trueRemoved;
//        item.getData().setTotal(item.getData().getTotal() - amount);
//        if (item.getData().getTotal() <= 0) {
//            entity.getData().getObjectDatas().remove(item.getData());
//            items.remove(item);
//            trueRemoved = oldTotal;
//        } else {
//            trueRemoved = oldTotal - item.getData().getTotal();
//        }
//        
//        // 触发侦听器
//        if (itemListeners != null) {
//            for (int i = 0; i < itemListeners.size(); i++) {
//                itemListeners.get(i).onItemRemoved(entity, item, trueRemoved);
//            }
//        }
//        
//        return true;
//    }
//    
//    /**
//     * 判断角色是否可以使用指定的物品
//     * @param item
//     * @return 
//     */
//    public boolean canUse(Item item) {
//        return item.canUse(entity);
//    }
//    
//    /**
//     * 让角色使用指定的物品
//     * @param item 
//     */
//    public void useItem(Item item) {
//        item.use(entity);
//    }
//    
//    /**
//     * 获取指定ID的物品
//     * @param itemId
//     * @return 
//     */
//    public Item getItem(String itemId) {
//        if (items == null) 
//            return null;
//        for (Item item : items) {
//            if (item.getData().getId().equals(itemId)) {
//                return item;
//            }
//        }
//        return null;
//    }
//    
//     /**
//     * 获取所有物品列表,如果没有任何物品则返回空列表,返回的列表不能直接修改。
//     * @return 
//     */
//    public List<Item> getItems() {
//        if (items != null) {
//            return Collections.unmodifiableList(items);
//        }
//        return Collections.EMPTY_LIST;
//    }
//
//    /**
//     * 添加物品侦听器
//     * @param itemListener 
//     */
//    public void addItemListener(ItemListener itemListener) {
//        if (itemListeners == null) {
//            itemListeners = new ArrayList<ItemListener>();
//        }
//        if (!itemListeners.contains(itemListener)) {
//            itemListeners.add(itemListener);
//        }
//    }
//    
//    /**
//     * 删除物品侦听器
//     * @param itemListener
//     * @return 
//     */
//    public boolean removeItemListener(ItemListener itemListener) {
//        return itemListeners != null && itemListeners.remove(itemListener);
//    }
//    
//    
//}
