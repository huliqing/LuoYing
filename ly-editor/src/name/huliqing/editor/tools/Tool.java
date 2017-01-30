/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import name.huliqing.editor.edit.JmeEdit;
import name.huliqing.editor.toolbar.Toolbar;

/**
 * 编辑工具
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public interface Tool<E extends JmeEdit, T extends Toolbar> {
    
    /**
     * 获取工具的名称
     * @return 
     */
    String getName();
    
    /**
     * 获取工具的描述或文字说明
     * @return 
     */
    String getTips();
    
    /**
     * 获取工具图标
     * @return 
     */
    String getIcon();
    
    void initialize(E jmeEdit, T toolbar);
    
    boolean isInitialized();
    
    void update(float tpf);

    void cleanup();

}
