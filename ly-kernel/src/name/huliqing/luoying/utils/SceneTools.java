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

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import name.huliqing.luoying.Config;

/**
 *
 * @author huliqing
 */
public class SceneTools {
    
    public static ChaseCamera createSimpleChaseCamera(Camera camera
            , InputManager inputManager) {
        ChaseCamera chaseCam = new ChaseCamera(camera, inputManager);
        
        // 开启镜头跟随可能让部分人容易犯头晕
//        chaseCam.setSmoothMotion(true);
//        chaseCam.setTrailingEnabled(false);
        
        chaseCam.setInvertVerticalAxis(true);
        chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(2f));
        chaseCam.setZoomSensitivity(0.5f);
        chaseCam.setRotationSpeed(5f);
        chaseCam.setRotationSensitivity(5);
        chaseCam.setMaxDistance(15);
        chaseCam.setMinDistance(2f);
        chaseCam.setDefaultDistance(15);
        chaseCam.setChasingSensitivity(5);
        chaseCam.setDownRotateOnCloseViewOnly(true); 
        chaseCam.setUpVector(Vector3f.UNIT_Y);
        // 不要隐藏光标,否则在MAC系统下鼠标点击后会上下错位
        chaseCam.setHideCursorOnRotate(false);
        return chaseCam;
    }
    
    /**
     * 创建照机跟随，这个相机可以处理穿墙
     * @param camera
     * @param inputManager
     * @return 
     */
    public static CollisionChaseCamera createChaseCam(Camera camera, InputManager inputManager) {
        CollisionChaseCamera chaseCam = new CollisionChaseCamera(camera, inputManager);
        
        // 开启镜头跟随可能让部分人容易犯头晕
//        chaseCam.setSmoothMotion(true);
//        chaseCam.setTrailingEnabled(false);
        
        chaseCam.setInvertVerticalAxis(true);
        chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(2f));
        chaseCam.setZoomSensitivity(0.5f);
        chaseCam.setRotationSpeed(5f);
        chaseCam.setRotationSensitivity(5);
        chaseCam.setMaxDistance(15);
        chaseCam.setMinDistance(2f);
        chaseCam.setDefaultDistance(15);
        chaseCam.setChasingSensitivity(5);
        chaseCam.setDownRotateOnCloseViewOnly(true); 
        chaseCam.setUpVector(Vector3f.UNIT_Y);
        // 不要隐藏光标,否则在MAC系统下鼠标点击后会上下错位
        chaseCam.setHideCursorOnRotate(false);
        
        if (Config.debug) {
            chaseCam.setMinDistance(1f);
            chaseCam.setMaxDistance(999999999);
            chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(2f));
            chaseCam.setZoomSensitivity(5f);
        }
        
        return chaseCam;
    }
}
