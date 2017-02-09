/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.terrain;

import com.jme3.font.BitmapText;
import com.jme3.gde.terraineditor.tools.AbstractTerrainToolAction;
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
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.controls.SpatialControlTile;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.tiles.AutoScaleControl;
import name.huliqing.editor.toolbar.TerrainToolbar;

/**  
 * @author huliqing
 */
public class SlopeTool extends AdjustTerrainTool {
    
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
        if (initialized) {
            p1.cleanup();
            p2.cleanup();
            line.removeFromParent();
            angleText.removeFromParent();
        }
        super.cleanup(); 
    }
    
    @Override
    protected AbstractTerrainToolAction createAction(float radius, float weight, Vector3f markerWorldLoc, EntityControlTile terrain) {
        Vector3f point1 = p1.getControlSpatial().getWorldTranslation();
        Vector3f point2 = p2.getControlSpatial().getWorldTranslation();
        if (point1 != null && point2 != null && point1.distance(point2) > 0.01f) { // Preventing unexpected behavior, like destroying the terrain
            boolean precision = toolbar.getSlopeParamsTool().getPrecision().getValue(); // Snap on terrain editor
            boolean lock = toolbar.getSlopeParamsTool().getLock().getValue(); // Contain on terrain editor
            SlopeTerrainToolAction action = new SlopeTerrainToolAction(terrain, markerWorldLoc
                    , point1, point2, radius, weight, precision, lock);
            setModified(true);
            return action;
        }
        return null;
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
        public void initialize(SimpleJmeEdit edit) {
            super.initialize(edit);
            edit.getEditRoot().attachChild(target);
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
   
}
