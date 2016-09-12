/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import java.util.List;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.network.Network;
import name.huliqing.core.mess.MessSkinWeaponTakeOn;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SkinListener;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeSkin(Actor actor, String skinId, int amount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void attachSkin(Actor actor, Skin skin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void detachSkin(Actor actor, Skin skin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCanTakeOnWeapon(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCanTakeOffWeapon(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean isWeaponTakeOn(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void takeOnWeapon(Actor actor, boolean force) {
        if (!NETWORK.isClient()) {
            boolean can = force || skinService.isCanTakeOnWeapon(actor);
            if (can) {
                if (NETWORK.hasConnections()) {
                    MessSkinWeaponTakeOn mess = new MessSkinWeaponTakeOn();
                    mess.setActorId(actor.getData().getUniqueId());
                    mess.setTakeOn(true);
                    NETWORK.broadcast(mess);
                }
                
                skinService.takeOnWeapon(actor, true);
            }
        }
    }

    @Override
    public void takeOffWeapon(Actor actor, boolean force) {
        if (!NETWORK.isClient()) {
            boolean can = force || skinService.isCanTakeOffWeapon(actor);
            if (can) {
                if (NETWORK.hasConnections()) {
                    MessSkinWeaponTakeOn mess = new MessSkinWeaponTakeOn();
                    mess.setActorId(actor.getData().getUniqueId());
                    mess.setTakeOn(false);
                    NETWORK.broadcast(mess);
                }
                
                skinService.takeOffWeapon(actor, true);
            }
        }
    }

    @Override
    public List<Skin> getSkins(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Skin> getUsingSkins(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getWeaponState(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addSkinListener(Actor actor, SkinListener skinListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeSkinListener(Actor actor, SkinListener skinListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    
}
