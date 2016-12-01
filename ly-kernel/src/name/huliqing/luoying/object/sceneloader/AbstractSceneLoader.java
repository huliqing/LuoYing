/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.sceneloader;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.SceneLoaderData;
import name.huliqing.luoying.object.scene.Scene;

/**
 * SceneLoader抽象基类, 子类需要实现{@link #loadEntities(name.huliqing.luoying.object.scene.Scene, java.util.List) }
 * 方法来载入实体，当载入完成时需要调用方法{@link #notifyLoadedOK() }来告知已经完成场景载入。
 * @author huliqing
 */
public abstract class AbstractSceneLoader implements SceneLoader {
    
    protected SceneLoaderData data;
    protected List<SceneLoaderListener> listeners;
    
    @Override
    public void setData(SceneLoaderData data) {
        this.data = data;
    }

    @Override
    public SceneLoaderData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void addListener(SceneLoaderListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<SceneLoaderListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(SceneLoaderListener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    @Override
    public final void loadScene(Scene scene) {
        List<EntityData> tempDatas = scene.getData().getEntityDatas();
        if (tempDatas == null || tempDatas.isEmpty()) {
            notifyLoadedOK();
            return;
        }
        List<EntityData> entityDatas = new ArrayList<EntityData>(tempDatas);
        loadEntities(scene, entityDatas);
    }
    
    /**
     * 调用这个方法来通知场景的载入完成, 该方法会触发{@link SceneLoaderListener}侦听器。
     * 子类在实现{@link #loadEntities(name.huliqing.luoying.object.scene.Scene, java.util.List) }时，
     * <b>需要在场景实体载入完毕之后主动调用这个方法来通知侦听器</b>。
     */
    protected void notifyLoadedOK() {
        if (listeners != null) {
            for (SceneLoaderListener l : listeners) {
                l.onSceneLoaded();
            }
        }
    }
    
    /**
     * 载入场景实体，当载入完成时需要调用<b> {@link #notifyLoadedOK} </b>来通知侦听器。
     * @param scene
     * @param entityDatas 
     */
    protected abstract void loadEntities(Scene scene, List<EntityData> entityDatas);
}
