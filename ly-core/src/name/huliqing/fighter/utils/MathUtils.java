/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.utils;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 用于一些通用的数学算法工具。
 * @author huliqing
 */
public class MathUtils {
    private final static Logger logger = Logger.getLogger(MathUtils.class.getName());
    private final static int[] positive_or_negative = new int[]{-1, 1};
    
    private final static DecimalFormat format = new DecimalFormat("#.##");

    public static String format(float value, String pattern) {
        format.applyPattern(pattern);
        return format.format(value);
    }
    
    /**
     * 黄金分割比率:0.618f
     */
    public final static float GOLD_SEPARATE = 0.618F;

    /**
     * 一个接近0的定义
     */
    public final static float ZERO_NEAR = 0.0001f;
    
    /**
     * 以一个点为中心，在该点的周围(xz平面内)的一个圆环内随机取一点。圆环内外半径由
     * innerRadius和outRadius指定
     * @param center 中心点位置
     * @param innerRadius 距离中心点的内半径
     * @param outRadius 距离中心点的外半径
     * @param store 存放结果
     * @return 
     */
    public static Vector3f getRandomPosition(Vector3f center, float innerRadius, float outRadius, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        // 先以原点为圆心取一个随机点
        store.set(getRandomFloat(innerRadius, outRadius), 0, 0);
        TempVars tv = TempVars.get();
        createRotation(FastMath.nextRandomFloat() * FastMath.TWO_PI, Vector3f.UNIT_Y, tv.quat1);
        tv.quat1.mult(store, store);
        tv.release();
        // 再加上中心点位置
        store.addLocal(center);
        return store;
    }
    
    /**
     * 在x,z平面上随机生成一个点, x的取值范围为 -xExt ~ xExt, z取值类似。
     * 如果except不为null, 则生成的点会避开该指定的点，避开的距离由
     * exceptDistance所指定。 注：exceptDistance指定的值不能太大，避免无限
     * 重复取不到值。
     * @param xExt
     * @param zExt
     * @param except
     * @param exceptDistance
     * @param store 存放结果值
     * @return 
     */
    public static Vector3f getRandomPosition(float xExt, float zExt, Vector3f except, float exceptDistance, Vector3f store) {
        return getRandomPosition(xExt, zExt, except, exceptDistance, store, 0);
    }
    
    private static Vector3f getRandomPosition(float xExt, float zExt, Vector3f except, float exceptDistance, Vector3f store, int count) {
        count += 1;
//        logger.log(Level.INFO, "getRandomPosition calculate times={0}", count);
        if (store == null) {
            store = new Vector3f();
        }
        store.setX(FastMath.nextRandomFloat() * xExt * 2 - xExt);
        store.setZ(FastMath.nextRandomFloat() * zExt * 2 - zExt);
        if (except == null) {
            return store;
        }
        if (store.distance(except) < exceptDistance) {
            // 如果距离太近，则重新获取。
            return getRandomPosition(xExt, zExt, except, exceptDistance, store, count);
        }
        return store;
    }
    
    /**
     * 在x,z平面上随机生成一个点, x的取值范围为 -xExt ~ xExt, z取值类似。
     * @param xExt
     * @param zExt
     * @param store
     * @return 
     */
    public static Vector3f getRandomPosition(float xExt, float zExt, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        store.setX((FastMath.nextRandomFloat() * 2 - 1) * xExt);
        store.setZ((FastMath.nextRandomFloat() * 2 - 1) * zExt);
        return store;
    }
    
    
    /**
     * 比较两个float值的大小是否相等.
     * @param value1
     * @param value2
     * @return 
     */
    public static boolean compareFloat(float value1, float value2) {
        return Math.abs(value1 - value2) < FastMath.ZERO_TOLERANCE;
    }
    
    /**
     * 创建旋转元数
     * @param angle 弧度
     * @param axis 旋转轴
     * @param store 存放结果
     * @return 
     */
    public static Quaternion createRotation(float angle, Vector3f axis, Quaternion store) {
        if (store == null) {
            store = new Quaternion();
        }
        TempVars temp = TempVars.get();
        temp.quat1.fromAngleAxis(angle, axis);
        store.set(temp.quat1);
        temp.release();
        return store;
    }
    
    /**
     * 以一个点(center)为圆心，在它的x,z平面的半径(radius)范围内随机取一个点。
     * @param center
     * @param radius
     * @return 
     */
    public static Vector3f getRandomPosition(Vector3f center, float radius, Vector3f store) {
        return getRandomPosition(center, 0, radius, store);
    }
    
//    /**
//     * 以一个点(center)为圆心，在它的x,z平面的半径(minRadius和maxRadius)
//     * 的圆环范围内随机取一个点。
//     * @param center
//     * @param radius
//     * @return 
//     */
//    public static Vector3f getRandomPosition(Vector3f center, float minRadius, float maxRadius, Vector3f store) {
//        if (store == null) {
//            store = new Vector3f();
//        }
//        float randomRadius = FastMath.nextRandomFloat() * (maxRadius - minRadius) + minRadius;
//        store.set(center).addLocal(randomRadius, 0, 0).subtractLocal(center);
//        TempVars temp = TempVars.get();
//        temp.quat1.fromAngleAxis(FastMath.nextRandomFloat() * FastMath.TWO_PI, Vector3f.UNIT_Y);
//        temp.quat1.multLocal(store);
//        store.addLocal(center);
//        temp.release();
//        return store;
//    }
    
    /**
     * 获取随机float值。
     * @param min
     * @param max
     * @return 
     */
    public static float getRandomFloat(float min, float max) {
        return FastMath.nextRandomFloat() * (max - min) + min;
    }
    
    /**
     * 将目标旋一定的弧度。
     * @param target 目标向量
     * @param angle 旋转弧度
     * @param axis 旋转轴
     * @param store 
     * @return 
     */
    public static Vector3f rotate(Vector3f target, float angle, Vector3f axis, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        TempVars tv = TempVars.get();
        tv.quat1.fromAngleAxis(angle, axis);
        tv.quat1.mult(target, store);
        tv.release();
        return store;
    }
    
    /**
     * 随机取数-1或1, 该方法要不返回-1，要不返回1(randomPositiveOrNegative)
     * @return 
     */
    public static int randomPON() {
        return positive_or_negative[FastMath.nextRandomInt(0, 1)];
    }
    
    /**
     * 比较两个向量的近似度，如果两个向量的各个相对应元素的差值在指定精度之内
     * （precision）之内（包含），则视为两个向量相似并返回true.
     * 如果有任何一个相对应元素之间的差值大于指定的精度则视为不相似。
     * @param v1
     * @param v2
     * @param precision
     * @return 
     */
    public static boolean isSimilar(Vector3f v1, Vector3f v2, float precision) {
        if (abs(v1.x - v2.x) > precision 
                || abs(v1.y - v2.y) > precision 
                || abs(v1.z - v2.z) > precision) {
            return false;
        }
        return true;
    }
    
    /**
     * 比较两个四元数的近似度，如果两个四元数的各个相对应元素的差值在指定精度之内
     * （precision）之内（包含），则视为两个四元数相似并返回true.
     * 如果有任何一个相对应元素之间的差值大于指定的精度则视为不相似。
     * @param q1
     * @param q2
     * @param precision
     * @return 
     */
    public static boolean isSimilar(Quaternion q1, Quaternion q2, float precision) {
        if (abs(q1.getX() - q2.getX()) > precision 
                || abs(q1.getY() - q2.getY()) > precision 
                || abs(q1.getZ() - q2.getZ()) > precision 
                || abs(q1.getW() - q2.getW()) > precision 
                ) {
            return false;
        }
        return true;
    }
    
    /**
     * 取绝对值
     * @param value
     * @return 
     */
    public static float abs(float value) {
        if (value < 0) {
            return -value;
        }
        return value;
    }
    
    /**
     * 创建一个随机旋转，该旋转随机生成一个旋转轴。并随机在minAngle和maxAngle
     * 中取一个旋转弧度。
     * @param minAngle 最小弧度, 不能为负值
     * @param maxAngle 最大弧度，必须大于或等于minAngle
     * @param store 存放结果
     * @return 
     */
    public static Quaternion createRandomRotation(float minAngle, float maxAngle, Quaternion store) {
        // remove20160408
//        if (minAngle < 0 || maxAngle < minAngle) {
//            throw new IllegalArgumentException("minAngle could not less than zero, and maxAngle must more than minAngle.minAngle=" 
//                    + minAngle + ", maxAngle=" + maxAngle);
//        } 
//        if (store == null ) {
//            store = new Quaternion();
//        }
//        float angle = FastMath.nextRandomFloat() * (maxAngle - minAngle) + minAngle;
//        TempVars tv = TempVars.get();
//        Vector3f axis = tv.vect1.set(
//                FastMath.nextRandomFloat() + 0.01f
//                ,FastMath.nextRandomFloat() + 0.01f
//                ,FastMath.nextRandomFloat() + 0.01f
//                ).normalizeLocal();
//        store.fromAngleAxis(angle, axis);
//        tv.release();
//        return store;
        
        return createRandomRotationAxis(minAngle, maxAngle, null, store);
    }
    
    /**
     * 创建一个随机旋转，但是使用指定的旋转轴
     * @param minAngle 最小弧度, 不能为负值
     * @param maxAngle 最大弧度，必须大于或等于minAngle
     * @param axis 旋转轴,如果为null则随机生成一个旋转轴
     * @param store 存放结果
     * @return 
     */
    public static Quaternion createRandomRotationAxis(float minAngle, float maxAngle, Vector3f axis, Quaternion store) {
        if (minAngle < 0 || maxAngle < minAngle) {
            throw new IllegalArgumentException("minAngle could not less than zero, and maxAngle must more than minAngle.minAngle=" 
                    + minAngle + ", maxAngle=" + maxAngle);
        } 
        if (store == null ) {
            store = new Quaternion();
        }
        if (axis == null) {
            axis = new Vector3f(FastMath.nextRandomFloat() + 0.01f
                ,FastMath.nextRandomFloat() + 0.01f
                ,FastMath.nextRandomFloat() + 0.01f);
        }
        axis.normalizeLocal();
        
        float angle = FastMath.nextRandomFloat() * (maxAngle - minAngle) + minAngle;
        store.fromAngleAxis(angle, axis);
        return store;
    }
    
    public static int clamp(int input, int min, int max) {
        return (input < min) ? min : (input > max) ? max : input;
    }
    
    public static float clamp(float input, float min, float max) {
        return (input < min) ? min : (input > max) ? max : input;
    }
    
    /**
     * 获取spline上某一个点的位置
     * @param spline spline
     * @param distance distance 为从spline上开始点到指定位置的距离
     * @param store 存储结果
     * @return 
     */
    public static Vector3f getSplinePoint(Spline spline, float distance, Vector3f store) {
        float sum = 0;
        int i = 0;
        for (Float len : spline.getSegmentsLength()) {
            if (sum + len >= distance) {
                spline.interpolate((distance - sum) / len, i, store);
                return store;
            }
            sum += len;
            i++;
        }
        List<Vector3f> cps = spline.getControlPoints();
        store.set(cps.get(cps.size() - 1));
        return store;
    }
}
