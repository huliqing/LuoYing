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
package name.huliqing.luoying.object.logic;

import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * @author huliqing
 * @param <T>
 */
public interface Logic<T extends LogicData> extends DataProcessor<T> {

    @Override
    public void setData(T data);

    @Override
    public T getData();
    
    /**
     * 初始化逻辑
     */
    void initialize();

    /**
     * 判断逻辑是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 更新逻辑
     * @param tpf 
     */
    void update(float tpf);

    /**
     * 清理并释放资源
     */
    void cleanup();
    
    /**
     * 设置执行逻辑的角色。
     * @param self 
     */
    void setActor(Entity self);

}
