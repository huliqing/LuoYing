/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.utils;

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
     * @param key
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
