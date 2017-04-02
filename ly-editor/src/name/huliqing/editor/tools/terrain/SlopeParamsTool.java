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
import name.huliqing.editor.tools.ParamsTool;

/**
 * @author huliqing
 */
public class SlopeParamsTool extends ParamsTool {
    
//                SlopeExtraToolParams params = new SlopeExtraToolParams();
//            params.precision = false; // Snap on terrain editor
//            params.lock = false; // Contain on terrain editor
    
    private final BooleanValueTool precision;
    private final BooleanValueTool lock;
    
    public SlopeParamsTool(String name, String tips, String icon) {
        super(name, tips, icon);
        precision = new BooleanValueTool("precision", null, null);
        lock = new BooleanValueTool("lock", null, null);
        addChild(precision);
        addChild(lock);

        precision.setValue(false);
        lock.setValue(false);
    }

    public BooleanValueTool getPrecision() {
        return precision;
    }

    public BooleanValueTool getLock() {
        return lock;
    }

}
