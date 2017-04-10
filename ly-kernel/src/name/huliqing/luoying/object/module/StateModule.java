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
package name.huliqing.luoying.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.layer.service.MathService;
import name.huliqing.luoying.layer.service.ResistService;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.DataHandler;
import name.huliqing.luoying.object.state.State;

/**
 * @author huliqing
 */
public class StateModule extends AbstractModule implements DataHandler<StateData> {
    private final ResistService resistService = Factory.get(ResistService.class);
    private final MathService mathService = Factory.get(MathService.class);

    private final SafeArrayList<State> states = new SafeArrayList<State>(State.class);
    private Control updateControl;
    
    @Override
    public void updateDatas() {
        for (State s : states.getArray()) {
            s.updateDatas();
        }
    }
    
    @Override
    public void initialize() {
        super.initialize(); 
        updateControl = new AdapterControl() {
            @Override
            public void update(float tpf) {stateUpdate(tpf);}
        };
        this.entity.getSpatial().addControl(updateControl);
        
        // 载入状态,这里不能再计算抵抗及机率，因为这些是从配置中或存档中读取出来的，已经计算好抵抗和机率
        // 所以直接还原状态就可以。
        List<StateData> stateDatas = entity.getData().getObjectDatas(StateData.class, new ArrayList<StateData>());
        if (stateDatas != null) {
            for (StateData sd : stateDatas) {
                addStateInner(sd);
            }
        }
    }
    
    private void stateUpdate(float tpf) {
        for (State s : states.getArray()) {
            if (s.isEnd()) {
//                removeState(s);
                entity.removeObjectData(s.getData(), 1); // 这样才会触发EntityDataListener
            } else {
                s.update(tpf);
            }
        }
    }

    @Override
    public void cleanup() {
        // 不要直接调用removeStateInner进行清理，因为这会导致data从entity中移除，
        // 这是不合理的.考虑到initialize -> cleanup -> initialize -> cleanup必须是可重复执行的，
        // 如果直接调用removeStateInner清理会导致重新initialize的时候状态数据都丢了。
        for (State s : states.getArray()) {
            s.cleanup();
        }
        states.clear();
        
        // 移除控制器
        if (updateControl != null) {
            entity.getSpatial().removeControl(updateControl);
        }
        super.cleanup();
    }
    
    @Override
    public Class<StateData> getHandleType() {
        return StateData.class;
    }
    
    @Override
    public boolean handleDataAdd(StateData data, int amount) {
        // 获取对指定状态的抵抗率
        float resist = resistService.getResist(entity, data.getId());
        
        // 如果毫无低抗的话，则不计算机率，直接添加状态，优先级比较高
        if (resist <= 0) {
            removeStateInner(data);
            data.setResist(0);
            addStateInner(data);
            addEntityDataAddMessage(StateCode.DATA_ADD, data, 1);
            return true;
        }
        
        // 完全低抗，则不添加。
        if (resist >= 1.0f) {
            addEntityDataAddMessage(StateCode.DATA_ADD_FAILURE, data, 1);
            return false;
        }
        
        // 给预一定机率实现完全抵抗,只要抵抗率不为0，则有机会产生完全低抗
        if (resist > mathService.getRandom()) {
            addEntityDataAddMessage(StateCode.DATA_ADD_FAILURE, data, 1);
            return false;
        }
        
        // 移除旧的并添加新的，注意设置抗性值,抵抗效果由状态自行实现。
        removeStateInner(data);
        data.setResist(resist);
        addStateInner(data);
        addEntityDataAddMessage(StateCode.DATA_ADD, data, 1);
        return true;
    }

    @Override
    public boolean handleDataRemove(StateData data, int amount) {
        if (removeStateInner(data)) {
            addEntityDataRemoveMessage(StateCode.DATA_REMOVE, data, amount);
            return true;
        } else {
            addEntityDataRemoveMessage(StateCode.DATA_REMOVE_FAILURE_NOT_FOUND, data, amount);
            return false;
        }
    }

    @Override
    public boolean handleDataUse(StateData data) {
        return false; // ignore
    }
    
     /**
     * 设置状态
     * @param state
     * @param force
     * @return 
     */
    private void addStateInner(StateData sdAdd) {
        State state = Loader.load(sdAdd);
        states.add(state);
        entity.getData().addObjectData(state.getData());
        state.setActor(entity);
        state.initialize();
    }
    
    private boolean removeStateInner(StateData sdRemove) {
        if (states.isEmpty()) 
            return false;
        
        for (State state : states.getArray()) {
            if (state.getData().getUniqueId() == sdRemove.getUniqueId() || state.getData().getId().equals(sdRemove.getId())) {
                states.remove(state);
                entity.getData().removeObjectData(state.getData());
                state.cleanup();
                return true;
            }
        }
        return false;
    }
}
