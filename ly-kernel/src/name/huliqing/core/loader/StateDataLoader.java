/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.StateData;
import name.huliqing.core.xml.DataLoader;

/**
 * @author huliqing
 * @param <T>
 */
public class StateDataLoader<T extends StateData> implements DataLoader<StateData> {

    @Override
    public void load(Proto proto, StateData store) {
        store.setUseTime(proto.getAsFloat("useTime", 30));
        store.setInterval(proto.getAsFloat("interval", 3f));
        store.setEffects(proto.getAsArray("effects"));
        store.setRemoveOnDead(proto.getAsBoolean("removeOnDead", false));
        
        // 以下参数是在程序中动态设置的。
//        store.setResist(xxxx);
//        store.setSourceActor(xxx);
    }


}
