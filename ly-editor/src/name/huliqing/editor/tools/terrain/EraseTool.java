/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.PaintTerrainToolAction;
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
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.utils.TerrainUtils;

/**
 *
 * @author huliqing
 */
public class EraseTool extends AbstractTerrainTool {

    private final static String EVENT_ERASE = "eraseEvent";
    
    protected Geometry controlObj;
    protected NumberValueTool radiusTool;
    protected NumberValueTool weightTool;
    protected TexLayerTool texLayerTool;
    
    protected final List<PaintTerrainToolAction> actions = new ArrayList<PaintTerrainToolAction>();
    
    private boolean modifying;
    private float lastRadiusUsed = 1.0f;
    private final float toolModifyRate = 0.05f; // how frequently (in seconds) it should update to throttle down the tool effect
    private float lastModifyTime; // last time the tool executed
    
    public EraseTool(String name, String tips, String icon) {
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
        texLayerTool = toolbar.getTexLayerTool();
    }

    @Override
    public void cleanup() {
        if (modifying) {
            endErase();
        }
        if (controlObj != null) {
            controlObj.removeFromParent();
        }
        super.cleanup();
    }
    
    public JmeEvent bindEraseEvent() {
        return this.bindEvent(EVENT_ERASE);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e.getName().equals(EVENT_ERASE)) {
            if (e.isMatch()) {
                modifying = true;
                lastModifyTime = 0;
                doErase();
                e.setConsumed(true);
            } else {
                modifying = false;
                endErase();
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
                doErase();
            }
        }
    }
    
    private void doErase() {
        float radius = radiusTool.getValue().floatValue();
        float weight = weightTool.getValue().floatValue();
        int textureIndex = texLayerTool.getSelectLayerIndex();
        if (radius <= 0 || weight == 0 || textureIndex < 0 || textureIndex >= TerrainUtils.MAX_TEXTURES)
            return;
        
        EntityControlTile terrain = getTerrainEntity();
        if (terrain == null) 
            return;
        
        PaintTerrainToolAction action = new PaintTerrainToolAction(terrain, controlObj.getWorldTranslation()
                , radius, -weight, textureIndex);
        action.doAction();
        actions.add(action);
    }

    private void endErase() {
        if (actions.isEmpty()) {
            return;
        }
        // record undo action
        List<PaintTerrainToolAction> actionList = new ArrayList<PaintTerrainToolAction>(actions);
        edit.addUndoRedo(new EraseUndoRedo(actionList));
        actions.clear();
    }
    
    private Geometry createMesh() {
        Geometry marker = new Geometry("edit marker primary");
        marker.setMesh(new Sphere(8, 8, 1));
        Material mat = new Material(editor.getAssetManager(), AssetConstants.MATERIAL_UNSHADED);
        mat.getAdditionalRenderState().setWireframe(true);
        marker.setMaterial(mat);
        marker.setLocalTranslation(0,0,0);
        mat.setColor("Color", ColorRGBA.Green);
        return marker;
    }

    private class EraseUndoRedo implements UndoRedo {
        
        private final List<PaintTerrainToolAction> actionList;
        
        public EraseUndoRedo(List<PaintTerrainToolAction> actionList) {
            this.actionList = actionList;
        }
        
        @Override
        public void undo() {
            for (int i = actionList.size() - 1; i >= 0; i--) {
                PaintTerrainToolAction action = actionList.get(i);
                action.undo();
            }
        }

        @Override
        public void redo() {
            for (int i = 0; i < actionList.size(); i++) {
                PaintTerrainToolAction action = actionList.get(i);
                action.redo();
            }
        }
        
    }
}
