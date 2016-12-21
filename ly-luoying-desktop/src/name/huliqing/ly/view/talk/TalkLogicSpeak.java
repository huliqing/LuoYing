/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.ly.view.talk;

import name.huliqing.luoying.Factory;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.ly.LyConfig;
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
            useTime = MathUtils.clamp(LyConfig.getSpeakTimeWorld() * worldLen
                    , LyConfig.getSpeakTimeMin()
                    , LyConfig.getSpeakTimeMax());
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
