/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.tools.EditTool;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.object.entity.TerrainEntity;

/**
 * 地形工具的基类
 * @author huliqing
 */
public abstract class AbstractTerrainTool extends EditTool<SimpleJmeEdit, TerrainToolbar> {
//    private static final Logger LOG = Logger.getLogger(AbstractTerrainTool.class.getName());

    public AbstractTerrainTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }
    
    /**
     * 获取鼠标在地形上的点击点，如果不存在这个点则返回null.
     * @return 
     */
    protected Vector3f getTerrainCollisionPoint() {
        Spatial terrain = (Spatial) getTerrain();
        if (terrain == null)
            return null;
        
        Vector3f result = PickManager.pick(editor.getCamera(), editor.getInputManager().getCursorPosition(), terrain);
        return result;
    }
    
    protected final Terrain getTerrain() {
        SelectObj so = edit.getSelected();
        if (so == null || !(so.getObject() instanceof TerrainEntity)) {
            return null;
        }
        TerrainEntity te = (TerrainEntity) so.getObject();
        Spatial spatial = te.getSpatial();
        if (!(spatial instanceof Terrain)) {
            return null;
        }
        return (Terrain) spatial;
    }
}
