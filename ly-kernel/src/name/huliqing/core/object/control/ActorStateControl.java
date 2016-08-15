/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.StateListener;
import name.huliqing.core.object.state.State;

/**
 *
 * @author huliqing
 */
public class ActorStateControl extends ActorControl {

    private final SafeArrayList<State> states = new SafeArrayList<State>(State.class);
    private Actor actor;
    
    // 状态监听
    private List<StateListener> stateListeners;

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor); 
        this.actor = actor;
    }

    @Override
    public void cleanup() {
        for (State s : states.getArray()) {
            s.cleanup();
        }
        states.clear();
    }
    
    @Override
    public void actorUpdate(float tpf) {
        for (State s : states.getArray()) {
            s.update(tpf);
        }
    }

    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {
    }
    
    public void addState(State state) {
        // 如果已经存在相同ID的状态，则要删除旧的，因状态不允许重复。
        State oldState = findStateInner(state.getData().getId());
        if (oldState != null) {
            removeState(oldState);
        }
        
        state.setActor(actor);
        state.initialize();
    }
    
    public boolean removeState(State state) {
        state.cleanup();
        return states.remove(state);
    }

    public State findState(String stateId) {
        return findStateInner(stateId);
    }

    private State findStateInner(String stateId) {
        for (State s : states.getArray()) {
            if (s.getData().getId().equals(stateId)) {
                return s;
            }
        }
        return null;
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
