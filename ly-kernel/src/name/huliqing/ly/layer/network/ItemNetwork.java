/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Inject;
import name.huliqing.ly.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public interface ItemNetwork extends Inject  {
    
    /**
     * 给角色添加物品
     * @param actor
     * @param itemId
     * @param count 
     */
    void addItem(Entity actor, String itemId, int count);

    /**
     * 移除角色身上的物品
     * @param actor
     * @param itemId
     * @param count 要移除的数量,可能比角色实际拥有的数量大
     */
    void removeItem(Entity actor, String itemId, int count);
    
    /**
     * 让角色使用物品
     * @param actor
     * @param itemId  
     */
    void useItem(Entity actor, String itemId);
    
}
