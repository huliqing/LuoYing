/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.AbstractTerrainToolAction;
import com.jme3.gde.terraineditor.tools.RaiseTerrainToolAction;
import com.jme3.math.Vector3f;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;

/**
 * 地形降低工具
 * @author huliqing
 */
public class LowerTool extends RaiseTool {

    public LowerTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected AbstractTerrainToolAction createAction(float radius, float weight, Vector3f markerWorldLoc, EntityControlTile terrain) {
         RaiseTerrainToolAction action = new RaiseTerrainToolAction(terrain, markerWorldLoc, radius, -weight);
         return action;
    }

//    @Override
//    protected void doRaise() {
//        float radius = radiusTool.getValue().floatValue();
//        float weight = weightTool.getValue().floatValue();
//        if (radius <= 0 || weight == 0) 
//            return;
//        
//        EntityControlTile terrain = getTerrainEntity();
//        if (terrain == null) 
//            return;
//        
//        RaiseTerrainToolAction action = new RaiseTerrainToolAction(terrain, controlObj.getWorldTranslation(), radius, -weight);
//        action.doAction();
//        actions.add(action);
//    }

//    @Override
//    protected Geometry createMesh() {
//        Geometry geo = super.createMesh(); 
//        geo.getMaterial().setColor("Color", ColorRGBA.Red);
//        return geo;
//    }

    
}
