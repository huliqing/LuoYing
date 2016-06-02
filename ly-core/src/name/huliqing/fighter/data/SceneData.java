/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * @author huliqing
 */
@Serializable
public class SceneData extends ProtoData {
    
    private EnvData sky;
    private EnvData terrain;
    private Vector3f boundary;
    
    // 直射光和方向
    private ColorRGBA directionalLightColor;
    private Vector3f directionalLightDir;
    // 环境环
    private ColorRGBA ambientLightColor;
    
    // 是否打开物体阴影,要看到物体阴影效果只有同时满足以下几个条件：
    // 1.在全局配置中打开了阴影功能(Config.xml)
    // 2.设置当前场景的阴影功能（即useShadow=true)
    // 3.设置当前场景的directionalLightColor参数.
    // 4.场景中有物体支持投影及接受投影。
    private boolean useShadow;
    // 是否使用物理功能
    private boolean usePhysics;
    // 重力方向及大小
    private Vector3f gravity;
    // 是否打开physics调试
    private boolean debugPhysics;
    
    public SceneData() {}
    
    public SceneData(String id) {
        super(id);
    }

    public Vector3f getBoundary() {
        return boundary;
    }

    public void setBoundary(Vector3f boundary) {
        this.boundary = boundary;
    }

    public EnvData getSky() {
        return sky;
    }

    public void setSky(EnvData sky) {
        this.sky = sky;
    }

    public EnvData getTerrain() {
        return terrain;
    }

    public void setTerrain(EnvData terrain) {
        this.terrain = terrain;
    }

    public ColorRGBA getDirectionalLightColor() {
        return directionalLightColor;
    }

    public void setDirectionalLightColor(ColorRGBA directionalLightColor) {
        this.directionalLightColor = directionalLightColor;
    }

    public Vector3f getDirectionalLightDir() {
        return directionalLightDir;
    }

    public void setDirectionalLightDir(Vector3f directionalLightDir) {
        this.directionalLightDir = directionalLightDir;
    }

    public ColorRGBA getAmbientLightColor() {
        return ambientLightColor;
    }

    public void setAmbientLightColor(ColorRGBA ambientLightColor) {
        this.ambientLightColor = ambientLightColor;
    }

    public boolean isUseShadow() {
        return useShadow;
    }

    public void setUseShadow(boolean useShadow) {
        this.useShadow = useShadow;
    }

    public boolean isUsePhysics() {
        return usePhysics;
    }

    public void setUsePhysics(boolean usePhysics) {
        this.usePhysics = usePhysics;
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
    }

    public boolean isDebugPhysics() {
        return debugPhysics;
    }

    public void setDebugPhysics(boolean debugPhysics) {
        this.debugPhysics = debugPhysics;
    }
    
}
