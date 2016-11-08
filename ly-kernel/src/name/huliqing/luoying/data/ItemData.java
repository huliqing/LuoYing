/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.define.CountObject;
import name.huliqing.luoying.data.define.TradeInfo;
import name.huliqing.luoying.data.define.TradeObject;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 基本的原始物体数据
 * @author huliqing
 */
@Serializable
public class ItemData extends ObjectData implements CountObject, TradeObject{
    
    private List<TradeInfo> tradeInfos;
    
    @Override
    public int getTotal() {
        return getAsInteger("total", 1);
    }

    @Override
    public void setTotal(int total) {
        setAttribute("total", total);
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
    
    @Override
    public List<TradeInfo> getTradeInfos() {
        return tradeInfos;
    }

    @Override
    public void setTradeInfos(List<TradeInfo> tradeInfos) {
        this.tradeInfos = tradeInfos;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        if (tradeInfos != null) {
            oc.writeSavableArrayList(new ArrayList<TradeInfo>(tradeInfos), "tradeInfos", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        tradeInfos = ic.readSavableArrayList("tradeInfos", null);
    }
}
