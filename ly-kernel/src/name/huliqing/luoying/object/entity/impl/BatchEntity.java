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

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * BatchEntity用于批量合并场景中的实体, 可以将多个相同材质的模型Batch到一个BatchEntity节点中
 * @author huliqing
 */
public class BatchEntity extends ModelEntity {
    private static final Logger LOG = Logger.getLogger(BatchEntity.class.getName());
    
    private int rows;
    private int columns;
    private Vector3f extents;
    private boolean debug;
    
    //  需要进行batch的实体列表
    private final Set<Long> entities = new HashSet<Long>();
    // 根节点
    private final Node batchRoot = new Node();
    private List<BatchZone> batchZones;
    private final BatchSceneListener bsListener = new BatchSceneListener();
    // Debug信息
    private Spatial debugRoot;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        rows = data.getAsInteger("rows", 1);
        columns = data.getAsInteger("columns", 1);
        extents = data.getAsVector3f("extents");
        debug = data.getAsBoolean("debug", debug);
        if (rows <= 0) {
            rows = 1;
        }
        if (columns <= 0) {
            columns = 1;
        }
        if (extents == null) {
            extents = new Vector3f(256, 256, 256);
        }
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
        } else {
            data.setAttribute("entities", (String)null);
        }
    }
    
    @Override
    protected Spatial loadModel() {
        return batchRoot;
    }
    
    /**
     * 获取所有进行Batch的实体
     * @return 
     */
    public Set<Long> getBatchEntities() {
        return entities;
    }
    
    /**
     * 重新对所有已经添加的实体进行batch处理
     */
    public void doRebatch() {
        if (scene == null) {
            return;
        }
        
        // 清理旧的
        if (batchZones != null) {
            for (BatchZone bz : batchZones) {
                bz.cleanup();
            }
            batchZones.clear();
        }
        
        // 重新进行batch
        batchZones = createBatchZone(rows, columns, extents.x, extents.y, extents.z);
        
        // 把Entity分配到各自的Batch区中
        for (Long e : entities) {
            Entity entity = scene.getEntity(e);
            if (entity == null || (entity instanceof BatchEntity)) {
                continue;
            }
            for (BatchZone bz : batchZones) {
                if (bz.canBatch(entity)) {
                    bz.addBatch(entity);
                    break;
                }
            }
        }
        
        // 把所有区域添加到 BatchRoot中
        for (BatchZone bz : batchZones) {
            batchRoot.attachChild(bz.batchZoneNode);
        }
        applyBatch();
    }
    
    /**
     * 添加一个实体进行Batch处理
     * @param entity
     */
    public void addBatchEntity(Entity entity) {
        if (entities.contains(entity.getEntityId())) {
            return;
        }
        entities.add(entity.getEntityId());
        if (!isInitialized() || scene == null) {
            return;
        }
        if (entity instanceof BatchEntity) {
            return;
        }
        for (BatchZone bz : batchZones) {
            if (bz.canBatch(entity)) {
                bz.addBatch(entity);
                break;
            }
        }
    }
    
    public boolean removeBatchEntity(Entity entity) {
        boolean result = entities.remove(entity.getEntityId());
        if (result) {
            for (BatchZone bz : batchZones) {
                if (bz.isBatched(entity)) {
                    bz.removeBatch(entity);
                    break;
                }
            }
        }
        return result;
    }
    
    public void applyBatch() {
        for (BatchZone bz : batchZones) {
            bz.applyBatch();
        }
    }
    
    public void setDebugVisible(boolean visible) {
        this.debug = visible;
        if (visible) {
            if (debugRoot == null) {
                debugRoot = createDebugNode();
            }
            if (scene != null) {
                scene.getRoot().attachChild(debugRoot);
            }
        } else {
            if (debugRoot != null) {
                debugRoot.removeFromParent();
            }
        }
    }

    @Override
    public void initEntity() {
        super.initEntity();
        // 不能移动
        batchRoot.setLocalTranslation(0, 0, 0);
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (scene.isInitialized()) {
            doRebatch();
        }
        scene.addSceneListener(bsListener);
        setDebugVisible(debug);
    }
    
    @Override
    public void cleanup() {
        scene.removeSceneListener(bsListener);
        batchRoot.detachAllChildren();
        if (debugRoot != null) {
            debugRoot.removeFromParent();
            debugRoot = null;
        }
        super.cleanup();
    }
    
    /**
     * 划分Batch区域
     * @param rows
     * @param columns
     * @param fullZoneXExtent
     * @param fullZoneYExtent
     * @param fullZoneZExtent
     * @return 
     */
    private List<BatchZone> createBatchZone(int rows, int columns, float fullZoneXExtent, float fullZoneYExtent, float fullZoneZExtent) {
        if (rows < 1 || columns < 1) {
            LOG.log(Level.WARNING, "Rows and Columns could not less than 1. rows={0}, columns={1}", new Object[] {rows, columns});
            return null;
        }
        if (fullZoneXExtent <= 0 || fullZoneYExtent <= 0 || fullZoneZExtent <= 0) {
            LOG.log(Level.WARNING, "Extents could not less than 0, fullZoneXExtent={0}, fullZoneYExtent={1}, fullZoneZExtent={2}"
                    , new Object[]{fullZoneXExtent, fullZoneYExtent, fullZoneZExtent});
            return null;
        }
        
        float batchZoneXExtent = fullZoneXExtent / columns;
        float batchZoneZExtent = fullZoneZExtent / rows;
        float batchZoneYExtent = fullZoneYExtent;
        List<BatchZone> zones = new ArrayList<BatchZone>(rows * columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String name = i + "_" + j;
                Vector3f center = new Vector3f(
                         -fullZoneXExtent + batchZoneXExtent + batchZoneXExtent * 2 * j
                        , 0
                        , -fullZoneZExtent + batchZoneZExtent + batchZoneZExtent * 2 * i
                );
                zones.add(new BatchZone(name, center, batchZoneXExtent, batchZoneYExtent, batchZoneZExtent));
            }
        }
        return zones;
    }
    
    private class BatchZone {
        BoundingBox bb;
        BatchNode batchZoneNode;
        Set<Long> entities = new HashSet<Long>();
        boolean needApplyBatch;
        public BatchZone(String name, Vector3f center, float xExtent, float yExtent, float zExtent) {
            batchZoneNode = new BatchNode(name);
            bb = new BoundingBox(center, xExtent, yExtent, zExtent);
        }
        
        public boolean canBatch(Entity entity) {
            return bb.contains(entity.getSpatial().getWorldTranslation());
        }
        
        public void addBatch(Entity entity) {
            batchZoneNode.attachChild(entity.getSpatial());
            entities.add(entity.getEntityId());
            needApplyBatch = true;
        }
        
        public void removeBatch(Entity entity) {
            batchZoneNode.detachChild(entity.getSpatial());
            entities.remove(entity.getEntityId());
            needApplyBatch = true;
        }
        
        public boolean isBatched(Entity entity) {
            return entities.contains(entity.getEntityId());
        }
        
        public void applyBatch() {
            if (needApplyBatch) {
                needApplyBatch = false;
                batchZoneNode.batch();
            }
        }
        
        public void cleanup() {
            needApplyBatch = false;
            batchZoneNode.detachAllChildren();
            batchZoneNode.removeFromParent();
        }
    }

    // for debug
    private Spatial createDebugNode() {
        if (batchZones == null) {
            return new Node("EmptyDebugNode");
        }
        Node debugNode = new Node("DebugNode");
        for (BatchZone bz : batchZones) {
            debugNode.attachChild(createDisplayBox(bz));
        }
        return debugNode;
    }
    
    private Spatial createDisplayBox(BatchZone bz) {
        Box box = new Box(bz.bb.getXExtent(), bz.bb.getYExtent(), bz.bb.getZExtent());
        Geometry geo = new Geometry("BatchZoneBox", box);
        Material mat = MaterialUtils.createUnshaded();
        mat.setColor("Color", new ColorRGBA(0f, 0f, 1.0f, 0.3f));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
        geo.setMaterial(mat);
        geo.setLocalTranslation(bz.bb.getCenter());
        return geo;
    }
    
    private final class BatchSceneListener implements SceneListener {
        @Override
        public void onSceneLoaded(Scene scene) {
            doRebatch();
        }

        @Override
        public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
            // ignore
        }

        @Override
        public void onSceneEntityRemoved(Scene scene, Entity entityRemoved) {
            if (entities.contains(entityRemoved.getEntityId())) {
                removeBatchEntity(entityRemoved);
                applyBatch();
            }
        }

        @Override
        public void onSceneEntityStateChanged(Scene scene, Entity entity) {
            if (entities.contains(entity.getEntityId())) {
                addBatchEntity(entity);
                applyBatch();
            }
        }
    
    }
    
    
}
