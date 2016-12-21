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
package name.huliqing.ly.state;

import name.huliqing.luoying.ui.UI;

/**
 * 界面上的工具栏，可用于添加按钮
 * @author huliqing
 */
public interface MenuTool {
    
    /**
     * 添加一个菜单 
     * @param menu
     */
    void addMenu(UI menu);
    
    /**
     * 添加菜单到指定索引位置
     * @param menu
     * @param index 
     */
    void addMenu(UI menu, int index);
 
    /**
     * 移除指定的菜单
     * @param menu
     * @return 
     */
    boolean removeMenu(UI menu);
}
