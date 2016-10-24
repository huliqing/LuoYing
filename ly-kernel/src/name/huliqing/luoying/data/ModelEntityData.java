/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial.CullHint;

/**
 *
 * @author huliqing
 */
@Serializable
public class ModelEntityData extends EntityData {
    
    private final static String ATTR_LOCATION = "location";
    private final static String ATTR_ROTATION = "rotation";
    private final static String ATTR_SCALE = "scale";
    private final static String ATTR_SHADOW_MODE = "shadowMode";
    private final static String ATTR_CULL_HINT = "cullHint";
    private final static String ATTR_QUEUE_BUCKET = "queueBucket";
    
    /**
     * 获取模型的位置，如果没有设置这个参数，则返回null.
     * @return 有可能返回null.
     */
    public Vector3f getLocation() {
        return getAsVector3f(ATTR_LOCATION);
    }
    
    /**
     * 设置模型的位置
     * @param location 
     */
    public void setLocation(Vector3f location) {
        setAttribute(ATTR_LOCATION, location);
    }
    
    /**
     * 获取模型的旋转变换，如果没有设置这个参数，则返回null.
     * @return 有可能返回null.
     */
    public Quaternion getRotation() {
        return getAsQuaternion(ATTR_ROTATION);
    }
    
    /**
     * 设置模型旋转变换
     * @param rotation 
     */
    public void setRotation(Quaternion rotation) {
        setAttribute(ATTR_ROTATION, rotation);
    }
    
    /**
     * 获取模型的缩放变换，如果没有设置这个参数，则返回null.
     * @return 有可能返回null.
     */
    public Vector3f getScale() {
        return getAsVector3f(ATTR_SCALE);
    }
    
    /**
     * 设置模型缩放
     * @param scale 
     */
    public void setScale(Vector3f scale) {
        setAttribute(ATTR_SCALE, scale);
    }
    
    /**
     * 获取ShadowMode, 如果没有设置这个参数则可能返回null.
     * @return 
     */
    public ShadowMode getShadowMode() {
        String shadowMode = getAsString(ATTR_SHADOW_MODE);
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
        // 如要要设置的值与本地变量或proto的值相同，则不需要设置, 这有一些好处，当值与proto的值相同时，可以不需要设
        // 置本地变量
        String shadowModeStr = getAsString(ATTR_SHADOW_MODE);
        if (shadowMode.name().equals(shadowModeStr)) {
            return;
        }
        setAttribute(ATTR_SHADOW_MODE, shadowMode.name());
    }
    
    /**
     * 获取CullHint设置，如果没有设置，则这个方法将返回null.
     * @return 
     */
    public CullHint getCullHint() {
        String cullHint = getAsString(ATTR_CULL_HINT);
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
        String cullHintStr = getAsString(ATTR_CULL_HINT);
        if (cullHint.name().equals(cullHintStr)) {
            return;
        }
        setAttribute(ATTR_CULL_HINT, cullHint.name());
    }
    
    public Bucket getQueueBucket() {
        String bucket = getAsString(ATTR_QUEUE_BUCKET);
        if (bucket == null) {
            return null;
        }
        for (Bucket b : Bucket.values()) {
            if (b.name().equals(bucket)) {
                return b;
            }
        }
        return null;
    }
    
    public void setQueueBucket(Bucket bucket) {
        String bucketStr = getAsString(ATTR_QUEUE_BUCKET);
        if (bucket.name().equals(bucketStr)) {
            return;
        }
        setAttribute(ATTR_QUEUE_BUCKET, bucket.name());
    }
    
}
