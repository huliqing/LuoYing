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

    // remove20161112
//    @Override
//    public void addTalent(Entity actor, String talentId) {
//        TalentData data = DataFactory.createData(talentId);
//        addTalent(actor, data);
//    }
//
//    @Override
//    public void addTalent(Entity actor, TalentData talentData) {
//        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
//        if (module != null) {
//            module.addTalent((Talent) Loader.load(talentData));
//        }
//    }
//
//    @Override
//    public void addTalent(Entity actor, Talent talent) {
//        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
//        if (module != null) {
//            module.addTalent(talent);
//        }
//    }
//
//    @Override
//    public void removeTalent(Entity actor, String talentId) {
//        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
//        if (module != null) {
//            Talent talent = module.getTalent(talentId);
//            if (talent != null) {
//                module.removeTalent(talent);
//            }
//        }
//    }
//
//    @Override
//    public List<Talent> getTalents(Entity actor) {
//        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
//        if (module != null) {
//            return module.getTalents();
//        }
//        return null;
//    }

    @Override
    public int getTalentPoints(Entity actor) {
        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
        if (module != null) {
            return module.getTalentPoints();
        }
        return 0;
    }

    // remove20160821
//    @Override
//    public void setTalentPoints(Entity actor, int talentPoints) {
//        TalentModule module = actor.getEntityModule().getModule(TalentModule.class);
//        if (module != null) {
//            module.setTalentPoints(talentPoints);
//        }
//    }
    
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
