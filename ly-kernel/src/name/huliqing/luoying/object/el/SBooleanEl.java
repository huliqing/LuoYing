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
 * SBooleanEl, 这个EL返回一个Boolean值，如果值为true，则说明可以使用一个目标对象,否则不能。
 * 这个EL可以用于判断一个Entity是否可以使用一件装备、一件武器、一个技能或一件物品...等。
 * 比如, 当给一件装备配置如下表达式时：<br>
 * CheckEL="#{!s.attributeDead && s.attributeLevel >= 5}"<br>
 * 这可以表示为：只有活着、并且等级大于或等于5的角色才可以使用目标装备.
 * 
 *  * 代码使用示例：
 * <code><pre>
 * CheckEl el = elService.createCheckEl(...);
 * el.setSource(source.getAttributeManager());
 * if (el.getValue()) {
 *      ...doSomething.
 * }
 * </pre></code>
 * @author huliqing
 */
public class SBooleanEl extends AbstractEl<Boolean>{

    private final AttributeElContext elContext = new AttributeElContext(this);

    @Override
    public ELContext getELContext() {
        return elContext;
    }
    
    /**
     * 设置源对象的属性管理器, 使用时表达式格式如：#{source.attributeXXX}
     * @param source 
     * @return  
     */
    public SBooleanEl setSource(AttributeManager source) {
        elContext.setAttributeManager("s", source);
        return this;
    }
    
}
