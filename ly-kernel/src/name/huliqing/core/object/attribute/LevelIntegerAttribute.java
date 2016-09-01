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
import name.huliqing.core.object.module.AttributeModule;

/**
 * LevelAttribute主要用于拥有等级功能的属性，这种属性会随着等级的提升,属性值也会随着改变。<br>
 * 这种属性包含两个基本值：一个等级值和一个动态值。这两个基本值的“和”构成了当前属性的完整值，<br>
 * 即 fullValue = levelValue + dynamicValue <br>
 * 等级值只能通过设置等级来设置,而动态值可能通过普通的setValue, add, subtract等方式来操作。<br>
 * @author huliqing
 */
public class LevelIntegerAttribute extends NumberAttribute<Integer, AttributeData> implements LevelAttribute {
    private final ElService elService = Factory.get(ElService.class);
    
    // 完整的value是由levelValue+dynamicValue
    private int value;

    // 等级值只能通过设置等级来设置
    private int levelValue;
    // 动态值可以进行随时改变
    private int dynamicValue;
    
    // 等级公式id,通过这个id来创建一条等级公式
    private String levelEl;
    // 当前等级
    private int level;
    // 用levelEl创建后的等级公式
    private LevelEl el;

    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        value = data.getAsInteger("value", 0);
        levelValue = data.getAsInteger("levelValue", levelValue);
        dynamicValue = data.getAsInteger("dynamicValue", dynamicValue);
        level = data.getAsInteger("level", 1);
        levelEl = data.getAsString("levelEl");
    }
    
    // 更新data值，以避免在外部使用data时获取不到实时的数据
    protected void updateData() {
        data.setAttribute("value", value);
        data.setAttribute("levelValue", levelValue);
        data.setAttribute("dynamicValue", dynamicValue);
        data.setAttribute("level", level);
//        data.setAttribute("levelEl", levelEl); // levelEl不会改变，所以不需要重新设置回去。
    }
    
    @Override
    public final int intValue() {
        return value;
    }

    @Override
    public final float floatValue() {
        return value;
    }
    
    @Override
    public final long longValue() {
        return value;
    }

    @Override
    public final double doubleValue() {
        return value;
    }
    
    /**
     * 获取属性的最终值，这个值是由当前属性的等级值和当前的动态值决定的。
     * 最终的返回值为 fullValue = levelValue + dynamicValue, 
     * 注：LevelIntegerAttribute根据不同等级可能会计算出不同的等级值。
     * @return 
     */
    @Override
    public final Integer getValue() {
        return value;
    }
    
    /**
     * 设置动态值，注：这个方法会把value设置在动态值上，
     * 最终的返回值 {@link #getValue() } 并不一定等于这个设置的值，
     * {@link #getValue() }的最终返回值会受当前属性的等级值的影响。
     * @param value 
     * @see #getValue() 
     */
    @Override
    public final void setValue(Number value) {
        this.dynamicValue = value.intValue();
        setAndNotify(levelValue + dynamicValue);
    }
    
    /**
     * 重新计算属性的最终值,这个方法可以在改变了动态值或等级值之后进行调用，以重新计算当前属性值。
     * @param newFullValue
     */
    protected final void setAndNotify(int newFullValue) {
        int oldValue = this.value;
        this.value = newFullValue;
        updateData();
        if (oldValue != this.value) {
            notifyValueChangeListeners(oldValue, this.value);
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
        return value == other;
    }

    @Override
    public final boolean isEqualTo(float other) {
        return value == other;
    }

    @Override
    public final boolean greaterThan(int other) {
        return value > other;
    }

    @Override
    public final boolean greaterThan(float other) {
        return value > other;
    }

    @Override
    public final boolean lessThan(int other) {
        return value < other;
    }

    @Override
    public final boolean lessThan(float other) {
        return value < other;
    }
   
    @Override
    public final boolean match(final Object other) {
        if (other instanceof Number) {
            return this.doubleValue() == ((Number) other).doubleValue();
        }
        if (other instanceof String) {
            return this.intValue() == Integer.parseInt((String) other);
        }
        return super.match(other);
    }

    @Override
    public void initialize(AttributeModule module) {
        super.initialize(module);
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
