/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.toolbar;

import javafx.scene.layout.Region;
import name.huliqing.editor.toolbar.Toolbar;

/**
 * JFX工具栏，用于渲染JME工具栏
 * @author huliqing
 */
public interface JfxToolbar {

    void initialize();
    
    boolean isInitialized();
    
    void cleanup();
    
    /**
     * 设置jme toolbar
     * @param toolbar 
     */
    void setToolbar(Toolbar toolbar);
    
    /**
     * 获取工具栏名称
     * @return 
     */
    String getName();
    
    /**
     * 获取工具栏的面板
     * @return 
     */
    Region getView();
    
}
