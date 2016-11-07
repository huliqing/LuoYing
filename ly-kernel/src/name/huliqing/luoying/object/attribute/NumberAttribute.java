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
public abstract class NumberAttribute extends SimpleAttribute<Number> {

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
    
    public abstract void add(int other);
    public abstract void add(float other);
    


    
}
