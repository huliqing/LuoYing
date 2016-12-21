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

import name.huliqing.luoying.object.define.MatDefine;


/**
 * 拥有"质的"属性的物体,这里的质地Mat主要用于指物体的构成成分，不同的质地可能产生不同的碰撞声音。
 * @author huliqing
 */
public interface MatObject {
    
    /**
     * 获取物体的质的类型。
     * @return 
     */
    int getMat();
    
    /**
     * 设置质地
     * @param mat 
     * @see MatDefine#getMat(java.lang.String) 
     */
    void setMat(int mat);

}
