package name.huliqing.core.object.effect;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.effect;
//
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.queue.RenderQueue;
//import com.jme3.renderer.queue.RenderQueue.ShadowMode;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import com.jme3.util.TempVars;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.fighter.Common;
//import name.huliqing.fighter.data.EffectData;
//import name.huliqing.fighter.loader.Loader;
//import name.huliqing.fighter.manager.SoundManager;
//import name.huliqing.fighter.object.anim.Anim;
//import name.huliqing.fighter.utils.GeometryUtils;
//
///**
// * <b>A</b>.特效，特效的生命周期阶段： 
// * 1.start(loop)阶段 - 2.display(loop)阶段 - 3.End(loop)阶段 - 4.cleanup.
// * 当display阶段的时间被设置为小于0时，效果将一直存在，这时End阶段将不会执行，
// * 除非效果为跟踪类型（trace不为null), 并且所跟踪的目标已经脱离场景时，display才
// * 会结束并执行后续阶段。<br />
// * <b>B</b>.设置start和end阶段的时间为小于或等于0时，start和end阶段将被跳过。<br />
// * <b>C</b>.设置display阶段的时间为0时，display阶段也会被跳过（设为小于0时会一直存在）。<br />
// * <b>D</b>.当效果执行完成时不管是否被打断或正常结束，效果将自动从场景中移除。<br />
// * 注：start和end阶段是不可以循环的，只有display阶段可以循环，即当display时间小于0时.
// * 
// * @author huliqing
// * @since v1.2 20150419
// */
//public abstract class AbstractEffect extends Node implements Effect {
//    private final static Logger LOG = Logger.getLogger(AbstractEffect.class.getName());
//    
//    /**
//     * 特效的跟随位置
//     */
//    public enum TracePositionType {
//        /** 以原点作为跟随位置(默认方式) */
//        origin,
//        
//        /** 以原点但是y值使用的是包围盒顶部最高位置点处的y值作为跟随位置 */
//        origin_bound_top,
//        
//        /** 以包围盒的中心点作为跟随位置 */
//        bound_center,
//        
//        /** 以包围盒的上顶点为跟随位置，对于box包围盒是其上顶面的中心点，  */
//        bound_top;
//        
//        public static TracePositionType identify(String name) {
//            TracePositionType[] types = values();
//            for (TracePositionType type : types) {
//                if (type.name().equals(name)) {
//                    return type;
//                }
//            }
//            throw new UnsupportedOperationException("Unknow TraceOffsetType:" + name);
//        }
//    }
//    protected EffectData data;
//    
//    // 效果运行时侦听器，在效果结束时要清理掉，以便为下次的运行作准备
//    protected List<Listener> listeners;
//    
//    // 定义特效运行时各个阶段的执行时间,注意:各阶段的实际执行时间将与特效的速度有关.
//    protected float phaseTimeStart = 0;
//    protected float phaseTimeDisplay = 3;
//    protected float phaseTimeEnd = 0;
//    
//    // 基本的效果声音.
//    protected String sound;
////    // 声音开始播放的时间,单位秒,默认0，即效果运行时一开始就播放声音。
////    protected float soundOffset;
//    // 是否让声效以单独实例播放
//    protected boolean soundInstance;
//    
//    // 跟踪的目标对象,必须必须有一个目标对象才有可能跟随
//    protected Spatial traceObject;
//    // 跟随目标的位置
//    private boolean tracePosition = true;
//    // 跟随目标的旋转
//    private boolean traceRotation = true;
//    // 跟随的位置偏移，必须打开tracePosition功能才有效
//    private Vector3f tracePositionOffset;
//    // 跟随的旋转偏移，必须打开traceRotation功能才有效
//    private Quaternion traceRotationOffset;
//    // 如果为true则一直跟随，否则只在开始时跟随
//    private boolean traceAlways;
//    // 跟随位置类型,该偏移会叠加到tracePositionOffset上
//    protected TracePositionType tracePositionType = TracePositionType.origin;
//    
//    // 开始、展示、结束三个阶段的动画控制。
//    protected List<Anim> animAll; // 全阶段的动画控制
//    protected List<Anim> animStarts;
//    protected List<Anim> animDisplays;
//    protected List<Anim> animEnds;
//    
//    // 特效的执行速度,注意:这个参数作为动态配置,不开放到xml中进行配置.
//    // 这个参数在每次cleanup的时候都会重置为1.0,因为特效需要缓存重用.
//    // 如:多个技能使用了当前同一个特效,而这些技能需要的特效播放速度可能都不一样
//    // 这就要求各个技能在执行的过程中都要特别设置特效执行速度,以避免被一些技能
//    // 改变了执行速度,导致后续所有技能都以不正确的速度播放
//    protected float speed = 1.0f;
//    
//    // ---- 内部
//    // 本地根作点, 为了隔离Effect自身的变换和Anim所执行的动画变换，必须提供一个本地根作点
//    // anim所执行的动画变换都依赖该localRoot作点。也就是不影响AbstractEffect本身
//    protected final Node localRoot = new Node("Effect_localRoot");
//    
//    // 是否开始运行
//    protected boolean started;
//    // 是否已经初始化,几个阶段分别有初始化init
//    protected boolean initDisplay;
//    protected boolean initEnd;
//    
//    /** 当前效果已经运行的总时间, 该时间包含各个阶段。 */
//    protected float time;
//    // 当前阶段用时，每个阶段都会重置
//    protected float phaseTime;
//    // 当前所处的阶段
//    protected Phase phase = Phase.none;
//    // 声音是否已经播放
//    private boolean soundPlayed;
//    
//    public AbstractEffect() {
//        super("AbstractEffect");
//        // 首先添加本地根作点，确保后续添加的所有子作点都在该localRoot下。
//        super.attachChild(localRoot);
//    }
//    
//    public AbstractEffect(EffectData data) {
//        this();
//        this.data = data;
//        if (data != null) {
//            this.phaseTimeStart = data.getAsFloat("timeStart", phaseTimeStart);
//            this.phaseTimeDisplay = data.getAsFloat("timeDisplay", phaseTimeDisplay);
//            this.phaseTimeEnd = data.getAsFloat("timeEnd", phaseTimeEnd);
//            
//            this.sound = data.getAttribute("sound");
////            this.soundOffset = data.getAsFloat("soundOffset", soundOffset);
//            this.soundInstance = data.getAsBoolean("soundInstance", soundInstance);
//            
//            tracePosition = data.getAsBoolean("tracePosition", tracePosition);
//            traceRotation = data.getAsBoolean("traceRotation", traceRotation);
//            tracePositionOffset = data.getAsVector3f("tracePositionOffset");
//            tracePositionType = TracePositionType.identify(data.getAttribute("tracePositionType", tracePositionType.name()));
//            float[] tempRotationOffset = data.getAsFloatArray("traceRotationOffset");
//            if (tempRotationOffset != null) {
//                traceRotationOffset = new Quaternion();
//                traceRotationOffset.fromAngles(tempRotationOffset);
//            }
//            traceAlways = data.getAsBoolean("traceAlways", traceAlways);
//            
//            // anim
//            String[] tempAnimStarts = data.getAsArray("animStarts");
//            String[] tempAnimAll = data.getAsArray("animAll");
//            String[] tempAnimDisplays = data.getAsArray("animDisplays");
//            String[] tempAnimEnds = data.getAsArray("animEnds");
//            if (tempAnimStarts != null && tempAnimStarts.length > 0) {
//                animStarts = new ArrayList<Anim>(tempAnimStarts.length);
//                for (String animId : tempAnimStarts) {
//                    animStarts.add(Loader.loadAnimation(animId));
//                }
//            }
//            if (tempAnimAll != null && tempAnimAll.length > 0) {
//                animAll = new ArrayList<Anim>(tempAnimAll.length);
//                for (String animId : tempAnimAll) {
//                    animAll.add(Loader.loadAnimation(animId));
//                }
//            }
//            if (tempAnimDisplays != null && tempAnimDisplays.length > 0) {
//                animDisplays = new ArrayList<Anim>(tempAnimDisplays.length);
//                for (String animId : tempAnimDisplays) {
//                    animDisplays.add(Loader.loadAnimation(animId));
//                }
//            }
//            if (tempAnimEnds != null && tempAnimEnds.length > 0) {
//                animEnds = new ArrayList<Anim>(tempAnimEnds.length);
//                for (String animId : tempAnimEnds) {
//                    animEnds.add(Loader.loadAnimation(animId));
//                }
//            }
//            
//            // 特效的初始位置
//            Vector3f position = data.getAsVector3f("position");
//            if (position != null) {
//                setLocalTranslation(position);
//            }
//            
//            // 特效的初始旋转
//            float[] rotation = data.getAsFloatArray("rotation");
//            if (rotation != null) {
//                Quaternion rot = getLocalRotation();
//                rot.fromAngles(rotation);
//                setLocalRotation(rot);
//            }
//            
//            // 缩放
//            Vector3f scale = data.getAsVector3f("scale");
//            if (scale != null) {
//                setLocalScale(scale);
//            }
//        }
//    }
//
//    // 覆盖这两个方法，确定效果的子对象都添加到这个本地根作点中。
//    @Override
//    public int attachChild(Spatial child) {
//        return localRoot.attachChild(child);
//    }
//
//    @Override
//    public int attachChildAt(Spatial child, int index) {
//        return localRoot.attachChildAt(child, index);
//    }
//    
//    @Override
//    public final void start() {
//        if (started) {
//            return;
//        }
//        started = true;
//        phase = Phase.start;
//        
//        // 1.初始化位置，并且要在doInit之前
//        doUpdatePosition();
//        
//        // 2.逻辑初始化，因为doInit中可能会有anim需要初始化，所以在anim初始化之前
//        // 需要先初始化effect的位置(doUpdatePosition)
//        doInit();
//    }
//    
//    @Override
//    public void update(float tpf) {
//        if (!started) {
//            return;
//        }
//        
//        time += tpf;
//        phaseTime += tpf;
//        
//        // 如果是持续跟随的情况则需要不停更新位置
//        if (traceAlways) {
//            doUpdatePosition();
//        }
//        
//        // 执行侦听器
//        if (listeners != null) {
//            for (int i = 0; i < listeners.size(); i++) {
//                listeners.get(i).onEffectPlay(this);
//            }
//        }
//        
//        // 检测并播放声音
//        checkPlaySound(time);
//        
//        updatePhaseAllAnim(tpf);
//        updatePhaseAll(tpf);
//        
//        // 6.执行各个阶段更新，注：如果阶段时间设置为0，则完全不执行，如果大于0，则要
//        // 确保至少执行一次,并且要确保执行到phaseTime>目标阶段的时间
//        boolean end = false;
//        if (phase == Phase.start) {
//            if (phaseTimeStart <= 0) { // 如果时间设置<=0 则完全不执行。
//                changePhase(Phase.display);
//            } else {
//                // 如果时间>0, 则确保至少执行一次。
//                float truePhaseTimeStart = phaseTimeStart / speed;
//                float inter = phaseTime / truePhaseTimeStart; // 开始阶段的时间插值
//                updatePhaseStartAnim(tpf, inter); // 自身的动画更新
//                updatePhaseStart(tpf, inter);     // 子类的动画更新
//                if (phaseTime > truePhaseTimeStart) {
//                    changePhase(Phase.display);
//                }
//            }
//        } else if (phase == Phase.display) {
//            if (phaseTimeDisplay == 0) {
//                // 如果display阶段时间为0，则直接跳过，完全不执行
//                changePhase(Phase.end);
//            } else {
//                // 判断并初始化Display
//                if (!initDisplay) {
//                    doInitDisplay();
//                    initDisplay = true;
//                }
//                // 执行display阶段逻辑
//                // 自身的动画更新
//                updatePhaseDisplayAnim(tpf); 
//                // 子类的动画更新
//                updatePhaseDisplay(tpf, phaseTime);
//                // 如果phaseTimeDisplay > 0,并且执行时间已过，则跳到end.(如果时间设置小于0则一直执行，这时永远不会跳到end)
//                float truePhaseTimeDisplay = phaseTimeDisplay / speed;
//                if (phaseTimeDisplay > 0 && phaseTime > truePhaseTimeDisplay) {
//                    changePhase(Phase.end);
//                }    
//            }
//        } else if (phase == Phase.end) {
//            // 与start阶段相同，如果时间设置小于等于0则直接跳过,但是需要确认，可能部分子类作了特殊处理，不会根据时间来结束效果。
//            if (phaseTimeEnd <= 0) {
//                end = confirmEnd();
//            } else {
////                logger.log(Level.INFO, "==== Do update phaseEnd, phaseTime={0}, phaseTimeEnd={1}, interpolation={2}"
////                        , new Object[] {phaseTime, phaseTimeEnd, phaseTime / phaseTimeEnd});
//                if (!initEnd) {
//                    doInitEnd();
//                    initEnd = true;
//                }
//                float truePhaseTimeEnd = phaseTimeEnd / speed;
//                float inter = phaseTime / truePhaseTimeEnd; // 结束阶段的时间插值
//                updatePhaseEndAnim(tpf, inter); // 自身的动画更新
//                updatePhaseEnd(tpf, inter);     // 子类的动画更新。
//                if (phaseTime > truePhaseTimeEnd) {
//                    end = confirmEnd();
//                }
////                logger.log(Level.INFO, "==phaseTimeEnd={0}, speed={1}, truePhaseTimeEnd={2}, inter={3}"
////                        , new Object[] {phaseTimeEnd, speed, truePhaseTimeEnd, inter});
//            }
//        }
//        
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
//    }
//
//    @Override
//    public void cleanup() {
//        started = false;
//        initDisplay = false;
//        initEnd = false;
//        phaseTime = 0;
//        phase = Phase.none;
//        time = 0;
//        soundPlayed = false;
//        // 重置速度,这很重要,因为效果是可重用的，每一个使用“效果”的物体可能
//        // 需要的速度都不同，所以使用效果前确保设置自己须要的速度，或者保持默认
//        // 的1.0
//        speed = 1.0f; 
//        
//        if (animStarts != null) {
//            for (Anim anim : animStarts) {
//                if (!anim.isEnd()) {
//                    anim.cleanup();
//                }
//            }
//        }
//        if (animAll != null) {
//            for (Anim anim : animAll) {
//                if (!anim.isEnd()) {
//                    anim.cleanup();
//                }
//            }
//        }
//        if (animDisplays != null) {
//            for (Anim anim : animDisplays) {
//                if (!anim.isEnd()) {
//                    anim.cleanup();
//                }
//            }
//        }
//        if (animEnds != null) {
//            for (Anim anim : animEnds) {
//                if (!anim.isEnd()) {
//                    anim.cleanup();
//                }
//            }
//        }
//        
//        // 清理效果侦听器
//        if (listeners != null) {
//            listeners.clear();
//        }
//        
//        // 清除被跟踪的对象。
//        this.traceObject = null;
//        
//        // 当效果结束时，将效果从场景中移除。
//        removeFromParent();
//    }
//    
//    protected final void changePhase(Phase phase) {
//        this.phaseTime = 0;
//        this.phase = phase;
//    }
//
//    @Override
//    public final boolean isEnd() {
//        return !started;
//    }
//    
//    @Override
//    public float getSpeed() {
//        return speed;
//    }
//
//    @Override
//    public void setSpeed(float speed) {
//        if (speed <= 0) {
//            LOG.log(Level.WARNING, "Effect speed could not less than zero, now use 0.0001 instead.");
//            speed = 0.0001f;
//        }
//        this.speed = speed;
//    }
//    
//    @Override
//    public final Phase getCurrentPhase() {
//        return phase;
//    }
//
//    @Override
//    public void jumpToEnd() {
//        if (!started)
//            return;
//        if (this.phase == Phase.end) 
//            return;
//        changePhase(Phase.end);
//    }
//
//    @Override
//    public final Spatial getTraceObject() {
//        return traceObject;
//    }
//
//    @Override
//    public final void setTraceObject(Spatial target) {
//        this.traceObject = target;
//    }
//
//    @Override
//    public final Vector3f getTracePositionOffset() {
//        return tracePositionOffset;
//    }
//
//    @Override
//    public final void setTracePositionOffset(Vector3f positionOffset) {
//        this.tracePositionOffset = positionOffset;
//    }
//
//    @Override
//    public final void setLocation(Vector3f position) {
//        setLocalTranslation(position);
//    }
//    
//    @Override
//    public final void setRotation(Quaternion rotation) {
//        setLocalRotation(rotation);
//    }
//
//    @Override
//    public void setScale(Vector3f scale) {
//        setLocalScale(scale);
//    }
//
//    @Override
//    public final void addListener(Listener listener) {
//        if (listeners == null) {
//            listeners = new ArrayList<Listener>();
//        }
//        if (!listeners.contains(listener)) {
//            listeners.add(listener);
//        }
//    }
//
//    @Override
//    public final boolean removeListener(Listener listener) {
//        if (listeners == null) {
//            return false;
//        }
//        return listeners.remove(listener);
//    }
//    
//    @Override
//    public Spatial getDisplay() {
//        return this;
//    }
//
//    @Override
//    public EffectData getData() {
//        return data;
//    }
//
//    @Override
//    public boolean isLoop() {
//        return phaseTimeDisplay < 0;
//    }
//    
//    @Override
//    public void updateLogicalState(float tpf) {
//        update(tpf);
//        super.updateLogicalState(tpf);
//    }
//    
//    /**
//     * 确认是否结束，默认情况下效果会根据设置的时间来结束当前效果，如果子
//     * 类需要阻止该效果的结束，则可根据情况返回false, 当该方法被调用时，说明
//     * 当前效果已经在时间上结束了。
//     * @param end
//     * @return 
//     */
//    protected boolean confirmEnd() {
//        return true;
//    }
//    
//    /**
//     * 如果当前效果为跟踪类型，并且被跟踪对象已经离开场景，则应该结束效果
//     * @return 
//     */
//    private boolean checkTraceEnd() {
//        return traceAlways && traceObject != null && !Common.getPlayState().isInScene(traceObject);
//    }
//    
//    /**
//     * 检查是否播放声效。
//     * @param timeUsed 效果已经执行的时间。
//     */
//    protected void checkPlaySound(float timeUsed) {
//        if (sound != null && !soundPlayed) {
//            if (soundInstance) {
//                SoundManager.getInstance().playSoundInstance(sound, getWorldTranslation());
//            } else {
//                SoundManager.getInstance().playSound(sound, getWorldTranslation());
//            }
//            soundPlayed = true;
//        }
//    }
//    
//    protected void doUpdatePosition() {
//        if (traceObject != null) {
//            // 如果跟踪的目标已经离开场景，则效果直接进入end阶段
//            if (!Common.getPlayState().isInScene(traceObject)) {
//                changePhase(Phase.end);
//                return;
//            }
//            if (tracePosition) {
//                // add type offset
//                Vector3f pos = getLocalTranslation();
//                if (tracePositionType == TracePositionType.origin) {
//                    pos.set(traceObject.getWorldTranslation());
//                } else if (tracePositionType == TracePositionType.origin_bound_top) {
//                    GeometryUtils.getBoundTopPosition(traceObject, pos);
//                    pos.setX(traceObject.getWorldTranslation().x);
//                    pos.setZ(traceObject.getWorldTranslation().z);
//                } else if (tracePositionType == TracePositionType.bound_center) {
//                    pos.set(traceObject.getWorldBound().getCenter());
//                } else if (tracePositionType == TracePositionType.bound_top) {
//                    GeometryUtils.getBoundTopPosition(traceObject, pos);
//                }
//                // 注：tracePositionOffset是以被跟随的目标对象的本地坐标为基准的,
//                // 所以必须mult上目标对象的旋转
//                if (tracePositionOffset != null) {
//                    TempVars tv = TempVars.get();
//                    traceObject.getWorldRotation().mult(tracePositionOffset, tv.vect2);
//                    pos.addLocal(tv.vect2);
//                    tv.release();
//                }
//                setLocalTranslation(pos);
//            }
//            
//            if (traceRotation) {
//                Quaternion rot = getLocalRotation();
//                rot.set(traceObject.getWorldRotation());
//                if (traceRotationOffset != null) {
//                    rot.multLocal(traceRotationOffset);
//                }
//                setLocalRotation(rot);
//            }
//        }
//    }
//    
//    /**
//     * 初始化
//     */
//    protected void doInit() {
//        // 开始阶段的动画初始化
//        if (animStarts != null) {
//            for (Anim anim : animStarts) {
//                anim.setTarget(localRoot);
//                anim.start();
//            }
//        }
//        // 全阶段的动画初始化
//        if (animAll != null) {
//            for (Anim anim : animAll) {
//                anim.setTarget(localRoot);
//                anim.start();
//            }
//        }
//    }
//    
//    private void doInitDisplay() {
//        if (animDisplays != null) {
//            for (Anim anim : animDisplays) {
//                anim.setTarget(localRoot);
//                anim.start();
//            }
//        }
//    }
//    
//    private void doInitEnd() {
//        if (animEnds != null) {
//            for (Anim anim : animEnds) {
//                anim.setTarget(localRoot);
//                anim.start();
//            }
//        }
//    }
//    
//    private void updatePhaseStartAnim(float tpf, float inter) {
//        if (animStarts != null) {
//            for (Anim anim : animStarts) {
//                anim.display(inter); // 开始和结束阶段的动画使用同步插值，展示阶段就不需要
//            }
//        }
//    }
//
//    private void updatePhaseAllAnim(float tpf) {
//        if (animAll != null) {
//            for (Anim anim : animAll) {
//                anim.update(tpf);
//            }
//        }
//    }
//    
//    private void updatePhaseDisplayAnim(float tpf) {
//        if (animDisplays != null) {
//            for (Anim anim : animDisplays) {
//                anim.update(tpf);
//            }
//        }
//    }
//
//    private void updatePhaseEndAnim(float tpf, float inter) {
//        if (animEnds != null) {
//            for (Anim anim : animEnds) {
////                logger.log(Level.INFO, "updatePhaseEndAnim, inter={0}, anim={1}"
////                        , new Object[] {inter, anim});
//                anim.display(inter);
//            }
//        }
//    }
//    
//    /**
//     * 全阶段更新, 该方法在start,display,end阶段都会执行到。重写这个方法来实现自定义逻辑.
//     * @param tpf 
//     */
//    protected void updatePhaseAll(float tpf) {}
//    
//    /**
//     * 更新效果的起始阶段逻辑,这个方法只有在开始阶段时间不为0的时候才会执行。
//     * 重写这个方法来实现自定义逻辑.
//     * @param tpf 
//     * @param interpolation 当前阶段的时间插值，取值[0.0, 1.0]
//     */
//    protected void updatePhaseStart(float tpf, float interpolation) {}
//    
//    /**
//     * 更新效果显示阶段的逻辑,重写这个方法来实现自定义逻辑.
//     * @param tpf
//     * @param phaseTime 当前阶段的已经使用的时间,注意：并非时间插值，因为有些
//     * 效果的存在不会受时间限制，当displayPhaseTime设置为小于0的值时，该效果会
//     * 一直存在，所以使用插值就没有意义。
//     */
//    protected void updatePhaseDisplay(float tpf, float phaseTime) {};
//    
//    /**
//     * 更新效果的结束阶段逻辑,重写这个方法来实现自定义逻辑.
//     * @param tpf
//     * @param interpolation 当前阶段的时间插值，取值[0.0, 1.0]
//     */
//    protected void updatePhaseEnd(float tpf, float interpolation) {};
//   
//
//}
