/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.shape;

import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.TempVars;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import name.huliqing.ly.utils.DebugDynamicUtils;
import name.huliqing.ly.utils.MathUtils;

/**
 * 使用spline来生成一个曲面
 * @author huliqing
 */
public class SplineSurface extends Mesh {
    
    private Spline spline;
    
    public SplineSurface(Spline spline, float width, int segments, Vector3f up) {
        this.spline = spline;
        updateGeometry(this.spline, width, segments, up);
    }
    
    /**
     * 使用路径点来生成曲面
     * @param waypoints 路径点
     * @param width 宽度
     * @param segments 分段数，数值越大曲面越平滑
     * @param up 开始的上方面
     * @param tension 曲线的张弛度，取值0.0~1.0, 0为折线，1为最平滑曲线
     * @param cycle 是否作为封闭曲面
     */
    public SplineSurface(Vector3f[] waypoints, float width, int segments, Vector3f up, float tension, boolean cycle) {
        spline = new MySpline();
        for (int i = 0; i < waypoints.length - 1; i++) {
            spline.getControlPoints().add(waypoints[i]);
        }
        spline.addControlPoint(waypoints[waypoints.length - 1]);
        spline.setCurveTension(tension);
        spline.setCycle(cycle);
        updateGeometry(spline, width, segments, up);
    }

    public final void updateGeometry(Spline spline, float width, int segments, Vector3f up) {
        TempVars tv = TempVars.get();
        float totalLen = spline.getTotalLength();
        float segLen = totalLen / segments; // 每一段的长度
        Vector3f left = tv.vect1;
        Vector3f right = tv.vect2;
        Vector3f tempLeft = tv.vect3;
        
        FloatBuffer pBuff = FloatBuffer.allocate(((segments + 1) * 2) * 3); // position
        FloatBuffer nBuff = FloatBuffer.allocate(((segments + 1) * 2) * 3); // normal
        // 最开始的两点
        Vector3f current = tv.vect4.set(spline.getControlPoints().get(0));
        Vector3f next = tv.vect5;
        Vector3f forwardDir = tv.vect6;
        Vector3f upDir = tv.vect7.set(up);
        Vector3f normal = tv.vect8.zero();
        // 左边和右边的总长度，用于计算texCoord
        float leftTotalLen = 0;
        float rightTotalLen = 0;
        // 左边和右边的各段的长度数组
        float[] leftSegLens = new float[segments];
        float[] rightSegLens = new float[segments];
        Vector3f previousLeft = tv.vect9;
        Vector3f previousRight = tv.vect10;
        float distance = 0;
        for (int i = 0; i < segments + 1; i++) {
            if (i < segments) {
                // 计算next点位置
//                spline.getSplinePoint(distance + segLen, next); // remove20160406
                MathUtils.getSplinePoint(spline, distance + segLen, next);
                
                // 计算forward方向
                next.subtract(current, forwardDir);
                // 计算up方向
                forwardDir.normalizeLocal();
                calUpDir(forwardDir, tempLeft, upDir);
            }
            // 计算出左右顶点位置left/right
            upDir.normalizeLocal();
            calPoints(current, forwardDir, upDir, width, left, right);
            
            // 储存
            pBuff.put(left.x).put(left.y).put(left.z);
            pBuff.put(right.x).put(right.y).put(right.z);
            
            // 混合前后up方向来平滑normal
            normal.addLocal(upDir).normalizeLocal();
            nBuff.put(normal.x).put(normal.y).put(normal.z);
            nBuff.put(normal.x).put(normal.y).put(normal.z);
            
            // 保存左边长度统计,由于spline的扭曲，左边和右边的长度和spline的总长度
            // 不一定是相等的。
            // 注意使用的distanceSquared，不需要去开方浪费资源，没卵用。
            if (i > 0) {
                leftSegLens[i - 1] = left.distanceSquared(previousLeft);
                rightSegLens[i - 1] = right.distanceSquared(previousRight);
                leftTotalLen += leftSegLens[i - 1];
                rightTotalLen += rightSegLens[i - 1];
            }
            
            // 计算当前点的位置
            current.set(next);
            distance += segLen;
            normal.set(upDir);
            previousLeft.set(left);
            previousRight.set(right);
        }
        tv.release();
        
        // 计算index
        int indexSize = segments * 6;
//        if (spline.isCycle()) {
//            indexSize += 6;
//        }
        ShortBuffer iBuff = ShortBuffer.allocate(indexSize);
        int i = 0;
        for (int count = 0; count < segments; count++) {
            iBuff.put((short)i);
            iBuff.put((short)(i + 1));
            iBuff.put((short)(i + 2));
            
            iBuff.put((short)(i + 1));
            iBuff.put((short)(i + 3));
            iBuff.put((short)(i + 2));
            i += 2;
        }
        
        // remove20160202,封闭展示效果不好
//        // 如果是首尾封闭则把它们连接起来
//        if (spline.isCycle()) {
//            iBuff.put((short) i);
//            iBuff.put((short)(i + 1));
//            iBuff.put((short)(0));
//            
//            iBuff.put((short)(i + 1));
//            iBuff.put((short)1);
//            iBuff.put((short)0);
//        }
        
        
        // 计算texcoord
        FloatBuffer tBuff = FloatBuffer.allocate(((segments + 1) * 2) * 2); // texCoord
        tBuff.put(0).put(0);
        tBuff.put(1).put(0);
        float leftSum = 0;
        float rightSum = 0;
        for (int j = 0; j < segments; j++) {
            leftSum += leftSegLens[j];
            rightSum += rightSegLens[j];
            tBuff.put(0).put(leftSum / leftTotalLen);
            tBuff.put(1).put(rightSum / rightTotalLen);
        }
        
        setBuffer(VertexBuffer.Type.Position, 3, pBuff.array());
        setBuffer(VertexBuffer.Type.TexCoord, 2, tBuff.array());
        setBuffer(VertexBuffer.Type.Normal, 3, nBuff.array());
        setBuffer(VertexBuffer.Type.Index, 3, iBuff.array());
        updateBound();
    }
    
    public void debug() {
        DebugDynamicUtils.debugSpline(toString(), spline, false);
    }
    
    // 根据前一个up方向计算下一个up方向,使用过度更自然一些
    private void calUpDir(Vector3f forwardDir, Vector3f tempLeft, Vector3f previousUp) {
        previousUp.cross(forwardDir, tempLeft).normalizeLocal();
        forwardDir.cross(tempLeft, previousUp);
    }
    
    // 计算出“左顶点”和“右顶点”位置, sp => splinePoint
    private void calPoints(Vector3f splinePoint, Vector3f forwardDir, Vector3f upDir, float width, Vector3f leftStore, Vector3f rightStore) {
        upDir.cross(forwardDir, leftStore).normalizeLocal().multLocal(width * 0.5f);
        rightStore.set(-leftStore.x, -leftStore.y, -leftStore.z);
        leftStore.addLocal(splinePoint);
        rightStore.addLocal(splinePoint);
    }
}
