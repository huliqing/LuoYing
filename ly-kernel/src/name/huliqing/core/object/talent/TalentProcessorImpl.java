/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.talent;

import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.core.object.actor.Actor;

/**
 * @author huliqing
 */
public class TalentProcessorImpl implements TalentProcessor {

    private Actor actor;
    // 角色的当前状态列表
    private final SafeArrayList<Talent> talents = new SafeArrayList<Talent>(Talent.class);

    public TalentProcessorImpl(Actor actor) {
        this.actor = actor;
    }
    
    @Override
    public void update(float tpf) {
        for (Talent t : talents.getArray()) {
            t.update(tpf);
        }
    }

    @Override
    public void addTalent(Talent talent) {
        if (!talents.contains(talent)) {
            talent.setActor(actor);
            talent.init();
            talents.add(talent);
        }
    }

    @Override
    public boolean removeTalent(Talent talent) {
        talent.cleanup();
        return talents.remove(talent);
    }

    @Override
    public boolean removeTalent(String talentId) {
        boolean result = false;
        for (Talent talent : talents.getArray()) {
            if (talent.getData().getId().equals(talentId)) {
                talent.cleanup();
                talents.remove(talent);
                result = true;
            }
        }
        return result;
    }

    @Override
    public Talent getTalent(String talentId) {
        for (Talent t : talents.getArray()) {
            if (t.getData().getId().equals(talentId)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public List<Talent> getTalents() {
        return talents;
    }
    
    @Override
    public void cleanup() {
        for (Talent t : talents.getArray()) {
            t.cleanup();
        }
        talents.clear();
    }
    
}
