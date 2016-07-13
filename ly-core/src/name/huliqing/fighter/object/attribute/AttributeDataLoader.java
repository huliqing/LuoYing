/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.attribute;

import name.huliqing.fighter.data.AttributeData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.object.DataLoader;

/**
 *
 * @author huliqing
 */
public class AttributeDataLoader implements DataLoader<AttributeData> {

    @Override
    public void load(Proto proto, AttributeData store) {
        store.setEl(proto.getAttribute("el"));
    }
    
}
