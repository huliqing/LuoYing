/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class ModuleData extends ObjectData {
    
    /**
     * 获取module的载入顺序
     * @return 
     */
    public int getModuleOrder() {
        // 这个参数不需要放在本地实例中，所以直接从proto中获取即可。
        return getAsInteger("moduleOrder", 0);
    }
    
}
