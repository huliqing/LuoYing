/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import name.huliqing.ly.data.define.MatObject;

/**
 * @deprecated 20161010不再使用
 * @author huliqing
 */
@Serializable
public class ActorData extends EntityData implements MatObject {
    
    // 角色名称
    private String name = "";
    private int mat;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(name.getBytes(), "name", null);
        oc.write(mat, "mat", -1);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        name = new String(ic.readByteArray("name", "".getBytes()), "utf-8");
        mat = ic.readInt("mat", -1);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null)
            return;
        this.name = name;
    }

    @Override
    public int getMat() {
        return mat;
    }
    
    @Override
    public void setMat(int mat) {
        this.mat = mat;
    }


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
