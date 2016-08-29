/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.core.data.StateData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.state.State;

/**
 * @author huliqing
 */
public class StateModule extends AbstractModule {

    private Actor actor;
    private final SafeArrayList<State> states = new SafeArrayList<State>(State.class);
    private List<StateListener> stateListeners;
    
    private Control updateControl;
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor); 
        this.actor = actor;

        List<StateData> stateDatas = actor.getData().getObjectDatas(StateData.class, null);
        if (stateDatas != null) {
            for (StateData sd : stateDatas) {
                addState((State)Loader.load(sd));
            }
        }
        
        updateControl = new AdapterControl() {
            @Override
            public void update(float tpf) {stateUpdate(tpf);}
        };
        this.actor.getSpatial().addControl(updateControl);
    }
    
    private void stateUpdate(float tpf) {
        for (State s : states.getArray()) {
            s.update(tpf);
        }
    }

    @Override
    public void cleanup() {
        for (State s : states.getArray()) {
            s.cleanup();
        }
        states.clear();
        if (updateControl != null) {
            actor.getSpatial().removeControl(updateControl);
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
        actor.getData().addObjectData(state.getData());
        
        state.setActor(actor);
        state.initialize();
        
        // 侦听器
        if (stateListeners != null && !stateListeners.isEmpty()) {
            for (StateListener sl : stateListeners) {
                sl.onStateAdded(actor, state);
            }
        }
    }
    
    public boolean removeState(State state) {
        if (!states.contains(state))
            return false;
        
        states.remove(state);
        actor.getData().removeObjectData(state.getData());
        state.cleanup();
        // 侦听器
        if (stateListeners != null && !stateListeners.isEmpty()) {
            for (StateListener sl : stateListeners) {
                sl.onStateRemoved(actor, state);
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
