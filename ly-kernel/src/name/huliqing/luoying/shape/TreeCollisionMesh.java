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
package name.huliqing.luoying.shape;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

/**
 * 主要用于优化树的碰撞检测,该类主要生成一个垂直的四面体，包围住树干，
 * 用于树干的碰撞检测。
 * @author huliqing
 */
public class TreeCollisionMesh extends Mesh{
    
    // 大概等于树干的粗细
    private float width = 1.5f;
    // 高度大概能够防止人物走上去就行了
    private float height = 10f;
    
    // 不需要normal
//    private static final float[] NORMAL = {
//        0, 0, 1,  0, 0, 1,  0, 0, 1,  0, 0, 1,  // front
//        1, 0, 0,  1, 0, 0,  1, 0, 0,  1, 0, 0,  // right
//        0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, // back
//        -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0  // left,
//    };
    
    private static final short[] INDEX = {
        0, 1, 2, 0, 2, 3, 1, 5, 6, 1, 6, 2,
        5, 4, 7, 5, 7, 6, 4, 0, 3, 4, 3, 7
    };
    
    public TreeCollisionMesh() {
        updateGeometry();
    }
    
    public TreeCollisionMesh(float width, float height) {
        this.width = width;
        this.height = height;
        updateGeometry();
    }
    
    private void updateGeometry() {
        float x = width * 0.5f;
        float h = height;
        setBuffer(VertexBuffer.Type.Position, 3, new float[] {
            -x,0,x,   x,0,x,   x,h,x,  -x,h,x   // front
           ,-x,0,-x,  x,0,-x,  x,h,-x,   -x,h,-x    // back
        });
//        setBuffer(VertexBuffer.Type.Normal, 3, NORMAL); // 不需要normal
        setBuffer(VertexBuffer.Type.Index, 3, INDEX);
    }
}
