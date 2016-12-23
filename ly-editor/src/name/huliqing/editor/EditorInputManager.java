/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class EditorInputManager implements ActionListener {
    private static final Logger LOG = Logger.getLogger(EditorInputManager.class.getName());
    
    private final String MAP_ML = "ML";
    private final String MAP_MR = "MR";
    private final String MAP_MM = "MM";
    private final String MAP_SHIFT = "SHIFT";
    
    private boolean mouse_left_pressed;
    private boolean mouse_middle_pressed;
    private boolean mouse_right_pressed;
    private boolean key_shift_pressed;
    
    private Editor editor;
    private boolean initialized;
    
    private final Vector2f lastCP = new Vector2f();
    private final Vector3f lastChasePos = new Vector3f();
    // 相机的移动速度，默认1.0f
    private final float camMoveSpeed = 1.0f;
    // 相机的缩放速度
    private final float camZoomSpeed = 1.0f;
    
    public void initialize(Editor editor) {
        if (initialized) {
            return;
        }
        initialized = true;
        this.editor = editor;
        lastCP.set(editor.getInputManager().getCursorPosition());
        lastChasePos.set(editor.chaseObj.getLocalTranslation());
        
        // 鼠标按键
        InputManager inputManager = editor.getInputManager();
        Trigger triggerML = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
        Trigger triggerMR = new MouseButtonTrigger(MouseInput.BUTTON_RIGHT);
        Trigger triggerMM= new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE);
        inputManager.addMapping(MAP_ML, triggerML);
        inputManager.addMapping(MAP_MR, triggerMR);
        inputManager.addMapping(MAP_MM, triggerMM);
        
        // 键盘按键
        Trigger triggerKLShift = new KeyTrigger(KeyInput.KEY_LSHIFT);
        Trigger triggerKRShift = new KeyTrigger(KeyInput.KEY_RSHIFT);
        inputManager.addMapping(MAP_SHIFT, triggerKLShift, triggerKRShift);
        
        String[] mpNames = new String[] {
              MAP_ML, MAP_MR, MAP_MM
            , MAP_SHIFT
        };
        inputManager.addListener(this, mpNames);
        inputManager.addListener(this, CameraInput.CHASECAM_ZOOMIN);
        inputManager.addListener(this, CameraInput.CHASECAM_ZOOMOUT);
    }
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (MAP_ML.equals(name)) {
            mouse_left_pressed = isPressed; 
            lastCP.set(editor.getInputManager().getCursorPosition());
            lastChasePos.set(editor.chaseObj.getLocalTranslation());
        } else if (MAP_MR.equals(name)) {
            mouse_right_pressed = isPressed;
            lastCP.set(editor.getInputManager().getCursorPosition());
            lastChasePos.set(editor.chaseObj.getLocalTranslation());
        } else if (MAP_MM.equals(name)) {
            mouse_middle_pressed = isPressed;
            lastCP.set(editor.getInputManager().getCursorPosition());
            lastChasePos.set(editor.chaseObj.getLocalTranslation());
        } else if (MAP_SHIFT.equals(name)) {
            key_shift_pressed = isPressed;
            lastCP.set(editor.getInputManager().getCursorPosition());
            lastChasePos.set(editor.chaseObj.getLocalTranslation());
        } else if (CameraInput.CHASECAM_ZOOMIN.equals(name) || CameraInput.CHASECAM_ZOOMOUT.equals(name)) {
            // 跟随距离来调整相机的缩放强度，距离越大，缩放强度越大
            float distance = editor.getCamera().getLocation().distance(editor.chaseObj.getLocalTranslation());
            editor.editorCam.setZoomSensitivity(distance * 0.1f * camZoomSpeed);
        } 
        actionCheck();
    }
    
    private void actionCheck() {
        if (mouse_middle_pressed && key_shift_pressed) {
            editor.editorCam.setEnableRotation(false);
            editor.editorCam.setEnabledRotationV(false);
        } else {
            editor.editorCam.setEnableRotation(true);
            editor.editorCam.setEnabledRotationV(true);
        }
    }

    public void update(float tpf) {
        // 平移操作
        if (mouse_middle_pressed && key_shift_pressed) {
           Vector2f cp = editor.getInputManager().getCursorPosition();
           float xDist = cp.x - lastCP.x;
           float yDist = cp.y - lastCP.y;
           if (xDist != 0 || yDist != 0) {
               TempVars tv = TempVars.get();
               Vector3f camDir = editor.getCamera().getDirection(tv.vect1);
               Vector3f right = tv.vect2.set(camDir).crossLocal(Vector3f.UNIT_Y).normalizeLocal();
               Vector3f up = tv.vect3.set(right).crossLocal(camDir).normalizeLocal();
               // 允许根据距离来调节移动速度
               float distance = editor.getCamera().getLocation().distance(lastChasePos)  * 0.002f;
               Vector3f chasePos = tv.vect4.set(lastChasePos);
               chasePos.subtractLocal(right.multLocal(xDist * camMoveSpeed * distance));
               chasePos.subtractLocal(up.multLocal(yDist * camMoveSpeed * distance));
               editor.chaseObj.setLocalTranslation(chasePos);
               tv.release();
           }
        }
    }


}
