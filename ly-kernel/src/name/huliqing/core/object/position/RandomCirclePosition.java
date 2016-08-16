/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.position;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import name.huliqing.core.data.PositionData;
import name.huliqing.core.enums.Plane;
import name.huliqing.core.utils.MathUtils;

/**
 * 环形内的随机点，通过设置不同参数可以形成:圆环\圆面\圆筒\圆柱等形式的shape
 * @author huliqing
 * @param <T>
 */
public final class RandomCirclePosition<T extends PositionData> extends AbstractPosition<T> {
    // 默认在xy平面上产生粒子,可指定为xz/yz
    private Plane plane = Plane.xy;

    // 圆形面的内半径和外半径
    private float minRadius = 0;
    private float maxRadius = 1;
    
    // 粒子位置的偏移，默认（0，0，0）
    private Vector3f minOffset;
    private Vector3f maxOffset;

    // 是否按顺序产生粒子位置，按顺序是指绕着圆周顺序产生粒子,默认情况下粒子位
    // 置是在平面内随机产生的,只有order设为true时才按顺序。
    // 圆周的半径由minRadius和maxRadius控制。
    private boolean order;
    // 当order=true时，粒子按圆周顺序产生，orderSize指定了在圆周上多少个位置作为
    // 一个圆周周期，比如orderSize=6,则在圆周上产生6个粒子位置之后，系统会重新
    // 计算一个随机圆周半径（由minRadius和maxRadius决定），然后重新按顺序产生6个粒子
    // 位置,这些粒子位置是在圆周上平均分布的，如orderSize=6,则在圆周上的每60度会
    // 产生一个粒子位置(该值必须大于0)
    private int orderSize = 6;
    // 当order=true时，默认为逆时针顺序产生粒子位置，使用orderInvert来反转顺序
    private boolean orderInvert;
    // 开始弧度
    private float orderStartAngle;
    
    // ---- inner
    private int lastUsedIndex = -1;
    // 粒子位置之间的平均弧度距离(当order=true时)
    private float avgAngle = FastMath.TWO_PI / orderSize;
    // 当前获取到的随机半径，限制在minRadius和maxRadius之内
    private float radius;
    // 是否有指定粒子位置偏移，该属性只是为了提高性能，当没有指定minOffset和maxOffset时
    // 就不需要去计算随机偏移位置
    private boolean hasOffset;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        
        String tempPlane = data.getAsString("plane");
        if (tempPlane != null) {
            plane = Plane.identify(tempPlane);
        }
        minRadius = data.getAsFloat("minRadius", minRadius);
        maxRadius = data.getAsFloat("maxRadius", maxRadius);
        minOffset = data.getAsVector3f("minOffset");
        maxOffset = data.getAsVector3f("maxOffset");
        order = data.getAsBoolean("order", order);
        orderSize = data.getAsInteger("orderSize", orderSize);
        orderInvert = data.getAsBoolean("orderInvert", orderInvert);
        Float orderStartDegree = data.getAsFloat("orderStartDegree"); // 注意是:degree角度,需要转换为弧度
        if (orderStartDegree != null) {
            orderStartAngle = orderStartDegree * FastMath.DEG_TO_RAD;
        }
        
        // ---- inner
        avgAngle = FastMath.TWO_PI / orderSize;
        hasOffset = (minOffset != null || maxOffset != null);
        radius = MathUtils.getRandomFloat(minRadius, maxRadius);
    }

    @Override
    public Vector3f getPoint(Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        
        if (order) {
            // 按圆周环形顺序产生粒子位置
            lastUsedIndex++;
            if (lastUsedIndex >= orderSize) {
                // 当完成一个周期后重置
                lastUsedIndex = 0;
                radius = MathUtils.getRandomFloat(minRadius, maxRadius);
            }
            TempVars tv = TempVars.get();
            store.set(radius, 0, 0);
            float angle = orderStartAngle;
            angle += avgAngle * lastUsedIndex * (orderInvert ? -1 : 1);
            MathUtils.createRotation(angle, Vector3f.UNIT_Z, tv.quat1);
            tv.quat1.mult(store, store);
            
            // 注:toPlane必须放在toOffset之前,否则位置不正确
            toPlane(store, plane, tv);
            toOffset(store, tv);
            
            tv.release();
        } else {
            // 随机取一点on xy
            radius = MathUtils.getRandomFloat(minRadius, maxRadius);
            float randomAngle = MathUtils.getRandomFloat(0, FastMath.TWO_PI);
            TempVars tv = TempVars.get();
            store.set(radius, 0, 0); // 在x轴的0~radius上随机取一点
            Quaternion rot = tv.quat1;
            MathUtils.createRotation(randomAngle, Vector3f.UNIT_Z, rot); // 先在xy平面上产生点。
            rot.mult(store, store);
            
            // 注:toPlane必须放在toOffset之前,否则位置不正确
            toPlane(store, plane, tv);
            toOffset(store, tv);
            
            tv.release();
        }
        
        return store;
    }
    
    // 旋转到指定的平面位置
    private void toPlane(Vector3f point, Plane plane, TempVars tv) {
        if (plane == Plane.xy) {
            // 默认情况下在xy,所以不需要转换
            return;
        } else if (plane == Plane.xz) {
            MathUtils.createRotation(FastMath.HALF_PI, Vector3f.UNIT_X, tv.quat1);
            tv.quat1.mult(point, point);
        } else if (plane == Plane.yz) {
            MathUtils.createRotation(FastMath.HALF_PI, Vector3f.UNIT_Y, tv.quat1);
            tv.quat1.mult(point, point);
        }
    }
    
    // 处理偏移
    private void toOffset(Vector3f point, TempVars tv) {
        if (hasOffset) {
            if (minOffset == null) {
                minOffset = Vector3f.ZERO.clone();
            }
            if (maxOffset == null) {
                maxOffset = Vector3f.ZERO.clone();
            }
            tv.vect5.interpolateLocal(minOffset, maxOffset, FastMath.nextRandomFloat());
            point.addLocal(tv.vect5);
        }
    }
}
