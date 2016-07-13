/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.position;

import name.huliqing.fighter.data.PositionData;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractPosition<T extends PositionData> implements Position<T> {

    protected T data;

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }
    
}
