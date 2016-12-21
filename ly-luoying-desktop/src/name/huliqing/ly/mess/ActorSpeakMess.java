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
package name.huliqing.ly.mess;


import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.mess.GameMess;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.layer.service.GameService;

/**
 * 让目标说话
 * @author huliqing
 */
@Serializable
public class ActorSpeakMess extends GameMess {
    
    private long actorId;
    // 说话的内容
    private String mess;
    // 说话的显示时间,单位秒,如果该值<=0,则该时间会被自动重新计算
    private float useTime;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public float getUseTime() {
        return useTime;
    }

    public void setUseTime(float useTime) {
        this.useTime = useTime;
    }

    @Override
    public void applyOnClient(GameClient gameClient) {
        PlayService playService = Factory.get(PlayService.class);
        GameService gameService = Factory.get(GameService.class);
        Entity actor = playService.getEntity(actorId);
        if (actor != null) {
            gameService.speak(actor, mess, useTime);
        }
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        // ignore
    }
    
    
}
