/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.SkinService;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.mess.SkinWeaponTakeOnMess;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.skin.Skin;

/**
 *
 * @author huliqing
 */
public class SkinNetworkImpl implements SkinNetwork {
//    private static final Logger LOG = Logger.getLogger(SkinNetworkImpl.class.getName());
    private final static Network NETWORK = Network.getInstance();
    private SkinService skinService;
    
    @Override
    public void inject() {
        skinService = Factory.get(SkinService.class);
    }
    
    // remove20161111
//    @Override
//    public void addSkin(Entity actor, String skinId, int amount) {
//        if (NETWORK.isClient()) {
//            // ignore 客户端不能主动添加物品
//        } else {
//            MessSkinAdd mess = new MessSkinAdd();
//            mess.setActorId(actor.getData().getUniqueId());
//            mess.setSkinId(skinId);
//            mess.setCount(amount);
//            NETWORK.broadcast(mess);
//        }
//    }
//
//    @Override
//    public void removeSkin(Entity actor, String skinId, int amount) {
//        MessSkinRemove mess = new MessSkinRemove();
//        mess.setActorId(actor.getData().getUniqueId());
//        mess.setSkinId(skinId);
//        mess.setCount(amount);
//        if (NETWORK.isClient()) {
//            NETWORK.sendToServer(mess);
//        } else {
//            NETWORK.broadcast(mess);
//            skinService.removeSkin(actor, skinId, amount);
//        }
//    }
    
     // remove20161111
//    @Override
//    public void attachSkin(Entity actor, Skin skin) {
//        if (!skin.canUse(actor)) {
//            return;
//        }
//        
//        MessSkinAttach mess = new MessSkinAttach();
//        mess.setActorId(actor.getData().getUniqueId());
//        mess.setSkinId(skin.getData().getId());
//        if (NETWORK.isClient()) {
//            NETWORK.sendToServer(mess);
//        } else {
//            NETWORK.broadcast(mess);
//            skinService.attachSkin(actor, skin);
//        }
//    }
//
//    @Override
//    public void detachSkin(Entity actor, Skin skin) {
//        MessSkinDetach mess = new MessSkinDetach();
//        mess.setActorId(actor.getData().getUniqueId());
//        mess.setSkinId(skin.getData().getId());
//        if (NETWORK.isClient()) {
//            NETWORK.sendToServer(mess);
//        } else {
//            NETWORK.broadcast(mess);
//            skinService.detachSkin(actor, skin);
//        }
//    }

    @Override
    public void takeOnWeapon(Entity actor) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                SkinWeaponTakeOnMess mess = new SkinWeaponTakeOnMess();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setTakeOn(true);
                NETWORK.broadcast(mess);
            }
            skinService.takeOnWeapon(actor);
        }
    }

    @Override
    public void takeOffWeapon(Entity actor) {
        SkinWeaponTakeOnMess mess = new SkinWeaponTakeOnMess();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setTakeOn(false);
        
        // client
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
            return;
        }

        // server
        if (NETWORK.hasConnections()) {
            NETWORK.broadcast(mess);
        }
        skinService.takeOffWeapon(actor);

    }
    
}
