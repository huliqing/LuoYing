/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.xml.DataLoader;

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
//        store.setResist(aaa);
//        store.setSourceActor(aaa);
    }


}
