/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * 动态控制场景中物体的大小
 * @author huliqing
 */
public class AutoScaleControl extends AbstractControl {
    
    private final Vector3f lastCamLoc = new Vector3f();
    private final float size = 0.1f;
    private Camera camera;
    
    /**
     * 强制立即更新，计算缩放
     */
    public void forceUpdate() {
        controlUpdate(0.016f);
    }
    
    @Override
    protected void controlUpdate(float tpf) {
         if (camera == null || spatial.getCullHint() == Spatial.CullHint.Always) {
            return;
        }
        // 自动缩放
//        Vector3f camloc = Editor.getApp().getCamera().getLocation();
        Vector3f camloc = camera.getLocation();
        if (Float.compare(camloc.x, lastCamLoc.x) != 0
                || Float.compare(camloc.y, lastCamLoc.y) != 0
                || Float.compare(camloc.z, lastCamLoc.z) != 0) {
            float scale = size * spatial.getWorldTranslation().distance(camloc);
            if (scale > 0) {
                spatial.setLocalScale(scale);
            }
            lastCamLoc.set(camloc);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        camera = vp.getCamera();
    }
    
}
