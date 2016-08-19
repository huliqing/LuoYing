/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.StateData;
import name.huliqing.core.data.module.StateModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.StateListener;
import name.huliqing.core.object.state.State;

/**
 * @author huliqing
 */
public class StateModule extends AbstractModule<StateModuleData> {

    private Actor actor;
    private final SafeArrayList<State> states = new SafeArrayList<State>(State.class);
    private List<StateListener> stateListeners;
    
    private Control updateControl;
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor); 
        this.actor = actor;
        
        // remove
//        // 从存档中载入状态，如果不是存档则从原始参数中获取
//        List<StateData> stateInits = (List<StateData>) data.getAttribute("stateDatas");
//        if (stateInits == null) {
//            String[] stateIdArr = data.getAsArray("states");
//            if (stateIdArr != null) {
//                stateInits = new ArrayList<StateData>(stateIdArr.length);
//                for (String stateId : stateIdArr) {
//                    stateInits.add((StateData) DataFactory.createData(stateId));
//                }
//            }
//        }
//        if (stateInits != null) {
//            for (StateData stateData : stateInits) {
//                addState((State)Loader.load(stateData));
//            }
//        }
//        data.setAttribute("stateDatas", stateDatas);

        if (data.getStateDatas() != null) {
            for (StateData sd : data.getStateDatas()) {
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
        
        state.setActor(actor);
        state.initialize();
        
        // 加入data列表和处理器列表
        states.add(state);
        if (data.getStateDatas() == null) {
            data.setStateDatas(new ArrayList<StateData>());
        }
        data.getStateDatas().add(state.getData());
        
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
        data.getStateDatas().remove(state.getData());
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
    
    public List<StateData> getStateDatas() {
        return data.getStateDatas();
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
