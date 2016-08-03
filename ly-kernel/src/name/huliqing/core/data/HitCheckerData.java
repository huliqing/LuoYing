/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import name.huliqing.core.xml.ProtoData;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class HitCheckerData extends ProtoData {

    public HitCheckerData() {}
    
    public HitCheckerData(String id) {
        super(id);
    }
    
}
