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
package name.huliqing.luoying.ui;

import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class UISound {
    private final static Map<String, AudioNode> cache = new HashMap<String, AudioNode>();
    
    /**
     * 播放音击时的音效
     * @param ui 
     */
    public static void playClick(UI ui) {
        if (!checkSoundEnabled(ui)) {
            return;
        }
        UIConfig uiconfig = UIFactory.getUIConfig();
        String sound = ui.getSoundClick();
        if (sound == null) {
            sound = uiconfig.getSoundClick();
        }
        if (sound == null) {
            Logger.getLogger(UISound.class.getName()).log(Level.WARNING, "No ui click sound set, ui={0}", ui);
            return;
        }
        AudioNode audio = cache.get(sound);
        if (audio == null) {
            audio = createAudio(sound);
            cache.put(sound, audio);
        }
        audio.play();
    }
    
//    /**
//     * 播放双击时的音效
//     * @param ui
//     */
//    public static void playDoubleClick(UI ui) {
//        //throw new UnsupportedOperationException();
//    }
//    
//    /**
//     * 播放移动时的音效
//     * @param ui
//     */
//    public static void playMoving(UI ui) {
//        //throw new UnsupportedOperationException();
//    }
    
    private static AudioNode createAudio(String file) {
        // 需要缓存
        AudioNode audio = new AudioNode(UIFactory.getUIConfig().getAssetManager(), file, DataType.Buffer);
        audio.setName(file);
        audio.setPositional(false);
        audio.setReverbEnabled(false);
        audio.setTimeOffset(0);
        audio.setVolume(1);
        audio.setLooping(false); // 暂不支持循环
        return audio;
    }
    
    private static boolean checkSoundEnabled(UI ui) {
        if (UIFactory.getUIConfig().isSoundEnabled() && ui.isSoundEnabled()) {
            return true;
        }
        return false;
    }
}
