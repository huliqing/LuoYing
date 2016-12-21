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
package name.huliqing.luoying.object.module;

import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * Module是用来扩展角色功能的,一个角色可以拥有一个至无穷多个的扩展模块，所有角色扩展模块都应该实现这个
 * 接口.
 * @author huliqing
 * @param <T>
 */
public interface Module<T extends ModuleData> extends DataProcessor<T>{
    
    @Override
    public void setData(T data);

    @Override
    public T getData();
    
    /**
     * 初始化模块
     * @param entity
     */
    void initialize(Entity entity);
    
    /**
     * 判断模块是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理模块
     */
    void cleanup();
    
    /**
     * 获取模块关联的实体
     * @return 
     */
    Entity getEntity();
}
