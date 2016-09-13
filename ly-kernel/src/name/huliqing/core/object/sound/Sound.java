/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.sound;

import com.jme3.audio.AudioContext;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;
import name.huliqing.core.LY;
import name.huliqing.core.data.SoundData;
import name.huliqing.core.xml.DataProcessor;

/**
 * 声效接口
 * @author huliqing
 * @param <T>
 */
public class Sound<T extends SoundData> extends Node implements DataProcessor<T> {

    protected T data;
    
    private AudioNode audio;
    private boolean initialized;
    
    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public T getData() {
        return data;
    }
    
    /**
     * 初始化声音
     */
    public void initialize() {
        if (audio == null) {
            audio = loadAudio();
            attachChild(audio);
        }
        initialized = true;
    }
    
    /**
     * 判断音效是否已经初始化
     * @return 
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 清理并释放音效资源
     */
    public void cleanup() {
        if (audio != null) {
            audio.stop();
            audio = null;
        }
        initialized = false;
    }
    
    /**
     * 播放声音文件
     */
    public void play() {
        // 声音文件在播放的时候需要在渲染线程,不然会报错。
        //    Caused by: java.lang.IllegalStateException: No audio renderer available, make sure call is being performed on render thread.
        //	at com.jme3.audio.AudioNode.getRenderer(AudioNode.java:207)
        //	at com.jme3.audio.AudioNode.play(AudioNode.java:218)
        //	at name.huliqing.core.object.sound.Sound.play(Sound.java:43)
        if (AudioContext.getAudioRenderer() == null) {
            return;
        }
        
        if (data.isInstance()) {
            audio.playInstance();
        } else {
            audio.play();
        }
    }
    
    public void pause() {
        if (AudioContext.getAudioRenderer() == null) {
            return;
        }
        audio.pause();
    }
    
    /**
     * 停止声音
     */
    public void stop() {
        if (AudioContext.getAudioRenderer() == null) {
            return;
        }
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
    
    public void setVolume(float volume) {
        audio.setVolume(volume);
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
        
//        audio.setDryFilter(); // 暂不支持。
//        audio.setReverbFilter();
        return an;
    }
}
