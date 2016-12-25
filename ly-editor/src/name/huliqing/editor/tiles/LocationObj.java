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
public class LocationObj extends TransformObj {
//    private static final Logger LOG = Logger.getLogger(LocationObj.class.getName());
    
    private final LocationAxis locationAxis;
    private final CollisionResults crs = new CollisionResults();
    
    public LocationObj() {
        locationAxis = new LocationAxis();
        locationAxis.addControl(new AutoScaleControl());
        attachChild(locationAxis);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible); 
        // 优化：当物体激活时，强制刷新一次，计算大小。
        locationAxis.getControl(AutoScaleControl.class).forceUpdate();
    }

    @Override
    public void showDebugLine(Axis axis, boolean visible) {
        super.showDebugLine(axis, visible);
        if (visible) {
            if (axis == locationAxis.getAxisX()) {
                debugLine.setColor(ColorRGBA.Red);
            } else if (axis == locationAxis.getAxisY()) {
                debugLine.setColor(ColorRGBA.Green);
            } else if (axis == locationAxis.getAxisZ()) {
                debugLine.setColor(ColorRGBA.Blue);
            }
        }
    }
    
    @Override
    public Axis pickTransformAxis(Ray ray) {
        Axis selectAxis = null;
        float minDistance = Float.MAX_VALUE;
        crs.clear();
        locationAxis.getAxisX().getCollisions(ray, crs);
        if (crs.size() > 0 &&  crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = locationAxis.getAxisX();
            minDistance = crs.getClosestCollision().getDistance();
        }
        crs.clear();
        locationAxis.getAxisY().getCollisions(ray, crs);
        if (crs.size() > 0 && crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = locationAxis.getAxisY();
            minDistance = crs.getClosestCollision().getDistance();
        }
        crs.clear();
        locationAxis.getAxisZ().getCollisions(ray, crs);
        if (crs.size() > 0 && crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = locationAxis.getAxisZ();
//            minDistance = crs.getClosestCollision().getDistance();
        }
        return selectAxis;
    }
    
}
