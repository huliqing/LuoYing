/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.el.LNumberEl;

/**
 * LevelIntegerAttribute，int类型，并带有等级的属性，这种属性的值由等级值(levelValue)和动态值(dynamicVaue)组成。
 * 即: value = levelValue  + dynamic <br>
 * 等级值只受等级的影响，不受属性值的影响，当设置属性值时只影响动态值, 只有设置等级时才影响等级值。
 * @author huliqing
 * @see LevelFloatAttribute
 */
public class LevelIntegerAttribute extends IntegerAttribute implements LevelAttribute {
    private final ElService elService = Factory.get(ElService.class);
    
    // 完整的value是由levelValue+dynamicValue

    // 等级值,这个值只会随着等级值而变化，设置属性的值不会影响这个值,这个值只有设置等级时才会发生变化。
    private int levelValue;
    // 动态值,当设置属性的值时会影响这个值，但不影响等级值。
    private int dynamicValue; 
    
    // 等级值和等级公式，等级值和等级公式将计算出一个等级值: levelValue
    private int level;
    private LNumberEl levelEl;

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        levelValue = data.getAsInteger("levelValue", levelValue);
        dynamicValue = data.getAsInteger("dynamicValue", dynamicValue);
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
        levelValue =levelEl.setLevel(level).getValue().intValue();
        setValue(levelValue + dynamicValue);
    }
    
    @Override
    protected boolean doSetSimpleValue(Number newValue) {
        if (super.doSetSimpleValue(newValue)) {
            // 注: value是由levelValue + dynamicValue相加而成的，为保持一致,这里要计算出dynamicValue，
            // 以便在存档及恢复的时候能保持一致。
            dynamicValue = value.intValue() - levelValue;
            return true;
        }
        return false;
    }

    @Override
    public void initialize(AttributeManager module) {
        super.initialize(module);
        // 初始化时只处理等级值，动态值由程序运行时去操作。
        levelValue = levelEl.setLevel(level).getValue().intValue();
        // 设置值并在可能值变的情况下触发侦听器,当有其它属性绑定了当前属性时，这个值变侦听很重要。
        setValue(levelValue + dynamicValue);
    }
    
}
