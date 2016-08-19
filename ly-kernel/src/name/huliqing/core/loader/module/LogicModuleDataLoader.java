/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.module;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.data.module.LogicModuleData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.xml.Proto;

/**
 *  载入LogicModule的逻辑配置
 * @author huliqing
 */
public class LogicModuleDataLoader implements DataLoader<LogicModuleData> {

    @Override
    public void load(Proto proto, LogicModuleData data) {
        String[] logicArr = proto.getAsArray("logics");
        if (logicArr != null && logicArr.length > 0) {
            data.setLogics(new ArrayList<ActorLogicData>(logicArr.length));
            for (String logicId : logicArr) {
                data.getLogics().add((ActorLogicData) DataFactory.createData(logicId));
            }
        }
    }

}
