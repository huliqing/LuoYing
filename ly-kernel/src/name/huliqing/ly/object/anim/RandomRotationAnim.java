/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.anim;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.ly.data.AnimData;
import name.huliqing.ly.utils.MathUtils;

/**
 * 随机旋转动画.
 * @author huliqing
 */
public final class RandomRotationAnim extends AbstractAnim<Spatial> {
    
    // 开始旋转
    private final Quaternion startRotation = new Quaternion();
    // 结束旋转，每次旋转完成后都会进行随机改变
    private Quaternion endRotation = new Quaternion();
    // 随机旋转的最小和最大弧度度
    private float minAngle = 0;
    private float maxAngle = FastMath.TWO_PI;
    // 旋转轴，如果没有指定这个旋转轴，则使用随机旋转轴
    private Vector3f axis;

    @Override
    public void setData(AnimData data) {
        super.setData(data);
        
        // 在xml的配置上是角度，需要转为弧度
        minAngle = data.getAsFloat("minDegree", 0) * FastMath.DEG_TO_RAD;
        maxAngle = data.getAsFloat("maxDegree", 0) * FastMath.DEG_TO_RAD;
        // 旋转轴
        axis = data.getAsVector3f("axis");
    }
    
    @Override
    protected void doInit() {
        // 初始化旋转及生成随机结束旋转
        startRotation.set(target.getLocalRotation());
        endRotation = MathUtils.createRandomRotationAxis(minAngle, maxAngle, axis, endRotation);
    }

    @Override
    protected void doAnimation(float interpolation) {
        TempVars tv = TempVars.get();
        Quaternion qua = tv.quat1.set(startRotation);
        qua.slerp(endRotation, interpolation);
        target.setLocalRotation(qua);
        tv.release();
        if (interpolation >= 1) {
            if (loop == Loop.loop || loop == Loop.cycle) {
                // 当结束一次循环之后重新产生随机旋转
                startRotation.set(target.getLocalRotation());
                endRotation = MathUtils.createRandomRotationAxis(minAngle, maxAngle, axis, endRotation);
            }
        }
    }
    
}
