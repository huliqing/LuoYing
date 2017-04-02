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
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import name.huliqing.editor.tiles.Axis.Type;

/**
 *
 * @author huliqing
 */
public class AxisNode extends Node {
    
    private final Axis axis;
    private final AxisLine axisLine = new AxisLine();
    private final Vector3f temp = new Vector3f();
    
    public AxisNode(Axis axis, ColorRGBA colorRGBA) {
        this.axis = axis;
        axisLine.setColor(colorRGBA);
        axisLine.setCullHint(CullHint.Always);
        attachChild(axis);
        attachChild(axisLine);
    }
    
    public void setAxisVisible(boolean visible) {
        axis.setCullHint(visible ? CullHint.Never : CullHint.Always);
    }
    
    public void setAxisLineVisible(boolean visible) {
        axisLine.setCullHint(visible ? CullHint.Never : CullHint.Always);
        if (axis != null) {
            axisLine.setDirection(axis.getWorldDirection(temp));
        }
    }
    
    public Type getType() {
        return axis.getType();
    }
    
    public Vector3f getDirection(Vector3f store) {
        return axis.getDirection(store);
    }
    
    public CollisionResults getCollisions(Ray ray, CollisionResults store) {
        return axis.getCollisions(ray, store);
    }
}
