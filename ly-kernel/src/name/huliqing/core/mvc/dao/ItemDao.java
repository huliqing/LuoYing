/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.dao;

import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ProtoData;

/**
 *
 * @author huliqing
 */
public interface ItemDao extends Inject{
    
    /**
     * 给角色身上添加物品
     * @param actor
     * @param objectId
     * @param count 不能小于0
     * @return 
     */
    boolean addItem(Actor actor, String objectId, int count);

    /**
     * 移除角色身上的物品
     * @param actor
     * @param objectId
     * @param count 不能小于0.
     * @return 返回实际移除的数量，因为角色本身存在的物品数可能比期望要移除的数量少。
     */
    int removeItem(Actor actor, String objectId, int count);

    /**
     * 从角色身上获取物品，如果角色身上不存在该物品，则返回null.
     * 注意：该方法不会包含"技能"
     * @param actor
     * @param objectId
     * @return 
     */
    ProtoData getItemExceptSkill(Actor actor, String objectId);
    
    /**
     * 获取角色所有物品(除技能)
     * @param actor
     * @param store 存放结果
     * @return 
     */
    List<ProtoData> getItems(Actor actor, List<ProtoData> store);
    
}
