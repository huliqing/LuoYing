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
package name.huliqing.luoying.object.scene;

import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.SceneProcessor;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.ProgressData;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.MaterialUtils;
import name.huliqing.luoying.object.progress.Progress;

/**
 * 抽象场景类
 * @author huliqing
 */
public abstract class AbstractScene implements Scene, SceneLoader.Listener {
    private static final Logger LOG = Logger.getLogger(AbstractScene.class.getName());
    
    /** 场景数据 */
    protected SceneData data;
    
    /** Entity列表，保存了当前场景中所有的物体 */
    protected final SafeArrayList<Entity> entities = new SafeArrayList<Entity>(Entity.class);
    
    /** 一直不可直接修改的列表，这个列表主要用于外部直接读取当前场景中的所有实体。但是不允许外部直接写操作这个列表 */
    protected final List<Entity> unmodifiedEntities = Collections.unmodifiableList(entities);
    
    /** 场景侦听器 */
    protected SafeArrayList<SceneListener> listeners;
    
    /** 场景根节点 */
    protected final Node root = new Node("AbstractScene-root");
    
    // ---- 这个场景“原点”物体用于处理防止出现以下BUG现象：
    // 现象：当执行粒子特效(bucket=Translucent)的时候，场景中的阴影会突然不见,粒子特效过后又恢复正常。
    // 情景：当场景中第一个添加的物体是天空盒(bucket=sky)，然后再添加其它类型的有网格物体时会发生这种情况。
    // 解决：在给场景添加阴影和天空盒功能时，确保先向场景中添加非天空盒类型的有网格的物体，或者给场景根节点添加一个隐藏的有网格物体（CullHint.Always）,
    // 以阻止这种BUG出现。
    private Geometry sceneOrigin;
    
    /** 默认的FilterPostProcessor */
    protected FilterPostProcessor defaultFilterPostProcessor;
    
    /** 
     * TranslucentBucketFilter用于处理半透明物体被一些Filter挡住的BUG，比如当场景中添加了高级水体效果时,<br>
     * 一些半透明的粒子效果会被挡住。处理这个问题的方法是：<br>
     * 1.把半透明的物体的QueueBucket设置为Bucket.Translucent<br>
     * 2.把一个TranslucentBucketFilter实例添加到FilterPostProcessor的<b>最后面</b>
     */
    protected TranslucentBucketFilter translucentBucketFilter;

    /** 默认要作为SceneProcessor的ViewPort */
    protected ViewPort[] processorViewPorts;
    
    /** 标记当前场景是否已经初始化 */
    protected boolean initialized;
    
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
        for (Entity entity : entities.getArray()) {
            entity.updateDatas();
        }
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Scene is already initialized! sceneId=" + data.getId());
        }
        
        // 如果没有设置ViewPort则使用默认场景的ViewPort
        if (processorViewPorts == null) {
            processorViewPorts = new ViewPort[]{LuoYing.getApp().getViewPort()};
        }
        
        defaultFilterPostProcessor = new FilterPostProcessor(LuoYing.getAssetManager());
        translucentBucketFilter = new TranslucentBucketFilter();
        
        // 主要用于防止阴影、天空盒、特效粒子的BUG。查看上面说明:
        if (sceneOrigin == null) {
            sceneOrigin = new Geometry("sceneOrigin", new Box(1,1,1));
            sceneOrigin.setMaterial(MaterialUtils.createUnshaded());
            sceneOrigin.setCullHint(Spatial.CullHint.Always);
            sceneOrigin.setLocalScale(0.0001f, 0.0001f, 0.0001f);
        }
        root.attachChildAt(sceneOrigin, 0);
        
        // 载入场景
        SceneLoader sl = new SceneLoader();
        sl.addListener(this);
        ProgressData pd = data.getProgress();
        if (pd != null) {
            sl.setProgress((Progress) Loader.load(pd));
        }
        sl.load(this);
    }
    
    @Override
    public void onSceneLoaded() {
        if (listeners != null) {
            for (SceneListener sl : listeners.getArray()) {
                sl.onSceneLoaded(this);
            }
        }
        // 在场景载入后才打开FilterPostProcessor, 否则有可能会导致一些异常
        // 因为FilterPostProcessor在场景动态载入的过程中，动态载入多个Filter的时候，一些Filter可能会冲突，
        // 导致FilterPostProcessor异常
         // 参考异常：
//                java.lang.UnsupportedOperationException: FrameBuffer already initialized.
//	at com.jme3.texture.FrameBuffer.setDepthTexture(FrameBuffer.java:448)
//	at com.jme3.post.FilterPostProcessor.initFilter(FilterPostProcessor.java:171)
//	at com.jme3.post.FilterPostProcessor.addFilter(FilterPostProcessor.java:112)
//	at name.huliqing.luoying.object.scene.AbstractScene.addFilter(AbstractScene.java:324)
//	at name.huliqing.luoying.object.entity.impl.DirectionalLightFilterShadowEntity$1.onSceneLoaded(DirectionalLightFilterShadowEntity.java:62)
//	at name.huliqing.luoying.object.scene.AbstractScene.onSceneLoaded(AbstractScene.java:146)
//	at name.huliqing.luoying.object.scene.SceneLoader.notifyLoadedOK(SceneLoader.java:169)
//	at name.huliqing.luoying.object.scene.SceneLoader.updateLoader(SceneLoader.java:135)
//	at name.huliqing.luoying.object.scene.SceneLoader.access$000(SceneLoader.java:43)
//	at name.huliqing.luoying.object.scene.SceneLoader$1.update(SceneLoader.java:119)
//	at com.jme3.scene.Spatial.runControlUpdate(Spatial.java:737)
//	at com.jme3.scene.Spatial.updateLogicalState(Spatial.java:880)
//	at com.jme3.scene.Node.updateLogicalState(Node.java:230)
//	at com.jme3.scene.Node.updateLogicalState(Node.java:241)
//	at com.jme3.app.SimpleApplication.update(SimpleApplication.java:242)
//	at com.jme3.system.lwjgl.LwjglAbstractDisplay.runLoop(LwjglAbstractDisplay.java:151)
//	at com.jme3.system.lwjgl.LwjglCanvas.runLoop(LwjglCanvas.java:229)
//	at com.jme3.system.lwjgl.LwjglAbstractDisplay.run(LwjglAbstractDisplay.java:232)
//	at java.lang.Thread.run(Thread.java:745)
        if (defaultFilterPostProcessor != null) {
            addProcessor(defaultFilterPostProcessor);
        }
        initialized = true;
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void cleanup() {
        if (listeners != null) {
            listeners.clear();
        }
        for (Entity entity : entities.getArray()) {
            entity.cleanup();
        }
        entities.clear();
        
        // 这里要清理掉Processor和Filter否则这些东西会常驻内存，并导致FrameBuffer越来越多，越来越慢。
        // 并且导致QueueBucket渲染存在一些问题，比如一些半透明的物体始终挡在不透明物体前面。
        if (defaultFilterPostProcessor != null) {
            if (translucentBucketFilter != null) {
                defaultFilterPostProcessor.removeFilter(translucentBucketFilter);
                translucentBucketFilter = null;
            }
            if (processorViewPorts != null) {
                for (ViewPort viewPort : processorViewPorts) {
                    viewPort.removeProcessor(defaultFilterPostProcessor);
                }
            }
            defaultFilterPostProcessor = null;
        }
        
        root.detachAllChildren();
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
        // 已经存在于场景中
        if (entities.contains(entity)) {
            return;
        }
        entities.add(entity);
        data.addEntityData(entity.getData());
        // 1.初始化,注意判断是否已经初始化过，因为Entity可能已经从外部代码进行了初始化.
        if (!entity.isInitialized()) {
            entity.initialize();
        }
        // 2.设置对场景的引用
        entity.onInitScene(this);
        if (listeners != null) {
            for (SceneListener ecl : listeners.getArray()) {
                ecl.onSceneEntityAdded(this, entity);
            }
        }
    }
    
    @Override
    public void removeEntity(Entity entity) {
        boolean removed = entities.remove(entity);
        data.removeEntityData(entity.getData());
        if (entity.isInitialized()) {
            entity.cleanup();
        }
        if (listeners != null && removed) {
            for (SceneListener ecl : listeners.getArray()) {
                ecl.onSceneEntityRemoved(this, entity);
            }
        }
    }

    @Override
    public void notifyEntityStateChanged(Entity entity) {
        if (listeners != null) {
            for (SceneListener ecl : listeners.getArray()) {
                ecl.onSceneEntityStateChanged(this, entity);
            }
        }
    }
    
    @Override
    public Entity getEntity(long entityId) {
        for (Entity e : entities.getArray()) {
            if (e.getEntityId() == entityId) {
                return e;
            }
        }
        return null;
    }
    
    @Override
    public List<Entity> getEntities() {
        return unmodifiedEntities;
    }
    
    @Override
    public <T extends Entity> List<T> getEntities(Class<T> type, List<T> store) {
        if (store == null) {
            store = new ArrayList<T>();
        }
        for (Entity so : entities.getArray()) {
            if (type.isAssignableFrom(so.getClass())) {
                store.add((T)so);
            }
        }
        return store;
    }
    
    @Override
    public <T extends Entity> List<T> getEntities(Class<T> type, Vector3f location, float radius, List<T> store) {
        if (store == null) {
            store = new ArrayList<T>();
        }
        float sqRadius = (float)Math.pow(radius, 2); // radius * radius;
        Spatial spatial;
        for (Entity so : entities.getArray()) {
            if (type.isAssignableFrom(so.getClass())) {
                spatial = so.getSpatial();
                if (spatial != null && spatial.getWorldTranslation().distanceSquared(location) <= sqRadius) {
                    store.add((T) so);
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
        // 需要保证translucentBucketFilter放在FilterPostProcessor的最后面。
        defaultFilterPostProcessor.removeFilter(translucentBucketFilter);
        defaultFilterPostProcessor.addFilter(filter);
        defaultFilterPostProcessor.addFilter(translucentBucketFilter);
    }

    @Override
    public void removeFilter(Filter filter) {
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
