/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import javafx.scene.Node;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.Tool;

/**
 * @author huliqing
 * @param <T>
 */
public interface JfxTool<T extends Tool> {
    
    void setToolbar(Toolbar toolbar);
    
    void setTool(T tool);
    
    /**
     * 获取编辑工具的渲染UI
     * @return 
     */
    Node getView();
    
    /**
     * 设置激活与否
     * @param activated 
     */
    void setActivated(boolean activated);
    
    /**
     * 设置工具是否可用。
     * @param enabled 
     */
    void setEnabled(boolean enabled);
    
    /**
     * 初始化toolview
     */
    void initialize();
}
