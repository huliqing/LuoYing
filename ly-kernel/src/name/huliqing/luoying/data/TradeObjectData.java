/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * 一个普通的可量化的物品的基类，可以直接作为其它可量化物体的基类。
 * @author huliqing
 */
@Serializable
public class TradeObjectData extends ObjectData implements TradeObject, CountObject {
    
    private List<TradeInfo> tradeInfos;
    
    @Override
    public int getTotal() {
        return getAsInteger("total", 0);
    }

    @Override
    public void setTotal(int total) {
        setAttribute("total", total);
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
        oc.writeSavableArrayList(new ArrayList<TradeInfo>(tradeInfos), "tradeInfos", null);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        tradeInfos = ic.readSavableArrayList("tradeInfos", null);
    }
}
