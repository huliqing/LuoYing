/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.bullet;

import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.BulletData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.anim.CurveMoveAnim;
import name.huliqing.core.object.position.Position;

/**
 * 曲线类型的子弹。
 * @author huliqing
 * @param <S>
 */
public class CurveBullet<S> extends StraightBullet<S> {
    
    // 曲线张力
    private float tension = 0.5f;
    // 中间路径点的emitterShape生成器
    protected List<Position> positions;
    // 上方向，用于计算中间路径点。
    protected Vector3f up;
    
    // ---- 内部参数
    // 是否处于跟踪状态
    private boolean tracing;
    // 动画对象
    private final CurveMoveAnim cma = new CurveMoveAnim();
    // 所有的路径点
    private List<Vector3f> waypoints;
    // 临时的用于缓存中间路径点,中间路径点是动态生成的。
    private List<Vector3f> tempCenterPositions;

    @Override
    public void setData(BulletData data) {
        super.setData(data);
        tension = data.getAsFloat("tension", tension);
        
        // format: "position1,position1,position1"
        String[] tempES = data.getAsArray("positions");
        if (tempES != null && tempES.length > 0) {
            positions = new ArrayList<Position>(tempES.length);
            tempCenterPositions = new ArrayList<Vector3f>(tempES.length);
            for (String eid : tempES) {
                positions.add(Loader.loadPosition(eid));
                tempCenterPositions.add(new Vector3f());
            }
            waypoints = new ArrayList<Vector3f>(positions.size() + 2); // waypoints包含startPoint和endPoint
        }
        up = data.getAsVector3f("up", Vector3f.UNIT_Y.clone());
        up.normalizeLocal();
    }

    @Override
    public void initialize() {
        super.initialize(); 
        if (waypoints != null) {
            // 添加开始和结束坐标点，以及生成中间坐标点。
            waypoints.add(data.getStartPoint());
            createCenterWappoint(waypoints);
            waypoints.add(data.getEndPoint());
            
            cma.setControlPoints(waypoints);
            cma.setFacePath(facing);
            cma.setCurveTension(tension);
            cma.setTarget(this);
            if (debug) {
                cma.debugPath();
            }
        }
        
    }
    
    /**
     * 获取曲线子弹的spline路径信息,该方法可能返回null,如果没有中间路径点信
     * 息，因为在没有设置中间路径点的时候将直接以直线形子弹进行处理。
     * @return 
     */
    protected Spline getSpline() {
        if (waypoints != null) {
            return cma.getSpline();
        } 
        return null;
    }
    
    /**
     * 获取当前曲线子弹的插值位置，取值为0.0~1.0.
     * 这个插值只对曲线有效，如果是跟踪形子弹，在转为直线追踪后这个值将始终为1.0.
     * @param endPos
     * @return 
     */
    protected float getInterpolation(Vector3f endPos) {
        // 在跟踪状态的时候,即表示curve已经结束
        if (tracing) {
            return 1.0f;
        }
        
        float inter;
        
        if (waypoints == null) {
            // 直线
            TempVars tv = TempVars.get();
            float distance = getWorldTranslation().subtract(data.getStartPoint(), tv.vect1).length();
            float fullLength = endPos.subtract(data.getStartPoint(), tv.vect2).length();
            inter = distance / fullLength;
            tv.release();
        } else {
            // 曲线
            inter = cma.getInterpolation();
        }
        return inter;
    }
    
    @Override
    protected void doUpdatePosition(float tpf, Vector3f endPos) {
        // 如果没有指定路径点，则使用直线方式运行
        if (waypoints == null) {
            super.doUpdatePosition(tpf, endPos);
            return;
        }
        
        if (!tracing) {
            float currentLength = baseSpeed * data.getSpeed() * timeUsed;
            cma.display(currentLength / cma.getTotalLength());
            
            // 判断是否进入tracing,在到达最后一个路径点之前转入跟踪
            if (trace && cma.getCurrentPointIndex() >= waypoints.size() - 2) {
                cma.cleanup();
                tracing = true;
            }
            
        } else {
            // 转为直线跟踪
            super.doUpdatePosition(tpf, endPos);
        }
    }

    @Override
    public void cleanup() {
        if (waypoints != null) {
            waypoints.clear();
        }
        tracing = false;
        if (!cma.isEnd()) {
            cma.cleanup();
        }
        super.cleanup();
    }
    
    private void createCenterWappoint(List<Vector3f> store) {
        if (store == null) 
            return;
        Vector3f startPos = data.getStartPoint();
        Vector3f endPos = data.getEndPoint();

        // 由每一个emmitterShape分别生成一个中间点
        if (positions != null && !positions.isEmpty()) {
            for (int i = 0; i < positions.size(); i++) {
                Vector3f tempWaypoint = tempCenterPositions.get(i);
                // 生成中间点
                positions.get(i).getPoint(tempWaypoint);
                // 转化为子弹坐标系的实际世界位置
                convertWaypointToWorld(startPos, endPos, tempWaypoint);
                // 存储
                store.add(tempWaypoint);
            }
        }
    }
    
    /**
     * 将本地中间路径点转化为Bullet坐标系下的世界坐标点.
     * @param startPos
     * @param endPos
     * @param localWaypoint
     * @return 
     */
    private void convertWaypointToWorld(Vector3f startPos, Vector3f endPos, Vector3f localWaypoint) {
        TempVars tv = TempVars.get();
        Vector3f dir = tv.vect1;
        Quaternion rot = tv.quat1;
        endPos.subtract(startPos, dir).normalizeLocal();
        rot.lookAt(dir, up);
        rot.mult(localWaypoint, localWaypoint);
        localWaypoint.addLocal(startPos);
        tv.release();
    }
}
