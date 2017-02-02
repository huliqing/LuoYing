/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.controls.entity;

import name.huliqing.luoying.data.EntityData;

/**
 *
 * @author huliqing
 */
public interface EntityControlTileListener {
    
    /**
     * 当属性变化时该方法被调用 
     * @param data
     * @param property
     * @param value 
     */
    void onPropertyChanged(EntityData data, String property, Object value);
}
