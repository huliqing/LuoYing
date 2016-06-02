/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.actor;

/**
 * 监听角色物品的增加及删除
 * @author huliqing
 */
public interface ItemListener {
    
    /**
     * 监听角色物品添加，当角色包裹获得物品时该方法被调用。
     * @param source 源角色
     * @param itemId 新添加的物品ID
     * @param trueAdded 实际的添加数量 
     */
    void onItemAdded(Actor source, String itemId, int trueAdded);
    
    /**
     * 监听角色的物品删除,当角色包裹中的物品被移除时该方法被调用。
     * @param source 源角色
     * @param itemId 被删除的物品ID
     * @param trueRemoved 实际的删除数量
     */
    void onItemRemoved(Actor source, String itemId, int trueRemoved);
}
