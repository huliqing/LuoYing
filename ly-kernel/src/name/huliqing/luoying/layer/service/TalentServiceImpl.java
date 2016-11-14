/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.TalentListener;
import name.huliqing.luoying.object.module.TalentModule;

/**
 *
 * @author huliqing
 */
public class TalentServiceImpl implements TalentService {

    @Override
    public void inject() {
        // ignore
    }

    @Override
    public int getTalentPoints(Entity actor) {
        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
        if (module != null) {
            return module.getTalentPoints();
        }
        return 0;
    }
    
    @Override
    public void addTalentPoints(Entity actor, String talentId, int points) {
        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
        if (module != null) {
            module.addTalentPoints(talentId, points);
        }
    }

    @Override
    public void addTalentListener(Entity actor, TalentListener talentListener) {
        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
        if (module != null) {
            module.addTalentListener(talentListener);
        }
    }

    @Override
    public void removeTalentListener(Entity actor, TalentListener talentListener) {
        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
        if (module != null) {
            module.removeTalentListener(talentListener);
        }
    }

}
