/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.RaiseTerrainToolAction;
import com.jme3.gde.terraineditor.tools.RoughExtraToolParams;
import com.jme3.gde.terraineditor.tools.RoughTerrainToolAction;
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
import name.huliqing.editor.tools.ToggleTool;

/**
 *
 * @author huliqing
 */
public class RoughTool extends AbstractTerrainTool implements ToggleTool {
    private final static String EVENT_ROUGH = "roughEvent";

    private Geometry controlObj;
    private final List<RoughTerrainToolAction> actions = new ArrayList();
    
    private boolean modifying;
    private float lastRadiusUsed = 1.0f;
    private final float toolModifyRate = 0.05f; // how frequently (in seconds) it should update to throttle down the tool effect
    private float lastModifyTime; // last time the tool executed
    
    public RoughTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public void initialize(SimpleJmeEdit edit, TerrainToolbar toolbar) {
        super.initialize(edit, toolbar); 
        if (controlObj == null) {
            controlObj = createMesh();
        }
        edit.getEditRoot().attachChild(controlObj);
    }

    @Override
    public void cleanup() {
        controlObj.removeFromParent();
        super.cleanup(); 
    }
    
    public JmeEvent bindRoughEvent() {
        return this.bindEvent(EVENT_ROUGH);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e.getName().equals(EVENT_ROUGH)) {
            if (e.isMatch()) {
                modifying = true;
                lastModifyTime = 0;
                doRough();
                e.setConsumed(true);
            } else {
                modifying = false;
                endRough();
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
        if (Float.compare(toolbar.getRadiusTool().getValue().floatValue(), lastRadiusUsed) != 0) {
            lastRadiusUsed = toolbar.getRadiusTool().getValue().floatValue();
            controlObj.setLocalScale(lastRadiusUsed);
        }
        
        if (modifying) {
            lastModifyTime += tpf;
            if (lastModifyTime >= toolModifyRate) {
                lastModifyTime = 0;
                doRough();
            }
        }
    }
    
    private void doRough() {
        float radius = toolbar.getRadiusTool().getValue().floatValue();
        float weight = toolbar.getWeightTool().getValue().floatValue();
        if (radius <= 0 || weight == 0) 
            return;
        
                
        EntityControlTile terrain = getTerrainEntity();
        if (terrain == null) 
            return;
        
        RoughExtraToolParams params = new RoughExtraToolParams();
        params.lacunarity = 2;
        params.octaves = 6;
        params.scale = 0.1f;
        
        RoughTerrainToolAction action = new RoughTerrainToolAction(terrain, controlObj.getWorldTranslation(), radius, weight, params);
        action.doAction();
        actions.add(action);
    }
    
    private void endRough() {
        if (actions.isEmpty()) {
            return;
        }
        // record undo action
        List<RoughTerrainToolAction> actionList = new ArrayList<RoughTerrainToolAction>(actions);
        edit.addUndoRedo(new RoughUndoRedo(actionList));
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
    
    private class RoughUndoRedo implements UndoRedo {
        
        private final List<RoughTerrainToolAction> actionList;
        
        public RoughUndoRedo(List<RoughTerrainToolAction> actionList) {
            this.actionList = actionList;
        }
        
        @Override
        public void undo() {
            for (int i = actionList.size() - 1; i >= 0; i--) {
                RoughTerrainToolAction action = actionList.get(i);
                action.undo();
            }
        }

        @Override
        public void redo() {
            for (int i = 0; i < actionList.size(); i++) {
                RoughTerrainToolAction action = actionList.get(i);
                action.redo();
            }
        }
        
    }
}
