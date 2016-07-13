/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.hitchecker;

import name.huliqing.fighter.data.HitCheckerData;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractHitChecker<T extends HitCheckerData> implements HitChecker<T> {
        
    private T data;

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }
    
}
