/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.gamelogic;

import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.game.GameListener;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 游戏逻辑的抽象类。
 * @author huliqing
 */
public abstract class AbstractGameLogic  implements GameLogic<GameLogicData>, GameListener {

    protected GameLogicData data;
    
    /** 逻辑运行的时间间隔 */ 
    protected float interval;
    
    // ---- inner
    protected Game game;
    /** 游戏逻辑是否已经初始化 */
    protected boolean enabled = true;
    protected boolean initialized;
    protected float intervalUsed;
    
    /** 判断场景是否有效,只有场景载入完毕后，场景才有效。*/
    protected boolean sceneValid;
    
    public AbstractGameLogic() {}
    
    /**
     * 逻辑的执行频率，单位秒，默认1秒。
     * @param interval 
     */
    public AbstractGameLogic(float interval) {
        this.interval = interval;
    }

    @Override
    public void setData(GameLogicData data) {
        this.data = data;
        interval = data.getAsFloat("interval", interval);
    }

    @Override
    public GameLogicData getData() {
        return data;
    }

    /**
     * 由覆盖这个方法来更新游戏数据到Data中, 以保证游戏在存档的时候能够获得实时的状态。
     */
    @Override
    public void updateDatas() {}
    
    @Override
    public final void initialize(Game game) {
        if (initialized) {
            throw new IllegalStateException("GameLogic already initialized! gameLogic=" + getClass().getName());
        }
        this.game = game;
        this.game.addListener(this);
        
        sceneValid = game.getScene() != null && game.getScene().isInitialized();
        if (sceneValid) {
            doLogicInit(game);
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void cleanup() {
        game.removeListener(this);
        initialized = false;
    }

    @Override
    public final void update(float tpf) {
        if (!enabled || !sceneValid) {
            return;
        }
        intervalUsed += tpf;
        if (intervalUsed > interval) {
            intervalUsed = 0;
            doLogicUpdate(tpf);
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * 判断当前游戏场景是否有效，即判断场景是否已经初始化完毕。
     * @return 
     */
    protected boolean isSceneValid() {
        return sceneValid;
    }

    @Override
    public void onGameSceneLoaded(Game game) {
        sceneValid = true;
        if (!initialized) {
            doLogicInit(game);
            initialized = true;
        }
    }

    @Override
    public void onGameExit(Game game) {
        // 由子类视情况覆盖调用
    }

    @Override
    public void onGameSceneChangeBefore(Game game, Scene oldScene, Scene newScene) {
        sceneValid = false;
    }
    
    /**
     * 初始化逻辑，该方法在整个游戏逻辑的生命周期中只调用一次，并且是在首个场景初始化载入完毕之后被调用。
     * 之后切换场景再不再调用。
     * @param game
     */
    protected void doLogicInit(Game game) {
        // 由子类实现
    }
    
    /**
     * 执行游戏逻辑
     * @param tpf 
     */
    protected abstract void doLogicUpdate(float tpf);
}
