/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.effect;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.core.data.EffectData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.shape.MySpline;
import name.huliqing.core.shape.SplineSurface;

/**
 * 在曲面上的颜色滑动效果
 * @author huliqing
 */
public class SlideColorSplineEffect extends SlideColorEffect {

    private Vector3f[] fixedPoints;
    private String[] randomPoints; // randomPoints指向的是emitterShape的id.
    private float width = 1;
    private int segments = 40;
    private float tension = 0.5f;
    private boolean cycle;
    private Vector3f up = Vector3f.UNIT_Y;
    
    // 维度
    private int dimension = 1;
    
    @Override
    protected final Spatial loadAnimModel() {
        fixedPoints = data.getAsVector3fArray("fixedPoints");
        randomPoints = data.getAsArray("randomPoints");
        width = data.getAsFloat("width", width);
        segments = data.getAsInteger("segments", segments);
        tension = data.getAsFloat("tension", tension);
        cycle = data.getAsBoolean("cycle", cycle);
        up = data.getAsVector3f("up", up).normalizeLocal();
        dimension = data.getAsInteger("dimension", dimension);
        
        if (fixedPoints == null && randomPoints == null)
            throw new NullPointerException("need fixedPoints or randomPoints");
        
        // 生成路径点
        if (fixedPoints == null) {
            fixedPoints = new Vector3f[randomPoints.length];
            for (int i = 0; i < randomPoints.length; i++) {
                Vector3f point = new Vector3f();
                Loader.loadPosition(randomPoints[i]).getPoint(point);
                fixedPoints[i] = point;
            }
        }
        MySpline spline = createSpline(fixedPoints, tension, cycle);
        if(dimension > 1) {
            // 计算出实际的维度旋转轴
            TempVars tv = TempVars.get();
            Vector3f nextPoint = tv.vect1;
            Vector3f forward = tv.vect2;
            Vector3f left = tv.vect3;
            Vector3f rotAxis = tv.vect4;
            spline.getSplinePoint(spline.getTotalLength() / segments, nextPoint);
            nextPoint.subtract(spline.getControlPoints().get(0), forward).normalizeLocal();
            up.cross(forward, left).normalizeLocal();
            left.cross(up, rotAxis).normalizeLocal();
            
            Quaternion qua = new Quaternion();
            Vector3f tempUp = tv.vect5;
            float angle = FastMath.PI / dimension;
            Node animNode = new Node();
            for (int i = 0; i < dimension; i++) {
                qua.fromAngleAxis(angle * i, rotAxis);
                qua.mult(up, tempUp);
                animNode.attachChild(createSurface(spline, tempUp));
            }
            tv.release();
            return animNode;
        } else {
            return createSurface(spline, up);
        }
    }
    
    private Geometry createSurface(MySpline spline, Vector3f up) {
        SplineSurface surface = new SplineSurface(spline, width, segments, up);
        Geometry geo = new Geometry("SplineSurface", surface);
        return geo;
    }
    
    private MySpline createSpline(Vector3f[] waypoints, float tension, boolean cycle) {
        MySpline spline = new MySpline();
        for (int i = 0; i < waypoints.length - 1; i++) {
            spline.getControlPoints().add(waypoints[i]);
        }
        spline.addControlPoint(waypoints[waypoints.length - 1]);
        spline.setCurveTension(tension);
        spline.setCycle(cycle);
        return spline;
    }

    
}
