/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.actor;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.data.SkillData;

/**
 *
 * @author huliqing
 */
@Serializable
public class SkillStore implements Savable {
    
    // 所有技能
    private List<SkillData> skills;
    
    // 最后一次添加技能或移除技能的时间。
    private long lastModifyTime;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        if (skills != null)
            oc.writeSavableArrayList(new ArrayList<SkillData>(skills), "skills", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        skills.clear();
        List<SkillData> temp = ic.readSavableArrayList("skills", new ArrayList<SkillData>());
        if (temp != null) {
            skills.addAll(temp);
        }
        lastModifyTime = System.currentTimeMillis();
    }
    
    public SkillStore() {
        lastModifyTime = Common.getGameTime();
        skills = new ArrayList<SkillData>();
    }
    
    /**
     * 添加技能
     * @param skill 
     */
    public void add(SkillData skillData) {
        if (skills.contains(skillData)) {
            return;
        }
        for (int i = 0; i < skills.size(); i++) {
            if (skills.get(i).getId().equals(skillData.getId())) {
                return;
            }
        }
        skills.add(skillData);
        lastModifyTime = Common.getGameTime();
    }
    
    /**
     * 清理掉所有的技能
     */
    public void clear() {
        skills.clear();
        lastModifyTime = Common.getGameTime();
    }

    /**
     * 获取最近一次技能变更的时间，如新增技能，减少技能
     * @return 
     */
    public long getLastModifyTime() {
        return lastModifyTime;
    }

    /**
     * 获取所有技能，注意：该方法只允许只读，不要随便修改该列表。
     * @return 
     */
    public List<SkillData> getSkills() {
        return skills;
    }

    public SkillData getSkillById(String skillId) {
        if (skills == null) 
            return null;
        for (SkillData sd : skills) {
            if (sd.getId().equals(skillId)) {
                return sd;
            }
        }
        return null;
    }
}
