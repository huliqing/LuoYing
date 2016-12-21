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

import javax.el.ELContext;
import name.huliqing.luoying.object.attribute.AttributeManager;

/**
 * STBooleanEl，这个EL的结果为一个Boolean类型的值，通过这个值来判断source是否可以作用于target
 * ,一般用于判断两个拥有属性功能（如Entity)的对象是否可以互相作用,当求值时需要设置两个基本值source, target<br>
 * 使用示例，比如当用于判断一个角色A的技能是否可以作用于另一个角色B时, <br>
 * 在xml中的表达式可以像这样：#{s.attributeGroup != t.attributeGroup}<br>
 * 这表示当角色A(source)与目标角色B(target)的派系（group属性值）不一样时，角色A的技能可以作用于角色B。<br>
 * 
 * 代码使用示例：
 * <code><pre>
 * HitCheckEl el = elService.createHitCheckEl(...);
 * el.setSource(source.getAttributeManager());
 * el.setTarget(target.getAttributeManager());
 * if (el.getValue()) {
 *      ...doSomething.
 * }
 * </pre></code>
 * @author huliqing
 */
public class STBooleanEl extends AbstractEl<Boolean> {

    private final AttributeElContext elContext = new AttributeElContext(this);
    
    @Override
    public ELContext getELContext() {
        return elContext;
    }
    
    /**
     * 设置源属性管理器，比如某个Entity的属性管理器。
     * @param source 
     * @return  
     */
    public STBooleanEl setSource(AttributeManager source) {
        elContext.setAttributeManager("s", source);
        return this;
    }
    
    /**
     * 设置目标属性管理器，比如某个Entity的属性管理器
     * @param target 
     * @return  
     */
    public STBooleanEl setTarget(AttributeManager target) {
        elContext.setAttributeManager("t", target);
        return this;
    }
    
}
