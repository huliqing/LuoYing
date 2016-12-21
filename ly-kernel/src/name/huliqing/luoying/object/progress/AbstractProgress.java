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
package name.huliqing.luoying.object.progress;

import com.jme3.scene.Node;
import name.huliqing.luoying.data.ProgressData;

/**
 * 抽象基类，处理掉一些通用的基本参数
 * @author huliqing
 */
public abstract class AbstractProgress implements Progress {
    
    protected ProgressData data;
    protected boolean initialized;
    
    @Override
    public void setData(ProgressData data) {
        this.data = data;
    }

    @Override
    public ProgressData getData() {
        return data;
    }

    @Override
    public void updateDatas() {}
    
    @Override
    public void initialize(Node viewRoot) {
        if (initialized) {
            throw new IllegalStateException("Progress already is initialized! progressId=" + data.getId());
        }
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
}
