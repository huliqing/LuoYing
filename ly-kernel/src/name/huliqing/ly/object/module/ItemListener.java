/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.module;

import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.item.Item;

/**
 * 监听角色物品的增加及删除
 * @author huliqing
 */
public interface ItemListener {
    
    /**
     * 监听角色物品添加，当角色包裹获得物品时该方法被调用。
     * @param source 源角色
     * @param item 新添加的物品
     * @param trueAdded 实际的添加数量 
     */
    void onItemAdded(Actor source, Item item, int trueAdded);
    
    /**
     * 监听角色的物品删除,当角色包裹中的物品被移除时该方法被调用。
     * @param source 源角色
     * @param item 被删除的物品
     * @param trueRemoved 实际的删除数量
     */
    void onItemRemoved(Actor source, Item item, int trueRemoved);
    
    /**
     * 当角色使用了物品之后该方法被调用
     * @param source
     * @param item 被使用的物品
     */
    void onItemUsed(Actor source, Item item);
}
