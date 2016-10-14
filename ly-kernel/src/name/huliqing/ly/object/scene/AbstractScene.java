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
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.ly.LuoYing;
import name.huliqing.ly.data.EntityData;
import name.huliqing.ly.data.SceneData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.entity.Entity;

/**
 * @author huliqing
 */
public class AbstractScene implements Scene {
    private static final Logger LOG = Logger.getLogger(AbstractScene.class.getName());
    
    protected SceneData data;
    protected final Map<Long, Entity> entities = new LinkedHashMap<Long, Entity>();
    protected SafeArrayList<SceneListener> listeners;
    
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

    // 默认要作为SceneProcessor的ViewPort
    protected ViewPort[] processorViewPorts;
    
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
        for (Entity entity : entities.values()) {
            entity.updateDatas();
        }
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Scene is already initialized! sceneId=" + data.getId());
        }
        // 载入场景中的所有实体
        List<EntityData> entityDatas = data.getEntityDatas();
        if (entityDatas != null) {
            for (EntityData ed : entityDatas) {
                addEntity((Entity) Loader.load(ed));
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
        for (Entity entity : entities.values()) {
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
    public void addEntity(Entity entity) {
        if (entity == null)
            throw new NullPointerException("Entity could not be null!");
        // 如果entity已经存在于其它场景中则需要先从其它场景中移除
        if (entity.isInitialized() && entity.getScene() != this) {
            entity.getScene().removeEntity(entity);
        }
        data.addEntityData(entity.getData());
        entities.put(entity.getEntityId(), entity);
        // 初始化,注意判断是否已经初始化过，因为Entity可能已经从外部代码进行了初始化.为减少BUG发生，大部分情况下重复
        // 初始化(initialize)都会报错。
        if (!entity.isInitialized()) {
            entity.initialize(this);
        }
        // 添加到场景（放在entity.initialize之后，避免Spatial还没有创建的情况）
        if (entity.getSpatial() != null) {
            root.attachChild(entity.getSpatial());
        }
        if (listeners != null) {
            for (SceneListener ecl : listeners) {
                ecl.onSceneEntityAdded(this, entity);
            }
        }
    }
    
    @Override
    public void removeEntity(Entity entity) {
        boolean removed = data.removeEntityData(entity.getData());
        entities.remove(entity.getEntityId());
        if (entity.getSpatial() != null) {
            entity.getSpatial().removeFromParent();
        }
        if (entity.isInitialized()) {
            entity.cleanup();
        }
        if (listeners != null && removed) {
            for (SceneListener ecl : listeners) {
                ecl.onSceneEntityRemoved(this, entity);
            }
        }
    }
    
    @Override
    public Entity getEntity(long entityId) {
        return entities.get(entityId);
    }

    @Override
    public <T extends Entity> List<T> getEntities(Class<T> type, List<T> store) {
        if (store == null) {
            store = new ArrayList<T>();
        }
        for (Entity so : entities.values()) {
            if (type.isAssignableFrom(so.getClass())) {
                store.add((T) so);
            }
        }
        return store;
    }

    @Override
    public <T extends Entity> List<T> getEntities(Class<T> type, Vector3f location, float radius, List<T> store) {
        if (store == null) {
            store = new ArrayList<T>();
        }
        float sqRadius = radius * radius;
        T me;
        for (Entity so : entities.values()) {
            if (type.isAssignableFrom(so.getClass())) {
                me = (T) so;
                if (me.getSpatial().getWorldTranslation().distanceSquared(location) <= sqRadius) {
                    store.add(me);
                }
            }
        }
        return store;
    }

    @Override
    public void setProcessorViewPorts(ViewPort... viewPorts) {
        processorViewPorts = viewPorts;
    }

    /**
     * 添加Scene Processor到场景，注：这个方法会把SceneProcessor添加到默认的ViewPort，
     * 如果需要将SceneProcessor添加到其它ViewPort，则需要给场景添加侦听器，
     * 并手动把SceneProcessor添加到其它ViewPort
     * @param sceneProcessor 
     */
    @Override
    public void addProcessor(SceneProcessor sceneProcessor) {
        if (processorViewPorts == null || processorViewPorts.length <= 0) {
            LOG.log(Level.WARNING, "Could not addProcessor, because no any ViewPorts set to the scene. sceneId={0}"
                    , data.getId());
            return;
        }
        for (ViewPort viewPort : processorViewPorts) {
            if (!viewPort.getProcessors().contains(sceneProcessor)) {
                viewPort.addProcessor(sceneProcessor);
            }
        }
    }
    
    @Override
    public void removeProcessor(SceneProcessor sceneProcessor) {
        if (processorViewPorts == null) 
            return;
        for (ViewPort viewPort : processorViewPorts) {
            viewPort.removeProcessor(sceneProcessor);
        }
    }
    
    @Override
    public void addFilter(Filter filter) {
        if (defaultFilterPostProcessor == null) {
            defaultFilterPostProcessor = new FilterPostProcessor(LuoYing.getAssetManager());
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
            listeners = new SafeArrayList<SceneListener>(SceneListener.class);
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
