/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.Loader;

/**
 * 状态组, 这个状态可以用于打包子状态，当给角色添加这个状态之后，所有子状态都会一起添加到角色身上。
 * 子状态是独立的，不受当前状态的设置影响。
 * @author huliqing
 */
public class GroupState extends AbstractState {

    private String[] states;
    
    // 标记子状态是否已经添加到角色,以避免重复添加。
    // 当角色获得这个状态后，在状态未消失之前存档，然后再读档的情况下，这个值会为true,这时应避免重复载入状态。
    private boolean childrenApplied;
    
    @Override
    public void setData(StateData data) {
        super.setData(data);
        childrenApplied = data.getAsBoolean("childrenApplied", childrenApplied);
        states = data.getAsArray("states");
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("childrenApplied", childrenApplied);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        if (childrenApplied) {
            return;
        }
        
        if (states != null) {
            for (String sid : states) {
                StateData stateData = Loader.loadData(sid);
                stateData.setSourceActor(data.getSourceActor());
                entity.addObjectData(stateData, 1);
            }
        }
        childrenApplied = true;
        updateDatas();
    }
    
    @Override
    public void cleanup() {
        // 不要去清理子状态，这些子状态由各子去清理,只标记childrenApplied=false就可以。
        childrenApplied = false;
        super.cleanup();
    }
    
}
