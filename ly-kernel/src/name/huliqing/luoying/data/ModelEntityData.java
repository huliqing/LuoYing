/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial.CullHint;

/**
 *
 * @author huliqing
 */
@Serializable
public class ModelEntityData extends EntityData {
    
    /**
     * 获取模型的位置，如果没有设置这个参数，则返回null.
     * @return 有可能返回null.
     */
    public Vector3f getLocation() {
        return getAsVector3f("location");
    }
    
    /**
     * 设置模型的位置
     * @param location 
     */
    public void setLocation(Vector3f location) {
        setAttribute("location", location);
    }
    
    /**
     * 获取模型的旋转变换，如果没有设置这个参数，则返回null.
     * @return 有可能返回null.
     */
    public Quaternion getRotation() {
        return getAsQuaternion("rotation");
    }
    
    /**
     * 设置模型旋转变换
     * @param rotation 
     */
    public void setRotation(Quaternion rotation) {
        setAttribute("rotation", rotation);
    }
    
    /**
     * 获取模型的缩放变换，如果没有设置这个参数，则返回null.
     * @return 有可能返回null.
     */
    public Vector3f getScale() {
        return getAsVector3f("scale");
    }
    
    /**
     * 设置模型缩放
     * @param scale 
     */
    public void setScale(Vector3f scale) {
        setAttribute("scale", scale);
    }
    
    /**
     * 获取ShadowMode, 如果没有设置这个参数则可能返回null.
     * @return 
     */
    public ShadowMode getShadowMode() {
        String shadowMode = getAsString("shadowMode");
        if (shadowMode == null) {
            return null;
        }
        for (ShadowMode sm : ShadowMode.values()) {
            if (sm.name().equals(shadowMode)) {
                return sm;
            }
        }
        return null;
    }
    
    public void setShadowMode(ShadowMode shadowMode) {
        setAttribute("shadowMode", shadowMode.name());
    }

    /**
     * 获取CullHint设置，如果没有设置，则这个方法将返回null.
     * @return 
     */
    public CullHint getCullHint() {
        String cullHint = getAsString("cullHint");
        if (cullHint == null)
            return null;
        
        for (CullHint ch : CullHint.values()) {
            if (ch.name().equals(cullHint)) {
                return ch;
            }
        }
        return null;
    }
    
    public void setCullHint(CullHint cullHint) {
        setAttribute("cullHint", cullHint.name());
    }
}
