/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
        controlAxis.addControl(new AutoScaleControl(0.1f));
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
