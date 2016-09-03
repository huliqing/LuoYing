/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.Factory;
import name.huliqing.core.data.DropData;
import name.huliqing.core.mvc.service.DropService;
import name.huliqing.core.network.Network;
import name.huliqing.core.object.actor.Actor;

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
    public void addDrop(Actor actor, String dropId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DropData createDrop(String objectId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doDrop(Actor source, Actor target) {
        if (NETWORK.isClient()) 
            return;
        
        // DROP只在服务端处理，不需要向客户端广播事件，因为奖励物品不能由客户端来计算机率
        dropService.doDrop(source, target);
    }
    
}
