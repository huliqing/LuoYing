/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.el;

import name.huliqing.core.data.ElData;
import name.huliqing.core.data.Proto;
import name.huliqing.core.object.DataLoader;

/**
 *
 * @author huliqing
 */
public class ElDataLoader implements DataLoader<ElData> {

    @Override
    public void load(Proto proto, ElData data) {
        data.setExpression(proto.getAttribute("expression"));
    }
    
}
