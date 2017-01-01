/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import name.huliqing.editor.forms.Form;

/**
 *
 * @author huliqing
 */
public interface EditorListener {
    
    /**
     * 当切换编辑器时该方法被调用
     * @param editor
     * @param newForm 
     */
    void onFormChanged(Editor editor, Form newForm);
    
    /**
     * 当编辑器窗品大小变化时该方法被调用
     * @param w
     * @param h 
     */
    void onReshape(int w, int h);
}
