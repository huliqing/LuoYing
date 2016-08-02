/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.service;

import name.huliqing.core.data.ResistData;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class ResistServiceImpl implements ResistService {

    @Override
    public void inject() {
        // ignore
    }

    @Override
    public float getResist(Actor actor, String stateId) {
        ResistData resistData = actor.getData().getResist();
        return actor.getResistProcessor().getResist(resistData, stateId);
    }
    
}
