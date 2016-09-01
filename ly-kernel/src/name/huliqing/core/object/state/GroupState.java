/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.StateData;
import name.huliqing.core.object.Loader;

/**
 * 状态组
 * @author huliqing
 */
public class GroupState extends State {

    private String[] stateIds;
    private List<State> states;

    @Override
    public void setData(StateData data) {
        super.setData(data);
        stateIds = data.getAsArray("states");
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        if (stateIds != null) {
            states = new ArrayList<State>(stateIds.length);
            for (String sid : stateIds) {
                State state = Loader.load(sid);
                state.setActor(actor);
                state.setSourceActor(sourceActor);
                state.initialize();
                states.add(state);
            }
        }
    }
    
    @Override
    public void cleanup() {
        if (states != null) {
            for (int i = 0; i < states.size(); i++) {
                states.get(i).cleanup();
            }
            states.clear();
        }
        super.cleanup();
    }
    
}
