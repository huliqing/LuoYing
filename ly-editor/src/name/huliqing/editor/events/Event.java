/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.events;

import java.util.List;

/**
 * 事件接口,一个事件可以绑定一个到多个键盘、鼠标按键来触发事件，当绑定的按键发生状态变化时会触发事件。
 * 通过调用{@link #isMatch() }方法来判断是否触发事件。
 * @author huliqing
 */
public interface Event {
    
    /**
     * 获取事件名称
     * @return 
     */
    String getName();
    
    /**
     * 初始化
     */
    void initialize();
    
    /**
     * 判断事件是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理事件
     */
    void cleanup();
    
    /**
     * 判断当前事件是否匹配。
     * @return 
     */
    boolean isMatch();
    
    /**
     * 添加一个按键匹配
     * @param keyMapping 
     */
    void addKeyMapping(KeyMapping keyMapping);
    
    /**
     * 移除一个按键匹配
     * @param keyMapping 
     * @return  
     */
    boolean removeKeyMapping(KeyMapping keyMapping);
    
    /**
     * 获取所有的按键绑定
     * @param <T>
     * @return 
     */
    <T extends KeyMapping> List<T> getKeyMappings();
    
    /**
     * 添加一个事件侦听器
     * @param listener 
     */
    void addListener(EventListener listener);
    
    /**
     * 移除指定的事件侦听器
     * @param listener
     * @return 
     */
    boolean removeListener(EventListener listener);
}
