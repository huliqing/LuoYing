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

import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ParamsTool;

/**
 * @author huliqing
 */
public class RoughParamsTool extends ParamsTool {
    private final NumberValueTool lacunarity;
    private final NumberValueTool octaves;
    private final NumberValueTool scale;
    
    public RoughParamsTool(String name, String tips, String icon) {
        super(name, tips, icon);
        lacunarity = new NumberValueTool("lacunarity", null, null);
        octaves = new NumberValueTool("octaves", null, null);
        scale = new NumberValueTool("scale", null, null);
        addChild(lacunarity);
        addChild(octaves);
        addChild(scale);

        lacunarity.setValue(2);
        octaves.setValue(6.0f);
        scale.setValue(0.1f);
        
//            params.lacunarity = 2;
//        params.octaves = 6;
//        params.scale = 0.1f;
    }

    public NumberValueTool getLacunarity() {
        return lacunarity;
    }

    public NumberValueTool getOctaves() {
        return octaves;
    }

    public NumberValueTool getScale() {
        return scale;
    }

    
}
