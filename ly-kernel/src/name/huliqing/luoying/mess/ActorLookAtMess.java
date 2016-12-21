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
package name.huliqing.luoying.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 朝向
 * @author huliqing
 */
@Serializable
public class ActorLookAtMess extends GameMess {
 
    private long actorId;
    private Vector3f pos;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    /**
     * 看向的目标位置，注意：是位置，不是方向
     * @return 
     */
    public Vector3f getPos() {
        return pos;
    }

    /**
     * 设置看向的目标位置，注意：是位置，不是方向
     * @param pos 
     */
    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    @Override
    public void applyOnClient(GameClient gameClient) {
        super.applyOnClient(gameClient);
        Entity actor = Factory.get(PlayService.class).getEntity(actorId);
        if (actor != null) {
            Factory.get(ActorService.class).setLookAt(actor, pos);
        }
    }
    
    
}
