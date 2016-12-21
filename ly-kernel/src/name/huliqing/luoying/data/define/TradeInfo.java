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
