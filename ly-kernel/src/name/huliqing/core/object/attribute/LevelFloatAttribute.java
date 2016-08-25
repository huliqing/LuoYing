/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import name.huliqing.core.Factory;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.mvc.service.ElService;
import name.huliqing.core.object.el.LevelEl;

/**
 * LevelAttribute主要用于拥有等级功能的属性，这种属性会随着等级的提升,属性值也会随着改变。<br>
 * 这种属性包含两个基本值：一个等级值和一个动态值。这两个基本值的“和”构成了当前属性的完整值，<br>
 * 即 fullValue = levelValue + dynamicValue <br>
 * 等级值只能通过设置等级来设置,而动态值可能通过普通的setValue, add, subtract等方式来操作。<br>
 * @author huliqing
 */
public class LevelFloatAttribute extends NumberAttribute<Float, AttributeData> implements LevelAttribute {
    private final ElService elService = Factory.get(ElService.class);

    // 等级值只能通过设置等级来设置
    private float levelValue;
    // 动态值可以进行随时改变
    private float dynamicValue;
    // 完整的value是由levelValue+dynamicValue
    private float fullValue;
    
    // 等级公式id,通过这个id来创建一条等级公式
    private String levelEl;
    // 当前等级
    private int level;
    // 用levelEl创建后的等级公式
    private LevelEl el;

    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        fullValue = data.getAsFloat("value", 0);
        level = data.getAsInteger("level", 1);
        levelEl = data.getAsString("levelEl");
    }

    @Override
    public AttributeData getData() {
        data.setAttribute("value", fullValue);
        data.setAttribute("level", level);
        // 一些不变化的值不需要重设回data, skip: levelEl
        return super.getData(); 
    }
    
    @Override
    public final int intValue() {
        return (int) fullValue;
    }

    @Override
    public final float floatValue() {
        return fullValue;
    }
    
    /**
     * 获取属性的最终值，这个值是由当前属性的等级值和当前的动态值决定的。
     * 最终的返回值为 fullValue = levelValue + dynamicValue, 
     * 注：LevelIntegerAttribute根据不同等级可能会计算出不同的等级值。
     * @return 
     */
    @Override
    public final Float getValue() {
        return fullValue;
    }
    
    /**
     * 设置动态值，注：这个方法会把value设置在动态值上，
     * 最终的返回值 {@link #getValue() } 并不一定等于这个设置的值，
     * {@link #getValue() }的最终返回值会受当前属性的等级值的影响。
     * @param value 
     * @see #getValue() 
     */
    @Override
    public final void setValue(Float value) {
        this.dynamicValue = value;
        setAndNotify(levelValue + dynamicValue);
    }
    
    /**
     * 重新计算属性的最终值,这个方法可以在改变了动态值或等级值之后进行调用，以重新计算当前属性值。
     * @param newFullValue
     */
    protected final void setAndNotify(float newFullValue) {
        float oldValue = this.fullValue;
        this.fullValue = newFullValue;
        if (Float.compare(oldValue, this.fullValue) != 0) {
            notifyValueChangeListeners(oldValue, this.fullValue);
        }
    }
    
    @Override
    public final int getLevel() {
        return level;
    }
    
    /**
     * 设置当前属性的等级，设置属性的等级之后会重新计算当前的属性值。
     * @param level 
     */
    @Override
    public final void setLevel(int level) {
        this.level = level;
        if (el != null) {
            levelValue = (int) el.getValue(level);
        }
        setAndNotify(levelValue + dynamicValue);
    }
    
    /**
     * 添加值到动态值上
     * @param other 
     */
    @Override
    public final void add(int other) {
        dynamicValue += other;
        setAndNotify(levelValue + dynamicValue);
    }

    /**
     * 添加值到动态值上.
     * @param other 
     */
    @Override
    public final void add(float other) {
        dynamicValue += other;
        setAndNotify(levelValue + dynamicValue);
    }

    /**
     * 在动态值上减少值.
     * @param other 
     */
    @Override
    public final void subtract(int other) {
        dynamicValue -= other;
        setAndNotify(levelValue + dynamicValue);
    }
    
    /**
     * 在动态值上减少值.
     * @param other 
     */
    @Override
    public final void subtract(float other) {
        dynamicValue -= other;
        setAndNotify(levelValue + dynamicValue);
    }

    @Override
    public final boolean isEqualTo(int other) {
        return fullValue == other;
    }

    @Override
    public final boolean isEqualTo(float other) {
        return fullValue == other;
    }

    @Override
    public final boolean greaterThan(int other) {
        return fullValue > other;
    }

    @Override
    public final boolean greaterThan(float other) {
        return fullValue > other;
    }

    @Override
    public final boolean lessThan(int other) {
        return fullValue < other;
    }

    @Override
    public final boolean lessThan(float other) {
        return fullValue < other;
    }
    
    @Override
    public final boolean match(Attribute other) {
        if (other instanceof NumberAttribute) {
            return NumberCompare.isEqualTo(fullValue, (NumberAttribute) other);
        }
        return false;
    }

    @Override
    public void initialize(AttributeStore store) {
        super.initialize(store);
        // 初始化时只处理等级值，动态值由程序运行时去操作。
        if (levelEl != null) {
            el = (LevelEl) elService.getEl(levelEl);
        }
        if (el != null) {
            levelValue = (int) el.getValue(level);
        }
        
        // 设置值并在可能值变的情况下触发侦听器,当有其它属性绑定了当前属性时，这个值变侦听很重要。
        setAndNotify(levelValue + dynamicValue);
        
    }
    
}
