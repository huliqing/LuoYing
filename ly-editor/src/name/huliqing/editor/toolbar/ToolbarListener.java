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
package name.huliqing.editor.toolbar;

import name.huliqing.editor.tools.Tool;

/**
 *
 * @author huliqing
 */
public interface ToolbarListener {
    
    /**
     * 当工具栏状态发生变化时该方法被调用,这表示工具栏从非激活转换到激活，或者从激活中转换到非激活。
     * @param enabled 
     */
    void onStateChanged(boolean enabled);
    
    /**
     * 当向工具栏添加工具时该方法被调用
     * @param toolAdded 
     */
    void onToolAdded(Tool toolAdded);
    
    /**
     * 当从工具栏移除工具时
     * @param toolRemoved 
     */
    void onToolRemoved(Tool toolRemoved);
    
    void onToolEnabled(Tool toolEnabled);
    
    void onToolDisabled(Tool toolDisabled);
    
//    /**
//     * 当指定工具被激活时该方法被调用。
//     * @param tool 
//     */
//    void onToolActivated(Tool tool);
//    
//    /**
//     * 当工具被取消激活时该方法被调用，非激活状态表示的是工具可以用，但未激活。
//     * @param tool 
//     */
//    void onToolDeactivated(Tool tool);
//    
//    /**
//     * 当指定工具从无效转变有有效时该方法被调用，这里的有效表示工具可以用，但不一定是激活状态。
//     * @param tool 
//     */
//    void onToolEnabled(Tool tool);
//    
//    /**
//     * 当工具由有效转变为无效时该方法被调用，这表示工具不可用。
//     * @param tool 
//     */
//    void onToolDisabled(Tool tool);
}
