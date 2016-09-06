/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;


/**
 * Number类型的属性。
 * @author huliqing
 */
public abstract class NumberAttribute extends AbstractSimpleAttribute<Number> {

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
    
    @Override
    public final boolean match(Object other) {
        if (other instanceof Number) {
            return Double.compare(doubleValue(), ((Number) other).doubleValue()) == 0;
        }
        if (other instanceof String) {
            return Double.compare(doubleValue(), Double.parseDouble((String) other)) == 0;
        }
        return value.equals(other);
    }
    
    /**
     * 通知提示值变侦听器
     * @param oldValue
     * @param newValue 
     */
    @Override
    protected void notifyValueChangeListeners(Number oldValue, Number newValue) {
        if (oldValue.doubleValue() != newValue.doubleValue()) {
            updateData();
            super.notifyValueChangeListeners(oldValue, newValue); 
        }
    }
    
    /**
     * 更新数据到data中,这个方法在数据发生变化时被自动调用,子类实现这个方法来存档数据
     */
    protected abstract void updateData();
    
    public abstract void add(int other);
    public abstract void add(float other);
    
//    public abstract boolean isEqualTo(int other);
//    public abstract boolean isEqualTo(float other);
//    
//    public abstract boolean greaterThan(final int other);
//    public abstract boolean greaterThan(final float other);
//    
//    public abstract boolean lessThan(final int other);
//    public abstract boolean lessThan(final float other);

    
}
