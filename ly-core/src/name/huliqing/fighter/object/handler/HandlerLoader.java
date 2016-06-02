/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.handler;

import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.object.DataLoader;

/**
 *
 * @author huliqing
 */
public class HandlerLoader<T extends HandlerData> implements DataLoader<T>{

    @Override
    public void load(Proto proto, T store) {
        // ignore
    }
    
}
