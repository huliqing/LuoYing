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
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkillModule;

/**
 *
 * @author huliqing
 */
@Serializable
public class SkillWalkMess extends GameMess {
 
    protected long actorId;
    protected String skillId;
    
    // 目标方向
    private Vector3f dir = new Vector3f();
    private boolean face;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public Vector3f getDir() {
        return dir;
    }

    public void setDir(Vector3f dir) {
        if (dir != null) {
            this.dir.set(dir);
        }
    }

    public boolean isFace() {
        return face;
    }

    public void setFace(boolean face) {
        this.face = face;
    }

    @Override
    public void applyOnClient(GameClient gameClient) {
        super.applyOnClient(gameClient);
        PlayService playService = Factory.get(PlayService.class);
        SkillService skillService = Factory.get(SkillService.class);
        Entity actor = playService.getEntity(actorId);
        if (actor == null) return;
        skillService.playWalk(actor, actor.getModuleManager().getModule(SkillModule.class).getSkill(skillId), dir, face, true);
    }
    
    
}
