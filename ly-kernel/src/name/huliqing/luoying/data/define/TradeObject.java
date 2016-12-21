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

import java.util.List;

/**
 * TradeObject定义了一类可以进行交易的物品,这类物品存在相应的价值，可以通过另外一些物品进行交易获得。
 * 例如：一件武器需要一些其它物品（如金币）来交换获得，那么武器就可以定义为TradeObject.
 * @author huliqing
 */
public interface TradeObject extends CountObject {
    
    /**
     * 获取物品的交易信息列表，返回的这个列表表示了物品的价值.如果返回null,则说明这件物品毫无价值.
     * @return 
     */
    List<TradeInfo> getTradeInfos();
    
    /**
     *  设置物品的交易信息列表.
     * @param tradeInfos 
     */
    void setTradeInfos(List<TradeInfo> tradeInfos);
}
