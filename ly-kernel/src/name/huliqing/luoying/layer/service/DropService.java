/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.layer.network.DropNetwork;
import name.huliqing.luoying.object.entity.Entity;

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
