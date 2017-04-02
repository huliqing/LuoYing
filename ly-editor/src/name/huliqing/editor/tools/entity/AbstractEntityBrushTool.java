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
package name.huliqing.editor.tools.entity;

import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.toolbar.EntityBrushToolbar;
import name.huliqing.editor.tools.AbstractTool;

/**
 * 场景实体刷的基本类,主要用于包装一些共用方法。
 * @author huliqing
 */
public abstract class AbstractEntityBrushTool extends AbstractTool<SimpleJmeEdit, EntityBrushToolbar> {

    public AbstractEntityBrushTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

}
