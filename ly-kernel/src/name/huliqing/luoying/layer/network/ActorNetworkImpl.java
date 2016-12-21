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
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.network.Network;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.mess.ActorPhysicsMess;
import name.huliqing.luoying.mess.ActorViewDirMess;
import name.huliqing.luoying.mess.ActorLookAtMess;
import name.huliqing.luoying.mess.ActorSetLocationMess;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class ActorNetworkImpl implements ActorNetwork{
    private final static Network NETWORK = Network.getInstance();
    private ActorService actorService;
    
    @Override
    public void inject() {
        actorService = Factory.get(ActorService.class);
    }

    @Override
    public void setLocation(Entity actor, Vector3f location) {
        if (!NETWORK.isClient()) {
            ActorSetLocationMess mess = new ActorSetLocationMess();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setLocation(location);
            NETWORK.broadcast(mess);
            actorService.setLocation(actor, location);
        }
    }
    
    @Override
    public void setViewDirection(Entity actor, Vector3f viewDirection) {
        if (!NETWORK.isClient()) {
            ActorViewDirMess mess = new ActorViewDirMess();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setViewDir(viewDirection);
            NETWORK.broadcast(mess);
            actorService.setViewDirection(actor, viewDirection);
        }
    }

    @Override
    public void setPhysicsEnabled(Entity actor, boolean enabled) {
         if (!NETWORK.isClient()) {
            ActorPhysicsMess mess = new ActorPhysicsMess();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setEnabled(enabled);
            NETWORK.broadcast(mess);
            actorService.setPhysicsEnabled(actor, enabled); 
        }
    }

    @Override
    public void setLookAt(Entity actor, Vector3f position) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                ActorLookAtMess mess = new ActorLookAtMess();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setPos(position);
                NETWORK.broadcast(mess);
            }
            actorService.setLookAt(actor, position); 
        }
    }

    
}
