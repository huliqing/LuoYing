/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.Inject;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface ObjectNetwork extends Inject {

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
    
}
