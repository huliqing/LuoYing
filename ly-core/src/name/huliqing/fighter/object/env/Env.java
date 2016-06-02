/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.loader.AssetLoader;
import name.huliqing.fighter.object.DataProcessor;

/**
 * @author huliqing
 */
public class Env<T extends EnvData> implements DataProcessor<T>{
    
    protected T data;
    // 模型
    protected Spatial model;
    protected boolean loaded;
    
    /**
     * 获取模型
     * @return 
     */
    public Spatial getModel() {
        if (!loaded) {
            model = load();
            loaded = true;
        }
        return model;
    }

    @Override
    public T getData() {
        if (!loaded) {
            return data;
        }
        data.setLocation(model.getLocalTranslation());
        data.setRotation(model.getLocalRotation());
        data.setScale(model.getLocalScale());
        return data;
    }

    @Override
    public void initData(T data) {
        this.data = data;
    }
    
    /**
     * 载入模型
     * @return 
     */
    protected Spatial load() {
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
