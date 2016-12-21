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
package name.huliqing.ly.object.game;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.entity.impl.TreeEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 用于帮助查找场景中的空白位置
 * @author huliqing
 */
public class SceneHelper {
    
    private final Scene scene;
    
    private final BoundingBox checkBox = new BoundingBox(new Vector3f(), 5, Float.MAX_VALUE, 5);
    // 查找场景中存在模型的物体,用于判断查找空白位置
    private List<ModelEntity> tempStore;
    
    public SceneHelper(Scene scene) {
        this.scene = scene;
    } 
    
    /**
     * 判断一个位置点是否在场景中的空白位置处，这个位置点在location处，在xExtend,yExtend,zExtend的范围内，如果不
     * 与场景中的其它模型发生包围盒碰撞（除地面和水体模型外），则视为这个位置点为空白位置.
     * @param location
     * @param xExtend
     * @param yExtend
     * @param zExtend
     * @return 
     */
    public boolean isInEmptyZone(Vector3f location, float xExtend, float yExtend, float zExtend) {
        if (tempStore == null) {
            findModelEntities();
        }
        if (tempStore.isEmpty()) {
            return true;
        }
        checkBox.setCenter(location);
        checkBox.setXExtent(xExtend);
        checkBox.setYExtent(yExtend);
        checkBox.setZExtent(zExtend);
        for (ModelEntity me : tempStore) {
            if (me.getSpatial().getWorldBound().intersects(checkBox)) {
                return false;
            }
        }
        return true;
    }
    
    private void findModelEntities() {
        // 查找场景中的实体模型(除地面模型和水体之外的其它存在网格的所有模型)
        tempStore = new ArrayList<ModelEntity>();
        scene.getEntities(ModelEntity.class, tempStore);
        Iterator<ModelEntity> it = tempStore.iterator();
        Entity temp;
        while(it.hasNext()) {
            temp = it.next();
            if (!(temp instanceof TreeEntity) || temp.getSpatial() == null || temp.getSpatial().getWorldBound() == null) {
                it.remove();
            }
        }
    }
}
