/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.font.BitmapText;
import com.jme3.gde.terraineditor.tools.SlopeExtraToolParams;
import com.jme3.gde.terraineditor.tools.SlopeTerrainToolAction;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.controls.SpatialControlTile;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.tiles.AutoScaleControl;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ToggleTool;

/**
 * @author huliqing
 */
public class SlopeTool extends AbstractTerrainTool implements ToggleTool {
    
    private final static String EVENT_SLOPE = "slopeEvent";
    
    protected Geometry controlObj;
    protected NumberValueTool radiusTool;
    protected NumberValueTool weightTool;
    
    protected final List<SlopeTerrainToolAction> actions = new ArrayList<SlopeTerrainToolAction>();
    
    private boolean modifying;
    private float lastRadiusUsed = 1.0f;
    private final float toolModifyRate = 0.05f; // how frequently (in seconds) it should update to throttle down the tool effect
    private float lastModifyTime; // last time the tool executed
    
    private PointControlTile p1;
    private PointControlTile p2;
    private Geometry line;
    private BitmapText angleText;

    public SlopeTool(String name, String tips, String icon) {
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

        if (p1 == null) {
            p1 = new PointControlTile(createPointMesh(ColorRGBA.Red, new Vector3f()));
        }
        if (p2 == null) {
            p2 = new PointControlTile(createPointMesh(ColorRGBA.Blue, new Vector3f(5, 5, 0)));
        }
        if (line == null) {
            line = createLine();
        }
        if (angleText == null) {
            angleText = createAngleText();
        }
        edit.addControlTile(p1);
        edit.addControlTile(p2);
        edit.getEditRoot().attachChild(line);
        edit.getEditRoot().attachChild(angleText);
        updateAngle();
    }
    
    @Override
    public void cleanup() {
        if (modifying) {
            endSlope();
        }
        if (initialized) {
            p1.cleanup();
            p2.cleanup();
            line.removeFromParent();
            angleText.removeFromParent();
            controlObj.removeFromParent();
        }
        super.cleanup(); 
    }
    
    public JmeEvent bindSlopeEvent() {
        return this.bindEvent(EVENT_SLOPE);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e.getName().equals(EVENT_SLOPE)) {
            if (e.isMatch()) {
                modifying = true;
                lastModifyTime = 0;
                doSlope();
                e.setConsumed(true);
            } else {
                modifying = false;
                endSlope();
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
                doSlope();
            }
        }
    }
    
    protected void doSlope() {
        float radius = radiusTool.getValue().floatValue();
        float weight = weightTool.getValue().floatValue();
        if (radius <= 0 || weight == 0) 
            return;
        
        EntityControlTile terrain = getTerrainEntity();
        if (terrain == null) 
            return;
        
        Vector3f point1 = p1.getControlSpatial().getWorldTranslation();
        Vector3f point2 = p2.getControlSpatial().getWorldTranslation();
        if (point1 != null && point2 != null && point1.distance(point2) > 0.01f) { // Preventing unexpected behavior, like destroying the terrain
            SlopeExtraToolParams params = new SlopeExtraToolParams();
            params.precision = false; // Snap on terrain editor
            params.lock = false; // Contain on terrain editor
            SlopeTerrainToolAction action = new SlopeTerrainToolAction(terrain, controlObj.getWorldTranslation()
                    , point1, point2, radius, weight, params.precision, params.lock);
            action.doAction();
            actions.add(action);
        }
    }
    
    private void endSlope() {
        if (actions.isEmpty()) {
            return;
        }
        // record undo action
        List<SlopeTerrainToolAction> actionList = new ArrayList<SlopeTerrainToolAction>(actions);
        edit.addUndoRedo(new SlopeUndoRedo(actionList));
        actions.clear();
    }
    
    protected Geometry createMesh() {
        Geometry marker = new Geometry();
        marker.setMesh(new Sphere(8, 8, 1));
        Material mat = new Material(editor.getAssetManager(), AssetConstants.MATERIAL_UNSHADED);
        mat.getAdditionalRenderState().setWireframe(true);
        marker.setMaterial(mat);
        marker.setLocalTranslation(0,0,0);
        mat.setColor("Color", ColorRGBA.Green);
        return marker;
    }
    
    private void updateAngle() {
        Vector3f temp, higher, lower;
        Vector3f point2 = p2.getTarget().getWorldTranslation();
        Vector3f point1 = p1.getTarget().getWorldTranslation();
        if (point2.y > point1.y) {
            temp = point2;
            higher = point2;
            lower = point1;
        } else {
            temp = point1;
            higher = point1;
            lower = point2;
        }
        temp = temp.clone().setY(lower.y);

        float angle = ((FastMath.asin(temp.distance(higher) / lower.distance(higher))) * FastMath.RAD_TO_DEG);
        angleText.setText(angle + "");
        angleText.setLocalTranslation(new Vector3f().interpolateLocal(point1, point2, 0.5f));

        if (line.getParent() == null) {
            edit.getEditRoot().attachChild(line);
            edit.getEditRoot().attachChild(angleText);
        }
        ((Line) line.getMesh()).updatePoints(point1, point2);
    }
    
    private Geometry createLine() {
        Geometry lineGeo = new Geometry("line", new Line(Vector3f.ZERO, Vector3f.ZERO));
        Material m = new Material(editor.getAssetManager(), AssetConstants.MATERIAL_UNSHADED);
        m.setColor("Color", ColorRGBA.White);
        lineGeo.setMaterial(m);
        return lineGeo;
    }
    
    private BitmapText createAngleText() {
        BitmapText label = new BitmapText(editor.getAssetManager().loadFont(AssetConstants.INTERFACE_FONTS_DEFAULT));
        BillboardControl control = new BillboardControl();
        label.addControl(control);
        label.setSize(0.5f);
        label.setCullHint(Spatial.CullHint.Never);
        AutoScaleControl asc = new AutoScaleControl(0.03f);
        label.addControl(asc);
        return label;
    }
    
    private Spatial createPointMesh(ColorRGBA color, Vector3f initLocation) {
        Material mat = new Material(editor.getAssetManager(), AssetConstants.MATERIAL_UNSHADED);
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        
        Geometry marker = new Geometry("Point");
        marker.setMesh(new Sphere(8, 8, 1));
        marker.setMaterial(mat);
        AutoScaleControl asc = new AutoScaleControl(0.02f);
        marker.addControl(asc);
        marker.setLocalTranslation(initLocation);
        return marker;
    }
    
    private class PointControlTile extends SpatialControlTile {

        public PointControlTile(Spatial target) {
            this.target = target;
        }
        
        @Override
        public void initialize(Node parent) {
            super.initialize(parent);
            parent.attachChild(target);
        }

        @Override
        public void cleanup() {
            target.removeFromParent();
            super.cleanup(); 
        }
        
        @Override
        protected void onLocationUpdated(Vector3f locaton) {
            super.onLocationUpdated(locaton);
            updateAngle();
        }
        
    }

    private class SlopeUndoRedo implements UndoRedo {
        
        private final List<SlopeTerrainToolAction> actionList;
        
        public SlopeUndoRedo(List<SlopeTerrainToolAction> actionList) {
            this.actionList = actionList;
        }
        
        @Override
        public void undo() {
            for (int i = actionList.size() - 1; i >= 0; i--) {
                SlopeTerrainToolAction action = actionList.get(i);
                action.undo();
            }
        }

        @Override
        public void redo() {
            for (int i = 0; i < actionList.size(); i++) {
                SlopeTerrainToolAction action = actionList.get(i);
                action.redo();
            }
        }
        
    }
}
