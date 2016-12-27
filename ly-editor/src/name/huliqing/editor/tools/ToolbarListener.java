/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

/**
 *
 * @author huliqing
 */
public interface ToolbarListener {
    
    /**
     * 当向工具栏添加工具时该方法被调用
     * @param toolAdded 
     */
    void onToolAdded(Tool toolAdded);
    
    /**
     * 当从工具栏移除工具时
     * @param toolRemoved 
     */
    void onToolRemoved(Tool toolRemoved);
    
    /**
     * 当指定工具被激活时
     * @param tool 
     */
    void onToolActivated(Tool tool);
    
    /**
     * 当工具被取消激时
     * @param tool 
     */
    void onToolDeactivated(Tool tool);
    
    /**
     * 当指定工具被无效时
     * @param tool 
     */
    void onToolEnabled(Tool tool);
    
    /**
     * 当工具不可用时该方法被调用
     * @param tool 
     */
    void onToolDisabled(Tool tool);
}
