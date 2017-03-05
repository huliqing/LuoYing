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

import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class DropDataLoader implements DataLoader<DropData> {

    @Override
    public void load(Proto proto, DropData data) {
        
        // remove20170306
//        // 基本掉落物品,format: id1|count, count可省略，默认为1.
//        List<DropItem> baseItems = null;
//        String[] baseDrop = proto.getAsArray("base");
//        if (baseDrop != null) {
//            baseItems = new ArrayList<DropItem>(baseDrop.length);
//            for (String drop : baseDrop) {
//                String[] dropArr = drop.split("\\|");
//                DropItem di = new DropItem(dropArr[0]);
//                if (dropArr.length >= 2) {
//                    di.setCount(ConvertUtils.toInteger(dropArr[1], 1));
//                }
//                baseItems.add(di);
//            }
//        }
//        
//        // 随机掉落物品,format: id1|count|factor
//        // factor和count可省略，默认factor=1,count=1
//        List<DropItem> randomItems = null;
//        String[] randomDrop = proto.getAsArray("random");
//        if (randomDrop != null) {
//            randomItems = new ArrayList<DropItem>(randomDrop.length);
//            for (String drop : randomDrop) {
//                String[] dropArr = drop.split("\\|");
//                DropItem di = new DropItem(dropArr[0]);
//                if (dropArr.length >= 2) {
//                    di.setCount(ConvertUtils.toInteger(dropArr[1], 1));
//                }
//                if (dropArr.length >= 3) {
//                    di.setFactor(ConvertUtils.toFloat(dropArr[2], 1));
//                }
//                randomItems.add(di);
//            }
//        }
//        
//        data.setBaseItems(baseItems);
//        data.setRandomItems(randomItems);
    }
    
}
