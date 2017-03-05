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

/**
 * 魔法数据
 * @author huliqing
 */
@Serializable
public class MagicData extends ModelEntityData {
    
    /**
     * 获取魔法的释放源，这个方法返回的是一个entity的唯一id.
     * @return 
     */
    public long getSource() {
        return getAsLong("source");
    }

    /**
     * 设置魔法的释放源,如果要清除释放源，可以设置一个小于或等于0的值。
     * @param source 
     */
    public void setSource(long source) {
        setAttribute("source", source);
    }
    
    /**
     * 获得魔法所针对的目标对象,如果不存在则返回null.
     * @return 
     */
    public long[] getTargets() {
        return getAsLongArray("targets");
    }

    /**
     * 设置魔法的针对的目标对象, 当魔法是特别针对个别对象时，可以通过这个方法来设置目标对象。
     * @param targets 
     */
    public void setTargets(long[] targets) {
        setAttribute("targets", targets);
    }
}
