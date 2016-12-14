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
 * 除了excepts所示例表之外，所有其它状态都一律抵抗
 * @author huliqing
 */
public class AllResist extends AbstractResist {

    /**
     * 例外的，不进行抵抗的属性
     */
    private List<String> excepts;
    
    // ---- inner
    private boolean statesChanged;
    
    @Override
    public void setData(ResistData data) {
        super.setData(data);
        excepts = data.getAsStringList("excepts");
    }
    
    @Override
    public void updateDatas() {
        if (statesChanged && excepts != null && excepts.size() > 0) {
            data.setAttribute("excepts", excepts);
        }
    }

    @Override
    public boolean isResistState(String state) {
        if (excepts != null && excepts.contains(state)) {
            return false;
        }
        return true;
    }

    @Override
    public void addState(String state) {
        if (excepts != null) {
            excepts.remove(state);
            statesChanged = true;
        }
    }

    @Override
    public boolean removeState(String state) {
        if (excepts == null) {
            excepts = new ArrayList<String>();
        }
        if (!excepts.contains(state)) {
            excepts.add(state);
            statesChanged = true;
            return true;
        }
        return false;
    }
    
}
