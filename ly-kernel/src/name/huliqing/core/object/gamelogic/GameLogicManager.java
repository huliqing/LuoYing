/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.gamelogic;

import com.jme3.util.SafeArrayList;
import java.util.Arrays;
import java.util.List;
import name.huliqing.core.object.game.Game;

/**
 * @author huliqing
 * @param <T>
 */
public class GameLogicManager <T extends GameLogic>{
    
    // 待初始化的状态列表
    private final SafeArrayList<T> initializing;
    
    // 运行时状态列表
    private final SafeArrayList<T> states;
    
    // 待清理的列表。
    private final SafeArrayList<T> terminating;
    
    private final Game game;
 
    public GameLogicManager(Game game, Class<T> type){
        this.game = game;
        initializing = new SafeArrayList<T>(type);
        states = new SafeArrayList<T>(type);
        terminating = new SafeArrayList<T>(type);
    }

    public boolean attach(T state){
        synchronized (states){
            if (!states.contains(state) && !initializing.contains(state)){
                initializing.add(state);
                return true;
            }else{
                return false;
            }
        }
    }

    public boolean detach(T state){
        synchronized (states){
            if (states.contains(state)){
                states.remove(state);
                terminating.add(state);
                return true;
            } else if(initializing.contains(state)){
                initializing.remove(state);
                return true;
            }else{
                return false;
            }
        }
    }

    public boolean hasState(T state){
        return states.contains(state) || initializing.contains(state);
    }
    
    /**
     * Calls update for attached states, do not call directly.
     * @param tpf Time per frame.
     */
    public void update(float tpf){
    
        // Cleanup any states 
        terminatePending();

        // Initialize any states
        initializePending();

        // Update enabled states    
        T[] array = getStates();
        for (T state : array){
            if (state.isEnabled()) {
                state.update(tpf);
            }
        }
    }

    public void cleanup(){
        for (T state : states.getArray()){
            state.cleanup();
        }
        states.clear();
        
        for (T state : terminating.getArray()){
            state.cleanup();
        }
        terminating.clear();
        
        // initializing队列也需要清理，有可能存在一些在外部进行了初始化之后又没有打开PlayManager的更新逻辑，那么这些
        // 已经初始化的对象会一直停留在initializing队列中（由于PlayManager没有更新,这发生在一些特殊的逻辑中），则在退出
        // 清理时也需要playManager来负责清理
        for (T state : initializing.getArray()) {
            state.cleanup();
        }
        initializing.clear();
    }

    private void initializePending(){
        T[] array = getInitializing();
        if (array.length == 0)
            return;

        synchronized( states ) {
            // Move the states that will be initialized
            // into the active array.  In all but one case the
            // order doesn't matter but if we do this here then
            // a state can detach itself in initialize().  If we
            // did it after then it couldn't.
            List<T> transfer = Arrays.asList(array);         
            states.addAll(transfer);
            initializing.removeAll(transfer);
        }
        for (T state : array) {
            
            // 避免重复实始化
            if (state.isInitialized()) {
                continue;
            }
            
            state.initialize(game);
        }
    }
    
    private void terminatePending(){
        T[] array = getTerminating();
        if (array.length == 0)
            return;

        for (T state : array) {
            state.cleanup();
        }
        
        synchronized( states ) {
            // Remove just the states that were terminated...
            // which might now be a subset of the total terminating
            // list.
            terminating.removeAll(Arrays.asList(array));         
        }
    }    
    
    /**
     * 获取待初始化的列表（这些对象“还未”进行过initialize()）,即还未进行过
     * 初始化，正在等待初始化中.
     * @return 
     */
    public final T[] getInitializing() { 
        synchronized (states){ // 注意：这里同步的是运行时队列
            return initializing.getArray();
        }
    } 
    
    /**
     * 获取正在执行中的对象列表,(已经调用过initialize()方法),即已经进行过初
     * 始化的对象列表。
     * @return 
     */
    public final T[] getStates(){
        synchronized (states){ // 注意：这里同步的是运行时队列
            return states.getArray();
        }
    }

    /**
     * 获取等待清理的对象列表，这些对象已经从运行时列表（states）中移出，
     * 但还没有经过cleanup的对象。
     * @return 
     */
    public final T[] getTerminating() { 
        synchronized (states){ // 注意：这里同步的是运行时队列
            return terminating.getArray();
        }
    } 
}
