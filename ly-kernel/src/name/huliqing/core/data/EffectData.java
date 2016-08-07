/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.core.enums.TracePositionType;
import name.huliqing.core.enums.TraceType;

/**
 *
 * @author huliqing
 */
@Serializable
public class EffectData extends ObjectData {
    
    // 初始变换
    private Vector3f initLocation;
    private Quaternion initRotation;
    private Vector3f initScale;
    
    // 跟随目标的位置
    private TraceType tracePosition;
    // 跟随目标的旋转
    private TraceType traceRotation;
    
    private TracePositionType tracePositionType;
    
    /**
     * 特效所合适的总时间，单位秒
     */
    private float useTime;
    
    // 特效的执行速度,注意:这个参数作为动态配置,不开放到xml中进行配置.
    private float speed = 1.0f;

    public Vector3f getInitLocation() {
        return initLocation;
    }

    /**
     * 设置特效的初始位置，这个位置同时也是跟随时的偏移位置。
     * @param initLocation 
     */
    public void setInitLocation(Vector3f initLocation) {
        this.initLocation = initLocation;
    }

    public Quaternion getInitRotation() {
        return initRotation;
    }

    /**
     * 设置特效的初始旋转，这个旋转也是跟随效果的偏移旋转。
     * @param initRotation 
     */
    public void setInitRotation(Quaternion initRotation) {
        this.initRotation = initRotation;
    }

    public Vector3f getInitScale() {
        return initScale;
    }

    public void setInitScale(Vector3f initScale) {
        this.initScale = initScale;
    }

    public TraceType getTracePosition() {
        return tracePosition;
    }

    public void setTracePosition(TraceType tracePosition) {
        this.tracePosition = tracePosition;
    }

    public TraceType getTraceRotation() {
        return traceRotation;
    }

    public void setTraceRotation(TraceType traceRotation) {
        this.traceRotation = traceRotation;
    }

    public TracePositionType getTracePositionType() {
        return tracePositionType;
    }

    public void setTracePositionType(TracePositionType tracePositionType) {
        this.tracePositionType = tracePositionType;
    }
    
    /**
     * 获取特效的运行速度,默认为1.0
     * @return 
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * 设置特效的执行速度，注意：这个参数不要开放到xml配置中去。这是作为在程序运行时动态设置的参数,最小值为0.0001f
     * @param speed 
     */
    public void setSpeed(float speed) {
        if (speed <= 0) {
            speed = 0.0001f;
        }
        this.speed = speed;
    }

    public float getUseTime() {
        return useTime;
    }

    public void setUseTime(float useTime) {
        this.useTime = useTime;
    }

    
}
