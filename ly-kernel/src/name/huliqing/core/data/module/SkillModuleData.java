/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data.module;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.SkillData;

/**
 *
 * @author huliqing
 */
@Serializable
public class SkillModuleData extends ModuleData {
    
    // 所有技能
    private List<SkillData> skills;

    public List<SkillData> getSkills() {
        return skills;
    }
    
    public void setSkills(List<SkillData> skills) {
        this.skills = skills;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        if (skills != null) {
            OutputCapsule oc = ex.getCapsule(this);
            oc.writeSavableArrayList(new ArrayList<SkillData>(skills), "skills", null);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        skills = ic.readSavableArrayList("skills", null);
    }
}
