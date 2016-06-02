/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.ActionData;
import name.huliqing.fighter.data.Proto;

/**
 *
 * @author huliqing
 */
public class ActionDataLoader implements DataLoader<ActionData>{

    @Override
    public ActionData loadData(Proto proto) {
        ActionData data = new ActionData(proto.getId());
        return data;
    }
    
}
