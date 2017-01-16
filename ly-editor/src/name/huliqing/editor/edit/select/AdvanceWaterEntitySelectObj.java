/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.select;

import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.object.entity.impl.AdvanceWaterEntity;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * 高级水体的操作物体
 * @author huliqing
 */
public class AdvanceWaterEntitySelectObj extends EntitySelectObj<AdvanceWaterEntity>{
    
    private final Spatial controlObj = createControlObj();
    private final float MAX_RADIUS = Integer.MAX_VALUE;
    
    public AdvanceWaterEntitySelectObj() {}
    
    @Override
    public void initialize(SceneEdit form) {
        super.initialize(form);
        form.getEditRoot().attachChild(controlObj);
        if (entity.getCenter() != null) {
            setLocalTranslation(entity.getCenter());
            setLocalScale(new Vector3f(entity.getRadius(),entity.getRadius(),entity.getRadius()));
        } else {
            Vector3f loc = entity.getSpatial().getLocalTranslation();
            loc.setY(entity.getWaterHeight());
            setLocalTranslation(loc);
            setLocalScale(new Vector3f(MAX_RADIUS, MAX_RADIUS, MAX_RADIUS));
        }
    }

    @Override
    public void cleanup() {
        controlObj.removeFromParent();
        super.cleanup();
    }

    @Override
    public void setLocalScale(Vector3f scale) {
        super.setLocalScale(scale);
        if (entity.getCenter() != null) {
            controlObj.setLocalScale(scale.getX());
            entity.setRadius(scale.getX());
        } else {
            controlObj.setLocalScale(MAX_RADIUS);
            entity.setRadius(0); // 0 表示无限大
        }
    }

    @Override
    public void setLocalRotation(Quaternion rotation) {
        super.setLocalRotation(rotation);
        // ignore
    }

    @Override
    public void setLocalTranslation(Vector3f location) {
        super.setLocalTranslation(location);
        controlObj.setLocalTranslation(location);
        entity.setWaterHeight(location.y);
        if (entity.getCenter() != null) {
            entity.setCenter(location.clone());
        }
    }
    
    @Override
    public Float distanceOfPick(Ray ray) {
        Float dist = null;
        CollisionResults cr = PickManager.pick(ray, controlObj, null);
        if (cr != null && cr.size() > 0) {
            dist = cr.getClosestCollision().getDistance();
            // 优化：对于无界限水体，当操作时把controlObj位置移动到点击点处，这样方便操作
            if (entity.getCenter() == null) {
                controlObj.setLocalTranslation(cr.getClosestCollision().getContactPoint());
            }
        }
        return dist;
    }

    @Override
    public Spatial getReadOnlySelectedSpatial() {
        return controlObj;
    }
    
    private Spatial createControlObj() {
        Quad quad = new Quad(1,1);
        Geometry geo = new Geometry("", quad);
        geo.rotate(-FastMath.HALF_PI, 0, 0);
        geo.setLocalTranslation(-0.5f, 0, 0.5f);
        Material mat = MaterialUtils.createUnshaded();
        geo.setMaterial(mat);
        geo.setCullHint(Spatial.CullHint.Always);
        
        Node root = new Node();
        root.attachChild(geo);
        return root;
    }
}
