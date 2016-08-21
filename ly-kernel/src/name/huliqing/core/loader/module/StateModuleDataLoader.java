/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.module;

import java.util.ArrayList;
import name.huliqing.core.data.StateData;
import name.huliqing.core.data.module.StateModuleData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.xml.Proto;

/**
 *
 * @author huliqing
 */
public class StateModuleDataLoader implements DataLoader<StateModuleData> {

    @Override
    public void load(Proto proto, StateModuleData data) {
//        String[] stateArr = proto.getAsArray("states");
//        if (stateArr != null && stateArr.length > 0) {
//            data.setStateDatas(new ArrayList<StateData>(stateArr.length));
//            for (String stateId : stateArr) {
//                data.getStateDatas().add((StateData) DataFactory.createData(stateId));
//            }
//        }
    }

}
