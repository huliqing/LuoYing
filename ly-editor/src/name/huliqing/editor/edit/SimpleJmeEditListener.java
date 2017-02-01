/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import name.huliqing.editor.edit.controls.ControlTile;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface SimpleJmeEditListener<T extends ControlTile> {
    
    /**
     * 当编辑模式变化时该方法被调用
     * @param mode 
     */
    void onModeChanged(Mode mode);
    
    /**
     * 当选择物体时该方法被调用，只要有发生“选择物体”的操作时，该方法将被调用，不管选择的物体是否发生变化。
     * @param controlTile 被选择到的物体
     */
    void onSelect(T controlTile);
}
