/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.sound;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;
import name.huliqing.core.LY;
import name.huliqing.core.data.SoundData;
import name.huliqing.core.xml.DataProcessor;

/**
 * @author huliqing
 * @param <T>
 */
public class Sound<T extends SoundData> extends Node implements DataProcessor<T> {

    protected T data;
    
    private AudioNode audio;
    
    @Override
    public void setData(T data) {
        this.data = data;
        this.audio = loadAudio();
    }

    @Override
    public T getData() {
        return data;
    }
    
    /**
     * 播放声音文件
     */
    public void play() {
        if (data.isInstance()) {
            audio.playInstance();
        } else {
            audio.play();
        }
    }
    
    public void pause() {
        audio.pause();
    }
    
    /**
     * 停止声音
     */
    public void stop() {
        audio.stop();
    }
    
    public boolean isLoop() {
        return audio.isLooping();
    }
    
    /**
     * 设置声音循环
     * @param loop
     */
    public void setLoop(boolean loop) {
        audio.setLooping(loop);
    }
    
    public AudioNode getAudio() {
        return audio;
    }
    
    private AudioNode loadAudio() {
        AudioNode an = new AudioNode(LY.getAssetManager(), data.getSoundFile(), AudioData.DataType.Buffer);
        an.setVolume(data.getVolume());
        an.setTimeOffset(data.getTimeOffset());
        an.setLooping(data.isLooping());
        an.setDirection(data.getDirection());
        an.setDirectional(data.isDirectional());
        an.setInnerAngle(data.getInnerAngle());
        an.setMaxDistance(data.getMaxDistance());
        an.setOuterAngle(data.getOuterAngle());
        an.setPitch(data.getPitch());
        
        // 容易java.lang.IllegalStateException: Only mono audio is supported for positional audio nodes
        an.setPositional(data.isPositional());
        
        an.setRefDistance(data.getRefDistance());
        an.setReverbEnabled(data.isReverbEnabled());
        an.setVelocity(data.getVelocity());
        an.setVelocityFromTranslation(data.isVelocityFromTranslation());
        
//        audio.setDryFilter(); // 使用反射，由class创建dryFilter，暂不支持。
//        audio.setReverbFilter();
        return an;
    }
}
