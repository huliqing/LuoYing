/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.position;

import name.huliqing.fighter.data.PositionData;

/**
 *
 * @author huliqing
 */
public abstract class AbstractPosition implements Position {

    protected PositionData data;

    public AbstractPosition(PositionData data) {
        this.data = data;
    }
    
}
