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
 * 自定义的Quad,水平的Quad,即在xz平面上，原点位于quad的中心点
 * @author huliqing
 */
public final class QuadXZC extends Mesh {

    private float width;
    private float height;

    public QuadXZC() {
    }

    public QuadXZC(float width, float height) {
        updateGeometry(width, height);
    }

    public QuadXZC(float width, float height, boolean flipCoords) {
        updateGeometry(width, height, flipCoords);
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void updateGeometry(float width, float height) {
        updateGeometry(width, height, false);
    }

    public void updateGeometry(float width, float height, boolean flipCoords) {
        this.width = width;
        this.height = height;
        setBuffer(VertexBuffer.Type.Position, 3,
                new float[]{   -width * 0.5f, 0, height * 0.5f,
                                width * 0.5f, 0, height * 0.5f,
                                width * 0.5f, 0, -height * 0.5f,
                               -width * 0.5f, 0, -height * 0.5f
        });


        if (flipCoords) {
            setBuffer(VertexBuffer.Type.TexCoord, 2, new float[]{0, 1,
                1, 1,
                1, 0,
                0, 0});
        } else {
            setBuffer(VertexBuffer.Type.TexCoord, 2, new float[]{0, 0,
                1, 0,
                1, 1,
                0, 1});
        }
        setBuffer(VertexBuffer.Type.Normal, 3, new float[]{0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1});
        if (height < 0) {
            setBuffer(VertexBuffer.Type.Index, 3, new short[]{0, 2, 1,
                0, 3, 2});
        } else {
            setBuffer(VertexBuffer.Type.Index, 3, new short[]{0, 1, 2,
                0, 2, 3});
        }

        updateBound();
    }
}
