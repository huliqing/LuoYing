/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;

/**
 *
 * @author huliqing
 */
public class RotationObj extends TransformObj {

//    private static final Logger LOG = Logger.getLogger(RotationObj.class.getName());
    
    private final RotationAxis tileRotation;
    private final CollisionResults crs = new CollisionResults();
    
    public RotationObj() {
        tileRotation = new RotationAxis();
        tileRotation.addControl(new AutoScaleControl());
        attachChild(tileRotation);
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible); 
        // 优化：当物体激活时，强制刷新一次，计算大小。
        tileRotation.getControl(AutoScaleControl.class).forceUpdate();
    }

    @Override
    public Axis pickTransformAxis(Ray ray) {
        Axis selectAxis = null;
        float minDistance = Float.MAX_VALUE;
        crs.clear();
        tileRotation.getAxisX().getCollisions(ray, crs);
        if (crs.size() > 0 &&  crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = tileRotation.getAxisX();
            minDistance = crs.getClosestCollision().getDistance();
        }
        crs.clear();
        tileRotation.getAxisY().getCollisions(ray, crs);
        if (crs.size() > 0 && crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = tileRotation.getAxisY();
            minDistance = crs.getClosestCollision().getDistance();
        }
        crs.clear();
        tileRotation.getAxisZ().getCollisions(ray, crs);
        if (crs.size() > 0 && crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = tileRotation.getAxisZ();
//            minDistance = crs.getClosestCollision().getDistance();
        }
        return selectAxis;
    }
    
    @Override
    public void showDebugLine(Axis axis, boolean visible) {
        super.showDebugLine(axis, visible);
        if (visible) {
            if (axis == tileRotation.getAxisX()) {
                debugLine.setColor(ColorRGBA.Red);
            } else if (axis == tileRotation.getAxisY()) {
                debugLine.setColor(ColorRGBA.Green);
            } else if (axis == tileRotation.getAxisZ()) {
                debugLine.setColor(ColorRGBA.Blue);
            }
        }
    }

}
