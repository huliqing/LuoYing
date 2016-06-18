/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.network;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.mess.MessProtoAdd;
import name.huliqing.fighter.game.mess.MessProtoRemove;
import name.huliqing.fighter.game.mess.MessProtoSync;
import name.huliqing.fighter.game.mess.MessProtoUse;
import name.huliqing.fighter.game.service.ProtoService;
import name.huliqing.fighter.game.state.lan.Network;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class ProtoNetworkImpl implements ProtoNetwork {
    private final Network network = Network.getInstance();
    private ProtoService protoService;
    
    @Override
    public void inject() {
        protoService = Factory.get(ProtoService.class);
    }
    
    @Override
    public ProtoData createData(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProtoData getData(Actor actor, String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addData(Actor actor, ProtoData data, int count) {
        if (network.isClient())
            return;
        
        // 本地服务端更新数量后同步到客户端
        protoService.addData(actor, data, count);

        // 同步物品数量
        ProtoData resultData = protoService.getData(actor, data.getId());
        MessProtoAdd mess = new MessProtoAdd();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setObjectId(data.getId());
        mess.setAddCount(count);
        mess.setSyncTotal(resultData != null ? resultData.getTotal() : 0);
        network.broadcast(mess);
    }

    @Override
    public void useData(Actor actor, ProtoData data) {
        if (network.isClient())
            return;
        if (data == null)
            return;
        
        // 广播到客户端
        if (network.hasConnections()) {
            MessProtoUse mess = new MessProtoUse();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setObjectId(data.getId());
            network.broadcast(mess);
        }

        // 自身执行
        protoService.useData(actor, data);

        // 同步物品数量
        if (network.hasConnections()) {
            ProtoData resultData = protoService.getData(actor, data.getId());
            MessProtoSync mess = new MessProtoSync();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setObjectId(data.getId());
            mess.setTotal(resultData != null ? resultData.getTotal() : 0);
            network.broadcast(mess);
        }
    }

    @Override
    public void removeData(Actor actor, ProtoData data, int count) {
        if (network.isClient())
            return;
        
        if (data == null)
            return;
        
        // 广播到所有客户端
        if (network.hasConnections()) {
            MessProtoRemove mess = new MessProtoRemove();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setObjectId(data.getId());
            mess.setAmount(count);
            network.broadcast(mess);
        }

        // 服务端删除
        protoService.removeData(actor, data, count);
        
        if (network.hasConnections()) {
            ProtoData resultData = protoService.getData(actor, data.getId());
            MessProtoSync messSync = new MessProtoSync();
            messSync.setActorId(actor.getData().getUniqueId());
            messSync.setObjectId(data.getId());
            messSync.setTotal(resultData != null ? resultData.getTotal() : 0);
            network.broadcast(messSync);
        }
    }

    @Override
    public void syncDataTotal(Actor actor, String objectId, int total) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
