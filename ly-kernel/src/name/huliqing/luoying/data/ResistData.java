/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.network.serializing.Serializable;

/**
 * ResistData用于定义抗性能力
 * @author huliqing
 */
@Serializable
public class ResistData extends ObjectData {
    
    public String getIcon() {
        return getAsString("icon");
    }
    
    /**
     * 获取抗性值
     * @return 
     */
    public float getValue() {
        return getAsFloat("value");
    }

    /**
     * 设置抗性值
     * @param value 
     */
    public void setValue(float value) {
        this.setAttribute("value", value);
    }

}
