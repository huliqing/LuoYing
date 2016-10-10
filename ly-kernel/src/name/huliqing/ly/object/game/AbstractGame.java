/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.util.SafeArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.ly.data.GameData;
import name.huliqing.ly.data.GameLogicData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.gamelogic.GameLogic;
import name.huliqing.ly.object.scene.Scene;

/**
 * 游戏基类
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractGame<T extends GameData> implements Game<T>{

    protected T data;
    
    /** 当前的游戏场景 */
    protected final SafeArrayList<GameLogic> logics = new SafeArrayList<GameLogic>(GameLogic.class);
    protected final SafeArrayList<GameListener> listeners = new SafeArrayList<GameListener>(GameListener.class);
    protected Scene scene;
    protected Scene guiScene;
    protected boolean initialized;
    
    private final List<GameLogic> unmodifiableLogics = Collections.unmodifiableList(logics);
    
    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public T getData() {
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
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Game is already initialized! gameId=" + data.getId());
        }
        
        // Load scene
        Scene initScene = Loader.load(data.getSceneData());
        setScene(initScene);
        
        // Load logics
        List<GameLogicData> initLogics = data.getGameLogicDatas();
        if (initLogics != null) {
            for (GameLogicData gld : initLogics) {
                addLogic((GameLogic) Loader.load(gld));
            }
        }
        
        initialized = true;
        
        // Notify 
        for (GameListener gl : listeners) {
            gl.onGameInitialized(this);
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void update(float tpf) {
        for (GameLogic gl : logics.getArray()) {
            gl.update(tpf);
        }
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
    public Scene getScene() {
        return scene;
    }
    
    @Override
    public void setScene(Scene newScene) {
        if (scene == newScene) {
            return;
        }
        Scene oldScene = scene;
        for (GameListener gl : listeners.getArray()) {
            gl.onGameSceneChangeBefore(this, oldScene, newScene);
        }
        if (oldScene != null) {
            oldScene.cleanup();
        }
        scene = newScene;
        data.setSceneData(newScene.getData());
        if (!scene.isInitialized()) {
            scene.initialize();
        }
        for (GameListener gl : listeners.getArray()) {
            gl.onGameSceneChangeAfter(this, oldScene, newScene);
        }
    }

    @Override
    public void setGuiScene(Scene scene) {
        if (guiScene == scene) {
            return;
        }
        if (guiScene != null) {
            guiScene.cleanup();
        }
        guiScene = scene;
        if (!guiScene.isInitialized()) {
            guiScene.initialize();
        }
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
