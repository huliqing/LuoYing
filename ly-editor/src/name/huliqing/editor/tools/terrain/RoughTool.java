/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.AbstractTerrainToolAction;
import com.jme3.gde.terraineditor.tools.RoughExtraToolParams;
import com.jme3.gde.terraineditor.tools.RoughTerrainToolAction;
import com.jme3.math.Vector3f;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;

/**
 *
 * @author huliqing
 */
public class RoughTool extends AbstractTerrainTool {
    
    public RoughTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected AbstractTerrainToolAction createAction(float radius, float weight, Vector3f markerWorldLoc, EntityControlTile terrain) {
        RoughExtraToolParams params = new RoughExtraToolParams();
        params.lacunarity = 2;
        params.octaves = 6;
        params.scale = 0.1f;
        
        RoughTerrainToolAction action = new RoughTerrainToolAction(terrain, markerWorldLoc, radius, weight, params);
        return action;
    }
   
}
