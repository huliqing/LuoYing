/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

import name.huliqing.editor.select.SelectObj;

/**
 *
 * @author huliqing
 */
public interface EditFormListener {
    
    /**
     * 当编辑模式变化时该方法被调用
     * @param mode 
     */
    void onModeChanged(Mode mode);
    
    /**
     * 当选择的物体发生变化时该方法被调用。
     * @param selectObj 
     */
    void onSelectChanged(SelectObj selectObj);
}
