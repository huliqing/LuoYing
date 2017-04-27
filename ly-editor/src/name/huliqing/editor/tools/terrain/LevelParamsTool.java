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
package name.huliqing.editor.tools.terrain;

import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.FloatValueTool;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ParamsTool;

/**
 * @author huliqing
 */
public class LevelParamsTool extends ParamsTool {
    
    private final FloatValueTool height;
    private final BooleanValueTool absolute;
    private final BooleanValueTool precision;
    
    public LevelParamsTool(String name, String tips, String icon) {
        super(name, tips, icon);
        height = new FloatValueTool("height", null, null);
        absolute = new BooleanValueTool("absolute", null, null);
        precision = new BooleanValueTool("precision", null, null);
        height.setValue(0);
        absolute.setValue(false);
        precision.setValue(false);
        addChild(height);
        addChild(absolute);
        addChild(precision);
    }

    public NumberValueTool getHeight() {
        return height;
    }

    public BooleanValueTool getAbsolute() {
        return absolute;
    }

    public BooleanValueTool getPrecision() {
        return precision;
    }

}
