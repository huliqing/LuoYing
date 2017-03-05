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

import com.jme3.network.serializing.Serializable;
import java.util.List;
import name.huliqing.luoying.data.define.TradeInfo;
import name.huliqing.luoying.data.define.TradeObject;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 基本的原始物体数据
 * @author huliqing
 */
@Serializable
public class ItemData extends ObjectData implements TradeObject{
    
    @Override
    public int getTotal() {
        return getAsInteger("total", 0);
    }

    @Override
    public void setTotal(int total) {
        setAttribute("total", total);
    }
    
    /**
     * 获取图标，如果没有设置则返回null.
     * @return 
     */
    public String getIcon() {
        return getAsString("icon");
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
        return getAsSavableList("tradeInfos");
    }

    @Override
    public void setTradeInfos(List<TradeInfo> tradeInfos) {
        setAttributeSavableList("tradeInfos", tradeInfos);
    }
    
}
