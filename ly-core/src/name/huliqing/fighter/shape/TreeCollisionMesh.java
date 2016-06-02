/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.shape;

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
