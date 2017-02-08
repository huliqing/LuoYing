/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.AbstractTerrainToolAction;
import com.jme3.gde.terraineditor.tools.LevelExtraToolParams;
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
