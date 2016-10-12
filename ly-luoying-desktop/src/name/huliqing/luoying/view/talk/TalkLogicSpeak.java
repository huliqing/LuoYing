/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.talk;

import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.network.ActorNetwork;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.ConfigService;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.utils.MathUtils;

/**
 *
 * @author huliqing
 */
public class TalkLogicSpeak extends AbstractTalkLogic {
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 说话的角色
    private final Entity actor;
    // 说话的内容
    private final String mess;
    
    public TalkLogicSpeak(Entity actor, String mess) {
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
