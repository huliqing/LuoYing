/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.bullet;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
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
 */
public abstract class AbstractBullet<T extends BulletData> extends Node implements Bullet<T> {
    private static final Logger LOG = Logger.getLogger(AbstractBullet.class.getName());
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final EffectService effectService = Factory.get(EffectService.class);
    
    protected T data;
    
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
    protected Vector3f startPoint;
    protected Vector3f endPoint;
    // 速度倍率,默认1.0，该参数不开放到xml中，主要作为动态加乘的参数使用.
    // 每次使用过后都重置为1.0
    protected float speed = 1.0f;
    protected SafeArrayList<BulletListener> listeners;
    
    // 实际的结束点
    private final Vector3f trueEndPoint = new Vector3f();
    // 基本速度
    private boolean started;
    // 标记是否要结束子弹运行
    private boolean toEnd;
    // 当前已经使用的时间。
    protected float timeUsed;
    
    // ---- inner
    
    // 这些是添加在子弹上的特效。
    private List<Effect> effects;
    
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
    public T getData() {
        return data;
    }

    @Override
    public void setPath(Vector3f startPoint, Vector3f endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    @Override
    public Vector3f getStartPoint() {
        if (startPoint == null) {
            startPoint = new Vector3f();
        }
        return startPoint;
    }

    @Override
    public Vector3f getEndPoint() {
        if (endPoint == null) {
            endPoint = new Vector3f();
        }
        return endPoint;
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public void updateLogicalState(float tpf) {
        update(tpf);
        
        // remove20160603,jme3.1后不再需要
//        super.updateLogicalState(tpf);
    }

    @Override
    public void start() {
        if (started) {
            return;
        }
        doInit();
        started = true;
    }
    
    /**
     * 获取当前的目标结束点。
     * @return 
     */
    protected Vector3f getCurrentEndPos() {
        if (trace) {
            trueEndPoint.set(endPoint);
        }
        return trueEndPoint;
    }
        
    protected void doInit() {
        // 初始化开始位置和结束位置
        setLocalTranslation(startPoint);
        if (!trace) {
            // 在不能跟踪的情况下要把trueEndPoint固定下来，不能动态引用。
            trueEndPoint.set(endPoint);
        }
        // 载入效果
        playEffects();
        // 播放声效
        playSounds();
    }
    
    protected void playEffects() {
        if (effectIds == null)
            return;
        
        if (effectIds != null) {
            effects = new ArrayList<Effect>(effectIds.length);
            for (String eid : effectIds) {
                Effect effect = EffectManager.getInstance().loadEffect(eid);
                effects.add(effect);
                // 把特效添加到子弹节点内，这样当子弹消失或脱离场景时，特效也可以一同立即消息。
                attachChild(effect);
                effect.initialize();
            }
        }
        
    }
    
    protected void playHitEffects() {
        if (hitEffects == null)
            return;
        for (String eid : hitEffects) {
            Effect effect = EffectManager.getInstance().loadEffect(eid);
            effect.setLocalTranslation(getWorldTranslation());
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
    public final void update(float tpf) {
        if (!started) {
            return;
        }
        
        timeUsed += tpf;
        getCurrentEndPos();
        doUpdatePosition(tpf, trueEndPoint);
        
        if (listeners != null) {
            for (BulletListener bl : listeners.getArray()) {
                if (bl.hitCheck(this)) {
                    playHitEffects();
                    playHitSounds();
                }
            }
        }
        
        // 手动结束,这可能在BulletListener内部调用了doEnd,所以优先放在前面。
        // 当外部调用doEnd强制结束时不需要执行hit效果,因为可能效果在hitCheck的时候已经执行。
        if (toEnd) {
            cleanup();
            return;
        }
        
        // 超时结束
        if (timeUsed >= timeout) {
            cleanup();
            return;
        }

        // 达到最终点自动结束，这个自动结束要执行效果
        if (isHit(trueEndPoint)) {
            playHitEffects();
            playHitSounds();
            cleanup();
        }
    }
    
    @Override
    public void cleanup() {
        started = false;
        startPoint = null;
        endPoint = null;
        timeUsed = 0;
        toEnd = false;
        speed = 1.0f;
        if (listeners != null) {
            listeners.clear();
        }
        
        if (effects != null) {
            for (Effect e : effects) {
                e.cleanup();
                e.removeFromParent();
            }
        }
        
        // 自行退出场景
        removeFromParent();
    }

    @Override
    public final Spatial getDisplay() {
        return this;
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

    @Override
    public final boolean isEnd() {
        return !started;
    }

    @Override
    public final void doEnd() {
        toEnd = true;
    }
    
    @Override
    public void addListener(BulletListener listener) {
        if (listeners == null) {
            listeners = new SafeArrayList<BulletListener>(BulletListener.class);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
 
    @Override
    public boolean removeListener(BulletListener listener) {
        if (listeners == null)
            return false;
        return listeners.remove(listener);
    }
    
    protected abstract void doUpdatePosition(float tpf, Vector3f endPos);
    
}
