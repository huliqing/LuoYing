/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.ly.data.StateData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.state.State;

/**
 * @author huliqing
 */
public class StateModule extends AbstractModule {

    private final SafeArrayList<State> states = new SafeArrayList<State>(State.class);
    private List<StateListener> stateListeners;
    
    private Control updateControl;

    @Override
    public void updateDatas() {
        // xxx updateDatas.
    }
    
    @Override
    public void initialize(Entity actor) {
        super.initialize(actor); 
        updateControl = new AdapterControl() {
            @Override
            public void update(float tpf) {stateUpdate(tpf);}
        };
        this.entity.getSpatial().addControl(updateControl);

        List<StateData> stateDatas = actor.getData().getObjectDatas(StateData.class, null);
        if (stateDatas != null) {
            for (StateData sd : stateDatas) {
                addState((State)Loader.load(sd));
            }
        }
        
    }
    
    private void stateUpdate(float tpf) {
        for (State s : states.getArray()) {
            if (s.isEnd()) {
                removeState(s);
            } else {
                s.update(tpf);
            }
        }
    }

    @Override
    public void cleanup() {
        for (State s : states.getArray()) {
            s.cleanup();
        }
        states.clear();
        if (updateControl != null) {
            entity.getSpatial().removeControl(updateControl);
        }
        super.cleanup();
    }

    public void addState(State state) {
        // 如果已经存在相同ID的状态，则要删除旧的，因状态不允许重复。
        State oldState = getState(state.getData().getId());
        if (oldState != null) {
            removeState(oldState);
        }
        
        // 加入data列表和处理器列表
        states.add(state);
        entity.getData().addObjectData(state.getData());
        
        state.setActor(entity);
        state.initialize();
        
        // 侦听器
        if (stateListeners != null && !stateListeners.isEmpty()) {
            for (StateListener sl : stateListeners) {
                sl.onStateAdded(entity, state);
            }
        }
    }
    
    public boolean removeState(State state) {
        if (!states.contains(state))
            return false;
        
        states.remove(state);
        entity.getData().removeObjectData(state.getData());
        state.cleanup();
        // 侦听器
        if (stateListeners != null && !stateListeners.isEmpty()) {
            for (StateListener sl : stateListeners) {
                sl.onStateRemoved(entity, state);
            }
        }
        return true;
    }

    public State getState(String stateId) {
        for (State s : states.getArray()) {
            if (s.getData().getId().equals(stateId)) {
                return s;
            }
        }
        return null;
    }
    
    public List<State> getStates() {
        return states;
    }
    
    public void addStateListener(StateListener stateListener) {
        if (stateListeners == null) {
            stateListeners = new SafeArrayList<StateListener>(StateListener.class);
        }
        if (!stateListeners.contains(stateListener)) {
            stateListeners.add(stateListener);
        }
    }

    public boolean removeStateListener(StateListener stateListener) {
        return stateListeners != null && stateListeners.remove(stateListener);
    }

    public List<StateListener> getStateListeners() {
        return stateListeners;
    }
}
