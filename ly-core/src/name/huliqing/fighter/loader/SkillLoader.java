/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.object.skill.Skill;
import name.huliqing.fighter.object.skill.impl.AttackSkill;
import name.huliqing.fighter.object.skill.impl.BackSkill;
import name.huliqing.fighter.object.skill.impl.DeadRagdollSkill;
import name.huliqing.fighter.object.skill.impl.DeadSkill;
import name.huliqing.fighter.object.skill.impl.DefendSkill;
import name.huliqing.fighter.object.skill.impl.DuckSkill;
import name.huliqing.fighter.object.skill.impl.HurtSkill;
import name.huliqing.fighter.object.skill.impl.IdleSkill;
import name.huliqing.fighter.object.skill.impl.ReadySkill;
import name.huliqing.fighter.object.skill.impl.ResetSkill;
import name.huliqing.fighter.object.skill.impl.RunSkill;
import name.huliqing.fighter.object.skill.impl.ShotBowSkill;
import name.huliqing.fighter.object.skill.impl.ShotSkill;
import name.huliqing.fighter.object.skill.impl.SkinSkill;
import name.huliqing.fighter.object.skill.impl.SummonSkill;
import name.huliqing.fighter.object.skill.impl.WaitSkill;
import name.huliqing.fighter.object.skill.impl.WalkSkill;

/**
 *
 * @author huliqing
 */
class SkillLoader {
    
    public static Skill loadSkill(SkillData data) {
        // ==== v2 
        String tagName = data.getProto().getTagName();
        if (tagName.equals("skillWalk")) {
            return new WalkSkill(data);
        } 

        if (tagName.equals("skillRun")) {
            return new RunSkill(data);
        } 

        if (tagName.equals("skillWait")) {
            return new WaitSkill(data);
        }

        if (tagName.equals("skillIdle")) {
            return new IdleSkill(data);
        } 

        if (tagName.equals("skillHurt")) {
            return new HurtSkill(data);
        }            

        if (tagName.equals("skillDead")) {
            return new DeadSkill(data);
        } 

        if (tagName.equals("skillDeadRagdoll")) {
            return new DeadRagdollSkill(data);
        } 

        if (tagName.equals("skillAttack")) {
            return new AttackSkill(data);
        }
        
        // 射击
        if (tagName.equals("skillShot")) {
            return new ShotSkill(data);
        } 
        
        // 射箭
        if (tagName.equals("skillShotBow")) {
            return new ShotBowSkill(data);
        }
        
        if (tagName.equals("skillSummon")) { // 召唤技
            return new SummonSkill(data);
        } 

        if (tagName.equals("skillBack")) { // 回城技
            return new BackSkill(data);
        } 

        if (tagName.equals("skillReady")) {
            return new ReadySkill(data);
        }

        if (tagName.equals("skillDefend")) {
            return new DefendSkill(data);
        }

        if (tagName.equals("skillDuck")) {
            return new DuckSkill(data);
        }
        
        if (tagName.equals("skillReset")) {
            return new ResetSkill(data);
        }
        
        if (tagName.equals("skillSkin")) {
            return new SkinSkill(data);
        }
        
        throw new UnsupportedOperationException("Unsupported TagName=" + tagName);
    }
    

}
