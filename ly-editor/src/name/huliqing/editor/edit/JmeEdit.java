/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import java.util.List;
import name.huliqing.editor.Editor;
import name.huliqing.editor.toolbar.Toolbar;

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
     * 获取主工具栏
     * @param <T>
     * @return 
     */
    <T extends Toolbar> T getToolbar();
    
    /**
     * 获取编辑器的扩展工具栏列表,如果没有则返回null.
     * @return 
     */
    List<Toolbar> getExtToolbars();
    
    Editor getEditor();
    
}
