/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.TalentListener;
import name.huliqing.core.object.actormodule.TalentActorModule;
import name.huliqing.core.object.talent.Talent;

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
        TalentActorModule module = actor.getModule(TalentActorModule.class);
        if (module != null) {
            module.addTalent((Talent) Loader.load(talentData));
        }
    }

    @Override
    public void removeTalent(Actor actor, String talentId) {
        TalentActorModule module = actor.getModule(TalentActorModule.class);
        if (module != null) {
            Talent talent = module.getTalent(talentId);
            if (talent != null) {
                module.removeTalent(talent);
            }
        }
    }

    @Override
    public List<TalentData> getTalents(Actor actor) {
        TalentActorModule module = actor.getModule(TalentActorModule.class);
        if (module != null) {
            return module.getTalentDatas();
        }
        return null;
    }

    @Override
    public int getTalentPoints(Actor actor) {
        TalentActorModule module = actor.getModule(TalentActorModule.class);
        if (module != null) {
            return module.getTalentPoints();
        }
        return 0;
    }

    @Override
    public void setTalentPoints(Actor actor, int talentPoints) {
        TalentActorModule module = actor.getModule(TalentActorModule.class);
        if (module != null) {
            module.setTalentPoints(talentPoints);
        }
    }
    
    @Override
    public void addTalentPoints(Actor actor, String talentId, int points) {
        TalentActorModule module = actor.getModule(TalentActorModule.class);
        if (module != null) {
            module.addTalentPoints(talentId, points);
        }
    }

    @Override
    public void addTalentListener(Actor actor, TalentListener talentListener) {
        TalentActorModule module = actor.getModule(TalentActorModule.class);
        if (module != null) {
            module.addTalentListener(talentListener);
        }
    }

    @Override
    public void removeTalentListener(Actor actor, TalentListener talentListener) {
        TalentActorModule module = actor.getModule(TalentActorModule.class);
        if (module != null) {
            module.removeTalentListener(talentListener);
        }
    }

}
