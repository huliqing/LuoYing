/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import java.util.List;
import name.huliqing.ly.layer.network.ItemNetwork;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.item.Item;
import name.huliqing.ly.object.module.ItemListener;

/**
 *
 * @author huliqing
 */
public interface ItemService extends ItemNetwork {
    
    /**
     * 获取角色的非技能物品
     * @param actor
     * @param itemId
     * @return 
     */
    Item getItem(Entity actor, String itemId);
    
    /**
     * 获取角色的所有物品,返回的列表不能直接修改
     * @param actor
     * @return 
     */
    List<Item> getItems(Entity actor);
    
    /**
     * 给角色添加物品侦听器
     * @param actor
     * @param itemListener 
     */
    void addItemListener(Entity actor, ItemListener itemListener);
    
    /**
     * 删除角色的物品侦听器
     * @param actor
     * @param itemListener
     * @return 
     */
    boolean removeItemListener(Entity actor, ItemListener itemListener);
    
    /**
     * 同步物品数量,如果total小于或等于0，则移除物品
     * @deprecated 不再使用
     * @param actor
     * @param itemId
     * @param total 
     */
    void syncItemTotal(Entity actor, String itemId, int total);

}
