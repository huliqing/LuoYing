/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ItemListener;

/**
 *
 * @author huliqing
 */
public interface ItemService extends Inject {
    
    /**
     * 给角色添加物品
     * @param actor
     * @param itemId
     * @param count 
     */
    void addItem(Actor actor, String itemId, int count);

    /**
     * 移除角色身上的物品
     * @param actor
     * @param itemId
     * @param count 要移除的数量,可能比角色实际拥有的数量大
     * @return
     */
    boolean removeItem(Actor actor, String itemId, int count);
    
    /**
     * 获取角色的非技能物品
     * @param actor
     * @param itemId
     * @return 
     */
    ItemData getItem(Actor actor, String itemId);
    
    /**
     * 获取角色的所有物品,注意：不要直接删除列表中的物品，
     * 使用物品使用 {@link #removeItem(Actor, java.lang.String, int) }
     * @param actor
     * @return 
     */
    List<ItemData> getItems(Actor actor);
    
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
    
    /**
     * 让角色使用物品
     * @param actor
     * @param itemId  
     */
    void useItem(Actor actor, String itemId);
    

}
