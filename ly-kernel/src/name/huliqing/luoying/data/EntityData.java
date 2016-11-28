/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.object.define.MatDefine;
import name.huliqing.luoying.xml.CloneHelper;

/**
 * @author huliqing
 */
@Serializable
public class EntityData extends ObjectData {
    
    // 模块控制器数据
    private List<ModuleData> moduleDatas;
    // 数据，由模块控制
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
    
    /**
     * 添加一个ObjectData
     * @param objectData 
     */
    public void addObjectData(ObjectData objectData) {
        if (objectDatas == null) {
            objectDatas = new ArrayList<ObjectData>();
        }
        // 注：确保不要重复添加
        if (!objectDatas.contains(objectData)) {
            objectDatas.add(objectData);
        }
    }

    public boolean removeObjectData(ObjectData objectData) {
        return objectDatas != null && objectDatas.remove(objectData);
    }
    
    /**
     * 设置objectDatas
     * @param objectDatas 
     */
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
    
    /**
     * 获取所有指定id类型的物品。
     * @param <T>
     * @param id
     * @param store
     * @return 
     */
    public <T extends ObjectData> List<T> getObjectDatas(String id, List<T> store) {
        if (store == null) {
            store = new ArrayList<T>();
        }
        if (objectDatas != null && !objectDatas.isEmpty()) {
            for (int i = 0; i < objectDatas.size(); i++) {
                if (objectDatas.get(i).getId().equals(id)) {
                    store.add((T) objectDatas.get(i));
                }
            }
        }
        return store;
    }
    
    /**
     * 获取指定的物品，如果不存在指定物品则返回null, <b>如果存在多个id相同的物品，则返回第一个找到的物品。</b>
     * @param <T>
     * @param id
     * @return 
     */
    public <T extends ObjectData> T getObjectData(String id) {
        if (objectDatas == null || objectDatas.isEmpty()) 
            return null;
        ObjectData od;
        for (int i = 0; i < objectDatas.size(); i++) {
            od = objectDatas.get(i);
            if (od.getId().equals(id)) {
                return (T) od;
            }
        }
        return null;
    }
    
    /**
     * 通过物品的唯一id来查找物品
     * @param <T>
     * @param id
     * @return 
     */
    public <T extends ObjectData> T getObjectDataByUniqueId(long id) {
        if (objectDatas == null || objectDatas.isEmpty()) 
            return null;
        
        ObjectData od;
        for (int i = 0; i < objectDatas.size(); i++) {
            od = objectDatas.get(i);
            if (od.getUniqueId() == id) {
                return (T) od;
            }
        }
        return null;
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
     * 获取Entity图标。
     * @return 
     */
    public String getIcon() {
        return getAsString("icon");
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
    
    @Override
    public EntityData clone() {
        EntityData clone = (EntityData) super.clone();
        clone.moduleDatas = CloneHelper.cloneObjectDataList(moduleDatas);
        clone.objectDatas = CloneHelper.cloneObjectDataList(objectDatas);
        return clone;
    }
    
}
