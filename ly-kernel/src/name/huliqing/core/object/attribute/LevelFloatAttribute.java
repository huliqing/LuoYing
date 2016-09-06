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
public class LevelFloatAttribute extends FloatAttribute implements LevelAttribute {
    private final ElService elService = Factory.get(ElService.class);
    
    // 完整的value是由levelValue+dynamicValue

    // 等级值只能通过设置等级来设置
    private float levelValue;
    // 动态值可以进行随时改变
    private float dynamicValue;
    
    // 等级公式id,通过这个id来创建一条等级公式
    private String levelEl;
    // 当前等级
    private int level;
    // 用levelEl创建后的等级公式
    private LevelEl el;

    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        levelValue = data.getAsFloat("levelValue", levelValue);
        dynamicValue = data.getAsFloat("dynamicValue", dynamicValue);
        level = data.getAsInteger("level", level);
        levelEl = data.getAsString("levelEl", levelEl);
    }
    
    // 更新data值，以避免在外部使用data时获取不到实时的数据
    @Override
    protected void updateData() {
        super.updateData();
        data.setAttribute("levelValue", levelValue);
        data.setAttribute("dynamicValue", dynamicValue);
        data.setAttribute("level", level);
//        data.setAttribute("levelEl", levelEl); // levelEl不会改变，所以不需要重新设置回去。
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
        setValue(levelValue + dynamicValue);
    }
    
    /**
     * 添加值到动态值上
     * @param other 
     */
    @Override
    public void add(int other) {
        dynamicValue += other;
        setValue(levelValue + dynamicValue);
    }

    /**
     * 添加值到动态值上.
     * @param other 
     */
    @Override
    public final void add(float other) {
        dynamicValue += other;
        setValue(levelValue + dynamicValue);
    }

    @Override
    public void initialize(AttributeModule module) {
        super.initialize(module);
        // 初始化时只处理等级值，动态值由程序运行时去操作。
        if (levelEl != null) {
            el = (LevelEl) elService.getEl(levelEl);
        }
        if (el != null) {
            levelValue = el.getValue(level);
        }
        
        // 设置值并在可能值变的情况下触发侦听器,当有其它属性绑定了当前属性时，这个值变侦听很重要。
        setValue(levelValue + dynamicValue);
    }
    
}
