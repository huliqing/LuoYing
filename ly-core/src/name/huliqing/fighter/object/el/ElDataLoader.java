/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.el;

import name.huliqing.fighter.data.ElData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.object.DataLoader;

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
