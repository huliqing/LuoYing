/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.effect;

import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.loader.Loader;

/**
 * 用一个模型作为一个效果
 * @author huliqing
 */
public class ModelEffect extends AbstractEffect {

    private boolean loaded;

    @Override
    public void initData(EffectData data) {
        super.initData(data);
        if (!loaded) {
            Spatial model = Loader.loadModel(data.getAttribute("file"));
            Bucket bucket = identifyBucket(data.getAttribute("bucket"));
            // 一些特效需要指定bucket,特别是一些半透明的物体。半透明的物体需要在阴影
            // 渲染后才能渲染,否则会挡住阴影
            if (bucket != null) {
                model.setQueueBucket(bucket);
            }
            localRoot.attachChild(model);
            loaded = true;
        }
    }
    
    private Bucket identifyBucket(String bucket) {
        if (bucket == null) {
            return null;
        }
        if (bucket.equalsIgnoreCase(Bucket.Gui.name())) {
            return Bucket.Gui;
        } else if (bucket.equalsIgnoreCase(Bucket.Inherit.name())) {
            return Bucket.Inherit;
        } else if (bucket.equalsIgnoreCase(Bucket.Opaque.name())) {
            return Bucket.Opaque;
        } else if (bucket.equalsIgnoreCase(Bucket.Sky.name())) {
            return Bucket.Sky;
        } else if (bucket.equalsIgnoreCase(Bucket.Translucent.name())) {
            return Bucket.Translucent;
        } else if (bucket.equalsIgnoreCase(Bucket.Transparent.name())) {
            return Bucket.Transparent;
        }
        return null;
    }
    
}
