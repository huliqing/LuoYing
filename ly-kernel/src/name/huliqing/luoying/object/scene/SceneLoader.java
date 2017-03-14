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

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.control.Control;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.AdapterControl;
import name.huliqing.luoying.object.progress.Progress;

/**
 *
 * @author huliqing
 */
public class SceneLoader {

    private static final Logger LOG = Logger.getLogger(SceneLoader.class.getName());
    
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    
    /**
     * 场景载入器的侦听器，用于监听当场景所有实体载入完成时。
     * @author huliqing
     */
    public interface Listener {

        /**
         * 当场景实体载入完毕时该方法会被自动调用。
         */
        void onSceneLoaded();

    }
    
    // 载入器控制器
    private Control loadControl;
    // 进度条
    private Progress progress;
    protected List<Listener> listeners;
    
     // 当前的载入数
    private int count;
    
    private Scene scene;
    private List<EntityData> entityDatas;
    
    public void setProgress(Progress progress) {
        this.progress = progress;
    }
    
    public void addListener(Listener listener) {
        if (listeners == null) {
            listeners = new ArrayList<Listener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public boolean removeListener(Listener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    public void load(Scene scene) {
        this.scene = scene;
        
        // 无数据
        List<EntityData> tempDatas = scene.getData().getEntityDatas();
        if (tempDatas == null || tempDatas.isEmpty()) {
            notifyLoadedOK();
            return;
        }
        
        // 这里要复制一份过来，因为在载入过程中，原始场景的实体列表有可能发生变化，这可能会造成一些不可预测的BUG.
        entityDatas = new ArrayList<EntityData>(tempDatas);
        
        // 进度条初始化
        if (progress != null) {
            Application app = LuoYing.getApp();
            if (app instanceof SimpleApplication) {
                progress.initialize(((SimpleApplication) app).getGuiNode());
            } else {
                LOG.log(Level.WARNING, "Could not display progress, only supported SimpleApplication, but found: {0}", app.getClass());
            }
        }
        
        // 控制器，用于载入场景实体，并更新进度条
         loadControl = new AdapterControl() {
            @Override
            public void update(float tpf) {
                updateLoader(tpf);
                
    //        // test
    //        try {
    //            Thread.sleep(100);
    //        } catch (InterruptedException ex) {
    ////            Logger.getLogger(SceneLoader.class.getName()).log(Level.SEVERE, null, ex);
    //        }
    
            }
        };
        scene.getRoot().addControl(loadControl);
    }
    
    private void updateLoader(float tpf) {
        if (count >= entityDatas.size()) {
            notifyLoadedOK();
            return;
        }
        
        // 载入实体。
        EntityData ed = null;
        try {
            ed = entityDatas.get(count);
            playNetwork.addEntity(scene, (Entity)Loader.load(ed));
        } catch (Exception e) {
            String id = ed != null ? ed.getId() : "";
            long uniqueId = ed != null ? ed.getUniqueId() : -1;
            LOG.log(Level.WARNING, "Could not load entity, id=" + id + ", uniqueId=" + uniqueId, e);
        } finally {
            count++;
        }
        
        // 更新进度条
        if (progress != null) {
            progress.display((float) count / entityDatas.size());
        }

    }
    
    private void notifyLoadedOK() {
        if (loadControl != null) {
            scene.getRoot().removeControl(loadControl);
        }
        if (progress != null && progress.isInitialized()) {
            progress.cleanup();
        }
        if (listeners != null) {
            for (Listener l : listeners) {
                l.onSceneLoaded();
            }
        }
    }
}
