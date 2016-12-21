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
package name.huliqing.luoying.object;

import com.jme3.app.Application;

/**
 * 带有逻辑行为的物体（不要去继承PlayObject,保持与PlayObject的平行关系。）
 * @author huliqing
 */
public interface PlayObject {

    /**
     * 初始化物体
     * @param app
     */
    public void initialize(Application app);

    /**
     * 判断是否已经初始化
     * @return 
     */
    public boolean isInitialized();

    /**
     * 更新Object的logic
     * @param tpf 
     */
    void update(float tpf);

    /**
     * 清理Logic产生的资源，当该方法被调用的时候Object已经被移出更新队列。
     */
    void cleanup();
}
