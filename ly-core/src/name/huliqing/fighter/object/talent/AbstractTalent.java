/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.talent;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.TalentData;
import name.huliqing.fighter.game.service.ElService;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public abstract class AbstractTalent implements Talent {
//    private final static ElService elService = Factory.get(ElService.class);
    
    protected TalentData data;
    protected Actor actor;
    
    private float timeUsed;
    
    public AbstractTalent(TalentData data) {
        this.data = data;
    }

    @Override
    public void setActor(Actor actor) {
        this.actor = actor;
    }
    
    @Override
    public TalentData getData() {
        return data;
    }

    @Override
    public void update(float tpf) {
        timeUsed += tpf;
        if (timeUsed >= data.getInterval()) {
            timeUsed = 0;
            doLogic(tpf);
        }
    }
    
    protected abstract void doLogic(float tpf);

}
