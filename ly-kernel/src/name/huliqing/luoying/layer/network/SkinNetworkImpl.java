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

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.SkinService;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.mess.SkinWeaponTakeOnMess;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class SkinNetworkImpl implements SkinNetwork {
    private final static Network NETWORK = Network.getInstance();
    private SkinService skinService;
    
    @Override
    public void inject() {
        skinService = Factory.get(SkinService.class);
    }

    @Override
    public void takeOnWeapon(Entity actor) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                SkinWeaponTakeOnMess mess = new SkinWeaponTakeOnMess();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setTakeOn(true);
                NETWORK.broadcast(mess);
            }
            skinService.takeOnWeapon(actor);
        }
    }

    @Override
    public void takeOffWeapon(Entity actor) {
        SkinWeaponTakeOnMess mess = new SkinWeaponTakeOnMess();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setTakeOn(false);
        
        // client
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
            return;
        }

        // server
        if (NETWORK.hasConnections()) {
            NETWORK.broadcast(mess);
        }
        skinService.takeOffWeapon(actor);

    }
    
}
