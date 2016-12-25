/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.action;

import name.huliqing.editor.Editor;

/**
 * 有更新逻辑功能需要的行为
 * @author huliqing
 */
public abstract class ComplexAction extends Action {

    public ComplexAction(Editor editor) {
        super(editor);
    }
    
    /**
     * 更新逻辑
     * @param tpf 
     */
    public abstract void update(float tpf);
    
}
