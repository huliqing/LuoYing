/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.logic;

import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.object.DataLoader;

/**
 *
 * @author huliqing
 */
public class LogicDataLoader implements DataLoader<LogicData> {

    @Override
    public void load(Proto proto, LogicData data) {
        data.setInterval(proto.getAsFloat("interval", 1.0f));
    }
    
}
