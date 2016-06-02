/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.dao;

import java.util.List;
import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.ActorData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.enums.SkillType;

/**
 *
 * @author huliqing
 */
public interface SkillDao extends Inject{
    
    
    List<SkillData> getSkills(ActorData data);
    
    SkillData getSkillById(ActorData data, String skillId);
    
    SkillData getSkillFirst(ActorData data, SkillType skillType);
    
    SkillData getSkillRandom(ActorData data, SkillType st, int weaponState);
    
//    SkillData getReadySkillRandom(ActorData data, int weaponState);
    
    SkillData getDefendSkillRandom(ActorData data, int weaponState);
    
    SkillData getDuckSkillRandom(ActorData data, int weaponState);
    
    SkillData getAttackSkillRandom(ActorData data, int weaponState);
    
    /**
     * 判断角色是否存在指定ID的技能
     * @param data
     * @param skillId
     * @return 
     */
    boolean hasSkill(ActorData data, String skillId);
}
