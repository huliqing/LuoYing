/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.RaiseTerrainToolAction;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.terrain.Terrain;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.tools.NumberValueTool;

/**
 * 地形上升工具
 * @author huliqing
 */
public class RaiseTool extends AbstractTerrainTool {
    
    private Geometry controlObj;
    private float lastRadiusUsed = 1.0f;
    private NumberValueTool radiusTool;
    private NumberValueTool weightTool;
    
    private final static String EVENT_RAISE = "raiseEvent";

    public RaiseTool(String name, String tips, String icon) {
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
        if (controlObj != null) {
            controlObj.removeFromParent();
        }
        super.cleanup(); 
    }
    
    public JmeEvent bindRaiseEvent() {
        return this.bindEvent(EVENT_RAISE);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e.isMatch()) {
            if (e.getName().equals(EVENT_RAISE)) {
                doRaise();
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
    
    private void doRaise() {
        Terrain terrain = getTerrain();
        if (terrain == null) 
            return;
        RaiseTerrainToolAction action = new RaiseTerrainToolAction(terrain, controlObj.getWorldTranslation()
                , radiusTool.getValue().floatValue()
                , weightTool.getValue().floatValue());
        action.doRaise();
        edit.addUndoRedo(action);
    }

}
