/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actormodule;

import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.ModuleData;
import name.huliqing.core.data.StateData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.actor.StateListener;
import name.huliqing.core.object.state.State;
import name.huliqing.core.xml.DataFactory;

/**
 * @author huliqing
 * @param <T>
 */
public class StateActorModule<T extends ModuleData> extends AbstractLogicActorModule<T> {

    private final SafeArrayList<State> states = new SafeArrayList<State>(State.class);
    private final List<StateData> stateDatas = new ArrayList<StateData>();
    private List<StateListener> stateListeners;

    @Override
    public void initialize() {
        super.initialize(); 
        
        // 从存档中载入状态，如果不是存档则从原始参数中获取
        List<StateData> stateInits = (List<StateData>) data.getAttribute("stateDatas");
        if (stateInits == null) {
            String[] stateIdArr = data.getAsArray("states");
            if (stateIdArr != null) {
                stateInits = new ArrayList<StateData>(stateIdArr.length);
                for (String stateId : stateIdArr) {
                    stateInits.add((StateData) DataFactory.createData(stateId));
                }
            }
        }
        
        if (stateInits != null) {
            for (StateData stateData : stateInits) {
                addState((State)Loader.load(stateData));
            }
        }
        
        data.setAttribute("stateDatas", stateDatas);
    }

    @Override
    public void cleanup() {
        for (State s : states.getArray()) {
            s.cleanup();
        }
        states.clear();
        stateDatas.clear();
        super.cleanup();
    }
    
    @Override
    public void update(float tpf) {
        for (State s : states.getArray()) {
            s.update(tpf);
        }
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
        stateDatas.add(state.getData());
        
        // 侦听器
        if (stateListeners != null && !stateListeners.isEmpty()) {
            for (StateListener sl : stateListeners) {
                sl.onStateAdded(actor, state);
            }
        }
    }
    
    public boolean removeState(State state) {
        boolean result = states.remove(state);
        stateDatas.remove(state.getData());
        state.cleanup();
        // 侦听器
        if (result && stateListeners != null && !stateListeners.isEmpty()) {
            for (StateListener sl : stateListeners) {
                sl.onStateRemoved(actor, state);
            }
        }
        return result;
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
        return stateDatas;
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
