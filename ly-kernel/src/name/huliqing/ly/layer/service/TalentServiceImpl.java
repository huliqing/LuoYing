/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import java.util.List;
import name.huliqing.ly.data.TalentData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.xml.DataFactory;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.module.TalentListener;
import name.huliqing.ly.object.module.TalentModule;
import name.huliqing.ly.object.talent.Talent;

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
    public void addTalent(Actor actor, String talentId) {
        TalentData data = DataFactory.createData(talentId);
        addTalent(actor, data);
    }

    @Override
    public void addTalent(Actor actor, TalentData talentData) {
        TalentModule module = actor.getModule(TalentModule.class);
        if (module != null) {
            module.addTalent((Talent) Loader.load(talentData));
        }
    }

    @Override
    public void addTalent(Actor actor, Talent talent) {
        TalentModule module = actor.getModule(TalentModule.class);
        if (module != null) {
            module.addTalent(talent);
        }
    }

    @Override
    public void removeTalent(Actor actor, String talentId) {
        TalentModule module = actor.getModule(TalentModule.class);
        if (module != null) {
            Talent talent = module.getTalent(talentId);
            if (talent != null) {
                module.removeTalent(talent);
            }
        }
    }

    @Override
    public List<Talent> getTalents(Actor actor) {
        TalentModule module = actor.getModule(TalentModule.class);
        if (module != null) {
            return module.getTalents();
        }
        return null;
    }

    @Override
    public int getTalentPoints(Actor actor) {
        TalentModule module = actor.getModule(TalentModule.class);
        if (module != null) {
            return module.getTalentPoints();
        }
        return 0;
    }

    // remove20160821
//    @Override
//    public void setTalentPoints(Actor actor, int talentPoints) {
//        TalentModule module = actor.getModule(TalentModule.class);
//        if (module != null) {
//            module.setTalentPoints(talentPoints);
//        }
//    }
    
    @Override
    public void addTalentPoints(Actor actor, String talentId, int points) {
        TalentModule module = actor.getModule(TalentModule.class);
        if (module != null) {
            module.addTalentPoints(talentId, points);
        }
    }

    @Override
    public void addTalentListener(Actor actor, TalentListener talentListener) {
        TalentModule module = actor.getModule(TalentModule.class);
        if (module != null) {
            module.addTalentListener(talentListener);
        }
    }

    @Override
    public void removeTalentListener(Actor actor, TalentListener talentListener) {
        TalentModule module = actor.getModule(TalentModule.class);
        if (module != null) {
            module.removeTalentListener(talentListener);
        }
    }

}