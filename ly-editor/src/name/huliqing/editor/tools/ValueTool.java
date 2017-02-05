/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

/**
 * 输入值的工具
 * @author huliqing
 * @param <V>
 */
public interface ValueTool<V> extends Tool {
    
    /**
     * 设置值，并返回当前实例
     * @param <T>
     * @param value
     * @return 
     */
    <T extends ValueTool> T setValue(V value);
    
    V getValue();
    
    void addValueChangeListener(ValueChangedListener<V> listener);
    
    boolean removeValueChangeListener(ValueChangedListener<V> listener);
}
