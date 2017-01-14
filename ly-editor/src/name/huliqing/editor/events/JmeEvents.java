/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.events;

/**
 *
 * @author huliqing
 */
public class JmeEvents {
    
    private final JmeEvent[] events;
    private boolean consumed;
    
    /**
     * 设置事件
     * @param events 
     */
    public JmeEvents(JmeEvent[] events) {
        this.events = events;
    }
    
    /**
     * 获取当前所有有响应的事件
     * @return 
     */
    public JmeEvent[] getEvents() {
        return events;
    }
    
    /**
     * 销毁所有事件，调用该方法后，后续事件将不再响应。
     */
    public void consume() {
        consumed = true;
    }
    
    /**
     * 判断所有事件是否销毁
     * @return 
     */
    public boolean isConsumed() {
        return consumed;
    }
}
