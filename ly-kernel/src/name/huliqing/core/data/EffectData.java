/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.core.enums.EffectPhase;
import name.huliqing.core.enums.TracePositionType;
import name.huliqing.core.enums.TraceType;

/**
 *
 * @author huliqing
 */
@Serializable
public class EffectData extends ObjectData {
    
    // 定义特效运行时各个阶段的执行时间,注意:各阶段的实际执行时间将与特效的速度有关.
    private float phaseTimeStart;
    private float phaseTimeDisplay;
    private float phaseTimeEnd;
    
    // 基本的效果声音.
    private String sound;
    // 是否让声效以单独实例播放
    private boolean soundInstance;
    
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
    
    // 各个阶段的动画控制器
    private AnimData[] animAll;
    private AnimData[] animStarts;
    private AnimData[] animDisplays;
    private AnimData[] animEnds;
    
    /** 当前效果已经运行的总时间, 该时间包含各个阶段。 */
    private float timeUsed;
    // 当前阶段用时，每个阶段都会重置
    private float phaseTimeUsed;
    // 当前所处的阶段
    private EffectPhase phase = EffectPhase.none;
    
    // 特效的执行速度,注意:这个参数作为动态配置,不开放到xml中进行配置.
    // 这个参数在每次cleanup的时候都会重置为1.0,因为特效需要缓存重用.
    // 如:多个技能使用了当前同一个特效,而这些技能需要的特效播放速度可能都不一样
    // 这就要求各个技能在执行的过程中都要特别设置特效执行速度,以避免被一些技能
    // 改变了执行速度,导致后续所有技能都以不正确的速度播放
    private float speed = 1.0f;
    
    public float getPhaseTimeStart() {
        return phaseTimeStart;
    }

    public void setPhaseTimeStart(float phaseTimeStart) {
        this.phaseTimeStart = phaseTimeStart;
    }

    public float getPhaseTimeDisplay() {
        return phaseTimeDisplay;
    }

    public void setPhaseTimeDisplay(float phaseTimeDisplay) {
        this.phaseTimeDisplay = phaseTimeDisplay;
    }

    public float getPhaseTimeEnd() {
        return phaseTimeEnd;
    }

    public void setPhaseTimeEnd(float phaseTimeEnd) {
        this.phaseTimeEnd = phaseTimeEnd;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public boolean isSoundInstance() {
        return soundInstance;
    }

    public void setSoundInstance(boolean soundInstance) {
        this.soundInstance = soundInstance;
    }

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

    public AnimData[] getAnimAll() {
        return animAll;
    }

    public void setAnimAll(AnimData[] animAll) {
        this.animAll = animAll;
    }

    public AnimData[] getAnimStarts() {
        return animStarts;
    }

    public void setAnimStarts(AnimData[] animStarts) {
        this.animStarts = animStarts;
    }

    public AnimData[] getAnimDisplays() {
        return animDisplays;
    }

    public void setAnimDisplays(AnimData[] animDisplays) {
        this.animDisplays = animDisplays;
    }

    public AnimData[] getAnimEnds() {
        return animEnds;
    }

    public void setAnimEnds(AnimData[] animEnds) {
        this.animEnds = animEnds;
    }

    public float getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(float timeUsed) {
        this.timeUsed = timeUsed;
    }

    public float getPhaseTimeUsed() {
        return phaseTimeUsed;
    }

    public void setPhaseTimeUsed(float phaseTimeUsed) {
        this.phaseTimeUsed = phaseTimeUsed;
    }

    public EffectPhase getPhase() {
        return phase;
    }

    public void setPhase(EffectPhase phase) {
        this.phase = phase;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        if (speed <= 0) {
            speed = 0.0001f;
        }
        this.speed = speed;
    }

    
}
