/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.resist;

import com.jme3.math.FastMath;

/**
 *
 * @author huliqing
 */
public class StateResist implements Cloneable {
    
    // 重要：该类用到clone,添加字段时注意克隆．
    
    private String stateId;
    private float factor;
    
    public StateResist() {}

    public StateResist(String stateId, float factor) {
        this.stateId = stateId;
        this.factor = factor;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }
    
    /**
     * 增加或减少机率，注：factor的值会被限制在[0.0~1.0]之间，如果超过这个
     * 范围则会被截断．返回的数值表示了实际添加或减少的数量．
     * 如factor=0.9, amount=0.3 则返回的值为0.1;<br />
     * 如factor=0.2, amount=-0.3 则返回的值为-0.2;
     * @param amount
     * @return 
     */
    public float applyFactor(float amount) {
        float old = factor;
        factor += amount;
        factor = FastMath.clamp(factor, 0f, 1.0f);
        float trueAmount = factor - old;
        return trueAmount;
    }

    @Override
    public StateResist clone() {
        try {
            return (StateResist) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
    
//    public static void main(String[] args) {
//        StateResist sr = new StateResist("test", 0.2f);
//        float trueAdd = sr.applyFactor(-0.3f);
//        System.out.println("result=" + sr.factor + ",trueAdd=" + trueAdd);
//        trueAdd = sr.applyFactor(-0.3f);
//        System.out.println("result=" + sr.factor + ",trueAdd=" + trueAdd);
//        trueAdd = sr.applyFactor(-0.3f);
//        System.out.println("result=" + sr.factor + ",trueAdd=" + trueAdd);
//    }
}
