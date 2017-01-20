/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import name.huliqing.editor.Editor;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.undoredo.UndoRedo;

/**
 * 编辑器的3D编辑器接口,这个界面主要作为3D环境来编辑3D物体
 * @author huliqing
 */
public interface JmeEdit {
    
    void initialize(Editor editor);
    
    boolean isInitialized();
    
    void update(float tpf);
    
    void cleanup();
    
    void undo();
    
    void redo();
    
    void addUndoRedo(UndoRedo ur);
    
    /**
     * 设置工具栏
     * @param toolbar 
     */
    void setToolbar(Toolbar toolbar);
    
    /**
     * 获取工具栏
     * @return 
     */
    Toolbar getToolbar();
        
    void addListener(JmeEditListener listener);
    
    boolean removeListener(JmeEditListener listener);
    
    Editor getEditor();
    
}
