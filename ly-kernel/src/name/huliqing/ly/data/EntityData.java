/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.object.define.MatDefine;

/**
 *
 * @author huliqing
 */
@Serializable
public class EntityData extends ObjectData {
    
    // 各种控制器的数据
    private List<ModuleData> moduleDatas;
    private List<ObjectData> objectDatas;
    
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
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
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
        moduleDatas = ic.readSavableArrayList("moduleDatas", null);
        objectDatas = ic.readSavableArrayList("objectDatas", null);
    }
    
    /**
     * 获取物全的名称
     * @return 
     */
    public String getName() {
        return getAsString("name");
    }

    /**
     * 设置物体的名称
     * @param name 
     */
    public void setName(String name) {
        setAttribute("name", name);
    }
    
    /**
     * 获取物体的质地
     * @return  
     * @see MatDefine
     * @see MatDefine#getMat(java.lang.String) 
     * @see MatDefine#getMat(int) 
     */
    public int getMat() {
        return getAsInteger("mat");
    }
    
    /**
     * 设置物体的质地
     * @param mat 
     * @see MatDefine
     * @see MatDefine#getMat(java.lang.String) 
     * @see MatDefine#getMat(int) 
     */
    public void setMat(int mat) {
        setAttribute("mat", mat);
    }
}
