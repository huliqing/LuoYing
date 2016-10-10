/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.DropService;
import name.huliqing.ly.network.Network;
import name.huliqing.ly.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class DropNetworkImpl implements DropNetwork {
    private final static Network NETWORK = Network.getInstance();
    private DropService dropService;

    @Override
    public void inject() {
        dropService = Factory.get(DropService.class);
    }

    @Override
    public void doDrop(Entity source, Entity target) {
        if (NETWORK.isClient()) 
            return;
        
        // DROP只在服务端处理，不需要向客户端广播事件，因为奖励物品不能由客户端来计算机率
        dropService.doDrop(source, target);
    }
    
}
