/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Node;

/**
 * 操作行为物体
 * @author huliqing
 */
public abstract class ControlObj extends Node {

    private final CollisionResults crs = new CollisionResults();

    public ControlObj() {}
    
    public boolean isVisible() {
        return getCullHint() == CullHint.Never;
    }

    public void setVisible(boolean visible) {
        setCullHint(visible ? CullHint.Never : CullHint.Always);
    }
    
    public void setAxisVisible(boolean visible) {
        getAxisX().setAxisVisible(visible);
        getAxisY().setAxisVisible(visible);
        getAxisZ().setAxisVisible(visible);
    }
    
    public void setAxisLineVisible(boolean visible) {
        getAxisX().setAxisLineVisible(visible);
        getAxisY().setAxisLineVisible(visible);
        getAxisZ().setAxisLineVisible(visible);
    }
    
    /**
     * 通过射线获取一个操作轴，如果没有可用的操作轴则返回null.
     * @param ray
     * @return 
     */
    public AxisNode getPickAxis(Ray ray) {
        AxisNode selectAxis = null;
        AxisNode axisX = getAxisX();
        AxisNode axisY = getAxisY();
        AxisNode axisZ = getAxisZ();
        
        float minDistance = Float.MAX_VALUE;
        crs.clear();
        axisX.getCollisions(ray, crs);
        if (crs.size() > 0 &&  crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = axisX;
            minDistance = crs.getClosestCollision().getDistance();
        }
        crs.clear();
        axisY.getCollisions(ray, crs);
        if (crs.size() > 0 && crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = axisY;
            minDistance = crs.getClosestCollision().getDistance();
        }
        crs.clear();
        axisZ.getCollisions(ray, crs);
        if (crs.size() > 0 && crs.getClosestCollision().getDistance() < minDistance) {
            selectAxis = axisZ;
//            minDistance = crs.getClosestCollision().getDistance();
        }
        return selectAxis;
    }
    
    public abstract AxisNode getAxisX();
    public abstract AxisNode getAxisY();
    public abstract AxisNode getAxisZ();
}
