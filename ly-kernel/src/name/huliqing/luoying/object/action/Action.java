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
package name.huliqing.luoying.object.action;

import name.huliqing.luoying.data.ActionData;
import name.huliqing.luoying.xml.DataProcessor;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface Action<T extends ActionData> extends DataProcessor<T>{
    
    /**
     * 开始行为
     */
    void initialize();
    
    /**
     * 判断行为是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 更新行为逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理行为数据，当行为正常退出或被打断时都会调用该方法来清理数
     * 据。
     */
    void cleanup();
    
    /**
     * 判断行为是否已经正常结束
     * @return 
     */
    boolean isEnd();
    
    /**
     * 锁定当前行为一定时间，让行为不更新，单位秒。
     * @param time 
     */
    void lock(float time);
    
    /**
     * 设置执行行为的角色。
     * @param actor 
     */
    void setActor(Entity actor);
}
