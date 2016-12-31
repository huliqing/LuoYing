/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.forms;

import name.huliqing.editor.toolbar.Toolbar;

/**
 *
 * @author huliqing
 */
public interface FormListener {
    
    void onToolbarChanged(Form form, Toolbar newToolbar);
}
