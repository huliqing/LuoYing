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
package name.huliqing.ly.enums;

import com.jme3.math.ColorRGBA;

/**
 * 用于定义HUD提示信息的类型
 * @author huliqing
 */
public enum MessageType {
    
    /** 普通提示信息 */
    info(ColorRGBA.LightGray),
    
    /** 重要提示 */
    notice(ColorRGBA.Yellow),
    
    /** 谈话对话 */
    talk(ColorRGBA.Cyan),
    
    /** 获得物品 */
    item(ColorRGBA.Green),
    
    /** 获得物品（任务） */
    itemTask(new ColorRGBA(0.6f, 1f, 0.6f, 1f)),
    
    /** 等级提升 */
    levelUp(new ColorRGBA(0.9f, 0.5f, 0.9f, 1f));
    
    
    private ColorRGBA color = ColorRGBA.White.clone();
    
    private MessageType(ColorRGBA color) {
        this.color.set(color);
    }
    
    public ColorRGBA getColor() {
        return color;
    }
}
