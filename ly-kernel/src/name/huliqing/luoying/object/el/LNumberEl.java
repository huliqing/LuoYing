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
import javax.el.ValueExpression;

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
