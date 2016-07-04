/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.state;

import com.jme3.app.Application;
import name.huliqing.fighter.object.PlayManager;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 角色状态处理器
 * @author huliqing
 */
public class StateProcessorImpl implements StateProcessor {
    
    private final PlayManager<State> pm;
    private final Actor actor;
    
    public StateProcessorImpl(Application app, Actor actor) {
        this.actor = actor;
        pm = new PlayManager<State>(app, State.class);
    }
    
    @Override
    public void update(float tpf) {
        pm.update(tpf);
    }
    
    @Override
    public void addState(State state) {
        // 如果已经存在相同ID的状态，则要删除旧的，因状态不允许重复。
        State oldState = findStateInner(state.getData().getId());
        if (oldState != null) {
            pm.detach(oldState);
        }
        
        state.setActor(actor);
        pm.attach(state);
    }
    
    @Override
    public State removeState(String stateId) {
        State state = findStateInner(stateId);
        if (state != null) {
            pm.detach(state);
        }
        return state;
    }

    @Override
    public State findState(String stateId) {
        return findStateInner(stateId);
    }

    @Override
    public void cleanup() {
        pm.cleanup();
    }

    private State findStateInner(String stateId) {
        State[] states = pm.getStates();
        for (State s : states) {
            if (s.getData().getId().equals(stateId)) {
                return s;
            }
        }
        
        State[] inits = (State[]) pm.getInitializing();
        for (State s : inits) {
            if (s.getData().getId().equals(stateId)) {
                return s;
            }
        }
        
        // Terminating队列不需要再处理，因为这个队列中都是已经是被移除的对象。
//        State[] terminatings = (State[]) pm.getTerminating();
        
        return null;
    }
}
