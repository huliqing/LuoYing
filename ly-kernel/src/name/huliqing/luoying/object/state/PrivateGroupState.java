/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
                stateData.setResist(data.getResist());
                stateData.setSourceActor(data.getSourceActor());
                State state = Loader.load(stateData);
                state.setActor(entity);
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
