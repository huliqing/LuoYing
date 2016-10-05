/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.gamelogic;

import name.huliqing.ly.data.GameLogicData;
import name.huliqing.ly.object.game.Game;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractGameLogic<T extends GameLogicData>  implements GameLogic<T> {

    protected T data;
    protected boolean enabled = true;
    protected boolean initialized;
    
    /**
     * 逻辑运行的时间间隔
     */ 
    protected float interval;
    
    private float intervalTimeUsed;

    @Override
    public void setData(T data) {
        this.data = data;
        interval = data.getInterval();
    }

    @Override
    public T getData() {
        return data;
    }
    
    @Override
    public void initialize(Game game) {
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
        intervalTimeUsed += tpf;
        if (intervalTimeUsed >= interval) {
            intervalTimeUsed = 0;
            doLogic(tpf);
        }
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
 
    protected abstract void doLogic(float tpf);
}
