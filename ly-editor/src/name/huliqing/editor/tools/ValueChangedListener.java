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
public interface ValueChangedListener<V> {
    
    void onValueChanged(ValueTool<V> vt, V oldValue, V newValue);
    
}
