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
package name.huliqing.luoying.utils;

import com.jme3.input.InputManager;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;

/**
 * 用于快速绑定事件
 * @author huliqing
 */
public class InputUtils {
    
    /**
     * 绑定按键
     * @param inputManager
     * @param mapName
     * @param key
     * @param listener 
     */
    public static void bindKey(InputManager inputManager, String mapName, int key, InputListener listener) {
        Trigger t = new KeyTrigger(key);
        inputManager.addMapping(mapName, t);
        inputManager.addListener(listener, mapName);
    }
    
    /**
     * 绑定鼠标事件
     * @param inputManager
     * @param mapName
     * @param button
     * @param listener 
     */
    public static void bindMouse(InputManager inputManager, String mapName, int button, InputListener listener) {
        Trigger t = new MouseButtonTrigger(button);
        inputManager.addMapping(mapName, t);
        inputManager.addListener(listener, mapName);
    }
    
    public static void bindMouseAxis(InputManager inputManager, String mapName, int axis, InputListener listener) {
        Trigger t = new MouseAxisTrigger(axis, true);
        inputManager.addMapping(mapName, t);
        inputManager.addListener(listener, mapName);
    }
    
}
