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
package name.huliqing.luoying.object.effect;

import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.sound.Sound;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 *
 * @author huliqing
 */
public class SoundWrap {
    private Effect effect;
    
    // xml上配置的声音id
    String soundId;
    // xml上配置的声音的开始时间
    float startTime;
    // 缓存的声音控制器
    Sound sound;
    // 声音的实际开始时间，受效果速度的影响。
    float trueStartTime;
    // 表示声音播放是否已经开始
    boolean started;
    
    public SoundWrap(Effect effect) {
        this.effect = effect;
    }

    public void update(float tpf, float effectTimeUsed) {
        if (started) {
            return;
        }
        if (effectTimeUsed >= trueStartTime) {
            if (sound == null) {
                sound = Loader.load(soundId);
                effect.attachChild(sound);
            }
            SoundManager.getInstance().addAndPlay(sound);
            started = true;
        }
    }

    public void cleanup() {
        if (sound != null) {
            SoundManager.getInstance().removeAndStopLoop(sound);
        }
        started = false;
    }
}
