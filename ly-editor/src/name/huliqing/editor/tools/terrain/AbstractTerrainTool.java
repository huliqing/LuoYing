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
        edit.setModified(true);
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
        edit.setModified(true);
    }
}
