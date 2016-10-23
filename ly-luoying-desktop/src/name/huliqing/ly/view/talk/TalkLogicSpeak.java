/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.talk;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.ly.Config;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 *
 * @author huliqing
 */
public class TalkLogicSpeak extends AbstractTalkLogic {
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    
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
            useTime = MathUtils.clamp(Config.getSpeakTimeWorld() * worldLen
                    , Config.getSpeakTimeMin()
                    , Config.getSpeakTimeMax());
        }
        // 委托给speak.
        // 注：该类没有做任何逻辑,只给speak计算一个时间，然后等待时间到即结束当前speak.
        if (network) {
            gameNetwork.speak(actor, mess, useTime);
        } else {
            gameService.speak(actor, mess, useTime);
        }
    }
    
    @Override
    protected void doTalkLogic(float tpf) {}
    
}
