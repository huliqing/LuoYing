/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import name.huliqing.editor.Editor;
import name.huliqing.editor.toolbar.JfxToolbar;

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
     * 设置主工具栏
     * @param toolbar 如果为null则移除工具栏
     */
    void setToolbar(JfxToolbar toolbar);

    /**
     * 添加扩展工具栏
     * @param name
     * @param toolPanel 
     */
    void addToolPanel(String name, Region toolPanel);
    
    /**
     * 移除扩展工具栏
     * @param toolPanelName 
     * @return  
     */
    boolean removeToolPanel(String toolPanelName);
    
    /**
     * 获取属性面板
     * @return 
     */
    Pane getPropertyPanel();
    
}
