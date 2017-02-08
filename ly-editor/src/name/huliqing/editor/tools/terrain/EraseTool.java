/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.AbstractTerrainToolAction;
import com.jme3.gde.terraineditor.tools.PaintTerrainToolAction;
import com.jme3.math.Vector3f;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.utils.TerrainUtils;

/**
 *
 * @author huliqing
 */
public class EraseTool extends AdjustTerrainTool {

    protected TexLayerTool texLayerTool;
    
    public EraseTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public void initialize(SimpleJmeEdit edit, TerrainToolbar toolbar) {
        super.initialize(edit, toolbar); 
        texLayerTool = toolbar.getTexLayerTool();
    }
 
    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    protected AbstractTerrainToolAction createAction(float radius, float weight, Vector3f markerWorldLoc, EntityControlTile terrain) {
        int textureIndex = texLayerTool.getSelectLayerIndex();
        if (textureIndex < 0 || textureIndex >= TerrainUtils.MAX_TEXTURES) 
            return null;
        
        PaintTerrainToolAction action = new PaintTerrainToolAction(terrain, markerWorldLoc, radius, -weight, textureIndex);
        setModifiedAlpha(true);
        return action;
    }

}
