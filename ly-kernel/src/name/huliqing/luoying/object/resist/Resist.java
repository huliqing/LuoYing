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
package name.huliqing.luoying.object.resist;

import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 抗性
 * @author huliqing
 */
public interface Resist extends DataProcessor<ResistData>{
    
    /**
     * 初始化抗性
     */
    void initialize();
    
    /**
     * 判断抗性是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理抗性
     */
    void cleanup();
    
    /**
     * 设置抗性的持有目标。
     * @param entity 
     */
    void setEntity(Entity entity);
    
    /**
     * 获取抗性值，这个抗性值适应于当前抗性所能够抵抗的所有状态类型。
     * @return 
     * @see #isResistState(java.lang.String) 
     */
    float getResist();
    
    /**
     * 添加抗性值,注：抗性值取值范围为0.0~1.0,如果添加后的抗性值超出这个范围将会被截取。
     * @param resist 
     */
    void addResist(float resist);
    
    /**
     * 判断当前抗性是否可以抵抗某一个状态
     * @param state 状态id
     * @return 
     */
    boolean isResistState(String state); 
    
    /**
     * 添加一个状态到抵抗列表。
     * @param state 
     */
    void addState(String state);
    
    /**
     * 从状态抵抗列表中移除一个指定的状态。
     * @param state
     * @return 
     */
    boolean removeState(String state);
}
