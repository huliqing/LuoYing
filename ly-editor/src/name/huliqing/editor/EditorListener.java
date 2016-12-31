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
    
    void onFormChanged(Editor editor, Form newForm);
}
