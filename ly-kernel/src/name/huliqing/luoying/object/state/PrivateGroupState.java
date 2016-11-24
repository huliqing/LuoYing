/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import com.jme3.util.SafeArrayList;
import name.huliqing.luoying.data.SavableArrayList;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataFactory;

/**
 * 私有状态组，这个状态可以自己持有一些子状态，这些子状态由当前私自持有，并且和当前状态一起结束,对外部不可见。
 * 和GroupState不同，GroupState所持有的子状态是独立存在的。
 * 换句话说：statePrivateGroup会把所有子状态变成一个状态，而GroupState所持有的子状态是独立存在的。
 * @author huliqing
 */
public class PrivateGroupState extends AbstractState {

    private SavableArrayList<StateData> childStateDatas;
    
    private final SafeArrayList<State> states = new SafeArrayList<State>(State.class);
    
    @Override
    public void setData(StateData data) {
        super.setData(data);
        // 必须先从data中获取childStateDatas，因为data有可能是从存档中读取的。
        childStateDatas = (SavableArrayList) data.getAttribute("childStateDatas");
        if (childStateDatas == null) {
            String[] stateArr = data.getAsArray("states");
            if (stateArr != null) {
                childStateDatas = new SavableArrayList<StateData>(stateArr.length);
                for (int i = 0; i < stateArr.length; i++) {
                    childStateDatas.add((StateData) DataFactory.createData(stateArr[i]));
                }
                data.setAttribute("childStateDatas", childStateDatas);
            }
        }
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        for (State s : states.getArray()) {
            s.updateDatas();
        }
    }
    
    @Override
    public void initialize() {
        super.initialize();
        if (childStateDatas != null) {
            for (StateData stateData : childStateDatas.getList()) {
                stateData.setSourceActor(data.getSourceActor());
                State state = Loader.load(stateData);
                state.setActor(actor);
                state.setResist(resist);
                state.initialize();
                states.add(state);
            }
        }
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        for (State s : states.getArray()) {
            s.update(tpf);
        }
    }
    
    @Override
    public void cleanup() {
        for (State state : states) {
            state.cleanup();
        }
        states.clear();
        super.cleanup();
    }
    
}
