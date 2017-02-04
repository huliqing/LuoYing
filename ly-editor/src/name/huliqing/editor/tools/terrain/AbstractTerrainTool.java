/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.AbstractTerrainToolAction;
import com.jme3.gde.terraineditor.tools.PaintTerrainToolAction;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.terrain.Terrain;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.tools.EditTool;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ToggleTool;
import name.huliqing.luoying.manager.PickManager;

/**
 * 地形工具的基类
 * @author huliqing
 */
public abstract class AbstractTerrainTool extends EditTool<SimpleJmeEdit, TerrainToolbar> implements ToggleTool {
//    private static final Logger LOG = Logger.getLogger(AbstractTerrainTool.class.getName());

    private boolean modifying;
    private float lastRadiusUsed = 1.0f;
    // how frequently (in seconds) it should update to throttle down the tool effect
    private final float toolModifyRate = 0.05f; 
    // last time the tool executed
    private float lastModifyTime; 
    
    private Geometry controlObj;
    private NumberValueTool radiusTool;
    private NumberValueTool weightTool;
    
    private final static String EVENT_ACTION = "actionEvent";
    private final List<AbstractTerrainToolAction> actions = new ArrayList();
    
    public AbstractTerrainTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public void initialize(SimpleJmeEdit edit, TerrainToolbar toolbar) {
        super.initialize(edit, toolbar);
        if (controlObj == null) {
            controlObj = createMesh();
        }
        edit.getEditRoot().attachChild(controlObj);
        
        radiusTool = toolbar.getRadiusTool();
        weightTool = toolbar.getWeightTool();
    }

    @Override
    public void cleanup() {
        if (modifying) {
            doEndAction();
        }
        if (controlObj != null) {
            controlObj.removeFromParent();
        }
        super.cleanup(); 
    }
    
    public JmeEvent bindActionEvent() {
        return this.bindEvent(EVENT_ACTION);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e.getName().equals(EVENT_ACTION)) {
            if (e.isMatch()) {
                modifying = true;
                lastModifyTime = 0;
                doAction();
                e.setConsumed(true);
            } else {
                modifying = false;
                doEndAction();
            }
        }
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        Vector3f pos = getTerrainCollisionPoint();
        if (pos != null && controlObj != null) {
            controlObj.setLocalTranslation(pos);
        }
        if (Float.compare(radiusTool.getValue().floatValue(), lastRadiusUsed) != 0) {
            lastRadiusUsed = radiusTool.getValue().floatValue();
            controlObj.setLocalScale(lastRadiusUsed);
        }
        
        if (modifying) {
            lastModifyTime += tpf;
            if (lastModifyTime >= toolModifyRate) {
                lastModifyTime = 0;
                doAction();
            }
        }
    }
    
    protected void doAction() {
        float radius = radiusTool.getValue().floatValue();
        float weight = weightTool.getValue().floatValue();
        if (radius <= 0 || weight == 0) {
            return;
        }
        EntityControlTile terrain = getTerrainEntity();
        if (terrain == null) 
            return;
        
        AbstractTerrainToolAction action = createAction(radius, weight, controlObj.getWorldTranslation(), terrain);
        if (action != null) {
            action.doAction();
            actions.add(action);
        }
    }
    
    protected void doEndAction() {
        if (actions.isEmpty()) {
            return;
        }
        // record undo action
        List<AbstractTerrainToolAction> actionList = new ArrayList(actions);
        edit.addUndoRedo(new ToolActionUndoRedo(actionList));
        actions.clear();
    }
    
    protected abstract AbstractTerrainToolAction createAction(float radius, float weight, Vector3f markerWorldLoc
            , EntityControlTile terrain);
    
    private Geometry createMesh() {
        Geometry marker = new Geometry();
        marker.setMesh(new Sphere(8, 8, 1));
        Material mat = new Material(editor.getAssetManager(), AssetConstants.MATERIAL_UNSHADED);
        mat.getAdditionalRenderState().setWireframe(true);
        marker.setMaterial(mat);
        marker.setLocalTranslation(0,0,0);
        mat.setColor("Color", ColorRGBA.Green);
        return marker;
    }
    
    /**
     * 获取鼠标在地形上的点击点，如果不存在这个点则返回null.
     * @return 
     */
    private Vector3f getTerrainCollisionPoint() {
        EntityControlTile eso = getTerrainEntity();
        if (eso == null)
            return null;
        
        Vector3f result = PickManager.pick(editor.getCamera(), editor.getInputManager().getCursorPosition(), eso.getTarget().getSpatial());
        return result;
    }
    
    /**
     * 获取当前编辑场景中的被选择的地形物体，如果当前未选择任何物体或者这个物体不是Terrain物体，则将返回null.
     * @return 
     */
    private EntityControlTile getTerrainEntity() {
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
    
    private class ToolActionUndoRedo implements UndoRedo {
        
        private final List<AbstractTerrainToolAction> actionList;
        
        public ToolActionUndoRedo(List<AbstractTerrainToolAction> actionList) {
            this.actionList = actionList;
        }
        
        @Override
        public void undo() {
            for (int i = actionList.size() - 1; i >= 0; i--) {
                AbstractTerrainToolAction action = actionList.get(i);
                action.undo();
            }
        }

        @Override
        public void redo() {
            for (int i = 0; i < actionList.size(); i++) {
                AbstractTerrainToolAction action = actionList.get(i);
                action.redo();
            }
        }
        
    }
}
