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
package name.huliqing.luoying.xml;

/**
 * SimpleIdGenerator作为简单的ID生成器，使用当前系统时间的毫秒数作为初始ID，每次生成ID时在该基础上递增。
 * @author huliqing
 */
public class SimpleIdGenerator implements IdGenerator {

    private static long currentId = System.currentTimeMillis();
    
    @Override
    public synchronized long generateUniqueId() {
        return currentId++;
    }
    
}
