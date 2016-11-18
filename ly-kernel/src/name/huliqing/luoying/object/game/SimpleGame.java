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
import name.huliqing.luoying.object.gamelogic.GameLogic;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 游戏基类
 * @author huliqing
 */
public class SimpleGame implements Game<GameData> {
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
        
        // load mainScene
        Scene _scene;
        if (initScene != null) {
            _scene = initScene;
        } else {
            if (data.getSceneData() != null) {
                _scene = Loader.load(data.getSceneData());
            } else {
                LOG.log(Level.WARNING, "Scene not found! You must specify a scene for the game, now use a default scene "
                        + "instead! gameId={0}", data.getId());
                _scene = Loader.load(IdConstants.SYS_SCENE);
            }
        }
        setScene(_scene);
        
        // load main gui scene
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
        
        // Load logics
        List<GameLogicData> initLogics = data.getGameLogicDatas();
        if (initLogics != null) {
            for (GameLogicData gld : initLogics) {
                addLogic((GameLogic) Loader.load(gld));
            }
        }
        
        // 通知侦听器
        if (listeners != null) {
            for (GameListener gl : listeners) {
                gl.onGameInitialized(this);
            }
        }
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
        Scene oldScene = scene;
        for (GameListener gl : listeners.getArray()) {
            gl.onGameSceneChangeBefore(this, oldScene, newScene);
        }
        if (oldScene != null && oldScene.isInitialized()) {
            oldScene.getRoot().removeFromParent();
            oldScene.cleanup();
        }
        data.setSceneData(newScene.getData());
        scene = newScene;
        scene.setProcessorViewPorts(app.getViewPort());
        if (!scene.isInitialized()) {
            scene.initialize();
        }
        app.getRootNode().attachChild(scene.getRoot());
        for (GameListener gl : listeners.getArray()) {
            gl.onGameSceneChangeAfter(this, oldScene, newScene);
        }
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
