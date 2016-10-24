/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.effect;

import com.jme3.scene.Spatial;
import name.huliqing.luoying.object.Loader;

/**
 * 用一个模型作为一个效果
 * @author huliqing
 */
public class ModelEffect extends Effect {

    private Spatial model;
    
    @Override
    public void initEntity() {
        super.initEntity();
        model = Loader.loadModel(data.getAsString("file"));
        animNode.attachChild(model);
    }

    @Override
    public void cleanup() {
        model.removeFromParent();
        super.cleanup(); 
    }
    
}
