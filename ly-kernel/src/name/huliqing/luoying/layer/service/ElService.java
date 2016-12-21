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
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.el.CustomEl;
import name.huliqing.luoying.object.el.STBooleanEl;
import name.huliqing.luoying.object.el.STNumberEl;
import name.huliqing.luoying.object.el.LNumberEl;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.el.SkillHitNumberEl;

/**
 *
 * @author huliqing
 */
public interface ElService extends Inject{
    
    /**
     * 载入一条SBooleanEl，参数可能是elId或是一条SBoolean表达式，如: #{!source.attributeDead}
     * @param idOrExpression
     * @return 
     */
    SBooleanEl createSBooleanEl(String idOrExpression);
    
    /**
     * 载入一条STBooleanEl, 参数可以是elId或是一条STBoolean表达式, 如：#{s.attributeGroup != t.attributeGroup}
     * @param idOrExpression
     * @return 
     */
    STBooleanEl createSTBooleanEl(String idOrExpression);
    
    /**
     * 载入一条STNumberEl, 参数可以是elId或是一条STNumber表达式, 如：#{s.attributeAttack - t.attributeDefense}
     * @param idOrExpression
     * @return 
     */
    STNumberEl createSTNumberEl(String idOrExpression);
    
    /**
     * 载入一条LNumberEl, 参数可以是elId或是一条LNumber表达式，如: #{l * 2}
     * @param idOrExpression
     * @return 
     */
    LNumberEl createLNumberEl(String idOrExpression);
    
    
    /**
     * 自定义的表达式,返回类型根据表达式实际情况而定，类型为Object.
     * 需要设置表达式及根据表达式设置参数值后才可以计算值。
     * @param idOrExpression
     * @return 
     */
    CustomEl createCustomEl(String idOrExpression);
    
    /**
     * 载入一条技能输出公式， 参数可以是一条公式id或者是表达式字段串形式。#{s.attributeXXX + skillValue - t.attributeXXX}
     * @param idOrExpression
     * @return 
     */
    SkillHitNumberEl createSkillHitNumberEl(String idOrExpression);
}
