/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

/**
 * 操作行为物体
 * @author huliqing
 */
public interface ActionObj {
    
    /**
     * 开始行为操作
     */
    void onActionStart();
    
    /**
     * 结束行为操作
     */
    void onActionEnd();
    
    /**
     * 判断是否可见
     * @return 
     */
    boolean isVisible();
    
    void setVisible(boolean visible);
}
