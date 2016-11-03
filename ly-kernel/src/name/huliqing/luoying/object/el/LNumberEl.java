/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el;

import javax.el.ELContext;

/**
 * 等级值计算公式, 给定一个等级然后计算出一个等级值<br>
 * 支持参数：l, 使用示例：<br>
 * #{l * 2} <br>
 * #{l * 2 + 3 * Math:pow(1.06, l)} <br>
 * #{l}<br>
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
public class LNumberEl extends AbstractEl<Number>{

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
    public LNumberEl setLevel(int level) {
        elContext.setBaseValue("l", level);
        return this;
    }
    
}
