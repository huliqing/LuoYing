/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.resist;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ResistData;

/**
 * 普通的抗性设置。
 * @author huliqing
 */
public class SimpleResist extends AbstractResist {
    
    private List<String> states;
    
    // ---- inner
    private boolean statesChanged;

    @Override
    public void setData(ResistData data) {
        super.setData(data);
        states = data.getAsStringList("states");
    }
    
    @Override
    public void updateDatas() {
        if (statesChanged && states != null && states.size() > 0) {
            data.setAttribute("states", states);
        }
    }
    
    @Override
    public boolean isResistState(String state) {
        return states != null && states.contains(state);
    }
    
    @Override
    public void addState(String state) {
        if (states == null) {
            states = new ArrayList<String>();
        }
        if (!states.contains(state)) {
            states.add(state);
            statesChanged = true;
        }
    }

    @Override
    public boolean removeState(String state) {
        if (states != null && states.remove(state)) {
            statesChanged = true;
            return true;
        }
        return false;
    }
    
}
