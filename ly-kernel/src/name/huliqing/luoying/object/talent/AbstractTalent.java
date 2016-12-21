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
package name.huliqing.luoying.object.talent;

import name.huliqing.luoying.data.TalentData;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractTalent<T extends TalentData> implements Talent<T> {
    protected T data;
    protected Entity actor;

    /**
     * 天赋的当前等级
     */
    protected int level;
    
    /**
     * 天赋的允许的最高等级
     */
    protected int maxLevel = 10;
    
    /**
     * 标记天赋是否已经初始化
     */
    protected boolean initialized;

    @Override
    public void setData(T data) {
        this.data = data;
        level = data.getLevel();
        maxLevel = data.getMaxLevel();
    }
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        data.setLevel(level);
        data.setMaxLevel(maxLevel);
    }

    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Talent already initialized! talent=" + this);
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
    
    @Override
    public void setActor(Entity actor) {
        this.actor = actor;
    }
    
    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        if (level > maxLevel) {
            return;
        }
        
        // 如果天赋已经初始化，则清理后重新载入.
        if (initialized) {
            cleanup();
            this.level = level;
            initialize();
        } else {
            this.level = level;
        }
        
        // 更新一下data数据。
        data.setLevel(level);
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        data.setMaxLevel(maxLevel);
    }
    
}
