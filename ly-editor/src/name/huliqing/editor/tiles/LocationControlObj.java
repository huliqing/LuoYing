/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;

/**
 *
 * @author huliqing
 */
public class LocationControlObj extends ControlObj {
//    private static final Logger LOG = Logger.getLogger(LocationControlObj.class.getName());
    
    private final LocationAxis controlAxis; 
    private final CollisionResults crs = new CollisionResults();
    
    public LocationControlObj() {
        controlAxis = new LocationAxis();
        controlAxis.addControl(new AutoScaleControl());
        attachChild(controlAxis);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible); 
        // 优化：当物体激活时，强制刷新一次，计算大小。
        controlAxis.getControl(AutoScaleControl.class).forceUpdate();
    }
    
    @Override
    public AxisNode getAxisX() {
        return controlAxis.getAxisX();
    }

    @Override
    public AxisNode getAxisY() {
        return controlAxis.getAxisY();
    }

    @Override
    public AxisNode getAxisZ() {
        return controlAxis.getAxisZ();
    }
    
    @Override
    public AxisNode getPickAxis(Ray ray) {
        AxisNode selectAxis = null;
        float minDistance = Float.MAX_VALUE;
        crs.clear();
        controlAxis.getAxisX().getCollisions(ray, crs);
        if (crs.size() > 0 &&  crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = controlAxis.getAxisX();
            minDistance = crs.getClosestCollision().getDistance();
        }
        crs.clear();
        controlAxis.getAxisY().getCollisions(ray, crs);
        if (crs.size() > 0 && crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = controlAxis.getAxisY();
            minDistance = crs.getClosestCollision().getDistance();
        }
        crs.clear();
        controlAxis.getAxisZ().getCollisions(ray, crs);
        if (crs.size() > 0 && crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = controlAxis.getAxisZ();
//            minDistance = crs.getClosestCollision().getDistance();
        }
        return selectAxis;
    }

}
