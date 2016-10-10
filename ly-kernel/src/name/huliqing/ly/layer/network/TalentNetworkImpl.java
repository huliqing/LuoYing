/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.TalentService;
import name.huliqing.ly.network.Network;
import name.huliqing.ly.mess.MessTalentAdd;
import name.huliqing.ly.mess.MessTalentAddPoint;
import name.huliqing.ly.object.entity.Entity;

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
