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
package name.huliqing.editor.tiles;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class Grid extends Node {
    
    public Grid() {
        Geometry grid = new Geometry("grid", new com.jme3.scene.debug.Grid(17, 17, 1.0f));
        grid.setMaterial(MaterialUtils.createUnshaded(new ColorRGBA(0.3f, 0.3f, 0.3f, 1.0f)));
        grid.setLocalTranslation(-8f, 0, -8f);
        attachChild(grid);
        
        Geometry xBox = new Geometry("xBox", new Box(0.5f, 0.5f, 0.5f));
        xBox.setMaterial(MaterialUtils.createUnshaded(new ColorRGBA(2f, 0f, 0.1f, 1f)));
        xBox.setLocalScale(16, 0.005f, 0.005f);
        attachChild(xBox);

        Geometry zBox = new Geometry("zBox", new Box(0.5f, 0.5f, 0.5f));
        zBox.setMaterial(MaterialUtils.createUnshaded(new ColorRGBA(0.1f, 0f, 1f, 1f)));
        zBox.setLocalScale(0.005f, 0.005f, 16);
        attachChild(zBox);
    }
}
