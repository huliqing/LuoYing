/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el;

import javax.el.ELContext;

/**
 * 用于计算等级值的表达式, 通过传递一个等级值，这个表达式将返回一个计算后的等级值(Number)。<br>
 * 支持参数：level, 使用示例：<br>
 * #{level * 2} <br>
 * #{level * 2 + 3 * Math:pow(1.06, level)} <br>
 * #{level}<br>
 * 使用示例：
 * <code>
 * <pre>
 * LevelEl el = elService.createLevelEl(xxx);
 * el.setLevel(levelValue);
 * Number result = el.getValue();
 * </pre>
 * </code>
 * 
 * @author huliqing
 */
public class LevelEl extends AbstractEl<Number>{

    private final SimpleElContext elContext = new SimpleElContext();
    
    @Override
    protected ELContext getELContext() {
        return elContext;
    }
    
    /**
     * 设置等级
     * @param level
     * @return 
     */
    public LevelEl setLevel(int level) {
        elContext.setBaseValue("level", level);
        return this;
    }
    
}
