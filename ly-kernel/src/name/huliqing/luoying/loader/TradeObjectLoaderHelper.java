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
package name.huliqing.luoying.loader;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.define.TradeInfo;
import name.huliqing.luoying.xml.Proto;

/**
 * 用于帮助载入tradeInfos参数。
 * @author huliqing
 */
public class TradeObjectLoaderHelper {
    
    /**
     * 载入TradeInfos信息，参数格式必须是:  tradeInfos="objectId|count,objectId|count,...", 如果没有指定tradeInfos则
     * 该方法返回null.
     * @param proto
     * @return 
     */
    public static List<TradeInfo> loadTradeInfos(Proto proto) {
        // format:  tradeInfos="objectId|count, objectId|count,..."
        String[] temps = proto.getAsArray("tradeInfos");
        if (temps != null) {
            List<TradeInfo> tradeInfos = new ArrayList<TradeInfo>();
            for (String temp : temps) {
                String[] tiArr = temp.split("\\|");
                TradeInfo tradeInfo = new TradeInfo();
                tradeInfo.setObjectId(tiArr[0]);
                if (tiArr.length > 1) {
                    tradeInfo.setCount(Integer.parseInt(tiArr[1]));
                } else {
                    tradeInfo.setCount(1);
                }
                tradeInfos.add(tradeInfo);
            }
            return tradeInfos;
        }
        return null;
    }
}
