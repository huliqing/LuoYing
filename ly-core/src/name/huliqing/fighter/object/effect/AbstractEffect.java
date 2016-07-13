/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.effect;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.data.AnimData;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.enums.EffectPhase;
import name.huliqing.fighter.enums.TracePositionType;
import name.huliqing.fighter.enums.TraceType;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.manager.SoundManager;
import name.huliqing.fighter.object.anim.Anim;
import name.huliqing.fighter.utils.GeometryUtils;

/**
 * <b>A</b>.特效，特效的生命周期阶段： 
 * 1.start(loop)阶段 - 2.display(loop)阶段 - 3.End(loop)阶段 - 4.cleanup.
 * 当display阶段的时间被设置为小于0时，效果将一直存在，这时End阶段将不会执行，
 * 除非效果为跟踪类型（trace不为null), 并且所跟踪的目标已经脱离场景时，display才
 * 会结束并执行后续阶段。<br />
 * <b>B</b>.设置start和end阶段的时间为小于或等于0时，start和end阶段将被跳过。<br />
 * <b>C</b>.设置display阶段的时间为0时，display阶段也会被跳过（设为小于0时会一直存在）。<br />
 * <b>D</b>.当效果执行完成时不管是否被打断或正常结束，效果将自动从场景中移除。<br />
 * 注：start和end阶段是不可以循环的，只有display阶段可以循环，即当display时间小于0时.
 * 
 * @author huliqing
 * @param <T>
 * @since v1.2 20150419
 */
public abstract class AbstractEffect<T extends EffectData> extends Node implements Effect<T> {
//    private final static Logger LOG = Logger.getLogger(AbstractEffect.class.getName());
    
    protected T data;
    
    // 效果运行时侦听器，在效果结束时要清理掉，以便为下次的运行作准备
    protected List<Listener> listeners;
    
    // 跟踪的目标对象,必须必须有一个目标对象才有可能跟随
    protected Spatial traceObject;
    
    // 开始、展示、结束三个阶段的动画控制。
    protected Anim[] animAll; // 全阶段的动画控制
    protected Anim[] animStarts;
    protected Anim[] animDisplays;
    protected Anim[] animEnds;
    
    // ---- 内部
    // 本地根作点, 为了隔离Effect自身的变换和Anim所执行的动画变换，必须提供一个本地根作点
    // anim所执行的动画变换都依赖该localRoot作点。也就是不影响AbstractEffect本身
    protected final Node localRoot = new Node("Effect_localRoot");
    
    // 是否开始运行
    protected boolean started;
    // 是否已经初始化,几个阶段分别有初始化init
    protected boolean initDisplay;
    protected boolean initEnd;
    
    // 声音是否已经播放
    private boolean soundPlayed;
    
    // ---- inner
    private boolean tempAlwayTrace;
    
    public AbstractEffect() {
        super("AbstractEffect");
        // 首先添加本地根作点，确保后续添加的所有子作点都在该localRoot下。
        // 并且所有Animation也都是作用于节点”localRoot“,这样animation的动画效果不会影响特效的跟随
        attachChild(localRoot);
    }
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public final void start() {
        if (started) {
            return;
        }
        started = true;
        data.setPhase(EffectPhase.start);
        
        // 1.初始化变换
        if (data.getLocation() != null) {
            setLocalTranslation(data.getLocation());
        }
        if (data.getRotation() != null) {
            setLocalRotation(data.getRotation());
        }
        if (data.getScale() != null) {
            setLocalScale(data.getScale());
        }
        
        // remove20160712,不再依赖于isInScene
        // 2.初始化跟随
//        if (traceObject != null && Common.getPlayState().isInScene(traceObject)) {
//            if (data.getTracePosition() == TraceType.once 
//                    || data.getTracePosition() == TraceType.always) {
//                doUpdateTracePosition();
//            }
//            if (data.getTraceRotation() == TraceType.once 
//                    || data.getTraceRotation() == TraceType.always) {
//                doUpdateTraceRotation();
//            }
//        }

        // 2.初始化跟随
        if (traceObject != null) {
            if (data.getTracePosition() == TraceType.once 
                    || data.getTracePosition() == TraceType.always) {
                doUpdateTracePosition();
            }
            if (data.getTraceRotation() == TraceType.once 
                    || data.getTraceRotation() == TraceType.always) {
                doUpdateTraceRotation();
            }
        }
        
        // 3.初始化animStates和animAll阶段的动画，需要放在“变换初始化”的后面,
        // 因为部分动画控需要先确定特效的位置。
        checkLoadAnims(); // 检查动画载入
        if (animStarts != null) {
            for (Anim anim : animStarts) {
                anim.setTarget(localRoot);
                anim.start();
            }
        }
        
        // 全阶段的动画初始化
        if (animAll != null) {
            for (Anim anim : animAll) {
                anim.setTarget(localRoot);
                anim.start();
            }
        }
        
        // 方便在update中判断
        tempAlwayTrace = traceObject != null && (data.getTracePosition() == TraceType.always || data.getTraceRotation() == TraceType.always);
        
        // 4.子类逻辑初始化
        doInit();
    }
    
    private void checkLoadAnims() {
        if (data.getAnimStarts() != null && animStarts == null) {
            animStarts = loadAnims(data.getAnimStarts());
        }
        if (data.getAnimDisplays() != null && animDisplays == null) {
            animDisplays = loadAnims(data.getAnimDisplays());
        }
        if (data.getAnimEnds() != null && animEnds == null) {
            animEnds = loadAnims(data.getAnimEnds());
        }
        if (data.getAnimAll() != null && animAll == null) {
            animAll = loadAnims(data.getAnimAll());
        }
    }
    
    private Anim[] loadAnims(AnimData[] animDatas) {
        Anim[] anims = new Anim[animDatas.length];
        for (int i = 0; i < animDatas.length; i++) {
            anims[i] = Loader.loadAnimation(animDatas[i]);
        }
        return anims;
    }
    
    @Override
    public void update(float tpf) {
        if (!started) {
            return;
        }
        
        // remove20160519以后不要再这样依赖于被跟踪物体是否在场景中来判断和结束效果了，这种依赖容易
        // 产生bug,什么时候结束效果将由外部应用决定,可调用jumpToEnd来结束效果，也可以调用cleanup直接结束
//        // 如果跟踪的目标已经离开场景，则效果直接进入end阶段,这对那些“子弹”类
//        // 效果特别重要，当目标子弹离开场景之后要把特效关了
//        if (traceObject != null && !Common.getPlayState().isInScene(traceObject)) {
//            changePhase(EffectPhase.end);
//        }

        data.setTimeUsed(data.getTimeUsed() + tpf);
        data.setPhaseTimeUsed(data.getPhaseTimeUsed() + tpf);
        
        // 如果是持续跟随的情况则需要不停更新位置
        if (tempAlwayTrace) {
            
            // remove20160712,不再依赖于traceObject是否存在于场景中
//            if (Common.getPlayState().isInScene(traceObject)) {
//                if (data.getTracePosition() == TraceType.always) {
//                    doUpdateTracePosition();
//                }
//                if (data.getTraceRotation() == TraceType.always) {
//                    doUpdateTraceRotation();
//                }
//            }

            if (data.getTracePosition() == TraceType.always) {
                doUpdateTracePosition();
            }
            if (data.getTraceRotation() == TraceType.always) {
                doUpdateTraceRotation();
            }
        }
        
        // 执行侦听器
        if (listeners != null && !listeners.isEmpty()) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onEffectPlay(this);
            }
        }
        
        // 检测并播放声音
        checkPlaySound(data.getTimeUsed());
        
        updatePhaseAllAnim(tpf);
        updatePhaseAll(tpf);
        
        // 6.执行各个阶段更新，注：如果阶段时间设置为0，则完全不执行，如果大于0，则要
        // 确保至少执行一次,并且要确保执行到phaseTime>目标阶段的时间
        boolean end = false;
        EffectPhase phase = data.getPhase();
        float speed = data.getSpeed();
        float phaseTime = data.getPhaseTimeUsed();
        
        if (phase == EffectPhase.start) {
            if (data.getPhaseTimeStart() <= 0) { // 如果时间设置<=0 则完全不执行。
                changePhase(EffectPhase.display);
            } else {
                // 如果时间>0, 则确保至少执行一次。
                float truePhaseTimeStart = data.getPhaseTimeStart() / speed;
                float inter = phaseTime / truePhaseTimeStart; // 开始阶段的时间插值
                updatePhaseStartAnim(tpf, inter); // 自身的动画更新
                updatePhaseStart(tpf, inter);     // 子类的动画更新
                if (phaseTime > truePhaseTimeStart) {
                    changePhase(EffectPhase.display);
                }
            }
        } else if (phase == EffectPhase.display) {
            if (data.getPhaseTimeDisplay() <= 0) {
                // 如果display阶段时间为0，则直接跳过，完全不执行
                changePhase(EffectPhase.end);
            } else {
                // 判断并初始化Display
                if (!initDisplay) {
                    doInitDisplay();
                    initDisplay = true;
                }
                // 执行display阶段逻辑
                // 自身的动画更新
                updatePhaseDisplayAnim(tpf); 
                // 子类的动画更新
                updatePhaseDisplay(tpf, phaseTime);
                // 如果phaseTimeDisplay > 0,并且执行时间已过，则跳到end.(如果时间设置小于0则一直执行，这时永远不会跳到end)
                float truePhaseTimeDisplay = data.getPhaseTimeDisplay() / speed;
                if (phaseTime > truePhaseTimeDisplay) {
                    changePhase(EffectPhase.end);
                }    
            }
        } else if (phase == EffectPhase.end) {
            // 与start阶段相同，如果时间设置小于等于0则直接跳过,但是需要确认，可能部分子类作了特殊处理，不会根据时间来结束效果。
            if (data.getPhaseTimeEnd() <= 0) {
                end = confirmEnd();
            } else {
//                logger.log(Level.INFO, "==== Do update phaseEnd, phaseTime={0}, phaseTimeEnd={1}, interpolation={2}"
//                        , new Object[] {phaseTime, phaseTimeEnd, phaseTime / phaseTimeEnd});
                if (!initEnd) {
                    doInitEnd();
                    initEnd = true;
                }
                float truePhaseTimeEnd = data.getPhaseTimeEnd() / speed;
                float inter = phaseTime / truePhaseTimeEnd; // 结束阶段的时间插值
                updatePhaseEndAnim(tpf, inter); // 自身的动画更新
                updatePhaseEnd(tpf, inter);     // 子类的动画更新。
                if (phaseTime > truePhaseTimeEnd) {
                    end = confirmEnd();
                }
//                logger.log(Level.INFO, "==phaseTimeEnd={0}, speed={1}, truePhaseTimeEnd={2}, inter={3}"
//                        , new Object[] {phaseTimeEnd, speed, truePhaseTimeEnd, inter});
            }
        }
        
        // remove20160519以后不要再这样依赖于被跟踪物体是否在场景中来判断和结束效果了，这种依赖容易
        // 产生bug,什么时候结束效果将由外部应用决定,可调用jumpToEnd来结束效果，也可以调用cleanup直接结束
//        // 7.两个条件决定效果是否应该结束
//        // A.时间执行完毕(可由子类干涉)
//        // B.被跟踪对象脱离场景(效果必须是跟踪类型)
//        if (end || checkTraceEnd()) {
//            if (listeners != null) {
//                for (int i = 0; i < listeners.size(); i++) {
//                    listeners.get(i).onEffectEnd(this);
//                }
//            }
//            cleanup();
//        }
        
        // 结束效果
        if (end) {
            if (listeners != null && !listeners.isEmpty()) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onEffectEnd(this);
                }
            }
            cleanup();
        }
    }

    @Override
    public void cleanup() {
        started = false;
        initDisplay = false;
        initEnd = false;
        tempAlwayTrace = false;
        
        data.setPhaseTimeUsed(0);
        data.setPhase(EffectPhase.none);
        data.setTimeUsed(0);
        
        soundPlayed = false;
        // 重置速度,这很重要,因为效果是可重用的，每一个使用“效果”的物体可能
        // 需要的速度都不同，所以使用效果前确保设置自己须要的速度，或者保持默认
        // 的1.0
        data.setSpeed(1.0f);
        
        if (animStarts != null) {
            for (Anim anim : animStarts) {
                if (!anim.isEnd()) {
                    anim.cleanup();
                }
            }
        }
        if (animAll != null) {
            for (Anim anim : animAll) {
                if (!anim.isEnd()) {
                    anim.cleanup();
                }
            }
        }
        if (animDisplays != null) {
            for (Anim anim : animDisplays) {
                if (!anim.isEnd()) {
                    anim.cleanup();
                }
            }
        }
        if (animEnds != null) {
            for (Anim anim : animEnds) {
                if (!anim.isEnd()) {
                    anim.cleanup();
                }
            }
        }
        
        // 清理效果侦听器
        if (listeners != null) {
            listeners.clear();
        }
        
        // 清除被跟踪的对象。
        this.traceObject = null;
        
        // 当效果结束时，将效果从场景中移除。
        removeFromParent();
    }
    
    protected final void changePhase(EffectPhase phase) {
        data.setPhaseTimeUsed(0);
        data.setPhase(phase);
    }

    @Override
    public final boolean isEnd() {
        return !started;
    }
    
    @Override
    public final EffectPhase getCurrentPhase() {
        return data.getPhase();
    }

    @Override
    public void jumpToEnd() {
        if (!started)
            return;
        if (data.getPhase() == EffectPhase.end) 
            return;
        changePhase(EffectPhase.end);
    }

    @Override
    public final Spatial getTraceObject() {
        return traceObject;
    }

    @Override
    public final void setTraceObject(Spatial target) {
        this.traceObject = target;
    }

    @Override
    public final void addListener(Listener listener) {
        if (listeners == null) {
            listeners = new ArrayList<Listener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public final boolean removeListener(Listener listener) {
        if (listeners == null) {
            return false;
        }
        return listeners.remove(listener);
    }
    
    @Override
    public Spatial getDisplay() {
        return this;
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        update(tpf);
        super.updateLogicalState(tpf);
    }
    
    /**
     * 确认是否结束，默认情况下效果会根据设置的时间来结束当前效果，如果子
     * 类需要阻止该效果的结束，则可根据情况返回false, 当该方法被调用时，说明
     * 当前效果已经在时间上结束了。
     * @param end
     * @return 
     */
    protected boolean confirmEnd() {
        return true;
    }
    
    /**
     * 检查是否播放声效。
     * @param timeUsed 效果已经执行的时间。
     */
    protected void checkPlaySound(float timeUsed) {
        if (data.getSound() != null && !soundPlayed) {
            if (data.isSoundInstance()) {
                SoundManager.getInstance().playSoundInstance(data.getSound(), getWorldTranslation());
            } else {
                SoundManager.getInstance().playSound(data.getSound(), getWorldTranslation());
            }
            soundPlayed = true;
        }
    }
    
    private void doUpdateTracePosition() {
        // add type offset
        Vector3f pos = getLocalTranslation();
        TracePositionType tpt = data.getTracePositionType();
        if (tpt == TracePositionType.origin) {
            pos.set(traceObject.getWorldTranslation());
        } else if (tpt == TracePositionType.origin_bound_center) {
            pos.set(traceObject.getWorldTranslation());
            BoundingVolume bv = traceObject.getWorldBound();
            if (bv != null) {
                pos.setY(bv.getCenter().getY());
            }
        } else if (tpt == TracePositionType.origin_bound_top) {
            GeometryUtils.getBoundTopPosition(traceObject, pos);
            pos.setX(traceObject.getWorldTranslation().x);
            pos.setZ(traceObject.getWorldTranslation().z);
        } else if (tpt == TracePositionType.bound_center) {
            pos.set(traceObject.getWorldBound().getCenter());
        } else if (tpt == TracePositionType.bound_top) {
            GeometryUtils.getBoundTopPosition(traceObject, pos);
        }
        // 注：tracePositionOffset是以被跟随的目标对象的本地坐标为基准的,
        // 所以必须mult上目标对象的旋转
        if (data.getTracePositionOffset() != null) {
            TempVars tv = TempVars.get();
            traceObject.getWorldRotation().mult(data.getTracePositionOffset(), tv.vect2);
            pos.addLocal(tv.vect2);
            tv.release();
        }
        setLocalTranslation(pos);
//        LOG.log(Level.INFO, "AbstractEffect doUpdateTracePosition, pos={0}", new Object[] {pos});
    }
    
    private void doUpdateTraceRotation() {
        Quaternion rot = getLocalRotation();
        rot.set(traceObject.getWorldRotation());
        if (data.getTraceRotationOffset() != null) {
            rot.multLocal(data.getTraceRotationOffset());
        }
        setLocalRotation(rot);
    } 
    
    /**
     * 子类初始化
     */
    protected void doInit() {
        // 子类初始化
    }
    
    private void doInitDisplay() {
        if (animDisplays != null) {
            for (Anim anim : animDisplays) {
                anim.setTarget(localRoot);
                anim.start();
            }
        }
    }
    
    private void doInitEnd() {
        if (animEnds != null) {
            for (Anim anim : animEnds) {
                anim.setTarget(localRoot);
                anim.start();
            }
        }
    }
    
    private void updatePhaseStartAnim(float tpf, float inter) {
        if (animStarts != null) {
            for (Anim anim : animStarts) {
                anim.display(inter); // 开始和结束阶段的动画使用同步插值，展示阶段就不需要
            }
        }
    }

    private void updatePhaseAllAnim(float tpf) {
        if (animAll != null) {
            for (Anim anim : animAll) {
                anim.update(tpf);
            }
        }
    }
    
    private void updatePhaseDisplayAnim(float tpf) {
        if (animDisplays != null) {
            for (Anim anim : animDisplays) {
                anim.update(tpf);
            }
        }
    }

    private void updatePhaseEndAnim(float tpf, float inter) {
        if (animEnds != null) {
            for (Anim anim : animEnds) {
                anim.display(inter);
            }
        }
    }
    
    /**
     * 全阶段更新, 该方法在start,display,end阶段都会执行到。重写这个方法来实现自定义逻辑.
     * @param tpf 
     */
    protected void updatePhaseAll(float tpf) {}
    
    /**
     * 更新效果的起始阶段逻辑,这个方法只有在开始阶段时间不为0的时候才会执行。
     * 重写这个方法来实现自定义逻辑.
     * @param tpf 
     * @param interpolation 当前阶段的时间插值，取值[0.0, 1.0]
     */
    protected void updatePhaseStart(float tpf, float interpolation) {}
    
    /**
     * 更新效果显示阶段的逻辑,重写这个方法来实现自定义逻辑.
     * @param tpf
     * @param phaseTime 当前阶段的已经使用的时间,注意：并非时间插值，因为有些
     * 效果的存在不会受时间限制，当displayPhaseTime设置为小于0的值时，该效果会
     * 一直存在，所以使用插值就没有意义。
     */
    protected void updatePhaseDisplay(float tpf, float phaseTime) {};
    
    /**
     * 更新效果的结束阶段逻辑,重写这个方法来实现自定义逻辑.
     * @param tpf
     * @param interpolation 当前阶段的时间插值，取值[0.0, 1.0]
     */
    protected void updatePhaseEnd(float tpf, float interpolation) {};
   

}
