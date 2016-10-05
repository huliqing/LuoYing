/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.ui;

/**
 *
 * @author huliqing
 */
public interface Title extends UI {
    
    /**
     * 设置标题
     * @param title 
     */
    void setTitle(UI title);
    
    /**
     * 获取退出按钮
     * @return 
     */
    UI getExitButton();
}
