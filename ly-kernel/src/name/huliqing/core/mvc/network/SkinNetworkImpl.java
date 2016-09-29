/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.Factory;
import name.huliqing.core.mess.MessSkinAdd;
import name.huliqing.core.mess.MessSkinAttach;
import name.huliqing.core.mess.MessSkinDetach;
import name.huliqing.core.mess.MessSkinRemove;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.network.Network;
import name.huliqing.core.mess.MessSkinWeaponTakeOn;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.skin.Skin;

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
    
    @Override
    public void addSkin(Actor actor, String skinId, int amount) {
        if (NETWORK.isClient()) {
            // ignore 客户端不能主动添加物品
        } else {
            MessSkinAdd mess = new MessSkinAdd();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setSkinId(skinId);
            mess.setCount(amount);
            NETWORK.broadcast(mess);
        }
    }

    @Override
    public void removeSkin(Actor actor, String skinId, int amount) {
        MessSkinRemove mess = new MessSkinRemove();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setSkinId(skinId);
        mess.setCount(amount);
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            NETWORK.broadcast(mess);
            skinService.removeSkin(actor, skinId, amount);
        }
    }
    
    @Override
    public void attachSkin(Actor actor, Skin skin) {
        if (!skin.canUse(actor)) {
            return;
        }
        
        MessSkinAttach mess = new MessSkinAttach();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setSkinId(skin.getData().getId());
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            NETWORK.broadcast(mess);
            skinService.attachSkin(actor, skin);
        }
    }

    @Override
    public void detachSkin(Actor actor, Skin skin) {
        MessSkinDetach mess = new MessSkinDetach();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setSkinId(skin.getData().getId());
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            NETWORK.broadcast(mess);
            skinService.detachSkin(actor, skin);
        }
    }

    @Override
    public void takeOnWeapon(Actor actor) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                MessSkinWeaponTakeOn mess = new MessSkinWeaponTakeOn();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setTakeOn(true);
                NETWORK.broadcast(mess);
            }
            skinService.takeOnWeapon(actor);
        }
    }

    @Override
    public void takeOffWeapon(Actor actor) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                MessSkinWeaponTakeOn mess = new MessSkinWeaponTakeOn();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setTakeOn(false);
                NETWORK.broadcast(mess);
            }
            skinService.takeOffWeapon(actor);
        }
    }
    
}
