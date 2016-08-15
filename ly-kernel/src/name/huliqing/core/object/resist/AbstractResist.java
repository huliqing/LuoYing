/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.resist;

import name.huliqing.core.data.ResistData;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractResist<T extends ResistData> implements Resist<T> {

    private T data;

    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public T getData() {
        return data;
    }

//    @Override
//    public Resist clone() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
    
    
}
