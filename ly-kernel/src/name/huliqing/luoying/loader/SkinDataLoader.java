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
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.AttributeApply;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.xml.DataLoader;
import name.huliqing.luoying.utils.ConvertUtils;

/**
 *
 * @author huliqing
 */
public class SkinDataLoader implements DataLoader<SkinData> {
    private final DefineService defineService = Factory.get(DefineService.class);

    @Override
    public void load(Proto proto, SkinData data) {
        // 交易信息
        data.setTradeInfos(TradeObjectLoaderHelper.loadTradeInfos(proto));
        // 装备属性
        String[] aaStr = proto.getAsArray("applyAttributes");
        if (aaStr != null) {
            ArrayList<AttributeApply> aas = new ArrayList<AttributeApply>(aaStr.length);
            for (int i = 0; i < aaStr.length; i++) {
                String[] aaArr = aaStr[i].split("\\|");
                aas.add(new AttributeApply(aaArr[0], ConvertUtils.toFloat(aaArr[1], 0)));
            }
            data.setApplyAttributes(aas);
        }
        // 设置质地,这里是必要的，因为需要将mat的字符串质地名称转化为int类型（索引）。
        data.setMat(defineService.getMatDefine().getMat(proto.getAsString("mat")));
    }
    
}
