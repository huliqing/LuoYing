/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.anim;

import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.Arrays;
import java.util.List;
import name.huliqing.luoying.data.AnimData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.position.Position;
import name.huliqing.luoying.utils.DebugDynamicUtils;

/**
 * @author huliqing
 */
public final class CurveMoveAnim extends AbstractAnim<Spatial> {
    
    // 是否自动跟随路径朝向
    private boolean facePath;
    // 初始旋转,只有打开facePath时才有用,该旋转会与运行时目标方向进行相乘,作为最
    // 终方向。
    private Quaternion rotationOffset;
    
    // ---- 内部
    private final Spline spline = new Spline();
    // 当前移动到的路径点索引
    private int currentPointIndex;
    private boolean debugInGui;
    
    public CurveMoveAnim() {
        super();
    }

    @Override
    public void setData(AnimData data) {
        super.setData(data);
        // 路径点
        String[] positions = data.getAsArray("waypoints");
        Vector3f[] waypoints = new Vector3f[positions.length];
        for (int i = 0; i < positions.length; i++) {
            waypoints[i] = ((Position)Loader.load(positions[i])).getPoint(null);
        }
        setControlPoints(Arrays.asList(waypoints));
        
        float curveTension = data.getAsFloat("curveTension", 0.5f);
        spline.setCurveTension(curveTension);
        
        this.facePath = data.getAsBoolean("facePath", facePath);
        
        Vector3f tempRotationOffset = data.getAsVector3f("rotationOffset");
        if (tempRotationOffset != null) {
            rotationOffset = new Quaternion();
            rotationOffset.fromAngles(tempRotationOffset.x, tempRotationOffset.y, tempRotationOffset.z);
        }
    }
    
    /**
     * 设置路径点，注意：这些顶点会被引用进去，所以不要在动画运行时修改points
     * 中路径点的信息，否则会影响动画。
     * @param points 
     */
    public final void setControlPoints(List<Vector3f> points) {
        spline.clearControlPoints();
        // 由于spline内部在每添加一次路径点时都会进行一次长度计算，浪费资源，所以
        // 这里先把前面所有点都直接加进去，后面再调用一次addControlPoint加最后一个点．
        // 注意：除最后一个顶点之外，前面的所有顶点都是引用进去的，非clone
        for (int i = 0; i < points.size() - 1; i++) {
            spline.getControlPoints().add(points.get(i));
        }
        // 最后一个点才调用addControlPoint,避免频繁计算长度，注：这个顶点是clone进去的
        spline.addControlPoint(points.get(points.size() - 1));
    }
    
    /**
     * 初始旋转
     * @param xAngle 弧度
     * @param yAngle 弧度
     * @param zAngle 弧度
     */
    public void setRotationOffset(float xAngle, float yAngle, float zAngle) {
        if (rotationOffset == null) {
            rotationOffset = new Quaternion();
        }
        rotationOffset.fromAngles(xAngle, yAngle, zAngle);
    }

    public boolean isFacePath() {
        return facePath;
    }

    /**
     * 设置是否在运行时自动朝向路线方向
     * @param facePath 
     */
    public void setFacePath(boolean facePath) {
        this.facePath = facePath;
    }
    
    /**
     * 设置曲线的张力
     * @param curveTension 
     */
    public void setCurveTension(float curveTension) {
        spline.setCurveTension(curveTension);
    }
    
    /**
     * 获取当前的路径点索引
     * @return 
     */
    public int getCurrentPointIndex() {
        return currentPointIndex;
    }
    
    /**
     * 获取曲线总长度
     * @return 
     */
    public float getTotalLength() {
        return spline.getTotalLength();
    }
    
    @Override
    protected void doInit() {
        currentPointIndex = 0;
        target.setLocalTranslation(spline.getControlPoints().get(0));
        if (debug) {
            debugPath();
        }
    }
    
    @Override
    protected void doAnimation(float interpolation) {
        
        TempVars tv = TempVars.get();
        float distance = interpolation * spline.getTotalLength();
        
        // 1.获取spline上指定距离处的位置
        getSplinePoint(spline, distance, tv.vect1);
        
        if (interpolation >= 1) {
            // end
            target.setLocalTranslation(spline.getControlPoints().get(spline.getControlPoints().size() - 1));
        } else {
            // dir
            if (facePath) {
                tv.vect1.subtract(target.getLocalTranslation(), tv.vect2).normalizeLocal();
                Quaternion rot = target.getLocalRotation();
                rot.lookAt(tv.vect2, Vector3f.UNIT_Y);
                if (rotationOffset != null) {
                    rot.multLocal(rotationOffset);
                }
                target.setLocalRotation(rot);
            }
            target.setLocalTranslation(tv.vect1);
        }
        tv.release();
    }
    
    // 获取spline上的点
    private Vector3f getSplinePoint(Spline spline, float distance, Vector3f store) {
        float sum = 0;
        int i = 0;
        for (Float len : spline.getSegmentsLength()) {
            if (sum + len >= distance) {
                currentPointIndex = i; // 重要
                spline.interpolate((distance - sum) / len, currentPointIndex, store);
                return store;
            }
            sum += len;
            i++;
        }
        
        // 到达最后一个点。
        List<Vector3f> cps = spline.getControlPoints();
        currentPointIndex = cps.size() - 1;
        store.set(cps.get(currentPointIndex));
        return store;
    }

    public void setDebug(boolean debug, boolean inGui) {
        this.debug = debug;
        this.debugInGui = inGui;
    }

    public boolean isDebug() {
        return debug;
    }

    public Spline getSpline() {
        return spline;
    }
    
    // ---- for debug 
    public void debugPath() {
        DebugDynamicUtils.debugSpline(this.hashCode() + "", spline, debugInGui);
    }
    
}
