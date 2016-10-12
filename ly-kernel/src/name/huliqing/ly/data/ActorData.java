/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.data.define.MatObject;

/**
 * @author huliqing
 */
@Serializable
public class ActorData extends ModelEntityData implements MatObject {
    
    /**
     * 获取文件模型
     * @return 
     */
    public String getFile() {
        return getAsString("file");
    }
    
    /**
     * 扩展的骨骼动画目标路径，这个参数指向一个asset中的目录,
     * 如："Models/actor/anim" 当角色使用的技能中找不到相应的动画时将会从这个目录中查找动画文件
     * @return 
     */
    public String getExtAnim() {
        return getAsString("extAnim");
    }
    
    /**
     * 指定角色原始视角方向,默认情况下为(0,0,1),如果模型默认不是该方向,则需要使用该方向指定模型的正视角方向。
     * @return 
     */
    public Vector3f getLocalForward() {
        return getAsVector3f("localForward");
    }
    
    /**
     * 判断角色是否打开hardwareSkinning,默认true
     * @return 
     */
    public boolean isHardwareSkinning() {
        return getAsBoolean("hardwareSkinning", true);
    }
    
    /**
     * 获取角色质量
     * @return 
     */
    public float getMass() {
        return getAsFloat("mass", 0);
    }
}
