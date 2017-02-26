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
    
    /**
     * 初始化
     * @param editor 
     */
    void initialize(Editor editor);
    
    /**
     * 更新逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理编辑器
     */
    void cleanup();
    
    /**
     * 回退历史记录
     */
    void undo();
    
    /**
     * 重做历史记录
     */
    void redo();
    
    /**
     * 添加一个编辑操作的历史记录
     * @param ur 
     */
    void addUndoRedo(UndoRedo ur);
    
    /**
     * 判断当前编辑器是否已经经过修改，一般这表示编辑器在退出的时候需要进行保存操作。
     * 可调用{@link #save() }来保存操作
     * @return 
     */
    boolean isModified();
    
    /**
     * 设置、标记编辑器是否经过编辑操作。
     * @param modified 
     */
    void setModified(boolean modified);
    
    /**
     * 保存编辑操作
     */
    void save();
    
    /**
     * 获取编辑器
     * @return 
     */
    Editor getEditor();
    
    /**
     * 获取JmeEdit
     * @return 
     */
    T getJmeEdit();
    
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
