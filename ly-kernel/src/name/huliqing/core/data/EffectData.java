/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.core.object.effect.TracePositionType;
import name.huliqing.core.object.effect.TraceType;

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
    
    private boolean autoDetach;

    /**
     * effect的初始位置(localTranslaction), 格式: "x,y,z", 当effect被添加到某个节点下时可以使用该参数来偏移位置，
     * 当设置这个参数之后，effect在初始化的时候会覆盖掉之前的setLocaleTranslation的设置。
     * 另外：当effect为“跟随”类型时这个参数将作为跟随时的偏移位置进行设置。
     * @return 
     */
    public Vector3f getInitLocation() {
        return initLocation;
    }

    /**
     * effect的初始位置(localTranslaction), 格式: "x,y,z", 当effect被添加到某个节点下时可以使用该参数来偏移位置，
     * 当设置这个参数之后，effect在初始化的时候会覆盖掉之前的setLocaleTranslation的设置。
     * 另外：当effect为“跟随”类型时这个参数将作为跟随时的偏移位置进行设置。
     * @param initLocation 
     */
    public void setInitLocation(Vector3f initLocation) {
        this.initLocation = initLocation;
    }

    /**
     * effect的初始旋转(localRotation), 格式:"x,y,z", 当effect被添加到某个节点下时可以使用该参数来偏移旋转，
     * 当设置这个参数之后，effect在初始化的时候会覆盖掉之前的setLocaleRotation的设置。
     * 另外：当effect为“跟随”类型时这个参数将作为跟随时的偏移旋转进行设置。
     * @return 
     */
    public Quaternion getInitRotation() {
        return initRotation;
    }

    /**
     * effect的初始旋转(localRotation), 格式:"x,y,z", 当effect被添加到某个节点下时可以使用该参数来偏移旋转，
     * 当设置这个参数之后，effect在初始化的时候会覆盖掉之前的setLocaleRotation的设置。
     * 另外：当effect为“跟随”类型时这个参数将作为跟随时的偏移旋转进行设置。
     * @param initRotation 
     */
    public void setInitRotation(Quaternion initRotation) {
        this.initRotation = initRotation;
    }

    /**
     * effect的初始缩放(localScale), 格式, "x,y,z", 当设置这个参数之后，
     * effect在初始化的时候会覆盖掉之前的setLocaleScale的设置。
     * @return 
     */
    public Vector3f getInitScale() {
        return initScale;
    }

    /**
     * effect的初始缩放(localScale), 格式, "x,y,z", 当设置这个参数之后，
     * effect在初始化的时候会覆盖掉之前的setLocaleScale的设置。
     * @param initScale 
     */
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

    /**
     * 如果为true,则特效在结束的时候会自动进行清理并从场景中脱离，不需要手动去移除特效。
     * @param autoDetach 
     */
    public void setAutoDetach(boolean autoDetach) {
        this.autoDetach = autoDetach;
    }

    /**
     * 如果为true,则特效在结束的时候会自动进行清理并从场景中脱离，不需要手动去移除特效。
     * @return 
     */
    public boolean isAutoDetach() {
        return autoDetach;
    }
}
