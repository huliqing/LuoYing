/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.Factory;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.mess.MessProtoAdd;
import name.huliqing.core.mess.MessProtoRemove;
import name.huliqing.core.mess.MessProtoSync;
import name.huliqing.core.mess.MessProtoUse;
import name.huliqing.core.mvc.service.ProtoService;
import name.huliqing.core.network.Network;
import name.huliqing.core.object.actor.Actor;

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
    public ObjectData createData(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObjectData getData(Actor actor, String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addData(Actor actor, String id, int count) {
        if (network.isClient())
            return;
        
        // 本地服务端更新数量后同步到客户端
        protoService.addData(actor, id, count);

        // 同步物品数量
        ObjectData resultData = protoService.getData(actor, id);
        MessProtoAdd mess = new MessProtoAdd();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setObjectId(id);
        mess.setAddCount(count);
        mess.setSyncTotal(resultData != null ? resultData.getTotal() : 0);
        network.broadcast(mess);
    }
    
    @Override
    public void removeData(Actor actor, String id, int count) {
        if (network.isClient())
            return;
        
        // 广播到所有客户端
        if (network.hasConnections()) {
            MessProtoRemove mess = new MessProtoRemove();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setObjectId(id);
            mess.setAmount(count);
            network.broadcast(mess);
        }
        
        // 服务端删除
        protoService.removeData(actor, id, count);
        
        if (network.hasConnections()) {
            ObjectData resultData = protoService.getData(actor, id);
            MessProtoSync messSync = new MessProtoSync();
            messSync.setActorId(actor.getData().getUniqueId());
            messSync.setObjectId(id);
            messSync.setTotal(resultData != null ? resultData.getTotal() : 0);
            network.broadcast(messSync);
        }
    }

    @Override
    public void useData(Actor actor, ObjectData data) {
        if (network.isClient())
            return;
        if (data == null)
            return;
        
        // 对于本地物体不需要传递到服务端或客户端，比如“地图”的使用，当打开地图的时候是不需要广播到其它客户端。
        // localObject这是一种特殊的物品，只通过本地handler执行，所以使用后物品数量不会实时同步到其它客户端。需要注意
        // 这一点。
        if (data.isLocalObject()) {
            protoService.useData(actor, data);
            return;
        }
        
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
            ObjectData resultData = protoService.getData(actor, data.getId());
            MessProtoSync mess = new MessProtoSync();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setObjectId(data.getId());
            mess.setTotal(resultData != null ? resultData.getTotal() : 0);
            network.broadcast(mess);
        }
    }

    @Override
    public void syncDataTotal(Actor actor, String objectId, int total) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getCost(ObjectData data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSellable(ObjectData data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
