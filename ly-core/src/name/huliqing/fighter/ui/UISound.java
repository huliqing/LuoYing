/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ui;

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
    
    /**
     * 播放双击时的音效
     */
    public static void playDoubleClick(UI ui) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * 播放移动时的音效
     */
    public static void playMoving(UI ui) {
        throw new UnsupportedOperationException();
    }
    
    private static AudioNode createAudio(String file) {
        // 需要缓存
        AudioNode audio = new AudioNode(UIFactory.getUIConfig().getAssetManager(), file);
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
