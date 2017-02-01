/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.font.BitmapText;
import com.jme3.gde.terraineditor.tools.SlopeExtraToolParams;
import com.jme3.gde.terraineditor.tools.SlopeTerrainToolAction;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
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
import name.huliqing.editor.edit.select.EntityControlTile;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ToggleTool;

/**
 * @author huliqing
 */
public class SlopeTool extends AbstractTerrainTool implements ToggleTool, ActionListener, AnalogListener{
    
    private final static String EVENT_SLOPE = "slopeEvent";
    
    protected Geometry controlObj;
    protected NumberValueTool radiusTool;
    protected NumberValueTool weightTool;
    
    protected final List<SlopeTerrainToolAction> actions = new ArrayList<SlopeTerrainToolAction>();
    
    private boolean modifying;
    private float lastRadiusUsed = 1.0f;
    private final float toolModifyRate = 0.05f; // how frequently (in seconds) it should update to throttle down the tool effect
    private float lastModifyTime; // last time the tool executed
    
    private Vector3f point1, point2;
    private Geometry markerSecondary, markerThird, line;
    private BitmapText angleText;
    private boolean ctrPressed = false;

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
        
        addMarkerSecondary(edit.getEditRoot());
        addMarkerThird(edit.getEditRoot());
        addLineAndText();
        
        editor.getInputManager().addMapping("slopeUp", new KeyTrigger(KeyInput.KEY_UP));
        editor.getInputManager().addMapping("slopeDown", new KeyTrigger(KeyInput.KEY_DOWN));
        editor.getInputManager().addMapping("ctr", new KeyTrigger(KeyInput.KEY_LCONTROL));
        editor.getInputManager().addMapping("ctr", new KeyTrigger(KeyInput.KEY_RCONTROL));
        editor.getInputManager().addMapping("clean", new KeyTrigger(KeyInput.KEY_C));
        editor.getInputManager().addListener(this, new String[] {"slopeUp", "slopeDown", "ctr", "clean"});
    }
    
    @Override
    public void cleanup() {
        editor.getInputManager().deleteMapping("slopeUp");
        editor.getInputManager().deleteMapping("slopeDown");
        editor.getInputManager().deleteMapping("ctr");
        editor.getInputManager().deleteMapping("clean");
        if (modifying) {
            endSlope();
        }
        if (initialized) {
            markerThird.removeFromParent();
            line.removeFromParent();
            angleText.removeFromParent();
            controlObj.removeFromParent();
        }
        super.cleanup(); 
    }
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("ctr")) {
            ctrPressed = isPressed;
        } else if (name.equals("clean")) {
            if (!isPressed) {
                point1 = null;
                point2 = null;
                markerSecondary.removeFromParent();
                markerThird.removeFromParent();
                line.removeFromParent();
                angleText.removeFromParent();
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals("slopeUp")) {
            markerThird.move(0f, 0.1f, 0f);
            point2.set(markerThird.getLocalTranslation());
            updateAngle();
        } else if (name.equals("slopeDown")) {
            markerThird.move(0f, -0.1f, 0f);
            point2.set(markerThird.getLocalTranslation());
            updateAngle();
        }
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
        Geometry marker = new Geometry("edit marker primary");
        marker.setMesh(new Sphere(8, 8, 1));
        Material mat = new Material(editor.getAssetManager(), AssetConstants.MATERIAL_UNSHADED);
        mat.getAdditionalRenderState().setWireframe(true);
        marker.setMaterial(mat);
        marker.setLocalTranslation(0,0,0);
        mat.setColor("Color", ColorRGBA.Green);
        return marker;
    }
    
     /**
     * Create the secondary marker mesh, placed
     * with the right mouse button.
     * @param parent it will attach to
     */
    public void addMarkerSecondary(Node parent) {
        if (markerSecondary == null) {
            markerSecondary = new Geometry("edit marker secondary");
            Mesh m2 = new Sphere(8, 8, 0.5f);
            markerSecondary.setMesh(m2);
            Material mat2 = new Material(editor.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            mat2.getAdditionalRenderState().setWireframe(false);
            markerSecondary.setMaterial(mat2);
            markerSecondary.setLocalTranslation(0,0,0);
            mat2.setColor("Color", ColorRGBA.Red);
        }
        parent.attachChild(markerSecondary);
    }
    
    private void addMarkerThird(Node parent) {
        if (markerThird == null) {
            markerThird = new Geometry("edit marker secondary");
            Mesh m2 = new Sphere(8, 8, 0.5f);
            markerThird.setMesh(m2);
            Material mat2 = new Material(editor.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            mat2.getAdditionalRenderState().setWireframe(false);
            markerThird.setMaterial(mat2);
            markerThird.setLocalTranslation(0, 0, 0);
            mat2.setColor("Color", ColorRGBA.Blue);
        }
        parent.attachChild(markerThird);
    }
    
    private void addLineAndText() {
        line = new Geometry("line", new Line(Vector3f.ZERO, Vector3f.ZERO));
        Material m = new Material(editor.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.White);
        line.setMaterial(m);

        angleText = new BitmapText(editor.getAssetManager().loadFont("Interface/Fonts/Default.fnt"));
        BillboardControl control = new BillboardControl();
        angleText.addControl(control);
        angleText.setSize(0.5f);
        angleText.setCullHint(Spatial.CullHint.Never);
    }
    
    private void updateAngle() {
        Vector3f temp, higher, lower;
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

        angleText.setText(angle + " degrees");
        angleText.setLocalTranslation(new Vector3f().interpolateLocal(point1, point2, 0.5f));

        if (line.getParent() == null) {
            edit.getEditRoot().attachChild(line);
            edit.getEditRoot().attachChild(angleText);
        }
        ((Line) line.getMesh()).updatePoints(point1, point2);
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
