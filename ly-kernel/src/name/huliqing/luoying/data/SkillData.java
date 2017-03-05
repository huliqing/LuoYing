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
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.network.serializing.Serializable;
import java.util.List;

/**
 * @author huliqing
 */
@Serializable
public class SkillData extends ObjectData {
    
    public String getIcon() {
        return getAsString("icon");
    }
    
    public void setIcon(String icon) {
        setAttribute("icon", icon);
    }
    
    public float getUseTime() {
        return getAsFloat("useTime", 1.0f);
    }

    /**
     * 设置技能的使用时间
     * @param useTime 
     */
    public void setUseTime(float useTime) {
        setAttribute("useTime", useTime);
    }
    
    public List<String> getTypes() {
        return getAsStringList("types");
    }

    public float getCooldown() {
        return getAsFloat("cooldown", 0);
    }
    
    /**
     * 设置技能的冷却时间，单位秒
     * @param cooldown 
     */
    public void setCooldown(float cooldown) {
        setAttribute("cooldown", cooldown);
    }
    
     /**
     * 获取技能的当前等级
     * @return 
     */
    public int getLevel() {
        return getAsInteger("level", 1);
    }

    /**
     * 设置技能的当前等级
     * @param level 
     */
    public void setLevel(int level) {
        setAttribute("level", level);
    }

    /**
     * 获取技能可以升级的最高等级限制
     * @return 
     */
    public int getMaxLevel() {
        return getAsInteger("maxLevel", 1);
    }

    /**
     * 设置技能可以升级的最高等级限制
     * @param maxLevel 
     */
    public void setMaxLevel(int maxLevel) {
        setAttribute("maxLevel", maxLevel);
    }
    
    /**
     * 获取技能的执行次数
     * @return 
     */
    public int getPlayCount() {
        return getAsInteger("playCount", 0);
    }

    /**
     * 设置技能的执行次数
     * @param playCount 
     */
    public void setPlayCount(int playCount) {
        setAttribute("playCount", playCount);
    }
    
    public long getLastPlayTime() {
        return getAsLong("lastPlayTime", 0);
    }

    /**
     * 记录技能的最近一次使用时间。
     * @param lastPlayTime 
     */
    public void setLastPlayTime(long lastPlayTime) {
        setAttribute("lastPlayTime", lastPlayTime);
    }
    
    /**
     * 获取技能需要消耗的属性
     * @return 
     */
    public List<AttributeUse> getUseAttributes() {
        return getAsSavableList("useAttributes");
    }

    /**
     * 设置技能需要消息的属性
     * @param useAttributes 
     */
    public void setUseAttributes(List<AttributeUse> useAttributes) {
        setAttributeSavableList("useAttributes", useAttributes);
    }

}
