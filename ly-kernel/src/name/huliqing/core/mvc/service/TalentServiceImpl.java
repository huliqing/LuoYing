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
import name.huliqing.core.object.control.ActorTalentControl;
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
        ActorTalentControl control = actor.getModel().getControl(ActorTalentControl.class);
        if (control != null) {
            control.addTalent((Talent) Loader.load(talentData));
        }
    }

    @Override
    public void removeTalent(Actor actor, String talentId) {
        ActorTalentControl control = actor.getModel().getControl(ActorTalentControl.class);
        if (control != null) {
            Talent talent = control.getTalent(talentId);
            if (talent != null) {
                control.removeTalent(talent);
            }
        }
    }

    @Override
    public List<TalentData> getTalents(Actor actor) {
        ActorTalentControl control = actor.getModel().getControl(ActorTalentControl.class);
        if (control != null) {
            return control.getTalentDatas();
        }
        return null;
    }

    @Override
    public void addTalentPoints(Actor actor, String talentId, int points) {
        ActorTalentControl control = actor.getModel().getControl(ActorTalentControl.class);
        if (control != null) {
            control.addTalentPoints(talentId, points);
        }
    }

    @Override
    public void addTalentListener(Actor actor, TalentListener talentListener) {
        ActorTalentControl control = actor.getModel().getControl(ActorTalentControl.class);
        if (control != null) {
            control.addTalentListener(talentListener);
        }
    }

    @Override
    public void removeTalentListener(Actor actor, TalentListener talentListener) {
        ActorTalentControl control = actor.getModel().getControl(ActorTalentControl.class);
        if (control != null) {
            control.removeTalentListener(talentListener);
        }
    }

}
