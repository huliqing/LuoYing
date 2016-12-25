/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.action;

import com.jme3.input.controls.ActionListener;
import name.huliqing.editor.Editor;

/**
 *
 * @author huliqing
 */
public abstract class Action implements ActionListener {
    
    protected Editor editor;
    
    public Action(Editor editor) {
        this.editor = editor;
    }
}
