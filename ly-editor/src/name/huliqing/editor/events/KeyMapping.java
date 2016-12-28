/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.events;

/**
 * KeyMapping用于匹配按键事件，每一个KeyMapping只匹配一个按键。
 * @author huliqing
 */
public interface KeyMapping {
    
     /**
     * 初始化按键匹配
     */
    void initialize();
    
    /**
     * 判断是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理按键绑定，并释放资源
     */
    void cleanup();
    
    /**
     * 获取匹配名称
     * @return 
     */
    String getMappingName();
    
    /**
     * 判断按键是否匹配
     * @return 
     */
    boolean isMatch();
    
    /**
     * 添加一个按键匹配侦听器
     * @param listener 
     */
    void addListener(KeyMappingListener listener);
    
    /**
     * 移除一个按键匹配侦听器
     * @param listener
     * @return true如果侦听器存在
     */
    boolean removeListener(KeyMappingListener listener);
 
}
