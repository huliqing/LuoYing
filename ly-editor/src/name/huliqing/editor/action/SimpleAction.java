/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.action;

import name.huliqing.editor.Editor;

/**
 * 普通的行为处理，不需要复杂的逻辑更新行为
 * @author huliqing
 */
public abstract class SimpleAction extends Action {
    
    public SimpleAction(Editor editor) {
        super(editor);
    }
    
}
