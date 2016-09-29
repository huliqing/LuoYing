/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.mvc.network.ObjectNetwork;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface ObjectService extends ObjectNetwork {
    
    /**
     * 创建一个物体
     * @param id
     * @return 
     */
    ObjectData createData(String id);
    
    /**
     * 从角色身上获取物品,如果角色存上不存在该物品则返回null.
     * @param actor
     * @param id 
     * @return  
     */
    ObjectData getData(Actor actor, String id);
    
    /**
     * 获取角色身上所有的物体,注：返回的列表不可以直接修改,只能作为只读使用。
     * @param actor
     * @return 
     * @deprecated 
     */
    List<ObjectData> getDatas(Actor actor);
    
    /**
     * 获取物体的价值
     * @param data
     * @return 
     */
    float getCost(ObjectData data);
    
    /**
     * 判断物品在当前情况下是否可卖出，一些物品可能不能进行出售，如金币
     * @param data
     * @return 
     */
    boolean isSellable(ObjectData data);
}
