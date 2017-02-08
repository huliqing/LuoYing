/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import name.huliqing.editor.constants.UserDataConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.tools.AbstractTool;

/**
 * 地形工具的基类，没有特别的，只有一些公用方法
 * @author huliqing
 */
public abstract class AbstractTerrainTool extends AbstractTool<SimpleJmeEdit, TerrainToolbar> {

    public AbstractTerrainTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }
    
    /**
     * 获取当前编辑场景中的被选择的地形物体，如果当前未选择任何物体或者这个物体不是Terrain物体，则将返回null.
     * @return 
     */
    protected EntityControlTile getTerrainControl() {
        ControlTile so = edit.getSelected();
        if (!(so instanceof EntityControlTile)) {
            return null;
        }
        EntityControlTile eso = (EntityControlTile) so;
        if (eso.getTarget().getSpatial() instanceof Terrain) {
            return eso;
        } else {
            return null;
        }
    }
    
    protected Terrain getTerrain() {
        EntityControlTile ct = getTerrainControl();
        if (ct != null) {
            return (Terrain) ct.getTarget().getSpatial();
        }
        return null;
    }
    
    /**
     * 标记地形已经执行过修改，需要进行保存。
     * @param modified
     */
    protected void setModified(boolean modified) {
        EntityControlTile terrainControl = getTerrainControl();
        if (terrainControl == null) 
            return;
        terrainControl.getTarget().getSpatial().setUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED, modified);
    }
    
    /**
     * 标记地形已经执行过贴图修改(Alpha)，如贴图笔刷工具的操作。
     * @param modified 
     */
    protected void setModifiedAlpha(boolean modified) {
        EntityControlTile terrainControl = getTerrainControl();
        if (terrainControl == null) 
            return;
        terrainControl.getTarget().getSpatial().setUserData(UserDataConstants.EDIT_TERRAIN_MODIFIED_ALPHA, modified);
    }
}
