/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.utils;

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import name.huliqing.fighter.Config;

/**
 *
 * @author huliqing
 */
public class SceneUtils {
    
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
