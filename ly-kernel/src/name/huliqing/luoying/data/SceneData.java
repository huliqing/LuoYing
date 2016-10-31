/*
 * To change this template, choose Tools | Templates
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

/**
 * 定义场景数据
 * @author huliqing
 */
@Serializable
public class SceneData extends ObjectData {
    
    // 场景中的物体数据列表
    private List<EntityData> entityDatas;
    
    /**
     * 获取场景中的物体数据列表，如果没有定义任何物体则返回null.
     * @return 
     */
    public List<EntityData> getEntityDatas() {
        return entityDatas;
    }

    /**
     * 设置场景物体数据列表
     * @param entityDatas 
     */
    public void setEntityDatas(List<EntityData> entityDatas) {
        this.entityDatas = entityDatas;
    }
    
    /**
     * 添加一个场景物体数据
     * @param entityData 
     */
    public void addEntityData(EntityData entityData) {
        if (entityDatas == null) {
            entityDatas = new ArrayList<EntityData>();
        }
        if (!entityDatas.contains(entityData)) {
            entityDatas.add(entityData);
        }
    }
    
    /**
     * 移除一个场景物体数据
     * @param entityData
     * @return 
     */
    public boolean removeEntityData(EntityData entityData) {
        return entityDatas != null && entityDatas.remove(entityData);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        if (entityDatas != null) {
            oc.writeSavableArrayList(new ArrayList<EntityData>(entityDatas), "entityDatas", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        entityDatas = ic.readSavableArrayList("entityDatas", null);
    }
}
