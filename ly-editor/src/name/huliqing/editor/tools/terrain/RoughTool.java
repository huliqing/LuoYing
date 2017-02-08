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
public class RoughTool extends AdjustTerrainTool {
    
    public RoughTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }
    
    @Override
    protected AbstractTerrainToolAction createAction(float radius, float weight, Vector3f markerWorldLoc, EntityControlTile terrain) {
        RoughExtraToolParams params = new RoughExtraToolParams();
        params.lacunarity = toolbar.getRoughParamsTool().getLacunarity().getValue().floatValue();
        params.octaves = toolbar.getRoughParamsTool().getOctaves().getValue().floatValue();;
        params.scale = toolbar.getRoughParamsTool().getScale().getValue().floatValue();
        
        RoughTerrainToolAction action = new RoughTerrainToolAction(terrain, markerWorldLoc, radius, weight, params);
        setModified(true);
        return action;
    }
    
}
