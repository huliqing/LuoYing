/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import name.huliqing.luoying.data.ModelEntityData;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 模型类的场景物体.
 * @author huliqing
 * @param <T>
 */
public abstract class ModelEntity<T extends ModelEntityData> extends AbstractEntity<T> {
    
    protected Spatial spatial;

    @Override
    public void setData(T data) {
        super.setData(data);
    }

    @Override
    public T getData() {
        return super.getData();
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setLocation(spatial.getLocalTranslation());
        data.setRotation(spatial.getLocalRotation());
        data.setScale(spatial.getLocalScale());
        data.setShadowMode(spatial.getShadowMode());
        data.setCullHint(spatial.getCullHint());
        data.setQueueBucket(spatial.getQueueBucket());
    }
    
    @Override
    public void initEntity() {
        spatial = loadModel();
        Vector3f location = data.getLocation();
        if (location != null) {
            spatial.setLocalTranslation(location);
        }
        Quaternion rotation = data.getRotation();
        if (rotation != null) {
            spatial.setLocalRotation(data.getRotation());
        }
        Vector3f scale = data.getScale();
        if (scale != null) {
            spatial.setLocalScale(scale);
        }
        ShadowMode sm = data.getShadowMode();
        if (sm != null) {
            spatial.setShadowMode(sm);
        }
        CullHint ch = data.getCullHint();
        if (ch != null) {
            spatial.setCullHint(ch);
        }
        Bucket qb = data.getQueueBucket();
        if (qb != null) {
            spatial.setQueueBucket(qb);
        }
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        // 当Entity被添加到场景的时候把Spatial放到场景中。
        scene.getRoot().attachChild(spatial);
    }

    @Override
    public void cleanup() {
        spatial.removeFromParent();
        super.cleanup(); 
    }
    
    @Override
    public Spatial getSpatial() {
        return spatial;
    }
    
    /**
     * 载入模型，这个方法需要返回一个代表当前ModelEntity的Spatial.
     * @return 
     */
    protected abstract Spatial loadModel();
    
}
