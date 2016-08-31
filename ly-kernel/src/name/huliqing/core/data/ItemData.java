/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.network.serializing.Serializable;
import java.util.List;
import name.huliqing.core.data.define.CostObject;
import name.huliqing.core.data.define.HandlerObject;

/**
 * 基本的原始物体数据
 * @author huliqing
 */
@Serializable
public class ItemData extends ObjectData implements CostObject, HandlerObject{
    
    // 属性限制
    private List<AttributeMatch> matchAttributes;
    
    // 判断物品是否是可删除的
    private boolean deletable = true;
    
    public ItemData() {} 

    @Override
    public float getCost() {
        return getAsFloat("cost", 0);
    }
    
    @Override
    public String getHandler() {
        return getAsString("handler");
    }

    /**
     * 获取属性限制
     * @return 
     */
    public List<AttributeMatch> getMatchAttributes() {
        return matchAttributes;
    }

    /**
     * 设置使用物品时的属性限制。
     * @param matchAttributes 
     */
    public void setMatchAttributes(List<AttributeMatch> matchAttributes) {
        this.matchAttributes = matchAttributes;
    }
    
    /**
     * 判断物品是否是可删除的 
     * @return 
     */
    public boolean isDeletable() {
        return deletable;
    }

    /**
     * 设置物品是否是可删除的
     * @param deletable 
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
}
