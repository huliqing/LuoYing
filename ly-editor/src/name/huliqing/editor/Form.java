/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

/**
 *
 * @author huliqing
 */
public interface Form {
    
    void initialize(Editor editor);
    
    boolean isInitialized();
    
    void update(float tpf);
    
    void cleanup();
    
    /**
     * 获取整个编辑器
     * @return 
     */
    Editor getEditor();
    
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
}
