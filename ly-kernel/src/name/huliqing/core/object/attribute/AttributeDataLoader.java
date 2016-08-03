/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import name.huliqing.core.data.AttributeData;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.xml.DataLoader;

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
