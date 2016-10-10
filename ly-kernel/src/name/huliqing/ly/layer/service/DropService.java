/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.data.DropData;
import name.huliqing.ly.layer.network.DropNetwork;
import name.huliqing.ly.object.entity.Entity;

/**
 * 角色物品掉落处理
 * @author huliqing
 */
public interface DropService extends DropNetwork {
    
    /**
     * 创建一个dropData
     * @param objectId
     * @return 
     */
    DropData createDrop(String objectId);
    
    /**
     * 给角色添加一个掉落设置。
     * @param actor
     * @param dropId 
     */
    void addDrop(Entity actor, String dropId);
    

}
