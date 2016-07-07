/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.loader.AssetLoader;
import name.huliqing.fighter.object.scene.Scene;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class ModelEnv<T extends ModelEnvData> extends Env<T> {
    
    // 模型
    protected Spatial model;

    @Override
    public void initData(T data) {
        super.initData(data); 
    }

    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        model = loadModel();
        if (model != null) {
            // 添加到地形节点中，这样玩家可以点击这个模型，并让角色在这个模型上走动。
            if (data.isTerrain()) {
                scene.addTerrainObject(model);
            } else {
                scene.addSceneObject(model);
            }            
        }
    }

    @Override
    public void cleanup() {
        if (model != null) {
            scene.removeSceneObject(model);
            model = null;
        }
        super.cleanup(); 
    }

    /**
     * 获取模型
     * @return 
     */
    public Spatial getModel() {
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
        spatial.setUserData(ProtoData.USER_DATA, data);
        spatial.setLocalTranslation(data.getLocation());
        spatial.setLocalRotation(data.getRotation());
        spatial.setLocalScale(data.getScale());
        
        // 要进行transform后再设置物理特性，因为某些物理特性在设置后就不能再通过
        // 普通的setLocalTranslation,..Rotaion,..Scale来设置变换了。
        if (data.isPhysics()) {
            addPhysicsControl(spatial, null, data);
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
     * @param data 
     */
    protected void addPhysicsControl(Spatial spatial, RigidBodyControl rbc, T data) {
        if (rbc == null) {
            rbc = new RigidBodyControl(data.getMass());
        }
        spatial.addControl(rbc);
        rbc.setFriction(data.getFriction());
        // 为简单和优化性能，一些参数暂不开放出来。
        rbc.setRestitution(0);
    }
}
