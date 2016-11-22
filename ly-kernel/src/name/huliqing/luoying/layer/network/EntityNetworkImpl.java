/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.mess.MessEntityAddData;
import name.huliqing.luoying.mess.MessEntityHitNumberAttribute;
import name.huliqing.luoying.mess.MessEntityHitAttribute;
import name.huliqing.luoying.mess.MessEntityRemoveData;
import name.huliqing.luoying.mess.MessEntityUseData;
import name.huliqing.luoying.mess.MessEntityUseDataById;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * Entity的网络层，主要负责将对于Entity的属性修改及数据的添加广播到客户端.
 * @author huliqing
 */
public class EntityNetworkImpl implements EntityNetwork {
    private final Network network = Network.getInstance();
    private EntityService entityService;
    
    @Override
    public void inject() {
        entityService = Factory.get(EntityService.class);
    }
    
    @Override
    public void hitAttribute(Entity entity, String attribute, Object value, Entity hitter) {
        // 在客户端
        if (network.isClient()) {
            return;
        }
        
        // 在服务端
        MessEntityHitAttribute mess = new MessEntityHitAttribute();
        mess.setEntityId(entity.getEntityId());
        mess.setAttribute(attribute);
        mess.setValue(value);
        mess.setHitterId(hitter != null ? hitter.getEntityId() : -1);
        network.broadcast(mess);
        entityService.hitAttribute(entity, attribute, value, hitter);
    }
    
    @Override
    public void hitNumberAttribute(Entity entity, String attribute, float addValue, Entity hitter) {
        // 客户端
        if (network.isClient()) {
            return;
        }
        
        // 服务端
        MessEntityHitNumberAttribute mess = new MessEntityHitNumberAttribute();
        mess.setEntityId(entity.getEntityId());
        mess.setAttribute(attribute);
        mess.setAddValue(addValue);
        mess.setHitterId(hitter != null ? hitter.getEntityId() : -1);
        network.broadcast(mess);
        entityService.hitNumberAttribute(entity, attribute, addValue, hitter);
    }

    @Override
    public boolean addObjectData(Entity entity, ObjectData data, int amount) {
        if (network.isClient()) {
            return false;
        }
        
        MessEntityAddData mess = new MessEntityAddData();
        mess.setEntityId(entity.getEntityId());
        mess.setObjectData(data);
        mess.setAmount(amount);
        network.broadcast(mess);
        return entityService.addObjectData(entity, data, amount);
    }

    // remove20161122不使用直接id添加物品的方式，这会造成添加后的物品的唯一id(uniqueId)在客户端和服务端不一致的问题。
//    @Override
//    public boolean addObjectData(Entity entity, String objectId, int amount) {
//        if (network.isClient()) {
//            return false;
//        }
//        
//        MessEntityAddDataById mess = new MessEntityAddDataById();
//        mess.setEntityId(entity.getEntityId());
//        mess.setObjectId(objectId);
//        mess.setAmount(amount);
//        network.broadcast(mess);
//        return entityService.addObjectData(entity, objectId, amount);
//    }

    @Override
    public boolean removeObjectData(Entity entity, long objectUniqueId, int amount) {
        if (network.isClient()) {
            return false;
        }
        
        MessEntityRemoveData mess = new MessEntityRemoveData();
        mess.setEntityId(entity.getEntityId());
        mess.setObjectId(objectUniqueId);
        mess.setAmount(amount);
        network.broadcast(mess);
        return entityService.removeObjectData(entity, objectUniqueId, amount);
    }

    @Override
    public boolean useObjectData(Entity entity, ObjectData data) {
        if (network.isClient()) {
            return false;
        }
        
        MessEntityUseData mess = new MessEntityUseData();
        mess.setEntityId(entity.getEntityId());
        mess.setObjectData(data);
        network.broadcast(mess);
        return entityService.useObjectData(entity, data);
    }

    @Override
    public boolean useObjectData(Entity entity, long objectUniqueId) {
        if (network.isClient()) {
            return false;
        }
        
        MessEntityUseDataById mess = new MessEntityUseDataById();
        mess.setEntityId(entity.getEntityId());
        mess.setObjectUniqueId(objectUniqueId);
        network.broadcast(mess);
        return entityService.useObjectData(entity, objectUniqueId);
    }
    
}
