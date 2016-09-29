/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.mvc.network.ItemNetwork;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.item.Item;
import name.huliqing.core.object.module.ItemListener;

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
    Item getItem(Actor actor, String itemId);
    
    /**
     * 获取角色的所有物品,返回的列表不能直接修改
     * @param actor
     * @return 
     */
    List<Item> getItems(Actor actor);
    
    /**
     * 给角色添加物品侦听器
     * @param actor
     * @param itemListener 
     */
    void addItemListener(Actor actor, ItemListener itemListener);
    
    /**
     * 删除角色的物品侦听器
     * @param actor
     * @param itemListener
     * @return 
     */
    boolean removeItemListener(Actor actor, ItemListener itemListener);
    
    /**
     * 同步物品数量,如果total小于或等于0，则移除物品
     * @deprecated 不再使用
     * @param actor
     * @param itemId
     * @param total 
     */
    void syncItemTotal(Actor actor, String itemId, int total);

}
