/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.TalentService;
import name.huliqing.core.network.Network;
import name.huliqing.core.mess.MessTalentAdd;
import name.huliqing.core.mess.MessTalentAddPoint;
import name.huliqing.core.object.actor.Actor;

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
    public void addTalent(Actor actor, String talentId) {
        if (NETWORK.isClient())
            return;
        
        MessTalentAdd mess = new MessTalentAdd();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setTalentId(talentId);
        NETWORK.broadcast(mess);
        
        talentService.addTalent(actor, talentId);
    }

    @Override
    public void addTalentPoints(Actor actor, String talentId, int points) {
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
