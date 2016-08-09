/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.bullet;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.core.data.BulletData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.manager.SoundManager;
import name.huliqing.core.object.effect.Effect;
import name.huliqing.core.object.effect.EffectManager;
import name.huliqing.core.object.shape.Shape;

/**
 * 子弹基类
 * @author huliqing
 * @param <T>
 * @param <S>
 */
public abstract class AbstractBullet<T extends BulletData, S> extends Bullet<T, S> {
    private static final Logger LOG = Logger.getLogger(AbstractBullet.class.getName());
    
    // 调试
    protected boolean debug;
    // 碰撞图形，用于检查碰撞
    protected Shape shape;
    // 是否自动朝向
    protected boolean facing;
    // 是否跟踪目标
    protected boolean trace;
    // 基本速度
    protected float baseSpeed = 6;
    // 超时时间限制，单位秒
    protected float timeout = 30;
    // 子弹效果组
    protected String[] effectIds;
    // 击中时的特效
    protected String[] hitEffects;
    // 子弹声效组
    protected String[] sounds;
    protected String[] hitSounds;
    
    // ---- inner

    // 速度倍率,默认1.0，该参数不开放到xml中，主要作为动态加乘的参数使用.
    // 每次使用过后都重置为1.0
//    protected float speed = 1.0f;
    
    // 实际的结束点
    private final Vector3f trueEndPoint = new Vector3f();

    // 当前已经使用的时间。
    protected float timeUsed;
    
    // ---- inner
    
    // 这些是添加在子弹上的特效。
    private List<Effect> effects;
    protected List<BulletListener> listeners;
    
    @Override
    public void setData(T data) {
        this.data = data;
        this.debug = data.getAsBoolean("debug", debug);
        this.shape = Loader.loadShape(data.getAttribute("shape"));
        this.baseSpeed = data.getAsFloat("baseSpeed", baseSpeed);
        this.facing = data.getAsBoolean("facing", false);
        this.trace = data.getAsBoolean("trace", false);
        this.timeout = data.getAsFloat("timeout", timeout);
        this.effectIds = data.getAsArray("effects");
        this.sounds = data.getAsArray("sounds");
        this.hitEffects = data.getAsArray("hitEffects");
        this.hitSounds = data.getAsArray("hitSounds");
        
        // 用于碰撞
        Geometry geo = shape.getGeometry();
        geo.setCullHint(debug ? CullHint.Never : CullHint.Always);
        attachChild(geo);
        
        // shape的位置偏移
        Vector3f shapeOffset = data.getAsVector3f("shapeOffset");
        if (shapeOffset != null) {
            geo.setLocalTranslation(shapeOffset);
        }
        
    }

    @Override
    public void initialize() {
        super.initialize();
        
        // 初始化开始位置和结束位置
        setLocalTranslation(data.getStartPoint());
        
        if (!trace) {
            // 在不能跟踪的情况下要把trueEndPoint固定下来，不能动态引用。
            trueEndPoint.set(data.getEndPoint());
        }
        // 载入效果
        playEffects();
        // 播放声效
        playSounds();
    }
    
    /**
     * 获取当前的目标结束点。
     * @return 
     */
    protected Vector3f getCurrentEndPos() {
        if (trace) {
            trueEndPoint.set(data.getEndPoint());
        }
        return trueEndPoint;
    }
    
    /**
     * 给子弹添加上特效
     */
    protected void playEffects() {
        // 载入特效
        if (effects == null && effectIds != null) {
            effects = new ArrayList<Effect>(effectIds.length);
            for (String eid : effectIds) {
                Effect e = EffectManager.getInstance().loadEffect(eid);
                effects.add(e);
                attachChild(e);
            }
        }
        
        if (effects == null) 
            return;
        
        // 初始化特效
        for (Effect e : effects) {
            e.initialize();
        }
    }
    
    protected void playHitEffects() {
        if (hitEffects == null)
            return;
        for (String eid : hitEffects) {
            Effect effect = EffectManager.getInstance().loadEffect(eid);
            effect.getData().setInitLocation(getWorldTranslation());
            EffectManager.getInstance().addEffect(effect);
        }
    }
    
    protected void playSounds() {
        if (sounds == null)
            return;
        for (String sid : sounds) {
            SoundManager.getInstance().playSound(sid, getWorldTranslation());
        }
    }
    
    protected void playHitSounds() {
        if (hitSounds == null)
            return;
        for (String sid : hitSounds) {
            SoundManager.getInstance().playSound(sid, getWorldTranslation());
        }
    }
    
    @Override
    protected void bulletUpdate(float tpf) {
        timeUsed += tpf;
        
        getCurrentEndPos();
        doUpdatePosition(tpf, trueEndPoint);
        
        // 执行帧听器，通过帧听器来干预子弹的“判断“，如果帧听器hitCheck(this)返回true则说明子弹击中了一个目标，
        // 则执行hit效果和声音
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                if (listeners.get(i).hitCheck(this)) {
                    playHitEffects();
                    playHitSounds();
                }
            }
            // 在listeners的hitCheck内部可能会调用consume()方法来结束子弹，当listeners执行完后如果isConsumed()为true,
            // 则说明子弹应该立即结束逻辑，不希望再执行后面的击中判断。
            if (isConsumed()) {
                return;
            }
        }
        
        // 击中目标结束
        if (isHit(trueEndPoint)) {
            playHitEffects();
            playHitSounds();
            consume();
        }
        
        // 超时结束,超时不会执行hit效果和声效
        if (timeUsed >= timeout) {
            consume();
        }
    }
    
    @Override
    public void cleanup() {
        timeUsed = 0;

        if (effects != null) {
            for (Effect e : effects) {
                e.cleanup();
            }
        }
        super.cleanup();
    }

    @Override
    public boolean isHit(Spatial target) {
        BoundingVolume bv = target.getWorldBound();
        if (bv != null) {
            return shape.getGeometry().getWorldBound().intersects(bv);
        }
        return shape.getGeometry().getWorldBound().contains(target.getWorldTranslation());
    }

    @Override
    public boolean isHit(Vector3f target) {
        return shape.getGeometry().getWorldBound().contains(target);
    }
    
    /**
     * 添加一个子弹侦听器,该侦听器会在结束后清理。
     * @param listener 
     */
    @Override
    public void addListener(BulletListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<BulletListener>(1);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * 删除子弹侦听器
     * @param listener
     * @return 
     */
    @Override
    public boolean removeListener(BulletListener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    /**
     * 更新子弹的位置逻辑
     * @param tpf
     * @param endPos 当前实时的结束目标的位置,当子弹是跟踪类型的，并且
     */
    protected abstract void doUpdatePosition(float tpf, Vector3f endPos);
    
}
