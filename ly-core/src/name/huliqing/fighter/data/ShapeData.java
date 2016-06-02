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
public class ShapeData extends ProtoData {
    
    public ShapeData() {}
    
    public ShapeData(String id) {
        super(id);
    }
}
