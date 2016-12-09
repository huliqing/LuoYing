/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import java.util.List;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.StateListener;
import name.huliqing.luoying.object.module.StateModule;
import name.huliqing.luoying.object.state.State;

/**
 *
 * @author huliqing
 */
public class StateServiceImpl implements StateService{
//    private static final Logger LOG = Logger.getLogger(StateServiceImpl.class.getName());

    @Override
    public void inject() {
    }

//    @Override
//    public void clearStates(Entity actor) {
//        StateModule module = actor.getModuleManager().getModule(StateModule.class);
//        if (module != null && module.getStates() != null) {
//            for (State state : module.getStates()) {
////                module.removeState(state);
//                actor.removeObjectData(state.getData(), 1);
//            }
//        }
//    }
    
    @Override
    public List<State> getStates(Entity actor) {
        StateModule module = actor.getModuleManager().getModule(StateModule.class);
        if (module != null) {
            return module.getStates();
        }
        return null;
    }

    @Override
    public void addListener(Entity actor, StateListener listener) {
        StateModule module = actor.getModuleManager().getModule(StateModule.class);
        if (module != null) {
            module.addStateListener(listener);
        }
    }

    @Override
    public boolean removeListener(Entity actor, StateListener listener) {
        StateModule module = actor.getModuleManager().getModule(StateModule.class);
        return module != null && module.removeStateListener(listener);
    }
    
    
}
