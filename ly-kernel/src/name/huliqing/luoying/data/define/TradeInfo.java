/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data.define;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import name.huliqing.luoying.LuoYingException;

/**
 * TradeInfo定义物品的交易信息，相当于定义物品的价值，一件物品需要多少指定的物品进行交换才可以获得。
 * @author huliqing
 */
@Serializable
public class TradeInfo implements Savable, Cloneable {
    
    // 指定的物品ID
    private String objectId;
    
    // 物品的数量
    private int count;

    /**
     * 获取物品的ID
     * @return 
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * 设置物品的id
     * @param objectId 
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * 获取需要的物品数量
     * @return 
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置物品数量
     * @param count 
     */
    public void setCount(int count) {
        this.count = count;
    }
    
    @Override
    public TradeInfo clone() {
        try {
            return (TradeInfo) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new LuoYingException(ex);
        }
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(objectId, "objectId", null);
        oc.write(count, "count", 0);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        objectId = ic.readString("objectId", null);
        count = ic.readInt("count", 0);
    }
}
