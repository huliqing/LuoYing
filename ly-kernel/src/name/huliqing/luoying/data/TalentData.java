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

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.network.serializing.Serializable;

/**
 * 天赋
 * @author huliqing
 */
@Serializable
public class TalentData extends ObjectData {
    
    public String getIcon() {
        return getAsString("icon");
    }
    
    public void setIcon(String icon) {
        setAttribute("icon", icon);
    }
    
    public int getMaxLevel() {
        return getAsInteger("maxLevel", 10);
    }

    public void setMaxLevel(int maxLevel) {
        setAttribute("maxLevel", maxLevel);
    }
    
    public int getLevel() {
        return getAsInteger("level", 1);
    }

    public void setLevel(int level) {
        setAttribute("level", level);
    }

    /**
     * 判断天赋等级是否已经达到最高
     * @return 
     */
    public boolean isMax() {
        return getLevel() >= getMaxLevel();
    }
    
}
