/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.shape;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

/**
 * 垂直竖立的quad,即在xy平面上，原点位于平面的中心点
 * @author huliqing
 */
public final class QuadXYC extends Mesh {

    private float width;
    private float height;

    public QuadXYC() {
    }

    public QuadXYC(float width, float height) {
        updateGeometry(width, height);
    }

    public QuadXYC(float width, float height, boolean flipCoords) {
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
                new float[]{-width * 0.5f, -height * 0.5f, 0,
            width * 0.5f, -height * 0.5f, 0,
            width * 0.5f, height * 0.5f, 0,
            -width * 0.5f, height * 0.5f, 0
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
