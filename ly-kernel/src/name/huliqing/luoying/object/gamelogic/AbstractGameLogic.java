/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.gamelogic;

import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.object.game.Game;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractGameLogic<T extends GameLogicData>  implements GameLogic<T> {

    protected T data;
    
    protected boolean enabled = true;
    
    /** 游戏逻辑是否已经初始化 */
    protected boolean initialized;
    
    /** 逻辑运行的时间间隔 */ 
    protected float interval;
    
    private float intervalTimeUsed;
    
    public AbstractGameLogic() {}
    
    /**
     * 逻辑的执行频率，单位秒，默认1秒。
     * @param interval 
     */
    public AbstractGameLogic(float interval) {
        this.interval = interval;
    }

    @Override
    public void setData(T data) {
        this.data = data;
        interval = data.getInterval();
    }

    @Override
    public T getData() {
        return data;
    }

    /**
     * 由覆盖这个方法来更新游戏数据到Data中, 以保证游戏在存档的时候能够获得实时的状态。
     */
    @Override
    public void updateDatas() {}
    
    @Override
    public void initialize(Game game) {
        if (initialized) {
            throw new IllegalStateException("GameLogic already initialized! gameLogic=" + getClass().getName());
        }
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
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
    public final void update(float tpf) {
        if (!enabled) {
            return;
        }
        intervalTimeUsed += tpf;
        if (intervalTimeUsed > interval) {
            intervalTimeUsed = 0;
            doLogic(tpf);
        }
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
 
    /**
     * 执行游戏逻辑
     * @param tpf 
     */
    protected abstract void doLogic(float tpf);
}
