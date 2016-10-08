/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import name.huliqing.ly.constants.ModelConstants;
import name.huliqing.ly.data.env.EnvData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.scene.Scene;

/**
 * box类型的边界盒，用于防止角色掉出场景边界。
 * @author huliqing
 */
public class BoundaryBoxEnv  extends AbstractEnv<EnvData>{

    private boolean debug;
     
    // ---- inner
    private Spatial boundary;
    private RigidBodyControl control;
    
    @Override
    public void setData(EnvData data) {
        super.setData(data);
        debug = data.getAsBoolean("debug", debug);
    }

    @Override
    public void updateDatas() {
        super.updateDatas(); 
        if (initialized) {
            data.setLocation(boundary.getLocalTranslation());
            data.setRotation(boundary.getLocalRotation());
            data.setScale(boundary.getLocalScale());
        }
    }
    
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        // boundary边界盒
        boundary = Loader.loadModel(ModelConstants.MODEL_BOUNDARY);

        boundary.setLocalTranslation(data.getLocation());
        boundary.setLocalRotation(data.getRotation());
        boundary.setLocalScale(data.getScale());

        // 记得在变换设置后(location,rotation,scale)再添加RigidBodyControl
        control = new RigidBodyControl(0);
        boundary.addControl(control);
        boundary.setCullHint(debug ? CullHint.Never : CullHint.Always);
        scene.addSpatial(boundary);
    }

    @Override
    public void cleanup() {
        scene.removeSpatial(boundary);
        super.cleanup(); 
    }
   
    @Override
    public Vector3f getLocation() {
        if (initialized) {
            return boundary.getLocalTranslation();
        }
        return data.getLocation();
    }

    @Override
    public void setLocation(Vector3f location) {
        if (initialized) {
            boundary.setLocalTranslation(location);
            return;
        }
        data.setLocation(location);
    }

    @Override
    public Quaternion getRotation() {
        if (initialized) {
            return boundary.getLocalRotation();
        }
        return data.getRotation();
    }

    @Override
    public void setRotation(Quaternion rotation) {
        if (initialized) {
            boundary.setLocalRotation(rotation);
            return;
        }
        data.setRotation(rotation);
    }

    @Override
    public Vector3f getScale() {
        if (initialized) {
            return boundary.getLocalScale();
        }
        return data.getScale();
    }

    @Override
    public void setScale(Vector3f scale) {
        if (initialized) {
            boundary.setLocalScale(scale);
            return;
        }
        data.setScale(scale);
    }
    
}
