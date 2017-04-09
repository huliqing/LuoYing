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
 * DataProcessor数据处理器
 * @author huliqing
 * @param <T>
 */
public interface DataProcessor<T extends ObjectData> {
 
    /**
     * 设置Data，这个data包含了DataProcessor要使用的所有可能的数据，在创建DataProcessor的时候这个方法会被自
     * 动调用，在运行时用户代码不应该再去改变这个data的引用。
     * @param data 
     */
    void setData(T data);
    
    /**
     * 获取data
     * @return 
     */
    T getData();
    
    /**
     * 更新实时状态数据到data中去,保持data的数据为当前的实时状态. <br>
     * 也就是说DataProcessor在运行时状态可能发生各种各样的变化，但是不需要实时更新到data中去，
     * 只在在调用这个方法时，才需要把当前状态更新到data中去，通常比如在存档之前都应该调用一次这个方法来将当前状
     * 态保存到data中去。
     */
    void updateDatas();
}
