/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import name.huliqing.editor.toolbar.Toolbar;

/**
 *
 * @author huliqing
 */
public interface JmeEditListener {
    
    void onToolbarChanged(JmeEdit jmeEdit, Toolbar newToolbar);
}
