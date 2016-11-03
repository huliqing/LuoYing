/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.el.LNumberEl;

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
    
    // 等级公式,通过这个id来创建一条等级公式
    private LNumberEl levelEl;
    // 当前等级
    private int level;


    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        levelValue = data.getAsFloat("levelValue", levelValue);
        dynamicValue = data.getAsFloat("dynamicValue", dynamicValue);
        level = data.getAsInteger("level", level);
        levelEl = elService.createLNumberEl(data.getAsString("levelEl"));
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("levelValue", levelValue);
        data.setAttribute("dynamicValue", dynamicValue);
        data.setAttribute("level", level);
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
        levelValue = levelEl.setLevel(level).getValue().floatValue();
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
    public void initialize(AttributeManager module) {
        super.initialize(module);
        // 初始化时只处理等级值，动态值由程序运行时去操作。
        levelValue = levelEl.setLevel(level).getValue().floatValue();
        // 设置值并在可能值变的情况下触发侦听器,当有其它属性绑定了当前属性时，这个值变侦听很重要。
        setValue(levelValue + dynamicValue);
    }
    
}
