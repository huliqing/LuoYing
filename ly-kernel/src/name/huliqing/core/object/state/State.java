/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import com.jme3.app.Application;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.StateData;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.EffectService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.object.AbstractPlayObject;
import name.huliqing.core.xml.DataProcessor;
import name.huliqing.core.object.effect.Effect;

/**
 * 注意状态有一个更新频率
 * @author huliqing
 * @param <T>
 */
public class State<T extends StateData> extends AbstractPlayObject implements DataProcessor<T>{
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final EffectService effectService = Factory.get(EffectService.class);
    private final StateService stateService = Factory.get(StateService.class);
    
    protected T data;
    
    /**
     * 状态的执行时长，单位秒
     */
    protected float totalUseTime;
    
    // ---- inner
    protected Actor actor;
    
    /**
     * 当前状态已经执行的时间，单位秒，这个时间是用来控制状态结束的。
     * 当这个时间达到useTime设定的时间时，状态就结束。但是这个时间可以被重置
     * 即重置为0，这时状态又会从头开始计算时间。
     */
    protected float timeUsed;
    
    // ---- inner
        
    /**
     * 角色获得状态时的效果,这些效果会在状态开始时附加在角色身上，在状态结束时停止．
     * 注意：这些效果引用只是临时的，在结束时要清空。
     */
    protected List<Effect> tempEffects;
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }
    
    @Override
    public void initialize(Application app) {
        super.initialize(app);
        if (data.getEffects() != null) {
            if (tempEffects == null) {
                tempEffects = new ArrayList<Effect>(data.getEffects().length);
            }
            for (String effectId : data.getEffects()) {
                Effect effect = effectService.loadEffect(effectId);
                effect.setTraceObject(actor.getModel());
                playService.addEffect(effect);
                tempEffects.add(effect);
            }
        }
        totalUseTime = data.getUseTime();
    }
    
    @Override
    public void update(float tpf) {
        timeUsed += tpf;
        
        // 时间到退出,cleanup由StateProcessor去调用。
        if (timeUsed >= totalUseTime) {
            doEnd();
            return;
        }
        
        if (data.isRemoveOnDead() && actorService.isDead(actor)) {
            doEnd();
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
        
        // 清理后应该直接把这三个参数清理掉，每次运行时重新设置。
        // 要尽量避免进行cleanup时做了多余的
        actor = null;
        
        // 清0
        timeUsed = 0;
        super.cleanup();
    }
    
    /**
     * 让状态时间重置回原点,这意味着当前状态会继续运行一个useTime的周期时间。
     */
    public void rewind() {
        timeUsed = 0;
    }
    
    /**
     * 默认情况下，状态会在时间达到的情况下自动退出。但部分情况下，子类可能需要
     * 主动退出。则可以使用这个方法来主动退出逻辑。
     */
    protected void doEnd() {
        stateService.removeState(actor, data.getId());
    }
    
}
