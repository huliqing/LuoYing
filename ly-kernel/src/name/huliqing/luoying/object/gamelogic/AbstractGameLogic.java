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
    public void updateDatas() {
        // 由子类视情况覆盖
    }
    
    @Override
    public final void initialize(Game game) {
        if (initialized) {
            throw new IllegalStateException("GameLogic already initialized! gameLogic=" + getClass().getName());
        }
        initialized = true;
        this.game = game;
        this.game.addListener(this);
        
        // 调用子类的游戏逻辑初始化，这个方法只调用一次，并且必须在场景完成载入之前初始化。
        // 避免NPE
        logicInit(game);
        
        // Game逻辑有可能是在游戏过程中动态添加，这个时候场景可能是已经完成了初始化的，
        // 则需要触发一次OnGameSceneLoaded, 这确保所有游戏逻辑在添加的时候，
        // 不管是否是在场景载入之前还是载入之后，都会触发一次游戏逻辑的onGameSceneLoaded.
        if (game.getScene() != null && game.getScene().isInitialized()) {
            onGameSceneLoaded(game);
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
        if (!enabled) {
            return;
        }
        intervalUsed += tpf;
        if (intervalUsed > interval) {
            intervalUsed = 0;
            logicUpdate(tpf);
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
    
//    /**
//     * 判断当前游戏场景是否有效，即判断场景是否已经初始化完毕。
//     * @return 
//     */
//    protected boolean isSceneValid() {
//        return sceneValid;
//    }
    
    /**
     * 当游戏场景切换<b>并完成载入后</b>该方法会被立即调用一次,以通知场景完成载入。
     * @param game 
     */
    @Override
    public void onGameSceneLoaded(Game game) {}

    /**
     * 当游戏场景切换之前该方法会被调用，每次切换场景之前该方法都会调用一次，默认情况下该方法不会处理任何逻辑，
     * 由子类视情况覆盖实现。
     * @param game
     * @param oldScene
     * @param newScene 
     */
    @Override
    public void onGameSceneChange(Game game, Scene oldScene, Scene newScene) {}
    
    /**
     * 当游戏退出之前该方法会被调用一次，默认情况下该方法不会处理任何逻辑，由子类视情况覆盖实现。
     * @param game 
     */
    @Override
    public void onGameExit(Game game) {}
    
    /**
     * 初始化逻辑，该方法在游戏逻辑整个生命周期中只执行一次。
     * @param game
     */
    protected abstract void logicInit(Game game);
    
    /**
     * 执行游戏逻辑
     * @param tpf 
     */
    protected abstract void logicUpdate(float tpf);
}
