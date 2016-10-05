/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.shape;

import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import java.util.List;

/**
 * 多一个getSplinePoint
 * @author huliqing
 */
public class MySpline extends Spline {
    
    /**
     * 获取spline上的点坐标
     * @param distance spline上的长度,从startPoint算起
     * @param store 存放结果
     * @return 
     */
    public Vector3f getSplinePoint(float distance, Vector3f store) {
        float sum = 0;
        int i = 0;
        for (Float len : getSegmentsLength()) {
            if (sum + len >= distance) {
                interpolate((distance - sum) / len, i, store);
                return store;
            }
            sum += len;
            i++;
        }
        List<Vector3f> cps = getControlPoints();
        store.set(cps.get(cps.size() - 1));
        return store;
    }
}
