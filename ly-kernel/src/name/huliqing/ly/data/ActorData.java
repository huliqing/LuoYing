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
import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.data.define.MatObject;

/**
 * @author huliqing
 */
@Serializable
public class ActorData extends SceneObjectData implements MatObject {
    
    // 角色名称
    private String name = "";
    private int mat;
    
    // 各种控制器的数据
    private List<ModuleData> moduleDatas;
    private List<ObjectData> objectDatas;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(name.getBytes(), "name", null);
        oc.write(mat, "mat", -1);
        if (moduleDatas != null) {
            oc.writeSavableArrayList(new ArrayList<ModuleData>(moduleDatas), "moduleDatas", null);
        }
        if (objectDatas != null) {
            oc.writeSavableArrayList(new ArrayList<ObjectData>(objectDatas), "objectDatas", null);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        name = new String(ic.readByteArray("name", "".getBytes()), "utf-8");
        mat = ic.readInt("mat", -1);
        moduleDatas = ic.readSavableArrayList("moduleDatas", null);
        objectDatas = ic.readSavableArrayList("objectDatas", null);
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
     * 获取角色的所有模块
     * @return 
     */
    public List<ModuleData> getModuleDatas() {
        return moduleDatas;
    }

    public void setModuleDatas(List<ModuleData> moduleDatas) {
        this.moduleDatas = moduleDatas;
    }
    
    public void addModuleData(ModuleData moduleData) {
        if (moduleDatas == null) {
            moduleDatas = new ArrayList<ModuleData>();
        }
        if (!moduleDatas.contains(moduleData)) {
            moduleDatas.add(moduleData);
        }
    }
    
    public boolean removeModuleData(ModuleData moduleData) {
        return moduleDatas != null && moduleDatas.remove(moduleData);
    }
    
    public void addObjectData(ObjectData objectData) {
        if (objectDatas == null) {
            objectDatas = new ArrayList<ObjectData>();
        }
        if (!objectDatas.contains(objectData)) {
            objectDatas.add(objectData);
        }
    }

    public boolean removeObjectData(ObjectData objectData) {
        return objectDatas != null && objectDatas.remove(objectData);
    }
    
    public void setObjectDatas(List<ObjectData> objectDatas) {
        this.objectDatas = objectDatas;
    }
    
    /**
     * 获取角色所持有的所有物品列表
     * @return 
     */
    public List<ObjectData> getObjectDatas() {
        return objectDatas;
    }

    /**
     * 查找指定类型的数据
     * @param <T>
     * @param objectType 数据类型
     * @param store 存放结束
     * @return 
     */
    public <T extends ObjectData> List<T> getObjectDatas(Class<T> objectType, List<T> store) {
        if (store == null) {
            store = new ArrayList<T>();
        }
        if (objectDatas != null) {
            for (ObjectData od : objectDatas) {
                if (objectType.isAssignableFrom(od.getClass())) {
                    store.add((T) od);
                }
            }
        }
        return store;
    }
    
    public <T extends ObjectData> T getObjectData(String id) {
        if (objectDatas == null)
            return null;
        
        for (int i = 0; i < objectDatas.size(); i++) {
            if (objectDatas.get(i).getId().equals(id)) {
                return (T) objectDatas.get(i);
            }
        }
        return null;
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
