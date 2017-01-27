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
 *
 * @author huliqing
 */
public interface JfxTool {
    
    Tool getTool();
    
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
     * @param tool 工具
     * @param toolbar 工具栏
     * @param name 工具显示名称
     * @param tooltip 提示
     * @param icon 图标
     */
    void initialize(Tool tool, Toolbar toolbar, String name, String tooltip, String icon);
}
