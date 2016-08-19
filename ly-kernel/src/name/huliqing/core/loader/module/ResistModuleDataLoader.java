/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.module;

import name.huliqing.core.data.ResistData;
import name.huliqing.core.data.module.ResistModuleData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.xml.Proto;

/**
 * 载入抗性设置
 * @author huliqing
 */
public class ResistModuleDataLoader implements DataLoader<ResistModuleData> {

    @Override
    public void load(Proto proto, ResistModuleData data) {
        String resistId = proto.getAsString("resist");
        if (resistId != null) {
            data.setResist((ResistData) DataFactory.createData(resistId));
        }
    }

}
