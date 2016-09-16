/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import com.jme3.animation.LoopMode;
import com.jme3.math.FastMath;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.mvc.service.ActorService;

/**
 * 这个技能可以允许使用角色动画中的任何一侦作为角色的reset状态．当某些角色
 * 没有可用的reset动画时可以使用这个技能来代替．
 * @author huliqing
 */
public class ResetSkill extends AbstractSkill {
    private final ActorService actorService = Factory.get(ActorService.class);
    
    /**
     * 指定要把角色动画定格在animation动画的哪一帧上，
     * 这是一个插值点。
     */
    private float timePoint;

    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        timePoint = FastMath.clamp(data.getAsFloat("timePoint", timePoint), 0, 1.0f);
    }

    @Override
    protected void init() {
        super.init();
        
        // 如果没有设置动画名称，则把动画定格在角色的当前帧上
        if (data.getAnimation() == null) {
            
            // remove20160815
//            channelProcessor.setSpeed(0.0001f);

            actorService.reset(actor);
        }
        
    }

    @Override
    protected void doUpdateAnimation(String animation, LoopMode loopMode, float animFullTime, float animStartTime) {
        // 直接reset到指定动画的指定时间帧
        actorService.resetToAnimationTime(actor, animation, timePoint);
    }
    
    @Override
    protected void doUpdateLogic(float tpf) {
    }
    
}
