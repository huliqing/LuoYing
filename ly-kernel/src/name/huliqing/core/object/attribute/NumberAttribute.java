/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import name.huliqing.core.data.AttributeData;

/**
 * Number类型的属性。
 * @author huliqing
 * @param <V>
 * @param <T>
 */
public abstract class NumberAttribute<V extends Number, T extends AttributeData> extends AbstractAttribute<V, T> {

    @Override
    public abstract V getValue();

    @Override
    public abstract void setValue(V value);
    
    public abstract int intValue();
    public abstract float floatValue();
    
    public abstract void add(int other);
    public abstract void add(float other);
    public abstract void subtract(int other);
    public abstract void subtract(float other);
    
    public abstract boolean isEqualTo(int other);
    public abstract boolean isEqualTo(float other);
    
    public abstract boolean greaterThan(final int other);
    public abstract boolean greaterThan(final float other);
    
    public abstract boolean lessThan(final int other);
    public abstract boolean lessThan(final float other);
}
