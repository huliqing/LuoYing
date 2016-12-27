/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class ScaleObj extends TransformObj {

    private static final Logger LOG = Logger.getLogger(ScaleObj.class.getName());
    
    private final ScaleAxis tileScale;
    private final CollisionResults crs = new CollisionResults();
    
    public ScaleObj() {
        tileScale = new ScaleAxis();
        tileScale.addControl(new AutoScaleControl());
        attachChild(tileScale);
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible); 
        // 优化：当物体激活时，强制刷新一次，计算大小。
        tileScale.getControl(AutoScaleControl.class).forceUpdate();
    }
    
    @Override
    public Axis pickTransformAxis(Ray ray) {
        Axis selectAxis = null;
        float minDistance = Float.MAX_VALUE;
        crs.clear();
        tileScale.getAxisX().getCollisions(ray, crs);
        if (crs.size() > 0 &&  crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = tileScale.getAxisX();
            minDistance = crs.getClosestCollision().getDistance();
        }
        crs.clear();
        tileScale.getAxisY().getCollisions(ray, crs);
        if (crs.size() > 0 && crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = tileScale.getAxisY();
            minDistance = crs.getClosestCollision().getDistance();
        }
        crs.clear();
        tileScale.getAxisZ().getCollisions(ray, crs);
        if (crs.size() > 0 && crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = tileScale.getAxisZ();
//            minDistance = crs.getClosestCollision().getDistance();
        }
        return selectAxis;
    }
    
    public boolean isPickCenter(Ray ray) {
        return tileScale.getCenter().getWorldBound().intersects(ray);
    }
    
    @Override
    public void showDebugLine(Axis axis, boolean visible) {
        super.showDebugLine(axis, visible);
        if (visible) {
            if (axis == tileScale.getAxisX()) {
                debugLine.setColor(ColorRGBA.Red);
            } else if (axis == tileScale.getAxisY()) {
                debugLine.setColor(ColorRGBA.Green);
            } else if (axis == tileScale.getAxisZ()) {
                debugLine.setColor(ColorRGBA.Blue);
            }
        }
    }
    
}
