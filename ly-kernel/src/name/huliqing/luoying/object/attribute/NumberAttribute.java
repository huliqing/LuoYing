/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;


/**
 * Number类型的属性。
 * @author huliqing
 */
public abstract class NumberAttribute extends AbstractAttribute<Number> {

    public byte byteValue() {
        return value.byteValue();
    }
    
    public short shortValue() {
        return value.shortValue();
    }
    
    public int intValue() {
        return value.intValue();
    }
    
    public float floatValue() {
        return value.floatValue();
    }
    
    public long longValue() {
        return value.longValue();
    }
    
    public double doubleValue() {
        return value.doubleValue();
    }
    
    // remove20161029
//    @Override
//    public final boolean match(Object other) {
//        if (other instanceof Number) {
//            return Double.compare(doubleValue(), ((Number) other).doubleValue()) == 0;
//        }
//        if (other instanceof String) {
//            return Double.compare(doubleValue(), Double.parseDouble((String) other)) == 0;
//        }
//        return value.equals(other);
//    }
//    
//    /**
//     * 通知提示值变侦听器
//     * @param oldValue
//     * @param newValue 
//     */
//    @Override
//    protected void notifyValueChangeListeners(Number oldValue, Number newValue) {
//        if (oldValue.doubleValue() != newValue.doubleValue()) {
//            updateDatas();
//            super.notifyValueChangeListeners(oldValue, newValue); 
//        }
//    }
    
    public abstract void add(int other);
    public abstract void add(float other);
    


    
}
