/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import name.huliqing.ly.data.env.ModelEnvData;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.ly.object.AssetLoader;
import name.huliqing.ly.object.scene.Scene;

/**
 * 模型类的环境物体
 * @author huliqing
 * @param <T>
 */
public class ModelEnv<T extends ModelEnvData> extends AbstractEnv<T> {
    
    // 模型
    protected Spatial model;

    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        model = loadModel();
        if (model != null) {
            scene.addSpatial(model);
        }
    }

    @Override
    public void updateDatas() {
        if (model != null) {
            data.setLocation(model.getLocalTranslation());
            data.setRotation(model.getLocalRotation());
            data.setScale(model.getLocalScale());
        }
    }

    @Override
    public void cleanup() {
        if (model != null) {
            scene.removeSpatial(model);
            model = null;
        }
        super.cleanup(); 
    }

    /**
     * 获取模型物体的实体
     * @return 
     */
    public Spatial getSpatial() {
        return model;
    }

    /**
     * 载入模型
     * @return 
     */
    protected Spatial loadModel() {
        if (data.getFile() == null) {
            throw new NullPointerException("Could not load model with no file path"
                    + ", dataId=" + data.getId()
                    + ", tagName=" + data.getTagName());
        }
        
        Spatial spatial;
        if (data.isUseUnshaded()) {
            spatial = AssetLoader.loadModelUnshaded(data.getFile());
        } else {
            spatial = AssetLoader.loadModelDirect(data.getFile());
        }
//        spatial.setUserData(ObjectData.USER_DATA, data); // remove20161008,不再需要
        spatial.setLocalTranslation(data.getLocation());
        spatial.setLocalRotation(data.getRotation());
        spatial.setLocalScale(data.getScale());
        
        // 要进行transform后再设置物理特性，因为某些物理特性在设置后就不能再通过
        // 普通的setLocalTranslation,..Rotaion,..Scale来设置变换了。
        if (data.isPhysics()) {
            addPhysicsControl(spatial, null);
        }
        
        if (data.getShadowMode() != null) {
            spatial.setShadowMode(data.getShadowMode());
        }
        
        return spatial;
    }
    
    /**
     * 为Spatial设置physics, 子类可以覆盖这个方法，来为模型添加特殊定制的
     * 物理控制器。
     * @param spatial
     * @param rbc 默认的RigidBodyControl,如果为null则重新创建一个
     */
    protected void addPhysicsControl(Spatial spatial, RigidBodyControl rbc) {
        if (rbc == null) {
            rbc = new RigidBodyControl(data.getMass());
        }
        spatial.addControl(rbc);
        rbc.setFriction(data.getFriction());
        // 为简单和优化性能，一些参数暂不开放出来。
        rbc.setRestitution(0);
    }

    @Override
    public Vector3f getLocation() {
        if (model != null) {
            return model.getLocalTranslation();
        }
        return data.getLocation();
    }
    
    @Override
    public void setLocation(Vector3f location) {
        if (model != null) {
            model.setLocalTranslation(location);
            RigidBodyControl rbc = model.getControl(RigidBodyControl.class);
            if (rbc != null) {
                rbc.setPhysicsLocation(location);
            }
            return;
        }
        data.setLocation(location);
    }
    
    @Override
    public Quaternion getRotation() {
        if (model != null) {
            return model.getLocalRotation();
        }
        return data.getRotation();
    }

    @Override
    public void setRotation(Quaternion rotation) {
        if (model != null) {
            model.setLocalRotation(rotation);
            RigidBodyControl rbc = model.getControl(RigidBodyControl.class);
            if (rbc != null) {
                rbc.setPhysicsRotation(rotation);
            }
            return;
        }
        data.setRotation(rotation);
    }

    @Override
    public Vector3f getScale() {
        if (model != null) {
            return model.getLocalScale();
        }
        return data.getScale();
    }

    @Override
    public void setScale(Vector3f scale) {
        if (model != null) {
            model.setLocalScale(scale);
            return;
        }
        data.setScale(scale);
    }
    
    
}
