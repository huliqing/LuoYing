/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractState<T extends StateData> implements State<T> {
    
    protected T data;
    
    /** 状态的执行时长，单位秒 */
    protected float useTime;
    
    /**
     * 当前状态已经执行的时间，单位秒，这个时间是用来控制状态结束的。
     * 当这个时间达到useTime设定的时间时，状态就结束。但是这个时间可以被重置
     * 即重置为0，这时状态又会从头开始计算时间。
     */
    protected float timeUsed;
    
    /**
     * 用于判断角色是否死亡的属性
     */
    protected String bindDeadAttribute;
    
    /**
     * 角色获得状态时的效果,这些效果会在状态开始时附加在角色身上，在状态结束时停止．
     */
    protected String[] effects;
    
    /**
     * 关闭抗性值，取值为0.0~1.0, 这个值会抵消状态的作用，值为0时无抵消作用，值为
     * 1时，则全抵消。
     */
    protected float resist;
    
    // 当角色死亡时从角色身上移除这个状态.
    protected boolean removeOnDead;
    
    // ---- inner
    /**
     * 角色获得状态时的效果,这些效果会在状态开始时附加在角色身上，在状态结束时停止．
     * 注意：这些效果引用只是临时的，在结束时要清空。
     */
    protected List<Effect> tempEffects;
    
    protected boolean initialized;
    
    /** 状态的持有者，即受状态影响的角色，不能为null */
    protected Entity actor;
    
//    /** 状态的产生者，也就是说，这个状态是哪一个角色发出的, 如果一个状态没有发起源，则这个参数可能为null. */
//    protected Entity sourceActor;
        
    protected BooleanAttribute deadAttribute;

    @Override
    public void setData(T data) {
        if (initialized) {
            throw new IllegalStateException("State was initialized, could not reset data. origin state id=" + this.data.getId() 
                    + ", new state id=" + data.getId() + ", actorId=" + actor.getData().getId());
        }
        this.data = data;
        useTime = data.getAsFloat("useTime", 30);
        timeUsed = data.getAsFloat("timeUsed", timeUsed);
        bindDeadAttribute = data.getAsString("bindDeadAttribute");
        effects = data.getAsArray("effects");
        resist = data.getAsFloat("resist", resist);
        removeOnDead = data.getAsBoolean("removeOnDead", removeOnDead);
    }
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        data.setAttribute("resist", resist);
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalArgumentException("State already initialized");
        }
        initialized = true;
        
        if (bindDeadAttribute != null) {
            deadAttribute = actor.getAttributeManager().getAttribute(bindDeadAttribute, BooleanAttribute.class);
        }
        
        if (effects != null) {
            if (tempEffects == null) {
                tempEffects = new ArrayList<Effect>(effects.length);
            }
            // xxx: 这里要重构，因为状态在初始化的时候场景可能还不存在，这种情况发生在角色是从存档中载入的时候发生。
            if (actor.getScene() != null) {
                for (String effectId : effects) {
                    Effect effect = Loader.load(effectId);
                    effect.setTraceEntity(actor.getEntityId());
                    actor.getScene().addEntity(effect);
                    tempEffects.add(effect);
                }
            }
        }
//        // 获取状态的施放源
//        sourceActor = actor.getScene().getEntity(data.getSourceActor());
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void update(float tpf) {
        timeUsed += tpf;
        
        // 时间到退出,cleanup由StateProcessor去调用。
        if (timeUsed >= useTime) {
            initialized = false;
            return;
        }
        
        if (removeOnDead && deadAttribute != null && deadAttribute.getValue()) {
            initialized = false;
        }
    }
    
    @Override
    public void cleanup() {
        // 在结束时要清理特效,让特效都跳转到end阶段，然后然它们自动结束就可以。
        // 不要用cleanup，这会导致特效突然消失，很不自然。
        if (tempEffects != null) {
            for (Effect effect : tempEffects) {
                if (!effect.isEnd()) {
                    effect.requestEnd();
                }
            }
            tempEffects.clear();
        }
        
        initialized = false;
        timeUsed = 0;
    }
    
    /**
     * 让状态时间重置回原点,这意味着当前状态会继续运行一个useTime的周期时间。
     */
    @Override
    public void rewind() {
        timeUsed = 0;
    }

    /**
     * 判断状态是否已经结束
     * @return 
     */
    @Override
    public boolean isEnd() {
        return !initialized;
    }
    
    /**
     * 设置状态的持有者，即受状态影响的角色，不能为null
     * @param actor 
     */
    @Override
    public void setActor(Entity actor) {
        this.actor = actor;
    }

    /**
     * 状态的持有者，即受状态影响的角色
     * @return 
     */
    @Override
    public Entity getActor() {
        return actor;
    }

    /**
     * 状态的产生者，也就是说，这个状态是哪一个角色发出的, 可能为null.
     * @return 
     */
    protected Entity getSourceActor() {
        if (data.getSourceActor() > 0 && actor.getScene() != null) {
            return actor.getScene().getEntity(data.getSourceActor());
        }
        return null;
    }

    @Override
    public float getResist() {
        return resist;
    }

    @Override
    public void setResist(float resist) {
        this.resist = FastMath.clamp(resist, 0f, 1.0f);
    }
}
