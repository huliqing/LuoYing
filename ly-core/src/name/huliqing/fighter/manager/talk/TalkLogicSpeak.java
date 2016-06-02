/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.manager.talk;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.utils.MathUtils;

/**
 *
 * @author huliqing
 */
public class TalkLogicSpeak extends AbstractTalkLogic {
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 说话的角色
    private Actor actor;
    // 说话的内容
    private String mess;
    
    public TalkLogicSpeak(Actor actor, String mess) {
        this.actor = actor;
        this.mess = mess;
    }
    
    @Override
    protected void doInit() {
        // 如果没有指定内容显示时间，自动计算useTime
        if (useTime <= 0) {
            int worldLen = ResourceManager.getWorldLength(mess);
            useTime = MathUtils.clamp(configService.getSpeakTimeWorld() * worldLen
                    , configService.getSpeakTimeMin()
                    , configService.getSpeakTimeMax());
        }
        // 委托给speak.
        // 注：该类没有做任何逻辑,只给speak计算一个时间，然后等待时间到即结束当前speak.
        if (network) {
            actorNetwork.speak(actor, mess, useTime);
        } else {
            actorService.speak(actor, mess, useTime);
        }
    }
    
    @Override
    protected void doTalkLogic(float tpf) {}
    
}
