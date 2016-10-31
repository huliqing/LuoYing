/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.object.entity.Entity;

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
    void addData(Entity actor, String id, int count);
    
    /**
     * 从角色身上移除指定的物体
     * @param actor
     * @param objectId
     * @param count 
     */
    void removeData(Entity actor, String objectId, int count);

    /**
     * 让指定角色使用物体
     * @param actor
     * @param data 
     */
    void useData(Entity actor, ObjectData data);
    
}
