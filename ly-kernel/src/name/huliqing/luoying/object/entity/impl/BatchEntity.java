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
package name.huliqing.luoying.object.entity.impl;

import com.jme3.scene.BatchNode;
import com.jme3.scene.Spatial;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;

/**
 *
 * @author huliqing
 */
public class BatchEntity extends ModelEntity {

    private BatchNode batchNode;
    
    //  需要进行batch的实体列表
    private final Set<Long> entities = new HashSet<Long>();
    
    private final SceneListener sceneListener = new BatchSceneListener();

    @Override
    public void setData(EntityData data) {
        super.setData(data);
        long[] tempEntities = data.getAsLongArray("entities");
        if (tempEntities != null) {
            for (long te : tempEntities) {
                entities.add(te);
            }
        }
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        if (entities != null && !entities.isEmpty()) {
            Long[] tempEntities = entities.toArray(new Long[0]);
            long[] storeEntities = new long[tempEntities.length];
            for (int i = 0; i < tempEntities.length; i++) {
                storeEntities[i] = tempEntities[i];
            }
            data.setAttribute("entities", storeEntities);
        }
    }
    
    @Override
    protected Spatial loadModel() {
        batchNode = new BatchNode();
        return batchNode;
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (scene.isInitialized() && !entities.isEmpty()) {
            Iterator<Long> it = entities.iterator();
            Entity entity;
            while (it.hasNext()) {
                entity = scene.getEntity(it.next());
                if (entity != null) {
                    batchNode.attachChild(entity.getSpatial());
                }
            }
            batchNode.batch();
        }
        scene.addSceneListener(sceneListener);
    }

    @Override
    public void cleanup() {
        scene.removeSceneListener(sceneListener);
        super.cleanup();
    }
    
    /**
     * 添加一个Entity进行batch,该Entity将会被Batch到当前BatchEntity中, 添加后需要调用 {@link #batch() }.
     * @param entity 
     */
    public void addBatchEntity(Entity entity) {
        entities.add(entity.getEntityId());
        if (isInitialized()) {
            batchNode.attachChild(entity.getSpatial());
        }
    }
    
    /**
     * 从batch中移除指定的实体.移除后需要重新调用 {@link #batch() }
     * @param entity 
     * @return  
     */
    public boolean removeBatchEntity(Entity entity) {
        boolean result = entities.remove(entity.getEntityId());
        if (result && isInitialized()) {
            batchNode.detachChild(entity.getSpatial());
        }
        return result;
    }
    
    /**
     * 调用Batch(),对已经添加到当前BatchEntity中的所有实体进行批处理。
     * @see BatchNode#batch() }
     */
    public void batch() {
        batchNode.batch();
    }
    
    private final class BatchSceneListener implements SceneListener {
        
        @Override
        public void onSceneLoaded(Scene scene) {
             if (!entities.isEmpty()) {
                Iterator<Long> it = entities.iterator();
                Entity entity;
                while (it.hasNext()) {
                    entity = scene.getEntity(it.next());
                    if (entity != null) {
                        batchNode.attachChild(entity.getSpatial());
                    }
                }
                batchNode.batch();
            }
        }

        @Override
        public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
            // ignore
        }

        @Override
        public void onSceneEntityRemoved(Scene scene, Entity entityRemoved) {
            if (entities.contains(entityRemoved.getEntityId())) {
                removeBatchEntity(entityRemoved);
                batchNode.batch();
            }
        }

        @Override
        public void onSceneEntityStateChanged(Scene scene, Entity entity) {
            if (entities.contains(entity.getEntityId())) {
                addBatchEntity(entity);
                batchNode.batch();
            }
        }
        
    }
    
    
    
    
}
