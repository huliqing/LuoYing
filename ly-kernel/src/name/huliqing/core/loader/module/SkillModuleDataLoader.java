/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.module;

import java.util.ArrayList;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.data.module.SkillModuleData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.xml.Proto;

/**
 *
 * @author huliqing
 */
public class SkillModuleDataLoader implements DataLoader<SkillModuleData> {

    @Override
    public void load(Proto proto, SkillModuleData data) {
//        String[] skillIds = proto.getAsArray("skills");
//        if (skillIds != null && skillIds.length > 0) {
//            data.setSkills(new ArrayList<SkillData>(skillIds.length));
//            for (String skillId : skillIds) {
//                data.getSkills().add((SkillData) DataFactory.createData(skillId));
//            }
//        }
    }

}
