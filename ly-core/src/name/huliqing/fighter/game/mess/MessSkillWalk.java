/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 */
@Serializable
public class MessSkillWalk extends MessSkillAbstract {
 
    // 目标方向
    private Vector3f dir = new Vector3f();
    private boolean face;
    
    public MessSkillWalk() {}

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
        Actor actor = playService.findActor(actorId);
        if (actor == null) return;
        
//        SkillData skillData = skillService.getSkill(actor, skillId);
        skillService.playWalk(actor, skillId, dir, face, force);
    }
    
    
}
