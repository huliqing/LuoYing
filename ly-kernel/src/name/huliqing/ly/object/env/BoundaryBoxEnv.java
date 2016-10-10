/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import name.huliqing.ly.constants.ModelConstants;
import name.huliqing.ly.data.EntityData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.entity.ModelEntity;
import name.huliqing.ly.object.scene.Scene;

/**
 * box类型的边界盒，用于防止角色掉出场景边界。
 * @author huliqing
 */
public class BoundaryBoxEnv  extends ModelEntity {

    private boolean debug;
     
    // ---- inner
    private Spatial boundary;
    private RigidBodyControl control;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        debug = data.getAsBoolean("debug", debug);
    }
    
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        // 添加RigidBodyControl
        control = new RigidBodyControl(0);
        boundary.addControl(control);
        boundary.setCullHint(debug ? CullHint.Never : CullHint.Always);
    }

    @Override
    protected Spatial loadModel() {
        boundary = Loader.loadModel(ModelConstants.MODEL_BOUNDARY);
        return boundary;
    }

}
