/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import java.util.List;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.edit.JmeEdit;

/**
 * 编辑工具接口
 * @author huliqing
 * @param <F> Form类型
 */
public interface Toolbar<F extends JmeEdit> {
    
    /**
     * 初始化工具栏
     */
    void initialize();
    
    /**
     * 判断工具栏是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
   /**
     * 工具栏更新，这个方法会在编辑窗口运行过程中持续更新
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 设置是否激活工具栏,只有工具栏处于激活状态时，工具才应该响应事件。
     * @param enabled 
     */
    void setEnabled(boolean enabled);
    
    /**
     * 判断是否工具栏已经激活
     * @return 
     */
    boolean isEnabled();
    
    /**
     * 清理工具栏
     */
    void cleanup();
    
    /**
     * 添加一个编辑工具
     * @param <T>
     * @param tool 编辑工具
     * @return 
     */
    <T extends Toolbar> T add(Tool tool);
    
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
     * 找到指定的工具
     * @param <T>
     * @param clazz
     * @return 
     */
    <T extends Tool> T getTool(Class<? extends Tool> clazz);
    
    /**
     * 获取所有工具栏
     * @return 
     */
    List<Tool> getTools();
    
    /**
     * 打开或关闭一个工具, 注：如果是关闭工具，如果工具当前处于激活状态，则该工具会被自动取消激活。
     * {@link #setActivated(java.lang.String, boolean) }
     * @param <T>
     * @param tool
     * @param enabled 
     * @return  
     */
    <T extends Toolbar> T setEnabled(Tool tool, boolean enabled);
    
    /**
     * 获取所有可用的工具
     * @return 
     */
    List<Tool> getToolsEnabled();
    
    /**
     * 添加一个侦听器，用于侦听工具的添加、移除、关闭、激活等操作
     * @param listener 
     */
    void addListener(ToolbarListener listener);
    
    /**
     * 移除指定的侦听器
     * @param listener
     * @return 
     */
    boolean removeListener(ToolbarListener listener);
    
    /**
     * 获取工具栏名称
     * @return 
     */
    String getName();
    
    F getEdit();
}
