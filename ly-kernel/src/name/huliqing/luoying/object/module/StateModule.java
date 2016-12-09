/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.manager.RandomManager;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.DataHandler;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.resist.Resist;
import name.huliqing.luoying.object.state.State;

/**
 * @author huliqing
 */
public class StateModule extends AbstractModule implements DataHandler<StateData> {

    private final SafeArrayList<State> states = new SafeArrayList<State>(State.class);
    private List<StateListener> stateListeners;
    
    private Control updateControl;
    
    /** 角色的抵抗设置 */
    private Resist resist;

    @Override
    public void updateDatas() {
        // xxx updateDatas.
    }
    
    @Override
    public void initialize(Entity actor) {
        super.initialize(actor); 
        updateControl = new AdapterControl() {
            @Override
            public void update(float tpf) {stateUpdate(tpf);}
        };
        this.entity.getSpatial().addControl(updateControl);
        
        // 载入抵抗设置:
        List<ResistData> rds = actor.getData().getObjectDatas(ResistData.class, new ArrayList<ResistData>());
        if (rds != null && !rds.isEmpty()) {
            setResist((Resist)Loader.load(rds.get(0)));
        }
        
        // 载入状态
        List<StateData> stateDatas = actor.getData().getObjectDatas(StateData.class, new ArrayList<StateData>());
        if (stateDatas != null) {
            for (StateData sd : stateDatas) {
                addState((State)Loader.load(sd), true);
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
        for (State s : states.getArray()) {
            s.cleanup();
        }
        states.clear();
        if (updateControl != null) {
            entity.getSpatial().removeControl(updateControl);
        }
        super.cleanup();
    }
    
    /**
     * 获取角色的抗性设置，如果角色没有配置这个设置，则该方法可能返回null.
     * @return 
     */
    public Resist getResist() {
        return this.resist;
    }
    
    /**
     * 设置角色的抵抗设置。
     * @param resist 
     */
    public void setResist(Resist resist) {
        if (this.resist != null) {
            entity.getData().removeObjectData(this.resist.getData());
        }
        this.resist = resist;
        this.entity.getData().addObjectData(this.resist.getData());
    }

    /**
     * 设置状态
     * @param state
     * @param force
     * @return 
     */
    private boolean addState(State state, boolean force) {
        float resistValue = getResistValue(state.getData().getId());
        if (!force && resistValue >= 1.0f) {
            return false;
        }
        state.setActor(entity);
        state.setResist(resistValue);
        addStateInner(state);
        addEntityDataAddMessage(StateCode.DATA_ADD, state.getData(), 1);
        return true;
    }
    
    // 根据角色的抵抗设置计算一个抵抗值。
    private float getResistValue(String stateId) {
        if (resist == null) {
            return 0;
        }
        float resistValue = resist.getResist(stateId);
        // 1.毫无抗性，直接添加
        if (resistValue <= 0) {
            return 0;
        }
        // 2.完全抗性
        if (resistValue >= 1.0f) {
            return 1;
        }
        // 3.给一个完全抵抗的机会
        if (resistValue >= RandomManager.getNextValue()) {
            return 1;
        }
        // 4.抵抗不成功仍有机会根据角色的最高抵抗值随机计算一个最终抵抗值，该
        // 值最高不会超过角色的最高抵抗值．该最终抵抗值可削弱一部分状态的作用．
        float resultResist = resistValue * RandomManager.getNextValue();
        return resultResist;
    }
    
    private void addStateInner(State state) {
        // 如果已经存在相同ID的状态，则要删除旧的，因状态不允许重复。
        State oldState = getState(state.getData().getId());
        if (oldState != null) {
            removeState(oldState);
        }
        
        // 加入data列表和处理器列表
        states.add(state);
        entity.getData().addObjectData(state.getData());
        
        state.setActor(entity);
        state.initialize();
        
        // 侦听器
        if (stateListeners != null && !stateListeners.isEmpty()) {
            for (StateListener sl : stateListeners) {
                sl.onStateAdded(entity, state);
            }
        }
    }
    
    private boolean removeState(State state) {
        if (!states.contains(state))
            return false;
        
        states.remove(state);
        entity.getData().removeObjectData(state.getData());
        state.cleanup();
        // 侦听器
        if (stateListeners != null && !stateListeners.isEmpty()) {
            for (StateListener sl : stateListeners) {
                sl.onStateRemoved(entity, state);
            }
        }
        // 消息
        addEntityDataRemoveMessage(StateCode.DATA_REMOVE, state.getData(), 1);
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
    
    @Override
    public Class<StateData> getHandleType() {
        return StateData.class;
    }
    
    @Override
    public boolean handleDataAdd(StateData data, int amount) {
        State state = Loader.load(data);
        return addState(state, false);
    }

    @Override
    public boolean handleDataRemove(StateData data, int amount) {
        State state = getState(data.getId());
        if (state != null) {
            return removeState(state);
        }
        return false;
    }

    @Override
    public boolean handleDataUse(StateData data) {
        return false; // ignore
    }
}
