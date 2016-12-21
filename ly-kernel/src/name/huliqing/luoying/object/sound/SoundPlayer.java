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
package name.huliqing.luoying.object.sound;

import com.jme3.math.Vector3f;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.object.Loader;

/**
 *
 * @author huliqing
 */ 
public class SoundPlayer {
    private static final Logger LOG = Logger.getLogger(SoundPlayer.class.getName());
    
    private final Map<String, Sound> soundCache = new HashMap<String, Sound>();
    
    public void playSound(String soundId, Vector3f position, float volume) {
        Sound sound = getSound(soundId);
        if (sound == null) {
            LOG.log(Level.WARNING, "Sound not found by soundId={0}", soundId);
            return;
        }
        sound.setLocalTranslation(position);
        sound.setVolume(volume);
        sound.play();
    }
    
    private Sound getSound(String soundId) {
        Sound sound = soundCache.get(soundId);
        if (sound == null) {
            sound = Loader.load(soundId);
            sound.initialize();
            sound.setLoop(false);
            soundCache.put(soundId, sound);
        }
        return sound;
    }
    
}
