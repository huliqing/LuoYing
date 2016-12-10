/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.mess.EntityAddDataMess;
import name.huliqing.luoying.mess.EntityHitNumberAttributeMess;
import name.huliqing.luoying.mess.EntityHitAttributeMess;
import name.huliqing.luoying.mess.EntityRemoveDataMess;
import name.huliqing.luoying.mess.EntityUseDataByIdMess;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * Entity的网络层，主要负责将对于Entity的属性修改及数据的添加广播到客户端.
 * @author huliqing
 */
public class EntityNetworkImpl implements EntityNetwork {
//    private static final Logger LOG = Logger.getLogger(EntityNetworkImpl.class.getName());
    
    private final Network network = Network.getInstance();
    private EntityService entityService;
    
    @Override
    public void inject() {
        entityService = Factory.get(EntityService.class);
    }
    
    @Override
    public void hitAttribute(Entity entity, String attribute, Object value, Entity hitter) {
        EntityHitAttributeMess mess = new EntityHitAttributeMess();
        mess.setEntityId(entity.getEntityId());
        mess.setAttribute(attribute);
        mess.setValue(value);
        mess.setHitterId(hitter != null ? hitter.getEntityId() : -1);
        
        // 在客户端
        if (network.isClient()) {
            network.sendToServer(mess);
            return;
        }
        
        // 在服务端
        network.broadcast(mess);
        entityService.hitAttribute(entity, attribute, value, hitter);
    }
    
    @Override
    public void hitNumberAttribute(Entity entity, String attribute, float addValue, Entity hitter) {
        EntityHitNumberAttributeMess mess = new EntityHitNumberAttributeMess();
        mess.setEntityId(entity.getEntityId());
        mess.setAttribute(attribute);
        mess.setAddValue(addValue);
        mess.setHitterId(hitter != null ? hitter.getEntityId() : -1);
        
        // 客户端
        if (network.isClient()) {
            network.sendToServer(mess);
            return;
        }
        
        // 服务端
        network.broadcast(mess);
        entityService.hitNumberAttribute(entity, attribute, addValue, hitter);
    }

    @Override
    public boolean addObjectData(Entity entity, ObjectData data, int amount) {
        EntityAddDataMess mess = new EntityAddDataMess();
        mess.setEntityId(entity.getEntityId());
        mess.setObjectData(data);
        mess.setAmount(amount);
        
        if (network.isClient()) {
            network.sendToServer(mess);
            return false;
        }
        
        network.broadcast(mess);
        return entityService.addObjectData(entity, data, amount);
    }

    // remove20161122不使用直接id添加物品的方式，这会造成添加后的物品的唯一id(uniqueId)在客户端和服务端不一致的问题。
//    @Override
//    public boolean addObjectData(Entity entity, String objectId, int amount) {
//        MessEntityAddDataById mess = new MessEntityAddDataById();
//        mess.setEntityId(entity.getEntityId());
//        mess.setObjectId(objectId);
//        mess.setAmount(amount);
//    
//        if (network.isClient()) {
//            network.sendToServer(mess);
//            return false;
//        }
//        
//        network.broadcast(mess);
//        return entityService.addObjectData(entity, objectId, amount);
//    }

    @Override
    public boolean removeObjectData(Entity entity, long objectUniqueId, int amount) {
        EntityRemoveDataMess mess = new EntityRemoveDataMess();
        mess.setEntityId(entity.getEntityId());
        mess.setObjectId(objectUniqueId);
        mess.setAmount(amount);
        
        if (network.isClient()) {
            network.sendToServer(mess);
            return false;
        }
        
        network.broadcast(mess);
        return entityService.removeObjectData(entity, objectUniqueId, amount);
    }

    // remove20161211,看接口说明
//    @Override
//    public boolean useObjectData(Entity entity, ObjectData data) {
//        EntityUseDataMess mess = new EntityUseDataMess();
//        mess.setEntityId(entity.getEntityId());
//        mess.setObjectData(data);
//        
//        if (network.isClient()) {
//            network.sendToServer(mess);
//            return false;
//        }
//        
//        network.broadcast(mess);
//        return entityService.useObjectData(entity, data);
//    }

    @Override
    public boolean useObjectData(Entity entity, long objectUniqueId) {
        EntityUseDataByIdMess mess = new EntityUseDataByIdMess();
        mess.setEntityId(entity.getEntityId());
        mess.setObjectUniqueId(objectUniqueId);
        
        if (network.isClient()) {
            network.sendToServer(mess);
            return false;
        }
        
        network.broadcast(mess);
        return entityService.useObjectData(entity, objectUniqueId);
    }
    
}
