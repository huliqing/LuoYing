/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.game.service.SkillService;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
@Serializable
public class MessSkill extends MessSkillAbstract {

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        SkillService skillService = Factory.get(SkillService.class);
        Actor actor = playService.findActor(actorId);
        if (actor == null) return;
//        SkillData skillData = skillService.getSkill(actor, skillId);
        skillService.playSkill(actor, skillId, force);
    }
    
  
}
