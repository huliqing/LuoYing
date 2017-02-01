/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.RaiseTerrainToolAction;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import name.huliqing.editor.edit.select.EntityControlTile;

/**
 * 地形降低工具
 * @author huliqing
 */
public class LowerTool extends RaiseTool {

    public LowerTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void doRaise() {
        float radius = radiusTool.getValue().floatValue();
        float weight = weightTool.getValue().floatValue();
        if (radius <= 0 || weight == 0) 
            return;
        
        EntityControlTile terrain = getTerrainEntity();
        if (terrain == null) 
            return;
        
        RaiseTerrainToolAction action = new RaiseTerrainToolAction(terrain, controlObj.getWorldTranslation(), radius, -weight);
        action.doRaise();
        actions.add(action);
    }

    @Override
    protected Geometry createMesh() {
        Geometry geo = super.createMesh(); 
        geo.getMaterial().setColor("Color", ColorRGBA.Red);
        return geo;
    }

    
}
