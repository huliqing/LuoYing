/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.scene;

import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.SceneProcessor;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import name.huliqing.ly.Ly;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.data.SceneData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.xml.DataProcessor;
import name.huliqing.ly.object.SceneObject;

/**
 * @author huliqing
 */
public class AbstractScene implements Scene {
    
    protected SceneData data;
    protected final Map<Long, SceneObject> entities = new LinkedHashMap<Long, SceneObject>();
    protected List<SceneListener> listeners;
    
    protected boolean initialized;
    
    /** 场景根节点 */
    protected final Node root = new Node("AbstractScene-root");
    
    /** 默认的FilterPostProcessor */
    protected FilterPostProcessor defaultFilterPostProcessor;
    
    /** 
     * TranslucentBucketFilter用于处理半透明物体被一些Filter挡住的BUG，比如当场景中添加了高级水体效果时,<br>
     * 一些半透明的粒子效果会被挡住。处理这个问题的方法是：<br>
     * 1.把半透明的物体的QueueBucket设置为Bucket.Translucent<br>
     * 2.把一个TranslucentBucketFilter实例添加到FilterPostProcessor的<b>最后面</b>
     */
    protected final TranslucentBucketFilter translucentBucketFilter = new TranslucentBucketFilter();

    @Override
    public void setData(SceneData data) {
        this.data = data;
    }
    
    @Override
    public SceneData getData() {
        return data;
    }
    
    @Override
    public void updateDatas() {
        // 更新所有实体
        for (SceneObject entity : entities.values()) {
            entity.updateDatas();
        }
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Scene is already initialized! sceneId=" + data.getId());
        }
        // 载入场景中的所有实体
        List<ObjectData> entityDatas = data.getSceneObjectDatas();
        if (entityDatas != null) {
            for (ObjectData od : entityDatas) {
                DataProcessor dp = Loader.load(od);
                if (!(dp instanceof SceneObject)) {
                    continue; // Only entity object
                }
                addSceneObject((SceneObject) dp);
            }
        }
        if (listeners != null) {
            for (SceneListener sl : listeners) {
                sl.onSceneInitialized(this);
            }
        }
        initialized = true;
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void cleanup() {
        for (SceneObject entity : entities.values()) {
            entity.cleanup();
        }
        entities.clear();
        root.detachAllChildren();
        if (listeners != null) {
            listeners.clear();
        }
        initialized = false;
    }    
    
    @Override
    public Node getRoot() {
        return root;
    }
    
    @Override
    public void addSceneObject(SceneObject entity) {
        data.addSceneObjectData(entity.getData());
        entities.put(entity.getObjectId(), entity);
        if (!entity.isInitialized()) {
            entity.initialize(this);
        }
        if (listeners != null) {
            for (SceneListener ecl : listeners) {
                ecl.onSceneObjectAdded(this, entity);
            }
        }
    }
    
    @Override
    public boolean removeSceneObject(SceneObject entity) {
        SceneObject removed = entities.remove(entity.getObjectId());
        if (removed == null) {
            return false;
        }
        data.removeEntityData(removed.getData());
        if (removed.isInitialized()) {
            removed.cleanup();
        }
        if (listeners != null) {
            for (SceneListener ecl : listeners) {
                ecl.onSceneObjectRemoved(this, removed);
            }
        }
        return true;
    }
    
    @Override
    public SceneObject getSceneObject(long entityId) {
        return entities.get(entityId);
    }
    
    @Override
    public Collection<SceneObject> getSceneObjects(Vector3f location, float radius, List<SceneObject> store) {
        if (store == null) {
            store = new ArrayList<SceneObject>();
        }
        float sqRadius = radius * radius;
        for (SceneObject so : entities.values()) {
            if (so.getLocation().distanceSquared(location) <= sqRadius) {
                store.add(so);
            }
        }
        return store;
    }

    @Override
    public Collection<SceneObject> getSceneObjects() {
        return Collections.unmodifiableCollection(entities.values());
    }    
    
    @Override
    public void addSpatial(Spatial objectAdded) {
        root.attachChild(objectAdded);
        if (listeners != null) {
            for (SceneListener sl : listeners) {
                sl.onSpatialAdded(this, objectAdded);
            }
        }
    }
    
    @Override
    public boolean removeSpatial(Spatial objectRemoved) {
        if (root.detachChild(objectRemoved) != -1) {
            if (listeners != null) {
                for (SceneListener sl : listeners) {
                    sl.onSpatialRemoved(this, objectRemoved);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 添加Scene Processor到场景，注：这个方法会把SceneProcessor添加到默认的ViewPort，
     * 如果需要将SceneProcessor添加到其它ViewPort，则需要给场景添加侦听器，
     * 并手动把SceneProcessor添加到其它ViewPort
     * @param sceneProcessor 
     */
    @Override
    public void addProcessor(SceneProcessor sceneProcessor) {
        if (!Ly.getApp().getViewPort().getProcessors().contains(sceneProcessor)) {
            Ly.getApp().getViewPort().addProcessor(sceneProcessor);
            if (listeners != null) {
                for (SceneListener sl : listeners) {
                    sl.onProcessorAdded(this, sceneProcessor);
                }
            }
        }
    }

    @Override
    public void removeProcessor(SceneProcessor sceneProcessor) {
        Ly.getApp().getViewPort().removeProcessor(sceneProcessor);
        if (listeners != null) {
            for (SceneListener sl : listeners) {
                sl.onProcessorRemoved(this, sceneProcessor);
            }
        }
    }
    
    @Override
    public void addFilter(Filter filter) {
        if (defaultFilterPostProcessor == null) {
            defaultFilterPostProcessor = new FilterPostProcessor(Ly.getAssetManager());
            addProcessor(defaultFilterPostProcessor);
        }
        // 需要保证translucentBucketFilter放在FilterPostProcessor的最后面。
        defaultFilterPostProcessor.removeFilter(translucentBucketFilter);
        defaultFilterPostProcessor.addFilter(filter);
        defaultFilterPostProcessor.addFilter(translucentBucketFilter);
    }

    @Override
    public void removeFilter(Filter filter) {
        if (defaultFilterPostProcessor == null) {
            return;
        }
        defaultFilterPostProcessor.removeFilter(filter);
    }
    
    @Override
    public void addSceneListener(SceneListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<SceneListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    @Override
    public boolean removeSceneListener(SceneListener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
}
