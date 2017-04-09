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
import name.huliqing.luoying.object.module.SkinListener;
import name.huliqing.luoying.object.module.SkinModule;

/**
 * @author huliqing
 */
public class SkinServiceImpl implements SkinService {
    
    @Override
    public void inject() {
        // ignore
    }

    @Override
    public boolean isWeaponTakeOn(Entity actor) {
        SkinModule module = actor.getModule(SkinModule.class);
        return module != null && module.isWeaponTakeOn();
    }

    @Override
    public void takeOnWeapon(Entity actor) {
        SkinModule control = actor.getModule(SkinModule.class);
        if (control != null) {
            control.takeOnWeapon();
        }
    }
    
    @Override
    public void takeOffWeapon(Entity actor) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            module.takeOffWeapon();
        }
    }

    @Override
    public long getWeaponState(Entity actor) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            return module.getWeaponState();
        }
        return -1;
    }

    @Override
    public void addSkinListener(Entity actor, SkinListener skinListener) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            module.addSkinListener(skinListener);
        }
    }

    @Override
    public boolean removeSkinListener(Entity actor, SkinListener skinListener) {
        SkinModule module = actor.getModule(SkinModule.class);
        if (module != null) {
            return module.removeSkinListener(skinListener);
        }
        return false;
    }
   
}
