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
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.define.MatDefine;
import name.huliqing.luoying.object.define.SkillTypeDefine;
import name.huliqing.luoying.object.define.SkinPartDefine;
import name.huliqing.luoying.object.define.WeaponTypeDefine;

/**
 *
 * @author huliqing
 */
public interface DefineService extends Inject {
    
    /**
     * 获取技能类型定义配置
     * @return 
     */
    SkillTypeDefine getSkillTypeDefine();
    
    /**
     * 获取皮肤类型定义配置
     * @return 
     */
    SkinPartDefine getSkinPartDefine();
    
    /**
     * 获取武器类型的定义配置。
     * @return 
     */
    WeaponTypeDefine getWeaponTypeDefine();
    
    /**
     * 获取物体质地定义
     * @return 
     */
    MatDefine getMatDefine();
    
    /**
     * 清理并重置,调用这个方法来清理各种定义的缓存。
     * 特别是当系统数据重置或重新初始化的时候需要调用这个方法来确保各种定义的重新载入.
     */
    void clearAndReset();
    
}
