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

/**
 * 自定义的表达式,返回类型根据表达式实际情况而定，类型为Object.
 * 需要设置表达式及根据表达式设置参数值后才可以计算值。
 * @author huliqing
 * @param <T>
 */
public class CustomEl<T> extends AbstractEl<T> {

    private final SimpleElContext elContext = new SimpleElContext();
    
    @Override
    protected ELContext getELContext() {
        return elContext;
    }
    
    /**
     * 设置基本参数.
     * @param prop 参数名称
     * @param value 参数值
     */
    public void setProperty(String prop, Object value) {
        elContext.setBaseValue(prop, value);
    }
}
