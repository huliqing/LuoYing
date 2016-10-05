/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.loader;

import name.huliqing.ly.data.ElData;
import name.huliqing.ly.xml.Proto;
import name.huliqing.ly.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class ElDataLoader implements DataLoader<ElData> {

    @Override
    public void load(Proto proto, ElData data) {
        data.setExpression(proto.getAsString("expression"));
    }
    
}
