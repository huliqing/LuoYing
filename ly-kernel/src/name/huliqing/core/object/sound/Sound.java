/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.sound;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import name.huliqing.core.LY;
import name.huliqing.core.data.SoundData;
import name.huliqing.core.xml.DataProcessor;

/**
 * @author huliqing
 * @param <T>
 */
public class Sound<T extends SoundData> implements DataProcessor<T> {

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
    
    /**
     * 判断声音是否为循环的
     * @return 
     */
    public boolean isLoop() {
        return data.isLoop();
    }
    
    /**
     * 设置声音位置
     * @param position 
     */
    public void setPosition(Vector3f position) {
        audio.setLocalTranslation(position);
    }
    
    private AudioNode loadAudio() {
        AudioNode an = new AudioNode(LY.getAssetManager(), data.getSoundFile(), AudioData.DataType.Buffer);
        an.setVolume(data.getVolume());
        an.setTimeOffset(data.getOffset());
        an.setLooping(data.isLoop());
        
        an.setDirection(data.getAsVector3f("direction", an.getDirection()));
        an.setDirectional(data.getAsBoolean("directional", an.isDirectional()));
//        audio.setDryFilter(); // 使用反射，由class创建dryFilter，暂不支持。
        an.setInnerAngle(data.getAsFloat("innerAngle", an.getInnerAngle()));
        an.setMaxDistance(data.getAsFloat("maxDistance", an.getMaxDistance()));
        an.setOuterAngle(data.getAsFloat("outerAngle", an.getOuterAngle()));
        an.setPitch(data.getAsFloat("pitch", an.getPitch()));
        an.setPositional(data.getAsBoolean("positional", false)); // 容易java.lang.IllegalStateException: Only mono audio is supported for positional audio nodes
        an.setRefDistance(data.getAsFloat("refDistance", an.getRefDistance()));
        an.setReverbEnabled(data.getAsBoolean("reverbEnabled", an.isReverbEnabled()));
//        audio.setReverbFilter();
        an.setVelocity(data.getAsVector3f("velocity", an.getVelocity()));
        an.setVelocityFromTranslation(data.getAsBoolean("velocityFromTranslation", an.isVelocityFromTranslation()));
        return an;
    }
}
