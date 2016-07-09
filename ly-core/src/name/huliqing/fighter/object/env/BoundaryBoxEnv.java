/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import name.huliqing.fighter.constants.ModelConstants;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.scene.Scene;

/**
 * box类型的边界盒，用于防止角色掉出场景边界。
 * @author huliqing
 * @param <T>
 */
public class BoundaryBoxEnv<T extends EnvData>  extends AbstractEnv<T>{

    private Vector3f xyzExtends;
    private Vector3f location;
    private Vector3f rotation;
    private Vector3f scale;
    private boolean debug;
     
    // ---- inner
    private Spatial boundary;
    
    @Override
    public void initData(T data) {
        super.initData(data);
        xyzExtends = data.getAsVector3f("xyzExtends");
        location = data.getAsVector3f("location");
        rotation = data.getAsVector3f("rotation");
        scale = data.getAsVector3f("scale");
        debug = data.getAsBoolean("debug", debug);
    }
    
    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        // boundary边界盒
        if (xyzExtends != null) {
            boundary = Loader.loadModel(ModelConstants.MODEL_BOUNDARY);
            boundary.setLocalScale(xyzExtends);
            if (location != null) {
                boundary.setLocalTranslation(location);
            }
            if (rotation != null) {
                Quaternion rot = boundary.getLocalRotation();
                rot.fromAngles(rotation.x, rotation.y, rotation.z);
                boundary.setLocalRotation(rot);
            }
            if (scale != null) {
                boundary.setLocalScale(scale);
            }
            // 记得在变换设置后(location,rotation,scale)再添加RigidBodyControl
            boundary.addControl(new RigidBodyControl(0));
            boundary.setCullHint(debug ? CullHint.Never : CullHint.Always);
            scene.addSceneObject(boundary);
        }
    }

    @Override
    public void cleanup() {
        if (scene != null) {
            scene.removeSceneObject(boundary);
        }
        super.cleanup(); 
    }
    
}
