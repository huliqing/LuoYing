/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.bullet;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.BulletData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.shape.Shape;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 * 子弹基类
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractBullet<T extends BulletData> extends ModelEntity<T> implements Bullet<T> {
//    private static final Logger LOG = Logger.getLogger(AbstractBullet.class.getName());
    
    // 调试
    protected boolean debug;
    // 碰撞图形，用于检查碰撞
    protected Shape shape;
    protected Vector3f shapeOffset;
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
    
    // 实际的结束点
    private final Vector3f trueEndPoint = new Vector3f();

    // 当前已经使用的时间。
    protected float timeUsed;
    
     /**
     * 标记子弹是否已经耗尽。
     */
    protected boolean consumed;
    
    protected final Vector3f start = new Vector3f();
    
    protected final Vector3f end = new Vector3f();
    
    /**
     * 发射子弹的目标源,可能是一个角色，也可能是其它
     */
    protected Entity source;
    
    protected List<Bullet.Listener> listeners;
    
    protected final Node bulletNode = new Node();
    
    protected Geometry hitChecker;
    
    protected float speed = 1.0f;
    
    public AbstractBullet() {
        bulletNode.addControl(new AbstractControl() {
            @Override
            protected void controlUpdate(float tpf) {
                bulletControlUpdate(tpf);
            }
            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {}
        });
    }
    
    @Override
    public void setData(T data) {
        this.data = data;
        this.debug = data.getAsBoolean("debug", debug);
        this.shape = Loader.load(data.getAsString("shape"));
        this.baseSpeed = data.getAsFloat("baseSpeed", baseSpeed);
        this.facing = data.getAsBoolean("facing", false);
        this.trace = data.getAsBoolean("trace", false);
        this.timeout = data.getAsFloat("timeout", timeout);
        this.sounds = data.getAsArray("sounds");
        this.hitEffects = data.getAsArray("hitEffects");
        this.hitSounds = data.getAsArray("hitSounds");
        this.shapeOffset = data.getAsVector3f("shapeOffset");
        this.effectIds = data.getAsArray("effects");
    }

    @Override
    protected Spatial loadModel() {
        return bulletNode;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        
        // 用于碰撞
        hitChecker = shape.getGeometry();
        hitChecker.setCullHint(debug ? CullHint.Never : CullHint.Always);
        if (shapeOffset != null) {
            hitChecker.setLocalTranslation(shapeOffset);
        }
        bulletNode.attachChild(hitChecker);
        bulletNode.setLocalTranslation(start);
        
        if (!trace) {
            // 在不能跟踪的情况下要把trueEndPoint固定下来，不能动态引用。
            trueEndPoint.set(end);
        }
        
        // 载入效果
        if (effectIds != null) {
            for (String eid : effectIds) {
                Effect e = Loader.load(eid);
                bulletNode.attachChild(e);
            }
        }
        
        // 播放声效
        if (sounds != null) {
            for (String sid : sounds) {
                SoundManager.getInstance().playSound(sid, bulletNode.getWorldTranslation());
            }
        }
    }
    
    @Override
    public void cleanup() {
        consumed = false;
        timeUsed = 0;
        super.cleanup();
    }
    
    private void bulletControlUpdate(float tpf) {
        // 如果已经标记为销毁，则不再处理逻辑，由BulletManager去清理和移除子弹。
        if (isConsumed()) {
            scene.removeEntity(this);
            return;
        }
        
        // 让子类去更新飞行逻辑。 
        bulletUpdate(tpf);
        
        // 触发子弹飞行时的侦听器，如果bulletFlying返回true,则说明击中了一个目标。
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                if (listeners.get(i).onBulletFlying(this)) {
                    onFiredTaget();
                }
            }
        }
    }

    @Override
    public void setStart(Vector3f startPoint) {
        this.start.set(startPoint);
    }

    @Override
    public void setEnd(Vector3f endPoint) {
        this.end.set(endPoint);
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void consume() {
        consumed = true;
    }
    
    @Override
    public boolean isConsumed() {
        return consumed;
    }
    
    @Override
    public void addListener(Bullet.Listener listener) {
        if (listeners == null) {
            listeners = new ArrayList<Bullet.Listener>(1);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    @Override
    public boolean removeListener(Bullet.Listener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    /**
     * 获取当前的目标结束点。
     * @return 
     */
    protected Vector3f getCurrentEndPos() {
        if (trace) {
            trueEndPoint.set(end);
        }
        return trueEndPoint;
    }
    
    /**
     * 获取发射该子弹的源，比如一个角色？或是一个未知的存在？
     * @return 
     */
    @Override
    public Entity getSource() {
        return source;
    }
    
    @Override
    public void setSource(Entity source) {
        this.source = source;
    }
    
    protected void bulletUpdate(float tpf) {
        timeUsed += tpf;
        
        getCurrentEndPos();
        doUpdatePosition(tpf, trueEndPoint);
        
        // 击中目标结束
        if (isHit(trueEndPoint)) {
            onFiredTaget();
            consume();
        }
        
        // 超时结束,超时不会执行hit效果和声效
        if (timeUsed >= timeout) {
            consume();
        }
    }

    @Override
    public boolean isHit(Spatial target) {
        BoundingVolume bv = target.getWorldBound();
        if (bv != null) {
            return hitChecker.getWorldBound().intersects(bv);
        }
        return hitChecker.getWorldBound().contains(target.getWorldTranslation());
    }

    @Override
    public boolean isHit(Vector3f target) {
        return hitChecker.getWorldBound().contains(target);
    }

    protected void onFiredTaget() {
        if (hitEffects != null) {
            for (String eid : hitEffects) {
                Effect effect = Loader.load(eid);
                effect.setLocalTranslation(bulletNode.getWorldTranslation());
                scene.addEntity(effect);
            }            
        }
        if (hitSounds != null) {
            for (String sid : hitSounds) {
                SoundManager.getInstance().playSound(sid, bulletNode.getWorldTranslation());
            }
        }
    }
    
    /**
     * 更新子弹的位置逻辑
     * @param tpf
     * @param endPos 当前实时的结束目标的位置,当子弹是跟踪类型的，并且
     */
    protected abstract void doUpdatePosition(float tpf, Vector3f endPos);
    
    
    
}
