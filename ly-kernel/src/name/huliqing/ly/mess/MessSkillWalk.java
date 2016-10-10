/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.layer.service.SkillService;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.module.SkillModule;

/**
 *
 * @author huliqing
 */
@Serializable
public class MessSkillWalk extends MessBase {
 
    protected long actorId;
    protected String skillId;
    
    // 目标方向
    private Vector3f dir = new Vector3f();
    private boolean face;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public Vector3f getDir() {
        return dir;
    }

    public void setDir(Vector3f dir) {
        if (dir != null) {
            this.dir.set(dir);
        }
    }

    public boolean isFace() {
        return face;
    }

    public void setFace(boolean face) {
        this.face = face;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        SkillService skillService = Factory.get(SkillService.class);
        Entity actor = playService.getEntity(actorId);
        if (actor == null) return;
        skillService.playWalk(actor, actor.getModule(SkillModule.class).getSkill(skillId), dir, face, true);
    }
    
    
}
