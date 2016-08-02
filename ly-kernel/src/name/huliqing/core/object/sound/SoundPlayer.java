/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.sound;

import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SoundData;
import name.huliqing.core.game.service.ConfigService;
import name.huliqing.core.object.DataFactory;

/**
 *
 * @author huliqing
 */
public class SoundPlayer {
    private final ConfigService configService = Factory.get(ConfigService.class);
    
    // 允许播放声音的最远距离，在该距离之外则不播放声音。
    // 该距离指与摄像机的距离,注意距离是平方表示，比较的时候用平方进行比较
    // 减少一次开方操作
    private final float MAX_DISTANCE_SQUARED = 80 * 80;
    private final Map<String, AudioNode> audioMap = new HashMap<String, AudioNode>();
    
    public void playSound(SoundData sound, Vector3f position) {
        playSound(sound, position, false);
    }
    
    public void playSound(SoundData sound, Vector3f position, boolean instance) {
        AudioNode audio = getSound(sound.getProto().getId());
        Vector3f camLoc = LY.getApp().getCamera().getLocation();
        // 注意：比较的是平方，可减少一次开方运算
        float distanceSquared = camLoc.distanceSquared(position);
        if (distanceSquared >= MAX_DISTANCE_SQUARED) {
            return;
        }
        float distanceFactor = (MAX_DISTANCE_SQUARED - distanceSquared) / MAX_DISTANCE_SQUARED;
        audio.setVolume(sound.getVolume() * distanceFactor * configService.getSoundVolume());
        if (instance) {
            audio.playInstance();
        } else {
            audio.play();
        }
    }
    
    public void stopSound(SoundData sound) {
        getSound(sound.getProto().getId()).stop();
    }
    
    private AudioNode getSound(String soundId) {
        AudioNode audio = audioMap.get(soundId);
        if (audio == null) {
            audio = createSound((SoundData) DataFactory.createData(soundId));
            audioMap.put(soundId, audio);
        }
        // 声效缓存数
        if (audioMap.size() > 100) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "audioMap size more than {0}", audioMap.size());
        }
        return audio;
    }
    
    public AudioNode createSound(SoundData sound) {
        String soundFile = sound.getProto().getFile();
        if (soundFile == null) {
            throw new NullPointerException("sound file not found, soundData id=" + sound.getProto().getFile());
        }
        AudioNode audio = new AudioNode(LY.getAssetManager(), soundFile);
        audio.setName(sound.getProto().getFile());
        // 不使用位置，直接根据距离来判断声音大小，see => public void playSound(SoundData sound, Vector3f position)
        // positional的设置目前似乎有bug.
        audio.setPositional(false); 
        audio.setRefDistance(10);
        audio.setMaxDistance(100);
        audio.setReverbEnabled(false);
        audio.setTimeOffset(sound.getOffset());
        audio.setVolume(sound.getVolume());
        audio.setLooping(sound.isLoop());
        return audio;
    }
}
