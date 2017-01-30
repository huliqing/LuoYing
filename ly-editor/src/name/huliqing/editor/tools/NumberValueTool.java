/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    public NumberValueTool(String name, String tips, String icon) {
        super(name, tips, icon);
        setValue(0.0);
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
     * @param stepAmount 
     */
    public void setStepAmount(Number stepAmount) {
        this.stepAmount = stepAmount;
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

}
