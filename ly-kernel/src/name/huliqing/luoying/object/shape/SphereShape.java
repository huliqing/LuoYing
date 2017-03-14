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
package name.huliqing.luoying.object.shape;

import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Sphere;
import name.huliqing.luoying.data.ShapeData;

/**
 * 球形的网格物体
 * @author huliqing
 */
public class SphereShape extends AbstractShape {

    private float radius = 1.0f;
    private int zSamples = 8;
    private int radialSamples = 8;
    
    @Override
    public void setData(ShapeData data) {
        super.setData(data);
        radius = data.getAsFloat("radius", radius);
        zSamples = data.getAsInteger("zSamples", zSamples);
        radialSamples = data.getAsInteger("radialSamples", radialSamples);
    }
    
    @Override
    public Mesh getMesh() {
        Sphere sp = new Sphere(zSamples, radialSamples, radius);
        return sp;
    }
    
    
     
}