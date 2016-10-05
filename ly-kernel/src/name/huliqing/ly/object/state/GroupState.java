/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.state;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.data.StateData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.xml.DataFactory;

/**
 * 状态组
 * @author huliqing
 */
public class GroupState extends AbstractState {

    private List<StateData> childStateDatas;
    private List<AbstractState> states;

    @Override
    public void setData(StateData data) {
        super.setData(data);
        // 必须先从data中获取childStateDatas，因为data有可能是从存档中读取的。
        childStateDatas = (List<StateData>) data.getAttribute("childStateDatas");
        if (childStateDatas == null) {
            String[] stateArr = data.getAsArray("states");
            if (stateArr != null) {
                childStateDatas = new ArrayList<StateData>(stateArr.length);
                for (int i = 0; i < stateArr.length; i++) {
                    childStateDatas.add((StateData) DataFactory.createData(stateArr[i]));
                }
                data.setAttribute("childStateDatas", childStateDatas);
            }
        }
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        if (childStateDatas != null) {
            states = new ArrayList<AbstractState>(childStateDatas.size());
            for (StateData stateData : childStateDatas) {
                AbstractState state = Loader.load(stateData);
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
