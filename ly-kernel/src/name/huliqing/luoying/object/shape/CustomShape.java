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

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3tools.optimize.GeometryBatchFactory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.ShapeData;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * 自定义的网格物体,这个类主要是从一个j3o文件中载入并读取其网格返回.
 * @author huliqing
 */
public class CustomShape extends AbstractShape {

    private String file;
    
    @Override
    public void setData(ShapeData data) {
        super.setData(data);
        file = data.getAsString("file");
    }
    
    @Override
    public Mesh getMesh() {
        if (file != null) {
            Spatial spa = LuoYing.getAssetManager().loadModel(file);
            if (spa instanceof Geometry) {
                return ((Geometry) spa).getMesh();
            }
            // 多个网格时需要整合成一个
            if (spa instanceof Node) {
                List<Geometry> listGeo = GeometryUtils.findAllGeometry(spa);
                if (listGeo != null && !listGeo.isEmpty()) {
                    Mesh outMesh = new Mesh();
                    GeometryBatchFactory.mergeGeometries(listGeo, outMesh);
                    return outMesh;
                }
            }
        }
        // 如果没有指定file或者创建不了,则使用一个默认的Box返回，避免异常。
        Logger.getLogger(CustomShape.class.getName())
                .log(Level.WARNING, "Could not create custom mesh, file={0}. Use new Box() instead!", file);
        return new Box();
    }


    
}
