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
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.TalentListener;
import name.huliqing.luoying.object.module.TalentModule;

/**
 *
 * @author huliqing
 */
public class TalentServiceImpl implements TalentService {

    @Override
    public void inject() {
        // ignore
    }

    @Override
    public int getTalentPoints(Entity actor) {
        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
        if (module != null) {
            return module.getTalentPoints();
        }
        return 0;
    }
    
    @Override
    public void addTalentPoints(Entity actor, String talentId, int points) {
        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
        if (module != null) {
            module.addTalentPoints(talentId, points);
        }
    }

    @Override
    public void addTalentListener(Entity actor, TalentListener talentListener) {
        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
        if (module != null) {
            module.addTalentListener(talentListener);
        }
    }

    @Override
    public void removeTalentListener(Entity actor, TalentListener talentListener) {
        TalentModule module = actor.getModuleManager().getModule(TalentModule.class);
        if (module != null) {
            module.removeTalentListener(talentListener);
        }
    }

}
