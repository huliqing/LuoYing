/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.util.SafeArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.gamelogic.GameLogic;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;

/**
 * 游戏基类
 * @author huliqing
 */
public class SimpleGame implements Game<GameData>, SceneListener {
    private static final Logger LOG = Logger.getLogger(SimpleGame.class.getName());

    protected GameData data;
    protected SimpleApplication app;
    /** 当前的游戏场景 */
    protected final SafeArrayList<GameLogic> logics = new SafeArrayList<GameLogic>(GameLogic.class);
    
    protected final SafeArrayList<GameListener> listeners = new SafeArrayList<GameListener>(GameListener.class);
    
    protected Scene scene;
    protected Scene guiScene;
    protected boolean initialized;
    
    protected final List<GameLogic> unmodifiableLogics = Collections.unmodifiableList(logics);
    
    protected Scene initScene;
    protected Scene initGuiScene;
    protected boolean enabled = true;
    
    @Override
    public void setData(GameData data) {
        this.data = data;
    }
    
    @Override
    public GameData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        if (initialized) {
            scene.updateDatas();
            for (GameLogic gl : logics) {
                gl.updateDatas();
            }
        }
    }

    @Override
    public void initialize(Application app) {
        if (initialized) {
            throw new IllegalStateException("Game is already initialized! gameId=" + data.getId());
        }
        if (!(app instanceof SimpleApplication)) {
            throw new UnsupportedOperationException("The simpleGame only supported SimpleApplication, app=" 
                    + app.getClass().getName());
        }
        this.app = (SimpleApplication) app;
        initialized = true;
        
        // GUI场景要优先载入，因为SceneLoader可能依赖这个场景。
        Scene _GuiScene;
        if (initGuiScene != null) {
            _GuiScene = initGuiScene;
        } else {
            // 注：如果没有配置指定的GUI场景，则默认使用系统内置的空场景,这个GUI场景是必要的
            if (data.getGuiSceneData() != null) {
                _GuiScene = Loader.load(data.getGuiSceneData());
            } else {
                _GuiScene = Loader.load(IdConstants.SYS_SCENE_GUI);
            }
        }
        setGuiScene(_GuiScene);
        
        // 载入主场景
        Scene tempScene;
        if (initScene != null) {
            tempScene = initScene;
        } else {
            if (data.getSceneData() != null) {
                tempScene = Loader.load(data.getSceneData());
            } else {
                LOG.log(Level.WARNING, "Scene not found! You must specify a scene for the game, now use a default scene "
                        + "instead! gameId={0}", data.getId());
                tempScene = Loader.load(IdConstants.SYS_SCENE);
            }
        }
        setScene(tempScene);
        
        // 初始化逻辑
        List<GameLogicData> initLogics = data.getGameLogicDatas();
        if (initLogics != null) {
            for (GameLogicData gld : initLogics) {
                addLogic((GameLogic) Loader.load(gld));
            }
        }
    }
    
    /**
     * 当场景物体初始化载入完毕时该方法将被调用，注意：如果游戏过程中切换场景，则该方法将会再次被调用。
     * 只要切换并重新载入场景，则该方法都会被重新调用。
     * @param scene 
     */
    @Override
    public void onSceneLoaded(Scene scene) {
        for (GameListener gl : listeners) {
            gl.onGameSceneLoaded(this);
        }
    }
    
    @Override
    public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
        // ignore
    }

    @Override
    public void onSceneEntityRemoved(Scene scene, Entity entityRemoved) {
        // ignore
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public final void update(float tpf) {
        if (!enabled) 
            return;
        
        // 更新游戏逻辑
        for (GameLogic gl : logics.getArray()) {
            gl.update(tpf);
        }
        
        // 更新子类游戏逻辑
        simpleUpdate(tpf);
    }
    
    /**
     * 更新游戏逻辑
     * @param tpf 
     */
    protected void simpleUpdate(float tpf) {
        // 由子类实现
    }

    @Override
    public void cleanup() {
        // Notify 
        for (GameListener gl : listeners) {
            gl.onGameExit(this);
        }
        scene.cleanup();
        guiScene.cleanup();
        for (GameLogic gl : logics.getArray()) {
            gl.cleanup();
        }
        initialized = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
    
    @Override
    public void setScene(Scene newScene) {
        if (!initialized) {
            this.initScene = newScene;
            return;
        }
        // 新旧场景切换之前，触发侦听器,然后清理旧场景
        Scene oldScene = scene;
        if (oldScene != null) {
            if (listeners != null) {
                for (GameListener gl : listeners) {
                    gl.onGameSceneChangeBefore(this, oldScene, newScene);
                }
            }
            if (oldScene.isInitialized()) {
                oldScene.getRoot().removeFromParent();
                oldScene.cleanup();
            }
        }
        data.setSceneData(newScene.getData());
        scene = newScene;
        scene.setProcessorViewPorts(app.getViewPort());
        scene.addSceneListener(this);
        scene.initialize();
        app.getRootNode().attachChild(scene.getRoot());
    }

    @Override
    public void setGuiScene(Scene newGuiScene) {
        if (!initialized) {
            this.initGuiScene = newGuiScene;
            return;
        }
        if (guiScene != null && guiScene.isInitialized()) {
            guiScene.getRoot().removeFromParent();
            guiScene.cleanup();
        }
        data.setGuiSceneData(newGuiScene.getData());
        guiScene = newGuiScene;
        guiScene.setProcessorViewPorts(app.getGuiViewPort());
        if (!guiScene.isInitialized()) {
            guiScene.initialize();
        }
        app.getGuiNode().attachChild(guiScene.getRoot());
    }

    @Override
    public Scene getGuiScene() {
        return guiScene;
    }
    
    @Override
    public void addLogic(GameLogic logic) {
        if (!logics.contains(logic)) {
            logics.add(logic);
            data.addGameLogicData(logic.getData());
            if (!logic.isInitialized()) {
                logic.initialize(this);
            }
        }
    }

    @Override
    public boolean removeLogic(GameLogic logic) {
        if (logics.remove(logic)) {
            data.removeGameLogicData(logic.getData());
            logic.cleanup();
            return true;
        }
        return false;
    }
    
    @Override
    public List<GameLogic> getLogics() {
        return unmodifiableLogics;
    }
    
    @Override
    public void addListener(GameListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(GameListener listener) {
        return listeners.remove(listener);
    }

    
}
