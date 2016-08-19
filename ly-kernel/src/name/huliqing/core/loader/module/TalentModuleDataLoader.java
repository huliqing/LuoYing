/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.module;

import java.util.ArrayList;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.data.module.TalentModuleData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.xml.Proto;

/**
 *
 * @author huliqing
 */
public class TalentModuleDataLoader implements DataLoader<TalentModuleData> {

    @Override
    public void load(Proto proto, TalentModuleData data) {
        String[] talentArr = proto.getAsArray("talents");
        if (talentArr != null) {
            data.setTalentDatas(new ArrayList<TalentData>(talentArr.length));
            for (String tid : talentArr) {
                data.getTalentDatas().add((TalentData)DataFactory.createData(tid));
            }
        }
        
        data.setTalentPoints(proto.getAsInteger("talentPoints", 0));
        data.setTalentPointsLevelEl(proto.getAsString("talentPointsLevelEl"));
    }

}
