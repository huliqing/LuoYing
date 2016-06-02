/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.resist;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.fighter.data.ResistData;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 * 全部状态都抵抗,可指定一部分例外．
 * @author huliqing
 */
public class AllResist extends AbstractResist{
    
    /**
     * 例外列表，除了这个列表中的其它都全抵抗．这个列表也可以为空，为null.
     * 或者单独设置．
     */
    private List<StateResist> excepts;
    
    public AllResist() {
        super();
    }
    
    public AllResist(ResistData data) {
        super(data);
        
        // format "state|factor,state|factor,state|factor"
        String[] tempExcepts = data.getProto().getAsArray("excepts");
        if (tempExcepts != null && tempExcepts.length > 0) {
            excepts = new ArrayList<StateResist>(tempExcepts.length);
            for (String str : tempExcepts) {
                String[] strArr = str.split("\\|");
                StateResist sr = new StateResist();
                sr.setStateId(strArr[0]);
                if (strArr.length >= 2) {
                    sr.setFactor(FastMath.clamp(ConvertUtils.toFloat(strArr[1], 0), 0f, 1f));
                } else {
                    sr.setFactor(0);
                }
                excepts.add(sr);
            }
        }
    }

    @Override
    public float getResist(String stateId) {
        if (excepts == null || excepts.isEmpty()) {
            return 1.0f;
        }
        for (StateResist sr : excepts) {
            if (sr.getStateId().equals(stateId)) {
                return sr.getFactor();
            }
        }
        return 1.0f;
    }

    /**
     * 增加或降低指定的抗性值，注：只有存在于例外列表中的抗性设置才有效．
     * 如果stateId不存在例外列表中，则直接返回0.
     * @param stateId
     * @param amount
     * @return 
     */
    @Override
    public float applyResist(String stateId, float amount) {
        if (excepts == null || excepts.isEmpty()) {
            return 0f;
        }
        for (StateResist sr : excepts) {
            if (sr.getStateId().equals(stateId)) {
                return sr.applyFactor(amount);
            }
        }
        return 0f;
    }

    /**
     * 该方法只返回例外列表中的抗性设置,如果没有例外设置，将返回空列表．
     * @return 
     */
    @Override
    public List<StateResist> getAll() {
        return excepts != null ? excepts : Collections.EMPTY_LIST;
    }

    @Override
    public Resist clone() {

        AllResist result = (AllResist) super.clone();
        if (excepts != null) {
            result.excepts = new ArrayList<StateResist>(excepts.size());
            for (StateResist sr : excepts) {
                result.excepts.add(sr.clone());
            }
        }
        return result;

    }
    
    
    
}
