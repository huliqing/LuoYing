/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.control;

import name.huliqing.ly.data.ControlData;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractControl<T extends ControlData> implements Control<T> {

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
