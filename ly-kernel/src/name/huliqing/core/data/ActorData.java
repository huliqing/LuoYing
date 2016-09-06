/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.define.MatObject;
import name.huliqing.core.enums.Mat;

/**
 * @author huliqing
 */
@Serializable
public class ActorData extends ObjectData implements MatObject{
    
    // 角色名称
    private String name = "";
    
    // 角色颜色,对于一些召唤类角色需要
    private ColorRGBA color;

    // 角色出生地,坐标位置,暂时不同步到客户端
    // TODO: sync to client
    private transient Vector3f bornPlace;
    
    // 各种控制器的数据
    private List<ModuleData> moduleDatas;
    private List<ObjectData> objectDatas;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(name.getBytes(), "name", null);
        oc.write(color, "color", null);
        if (bornPlace != null) {
            oc.write(bornPlace, "bornPlace", null);
        }
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
        color = (ColorRGBA) ic.readSavable("color", null);
        bornPlace = (Vector3f)ic.readSavable("bornPlace", null);
        moduleDatas = ic.readSavableArrayList("moduleDatas", null);
        objectDatas = ic.readSavableArrayList("objectDatas", null);
    }
    
    public ActorData() {}

    public ColorRGBA getColor() {
        return color;
    }

    public void setColor(ColorRGBA color) {
        if (this.color == null) {
            this.color = new ColorRGBA(color);
        } else {
            this.color.set(color);
        }
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null)
            return;
        this.name = name;
    }

    /**
     * 获取角色的出生位置
     * @return 
     */
    public Vector3f getBornPlace() {
        return bornPlace;
    }

    /**
     * 设置角色的出生位置
     * @param bornPlace 
     */
    public void setBornPlace(Vector3f bornPlace) {
        this.bornPlace = bornPlace;
    }

    /**
     * @deprecated 暂不开放这个功能,作用不大
     * 判断角色是否打开了batch优化,默认false.不可动态设置。只能在xml中初始
     * 化配置.打开了batch功能之后角色的装备不可穿和脱.对于一些不需要动态更
     * 换装备和武器的角色可以打开该选项进行优化。如：固定装备的NPC。
     * @return 
     */
    public boolean isBatchEnabled() {
        return getAsBoolean("batch", false);
    }
    
    /**
     * 获取文件模型
     * @return 
     */
    public String getFile() {
        return getAsString("file");
    }

    @Override
    public Mat getMat() {
        int matInt = getAsInteger("mat", Mat.none.getValue());
        return Mat.identify(matInt);
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    
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
    

    
}
