/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.progress;

import com.jme3.scene.Node;
import name.huliqing.luoying.data.ProgressData;

/**
 * SceneLoader抽象基类, 子类需要实现{@link #loadEntities(name.huliqing.luoying.object.scene.Scene, java.util.List) }
 * 方法来载入实体，当载入完成时需要调用方法{@link #notifyLoadedOK() }来告知已经完成场景载入。
 * @author huliqing
 */
public abstract class AbstractProgress implements Progress {
    
    protected ProgressData data;
    protected boolean initialized;
    
//    protected List<ProgressListener> listeners;
    
    @Override
    public void setData(ProgressData data) {
        this.data = data;
    }

    @Override
    public ProgressData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void initialize(Node viewRoot) {
        if (initialized) {
            throw new IllegalStateException("Progress already is initialized! progressId=" + data.getId());
        }
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
    
//    @Override
//    public void addListener(ProgressListener listener) {
//        if (listeners == null) {
//            listeners = new ArrayList<ProgressListener>();
//        }
//        if (!listeners.contains(listener)) {
//            listeners.add(listener);
//        }
//    }
//
//    @Override
//    public boolean removeListener(ProgressListener listener) {
//        return listeners != null && listeners.remove(listener);
//    }
    
//    @Override
//    public final void loadScene(Scene scene) {
//        List<EntityData> tempDatas = scene.getData().getEntityDatas();
//        if (tempDatas == null || tempDatas.isEmpty()) {
//            notifyLoadedOK();
//            return;
//        }
//        List<EntityData> entityDatas = new ArrayList<EntityData>(tempDatas);
//        loadEntities(scene, entityDatas);
//    }
    
//    /**
//     * 调用这个方法来通知场景的载入完成, 该方法会触发{@link ProgressListener}侦听器。
//     * 子类在实现{@link #loadEntities(name.huliqing.luoying.object.scene.Scene, java.util.List) }时，
//     * <b>需要在场景实体载入完毕之后主动调用这个方法来通知侦听器</b>。
//     */
//    protected void notifyLoadedOK() {
//        if (listeners != null) {
//            for (ProgressListener l : listeners) {
//                l.onSceneLoaded();
//            }
//        }
//    }
//    
//    /**
//     * 载入场景实体，当载入完成时需要调用<b> {@link #notifyLoadedOK} </b>来通知侦听器。
//     * @param scene
//     * @param entityDatas 
//     */
//    protected abstract void loadEntities(Scene scene, List<EntityData> entityDatas);


    
}
