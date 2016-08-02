/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.dao;

import com.jme3.math.FastMath;
import java.util.List;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.utils.Temp;

/**
 *
 * @author huliqing
 */
public class SkillDaoImpl implements SkillDao {

    @Override
    public void inject() {
        // 
    }
    
    @Override
    public List<SkillData> getSkills(ActorData actorData) {
        return actorData.getSkillStore().getSkills();
    }

    @Override
    public SkillData getSkillById(ActorData data, String skillId) {
        List<SkillData> skills = data.getSkillStore().getSkills();
        if (skills == null) {
            return null;
        }
        for (SkillData sd : skills) {
            if (sd.getId().equals(skillId)) {
                return sd;
            }
        }
        return null;
    }
  
    @Override
    public SkillData getSkillFirst(ActorData actorData, SkillType skillType) {
        List<SkillData> skills = actorData.getSkillStore().getSkills();
        if (skills == null) {
            return null;
        }
        for (SkillData sd : skills) {
            if (sd.getSkillType() == skillType) {
                return sd;
            }
        }
        return null;
    }
    
    @Override
    public SkillData getSkillRandom(ActorData actorData, SkillType st, int weaponState) {
        List<SkillData> skills = actorData.getSkillStore().getSkills();
        if (skills == null) {
            return null;
        }
        Temp temp = Temp.get();
        List list = temp.list1;
        list.clear();
        SkillData result = null;
        for (SkillData sd : skills) {
            if (sd.getSkillType() != st) {
                continue;
            }
            
            if (sd.getWeaponStateLimit() == null || sd.getWeaponStateLimit().contains(weaponState)) {
                list.add(sd);
            }
        }
        if (list.size() > 0) {
            result = (SkillData) list.get(FastMath.nextRandomInt(0, list.size() - 1));
        }
        list.clear();
        temp.release();
        
        return result;
    }

    @Override
    public SkillData getDefendSkillRandom(ActorData actorData, int weaponState) {
        List<SkillData> skills = actorData.getSkillStore().getSkills();
        if (skills == null) {
            return null;
        }
        Temp temp = Temp.get();
        List list = temp.list1;
        list.clear();
        SkillData result = null;
        for (SkillData sd : skills) {
            if (sd.getSkillType() != SkillType.defend) {
                continue;
            }
            if (sd.getWeaponStateLimit() == null || sd.getWeaponStateLimit().contains(weaponState)) {
                list.add(sd);
            }
        }
        if (list.size() > 0) {
            result = (SkillData) list.get(FastMath.nextRandomInt(0, list.size() - 1));
        }
        list.clear();
        temp.release();
        
        return result;
    }
    
    @Override
    public SkillData getDuckSkillRandom(ActorData actorData, int weaponState) {
        List<SkillData> skills = actorData.getSkillStore().getSkills();
        if (skills == null) {
            return null;
        }
        Temp temp = Temp.get();
        List list = temp.list1;
        list.clear();
        SkillData result = null;
        for (SkillData sd : skills) {
            if (SkillType.duck != sd.getSkillType()) {
                continue;
            }
            if (sd.getWeaponStateLimit() == null || sd.getWeaponStateLimit().contains(weaponState)) {
                list.add(sd);
            }
        }
        if (list.size() > 0) {
            result = (SkillData) list.get(FastMath.nextRandomInt(0, list.size() - 1));
        }
        list.clear();
        temp.release();
        
        return result;
    }
    
    @Override
    public SkillData getAttackSkillRandom(ActorData actorData, int weaponState) {
        List<SkillData> skills = actorData.getSkillStore().getSkills();
        if (skills == null) {
            return null;
        }
        Temp temp = Temp.get();
        List list = temp.list1;
        list.clear();
        SkillData result = null;
        for (SkillData sd : skills) {
            if (sd.getSkillType() != SkillType.attack) {
                continue;
            }
            
            if (sd.getWeaponStateLimit() == null || sd.getWeaponStateLimit().contains(weaponState)) {
                list.add(sd);
            }
        }
        if (list.size() > 0) {
            result = (SkillData) list.get(FastMath.nextRandomInt(0, list.size() - 1));
        }
        list.clear();
        temp.release();
        
        return result;
    }

    @Override
    public boolean hasSkill(ActorData data, String skillId) {
        List<SkillData> skills = data.getSkillStore().getSkills();
        for (SkillData skill : skills) {
            if (skill.getId().equals(skillId)) {
                return true;
            }
        }
        return false;
    }
    
    
}
