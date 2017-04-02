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
package name.huliqing.editor.tools;

import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;

/**
 * 数值类型的工具
 * @author huliqing
 */
public class NumberValueTool extends AbstractValueTool<Number> {

    private final static String EVENT_INCREASE = "increaseEvent";
    private final static String EVENT_DECREASE = "decreaseEvent";
    
    private Number stepAmount = 1;
    private Number minValue;
    private Number maxValue;

    public NumberValueTool(String name, String tips, String icon) {
        super(name, tips, icon);
        setValue(0.0);
    }

    @Override
    public <T extends ValueTool> T setValue(Number newValue) {
        if (minValue != null && newValue.doubleValue() < minValue.doubleValue()) {
            newValue = minValue.doubleValue();
        }
        if (maxValue != null && newValue.doubleValue() > maxValue.doubleValue()) {
            newValue = maxValue.doubleValue();
        }
        return super.setValue(newValue);
    }

    public Number getMinValue() {
        return minValue;
    }
    
    public <T extends NumberValueTool> T setMinValue(Number minValue) {
        this.minValue = minValue;
        return (T) this;
    }

    public Number getMaxValue() {
        return maxValue;
    }

    public <T extends NumberValueTool> T setMaxValue(Number maxValue) {
        this.maxValue = maxValue;
        return (T) this;
    }
    
    /**
     * 绑定一个按键，这个快捷键用于提高数值
     * @return 
     */
    public JmeEvent bindIncreaseEvent() {
        return bindEvent(EVENT_INCREASE);
    }
    
    /**
     * 绑定一个按键，这个快捷键用于降低数值
     * @return 
     */
    public JmeEvent bindDecreaseEvent() {
        return bindEvent(EVENT_DECREASE);
    }

    public Number getStepAmount() {
        return stepAmount;
    }

    /**
     * 设置步增或步减的量
     * @param <T>
     * @param stepAmount 
     * @return  
     */
    public <T extends NumberValueTool> T setStepAmount(Number stepAmount) {
        this.stepAmount = stepAmount;
        return (T) this;
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e.isMatch()) {
            if (e.getName().equals(EVENT_INCREASE)) {
                setValue(getValue().doubleValue() + stepAmount.doubleValue());
            } else if (e.getName().equals(EVENT_DECREASE)) {
                setValue(getValue().doubleValue() - stepAmount.doubleValue());
            }
        }
    }

    @Override
    public void update(float tpf) {
        // ignore
    }

}
