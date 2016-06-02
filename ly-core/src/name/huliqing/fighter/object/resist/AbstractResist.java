/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.resist;

import java.util.List;
import name.huliqing.fighter.data.ResistData;

/**
 *
 * @author huliqing
 */
public abstract class AbstractResist implements Resist {

    private ResistData data;
    
    public AbstractResist() {}
    
    public AbstractResist(ResistData data) {
        this.data = data;
    }
    
    @Override
    public ResistData getResistData() {
        return data;
    }

    @Override
    public Resist clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
