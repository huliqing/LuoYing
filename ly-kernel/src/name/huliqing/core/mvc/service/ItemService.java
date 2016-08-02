/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.object.actor.Actor;

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
     * @return 返回实际移除的数量
     */
    int removeItem(Actor actor, String itemId, int count);
    
    /**
     * 获取角色的非技能物品
     * @param actor
     * @param itemId
     * @return 
     */
    ProtoData getItem(Actor actor, String itemId);
    
    /**
     * 获取角色的所有物品(除技能外）
     * @param actor
     * @param store
     * @return 
     */
    List<ProtoData> getItems(Actor actor, List<ProtoData> store);
    
    /**
     * 判断物品在当前情况下是否可卖出，一些物品可能不能进行出售，如金币，或如一些穿
     * 在身上的装备（正在使用中）。
     * @param data
     * @return 
     */
    boolean isSellable(ProtoData data);
    
    /**
     * 同步物品数量
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
