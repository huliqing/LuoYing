/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import java.util.List;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.ToolbarListener;

/**
 * 编辑工具接口
 * @author huliqing
 * @param <F> Form类型
 */
public interface Toolbar<F extends Form> {
    
    void initialize();
    
    boolean isInitialized();
    
   /**
     * 工具栏更新，这个方法会在编辑窗口运行过程中持续更新
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理工具栏
     */
    void cleanup();
    
    /**
     * 添加一个编辑工具
     * @param tool 编辑工具
     */
    void add(Tool tool);
    
    /**
     * 移除指定的工具
     * @param tool
     * @return 
     */
    boolean remove(Tool tool);
    
    /**
     * 找到指定的工具
     * @param tool
     * @return 
     */
    Tool getTool(String tool);
    
    /**
     * 获取所有工具栏
     * @return 
     */
    List<Tool> getTools();

    /**
     * 激活或取消激活指定的工具, 当激活一个工具时，如果该工具处于disabled状态，则该工具会被自动设置为enabled状态.
     * {@link #setEnabled(java.lang.String, boolean) }
     * @param tool
     * @param activated 
     */
    void setActivated(Tool tool, boolean activated);
    
    /**
     * 通过名称来激活指定的tool
     * @param tool
     * @param activated 
     */
    void setActivated(String tool, boolean activated);
    
    /**
     * 打开或关闭一个工具, 注：如果是关闭工具，如果工具当前处于激活状态，则该工具会被自动取消激活。
     * {@link #setActivated(java.lang.String, boolean) }
     * @param tool
     * @param valid 
     */
    void setEnabled(String tool, boolean valid);
    
    void addListener(ToolbarListener listener);
    
    boolean removeListener(ToolbarListener listener);
    
    /**
     * 获取工具栏所在的当前编辑窗口
     * @return 
     */
    F getForm();
    
    void setForm(F form);
}
