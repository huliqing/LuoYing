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
 * Module是用来控制实体的，一个实体可以拥有一个至多个的模块，模块可以绑定实体属性或数据，通过实体的属性或数据变化
 * 来控制实体。
 * @author huliqing
 */
public interface Module extends DataProcessor<ModuleData>{
    
    /**
     * 设置模块控制的实体目标
     * @param entity 
     */
    void setEntity(Entity entity);
    
    /**
     * 获取模块关联的实体
     * @return 
     */
    Entity getEntity();
    
    /**
     * 初始化模块
     */
    void initialize();
    
    /**
     * 判断模块是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 设置是否打开或关闭模块功能
     * @param enabled 
     */
    void setEnabled(boolean enabled);
    
    /**
     * 判断模块功能是否开启
     * @return 
     */
    boolean isEnabled();
    
    /**
     * 清理模块
     */
    void cleanup();
    

}
