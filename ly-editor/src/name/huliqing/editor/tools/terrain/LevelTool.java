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

import com.jme3.gde.terraineditor.tools.AbstractTerrainToolAction;
import com.jme3.gde.terraineditor.tools.LevelTerrainToolAction;
import com.jme3.math.Vector3f;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;

/**
 * @author huliqing
 */
public class LevelTool extends AdjustTerrainTool {
    
    private Vector3f desiredHeight; 

    public LevelTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected AbstractTerrainToolAction createAction(float radius, float weight, Vector3f markerWorldLoc, EntityControlTile terrain) {
        boolean absolute = toolbar.getLevelParamTool().getAbsolute().getValue();
        boolean precision = toolbar.getLevelParamTool().getPrecision().getValue();
        float height = toolbar.getLevelParamTool().getHeight().getValue().floatValue();
        
        if (desiredHeight == null) {
            desiredHeight = markerWorldLoc.clone();
        } else {
            desiredHeight.y = markerWorldLoc.y;
        }
        if (absolute) {
            desiredHeight.y = height;
        }
        
        LevelTerrainToolAction action = new LevelTerrainToolAction(terrain, markerWorldLoc, radius, weight
                , desiredHeight, precision);
        setModified(true);
        return action;
    }

}
