/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.TalentService;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.mess.MessTalentAdd;
import name.huliqing.luoying.mess.MessTalentAddPoint;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 */
public class TalentNetworkImpl implements TalentNetwork {
    
    private final static Network NETWORK = Network.getInstance();
    private TalentService talentService;
    
    @Override
    public void inject() {
        talentService = Factory.get(TalentService.class);
    }
    
    @Override
    public void addTalent(Entity actor, String talentId) {
        if (NETWORK.isClient())
            return;
        
        MessTalentAdd mess = new MessTalentAdd();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setTalentId(talentId);
        NETWORK.broadcast(mess);
        
        talentService.addTalent(actor, talentId);
    }

    @Override
    public void addTalentPoints(Entity actor, String talentId, int points) {
        if (NETWORK.isClient())
            return;
        
        MessTalentAddPoint mess = new MessTalentAddPoint();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setTalentId(talentId);
        mess.setPoints(points);
        NETWORK.broadcast(mess);
        
        talentService.addTalentPoints(actor, talentId, points);
    }
    
}
