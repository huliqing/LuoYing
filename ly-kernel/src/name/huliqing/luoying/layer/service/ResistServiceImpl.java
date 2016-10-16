/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ResistModule;
import name.huliqing.luoying.object.resist.Resist;

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
    public float getResist(Entity actor, String stateId) {
        ResistModule module = actor.getEntityModule().getModule(ResistModule.class);
        if (module != null) {
            Resist resist = module.getResist();
            if (resist != null) {
                return resist.getResist(stateId);
            }
        }
        return 0;
    }
    
}
