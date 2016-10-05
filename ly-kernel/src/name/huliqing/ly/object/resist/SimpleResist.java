/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.resist;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.ly.data.ResistData;
import name.huliqing.ly.utils.ConvertUtils;

/**
 * 简单的抗性设置,注意不要同时设置多个对于同一状态的抗性，只有第一个被找到的
 * 有效．
 * @author huliqing
 * @param <T>
 */
public class SimpleResist<T extends ResistData> extends AbstractResist<T> {

    // 重要：该类用到clone,添加字段时注意克隆．
    
    // ---- 内部
    // 抗性列表
    private List<StateResist> resists;

    @Override
    public void setData(T data) {
        super.setData(data); 
        // format "state|factor,state|factor,state|factor"
        String[] resistsArr = data.getAsArray("resists");
        if (resistsArr != null && resistsArr.length > 0) {
            resists = new ArrayList<StateResist>(resistsArr.length);
            for (String str : resistsArr) {
                String[] strArr = str.split("\\|");
                StateResist sr = new StateResist();
                sr.setStateId(strArr[0]);
                if (strArr.length >= 2) {
                    sr.setFactor(FastMath.clamp(ConvertUtils.toFloat(strArr[1], 0), 0f, 1f));
                } else {
                    sr.setFactor(1.0f);
                }
                resists.add(sr);
            }
        }
    }

    @Override
    public float getResist(String stateId) {
        if (resists == null) {
            return 0;
        }
        StateResist sr;
        for (int i = 0; i < resists.size(); i++) {
            sr = resists.get(i);
            if (sr.getStateId().equals(stateId)) {
                return sr.getFactor();
            }
        }
        return 0;
    }

    /**
     * @param stateId
     * @param amount
     * @return 返回的数值表示了实际添加或减少的数量．
     * 如factor=0.9, amount=0.3 则返回的值为0.1;<br>
     * 如factor=0.2, amount=-0.3 则返回的值为-0.2;
     * @see StateResist#applyFactor(float) 
     */
    @Override
    public float applyResist(String stateId, float amount) {
        for (StateResist sr : resists) {
            if (sr.getStateId().equals(stateId)) {
                return sr.applyFactor(amount);
            }
        }
        return 0;
    }

    @Override
    public List<StateResist> getAll() {
        return resists != null ? resists : Collections.EMPTY_LIST;
    }

    // remove20160815
//    @Override
//    public SimpleResist clone() {
//        SimpleResist result = (SimpleResist) super.clone();
//        if (resists != null) {
//            result.resists = new ArrayList<StateResist>(resists.size());
//            for (StateResist sr : resists) {
//                result.resists.add(sr.clone());
//            }
//        }
//        return result;
//    }
    
}
