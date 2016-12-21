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
package name.huliqing.ly.view.actor;

import java.util.List;
import name.huliqing.luoying.data.AttributeApply;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.manager.ResourceManager;

/**
 *
 * @author huliqing
 */
public class SkinUtils {
    
    /**
     * 获取Skin的描述说明
     * @param skinData
     * @return 
     */
    public static String getSkinDes(SkinData skinData) {
        List<AttributeApply> aas = skinData.getApplyAttributes();
        if (aas != null) {
            StringBuilder sb = new StringBuilder();
            for (AttributeApply aa : aas) {
                sb.append(ResourceManager.getObjectName(aa.getAttribute()))
                        .append(":")
                        .append(aa.getAmount())
                        .append("  ");
            }
            return sb.toString();
        }
        return ResourceManager.get(ResConstants.COMMON_UNKNOW);
    }
}
