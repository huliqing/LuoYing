/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import javafx.scene.layout.Pane;
import name.huliqing.editor.Editor;

/**
 * 编辑器的UI界面接口, 主要作为编辑器的UI编辑界面，包含各种编辑工具等。
 * @author huliqing
 * @param <T>
 */
public interface JfxEdit<T extends JmeEdit> {
    
    void initialize(Editor editor);
    
    void update(float tpf);
    
    void cleanup();
    
    void undo();
    
    void redo();
    
    void addUndoRedo(UndoRedo ur);
    
    Editor getEditor();
    
    /**
     * 获取当前编辑器的UI根节点
     * @return 
     */
    Pane getEditRoot();
    
    /**
     * 获取属性面板
     * @return 
     */
    Pane getPropertyPanel();
    
}
