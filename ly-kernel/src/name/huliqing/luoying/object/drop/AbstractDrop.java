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
package name.huliqing.luoying.object.drop;

import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 * @author huliqing
 */
public abstract class AbstractDrop implements Drop {
    
    protected DropData data;
    protected String[] sounds;
    
    @Override
    public void setData(DropData data) {
        this.data = data;
        sounds = data.getAsArray("sounds");
    }
    
    @Override
    public DropData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    /**
     * 播放drop声效,这个方法由子类调用，当子类逻辑确认掉落物品时可调用这个方法来播放掉落声音。
     * @param source 
     */
    protected void playDropSounds(Entity source) {
        if (sounds != null) {
            for (String s : sounds) {
                SoundManager.getInstance().playSound(s, source.getSpatial().getWorldTranslation());
            }
        }
    }
    
}
