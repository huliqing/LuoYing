/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
