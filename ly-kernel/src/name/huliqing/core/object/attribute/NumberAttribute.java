/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;


/**
 * Number类型的属性。
 * @author huliqing
 * @param <T>
 */
public abstract class NumberAttribute<T extends Number> extends AbstractSimpleAttribute<Number> {

    @Override
    public Number getValue() {
        return super.getValue(); 
    }

    @Override
    public void setValue(Number newValue) {
        super.setValue(newValue);
    }
    
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
        return this.equals(other);
    }
    
    public abstract void add(int other);
    public abstract void add(float other);
    public abstract void subtract(int other);
    public abstract void subtract(float other);
    
//    public abstract boolean isEqualTo(int other);
//    public abstract boolean isEqualTo(float other);
//    
//    public abstract boolean greaterThan(final int other);
//    public abstract boolean greaterThan(final float other);
//    
//    public abstract boolean lessThan(final int other);
//    public abstract boolean lessThan(final float other);

    
}
