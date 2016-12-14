///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.data;
//
//import com.jme3.export.InputCapsule;
//import com.jme3.export.JmeExporter;
//import com.jme3.export.JmeImporter;
//import com.jme3.export.OutputCapsule;
//import com.jme3.export.Savable;
//import java.io.IOException;
//
///**
// *
// * @author huliqing
// */
//public class StateResist implements Savable, Cloneable{
//    
//    private String state;
//    private float resist;
//
//    public String getStateId() {
//        return state;
//    }
//
//    public void setStateId(String stateId) {
//        this.state = stateId;
//    }
//
//    public float getResist() {
//        return resist;
//    }
//
//    public void setResist(float resist) {
//        this.resist = resist;
//    }
//    
////    /**
////     * 增加或减少机率，注：factor的值会被限制在[0.0~1.0]之间，如果超过这个
////     * 范围则会被截断．返回的数值表示了实际添加或减少的数量．
////     * 如factor=0.9, amount=0.3 则返回的值为0.1;<br />
////     * 如factor=0.2, amount=-0.3 则返回的值为-0.2;
////     * @param amount
////     * @return 
////     */
////    public float applyFactor(float amount) {
////        float old = factor;
////        factor += amount;
////        factor = FastMath.clamp(factor, 0f, 1.0f);
////        float trueAmount = factor - old;
////        return trueAmount;
////    }
//
//    @Override
//    public StateResist clone() {
//        try {
//            return (StateResist) super.clone();
//        } catch (CloneNotSupportedException e) {
//            throw new AssertionError(e);
//        }
//    }
//
//    @Override
//    public void write(JmeExporter ex) throws IOException {
//        OutputCapsule oc = ex.getCapsule(this);
//        oc.write(state, "state", null);
//        oc.write(resist, "resist", 0);
//    }
//
//    @Override
//    public void read(JmeImporter im) throws IOException {
//        InputCapsule ic = im.getCapsule(this);
//        state = ic.readString("state", null);
//        resist = ic.readFloat("resist", 0);
//    }
//}
