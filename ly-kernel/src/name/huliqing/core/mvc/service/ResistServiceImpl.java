/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.data.ResistData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ResistModule;

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
        ResistModule module = actor.getModule(ResistModule.class);
        ResistData resistData = actor.getData().getResist();
        return module.getResist(resistData, stateId);
    }
    
}
