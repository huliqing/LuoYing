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
    private float size = 0.15f;
    private Camera camera;
    
    public AutoScaleControl() {}
    
    public AutoScaleControl(float size) {
        this.size = size;
    }
    
    /**
     * 设置图标大小，默认0.15f
     * @param size 
     */
    public void setSize(float size) {
        this.size = size;
    }
    
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
        Vector3f camloc = camera.getLocation();
        float scale = size * spatial.getWorldTranslation().distance(camloc);
        if (scale > 0) {
            spatial.setLocalScale(scale);
        }
        lastCamLoc.set(camloc);        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        camera = vp.getCamera();
    }
    
}
