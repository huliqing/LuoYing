/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.TalentListener;
import name.huliqing.core.object.talent.Talent;

/**
 *
 * @author huliqing
 */
public class ActorTalentControl extends ActorControl {
    
    private Actor actor;
    // 角色的当前状态列表
    private final SafeArrayList<Talent> talents = new SafeArrayList<Talent>(Talent.class);
    
    // 监听角色天赋的增加，删除，更新等
    private List<TalentListener> talentListeners;

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;
    }

    @Override
    public void cleanup() {
        for (Talent t : talents.getArray()) {
            t.cleanup();
        }
        talents.clear();
        talentListeners.clear();
    }
    
    @Override
    public void actorUpdate(float tpf) {
        for (Talent t : talents.getArray()) {
            t.update(tpf);
        }
    }

    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {
    }
    
    public void addTalent(Talent talent) {
        if (!talents.contains(talent)) {
            talent.setActor(actor);
            talent.init();
            talents.add(talent);
        }
    }
    
    public boolean removeTalent(Talent talent) {
        talent.cleanup();
        return talents.remove(talent);
    }
    
    public boolean removeTalent(String talentId) {
        Talent talent = getTalent(talentId);
        return removeTalent(talent);
    }

    public Talent getTalent(String talentId) {
        for (Talent t : talents.getArray()) {
            if (t.getData().getId().equals(talentId)) {
                return t;
            }
        }
        return null;
    }

    public List<Talent> getTalents() {
        return talents;
    }
    
    public void addTalentListener(TalentListener talentListener) {
        if (talentListeners == null) {
            talentListeners = new ArrayList<TalentListener>();
        }
        if (!talentListeners.contains(talentListener)) {
            talentListeners.add(talentListener);
        }
    }

    public boolean removeTalentListener(TalentListener talentListener) {
        return talentListeners != null && talentListeners.remove(talentListener);
    }

    public List<TalentListener> getTalentListeners() {
        return talentListeners;
    }
}
