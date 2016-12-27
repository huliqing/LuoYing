/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.List;
import name.huliqing.editor.Editor;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.events.JmeKeyMapping;
import name.huliqing.editor.tiles.ChaseObj;
import name.huliqing.editor.utils.EditorCamera;

/**
 * 场景镜头工具
 * @author huliqing
 */
public class CameraTool extends EditTool {
    
    private final String EVENT_DRAG = "cameraDragEvent";
    private final String EVENT_ROTATE = "cameraRotateEvent";
    private final String EVENT_ZOOM_IN = "cameraZoomInEvent";
    private final String EVENT_ZOOM_OUT = "cameraZoomOutEvent";
    
    private Editor editor;
    // 镜头
    private EditorCamera editorCam;
    // 被镜头跟随的物体
    private final ChaseObj chaseObj = new ChaseObj();
    
    // 相机拖动标记
    private boolean dragEnabled;
    // 最近一次记录光标的位置
    private final Vector2f lastCursorPos = new Vector2f();
    // 最近一次记录的相机所跟随的物体的位置
    private final Vector3f lastChasePos = new Vector3f();
    // 相机的移动速度，默认1.0f
    private final float camMoveSpeed = 1.0f;
    
    public CameraTool(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        super.initialize(); 
        editor = toolbar.getForm().getEditor();
        
        toolbar.getForm().getEditRoot().attachChild(chaseObj);
        editorCam = new EditorCamera(editor.getCamera(), editor.getInputManager());
        editorCam.setChase(chaseObj);
        
        JmeEvent dragEvent = bindDragEvent();
        List<JmeKeyMapping> kms = dragEvent.getKeyMappings();
        kms.forEach(t -> {
//            editorCam.
        });
        
        lastCursorPos.set(editor.getInputManager().getCursorPosition());
        lastChasePos.set(chaseObj.getLocalTranslation());
    }

    @Override
    public void cleanup() {
        editorCam.cleanup();
        editorCam = null;
        chaseObj.removeFromParent();
        super.cleanup(); 
    }
 
    /**
     * 绑定拖动事件，用来拖动相机在场景中的视角。
     * @return 
     */
    public JmeEvent bindDragEvent() {
        return bindEvent(EVENT_DRAG);
    }
    
    /**
     * 绑定相机的转动事件。
     * @return 
     */
    public JmeEvent bindRotateEvent() {
        return bindEvent(EVENT_ROTATE);
    }
    
    /**
     * 绑定相机的放大事件
     * @return 
     */
    public JmeEvent bindZoomInEvent() {
        return bindEvent(EVENT_ZOOM_IN);
    }
    
    /**
     * 绑定相机的缩小事件
     * @return 
     */
    public JmeEvent bindZoomOutEvent() {
        return bindEvent(EVENT_ZOOM_OUT);
    }

    @Override
    protected void onToolEvent(Event e) {
        dragEnabled = e.isMatch() && e.getName().equals(EVENT_DRAG);
        if (dragEnabled) {
            lastCursorPos.set(editor.getInputManager().getCursorPosition());
            lastChasePos.set(chaseObj.getLocalTranslation());
            editorCam.setEnableRotation(false);
            editorCam.setEnabledRotationV(false);
        } else {
            editorCam.setEnableRotation(true);
            editorCam.setEnabledRotationV(true);
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        if (dragEnabled) {
               Vector2f cp = editor.getInputManager().getCursorPosition();
               float xDist = cp.x - lastCursorPos.x;
               float yDist = cp.y - lastCursorPos.y;
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
                   chaseObj.setLocalTranslation(chasePos);
                   tv.release();
               }
        }
        
    }
    
    
}
