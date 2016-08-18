/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import name.huliqing.core.data.HandlerData;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class HandlerDataLoader<T extends HandlerData> implements DataLoader<T>{

    @Override
    public void load(Proto proto, T store) {
        // ignore
    }
    
}
