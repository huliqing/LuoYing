/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.math.Vector3f;
import com.jme3.terrain.Terrain;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.select.EntitySelectObj;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.tools.EditTool;
import name.huliqing.luoying.manager.PickManager;

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
        EntitySelectObj eso = getTerrainEntity();
        if (eso == null)
            return null;
        
        Vector3f result = PickManager.pick(editor.getCamera(), editor.getInputManager().getCursorPosition(), eso.getTarget().getSpatial());
        return result;
    }
    
    /**
     * 获取当前编辑场景中的被选择的地形物体，如果当前未选择任何物体或者这个物体不是Terrain物体，则将返回null.
     * @return 
     */
    protected final EntitySelectObj getTerrainEntity() {
        ControlTile so = edit.getSelected();
        if (!(so instanceof EntitySelectObj)) {
            return null;
        }
        EntitySelectObj eso = (EntitySelectObj) so;
        if (eso.getTarget().getSpatial() instanceof Terrain) {
            return eso;
        } else {
            return null;
        }
    }
}
