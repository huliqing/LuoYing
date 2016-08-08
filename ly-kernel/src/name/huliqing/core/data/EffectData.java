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
    // 基本变换
    private Vector3f location;
    private Quaternion rotation;
    private Vector3f scale;
    
    // 跟随目标的位置
    private TraceType tracePosition;
    // 跟随目标的旋转
    private TraceType traceRotation;
    // 跟随的位置偏移，必须打开tracePosition功能才有效
    private Vector3f tracePositionOffset;
    // 跟随的旋转偏移，必须打开traceRotation功能才有效
    private Quaternion traceRotationOffset;
    // 跟随位置类型,该偏移会叠加到tracePositionOffset上
    private TracePositionType tracePositionType;
    
    /**
     * 特效所合适的总时间，单位秒
     */
    private float useTime;
    
    // 特效的执行速度,注意:这个参数作为动态配置,不开放到xml中进行配置.
    private float speed = 1.0f;
    
    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
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

    public Vector3f getTracePositionOffset() {
        return tracePositionOffset;
    }

    public void setTracePositionOffset(Vector3f tracePositionOffset) {
        this.tracePositionOffset = tracePositionOffset;
    }

    public Quaternion getTraceRotationOffset() {
        return traceRotationOffset;
    }

    public void setTraceRotationOffset(Quaternion traceRotationOffset) {
        this.traceRotationOffset = traceRotationOffset;
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
