/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

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
