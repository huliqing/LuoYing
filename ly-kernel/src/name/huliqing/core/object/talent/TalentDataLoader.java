/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.talent;

import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class TalentDataLoader implements DataLoader<TalentData> {

    @Override
    public void load(Proto proto, TalentData data) {
        data.setInterval(proto.getAsFloat("interval", 10));
        data.setLevel(proto.getAsInteger("level", 0));
        data.setMaxLevel(proto.getAsInteger("maxLevel", 10));
    }
    
}
