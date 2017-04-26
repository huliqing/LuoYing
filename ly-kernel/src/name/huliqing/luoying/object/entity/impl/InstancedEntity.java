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

import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.renderer.Caps;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.instancing.InstancedNode;
import com.jme3.shader.VarType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;

/**
 *
 * @author huliqing
 */
public class InstancedEntity extends ModelEntity implements SceneListener {

    private static final Logger LOG = Logger.getLogger(InstancedEntity.class.getName());
    
    private InstancedNode instancedNode;

    //  需要被instanced的实体列表
    private final Set<Long> entities = new HashSet<Long>();
    
    // 判断显卡是否支持instancing功能
    private boolean instancingSupported;
    
    private final Map<Mesh, Material> matMaps = new HashMap<Mesh, Material>();
    
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
        instancedNode = new InstancedNode("instanced_node");
        return instancedNode;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        instancingSupported = LuoYing.getApp().getRenderer().getCaps().contains(Caps.MeshInstancing);
        instancedNode.setLocalTranslation(0, 0, 0); // 保持位置始终在原点
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (!entities.isEmpty()) {
            Iterator<Long> it = entities.iterator();
            Entity entity;
            while (it.hasNext()) {
                entity = scene.getEntity(it.next());
                if (entity != null) {
                    addInstanceInner(entity);
                }
            }
            instancedNode.instance();
        }
        scene.addSceneListener(this);
    }
    
    @Override
    public void cleanup() {
        // remove20170424不能detachAllChildren,这会导致当instancedEntity在动态重新
        // 初始化的时候所有关联的Entity的节点丢失
//        instancedNode.detachAllChildren(); 

        scene.removeSceneListener(this);
        super.cleanup();
    }
    
    private void addInstanceInner(final Entity entity) {
        if (!instancingSupported) {
            LOG.log(Level.WARNING, "Unsupported instancing!");
            return;
        }
        entity.getSpatial().depthFirstTraversal(new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
                if (!(spatial instanceof Geometry))
                    return;
                Geometry geo = (Geometry) spatial;
                Mesh mesh = geo.getMesh();
                // 直接从mapMaps中查找已经存在的material实例, 必须相同的mesh使用相同的material实例，否则instance无意义
                Material mat = matMaps.get(mesh);
                if (mat != null) {
                    geo.setMaterial(mat);
                    return;
                }
                mat = geo.getMaterial();
                MatParam uiDef = mat.getMaterialDef().getMaterialParam("UseInstancing");
                if (uiDef != null) {
                    // 把UseInstancing设置为true
                    mat.setParam("UseInstancing", VarType.Boolean, true);
                    matMaps.put(mesh, mat);
                    LOG.log(Level.INFO, "Instancing of entityId={0}, mesh={1}, material={2}"
                            , new Object[] {entity.getEntityId(), geo.getMesh(), mat});
                } else {
                    // 如果entity中的材质没有UseInstancing属性，则不能进行instance,否则添加到InstancedNode的时候会报错.
                    entity.getSpatial().setUserData("_NoUseInstancing_", true);
                    LOG.log(Level.WARNING, "Could not find material param\"UseInstancing\", "
                            + "Unable to use instancing! entity={0}, uniqueId={1}, geometry={2}, material={3}"
                            , new Object[] {entity.getData().getId(), entity.getEntityId(), geo.getName(), mat.getMaterialDef().getAssetName()});
                }
            }
        });
        // 这表示entity中某一个节点不能进行instancing.
        if (entity.getSpatial().getUserData("_NoUseInstancing_") != null) {
            return;
        }
        instancedNode.attachChild(entity.getSpatial());
    }
    
    /**
     * 添加一个Entity到InstancedEntity
     * @param entity 
     */
    public void addInstance(Entity entity) {
        entities.add(entity.getEntityId());
        if (isInitialized()) {
            addInstanceInner(entity);
            instancedNode.instance();
        }
    }
    
    /**
     * 从InstancedEntity中移除一个Entity
     * @param entity
     * @return 
     */
    public boolean removeInstance(Entity entity) {
        boolean result = entities.remove(entity.getEntityId());
        if (result && isInitialized()) {
            instancedNode.detachChild(entity.getSpatial());
        }
        return result;
    }

    @Override
    public void onSceneLoaded(Scene scene) {
        // ignore
    }

    @Override
    public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
        if (entities.contains(entityAdded.getEntityId())) {
            addInstanceInner(entityAdded);
            instancedNode.instance();
        }
    }

    @Override
    public void onSceneEntityRemoved(Scene scene, Entity entityRemoved) {
        if (entities.contains(entityRemoved.getEntityId())) {
            removeInstance(entityRemoved);
        }
    }

    @Override
    public void onSceneEntityStateChanged(Scene scene, Entity entity) {
        if (entities.contains(entity.getEntityId())) {
            addInstanceInner(entity);
            instancedNode.instance();
        }
    }

}
