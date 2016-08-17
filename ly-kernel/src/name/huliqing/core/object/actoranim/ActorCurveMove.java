/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actoranim;

import com.jme3.math.FastMath;
import com.jme3.math.Spline;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActorAnimData;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.utils.ConvertUtils;
import name.huliqing.core.utils.DebugDynamicUtils;

/**
 * 让角色进行曲线运动
 * @author huliqing
 * @param <T>
 */
public class ActorCurveMove<T extends ActorAnimData> extends ActorAnim<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    
    public enum Facing {
        /** 不作朝向处理 */
        none,
        
        /** 面朝路径方向 */
        path,
        
        /** 面朝当前目标 */
        target,
    }
    
    // 坐标，注意：这里的坐标是以角色本身坐标系统为依据的,非世界坐标。
    // 使用时需要随着角色坐标的变化而转换
    private List<Vector3f> points;
    // 始终保持point在地面上,该选项可防止产生的控制点在地面下，导致角色掉下去。
    private boolean upperGround = true;
    // curveTension
    private float tension = 0.5f;
    
    // ---- 内部参数 ----
    private final Spline spline = new Spline();
    // 曲线总长度
    private float totalLength;
    // 是否自动跟随路径朝向
    private Facing facing = Facing.none;
    // 使用正弦曲线来增强运动效果,匀速的运动很难看
    private boolean useSine;
    // 记住原始kinematic状态,以便结束时还原
    private boolean oldKinematicState;

    @Override
    public void setData(T data) {
        super.setData(data);
        if (data != null) {
            // 1.points
            String[] tempPoints = data.getAsArray("points");
            if (tempPoints != null) {
                points = new ArrayList<Vector3f>(tempPoints.length);
                for (String tp : tempPoints) {
                    if (tp.trim().equals("")) continue;
                    String[] tpArr = tp.split("\\|");
                    Vector3f point = new Vector3f(
                            ConvertUtils.toFloat(tpArr[0], 0)
                            ,ConvertUtils.toFloat(tpArr[1], 0)
                            ,ConvertUtils.toFloat(tpArr[2], 0)
                            );
                    points.add(point);
                }
            }
            
            String tempFacing = data.getAsString("facing", facing.name());
            if (tempFacing.equals(Facing.path.name())) {
                facing = Facing.path;
            } else if (tempFacing.equals(Facing.target.name())) {
                facing = Facing.target;
            }
            
            upperGround = data.getAsBoolean("upperGround", upperGround);
            tension = data.getAsFloat("tension", tension);
            useSine = data.getAsBoolean("useSine", useSine);
        }
    }
    
    public void setControlPoints(Vector3f[] controlPoints) {
        if (points == null) {
            points = new ArrayList<Vector3f>(controlPoints.length);
        }
        points.clear();
        points.addAll(Arrays.asList(controlPoints));
    }

    @Override
    protected void doInit() {
        
        // --- 优化版
        // 清理后重新加载控制点
        spline.clearControlPoints();
        // 第一个点为角色的当前位置
        spline.getControlPoints().add(target.getSpatial().getWorldTranslation().clone());
        if (points != null && !points.isEmpty()) {
            TempVars tv = TempVars.get();
            for (int i = 0; i < points.size(); i++) {
                localToWorld(points.get(i), tv.vect1);
                // 把该点移动到地面上（有部分点可能在地面下），这可防止角色掉到地下。
                if (upperGround) {
                    playService.moveUpToTerrain(tv.vect1);
                }
                
                // 中间的路径点直接通过getControlPoints().add(..)就可以，这样
                // 不需要每次都去触发spline计算线路,只有最后一个顶点需要触发这个计算就可以。
                // 这可以节省一些计算资源
                if (i < points.size() - 1) {
                    spline.getControlPoints().add(tv.vect1.clone());
                } else {
                    spline.addControlPoint(tv.vect1); // addControlPoint自己会复制一份。
                }
            }
            tv.release();
        }
        spline.setCurveTension(tension);
        totalLength = spline.getTotalLength();
        
        if (debug) {
            debugPath();
        }
        
        // 记住原状态
        oldKinematicState = actorService.isKinematic(target);
        // 该设置可避免在运动过程中产生擅抖现象, 但是需要在cleanup的时候复原
        // 直接关闭物理效果也可以避免抖动，但是角色有可能掉入其它物体内部
        actorService.setKinematic(target, true);
    }
    
    /**
     * 将角色本地相对坐标转换为世界坐标
     * @param local
     * @param store
     * @return 
     */
    private Vector3f localToWorld(Vector3f local, Vector3f store) {
        target.getSpatial().getWorldRotation().mult(local, store);
        store.addLocal(target.getSpatial().getWorldTranslation());
        return store;
    }

    @Override
    protected void doAnimation(float interpolation) {
        // 正弦曲线,增强运动效果
        if (useSine) {
            interpolation = FastMath.sin(interpolation * FastMath.HALF_PI);
        }
        
        TempVars tv = TempVars.get();
        float distance = interpolation * totalLength;
        getWayPointIndexForDistance(distance, tv.vect2d);
        spline.interpolate(tv.vect2d.y, (int) tv.vect2d.x, tv.vect1);
        
        // dir
        if (facing == Facing.path) {
            tv.vect1.subtract(target.getSpatial().getWorldTranslation(), tv.vect2).normalizeLocal();
            actorService.setViewDirection(target, tv.vect2);
            
        } else if (facing == Facing.target) {
            Actor other = actorService.getTarget(target);
            
            if (other != null) {
                other.getSpatial().getWorldTranslation()
                        .subtract(target.getSpatial().getWorldTranslation(), tv.vect5).normalizeLocal();
                actorService.setViewDirection(target, tv.vect5);
            }
        }
        
        if (interpolation >= 1) {
            // end
            actorService.setLocation(target, spline.getControlPoints().get(spline.getControlPoints().size() - 1));
            
        } else {
            // local必须在计算出facing后再设置
            actorService.setLocation(target, tv.vect1);
            
        }
        tv.release();
    }

    @Override
    public void cleanup() {
        if (target != null) {
            actorService.setKinematic(target, oldKinematicState);
        }
        super.cleanup(); 
    }
    
    /**
     * compute the index of the waypoint and the interpolation value according to a distance
     * returns a vector 2 containing the index in the x field and the interpolation value in the y field
     * @param distance the distance traveled on this path
     * @return the waypoint index and the interpolation value in a vector2
     */
    private Vector2f getWayPointIndexForDistance(float distance, Vector2f store) {
        float sum = 0;
        distance = distance % spline.getTotalLength();
        int i = 0;
        for (Float len : spline.getSegmentsLength()) {
            if (sum + len >= distance) {
                store.set((float) i, (distance - sum) / len);
                return store;
            }
            sum += len;
            i++;
        }
        store.set((float) spline.getControlPoints().size() - 1, 1.0f);
        return store;
    }
    
    public void debugPath() {
        DebugDynamicUtils.debugSpline(this.hashCode() + "", spline, false);
    }
}
