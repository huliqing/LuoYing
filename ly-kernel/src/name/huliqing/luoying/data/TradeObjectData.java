/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
import name.huliqing.luoying.xml.SimpleCloner;
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
    public TradeObjectData clone() {
        TradeObjectData clone = (TradeObjectData) super.clone();
        clone.tradeInfos = SimpleCloner.deepClone(tradeInfos);
        return clone;
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
