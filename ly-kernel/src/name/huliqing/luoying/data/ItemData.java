/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.data.define.CountObjectImpl;
import name.huliqing.luoying.data.define.TradeObject;

/**
 * 基本的原始物体数据
 * @author huliqing
 */
@Serializable
public class ItemData extends CountObjectImpl implements TradeObject{
    
    @Override
    public float getCost() {
        return getAsFloat("cost", 0);
    }
    
    /**
     * 判断物品是否是可删除的 
     * @return 
     */
    public boolean isDeletable() {
        return getAsBoolean("deletable", true);
    }

    /**
     * 设置物品是否是可删除的
     * @param deletable 
     */
    public void setDeletable(boolean deletable) {
        setAttribute("deletable", deletable);
    }
    
    /**
     * 判断物品是否是可以出售的
     * @return 
     */
    public boolean isSellable() {
        return getAsBoolean("sellable", true);
    }
    
    /**
     * 设置物品是否是可以出售的。
     * @param sellable 
     */
    public void setSellable(boolean sellable) {
        setAttribute("sellable", sellable);
    }
    
}
