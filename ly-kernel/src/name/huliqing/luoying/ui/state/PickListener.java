/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.ui.state;

/**
 * 鼠标点击时的事件监听
 * @author huliqing
 */
public interface PickListener {
    
    /**
     * 如果选择成功则返回true,否则返回false
     * @param isPressed
     * @param tpf
     * @return 
     */
    boolean pick(boolean isPressed, float tpf);
        
}
