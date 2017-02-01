/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.LevelExtraToolParams;
import com.jme3.gde.terraineditor.tools.LevelTerrainToolAction;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.select.EntityControlTile;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ToggleTool;

/**
 * @author huliqing
 */
public class LevelTool extends AbstractTerrainTool implements ToggleTool {
    
    private final static String EVENT_LEVEL = "levelEvent";
    
    protected Geometry controlObj;
    protected NumberValueTool radiusTool;
    protected NumberValueTool weightTool;
    
    protected final List<LevelTerrainToolAction> actions = new ArrayList<LevelTerrainToolAction>();
    
    private boolean modifying;
    private float lastRadiusUsed = 1.0f;
    private final float toolModifyRate = 0.05f; // how frequently (in seconds) it should update to throttle down the tool effect
    private float lastModifyTime; // last time the tool executed
    
    private Vector3f desiredHeight;

    public LevelTool(String name, String tips, String icon) {
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
            endLevel();
        }
        if (controlObj != null) {
            controlObj.removeFromParent();
        }
        super.cleanup(); 
    }
    
    public JmeEvent bindLevelEvent() {
        return this.bindEvent(EVENT_LEVEL);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e.getName().equals(EVENT_LEVEL)) {
            if (e.isMatch()) {
                modifying = true;
                lastModifyTime = 0;
                doLevel();
                e.setConsumed(true);
            } else {
                modifying = false;
                endLevel();
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
                doLevel();
            }
        }
    }
    
    private void doLevel() {
        float radius = radiusTool.getValue().floatValue();
        float weight = weightTool.getValue().floatValue();
        if (radius <= 0 || weight == 0) 
            return;
        
        EntityControlTile terrain = getTerrainEntity();
        if (terrain == null) 
            return;
        
        LevelExtraToolParams params = new LevelExtraToolParams();
        params.absolute = false;
        params.precision = false;
        params.height = 0;
        
        if (desiredHeight == null)
            desiredHeight = controlObj.getWorldTranslation().clone();
        else
            desiredHeight.y = controlObj.getWorldTranslation().y;
        if (params.absolute)
            desiredHeight.y = params.height;
        
        LevelTerrainToolAction action = new LevelTerrainToolAction(terrain, controlObj.getWorldTranslation()
                , radius, weight, desiredHeight, params.precision);
        action.doAction();
        actions.add(action);
    }
    
    private void endLevel() {
        if (actions.isEmpty()) {
            return;
        }
        List<LevelTerrainToolAction> actionList = new ArrayList<LevelTerrainToolAction>(actions);
        edit.addUndoRedo(new LevelUndoRedo(actionList));
        actions.clear();
    }
    
    protected Geometry createMesh() {
        Geometry marker = new Geometry("edit marker primary");
        marker.setMesh(new Sphere(8, 8, 1));
        Material mat = new Material(editor.getAssetManager(), AssetConstants.MATERIAL_UNSHADED);
        mat.getAdditionalRenderState().setWireframe(true);
        marker.setMaterial(mat);
        marker.setLocalTranslation(0,0,0);
        mat.setColor("Color", ColorRGBA.Green);
        return marker;
    }

    private class LevelUndoRedo implements UndoRedo {
        
        private final List<LevelTerrainToolAction> actionList;
        
        public LevelUndoRedo(List<LevelTerrainToolAction> actionList) {
            this.actionList = actionList;
        }
        
        @Override
        public void undo() {
            for (int i = actionList.size() - 1; i >= 0; i--) {
                LevelTerrainToolAction action = actionList.get(i);
                action.undo();
            }
        }

        @Override
        public void redo() {
            for (int i = 0; i < actionList.size(); i++) {
                LevelTerrainToolAction action = actionList.get(i);
                action.redo();
            }
        }
        
    }
}
