/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.network;

import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.game.service.TalentService;
import name.huliqing.core.network.Network;
import name.huliqing.core.game.mess.MessTalentAdd;
import name.huliqing.core.game.mess.MessTalentAddPoint;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class TalentNetworkImpl implements TalentNetwork {
    
    private final static Network network = Network.getInstance();
    private TalentService talentService;
    
    @Override
    public void inject() {
        talentService = Factory.get(TalentService.class);
    }
    
    @Override
    public void addTalent(Actor actor, String talentId) {
        if (network.isClient())
            return;
        
        MessTalentAdd mess = new MessTalentAdd();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setTalentId(talentId);
        network.broadcast(mess);
        
        talentService.addTalent(actor, talentId);
    }

    @Override
    public void addTalent(Actor actor, TalentData talentData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeTalent(Actor actor, String talentId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TalentData> getTalents(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addTalentPoints(Actor actor, String talentId, int points) {
        if (network.isClient())
            return;
        
        MessTalentAddPoint mess = new MessTalentAddPoint();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setTalentId(talentId);
        mess.setPoints(points);
        network.broadcast(mess);
        
        talentService.addTalentPoints(actor, talentId, points);
    }

    
}
