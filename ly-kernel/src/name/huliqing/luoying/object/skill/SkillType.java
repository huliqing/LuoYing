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
package name.huliqing.luoying.object.skill;

/**
 * 包装技能类型。
 * @author huliqing
 */
public class SkillType {
    
    private final int index;
    private final String name;
    
    public SkillType(int index, String type) {
        this.index = index;
        this.name = type;
    }
    
    /**
     * 获取技能标记的索引值
     * @return 
     */
    public final int index() {
        return index;
    }
    
    /**
     * <code>
     * 1L << index
     * </code>
     * @return 
     */
    public final long indexAsBinary() {
        // 1必须为long
        return 1L << index;
    }

    /**
     * 获取技能标记的名称
     * @return 
     */
    public final String name() {
        return name;
    }

    @Override
    public String toString() {
        return "SkillType=" + index + ", skillTypeName=" + name; 
    }
    
    
}
