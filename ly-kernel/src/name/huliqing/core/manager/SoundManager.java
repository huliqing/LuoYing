/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.manager;

import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource.Status;
import com.jme3.math.Vector3f;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.data.SoundData;
import name.huliqing.core.enums.Mat;
import name.huliqing.core.mvc.service.ConfigService;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.sound.SoundCollision;
import name.huliqing.core.object.sound.SoundPlayer;

/**
 *
 * @author huliqing
 */
public class SoundManager {
    private final ConfigService configService = Factory.get(ConfigService.class);
    private static final SoundManager SM = new SoundManager();
    
    // 声效播放
    private final SoundPlayer player = new SoundPlayer();
    
    // 碰撞声效
    private final SoundCollision soundCollision = new SoundCollision();
    
    private SoundManager() {}
    
    public static SoundManager getInstance() {
        return SM;
    }
    
    /**
     * 播放声效，非循环。
     * @param soundId 声效ID
     * @param position 声源位置
     */
    public void playSound(String soundId, Vector3f position) {
        if (!configService.isSoundEnabled()) {
            return;
        }
        SoundData sd = DataFactory.createData(soundId);
        player.playSound(sd, position);
    }
    
    /**
     * 播放声效，非循环
     * @param sound 声效
     * @param position 声源位置
     */
    public void playSound(SoundData sound, Vector3f position) {
        if (!configService.isSoundEnabled()) {
            return;
        }
        player.playSound(sound, position);
    }
    
    /**
     * 尽量少用这个方法，尽量把需要多重播放的声音合成一个。
     * @param soundId
     * @param position 
     */
    public void playSoundInstance(String soundId, Vector3f position) {
        if (!configService.isSoundEnabled()) {
            return;
        }
        SoundData sd = DataFactory.createData(soundId);
        player.playSound(sd, position, true);
    }
    
    /**
     * 这个方法会根据系统的配置来确定是否要播放声音文件或是停止播放。当configService.isSoundEnabled()时会偿试播放
     * 声音文件，当configService.isSoundEnabled()关闭时会偿试停止声音播放。
     * @param audio 
     */
    public void checkToPlayAudio(AudioNode audio) {
        if (audio == null)
            return;
        if (configService.isSoundEnabled()) {
            if (audio.getStatus() == Status.Stopped) {
                audio.play();
            }
        } else {
            if (audio.getStatus() == Status.Playing) {
                audio.stop();
            }
        }
    }
    
    public void stopSound(SoundData sound) {
        player.stopSound(sound);
    }
    
    /**
     * 播放物体碰撞声音。
     * @param obj1
     * @param obj2 
     * @param position 
     */
    public void playCollision(ProtoData obj1, ProtoData obj2, Vector3f position) {
        if (!configService.isSoundEnabled()) {
            return;
        }
        soundCollision.playCollision(obj1, obj2, position);
    }
    
    public void playCollision(Mat mat1, Mat mat2, Vector3f position) {
        if (!configService.isSoundEnabled()) {
            return;
        }
        soundCollision.playCollision(mat1, mat2, position);
    }

    /**
     * 播放获得物品时的声效
     * @param objectId
     * @param position
     */
    public void playGetItemSound(String objectId, Vector3f position) {
        if (!configService.isSoundEnabled()) {
            return;
        }
        if (objectId.equals(IdConstants.ITEM_GOLD)) {
            playSound(IdConstants.SOUND_GET_COIN, position);
        } else {
            playSound(IdConstants.SOUND_GET_ITEM, position);
        }
    }
    
    
}
