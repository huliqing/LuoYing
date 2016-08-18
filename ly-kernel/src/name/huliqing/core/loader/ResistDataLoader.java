/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.ResistData;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class ResistDataLoader implements DataLoader<ResistData> {

    @Override
    public void load(Proto proto, ResistData data) {
        // 默认抵抗率为0,也就是如果没有设置，也就没有抵抗
        data.setFactor(proto.getAsFloat("factor", 0));
    }
    
}
