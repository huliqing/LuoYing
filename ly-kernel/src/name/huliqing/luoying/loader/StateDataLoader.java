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
        // ignore
    }


}
