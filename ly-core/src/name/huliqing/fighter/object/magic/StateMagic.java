/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.magic;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.MagicData;
import name.huliqing.fighter.game.service.ActorAnimService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.object.actoranim.ActorAnim;
import name.huliqing.fighter.utils.ConvertUtils;
import name.huliqing.fighter.utils.MathUtils;

/**
 * 状态类的魔法,这类魔法会给进入魔法范围内的角色添加一些状态。
 * @deprecated 可能以后不再使用这类魔法
 * @author huliqing
 */
public class StateMagic extends Magic {
    private final StateService stateService = Factory.get(StateService.class);
    private final ActorAnimService actorAnimService = Factory.get(ActorAnimService.class);
    
    // 格式:stateId|timePoint,stateId|timePoint,stateId|timePoint...
    private List<StateWrap> states;
    
    // 角色动画,格式:actorAnimId|timePoint,actorAnimId|timePoint,actorAnimId|timePoint...
    private List<ActorAnimWrap> actorAnims;

    @Override
    public void setData(MagicData data) {
        super.setData(data);
        // 状态格式:stateId|timePoint,stateId|timePoint,stateId|timePoint...
        String[] tempStates = data.getAsArray("states");
        if (tempStates != null) {
            states = new ArrayList<StateWrap>(tempStates.length);
            for (int i = 0; i < tempStates.length; i++) {
                String[] stateArr = tempStates[i].split("\\|");
                StateWrap sw = new StateWrap();
                sw.stateId = stateArr[0];
                if (stateArr.length >= 2) {
                    sw.timePoint = MathUtils.clamp(ConvertUtils.toFloat(stateArr[1], 0f), 0, 1);
                }
                states.add(sw);
            }
        }
        
        // 角色动画,格式:actorAnimId|timePoint,actorAnimId|timePoint,actorAnimId|timePoint...
        String[] tempActorAnims = data.getAsArray("actorAnims");
        if (tempActorAnims != null) {
            actorAnims = new ArrayList<ActorAnimWrap>(tempActorAnims.length);
            for (int i = 0; i < tempActorAnims.length; i++) {
                String[] actorAnimArr = tempActorAnims[i].split("\\|");
                ActorAnimWrap aaw = new ActorAnimWrap();
                aaw.actorAnimId = actorAnimArr[0];
                if (actorAnimArr.length >= 2) {
                    aaw.timePoint = MathUtils.clamp(ConvertUtils.toFloat(actorAnimArr[1], 0f), 0, 1);
                }
                actorAnims.add(aaw);
            }
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); 
        
        float inter = getInterpolation();
        
        // update states
        doUpdateStates(inter);
        
        // update actorAnims
        doUpdateActorAnims(inter);
    }

    @Override
    public void cleanup() {
        if (states != null) {
            for (StateWrap sw : states) {
                sw.cleanup();
            }
        }
        if (actorAnims != null) {
            for (ActorAnimWrap aaw : actorAnims) {
                aaw.cleanup();
            }
        }
        super.cleanup(); 
    }
    
    protected void doUpdateStates(float inter) {
        if (states != null) {
            for (StateWrap sw : states) {
                sw.update(inter);
            }
        }
    }
    
    protected void doUpdateActorAnims(float inter) {
        if (actorAnims != null) {
            for (ActorAnimWrap aaw : actorAnims) {
                aaw.update(inter);
            }
        }
    }
    
    protected void playState(String stateId) {
        if (target == null)
            return;
        stateService.addState(target, stateId, null);
    }
    
    protected void playActorAnim(String actorAnimId) {
        if (target == null) 
            return;
        ActorAnim anim = actorAnimService.loadAnim(actorAnimId);
        anim.setActor(target);
        target.getModel().addControl(anim);
        anim.start();
    }
    
    // 状态更新控制
    protected class StateWrap {
        String stateId;
        // 效果的开始播放时间点
        float timePoint;
        // 标记效果是否已经开始
        boolean started;
        
        void update(float interpolation) {
            if (started) return;
            if (interpolation >= timePoint) {
                if (hitChecker == null || (target != null && hitChecker.canHit(source, target))) {
                    playState(stateId);
                }
                started = true;
            }
        }
        
        void cleanup() {
            started = false;
        }
    }
    
    public class ActorAnimWrap {
        // 原始的时间开始点和结束点
        String actorAnimId;
        float timePoint;
        boolean started;
        
        void update(float interpolation) {
            if (started) return;
            if (interpolation >= timePoint) {
                if (hitChecker == null || (target != null && hitChecker.canHit(source, target))) {
                    playActorAnim(actorAnimId);
                }
                started = true;
            }
        }
      
        void cleanup() {
            started = false;
        }
    }
}
