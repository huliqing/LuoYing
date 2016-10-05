/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Factory;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.mess.MessProtoAdd;
import name.huliqing.ly.mess.MessProtoRemove;
import name.huliqing.ly.mess.MessProtoUse;
import name.huliqing.ly.network.Network;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.layer.service.ObjectService;

/**
 *
 * @author huliqing
 */
public class ObjectNetworkImpl implements ObjectNetwork {
    private final Network network = Network.getInstance();
    private ObjectService protoService;
    
    @Override
    public void inject() {
        protoService = Factory.get(ObjectService.class);
    }
    
    @Override
    public void addData(Actor actor, String id, int count) {
        if (network.isClient())
            return;
        
        // 本地服务端更新数量后同步到客户端
        protoService.addData(actor, id, count);

        // 同步物品数量
        MessProtoAdd mess = new MessProtoAdd();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setObjectId(id);
        mess.setAddCount(count);

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

    }

}
