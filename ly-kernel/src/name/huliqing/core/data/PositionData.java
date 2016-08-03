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
public class PositionData extends ProtoData {

    public PositionData(){}
    
    public PositionData(String id) {
        super(id);
    }

}
