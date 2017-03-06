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
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义场景数据
 * @author huliqing
 */
@Serializable
public class SceneData extends ObjectData {

    /**
     * 获取场景中的物体数据列表，如果没有定义任何物体则返回null.
     * @return 
     */
    public List<EntityData> getEntityDatas() {
        return getAsObjectDataList("entityDatas");
    }

    /**
     * 设置场景物体数据列表
     * @param entityDatas 
     */
    public void setEntityDatas(List<EntityData> entityDatas) {
        setAttributeSavableList("entityDatas", entityDatas);
    }
    
    /**
     * 添加一个场景物体数据
     * @param entityData 
     */
    public void addEntityData(EntityData entityData) {
        List<EntityData> eds = getEntityDatas();
        if (eds == null) {
            eds = new ArrayList<EntityData>();
            setEntityDatas(eds);
        }
        if (!eds.contains(entityData)) {
            eds.add(entityData);
        }
    }
    
    /**
     * 移除一个场景物体数据
     * @param entityData
     * @return 
     */
    public boolean removeEntityData(EntityData entityData) {
        List<EntityData> entityDatas = getEntityDatas();
        return entityDatas != null && entityDatas.remove(entityData);
    }
    
    /**
     * 获取场景载入时的进度指示器，如果没有指定，则返回null.
     * @return 
     */
    public ProgressData getProgress() {
        return getAsObjectData("progress");
    }
    
    /**
     * 设置场景载入时的进度指示器
     * @param progress 
     */
    public void setProgress(ProgressData progress) {
        setAttribute("progress", progress);
    }
    
}
