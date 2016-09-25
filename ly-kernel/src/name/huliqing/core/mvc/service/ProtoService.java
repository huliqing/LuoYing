/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface ProtoService extends Inject {
    
    /**
     * 创建一个物体
     * @param id
     * @return 
     */
    ObjectData createData(String id);
    
    /**
     * 给指定角色添加物体
     * @param actor
     * @param id
     * @param count 
     */
    void addData(Actor actor, String id, int count);
    
    /**
     * 从角色身上移除指定的物体
     * @param actor
     * @param objectId
     * @param count 
     */
    void removeData(Actor actor, String objectId, int count);

    /**
     * 让指定角色使用物体
     * @param actor
     * @param data 
     */
    void useData(Actor actor, ObjectData data);
    
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
