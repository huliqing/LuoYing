/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.object.define.MatDefine;

/**
 * @author huliqing
 */
@Serializable
public class EntityData extends ObjectData {
    
    /**
     * 获取模型的位置，如果没有设置这个参数，则返回null.
     * @return 有可能返回null.
     */
    public Vector3f getLocation() {
        return getAsVector3f("location");
    }
    
    /**
     * 设置模型的位置
     * @param location 
     */
    public void setLocation(Vector3f location) {
        setAttribute("location", location);
    }
    
    /**
     * 获取模型的旋转变换，如果没有设置这个参数，则返回null.
     * @return 有可能返回null.
     */
    public Quaternion getRotation() {
        return getAsQuaternion("rotation");
    }
    
    /**
     * 设置模型旋转变换
     * @param rotation 
     */
    public void setRotation(Quaternion rotation) {
        setAttribute("rotation", rotation);
    }
    
    /**
     * 获取模型的缩放变换，如果没有设置这个参数，则返回null.
     * @return 有可能返回null.
     */
    public Vector3f getScale() {
        return getAsVector3f("scale");
    }
    
    /**
     * 设置模型缩放
     * @param scale 
     */
    public void setScale(Vector3f scale) {
        setAttribute("scale", scale);
    }
    
    /**
     * 获取角色的所有模块
     * @return 
     */
    public List<ModuleData> getModules() {
        return getAsObjectDataList("modules");
    }

    /**
     * 设置模块数据列表
     * @param modules 
     */
    public void setModules(List<ModuleData> modules) {
        setAttributeSavableList("modules", modules);
    }
    
    /**
     * 设置objectDatas
     * @param objectDatas 
     */
    public void setObjectDatas(List<ObjectData> objectDatas) {
        setAttributeSavableList("objectDatas", objectDatas);
    }
    
    /**
     * 获取角色所持有的所有物品列表
     * @return 
     */
    public List<ObjectData> getObjectDatas() {
        return getAsObjectDataList("objectDatas");
    }

    
    public synchronized void addModuleData(ModuleData moduleData) {
        List<ModuleData> moduleDatas = getModules();
        if (moduleDatas == null) {
            moduleDatas = new ArrayList<ModuleData>();
            setModules(moduleDatas);
        }
        if (!moduleDatas.contains(moduleData)) {
            moduleDatas.add(moduleData);
        }
    }
    
    public synchronized boolean removeModuleData(ModuleData moduleData) {
        List<ModuleData> moduleDatas = getModules();
        return moduleDatas != null && moduleDatas.remove(moduleData);
    }
    
    /**
     * 添加一个ObjectData
     * @param objectData 
     */
    public synchronized void addObjectData(ObjectData objectData) {
        List<ObjectData> ods = getObjectDatas();
        if (ods == null) {
            ods = new ArrayList<ObjectData>();
            setObjectDatas(ods);
        }
        // 注：确保不要重复添加
        if (!ods.contains(objectData)) {
            ods.add(objectData);
        }
    }

    public synchronized boolean removeObjectData(ObjectData objectData) {
        List<ObjectData> ods = getObjectDatas();
        return ods != null && ods.remove(objectData);
    }

    /**
     * 查找指定类型的数据
     * @param <T>
     * @param objectType 数据类型
     * @param store 存放结果
     * @return 
     */
    public <T extends ObjectData> List<T> getObjectDatas(Class<T> objectType, List<T> store) {
        if (store == null) {
            store = new ArrayList<T>();
        }
        List<ObjectData> objectDatas = getObjectDatas();
        if (objectDatas != null) {
            for (ObjectData od : objectDatas) {
                if (od == null) {
                    continue;
                } 
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
        List<ObjectData> objectDatas = getObjectDatas();
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
        List<ObjectData> objectDatas = getObjectDatas();
        if (objectDatas == null || objectDatas.isEmpty()) 
            return null;
        ObjectData od;
        for (int i = 0; i < objectDatas.size(); i++) {
            od = objectDatas.get(i);
            if (od != null && od.getId().equals(id)) {
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
        List<ObjectData> objectDatas = getObjectDatas();
        if (objectDatas == null || objectDatas.isEmpty()) 
            return null;
        
        ObjectData od;
        for (int i = 0; i < objectDatas.size(); i++) {
            od = objectDatas.get(i);
            if (od != null && od.getUniqueId() == id) {
                return (T) od;
            }
        }
        return null;
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
