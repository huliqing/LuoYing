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
 * 技能输出值计算公式, 这个公式用于为技能计算一个最终的输出值。例如，当角色A使用技能B攻击角色C时，
 * 这个公式将为这样的攻击输出计算出一个输出值，用于作用到受攻击者身上(目标C).
 * 表达式参数：<br>
 * 1.s: 这个参数表示源对象,即攻击者。<br>
 * 2.t: 这个参数表示目标对象,即受攻击者。<br>
 * 3.skillHitValue: 技能的自身输出值，这是一个数值，来自于技能自身。<br>
 * @author huliqing
 */
public class SkillHitNumberEl extends AbstractEl<Number>{
    
    private final AttributeElContext elContext = new AttributeElContext(this);

    @Override
    protected ELContext getELContext() {
        return elContext;
    }
    
     /**
     * 设置源对象
     * @param source 
     * @return  
     */
    public SkillHitNumberEl setSource(AttributeManager source) {
        elContext.setAttributeManager("s", source);
        return this;
    }
    
    /**
     * 设置目标对象
     * @param target 
     * @return  
     */
    public SkillHitNumberEl setTarget(AttributeManager target) {
        elContext.setAttributeManager("t", target);
        return this;
    }
    
    /**
     * 设置技能自身的输出值。
     * @param skillValue
     * @return 
     */
    public SkillHitNumberEl setSkillValue(Number skillValue) {
        elContext.setBaseValue("skillHitValue", skillValue);
        return this;
    }

    /**
     * 计算出技能最终的输出值。
     * @return 
     */
    @Override
    public Number getValue() {
        return super.getValue(); 
    }
    
}
