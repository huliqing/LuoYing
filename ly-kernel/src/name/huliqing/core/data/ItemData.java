/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.network.serializing.Serializable;
import name.huliqing.core.data.define.CostObject;
import name.huliqing.core.data.define.HandlerObject;

/**
 * 基本的原始物体数据
 * @author huliqing
 */
@Serializable
public class ItemData extends PkgItemData implements CostObject, HandlerObject{
    
    public ItemData() {} 

    @Override
    public float getCost() {
        return getAsFloat("cost", 0);
    }
    
    @Override
    public String getHandler() {
        return getAttribute("handler");
    }
    
}
