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
 * 刷图工具,注意：地形在载入的时候需要重新设置材质，使用地形中的所有分块指定同一个材质实例，否则指定刷到特定的材质上。
 * 参考以下代码：
 * <code>
 * <pre>
 * if (target.getSpatial() instanceof Terrain) {
 *      Terrain terrain = (Terrain) target.getSpatial();
 *      target.getSpatial().setMaterial(terrain.getMaterial());
 * }
 * </pre>
 * </code>
 * @author huliqing
 */
public class PaintTool extends AbstractTerrainTool {
    
    protected TexLayerTool texLayerTool;
    
    public PaintTool(String name, String tips, String icon) {
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
        
        PaintTerrainToolAction action = new PaintTerrainToolAction(terrain, markerWorldLoc, radius, weight, textureIndex);
        return action;
    } 

}
