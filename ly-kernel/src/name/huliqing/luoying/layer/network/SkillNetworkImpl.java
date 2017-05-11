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
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.mess.SkillWalkMess;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.skill.Skill;

/**
 *
 * @author huliqing
 */
public class SkillNetworkImpl implements SkillNetwork {
    private final static Network NETWORK = Network.getInstance();
    private SkillService skillService;
    
    @Override
    public void inject() {
        skillService = Factory.get(SkillService.class);
    }
    
    @Override
    public boolean playWalk(Entity actor, Skill walkSkill, Vector3f dir, boolean faceToDir, boolean force) {
        SkillWalkMess mess = new SkillWalkMess();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setDir(dir);
        mess.setFace(faceToDir);
        mess.setSkillId(walkSkill.getData().getId());
        
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
            return false;
        }
        
        if (force || skillService.isPlayable(actor, walkSkill)) {
            if (NETWORK.hasConnections()) {
                NETWORK.broadcast(mess);
            }
            return skillService.playWalk(actor, walkSkill, dir, faceToDir, true);
        }
        
        return false;
    }
    
    
    
}
