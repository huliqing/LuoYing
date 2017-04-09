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
package name.huliqing.luoying.object.el;

import java.util.logging.Logger;
import javax.el.ELContext;
import name.huliqing.luoying.object.attribute.AttributeManager;

/**
 * STNumberEl,这个EL会计算并返回一个Number值，这个Number值定义了一个对象source对另一个对象target产生的作用值。
 * 可以用来表示计算技能的伤害值、BUFF增益值、
 * <br>表达式参数：
 * 1.s: 这个参数表示源对象。<br>
 * 2.t: 这个参数表示目标对象。<br>
 * 表达式示例：#{s.attributeAttack - t.attributeDefence}, 这可以用来表示s角色对t角色的技能伤害
 * 值。
 * @author huliqing
 */
public class STNumberEl extends AbstractEl<Number> {

    private static final Logger LOG = Logger.getLogger(STNumberEl.class.getName());

    private final AttributeElContext elContext = new AttributeElContext(this);

    @Override
    public ELContext getELContext() {
        return elContext;
    }
    
    /**
     * 设置源对象
     * @param source 
     * @return  
     */
    public STNumberEl setSource(AttributeManager source) {
        elContext.setAttributeManager("s", source);
        return this;
    }
    
    /**
     * 设置目标对象
     * @param target 
     * @return  
     */
    public STNumberEl setTarget(AttributeManager target) {
        elContext.setAttributeManager("t", target);
        return this;
    }

    
}
