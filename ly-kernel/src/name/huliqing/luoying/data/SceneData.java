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
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.xml.SimpleCloner;

/**
 * 定义场景数据
 * @author huliqing
 */
@Serializable
public class SceneData extends ObjectData {
    
    // 场景中的物体数据列表.
    // 注：场景中的实体数据不会
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
    
    /**
     * 获取场景载入时的进度指示器id，如果没有指定，则返回null.
     * @return 
     */
    public String getProgress() {
        return getAsString("progress");
    }
    
    /**
     * 设置场景载入时的进度指示器
     * @param progress 
     */
    public void setProgress(String progress) {
        setAttribute("progress", progress);
    }
    
    @Override
    public SceneData clone() {
        SceneData clone = (SceneData) super.clone(); 
        clone.entityDatas = SimpleCloner.deepClone(entityDatas);
        return clone;
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
